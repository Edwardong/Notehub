import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("serial")
@WebServlet("/Request")
public class FirstServlet extends HttpServlet {

    //Firebase variables
    DatabaseReference mDatabase;
    DatabaseReference mUserReference;
    DatabaseReference mUserReference2;
    CountDownLatch latch;

    //state of servlet, used to determine when to fetch data back from firebase
    private State state;
    boolean userArraygetted = false;

    //Time tasker used to record time
    TimeTask task;

    //variables used for servlet

    //follower numbers
    int followerNum = 0;
    int followingNum = 0;

    //set ArrayList by tag
    ArrayList<String> MathArray = new ArrayList<>();
    ArrayList<String> CSArray = new ArrayList<>();
    ArrayList<String> ArtArray = new ArrayList<>();
    ArrayList<String> LitArray = new ArrayList<>();
    ArrayList<String> BusArray = new ArrayList<>();
    ArrayList<String> StatArray = new ArrayList<>();
    ArrayList<String> HistoryArray = new ArrayList<>();
    ArrayList<String> PhysicsArray = new ArrayList<>();
    ArrayList<String> ChemArray = new ArrayList<>();

    //Design an Arraylist to convert the Month from Int to String
    Map<Integer, String> monthConvert = new HashMap<>();
    Map<String, String> descriptionMap = new HashMap<>();
    Map<String, String> tagMap = new HashMap<>();
    Map<String, String> monthMap = new HashMap<>();
    Map<String, String> yearMap = new HashMap<>();
    Map<String,String> urlMap = new HashMap<>();
    Map<String,String> dayMap = new HashMap<>();

    //pdf names
    ArrayList<String> nameArray = new ArrayList<>();

    //user names
    ArrayList<String> userArray = new ArrayList<>();
    Map<String, PDF> pdfs;
    Map<String,String> names;

