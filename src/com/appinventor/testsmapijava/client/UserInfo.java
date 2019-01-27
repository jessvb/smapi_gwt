package com.appinventor.testsmapijava.client;

import com.google.gwt.core.client.JavaScriptObject;

public class UserInfo extends JavaScriptObject {

    // Overlay types always have protected, zero-arg actors
    protected UserInfo() { }

    // Typically, methods on overlay types are JSNI
    public final native String getUserId() /*-{ return this.user_id; }-*/;
    public final native String getEmail() /*-{ return this.email; }-*/;
    public final native String getName()  /*-{ return this.name;  }-*/;
    public final native String getPostalCode() /*-{ return this.postalcode; }-*/;
    public final native String getError() /*-{ var err = this.error; 
                                            if (!err) {
                                            err = null; // ensure exact null
                                            }
                                            return err;
                                            }-*/;
}
