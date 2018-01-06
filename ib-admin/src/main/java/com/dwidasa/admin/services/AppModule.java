package com.dwidasa.admin.services;

import java.io.IOException;
import java.net.URL;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ValidationDecorator;
import org.apache.tapestry5.Validator;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.annotations.Value;
import org.apache.tapestry5.ioc.internal.util.ClasspathResource;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.BindingFactory;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.FieldTranslatorSource;
import org.apache.tapestry5.services.LocalizationSetter;
import org.apache.tapestry5.services.MarkupRenderer;
import org.apache.tapestry5.services.MarkupRendererFilter;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;
import org.chenillekit.quartz.services.JobSchedulingBundle;
import org.slf4j.Logger;

import com.dwidasa.admin.custom.CustomLocalizationSetter;
import com.dwidasa.admin.custom.CustomValidationDecorator;
import com.dwidasa.admin.filter.AuthenticationFilter;
import com.dwidasa.admin.services.scheduler.MidnightBundle;
import com.dwidasa.admin.services.scheduler.QueuedBundle;
import com.dwidasa.admin.validators.AcrossField;
import com.dwidasa.admin.validators.FutureDate;
import com.dwidasa.admin.validators.RequiredIf;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.task.MidnightExecutor;
import com.dwidasa.engine.service.task.QueuedTransactionExecutor;

/**
 * This module is automatically included as part of the Tapestry IoC Registry, it's a good place to
 * configure and extend Tapestry, or to place your own service definitions.
 */
public class AppModule {
    public static void bind(ServiceBinder binder) {
        binder.bind(GenericSelectModelFactory.class);
        binder.bind(SessionManager.class);
        binder.bind(MenuManager.class);
    }

    public static void contributeApplicationDefaults(
            MappedConfiguration<String, String> configuration) {
        // Contributions to ApplicationDefaults will override any contributions to
        // FactoryDefaults (with the same key). Here we're restricting the supported
        // locales to just "en" (English). As you add localised message catalogs and other assets,
        // you can extend this list of locales (it's a comma separated series of locale names;
        // the first locale name is the default when there's no reasonable match).
        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "in_ID,en");

        // The factory default is true but during the early stages of an application
        // overriding to false is a good idea. In addition, this is often overridden
        // on the command line as -Dtapestry.production-mode=false
        configuration.add(SymbolConstants.PRODUCTION_MODE, "false");

        // The application version number is incorprated into URLs for some
        // assets. Web browsers will cache assets because of the far future expires
        // header. If existing assets are changed, the version number should also
        // change, to force the browser to download new versions.
        configuration.add(SymbolConstants.APPLICATION_VERSION, "1.0-SNAPSHOT");
    }


    /**
     * This is a service definition, the service will be named "TimingFilter". The interface,
     * RequestFilter, is used within the RequestHandler service pipeline, which is built from the
     * RequestHandler service configuration. Tapestry IoC is responsible for passing in an
     * appropriate Logger instance. Requests for static resources are handled at a higher level, so
     * this filter will only be invoked for Tapestry related requests.
     * <p/>
     * <p/>
     * Service builder methods are useful when the implementation is inline as an inner class
     * (as here) or require some other kind of special initialization. In most cases,
     * use the static bind() method instead.
     * <p/>
     * <p/>
     * If this method was named "build", then the service id would be taken from the
     * service interface and would be "RequestFilter".  Since Tapestry already defines
     * a service named "RequestFilter" we use an explicit service id that we can reference
     * inside the contribution method.
     */
    public RequestFilter buildTimingFilter(final Logger log) {
        return new RequestFilter() {
            public boolean service(Request request, Response response, RequestHandler handler)
                    throws IOException {
                long startTime = System.currentTimeMillis();

                try {
                    // The responsibility of a filter is to invoke the corresponding method
                    // in the handler. When you chain multiple filters together, each filter
                    // received a handler that is a bridge to the next filter.

                    return handler.service(request, response);
                } finally {
                    long elapsed = System.currentTimeMillis() - startTime;

                    log.info(String.format("Request time: %d ms", elapsed));
                }
            }
        };
    }

    /**
     * This is a contribution to the RequestHandler service configuration. This is how we extend
     * Tapestry using the timing filter. A common use for this kind of filter is transaction
     * management or filter. The @Local annotation selects the desired service by type, but only
     * from the same module.  Without @Local, there would be an error due to the other service(s)
     * that implement RequestFilter (defined in other modules).
     */
    public void contributeRequestHandler(OrderedConfiguration<RequestFilter> configuration,
                                         @Local
                                         RequestFilter filter) {
        // Each contribution to an ordered configuration has a name, When necessary, you may
        // set constraints to precisely control the invocation order of the contributed filter
        // within the pipeline.

        configuration.add("Timing", filter);
    }

    public static void contributeComponentRequestHandler(
            OrderedConfiguration<ComponentRequestFilter> configuration) {
        // Add authentication filter to each page request
        configuration.addInstance("RequiresLogin", AuthenticationFilter.class);
    }
    

    public static void contributeMarkupRenderer(OrderedConfiguration<MarkupRendererFilter> configuration,
                                                final Environment environment) {

        MarkupRendererFilter validationDecorator = new MarkupRendererFilter() {
            public void renderMarkup(MarkupWriter writer, MarkupRenderer renderer) {
            	ValidationDecorator decorator = new
                        CustomValidationDecorator(environment, null, writer);

                environment.push(ValidationDecorator.class, decorator);
                renderer.renderMarkup(writer);
                environment.pop(ValidationDecorator.class);
            }
        };

        configuration.override("ValidationDecorator", validationDecorator);
    }

    public static void contributeServiceOverride(MappedConfiguration<Class, Object> configuration) {
        configuration.addInstance(LocalizationSetter.class, CustomLocalizationSetter.class);
    }

    public static void contributeFieldValidatorSource(MappedConfiguration<String, Validator> configuration) {
        configuration.add("futureDate", new FutureDate());
        configuration.add("requiredIf", new RequiredIf());
        configuration.add("acrossField", new AcrossField());
    }

    public static void contributeComponentMessagesSource(@Value("/com/dwidasa/admin/Messages.properties")
                                                         Resource resource,
                                                         OrderedConfiguration<Resource> configuration) {
        configuration.add("Messages", resource);
    }
    
    public static void contributeBindingSource( 
            MappedConfiguration<String,BindingFactory> configuration, 
            FieldTranslatorSource fieldTranslatorSource, 
            TypeCoercer typeCoercer ) { 

        configuration.add("numberFormat", new NumberFormatBindingFactory( fieldTranslatorSource, typeCoercer )); 
    } 

    public static void contributeSchedulerFactory(OrderedConfiguration<URL> configuration)
    {
    	Resource configResource = new ClasspathResource("quartz.properties"); 
    	configuration.add("configuration", configResource.toURL()); 
    }
    
    public static void contributeQuartzSchedulerManager(OrderedConfiguration<JobSchedulingBundle> configuration,
    		@Inject CacheManager cacheManager,    		
    		@Inject QueuedTransactionExecutor queuedTransactionExecutor
		)
    {
    	//configuration.add("midnight", new MidnightBundle(cacheManager, midnightExecutor));
    	configuration.add("queuedJob", new QueuedBundle(cacheManager, queuedTransactionExecutor));
    }
}
