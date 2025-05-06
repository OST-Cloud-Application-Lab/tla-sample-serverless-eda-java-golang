package main

import (
	"contextmapper.org/tla-resolver/internal/infrastructure/webapi"
	"github.com/aws/aws-lambda-go/lambda"
)

func main() {
	lambda.Start(webapi.TlasHandler)
}
