package com.urlkin.librato.core

import org.codehaus.jettison.json.JSONObject

/**
  * Created by prateek on 15/12/15.
  */
class Gauge(name: String, value: Number) extends Measurement(name = name, value = value) {

  var summarization: String = null
  var shouldAggregate: Boolean = false

  override def toJSON: JSONObject = {
    val json = super.toJSON
    if (summarization != null)
      json.put("summarize_function", summarization)
    json.put("aggregate", shouldAggregate)
    json
  }

  def setTime(time: Long): Gauge = {
    measureTime = time
    this
  }

  def setSummarisation(value: String): Gauge = {
    this.summarization = value
    this
  }

  def aggregate: Gauge = {
    this.shouldAggregate = true
    this
  }

  def setPeriod(value: Int) = {
    this.period = value
    this
  }
}

object GaugeSummarization {
  val AVERAGE = "average"
  val SUM = "sum"
  val COUNT = "count"
  val MIN = "min"
  val MAX = "max"
}
