/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/proxy/ProxyListController.java,v $
 */

package com.biperf.core.ui.proxy;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.ProxyCoreAccessType;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.proxy.ProxyAssociationRequest;
import com.biperf.core.service.proxy.ProxyService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;

/**
 * Implements the controller for the Proxyist page.
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
public class ProxyListController extends BaseController
{
  private static final Log logger = LogFactory.getLog( ProxyListController.class );

  /**
   * Tiles controller for the ProxyList page Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param context
   * @param request
   * @param response
   * @param servletContext
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @throws Exception
   */
  public void onExecute( ComponentContext context, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    String userId = null;
    try
    {
      String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
      if ( clientState != null && clientState.length() != 0 )
      {
        String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
        String password = ClientStatePasswordManager.getPassword();

        if ( cryptoPass != null && cryptoPass.equals( "1" ) )
        {
          password = ClientStatePasswordManager.getGlobalPassword();
        }
        // Deserialize the client state.
        Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
        try
        {
          userId = (String)clientStateMap.get( "userId" );
        }
        catch( ClassCastException cce )
        {
          userId = ( (Long)clientStateMap.get( "userId" ) ).toString();
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    String includeCancel = RequestUtils.getOptionalParamString( request, "includeCancel" );

    ProxyListForm proxyListForm = (ProxyListForm)request.getAttribute( "proxyListForm" );
    String myProxy = "false";
    if ( userId == null )
    {
      if ( proxyListForm.getMainUserId() != null && !proxyListForm.getMainUserId().equals( "" ) )
      {
        userId = proxyListForm.getMainUserId();
      }
    }
    if ( userId == null )
    {
      userId = UserManager.getUserId().toString();
    }

    if ( userId.equals( UserManager.getUserId().toString() ) )
    {
      myProxy = "true";
    }

    if ( includeCancel != null && includeCancel.equals( "true" ) )
    {
      proxyListForm.setShowCancel( true );
    }

    proxyListForm.setMainUserId( userId );
    proxyListForm.setProxyId( "" );

    request.setAttribute( "proxyListForm", proxyListForm );
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ProxyAssociationRequest( ProxyAssociationRequest.PROXY_USER_ADDRESS ) );
    request.setAttribute( "proxyList", getProxyService().getProxiesByUserIdWithAssociation( new Long( userId ), associationRequestCollection ) );
    request.setAttribute( "myProxy", myProxy );
    request.setAttribute( "proxyCoreAccessList", ProxyCoreAccessType.getList() );
    request.setAttribute( "isRosterMgmtAvailable", isRosterMgmtAvailable() );

    // Choose Sub Nav
    String subNavSelected = (String)context.getAttribute( "subNavSelected" );
    request.setAttribute( "subNavSelected", subNavSelected );
    logger.debug( "subNavSelected=" + subNavSelected );
  }

  private Boolean isRosterMgmtAvailable()
  {
    return getBooleanPropertyValue( SystemVariableService.ROSTER_MGMT_AVAILABLE );
  }

  private Boolean getBooleanPropertyValue( String propertyName )
  {
    PropertySetItem property = getSystemVariableService().getPropertyByName( propertyName );
    return property != null ? new Boolean( property.getBooleanVal() ) : Boolean.FALSE;
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  /**
   * Get the ProxyService from the beanLocator.
   * 
   * @return ProxyService
   */

  private ProxyService getProxyService()
  {
    return (ProxyService)getService( ProxyService.BEAN_NAME );
  }

}
