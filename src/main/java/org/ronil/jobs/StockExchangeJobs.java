package org.ronil.jobs;

import lombok.extern.slf4j.Slf4j;
import org.ronil.entity.Stock;
import org.ronil.entity.Trade;
import org.ronil.entity.TradeState;
import org.ronil.entity.TradeType;
import org.ronil.repository.TradeRepository;
import org.ronil.service.TradeService;
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

    private TradeService tradeService;

    public StockExchangeJobs(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @Scheduled(fixedDelay = 60, initialDelay = 10, timeUnit = java.util.concurrent.TimeUnit.SECONDS)
    public void closeOpenOrders() {
        log.info("Checking for open orders and see if matching orders are present");
        List<Trade> buyTrades = tradeService.findOpenOrders(TradeType.BUY);
        List<Trade> sellTrades = tradeService.findOpenOrders(TradeType.SELL);
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
                    try {
                        tradeService.fulfillOrder(buyTrade, sellTrade);
                    } catch (Exception e) {
                        log.error("Some fault happened midway");
                    }
                });
            }
        });
    }
}
