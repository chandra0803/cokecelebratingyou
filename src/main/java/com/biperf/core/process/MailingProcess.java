/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/MailingProcess.java,v $
 */

package com.biperf.core.process;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.service.email.MailingService;

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
public class MailingProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( MailingProcess.class );

  public static final String PROCESS_NAME = "Mailing Process";
  public static final String BEAN_NAME = "mailingProcess";
  public static final String PARAM_NAME = "mailingId";

  private static int executeCount = 0;
  // properties set from jobDataMap
  private String mailingId;

  private MailingService mailingService;

  public void setMailingId( String mailingId )
  {
    this.mailingId = mailingId;
  }

  public void onExecute()
  {
    // System.out.println(new Date() + "SYSOUT: Running mailing process (" + executeCount + ") on
    // mailing " + mailingId + ".");
    log.debug( "Running mailing process (" + executeCount + ") on mailing " + mailingId + "." );
    executeCount++;
    mailingService.processMailing( new Long( Long.parseLong( mailingId ) ) );
    addComment( "Running mailing process (" + executeCount + ") on mailing " + mailingId + "." );
  }

  /**
   * @param mailingService value for claimService property
   */
  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

}
