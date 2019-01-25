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

import com.google.gwt.user.client.Window;

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
    final Button createSkillBtn = new Button("Create Skill");
    final Button updateManifestBtn = new Button("Update Skill Manifest");
    final Button deleteLastSkillBtn = new Button("Delete Last Skill");
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
    // TODO: get delete skill working ...
    // RootPanel.get("buttonContainer").add(deleteLastSkillBtn);
    RootPanel.get("buttonContainer").add(createSkillBtn);
    RootPanel.get("buttonContainer").add(updateManifestBtn);
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
	createSkillBtn.setEnabled(true);	
	updateManifestBtn.setEnabled(true);	
	deleteLastSkillBtn.setEnabled(true);	
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
	    amazon.listSkills(null,null);
	}
    }); 

    // Add handler for the create skill button
    createSkillBtn.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
		// Note that the vendorId will be replaced within the createSkill() method
	 	String jsonManifest = "{\"vendorId\": \"IAMZORILLA\",\"manifest\": {\"publishingInformation\": {\"locales\": {\"en-US\": {\"summary\": \"This is a sample Alexa skill.\",\"examplePhrases\": [\"Alexa, open sample skill.\",\"Alexa, turn on kitchen lights.\",\"Alexa, blink kitchen lights.\"],\"keywords\": [\"Smart Home\",\"Lights\",\"Smart Devices\"],\"name\": \"Sample custom skill name.\",\"description\": \"This skill has basic and advanced smart devices control features.\"}},\"isAvailableWorldwide\": false,\"testingInstructions\": \"1) Say 'Alexa, discover my devices' 2) Say 'Alexa, turn on sample lights'\",\"category\": \"SMART_HOME\",\"distributionCountries\": [\"US\",\"GB\"]},\"apis\": {\"custom\": {\"endpoint\": {\"uri\": \"arn:aws:lambda:us-east-1:032174894474:function:ask-custom-custome_cert\"}}},\"manifestVersion\": \"1.0\",\"privacyAndCompliance\": {\"allowsPurchases\": false,\"locales\": {\"en-US\": {\"termsOfUseUrl\": \"http://www.termsofuse.sampleskill.com\",\"privacyPolicyUrl\": \"http://www.myprivacypolicy.sampleskill.com\"}},\"isExportCompliant\": true,\"isChildDirected\": false,\"usesPersonalInfo\": false}}}";			
			
		amazon.createSkill(jsonManifest);
	}
    }); 

    // Add handler for the update skill manifest button
    updateManifestBtn.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
		// Note that the vendorId will be replaced within the createSkill() method
	 	String jsonManifest = "{\"manifest\": {\"publishingInformation\": {\"locales\": {\"en-US\": {\"summary\": \"This is a NEWLY IMPROVED SUPER DUPER Alexa skill!\",\"examplePhrases\": [\"Alexa, open my pet zorilla skill.\",\"Alexa, I want to play with my pet zorilla.\",\"Alexa, how's my zorilla doing?\"],\"keywords\": [\"Zorilla\",\"Pet\",\"Animals\"],\"name\": \"Sample custom skill name.\",\"description\": \"This skill has basic and advanced smart devices control features.\"}},\"isAvailableWorldwide\": false,\"testingInstructions\": \"1) Say 'Alexa, discover my devices' 2) Say 'Alexa, turn on sample lights'\",\"category\": \"SMART_HOME\",\"distributionCountries\": [\"US\",\"GB\"]},\"apis\": {\"custom\": {\"endpoint\": {\"uri\": \"arn:aws:lambda:us-east-1:032174894474:function:ask-custom-custome_cert\"}}},\"manifestVersion\": \"1.0\",\"privacyAndCompliance\": {\"allowsPurchases\": false,\"locales\": {\"en-US\": {\"termsOfUseUrl\": \"http://www.termsofuse.sampleskill.com\",\"privacyPolicyUrl\": \"http://www.myprivacypolicy.sampleskill.com\"}},\"isExportCompliant\": true,\"isChildDirected\": false,\"usesPersonalInfo\": false}}}";			
		String skillId = amazon.getLastSkillIdCreated();
		if (skillId != null) {		
			amazon.updateSkillManifest(skillId, jsonManifest);
		} else {
			Window.alert("No skills recently created. Create a new skill, and then you can update it.");
		}
	}
   }); 

    // Add a handler for the delete last skill button
    deleteLastSkillBtn.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
	    amazon.deleteLastSkill();
	}
    }); 

  }
}
