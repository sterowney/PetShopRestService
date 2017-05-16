package com.thebluecabrio.petstore.service.requests;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

/**
 * Created by stevenrowney on 16/05/2017.
 */
@Getter
@Setter
public class PetsRequest {

    private Pageable pageable;
    private String name;
}
