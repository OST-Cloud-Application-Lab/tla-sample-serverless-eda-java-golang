package webapi

import (
	"context"
	"encoding/json"
	"fmt"
	"log"

	"contextmapper.org/tla-resolver/internal/application"
	"contextmapper.org/tla-resolver/internal/domain/tla"
	"contextmapper.org/tla-resolver/internal/infrastructure/persistence"
	"contextmapper.org/tla-resolver/internal/infrastructure/persistence/internal_repos"
	ddbconversions "github.com/aereal/go-dynamodb-attribute-conversions/v2"

	"github.com/aws/aws-lambda-go/events"
	"github.com/aws/aws-sdk-go-v2/config"
	"github.com/aws/aws-sdk-go-v2/feature/dynamodb/attributevalue"
	"github.com/aws/aws-sdk-go-v2/service/dynamodb"
)

func PutAcceptedTLAHandler(event events.EventBridgeEvent) (bool, error) {
	fmt.Println("PutAcceptedTLAHandler")

	var dynamoDbEventRecord events.DynamoDBEventRecord
	err := json.Unmarshal(event.Detail, &dynamoDbEventRecord)
	if err != nil {
		fmt.Println("Error unmarshalling event detail:", err)
		return false, err
	}

	var acceptedTLAGroup tla.TLAGroup
	m := ddbconversions.AttributeValueMapFrom(dynamoDbEventRecord.Change.NewImage)
	err = attributevalue.UnmarshalMap(m, &acceptedTLAGroup)
	if err != nil {
		fmt.Println("Error unmarshalling TLA group detail:", err)
		return false, err
	}

	cfg, err := config.LoadDefaultConfig(context.TODO())
	if err != nil {
		log.Fatalf("unable to load SDK config, %v", err)
	}

	dynamodbClient := dynamodb.NewFromConfig(cfg)

	repository := internal_repos.NewDynamoDBRepository(dynamodbClient)

	appService := application.NewTLAGroupAppService(
		persistence.NewTLAGroupRepositoryImpl(repository))

	err = appService.PutAcceptedTLA(acceptedTLAGroup)
	if err != nil {
		fmt.Println("Error putting accepted TLA:", err)
		return false, err
	}

	fmt.Println("Successfully put accepted TLA")
	return true, nil
}
