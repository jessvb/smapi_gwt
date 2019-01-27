package com.appinventor.testsmapijava.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;

public class SkillsInfo extends JavaScriptObject {

    // Overlay types always have protected, zero-arg actors
    protected SkillsInfo() { }
    // Typically, methods on overlay types are JSNI
    public final native JsArray getSkills() /*-{ return this.skills; }-*/;
    public final native JsArrayString getSkillNames() /*-{ 
                                                        var skillNames = [];
                                                        for (var i = 0; i < this.skills.length; i++) {
                                                        skillNames.push(this.skills[i].nameByLocale["en-US"]);
                                                        }
                                                        return skillNames;
                                                        }-*/;
        public final native JsArrayString getSkillIds() /*-{
                                                          var skillIds = [];
                                                          for (var i = 0; i < this.skills.length; i++) {
                                                          skillIds.push(this.skills[i].skillId);
                                                          }
                                                          return skillIds;
                                                          }-*/;
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
