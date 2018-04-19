package io.devcon5.vertx.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 */
@RunWith(VertxUnitRunner.class)
public class AbstractServiceTest {

  @Rule
  public RunTestOnContext context = new RunTestOnContext();

  @Test
  public void defaultConfig() throws Exception {

    TestService service = new TestService();

    assertNotNull(service.config());
    assertTrue(service.config().isEmpty());
  }

  @Test
  public void defaultAuth(TestContext context) throws Exception {

    TestService service = new TestService();

    assertNotNull(service.authProvider());

    Async async = context.async();
    service.authProvider().authenticate(new JsonObject().put("username","anyUser"), result -> {
      context.assertTrue(result.succeeded());
      context.assertEquals("anyUser", result.result().principal().getString("username"));
      async.complete();
    });
  }

  @Test
  public void withConfig() throws Exception {

    JsonObject config = new JsonObject();
    TestService service = new TestService().withConfig(config);

    assertNotNull(service);
    assertEquals(config, service.config());
  }

  @Test
  public void withAuth() throws Exception {
    AuthProvider  authProvider = mock(AuthProvider.class);
    TestService service = new TestService().withAuth(authProvider);

    assertNotNull(service);
    assertEquals(authProvider, service.authProvider());
  }


  @Test
  public void vertx() throws Exception {

    TestService service = new TestService();

    assertNotNull(service.vertx());
  }

  @Test
  public void context() throws Exception {
    TestService service = new TestService();

    assertNotNull(service.context());
  }


  @Test
  public void mount() throws Exception {

    Router parent = Router.router(context.vertx());

    Future fut = new TestService().mount(parent);

    assertTrue(fut.isComplete());

    List<Route> routes = parent.getRoutes();
    assertEquals(1,routes.size());
    assertEquals("/test", routes.get(0).getPath());
  }


  @Test
  public void startWithFuture() throws Exception {
    Future<Void> fut = Future.future();
    new TestService().start(fut);

    assertTrue(fut.isComplete());
  }


}
