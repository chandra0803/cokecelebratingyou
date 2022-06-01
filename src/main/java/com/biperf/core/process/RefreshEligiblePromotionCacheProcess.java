/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/RefreshEligiblePromotionCacheProcess.java,v $
 */

package com.biperf.core.process;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.service.jms.GJavaMessageService;
import com.biperf.core.utils.jms.JmsRefreshEligiblePromotionsCacheProcessMessage;

public class RefreshEligiblePromotionCacheProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( RefreshEligiblePromotionCacheProcess.class );
  public static final String BEAN_NAME = "refreshEligiblePromotionsCacheProcess";

  private GJavaMessageService gJavaMessageService = null;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  @Override
  protected void onExecute()
  {
    if ( log.isDebugEnabled() )
    {
      log.debug( "Sending JmsRefreshEligiblePromotionsCacheProcessMessage to Topic..." );
    }
    try
    {
      JmsRefreshEligiblePromotionsCacheProcessMessage refreshMessage = createMessage();
      gJavaMessageService.sendToJmsTopic( refreshMessage );
    }
    catch( Exception e )
    {
      log.error( "Unable to execute process invocation " + getProcessInvocationId(), e );
      addComment( "An exception occurred while sending JMS notification to refresh the cache.  " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

  public GJavaMessageService getGJavaMessageService()
  {
    return gJavaMessageService;
  }

  public void setGJavaMessageService( GJavaMessageService gJavaMessageService )
  {
    this.gJavaMessageService = gJavaMessageService;
  }

  protected JmsRefreshEligiblePromotionsCacheProcessMessage createMessage()
  {
    return new JmsRefreshEligiblePromotionsCacheProcessMessage();
  }
}
