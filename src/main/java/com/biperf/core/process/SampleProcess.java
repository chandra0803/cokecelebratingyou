/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.process;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.hierarchy.HierarchyService;

/**
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
 * <td>wadzinsk</td>
 * <td>Nov 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SampleProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( SampleProcess.class );

  public static final String BEAN_NAME = "sampleProcess";

  private ClaimService claimService;
  private HierarchyService hierarchyService;

  // properties set from jobDataMap
  private String[] sampleParamName1;
  private String sampleParamName2;
  private String sampleParamName3;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  public void onExecute()
  {
    log.error( "process :" + toString() );
    log.error( sampleParamName1 );
    log.error( sampleParamName2 );
    log.error( sampleParamName3 );

    addComment( "my little comment" );
    addComment( "my little comment2" );

    // Performing a sample persistent operation. (will throw excpetion on second run since
    // non-unique)
    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setCmAssetCode( "processtest7" );
    hierarchy.setDescription( "processtest7" );
    hierarchy.setName( "processtest7" );
    hierarchy.setNameCmKey( "processtest7" );

    try
    {
      hierarchyService.save( hierarchy );
    }
    catch( ServiceErrorException e )
    {
      throw new BeaconRuntimeException( e.getMessage(), e );
    }

    try
    {
      Thread.sleep( 10000 );
    }
    catch( InterruptedException e )
    {
      log.error( e.getMessage(), e );
    }
    if ( interrupted )
    {
      return;
    }
    claimService.processDelayedApprovalClaims();

  }

  /**
   * @param claimService value for claimService property
   */
  public void setClaimService( ClaimService claimService )
  {
    this.claimService = claimService;
  }

  /**
   * @param hierarchyService value for hierarchyService property
   */
  public void setHierarchyService( HierarchyService hierarchyService )
  {
    this.hierarchyService = hierarchyService;
  }

  /**
   * @return value of sampleParamName1 property
   */
  public String[] getSampleParamName1()
  {
    return sampleParamName1;
  }

  /**
   * @param sampleParamName1 value for sampleParamName1 property
   */
  public void setSampleParamName1( String[] sampleParamName1 )
  {
    this.sampleParamName1 = sampleParamName1;
  }

  /**
   * @return value of sampleParamName2 property
   */
  public String getSampleParamName2()
  {
    return sampleParamName2;
  }

  /**
   * @param sampleParamName2 value for sampleParamName2 property
   */
  public void setSampleParamName2( String sampleParamName2 )
  {
    this.sampleParamName2 = sampleParamName2;
  }

  public String getSampleParamName3()
  {
    return sampleParamName3;
  }

  public void setSampleParamName3( String sampleParamName3 )
  {
    this.sampleParamName3 = sampleParamName3;
  }

}
