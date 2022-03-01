package com.tejnalbetmaster.controller;

import com.tejnalbetmaster.data.entity.models.EBetMode;
import com.tejnalbetmaster.payload.request.BetInitiationRequest;
import com.tejnalbetmaster.payload.response.MessageResponse;
import com.tejnalbetmaster.payload.response.MultipleUserBetDetailsResponse;
import com.tejnalbetmaster.payload.response.UserBetDetailsResponse;
import com.tejnalbetmaster.payload.response.UserBetTransactionResponse;
import com.tejnalbetmaster.security.exception.CustomInputException;
import com.tejnalbetmaster.security.jwt.advice.ErrorMessage;
import com.tejnalbetmaster.service.UserBetTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/bet")
public class BetController {

  @Autowired private UserBetTransactionService userBetTransactionService;

  private Logger logger = LoggerFactory.getLogger(BetController.class);

  @PostMapping("/initiate")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> initiateBet(
      @Valid @RequestBody BetInitiationRequest betInitiationRequest) {
    UserDetails userDetails =
        (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    logger.info("Username is -> {}", userDetails.getUsername());
    String response = null;
    try {
      response =
          userBetTransactionService.saveBetTransactionDetails(
              betInitiationRequest, userDetails.getUsername());
    } catch (CustomInputException ex) {
      ErrorMessage eMessage =
          new ErrorMessage(
              HttpStatus.BAD_REQUEST.value(),
              new Date(),
              "REQUEST VALIDATION FAILED!",
              ex.getMessage());
      return new ResponseEntity<>(eMessage, HttpStatus.BAD_REQUEST);
    }
    return ResponseEntity.ok(new MessageResponse("Bet details saved successfully - " + response));
  }

  @GetMapping("/history")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> initiateBet(
      @Valid @RequestParam(required = false, name = "mode") EBetMode betMode) {
    UserDetails userDetails =
        (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    // logger.info("Fetching history for user -> {}", userDetails.getUsername());
    UserBetTransactionResponse userBetTransactionResponse = null;
    try {
      userBetTransactionResponse =
          userBetTransactionService.fetchAllDetails(betMode, userDetails.getUsername());
    } catch (CustomInputException ex) {
      ErrorMessage eMessage =
          new ErrorMessage(
              HttpStatus.BAD_REQUEST.value(),
              new Date(),
              "REQUEST VALIDATION FAILED!",
              ex.getMessage());
      return new ResponseEntity<ErrorMessage>(eMessage, HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<UserBetTransactionResponse>(
        userBetTransactionResponse, HttpStatus.OK);
  }

  @GetMapping("/check_details")
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<?> fetchDetailsWithBalance(
      @Valid @RequestParam(required = false, name = "include_hist") boolean includeHistory) {
    UserDetails userDetails =
        (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    // logger.info("Fetching history for user -> {}", userDetails.getUsername());
    UserBetDetailsResponse userDetailsResponse;
    try {
      userDetailsResponse =
          userBetTransactionService.fetchDetailsWithBalance(
              includeHistory, userDetails.getUsername());
    } catch (CustomInputException ex) {
      ErrorMessage eMessage =
          new ErrorMessage(
              HttpStatus.BAD_REQUEST.value(),
              new Date(),
              "REQUEST VALIDATION FAILED!",
              ex.getMessage());
      return new ResponseEntity<>(eMessage, HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(userDetailsResponse, HttpStatus.OK);
  }

  @GetMapping("/admin/check_details")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> fetchDetailsWithBalance(
      @Valid @RequestParam(required = true, name = "user_id") Long userId,
      @Valid @RequestParam(required = false, name = "include_hist") boolean includeHistory) {
    UserBetDetailsResponse userDetailsResponse = null;
    try {
      userDetailsResponse =
          userBetTransactionService.fetchDetailsWithBalanceById(includeHistory, userId);
    } catch (CustomInputException ex) {
      ErrorMessage eMessage =
          new ErrorMessage(
              HttpStatus.BAD_REQUEST.value(),
              new Date(),
              "REQUEST VALIDATION FAILED!",
              ex.getMessage());
      return new ResponseEntity<>(eMessage, HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(userDetailsResponse, HttpStatus.OK);
  }

  @GetMapping("/admin/all")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> fetchAll(
      @Valid @RequestParam(required = false, name = "include_hist") boolean includeHistory) {
    MultipleUserBetDetailsResponse multipleUserDetailsResponse = null;
    try {
      multipleUserDetailsResponse = userBetTransactionService.fetchAll(includeHistory);
    } catch (CustomInputException ex) {
      ErrorMessage eMessage =
          new ErrorMessage(
              HttpStatus.BAD_REQUEST.value(),
              new Date(),
              "REQUEST VALIDATION FAILED!",
              ex.getMessage());
      return new ResponseEntity<>(eMessage, HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(multipleUserDetailsResponse, HttpStatus.OK);
  }
}
