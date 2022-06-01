/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/user/UserPhoneMaintainController.java,v $
 */

package com.biperf.core.ui.user;

import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.country.CountryComparator;
import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.UserManager;

/**
 * UserPhoneMaintainController.
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
 * <td>Apr 27, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserPhoneMaintainController extends BaseController
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
    UserPhoneForm userPhoneForm = (UserPhoneForm)request.getAttribute( UserPhoneForm.FORM_NAME );

    Long userId = new Long( userPhoneForm.getUserId() );

    boolean currentUserMatchesView = userId.equals( UserManager.getUserId() );
    request.setAttribute( "currentUserMatchesView", currentUserMatchesView );

    // Remove the recovery type for people other than themselves
    List<PhoneType> phoneTypes = getUserService().getAvailablePhoneTypes( userId );
    if ( !currentUserMatchesView && !UserManager.isUserInRole( AuthorizationService.ROLE_CODE_MODIFY_RECOVERY_CONTACTS ) )
    {
      phoneTypes.remove( PhoneType.lookup( PhoneType.RECOVERY ) );
    }
    request.setAttribute( "phoneTypes", phoneTypes );

    // for displaying name
    if ( userPhoneForm.getUserId() != null && !"".equals( userPhoneForm.getUserId() ) )
    {
      request.setAttribute( "displayNameUserId", userPhoneForm.getUserId() );
    }

    request.setAttribute( "countryList", getCountryList() );
  }

  private List getCountryList()
  {
    List countryList = getCountryService().getAllActive();
    Collections.sort( countryList, new CountryComparator() );

    return countryList;
  }

  private CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }

  private UserService getUserService() throws Exception
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

}
