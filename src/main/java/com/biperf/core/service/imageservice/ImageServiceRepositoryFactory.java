/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.imageservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ImageServiceRepositoryFactory
{
  @Autowired
  @Qualifier( "ImageServiceRepositoryImpl" )
  private ImageServiceRepository original;

  public ImageServiceRepository getRepo()
  {
    return original;
  }
}
