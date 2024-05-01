package com.spring.msscbeerservice.services.brewing;

import com.spring.msscbeerservice.config.JmsConfig;
import com.spring.msscbeerservice.domain.Beer;
import com.spring.brewery.model.events.BrewBeerEvent;
import com.spring.msscbeerservice.repositories.BeerRepository;
import com.spring.msscbeerservice.services.inventory.BeerInventoryService;
import com.spring.msscbeerservice.web.mappers.BeerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrewingService {
    private final BeerRepository beerRepository;
    private final BeerInventoryService beerInventoryService;
    private final JmsTemplate jmsTemplate;
    private final BeerMapper beerMapper;

    @Scheduled(fixedRate = 5000)
    public void checkForLowInventory() {
        List<Beer> beers = beerRepository.findAll();
        beers.forEach(beer -> {
            Integer quantityOnHand = beerInventoryService.getOnhandInventory(beer.getId());
            log.debug("Minimum onhand is : " + beer.getMinOnHand());
            log.debug("Inventory is : " + quantityOnHand);

            if(beer.getMinOnHand() >= quantityOnHand) {
                log.info("Creating a Brew Beer event...");
                jmsTemplate.convertAndSend(JmsConfig.BREWING_REQUEST_QUEUE,
                        new BrewBeerEvent(beerMapper.beerToBeerDto(beer)));
            }
        });
    }
}
