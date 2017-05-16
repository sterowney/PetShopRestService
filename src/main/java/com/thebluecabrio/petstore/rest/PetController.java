package com.thebluecabrio.petstore.rest;

import com.thebluecabrio.petstore.domain.Pet;
import com.thebluecabrio.petstore.service.PetService;
import com.thebluecabrio.petstore.service.requests.CreatePetRequest;
import com.thebluecabrio.petstore.service.requests.PetsRequest;
import com.thebluecabrio.petstore.service.requests.UpdatePetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by stevenrowney on 16/05/2017.
 */
@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    private PetService petService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<Pet> getPets(
            @PageableDefault(direction = Sort.Direction.ASC, sort = "name") Pageable pageable,
            @RequestParam(value = "name", required = false) String name ) {

        PetsRequest petsRequest = new PetsRequest();
        petsRequest.setPageable(pageable);
        petsRequest.setName(name);

        return petService.getPets(petsRequest);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Pet getPet(@PathVariable() Long id) {

        return petService.getPet(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Pet createPet(@RequestBody(required = true) CreatePetRequest createPetRequest) {

        return petService.createPet(createPetRequest);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Pet updatePet(@PathVariable() Long id, @RequestBody(required = true) UpdatePetRequest updatePetRequest) {

        updatePetRequest.setId(id);

        return petService.updatePet(updatePetRequest);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deletePet(@PathVariable() Long id) {

        petService.deletePet(id);
    }
}
