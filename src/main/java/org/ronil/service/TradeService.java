package org.ronil.service;

import lombok.extern.slf4j.Slf4j;
import org.ronil.controller.request.TradeRequest;
import org.ronil.entity.Stock;
import org.ronil.entity.Trade;
import org.ronil.entity.TradeState;
import org.ronil.entity.Trader;
import org.ronil.repository.TradeRepository;
import org.ronil.repository.StockRepository;
import org.ronil.repository.TraderRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class TradeService {

    private TradeRepository tradeRepository;

    private TraderRepository traderRepository;

    private StockRepository stockRepository;


    public TradeService(TradeRepository tradeRepository, TraderRepository traderRepository, StockRepository stockRepository) {
        this.tradeRepository = tradeRepository;
        this.traderRepository = traderRepository;
        this.stockRepository = stockRepository;
    }

    public Trade createTrade(TradeRequest tradeRequest) {
        Trader trader = traderRepository.getReferenceById(tradeRequest.getTraderId());
        Stock stock = stockRepository.getReferenceById(tradeRequest.getStockCode());

        Trade trade = Trade.builder().tradeState(TradeState.OPEN)
                .stock(stock)
                .trader(trader)
                .quantity(tradeRequest.getQuantity())
                .tradeType(tradeRequest.getTradeType())
                .build();
        try {
            trade = tradeRepository.save(trade);
        } catch (Exception e) {
            log.error("Error in saving order: " + e.getMessage());
            return null;
        }
        log.info("Order saved successfully");
        return trade;
    }

    public Trade fetchOrder(Integer orderId) {
        Optional<Trade> orderOptional = tradeRepository.findById(orderId);
        Trade trade = orderOptional.isPresent() ? orderOptional.get() : null;
        return trade;
    }

}
