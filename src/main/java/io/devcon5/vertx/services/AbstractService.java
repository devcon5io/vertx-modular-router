package io.devcon5.vertx.services;

import static org.slf4j.LoggerFactory.getLogger;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AbstractUser;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.web.Router;

/**
 * Defines a service containing a set of endpoints and routes
 */
public abstract class AbstractService<T extends AbstractService> implements Service<T>{

  private JsonObject config = new JsonObject();
  //default auth-provider is always permitting & authenticating
  private AuthProvider authProvider = (user, handler) -> handler.handle(Future.succeededFuture(new AbstractUser() {

    @Override
    protected void doIsPermitted(final String s, final Handler<AsyncResult<Boolean>> handler) {
      handler.handle(Future.succeededFuture(true));
    }

    @Override
    public JsonObject principal() {

      return user;
    }

    @Override
    public void setAuthProvider(final AuthProvider authProvider) {
      //not supported
    }
  }));

  /**
   * Creates a new createRouter defining the endpoints of the service.
   *
   * @return the new sub createRouter
   */
  public abstract Router createRouter();

  /**
   * The path this service's createRouter is mounted as subrouter to.
   *
   * @return
   *  the path of the mountpoint. Must begin with a single '/' and must not end with a '/'
   */
  public abstract String mountPoint();

  protected JsonObject config(){
    return config;
  }

  protected AuthProvider authProvider(){
    return authProvider;
  }

  protected Vertx vertx(){
    return Vertx.currentContext().owner();
  }
  protected Context context(){
    return Vertx.currentContext();
  }

  @Override
  public T withConfig(final JsonObject config) {
    this.config = config;
    return (T) this;
  }

  @Override
  public <A extends AuthProvider> T withAuth(final A provider) {
    this.authProvider = provider;
    return (T) this;
  }

  @Override
  public Future<Void> mount(final Router parent) {
    getLogger(Service.class).info("Mounting {} at {}", getClass().getCanonicalName(), mountPoint());
    parent.mountSubRouter(mountPoint(), createRouter());
    final Future<Void> startFuture = Future.future();
    start(startFuture);
    return startFuture;
  }

  /**
   * Implement to define long-running/blocking steps to start the service. Once the long running tasks have been
   * completed, complete the future.
   * @param startFuture
   *  the future to indicate the service startup is complete
   */
  protected void start(final Future<Void> startFuture) {
    start();
    startFuture.complete();
  }

  /**
   * Implement to provide non-blocking/fast running steps to start the service
   */
  protected void start() {
  }

}
