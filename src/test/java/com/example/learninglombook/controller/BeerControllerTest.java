package com.example.learninglombook.controller;

import com.example.learninglombook.model.Beer;
import com.example.learninglombook.service.BeerService;
import com.example.learninglombook.service.impl.BeerServiceImplementation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;
    @Captor
    ArgumentCaptor<Beer> beerArgumentCaptor;
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
    void testPatchBeer() throws Exception {
        Beer beer = beerServiceImplementation.getBeerList().get(0);

        Map<String,Object> beerMap = new HashMap<>();
        beerMap.put("beerName","New Name");

        mockMvc.perform(patch(BeerController.BEER_PATH+"/patch_beerId/" + beer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isNoContent());

        verify(beerService).updateBeerPatchById(uuidArgumentCaptor.capture(),beerArgumentCaptor.capture());

        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(beerMap.get("beerName")).isEqualTo(beerArgumentCaptor.getValue().getBeerName());

    }

    @Test
    void testDeleteBeer() throws Exception {
        Beer beer = beerServiceImplementation.getBeerList().get(0);

        mockMvc.perform(delete("/api/v1/beer/deleteBeer_byId/" + beer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(beerService).deleteByBeerId(uuidArgumentCaptor.capture());

        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testUpdateBeer() throws Exception {
        Beer beer = beerServiceImplementation.getBeerList().get(0);

        mockMvc.perform(put("/api/v1/beer/updateBeer_byId/" + beer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isNoContent());

        verify(beerService).updateBeerById(any(UUID.class),any(Beer.class));
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

        mockMvc.perform(get(BeerController.BEER_PATH)
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