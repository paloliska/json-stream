package bug.liska;

import java.time.Duration;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.crypto.Data;
import org.jboss.resteasy.reactive.RestStreamElementType;
import org.jboss.resteasy.reactive.common.util.RestMediaType;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.smallrye.mutiny.subscription.FixedDemandPacer;

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
