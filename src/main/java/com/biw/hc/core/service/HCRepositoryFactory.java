
package com.biw.hc.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class HCRepositoryFactory
{

  @Autowired
  @Qualifier( "HCRepositoryImpl" )
  private HCRepository hcRepository;

  public HCRepository getRepo()
  {
    return hcRepository;
  }

}
