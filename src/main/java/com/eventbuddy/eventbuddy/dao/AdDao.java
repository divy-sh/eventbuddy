package com.eventbuddy.eventbuddy.dao;

import com.eventbuddy.eventbuddy.Utils.BuddyError;
import com.eventbuddy.eventbuddy.model.Ad;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AdDao {

  @Autowired
  private QueryManager queryManager;

  public Ad createAd(Ad ad) throws BuddyError {
    String query = "call insert_advertisement(?, ?, ?, ?, ?)";
    List<Ad> ads = queryManager.runQuery(query, Ad.class, ad.getAdTitle(), ad.getAdImageLocation(),
        ad.getBeginTime(), ad.getEndTime(), ad.getOrgId());
    if (ads.isEmpty()) {
      return null;
    }
    return ads.getFirst();
  }

  public List<Ad> getAdWithApproval(String approvalStatus) throws BuddyError {
    String query = "call get_ad_by_status(?)";
    return queryManager.runQuery(query, Ad.class, approvalStatus);
  }

  public void delete(int adId) {
    String query = "call delete_ads(?)";
    queryManager.update(query, adId);
  }

  public Ad update(Ad ad) throws BuddyError {
    String query = "call update_advertisement(?, ?, ?, ?, ?, ?)";
    List<Ad> ads =
        queryManager.runQuery(query, Ad.class, ad.getAdId(), ad.getAdTitle(),
            ad.getAdImageLocation(),
            ad.getBeginTime(), ad.getEndTime(), ad.getOrgId());
    if (ads.isEmpty()) {
      return null;
    }
    return ads.getFirst();
  }

  public List<Ad> getAllAdByOrg(String upperCase, int orgId) throws BuddyError {
    String query = "call get_ads_by_status_org_id(?, ?)";
    return queryManager.runQuery(query, Ad.class, upperCase, orgId);
  }
}
