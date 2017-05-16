package com.thebluecabrio.petstore.domain.repository;

import com.thebluecabrio.petstore.domain.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by stevenrowney on 16/05/2017.
 */
@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {

}
