package com.thebluecabrio.petstore.service.requests;

import lombok.Getter;

/**
 * Created by stevenrowney on 16/05/2017.
 */
@Getter
public class LookupItem {

    private Long id;
    private String name;

    public LookupItem() {
        // default constructor
    }

    public LookupItem(String name) {
        this.name = name;
    }

    public LookupItem(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
