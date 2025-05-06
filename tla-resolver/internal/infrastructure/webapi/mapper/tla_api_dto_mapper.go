package mapper

import (
	"contextmapper.org/tla-resolver/internal/domain/tla"
	"contextmapper.org/tla-resolver/internal/infrastructure/webapi/dtos"
)

func MapTLAGroupListToDto(tlaGroups []*tla.TLAGroup) []*dtos.TLAGroupDTO {
	if tlaGroups == nil {
		return nil
	}

	tlaGroupDtos := make([]*dtos.TLAGroupDTO, len(tlaGroups))
	for i, tlaGroup := range tlaGroups {
		tlaGroupDtos[i] = MapTLAGroupToDto(tlaGroup)
	}

	return tlaGroupDtos
}

func MapTLAGroupToDto(tlaGroup *tla.TLAGroup) *dtos.TLAGroupDTO {
	if tlaGroup == nil {
		return nil
	}

	tlaDtos := make([]*dtos.TLADto, len(tlaGroup.Tlas))
	for i, t := range tlaGroup.Tlas {
		tlaDtos[i] = MapTLAToDto(t)
	}

	return dtos.NewTLAGroupDTO(tlaGroup.Name, tlaGroup.Description, tlaDtos)
}

func MapTLAToDto(tla *tla.ThreeLetterAbbreviation) *dtos.TLADto {
	if tla == nil {
		return nil
	}

	tlaDto := dtos.NewTLADto(tla.Name, tla.Meaning)

	if tla.AlternativeMeanings != nil {
		tlaDto.AlternativeMeanings = tla.AlternativeMeanings
	}
	if tla.Link != "" {
		tlaDto.Link = tla.Link
	}

	return tlaDto
}
