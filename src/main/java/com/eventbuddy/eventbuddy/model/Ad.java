package com.eventbuddy.eventbuddy.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class Ad {

  @JsonProperty("ad_id")
  private String adId;
  @JsonProperty("ad_title")
  private String adTitle;
  @JsonProperty("ad_img_loc")
  private String adImageLocation;
  @JsonProperty("ad_begin_time")
  private LocalDateTime beginTime;
  @JsonProperty("ad_end_time")
  private LocalDateTime endTime;
  @JsonProperty("ad_org_id")
  private String orgId;

  public String getAdId() {
    return adId;
  }

  public void setAdId(String adId) {
    this.adId = adId;
  }

  public String getAdTitle() {
    return adTitle;
  }

  public void setAdTitle(String adTitle) {
    this.adTitle = adTitle;
  }

  public String getAdImageLocation() {
    return adImageLocation;
  }

  public void setAdImageLocation(String adImageLocation) {
    this.adImageLocation = adImageLocation;
  }

  public LocalDateTime getBeginTime() {
    return beginTime;
  }

  public void setBeginTime(LocalDateTime beginTime) {
    this.beginTime = beginTime;
  }

  public LocalDateTime getEndTime() {
    return endTime;
  }

  public void setEndTime(LocalDateTime endTime) {
    this.endTime = endTime;
  }

  public String getOrgId() {
    return orgId;
  }

  public void setOrgId(String orgId) {
    this.orgId = orgId;
  }
}
