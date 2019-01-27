package com.appinventor.testsmapijava.client;

import com.google.gwt.core.client.JavaScriptObject;

public class AccessTokenInfo extends JavaScriptObject {

    // Overlay types always have protected, zero-arg actors
    protected AccessTokenInfo() { }

    // Typically, methods on overlay types are JSNI
    public final native String getAccessToken() /*-{ return this.access_token; }-*/;
    public final native String getRefreshToken() /*-{ return this.refresh_token; }-*/;
    public final native String getTokenType()  /*-{ return this.token_type;  }-*/;
    public final native int getExpireTime() /*-{ return this.expires_in }-*/;
}
