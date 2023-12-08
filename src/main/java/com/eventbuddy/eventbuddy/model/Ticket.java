package com.eventbuddy.eventbuddy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Ticket {

  @JsonProperty("ticket_id")
  private int ticketId;
  @JsonProperty("ticket_user_name")
  private String ticketUserName;
  @JsonProperty("id_proof")
  private String idProof;
  @JsonProperty("trans_id")
  private String transactionId;
}
