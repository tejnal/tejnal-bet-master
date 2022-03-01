package com.tejnalbetmaster.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class UserBetTransactionResponse {

  @JsonProperty("details")
  private List<UserBetTransactionDetailsResponse> details;

  public List<UserBetTransactionDetailsResponse> getDetails() {
    if (null == details) {
      details = new ArrayList<>();
    }
    return details;
  }

  public void setDetails(List<UserBetTransactionDetailsResponse> details) {
    this.details = details;
  }

  @Override
  public String toString() {
    return "UserBetTransactionResponse{" + "details=" + details + '}';
  }
}
