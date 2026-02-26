package internal_repos

import (
	"context"
	"fmt"
	"os"

	. "contextmapper.org/tla-resolver/internal/domain/tla"
	"github.com/aws/aws-sdk-go-v2/aws"
	"github.com/aws/aws-sdk-go-v2/feature/dynamodb/attributevalue"
	"github.com/aws/aws-sdk-go-v2/service/dynamodb"
	"github.com/aws/aws-sdk-go-v2/service/dynamodb/types"
)

func (r *DynamoDBRepository) FindById(name string) (*TLAGroup, error) {
	result, err := r.client.GetItem(context.TODO(), &dynamodb.GetItemInput{
		TableName: aws.String(os.Getenv("TLA_TABLE_NAME")),
		Key: map[string]types.AttributeValue{
			"name": &types.AttributeValueMemberS{Value: name},
		},
	})
	if err != nil {
		return nil, err
	}

	if len(result.Item) == 0 {
		return nil, fmt.Errorf("TLAGroup with name %s not found", name)
	}

	tlaGroup := new(TLAGroup)
	if err := attributevalue.UnmarshalMap(result.Item, tlaGroup); err != nil {
		return nil, fmt.Errorf("failed to unmarshal record: %w", err)
	}

	return tlaGroup, nil
}

func (r *DynamoDBRepository) FindAll() ([]*TLAGroup, error) {
	params := &dynamodb.ScanInput{
		TableName: aws.String(os.Getenv("TLA_TABLE_NAME")),
	}

	result, err := r.client.Scan(context.TODO(), params)

	if err != nil {
		fmt.Println("Failed to scan items:", err)
		return nil, err
	}

	tlaGroups := make([]*TLAGroup, 0)
	for _, i := range result.Items {
		tlaGroup := &TLAGroup{}

		err = attributevalue.UnmarshalMap(i, tlaGroup)
		if err != nil {
			fmt.Println("Failed to unmarshal Record", err)
			return nil, err
		}
		tlaGroups = append(tlaGroups, tlaGroup)
	}

	return tlaGroups, nil
}

func (r *DynamoDBRepository) PutAcceptedTLA(acceptedTLAGroup *TLAGroup) error {
	item, err := attributevalue.MarshalMap(acceptedTLAGroup)
	if err != nil {
		fmt.Println("Failed to marshal Record",
			err)
	}
	// upsert the item in the table
	_, err = r.client.PutItem(context.TODO(), &dynamodb.PutItemInput{
		TableName: aws.String(os.Getenv("TLA_TABLE_NAME")),
		Item:      item,
	})
	if err != nil {
		fmt.Println("Failed to put item in table", err)
		return err
	}
	fmt.Println("Successfully put item in table")
	return nil
}
