dep:
	mvn clean dependency:resolve -DskipTests
clean:
	mvn clean
build: clean
	mvn package -DskipTests

run: clean
	mvn spring-boot:run

run-fake:
	json-server --watch db.json
