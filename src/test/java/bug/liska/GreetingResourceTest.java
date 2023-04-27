package bug.liska;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResourceManager;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.jboss.resteasy.reactive.RestStreamElementType;
import org.jboss.resteasy.reactive.common.util.RestMediaType;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.Duration;
import java.util.logging.Logger;

@QuarkusTest
@TestHTTPEndpoint(GreetingResource.class)
public class GreetingResourceTest {

    Logger log = Logger.getLogger(GreetingResourceTest.class.getName());

    @Test
    public void testHelloEndpoint() {
        RestClientBuilder.newBuilder()
                .baseUri(URI.create(TestHTTPResourceManager.getUri()))
                .build(GreetingClient.class)
                .hello()
                .runSubscriptionOn(Infrastructure.getDefaultExecutor())
                .onItem()
                .invoke((item) -> log.info("Client onItem"))
                .subscribe()
                .withSubscriber(AssertSubscriber.create(3))
                .awaitItems(3, Duration.ofSeconds(5));
    }
}

@Path("/hello")
interface GreetingClient {
    @GET
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    @Produces(RestMediaType.APPLICATION_STREAM_JSON)
    Multi<ResponseData> hello();
}
