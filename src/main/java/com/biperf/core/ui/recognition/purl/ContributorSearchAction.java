
package com.biperf.core.ui.recognition.purl;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.participant.ParticipantSearchCriteria;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.search.ParticipantAutoCompleteSearchAction;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.util.CmsResourceBundle;

public class ContributorSearchAction extends ParticipantAutoCompleteSearchAction
{
  private Long getRecipientId( HttpServletRequest request )
  {
    return RequestUtils.getRequiredParamLong( request, "recipientId" );
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

  @Override
  protected List<Participant> filterUsers( List<Participant> participants, HttpServletRequest request )
  {
    String[] contributorIds = request.getParameterValues( "contributorId[]" );
    Promotion promotion = getPromotion( request );
    Long userId = UserManager.getUserId();
    Long proxyUserId = null;

    // filter paxes
    for ( Iterator paxIter = participants.iterator(); paxIter.hasNext(); )
    {
      Participant pax = (Participant)paxIter.next();
      if ( mustRemove( pax, userId, proxyUserId, promotion, contributorIds, request ) )
      {
        paxIter.remove();
      }
    }

    return participants;
  }

  private boolean mustRemove( Participant pax, Long userId, Long proxyUserId, Promotion promotion, String[] contributorIds, HttpServletRequest request )
  {
    if ( userId == null )
    {
      userId = UserManager.getUserId();
    }

    if ( userId != null && userId.equals( pax.getId() ) )
    {
      return true;
    }

    if ( proxyUserId != null && proxyUserId.equals( pax.getId() ) )
    {
      return true;
    }

    Long purlRecipientUserId = getRecipientId( request );
    if ( null != purlRecipientUserId )
    {
      if ( purlRecipientUserId.equals( pax.getId() ) )
      {
        return true;
      }
    }

    if ( contributorIds != null && contributorIds.length > 0 )
    {
      // Exclude preselected contributors
      for ( int i = 0; i < contributorIds.length; i++ )
      {
        String contributorUserId = contributorIds[i];
        if ( contributorUserId.equals( pax.getId().toString() ) )
        {
          return true;
        }
      }
    }

    // remove self
    if ( excludeSelf( promotion ) )
    {
      if ( userId != null && userId.equals( pax.getId() ) )
      {
        return true;
      }
      if ( proxyUserId != null && proxyUserId.equals( pax.getId() ) )
      {
        return true;
      }
    }

    return false;

  }

  @Override
  protected boolean isSearchEmpty( HttpServletRequest request )
  {
    return false;
  }

  private boolean excludeSelf( Promotion promotion )
  {
    boolean exclude = false;

    if ( promotion.isNominationPromotion() )
    {
      if ( ! ( (NominationPromotion)promotion ).isSelfNomination() )
      {
        exclude = true;
      }
    }
    else if ( promotion.isRecognitionPromotion() )
    {
      if ( ! ( (RecognitionPromotion)promotion ).isSelfRecognitionEnabled() )
      {
        exclude = true;
      }
    }
    else
    {
      exclude = true;
    }

    return exclude;
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  @Override
  protected void populatePaxSourceType( Participant pax, ParticipantSearchCriteria searchCriteria )
  {
    if ( searchCriteria.getNodeId() != null )
    {
      pax.setSourceType( CmsResourceBundle.getCmsBundle().getString( "participant.search.TEAM" ) );
    }
  }

}
