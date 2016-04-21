#!/bin/bash

echo "create Jar"
mvn clean compile assembly:single

echo "Test running"
cd target
mv pewpew-1.0-SNAPSHOT-jar-with-dependencies.jar multiplayer.jar

echo "Sending"
scp multiplayer.jar pewpew@pewpew.pro:pewpew_project
cd ..

echo "Ready"
