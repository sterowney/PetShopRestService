package com.thebluecabrio.petstore.service;

import com.thebluecabrio.petstore.domain.Pet;
import com.thebluecabrio.petstore.domain.Status;
import com.thebluecabrio.petstore.domain.Tag;
import com.thebluecabrio.petstore.exceptions.CategoryNotFoundException;
import com.thebluecabrio.petstore.exceptions.PetNotFoundException;
import com.thebluecabrio.petstore.service.requests.CreatePetRequest;
import com.thebluecabrio.petstore.service.requests.LookupItem;
import com.thebluecabrio.petstore.service.requests.PetsRequest;
import com.thebluecabrio.petstore.service.requests.UpdatePetRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by stevenrowney on 16/05/2017.
 */
@WithMockUser(authorities = { "READ_PETS", "UPDATE_PETS", "CREATE_PETS", "DELETE_PETS" })
@RunWith(SpringRunner.class)
@SpringBootTest
public class PetServiceTest {

    @Autowired
    private PetService petService;

    @Test
    public void test_create_pet() throws Exception {

        CreatePetRequest newPet = new CreatePetRequest();
        newPet.setName("fido");
        newPet.setStatus(Status.AVAILABLE.name());
        newPet.setTags(Arrays.asList(new LookupItem("puppy")));
        newPet.setPhotoUrls(Arrays.asList(new LookupItem("http://www.bathroomreader.com/wp-content/uploads/2014/02/Fido.jpg")));

        newPet.setCategory(new LookupItem(3l, "puppy"));

        Pet newPetResponse = petService.createPet(newPet);

        assertNotNull(newPetResponse);
        assertNotNull(newPetResponse.getId());
        assertNotNull(newPetResponse.getName());
        assertNotNull(newPetResponse.getTags());
        assertNotNull(newPetResponse.getPhotoUrls());
        assertNotNull(newPetResponse.getCategory());
        assertNotNull(newPetResponse.getCategory().getId());
        assertNotNull(newPetResponse.getCategory().getName());

        petService.deletePet(newPetResponse.getId());
    }

    @Test(expected = CategoryNotFoundException.class)
    public void test_create_pet_with_unknown_category() throws Exception {

        CreatePetRequest createPetRequest = new CreatePetRequest();
        createPetRequest.setCategory(new LookupItem(9999l, "Unknown category"));

        petService.createPet(createPetRequest);
    }

    @Transactional
    @Test
    public void test_update_pet_add_new_tags_and_photo_urls() throws Exception {

        Pet joey = petService.getPet(2l);

        assertNotNull(joey);
        assertEquals("Name should be Joey", "Joey", joey.getName());
        assertEquals("Has one photo url", 1, joey.getPhotoUrls().size());
        assertEquals("Has two tag", 1, joey.getTags().size());

        UpdatePetRequest updatedJoey = new UpdatePetRequest();
        updatedJoey.setId(joey.getId());
        updatedJoey.setName(joey.getName());
        updatedJoey.setCategory(new LookupItem(joey.getCategory().getId(), joey.getCategory().getName()));
        updatedJoey.setStatus(Status.PENDING.name());

        updatedJoey.setPhotoUrls(Arrays.asList(new LookupItem("http://google.com")));
        updatedJoey.setTags(Arrays.asList(new LookupItem("tuxedo")));

        joey = petService.updatePet(updatedJoey);

        assertNotNull(joey);
        assertEquals("Has two photo url", 2, joey.getPhotoUrls().size());
        assertEquals("Has three tag", 2, joey.getTags().size());

        updatedJoey.setPhotoUrls(new ArrayList());
        updatedJoey.setTags(Arrays.asList(new LookupItem(new ArrayList<Tag>(joey.getTags()).get(0).getId(), new ArrayList<Tag>(joey.getTags()).get(0).getName())));

        joey = petService.updatePet(updatedJoey);

        assertNotNull(joey);
        assertEquals("Still has two photo url", 2, joey.getPhotoUrls().size());
        assertEquals("Still has three tag", 2, joey.getTags().size());
    }

