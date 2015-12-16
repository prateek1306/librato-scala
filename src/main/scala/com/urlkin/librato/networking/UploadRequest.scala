package com.urlkin.librato.networking

import java.util.concurrent.Executors

/**
  * Created by prateek on 15/12/15.
  */

object UploadRequest {
  val NETWORK_SERVICE = Executors.newSingleThreadExecutor()
}

class UploadRequest(helper: URLConnectionHelper) {

  private var response: Response = null

  def isSuccessful: Boolean = {
    response != null && response.responseCode / 100 == 2
  }

  def upload: Response = {
    UploadRequest.NETWORK_SERVICE.execute(new Runnable {
      override def run(): Unit = {
        response = helper.call
      }
    })
    response
  }
}
