  window.fbAsyncInit = function() {
    FB.init({
      appId      : '1888602084785022',
      cookie     : true,
      xfbml      : true,
      version    : 'v2.12'
    });
    FB.getLoginStatus(function(response) {
    statusChangeCallback(response);
	});  
    FB.AppEvents.logPageView();   
      
  };

  (function(d, s, id){
     var js, fjs = d.getElementsByTagName(s)[0];
     if (d.getElementById(id)) {return;}
     js = d.createElement(s); js.id = id;
     js.src = "https://connect.facebook.net/en_US/sdk.js";
     fjs.parentNode.insertBefore(js, fjs);
   }(document, 'script', 'facebook-jssdk'));

   function statusChangeCallback(response){
   	if(response.status === 'connected'){
   		console.log('Logged in and authenticated');
   	}
   	else{
   		console.log('Not authenticated');
   	}
   }

	function checkLoginState() {
		FB.getLoginStatus(function(response) {
			statusChangeCallback(response);
		});
	}

	function logout(){
		FB.logout(function(response){
			
		});
	}

	document.getElementById('loginBtn').addEventListener('click', function() {
    //do the login
    FB.login(function(response) {
        if (response.authResponse) {
            //user just authorized your app
            top.location.href = 'example.com/facebook_connect.php';
        }
    }, {scope: 'email,public_profile', return_scopes: true});
}, false);
