(function() {

    var config = {
        apiKey: "AIzaSyDMN4L5QAuJI6pA1e65TCEvid4iprR0KaM",
        authDomain: "notehub-cs48.firebaseapp.com",
        databaseURL: "https://notehub-cs48.firebaseio.com",
        //projectId: "notehub-cs48",
        storageBucket: "notehub-cs48.appspot.com",
        //messagingSenderId: "1094694673414"
    };
    firebase.initializeApp(config);
    //firebase.auth().signOut();
    const txtEmail = document.getElementById('txtEmail');
    const txtPassword = document.getElementById('txtPassword');
    const btnLogin = document.getElementById('btnLogin');
    //const btnSignUp = document.getElementById('btnSignUp');
    //const btnLogout = document.getElementById('btnLogout');

    // login event
    btnLogin.addEventListener('click', e=>{

        const email = txtEmail.value;
        const pass = txtPassword.value;
        const auth = firebase.auth();

        // Sign in
        const promise = auth.signInWithEmailAndPassword(email, pass);
        promise.catch(e => console.log(e.message));
        promise.catch(e => alert(e.message));
    });

    /*
      btnSignUp.addEventListener('click', e=>{
        const email = txtEmail.value;
        const pass = txtPassword.value;
        const auth = firebase.auth();
        // Sign in
        const promise = auth.createUserWithEmailAndPassword(email, pass);
        promise.catch(e => console.log(e.message));
      });
    */

/*
    btnLogout.addEventListener('click', e =>{
        firebase.auth().signOut();
    });
*/

    firebase.auth().onAuthStateChanged(firebaseUser => {
        if(firebaseUser){
            console.log(firebaseUser);
            var email1 = document.getElementById('txtEmail').value;
            //alert(email1);
            window.location.href = "Request?useremail="+email1;
            //window.location.href = "www.google.com";
            //alert('Logged In!!!')
            //btnLogout.classList.remove('hide');
        } else{
            //alert('not logged in')
            console.log('not logged in');
            //btnLogout.classList.add('hide');
        }
    });

}());
