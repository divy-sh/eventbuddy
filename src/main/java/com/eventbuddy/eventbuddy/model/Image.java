package com.eventbuddy.eventbuddy.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Image {

  @JsonProperty("image_id")
  private String imageId;
  @JsonProperty("image_url")
  private String imageUrl;
  @JsonProperty("event_id")
  private int event_id;

  public String getImageId() {
    return imageId;
  }

  public void setImageId(String imageId) {
    this.imageId = imageId;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public int getEvent_id() {
    return event_id;
  }

  public void setEvent_id(int event_id) {
    this.event_id = event_id;
  }
}
