package com.appinventor.testsmapijava.client;

// For requesting a JSON from PHP file:
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.google.gwt.user.client.Window;

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
    // save "this" so that we can call a java fxn on this instance later
    var self = this;
    options = {
        scope: 'profile',
        state: '~jessicav/test_smapi/index.html' // TODO
    };
    $wnd.amazon.Login.authorize(options, function (response) {
        if (response.error) {
            alert('oauth error ' + response.error);
            return;
        } else {
	self.@com.appinventor.testsmapijava.client.AmazonSdk::saveAccessToken(Ljava/lang/String;)(response.access_token);
	}
    });
    }-*/;
  
  /*
   * Stores the access token from Login with Amazon
   */
  private void saveAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  /*
   * Logs the user out of their Amazon account on this website by calling
   * a native JSNI method, and removes the previously-saved access token.
   */
  public void logoutAmazon() {
    this.accessToken = null;
    this.logoutAmazonJsni();
  }

  /*
   * Logs the user out of their Amazon account on this website.
   */
  private final native void logoutAmazonJsni() /*-{
    $wnd.amazon.Login.logout();
				       }-*/;

  public String[] getNameEmailUserid() {
    // From https://developer.amazon.com/docs/login-with-amazon/obtain-customer-profile.html
    // Let a PHP script make a call to the Amazon server and return user info
    
    if (accessToken != null) {
      // TODO: make real url etc.
      String phpUrl = "https://appinventor-alexa.csail.mit.edu/smapi_gwt/gwt-2.8.2/TestSmapiJava/war/amazonUserInfo.php?callback=cb";
     JsonpRequestBuilder builder = new JsonpRequestBuilder(); 
     
     builder.requestObject(phpUrl, new AsyncCallback<AccessTokenInfo>() {
       public void onFailure(Throwable caught) {
         Window.alert("Couldn't retrieve JSON");
       }

       public void onSuccess(AccessTokenInfo data) {
         Window.alert("Access Token: " + data.getAccessToken() +"\nRefresh Token: " + data.getRefreshToken() + "\nToken Type: " + data.getTokenType() + "\nExpire Time: " + data.getExpireTime());
       }
     });
   } else {
     Window.alert("Please login to Amazon. (No access token.)");
   } 
  

    String[] nameEmailUserid = {"TODO name", "TODO email", "TODO user id"};
    return nameEmailUserid;
  }

}
