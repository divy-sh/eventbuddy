package com.eventbuddy.eventbuddy.dao;

import com.eventbuddy.eventbuddy.model.Ad;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AdDao {

  @Autowired
  private QueryManager queryManager;

  public Ad createAd(Ad ad) {
    String query = "call insert_advertisement(?, ?, ?, ?, ?)";
    List<Ad> ads = queryManager.runQuery(query, Ad.class, ad.getAdTitle(), ad.getAdImageLocation(),
        ad.getBeginTime(), ad.getEndTime(), ad.getOrgId());
    if(ads.isEmpty()) {
      return null;
    }
    return ads.get(0);
  }

  public Ad getAd() {
    String query = "call get_advertisement()";
    List<Ad> ads = queryManager.runQuery(query, Ad.class);
    if(ads.isEmpty()) {
      return null;
    }
    return ads.get(0);
  }
}
