package com.thebluecabrio.petstore.exceptions;

/**
 * Created by stevenrowney on 16/05/2017.
 */
public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(Long id) {
        super(String.format("Category not found %s", id));
    }
}
