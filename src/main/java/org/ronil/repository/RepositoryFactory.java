package org.ronil.repository;

import org.ronil.entity.Stock;
import org.ronil.entity.Trade;
import org.ronil.entity.Trader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class RepositoryFactory {

    private StockRepository stockRepository;

    private TraderRepository traderRepository;

    private TradeRepository tradeRepository;

    public RepositoryFactory(StockRepository stockRepository, TraderRepository traderRepository, TradeRepository tradeRepository) {
        this.stockRepository = stockRepository;
        this.traderRepository = traderRepository;
        this.tradeRepository = tradeRepository;
    }

    public <T,ID>JpaRepository<T,ID> getRepositoryInstance(Class<T> clazz) {
        if (clazz == Stock.class) {
            return (JpaRepository<T, ID>) stockRepository;
        } else if (clazz == Trader.class) {
            return (JpaRepository<T, ID>) traderRepository;
        } else if (clazz == Trade.class) {
            return (JpaRepository<T, ID>) tradeRepository;
        } else {
            //none found
            return null;
        }
    }

}
