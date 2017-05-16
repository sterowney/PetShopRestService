package com.thebluecabrio.petstore.exceptions;

/**
 * Created by stevenrowney on 16/05/2017.
 */
public class PetNotFoundException extends RuntimeException {

    public PetNotFoundException(Long id) {
        super(String.format("Pet not found %s", id));
    }
}
