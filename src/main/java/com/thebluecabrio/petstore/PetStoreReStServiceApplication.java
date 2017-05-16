package com.thebluecabrio.petstore;

import com.thebluecabrio.petstore.domain.Category;
import com.thebluecabrio.petstore.domain.Pet;
import com.thebluecabrio.petstore.domain.Status;
import com.thebluecabrio.petstore.domain.repository.CategoryRepository;
import com.thebluecabrio.petstore.domain.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import javax.annotation.PostConstruct;

@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
@SpringBootApplication
public class PetStoreReStServiceApplication {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public static void main(String[] args) {
        SpringApplication.run(PetStoreReStServiceApplication.class, args);
    }

    @PostConstruct
    public void init() throws Exception {

        Category dog = new Category();
        dog.setName("Dog");

        Category cat = new Category();
        cat.setName("Cat");

        Category puppy = new Category();
        puppy.setName("Puppy");

        categoryRepository.save(dog);
        categoryRepository.save(cat);
        categoryRepository.save(puppy);

        Pet alfie = new Pet();
        alfie.setName("Alfie");
        alfie.setStatus(Status.AVAILABLE);
        alfie.setCategory(dog);
        alfie.addPhotoUrl("http://www.dogster.com/wp-content/uploads/2015/05/dog-breeds-airedale-indoors-lying-down.jpg");
        alfie.addTag("naughty");

        Pet joey = new Pet();
        joey.setName("Joey");
        joey.setStatus(Status.PENDING);
        joey.setCategory(cat);
        joey.addPhotoUrl("http://www.cat-breeds-encyclopedia.com/images/tuxedo-cat-portrait.jpg");
        joey.addTag("sleepy");

        Pet jacques = new Pet();
        jacques.setName("Jacques");
        jacques.setStatus(Status.SOLD);
        jacques.setCategory(cat);
        jacques.addPhotoUrl("http://www.publicdomainpictures.net/pictures/90000/velka/ginger-cat-14011197866ER.jpg");
        jacques.addTag("smart");

        petRepository.save(alfie);
        petRepository.save(joey);
        petRepository.save(jacques);

        Pet newPet;
        for(int i = 1; i < 150; i++) {

            newPet = new Pet();
            newPet.setName("Pet "+i);
            newPet.setStatus(Status.PENDING);
            newPet.setCategory(cat);
            newPet.addPhotoUrl("https://placehold.it/"+i+"x"+i);
            newPet.addTag("tag-"+i);

            petRepository.save(newPet);
        }
    }
}
