package org.ronil.data_loaders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.extern.slf4j.Slf4j;
import org.ronil.controller.request.TradeRequest;
import org.ronil.entity.Stock;
import org.ronil.entity.Trade;
import org.ronil.entity.Trader;
import org.ronil.repository.RepositoryFactory;
import org.ronil.repository.StockRepository;
import org.ronil.service.TradeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Profile("dev")
@Component
@Slf4j
public class DataLoader implements CommandLineRunner {


    @Override
    public void run(String... args) throws Exception {
        loadTestData();
    }

    private RepositoryFactory repositoryFactory;

    private TradeService tradeService;

    public DataLoader(RepositoryFactory repositoryFactory, TradeService tradeService) {
        this.repositoryFactory = repositoryFactory;
        this.tradeService = tradeService;
    }

    private void loadTestData() throws IOException {
        loadEntityData("/data/load_stocks.json", Stock.class);
        loadEntityData("/data/load_traders.json", Trader.class);
        loadTrades();
    }

    private  <T, ID>void loadEntityData(String fileName, Class<T> clazz) throws IOException {
        log.info("Entering loading of test data for app of type: " + clazz);
        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = TypeReference.class.getResourceAsStream(fileName);
        CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
        List<T> entityList = mapper.readValue(inputStream, type);
        JpaRepository<T, ID> repository = repositoryFactory.getRepositoryInstance(clazz);
        repository.saveAll(entityList);
        log.info("Completed loading of test data for app of type: " + clazz);
    }

    private void loadTrades() throws IOException {
        log.info("Entering loading of test Trade data for app");
        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = TypeReference.class.getResourceAsStream("/data/load_trades.json");
        CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, TradeRequest.class);
        List<TradeRequest> tradeList = mapper.readValue(inputStream, type);
        tradeList.forEach(trade -> tradeService.createTrade(trade));
        log.info("Completed loading of test data for Trade");
    }
}
