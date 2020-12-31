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
    private DatabaseReference mDatabase;
    private DatabaseReference mUserReference;
    private DatabaseReference mUserReference2;
    private CountDownLatch latch;

    //state of servlet, used to determine when to fetch data back from firebase
    private State state;
    private boolean userArraygetted = false;

    //Time tasker used to record time
    private TimeTask task;

    //variables used for servlet

    //follower numbers
    private int followerNum = 0;
    private int followingNum = 0;

    //follower numbers
    private int ufollowerNum = 0;
    private int ufollowingNum = 0;


    //set ArrayList by tag
    private ArrayList<String> MathArray = new ArrayList<>();
    private ArrayList<String> CSArray = new ArrayList<>();
    private ArrayList<String> ArtArray = new ArrayList<>();
    private ArrayList<String> LitArray = new ArrayList<>();
    private ArrayList<String> BusArray = new ArrayList<>();
    private ArrayList<String> StatArray = new ArrayList<>();
    private ArrayList<String> HistoryArray = new ArrayList<>();
    private ArrayList<String> PhysicsArray = new ArrayList<>();
    private ArrayList<String> ChemArray = new ArrayList<>();

    //set ArrayList by tag for user
    private ArrayList<String> uMathArray = new ArrayList<>();
    private ArrayList<String> uCSArray = new ArrayList<>();
    private ArrayList<String> uArtArray = new ArrayList<>();
    private ArrayList<String> uLitArray = new ArrayList<>();
    private ArrayList<String> uBusArray = new ArrayList<>();
    private ArrayList<String> uStatArray = new ArrayList<>();
    private ArrayList<String> uHistoryArray = new ArrayList<>();
    private ArrayList<String> uPhysicsArray = new ArrayList<>();
    private ArrayList<String> uChemArray = new ArrayList<>();

    //Design an Arraylist to convert the Month from Int to String
    private Map<Integer, String> monthConvert = new HashMap<>();
    private Map<String, String> descriptionMap = new HashMap<>();
    private Map<String, String> tagMap = new HashMap<>();
    private Map<String, String> monthMap = new HashMap<>();
    private Map<String, String> yearMap = new HashMap<>();
    private Map<String,String> urlMap = new HashMap<>();
    private Map<String,String> dayMap = new HashMap<>();

    //Design an Arraylist to convert the Month from Int to String
    private Map<String, String> udescriptionMap = new HashMap<>();
    private Map<String, String> utagMap = new HashMap<>();
    private Map<String, String> umonthMap = new HashMap<>();
    private Map<String, String> uyearMap = new HashMap<>();
    private Map<String,String> uurlMap = new HashMap<>();
    private Map<String,String> udayMap = new HashMap<>();

    //Arrays used for follower following
    private ArrayList<String> followerArray = new ArrayList<>();
    private ArrayList<String> followingArray = new ArrayList<>();
    private ArrayList<String> followerUrlArray = new ArrayList<>();
    private ArrayList<String> followingUrlArray = new ArrayList<>();

    //Arrays used for follower following
    private ArrayList<String> ufollowerArray = new ArrayList<>();
    private ArrayList<String> ufollowingArray = new ArrayList<>();
    private ArrayList<String> ufollowerUrlArray = new ArrayList<>();
    private ArrayList<String> ufollowingUrlArray = new ArrayList<>();

    //Arrays for search pdf
    private ArrayList<String> SearchArray = new ArrayList<>();
    private ArrayList<String> SearchDesArray = new ArrayList<>();
    private ArrayList<String> SearchMonthArray = new ArrayList<>();
    private ArrayList<String> SearchYearArray = new ArrayList<>();
    private ArrayList<String> SearchDayArray = new ArrayList<>();
    private ArrayList<String> SearchUrlArray = new ArrayList<>();
    private ArrayList<String> SearchUserArray = new ArrayList<>();

    //pdf names
    private ArrayList<String> nameArray = new ArrayList<>();

    //pdf names for user
    private ArrayList<String> unameArray = new ArrayList<>();

    //user names
    private ArrayList<String> userArray = new ArrayList<>();

    private Map<String, PDF> pdfs;
    private Map<String,String> names;
    ArrayList<Map.Entry<Map,String>> namelists = new ArrayList<Map.Entry<Map, String>>();

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
        if(!userArraygetted){
            HttpSession session = request.getSession();

            //code to get all the usernames
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

            //Code to get the all the pdfs
            CountDownLatch latch11 = new CountDownLatch(1);
            mUserReference = FirebaseDatabase.getInstance().getReference("users");
            postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    Map<String,Object> searchnames = new HashMap<String, Object>((Map<String,Object>) dataSnapshot.getValue());
                    session.setAttribute("searchnames",searchnames);
                    latch11.countDown();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    latch11.countDown();
                }
            };
            mUserReference.addListenerForSingleValueEvent(postListener);
            try {
                latch11.await(120, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Map<String,Map> searchnames = (Map<String,Map>)session.getAttribute("searchnames");
            namelists = new ArrayList<Map.Entry<Map, String>>();

            for (Map.Entry<String, Map> entry: searchnames.entrySet()){
                Map singleUser = (Map) entry.getValue();
                Map<String,Object> test = new HashMap<String, Object>((Map<String,Object>)singleUser.get("pdfs"));
                for (Map.Entry<String,Object> entry1: test.entrySet()){
                    Map singlePdf = (Map) entry1.getValue();
                    namelists.add(new AbstractMap.SimpleEntry<Map, String>(singlePdf,(String) singleUser.get("username")));
                }
            }

            SearchArray = new ArrayList<>();
            SearchUserArray = new ArrayList<>();
            SearchDesArray = new ArrayList<>();
            SearchMonthArray = new ArrayList<>();
            SearchYearArray = new ArrayList<>();
            SearchDayArray = new ArrayList<>();
            SearchUrlArray = new ArrayList<>();

            for (int i=0;i<namelists.size();i++){
                SearchArray.add((String) namelists.get(i).getKey().get("filename"));
                SearchUserArray.add((String) namelists.get(i).getValue());
                SearchDesArray.add((String) namelists.get(i).getKey().get("description"));
                String monthNum = String.valueOf(namelists.get(i).getKey().get("month"));
                SearchMonthArray.add((String) monthConvert.get(Integer.parseInt(monthNum)));
                SearchYearArray.add((String) String.valueOf(namelists.get(i).getKey().get("year")));
                SearchDayArray.add((String) String.valueOf(namelists.get(i).getKey().get("day")));
                SearchUrlArray.add((String) namelists.get(i).getKey().get("url"));
            }
            session.setAttribute("SearchArray",SearchArray);
            session.setAttribute("SearchUserArray",SearchUserArray);
            session.setAttribute("SearchDesArray",SearchDesArray);
            session.setAttribute("SearchMonthArray",SearchMonthArray);
            session.setAttribute("SearchYearArray",SearchYearArray);
            session.setAttribute("SearchDayArray",SearchDayArray);
            session.setAttribute("SearchUrlArray",SearchUrlArray);

            names = (Map<String, String>) session.getAttribute("names");
            //userArray = new ArrayList<String>();
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
            uMathArray = new ArrayList<>();
            uCSArray = new ArrayList<>();
            uArtArray = new ArrayList<>();
            uLitArray = new ArrayList<>();
            uBusArray = new ArrayList<>();
            uStatArray = new ArrayList<>();
            uHistoryArray = new ArrayList<>();
            uPhysicsArray = new ArrayList<>();
            uChemArray = new ArrayList<>();
            udescriptionMap = new HashMap<>();
            utagMap = new HashMap<>();
            umonthMap = new HashMap<>();
            uyearMap = new HashMap<>();
            uurlMap = new HashMap<>();
            udayMap = new HashMap<>();
            unameArray = new ArrayList<>();
            ufollowerArray = new ArrayList<>();
            ufollowingArray = new ArrayList<>();
            ufollowerUrlArray = new ArrayList<>();
            ufollowingUrlArray = new ArrayList<>();

            HttpSession session = request.getSession();

            //latch the method to wait for the Firebase
            CountDownLatch latch2 = new CountDownLatch(1);
            String url = request.getParameter("useremail");
            String image = MD5Util.getImgURL(url);
            String username = "";

            //Get username by email (all variables putting into the session should start by "u")
            CountDownLatch latch22 = new CountDownLatch(1);
            mUserReference = FirebaseDatabase.getInstance().getReference("users");
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    Map<String,Object> searchnames = new HashMap<String, Object>((Map<String,Object>) dataSnapshot.getValue());
                    session.setAttribute("searchnames",searchnames);
                    latch22.countDown();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    latch22.countDown();
                }
            };
            mUserReference.addListenerForSingleValueEvent(postListener);
            try {
                latch22.await(120, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Map<String,Map> searchnames = (Map<String,Map>)session.getAttribute("searchnames");
            namelists = new ArrayList<Map.Entry<Map, String>>();namelists = new ArrayList<Map.Entry<Map, String>>();

            for (Map.Entry<String,Map> entry: searchnames.entrySet()){
                Map singleUser = (Map) entry.getValue();
                String test = ((String)singleUser.get("email"));
                if (test.equals(url)){
                    username = (String)singleUser.get("username");
                }
            }

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
                            session.setAttribute("user",user);
                            session.setAttribute("ufollowerNum", user.followers.size());
                            session.setAttribute("ufollowingNum", user.following.size());
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
            User user = (User) session.getAttribute("user");
            for (Map.Entry<String, PDF> entry : pdfs.entrySet()) {
                PDF pdf = (PDF) entry.getValue();
                udescriptionMap.put(entry.getKey(),pdf.description);
                uyearMap.put(entry.getKey(), String.valueOf(pdf.year));
                umonthMap.put(entry.getKey(),monthConvert.get(pdf.month));
                utagMap.put(entry.getKey(),pdf.tag);
                udayMap.put(entry.getKey(), String.valueOf(pdf.day));
                uurlMap.put(entry.getKey(),pdf.url);
                unameArray.add(entry.getKey());
            }
            for (Map.Entry<String, Object> entry : user.followers.entrySet()) {
                String followerName = String.valueOf(entry.getValue());
                ufollowerArray.add(followerName);
                ufollowerUrlArray.add(MD5Util.getImgURL(followerName));
            }
            for (Map.Entry<String, Object> entry : user.following.entrySet()) {
                String followingName = String.valueOf(entry.getValue());
                ufollowingArray.add(followingName);
                ufollowingUrlArray.add(MD5Util.getImgURL(followingName));
            }
            for (Map.Entry<String, String> entry : utagMap.entrySet()) {
                String temp = (String) entry.getValue();
                String tempkey = (String) entry.getKey();
                if (temp.equals("Mathematics"))
                    uMathArray.add(tempkey);
                if (temp.equals("Computer Science"))
                    uCSArray.add(tempkey);
                if (temp.equals("Art & Music"))
                    uArtArray.add(tempkey);
                if (temp.equals("Statistical Science"))
                    uStatArray.add(tempkey);
                if (temp.equals("World History"))
                    uHistoryArray.add(tempkey);
                if (temp.equals("Physics"))
                    uPhysicsArray.add(tempkey);
                if (temp.equals("Chemistry"))
                    uChemArray.add(tempkey);
                if (temp.equals("Literature"))
                    uLitArray.add(tempkey);
                if (temp.equals("Business"))
                    uBusArray.add(tempkey);
            }
            //Set by session
            session.setAttribute("ushared",unameArray.size());
            session.setAttribute("udescriptionMap",udescriptionMap);
            session.setAttribute("uyearMap",uyearMap);
            session.setAttribute("umonthMap",umonthMap);
            session.setAttribute("utagMap",utagMap);
            session.setAttribute("udayMap",udayMap);
            session.setAttribute("uurlMap",uurlMap);

            //tag array
            session.setAttribute("unameArray", unameArray);
            session.setAttribute("uMathArray", uMathArray);
            session.setAttribute("uCSArray", uCSArray);
            session.setAttribute("uArtArray", uArtArray);
            session.setAttribute("uLitArray", uLitArray);
            session.setAttribute("uBusArray", uBusArray);
            session.setAttribute("uStatArray", uStatArray);
            session.setAttribute("uHistoryArray", uHistoryArray);
            session.setAttribute("uPhysicsArray", uPhysicsArray);
            session.setAttribute("uChemArray", uChemArray);
            session.setAttribute("ufollowerArray",ufollowerArray);
            session.setAttribute("ufollowingArray",ufollowingArray);
            session.setAttribute("ufollowerUrlArray",ufollowerUrlArray);
            session.setAttribute("ufollowingUrlArray",ufollowingArray);
            session.setAttribute("uimage", image);
            session.setAttribute("uemail", url);
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
            followerArray = new ArrayList<>();
            followingArray = new ArrayList<>();
            followerUrlArray = new ArrayList<>();
            followingUrlArray = new ArrayList<>();
            String url = request.getParameter("SearchedUsername");
            //latch the method to wait for the Firebase
            CountDownLatch latch3 = new CountDownLatch(1);
            String username = url;
            /*
            if(!userArray.contains(username)&&(!SearchArray.contains(username))){
                response.sendRedirect("SearchedProfile.jsp");
                return;
            }
            */
            if(!userArray.contains(username)){
                response.sendRedirect("SearchedList.jsp");
                HttpSession session = request.getSession();
                session.setAttribute("Searchedkeyword", username);
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
                            session.setAttribute("user",user);
                            session.setAttribute("followerNum", user.followers.size());
                            session.setAttribute("followingNum", user.following.size());
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
            User user = (User) session.getAttribute("user");
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
            for (Map.Entry<String, Object> entry : user.followers.entrySet()) {
                followerArray.add(String.valueOf(entry.getValue()));
            }
            for (Map.Entry<String, Object> entry : user.following.entrySet()) {
                followingArray.add(String.valueOf(entry.getValue()));
            }
            String email = user.email;
            String image = MD5Util.getImgURL(email);
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
            session.setAttribute("followerArray",followerArray);
            session.setAttribute("followingArray",followingArray);
            session.setAttribute("followerUrlArray",followerUrlArray);
            session.setAttribute("followingUrlArray",followingArray);
            session.setAttribute("email",email);
            session.setAttribute("image", image);
            session.setAttribute("SearchedUsername", url);
            response.sendRedirect("SearchedProfile.jsp");
        }

        //log out
        if(request.getParameter("logout") != null){
            HttpSession session = request.getSession();
            if(session.getAttribute("username") != null) {
                session.removeAttribute("username");
                session.removeAttribute("uemail");
            }
            this.state.handle(this);
            request.removeAttribute("logout");
            response.sendRedirect("index.jsp");
        }

        //follow me
        if(request.getParameter("followme") != null){
            HttpSession session = request.getSession();
            if(session.getAttribute("username") == null) {
                this.state.handle(this);
                request.removeAttribute("followme");
                response.sendRedirect("login.html");
                return;
            }
            int result = add_follower((String)session.getAttribute("username"),(String)session.getAttribute("SearchedUsername"));
            //Update data from firebase
            this.state.handle(this);
            if(request.getParameter("followme")!=null)
                request.removeAttribute("followme");
            response.getWriter().print("<script> alert(\"You successfully followed "+(String)session.getAttribute("SearchedUsername")+"!\"); </script>");
            response.sendRedirect("SearchedProfile.jsp");
        }

        //update
        if(request.getParameter("update") != null){
            this.state.handle(this);
            request.removeAttribute("update");
            response.sendRedirect("index.jsp");
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
        for (String data : this.SearchArray) {
            if (data.contains(keyword)) {
                if(!list.contains(data))
                    list.add(data);
            }
        }
        return list;
    }
    //Function for firebase
    //Firebase function
    public void add_follower_helper(String uid, String uid2, final String username){
        Map<String, Object> childUpdate = new HashMap<>();
        childUpdate.put("/users/" + uid + "/followers/" + uid2, username);
        mDatabase.updateChildrenAsync(childUpdate);
    }

    public int add_follower(String username1, final String username2){
        CountDownLatch latch1 = new CountDownLatch(1);
        mUserReference = FirebaseDatabase.getInstance().getReference("username/" + username1);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                final String uid = dataSnapshot.getValue(String.class);
                CountDownLatch latch2 = new CountDownLatch(1);
                mUserReference2 = FirebaseDatabase.getInstance().getReference("username/" + username2);
                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get Post object and use the values to update the UI
                        String uid2 = dataSnapshot.getValue(String.class);
                        add_follower_helper(uid, uid2, username2);
                        latch2.countDown();
                        //Toast.makeText(MainActivity.this, uid, Toast.LENGTH_SHORT).show();
                        // ...
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        latch2.countDown();
                        // ...
                    }
                };
                mUserReference2.addListenerForSingleValueEvent(postListener);
                try {
                    latch2.await(120, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                latch1.countDown();
                //Toast.makeText(MainActivity.this, uid, Toast.LENGTH_SHORT).show();
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
        return 0;
    }

    //output status of servlet, whether it is ready to fetch data from firebase
    boolean FirebaseisReady(){
        return userArraygetted;
    }
}