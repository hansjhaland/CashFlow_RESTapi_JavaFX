import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path(UserRestServive.USER_REST_SERVICE_PATH)
@Produces(MediaType.APPLICATION_JSON)
public class UserRestServive {
    
    public static final String USER_REST_SERVICE_PATH = "user";
}

