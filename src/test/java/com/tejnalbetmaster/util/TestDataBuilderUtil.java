package com.tejnalbetmaster.util;

import com.tejnalbetmaster.data.entity.PrizeMap;
import com.tejnalbetmaster.data.entity.User;
import com.tejnalbetmaster.data.entity.UserBetDetails;
import com.tejnalbetmaster.data.entity.UserBetTransactions;
import com.tejnalbetmaster.data.entity.models.EBetMode;
import com.tejnalbetmaster.data.entity.models.EPrizeMode;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

public class TestDataBuilderUtil {
  public static UserBetDetails getDummyUserbetDetails() {
    UserBetDetails userBetDetails =
        new UserBetDetails(
            0L, 20.0, null, null, new Date(), new Random(), new Random(), new Random());
    return userBetDetails;
  }

  public static Optional<UserBetDetails> getDummyFetchedUserbetDetails1() {
    UserBetDetails userBetDetails =
        new UserBetDetails(
            0L, 20.0, null, null, new Date(), new Random(), new Random(), new Random());
    return Optional.ofNullable(userBetDetails);
  }

  public static Optional<User> getDummyJunitUser() {
    User user = new User("JUNIT_USR", "JUNIT_USR@DUMMY.COM", "JUNIT_PWD");
    user.setId(0L);
    return Optional.ofNullable(user);
  }

  public static Optional<PrizeMap> getDummyNonePrizeMap() {
    PrizeMap prizeMap = new PrizeMap("Dummy prize", EPrizeMode.NONE);
    return Optional.ofNullable(prizeMap);
  }

  public static UserBetTransactions getDummyuserBetTransactions() {
    UserBetTransactions userBetTransactions =
        new UserBetTransactions(0L, new Date(), EBetMode.CASH, 2.0, 0L);
    return userBetTransactions;
  }
}
