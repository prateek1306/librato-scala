package com.urlkin.librato.core

import com.urlkin.librato.networking.{URLConnectionHelper, UploadRequest}
import org.codehaus.jettison.json.{JSONArray, JSONObject}

import scala.collection.mutable

/**
  * Created by prateek on 15/12/15.
  */

object LibratoBatch {
  val DEFAULT_BATCH_SIZE = 10
}

class LibratoBatch {

  private var batchSize = LibratoBatch.DEFAULT_BATCH_SIZE
  private var gauges: mutable.MutableList[Gauge] = new mutable.MutableList[Gauge]

  private var source = "NA"

  private val url = "https://metrics-api.librato.com/v1/metrics"
  private var username = ""
  private var token = ""


  private def validString(s: String): Boolean = {
    s != null && !s.isEmpty
  }

  def setBatchSize(size: Int) : LibratoBatch = {
    if (size > 0)
      batchSize = size
    this
  }

  def setUsername(u: String): LibratoBatch = {
    if (validString(u))
      username = u
    this
  }

  def setToken(t: String): LibratoBatch = {
    if (validString(t))
      token = t
    this
  }

  def setSource(s: String): LibratoBatch = {
    if (validString(s))
      source = s
    this
  }

  def addGauge(gauge: Gauge): LibratoBatch = {
    this.synchronized {
      gauges += gauge
    }
    onNewMetric
    this
  }

  private def onNewMetric = {
    if (gauges.size >= batchSize) post
  }

  private def prepareResponse(glist: mutable.MutableList[Gauge]): JSONObject = {
    val json = new JSONObject()
    json.put("source", source)
    val gaugeArray: JSONArray = new JSONArray()
    for (gauge <- glist) {
      gaugeArray.put(gauge.toJSON)
    }
    json.put("gauges", gaugeArray)
    return json
  }

  private def postToLibrato(json: JSONObject): Unit = {
    val wrh = new URLConnectionHelper(url)
    wrh.setPostParam(json)
    wrh.setAuthDetails(username, token)
    val helper = new UploadRequest(wrh)
    System.out.println("Uploading to librato : " + json.toString)
    helper.upload
    System.out.println("Uploaded to librato")
  }

  def post: Unit = {
    var toUpload: mutable.MutableList[Gauge] = new mutable.MutableList[Gauge]
    this.synchronized {
      toUpload = gauges
      gauges = new mutable.MutableList[Gauge]
    }

    val json = prepareResponse(toUpload)
    postToLibrato(json)
  }
}