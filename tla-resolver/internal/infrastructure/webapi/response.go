package webapi

import "github.com/aws/aws-lambda-go/events"

func ResponseOk(body string) (events.APIGatewayProxyResponse, error) {
	return events.APIGatewayProxyResponse{
		Headers: map[string]string{
			"Content-Type":    "application/json",
			"X-Custom-Header": "application/json",
		},
		Body:       body,
		StatusCode: 200,
	}, nil
}

func ResponseError(statusCode int, err error) (events.APIGatewayProxyResponse, error) {
	return events.APIGatewayProxyResponse{
		StatusCode: statusCode,
		Body:       err.Error(),
	}, err
}