    public FirstServlet() {
        super();

        //set up the firebase
        String path = FirstServlet.class.getClassLoader().getResource("ServiceAccount.json").getPath();
        FileInputStream serviceAccount =
                null;
        try {
            serviceAccount = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FirebaseOptions options = null;
        try {
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://notehub-cs48.firebaseio.com")
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FirebaseApp.initializeApp(options);

        //set up the monthConvert map
        monthConvert.put(1, "Jan");
        monthConvert.put(2, "Feb");
        monthConvert.put(3, "Mar");
        monthConvert.put(4, "Apr");
        monthConvert.put(5, "May");
        monthConvert.put(6, "Jun");
        monthConvert.put(7, "Jul");
        monthConvert.put(8, "Aug");
        monthConvert.put(9, "Sep");
        monthConvert.put(10, "Oct");
        monthConvert.put(11, "Nov");
        monthConvert.put(12, "Dec");

        //set up the state
        state = new State();

        //set up time task
        task = new TimeTask(this);
        Timer timer = new Timer();
        timer.schedule(task,30000,30000);
    }

    //State design pattern
    public void request(){
        this.state.handle(this);
    }

    //Since we only need to change from one state to another,
    //but do not need to change it back, so the changeState method have never been used
    // I just put it here to make the structure of State design pattern more clear
    public void changeState(State state){
        this.state = state;
    }

    //doGet function, since every servlet could only have one doGet function, so it would be pretty long
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        //set the format
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        //Check the request passed in
        if(userArraygetted == false){
            HttpSession session = request.getSession();
            CountDownLatch latch1 = new CountDownLatch(1);
            mUserReference = FirebaseDatabase.getInstance().getReference("username");
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    names = new HashMap<String, String>((Map<String,String>) dataSnapshot.getValue());
                    session.setAttribute("names",names);
                    latch1.countDown();
                    // ...
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    latch1.countDown();
                    // ...
                }
            };
            mUserReference.addListenerForSingleValueEvent(postListener);
            try {
                latch1.await(120, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            names = (Map<String, String>) session.getAttribute("names");
            for (Map.Entry<String,String> entry: names.entrySet()){
                userArray.add(entry.getKey());
            }
            userArraygetted = true;
        }

        // code for the search
        if (request.getParameter("keyword") != null) {
            String keyword = request.getParameter("keyword");
            List<String> listData = getData(keyword);
            response.getWriter().write(JSONArray.fromObject(listData).toString());
        }

        //code for login
        if (request.getParameter("useremail") != null) {
            //renew lists and maps
            MathArray = new ArrayList<>();
            CSArray = new ArrayList<>();
            ArtArray = new ArrayList<>();
            LitArray = new ArrayList<>();
            BusArray = new ArrayList<>();
            StatArray = new ArrayList<>();
            HistoryArray = new ArrayList<>();
            PhysicsArray = new ArrayList<>();
            ChemArray = new ArrayList<>();
            descriptionMap = new HashMap<>();
            tagMap = new HashMap<>();
            monthMap = new HashMap<>();
            yearMap = new HashMap<>();
            urlMap = new HashMap<>();
            dayMap = new HashMap<>();
            nameArray = new ArrayList<>();

            //latch the method to wait for the Firebase
            CountDownLatch latch2 = new CountDownLatch(1);
            String url = request.getParameter("useremail");
            String image = MD5Util.getImgURL(url);
            int index = url.indexOf('@');
            String username = url.substring(0, index);
            if(!userArray.contains(username)){
                response.sendRedirect("index.jsp");
                return;
            }
            HttpSession session = request.getSession();

            //firebase
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("username/" + username);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final String uid = dataSnapshot.getValue(String.class);

                    DatabaseReference ref1 = database.getReference("users/" + uid);
                    ValueEventListener postListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            pdfs = user.pdfs;
                            session.setAttribute("pdfs",pdfs);
                            session.setAttribute("followerNum", user.followers.size());
                            session.setAttribute("followingNum", user.following.size());
                            session.setAttribute("username", user.username);
                            latch2.countDown();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Getting Post failed, log a message
                            // ...
                            latch2.countDown();
                        }
                    };
                    ref1.addListenerForSingleValueEvent(postListener);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    // ...
                    latch2.countDown();
                }
            });
            try {
                latch2.await(120, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pdfs = (Map<String, PDF>) session.getAttribute("pdfs");
            for (Map.Entry<String, PDF> entry : pdfs.entrySet()) {
                PDF pdf = (PDF) entry.getValue();
                descriptionMap.put(entry.getKey(),pdf.description);
                yearMap.put(entry.getKey(), String.valueOf(pdf.year));
                monthMap.put(entry.getKey(),monthConvert.get(pdf.month));
                tagMap.put(entry.getKey(),pdf.tag);
                dayMap.put(entry.getKey(), String.valueOf(pdf.day));
                urlMap.put(entry.getKey(),pdf.url);
                nameArray.add(entry.getKey());
            }
            for (Map.Entry<String, String> entry : tagMap.entrySet()) {
                String temp = (String) entry.getValue();
                String tempkey = (String) entry.getKey();
                if (temp.equals("Mathematics"))
                    MathArray.add(tempkey);
                if (temp.equals("Computer Science"))
                    CSArray.add(tempkey);
                if (temp.equals("Art & Music"))
                    ArtArray.add(tempkey);
                if (temp.equals("Statistical Science"))
                    StatArray.add(tempkey);
                if (temp.equals("World History"))
                    HistoryArray.add(tempkey);
                if (temp.equals("Physics"))
                    PhysicsArray.add(tempkey);
                if (temp.equals("Chemistry"))
                    ChemArray.add(tempkey);
                if (temp.equals("Literature"))
                    LitArray.add(tempkey);
                if (temp.equals("Business"))
                    BusArray.add(tempkey);
            }
            //Set by session
            session.setAttribute("shared",nameArray.size());
            session.setAttribute("descriptionMap",descriptionMap);
            session.setAttribute("yearMap",yearMap);
            session.setAttribute("monthMap",monthMap);
            session.setAttribute("tagMap",tagMap);
            session.setAttribute("dayMap",dayMap);
            session.setAttribute("urlMap",urlMap);
            //tag array
            session.setAttribute("nameArray", nameArray);
            session.setAttribute("MathArray", MathArray);
            session.setAttribute("CSArray", CSArray);
            session.setAttribute("ArtArray", ArtArray);
            session.setAttribute("LitArray", LitArray);
            session.setAttribute("BusArray", BusArray);
            session.setAttribute("StatArray", StatArray);
            session.setAttribute("HistoryArray", HistoryArray);
            session.setAttribute("PhysicsArray", PhysicsArray);
            session.setAttribute("ChemArray", ChemArray);
            session.setAttribute("image", image);
            session.setAttribute("email", url);
            response.sendRedirect("userProfile.jsp");
        }
        //code for search jump
        if (request.getParameter("SearchedUsername") != null) {
            //renew lists and maps
            MathArray = new ArrayList<>();
            CSArray = new ArrayList<>();
            ArtArray = new ArrayList<>();
            LitArray = new ArrayList<>();
            BusArray = new ArrayList<>();
            StatArray = new ArrayList<>();
            HistoryArray = new ArrayList<>();
            PhysicsArray = new ArrayList<>();
            ChemArray = new ArrayList<>();
            descriptionMap = new HashMap<>();
            tagMap = new HashMap<>();
            monthMap = new HashMap<>();
            yearMap = new HashMap<>();
            urlMap = new HashMap<>();
            dayMap = new HashMap<>();
            nameArray = new ArrayList<>();
            String url = request.getParameter("SearchedUsername");
            //latch the method to wait for the Firebase
            CountDownLatch latch3 = new CountDownLatch(1);
            String image = MD5Util.getImgURL(url);
            String username = url;
            if(!userArray.contains(username)){
                response.sendRedirect("SearchedProfile.jsp");
                return;
            }

            //Get session
            HttpSession session = request.getSession();

            //firebase
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("username/" + username);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final String uid = dataSnapshot.getValue(String.class);

                    DatabaseReference ref1 = database.getReference("users/" + uid);
                    ValueEventListener postListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            pdfs = user.pdfs;
                            session.setAttribute("pdfs",pdfs);
                            session.setAttribute("followerNum", user.followers.size());
                            session.setAttribute("followingNum", user.following.size());
                            session.setAttribute("username", user.username);
                            latch3.countDown();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Getting Post failed, log a message
                            // ...
                            latch3.countDown();
                        }
                    };
                    ref1.addListenerForSingleValueEvent(postListener);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    // ...
                    latch3.countDown();
                }
            });
            try {
                latch3.await(120, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pdfs = (Map<String, PDF>) session.getAttribute("pdfs");
            for (Map.Entry<String, PDF> entry : pdfs.entrySet()) {
                PDF pdf = (PDF) entry.getValue();
                descriptionMap.put(entry.getKey(),pdf.description);
                yearMap.put(entry.getKey(), String.valueOf(pdf.year));
                monthMap.put(entry.getKey(),monthConvert.get(pdf.month));
                tagMap.put(entry.getKey(),pdf.tag);
                urlMap.put(entry.getKey(),pdf.url);
                dayMap.put(entry.getKey(), String.valueOf(pdf.day));
                nameArray.add(entry.getKey());
            }
            for (Map.Entry<String, String> entry : tagMap.entrySet()) {
                String temp = (String) entry.getValue();
                String tempkey = (String) entry.getKey();
                if (temp.equals("Mathematics"))
                    MathArray.add(tempkey);
                if (temp.equals("Computer Science"))
                    CSArray.add(tempkey);
                if (temp.equals("Art & Music"))
                    ArtArray.add(tempkey);
                if (temp.equals("Statistical Science"))
                    StatArray.add(tempkey);
                if (temp.equals("World History"))
                    HistoryArray.add(tempkey);
                if (temp.equals("Physics"))
                    PhysicsArray.add(tempkey);
                if (temp.equals("Chemistry"))
                    ChemArray.add(tempkey);
                if (temp.equals("Literature"))
                    LitArray.add(tempkey);
                if (temp.equals("Business"))
                    BusArray.add(tempkey);
            }

            //Send by session
            session.setAttribute("shared",nameArray.size());
            session.setAttribute("descriptionMap",descriptionMap);
            session.setAttribute("yearMap",yearMap);
            session.setAttribute("monthMap",monthMap);
            session.setAttribute("tagMap",tagMap);
            session.setAttribute("urlMap",urlMap);
            session.setAttribute("dayMap",dayMap);

            //tag arrays
            session.setAttribute("nameArray", nameArray);
            session.setAttribute("MathArray", MathArray);
            session.setAttribute("CSArray", CSArray);
            session.setAttribute("ArtArray", ArtArray);
            session.setAttribute("LitArray", LitArray);
            session.setAttribute("BusArray", BusArray);
            session.setAttribute("StatArray", StatArray);
            session.setAttribute("HistoryArray", HistoryArray);
            session.setAttribute("PhysicsArray", PhysicsArray);
            session.setAttribute("ChemArray", ChemArray);
            session.setAttribute("image", image);
            session.setAttribute("SearchedUsername", url);
            response.sendRedirect("SearchedProfile.jsp");
        }
    }

    //Function to make the servlet ready to fetch data back fron firebase
    public void OnFetching(){
        this.userArraygetted = false;
    }

    //Function to generate the list of words to search
    public List<String> getData(String keyword) {
        List<String> list = new ArrayList<>();
        for (String data : this.userArray) {
            if (data.contains(keyword)) {
                if(!list.contains(data))
                    list.add(data);
            }
        }
        return list;
    }

    //Function to set a time cycle to the Servlet to fetch data from firebase

}





