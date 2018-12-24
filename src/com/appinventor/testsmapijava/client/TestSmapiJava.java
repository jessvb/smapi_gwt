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
   * The url where the php code lives and serves up json callbacks
   */
  private final String JSON_URL = "http://appinventor-alexa.csail.mit.edu/stockPrices.php";

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    final Button getButton = new Button("GET");
    // final TextBox nameField = new TextBox();
    // nameField.setText("GWT User");
    final Label errorLabel = new Label();

    // We can add style names to widgets
    getButton.addStyleName("getButton");

    // Add the nameField and sendButton to the RootPanel
    // Use RootPanel.get() to get the entire body element
    // RootPanel.get("nameFieldContainer").add(nameField);
    RootPanel.get("getButtonContainer").add(getButton);
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
        getButton.setEnabled(true);
        getButton.setFocus(true);
      }
    });

    // Create a handler for the sendButton and nameField
    class MyHandler implements ClickHandler, KeyUpHandler {
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
        String todoTest = "test1";
        textToServerLabel.setText(todoTest);
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
        String url = JSON_URL;
        url += "?q=ABC&callback=callback29";
        url = URL.encode(url);

        JsonpRequestBuilder builder = new JsonpRequestBuilder();
        builder.requestObject(url, new AsyncCallback<JsArray<StockDataTodoDel>>() {
          public void onFailure(Throwable caught) {
            displayError("Couldn't retrieve JSON");
          }

          public void onSuccess(JsArray<StockDataTodoDel> data) {
            displayTodoObj(data);
          }
        });
      }

      private void displayError(String msg) {
        dialogBox.setText("Remote Procedure Call - Failure");
        serverResponseLabel.addStyleName("serverResponseLabelError");
        serverResponseLabel.setHTML(SERVER_ERROR);
        dialogBox.center();
        closeButton.setFocus(true);
      }

      private void displayTodoObj(JsArray<StockDataTodoDel> data) {
        dialogBox.setText("Remote Procedure Call");
        serverResponseLabel.removeStyleName("serverResponseLabelError");

        serverResponseLabel.setHTML("Symbol: " + data.get(0).getSymbol() + " Price: " + data.get(0).getPrice()
            + " Change: " + data.get(0).getChange());
        dialogBox.center();
        closeButton.setFocus(true);
      }
    }

    // Add a handler to send the name to the server
    MyHandler handler = new MyHandler();
    getButton.addClickHandler(handler);
    // nameField.addKeyUpHandler(handler);
  }
}
