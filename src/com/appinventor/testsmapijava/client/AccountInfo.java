package com.appinventor.testsmapijava.client;

 import com.google.gwt.core.client.JavaScriptObject;

public class AccountInfo extends JavaScriptObject {

  // Overlay types always have protected, zero-arg actors
  protected AccountInfo() { }

  // Typically, methods on overlay types are JSNI
  public final native String getAccountLinkingResponse() /*-{ return this.accountLinkingResponse; }-*/;
  public final native String getClientId() /*-{ return this.accountLinkingResponse.clientId; }-*/;
  public final native String getAccessTokenUrl()  /*-{ return this.accountLinkingResponse.accessTokenUrl;  }-*/;
  public final native String getAuthorizationUrl() /*-{ return this.accountLinkingResponse.authorizationUrl }-*/;
}
