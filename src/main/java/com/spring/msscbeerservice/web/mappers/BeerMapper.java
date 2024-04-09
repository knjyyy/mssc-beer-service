package com.spring.msscbeerservice.web.mappers;

import com.spring.msscbeerservice.domain.Beer;
import com.spring.msscbeerservice.web.model.BeerDto;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class})
public interface BeerMapper {
    BeerDto beerToBeerDto(Beer beer);
    Beer beerDtoToBeer(BeerDto beerDto);
}