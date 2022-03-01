package com.tejnalbetmaster.service;

import com.tejnalbetmaster.data.entity.UserBetDetails;
import com.tejnalbetmaster.data.entity.models.EBetMode;
import com.tejnalbetmaster.data.entity.models.EPrizeMode;
import com.tejnalbetmaster.data.repository.UserBetDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class UserBetDetailsServiceImpl implements UserBetDetailsService {
  private static final Logger logger = LoggerFactory.getLogger(UserBetDetailsServiceImpl.class);

  @Value("${bet.app.initial.deposit.amount}")
  private Double initialBetAmount;

  @Autowired private UserBetDetailsRepository userBetDetailsRepository;

  @Override
  public void saveInitialBetDetails(Long userId) {
    UserBetDetails userBetDetails =
        new UserBetDetails(
            userId,
            initialBetAmount,
            null,
            null,
            new Date(),
            new Random(),
            new Random(),
            new Random());
    userBetDetailsRepository.save(userBetDetails);
  }

  @Override
  public void updateBetDetailsWithBalanceDecrement(
      Long userId, Double betAmount, EBetMode latestMode, EPrizeMode latestPrizeCode) {

    UserBetDetails userBetDetails = userBetDetailsRepository.findByUserId(userId).get();
    boolean isValueChanged = false;
    if (null != betAmount) {
      userBetDetails.setBalance(userBetDetails.getBalance() - betAmount);
      isValueChanged = true;
    }
    if (null != latestMode) {
      userBetDetails.setLatestMode(latestMode);
      isValueChanged = true;
    }

    if (null != latestPrizeCode) {
      userBetDetails.setLatestPrizeCode(latestPrizeCode);
      isValueChanged = true;
    }

    if (isValueChanged) {
      userBetDetailsRepository.save(userBetDetails);
    } else {
      logger.warn("No values was changed, and update primary Bet Details was called.");
    }
  }

  @Override
  public UserBetDetails fetchUserBetDetailsByUserId(Long userId) {
    return userBetDetailsRepository.findByUserId(userId).get();
  }

  @Override
  public List<UserBetDetails> fetchAllUserBetDetails() {
    return userBetDetailsRepository.findAll();
  }
}
