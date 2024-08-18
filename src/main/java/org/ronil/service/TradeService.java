package org.ronil.service;

import lombok.extern.slf4j.Slf4j;
import org.ronil.controller.request.TradeRequest;
import org.ronil.entity.*;
import org.ronil.repository.TradeRepository;
import org.ronil.repository.StockRepository;
import org.ronil.repository.TraderRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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

    public List<Trade> fetchAllOrders() {
        return tradeRepository.findAll();
    }

    @Transactional
    public void fulfillOrder(Trade buyTrade, Trade sellTrade) throws Exception {
        if (buyTrade.getQuantity() == sellTrade.getQuantity()
                && buyTrade.getTradeState() == TradeState.OPEN && sellTrade.getTradeState() == TradeState.OPEN) {
            buyTrade.setTradeState(TradeState.EXECUTED);
            buyTrade.setUpdatedAt(LocalDateTime.now());
            sellTrade.setTradeState(TradeState.EXECUTED);
            sellTrade.setUpdatedAt(LocalDateTime.now());
            tradeRepository.saveAll(Arrays.asList(new Trade[]{buyTrade, sellTrade}));
            //troubleMaker();
            log.info("Trade matched and fulfilled: Buy:::" + buyTrade + ":SELL::: " + sellTrade);
        }
    }

    public List<Trade> findOpenOrders(TradeType tradeType) {
        return tradeRepository.findAllByTradeStateAndTradeType(TradeState.OPEN, tradeType);
    }

    private void troubleMaker() {
        throw new RuntimeException("testing transactionality by throwing exception mid way");
    }
}

