package com.siddharth.plutocracy.service;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.siddharth.plutocracy.dto.request.BalanceModeCreationRequestDto;
import com.siddharth.plutocracy.dto.request.BalanceModeUpdationRequestDto;
import com.siddharth.plutocracy.dto.response.BalanceModeDto;
import com.siddharth.plutocracy.entity.BalanceMode;
import com.siddharth.plutocracy.repository.BalanceModeRepository;
import com.siddharth.plutocracy.repository.TotalBalanceRepository;
import com.siddharth.plutocracy.security.utility.JwtUtils;
import com.siddharth.plutocracy.utils.ResponseUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BalanceModeService {

	private final BalanceModeRepository balanceModeRepository;

	private final TotalBalanceRepository totalBalanceRepository;

	private final JwtUtils jwtUtils;

	private final ResponseUtils responseUtils;

	public ResponseEntity<?> create(final BalanceModeCreationRequestDto balanceModeCreationRequestDto,
			final String token) {
		final var totalBalanceId = jwtUtils.extractTotalBalanceId(token.replace("Bearer ", ""));
		final var balanceMode = new BalanceMode();
		balanceMode.setActive(true);
		balanceMode.setModeType(balanceModeCreationRequestDto.getModeType());
		balanceMode.setName(balanceModeCreationRequestDto.getName());
		balanceMode.setValue(balanceModeCreationRequestDto.getValue());
		balanceMode.setTotalBalanceId(totalBalanceId);

		final var savedBalancemode = balanceModeRepository.save(balanceMode);
		return responseUtils.balanceModeSuccessResponse(savedBalancemode.getId());
	}

	public ResponseEntity<?> update(final BalanceModeUpdationRequestDto balanceModeUpdationRequestDto,
			final String token) {
		final var totalBalanceId = jwtUtils.extractTotalBalanceId(token.replace("Bearer ", ""));
		final var balanceMode = balanceModeRepository.findById(balanceModeUpdationRequestDto.getId()).get();

		if (!balanceMode.getTotalBalanceId().equals(totalBalanceId))
			return responseUtils.unauthorizedResponse();

		balanceMode.setActive(balanceModeUpdationRequestDto.getIsActive());
		balanceMode.setValue(balanceModeUpdationRequestDto.getValue());

		final var savedBalancemode = balanceModeRepository.save(balanceMode);
		return responseUtils.balanceModeSuccessResponse(savedBalancemode.getId());
	}

	public ResponseEntity<?> retreive(final String token) {
		final var totalBalanceId = jwtUtils.extractTotalBalanceId(token.replace("Bearer ", ""));
		final var totalBalance = totalBalanceRepository.findById(totalBalanceId).get();
		return ResponseEntity.ok(totalBalance.getBalanceModes().parallelStream()
				.map(balanceMode -> BalanceModeDto.builder().createdAt(balanceMode.getCreatedAt())
						.id(balanceMode.getId()).isActive(balanceMode.isActive()).modeType(balanceMode.getModeType())
						.name(balanceMode.getName()).updatedAt(balanceMode.getUpdatedAt()).value(balanceMode.getValue())
						.build())
				.collect(Collectors.toList()));
	}

}
