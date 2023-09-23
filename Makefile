DEBUG_5005=-Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
FLYWAY_MIGRATE= flyway:migrate
FLYWAY_CLEAN= flyway:clean
FLYWAY_REPAIR= flyway:repair
FLYWAY_DB_USER=-Dflyway.user=postgres
FLYWAY_DB_PASSWORD= -Dflyway.password=s3cr3tp4ssw0rd
FLYWAY_DB_URL=-Dflyway.url=jdbc:postgresql://localhost:5432/users

run-local: start ## Starts the application in local environment with all dependencies
	SPRING_PROFILES_ACTIVE=local mvn spring-boot:run ${DEBUG_5005}

reset-local: ## Restarts all containers
	- $(shell docker-compose down && docker-compose up -d && SPRING_PROFILES_ACTIVE=local mvn spring-boot:run ${DEBUG_5005} )

remove-local: ## Remove all containers and dependencies
	- $(shell docker-compose down)

clear-local: ## Remove all containers, volumes and dependencies. This action can't be undone
	- $(shell docker-compose down -v --remove-orphans)

start:  ## Run containers and execute default configurations
#	- ./init-scripts/clear-docker-containers.sh
	-# sleep 1
	- docker-compose up -d
	-# sleep 3 # wait for the localstack to start
	-# ./init-scripts/docker-compose-init-script.sh

stop: ## Stop containers but it won’t remove them
	- $(shell docker-compose stop)

reset: remove start## Reset all containers and dependencies

remove: ## Remove all containers and dependencies
	- $(shell docker-compose down)

clean: ## Remove all containers, volumes and dependencies. This action can't be undone
	- $(shell docker-compose down -v --remove-orphans)

flyway-clean: ## Execute the database migration clean
	- mvn $(FLYWAY_CLEAN) $(FLYWAY_DB_URL) $(FLYWAY_DB_USER) $(FLYWAY_DB_PASSWORD)

flyway-migrate: ## Execute the database migration
	- mvn $(FLYWAY_MIGRATE) $(FLYWAY_DB_URL) $(FLYWAY_DB_USER) $(FLYWAY_DB_PASSWORD)

flyway-reset: flyway-clean flyway-migrate ## Resets the migration of flyway

help: ## Show makefile helper
	- @printf '\e[1;33m%-6s\e[m' "Makefile available commands"
	- @echo ''
	- @grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}'

test: start ## Execute Test (Unit and Integration)
	- mvn -B clean verify

.DEFAULT_GOAL := help
.PHONY:= help