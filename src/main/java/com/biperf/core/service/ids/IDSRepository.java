
package com.biperf.core.service.ids;

import org.springframework.web.client.HttpStatusCodeException;

import com.biperf.core.exception.ServiceErrorException;

public interface IDSRepository
{
  public static final String BEAN_NAME = "IDSRepository";

  public String getKongGateWayToken() throws HttpStatusCodeException, ServiceErrorException;

  public String getKongGateWayTokenByPersonId( String personId ) throws HttpStatusCodeException, ServiceErrorException;

  public String getKongGateWayForClientCredentials() throws HttpStatusCodeException, ServiceErrorException;

  public String getIDSSSOEndpoint() throws HttpStatusCodeException, ServiceErrorException;

}
