package com.task.app.lotterysystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.task.app.lotterysystem.model.LotteryTicketLine;

@Repository
public interface LotteryTicketLineRepository extends JpaRepository<LotteryTicketLine, Integer> {

}
