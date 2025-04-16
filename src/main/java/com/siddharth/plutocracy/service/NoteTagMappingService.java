package com.siddharth.plutocracy.service;

import org.springframework.stereotype.Service;

import com.siddharth.plutocracy.repository.NoteTagMappingRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NoteTagMappingService {

	private final NoteTagMappingRepository noteTagMappingRepository;

}
