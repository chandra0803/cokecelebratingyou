
package com.biperf.core.service.rosterproxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class RosterProxyRepositoryFactory
{
  public static final String BEAN_NAME = "RosterProxyRepositoryFactory";

  @Autowired
  @Qualifier( "RosterProxyRepositoryImpl" )
  private RosterProxyRepository original;

  public RosterProxyRepository getRepo()
  {
    return original;
  }
}
