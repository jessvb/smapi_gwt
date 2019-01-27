package com.appinventor.testsmapijava.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;

public class VendorIdInfo extends JavaScriptObject {

    // Overlay types always have protected, zero-arg actors
    protected VendorIdInfo() { }

    // Typically, methods on overlay types are JSNI
    public final native String getVendorId() /*-{ return this.vendors[0].id; }-*/;
    public final native String getName()  /*-{ return this.vendors[0].name;  }-*/;
    public final native JsArrayString getRoles() /*-{ return this.vendors[0].roles; }-*/;
    public final native String getMessage() /*-{ var msg = this.message;
                                              if (!msg) {
                                              msg = null; // ensure exact null
                                              }
                                              return msg;
                                              }-*/;
    public final native String getError() /*-{ var err = this.error; 
                                            if (!err) {
                                            err = null; // ensure exact null
                                            }
                                            return err;
                                            }-*/;
}
