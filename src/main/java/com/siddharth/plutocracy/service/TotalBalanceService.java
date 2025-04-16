package com.siddharth.plutocracy.service;

import org.springframework.stereotype.Service;

import com.siddharth.plutocracy.repository.TotalBalanceRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TotalBalanceService {

	private final TotalBalanceRepository totalBalanceRepository;

}
