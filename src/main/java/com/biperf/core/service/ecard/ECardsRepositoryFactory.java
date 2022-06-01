
package com.biperf.core.service.ecard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ECardsRepositoryFactory
{

  @Autowired
  @Qualifier( "eCardsRepositoryImpl" )
  private ECardsRepository original;

  public ECardsRepository getRepo()
  {
    return original;
  }

}
