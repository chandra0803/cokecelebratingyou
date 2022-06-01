/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/sysadmin/CacheStatsAction.java,v $
 */

package com.biperf.core.ui.sysadmin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.service.cache.CacheManagementBean;
import com.biperf.core.service.cache.CacheManagementService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.BeanLocator;

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
public class CacheManagementAction extends BaseDispatchAction
{
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

  public ActionForward clearCache( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    CacheManagementForm cacheForm = (CacheManagementForm)form;
    getCacheManagementService().clearCache( cacheForm.getCacheName() );

    return mapping.findForward( forwardTo );
  }

  public ActionForward clearStatistics( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    CacheManagementForm cacheForm = (CacheManagementForm)form;
    getCacheManagementService().clearCacheStatistics( cacheForm.getCacheName() );
    return mapping.findForward( forwardTo );
  }

  public ActionForward changeSettings( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    CacheManagementForm cacheForm = (CacheManagementForm)form;
    CacheManagementBean updatedSettings = new CacheManagementBean();
    updatedSettings.setCacheName( cacheForm.getCacheName() );
    // updatedSettings.setMaxBytesLocalDisk( cacheForm.getMaxBytesLocalDisk() ) ;
    updatedSettings.setMaxBytesLocalHeap( cacheForm.getMaxBytesLocalHeap() );
    updatedSettings.setTimeToIdleSeconds( cacheForm.getTimeToIdleSeconds() );
    updatedSettings.setTimeToLiveSeconds( cacheForm.getTimeToLiveSeconds() );
    getCacheManagementService().updateCacheSettings( updatedSettings );
    return mapping.findForward( forwardTo );
  }

  private CacheManagementService getCacheManagementService()
  {
    return (CacheManagementService)BeanLocator.getBean( CacheManagementService.BEAN_NAME );
  }
}
