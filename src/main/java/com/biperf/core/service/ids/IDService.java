
package com.biperf.core.service.ids;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;

public interface IDService extends SAO
{
  public static final String BEAN_NAME = "IDService";

  public String getKongGateWayToken() throws ServiceErrorException;

  public String getIDSSsoUrl() throws ServiceErrorException;

}
