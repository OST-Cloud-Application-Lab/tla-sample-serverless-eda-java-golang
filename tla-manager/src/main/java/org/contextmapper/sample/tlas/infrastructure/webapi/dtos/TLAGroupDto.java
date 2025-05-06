package org.contextmapper.sample.tlas.infrastructure.webapi.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class TLAGroupDto {

    private String name;
    private String description;
    private List<TLADto> tlas;

    public TLAGroupDto(String name, String description, List<TLADto> tlas) {
        this.name = name;
        this.description = description;
        this.tlas = tlas;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<TLADto> getTlas() {
        return tlas;
    }

}
