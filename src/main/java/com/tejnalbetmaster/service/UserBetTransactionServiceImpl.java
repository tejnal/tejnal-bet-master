package com.tejnalbetmaster.service;

import com.tejnalbetmaster.data.entity.PrizeMap;
import com.tejnalbetmaster.data.entity.User;
import com.tejnalbetmaster.data.entity.UserBetDetails;
import com.tejnalbetmaster.data.entity.UserBetTransactions;
import com.tejnalbetmaster.data.entity.models.EBetMode;
import com.tejnalbetmaster.data.entity.models.EPrizeMode;
import com.tejnalbetmaster.data.repository.PrizeMapRepository;
import com.tejnalbetmaster.data.repository.UserBetTransactionsRepository;
import com.tejnalbetmaster.data.repository.UserRepository;
import com.tejnalbetmaster.payload.request.BetInitiationRequest;
import com.tejnalbetmaster.payload.response.MultipleUserBetDetailsResponse;
import com.tejnalbetmaster.payload.response.UserBetDetailsResponse;
import com.tejnalbetmaster.payload.response.UserBetTransactionResponse;
import com.tejnalbetmaster.security.exception.CustomInputException;
import com.tejnalbetmaster.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

@Service
public class UserBetTransactionServiceImpl implements UserBetTransactionService {
  private static final Logger logger = LoggerFactory.getLogger(UserBetTransactionServiceImpl.class);

  @Autowired private UserBetTransactionsRepository userBetTransactionsRepository;

  @Autowired private UserBetDetailsService userBetDetailsService;

  @Autowired private UserRepository userRepository;

  @Autowired private PrizeMapRepository prizeMapRepository;

  @Override
  public String saveBetTransactionDetails(
      BetInitiationRequest betInitiationRequest, String userName) throws CustomInputException {
    logger.info("Saving bet Transaction Details.");

    // Fetch User details
    User userDetails = userRepository.findByUsername(userName).get();

    // Fetch User Bet Details for Initial Random object
    UserBetDetails userBetDetails =
        userBetDetailsService.fetchUserBetDetailsByUserId(userDetails.getId());

    // Update Bet Mode in case last prize mode was FR
    if (Utils.isCurrentRoundFree(userBetDetails.getLatestPrizeCode())) {
      betInitiationRequest.setBetMode(EBetMode.FREE);
      logger.info("Bet Mode updated to FREE for current round.");
    }

    // Validate if balance is at least equal to bet amount
    if (!betInitiationRequest.getBetMode().equals(EBetMode.FREE)
        && betInitiationRequest.getBetAmount() > userBetDetails.getBalance()) {
      logger.error("Bet amount can't be greater than balance.");
      throw new CustomInputException(
          "REQUEST VALIDATION FAILED!", "Bet amount can't be greater than balance.");
    }

    // Populate Probability for Win
    Random random = userBetDetails.getProbabilityForWin();
    Random randomPrize = userBetDetails.getProbabilityForPrize();
    Random randomFreeRoundPrize = userBetDetails.getProbabilityForFreeRound();
    double probabilityForWin = random.nextDouble();
    EPrizeMode ePrizeMode = EPrizeMode.NONE;
    Double upliftedBalanceAmount = null;
    if (probabilityForWin <= 0.3) {
      double probabilityForPrize = randomPrize.nextDouble();
      // WIN CASE
      if (probabilityForPrize <= 0.3) {
        ePrizeMode = EPrizeMode.BIG;
        upliftedBalanceAmount = -50 * betInitiationRequest.getBetAmount();
      } else if (probabilityForPrize <= 0.6) {
        ePrizeMode = EPrizeMode.MEDIUM;
        upliftedBalanceAmount = -10 * betInitiationRequest.getBetAmount();
      } else {
        ePrizeMode = EPrizeMode.SMALL;
        upliftedBalanceAmount = -3 * betInitiationRequest.getBetAmount();
      }

      // WIN FOR FR Case
      if (!betInitiationRequest.getBetMode().equals(EBetMode.FREE)) {
        double probabilityForFreeRoundPrize = randomFreeRoundPrize.nextDouble();
        if (probabilityForFreeRoundPrize >= 0.2 && probabilityForFreeRoundPrize < 0.3) {
          switch (ePrizeMode) {
            case BIG:
              ePrizeMode = EPrizeMode.BIG_AND_FR;
              break;
            case MEDIUM:
              ePrizeMode = EPrizeMode.MEDIUM_AND_FR;
              break;
            case SMALL:
              ePrizeMode = EPrizeMode.SMALL_AND_FR;
              break;
            default:
              ePrizeMode = EPrizeMode.FR;
              break;
          }
        }
      }
    }

    logger.info(
        "User name -> {}, Prize Mode -> {}, Probaility for Win -> {}, Won Balance Amount -> {}, Bet Mode -> {}",
        userName,
        ePrizeMode,
        probabilityForWin,
        upliftedBalanceAmount,
        betInitiationRequest.getBetMode());

    // Fetch Prize Map Details
    PrizeMap prizeMap = prizeMapRepository.findByPrizeCode(ePrizeMode).get();

    UserBetTransactions userBetTransactions =
        new UserBetTransactions(
            userDetails.getId(),
            new Date(),
            betInitiationRequest.getBetMode(),
            betInitiationRequest.getBetAmount(),
            prizeMap.getId());

    // If not FREE, decrease the bet amount from total
    if (betInitiationRequest.getBetMode().equals(EBetMode.CASH)) {
      // Update the User Bet details record - CASH
      userBetDetailsService.updateBetDetailsWithBalanceDecrement(
          userDetails.getId(),
          ((null != upliftedBalanceAmount)
              ? upliftedBalanceAmount + betInitiationRequest.getBetAmount()
              : betInitiationRequest.getBetAmount()),
          EBetMode.CASH,
          ePrizeMode);
    } else {
      // Update the User Bet details record - FREE
      userBetDetailsService.updateBetDetailsWithBalanceDecrement(
          userDetails.getId(),
          (null != upliftedBalanceAmount) ? upliftedBalanceAmount : null,
          EBetMode.FREE,
          ePrizeMode);
    }

    userBetTransactionsRepository.save(userBetTransactions);

    return prizeMap.getPrizeDescription();
  }

