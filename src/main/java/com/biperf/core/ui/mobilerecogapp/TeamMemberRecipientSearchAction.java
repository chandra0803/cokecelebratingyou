
package com.biperf.core.ui.mobilerecogapp;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.mobileapp.recognition.service.RecognitionService;
import com.biperf.core.service.participant.ParticipantSearchCriteria;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;

public class TeamMemberRecipientSearchAction extends com.biperf.core.ui.recognition.RecipientSearchAction
{
  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return super.generatePaxSearchView( mapping, form, request, response );
  }

  @Override
  protected List<Participant> searchParticipants( HttpServletRequest request )
  {
    Long userId = UserManager.getUserId();
    Long promotionId = RequestUtils.getRequiredParamLong( request, RecipientSearchAction.REQUEST_PARAM_PROMOTION_ID );
    return getRecognitionService().getTeamMemberRecipients( userId, promotionId );
  }

  @Override
  protected Long getPaxCountBasedOnCriteria( ParticipantSearchCriteria criteria )
  {
    return new Long( 0 );
  }

  @Override
  protected boolean isSearchEmpty( HttpServletRequest request )
  {
    return false;
  }

  protected RecognitionService getRecognitionService()
  {
    return (RecognitionService)BeanLocator.getBean( RecognitionService.BEAN_NAME );
  }
}
