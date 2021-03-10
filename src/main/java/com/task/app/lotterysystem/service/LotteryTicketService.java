package com.task.app.lotterysystem.service;

import com.task.app.lotterysystem.model.LotteryTicket;
import com.task.app.lotterysystem.request.LotteryTicketRequest;

public interface LotteryTicketService {
	LotteryTicket createLotteryTicket(LotteryTicketRequest lotteryTicketRequest);
}
