package bug.liska;

import io.smallrye.mutiny.Multi;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestStreamElementType;
import org.jboss.resteasy.reactive.common.util.RestMediaType;

import java.time.Duration;

@Path("/hello")
public class GreetingResource {

    @GET
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    @Produces(RestMediaType.APPLICATION_STREAM_JSON)
    public Multi<ResponseData> hello() {
        return Multi.createFrom()
                .ticks()
                .every(Duration.ofSeconds(2))
                .log()
                .onItem()
                .transform((Long tick) -> new ResponseData("tick " + tick));
    }
}

record ResponseData(
        String just
) {
}
