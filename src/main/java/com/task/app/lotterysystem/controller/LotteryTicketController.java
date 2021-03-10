package com.task.app.lotterysystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.task.app.lotterysystem.model.LotteryTicket;
import com.task.app.lotterysystem.request.LotteryTicketRequest;
import com.task.app.lotterysystem.service.LotteryTicketService;
import com.task.app.lotterysystem.service.impl.LotteryTicketServiceImpl;

@RestController
@RequestMapping(path = "/api/")
public class LotteryTicketController implements LotteryTicketService {

	@Autowired
	private LotteryTicketServiceImpl lotteryTicketServiceImpl;

	@Override
	@GetMapping(path = "ticket/create")
	public LotteryTicket createLotteryTicket(LotteryTicketRequest lotteryTicketRequest) {
		return lotteryTicketServiceImpl.createLotteryTicket(lotteryTicketRequest);
	}

}
