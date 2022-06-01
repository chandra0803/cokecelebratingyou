
package com.biperf.core.service.serviceanniversary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ServiceAnniversaryRepositoryFactory
{
  @Autowired
  @Qualifier( "serviceAnniversaryRepositoryImpl" )
  private ServiceAnniversaryRepository original;

  public ServiceAnniversaryRepository getRepo()
  {
    return original;
  }

}
