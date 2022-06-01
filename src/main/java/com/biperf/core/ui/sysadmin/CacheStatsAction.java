/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/sysadmin/CacheStatsAction.java,v $
 */

package com.biperf.core.ui.sysadmin;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.cache.oscache.ManageableCacheAdministrator;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * CacheStatsAction.
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
 * <td>Jun 13, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CacheStatsAction extends BaseDispatchAction
{
  private static final Log log = LogFactory.getLog( CacheStatsAction.class );

  /**
   * Overridden from
   * 
   * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping,
   *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse)
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward for display
   * @throws Exception
   */
  protected ActionForward unspecified( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return display( mapping, form, request, response );
  }

  /**
   * display
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
    return mapping.findForward( forwardTo );
  }

  /**
   * flush the platform cache
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward flushPlatform( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    log.info( "flushing the platform cache" );
    getPlatformCache().flushAll( new Date() );
    return mapping.findForward( forwardTo );
  }

  /**
   * clear the platform cache
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward clearPlatform( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    log.info( "clearing the platform cache" );
    getPlatformCache().clear();
    return mapping.findForward( forwardTo );
  }

  /**
   * flush the cm cache
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward flushContentManager( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    log.info( "flushing the cm cache" );
    getContentManagerCache().flushAll( new Date() );
    return mapping.findForward( forwardTo );
  }

  /**
   * clear the cm cache
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward clearContentManager( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    log.info( "clearing the cm cache" );
    getContentManagerCache().clear();
    return mapping.findForward( forwardTo );
  }

  private ManageableCacheAdministrator getPlatformCache()
  {
    return (ManageableCacheAdministrator)ApplicationContextFactory.getApplicationContext().getBean( "cacheAdministrator" );
  }

  private ManageableCacheAdministrator getContentManagerCache()
  {
    return (ManageableCacheAdministrator)ApplicationContextFactory.getContentManagerApplicationContext().getBean( "cmsCacheAdministrator" );
  }

}
