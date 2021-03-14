package com.task.app.lotterysystem.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
public class LotteryTicketLine implements Serializable {

	private static final long serialVersionUID = 1L;

	public LotteryTicketLine() {
	}

	public LotteryTicketLine(int firstNumber, int secondNumber, int thirdNumber, int preComputedOutcome,
			LotteryTicket lotteryTicket) {
		super();
		this.firstNumber = firstNumber;
		this.secondNumber = secondNumber;
		this.thirdNumber = thirdNumber;
		this.preComputedOutcome = preComputedOutcome;
		this.lotteryTicket = lotteryTicket;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int lineId;

	@Column(nullable = false)
	private int firstNumber;

	@Column(nullable = false)
	private int secondNumber;

	@Column(nullable = false)
	private int thirdNumber;

	@JsonIgnore
	@Column(nullable = false)
	private int preComputedOutcome;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Transient
	private String outcome = "NA";

	@ManyToOne
	@JoinColumn(name = "ticketId", nullable = false)
	@JsonIgnore
	private LotteryTicket lotteryTicket;

	public int getLineId() {
		return lineId;
	}

	public void setLineId(int lineId) {
		this.lineId = lineId;
	}

	public int getFirstNumber() {
		return firstNumber;
	}

	public void setFirstNumber(int firstNumber) {
		this.firstNumber = firstNumber;
	}

	public int getSecondNumber() {
		return secondNumber;
	}

	public void setSecondNumber(int secondNumber) {
		this.secondNumber = secondNumber;
	}

	public int getThirdNumber() {
		return thirdNumber;
	}

	public void setThirdNumber(int thirdNumber) {
		this.thirdNumber = thirdNumber;
	}

	public int getPreComputedOutcome() {
		return preComputedOutcome;
	}

	public void setPreComputedOutcome(int preComputedOutcome) {
		this.preComputedOutcome = preComputedOutcome;
	}

	public String getOutcome() {
		if (lotteryTicket.isTicketStatusChecked()) {
			return String.valueOf(preComputedOutcome);
		} else {
			return outcome;
		}
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public LotteryTicket getLotteryTicket() {
		return lotteryTicket;
	}

	public void setLotteryTicket(LotteryTicket lotteryTicket) {
		this.lotteryTicket = lotteryTicket;
	}
}
