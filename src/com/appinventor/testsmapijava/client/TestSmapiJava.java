package com.appinventor.testsmapijava.client;

import com.appinventor.testsmapijava.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.http.client.URL;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.core.client.JsArray;

import com.appinventor.testsmapijava.client.MyHandler;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestSmapiJava implements EntryPoint {
  /**
   * The message displayed to the user when the server cannot be reached or
   * returns an error.
   */
  private static final String SERVER_ERROR = "An error occurred while "
      + "attempting to contact the server. Please check your network " + "connection and try again.";

  /**
   * Create a remote service proxy to talk to the server-side Greeting service.
   */
  private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

  /**
   * Make an AmazonSdk object to interact with Login with Amazon
   */
   private final AmazonSdk amazon = new AmazonSdk(AmazonClientId.getClientId());

  /**
   * The url where the php code lives and serves up json callbacks
   */
  private final String JSON_URL = "https://appinventor-alexa.csail.mit.edu/stockPrices.php";
  private final String TEST_URL = "https://appinventor-alexa.csail.mit.edu/test_access_token.php";

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    final Button loginButton = new Button("LOGIN WITH AMAZON");
    final Button logoutButton = new Button("LOGOUT OF AMAZON");
    final Button getUserInfoBtn = new Button("Get User Info");
    final Button getSkillBtn = new Button("Get Skill Info");
    // final TextBox nameField = new TextBox();
    // nameField.setText("GWT User");
    final Label errorLabel = new Label();

    // We can add style names to widgets TODO: delete
    // getUserInfoBtn.addStyleName("getUserInfoBtn");

    // Add the nameField and sendButton to the RootPanel
    // Use RootPanel.get() to get the entire body element
    // RootPanel.get("nameFieldContainer").add(nameField);
    RootPanel.get("buttonContainer").add(getUserInfoBtn);
    RootPanel.get("buttonContainer").add(getSkillBtn);
    RootPanel.get("buttonContainer").add(loginButton);
    RootPanel.get("buttonContainer").add(logoutButton);
    RootPanel.get("errorLabelContainer").add(errorLabel);

    // Focus the cursor on the name field when the app loads
    // nameField.setFocus(true);
    // nameField.selectAll();

    // Create the popup dialog box
    final DialogBox dialogBox = new DialogBox();
    dialogBox.setText("Remote Procedure Call");
    dialogBox.setAnimationEnabled(true);
    final Button closeButton = new Button("Close");
    // We can set the id of a widget by accessing its Element
    closeButton.getElement().setId("closeButton");
    final Label textToServerLabel = new Label();
    final HTML serverResponseLabel = new HTML();
    VerticalPanel dialogVPanel = new VerticalPanel();
    dialogVPanel.addStyleName("dialogVPanel");
    dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
    dialogVPanel.add(textToServerLabel);
    dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
    dialogVPanel.add(serverResponseLabel);
    dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
    dialogVPanel.add(closeButton);
    dialogBox.setWidget(dialogVPanel);

    // Add a handler to close the DialogBox
    closeButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        dialogBox.hide();
        getUserInfoBtn.setEnabled(true);
        getSkillBtn.setEnabled(true);
	loginButton.setEnabled(true);
        getUserInfoBtn.setFocus(true);
      }
    });
 
    // Add a handler to the logout button
    logoutButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        amazon.logoutAmazon();
      }
    });
    
    // Add a handler to the login button
    loginButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        amazon.loginAmazon(); // NOTE: need to allow pop-ups for this to occur
      }
    });

    // Add a handler for the get user info button
    getUserInfoBtn.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
	    amazon.getNameEmailUserid();
	}
    }); 

    // Add a handler for the get skill info button
    getSkillBtn.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
	    amazon.getSkillInfo();
	}
    }); 
    
  }
}
