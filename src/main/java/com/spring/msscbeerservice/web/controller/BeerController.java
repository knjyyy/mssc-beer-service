package com.spring.msscbeerservice.web.controller;

import com.spring.msscbeerservice.web.model.BeerDto;
import com.spring.msscbeerservice.web.model.BeerStyleEnum;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {

    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") UUID beerId) {
        // TODO impl
         return new ResponseEntity<>(BeerDto.builder()
                 .id(beerId)
                 .beerName("San Miguel Beer Pale Pilsen")
                 .beerStyle(BeerStyleEnum.PILSNER)
                 .price(new BigDecimal(1.00))
                 .createdDate(OffsetDateTime.now())
                 .build(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BeerDto> saveBeer(@Valid @RequestBody BeerDto beerDto) {
        // TODO impl
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{beerId}")
    public ResponseEntity updateBeerById(@PathVariable("beerId") UUID beerId, @Valid @RequestBody BeerDto beerDto) {
        // TODO impl
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
