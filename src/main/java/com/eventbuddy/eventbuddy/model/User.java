package com.eventbuddy.eventbuddy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

  @JsonProperty("email_id")
  private String email;
  @JsonProperty("first_name")
  private String firstName;
  @JsonProperty("last_name")
  private String lastName;
  @JsonProperty("date_of_birth")
  private Date dateOfBirth;
  @JsonProperty(value = "user_password", access = JsonProperty.Access.WRITE_ONLY)
  private String password;
  @JsonProperty(value = "is_admin")
  private boolean isAdmin;
  @JsonProperty(value = "is_organizer")
  private boolean isOrganizer;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Date getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(Date dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isAdmin() {
    return isAdmin;
  }

  public void setAdmin(boolean admin) {
    isAdmin = admin;
  }

  public boolean isOrganizer() {
    return isOrganizer;
  }

  public void setOrganizer(boolean organizer) {
    isOrganizer = organizer;
  }
}
