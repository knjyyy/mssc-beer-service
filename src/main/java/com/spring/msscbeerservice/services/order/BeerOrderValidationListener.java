package com.spring.msscbeerservice.services.order;

import com.spring.brewery.model.events.ValidateOrderRequest;
import com.spring.brewery.model.events.ValidateOrderResult;
import com.spring.msscbeerservice.config.JmsConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BeerOrderValidationListener {
    private final BeerOrderValidator beerOrderValidator;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination  = JmsConfig.QUEUE_VALIDATE_ORDER)
    public void listen(ValidateOrderRequest validateOrderRequest) {
        Boolean isValid = beerOrderValidator.validate(validateOrderRequest.getBeerOrderDto());

        jmsTemplate.convertAndSend(JmsConfig.QUEUE_VALIDATE_ORDER_RESPONSE, ValidateOrderResult.builder()
                .isValid(isValid)
                .orderId(validateOrderRequest.getBeerOrderDto().getId())
                .build());
    }
}
