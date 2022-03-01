package com.tejnalbetmaster.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserBetDetailsResponse {
  @JsonProperty("id")
  private long id;

  @JsonProperty("balance")
  private Double balance;

  @JsonProperty("creation_date")
  private Date createdDate;

  @JsonProperty("history_details")
  private List<UserBetTransactionDetailsResponse> details;

  public List<UserBetTransactionDetailsResponse> getDetails() {
    if (null == details) {
      details = new ArrayList<UserBetTransactionDetailsResponse>();
    }
    return details;
  }

  public void setDetails(List<UserBetTransactionDetailsResponse> details) {
    this.details = details;
  }

  public Double getBalance() {
    return balance;
  }

  public void setBalance(Double balance) {
    this.balance = balance;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "UserBetDetailsResponse{"
        + "id="
        + id
        + ", balance="
        + balance
        + ", createdDate="
        + createdDate
        + ", details="
        + details
        + '}';
  }
}
