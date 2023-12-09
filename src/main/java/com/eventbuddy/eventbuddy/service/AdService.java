package com.eventbuddy.eventbuddy.service;

import com.eventbuddy.eventbuddy.Utils.BuddyError;
import com.eventbuddy.eventbuddy.dao.AdDao;
import com.eventbuddy.eventbuddy.model.Ad;
import java.util.List;
import java.util.Random;
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

  public Ad getRandomAd(String status) throws BuddyError {
    List<Ad> result = adDao.getAdWithApproval(status.toUpperCase());
    if (result.isEmpty()) {
      return null;
    }
    int randomIndex = new Random().nextInt(result.size());
    return result.get(randomIndex);
  }

  public List<Ad> getAdWithFilter(String approvalStatus) throws BuddyError {
    return adDao.getAdWithApproval(approvalStatus.toUpperCase());
  }

  public boolean delete(int adId) {
    adDao.delete(adId);
    return true;
  }

  public Ad update(Ad ad) throws BuddyError {
    Ad re = adDao.update(ad);
    if (re == null) {
      throw new BuddyError("ad update failed");
    }
    return re;
  }

  public List<Ad> getAllByOrg(String status, int orgId) throws BuddyError {
    return adDao.getAllAdByOrg(status.toUpperCase(), orgId);
  }
}
