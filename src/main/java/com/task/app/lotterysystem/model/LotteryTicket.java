package com.task.app.lotterysystem.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class LotteryTicket implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private int ticket_id;

	public int getTicket_id() {
		return ticket_id;
	}

	public void setTicket_id(int ticket_id) {
		this.ticket_id = ticket_id;
	}
}
