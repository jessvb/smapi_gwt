package com.appinventor.testsmapijava.client;

// For requesting a JSON from PHP file:
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.Stack;
import java.util.EmptyStackException;

import com.google.gwt.user.client.Window;

public class AmazonSdk {
  /*
   * Amazon access token is for future calls to the Login with Amazon SDK
   * with the same user that logged in. 
   */
  private String accessToken;
 
  /*
   * Amazon vendor ID is for Alexa skills requests.
   */
  private String vendorId;
  
  /*
   * Stack of most recently created skill ids.
   */
  private Stack<String> createdSkills = new Stack<String>(); 
  
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
        scope: 'profile alexa::ask:skills:readwrite alexa::ask:models:readwrite alexa::ask:skills:test'
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
    this.vendorId = null;
    this.logoutAmazonJsni();
  }

  /*
   * Logs the user out of their Amazon account on this website.
   */
  private final native void logoutAmazonJsni() /*-{
    $wnd.amazon.Login.logout();
				       }-*/;

  public void getNameEmailUserid() {
    // From https://developer.amazon.com/docs/login-with-amazon/obtain-customer-profile.html
    // Let a PHP script make a call to the Amazon server and return user info
    
    if (accessToken != null) {
      String phpUrl = "https://appinventor-alexa.csail.mit.edu/smapi_gwt/gwt-2.8.2/TestSmapiJava/war/amazonUserInfo.php?callback=cb&accessToken=" + accessToken;
     JsonpRequestBuilder builder = new JsonpRequestBuilder(); 
     
     builder.requestObject(phpUrl, new AsyncCallback<UserInfo>() {
       public void onFailure(Throwable caught) {
         Window.alert("Couldn't retrieve JSON");
       }

       public void onSuccess(UserInfo data) {
        if (data.getError() != null) {
		Window.alert("Error retrieving user info: " + data.getError());
	 } else {
	 	Window.alert("user_id: " + data.getUserId() +"\nEmail: " + data.getEmail() + "\nName: " + data.getName() + "\nPostal Code: " + data.getPostalCode());
	 }
       }
     });
   } else {
     Window.alert("Please login to Amazon. (No access token.)");
   }
  }


  public void getVendorId() {
	  // From https://developer.amazon.com/docs/smapi/account-linking-operations.html
	  // Let a PHP script make a call to the Amazon server and return user info
    
    if (accessToken != null) {
	    String phpUrl = "https://appinventor-alexa.csail.mit.edu/smapi_gwt/gwt-2.8.2/TestSmapiJava/war/amazonVendorId.php?callback=cb&accessToken=" + accessToken;
     JsonpRequestBuilder builder = new JsonpRequestBuilder(); 
     
     builder.requestObject(phpUrl, new AsyncCallback<VendorIdInfo>() {
       public void onFailure(Throwable caught) {
         Window.alert("Couldn't retrieve JSON");
       }

       public void onSuccess(VendorIdInfo data) {
        if (data.getError() != null) {
        	Window.alert("Error retrieving user info: " + data.getError());
	} else if (data.getMessage() != null) {
	 	Window.alert("Error: " + data.getMessage());
	} else {
         	 // TODO: deal with the case of multiple vendor ids... 
	//	 String allInfo = "";
	//	 allInfo += "Vendor Id: " + data.getVendorId();
	//	 allInfo += "\nName: " + data.getName();
	//	 allInfo += "\nRoles: " + data.getRoles();
	//	 allInfo += "\n";
	//	 
	//	 Window.alert(allInfo);
        	setVendorId(data.getVendorId());
		Window.alert("set vendor id: "+vendorId);
	}
       }
     });
   } else {
     Window.alert("Please login to Amazon. (No access token.)");
   } 
  }

  private void setVendorId(String vendorId) {
	  this.vendorId = vendorId;
  }

  public void listSkills(Integer maxSkills, String nextToken) {
    if (accessToken != null) {
	    String phpUrl = "https://appinventor-alexa.csail.mit.edu/smapi_gwt/gwt-2.8.2/TestSmapiJava/war/smapi_get.php?callback=cb&accessToken=" + accessToken;
	    if(vendorId == null) {
		getVendorId();
		Window.alert("got vendorId");
	    }
	    Window.alert("vendorId: "+vendorId);
	   // check for the case where you try to get the vendor id, but there's an error:
	   if (vendorId != null) { 
		// build the url:
     		phpUrl += "&dir=/v1/skills";
		phpUrl += "&q=vendorId=" + vendorId;
		if (maxSkills != null) {
			phpUrl += "%26maxResults=" + maxSkills;
		}
		if (nextToken != null) {
			phpUrl += "%26nextToken=" + nextToken;
		}

		// TODO DEL: use this in getting a skill status
		//phpUrl = "https://appinventor-alexa.csail.mit.edu/smapi_gwt/gwt-2.8.2/TestSmapiJava/war/smapi_get.php?callback=cb&accessToken=" + accessToken + "&dir=/v1/skills/amzn1.ask.skill.9d619d79-4a9d-4bf7-975e-b6a0c8786bd1/status" + "&q=resource=interactionModel"; 

		JsonpRequestBuilder builder = new JsonpRequestBuilder(); 
     
     		builder.requestObject(phpUrl, new AsyncCallback<SkillsInfo>() {
       			public void onFailure(Throwable caught) {
        	 		Window.alert("Couldn't retrieve JSON");
       			}

       			public void onSuccess(SkillsInfo data) {
       				 if (data.getError() != null) {
       				 	Window.alert("Error retrieving user info: " + data.getError());
       				 } else if (data.getMessage() != null) {
       				  	Window.alert("Error: " + data.getMessage());
       				 } else {
					String allInfo = "";
					for (int i = 0; i < data.getSkillNames().length(); i++) {
						allInfo += "Skill" + i + " Name: " + data.getSkillNames().get(i) + "\n";
						allInfo += "Skill" + i + " ID: " + data.getSkillIds().get(i) + "\n";
					}
					if (allInfo == "") {
						allInfo = "No skills created.";
					}
       				 	 Window.alert(allInfo);
       				 }
     			 }
     		});
	   }
   } else {
     Window.alert("Please login to Amazon. (No access token.)");
   } 
    
  }

  public void createSkill(String jsonManifest) {
    if (accessToken != null) {
	    String phpUrl = "https://appinventor-alexa.csail.mit.edu/smapi_gwt/gwt-2.8.2/TestSmapiJava/war/smapi_post.php?callback=cb&accessToken=" + accessToken;
	    // get vendorId if we don't have it, and add it to the url
	    if(vendorId == null) {
		getVendorId();
		Window.alert("got vendorId");
	    }
	    Window.alert("vendorId: "+vendorId);
	   // check for the case where you try to get the vendor id, but there's an error:
	   if (vendorId != null) { 
		// build the url:
     		phpUrl += "&dir=/v1/skills";
		phpUrl += "&vendorId=" + vendorId;
		phpUrl += "&json=";
		phpUrl += URL.encode(jsonManifest);
		
		JsonpRequestBuilder builder = new JsonpRequestBuilder(); 
     
     		builder.requestObject(phpUrl, new AsyncCallback<SkillIdInfo>() {
       			public void onFailure(Throwable caught) {
        	 		Window.alert("Couldn't retrieve JSON");
       			}

       			public void onSuccess(SkillIdInfo data) {
       				 if (data.getError() != null) {
       				 	Window.alert("Error retrieving user info: " + data.getError());
       				 } else if (data.getMessage() != null) {
       				  	Window.alert("Error: " + data.getMessage());
       				 } else {
					if (isSkillId(data.getId())) {
       						Window.alert("New Skill ID: " + data.getId());
						createdSkills.push(data.getId());
					} else {
						Window.alert("New skill ID is incorrect: " + data.getId() + ". Please try again.");
					}
				 }
     			 }
     		});
	   }
   } else {
     Window.alert("Please login to Amazon. (No access token.)");
   } 
    
  }
  
  /*
   * Updates an existing skill's manifest  with the skill ID, skillId, and 
   * the manifest, jsonManifest.
   */
  public void updateSkillManifest(String skillId, String jsonManifest) {
    if (accessToken != null) {
	    String phpUrl = "https://appinventor-alexa.csail.mit.edu/smapi_gwt/gwt-2.8.2/TestSmapiJava/war/smapi_put.php?callback=cb&accessToken=" + accessToken;
	    // get vendorId if we don't have it, and add it to the url
	    if(vendorId == null) {
		getVendorId();
		Window.alert("got vendorId");
	    }
	    Window.alert("vendorId: "+vendorId);
	   // check for the case where you try to get the vendor id, but there's an error:
	   if (vendorId != null) { 
		// build the url:
     		phpUrl += "&dir=/v1/skills/" + skillId + "/stages/development/manifest";
		phpUrl += "&vendorId=" + vendorId;
		phpUrl += "&json=";
		phpUrl += URL.encode(jsonManifest);
		
		JsonpRequestBuilder builder = new JsonpRequestBuilder();  
     		builder.requestObject(phpUrl, new AsyncCallback<SkillIdInfo>() {
       			public void onFailure(Throwable caught) {
        	 		Window.alert("Couldn't retrieve JSON");
       			}

       			public void onSuccess(SkillIdInfo data) {
       				 if (data.getError() != null) {
       				 	Window.alert("Error retrieving user info: " + data.getError());
       				 } else if (data.getMessage() != null) {
       				  	Window.alert("Error: " + data.getMessage());
       				 } else {
       					Window.alert("Success! The skill manifest was updated.");
				 }
     			 }
     		});
	   }
   } else {
     Window.alert("Please login to Amazon. (No access token.)");
   } 
    
  }

  /*
   * Deletes the most recent skill created using the most recent skill id 
   * on the createdSkills stack.
   */
  public void deleteLastSkill () {
	Window.alert("deleting last skill...");
	// check for empty stack:
	try {
		// the "true" in deleteSkill() causes the last skill to be popped off
		deleteSkill(createdSkills.peek(), true);
	} catch (EmptyStackException e) { 
		Window.alert("No skills created this session. Can't delete last skill.");
	}
  }

  /*
   * Deletes skill using the skillId provided. 
   */
  public void deleteSkill(String skillId) {
  	// note that if this skill is in the createdSkills stack, it won't 
	// be popped off the stack (and if you try to delete it later using 
	// deleteLastSkill, you may run into an error). 
	  deleteSkill(skillId, false);	
  }

  /*
   * Deletes skill using the skillId provided. If popLastSkill is set to true, 
   * the most recently created skill will be popped off of the stack after 
   * being deleted.
   */
  private void deleteSkill(String skillId, final boolean popLastSkill) {
	Window.alert("TODO. In deleteSkill. popLastSkill: " + popLastSkill);
	// check to make sure the user has logged in
	if (accessToken != null) {
	    String phpUrl = "https://appinventor-alexa.csail.mit.edu/smapi_gwt/gwt-2.8.2/TestSmapiJava/war/smapi_delete.php?callback=cb&accessToken=" + accessToken;
	    // get vendorId if we don't have it, and add it to the url
	    if(vendorId == null) {
		getVendorId();
		Window.alert("got vendorId");
	    }
	    Window.alert("vendorId: " + vendorId);
	   // check for the case where you try to get the vendor id, but there's an error:
	   if (vendorId != null) {
		// build the url:
     		phpUrl += "&dir=/v1/skills/" + skillId + "/";
		phpUrl += "&vendorId=" + vendorId;
		JsonpRequestBuilder builder = new JsonpRequestBuilder(); 
     		
		// make the request
     		builder.requestObject(phpUrl, new AsyncCallback<SkillIdInfo>() {
       			public void onFailure(Throwable caught) {
        	 		Window.alert("Couldn't retrieve JSON");
       			}

       			public void onSuccess(SkillIdInfo data) {
       				 if (data.getError() != null) {
       				 	Window.alert("Error retrieving user info: " + data.getError());
       				 } else if (data.getMessage() != null) {
       				  	Window.alert("Error: " + data.getMessage());
       				 } else { 
					 // TODO: if popLastSkill=true and we run into an error BECAUSE THAT SKILL WASN'T FOUND then we still need to pop it too!! check the errors to see if this occurs, and then pop the last skill in that case
					if (popLastSkill) {
						createdSkills.pop();
					}
					Window.alert("Success!");
				 }
     			 }
     		});
	   }
   } else {
     Window.alert("Please login to Amazon. (No access token.)");
   } 
  }

  /*
   * Returns the most recent skillId. Returns null if no skills were created
   * in this session.
   */
  public String getLastSkillIdCreated() {
	String skillId = null;
	// check for empty stack:
	try {
		skillId = createdSkills.peek();
	} catch (EmptyStackException e) { 
	}
	return skillId;	
  }

  /*
   * Tests to see if the given string has the basic requirements to be 
   * an Amazon Skill ID.
   */
  private boolean isSkillId(String id) {
	boolean isId = true;
	if(!id.contains(".")) {
		isId = false;
	} else {
       		String[] idParts = id.split("\\.");
		if (idParts.length != 4) {
			isId = false;
		} else {
			if (!idParts[0].equals("amzn1") || !idParts[1].equals("ask") || !idParts[2].equals("skill")) {
				isId = false;
			}
		}
	}
	return isId;
  }

}
