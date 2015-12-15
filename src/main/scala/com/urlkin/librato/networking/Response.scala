package com.urlkin.librato.networking

/**
  * Created by prateek on 12/12/15.
  */
class Response {
  var stringBuilder: StringBuilder = null
  var responseCode: Int = 0

  def setStringBuilder(value: StringBuilder) = {
    this.stringBuilder = value
  }

  def setResponseCode(value: Int) = {
    this.responseCode = value
  }
}
