/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/user/UserPhoneListController.java,v $
 */

package com.biperf.core.ui.user;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;

/**
 * UserPhoneListController.
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
 * <td>zahler</td>
 * <td>Apr 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserPhoneListController extends BaseController
{

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
    UserPhoneListForm userPhoneListForm = (UserPhoneListForm)request.getAttribute( "userPhoneListForm" );
    Long userId = new Long( userPhoneListForm.getUserId() );
    
    boolean currentUserMatchesView = userId.equals( UserManager.getUserId() );
    request.setAttribute( "currentUserMatchesView", currentUserMatchesView );

    Set<UserPhone> userPhones = getUserService().getUserPhones( userId );
    Set<UserPhone> userPhoneWithCountryCode = new LinkedHashSet<>();
    if ( userPhones != null && userPhones.size() > 0 )
    {
      for ( Iterator<UserPhone> iter = userPhones.iterator(); iter.hasNext(); )
      {
        UserPhone tempUserPhone = (UserPhone)iter.next();
        if ( tempUserPhone.getCountryPhoneCode() != null )
        {
          String temp = tempUserPhone.getCountryPhoneCode();
          tempUserPhone.setCountryPhoneCode( getCountryService().getCountryByCode( temp ).getPhoneCountryCode() );
          userPhoneWithCountryCode.add( tempUserPhone );
        }
      }
    }
    
    // Mask recovery phone number if a user is not looking at their own
    if ( !currentUserMatchesView )
    {
      for ( UserPhone userPhone : userPhoneWithCountryCode )
      {
        if ( PhoneType.RECOVERY.equals( userPhone.getPhoneType().getCode() ) )
        {
          userPhone.setPhoneNbr( StringUtil.maskPhoneNumber( userPhone.getPhoneNbr() ) );
        }
      }
    }
    
    request.setAttribute( "phoneList", userPhoneWithCountryCode );
    if ( userPhones != null )
    {
      request.setAttribute( "size", "" + userPhones.size() );
    }

    // for displaying name
    if ( userPhoneListForm.getUserId() != null && !"".equals( userPhoneListForm.getUserId() ) )
    {
      request.setAttribute( "displayNameUserId", userPhoneListForm.getUserId() );
    }

    User user = getUserService().getUserById( userId );
    request.setAttribute( "user", user );// for name display
  }

  private UserService getUserService() throws Exception
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }
}
