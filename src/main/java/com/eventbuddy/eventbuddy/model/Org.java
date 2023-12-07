package com.eventbuddy.eventbuddy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Org {
  @JsonProperty("org_id")
  private int orgId;

  @JsonProperty("org_desc")
  private int orgDesc;

  @JsonProperty("org_name")
  private int orgName;

  public int getOrgId() {
    return orgId;
  }

  public void setOrgId(int orgId) {
    this.orgId = orgId;
  }

  public int getOrgDesc() {
    return orgDesc;
  }

  public void setOrgDesc(int orgDesc) {
    this.orgDesc = orgDesc;
  }

  public int getOrgName() {
    return orgName;
  }

  public void setOrgName(int orgName) {
    this.orgName = orgName;
  }
}
