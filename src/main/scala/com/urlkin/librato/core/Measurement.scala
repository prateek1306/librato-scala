package com.urlkin.librato.core

import org.codehaus.jettison.json.JSONObject

/**
  * Created by prateek on 12/12/15.
  */
abstract class Measurement(name: String, value: Number) {
  /** ***************************************
    * Metric properties
    * ***************************************/

  def toJSON: JSONObject = {
    val json = new JSONObject()
    json.put("name", if (validString(name)) name else throw new IllegalArgumentException("name is a compulsory parameter"))
    json.put("value", if (value != null) value else throw new IllegalArgumentException("value is a compulsory parameter"))
    if (period > -1) json.put("period", period)
    if (validString(description)) json.put("description", description)
    if (validString(displayName)) json.put("display_name", displayName)
    if (measureTime > -1) json.put("measure_time", measureTime)
    json
  }

  var period: Int = -1

  var description: String = null

  var displayName: String = null

  /** ***************************************
    * Measurement properties
    * ***************************************/

  var measureTime: Long = -1

  def validString(s: String): Boolean = {
    s != null && !s.isEmpty
  }

}