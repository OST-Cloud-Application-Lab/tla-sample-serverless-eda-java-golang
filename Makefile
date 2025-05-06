clean:
	rm -rf ./tla-manager/target
	rm -rf ./tla-resolver/dist

build:
	cd ./tla-manager && ./mvnw clean package

	cd ./tla-resolver && \
 	make dep && \
	make build

deploy: clean build
	sls deploy