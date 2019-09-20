package io.airbrake.javabrake;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class ResultFuture<T> implements Future<T> {
	private final CountDownLatch latch = new CountDownLatch(1);
	private T value;

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return false;
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public boolean isDone() {
		return latch.getCount() == 0;
	}

	@Override
	public T get() throws InterruptedException {
		latch.await();
		return value;
	}

	@Override
	public T get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
		if (latch.await(timeout, unit)) {
			return value;
		} else {
			throw new TimeoutException();
		}
	}

	// calling this more than once doesn't make sense, and won't work properly in this implementation. so: don't.
	void complete(T result) {
		value = result;
		latch.countDown();
	}

	public void completeExceptionally(Throwable ex) {
		latch.countDown();
	}
}