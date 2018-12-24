package com.appinventor.testsmapijava.client;

 import com.google.gwt.core.client.JavaScriptObject;

public class StockDataTodoDel extends JavaScriptObject {

  // Overlay types always have protected, zero-arg actors
  protected StockDataTodoDel() { }

  // Typically, methods on overlay types are JSNI
  public final native String getSymbol() /*-{ return this.symbol; }-*/;
  public final native String getPrice()  /*-{ return this.price;  }-*/;
  public final native String getChange()  /*-{ return this.change;  }-*/;

  // Note, though, that methods aren't required to be JSNI
  public final String getNewThings() {
    return "new things!";
  }
}