package com.dwidasa.ib.services;

import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ValidationDecorator;
import org.apache.tapestry5.Validator;
import org.apache.tapestry5.internal.services.GenericValueEncoderFactory;
import org.apache.tapestry5.internal.services.RequestConstants;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.annotations.Order;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.annotations.Value;
import org.apache.tapestry5.plastic.MethodAdvice;
import org.apache.tapestry5.plastic.MethodInvocation;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.ComponentRequestHandler;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.LocalizationSetter;
import org.apache.tapestry5.services.MarkupRenderer;
import org.apache.tapestry5.services.MarkupRendererFilter;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.services.ValueEncoderFactory;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.CustomerDevice;
import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.service.facade.NativeAuthenticationService;
import com.dwidasa.ib.annotations.Authenticate2;
import com.dwidasa.ib.annotations.NoValidate;
import com.dwidasa.ib.annotations.SessionValidate;
import com.dwidasa.ib.custom.AccountViewEncoder;
import com.dwidasa.ib.custom.CustomLocalizationSetter;
import com.dwidasa.ib.custom.CustomValidationDecorator;
import com.dwidasa.ib.custom.CustomerDeviceEncoder;
import com.dwidasa.ib.filter.AuthenticationFilter;
import com.dwidasa.ib.services.impl.AccountResourceImpl;
import com.dwidasa.ib.services.impl.AdministrationResourceImpl;
import com.dwidasa.ib.services.impl.AeroticketingResourceImpl;
import com.dwidasa.ib.services.impl.AuthenticationResourceImpl;
import com.dwidasa.ib.services.impl.BatchResourceImpl;
import com.dwidasa.ib.services.impl.CardSecurityResourceImpl;
import com.dwidasa.ib.services.impl.DelimaResourceImpl;
import com.dwidasa.ib.services.impl.InboxResourceImpl;
import com.dwidasa.ib.services.impl.KioskResourceImpl;
import com.dwidasa.ib.services.impl.LocationResourceImpl;
import com.dwidasa.ib.services.impl.PaymentResourceImpl;
import com.dwidasa.ib.services.impl.PurchaseResourceImpl;
import com.dwidasa.ib.services.impl.ServerResourceImpl;
import com.dwidasa.ib.services.impl.SynchronizationResourceImpl;
import com.dwidasa.ib.services.impl.TrainResourceImpl;
import com.dwidasa.ib.services.impl.TransactionResourceImpl;
import com.dwidasa.ib.services.impl.TransferResourceImpl;
import com.dwidasa.ib.validators.AcrossField;
import com.dwidasa.ib.validators.FutureDate;
import com.dwidasa.ib.validators.RequiredIf;

/**
 * This module is automatically included as part of the Tapestry IoC Registry,
 * it's a good place to configure and extend Tapestry, or to place your own
 * service definitions.
 */
public class AppModule {
	private static Logger logger = Logger.getLogger(AppModule.class);

	public static void bind(ServiceBinder binder) {
		binder.bind(SessionManager.class);
		binder.bind(GenericSelectModelFactory.class);
		binder.bind(OtpManager.class);

		binder.bind(AccountResource.class, AccountResourceImpl.class);
		binder.bind(AuthenticationResource.class,
				AuthenticationResourceImpl.class);
		binder.bind(AdministrationResource.class,
				AdministrationResourceImpl.class);
		binder.bind(InboxResource.class, InboxResourceImpl.class);
		binder.bind(LocationResource.class, LocationResourceImpl.class);
		binder.bind(PaymentResource.class, PaymentResourceImpl.class);
		binder.bind(PurchaseResource.class, PurchaseResourceImpl.class);
		binder.bind(SynchronizationResource.class,
				SynchronizationResourceImpl.class);
		binder.bind(TransferResource.class, TransferResourceImpl.class);
		binder.bind(DelimaResource.class, DelimaResourceImpl.class);
		binder.bind(TransactionResource.class, TransactionResourceImpl.class);
		binder.bind(CardSecurityResource.class, CardSecurityResourceImpl.class);
		binder.bind(ServerResource.class, ServerResourceImpl.class);
		binder.bind(KioskResource.class, KioskResourceImpl.class);
		binder.bind(BatchResource.class, BatchResourceImpl.class);
		binder.bind(AeroticketingResource.class,
				AeroticketingResourceImpl.class);
		binder.bind(TrainResource.class,
				TrainResourceImpl.class);
	}

