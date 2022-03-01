package com.tejnalbetmaster.data.entity;

import com.tejnalbetmaster.data.entity.models.EBetMode;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users_bet_trans")
public class UserBetTransactions {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "crtd_dt")
	private Date creationDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "mode")
	private EBetMode betMode;

	@Column(name = "bet_am")
	private Double betAmount;

	@Column(name = "prize_id")
	private Long prizeId;

	public UserBetTransactions() {
		super();
	}

	public UserBetTransactions(Long userId, Date creationDate, EBetMode betMode, Double betAmount, Long prizeId) {
		super();
		this.userId = userId;
		this.creationDate = creationDate;
		this.betMode = betMode;
		this.betAmount = betAmount;
		this.prizeId = prizeId;
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

	public Long getPrizeId() {
		return prizeId;
	}

	public void setPrizeId(Long prizeId) {
		this.prizeId = prizeId;
	}

	public Double getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(Double betAmount) {
		this.betAmount = betAmount;
	}

}
