
package com.biperf.core.ui.goalquest;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.hierarchy.NodeSearchCriteria;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantSearchCriteria;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.participant.ParticipantSearchAjaxAction;
import com.biperf.core.ui.utils.RequestUtils;

/**
 * PartnerSearchAction.
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
 * <td>reddy</td>
 * <td>Feb 29, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class PartnerSearchAction extends ParticipantSearchAjaxAction
{

  // override
  protected String doParticipantSearch( ParticipantSearchCriteria criteria, HttpServletRequest request )
  {
    return super.doParticipantSearch( criteria, request );
  }

  // override
  /**
   * Overridden from @see com.biperf.core.ui.participant.ParticipantSearchAjaxAction#filterUsers(java.util.List, javax.servlet.http.HttpServletRequest)
   * @param participants
   * @param request
   * @return List
   */
  protected List filterUsers( List participants, HttpServletRequest request )
  {
    Promotion promotion = getPromotion( request );
    Long userId = getUserId( request );
    Long proxyUserId = getProxyUserId( request );
    Node mainUserNode = null;
    // filter paxes
    GoalQuestPromotion gqPromo = (GoalQuestPromotion)promotion;
    if ( gqPromo.getPartnerAudienceType() != null && gqPromo.getAwardType() != null && gqPromo.getAwardType().getCode().equals( PromotionAwardsType.POINTS ) )
    {
      AssociationRequestCollection ascRequestCollection = new AssociationRequestCollection();
      PromotionAssociationRequest promoAsscoiationRequest = new PromotionAssociationRequest( PromotionAssociationRequest.PARTNER_AUDIENCES );
      ascRequestCollection.add( promoAsscoiationRequest );
      gqPromo = (GoalQuestPromotion)getPromotionService().getPromotionByIdWithAssociations( gqPromo.getId(), ascRequestCollection );
    }

    for ( Iterator paxIter = participants.iterator(); paxIter.hasNext(); )
    {
      Participant pax = (Participant)paxIter.next();
      if ( mustRemove( pax, userId, proxyUserId, mainUserNode, gqPromo, request ) )
      {
        paxIter.remove();
      }
    }
    return participants;
  }

  // override
  protected String doLocationSearch( String query, NodeSearchCriteria criteria, boolean isSecondLevelSearch, HttpServletRequest request )
  {
    return super.doLocationSearch( query, criteria, isSecondLevelSearch, request );
  }

  /**
   * @param pax
   * @param userId
   * @param proxyUserId
   * @param mainUserNode
   * @param promotion
   * @param request
   * @return true/false
   */
  protected boolean mustRemove( Participant pax, Long userId, Long proxyUserId, Node mainUserNode, GoalQuestPromotion promotion, HttpServletRequest request )
  {
    // remove self
    if ( excludeSelf( request ) )
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
    // 20771 inactive pax wouldn't display in ajax partner search list.
    if ( pax.isActive() != null && !pax.isActive().booleanValue() )
    {
      return true;
    }

    // remove if not valid for this audience
    if ( promotion != null )
    {
      if ( !getAudienceService().isUserInPromotionPartnerAudiences( pax, promotion.getPartnerAudiences() ) )
      {
        return true;
      }
    }
    return false;

  }

  // Override For Goalquest promotion
  protected Promotion getPromotion( HttpServletRequest request )
  {
    // long paxId = RequestUtils.getOptionalParamLong( request, "participantId" );
    long promotionId = RequestUtils.getOptionalParamLong( request, "promotionId" );
    Promotion promotion = null;
    if ( promotionId != 0 )
    {
      promotion = getPromotionService().getPromotionById( new Long( promotionId ) );
    }
    return promotion;
  }

  protected boolean excludeSelf( HttpServletRequest request )
  {
    return true;
  }

  protected static PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  protected static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  protected static AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }
}
