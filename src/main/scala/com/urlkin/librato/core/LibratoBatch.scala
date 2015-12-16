package com.urlkin.librato.core

import java.util

import com.urlkin.librato.networking.{URLConnectionHelper, UploadRequest}
import org.codehaus.jettison.json.JSONObject

/**
  * Created by prateek on 15/12/15.
  */

object LibratoBatch {
  val DEFAULT_BATCH_SIZE = 100
}

class LibratoBatch {

  private var batchSize = LibratoBatch.DEFAULT_BATCH_SIZE
  private val gauges = new util.ArrayList[Gauge]()

  private var source = "NA"

  private val url = "https://metrics-api.librato.com/v1/metrics"
  private var username = ""
  private var token = ""


  private def validString(s: String): Boolean = {
    s != null && !s.isEmpty
  }

  def setBatchSize(size: Int) = {
    if (size > 0)
      batchSize = size
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
      gauges.add(gauge)
    }
    this
  }

  def prepareResponse: JSONObject = {
    val json = new JSONObject()
    json
  }

  def postToLibrato(json: JSONObject): UploadRequest = {
    val wrh = new URLConnectionHelper(url)
    wrh.setPostParam(json)
    wrh.setAuthDetails(username, token)
    val helper = new UploadRequest(wrh)
    helper.upload
    helper
  }

  def post: UploadRequest = {
    val json = prepareResponse
    postToLibrato(json)
  }
}
