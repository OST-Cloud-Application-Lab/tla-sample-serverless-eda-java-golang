package persistence

import (
	"contextmapper.org/tla-resolver/internal/domain/tla"
)

type TLAGroupRepository interface {
	FindByName(name string) (*tla.TLAGroup, error)
	FindAll() ([]*tla.TLAGroup, error)
	PutAcceptedTLA(acceptedTLAGroup *tla.TLAGroup) (*tla.TLAGroup, error)
}
