package org.ronil.controller;

import lombok.extern.slf4j.Slf4j;
import org.ronil.controller.request.TradeRequest;
import org.ronil.entity.Trade;
import org.ronil.entity.TradeType;
import org.ronil.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("trade")
public class TradeController {


    @Autowired
    TradeService tradeService;

    @GetMapping("/{id}")
    public Trade getTradeInfo(@PathVariable("id") Integer orderId) {
        log.info("Requesting info for Order with id " + orderId);
        Trade trade =  tradeService.fetchOrder(orderId);
        return trade;
    }

    @GetMapping("/all")
    public List<Trade> getAllTradeInfo() {
        log.info("Requesting info for all trades");
        return tradeService.fetchAllOrders();
    }


    @PostMapping
    public Trade createTrade(@RequestBody TradeRequest tradeRequest) {
        log.info("Creating order with request " + tradeRequest.toString());
        Trade trade = tradeService.createTrade(tradeRequest);
        return trade;
    }
}
