package io.devcon5.vertx.services;

import io.vertx.ext.web.Router;

/**
 *
 */
public class TestService extends AbstractService<TestService> {

  @Override
  public Router createRouter() {

    return Router.router(vertx());
  }

  @Override
  public String mountPoint() {

    return "/test";
  }
}
