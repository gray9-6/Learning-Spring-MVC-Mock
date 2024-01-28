package com.example.learninglombook.service.impl;

import com.example.learninglombook.model.Beer;
import com.example.learninglombook.model.BeerStyle;
import com.example.learninglombook.service.BeerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class BeerServiceImplementation implements BeerService {

    // this acts like a db
    private Map<UUID,Beer> beerMap;

    public BeerServiceImplementation() {
        beerMap = new HashMap<>();

        Beer beer1 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Tuborg")
                .beerStyle(BeerStyle.LARGER)
                .upc("12345")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        Beer beer2 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Karlsberg")
                .beerStyle(BeerStyle.LARGER)
                .upc("123456222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        Beer beer3 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("KingFisher")
                .beerStyle(BeerStyle.LARGER)
                .upc("123456")
                .price(new BigDecimal("10.99"))
                .quantityOnHand(132)
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .build();

        beerMap.put(beer1.getId(),beer1);
        beerMap.put(beer2.getId(),beer2);
        beerMap.put(beer3.getId(),beer3);
    }

    @Override
    public List<Beer> getBeerList(){
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Beer getBeerById(UUID id) {
        log.debug("Get Beer id in the service layer - " +id.toString() );

        return beerMap.get(id);
    }

    @Override
    public Beer addBeer(Beer beer) {
        Beer savedBeer = Beer.builder()
                .id(UUID.randomUUID())
                .beerStyle(beer.getBeerStyle())
                .beerName(beer.getBeerName())
                .version(beer.getVersion())
                .updatedDate(LocalDateTime.now())
                .createdDate(LocalDateTime.now())
                .quantityOnHand(beer.getQuantityOnHand())
                .price(beer.getPrice())
                .upc(beer.getUpc())
                .build();

        beerMap.put(savedBeer.getId(),savedBeer);
        return savedBeer;
    }

    @Override
    public void updateBeerById(UUID beerId, Beer beer) {
        Beer existingBeer = beerMap.get(beerId);
        existingBeer.setBeerName(beer.getBeerName());
        existingBeer.setBeerStyle(beer.getBeerStyle());
        existingBeer.setPrice(beer.getPrice());
        existingBeer.setUpc(beer.getUpc());
        existingBeer.setQuantityOnHand(beer.getQuantityOnHand());
        existingBeer.setUpdatedDate(LocalDateTime.now());

        beerMap.put(existingBeer.getId(),existingBeer);
    }

    @Override
    public void deleteByBeerId(UUID beerId) {
        beerMap.remove(beerId);
    }

    @Override
    public void updateBeerPatchById(UUID beerId, Beer beer) {
        Beer existing = beerMap.get(beerId);

        if(StringUtils.hasText(beer.getBeerName())){
            existing.setBeerName(beer.getBeerName());
        }

        if(beer.getBeerStyle() != null){
            existing.setBeerStyle(beer.getBeerStyle());
        }

        if(beer.getPrice() != null){
            existing.setPrice(beer.getPrice());
        }

        if(beer.getQuantityOnHand() != null){
            existing.setQuantityOnHand(beer.getQuantityOnHand());
        }

        if(StringUtils.hasText(beer.getUpc())){
            existing.setUpc(beer.getUpc());
        }
    }
}
