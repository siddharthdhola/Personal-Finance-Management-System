package com.siddharth.plutocracy.service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.siddharth.plutocracy.constant.TicketType;
import com.siddharth.plutocracy.dto.response.SpendingThresholdRecordDto;
import com.siddharth.plutocracy.entity.CompletedTicket;
import com.siddharth.plutocracy.entity.SpendingThresholdRecord;
import com.siddharth.plutocracy.repository.CompletedTicketRepository;
import com.siddharth.plutocracy.repository.CurrentMonthlySpendingThresholdLimitRepository;
import com.siddharth.plutocracy.repository.SpendingThresholdRecordRepository;
import com.siddharth.plutocracy.repository.UserRepository;
import com.siddharth.plutocracy.security.utility.JwtUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SpendingThresholdRecordService {

	private final SpendingThresholdRecordRepository spendingThresholdRecordRepository;
	private final CurrentMonthlySpendingThresholdLimitRepository currentMonthSpendingRepository;
	private final CompletedTicketRepository completedTicketRepository;
	private final UserRepository userRepository;
	private final JwtUtils jwtUtils;

	public ResponseEntity<List<SpendingThresholdRecordDto>> retreivePastRecords(final String token) {
		return ResponseEntity.ok(spendingThresholdRecordRepository
				.findByUserId(jwtUtils.extractUserId(token.replace("Bearer ", ""))).parallelStream()
				.map(pastSpendingRecord -> SpendingThresholdRecordDto.builder().id(pastSpendingRecord.getId())
						.limitValue(pastSpendingRecord.getLimitValue()).month(pastSpendingRecord.getMonth())
						.valueSpent(pastSpendingRecord.getValueSpent()).year(pastSpendingRecord.getYear()).build())
				.collect(Collectors.toList()));
	}

	public void calulate() {
		userRepository.findAll().forEach(user -> {
			final var currentMonthSpending = currentMonthSpendingRepository.findByUserId(user.getId());
			final var previousMonthDate = LocalDate.now().minusDays(1);

			if (currentMonthSpending.isPresent() && currentMonthSpending.get().getIsActive()) {
				final var spendingThresholdRecord = new SpendingThresholdRecord();
				spendingThresholdRecord.setLimitValue(currentMonthSpending.get().getLimitValue());
				spendingThresholdRecord.setMonth(Month.of(previousMonthDate.getMonthValue()).toString());
				spendingThresholdRecord.setUserId(user.getId());
				spendingThresholdRecord.setYear(String.valueOf(previousMonthDate.getYear()));

				Double totalMonthExpense = 0.0;

				final var monthlyExpenseTicketList = completedTicketRepository
						.findByUserIdAndCreatedAtBetween(user.getId(),
								LocalDate.of(previousMonthDate.getYear(), previousMonthDate.getMonthValue(), 1),
								previousMonthDate)
						.parallelStream().filter(completedTicket -> completedTicket.getTicketType()
								.equalsIgnoreCase(TicketType.EXPENSE.getType()))
						.collect(Collectors.toList());
				;

				for (CompletedTicket completedTicket : monthlyExpenseTicketList) {
					totalMonthExpense += completedTicket.getValue();
				}

				spendingThresholdRecord.setValueSpent(totalMonthExpense);
				spendingThresholdRecordRepository.save(spendingThresholdRecord);
			}

		});
	}

}