//Test code, we will add these code in in the future
/*
Map<String, Object> pdfs =user.pdfs;
                            Map<String,String> descriptionMap = new HashMap<>();
                            Map<String,String> yearMap = new HashMap<>();
                            Map<String,String> monthMap = new HashMap<>();
                            Map<String,String> tagMap = new HashMap<>();
                            ArrayList<String> nameArray = new ArrayList<>();

                            //Design an Arraylist to convert the Month from Int to String
                            Map<Integer,String> monthConvert  = new HashMap<>();
                            monthConvert.put(1,"Jan");
                            monthConvert.put(2,"Feb");
                            monthConvert.put(3,"Mar");
                            monthConvert.put(4,"Apr");
                            monthConvert.put(5,"May");
                            monthConvert.put(6,"Jun");
                            monthConvert.put(7,"Jul");
                            monthConvert.put(8,"Aug");
                            monthConvert.put(9,"Sep");
                            monthConvert.put(10,"Oct");
                            monthConvert.put(11,"Nov");
                            monthConvert.put(12,"Dec");

                            //set ArrayList by tag
                            ArrayList<String> MathArray = new ArrayList<>();
                            ArrayList<String> CSArray = new ArrayList<>();
                            ArrayList<String> ArtArray = new ArrayList<>();
                            ArrayList<String> LitArray = new ArrayList<>();
                            ArrayList<String> BusArray = new ArrayList<>();
                            ArrayList<String> StatArray = new ArrayList<>();
                            ArrayList<String> HistoryArray = new ArrayList<>();
                            ArrayList<String> PhysicsArray = new ArrayList<>();
                            ArrayList<String> ChemArray = new ArrayList<>();

                            for (Map.Entry<String, String> entry : tagMap.entrySet()){
                                if(entry.getValue() == "Mathematics")
                                    MathArray.add(entry.getKey());
                                if(entry.getValue() == "Computer Science")
                                    CSArray.add(entry.getKey());
                                if(entry.getValue() == "Art & Music")
                                    ArtArray.add(entry.getKey());
                                if(entry.getValue() == "Statistical Science")
                                    StatArray.add(entry.getKey());
                                if(entry.getValue() == "World History")
                                    HistoryArray.add(entry.getKey());
                                if(entry.getValue() == "Physics")
                                    PhysicsArray.add(entry.getKey());
                                if(entry.getValue() == "Chemistry")
                                    ChemArray.add(entry.getKey());
                                if(entry.getValue() == "Literature")
                                    LitArray.add(entry.getKey());
                                if(entry.getValue() == "Business")
                                    BusArray.add(entry.getKey());
                            }

                            for(int i = 0; i < 7; i++){
                                MathArray.add("");
                                CSArray.add("");
                                ArtArray.add("");
                                StatArray.add("");
                                HistoryArray.add("");
                                PhysicsArray.add("");
                                ChemArray.add("");
                                LitArray.add("");
                                BusArray.add("");
                            }

                            //
                            for (Map.Entry<String, Object> entry : pdfs.entrySet()){
                                PDF pdf = (PDF) entry.getValue();
                                descriptionMap.put(entry.getKey(),pdf.description);
                                yearMap.put(entry.getKey(), String.valueOf(pdf.year));
                                monthMap.put(entry.getKey(),monthConvert.get(pdf.month));
                                tagMap.put(entry.getKey(),pdf.tag);
                                nameArray.add(entry.getKey());
                            }
                            descriptionMap.put("","");
                            yearMap.put("","");
                            monthMap.put("","");
                            tagMap.put("","");


                            session.setAttribute("followerNum",user.followers.size());
                            session.setAttribute("followingNum",user.following.size());
                            session.setAttribute("username",username);
                            //Set by session
                            session.setAttribute("descriptionMap",descriptionMap);
                            session.setAttribute("yearMap",yearMap);
                            session.setAttribute("monthMap",monthMap);
                            session.setAttribute("tagMap",tagMap);
                            //tag array
                            session.setAttribute("nameArray",nameArray);
                            session.setAttribute("MathArray",MathArray);
                            session.setAttribute("CSArray",CSArray);
                            session.setAttribute("ArtArray",ArtArray);
                            session.setAttribute("LitArray",LitArray);
                            session.setAttribute("BusArray",BusArray);
                            session.setAttribute("StatArray",StatArray);
                            session.setAttribute("HistoryArray",HistoryArray);
                            session.setAttribute("PhysicsArray",PhysicsArray);
                            session.setAttribute("ChemArray",ChemArray);

 */

