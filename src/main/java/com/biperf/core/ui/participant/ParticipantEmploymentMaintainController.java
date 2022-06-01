/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/participant/ParticipantEmploymentMaintainController.java,v $
 *
 */

package com.biperf.core.ui.participant;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.PositionType;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.employer.EmployerService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.ui.BaseController;

/**
 * ParticipantEmploymentMaintainController <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>May 12, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 *          Exp $
 */
public class ParticipantEmploymentMaintainController extends BaseController
{
  /**
   * Will preload a list of all positions, departments and the current employer name and put them in
   * the request scope.
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
    // request.setAttribute( "jobPositionList", PositionType.getList() );
    request.setAttribute( "jobPositionList", getUserService().getPickListValuesFromCM( PositionType.PICKLIST_ASSET + ".items", "en_US" ) );
    request.setAttribute( "departmentList", DepartmentType.getList() );

    ParticipantEmploymentForm form = (ParticipantEmploymentForm)request.getAttribute( ParticipantEmploymentForm.UPDATE_FORM_NAME );
    Long userId = new Long( form.getUserId() );

    User user = getUserService().getUserById( userId );
    request.setAttribute( "user", user );
    request.setAttribute( "employerName", getEmployerService().getEmployerById( new Long( form.getEmployerId() ) ).getName() );

    // for displaying name
    if ( userId != null )
    {
      String requestUserId = userId.toString();
      request.setAttribute( "displayNameUserId", requestUserId );
    }
  }

  private EmployerService getEmployerService() throws Exception
  {
    return (EmployerService)getService( EmployerService.BEAN_NAME );
  }

  /**
   * Get the EmployerService from the beanLocator.
   * 
   * @return EmployerService
   * @throws Exception
   */
  private UserService getUserService() throws Exception
  {

    return (UserService)getService( UserService.BEAN_NAME );
  }

}
