package com.tejnalbetmaster.service;

import com.tejnalbetmaster.data.entity.models.EBetMode;
import com.tejnalbetmaster.payload.request.BetInitiationRequest;
import com.tejnalbetmaster.payload.response.MultipleUserBetDetailsResponse;
import com.tejnalbetmaster.payload.response.UserBetDetailsResponse;
import com.tejnalbetmaster.payload.response.UserBetTransactionResponse;
import com.tejnalbetmaster.security.exception.CustomInputException;

public interface UserBetTransactionService {

  String saveBetTransactionDetails(BetInitiationRequest betInitiationRequest, String userName)
      throws CustomInputException;

  UserBetTransactionResponse fetchAllDetails(EBetMode eBetMode, String userName)
      throws CustomInputException;

  UserBetDetailsResponse fetchDetailsWithBalance(boolean isHistoryRequired, String userName)
      throws CustomInputException;

  UserBetDetailsResponse fetchDetailsWithBalanceById(boolean isHistoryRequired, long userId)
      throws CustomInputException;

  MultipleUserBetDetailsResponse fetchAll(boolean isHistoryRequired) throws CustomInputException;
}
