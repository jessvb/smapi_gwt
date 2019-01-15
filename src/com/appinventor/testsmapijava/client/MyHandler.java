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

public class MyHandler implements ClickHandler, KeyUpHandler {
   private Button getButton;
   private String url;
   private String query;
   private String serverError; 
   private Label textToServerLabel;
   private HTML serverResponseLabel;
   private Label errorLabel;
   private Button closeButton;
   private DialogBox dialogBox;

   public MyHandler(Button getButton, String url, String query, String serverErrorMsg, Label textToServerLabel, HTML serverResponseLabel, Label errorLabel, Button closeButton, DialogBox dialogBox) {
     super();
     this.getButton = getButton;
     this.url = url;
     this.query = query;
     this.serverError = serverErrorMsg;
     this.textToServerLabel = textToServerLabel;
     this.serverResponseLabel = serverResponseLabel;
     this.errorLabel = errorLabel;
     this.closeButton = closeButton;
     this.dialogBox = dialogBox;
   }

    /**
    * Fired when the user clicks on the sendButton.
    */
   public void onClick(ClickEvent event) {
     getReq();
   }

   /**
    * Fired when the user types in the nameField.
    */
   public void onKeyUp(KeyUpEvent event) {
     if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
       getReq();
     }
   }

   /**
    * Send the name from the nameField to the server and wait for a response.
    */
   private void getReq() {
     // First, we validate the input.
     errorLabel.setText("");
     // String textToServer = nameField.getText();
     // if (!FieldVerifier.isValidName(textToServer)) {
     // errorLabel.setText("Please enter at least four characters");
     // return;
     // }

     // Then, we send the input to the server.
     getButton.setEnabled(false);
     textToServerLabel.setText(query);
     serverResponseLabel.setText("");
     // greetingService.greetServer(todoTest, new AsyncCallback<String>() {
     // public void onFailure(Throwable caught) {
     // // Show the RPC error message to the user
     // dialogBox.setText("Remote Procedure Call - Failure");
     // serverResponseLabel.addStyleName("serverResponseLabelError");
     // serverResponseLabel.setHTML(SERVER_ERROR);
     // dialogBox.center();
     // closeButton.setFocus(true);
     // }

     // public void onSuccess(String result) {
     // dialogBox.setText("Remote Procedure Call");
     // serverResponseLabel.removeStyleName("serverResponseLabelError");
     // serverResponseLabel.setHTML(result);
     // dialogBox.center();
     // closeButton.setFocus(true);
     // }
     // });
     url += query;
     url = URL.encode(url);

     JsonpRequestBuilder builder = new JsonpRequestBuilder();
     //builder.requestObject(url, new AsyncCallback<JsArray<StockDataTodoDel>>() {
     //builder.requestObject(url, new AsyncCallback<AccountInfo>() {
     builder.requestObject(url, new AsyncCallback<AccessTokenInfo>() {
       public void onFailure(Throwable caught) {
         displayError("Couldn't retrieve JSON");
       }

       public void onSuccess(AccessTokenInfo data) {
         displayObj(data);
       }
     });
   }

   private void displayError(String msg) {
     dialogBox.setText("Remote Procedure Call - Failure");
     serverResponseLabel.addStyleName("serverResponseLabelError");
     serverResponseLabel.setHTML(serverError + " Error Message: " + msg);
     dialogBox.center();
     closeButton.setFocus(true);
   }

   private void displayObj(AccessTokenInfo data) {
     dialogBox.setText("Remote Procedure Call");
     serverResponseLabel.removeStyleName("serverResponseLabelError");

     serverResponseLabel.setHTML("Access Token: " + data.getAccessToken() + "\nRefresh Token: " + data.getRefreshToken() + "\nToken Type: " + data.getTokenType() + "\nExpire Time: " + data.getExpireTime());
    // serverResponseLabel.setHTML("data.getAccessTokenUrl(): " + data.getAccessTokenUrl() + "\nClientID: " + data.getClientId() + "\nAccessTokenUrl: " + data.getAccessTokenUrl() + "\nAuthUrl: " + data.getAuthorizationUrl());
   //  serverResponseLabel.setHTML("Symbol: " + data.get(0).getSymbol() + " Price: " + data.get(0).getPrice()
   //      + " Change: " + data.get(0).getChange());
   dialogBox.center();
   closeButton.setFocus(true);
   }
 }

