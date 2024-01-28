package com.example.learninglombook.controller;

import com.example.learninglombook.model.Beer;
import com.example.learninglombook.service.BeerService;
import com.example.learninglombook.service.impl.BeerServiceImplementation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    BeerService beerService;


    BeerServiceImplementation beerServiceImplementation ;   // implementation class


    @BeforeEach
    void setUp() {
       beerServiceImplementation = new BeerServiceImplementation();
    }

    @Test
    void testCreateNewBeer() throws Exception {
        Beer beer = beerServiceImplementation.getBeerList().get(0);
        beer.setVersion(null);
        beer.setId(null);

        // koi bhi beer ka object de do , ye list se 1 index waala hi return karega
        given(beerService.addBeer(any(Beer.class))).willReturn(beerServiceImplementation.getBeerList().get(1));

        mockMvc.perform(post("/api/v1/beer/addBeer")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

    }

    @Test
    void testListBeers() throws Exception {

        given(beerService.getBeerList()).willReturn(beerServiceImplementation.getBeerList());

        mockMvc.perform(get("/api/v1/beer")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()",is(3)));
    }

    @Test
    void getBeerById() throws Exception {
        Beer testBeer = beerServiceImplementation.getBeerList().get(0);

        // means , humne testBeer ki id di hao , to ye hume test beer ka object hi return karega
        given(beerService.getBeerById(testBeer.getId())).willReturn(testBeer);

        // mock mvc is performing the get operation
        mockMvc.perform(get("/api/v1/beer/getBeerById/" + testBeer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id",is(testBeer.getId().toString())))
//                .andExpect(jsonPath("$.beerName",is(testBeer.getBeerName()+"foo"))); // fail
                .andExpect(jsonPath("$.beerName",is(testBeer.getBeerName())));
        // means jo json aayega usme beerName root document ke name name se match karna chahiye (i.e test beer) and same goes for id
    }
}

/*
   $.id :- means, from the root of the document
 */