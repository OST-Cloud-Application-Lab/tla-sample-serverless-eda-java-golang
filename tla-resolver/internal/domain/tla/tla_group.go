package tla

type TLAGroup struct {
	Name        string                     `json:"name"`
	Description string                     `json:"description"`
	Tlas        []*ThreeLetterAbbreviation `json:"tlas"`
}
