/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/merchlevel/impl/MerchLevelServiceImplTest.java,v $
 */

package com.biperf.core.service.merchlevel.impl;

import java.util.Collection;
import java.util.Iterator;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.awardbanq.AwardBanqMerchResponseValueObject;
import com.biperf.integration.BaseIntegrationTest;

/*
 * MerchLevelServiceImplTest Test MerchLevelSerivce Wrapper.  
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>babu</td>
 * <td>Jul 13, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class MerchLevelServiceImplTest extends BaseIntegrationTest
{

  private String programId = null;
  private MerchLevelServiceImpl merchLevelServiceImpl = new MerchLevelServiceImpl();

  public void testGetMerchlinqLevelData() throws ServiceErrorException
  {
    System.setProperty( "bi.http.proxyHost", "proxyuser.bius.bi.corp" );
    System.setProperty( "bi.http.proxyPort", "8080" );
    System.setProperty( "bi.https.proxyHost", "proxyuser.bius.bi.corp" );
    System.setProperty( "bi.https.proxyPort", "8080" );
    AwardBanqMerchResponseValueObject merchlinqLevelData = merchLevelServiceImpl.getMerchlinqLevelDataWebService( "09018", "qa" );
    Collection levels = merchlinqLevelData.getMerchLevel();
    if ( levels != null )
    {
      Iterator levelsIter = levels.iterator();
      while ( levelsIter.hasNext() )
      {
        System.out.println( levelsIter.next() );
      }
    }
  }

  /**
   * @return the programId
   */
  public String getProgramId()
  {
    return programId;
  }

  /**
   * @param programId the programId to set
   */
  public void setProgramId( String programId )
  {
    this.programId = programId;
  }
}
