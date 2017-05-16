package com.thebluecabrio.petstore.domain.repository;

import com.thebluecabrio.petstore.domain.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by stevenrowney on 16/05/2017.
 */
@Repository
public interface PetRepository extends PagingAndSortingRepository<Pet, Long> {

    Page<Pet> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
