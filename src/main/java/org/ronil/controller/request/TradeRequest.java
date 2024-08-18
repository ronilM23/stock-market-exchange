package org.ronil.controller.request;

import lombok.Data;
import org.ronil.entity.TradeType;


@Data
public class TradeRequest {

    private Integer traderId;

    private String stockCode;

    private Integer quantity;

    private TradeType tradeType;
}
