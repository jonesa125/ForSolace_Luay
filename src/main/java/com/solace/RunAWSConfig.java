package src.com.solace;

import com.solace.cloud.aws.resource.AwsResourceFactory;
import com.solace.configHandler.*;
import com.solace.cloud.CloudProviderFactory;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.*;
import java.io.InputStream;


public class RunAWSConfig {
    public static String AWS = "aws";
    private static final Logger logger = LoggerFactory.getLogger(RunAWSConfig.class);

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("c", "config", true, "Name of the configuration YAML file in the resources directory");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("c")) {
                String configFileName = cmd.getOptionValue("c");
                String configFilePath = "resources/" + configFileName; // Assuming the resources folder is at the root

                Yaml yaml = new Yaml();
                InputStream inputStream = RunAWSConfig.class.getClassLoader().getResourceAsStream(configFileName);
                if (inputStream == null) {
                    logger.error("File not found: " + configFileName);
                }

                RootConfig rootConfig = yaml.loadAs(inputStream, RootConfig.class);
                if (rootConfig != null && rootConfig.getIac() != null) {
                    IacConfig iacConfig = rootConfig.getIac();
                    if (iacConfig != null && iacConfig.getCloud() != null) {
                        CloudConfig cloudConfig = iacConfig.getCloud();
                        logger.info("provider is: " + cloudConfig.getProvider());
                        CloudProviderFactory.getResourceManager(cloudConfig);
                    }
                }
            } else {
                logger.error("Configuration file is required.");
            }
        } catch (ParseException e) {
            logger.error("Error parsing command line arguments: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unknown error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

