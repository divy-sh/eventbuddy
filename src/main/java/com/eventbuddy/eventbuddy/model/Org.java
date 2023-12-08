package com.eventbuddy.eventbuddy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Org {

  @JsonProperty("org_id")
  private int orgId;

  @JsonProperty("org_desc")
  private String orgDesc;

  @JsonProperty("org_name")
  private String orgName;

  public int getOrgId() {
    return orgId;
  }

  public void setOrgId(int orgId) {
    this.orgId = orgId;
  }

  public String getOrgDesc() {
    return orgDesc;
  }

  public void setOrgDesc(String orgDesc) {
    this.orgDesc = orgDesc;
  }

  public String getOrgName() {
    return orgName;
  }

  public void setOrgName(String orgName) {
    this.orgName = orgName;
  }
}
