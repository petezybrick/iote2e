package com.pzybrick.iote2e.stream.validic;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class ValidicBodyDeserializer extends StdDeserializer<ValidicBody> { 	
	private static final long serialVersionUID = 1L;
	private ObjectMapper objectMapper;
	 
    public ValidicBodyDeserializer(ObjectMapper objectMapper) { 
    	super( (JavaType)null);
        this.objectMapper = objectMapper;
    } 
 
    public ValidicBodyDeserializer(Class<?> vc) { 
        super(vc); 
    }


	@Override
	public ValidicBody deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		String schemaName = node.get("schema_name").asText();
		ValidicBody validicBody = null;
		if( "Biometric".equals(schemaName)) {
			validicBody = objectMapper.treeToValue(node, Biometric.class);
		} else if( "Diabete".equals(schemaName)) {
			validicBody = objectMapper.treeToValue(node, Diabete.class);
		} else throw new IOException("Deserialization must be implemented for SchemaName: " + schemaName );
		return validicBody;
	}
}
