
package com.biperf.core.service.ots;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class OTSRepositoryFactory
{
  @Autowired
  @Qualifier( "OTSRepositoryImpl" )
  private OTSRepository original;

  public OTSRepository getRepo()
  {
    return original;
  }

}
