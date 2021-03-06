package org.example.agent;

import com.mulesoft.agent.exception.AgentEncryptionException;
import com.mulesoft.agent.exception.ApplicationValidationException;
import com.mulesoft.agent.services.ApplicationValidator;
import com.mulesoft.agent.services.EncryptionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.agent.exceptions.BusinessNotAllowedException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Map;

/**
 * Hello world!
 *
 */
@Named("MuleAgentHelloValidator")
@Singleton
public class MuleAgentHelloValidator implements ApplicationValidator {

    private static final Logger LOGGER = LogManager.getLogger(MuleAgentHelloValidator.class);

    public String getType() {
        return "helloValidator";
    }

    public String getName() {
        return "defaultHelloValidator";
    }

    @Inject
    EncryptionService encryptionService;

    public void validate(Map<String, Object> args) throws ApplicationValidationException {

        // Values injected by the MuleAgentApplicationValidator service
        String applicationName = (String) args.get("_APPLICATION_NAME");
        String applicationFilePath = (String) args.get("_APPLICATION_FILE_PATH");
        Map<String, String> applicationProperties = (Map) args.get("_APPLICATION_PROPERTIES");

        // Validator configuration values
        String allowedBusiness = (String) args.get("business");
        String welcomeMessage = (String) args.get("welcomeMessage");
        String errorMessage = (String) args.get("errorMessage");
        String secret = (String) args.get("secret");

        LOGGER.info("Values injected by the service:");
        LOGGER.info("\tApplication name: {}", applicationName);
        LOGGER.info("\tApplication file path: {}", applicationFilePath);
        LOGGER.info("\tApplication properties: {}", applicationProperties);

        LOGGER.info("Validator configurations:");
        LOGGER.info("\tAllowed Business: {}", allowedBusiness);
        LOGGER.info("\tWelcome message: {}", welcomeMessage);
        LOGGER.info("\tSecret: {}", secret);

        String secretPlainText = null;

        try {
            secretPlainText = encryptionService.decrypt(secret);
        } catch (AgentEncryptionException e) {
            LOGGER.error(e);
        }
        LOGGER.info("\tSecret (plain text): {}", secretPlainText);


        String applicationBusiness = (applicationProperties == null) ? null : applicationProperties.get("business");

        if (applicationBusiness != null && applicationBusiness.equals(allowedBusiness)) {
            LOGGER.info(welcomeMessage);
            return;
        }

        throw new BusinessNotAllowedException(errorMessage);
    }
}