	public static void contributeApplicationDefaults(
			MappedConfiguration<String, String> configuration) {
		// Contributions to ApplicationDefaults will override any contributions
		// to
		// FactoryDefaults (with the same key). Here we're restricting the
		// supported
		// locales to just "en" (English). As you add localised message catalogs
		// and other assets,
		// you can extend this list of locales (it's a comma separated series of
		// locale names;
		// the first locale name is the default when there's no reasonable
		// match).
		configuration.add(SymbolConstants.SUPPORTED_LOCALES, "in,en");

		// The factory default is true but during the early stages of an
		// application
		// overriding to false is a good idea. In addition, this is often
		// overridden
		// on the command line as -Dtapestry.production-mode=false
		configuration.add(SymbolConstants.PRODUCTION_MODE, "true");

		final String applicationVersion = "1.2.3-20140603";

		// The application version number is incorprated into URLs for some
		// assets. Web browsers will cache assets because of the far future
		// expires
		// header. If existing assets are changed, the version number should
		// also
		// change, to force the browser to download new versions.
		configuration.add(SymbolConstants.APPLICATION_VERSION,
				applicationVersion);

	}

	@Contribute(ComponentRequestHandler.class)
	public static void contributeComponentRequestHandler(
			OrderedConfiguration<ComponentRequestFilter> configuration) {
		// Add authentication filter to each page request
		configuration.addInstance("RequiresLogin", AuthenticationFilter.class);

	}

	@Contribute(RequestHandler.class)
	public void disableAssetDirListing(
			OrderedConfiguration<RequestFilter> configuration,
			@Symbol(SymbolConstants.APPLICATION_VERSION) final String applicationVersion) {
		configuration.add("DisableDirListing", new RequestFilter() {
			@Override
			public boolean service(Request request, Response response,
					RequestHandler handler) throws IOException {
				final String assetFolder = "/assets/"
						+ applicationVersion + "/"
						+ RequestConstants.CONTEXT_FOLDER;
				if (request.getPath().startsWith(assetFolder)
						&& request.getPath().endsWith("/")) {
					return false;
				} else {
					return handler.service(request, response);
				}
			}
		}, "before:AssetDispatcher");
	}

	public static void contributeApplication(Configuration<Object> singletons,
			ObjectLocator locator) {
		singletons.add(locator.getService(AccountResource.class));
		singletons.add(locator.getService(AuthenticationResource.class));
		singletons.add(locator.getService(AdministrationResource.class));
		singletons.add(locator.getService(InboxResource.class));
		singletons.add(locator.getService(LocationResource.class));
		singletons.add(locator.getService(PaymentResource.class));
		singletons.add(locator.getService(PurchaseResource.class));
		singletons.add(locator.getService(SynchronizationResource.class));
		singletons.add(locator.getService(TransferResource.class));
		singletons.add(locator.getService(DelimaResource.class));
		singletons.add(locator.getService(TransactionResource.class));
		singletons.add(locator.getService(CardSecurityResource.class));
		singletons.add(locator.getService(ServerResource.class));
		singletons.add(locator.getService(KioskResource.class));
		singletons.add(locator.getService(BatchResource.class));
		singletons.add(locator.getService(AeroticketingResource.class));
		singletons.add(locator.getService(TrainResource.class));
	}

	@Match("*Resource")
	public static void adviseException(MethodAdviceReceiver receiver) {
		MethodAdvice advice = new MethodAdvice() {
			public void advise(MethodInvocation invocation) {
				BusinessException be = null;
				try {
					invocation.proceed();
				} catch (BusinessException e) {
					e.printStackTrace();
					be = e;
					e.printStackTrace();
				} catch (Exception e) {
					logger.info("e.getMessage()=" + e.getMessage());
					e.printStackTrace();
					be = new BusinessException("IB-0600");
				}

				if (be != null) {
					Object result = PojoJsonMapper.toJson(be);
					invocation.setReturnValue(result);
				}
			}
		};

		receiver.adviseAllMethods(advice);
	}

