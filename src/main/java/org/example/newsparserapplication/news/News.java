/**
 * <==================================>
 * Copyright (c) 2024 Ilya Sukhina.*
 * <=================================>
 */
package org.example.newsparserapplication.news;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.time.LocalDateTime;

/**
 * The News class represents a news article with a headline, description, and publication time.
 * * This class uses Jackson annotations for JSON serialization/deserialization and Lombok annotations
 * to reduce boilerplate code for constructors, getters, setters, and builder pattern.
 **/

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class News {

    @JsonProperty("headline")
    private String headline;

    @JsonProperty("description")
    private String description;

    @JsonProperty("publicationTime")
    private LocalDateTime publicationTime;

}
