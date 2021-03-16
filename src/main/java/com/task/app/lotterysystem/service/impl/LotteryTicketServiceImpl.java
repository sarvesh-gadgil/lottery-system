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
		// Checking the input parameter validity
		checkLotteryTicketRequest(lotteryTicketRequest);
		try {
			// Create a lottery ticket
			LotteryTicket lotteryTicket = lotteryTicketRepository.save(new LotteryTicket());

			// Create lines per ticket
			List<LotteryTicketLine> linesList = createTicketLines(lotteryTicketRequest.getNumberOfLines(),
					lotteryTicket);
			lineRepository.saveAll(linesList);
			lotteryTicket.setLotteryTicketLine(linesList);

			// Return ticket response
			return lotteryTicket;
		} catch (Exception exception) {
			LOGGER.error("Something went wrong in createLotteryTicket" + exception);
			throw new InternalServerError("Something went wrong in creating lottery ticket");
		}
	}

	@Override
	public List<LotteryTicket> getAllLotteryTickets() {
		// Getting the tickets list
		List<LotteryTicket> ticket = null;
		try {
			ticket = lotteryTicketRepository.findAllByIsCancelled(false);
		} catch (Exception exception) {
			LOGGER.error("Something went wrong in getAllLotteryTickets" + exception);
			throw new InternalServerError("Something went wrong in getting all lottery tickets");
		}

		// Check if ticket list is empty
		if (ticket == null || ticket.size() == 0) {
			throw new InternalServerError("No tickets found");
		}

		// Return tickets list response
		return ticket;
	}

	@Override
	public LotteryTicket getLotteryTicket(int ticketId) {
		// Getting the ticket object as per ticket id
		LotteryTicket ticket = null;
		try {
			ticket = lotteryTicketRepository.findByTicketIdAndIsCancelled(ticketId, false);
		} catch (Exception exception) {
			LOGGER.error("Something went wrong in getLotteryTicket" + exception);
			throw new InternalServerError("Something went wrong in getting the lottery ticket");
		}

		// Check if ticket object is empty
		if (ticket == null) {
			throw new InternalServerError("This ticket id does not exists");
		}

		// Return ticket response
		return ticket;
	}

	@Override
	public LotteryTicket retrieveLotteryTicketStatus(int ticketId) {
		// Get the ticket object
		LotteryTicket ticket = getLotteryTicket(ticketId);

		// Check if ticket status is checked or not
		if (ticket.isTicketStatusChecked()) {
			throw new InternalServerError("This ticket is already checked");
		} else {
			// Update the ticket object
			ticket.setTicketStatusChecked(true);
			try {
				// Save and return ticket response
				return lotteryTicketRepository.save(ticket);
			} catch (Exception exception) {
				LOGGER.error("Something went wrong in retrieveLotteryTicketStatus" + exception);
				throw new InternalServerError("Something went wrong in retrieving the lottery ticket status");
			}
		}
	}

	@Override
	public LotteryTicket cancelLotteryTicket(int ticketId) {
		// Get the ticket object
		LotteryTicket ticket = getLotteryTicket(ticketId);

		// Check if ticket status is checked or not
		if (ticket.isTicketStatusChecked()) {
			throw new InternalServerError("This ticket is already checked and cannot be cancelled");
		} else {
			// Update the ticket object
			ticket.setCancelled(true);
			try {
				// Save and return ticket response
				return lotteryTicketRepository.save(ticket);
			} catch (Exception exception) {
				LOGGER.error("Something went wrong in cancelLotteryTicket" + exception);
				throw new InternalServerError("Something went wrong in cancelling the lottery ticket");
			}
		}
	}

	@Override
	@Transactional
	public LotteryTicket ammendLotteryTicket(int ticketId, LotteryTicketRequest lotteryTicketRequest) {
		// Checking the input parameter validity
		checkLotteryTicketRequest(lotteryTicketRequest);

		// Get the ticket object
		LotteryTicket ticket = getLotteryTicket(ticketId);

		// Check if ticket status is checked or not
		if (ticket.isTicketStatusChecked()) {
			throw new InternalServerError("This ticket is already checked and cannot be ammended anymore");
		} else {
			try {
				// Append with n lines to existing lines and save in db
				List<LotteryTicketLine> additionalLinesList = createTicketLines(lotteryTicketRequest.getNumberOfLines(),
						ticket);
				lineRepository.saveAll(additionalLinesList);
			} catch (Exception exception) {
				LOGGER.error("Something went wrong in ammendLotteryTicket" + exception);
				throw new InternalServerError("Something went wrong in ammending the lottery ticket");
			}

			// Return the updated ticket with lines
			return ticket;
		}
	}

	// ------------------------- Helper methods ----------------------------
	private void checkLotteryTicketRequest(LotteryTicketRequest lotteryTicketRequest) {
		// Checking if number of lines object is null or less than/equal to zero
		if (lotteryTicketRequest == null || lotteryTicketRequest.getNumberOfLines() <= 0) {
			throw new BadRequestException("Invalid input parameters found");
		}
	}

	private List<LotteryTicketLine> createTicketLines(int lines, LotteryTicket ticket) {
		// Create ticket lines object
		List<LotteryTicketLine> linesList = new ArrayList<>();

		// Iterate the lines
		for (int linesItr = 1; linesItr <= lines; linesItr++) {
			// Generate appropriate parameters and add in list object
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
		// Generate integers between and including 0 and 2
		return new SplittableRandom().nextInt(0, 3);
	}

	public int getOutcome(int first, int second, int third) {
		// Generate outcomes as per rules and return int value
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
