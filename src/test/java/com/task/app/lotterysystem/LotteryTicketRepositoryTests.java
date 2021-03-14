package com.task.app.lotterysystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.annotation.Rollback;

import com.task.app.lotterysystem.model.LotteryTicket;
import com.task.app.lotterysystem.model.LotteryTicketLine;
import com.task.app.lotterysystem.repository.LotteryTicketLineRepository;
import com.task.app.lotterysystem.repository.LotteryTicketRepository;
import com.task.app.lotterysystem.request.LotteryTicketRequest;
import com.task.app.lotterysystem.service.impl.LotteryTicketServiceImpl;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(OrderAnnotation.class)
@Profile("test")
public class LotteryTicketRepositoryTests {

	@Autowired
	private LotteryTicketRepository lotteryTicketRepository;

	@Autowired
	private LotteryTicketLineRepository lineRepository;

	@Test
	@Order(1)
	public void testTicketNotPresentForEmptyDB() {
		assertThat(lotteryTicketRepository.findAllByIsCancelled(false)).isEmpty();
	}

	@Test
	@Rollback(false)
	@Order(2)
	public void testCreateLotteryTicket() {
		LotteryTicket ticket = lotteryTicketRepository.save(new LotteryTicket(1));
		assertThat(ticket.getTicketId()).isGreaterThan(0);
		assertThat(ticket.isCancelled()).isFalse();
		assertThat(ticket.isTicketStatusChecked()).isFalse();
	}

	@Test
	@Order(3)
	public void testFindLotteryTicketById() {
		LotteryTicket ticket = lotteryTicketRepository.findByTicketIdAndIsCancelled(1, false);
		assertThat(ticket.getTicketId()).isEqualTo(1);
	}

	@Test
	@Rollback(false)
	@Order(4)
	public void testCreateLotteryTicketLines() {
		LotteryTicketServiceImpl service = new LotteryTicketServiceImpl();
		LotteryTicket ticket = lotteryTicketRepository.findByTicketIdAndIsCancelled(1, false);
		List<LotteryTicketLine> linesList = new ArrayList<>();
		linesList.add(new LotteryTicketLine(2, 0, 0, service.getOutcome(2, 0, 0), ticket));
		linesList.add(new LotteryTicketLine(2, 2, 2, service.getOutcome(2, 2, 2), ticket));
		linesList.add(new LotteryTicketLine(0, 2, 2, service.getOutcome(0, 2, 2), ticket));
		linesList.add(new LotteryTicketLine(2, 1, 2, service.getOutcome(2, 1, 2), ticket));
		List<LotteryTicketLine> updatedLinesList = lineRepository.saveAll(linesList);
		assertThat(updatedLinesList).isNotNull();
		assertThat(updatedLinesList.get(0).getPreComputedOutcome()).isEqualTo(10);
		assertThat(updatedLinesList.get(1).getPreComputedOutcome()).isEqualTo(5);
		assertThat(updatedLinesList.get(2).getPreComputedOutcome()).isEqualTo(1);
		assertThat(updatedLinesList.get(3).getPreComputedOutcome()).isEqualTo(0);
	}

	@Test
	@Rollback(false)
	@Order(5)
	public void testAmmendLotteryTicketLines() {
		LotteryTicketServiceImpl service = new LotteryTicketServiceImpl();
		LotteryTicket ticket = lotteryTicketRepository.findByTicketIdAndIsCancelled(1, false);
		List<LotteryTicketLine> linesList = new ArrayList<>();
		linesList.add(new LotteryTicketLine(0, 0, 0, service.getOutcome(0, 0, 0), ticket));
		List<LotteryTicketLine> updatedLinesList = lineRepository.saveAll(linesList);
		assertThat(updatedLinesList).isNotNull();
		assertThat(ticket.getLotteryTicketLine().get(0).getPreComputedOutcome()).isEqualTo(10);
		assertThat(ticket.getLotteryTicketLine().get(1).getPreComputedOutcome()).isEqualTo(5);
		assertThat(ticket.getLotteryTicketLine().get(2).getPreComputedOutcome()).isEqualTo(1);
		assertThat(ticket.getLotteryTicketLine().get(3).getPreComputedOutcome()).isEqualTo(0);
		assertThat(ticket.getLotteryTicketLine().get(4).getPreComputedOutcome()).isEqualTo(5);
	}

	@Test
	@Rollback(false)
	@Order(6)
	public void testCheckLotteryTicketStatus() {
		LotteryTicket ticket = lotteryTicketRepository.findByTicketIdAndIsCancelled(1, false);
		ticket.setTicketStatusChecked(true);
		ticket = lotteryTicketRepository.save(ticket);
		assertThat(ticket.isTicketStatusChecked()).isTrue();
		assertThat(ticket.getLotteryTicketLine().get(0).getOutcome()).isEqualTo("10");
		assertThat(ticket.getLotteryTicketLine().get(1).getOutcome()).isEqualTo("5");
		assertThat(ticket.getLotteryTicketLine().get(2).getOutcome()).isEqualTo("1");
		assertThat(ticket.getLotteryTicketLine().get(3).getOutcome()).isEqualTo("0");
		assertThat(ticket.getLotteryTicketLine().get(4).getOutcome()).isEqualTo("5");
	}

	@Test
	@Rollback(false)
	@Order(7)
	public void testCancelLotteryTicket() {
		LotteryTicket ticket = lotteryTicketRepository.save(new LotteryTicket(2));
		List<LotteryTicketLine> linesList = new ArrayList<>();
		LotteryTicketServiceImpl service = new LotteryTicketServiceImpl();
		linesList.add(new LotteryTicketLine(2, 0, 0, service.getOutcome(2, 0, 0), ticket));
		linesList.add(new LotteryTicketLine(2, 2, 2, service.getOutcome(2, 2, 2), ticket));
		lineRepository.saveAll(linesList);
		ticket.setCancelled(true);
		ticket = lotteryTicketRepository.save(ticket);
		assertThat(ticket.isCancelled()).isTrue();
	}

	@Test
	@Order(8)
	public void testFindCancelledTicket() {
		assertThat(lotteryTicketRepository.findByTicketIdAndIsCancelled(2, false)).isNull();
	}

	@Test
	@Order(9)
	public void testTicketNotPresent() {
		assertThat(lotteryTicketRepository.findByTicketIdAndIsCancelled(100, false)).isNull();
	}

	@Test
	@Order(10)
	public void testTicketCannotBeAmmended() {
		LotteryTicketServiceImpl service = new LotteryTicketServiceImpl();
		assertThatExceptionOfType(RuntimeException.class)
				.isThrownBy(() -> service.ammendLotteryTicket(1, new LotteryTicketRequest(2)));
	}

	@Test
	@Order(11)
	public void testTicketCannotBeCancelled() {
		LotteryTicketServiceImpl service = new LotteryTicketServiceImpl();
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> service.cancelLotteryTicket(1));
	}
}
