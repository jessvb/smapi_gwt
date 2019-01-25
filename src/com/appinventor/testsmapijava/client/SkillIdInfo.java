package com.appinventor.testsmapijava.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * JSNI overlay class that returns a skill's ID.
 */
public class SkillIdInfo extends JavaScriptObject {

  // Overlay types always have protected, zero-arg actors
  protected SkillIdInfo() { }
  // Typically, methods on overlay types are JSNI
  public final native String getId() /*-{ return this.skillId; }-*/;
  
  public final native String getMessage() /*-{ var msg = this.message;
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
