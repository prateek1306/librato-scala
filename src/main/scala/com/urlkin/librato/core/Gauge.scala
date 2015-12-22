package com.urlkin.librato.core

import org.codehaus.jettison.json.JSONObject

/**
  * Created by prateek on 15/12/15.
  */
class Gauge(name: String, value: Number) extends Measurement(name = name, value = value) {

  override def toJSON: JSONObject = {
    val json = super.toJSON
    json
  }

  def setTime(time: Long): Gauge = {
    measureTime = time
    this
  }
}
