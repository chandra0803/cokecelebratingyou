
package com.biperf.core.dao.claim.hibernate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.utils.ApplicationContextFactory;

public class CallPrcNominationsApprovalPageTableDataTest extends BaseDAOTest
{

  /*
   * public void testPrcSuccessCode() { Map<String, Object> inParams = new HashMap<String,
   * Object>(); inParams.put( "p_in_promotion_id", 1l ); inParams.put( "p_in_level_number", 1l );
   * inParams.put( "p_in_approver_id", 123l ); inParams.put( "p_in_time_period_id", 1l );
   * inParams.put( "p_in_submit_start_date", new Date() ); inParams.put( "p_in_submit_end_date", new
   * Date() ); inParams.put( "p_in_status", "complete" ); inParams.put( "p_in_locale", "en" );
   * inParams.put( "p_in_rowNumStart", 1l ); inParams.put( "p_in_rowNumEnd", 10l ); inParams.put(
   * "p_in_sortedBy", "asc" ); inParams.put( "p_in_sortedOn", "1" );
   * CallPrcNominationsApprovalPageTableData prc = new CallPrcNominationsApprovalPageTableData(
   * getDataSource() ); Map<String, Object> prcResult = prc.executeProcedure( inParams );
   * assertNotNull( prcResult ); Object returnCode = prcResult.get( "p_out_returncode" );
   * assertNotNull( returnCode ); assertTrue( 99 != ((BigDecimal)returnCode).intValue() ); }
   */

  public void testPrcFailCode()
  {
    Map<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_promotion_id", null );
    inParams.put( "p_in_level_number", 1L );
    inParams.put( "p_in_approver_id", 123L );
    inParams.put( "p_in_time_period_id", 1L );
    inParams.put( "p_in_submit_start_date", new Date() );
    inParams.put( "p_in_submit_end_date", new Date() );
    inParams.put( "p_in_status", "complete" );
    inParams.put( "p_in_locale", "en" );
    inParams.put( "p_in_rowNumStart", 1L );
    inParams.put( "p_in_rowNumEnd", 10L );

    inParams.put( "p_in_sortedBy", "asc" );
    inParams.put( "p_in_sortedOn", "1" );

    CallPrcNominationsApprovalPageTableData prc = new CallPrcNominationsApprovalPageTableData( getDataSource() );
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
