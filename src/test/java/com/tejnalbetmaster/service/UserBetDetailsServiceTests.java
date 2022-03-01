package com.tejnalbetmaster.service;

import com.tejnalbetmaster.config.SpringTestConfig;
import com.tejnalbetmaster.data.entity.UserBetDetails;
import com.tejnalbetmaster.data.entity.models.EBetMode;
import com.tejnalbetmaster.data.entity.models.EPrizeMode;
import com.tejnalbetmaster.data.repository.UserBetDetailsRepository;
import com.tejnalbetmaster.util.TestDataBuilderUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest({UserBetDetailsServiceImpl.class})
@PowerMockRunnerDelegate(SpringRunner.class)
@Import(SpringTestConfig.class)
@SuppressStaticInitializationFor("com.na.bet.master.config.SpringTestConfig")
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*", "com.sun.org.apache.xalan.*"})
public class UserBetDetailsServiceTests {

  UserBetDetailsServiceImpl userBetDetailsService = new UserBetDetailsServiceImpl();

  @Mock UserBetDetailsRepository userBetDetailsRepository;

  @Before
  public void setup() {
    ReflectionTestUtils.setField(
        userBetDetailsService, "userBetDetailsRepository", userBetDetailsRepository);
    Mockito.when(userBetDetailsRepository.findByUserId(0L))
        .thenReturn(TestDataBuilderUtil.getDummyFetchedUserbetDetails1());
  }

  @Test
  public void test_save_initial_bet_details() throws Exception {
    UserBetDetails userBetDetails = TestDataBuilderUtil.getDummyUserbetDetails();
    PowerMockito.whenNew(UserBetDetails.class).withAnyArguments().thenReturn(userBetDetails);
    userBetDetailsService.saveInitialBetDetails(1L);
    verify(userBetDetailsRepository, Mockito.times(1)).save(userBetDetails);
  }

  @Test
  public void test_update_details_with_balance_bet() throws Exception {
    UserBetDetails userBetDetails = TestDataBuilderUtil.getDummyUserbetDetails();
    PowerMockito.whenNew(UserBetDetails.class).withAnyArguments().thenReturn(userBetDetails);
    userBetDetailsService.updateBetDetailsWithBalanceDecrement(
        0L, 10.0, EBetMode.CASH, EPrizeMode.BIG);
    verify(userBetDetailsRepository, Mockito.times(1)).findByUserId(0L);
  }

  @Test
  public void test_fetch_user_details() throws Exception {
    userBetDetailsService.fetchUserBetDetailsByUserId(0L);
    verify(userBetDetailsRepository, Mockito.times(1)).findByUserId(0L);
  }
}
