package com.thebluecabrio.petstore.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * Created by stevenrowney on 16/05/2017.
 */
@Setter
@Getter
@Entity(name = "tag")
public class Tag extends BaseEntity {

}
