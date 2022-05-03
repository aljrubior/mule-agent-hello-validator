package org.example.agent.configuration.guice;

import com.google.inject.Binder;
import com.mulesoft.agent.configuration.guice.BaseModuleProvider;
import com.mulesoft.agent.services.ApplicationValidator;
import org.example.agent.MuleAgentHelloValidator;

public class MuleAgentHelloValidatorProvider extends BaseModuleProvider {
    @Override
    protected void configureModule(Binder binder) {
        bindNamedSingleton(binder, ApplicationValidator.class, MuleAgentHelloValidator.class);
    }
}
