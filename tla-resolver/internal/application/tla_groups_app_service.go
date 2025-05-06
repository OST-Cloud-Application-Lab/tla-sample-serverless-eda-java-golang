package application

import (
	"contextmapper.org/tla-resolver/internal/domain/tla"
	"contextmapper.org/tla-resolver/internal/infrastructure/persistence"
	"encoding/json"
	"fmt"
)

type TLAGroupAppService struct {
	repository persistence.TLAGroupRepository
}

func NewTLAGroupAppService(repository persistence.TLAGroupRepository) *TLAGroupAppService {
	return &TLAGroupAppService{
		repository: repository,
	}
}

func (t *TLAGroupAppService) FindAllTLAGroups() ([]*tla.TLAGroup, error) {
	tlaGroups, err := t.repository.FindAll()
	if err != nil {
		return nil, err
	}
	return tlaGroups, nil
}

func (t *TLAGroupAppService) FindAllTLAsByName(name string) ([]*tla.TLAGroup, error) {
	allTlaGroups, err := t.FindAllTLAGroups()
	if err != nil {
		return nil, err
	}

	var tlaGroups []*tla.TLAGroup
	for _, tlaGroup := range allTlaGroups {
		matchedTlas := make([]*tla.ThreeLetterAbbreviation, 0)
		for _, tla := range tlaGroup.Tlas {
			if tla.Name == name {
				matchedTlas = append(matchedTlas, tla)
			}
		}

		tlaGroup.Tlas = matchedTlas

		if len(tlaGroup.Tlas) > 0 {
			tlaGroups = append(tlaGroups, tlaGroup)
		}
	}

	return tlaGroups, nil
}

func (t *TLAGroupAppService) FindGroupByName(name string) (*tla.TLAGroup, error) {
	tlaGroup, err := t.repository.FindByName(name)
	if err != nil {
		return nil, err
	}
	return tlaGroup, nil
}

func (t *TLAGroupAppService) PutAcceptedTLA(acceptedTLAGroup tla.TLAGroup) error {
	acceptedTLAs := make([]*tla.ThreeLetterAbbreviation, 0)
	for _, incomingTLA := range acceptedTLAGroup.Tlas {
		if tla.NewStatus(incomingTLA.Status) == tla.Accepted {
			acceptedTLAs = append(acceptedTLAs, incomingTLA)
		}
	}
	acceptedTLAGroup.Tlas = acceptedTLAs

	acceptedTLA, err := t.repository.PutAcceptedTLA(&acceptedTLAGroup)
	if err != nil {
		return err
	}

	acceptedTLAJson, err := json.Marshal(acceptedTLA)
	if err != nil {
		fmt.Println("Error marshalling accepted TLA:", err)
	}
	fmt.Println("Accepted TLA JSON:", string(acceptedTLAJson))
	return nil
}
