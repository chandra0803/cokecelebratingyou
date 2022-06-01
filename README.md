Project: Gradle Build Project for G6

Environment changes:
 - Add trustAllPackages="true" to the ActiveMQConnectionFactory resource in context.xml
 
Structure changes:
 - Output paths
  - Eclipse compiler outputs to build/eclipse
  - Gradle compilation outputs to build/gradle
  - Separated so incremental compilation and different compilers do not conflict

## Java Build

`./gradlew war_cm war assemble_static_content_local`

## Database Recreate

`./gradlew -Penvironment=DEV -Pconfirmation=Yes -PgseriesMachineConfig=docker.yml database_recreate`

## Docker Setup

Required environment variable: `G_TOMCAT_WEBAPPS_DIR` which is the path on your machine that you've got whatever G .war files in or that you're going to have gradle push stuff to.

1. Install Docker

2. Install AWS CLI (if you haven't already) (`brew install awscli` seems to work without admin privileges needed)

3. Follow the guide at `http://docs.aws.amazon.com/cli/latest/userguide/cli-chap-getting-started.html`

4. Run `eval $(aws ecr get-login --region us-west-2)` to get up-to-date ECR credentials.  These expire after 12 hours so you'll have to do this periodically.  You'll get something like `ERROR: unauthorized: authentication required` if they've expired.

5. Get the database ready:
	1. docker run --name gdb -p 49161:1521 -v $PWD/.db-storage:/u01/app/oracle sath89/oracle-xe-11g
	2. docker run -it --name oracleclient --link gdb:oracle -v $PWD:/tmp/host:ro 302265824077.dkr.ecr.us-west-2.amazonaws.com/oracle-client
	3. sqlplus sys/oracle@oracle AS SYSDBA @/tmp/host/db-create-user.sql
	4. ./gradlew -Penvironment=DEV -Pconfirmation=Yes -PgseriesMachineConfig=docker.yml database_recreate

6. Set `G_TOMCAT_WEBAPPS_DIR` environment variable for the location of the g6 war file that you want to be served by the Tomcat container

7. Make sure you have at least _something_ (eg g6bb8.war) in that folder to be served up :)

8. Be off the guest wifi.  For whatever reason this setup seems to fail to deploy the app when on the BIW Guest Wifi.  Home wifi seems to work fine, VPNd seems to work fine and on the network seems to work fine.

9. Run `docker-compose up`
"# cokecelebratingyou" 
"# cokecelebratingyou" 
"# cokecelebratingyou" 
