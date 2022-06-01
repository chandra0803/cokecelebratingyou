/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/system/SystemVariableService.java,v $
 */

package com.biperf.core.service.underarmour;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.core.service.underarmour.impl.GetActigraphyResult;

public interface UnderArmourService extends SAO
{
  /**
   * name referenced in Spring applicationConfig.xml
   */
  public static String BEAN_NAME = "underArmourService";

  public boolean authorizeParticipant( long clientPaxId, String authCode ) throws ServiceErrorException;

  public boolean deAuthorizeParticipant( long clientPaxId ) throws ServiceErrorException;

  public boolean isParticipantAuthorized( long clientPaxId ) throws ServiceErrorException;

  public GetActigraphyResult getActigraphyData();

  public boolean isPaxEligibleForUAPromotion( long clientPaxId );

  public boolean uaEnabled();

}
