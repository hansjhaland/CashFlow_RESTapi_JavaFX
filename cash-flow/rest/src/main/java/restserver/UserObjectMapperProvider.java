package restserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;
import json.CashFlowPersistence;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserObjectMapperProvider implements ContextResolver<ObjectMapper> {
    
    private final ObjectMapper objectMapper;

    public UserObjectMapperProvider(){
        objectMapper = CashFlowPersistence.createObjectMapper();
    }

    @Override
    public ObjectMapper getContext(final Class<?> type){
        return objectMapper;
    }

}
