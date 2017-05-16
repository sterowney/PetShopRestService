package com.thebluecabrio.petstore.service.requests;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by stevenrowney on 16/05/2017.
 */
@Getter
@Setter
public class UpdatePetRequest extends CreatePetRequest {

    private Long id;
}
