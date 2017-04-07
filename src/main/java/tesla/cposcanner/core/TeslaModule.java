package tesla.cposcanner.core;

import javax.inject.Singleton;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import tesla.cposcanner.props.EmailProperties;

/**
 * Guice module Tesla Scanning Service
 * @author zhang165
 *
 */
public class TeslaModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(EmailProperties.class).in(Singleton.class);
	}
	
	@Provides
	public ObjectMapper providesObjectMapper(){
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		return mapper;
	}
	
}
