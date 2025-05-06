package persistence

import (
	"contextmapper.org/tla-resolver/internal/domain/tla"
	. "contextmapper.org/tla-resolver/internal/infrastructure/persistence/internal_repos"
)

type TLAGroupRepositoryImpl struct {
	repository *DynamoDBRepository
}

func NewTLAGroupRepositoryImpl(repository *DynamoDBRepository) *TLAGroupRepositoryImpl {
	return &TLAGroupRepositoryImpl{
		repository: repository,
	}
}

func (r *TLAGroupRepositoryImpl) FindByName(name string) (*tla.TLAGroup, error) {
	tlaGroup, err := r.repository.FindById(name)
	if err != nil {
		return nil, err
	}
	return tlaGroup, nil
}

func (r *TLAGroupRepositoryImpl) FindAll() ([]*tla.TLAGroup, error) {
	tlaGroups, err := r.repository.FindAll()
	if err != nil {
		return nil, err
	}
	return tlaGroups, nil
}

func (r *TLAGroupRepositoryImpl) PutAcceptedTLA(acceptedTLAGroup *tla.TLAGroup) (*tla.TLAGroup, error) {
	err := r.repository.PutAcceptedTLA(acceptedTLAGroup)
	if err != nil {
		return nil, err
	}
	return acceptedTLAGroup, nil
}
