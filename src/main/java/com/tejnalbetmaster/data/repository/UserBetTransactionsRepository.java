package com.tejnalbetmaster.data.repository;

import com.tejnalbetmaster.data.entity.UserBetTransactions;
import com.tejnalbetmaster.data.entity.models.EBetMode;
import com.tejnalbetmaster.payload.response.UserBetTransactionDetailsResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBetTransactionsRepository extends JpaRepository<UserBetTransactions, Long> {
  @Query(
      "Select new com.tejnalbetmaster.payload.response.UserBetTransactionDetailsResponse (a.creationDate, a.betMode, a.betAmount as amount, b.prizeDescription as prize) from UserBetTransactions a left join PrizeMap b on a.prizeId=b.id WHERE a.userId = :userId order by a.creationDate DESC")
  List<UserBetTransactionDetailsResponse> fetchAllDetailedTransactions(
      @Param("userId") Long userId);

  @Query(
      "Select new com.tejnalbetmaster.payload.response.UserBetTransactionDetailsResponse(a.creationDate, a.betMode, a.betAmount as amount, b.prizeDescription as prize) from UserBetTransactions a left join PrizeMap b on a.prizeId=b.id WHERE a.betMode = :betMode AND a.userId = :userId order by a.creationDate DESC")
  List<UserBetTransactionDetailsResponse> fetchAllDetailedTransactionsByBetMode(
      @Param("betMode") EBetMode betMode, @Param("userId") Long userId);
}
