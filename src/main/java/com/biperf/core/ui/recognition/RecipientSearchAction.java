
package com.biperf.core.ui.recognition;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantSearchListView;
import com.biperf.core.domain.participant.ParticipantSearchView;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.search.ParticipantAutoCompleteSearchAction;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BudgetUtils;
import com.biperf.core.utils.UserManager;

public class RecipientSearchAction extends ParticipantAutoCompleteSearchAction
{

  protected static final String REQUEST_PARAM_PROMOTION_ID = "promotionId";

  @Override
  protected List<Participant> filterUsers( List<Participant> participants, HttpServletRequest request )
  {
    Promotion promotion = getPromotion( request );
    Long userId = UserManager.getUserId();
    Long proxyUserId = getProxyUserId();
    // load main user node
    Node mainUserNode = getNodeService().getNodeById( getMainUserNode( request ) );

    // filter paxes
    for ( Iterator paxIter = participants.iterator(); paxIter.hasNext(); )
    {
      Participant pax = (Participant)paxIter.next();
      if ( mustRemove( pax, userId, proxyUserId, mainUserNode, promotion, request ) )
      {
        paxIter.remove();
      }
    }

    // filter by node
    if ( shouldFilterNodes( promotion ) )
    {
      participants = filterByNode( participants, promotion, mainUserNode );
    }

    participants = getAudienceService().filterParticipantNodesByAudienceNodeRole( participants, promotion.getSecondaryAudiences() );

    return participants;
  }

  private boolean mustRemove( Participant pax, Long userId, Long proxyUserId, Node mainUserNode, Promotion promotion, HttpServletRequest request )
  {
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

    // remove if not valid for this audience
    if ( !getPromotionService().isParticipantMemberOfPromotionAudience( pax, promotion, false, mainUserNode ) )
    {
      return true;
    }

    return false;
  }

  private List<Participant> filterByNode( List<Participant> participants, Promotion promotion, Node mainUserNode )
  {
    // collect user nodes
    Set nodeSet = new HashSet();
    for ( Iterator paxIter = participants.iterator(); paxIter.hasNext(); )
    {
      User currentPax = (User)paxIter.next();
      nodeSet.addAll( currentPax.getUserNodesAsNodes() );
    }

    // filter nodes
    List<Node> filteredNodes = getAudienceService().filterNodeListBySecondaryAudience( promotion, nodeSet, mainUserNode );
    // filter participants that are in removed nodes
    for ( Iterator paxIter = participants.iterator(); paxIter.hasNext(); )
    {
      User currentPax = (User)paxIter.next();
      for ( Iterator userNodeIter = currentPax.getUserNodes().iterator(); userNodeIter.hasNext(); )
      {
        UserNode userNode = (UserNode)userNodeIter.next();
        if ( !filteredNodes.contains( userNode.getNode() ) )
        {
          userNodeIter.remove();
        }
      }
    }

    return participants;
  }

  @Override
  protected Promotion getPromotion( HttpServletRequest request )
  {
    Long promotionId = null;

    if ( request.getParameter( "promoId" ) != null )
    {
      promotionId = Long.valueOf( request.getParameter( "promoId" ) );
    }
    else
    {
      promotionId = RequestUtils.getRequiredParamLong( request, BaseRecognitionAction.REQUEST_PARAM_PROMOTION_ID );
    }

    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PRIMARY_AUDIENCES ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SECONDARY_AUDIENCES ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_MERCHANDISE_COUNTRIES ) );
    Promotion promotion = getPromotionService().getPromotionByIdWithAssociations( promotionId, promoAssociationRequestCollection );

    return promotion;
  }

  private Long getProxyUserId()
  {
    return null;
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

  private boolean shouldFilterNodes( Promotion promotion )
  {
    if ( promotion == null || promotion.getSecondaryAudienceType() == null )
    {
      return false;
    }

    if ( promotion.getSecondaryAudienceType().isSpecificNodeType() )
    {
      return true;
    }

    if ( promotion.getSecondaryAudienceType().isSpecificNodeAndBelowType() )
    {
      return true;
    }

    if ( promotion.getSecondaryAudienceType().isSpecifyAudienceType() )
    {
      return true;
    }

    if ( promotion.getSecondaryAudienceType().isSameAsPrimaryType() && promotion.getPrimaryAudienceType() != null && promotion.getPrimaryAudienceType().isSpecifyAudienceType() )
    {
      return true;
    }

    return false;
  }

  private Long getMainUserNode( HttpServletRequest request )
  {
    if ( request.getParameter( "nodeId" ) != null )
    {
      return RequestUtils.getRequiredParamLong( request, BaseRecognitionAction.REQUEST_PARAM_NODE_ID );
    }
    return (long)0;
  }

  /**
   * Will return a list of valid recipient countries.  If all countries are valid
   * then null will be returned.  If this is a merchandise promotion then only 
   * countries that have programs set will be valid.
   */
  public Set<String> getValidRecipientCountries( Promotion promotion )
  {
    if ( promotion != null && promotion.getAwardType() != null && promotion.getAwardType().isMerchandiseAwardType() )
    {
      Set<String> validCountries = new HashSet<String>();

      if ( promotion.getPromoMerchCountries() != null )
      {
        for ( Iterator<PromoMerchCountry> promoMerchCountryIter = promotion.getPromoMerchCountries().iterator(); promoMerchCountryIter.hasNext(); )
        {
          PromoMerchCountry promoMerchCountry = (PromoMerchCountry)promoMerchCountryIter.next();
          validCountries.add( promoMerchCountry.getCountry().getCountryCode() );
        }
      }
      return validCountries;
    }
    return null;
  }

  @Override
  protected void flagInvalidCountries( List<Participant> participants, Promotion promotion, ParticipantSearchListView listViewObj )
  {
    Set<String> validCountries = getValidRecipientCountries( promotion );
    if ( validCountries != null )
    {
      flagInvalidCountries( participants, validCountries, listViewObj );
    }
  }

  private void flagInvalidCountries( List<Participant> participants, Set<String> validCountries, ParticipantSearchListView listViewObj )
  {
    List<ParticipantSearchView> participantSearchViews = listViewObj.getParticipants();
    if ( participantSearchViews != null )
    {
      for ( int i = 0; i < participantSearchViews.size(); i++ )
      {
        ParticipantSearchView participantSearchView = participantSearchViews.get( i );
        if ( !validCountries.contains( participantSearchView.getCountryCode() ) )
        {
          // If participant is not part of the valid country of promotion then remove the
          // participant form the view
          participantSearchViews.remove( i );
          i--;
        }
      }
    }
  }

  @Override
  protected BigDecimal getBudgetConversionRatio( Long receiverId, Long submitterId, Long promotionId )
  {
    return BudgetUtils.getBudgetConversionRatio( getUserService(), getPromotionService(), promotionId, receiverId, submitterId );
  }

  /**
   * Does a Bean lookup for the UserService
   * 
   * @return UserService
   */
  protected static UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private NodeService getNodeService()
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

}
