package com.tejnalbetmaster.data.repository;


import com.tejnalbetmaster.data.entity.UserBetDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserBetDetailsRepository extends JpaRepository<UserBetDetails, Long> {
	Optional<UserBetDetails> findByUserId(Long userId);
}
