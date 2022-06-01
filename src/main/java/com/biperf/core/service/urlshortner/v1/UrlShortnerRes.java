/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.urlshortner.v1;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class UrlShortnerRes implements Serializable
{
  private static final long serialVersionUID = 1L;

  private String ip;
  private String url;
  private String title;
  private String keyword;
  private String shorturl;
  private String createdAt;
  private String status;
  private String message;

  public String getIp()
  {
    return ip;
  }

  public void setIp( String ip )
  {
    this.ip = ip;
  }

  public String getUrl()
  {
    return url;
  }

  public void setUrl( String url )
  {
    this.url = url;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle( String title )
  {
    this.title = title;
  }

  public String getKeyword()
  {
    return keyword;
  }

  public void setKeyword( String keyword )
  {
    this.keyword = keyword;
  }

  public String getShorturl()
  {
    return shorturl;
  }

  public void setShorturl( String shorturl )
  {
    this.shorturl = shorturl;
  }

  public String getCreatedAt()
  {
    return createdAt;
  }

  public void setCreatedAt( String createdAt )
  {
    this.createdAt = createdAt;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage( String message )
  {
    this.message = message;
  }

}
