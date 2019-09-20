package io.airbrake.javabrake;

import java.util.concurrent.Future;

public interface AsyncSender {
  void setHost(String host);

  Future<Notice> send(Notice notice);
}
