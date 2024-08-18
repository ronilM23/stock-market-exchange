package org.ronil.repository;


import org.ronil.entity.Trade;
import org.ronil.entity.TradeState;
import org.ronil.entity.TradeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Integer> {

    List<Trade> findAllByTradeStateAndTradeType(TradeState tradeState, TradeType tradeType);
}
