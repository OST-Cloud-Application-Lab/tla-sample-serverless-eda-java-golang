package org.contextmapper.sample.tlas.infrastructure.webapi.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class TLADto {

    private String name;
    private String meaning;
    private Set<String> alternativeMeanings;
    private String link;

    public TLADto(String name, String meaning) {
        this.name = name;
        this.meaning = meaning;
    }

    public TLADto alternativeMeanings(Set<String> alternativeMeanings) {
        this.alternativeMeanings = alternativeMeanings;
        return this;
    }

    public TLADto link(String link) {
        this.link = link;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getMeaning() {
        return meaning;
    }

    public Set<String> getAlternativeMeanings() {
        return alternativeMeanings;
    }

    public String getLink() {
        return link;
    }
}
