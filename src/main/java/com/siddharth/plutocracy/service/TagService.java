package com.siddharth.plutocracy.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.siddharth.plutocracy.constant.ContextType;
import com.siddharth.plutocracy.dto.response.TagRetreivalRequestDto;
import com.siddharth.plutocracy.entity.Tag;
import com.siddharth.plutocracy.repository.NoteTagMappingRepository;
import com.siddharth.plutocracy.repository.TagRepository;
import com.siddharth.plutocracy.repository.TicketTagMappingRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TagService {

	private final TagRepository tagRepository;

	private final NoteTagMappingRepository noteTagMappingRepository;

	private final TicketTagMappingRepository ticketTagMappingRepository;

	public List<Tag> retreiveAll() {
		return tagRepository.findAll();
	}

	public ResponseEntity<List<String>> retreive(final TagRetreivalRequestDto tagRetreivalRequest, final String token) {
		if (tagRetreivalRequest.getContextType().equalsIgnoreCase(ContextType.NOTE.getName())) {
			return ResponseEntity
					.ok(noteTagMappingRepository.findByNoteId(tagRetreivalRequest.getContextId()).parallelStream()
							.map(noteTagMapping -> noteTagMapping.getTag().getName()).collect(Collectors.toList()));
		}
		if (tagRetreivalRequest.getContextType().equalsIgnoreCase(ContextType.COMPLETED_TICKET.getName())
				|| tagRetreivalRequest.getContextType().equalsIgnoreCase(ContextType.FUTURE_TICKET.getName()))
			return ResponseEntity
					.ok(ticketTagMappingRepository.findByTicketId(tagRetreivalRequest.getContextId()).parallelStream()
							.map(ticketTagMapping -> ticketTagMapping.getTag().getName()).collect(Collectors.toList()));
		return ResponseEntity.badRequest().build();

	}

}
