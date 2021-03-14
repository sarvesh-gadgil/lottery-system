package com.task.app.lotterysystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.task.app.lotterysystem.model.LotteryTicket;
import com.task.app.lotterysystem.request.LotteryTicketRequest;
import com.task.app.lotterysystem.service.LotteryTicketService;
import com.task.app.lotterysystem.service.impl.LotteryTicketServiceImpl;

@RestController
@RequestMapping(path = "api/")
public class LotteryTicketController implements LotteryTicketService {

	@Autowired
	private LotteryTicketServiceImpl lotteryTicketService;

	@Override
	@PostMapping(path = "ticket/create")
	public LotteryTicket createLotteryTicket(@RequestBody(required = true) LotteryTicketRequest lotteryTicketRequest) {

		return lotteryTicketService.createLotteryTicket(lotteryTicketRequest);
	}

	@Override
	@GetMapping(path = "ticket/get/all")
	public List<LotteryTicket> getAllLotteryTickets() {

		return lotteryTicketService.getAllLotteryTickets();
	}

	@Override
	@GetMapping(path = "ticket/get/{ticketId}")
	public LotteryTicket getLotteryTicket(@PathVariable(required = true) int ticketId) {

		return lotteryTicketService.getLotteryTicket(ticketId);
	}

	@Override
	@PutMapping(path = "ticket/status/{ticketId}")
	public LotteryTicket retrieveLotteryTicketStatus(@PathVariable(required = true) int ticketId) {

		return lotteryTicketService.retrieveLotteryTicketStatus(ticketId);
	}

	@Override
	@PutMapping(path = "ticket/cancel/{ticketId}")
	public LotteryTicket cancelLotteryTicket(@PathVariable(required = true) int ticketId) {

		return lotteryTicketService.cancelLotteryTicket(ticketId);
	}

	@Override
	@PutMapping(path = "ticket/ammend/{ticketId}")
	public LotteryTicket ammendLotteryTicket(@PathVariable(required = true) int ticketId,
			@RequestBody(required = true) LotteryTicketRequest lotteryTicketRequest) {

		return lotteryTicketService.ammendLotteryTicket(ticketId, lotteryTicketRequest);
	}

}
