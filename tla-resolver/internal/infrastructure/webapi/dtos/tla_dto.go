package dtos

type TLADto struct {
	Name                string   `json:"name"`
	Meaning             string   `json:"meaning"`
	AlternativeMeanings []string `json:"alternative_meanings"`
	Link                string   `json:"link"`
}

func NewTLADto(name string, meaning string) *TLADto {
	return &TLADto{
		Name:    name,
		Meaning: meaning,
	}
}
