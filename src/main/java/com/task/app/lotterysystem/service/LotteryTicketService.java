package com.task.app.lotterysystem.service;

import java.util.List;

import com.task.app.lotterysystem.model.LotteryTicket;
import com.task.app.lotterysystem.request.LotteryTicketRequest;

public interface LotteryTicketService {
	LotteryTicket createLotteryTicket(LotteryTicketRequest lotteryTicketRequest);

	List<LotteryTicket> getAllLotteryTickets();

	LotteryTicket getLotteryTicket(int ticketId);

	LotteryTicket retrieveLotteryTicketStatus(int ticketId);

	LotteryTicket cancelLotteryTicket(int ticketId);

	LotteryTicket ammendLotteryTicket(int ticketId, LotteryTicketRequest lotteryTicketRequest);
}
