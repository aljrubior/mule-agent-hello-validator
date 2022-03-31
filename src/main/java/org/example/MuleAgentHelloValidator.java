package org.example;

import com.mulesoft.agent.exception.ArtifactValidationException;
import com.mulesoft.agent.services.ArtifactValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exceptions.BusinessNotAllowedException;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Map;

/**
 * Hello world!
 *
 */
@Named("MuleAgentHelloValidator")
@Singleton
public class MuleAgentHelloValidator implements ArtifactValidator {

    private static final Logger LOGGER = LogManager.getLogger(MuleAgentHelloValidator.class);

    public String getType() {
        return "helloValidator";
    }

    public String getName() {
        return "defaultHelloValidator";
    }

    public void validate(Map<String, Object> args) throws ArtifactValidationException {

        // Values injected by the MuleAgentApplicationValidator service
        String applicationName = (String) args.get("_APPLICATION_NAME");
        String applicationFilePath = (String) args.get("_APPLICATION_FILE_PATH");
        Map<String, String> applicationProperties = (Map) args.get("_APPLICATION_PROPERTIES");

        // Validator configuration values
        String allowedBusiness = (String) args.get("business");
        String welcomeMessage = (String) args.get("welcomeMessage");
        String errorMessage = (String) args.get("errorMessage");

        LOGGER.info("Values injected by the service:");
        LOGGER.info("\tApplication name: {}", applicationName);
        LOGGER.info("\tApplication file path: {}", applicationFilePath);
        LOGGER.info("\tApplication properties: {}", applicationProperties);

        LOGGER.info("Validator configurations:");
        LOGGER.info("\tAllowed Business: {}", allowedBusiness);
        LOGGER.info("\tWelcome message: {}", welcomeMessage);
        LOGGER.info("\tError message: {}", errorMessage);

        String applicationBusiness = (applicationProperties == null) ? null : applicationProperties.get("business");

        if (applicationBusiness != null && applicationBusiness.equals(allowedBusiness)) {
            LOGGER.info(welcomeMessage);
            return;
        }

        throw new BusinessNotAllowedException(errorMessage);
    }
}