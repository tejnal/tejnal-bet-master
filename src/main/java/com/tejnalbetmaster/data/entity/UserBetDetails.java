package com.tejnalbetmaster.data.entity;

import com.tejnalbetmaster.data.entity.models.EBetMode;
import com.tejnalbetmaster.data.entity.models.EPrizeMode;

import javax.persistence.*;
import java.util.Date;
import java.util.Random;

@Entity
@Table(name = "users_bet_details",
    uniqueConstraints = {
      @UniqueConstraint(columnNames = "user_id")
    })
public class UserBetDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "balance")
	private Double balance;

	@Enumerated(EnumType.STRING)
	@Column(name = "latest_mode")
	private EBetMode latestMode;

	@Enumerated(EnumType.STRING)
	@Column(name = "latestPrizeCode")
	private EPrizeMode latestPrizeCode;

	@Column(name = "crtd_dt")
	private Date creationDate;

	@Column(name = "updt_dt")
	private Date updatedDate;

	@Lob
	@Column(name = "prbl_win_obj")
	private Random probabilityForWin;

	@Lob
	@Column(name = "prbl_prize_obj")
	private Random probabilityForPrize;

	@Lob
	@Column(name = "prbl_fr_obj")
	private Random probabilityForFreeRound;

	public UserBetDetails() {
		super();
	}

	public UserBetDetails(Long userId, Double balance, EBetMode latestMode, EPrizeMode latestPrizeCode,
                          Date creationDate) {
		super();
		this.userId = userId;
		this.balance = balance;
		this.latestMode = latestMode;
		this.latestPrizeCode = latestPrizeCode;
		this.creationDate = creationDate;
	}

	public UserBetDetails(Long userId, Double balance, EBetMode latestMode, EPrizeMode latestPrizeCode,
                          Date creationDate, Random probabilityForWin, Random probabilityForPrize, Random probabilityForFreeRound) {
		super();
		this.userId = userId;
		this.balance = balance;
		this.latestMode = latestMode;
		this.latestPrizeCode = latestPrizeCode;
		this.creationDate = creationDate;
		this.probabilityForWin = probabilityForWin;
		this.probabilityForPrize = probabilityForPrize;
		this.probabilityForFreeRound = probabilityForFreeRound;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public EBetMode getLatestMode() {
		return latestMode;
	}

	public void setLatestMode(EBetMode latestMode) {
		this.latestMode = latestMode;
	}

	public EPrizeMode getLatestPrizeCode() {
		return latestPrizeCode;
	}

	public void setLatestPrizeCode(EPrizeMode latestPrizeCode) {
		this.latestPrizeCode = latestPrizeCode;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Random getProbabilityForWin() {
		return probabilityForWin;
	}

	public void setProbabilityForWin(Random probabilityForWin) {
		this.probabilityForWin = probabilityForWin;
	}

	public Random getProbabilityForPrize() {
		return probabilityForPrize;
	}

	public void setProbabilityForPrize(Random probabilityForPrize) {
		this.probabilityForPrize = probabilityForPrize;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Random getProbabilityForFreeRound() {
		return probabilityForFreeRound;
	}

	public void setProbabilityForFreeRound(Random probabilityForFreeRound) {
		this.probabilityForFreeRound = probabilityForFreeRound;
	}

}
