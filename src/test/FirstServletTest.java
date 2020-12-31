import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;



public class FirstServletTest {
    DatabaseReference mDatabase;
    DatabaseReference mUserReference;
    DatabaseReference mUserReference2;

    public FirstServletTest(){

    }
    public void initialize(){
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
    }

    //Firebase function
    public void add_follower_helper(String uid, String uid2, final String username){
        Map<String, Object> childUpdate = new HashMap<>();
        childUpdate.put("/users/" + uid + "/followers/" + uid2, username);
        mDatabase.updateChildrenAsync(childUpdate);
    }

    public void add_follower(String username1, final String username2){
        mUserReference = FirebaseDatabase.getInstance().getReference("username/" + username1);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                final String uid = dataSnapshot.getValue(String.class);
                mUserReference2 = FirebaseDatabase.getInstance().getReference("username/" + username2);
                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get Post object and use the values to update the UI
                        String uid2 = dataSnapshot.getValue(String.class);
                        add_follower_helper(uid, uid2, username2);
                        //Toast.makeText(MainActivity.this, uid, Toast.LENGTH_SHORT).show();
                        // ...
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        // ...
                    }
                };
                mUserReference2.addListenerForSingleValueEvent(postListener);
                //Toast.makeText(MainActivity.this, uid, Toast.LENGTH_SHORT).show();
                // ...
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // ...
            }
        };
        mUserReference.addListenerForSingleValueEvent(postListener);
    }



    @Test
    public void testFirebaseGetuser1(){
        //firebase
        initialize();
        String username = "Zichen Sun";
        CountDownLatch latch = new CountDownLatch(1);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        TestCase.assertEquals(true,true);
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
                        Map<String, PDF> pdfs = user.pdfs;
                        TestCase.assertEquals(user!=null,true);
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
        try {
            latch.await(120, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFirebaseGetuser2(){
        //firebase
        String username = "Zichen Sun";
        CountDownLatch latch = new CountDownLatch(1);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        TestCase.assertEquals(true,true);
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
                        Map<String, PDF> pdfs = user.pdfs;
                        TestCase.assertEquals(pdfs.size()!=0,true);
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
        try {
            latch.await(120, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFirebaseGetuser3(){
        //firebase
        String username = "Delin Sun";
        CountDownLatch latch = new CountDownLatch(1);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        TestCase.assertEquals(true,true);
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
                        Map<String, PDF> pdfs = user.pdfs;
                        TestCase.assertEquals(user!=null,true);
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
        try {
            latch.await(120, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFirebaseGetuser4(){
        //firebase
        String username = "Delin Sun";
        CountDownLatch latch = new CountDownLatch(1);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        TestCase.assertEquals(true,true);
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
                        Map<String, PDF> pdfs = user.pdfs;
                        TestCase.assertEquals(pdfs.size()!=0,true);
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
        try {
            latch.await(120, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFirebaseGetuser5(){
        //firebase
        String username = "Chandana";
        CountDownLatch latch = new CountDownLatch(1);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        TestCase.assertEquals(true,true);
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
                        Map<String, PDF> pdfs = user.pdfs;
                        TestCase.assertEquals(user!=null,true);
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
        try {
            latch.await(120, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFirebaseGetuser6(){
        //firebase
        String username = "Chandana";
        CountDownLatch latch = new CountDownLatch(1);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        TestCase.assertEquals(true,true);
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
                        Map<String, PDF> pdfs = user.pdfs;
                        TestCase.assertEquals(pdfs.size()!=0,true);
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
        try {
            latch.await(120, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
