
package com.biperf.core.process;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.service.process.ProcessService;

public class ClearEhCacheProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( ClearEhCacheProcess.class );

  public static final String BEAN_NAME = "clearEhCacheProcess";

  private ProcessService processService;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  protected void onExecute()
  {
    try
    {
      processService.clearHibernateSecondLevelCache();
    }
    catch( Exception e )
    {
      log.error( e );
      addComment( "An Exception occurred. " + "\n" + e.toString() + "\n" + " See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId() + ")" );
    }
  }

  public void setProcessService( ProcessService processService )
  {
    this.processService = processService;
  }

}
