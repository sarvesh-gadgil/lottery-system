package com.task.app.lotterysystem.request;

public class LotteryTicketRequest {

	private int numberOfLines;

	public LotteryTicketRequest() {
	}

	public LotteryTicketRequest(int numberOfLines) {
		super();
		this.numberOfLines = numberOfLines;
	}

	public int getNumberOfLines() {
		return numberOfLines;
	}

	public void setNumberOfLines(int numberOfLines) {
		this.numberOfLines = numberOfLines;
	}
}
