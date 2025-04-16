package com.siddharth.plutocracy.service;

import org.springframework.stereotype.Service;

import com.siddharth.plutocracy.repository.TicketTagMappingRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TicketTagMappingService {

	private final TicketTagMappingRepository ticketTagMappingRepository;

}
