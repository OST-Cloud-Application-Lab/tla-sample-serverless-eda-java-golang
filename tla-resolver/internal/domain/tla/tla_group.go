package tla

import (
	"github.com/aws/aws-sdk-go-v2/feature/dynamodb/attributevalue"
	"github.com/aws/aws-sdk-go-v2/service/dynamodb/types"
)

type TLAGroup struct {
	Name        string                     `json:"name" dynamodbav:"name"`
	Description string                     `json:"description" dynamodbav:"description"`
	Tlas        []*ThreeLetterAbbreviation `json:"tlas" dynamodbav:"tlas"`
}

func (group TLAGroup) GetKey() map[string]types.AttributeValue {
	name, err := attributevalue.Marshal(group.Name)
	if err != nil {
		panic(err)
	}
	return map[string]types.AttributeValue{"name": name}
}