/*

//1
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
@Override
public void onDataChange(DataSnapshot dataSnapshot) {
final String uid = dataSnapshot.getValue(String.class);

        DatabaseReference ref1 = database.getReference("users/"+uid);
        ValueEventListener postListener = new ValueEventListener() {
@Override
public void onDataChange(DataSnapshot dataSnapshot) {
        // Get Post object and use the values to update the UI
        User user = dataSnapshot.getValue(User.class);
        Map<String,String> descriptionMap = new HashMap<>();

        ArrayList<String> nameArray = new ArrayList<>();
        Map<String, Object> pdfs =user.pdfs;


        PDF pdf;
        String yearString;
        for (Map.Entry<String, Object> entry : pdfs.entrySet()){
        pdf = (PDF) entry.getValue();
        descriptionMap.put(entry.getKey(),pdf.description);
        }
        descriptionMap.put("","");


        //



        session.setAttribute("followerNum",user.followers.size());
        session.setAttribute("followingNum",user.following.size());
        session.setAttribute("username",user.username);
        //Set by session
        session.setAttribute("descriptionMap",descriptionMap);
        latch.countDown();
        }

@Override
public void onCancelled(DatabaseError databaseError) {
        // Getting Post failed, log a message
        // ...
        latch.countDown();
        }
        };
        ref1.addListenerForSingleValueEvent(postListener);
        }

@Override
public void onCancelled(DatabaseError databaseError) {
        // Getting Post failed, log a message
        // ...
        latch.countDown();
        }
        });

        //2
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
@Override
public void onDataChange(DataSnapshot dataSnapshot) {
final String uid = dataSnapshot.getValue(String.class);

        DatabaseReference ref1 = database.getReference("users/"+uid);
        ValueEventListener postListener = new ValueEventListener() {
@Override
public void onDataChange(DataSnapshot dataSnapshot) {

        User user = dataSnapshot.getValue(User.class);
        //set ArrayList by tag
        ArrayList<String> MathArray = new ArrayList<>();
        ArrayList<String> CSArray = new ArrayList<>();
        ArrayList<String> ArtArray = new ArrayList<>();
        ArrayList<String> LitArray = new ArrayList<>();
        ArrayList<String> BusArray = new ArrayList<>();
        ArrayList<String> StatArray = new ArrayList<>();
        ArrayList<String> HistoryArray = new ArrayList<>();
        ArrayList<String> PhysicsArray = new ArrayList<>();
        ArrayList<String> ChemArray = new ArrayList<>();
        Map<String, Object> pdfs =user.pdfs;
        Map<String,String> tagMap = new HashMap<>();



        PDF pdf;
        for (Map.Entry<String, Object> entry : pdfs.entrySet()){
        pdf = (PDF)entry.getValue();
        tagMap.put(entry.getKey(),pdf.tag);
        }
        tagMap.put("","");


        for (Map.Entry<String, String> entry : tagMap.entrySet()){
        if(entry.getValue() == "Mathematics")
        MathArray.add(entry.getKey());
        if(entry.getValue() == "Computer Science")
        CSArray.add(entry.getKey());
        if(entry.getValue() == "Art & Music")
        ArtArray.add(entry.getKey());
        if(entry.getValue() == "Statistical Science")
        StatArray.add(entry.getKey());
        if(entry.getValue() == "World History")
        HistoryArray.add(entry.getKey());
        if(entry.getValue() == "Physics")
        PhysicsArray.add(entry.getKey());
        if(entry.getValue() == "Chemistry")
        ChemArray.add(entry.getKey());
        if(entry.getValue() == "Literature")
        LitArray.add(entry.getKey());
        if(entry.getValue() == "Business")
        BusArray.add(entry.getKey());
        }

        for(int i = 0; i < 7; i++){
        MathArray.add("");
        CSArray.add("");
        ArtArray.add("");
        StatArray.add("");
        HistoryArray.add("");
        PhysicsArray.add("");
        ChemArray.add("");
        LitArray.add("");
        BusArray.add("");
        }

        session.setAttribute("MathArray",MathArray);
        session.setAttribute("CSArray",CSArray);
        session.setAttribute("ArtArray",ArtArray);
        session.setAttribute("LitArray",LitArray);
        session.setAttribute("BusArray",BusArray);
        session.setAttribute("StatArray",StatArray);
        session.setAttribute("HistoryArray",HistoryArray);
        session.setAttribute("PhysicsArray",PhysicsArray);
        session.setAttribute("ChemArray",ChemArray);
        session.setAttribute("tagMap",tagMap);
        latch.countDown();
        }

@Override
public void onCancelled(DatabaseError databaseError) {
        // Getting Post failed, log a message
        // ...
        latch.countDown();
        }
        };
        ref1.addListenerForSingleValueEvent(postListener);
        }

@Override
public void onCancelled(DatabaseError databaseError) {
        // Getting Post failed, log a message
        // ...
        latch.countDown();
        }
        });


        // 3
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
@Override
public void onDataChange(DataSnapshot dataSnapshot) {
final String uid = dataSnapshot.getValue(String.class);

        Map<String,String> yearMap = new HashMap<>();
        DatabaseReference ref1 = database.getReference("users/"+uid);
        ValueEventListener postListener = new ValueEventListener() {
@Override
public void onDataChange(DataSnapshot dataSnapshot) {

        User user = dataSnapshot.getValue(User.class);



        Map<String,String> tagMap = new HashMap<>();
        Map<String, Object> pdfs =user.pdfs;




        String yearString;
        PDF pdf;
        for (Map.Entry<String, Object> entry : pdfs.entrySet()){
        pdf = (PDF) entry.getValue();
        yearString = String.valueOf(pdf.year);
        yearMap.put(entry.getKey(), yearString);
        }
        yearMap.put("","");



        session.setAttribute("yearMap",yearMap);
        latch.countDown();
        }

@Override
public void onCancelled(DatabaseError databaseError) {
        // Getting Post failed, log a message
        // ...
        latch.countDown();
        }
        };
        ref1.addListenerForSingleValueEvent(postListener);
        }

@Override
public void onCancelled(DatabaseError databaseError) {
        // Getting Post failed, log a message
        // ...
        latch.countDown();
        }
        });


        //4
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
@Override
public void onDataChange(DataSnapshot dataSnapshot) {
final String uid = dataSnapshot.getValue(String.class);

        DatabaseReference ref1 = database.getReference("users/"+uid);
        ValueEventListener postListener = new ValueEventListener() {
@Override
public void onDataChange(DataSnapshot dataSnapshot) {
        User user = dataSnapshot.getValue(User.class);

        String yearString;
        Map<String, Object> pdfs =user.pdfs;
        Map<String,String> monthMap = new HashMap<>();
        ArrayList<String> nameArray = new ArrayList<>();


        PDF pdf;
        for (Map.Entry<String, Object> entry : pdfs.entrySet()){
        pdf = (PDF) entry.getValue();
        monthMap.put(entry.getKey(),monthConvert.get(pdf.month));
        nameArray.add(entry.getKey());
        }
        monthMap.put("","");



        session.setAttribute("monthMap",monthMap);
        session.setAttribute("nameArray",nameArray);

        latch.countDown();
        }

@Override
public void onCancelled(DatabaseError databaseError) {
        // Getting Post failed, log a message
        // ...
        latch.countDown();
        }
        };
        ref1.addListenerForSingleValueEvent(postListener);
        }

@Override
public void onCancelled(DatabaseError databaseError) {
        // Getting Post failed, log a message
        // ...
        latch.countDown();
        }
        });
        */

