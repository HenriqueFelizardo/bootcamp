package com.project.bootcamp.service;

import com.project.bootcamp.exceptions.BusinessException;
import com.project.bootcamp.exceptions.NotFoundException;
import com.project.bootcamp.mapper.StockMapper;
import com.project.bootcamp.model.Stock;
import com.project.bootcamp.model.dto.StockDTO;
import com.project.bootcamp.repository.StockRepository;
import com.project.bootcamp.util.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StockService {

    @Autowired
    private StockRepository repository;

    @Autowired
    private StockMapper mapper;

    @Transactional //Da rollback caso alguma das operações falhe
    public StockDTO save(StockDTO dto) {

        Optional<Stock> optionalStock = repository.findByNameAndDate(dto.getName(), dto.getDate());

        if (optionalStock.isPresent() && dto.getId() == null) {
            throw new BusinessException(MessageUtils.STOCK_ALREADY_EXISTS);
        }

        Stock stock = mapper.toEntity(dto);

        repository.save(stock);

        return mapper.toDto(stock);
    }

    @Transactional(readOnly = true)
    public List<StockDTO> getAll() {
        List<Stock> listStock = repository.findAll();
        List<StockDTO> listStockDTO = new ArrayList<>();

        listStock.forEach(stock -> {
            listStockDTO.add(mapper.toDto(stock));
        });

        return listStockDTO;
    }

    @Transactional(readOnly = true)
    public StockDTO get(Long id) {
        Optional<Stock> stock = repository.findById(id);

        if (!stock.isPresent()) {
            throw new NotFoundException(MessageUtils.NO_RECORDS_FOUND);
        }

        return mapper.toDto(stock.get());
    }

    @Transactional
    public StockDTO delete(Long id) {

        StockDTO dto = this.get(id);

        repository.deleteById(id);

        return dto;
    }

    @Transactional(readOnly = true)
    public List<StockDTO> getAllToday() {
        List<Stock> listStock = repository.getAllToday(LocalDate.now());

        if (listStock.isEmpty()){
            throw new NotFoundException(MessageUtils.NO_RECORDS_FOUND);
        }

        return mapper.toDto(listStock);
    }
}
