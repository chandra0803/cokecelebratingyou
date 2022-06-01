/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/CacheClearProcess.java,v $
 */

package com.biperf.core.process;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.cache.oscache.ManageableCacheAdministrator;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * CacheClearProcess is a process that clears both the G3 and CM caches.
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
 * <td>Brian Repko</td>
 * <td>Dec 7, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CacheClearProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( CacheClearProcess.class );
  private static final String EMAIL_SUBJECT = "Clear Cache Process Summary Email";

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  protected void onExecute()
  {
    boolean completed = false;
    try
    {
      ManageableCacheAdministrator cache = null;
      if ( !isInterrupted() )
      {
        cache = (ManageableCacheAdministrator)ApplicationContextFactory.getApplicationContext().getBean( "cacheAdministrator" );
        cache.flushAll( new Date() );
        cache.clear();
        addComment( "Platform cache flushed and cleared" );
      }
      if ( !isInterrupted() )
      {
        cache = (ManageableCacheAdministrator)ApplicationContextFactory.getContentManagerApplicationContext().getBean( "cmsCacheAdministrator" );
        cache.flushAll( new Date() );
        cache.clear();
        addComment( "Content Manager cache flushed and cleared" );
      }
      if ( !isInterrupted() )
      {
        completed = true;
      }
    }
    catch( Exception e )
    {
      log.error( "Unable to execute process invocation " + getProcessInvocationId(), e );
      addComment( "An exception occurred while clearing caches.  " + "See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId() + ")" );
    }
    sendSummaryMessage( completed );
  }

  private void sendSummaryMessage( boolean completed )
  {
    try
    {
      String message = "The cache clear process was run at " + new Date() + " and " + ( completed ? "completed" : "did not complete" );
      mailingService.submitSystemMailing( EMAIL_SUBJECT, message, message );
      addComment( "Summary email has been sent to system user" );
    }
    catch( Exception e )
    {
      log.error( "Unable to send Cache Clear summary email for process invocation " + getProcessInvocationId(), e );
      addComment( "An exception occurred while sending Cache Clear summary email.  " + "See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId()
          + ")" );
    }
  }

}
