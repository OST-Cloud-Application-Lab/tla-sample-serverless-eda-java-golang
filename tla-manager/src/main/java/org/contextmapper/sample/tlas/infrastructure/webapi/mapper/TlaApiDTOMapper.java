package org.contextmapper.sample.tlas.infrastructure.webapi.mapper;

import org.contextmapper.sample.tlas.domain.tla.TLAGroup;
import org.contextmapper.sample.tlas.domain.tla.ThreeLetterAbbreviation;
import org.contextmapper.sample.tlas.infrastructure.webapi.dtos.TLADto;
import org.contextmapper.sample.tlas.infrastructure.webapi.dtos.TLAGroupDto;

public class TlaApiDTOMapper {

    private TlaApiDTOMapper() {
        // hide constructor
    }

    public static TLAGroupDto tlaGroupToDto(final TLAGroup group) {
        return new TLAGroupDto(group.getName().toString(),
                group.getDescription(),
                group.getTLAs().stream()
                        .map(tla -> new TLADto(tla.getName().toString(), tla.getMeaning())
                                .alternativeMeanings(tla.getAlternativeMeanings())
                                .link(tla.getLink()))
                        .toList()
        );
    }

    public static TLAGroup createTlaGroupDtoToTlaGroup(final TLAGroupDto groupDto) {
        var builder = new TLAGroup.TLAGroupBuilder(groupDto.getName())
                .withDescription(groupDto.getDescription());
        if (groupDto.getTlas() != null) {
            for (TLADto tlaDto : groupDto.getTlas()) {
                builder.withTLA(tlaDtoToTla(tlaDto));
            }
        }
        return builder.build();
    }

    public static ThreeLetterAbbreviation tlaDtoToTla(final TLADto tlaDto) {
        return new ThreeLetterAbbreviation.TLABuilder(tlaDto.getName())
                .withMeaning(tlaDto.getMeaning())
                .withLink(tlaDto.getLink())
                .withAlternativeMeanings(tlaDto.getAlternativeMeanings())
                .build();
    }

}
