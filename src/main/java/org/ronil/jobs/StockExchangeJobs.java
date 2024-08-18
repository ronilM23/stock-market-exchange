package org.ronil.jobs;

import lombok.extern.slf4j.Slf4j;
import org.ronil.entity.Stock;
import org.ronil.entity.Trade;
import org.ronil.entity.TradeState;
import org.ronil.entity.TradeType;
import org.ronil.repository.TradeRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;


@Slf4j
@Component
@Profile("dev")
public class StockExchangeJobs {

    private TradeRepository tradeRepository;

    public StockExchangeJobs(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    @Scheduled(fixedDelay = 60, initialDelay = 30, timeUnit = java.util.concurrent.TimeUnit.SECONDS)
    public void closeOpenOrders() {
        log.info("Checking for open orders and see if matching orders are present");
        List<Trade> buyTrades = tradeRepository.findAllByTradeStateAndTradeType(TradeState.OPEN, TradeType.BUY);
        List<Trade> sellTrades = tradeRepository.findAllByTradeStateAndTradeType(TradeState.OPEN, TradeType.SELL);
        Map<Stock, List<Trade>> sellTradeListByStockMap = new HashMap<>();
        sellTrades.stream().forEach(trade -> {
            if (!sellTradeListByStockMap.containsKey(trade.getStock())) {
                sellTradeListByStockMap.put(trade.getStock(), new ArrayList<>());
            }
            sellTradeListByStockMap.get(trade.getStock()).add(trade);
        });

        buyTrades.forEach(buyTrade -> {
            log.info("Checking matching sell orders for buy trade: " + buyTrade);
            if (sellTradeListByStockMap.containsKey(buyTrade.getStock())) {
                List<Trade> currSellTrades = sellTradeListByStockMap.get(buyTrade.getStock());
                currSellTrades.forEach(sellTrade -> {
                    fulfillOrder(buyTrade, sellTrade);
                });
            }
        });
        //Map<Stock, Trade> sellTradeMap = sellTrades.stream().collect(Collectors.toMap(Trade::getStock, Function.identity()));
    }

    @Transactional
    private void fulfillOrder(Trade buyTrade, Trade sellTrade) {
        if (buyTrade.getQuantity() == sellTrade.getQuantity()
                && buyTrade.getTradeState() == TradeState.OPEN && sellTrade.getTradeState() == TradeState.OPEN) {
            buyTrade.setTradeState(TradeState.EXECUTED);
            buyTrade.setUpdatedAt(LocalDateTime.now());
            sellTrade.setTradeState(TradeState.EXECUTED);
            sellTrade.setUpdatedAt(LocalDateTime.now());
            tradeRepository.saveAll(Arrays.asList(new Trade[]{buyTrade, sellTrade}));
            log.info("Trade matched and fulfilled: Buy:::" + buyTrade + ":SELL::: " + sellTrade);
        }
    }
}
