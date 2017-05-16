package com.thebluecabrio.petstore.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by stevenrowney on 16/05/2017.
 */
@EqualsAndHashCode
@Getter
@Setter
@Entity(name = "pet")
public class Pet extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @OneToMany(cascade = { CascadeType.ALL })
    private Set<PhotoUrl> photoUrls = new LinkedHashSet();

    @OneToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "pet_tag",
            joinColumns = { @JoinColumn(name = "pet_id", referencedColumnName = "id", nullable = false) },
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id")
    )
    private Set<Tag> tags = new LinkedHashSet();

    @Enumerated(EnumType.STRING)
    private Status status;


    /**
     * Add a photo url
     *
     * @param id
     * @param url
     */
    public void addPhotoUrl(Long id, String url) {

        PhotoUrl photoUrl = new PhotoUrl();
        photoUrl.setName(url);
        photoUrl.setId(id);

        getPhotoUrls().add(photoUrl);
    }

    /**
     * Add a photo url
     *
     * @param url
     */
    public void addPhotoUrl(String url) {
        addPhotoUrl(null, url);
    }

    /**
     * Add a tag
     *
     * @param tagName
     */
    public void addTag(Long id, String tagName) {

        Tag tag = new Tag();
        tag.setName(tagName);
        tag.setId(id);

        getTags().add(tag);
    }

    /**
     * Add a tag
     *
     * @param tagName
     */
    public void addTag(String tagName) {
        addTag(null, tagName);
    }
}
