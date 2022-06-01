/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.urlshortner;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;

public interface UrlShortnerService extends SAO
{
  public static final String BEAN_NAME = "UrlShortnerService";

  public String getShortUrl( String url ) throws ServiceErrorException;

}
