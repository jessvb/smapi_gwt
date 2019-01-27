package com.appinventor.testsmapijava.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;

public class ErrorInfo extends JavaScriptObject {

    /*
     * The SkillError class is used for HTTP requests that return nothing
     * on success, but return error messages on failure.
     */
    // Overlay types always have protected, zero-arg actors
    protected ErrorInfo() { }
    // Typically, methods on overlay types are JSNI
    public final native JsArrayString getViolations() /*-{
                                                        var violationArr = [];
                                                        if (this.violations) {
                                                        for (var i = 0; i < this.violations.length; i++) {
                                                        violationArr.push(this.violations[i].message);
                                                        }
                                                        }
                                                        return violationArr;
                                                        }-*/;
        public final native String getMessage() /*-{ 
                                                  var msg = this.message;
                                                  if (!msg) {
                                                  msg = null; // ensure exact null
                                                  }
                                                  return msg;
                                                  }-*/;
        public final native String getError() /*-{ 
                                                if (this) { // ensure we got *something*
                                                var err = this.error; 
                                                if (!err) {
                                                err = null; // ensure exact null
                                                }
                                                } else {
                                                err = "No skills.";
                                                }
                                                return err;
                                                }-*/;
}
