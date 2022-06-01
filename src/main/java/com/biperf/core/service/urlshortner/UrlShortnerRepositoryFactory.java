/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.urlshortner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class UrlShortnerRepositoryFactory
{
  @Autowired
  @Qualifier( "UrlShortnerRepositoryImpl" )
  private UrlShortnerRepository original;

  public UrlShortnerRepository getRepo()
  {
    return original;
  }
}
