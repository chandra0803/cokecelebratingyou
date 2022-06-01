/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/participant/ParticipantEmploymentListController.java,v $
 *
 */

package com.biperf.core.ui.participant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.ui.BaseController;

/**
 * ParticipantEmploymentListController <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>May 11, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class ParticipantEmploymentListController extends BaseController
{
  /**
   * Will preload a list of all participant employments and put them in the request scope.
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
    ParticipantEmploymentListForm participantEmploymentListForm = (ParticipantEmploymentListForm)request.getAttribute( "participantEmploymentListForm" );
    Long userId = new Long( participantEmploymentListForm.getUserId() );

    // Replacing paxOverview call with specific associations
    // Participant pax = getParticipantService().getParticipantOverviewById( userId );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMPLOYER ) );
    Participant pax = getParticipantService().getParticipantByIdWithAssociations( userId, associationRequestCollection );

    // The list of ParticipantEmployers is reversed in a new list, since we want to display the
    // current record at the
    // top of the list instead of the bottom. Also, we don't want hibernated to update all of the
    // records, hence the need
    // for a new list.
    List inverseOrderedPaxEmploymentList = new ArrayList( pax.getParticipantEmployers() );
    Collections.reverse( inverseOrderedPaxEmploymentList );
    request.setAttribute( "user", pax );
    request.setAttribute( "deptName", pax.getPaxDeptName() != null ? pax.getPaxDeptName() : null );
    request.setAttribute( "positionName", pax.getPaxJobName() != null ? pax.getPaxJobName() : null );
    request.setAttribute( "paxEmploymentList", inverseOrderedPaxEmploymentList );
    request.setAttribute( "emptyPaxEmploymentList", Boolean.valueOf( inverseOrderedPaxEmploymentList.size() == 0 ) );

    // for displaying name
    if ( userId != null )
    {
      String requestUserId = userId.toString();
      request.setAttribute( "displayNameUserId", requestUserId );
    }
  }

  /**
   * Get the EmployerService from the beanLocator.
   * 
   * @return EmployerService
   * @throws Exception
   */
  protected ParticipantService getParticipantService() throws Exception
  {

    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

}
