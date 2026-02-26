package internal_repos

import "github.com/aws/aws-sdk-go-v2/service/dynamodb"

type DynamoDBRepository struct {
	client *dynamodb.Client
}

func NewDynamoDBRepository(client *dynamodb.Client) *DynamoDBRepository {
	return &DynamoDBRepository{
		client: client,
	}
}
