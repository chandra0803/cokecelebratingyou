
package com.biperf.core.service.underarmour;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.service.system.SystemVariableService;

@Component
public class UARepositoryFactory
{

  @Autowired
  private SystemVariableService systemVariableService;

  @Autowired
  @Qualifier( "UARepoMock" )
  private UARepository mock;

  @Autowired
  @Qualifier( "UARepositoryImpl" )
  private UARepository original;

  public UARepository getRepo()
  {

    PropertySetItem propertyByName = systemVariableService.getPropertyByName( SystemVariableService.UNDERARMOUR_REPO );

    if ( propertyByName != null && "mock".equalsIgnoreCase( propertyByName.getStringVal() ) )
    {
      return mock;
    }

    return original;

  }

}
