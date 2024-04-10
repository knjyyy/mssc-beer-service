package com.spring.msscbeerservice.web.mappers;

import com.spring.msscbeerservice.domain.Beer;
import com.spring.msscbeerservice.services.inventory.BeerInventoryService;
import com.spring.msscbeerservice.web.model.BeerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class BeerMapperDecorator implements BeerMapper {

    @Autowired // disable when running tests to pass
    private BeerInventoryService beerInventoryService;
    @Autowired // disable when running tests to pass
    private BeerMapper beerMapper;

    //@Autowired
//    public void setBeerInventoryService(BeerInventoryService beerInventoryService) {
//        this.beerInventoryService = beerInventoryService;
//    }
//
//    //@Autowired
//    public void setBeerMapper(BeerMapper beerMapper) {
//        this.beerMapper = beerMapper;
//    }

    @Override
    public BeerDto beerToBeerDto(Beer beer) {
        BeerDto beerDto = beerMapper.beerToBeerDto(beer);
        beerDto.setQuantityOnHand(beerInventoryService.getOnhandInventory(beer.getId()));
        return beerDto;
    }

    @Override
    public Beer beerDtoToBeer(BeerDto beerDto) {
        return beerMapper.beerDtoToBeer(beerDto);
    }
}
