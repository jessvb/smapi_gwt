package com.appinventor.testsmapijava.client;

public class AmazonSdk {
  /*
   * Amazon access token is for future calls to the Login with Amazon SDK
   * with the same user that logged in. 
   */
  private String accessToken;
 
  /*
   * Constructor calls JSNI method that initializes the Amazon SDK in 
   * javascript.
   */ 
  public AmazonSdk(String clientID) {
      initializeAmazonSdk(clientID);
  }

  /*
   * Initializes the Amazon SDK javascript library.
   */
  private final native void initializeAmazonSdk(String clientID) /*-{ 
    // TODO: for some reason, I can't access $wnd.amazon if I only put the following code here :( I have to actually put this code in the HTML DOM for it to work... --> Figure out how to only have this code in this file (not nec. in the HTML) 
    $wnd.onAmazonLoginReady = function() {
      $wnd.amazon.Login.setClientId(clientID);
    };
    (function(d) {
      var a = d.createElement('script'); 
      a.type = 'text/javascript';
      a.async = true;
      a.id = 'amazon-login-sdk';
      a.src = 'https://assets.loginwithamazon.com/sdk/na/login1.js';
      //d.getElementById('amazon-root').appendChild(a);
      $doc.getElementById('amazon-root').appendChild(a);
    })(document);
  }-*/;
 
  /*
   * Calls for a pop-up that allows Amazon users to log in.
   */
  public final native void loginAmazon() /*-{ 
	    options = {
                scope: 'profile',
                state: '~jessicav/test_smapi/index.html' // TODO
            };
            $wnd.amazon.Login.authorize(options, function (response) {
                if (response.error) {
                    alert('oauth error ' + response.error);
                    return;
                } else {
                  // TODO add callback here with response.access_token
		}
	    });
	    }-*/;
  /*
   * Logs the user out of their Amazon account on this website.
   */
  public final native void logoutAmazon() /*-{
    $wnd.amazon.Login.logout();
				       }-*/;

}
