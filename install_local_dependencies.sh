#!/bin/bash
path="$1"

install_jar_file() {
  local file="$1"
  local group_id="$2"
  local artifact_id="$3"
  local version="$4"

  mvn install:install-file -Dfile="${file}" -DgroupId="${group_id}" -DartifactId="${artifact_id}" -Dversion="${version}" -Dpackaging="jar"

  echo "Installed JAR in local Maven repository [GroupId=${group_id}, ArtifactId=${artifact_id}, Version=${version}]"

  echo "Maven POM dependency:
  <dependency>
    <groupId>${group_id}</groupId>
    <artifactId>${artifact_id}</artifactId>
    <version>${version}</version>
  </dependency>"
}

# SDL Web 8.1.0
install_jar_file "$path\cd_deployer-8.1.0-1232.jar" 'com.sdl.delivery' 'cd_deployer' '8.1.0-1232'

install_jar_file "$path\odata_api-2.0.0-1218.jar" 'com.sdl.odata' 'odata_api' '2.0.0-1218'
install_jar_file "$path\odata_client-2.0.0-1218.jar" 'com.sdl.odata' 'odata_client' '2.0.0-1218'
install_jar_file "$path\odata_client_api-2.0.0-1218.jar" 'com.sdl.odata' 'odata_client_api' '2.0.0-1218'

install_jar_file "$path\content-client-api-8.1.0-1233.jar" 'com.sdl.web' 'content-client-api' '8.1.0-1233'
install_jar_file "$path\content-model-8.1.0-1230.jar" 'com.sdl.web' 'content-client-api' '8.1.0-1230'
install_jar_file "$path\discovery-client-8.1.0-1228.jar" 'com.sdl.web' 'discovery-client' '8.1.0-1228'
install_jar_file "$path\discovery-client-lightweight-8.1.0-1228.jar" 'com.sdl.web' 'discovery-client-lightweight' '8.1.0-1228'
install_jar_file "$path\discovery-common-8.1.0-1228.jar" 'com.sdl.web' 'discovery-common' '8.1.0-1228'

install_jar_file "$path\cd_common_config-8.1.0-1219.jar" 'com.tridion' 'cd_common_config' '8.1.0-1219'
install_jar_file "$path\cd_common_config_api-8.1.0-1219.jar" 'com.tridion' 'cd_common_config_api' '8.1.0-1219'
install_jar_file "$path\cd_common_config_legacy-8.1.0-1219.jar" 'com.tridion' 'cd_common_config_legacy' '8.1.0-1219'
install_jar_file "$path\cd_common_util-8.1.0-1219.jar" 'com.tridion' 'cd_common_util' '8.1.0-1219'
install_jar_file "$path\cd_core-8.1.0-1228.jar" 'com.tridion' 'cd_core' '8.1.0-1228'
install_jar_file "$path\cd_model-8.1.0-1230.jar" 'com.tridion' 'cd_model' '8.1.0-1230'
