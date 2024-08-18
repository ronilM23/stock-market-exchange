package org.ronil.controller;

import lombok.extern.slf4j.Slf4j;
import org.ronil.entity.Stock;
import org.ronil.entity.StockCategory;
import org.ronil.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("stock")
public class StockController {


    @Autowired
    StockRepository stockRepository;

    @GetMapping("/{code}")
    public Stock getStockInfo(@PathVariable("code") String stockCode) {
        log.info("Requesting info for " + stockCode);
        Optional<Stock> stockOptional =  stockRepository.findById(stockCode);
        Stock stock = stockOptional.isPresent() ? stockOptional.get() : null;
        return stock;
    }

    @GetMapping("/all")
    public List<Stock> getAllStocks() {
        log.info("Fetching all stocks");
        List<Stock> stockList =  stockRepository.findAll();
        return stockList;
    }


    @GetMapping("/filter/category/{category}")
    public List<Stock> getAllStocksByCategory(@PathVariable("category") StockCategory category) {
        log.info("Fetching all stocks");
        List<Stock> stockList =  stockRepository.findAllByCategory(category);
        return stockList;
    }

    @PostMapping
    public String createStock(@RequestBody Stock stock) {
        log.info("Ingesting stock " + stock.toString());
        stockRepository.save(stock);
        return stock.getStockCode();
    }
}
