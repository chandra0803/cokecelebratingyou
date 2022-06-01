/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.urlshortner.impl;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.urlshortner.UrlShortnerRepositoryFactory;
import com.biperf.core.service.urlshortner.UrlShortnerService;

@Service( "UrlShortnerService" )
public class UrlShortnerServiceImpl implements UrlShortnerService
{
  @Autowired
  private UrlShortnerRepositoryFactory urlShortnerRepositoryFactory;

  @Override
  public String getShortUrl( String url ) throws ServiceErrorException
  {
    return urlShortnerRepositoryFactory.getRepo().getShortUrl( buildTitle(), url );
  }

  private String buildTitle()
  {
    return "dm" + "_" + System.currentTimeMillis() + "_" + RandomStringUtils.randomAlphanumeric( 7 );
  }
}
