package io.devcon5.vertx.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import org.junit.Test;

/**
 *
 */
public class FuturesTest {

  @Test
  public void completer_handleFailedResult() throws Exception {

    final Future<Void> fut = Future.future();
    final RuntimeException cause = new RuntimeException();
    final AsyncResult<String> result = Future.failedFuture(cause);

    Futures.<String>completer(fut).handle(result);


    assertTrue(fut.failed());
    assertEquals(cause, fut.cause());
  }

  @Test
  public void completer_handleSucceededResult() throws Exception {

    final Future<Void> fut = Future.future();
    final AsyncResult<String> result = Future.succeededFuture("good");

    Futures.<String>completer(fut).handle(result);


    assertTrue(fut.succeeded());
    assertNull(fut.result());
  }

  @Test
  public void completerWithResultMapper() throws Exception {

    final Future<String> fut = Future.future();
    final AsyncResult<Integer> result = Future.succeededFuture(123);

    Futures.<Integer,String>completer(fut, Object::toString).handle(result);

    assertTrue(fut.succeeded());
    assertEquals("123", fut.result());
  }

}
