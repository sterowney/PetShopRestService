package com.thebluecabrio.petstore.service;

import com.thebluecabrio.petstore.domain.Pet;
import com.thebluecabrio.petstore.service.requests.CreatePetRequest;
import com.thebluecabrio.petstore.service.requests.PetsRequest;
import com.thebluecabrio.petstore.service.requests.UpdatePetRequest;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Created by stevenrowney on 16/05/2017.
 */
public interface PetService {

    /**
     * Get pets
     *
     * @param petsRequest
     * @return
     */
    @PreAuthorize("hasAuthority('READ_PETS')")
    Page<Pet> getPets(PetsRequest petsRequest);

    /**
     * Create a new pet
     *
     * @param createPetRequest
     * @return
     */
    @PreAuthorize("hasAuthority('CREATE_PETS')")
    Pet createPet(CreatePetRequest createPetRequest);

    /**
     * Update an existing pet
     *
     * @param updatePetRequest
     * @return
     */
    @PreAuthorize("hasAuthority('UPDATE_PETS')")
    Pet updatePet(UpdatePetRequest updatePetRequest);

    /**
     * Get a pet by id
     *
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('READ_PETS')")
    Pet getPet(Long id);

    /**
     * Delete an existing pet
     *
     * @param id
     */
    @PreAuthorize("hasAuthority('DELETE_PETS')")
    void deletePet(Long id);
}
