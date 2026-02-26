package webapi

import (
	"context"
	"encoding/json"
	"fmt"
	"log"

	"contextmapper.org/tla-resolver/internal/application"
	"contextmapper.org/tla-resolver/internal/infrastructure/persistence"
	"contextmapper.org/tla-resolver/internal/infrastructure/persistence/internal_repos"
	"contextmapper.org/tla-resolver/internal/infrastructure/webapi/mapper"

	"github.com/aws/aws-lambda-go/events"
	"github.com/aws/aws-sdk-go-v2/config"
	"github.com/aws/aws-sdk-go-v2/service/dynamodb"
)

func TlaGroupsHandler(_ events.APIGatewayProxyRequest) (events.APIGatewayProxyResponse, error) {

	ctx := context.TODO()

	cfg, err := config.LoadDefaultConfig(ctx)
	if err != nil {
		log.Fatalf("unable to load SDK config, %v", err)
	}

	dynamodbClient := dynamodb.NewFromConfig(cfg)

	// Initialize your repository here
	repository := internal_repos.NewDynamoDBRepository(dynamodbClient)

	appService := application.NewTLAGroupAppService(
		persistence.NewTLAGroupRepositoryImpl(repository))

	groups, err := appService.FindAllTLAGroups()
	if err != nil {
		fmt.Println("Error fetching TLA groups:", err)
		return ResponseError(500, err)
	}

	tlaGroupDTOList := mapper.MapTLAGroupListToDto(groups)

	groupsArrayString, err := json.Marshal(tlaGroupDTOList)
	if err != nil {
		fmt.Println("Error marshalling TLA groups:", err)
		return ResponseError(500, err)
	}

	return ResponseOk(string(groupsArrayString))
}
