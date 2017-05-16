package com.thebluecabrio.petstore.domain;

import com.thebluecabrio.petstore.domain.repository.CategoryRepository;
import com.thebluecabrio.petstore.domain.repository.PetRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by stevenrowney on 16/05/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DomainTest {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Before
    public void setUp() throws Exception {

        assertNotNull("Pet repository should not be null, failed to autowire", petRepository);
    }

    /**
     * Simple tests to see if our JPA mapping is working
     *
     * @throws Exception
     */
    @Test
    public void test_create_a_new_pet_update_and_delete() throws Exception {

        Pet dog = new Pet();
        dog.setName("Alfie");
        dog.setStatus(Status.AVAILABLE);

        dog = petRepository.save(dog);

        assertNotNull("dog should not be null after save", dog);
        assertNotNull("dog id should have a value after save", dog.getId());

        assertEquals("dog status should be available", Status.AVAILABLE, dog.getStatus());
        assertEquals("dog name should be Alfie", "Alfie", dog.getName());

        Category category = new Category();
        category.setName("Dog");

        category = categoryRepository.save(category);

        assertNotNull("category should not be null after save", category);
        assertNotNull("category id should have a value after save", category.getId());

        dog.setCategory(category);

        dog = petRepository.save(dog);

        assertNotNull("Dog should have a category", dog.getCategory());
        assertEquals("Dog category should match new category", category, dog.getCategory());

        dog.setStatus(Status.SOLD);

        dog = petRepository.save(dog);

        assertEquals("dog status should be available", Status.SOLD, dog.getStatus());

        Long dogId = dog.getId();

        petRepository.delete(dogId);

        Pet deletedDog = petRepository.findOne(dogId);

        assertNull("deleted dog should be null", deletedDog);
        assertEquals(null, deletedDog);
    }
}
