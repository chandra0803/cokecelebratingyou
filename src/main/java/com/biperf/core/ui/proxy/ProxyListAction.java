/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/proxy/ProxyListAction.java,v $ */

package com.biperf.core.ui.proxy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.proxy.ProxyService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.utils.ArrayUtil;
import com.biperf.core.utils.ClientStateUtils;

/**
 * Action class for Proxy List operations.
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
 * <td>sedey</td>
 * <td>Nov 17, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProxyListAction extends BaseDispatchAction
{
  /** Log */
  private static final Log logger = LogFactory.getLog( ProxyListAction.class );

  /**
   * Cancelled
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward cancelled( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ProxyListForm proxyListForm = (ProxyListForm)actionForm;
    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "userId", proxyListForm.getMainUserId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    queryString += "&method=display";
    return ActionUtils.forwardWithParameters( actionMapping, ActionConstants.CANCEL_FORWARD, new String[] { queryString } );
  }

  /**
   * Delete proxies
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward delete( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    ProxyListForm proxyListForm = (ProxyListForm)form;
    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "userId", proxyListForm.getMainUserId() );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      queryString += "&method=display";

      return ActionUtils.forwardWithParameters( mapping, ActionConstants.CANCEL_FORWARD, new String[] { queryString } );
    }
    if ( proxyListForm.getDeleteProxy() != null && proxyListForm.getDeleteProxy().length > 0 )
    {
      deleteProxies( proxyListForm.getDeleteProxy() );
    }
    // get the actionForward to display the udpate pages.
    return mapping.findForward( forwardTo );
  }

  /**
   * delete a list of promotions
   * 
   * @param proxyIds
   */
  private void deleteProxies( String[] proxyIds )
  {
    // Convert String[] of promotionIds to Long[]
    List proxyIdList = ArrayUtil.convertStringArrayToListOfLongObjects( proxyIds );

    try
    {
      getProxyService().deleteProxies( proxyIdList );
    }
    catch( ServiceErrorException e )
    {
      // Exception thrown if the promotion to be deleted is live
      logger.error( e.getMessage(), e );
    }

  }

  private ProxyService getProxyService()
  {
    return (ProxyService)getService( ProxyService.BEAN_NAME );
  }

}
