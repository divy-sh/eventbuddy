package com.eventbuddy.eventbuddy.dao;

import com.eventbuddy.eventbuddy.Utils.BuddyError;
import com.eventbuddy.eventbuddy.model.Org;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrgDao {

  @Autowired
  private QueryManager queryManager;

  public Org getOrg(int orgId) throws BuddyError {
    String query = "call get_event_org(?)";
    List<Org> org = queryManager.runQuery(query, Org.class, orgId);
    if (org.isEmpty()) {
      return null;
    }
    return org.getFirst();
  }

  public List<Org> getOrgs(String email) throws BuddyError {
    String query = "call get_all_orgs(email)";
    return queryManager.runQuery(query, Org.class);
  }

  public void createOrg(Org org, String email) throws BuddyError {
    String query = "call insert_event_org(?, ?, ?)";
    queryManager.update(query, email, org.getOrgName(), org.getOrgDesc());
  }

  public void addUserToOrg(int orgId, String email) throws BuddyError {
    String query = "call insert_event_org_team(?, ?)";
    queryManager.update(query, orgId, email);
  }

  public void delete(int orgId) throws BuddyError {
    String query = "call delete_event_org(?)";
    queryManager.update(query, orgId);
  }
}
