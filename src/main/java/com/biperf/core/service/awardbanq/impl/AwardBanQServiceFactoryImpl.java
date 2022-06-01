/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/awardbanq/impl/AwardBanQServiceFactoryImpl.java,v $
 */

package com.biperf.core.service.awardbanq.impl;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.system.SystemVariableService;

/**
 * AwardBanQServiceFactoryImpl.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>zahler</td>
 * <td>Aug 18, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AwardBanQServiceFactoryImpl implements AwardBanQServiceFactory
{
  private static final Log log = LogFactory.getLog( AwardBanQServiceFactoryImpl.class );

  private SystemVariableService systemVariableService;

  private Map entries;

  public void setEntries( Map entries )
  {
    this.entries = entries;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.awardbanq.AwardBanQServiceFactory#getAwardBanQService()
   * @return AwardBanQService
   */
  public AwardBanQService getAwardBanQService()
  {
    String awardBanqMode = NO_AWARDBANQ;
    PropertySetItem sysVar = systemVariableService.getPropertyByName( SystemVariableService.AWARDBANQ_MODE );
    if ( sysVar != null )
    {
      awardBanqMode = sysVar.getStringVal();
    }
    else
    {
      log.error( "No system variable for " + SystemVariableService.AWARDBANQ_MODE + ", using \"none\"" );
    }
    AwardBanQService awardBanQService = (AwardBanQService)entries.get( awardBanqMode );
    if ( awardBanQService == null )
    {
      throw new BeaconRuntimeException( "Unable to get AwardBanQService Implementation.  Make sure system variable for AwardbanQ mode is 'awardbanq', 'mock' or 'none'" );
    }

    return awardBanQService;
  }

}
