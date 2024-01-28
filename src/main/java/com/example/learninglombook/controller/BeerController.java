package com.example.learninglombook.controller;

import com.example.learninglombook.model.Beer;
import com.example.learninglombook.service.BeerService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BeerController {

    public static final String BEER_PATH = "/api/v1/beer";
    public static final String BEER_PATH_ID = "/{beerId}";

    private final BeerService beerService;


    @PatchMapping(BEER_PATH+"/patch_beerId"+BEER_PATH_ID)
    public ResponseEntity updateBeerPatchById(@PathVariable UUID beerId,@RequestBody Beer beer){
        beerService.updateBeerPatchById(beerId,beer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(BEER_PATH +"/deleteBeer_byId"+BEER_PATH_ID)
    public ResponseEntity deleteByBeerId(@PathVariable UUID beerId){
        beerService.deleteByBeerId(beerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(BEER_PATH+"/updateBeer_byId"+BEER_PATH_ID)
    public ResponseEntity updateBeerById(@PathVariable UUID beerId,@RequestBody Beer beer){
        beerService.updateBeerById(beerId,beer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PostMapping(BEER_PATH+"/addBeer")
//    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity addBeer(@RequestBody Beer beer){
        Beer savedBeer = beerService.addBeer(beer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location","/api/v1/beer/getBeerById/" + savedBeer.getId().toString());
//        return new ResponseEntity<>("created",HttpStatus.CREATED);
        return new ResponseEntity<>(headers ,HttpStatus.CREATED);
    }

    @GetMapping(BEER_PATH)
    public List<Beer> getBeerList(){
        log.debug("getting all the beer list in controller");
        return beerService.getBeerList();
    }


    @GetMapping(BEER_PATH+"/getBeerById"+BEER_PATH_ID)
    public Beer getBeerById(@PathVariable UUID beerId){
        log.debug("Get Beer By id in the controller layer");
        return  beerService.getBeerById(beerId);
    }
}
