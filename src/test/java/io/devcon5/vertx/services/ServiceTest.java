package io.devcon5.vertx.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class ServiceTest {

  @Rule
  public RunTestOnContext context = new RunTestOnContext();

  @Test
  public void mountAll() throws Exception {

    Router parent = Router.router(context.vertx());

    JsonObject config = new JsonObject();
    AuthProvider authProvider = mock(AuthProvider.class);

    List<Future> services = Service.mountAll(parent, config, authProvider);

    assertEquals(2, services.size());

    List<Route> routes = parent.getRoutes();
    assertEquals(2,routes.size());
    assertEquals("/test", routes.get(0).getPath());
    assertEquals("/test2", routes.get(1).getPath());
  }

}
