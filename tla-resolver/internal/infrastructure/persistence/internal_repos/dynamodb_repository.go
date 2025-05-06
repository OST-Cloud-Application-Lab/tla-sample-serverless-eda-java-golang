package internal_repos

import "github.com/aws/aws-sdk-go/service/dynamodb"

type DynamoDBRepository struct {
	client *dynamodb.DynamoDB
}

func NewDynamoDBRepository(client *dynamodb.DynamoDB) *DynamoDBRepository {
	return &DynamoDBRepository{
		client: client,
	}
}
