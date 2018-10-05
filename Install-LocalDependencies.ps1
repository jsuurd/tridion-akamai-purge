param (
        [Parameter(Position=0, Mandatory=$true)]
        [string]$Path
)

function Install-JarFile
{
    param (
        [Parameter(Position=0, Mandatory=$true)]
        [string]$File,
        [Parameter(Position=1, Mandatory=$true)]
        [string]$GroupId,
        [Parameter(Position=2, Mandatory=$true)]
        [string]$ArtifactId,
        [Parameter(Position=2, Mandatory=$true)]
        [string]$Version
    )

    Invoke-Expression -Command 'mvn install:install-file -Dfile="$File" -DgroupId="$GroupId" -DartifactId="$ArtifactId" -Dversion="$Version" -Dpackaging="jar"'

    Write-Host "Installed JAR in local Maven repository [GroupId=$GroupId, ArtifactId=$ArtifactId, Version=$Version]"

    Write-Host "Maven POM dependency:
                <dependency>
                    <groupId>$GroupId</groupId>
                    <artifactId>$ArtifactId</artifactId>
                    <version>$Version</version>
                </dependency>"
}

# SDL Web 8.1.0
Install-JarFile -File "$Path\cd_deployer-8.1.0-1232.jar" -GroupId 'com.sdl.delivery' -ArtifactId 'cd_deployer' -Version '8.1.0-1232'

Install-JarFile -File "$Path\odata_api-2.0.0-1218.jar" -GroupId 'com.sdl.odata' -ArtifactId 'odata_api' -Version '2.0.0-1218'
Install-JarFile -File "$Path\odata_client-2.0.0-1218.jar" -GroupId 'com.sdl.odata' -ArtifactId 'odata_client' -Version '2.0.0-1218'
Install-JarFile -File "$Path\odata_client_api-2.0.0-1218.jar" -GroupId 'com.sdl.odata' -ArtifactId 'odata_client_api' -Version '2.0.0-1218'

Install-JarFile -File "$Path\content-client-api-8.1.0-1233.jar" -GroupId 'com.sdl.web' -ArtifactId 'content-client-api' -Version '8.1.0-1233'
Install-JarFile -File "$Path\content-model-8.1.0-1230.jar" -GroupId 'com.sdl.web' -ArtifactId 'content-client-api' -Version '8.1.0-1230'
Install-JarFile -File "$Path\discovery-client-8.1.0-1228.jar" -GroupId 'com.sdl.web' -ArtifactId 'discovery-client' -Version '8.1.0-1228'
Install-JarFile -File "$Path\discovery-client-lightweight-8.1.0-1228.jar" -GroupId 'com.sdl.web' -ArtifactId 'discovery-client-lightweight' -Version '8.1.0-1228'
Install-JarFile -File "$Path\discovery-common-8.1.0-1228.jar" -GroupId 'com.sdl.web' -ArtifactId 'discovery-common' -Version '8.1.0-1228'

Install-JarFile -File "$Path\cd_common_config-8.1.0-1219.jar" -GroupId 'com.tridion' -ArtifactId 'cd_common_config' -Version '8.1.0-1219'
Install-JarFile -File "$Path\cd_common_config_api-8.1.0-1219.jar" -GroupId 'com.tridion' -ArtifactId 'cd_common_config_api' -Version '8.1.0-1219'
Install-JarFile -File "$Path\cd_common_config_legacy-8.1.0-1219.jar" -GroupId 'com.tridion' -ArtifactId 'cd_common_config_legacy' -Version '8.1.0-1219'
Install-JarFile -File "$Path\cd_common_util-8.1.0-1219.jar" -GroupId 'com.tridion' -ArtifactId 'cd_common_util' -Version '8.1.0-1219'
Install-JarFile -File "$Path\cd_core-8.1.0-1228.jar" -GroupId 'com.tridion' -ArtifactId 'cd_core' -Version '8.1.0-1228'
Install-JarFile -File "$Path\cd_model-8.1.0-1230.jar" -GroupId 'com.tridion' -ArtifactId 'cd_model' -Version '8.1.0-1230'
