#!/bin/bash
cd /workspaces/srv-usuario-poc
mvn clean compile -e 2>&1 | tee build.log
