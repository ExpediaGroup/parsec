#!/bin/sh
#
# Copyright 2020 Expedia, Inc
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# Non-daemon script for Parsec service.

serviceName=Parsec
serviceHome=/opt/parsec                # location where service should be run from
serviceConfigHome=/etc/parse           # location of the configuration for the service

[ -r /etc/profile.d/java.sh ] && . /etc/profile.d/java.sh

JAVA_OPTS="-Dfile.encoding=UTF-8 -server -XX:NewSize=256m -XX:MaxNewSize=256m -XX:+CMSClassUnloadingEnabled -XX:+UnlockExperimentalVMOptions"

echo "Starting $serviceName"
java -cp "$serviceConfigHome:$serviceHome/bin/*:$serviceHome/lib/*" -server ${JAVA_OPTS} -Dconfig="$serviceConfigHome/config.edn" parsec.service
