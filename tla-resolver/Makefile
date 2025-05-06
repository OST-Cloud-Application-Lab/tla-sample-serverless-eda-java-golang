BUILD_DIR=./dist

# include .env

build:
	mkdir -p ${BUILD_DIR}/tla_group_by_name
	mkdir -p ${BUILD_DIR}/tla_groups
	mkdir -p ${BUILD_DIR}/tlas
	mkdir -p ${BUILD_DIR}/put_accepted_tla
	CGO_ENABLED=0 GOARCH=amd64 GOOS=linux go build -tags lambda.norpc -ldflags "-s -w" -trimpath -o ${BUILD_DIR}/tla_group_by_name/bootstrap cmd/tla_group_by_name.go
	CGO_ENABLED=0 GOARCH=amd64 GOOS=linux go build -tags lambda.norpc -ldflags "-s -w" -trimpath -o ${BUILD_DIR}/tla_groups/bootstrap cmd/tla_groups.go
	CGO_ENABLED=0 GOARCH=amd64 GOOS=linux go build -tags lambda.norpc -ldflags "-s -w" -trimpath -o ${BUILD_DIR}/tlas/bootstrap cmd/tlas.go
	CGO_ENABLED=0 GOARCH=amd64 GOOS=linux go build -tags lambda.norpc -ldflags "-s -w" -trimpath -o ${BUILD_DIR}/put_accepted_tla/bootstrap cmd/put_accepted_tla.go
	cd ${BUILD_DIR}/tla_group_by_name && zip tla_group_by_name.zip bootstrap && cd -
	cd ${BUILD_DIR}/tla_groups && zip tla_groups.zip bootstrap && cd -
	cd ${BUILD_DIR}/tlas && zip tlas.zip bootstrap && cd -
	cd ${BUILD_DIR}/put_accepted_tla && zip put_accepted_tla.zip bootstrap && cd -

clean:
	go clean

clean_all:
	rm -rd ${BUILD_DIR}

dep:
	go mod download

deploy: clean build
	sls deploy