package com.siddharth.plutocracy.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.siddharth.plutocracy.entity.BalanceMode;

@Repository
public interface BalanceModeRepository extends JpaRepository<BalanceMode, UUID> {

}
