package com.spring.msscbeerservice.services;

import com.spring.msscbeerservice.web.model.BeerDto;

import java.util.List;
import java.util.UUID;

public interface BeerService {
    List<BeerDto> findAll();

    BeerDto getById(UUID beerId);

    BeerDto saveBeer(BeerDto beerDto);

    BeerDto updateBeer(UUID beerId, BeerDto beerDto);
}
