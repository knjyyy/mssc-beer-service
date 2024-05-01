package com.spring.brewery.model.events;

import com.spring.brewery.model.BeerOrderDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ValidateOrderRequest {
    private BeerOrderDto beerOrderDto;
}
