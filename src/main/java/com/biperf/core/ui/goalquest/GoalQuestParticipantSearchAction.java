
package com.biperf.core.ui.goalquest;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.domain.enums.PartnerAudienceType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.goalquest.GoalQuestService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantSearchCriteria;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.search.ParticipantAutoCompleteSearchAction;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.UserManager;

public class GoalQuestParticipantSearchAction extends ParticipantAutoCompleteSearchAction
{
  protected static final String REQUEST_PARAM_PROMOTION_ID = "promotionId";

  @Override
  protected List<Participant> filterUsers( List<Participant> participants, HttpServletRequest request )
  {
    participants = super.filterUsers( participants, request );

    // exclude the user, always
    excludeMe( participants, request );

    GoalQuestPromotion promotion = getPromotion( request );

    // Audience based Partner filtering
    if ( isSpecificAudience( promotion ) )
    {
      for ( Iterator<Participant> paxIter = participants.iterator(); paxIter.hasNext(); )
      {
        Participant pax = (Participant)paxIter.next();
        if ( !getPromotionService().isParticipantMemberOfPromotionAudience( pax, promotion, false, null ) )
        {
          paxIter.remove();
        }
      }
    }
    // Character based Partner audience filtering
    if ( isUserCharBasedAudience( promotion ) )
    {
      for ( Iterator<Participant> paxIter = participants.iterator(); paxIter.hasNext(); )
      {
        Participant pax = (Participant)paxIter.next();
        // TO check for participant in UserCharacteristicTypeAudience
        if ( !getGoalQuestService().isParticipantInUserCharType( pax, promotion ) )
        {
          paxIter.remove();
        }
      }
    }

    return participants;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  protected ParticipantSearchCriteria getSearchCriteria( HttpServletRequest request )
  {
    ParticipantSearchCriteria criteria = super.getSearchCriteria( request );
    // add GQ/CP criteria
    if ( isNodeBasedAudience( getPromotion( request ) ) )
    {
      AssociationRequestCollection ascReqColl = new AssociationRequestCollection();
      ascReqColl.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.USER_NODE ) );
      Participant pax = getParticipantService().getParticipantByIdWithAssociations( getUserId( request ), ascReqColl );
      criteria.setNodeList( pax.getUserNodesAsNodes() );
      criteria.setNodeIdAndBelow( false );
    }
    return criteria;
  }

  protected void excludeMe( List<Participant> participants, HttpServletRequest request )
  {
    Long userId = getUserId( request );
    for ( Iterator<Participant> paxIter = participants.iterator(); paxIter.hasNext(); )
    {
      if ( ( (Participant)paxIter.next() ).getId().equals( userId ) )
      {
        paxIter.remove();
        break;
      }
    }
  }

  protected GoalQuestPromotion getPromotion( HttpServletRequest request )
  {
    GoalQuestWizardForm wizard = (GoalQuestWizardForm)request.getSession().getAttribute( "goalQuestWizardForm" );
    return wizard.getPromotion();
  }

  protected boolean isNodeBasedAudience( GoalQuestPromotion promotion )
  {
    return null != promotion.getPartnerAudienceType() && promotion.getPartnerAudienceType().getCode().equals( PartnerAudienceType.NODE_BASED_PARTNER_AUDIENCE_CODE );
  }

  protected boolean isUserCharBasedAudience( GoalQuestPromotion promotion )
  {
    return null != promotion.getPartnerAudienceType() && promotion.getPartnerAudienceType().getCode().equals( PartnerAudienceType.USER_CHAR );
  }

  protected boolean isSpecificAudience( GoalQuestPromotion promotion )
  {
    return null != promotion.getPartnerAudienceType() && promotion.getPartnerAudienceType().getCode().equals( PartnerAudienceType.SPECIFY_AUDIENCE_CODE );
  }

  protected PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  protected AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

  protected GoalQuestService getGoalQuestService()
  {
    return (GoalQuestService)getService( GoalQuestService.BEAN_NAME );
  }

  protected Long getUserId( HttpServletRequest request )
  {
    String id = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "userId" );
    if ( null != id )
    {
      return new Long( id );
    }
    else
    {
      GoalQuestWizardForm form = (GoalQuestWizardForm)request.getSession().getAttribute( "goalQuestWizardForm" );
      if ( form.getUserId() != null )
      {
        return form.getUserId();
      }
      else
      {
        return UserManager.getUserId();
      }
    }
  }
}
