package com.tejnalbetmaster.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tejnalbetmaster.data.entity.models.EBetMode;

import java.util.Date;

public class UserBetTransactionDetailsResponse {
  @JsonProperty("crtd_dt")
  private Date creationDate;

  @JsonProperty("mode")
  private EBetMode betMode;

  @JsonProperty("amount")
  private Double betAmount;

  @JsonProperty("prize")
  private String prize;

  public UserBetTransactionDetailsResponse() {
    super();
  }

  public UserBetTransactionDetailsResponse(
      Date creationDate, EBetMode betMode, Double betAmount, String prize) {
    super();
    this.creationDate = creationDate;
    this.betMode = betMode;
    this.betAmount = betAmount;
    this.prize = prize;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public EBetMode getBetMode() {
    return betMode;
  }

  public void setBetMode(EBetMode betMode) {
    this.betMode = betMode;
  }

  public Double getBetAmount() {
    return betAmount;
  }

  public void setBetAmount(Double betAmount) {
    this.betAmount = betAmount;
  }

  public String getPrize() {
    return prize;
  }

  public void setPrize(String prize) {
    this.prize = prize;
  }

  @Override
  public String toString() {
    return "UserBetTransactionDetailsResponse{"
        + "creationDate="
        + creationDate
        + ", betMode="
        + betMode
        + ", betAmount="
        + betAmount
        + ", prize='"
        + prize
        + '\''
        + '}';
  }
}
