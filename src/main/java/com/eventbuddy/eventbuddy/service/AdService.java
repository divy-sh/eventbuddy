package com.eventbuddy.eventbuddy.service;

import com.eventbuddy.eventbuddy.Utils.BuddyError;
import com.eventbuddy.eventbuddy.dao.AdDao;
import com.eventbuddy.eventbuddy.model.Ad;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdService {

  @Autowired
  private AdDao adDao;

  public Ad createAd(Ad ad) throws BuddyError {
    Ad result = adDao.createAd(ad);
    if (result == null) {
      throw new BuddyError("error in creating ad, please check data");
    }
    return result;
  }

  public Ad getAd() throws BuddyError {
    Ad result = adDao.getAd();
    if (result == null) {
      throw new BuddyError("error in creating ad, please check data");
    }
    return result;
  }

  public List<Ad> getAdWithFilter(String approvalStatus) {
    return adDao.getAdWithApproval(approvalStatus.toUpperCase());
  }
}
