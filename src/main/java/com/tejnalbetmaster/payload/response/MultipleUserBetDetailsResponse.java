package com.tejnalbetmaster.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class MultipleUserBetDetailsResponse {
  @JsonProperty("details")
  private List<UserBetDetailsResponse> details;

  public List<UserBetDetailsResponse> getDetails() {
    if (null == details) details = new ArrayList<UserBetDetailsResponse>();
    return details;
  }

  public void setDetails(List<UserBetDetailsResponse> details) {
    this.details = details;
  }

	@Override
	public String toString() {
		return "MultipleUserBetDetailsResponse{" +
				"details=" + details +
				'}';
	}
}
