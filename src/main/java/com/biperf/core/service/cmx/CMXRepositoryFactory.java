
package com.biperf.core.service.cmx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class CMXRepositoryFactory
{
  @Autowired
  @Qualifier( "cmxRepositoryImpl" )
  private CMXRepository original;

  public CMXRepository getRepo()
  {
    return original;
  }

}
