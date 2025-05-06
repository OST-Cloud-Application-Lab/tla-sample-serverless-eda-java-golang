package dtos

type TLAGroupDTO struct {
	Name        string    `json:"name"`
	Description string    `json:"description"`
	Tlas        []*TLADto `json:"tlas"`
}

func NewTLAGroupDTO(name string, description string, tlas []*TLADto) *TLAGroupDTO {
	return &TLAGroupDTO{
		Name:        name,
		Description: description,
		Tlas:        tlas,
	}
}
