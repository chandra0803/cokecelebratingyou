/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/user/UserEmailAddressListController.java,v $
 */

package com.biperf.core.ui.user;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;

/**
 * UserEmailAddressListController.
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
 * <td>sharma</td>
 * <td>Apr 27, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserEmailAddressListController extends BaseController
{

  private static final Log LOG = LogFactory.getLog( UserEmailAddressController.class );

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param componentContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext componentContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    LOG.info( ">>> execute" );
    String userId = null;
    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
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
        Long id = (Long)clientStateMap.get( "userId" );
        userId = id.toString();
      }
      if ( userId != null )
      {
        boolean currentUserMatchesView = userId.equals( UserManager.getUserId().toString() );
        request.setAttribute( "currentUserMatchesView", currentUserMatchesView );
        
        UserService userService = (UserService)getService( UserService.BEAN_NAME );
        Set<UserEmailAddress> userEmailAddressSet = userService.getUserEmailAddresses( new Long( userId ) );
        
        // Mask recovery email if a user is not looking at their own
        if ( !currentUserMatchesView )
        {
          for ( UserEmailAddress userEmailAddress : userEmailAddressSet )
          {
            if ( EmailAddressType.RECOVERY.equals( userEmailAddress.getEmailType().getCode() ) )
            {
              userEmailAddress.setEmailAddr( StringUtil.maskEmailAddress( userEmailAddress.getEmailAddr() ) );
            }
          }
        }
        
        request.setAttribute( "size", "" + userEmailAddressSet.size() );
        request.setAttribute( "userEmailAddressList", userEmailAddressSet );
        request.setAttribute( "displayNameUserId", userId );
        // set the primary email address type in the form, if any
        UserEmailAddressForm form = (UserEmailAddressForm)request.getAttribute( "userEmailAddressForm" );
        for ( Iterator<UserEmailAddress> iter = userEmailAddressSet.iterator(); iter.hasNext(); )
        {
          UserEmailAddress userEmailAddress = (UserEmailAddress)iter.next();
          Boolean isPrimary = userEmailAddress.getIsPrimary();
          if ( isPrimary != null && isPrimary.equals( Boolean.TRUE ) )
          {
            form.setPrimary( userEmailAddress.getEmailType().getCode() );
          }
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
  }
}
