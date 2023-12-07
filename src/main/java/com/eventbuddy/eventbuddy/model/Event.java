package com.eventbuddy.eventbuddy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.lang.reflect.Field;
import java.time.LocalDateTime;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {

  @JsonProperty("event_id")
  private int eventId;
  @JsonProperty("event_name")
  private String eventName;
  @JsonProperty("event_description")
  private String eventDescription;
  @JsonProperty("start_time")
  private LocalDateTime eventStart;
  @JsonProperty("end_time")
  private LocalDateTime eventEnd;
  @JsonProperty("last_registration_date")
  private LocalDateTime lastRegistrationDate;
  @JsonProperty("capacity")
  private int capacity;
  @JsonProperty("entry_fees")
  private float entryFee;
  @JsonProperty("org_id")
  private int orgId;

  public int getEventId() {
    return eventId;
  }

  public void setEventId(int eventId) {
    this.eventId = eventId;
  }

  public String getEventName() {
    return eventName;
  }

  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  public String getEventDescription() {
    return eventDescription;
  }

  public void setEventDescription(String eventDescription) {
    this.eventDescription = eventDescription;
  }

  public LocalDateTime getEventStart() {
    return eventStart;
  }

  public void setEventStart(LocalDateTime eventStart) {
    this.eventStart = eventStart;
  }

  public LocalDateTime getEventEnd() {
    return eventEnd;
  }

  public void setEventEnd(LocalDateTime eventEnd) {
    this.eventEnd = eventEnd;
  }

  public LocalDateTime getLastRegistrationDate() {
    return lastRegistrationDate;
  }

  public void setLastRegistrationDate(LocalDateTime lastRegistrationDate) {
    this.lastRegistrationDate = lastRegistrationDate;
  }

  public int getCapacity() {
    return capacity;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  public float getEntryFee() {
    return entryFee;
  }

  public void setEntryFee(float entryFee) {
    this.entryFee = entryFee;
  }

  public int getOrgId() {
    return orgId;
  }

  public void setOrgId(int orgId) {
    this.orgId = orgId;
  }

  public void update(Event event) throws IllegalAccessException {
    Class<?> clazz = Event.class;
    Field[] fields = clazz.getFields();

    for (Field field : fields) {
      Object val = field.get(event);
      if (val != null) {
        field.set(this, val);
      }
    }
  }
}
