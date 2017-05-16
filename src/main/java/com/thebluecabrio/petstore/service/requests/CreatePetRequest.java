package com.thebluecabrio.petstore.service.requests;

import com.thebluecabrio.petstore.domain.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stevenrowney on 16/05/2017.
 */
@Setter
@Getter
public class CreatePetRequest {

    private String name;
    private String status = Status.PENDING.name();
    private List<LookupItem> tags = new ArrayList();
    private List<LookupItem> photoUrls = new ArrayList();
    private LookupItem category = new LookupItem();

}
