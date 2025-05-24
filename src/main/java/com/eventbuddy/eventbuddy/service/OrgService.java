package com.eventbuddy.eventbuddy.service;

import com.eventbuddy.eventbuddy.Utils.BuddyError;
import com.eventbuddy.eventbuddy.Utils.Utils;
import com.eventbuddy.eventbuddy.dao.OrgDao;
import com.eventbuddy.eventbuddy.dao.UserDao;
import com.eventbuddy.eventbuddy.model.Org;
import com.eventbuddy.eventbuddy.model.OrgTeam;
import com.eventbuddy.eventbuddy.model.User;
import java.util.List;
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

  public Org createOrg(Org org) throws BuddyError {
    User user = Utils.getAuthenticatedUser();
    if (user == null) {
      throw new BuddyError("Invalid user email");
    }
    if (!user.isOrganizer()) {
      throw new BuddyError("normal user can't create an organization");
    }
    orgDao.createOrg(org, user.getEmail());
    return org;
  }

  public void addUserToOrg(int orgId, String email) throws BuddyError {
    // check if org is valid
    validOrg(orgId);
    
    // check if the current auth user is a valid organizer who is part of the org
    authUserHasAccessToOrg(orgId);

    // check if the email we are trying to add is of a valid user
    User user = userDao.getUserDetail(email);
    if (user == null) {
      throw new BuddyError("invalid user email");
    }
    // check if the user is already a part of the org, in that case throw an error
    List<OrgTeam> orgTeam = orgDao.getOrgTeam(orgId);
    boolean isEmailAlreadyInOrg = orgTeam.stream()
        .anyMatch(teamMember -> teamMember.getOrganizerEmailId().equals(email));

    if (isEmailAlreadyInOrg) {
        throw new BuddyError("User is already a member of the organization");
    }

    // add user to the org
    orgDao.addUserToOrg(orgId, email);
  }

  public boolean delete(int orgId) throws BuddyError {
    // check if org is valid
    validOrg(orgId);

    // check if the current auth user is a valid organizer who is part of the org
    authUserHasAccessToOrg(orgId);

    orgDao.delete(orgId);
    return true;
  }

  private void validOrg(int orgId) throws BuddyError {
    Org org = orgDao.getOrg(orgId);
    if (org == null) {
      throw new BuddyError("invalid org id");
    }
  }

  private void authUserHasAccessToOrg(int orgId) throws BuddyError {
    User authUser = Utils.getAuthenticatedUser();
    if (authUser == null) {
      throw new BuddyError("No Authentication");
    }
    List<OrgTeam> orgTeam = orgDao.getOrgTeam(orgId);
    boolean isUserInOrgTeam = orgTeam.stream()
    .anyMatch(teamMember -> teamMember.getOrganizerEmailId().equals(authUser.getEmail()));
    if (!isUserInOrgTeam) {
        throw new BuddyError("Authenticated user is not a member of the organization");
    }
  }
}
