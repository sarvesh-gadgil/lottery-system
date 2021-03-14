package com.task.app.lotterysystem.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class LotteryTicket implements Serializable {

	private static final long serialVersionUID = 1L;

	public LotteryTicket() {
	}

	public LotteryTicket(int ticketId, List<LotteryTicketLine> lotteryTicketLine) {
		super();
		this.ticketId = ticketId;
		this.lotteryTicketLine = lotteryTicketLine;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ticketId;

	@Column(nullable = false, columnDefinition = "TINYINT", length = 1)
	private boolean isTicketStatusChecked = false;

	@Column(nullable = false, columnDefinition = "TINYINT", length = 1)
	private boolean isCancelled = false;

	@OneToMany(mappedBy = "lotteryTicket")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<LotteryTicketLine> lotteryTicketLine;

	public int getTicketId() {
		return ticketId;
	}

	public void setTicketId(int ticketId) {
		this.ticketId = ticketId;
	}

	public boolean isTicketStatusChecked() {
		return isTicketStatusChecked;
	}

	public void setTicketStatusChecked(boolean isTicketStatusChecked) {
		this.isTicketStatusChecked = isTicketStatusChecked;
	}

	public boolean isCancelled() {
		return isCancelled;
	}

	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	public List<LotteryTicketLine> getLotteryTicketLine() {
		return lotteryTicketLine;
	}

	public void setLotteryTicketLine(List<LotteryTicketLine> lotteryTicketLine) {
		this.lotteryTicketLine = lotteryTicketLine;
	}
}
