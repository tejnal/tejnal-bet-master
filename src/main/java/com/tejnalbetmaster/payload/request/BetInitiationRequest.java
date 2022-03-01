package com.tejnalbetmaster.payload.request;


import com.tejnalbetmaster.data.entity.models.EBetMode;
import com.tejnalbetmaster.util.EnumNamePattern;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

public class BetInitiationRequest {

    @EnumNamePattern(regexp = "CASH")
    private EBetMode betMode;

    @Range(min = 1, max = 10)
    @NotNull
    private Double betAmount;


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

    @Override
    public String toString() {
        return "BetInitiationRequest{" +
                "betMode=" + betMode +
                ", betAmount=" + betAmount +
                '}';
    }
}
