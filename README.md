To build program:
mvn clean package

to run program for vpc creation:
java -jar target\CodingAssessment-1.0-SNAPSHOT.jar -c aws_vpc_config.yml

to run program for ec2 creation:
java -jar target\CodingAssessment-1.0-SNAPSHOT.jar -c aws_ec2_config.yml

to run program for rds creation:
java -jar target\CodingAssessment-1.0-SNAPSHOT.jar -c aws_rds_config.yml

to run program for ec2 state update:
java -jar target\CodingAssessment-1.0-SNAPSHOT.jar -c aws_config.yml

for a failure case:
java -jar target\CodingAssessment-1.0-SNAPSHOT.jar -c aws_ec2_fail.yml

For the purposes of this exercise:
1) This code covers resource creation (ec2, vpc and rds)  with configs (aws_vpc_config.yml, aws_ec2_config.yml, aws_rds_config.yml)
2) covers ec2 state update (aws_config.yml)
3) I mocked out the AWS apis
4) I tried to abstract the aws from the generic "cloud" work as much as possible. I ran into an issue with the Properties/PropertiesInterface that I spent too much time on trying to abstract 
    (which I then conceeded to get this work done on time)
5) The code under the configHandler is to extract the yaml values 


1) did not include tags for the resources
2) did not include a key name for the ec2 (.pem)
3) tried to keep this is as simple as possible
4) One thing I noticed is that there are requirements for an ec2 to have a vpc/subnet however the api provides you a default vpc so you don't need to create one.  However, I created 
a vpc/subnet for the ec2 instances that were created in the program
5) when creating EC2/RDS I didn't do a "dry run,"describe" or "verify" (these could be mocked out as well)
6) Did not include "delete" resources - however this would be important because you would want to track all the resources that
that were created and if an exception happened during the creation of another resosurce type then we could unwind all of the creations by looking at this map and then deleting as necessary
7) I quickly created one unit test but if I had more time - I would have created more. 

I tried separate generic cloud yaml properties from aws specific ones.  It took me a while to realize the snake yaml does not cameCase or underscores in the property names.  

Overall I enjoyed the assignment, although I felt a bit of pressure to get done on time and doing this after a work day was taxing. My hope is that it covers most of what you are looking for.