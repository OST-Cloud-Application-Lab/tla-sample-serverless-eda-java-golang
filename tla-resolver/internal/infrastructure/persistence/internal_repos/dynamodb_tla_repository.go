package internal_repos

import (
	. "contextmapper.org/tla-resolver/internal/domain/tla"
	"fmt"
	"github.com/aws/aws-sdk-go/aws"
	"github.com/aws/aws-sdk-go/service/dynamodb"
	"github.com/aws/aws-sdk-go/service/dynamodb/dynamodbattribute"
	"os"
)

func (r *DynamoDBRepository) FindById(name string) (*TLAGroup, error) {
	result, err := r.client.GetItem(&dynamodb.GetItemInput{
		TableName: aws.String(os.Getenv("TLA_TABLE_NAME")),
		Key: map[string]*dynamodb.AttributeValue{
			"name": {
				S: aws.String(name),
			},
		},
	})

	if err != nil {
		fmt.Println(err.Error())
		return nil, err
	}

	if len(result.Item) == 0 {
		return nil, fmt.Errorf("TLAGroup with name %s not found", name)
	}

	tlaGroup := &TLAGroup{}

	err = dynamodbattribute.UnmarshalMap(result.Item, tlaGroup)
	if err != nil {
		fmt.Println("Failed to unmarshal Record", err)
		return nil, err
	}

	return tlaGroup, nil
}

func (r *DynamoDBRepository) FindAll() ([]*TLAGroup, error) {
	params := &dynamodb.ScanInput{
		TableName: aws.String(os.Getenv("TLA_TABLE_NAME")),
	}

	result, err := r.client.Scan(params)

	if err != nil {
		fmt.Println("Failed to scan items:", err)
		return nil, err
	}

	tlaGroups := make([]*TLAGroup, 0)
	for _, i := range result.Items {
		tlaGroup := &TLAGroup{}

		err = dynamodbattribute.UnmarshalMap(i, tlaGroup)
		if err != nil {
			fmt.Println("Failed to unmarshal Record", err)
			return nil, err
		}
		tlaGroups = append(tlaGroups, tlaGroup)
	}

	return tlaGroups, nil
}

func (r *DynamoDBRepository) PutAcceptedTLA(acceptedTLAGroup *TLAGroup) error {
	av, err := dynamodbattribute.MarshalMap(acceptedTLAGroup)
	if err != nil {
		fmt.Println("Failed to marshal Record",
			err)
	}
	// upsert the item in the table
	_, err = r.client.PutItem(&dynamodb.PutItemInput{
		TableName: aws.String(os.Getenv("TLA_TABLE_NAME")),
		Item:      av,
	})
	if err != nil {
		fmt.Println("Failed to put item in table", err)
		return err
	}
	fmt.Println("Successfully put item in table")
	return nil
}
