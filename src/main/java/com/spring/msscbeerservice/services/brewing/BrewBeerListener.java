package com.spring.msscbeerservice.services.brewing;

import com.spring.msscbeerservice.config.JmsConfig;
import com.spring.msscbeerservice.domain.Beer;
import com.spring.msscbeerservice.events.BrewBeerEvent;
import com.spring.msscbeerservice.events.NewInventoryEvent;
import com.spring.msscbeerservice.repositories.BeerRepository;
import com.spring.msscbeerservice.web.controller.NotFoundException;
import com.spring.msscbeerservice.web.model.BeerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrewBeerListener {

    private final BeerRepository beerRepository;
    private final JmsTemplate jmsTemplate;

    @Transactional
    @JmsListener(destination = JmsConfig.BREWING_REQUEST_QUEUE)
    public void listen(BrewBeerEvent event){
        BeerDto beerDto = event.getBeerDto();
        Beer beer = beerRepository.findById(beerDto.getId()).orElseThrow(NotFoundException::new);
        beerDto.setQuantityOnHand(beer.getQuantityToBrew());

        log.debug("Brewed Beer : " + beer.getMinOnHand() + ", Quantity On Hand : " + beerDto.getQuantityOnHand());
        NewInventoryEvent newInventoryEvent = new NewInventoryEvent(beerDto);
        jmsTemplate.convertAndSend(JmsConfig.NEW_INVENTORY_REQUEST_QUEUE, newInventoryEvent);
    }
}