	@Match("*Resource")
	@Order("after:Exception")
	public static void adviseAuthenticator(MethodAdviceReceiver receiver,
			final NativeAuthenticationService authenticator) {

		MethodAdvice credentialValidator = new MethodAdvice() {
			public void advise(MethodInvocation invocation) {
				Long customerId = (Long) invocation.getParameter(0);
				String deviceId = (String) invocation.getParameter(1);
				String sessionId = (String) invocation.getParameter(2);
				String token = (String) invocation.getParameter(3);

				if (authenticator.authenticateToken(customerId, deviceId,
						sessionId, token)) {
					invocation.proceed();
				}
			}
		};

		MethodAdvice credentialValidator2 = new MethodAdvice() {
			public void advise(MethodInvocation invocation) {
				Long customerId = (Long) invocation.getParameter(0);
				String deviceId = (String) invocation.getParameter(1);
				String sessionId = (String) invocation.getParameter(2);
				String token = (String) invocation.getParameter(3);
				String json = (String) invocation.getParameter(4);

				if (authenticator.authenticateToken2(customerId, deviceId,
						sessionId, token, json)) {
					invocation.proceed();
				}
			}
		};

		MethodAdvice sessionValidator = new MethodAdvice() {
			public void advise(MethodInvocation invocation) {
				Long customerId = (Long) invocation.getParameter(0);
				String sessionId = (String) invocation.getParameter(1);

				if (authenticator
						.validateCustomerSession(customerId, sessionId)) {
					invocation.proceed();
				}
			}
		};

		for (Method method : receiver.getInterface().getMethods()) {
			if (method.isAnnotationPresent(SessionValidate.class)) {
				receiver.adviseMethod(method, sessionValidator);
			} else if (method.isAnnotationPresent(NoValidate.class)) {
				// do nothing
			} else if (method.isAnnotationPresent(Authenticate2.class)) {
				receiver.adviseMethod(method, credentialValidator2);
			} else {
				receiver.adviseMethod(method, credentialValidator);
			}
		}
	}

	public static void contributeMarkupRenderer(
			OrderedConfiguration<MarkupRendererFilter> configuration,
			final Environment environment) {

		MarkupRendererFilter validationDecorator = new MarkupRendererFilter() {
			public void renderMarkup(MarkupWriter writer,
					MarkupRenderer renderer) {
				ValidationDecorator decorator = new CustomValidationDecorator(
						environment, writer);

				environment.push(ValidationDecorator.class, decorator);
				renderer.renderMarkup(writer);
				environment.pop(ValidationDecorator.class);
			}
		};

		configuration.override("ValidationDecorator", validationDecorator);
		configuration.override("InjectDefaultStylesheet", null);
	}

	public static void contributeServiceOverride(
			MappedConfiguration<Class, Object> configuration) {
		configuration.addInstance(LocalizationSetter.class,
				CustomLocalizationSetter.class);
		// configuration.add(ClasspathURLConverter.class, new
		// CustomClasspathURLConverter());
	}

	public static void contributeValueEncoderSource(
			MappedConfiguration<Class, ValueEncoderFactory> configuration) {
		configuration.add(CustomerDevice.class,
				GenericValueEncoderFactory.create(new CustomerDeviceEncoder()));
		configuration.add(AccountView.class,
				GenericValueEncoderFactory.create(new AccountViewEncoder()));
	}

	public static void contributeFieldValidatorSource(
			MappedConfiguration<String, Validator> configuration) {
		configuration.add("futureDate", new FutureDate());
		configuration.add("requiredIf", new RequiredIf());
		configuration.add("acrossField", new AcrossField());
	}

	public static void contributeComponentMessagesSource(
			@Value("/com/dwidasa/ib/Messages.properties") Resource resource,
			OrderedConfiguration<Resource> configuration) {
		configuration.add("Messages", resource);
	}

	public static void contributeKaptchaProducer(
			MappedConfiguration<String, String> configuration) {

		configuration.add("kaptcha.textproducer.char.string", "23456789");
	}
}
