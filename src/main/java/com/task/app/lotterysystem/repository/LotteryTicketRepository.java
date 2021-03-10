package com.task.app.lotterysystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.task.app.lotterysystem.model.LotteryTicket;

@Repository
public interface LotteryTicketRepository extends JpaRepository<LotteryTicket, Integer> {

}
