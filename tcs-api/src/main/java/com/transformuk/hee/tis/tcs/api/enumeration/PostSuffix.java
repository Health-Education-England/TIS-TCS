package com.transformuk.hee.tis.tcs.api.enumeration;

public enum PostSuffix {

  SUPERNUMERY("/S"), MILITARY("/M"), ACADEMIC("/A");

  private String suffixValue;

  PostSuffix(String suffixValue) {
    this.suffixValue = suffixValue;
  }

  public String getSuffixValue() {
    return suffixValue;
  }

  public void setSuffixValue(String suffixValue) {
    this.suffixValue = suffixValue;
  }
}
