/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/proxy/ProxyDetailController.java,v $
 */

package com.biperf.core.ui.proxy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.ProxyCoreAccessType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.proxy.ProxyService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.UserManager;

/**
 * Implements the controller for the Proxy Detail page.
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
 * <td>jenniget</td>
 * <td>May 31, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProxyDetailController extends BaseController
{
  private static final String MANAGER = "manager";

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
    ProxyDetailForm proxyDetailForm = (ProxyDetailForm)request.getAttribute( "proxyDetailForm" );

    request.setAttribute( "showPC", isProductClaimAvailable() );
    request.setAttribute( "showRec", isRecognitionAvailable() );
    request.setAttribute( "showNom", isNominationAvailable() );
    // request.setAttribute( "showSsi", isSSIAvailable() );
    request.setAttribute( "showLB", isLeaderBoardAvailable() );
    List<ProxyCoreAccessType> coreAccessList = filterProxyCoreAccessType( ProxyCoreAccessType.getList() );
    request.setAttribute( "proxyCoreAccessList", coreAccessList );
    proxyDetailForm.setShowCoreAccess( !coreAccessList.isEmpty() );
    request.setAttribute( "proxyDetailForm", proxyDetailForm );
  }

  private List<ProxyCoreAccessType> filterProxyCoreAccessType( List<ProxyCoreAccessType> proxyCoreAccessTypes )
  {
    List<ProxyCoreAccessType> filteredProxyCoreAccessTypes = new ArrayList<ProxyCoreAccessType>();
    for ( ProxyCoreAccessType proxyCoreAccessType : proxyCoreAccessTypes )
    {
      if ( proxyCoreAccessType != null && proxyCoreAccessType.getCode().equals( ProxyCoreAccessType.ROSTER_MGMT ) && isRosterMgmtAvailable() && isRosterMgmtAccessible( UserManager.getUserId() ) )
      {
        filteredProxyCoreAccessTypes.add( proxyCoreAccessType );
      }
      if ( proxyCoreAccessType != null && proxyCoreAccessType.getCode().equals( ProxyCoreAccessType.SEND_ALERT ) && isSendAlertAvailable() && isManager() )
      {
        filteredProxyCoreAccessTypes.add( proxyCoreAccessType );
      }
      if ( proxyCoreAccessType != null && proxyCoreAccessType.getCode().equals( ProxyCoreAccessType.BUDGET_TRANSFER ) )
      {
        filteredProxyCoreAccessTypes.add( proxyCoreAccessType );
      }
    }
    return filteredProxyCoreAccessTypes;
  }

  private Boolean isRosterMgmtAccessible( Long rosterManagerId )
  {
    if ( null != rosterManagerId )
    {
      User user = getUserService().getUserById( rosterManagerId );
      if ( null != user )
      {
        return new Boolean( user.isManager() );
      }
    }
    return Boolean.FALSE;
  }

  private boolean isLeaderBoardAvailable()
  {
    boolean showLB = false;
    if ( getBooleanPropertyValue( SystemVariableService.LEADERBOARD_SHOW_HIDE ) && isManager() )
    {
      showLB = true;
    }
    return showLB;
  }

  private Boolean isSendAlertAvailable()
  {
    return getBooleanPropertyValue( SystemVariableService.MANAGER_SEND_ALERT );
  }

  private Boolean isRosterMgmtAvailable()
  {
    return getBooleanPropertyValue( SystemVariableService.ROSTER_MGMT_AVAILABLE );
  }

  private Boolean isProductClaimAvailable()
  {
    return getBooleanPropertyValue( SystemVariableService.INSTALL_PRODUCTCLAIMS );
  }

  private Boolean isRecognitionAvailable()
  {
    return getBooleanPropertyValue( SystemVariableService.INSTALL_RECOGNITION );
  }

  private Boolean isNominationAvailable()
  {
    return getBooleanPropertyValue( SystemVariableService.INSTALL_NOMINATIONS );
  }

  /*
   * private Boolean isSSIAvailable() { return getBooleanPropertyValue(
   * SystemVariableService.INSTALL_SSI ); }
   */
  private Boolean getBooleanPropertyValue( String propertyName )
  {
    PropertySetItem property = getSystemVariableService().getPropertyByName( propertyName );
    return property != null ? new Boolean( property.getBooleanVal() ) : Boolean.FALSE;
  }

  private boolean isManager()
  {
    Set roles = new HashSet();
    roles.add( AuthorizationService.ROLE_CODE_BI_ADMIN );
    roles.add( MANAGER.toUpperCase() );
    return getAuthorizationService().isUserInRole( null, roles, null );
  }

  private List getRecognitionPromos( Participant participant )
  {
    return getProxyService().getRecognitionPromotionsByPax( participant );
  }

  private List getProductClaimPromos( Participant participant )
  {
    return getProxyService().getProductClaimPromotionsByPax( participant );
  }

  private List getNominationPromos( Participant participant )
  {
    return getProxyService().getNominationPromotionsByPax( participant );
  }

  /*
   * private List getSsiPromos( Participant participant ) { return
   * getProxyService().getSSIPromotionsByPax( participant ); }
   */
  /**
   * Get the ProxyService from the beanLocator.
   * 
   * @return ProxyService
   */

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private AuthorizationService getAuthorizationService()
  {
    return (AuthorizationService)getService( AuthorizationService.BEAN_NAME );
  }

  private ProxyService getProxyService()
  {
    return (ProxyService)getService( ProxyService.BEAN_NAME );
  }

}
