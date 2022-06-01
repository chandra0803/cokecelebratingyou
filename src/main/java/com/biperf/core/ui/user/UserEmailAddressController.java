/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/user/UserEmailAddressController.java,v $
 */

package com.biperf.core.ui.user;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.UserManager;

/**
 * UserEmailAddressController.
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
public class UserEmailAddressController extends BaseController
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

    UserEmailAddressForm userEmailAddressForm = (UserEmailAddressForm)request.getAttribute( "userEmailAddressForm" );
    Long userId = new Long( userEmailAddressForm.getUserId() );
    UserService userService = (UserService)getService( UserService.BEAN_NAME );
    if ( userId != null )// for name display
    {
      request.setAttribute( "displayNameUserId", userId );
    }

    boolean currentUserMatchesView = userId.equals( UserManager.getUserId() );
    request.setAttribute( "currentUserMatchesView", currentUserMatchesView );

    Set<UserEmailAddress> userEmailAddresses = userService.getUserEmailAddresses( userId );
    List<EmailAddressType> emailAddressTypeList = new ArrayList<>( EmailAddressType.getList() );

    // Remove email types that have already been used
    for ( Iterator<UserEmailAddress> iter = userEmailAddresses.iterator(); iter.hasNext(); )
    {
      UserEmailAddress userEmailAddress = iter.next();
      emailAddressTypeList.remove( userEmailAddress.getEmailType() );
    }

    // Remove the recovery type for people other than the user themselves
    if ( !currentUserMatchesView && !UserManager.isUserInRole( AuthorizationService.ROLE_CODE_MODIFY_RECOVERY_CONTACTS ) )
    {
      emailAddressTypeList.remove( EmailAddressType.lookup( EmailAddressType.RECOVERY ) );
    }

    request.setAttribute( "userEmailAddressTypes", emailAddressTypeList );
  }

}
