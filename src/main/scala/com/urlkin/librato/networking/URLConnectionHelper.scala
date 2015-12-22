package com.urlkin.librato.networking


import java.io._
import java.net.{HttpURLConnection, URL}

import org.codehaus.jettison.json.JSONObject

/**
  * Created by prateek on 12/12/15.
  */

class URLConnectionHelper {

  private var mUrl: String = ""
  private var encryptedAuthDetails: String = null
  private var mPostParam: JSONObject = null

  def this(url: String) {
    this()
    mUrl = url
  }

  def setAuthDetails(username: String, password: String) {
    val u = if (username != null) username else ""
    val p = if (password != null) password else ""
    val userPassword = String.format("%s:%s", u, p)
    encryptedAuthDetails = javax.xml.bind.DatatypeConverter.printBase64Binary(userPassword.getBytes("UTF-8"))
  }

  @throws(classOf[IOException])
  def call: Response = {
    var connection: HttpURLConnection = buildConnection
    if (mPostParam != null) connection = appendPostParams(connection, mPostParam)
    val responseCode: Int = connection.getResponseCode
    val isSuccess: Boolean = responseCode / 100 == 2
    val response: Response = getResponse(connection, isSuccess)
    response.setResponseCode(responseCode)
    return response
  }

  def setPostParam(jsonObject: JSONObject) {
    this.mPostParam = jsonObject
  }

  @throws(classOf[IOException])
  private def appendPostParams(connection: HttpURLConnection, param: JSONObject): HttpURLConnection = {
    val charset: String = "UTF-8"
    connection.setDoOutput(true)
    connection.setRequestProperty("Accept-Charset", charset)
    connection.setRequestProperty("Content-Type", "application/json")
    val output: OutputStream = connection.getOutputStream
    try {
      output.write(param.toString.getBytes(charset))
    } finally {
      if (output != null) output.close()
    }
    connection
  }

  @throws(classOf[IOException])
  private def buildConnection: HttpURLConnection = {
    val url: URL = new URL(mUrl)
    val connection: HttpURLConnection = url.openConnection.asInstanceOf[HttpURLConnection]
    if (encryptedAuthDetails != null) connection.setRequestProperty("Authorization", "Basic " + encryptedAuthDetails)
    connection
  }

  @throws(classOf[IOException])
  private def getResponse(connection: HttpURLConnection, isSuccess: Boolean): Response = {
    val sb: StringBuilder = {
      var s: StringBuilder = null
      if (isSuccess) s = readStream(connection.getInputStream)
      else s = readStream(connection.getErrorStream)
      s
    }
    val response: Response = new Response
    response.setStringBuilder(sb)
    response
  }

  private def readStream(inputStream: InputStream): StringBuilder = {
    val stringBuilder: StringBuilder = new StringBuilder
    var reader: BufferedReader = null
    try {
      reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))
      var c: Int = 0
      while (c != -1) {
        c = reader.read
        stringBuilder.append(c.toChar)
      }
    }
    catch {
      case e: IOException => e.printStackTrace()
    } finally {
      if (reader != null) {
        try {
          reader.close()
        }
        catch {
          case e: IOException => e.printStackTrace()
        }
      }
    }
    return stringBuilder
  }
}