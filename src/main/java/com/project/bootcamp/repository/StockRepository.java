package com.project.bootcamp.repository;

import com.project.bootcamp.model.Stock;
import com.project.bootcamp.model.dto.StockDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository //Controla as instancias da classe automaticamente
public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByNameAndDate(String name, LocalDate date);

    @Query("SELECT stock " +
            "FROM Stock stock " +
            "WHERE stock.date = :date")
    List<Stock> getAllToday(LocalDate date);
}
