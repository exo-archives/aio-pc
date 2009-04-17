package com.sample.samplejsfmyfacesportlet.bean;

public class MyFacesUserBean {


  public String getName() {
    return _name;
  }

  public void setName(String name) {
    _name = name;
  }

  private String _name = new String("Hello, World! From smyfacesportlet.");

}
