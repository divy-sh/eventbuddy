package com.eventbuddy.eventbuddy.service;

import com.eventbuddy.eventbuddy.Utils.BuddyError;
import com.eventbuddy.eventbuddy.dao.OrgDao;
import com.eventbuddy.eventbuddy.dao.UserDao;
import com.eventbuddy.eventbuddy.model.Org;
import com.eventbuddy.eventbuddy.model.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrgService {

  @Autowired
  private OrgDao orgDao;
  @Autowired
  private UserDao userDao;

  public Org get(int orgId) throws BuddyError {
    Org org = orgDao.getOrg(orgId);
    if (org == null) {
      throw new BuddyError("no org found");
    }
    return org;
  }

  public List<Org> getOrgs(String email) throws BuddyError {
    if (userDao.getUserDetail(email) == null) {
      throw new BuddyError("Invalid user email");
    }
    List<Org> orgs = orgDao.getOrgs(email);
    if (orgs.isEmpty()) {
      throw new BuddyError("no orgs found");
    }
    return orgs;
  }

  public Org createOrg(Org org, String email) throws BuddyError {
    User user = userDao.getUserDetail(email);
    if (user == null) {
      throw new BuddyError("Invalid user email");
    }
    if (!user.isOrganizer()) {
      throw new BuddyError("normal user can't create an organization");
    }
    orgDao.createOrg(org, email);
    return org;
  }

  public void addUserToOrg(int orgId, String email) throws BuddyError {
    Org org = orgDao.getOrg(orgId);
    User user = userDao.getUserDetail(email);
    if (org == null) {
      throw new BuddyError("invalid org id");
    }
    if (user == null) {
      throw new BuddyError("invalid user email");
    }
    if (!user.isOrganizer()) {
      throw new BuddyError("user is not an organizer");
    }
    orgDao.addUserToOrg(orgId, email);
  }
}
