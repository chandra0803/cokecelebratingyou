/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/process/HardCopyPDFStatementProcess.java,v $
 */

package com.biperf.core.process;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This process is to produce hard copy statements which the user can download in PDF format.
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
 * <td>sathish</td>
 * <td>Dec 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class HardCopyPDFStatementProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( HardCopyPDFStatementProcess.class );

  public static final String BEAN_NAME = "HardCopyPDFStatementProcess";

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  public void onExecute()
  {
    log.debug( "process :" + toString() );

    addComment( "HardCopyPDFStatement process comment" );

    // TODO: add logic to run the hardcopy statement report

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

  }

}