  @Override
  public UserBetTransactionResponse fetchAllDetails(EBetMode eBetMode, String userName)
      throws CustomInputException {
    UserBetTransactionResponse response = new UserBetTransactionResponse();
    // Fetch User details
    User userDetails = userRepository.findByUsername(userName).get();
    try {
      if (eBetMode != null) {
        // Bet Mode Filtration requested
        response
            .getDetails()
            .addAll(
                userBetTransactionsRepository.fetchAllDetailedTransactionsByBetMode(
                    eBetMode, userDetails.getId()));
      } else {
        // No Filtration provided
        response
            .getDetails()
            .addAll(
                userBetTransactionsRepository.fetchAllDetailedTransactions(userDetails.getId()));
      }
    } catch (NoSuchElementException nec) {
      throw new CustomInputException(
          "NO DATA FOUND!", "no data was found for the given user - " + userName);
    } catch (Exception ex) {
      throw new CustomInputException(
          "TECHNICAL ERROR!",
          "Some unexpected error occured. Reach out to support for further assistance.");
    }

    return response;
  }

  @Override
  public UserBetDetailsResponse fetchDetailsWithBalance(boolean isHistoryRequired, String userName)
      throws CustomInputException {
    UserBetDetailsResponse response = new UserBetDetailsResponse();
    try {
      // Fetch User details
      User userDetails = userRepository.findByUsername(userName).get();

      // Fetch User Bet Details for balance
      UserBetDetails userBetDetails =
          userBetDetailsService.fetchUserBetDetailsByUserId(userDetails.getId());
      if (isHistoryRequired) {
        response
            .getDetails()
            .addAll(
                userBetTransactionsRepository.fetchAllDetailedTransactions(userDetails.getId()));
      }
      response.setBalance(userBetDetails.getBalance());
      response.setCreatedDate(userBetDetails.getCreationDate());
      response.setId(userBetDetails.getUserId());
    } catch (NoSuchElementException nec) {
      throw new CustomInputException(
          "NO DATA FOUND!", "no data was found for the given user - " + userName);
    } catch (Exception ex) {
      throw new CustomInputException(
          "TECHNICAL ERROR!",
          "Some unexpected error occured. Reach out to support for further assistance.");
    }
    return response;
  }

  @Override
  public UserBetDetailsResponse fetchDetailsWithBalanceById(boolean isHistoryRequired, long userId)
      throws CustomInputException {
    UserBetDetailsResponse response = new UserBetDetailsResponse();

    // Fetch User Bet Details for balance
    try {
      UserBetDetails userBetDetails = userBetDetailsService.fetchUserBetDetailsByUserId(userId);

      if (isHistoryRequired) {
        response
            .getDetails()
            .addAll(userBetTransactionsRepository.fetchAllDetailedTransactions(userId));
      }
      response.setBalance(userBetDetails.getBalance());
      response.setCreatedDate(userBetDetails.getCreationDate());
      response.setId(userBetDetails.getUserId());
    } catch (NoSuchElementException nec) {
      throw new CustomInputException(
          "NO DATA FOUND!", "no data was found for the given user ID - " + userId);
    } catch (Exception ex) {
      throw new CustomInputException(
          "TECHNICAL ERROR!",
          "Some unexpected error occured. Reach out to support for further assistance.");
    }

    return response;
  }

  @Override
  public MultipleUserBetDetailsResponse fetchAll(boolean isHistoryRequired)
      throws CustomInputException {
    MultipleUserBetDetailsResponse finalResponse = new MultipleUserBetDetailsResponse();

    // Fetch User Bet Details for balance
    try {
      List<UserBetDetails> userBetDetails = userBetDetailsService.fetchAllUserBetDetails();
      for (UserBetDetails eachUserBetDetails : userBetDetails) {
        UserBetDetailsResponse response = new UserBetDetailsResponse();
        if (isHistoryRequired) {
          response
              .getDetails()
              .addAll(
                  userBetTransactionsRepository.fetchAllDetailedTransactions(
                      eachUserBetDetails.getUserId()));
        }
        response.setBalance(eachUserBetDetails.getBalance());
        response.setCreatedDate(eachUserBetDetails.getCreationDate());
        response.setId(eachUserBetDetails.getUserId());
        finalResponse.getDetails().add(response);
      }

    } catch (NoSuchElementException nec) {
      throw new CustomInputException("NO DATA FOUND!", "no data was found.");
    } catch (Exception ex) {
      throw new CustomInputException(
          "TECHNICAL ERROR!",
          "Some unexpected error occured. Reach out to support for further assistance.");
    }

    return finalResponse;
  }
}
