package tla

type ThreeLetterAbbreviation struct {
	Name                string   `json:"name"`
	Meaning             string   `json:"meaning"`
	AlternativeMeanings []string `json:"alternative_meanings"`
	Link                string   `json:"link"`
	Status              string   `json:"status"`
}
