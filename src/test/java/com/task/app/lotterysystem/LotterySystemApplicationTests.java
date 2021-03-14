package com.task.app.lotterysystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.task.app.lotterysystem.controller.LotteryTicketController;
import com.task.app.lotterysystem.repository.LotteryTicketLineRepository;
import com.task.app.lotterysystem.repository.LotteryTicketRepository;
import com.task.app.lotterysystem.request.LotteryTicketRequest;
import com.task.app.lotterysystem.service.impl.LotteryTicketServiceImpl;

@SpringBootTest
class LotterySystemApplicationTests {

	@Autowired
	private LotteryTicketServiceImpl lotteryTicketService;

	@Autowired
	private LotteryTicketRepository lotteryTicketRepository;

	@Autowired
	private LotteryTicketLineRepository lineRepository;

	@Autowired
	private LotteryTicketController ticketController;

	// Smoke test for beans
	@Test
	public void contextLoads() throws Exception {
		assertThat(lotteryTicketService).isNotNull();
		assertThat(lotteryTicketRepository).isNotNull();
		assertThat(lineRepository).isNotNull();
		assertThat(ticketController).isNotNull();
	}

	@Test
	public void checkInputParamsValidityForCreate() {
		assertThatExceptionOfType(RuntimeException.class)
				.isThrownBy(() -> lotteryTicketService.createLotteryTicket(new LotteryTicketRequest(0)));
		assertThatExceptionOfType(RuntimeException.class)
				.isThrownBy(() -> lotteryTicketService.createLotteryTicket(new LotteryTicketRequest(-1)));
	}
}
