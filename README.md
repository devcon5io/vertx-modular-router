# vertx-modular-router
Lightweight API to create modular routers for Vert.x REST endpoints.


# Developing a Service

Create your own service by extending the `io.devcon5.vertx.services.AbstractService`.

- define a mount point. This is the context root relative to the root of your application. All further
 routes are defined below this mountpoint
 
- create a router. Create a router and define your routes as you would with any other Vert.x router

- create a `META-INF/services/io.devcon5.vertx.services.Service` file and put your service's fully qualified
class names in it

- Optional: if your service requires a Verticle to be deployed, this is best done by overriding the `start` method
and deploy your Verticle as you probably would in the start method of an AbstractVerticle

Example:

```java
public class TestService extends AbstractService<TestService> {

  @Override
  public Router createRouter() {

    Router router = Router.router(vertx());
    router.get("/world").handler(ctx -> ctx.response().end("Hello world"));
    return router;
  }

  @Override
  public String mountPoint() {

    return "/hello";
  }
}
```

# Build an App with modular router
In order to build an app that assembles all modular Verticles, you create your HTTP Server and a router as you would
with any other Vert.x Web application.

After creating and initializing the Router, but _before_ adding a StaticContentHandler.

The services are discovered automatically via the Java ServiceLoader mechanism when `Service.mountAll` is invoked.
The method takes three arguments:
- the parent router
- the config that is passed to _every_ service
- the authProvider for services that require authentication

```java

final AuthProvider auth = ...
final JsonObject config = ...

//create the main router
final Router router = Router.router(vertx);

//add global handler
router.route().handler(BodyHandler.create());
router.route().handler(CookieHandler.create());
router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));
router.route().handler(UserSessionHandler.create(cognitoAuth));

//mount all services that are on the classpath
Service.mountAll(router, config, auth);

//static handler should be added after everything else
router.route().handler(StaticHandler.create());
```


and finally create the server with the router to handle the requests
```java
vertx.createHttpServer().requestHandler(router::accept).listen(8080);
```
