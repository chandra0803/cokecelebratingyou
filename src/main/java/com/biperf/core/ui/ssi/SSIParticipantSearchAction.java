
package com.biperf.core.ui.ssi;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.ssi.SSIPromotionService;
import com.biperf.core.ui.search.ParticipantAutoCompleteSearchAction;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.UserManager;

/**
 * Action class used to load and save the contest participants 
 * SSIContestParticipantSearchAction.
 * 
 * @author kandhi
 * @since Nov 19, 2014
 * @version 1.0
 */
public class SSIParticipantSearchAction extends ParticipantAutoCompleteSearchAction
{

  @Override
  protected List<Participant> filterUsers( List<Participant> participants, HttpServletRequest request )
  {
    Promotion promotion = getPromotion( request );
    AssociationRequestCollection userAssociationRequestCollection = new AssociationRequestCollection();
    userAssociationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.NODE ) );
    User user = getUserService().getUserByIdWithAssociations( UserManager.getUserId(), userAssociationRequestCollection );
    Node node = user.getPrimaryUserNode() != null ? user.getPrimaryUserNode().getNode() : null;
    for ( Iterator paxIter = participants.iterator(); paxIter.hasNext(); )
    {
      Participant pax = (Participant)paxIter.next();
      if ( UserManager.getUserId().equals( pax.getId() ) || !getPromotionService().isParticipantMemberOfPromotionAudience( pax, promotion, false, node ) )
      {
        paxIter.remove();
      }
    }
    return participants;
  }

  @Override
  protected Promotion getPromotion( HttpServletRequest request )
  {
    Promotion ssiPromotion = getSSIPromotionService().getLiveSSIPromotion();
    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PRIMARY_AUDIENCES ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SECONDARY_AUDIENCES ) );
    Promotion promotion = getPromotionService().getPromotionByIdWithAssociations( ssiPromotion.getId(), promoAssociationRequestCollection );
    return promotion;
  }

  @Override
  protected Integer getPaxSearchResultCount()
  {
    return SSIContestUtil.PAX_SEARCH_SIZE;
  }

  @Override
  protected boolean needParticipantProfileLink()
  {
    return false;
  }

  protected SSIPromotionService getSSIPromotionService()
  {
    return (SSIPromotionService)getService( SSIPromotionService.BEAN_NAME );
  }

  protected PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

}
