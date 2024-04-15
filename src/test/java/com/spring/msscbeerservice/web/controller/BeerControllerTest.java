package com.spring.msscbeerservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.msscbeerservice.bootstrap.BeerLoader;
import com.spring.msscbeerservice.services.BeerService;
import com.spring.msscbeerservice.services.inventory.BeerInventoryService;
import com.spring.msscbeerservice.web.model.BeerDto;
import com.spring.msscbeerservice.web.model.BeerStyleEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "dev.springframework.rk3", uriPort = 80)
@WebMvcTest(BeerController.class)
@ComponentScan(basePackages = "com.spring.msscbeerservice.web.mappers")
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BeerService beerService;

    @MockBean
    BeerInventoryService beerInventoryService;

    @Test
    void getBeerById() throws Exception {
        given(beerService.getById(any(), any())).willReturn(getBeerDtoData());

        mockMvc.perform(get("/api/v1/beer/{beerId}", UUID.randomUUID())
                .param("isCold", "yes")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("v1/beer-get",
                        pathParameters(
                            parameterWithName("beerId").description("UUID of the beer to be retrieved")
                        ),
                        //pathParameters(
                        //        parameterWithName("isCold").description("Is Beer Cold Query Parameter")
                        //),
                        responseFields(
                                fieldWithPath("id").description("Beer ID ").type(UUID.class),
                                fieldWithPath("version").description("Version number").type(Integer.class),
                                fieldWithPath("createdDate").description("Date Created").type(OffsetDateTime.class),
                                fieldWithPath("lastModifiedDate").description("Date Updated").type(OffsetDateTime.class),
                                fieldWithPath("beerName").description("Beer Name").type(String.class),
                                fieldWithPath("beerStyle").description("Beer Style").type(UUID.class),
                                fieldWithPath("upc").description("UPC of Beer").type(Integer.class),
                                fieldWithPath("price").description("Price").type(Double.class),
                                fieldWithPath("quantityOnHand").description("Quantity on Hand").type(Integer.class)
                        )
                ));
    }

    @Test
    void saveBeer() throws Exception {
        BeerDto beerDto = getBeerDtoData();
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);

        ConstrainedFields fields = new ConstrainedFields(BeerDto.class);

        given(beerService.saveBeer(any())).willReturn(getBeerDtoData());

        mockMvc.perform(post("/api/v1/beer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerDtoJson))
                .andExpect(status().isCreated())
                .andDo(document("v1/beer-post",
                        requestFields(
                                fields.withPath("id").ignored().type(UUID.class),
                                fields.withPath("version").ignored().type(Integer.class),
                                fields.withPath("createdDate").ignored().type(OffsetDateTime.class),
                                fields.withPath("lastModifiedDate").ignored().type(OffsetDateTime.class),
                                fields.withPath("beerName").description("Name of the Beer").type(String.class),
                                fields.withPath("beerStyle").description("Style of the Beer").type(String.class),
                                fields.withPath("upc").description("Beer UPC").attributes().type(Integer.class),
                                fields.withPath("price").description("Beer Price").type(Double.class),
                                fields.withPath("quantityOnHand").ignored().type(Integer.class)
                        )));
    }

    @Test
    void updateBeerById() throws Exception {
        BeerDto beerDto = getBeerDtoData();
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);

        given(beerService.updateBeer(any(), any())).willReturn(getBeerDtoData());
        mockMvc.perform(put("/api/v1/beer/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerDtoJson))
                .andExpect(status().isNoContent());
    }

    BeerDto getBeerDtoData() {
        return BeerDto.builder()
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyleEnum.PILSNER)
                .upc(BeerLoader.BEER_1_UPC)
                .price(new BigDecimal("11.95"))
                .build();
    }

    private static class ConstrainedFields {
        private final ConstraintDescriptions constraintDescriptions;

        ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        private FieldDescriptor withPath(String path) {
            return fieldWithPath(path).attributes(key("constraints").value(
                    StringUtils.collectionToDelimitedString(this.constraintDescriptions
                            .descriptionsForProperty(path), ". ")
            ));
        }
    }
}