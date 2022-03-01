package com.tejnalbetmaster.service;

import com.tejnalbetmaster.config.SpringTestConfig;
import com.tejnalbetmaster.data.entity.UserBetTransactions;
import com.tejnalbetmaster.data.entity.models.EBetMode;
import com.tejnalbetmaster.data.entity.models.EPrizeMode;
import com.tejnalbetmaster.data.repository.PrizeMapRepository;
import com.tejnalbetmaster.data.repository.UserBetTransactionsRepository;
import com.tejnalbetmaster.data.repository.UserRepository;
import com.tejnalbetmaster.payload.request.BetInitiationRequest;
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
@PrepareForTest({UserBetTransactionServiceImpl.class})
@PowerMockRunnerDelegate(SpringRunner.class)
@Import(SpringTestConfig.class)
@SuppressStaticInitializationFor("com.na.bet.master.config.SpringTestConfig")
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "org.w3c.*", "com.sun.org.apache.xalan.*"})
public class UserBetTransactionServiceTests {
  UserBetTransactionServiceImpl userBetTransactionService = new UserBetTransactionServiceImpl();

  @Mock UserBetTransactionsRepository userBetTransactionsRepository;

  @Mock PrizeMapRepository prizeMapRepository;

  @Mock UserRepository userRepository;

  @Mock UserBetDetailsServiceImpl userBetDetailsService;

  @Before
  public void setup() {
    ReflectionTestUtils.setField(
        userBetTransactionService, "userBetTransactionsRepository", userBetTransactionsRepository);
    ReflectionTestUtils.setField(
        userBetTransactionService, "userBetDetailsService", userBetDetailsService);
    ReflectionTestUtils.setField(
        userBetTransactionService, "prizeMapRepository", prizeMapRepository);
    ReflectionTestUtils.setField(userBetTransactionService, "userRepository", userRepository);
    Mockito.when(userRepository.findByUsername("JUNIT_USR"))
        .thenReturn(TestDataBuilderUtil.getDummyJunitUser());
    Mockito.when(userBetDetailsService.fetchUserBetDetailsByUserId(0L))
        .thenReturn(TestDataBuilderUtil.getDummyUserbetDetails());
    Mockito.when(prizeMapRepository.findByPrizeCode(EPrizeMode.NONE))
        .thenReturn(TestDataBuilderUtil.getDummyNonePrizeMap());
    Mockito.when(prizeMapRepository.findByPrizeCode(EPrizeMode.FR))
        .thenReturn(TestDataBuilderUtil.getDummyNonePrizeMap());
    Mockito.when(prizeMapRepository.findByPrizeCode(EPrizeMode.BIG_AND_FR))
        .thenReturn(TestDataBuilderUtil.getDummyNonePrizeMap());
    Mockito.when(prizeMapRepository.findByPrizeCode(EPrizeMode.MEDIUM_AND_FR))
        .thenReturn(TestDataBuilderUtil.getDummyNonePrizeMap());
    Mockito.when(prizeMapRepository.findByPrizeCode(EPrizeMode.SMALL_AND_FR))
        .thenReturn(TestDataBuilderUtil.getDummyNonePrizeMap());
    Mockito.when(prizeMapRepository.findByPrizeCode(EPrizeMode.BIG))
        .thenReturn(TestDataBuilderUtil.getDummyNonePrizeMap());
    Mockito.when(prizeMapRepository.findByPrizeCode(EPrizeMode.SMALL))
        .thenReturn(TestDataBuilderUtil.getDummyNonePrizeMap());
  }

  @Test
  public void test_save_bet_transaction_details() throws Exception {
    UserBetTransactions userBetTransactions = TestDataBuilderUtil.getDummyuserBetTransactions();
    PowerMockito.whenNew(UserBetTransactions.class)
        .withAnyArguments()
        .thenReturn(userBetTransactions);
    BetInitiationRequest betInitiationRequest = new BetInitiationRequest();
    betInitiationRequest.setBetAmount(1.0);
    betInitiationRequest.setBetMode(EBetMode.FREE);
    userBetTransactionService.saveBetTransactionDetails(betInitiationRequest, "JUNIT_USR");
    verify(userBetDetailsService, Mockito.times(1))
        .updateBetDetailsWithBalanceDecrement(
            Mockito.anyLong(), Mockito.any(), Mockito.any(), Mockito.any());
    verify(userBetTransactionsRepository, Mockito.times(1)).save(null);
  }

  /*@Test
  public void test_fetch_details_with_balance_by_id() throws Exception {
  	UserBetDetailsResponse userBetDetailsResponse = userBetTransactionService.fetchDetailsWithBalanceById(false, 0L);
  	assert(userBetDetailsResponse.getBalance().equals(20.0));
  }*/
}
