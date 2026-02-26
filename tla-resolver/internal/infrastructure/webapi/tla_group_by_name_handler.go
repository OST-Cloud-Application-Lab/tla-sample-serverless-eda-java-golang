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

func TlaGroupByNameHandler(request events.APIGatewayProxyRequest) (events.APIGatewayProxyResponse, error) {
	name := request.PathParameters["groupName"]

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

	group, err := appService.FindGroupByName(name)
	if err != nil {
		return ResponseOk("{}")
	}

	tlaGroupDto := mapper.MapTLAGroupToDto(group)

	groupJson, err := json.Marshal(tlaGroupDto)
	if err != nil {
		fmt.Println("Error marshalling single TLA group:", err)
		return ResponseError(500, err)
	}

	return ResponseOk(string(groupJson))
}
