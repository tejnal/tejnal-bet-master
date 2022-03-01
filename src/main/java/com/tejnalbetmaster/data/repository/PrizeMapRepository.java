package com.tejnalbetmaster.data.repository;

import com.tejnalbetmaster.data.entity.PrizeMap;
import com.tejnalbetmaster.data.entity.models.EPrizeMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrizeMapRepository extends JpaRepository<PrizeMap, Long> {
	Optional<PrizeMap> findByPrizeCode(EPrizeMode ePrizeMode);
}
