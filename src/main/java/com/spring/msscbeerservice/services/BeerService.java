package com.spring.msscbeerservice.services;

import com.spring.msscbeerservice.web.model.BeerDto;
import com.spring.msscbeerservice.web.model.BeerPagedList;
import com.spring.msscbeerservice.web.model.BeerStyleEnum;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface BeerService {
    BeerPagedList listBeers(String beerName, BeerStyleEnum beerStyle, PageRequest of);

    BeerDto getById(UUID beerId);

    BeerDto saveBeer(BeerDto beerDto);

    BeerDto updateBeer(UUID beerId, BeerDto beerDto);
}
