/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.claim;

import com.biperf.core.domain.claim.MinimumQualifierStatus;
import com.biperf.core.service.SAO;

/**
 * 
 * MinimumQualifierStatusService.
 * 
 * @author bethke
 * @since Jun 10, 2016
 */
public interface MinimumQualifierStatusService extends SAO
{
  public static final String BEAN_NAME = "minimumQualifierStatusService";

  public MinimumQualifierStatus getMinimumQualifierStatus( Long minimumQualifierStatusId );

  public MinimumQualifierStatus getMinimumQualifierStatusById( Long minimumQualifierStatusId );

}
