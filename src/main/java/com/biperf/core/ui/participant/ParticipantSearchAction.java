/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/participant/ParticipantSearchAction.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.participant;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.domain.enums.PositionType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.participant.ParticipantSearchCriteria;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;

/**
 * ParticipantSearchAction.
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
 * <td>May 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ParticipantSearchAction extends BaseDispatchAction
{
  public static String RETURN_ACTION_URL_PARAM = "returnActionUrl";

  /**
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward unspecified( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ParticipantSearchForm participantSearchForm = (ParticipantSearchForm)actionForm;

    participantSearchForm.setReturnActionUrl( RequestUtils.getOptionalParamString( request, RETURN_ACTION_URL_PARAM ) );

    return mapping.findForward( ActionConstants.SEARCH_FORWARD );
  }

  public ActionForward search( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ParticipantSearchForm participantSearchForm = (ParticipantSearchForm)form;

    ParticipantSearchCriteria searchCriteria = participantSearchForm.toDomainObject();

    List participantList = getParticipantService().searchParticipant( searchCriteria );

    for ( Iterator iter = participantList.iterator(); iter.hasNext(); )
    {
      Participant participant = (Participant)iter.next();

      // PositionType jobPositionItem = PositionType.lookup( participant.getPositionType() );
      if ( participant.getPositionType() != null )
      {
        DynaPickListType jobPositionItem = DynaPickListType.lookup( PositionType.PICKLIST_ASSET + PositionType.ASSET_ITEM_SUFFIX, participant.getPositionType() );
        if ( jobPositionItem != null )
        {
          participant.setPositionType( jobPositionItem.getName() );
        }
      }

      if ( participant.getDepartmentType() != null )
      {
        // DepartmentType departmentItem = DepartmentType.lookup( participant.getDepartmentType() );
        DynaPickListType departmentItem = DynaPickListType.lookup( DepartmentType.PICKLIST_ASSET + DepartmentType.ASSET_ITEM_SUFFIX, participant.getDepartmentType() );
        if ( departmentItem != null )
        {
          participant.setDepartmentType( departmentItem.getName() );
        }
      }
    }

    request.setAttribute( "participantList", participantList );

    return mapping.findForward( ActionConstants.SUCCESS_SEARCH );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }
}
