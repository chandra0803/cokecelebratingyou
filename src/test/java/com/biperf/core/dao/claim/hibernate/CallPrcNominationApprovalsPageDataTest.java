
package com.biperf.core.dao.claim.hibernate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.utils.ApplicationContextFactory;

public class CallPrcNominationApprovalsPageDataTest extends BaseDAOTest
{

  // TODO implementation on result mapper needs to be resolved -
  public void testPrcSuccessCode()
  {

    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "promotionId", 1L );
    inParams.put( "levelNumber", 1L );
    inParams.put( "approverUserId", 1234L );
    inParams.put( "userLocale", "eN" );

    CallPrcNominationApprovalsPageData prc = new CallPrcNominationApprovalsPageData( getDataSource() );
    Map<String, Object> prcResult = prc.executeProcedure( inParams );
    assertNotNull( prcResult );
    Object returnCode = prcResult.get( "p_out_returncode" );
    assertNotNull( returnCode );
    assertTrue( 99 != ( (BigDecimal)returnCode ).intValue() );

  }

  // TODO implementation on result mapper needs to be resolved -
  public void testPrcFailCode()
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "promotionId", 1L );
    inParams.put( "levelNumber", 1L );
    inParams.put( "approverUserId", 1234L );
    inParams.put( "userLocale", "eN" );

    CallPrcNominationApprovalsPageData prc = new CallPrcNominationApprovalsPageData( getDataSource() );
    Map<String, Object> prcResult = prc.executeProcedure( inParams );
    assertNotNull( prcResult );
    Object returnCode = prcResult.get( "p_out_returncode" );
    assertNotNull( returnCode );
    assertTrue( 0 == ( (BigDecimal)returnCode ).intValue() );

  }

  private static DataSource getDataSource()
  {
    return (DataSource)ApplicationContextFactory.getApplicationContext().getBean( "dataSource" );
  }

}
