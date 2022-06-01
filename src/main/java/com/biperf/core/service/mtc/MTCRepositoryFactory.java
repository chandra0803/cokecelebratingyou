
package com.biperf.core.service.mtc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MTCRepositoryFactory
{
  @Autowired
  @Qualifier( "MTCRepositoryImpl" )
  private MTCRepository original;

  public MTCRepository getRepo()
  {
    return original;
  }

}
