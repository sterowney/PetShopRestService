package com.thebluecabrio.petstore.service.impl;

import com.thebluecabrio.petstore.domain.Category;
import com.thebluecabrio.petstore.domain.Pet;
import com.thebluecabrio.petstore.domain.Status;
import com.thebluecabrio.petstore.domain.repository.CategoryRepository;
import com.thebluecabrio.petstore.domain.repository.PetRepository;
import com.thebluecabrio.petstore.exceptions.CategoryNotFoundException;
import com.thebluecabrio.petstore.exceptions.PetNotFoundException;
import com.thebluecabrio.petstore.service.PetService;
import com.thebluecabrio.petstore.service.requests.CreatePetRequest;
import com.thebluecabrio.petstore.service.requests.LookupItem;
import com.thebluecabrio.petstore.service.requests.PetsRequest;
import com.thebluecabrio.petstore.service.requests.UpdatePetRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by stevenrowney on 16/05/2017.
 */
@Service(value = "petService")
public class PetServiceImpl implements PetService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PetServiceImpl.class);

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public Page<Pet> getPets(PetsRequest petsRequest) {

        if(StringUtils.isEmpty(petsRequest.getName())) {
            return petRepository.findAll(petsRequest.getPageable());
        }

        return petRepository.findByNameContainingIgnoreCase(petsRequest.getName(), petsRequest.getPageable());
    }

    @Override
    public Pet createPet(CreatePetRequest createPetRequest) {

        Pet newPet = new Pet();
        newPet.setName(createPetRequest.getName());
        newPet.setStatus(getStatus(createPetRequest.getStatus()));

        for(LookupItem tag : createPetRequest.getTags()) {
            newPet.addTag(tag.getName());
        }

        for(LookupItem photoUrl : createPetRequest.getPhotoUrls()) {
            newPet.addPhotoUrl(photoUrl.getName());
        }

        newPet.setCategory(getCategory(createPetRequest.getCategory()));

        return petRepository.save(newPet);
    }

    @Override
    public Pet updatePet(UpdatePetRequest updatePetRequest) {

        Pet updatedPet = petRepository.findOne(updatePetRequest.getId());

        if(updatedPet == null) {
            throw new PetNotFoundException(updatePetRequest.getId());
        }

        updatedPet.setName(updatePetRequest.getName());
        updatedPet.setStatus(getStatus(updatePetRequest.getStatus()));
        updatedPet.setCategory(getCategory(updatePetRequest.getCategory()));

        for(LookupItem tag : updatePetRequest.getTags()) {
            updatedPet.addTag(tag.getId(), tag.getName());
        }

        for(LookupItem photoUrl : updatePetRequest.getPhotoUrls()) {
            updatedPet.addPhotoUrl(photoUrl.getId(), photoUrl.getName());
        }

        updatedPet = petRepository.save(updatedPet);

        return updatedPet;
    }

    @Override
    public Pet getPet(Long id) {

        Pet pet = petRepository.findOne(id);

        if(pet == null) {
            throw new PetNotFoundException(id);
        }

        return pet;
    }

    @Override
    public void deletePet(Long id) {

        Pet deletingPet = petRepository.findOne(id);

        if(deletingPet == null) {
            throw new PetNotFoundException(id);
        }

        petRepository.delete(deletingPet);
    }

    /**
     * Lookup for an existing category, must exist already
     *
     * @param categoryRequest
     * @return
     */
    private Category getCategory(LookupItem categoryRequest) {

        Category category;

        try {

            category = categoryRepository.findOne(categoryRequest.getId());

            if(category != null) {
                return category;
            }

        } catch (Exception e) {
            LOGGER.error("Unable to find category", e);
        }

        throw new CategoryNotFoundException(categoryRequest.getId());
    }

    private Status getStatus(String status) {

        Status petStatus;

        try {

            petStatus = Status.valueOf(status);

        } catch (IllegalArgumentException e) {

            LOGGER.error("Unable to find status", e);
            return Status.PENDING;
        }

        return petStatus;
    }
}
