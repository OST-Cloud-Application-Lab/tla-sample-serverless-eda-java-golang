package tla

type ThreeLetterAbbreviation struct {
	Name                string   `json:"name" dynamodbav:"name"`
	Meaning             string   `json:"meaning" dynamodbav:"meaning"`
	AlternativeMeanings []string `json:"alternative_meanings" dynamodbav:"alternative_meanings"`
	Link                string   `json:"link" dynamodbav:"link"`
	Status              string   `json:"status" dynamodbav:"status"`
}
