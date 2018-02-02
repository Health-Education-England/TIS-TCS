package com.transformuk.hee.tis.tcs.api.enumeration;

public enum QualificationType {
  HIGHER_DEGREE("Higher Degree"),
  BASIC_DEGREE("Basic Degree"),
  BACHELORS_DEGREE_HONS("Bachelors Degree Hons"),
  BACHELORS_DEGREE("Bachelors Degree"),
  DIPLOMA("Diploma"),
  NON_MEDICAL_DEGREE("Non Medical Degree"),
  BM("BM"),
  PRIMARY_QUALIFICATION("Primary qualificatio"),
  MB_BS("MB BS"),
  BM_BS("BM BS"),
  MB_CHB("MB ChB"),
  MB_BCHIR("MB BChir"),
  MB_BCH("MB BCh"),
  BM_BCH("BM BCh"),
  NVQ("NVQ"),
  CERTIFICATION("Certification"),
  COLLEGE_FACULTY("College/Faculty"),
  BDS("BDS"),
  MD("MD"),
  PHD("PhD"),
  STATE_EXAM("State Exam"),
  HIGHER_QUALIFICATION("Higher qualification"),
  LEKARZ("Lekarz"),
  MASTERS_DEGREE("Masters Degree"),
  MBBS_OR_BSC("MB BS/BSc"),
  MEDICINE("Medicine"),
  BMBS("BMBS"),
  MBBS("MBBS"),
  MBCHB("MbChB"),
  MEDICINE_MBCHB("Medicine MBChB");

  private String name;

  QualificationType(String name) {
    this.name = name;
  }

  public String getName(){
    return this.name;
  }

  @Override
  public String toString() {
    return name;
  }
}
