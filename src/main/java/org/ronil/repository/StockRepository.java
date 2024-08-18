package org.ronil.repository;

import org.ronil.entity.Stock;
import org.ronil.entity.StockCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, String> {

    List<Stock> findAllByCategory(StockCategory stockCategory);
}
