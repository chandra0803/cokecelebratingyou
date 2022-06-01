/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/claim/impl/MinimumQualifierStatusServiceImpl.java,v $
 */

package com.biperf.core.service.claim.impl;

import com.biperf.core.dao.claim.MinimumQualifierStatusDAO;
import com.biperf.core.domain.claim.MinimumQualifierStatus;
import com.biperf.core.service.claim.MinimumQualifierStatusService;

/**
 * 
 *  MinimumQualifierStatusServiceImpl.
 * 
 * @author bethke
 * @since Jun 10, 2016
 */
public class MinimumQualifierStatusServiceImpl implements MinimumQualifierStatusService
{

  private MinimumQualifierStatusDAO minimumQualifierStatusDAO;

  public MinimumQualifierStatus getMinimumQualifierStatus( Long minimumQualifierStatusId )
  {
    return minimumQualifierStatusDAO.getMinimumQualifierStatus( minimumQualifierStatusId );
  }

  public void setMinimumQualifierStatusDAO( MinimumQualifierStatusDAO minimumQualifierStatusDAO )
  {
    this.minimumQualifierStatusDAO = minimumQualifierStatusDAO;
  }

  public MinimumQualifierStatus getMinimumQualifierStatusById( Long minimumQualifierStatusId )
  {
    return minimumQualifierStatusDAO.getMinimumQualifierStatusById( minimumQualifierStatusId );
  }

}
