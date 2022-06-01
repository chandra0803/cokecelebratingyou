/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 
 * @author sivanand
 * @since Jan 29, 2019
 * @version 1.0
 */

@Component
public class CompanyServiceFactory
{
  @Autowired
  @Qualifier( "CompanySetupRepositoryImpl" )
  private CompanySetupRepository original;

  public CompanySetupRepository getRepo()
  {
    return original;
  }
}
