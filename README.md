To build program:
mvn clean package

to run program for all resource creation:
java -jar target\CodingAssessment-1.0-SNAPSHOT.jar -c aws_full_config.yml

to run program for vpc creation:
java -jar target\CodingAssessment-1.0-SNAPSHOT.jar -c aws_create_vpc_config.yml

to run program for ec2 creation:
java -jar target\CodingAssessment-1.0-SNAPSHOT.jar -c aws_create_ec2_config.yml

to run program for rds creation:
java -jar target\CodingAssessment-1.0-SNAPSHOT.jar -c aws_create_rds_config.yml

to run program for ec2 state update:
java -jar target\CodingAssessment-1.0-SNAPSHOT.jar -c aws_update_ec2_config.yml

to run program for rds state update:
java -jar target\CodingAssessment-1.0-SNAPSHOT.jar -c aws_update_rds_config.yml

to run program for vpc state update:
java -jar target\CodingAssessment-1.0-SNAPSHOT.jar -c aws_update_vpc_config.yml

for a failure case:
java -jar target\CodingAssessment-1.0-SNAPSHOT.jar -c aws_create_ec2_fail.yml

For the purposes of this exercise:
1) This code covers resource creation (ec2, vpc and rds)  with configs
2) covers ec2 and rds state updates and tries a vpc one
3) I mocked out the AWS apis
4) Assumed that no default VPC/Subnet/Security group would be created for an ec2 or a rds.
5) The code under the configHandler is to extract the yaml values into Objects 
6) did not include tags for the resources
7) did not include a key name for the ec2 (.pem)
8) tried to keep this is as simple as possible
9) One thing I noticed is that there are requirements for an ec2 to have a vpc/subnet however the api provides you a default vpc so you don't need to create one.  However, I created 
a vpc/subnet for the ec2 instances that were created in the program
10) when creating EC2/RDS I didn't do a "dry run, or "verify" (these could be mocked out as well)
11) Include "delete" resources for VPC only - however this would be important because you would want to track all the resources that
that were created and if an exception happened during the creation of another resosurce type then we could unwind all of the creations by looking at this map and then deleting as necessary
12) I created some unit tests. More could be done
13) I did a describe before and after for VPC and RDS. For EC2 I didn't see how a describe before would be useful (lack of knowledge here) but I did a describe after
 
I tried separate generic cloud yaml properties from aws specific ones.  It took me a while to realize the snake yaml does not cameCase or underscores in the property names.  

Overall I enjoyed the assignment. My hope is that it covers most of what you are looking for.