package com.eventbuddy.eventbuddy.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrgTeam {
    
    @JsonProperty("organizer_email_id")
    String organizerEmailId;

    @JsonProperty("org_id")
    private int orgId;

    public String getOrganizerEmailId() {
        return organizerEmailId;
    }

    public void setOrganizerEmailId(String organizerEmailId) {
        this.organizerEmailId = organizerEmailId;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }
}
