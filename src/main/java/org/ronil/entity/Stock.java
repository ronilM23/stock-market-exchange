package org.ronil.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
public class Stock implements Serializable {

    @Id
    private String stockCode;

    private StockCategory category;

    private String name;
}
