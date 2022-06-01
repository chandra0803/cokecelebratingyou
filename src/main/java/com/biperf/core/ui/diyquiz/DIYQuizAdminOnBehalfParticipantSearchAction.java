
package com.biperf.core.ui.diyquiz;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.search.ParticipantAutoCompleteSearchAction;
import com.biperf.core.ui.utils.RequestUtils;

/**
 * 
 * DIYQuizAdminOnBehalfParticipantSearchAction.
 * 
 * @author kandhi
 * @since Jul 22, 2013
 * @version 1.0
 */
public class DIYQuizAdminOnBehalfParticipantSearchAction extends ParticipantAutoCompleteSearchAction
{
  @Override
  protected List<Participant> filterUsers( List<Participant> participants, HttpServletRequest request )
  {
    Promotion promotion = getPromotion( request );
    for ( Iterator paxIter = participants.iterator(); paxIter.hasNext(); )
    {
      Participant pax = (Participant)paxIter.next();
      if ( !getPromotionService().isParticipantMemberOfPromotionAudience( pax, promotion, true, null ) )
      {
        paxIter.remove();
      }
    }
    return participants;
  }

  @Override
  protected Promotion getPromotion( HttpServletRequest request )
  {
    Long promotionId = RequestUtils.getRequiredParamLong( request, "promotionId" );
    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PRIMARY_AUDIENCES ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SECONDARY_AUDIENCES ) );
    Promotion promotion = getPromotionService().getPromotionByIdWithAssociations( promotionId, promoAssociationRequestCollection );
    return promotion;
  }

  protected static UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

}
