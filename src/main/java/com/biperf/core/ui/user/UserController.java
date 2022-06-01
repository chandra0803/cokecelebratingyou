/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/user/UserController.java,v $
 */

package com.biperf.core.ui.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.dao.security.hibernate.RoleQueryConstraint;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.country.CountryComparator;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.SecretQuestionType;
import com.biperf.core.domain.enums.StatusType;
import com.biperf.core.domain.enums.SuffixType;
import com.biperf.core.domain.enums.TitleType;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.user.Role;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.security.RoleService;
import com.biperf.core.ui.BaseAdminUserController;

/**
 * UserController.
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
 * <td>Apr 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class UserController extends BaseAdminUserController
{
  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Fetches data for the Add BI User page and the Add Client User page.
   *
   * @param componentContext the context for the tile associated with this controller.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @param servletContext the context for servlets of this web application.
   */
  public void onExecute( ComponentContext componentContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
  {
    request.setAttribute( "allRoles", getRoleList( request.getParameter( "userTypeCode" ) ) );
    request.setAttribute( "statusList", StatusType.getList() );
    request.setAttribute( "secretQuestionList", SecretQuestionType.getList() );
    request.setAttribute( "titleList", TitleType.getList() );
    request.setAttribute( "suffixList", SuffixType.getList() );
    request.setAttribute( "nodeRelationshipList", HierarchyRoleType.getList() );
    request.setAttribute( "countryList", getCountryList() );

    List allUserTypes = UserType.getList();

    List<UserType> userTypeList = new ArrayList<UserType>();

    // Pax should not be an option from this screen.
    Iterator iter = allUserTypes.iterator();
    while ( iter.hasNext() )
    {
      UserType userType = (UserType)iter.next();
      if ( !userType.getCode().equals( UserType.PARTICIPANT ) )
      {
        userTypeList.add( userType );
      }
    }
    request.setAttribute( "userTypeList", userTypeList );

    String userId = request.getParameter( "userId" );
    if ( userId != null && !"".equals( userId ) )
    {
      request.setAttribute( "displayNameUserId", userId );
    }
    this.setContactMethods( request );
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns the roles that can be assumed by the specified type of user.  Returns
   * all roles if the argument "userTypeCode" is null or empty.
   *
   * @param userTypeCode  return roles that can be assumed by this type of user.
   * @return the roles that can be assumed by the type of user being created or edited,
   *         as a <code>List</code> of {@link Role} objects ordered by role name.
   */
  private List<Role> getRoleList( String userTypeCode )
  {
    List<Role> roleList = null;

    if ( userTypeCode != null && userTypeCode.length() > 0 )
    {
      RoleQueryConstraint queryConstraint = new RoleQueryConstraint();
      queryConstraint.setActive( Boolean.TRUE );
      queryConstraint.setUserTypesIncluded( new String[] { userTypeCode } );
      queryConstraint.setOrderedByName( true );

      roleList = getRoleService().getRoleList( queryConstraint );
    }
    else
    {
      roleList = new ArrayList<Role>( getRoleService().getAll() );
    }

    return roleList;
  }

  private List<Country> getCountryList()
  {
    List<Country> countryList = getCountryService().getAllActive();
    Collections.sort( countryList, new CountryComparator() );

    return countryList;
  }

  private CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }

  /**
   * Returns the role service.
   *
   * @return a reference to the role service.
   */
  private RoleService getRoleService()
  {
    return (RoleService)getService( RoleService.BEAN_NAME );
  }
}
