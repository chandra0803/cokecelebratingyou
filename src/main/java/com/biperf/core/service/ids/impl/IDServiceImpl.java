
package com.biperf.core.service.ids.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.ids.IDSRepositoryFactory;
import com.biperf.core.service.ids.IDService;

@Service( "IDService" )
public class IDServiceImpl implements IDService
{
  @Autowired
  private IDSRepositoryFactory idsRepo;

  @Override
  public String getKongGateWayToken() throws ServiceErrorException
  {
    String token = null;

    try
    {
      token = idsRepo.getRepo().getKongGateWayToken();
    }
    catch( ServiceErrorException exception )
    {
      throw new ServiceErrorException( exception.getMessage() );
    }

    return token;
  }

  @Override
  public String getIDSSsoUrl() throws ServiceErrorException
  {
    String ssoUrl = null;

    try
    {
      ssoUrl = idsRepo.getRepo().getIDSSSOEndpoint();
    }
    catch( ServiceErrorException exception )
    {
      throw new ServiceErrorException( exception.getMessage() );
    }

    return ssoUrl;
  }

}
