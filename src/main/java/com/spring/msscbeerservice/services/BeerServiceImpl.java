package com.spring.msscbeerservice.services;

import com.spring.msscbeerservice.domain.Beer;
import com.spring.msscbeerservice.repositories.BeerRepository;
import com.spring.msscbeerservice.web.controller.NotFoundException;
import com.spring.msscbeerservice.web.mappers.BeerMapper;
import com.spring.msscbeerservice.web.model.BeerDto;
import com.spring.msscbeerservice.web.model.BeerPagedList;
import com.spring.msscbeerservice.web.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public BeerPagedList listBeers(String beerName, BeerStyleEnum beerStyle, PageRequest pageRequest,
                                   Boolean showInventoryOnHand) {
        BeerPagedList beerPagedList;
        Page<Beer> beerPage;

        if(!StringUtils.isEmpty(beerName) && !StringUtils.isEmpty(beerStyle)) {
            // search both
            beerPage = beerRepository.findAllByBeerNameAndBeerStyle(beerName, beerStyle, pageRequest);
        } else if(!StringUtils.isEmpty(beerName) && StringUtils.isEmpty(beerStyle)) {
            // search beer_service name
            beerPage = beerRepository.findAllByBeerName(beerName, pageRequest);
        } else if(!StringUtils.isEmpty(beerName) && StringUtils.isEmpty(beerStyle)) {
            // search beer_service style
            beerPage = beerRepository.findAllByBeerStyle(beerStyle, pageRequest);
        } else {
            beerPage = beerRepository.findAll(pageRequest);
        }


        beerPagedList = new BeerPagedList(beerPage
                .getContent()
                .stream()
                .map(showInventoryOnHand ? beerMapper::beerToBeerDtoWithInventory : beerMapper::beerToBeerDto)
                .collect(Collectors.toList()),
                PageRequest.of(beerPage.getPageable().getPageNumber(), beerPage.getPageable().getPageSize()), beerPage.getTotalElements());

        return beerPagedList;
    }

    @Override
    public BeerDto getById(UUID beerId, Boolean showInventoryOnHand) {
        System.out.println(beerId);
        System.out.println(showInventoryOnHand);
        Beer beer = beerRepository.findById(beerId).orElseThrow(NotFoundException::new);
        System.out.println(beer.toString());
        if(showInventoryOnHand) {
            return beerMapper.beerToBeerDtoWithInventory(beer);
        } else {
            return beerMapper.beerToBeerDto(beer);
        }
    }

    @Override
    public BeerDto saveBeer(BeerDto beerDto) {
        return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beerDto)));
    }

    @Override
    public BeerDto updateBeer(UUID beerId, BeerDto beerDto) {
        Beer beer = beerRepository.findById(beerId).orElseThrow(NotFoundException::new);
        beer.setBeerName(beerDto.getBeerName());
        beer.setBeerStyle(beerDto.getBeerStyle().name());
        beer.setPrice(beerDto.getPrice());
        beer.setUpc(beerDto.getUpc());

        return beerMapper.beerToBeerDto(beerRepository.save(beer));
    }
}
