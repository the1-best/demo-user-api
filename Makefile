.PHONY: build

build:
	./gradlew clean build
	docker build -t demo-user-api .

ecr_login:
	aws ecr get-login --no-include-email | sh

push: build
	docker tag demo-user-api sarayutorion/demo-user-api:latest
	docker push sarayutorion/demo-user-api:latest

init:
	git config core.hooksPath .githooks
