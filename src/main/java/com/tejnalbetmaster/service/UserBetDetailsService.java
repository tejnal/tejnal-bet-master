package com.tejnalbetmaster.service;

import com.tejnalbetmaster.data.entity.UserBetDetails;
import com.tejnalbetmaster.data.entity.models.EBetMode;
import com.tejnalbetmaster.data.entity.models.EPrizeMode;

import java.util.List;

public interface UserBetDetailsService {

  void saveInitialBetDetails(Long userId);

  void updateBetDetailsWithBalanceDecrement(
      Long userId, Double betAmount, EBetMode latestMode, EPrizeMode latestPrizeCode);

  UserBetDetails fetchUserBetDetailsByUserId(Long userId);

  List<com.tejnalbetmaster.data.entity.UserBetDetails> fetchAllUserBetDetails();
}
