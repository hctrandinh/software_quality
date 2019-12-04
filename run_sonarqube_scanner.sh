#! /bin/sh

gradle wrapper --gradle-version 4.4.1

./gradlew --continue sonarqube
#./gradlew --continue sonarqube -Dsonar.host.url=http://127.0.0.1:8080 -Dsonar.login=admin -Dsonar.password=sonaradmin456
