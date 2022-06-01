
package com.biperf.core.service.ids;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class IDSRepositoryFactory
{
  @Autowired
  @Qualifier( "IDSRepositoryImpl" )
  private IDSRepository original;

  public IDSRepository getRepo()
  {
    return original;
  }

}
