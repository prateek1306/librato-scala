package com.urlkin.librato.networking

import java.util.concurrent.Executors

/**
  * Created by prateek on 15/12/15.
  */

object UploadRequest {
  val NETWORK_SERVICE = Executors.newSingleThreadExecutor()
}

class UploadRequest(helper: URLConnectionHelper) {

  def upload: Unit = {
    UploadRequest.NETWORK_SERVICE.execute(new Runnable {
      override def run(): Unit = {
        helper.call
      }
    })
  }
}
