package io.devcon5.vertx.services;

import io.vertx.ext.web.Router;

/**
 * just another service to properly test service discovery
 */
public class TestService2 extends AbstractService<TestService2> {

  @Override
  public Router createRouter() {

    return Router.router(vertx());
  }

  @Override
  public String mountPoint() {

    return "/test2";
  }
}
