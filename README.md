## Install the Validator in the Mule Runtime

The corresponding JAR should be added under the lib folder within the mule-agent plugin, which is contained in the plugins folder of the Mule instance.

For example, $MULE_HOME/server-plugins/mule-agent-plugin/lib/mule-agent-hello-validator.jar.

## Mule Agent Moratorium Validator Configuration

In the following configuration, we are going to implement rules for the hello validator.

File: $MULE_HOME/conf/mule-agent.yml

```
services:
  mule.agent.artifact.validator.service:
    enabled: true
    validators:
    - type: helloValidator
      name: defaultHelloValidator
      enabled: true
      args:
        business: finance
        welcomeMessage: 'Hi, welcome to Finance Cluster Runtimes'
        errorMessage: 'I am sorry you not allowed to deploy in this Cluster'
        secret: '![PBEWITHSHA1ANDDESEDE,wFE1D5V4DMb0uG77mzU+gibrlmnj3Kzb]'
```

### Log4j Configuration

File: $MULE_HOME/conf/log4j2.xml

```
 <AsyncLogger name="org.example" level="INFO"/>
```

### Test the Name Validator

Deploy an application that has an invalid name.

Command

```
curl -X PUT 'http://localhost:9999/mule/applications/app-01' \
-H 'Content-Type: application/json' \
-d '{
  "url": "file:/tmp/app-01.jar",
  "configuration": {
    "mule.agent.application.properties.service": {
      "applicationName": "app-01",
      "properties": {
          "business": "account"
      }
    }
  }
}'
```

Response

```
{
  "type": "class org.example.exceptions.BusinessNotAllowedException",
  "message": "I am sorry you not allowed to be deployed in this Cluster"
}
```

Logs

```
INFO  2022-03-31 17:22:22,726 [qtp353278882-67] [processor: ; event: ] com.mulesoft.agent.services.application.AgentApplicationService: Deploying the app-01 application from URL file:/tmp/app-01.jar
INFO  2022-03-31 17:22:22,734 [qtp353278882-67] [processor: ; event: ] org.example.MuleAgentHelloValidator: Values injected by the service:
INFO  2022-03-31 17:22:22,734 [qtp353278882-67] [processor: ; event: ] org.example.MuleAgentHelloValidator: 	Application name: app-01
INFO  2022-03-31 17:22:22,734 [qtp353278882-67] [processor: ; event: ] org.example.MuleAgentHelloValidator: 	Application file path: /tmp/mule-received-artifact-589680011267239839/app-01.jar
INFO  2022-03-31 17:22:22,734 [qtp353278882-67] [processor: ; event: ] org.example.MuleAgentHelloValidator: 	Application properties: {business=account}
INFO  2022-03-31 17:22:22,735 [qtp353278882-67] [processor: ; event: ] org.example.MuleAgentHelloValidator: Validator configurations:
INFO  2022-03-31 17:22:22,735 [qtp353278882-67] [processor: ; event: ] org.example.MuleAgentHelloValidator: 	Allowed Business: finance
INFO  2022-03-31 17:22:22,735 [qtp353278882-67] [processor: ; event: ] org.example.MuleAgentHelloValidator: 	Welcome message: Hi, welcome to Finance Cluster Runtimes
INFO  2022-03-31 17:22:22,736 [qtp353278882-67] [processor: ; event: ] org.example.MuleAgentHelloValidator: 	Secret: ![PBEWITHSHA1ANDDESEDE,wFE1D5V4DMb0uG77mzU+gibrlmnj3Kzb]
INFO  2022-03-31 17:22:22,756 [qtp353278882-67] [processor: ; event: ] org.example.MuleAgentHelloValidator: 	Secret (plain text): my-secret-value
ERROR 2022-03-31 17:22:22,762 [qtp353278882-67] [processor: ; event: ] com.mulesoft.agent.external.handlers.deployment.ApplicationsRequestHandler: Error performing the deployment of app-01. Cause: BusinessNotAllowedException: I am sorry you not allowed to deploy in this Cluster
```