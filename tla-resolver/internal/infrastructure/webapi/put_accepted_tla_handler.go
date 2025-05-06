package webapi

import (
	"contextmapper.org/tla-resolver/internal/application"
	"contextmapper.org/tla-resolver/internal/domain/tla"
	"contextmapper.org/tla-resolver/internal/infrastructure/persistence"
	"contextmapper.org/tla-resolver/internal/infrastructure/persistence/internal_repos"
	"encoding/json"
	"fmt"
	"github.com/aws/aws-lambda-go/events"
	"github.com/aws/aws-sdk-go/aws/session"
	"github.com/aws/aws-sdk-go/service/dynamodb"
	"github.com/aws/aws-sdk-go/service/dynamodb/dynamodbattribute"
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
	err = UnmarshalStreamImage(dynamoDbEventRecord.Change.NewImage, &acceptedTLAGroup)
	if err != nil {
		return false, err
	}

	sess := session.Must(session.NewSessionWithOptions(session.Options{
		SharedConfigState: session.SharedConfigEnable,
	}))

	dynamodbClient := dynamodb.New(sess)

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

// As recommended by a user on StackOverflow:
// https://stackoverflow.com/a/50017398/4618781
func UnmarshalStreamImage(attribute map[string]events.DynamoDBAttributeValue, out interface{}) error {
	dbAttrMap := make(map[string]*dynamodb.AttributeValue)

	for k, v := range attribute {
		var dbAttr dynamodb.AttributeValue
		bytes, marshalErr := v.MarshalJSON()
		if marshalErr != nil {
			return marshalErr
		}
		err := json.Unmarshal(bytes, &dbAttr)
		if err != nil {
			return err
		}
		dbAttrMap[k] = &dbAttr
	}
	return dynamodbattribute.UnmarshalMap(dbAttrMap, out)
}
