package com.example.learninglombook.service;

import com.example.learninglombook.model.Beer;

import java.util.List;
import java.util.UUID;

public interface BeerService {

    List<Beer> getBeerList();

    Beer getBeerById(UUID id);

    Beer addBeer(Beer beer);


    void updateBeerById(UUID beerId, Beer beer);

    void deleteByBeerId(UUID beerId);

    void updateBeerPatchById(UUID beerId, Beer beer);
}
