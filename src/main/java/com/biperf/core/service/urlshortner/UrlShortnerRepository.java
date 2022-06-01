/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.urlshortner;

import org.springframework.web.client.HttpStatusCodeException;

import com.biperf.core.exception.ServiceErrorException;

public interface UrlShortnerRepository
{
  public static final String BEAN_NAME = "UrlShortnerRepository";

  public String getShortUrl( String title, String url ) throws HttpStatusCodeException, ServiceErrorException;

}
