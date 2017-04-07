package tesla.cposcanner.parser;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import tesla.cposcanner.models.TeslaModel;

@JsonIgnoreProperties
public class TeslaParser {
	
	private final ObjectMapper mapper;
	
	@Inject
	public TeslaParser(final ObjectMapper mapper){
		this.mapper = mapper;
	}

	public List<TeslaModel> parse(String data) throws JsonParseException, JsonMappingException, IOException{
		final TypeReference<List<TeslaModel>> mapType = new TypeReference<List<TeslaModel>>(){};
		final List<TeslaModel> result = mapper.readValue(data, mapType);
		return result;
	}
}
