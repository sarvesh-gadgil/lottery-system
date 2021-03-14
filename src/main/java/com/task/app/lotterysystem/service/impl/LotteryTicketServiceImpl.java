package com.task.app.lotterysystem.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.task.app.lotterysystem.exception.BadRequestException;
import com.task.app.lotterysystem.exception.InternalServerError;
import com.task.app.lotterysystem.model.LotteryTicket;
import com.task.app.lotterysystem.model.LotteryTicketLine;
import com.task.app.lotterysystem.repository.LotteryTicketLineRepository;
import com.task.app.lotterysystem.repository.LotteryTicketRepository;
import com.task.app.lotterysystem.request.LotteryTicketRequest;
import com.task.app.lotterysystem.service.LotteryTicketService;

@Service
public class LotteryTicketServiceImpl implements LotteryTicketService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LotteryTicketServiceImpl.class.getName());

	@Autowired
	private LotteryTicketRepository lotteryTicketRepository;

	@Autowired
	private LotteryTicketLineRepository lineRepository;

	@Override
	@Transactional
	public LotteryTicket createLotteryTicket(LotteryTicketRequest lotteryTicketRequest) {

		// Checking the input params
		if (lotteryTicketRequest == null || lotteryTicketRequest.getNumberOfLines() <= 0) {
			throw new BadRequestException("Invalid input parameters found");
		}

		try {
			LotteryTicket lotteryTicket = lotteryTicketRepository.save(new LotteryTicket());
			List<LotteryTicketLine> linesList = createTicketLines(lotteryTicketRequest.getNumberOfLines(),
					lotteryTicket);
			lineRepository.saveAll(linesList);
			lotteryTicket.setLotteryTicketLine(linesList);
			return lotteryTicket;
		} catch (Exception exception) {
			LOGGER.error("Something went wrong in createLotteryTicket" + exception);
			throw new InternalServerError("Something went wrong in creating lottery ticket");
		}
	}

	@Override
	public List<LotteryTicket> getAllLotteryTickets() {
		List<LotteryTicket> ticket = null;
		try {
			ticket = lotteryTicketRepository.findAllByIsCancelled(false);
		} catch (Exception exception) {
			LOGGER.error("Something went wrong in getAllLotteryTickets" + exception);
			throw new InternalServerError("Something went wrong in getting all lottery tickets");
		}

		if (ticket == null || ticket.size() == 0) {
			throw new InternalServerError("No tickets found");
		}
		return ticket;
	}

	@Override
	public LotteryTicket getLotteryTicket(int ticketId) {
		LotteryTicket ticket = null;
		try {
			ticket = lotteryTicketRepository.findByTicketIdAndIsCancelled(ticketId, false);
		} catch (Exception exception) {
			LOGGER.error("Something went wrong in getLotteryTicket" + exception);
			throw new InternalServerError("Something went wrong in getting the lottery ticket");
		}

		if (ticket == null) {
			throw new InternalServerError("This ticket id does not exists");
		}
		return ticket;
	}

	@Override
	public LotteryTicket retrieveLotteryTicketStatus(int ticketId) {
		LotteryTicket ticket = getLotteryTicket(ticketId);
		if (ticket.isTicketStatusChecked()) {
			throw new InternalServerError("This ticket is already checked");
		} else {
			ticket.setTicketStatusChecked(true);
			return lotteryTicketRepository.save(ticket);
		}
	}

	@Override
	public LotteryTicket cancelLotteryTicket(int ticketId) {
		LotteryTicket ticket = getLotteryTicket(ticketId);
		if (ticket.isTicketStatusChecked()) {
			throw new InternalServerError("This ticket is already checked and cannot be cancelled");
		} else {
			ticket.setCancelled(true);
			return lotteryTicketRepository.save(ticket);
		}
	}

	@Override
	@Transactional
	public LotteryTicket ammendLotteryTicket(int ticketId, LotteryTicketRequest lotteryTicketRequest) {
		LotteryTicket ticket = getLotteryTicket(ticketId);
		if (ticket.isTicketStatusChecked()) {
			throw new InternalServerError("This ticket is already checked and cannot be ammended anymore");
		} else {
			List<LotteryTicketLine> additionalLinesList = createTicketLines(lotteryTicketRequest.getNumberOfLines(),
					ticket);
			lineRepository.saveAll(additionalLinesList);
			return ticket;
		}
	}

	// ------------------- Helper methods ----------------------
	private List<LotteryTicketLine> createTicketLines(int lines, LotteryTicket ticket) {
		List<LotteryTicketLine> linesList = new ArrayList<>();
		for (int linesItr = 1; linesItr <= lines; linesItr++) {
			int first = generateRandomNumber();
			int second = generateRandomNumber();
			int third = generateRandomNumber();
			LotteryTicketLine line = new LotteryTicketLine(first, second, third, getOutcome(first, second, third),
					ticket);
			linesList.add(line);
		}
		return linesList;
	}

	private int generateRandomNumber() {
		return new SplittableRandom().nextInt(0, 3);
	}

	private int getOutcome(int first, int second, int third) {
		if (first + second + third == 2) {
			return 10;
		} else if (first == second && first == third && second == third) {
			return 5;
		} else if (first != second && first != third) {
			return 1;
		} else {
			return 0;
		}
	}
}
