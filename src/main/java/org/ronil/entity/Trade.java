package org.ronil.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Trade implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Trader trader;

    @ManyToOne
    private Stock stock;

    private Integer quantity;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private TradeState tradeState;

    private TradeType tradeType;
}
