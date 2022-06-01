/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/sysadmin/PerformanceStatsAction.java,v $
 */

package com.biperf.core.ui.sysadmin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.aop.PerformanceMonitorInterceptor;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.jamonapi.MonitorFactory;

/**
 * PerformanceStatsAction.
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
 * <td>Sep 30, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PerformanceStatsAction extends BaseDispatchAction
{
  private static final Log log = LogFactory.getLog( PerformanceStatsAction.class );

  /**
   * reset the performance statistics
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward reset( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    log.info( "reseting performance monitoring statistics" );
    MonitorFactory.reset();
    return mapping.findForward( forwardTo );
  }

  /**
   * enable performance monitoring
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward enable( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    log.info( "enabling performance monitoring" );
    MonitorFactory.setEnabled( true );
    return mapping.findForward( forwardTo );
  }

  /**
   * disable performance monitoring
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward disable( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    log.info( "disabling performance monitoring" );
    MonitorFactory.setEnabled( false );
    return mapping.findForward( forwardTo );
  }

  /**
   * setLimit on performance error message via interceptor
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward setLimit( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    String limitValue = request.getParameter( "limit" );
    log.debug( "setting performance error limit to " + limitValue );
    if ( limitValue != null && !limitValue.trim().equals( "" ) )
    {
      // try to make it a number
      try
      {
        int limit = Integer.parseInt( limitValue );
        if ( limit >= 0 )
        {
          PerformanceMonitorInterceptor.setErrorLimit( limit );
        }
      }
      catch( NumberFormatException ignore )
      {
        // ignore
      }
    }
    return mapping.findForward( forwardTo );
  }

  /**
   * display performance monitoring
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    log.debug( "refresh performance monitoring statistics" );
    return mapping.findForward( forwardTo );
  }

  /**
   * unspecified -> calls display
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward unspecified( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    return display( mapping, form, request, response );
  }
}
