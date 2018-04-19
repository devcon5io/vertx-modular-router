package io.devcon5.vertx.util;

import java.util.function.Function;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

/**
 * Helper to deal with futures
 */
public final class Futures {


  private Futures(){}

  /**
   * Creates a handler for Void-Futures.
   * @param future
   *
   * @param <T>
   * @return
   */
  public static <T> Handler<AsyncResult<T>> completer(Future<Void> future){
    return completer(future, t -> null);
  }

  /**
   * Creates a handler that passes the result of an async operation to future after mapping it to the result type
   * @param future
   *  the future to complete (or fail) upon completion
   * @param resultMapper
   *  a mapper to transform the result to the accept-type of the future
   * @param <T>
   * @param <R>
   * @return
   */
  public static <T,R> Handler<AsyncResult<T>> completer(Future<R> future, Function<T,R> resultMapper){
    return completion -> {
      if(completion.succeeded()){
        future.complete(resultMapper.apply(completion.result()));
      } else {
        future.fail(completion.cause());
      }
    };
  }

}