    @Test
    public void test_get_alfie_by_id() throws Exception {

        Pet alfie = petService.getPet(1l);

        assertNotNull(alfie);
        assertEquals("Name should be alfie", "Alfie", alfie.getName());

    }

    @Test
    public void test_update_joeys_status_to_available() throws Exception {

        Pet joey = petService.getPet(2l);

        assertNotNull(joey);
        assertEquals("Name should be Joey", "Joey", joey.getName());

        UpdatePetRequest updatedJoey = new UpdatePetRequest();
        updatedJoey.setId(joey.getId());
        updatedJoey.setName(joey.getName());
        updatedJoey.setCategory(new LookupItem(joey.getCategory().getId(), joey.getCategory().getName()));

        updatedJoey.setStatus(Status.AVAILABLE.name());

        joey = petService.updatePet(updatedJoey);

        assertNotNull(joey);
        assertEquals("Name should be Joey", "Joey", joey.getName());
        assertEquals(Status.AVAILABLE, joey.getStatus());
    }

    @Test
    public void test_delete_jacques() throws Exception {

        CreatePetRequest newPet = new CreatePetRequest();
        newPet.setName("fido 2");
        newPet.setStatus(Status.AVAILABLE.name());
        newPet.setTags(Arrays.asList(new LookupItem("puppy")));
        newPet.setPhotoUrls(Arrays.asList(new LookupItem("http://www.bathroomreader.com/wp-content/uploads/2014/02/Fido.jpg")));

        newPet.setCategory(new LookupItem(3l, "puppy"));

        Pet newPetResponse = petService.createPet(newPet);

        petService.deletePet(newPetResponse.getId());

        try {

            petService.getPet(newPetResponse.getId());

            fail("expected exception not found");

        } catch (PetNotFoundException e) {
            assertTrue("If you've reached here than you've passed!", true);
        }
    }

    @Test
    public void test_find_pets() throws Exception {

        PetsRequest petsRequest = new PetsRequest();
        petsRequest.setPageable(new PageRequest(0, 10, Sort.Direction.ASC, "name"));

        Page<Pet> pets = petService.getPets(petsRequest);

        assertNotNull(pets);
    }

    @Test
    public void test_find_pets_filter_by_name() throws Exception {

        PetsRequest petsRequest = new PetsRequest();
        petsRequest.setPageable(new PageRequest(0, 10, Sort.Direction.ASC, "name"));
        petsRequest.setName("Alfie");

        Page<Pet> pets = petService.getPets(petsRequest);

        assertNotNull(pets);
        assertEquals("Only one result, because of filtering", 1, pets.getTotalElements());
    }

    @Test(expected = PetNotFoundException.class)
    public void test_get_pet_not_found() throws Exception {

        petService.getPet(99999l);
    }

    @Test(expected = PetNotFoundException.class)
    public void test_delete_pet_not_found() throws Exception {

        petService.deletePet(99999l);
    }

    @Test(expected = PetNotFoundException.class)
    public void test_update_pet_not_found() throws Exception {

        UpdatePetRequest pet = new UpdatePetRequest();
        pet.setId(99999l);

        petService.updatePet(pet);
    }

    @Test
    public void test_create_a_pet_with_invalid_status() throws Exception {

        CreatePetRequest newPet = new CreatePetRequest();
        newPet.setName("fido 2");
        newPet.setTags(Arrays.asList(new LookupItem("puppy")));
        newPet.setPhotoUrls(Arrays.asList(new LookupItem("http://www.bathroomreader.com/wp-content/uploads/2014/02/Fido.jpg")));
        newPet.setCategory(new LookupItem(3l, "puppy"));

        newPet.setStatus("INVALID_STATUS");

        Pet newPetResponse = petService.createPet(newPet);

        assertNotNull(newPetResponse);

        assertEquals("Status should default to pending", Status.PENDING, newPetResponse.getStatus());

    }
}