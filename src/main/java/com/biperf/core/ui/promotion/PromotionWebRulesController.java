/**
 * 
 */

package com.biperf.core.ui.promotion;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.AudienceNameComparator;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionManagerWebRulesAudience;
import com.biperf.core.domain.promotion.PromotionPartnerWebRulesAudience;
import com.biperf.core.domain.promotion.PromotionWebRulesAudience;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.util.StringUtils;

/**
 * PromotionWebRulesController.
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
 * <td>asondgeroth</td>
 * <td>Jul 12, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionWebRulesController extends BaseController
{

  /**
   * Overridden from
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    List availableAudiences = getAudienceService().getAll();
    List managerAvailableAudiences = getAudienceService().getAll();
    List partnerAvailableAudiences = getAudienceService().getAll();

    Set assignedAudiences = (LinkedHashSet)request.getSession().getAttribute( "sessionAudienceList" );
    if ( assignedAudiences != null && !assignedAudiences.isEmpty() )
    {
      Iterator audienceIterator = availableAudiences.iterator();

      // Iterate over the webRulesAudience
      while ( audienceIterator.hasNext() )
      {
        Audience promotionWebRulesAudience = (Audience)audienceIterator.next();
        Iterator assignedIterator = assignedAudiences.iterator();
        while ( assignedIterator.hasNext() )
        {
          PromotionWebRulesAudience promoWebRuleAudience = (PromotionWebRulesAudience)assignedIterator.next();
          if ( promoWebRuleAudience.getAudience().getName().equals( promotionWebRulesAudience.getName() ) )
          {
            audienceIterator.remove();
          }
        }
      }
    }

    PromotionWebRulesForm promoWebRulesForm = (PromotionWebRulesForm)request.getAttribute( "promotionWebRulesForm" );

    if ( ObjectUtils.equals( promoWebRulesForm.getPromotionStatus(), PromotionStatusType.EXPIRED ) && ( !promoWebRulesForm.isActive() || !StringUtils.isEmpty( promoWebRulesForm.getEndDate() ) ) )
    {
      request.setAttribute( "isPageEditable", Boolean.FALSE );
    }
    else
    {
      request.setAttribute( "isPageEditable", Boolean.TRUE );
    }

    // determine if the secondary audience are non-participant - if they are
    // then we should exclude the 'all eligible givers and receivers' and
    // 'all eligible receivers' audiences
    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();

    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SECONDARY_AUDIENCES ) );
    if ( promoWebRulesForm.getPromotionTypeCode() != null && promoWebRulesForm.getPromotionTypeCode().equals( PromotionType.GOALQUEST ) )
    {
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PARTNER_AUDIENCES ) );
    }

    Promotion promotion = getPromotionService().getPromotionByIdWithAssociations( new Long( promoWebRulesForm.getPromotionId() ), promoAssociationRequestCollection );

    // check for non-pax
    Collections.sort( availableAudiences, new AudienceNameComparator() );
    request.setAttribute( "secondaryAudienceType", promotion.getSecondaryAudienceType() );
    request.setAttribute( "availableAudiences", availableAudiences );
    request.setAttribute( "hasParent", Boolean.valueOf( promoWebRulesForm.isHasParent() ) );
    request.setAttribute( "promotionStatus", promoWebRulesForm.getPromotionStatus() );

    if ( promotion.isGoalQuestOrChallengePointPromotion() )
    {
      Set assignedManagerAudiences = (LinkedHashSet)request.getSession().getAttribute( "sessionManagerAudienceList" );

      Set assignedPartnerAudiences = (LinkedHashSet)request.getSession().getAttribute( "sessionPartnerAudienceList" );

      if ( assignedManagerAudiences != null )
      {
        Iterator audienceManagerIterator = managerAvailableAudiences.iterator();

        // Iterate over the webRulesManagerAudience
        while ( audienceManagerIterator.hasNext() )
        {
          Audience promotionManagerWebRulesAudience = (Audience)audienceManagerIterator.next();
          Iterator assignedManagerIterator = assignedManagerAudiences.iterator();
          while ( assignedManagerIterator.hasNext() )
          {
            PromotionManagerWebRulesAudience promoManagerWebRuleAudience = (PromotionManagerWebRulesAudience)assignedManagerIterator.next();
            if ( promoManagerWebRuleAudience.getAudience().getName().equals( promotionManagerWebRulesAudience.getName() ) )
            {
              audienceManagerIterator.remove();
            }
          }
        }
      }

      request.setAttribute( "availableManagerAudiences", managerAvailableAudiences );

      if ( promoWebRulesForm.isPartnerAvailable( promotion ) )
      {
        if ( assignedPartnerAudiences != null )
        {
          Iterator audiencePartnerIterator = partnerAvailableAudiences.iterator();

          // Iterate over the webRulesPartnerAudience
          while ( audiencePartnerIterator.hasNext() )
          {
            Audience promotionPartnerWebRulesAudience = (Audience)audiencePartnerIterator.next();
            Iterator assignedPartnerIterator = assignedPartnerAudiences.iterator();
            while ( assignedPartnerIterator.hasNext() )
            {
              PromotionPartnerWebRulesAudience promoPartnerWebRuleAudience = (PromotionPartnerWebRulesAudience)assignedPartnerIterator.next();
              if ( promoPartnerWebRuleAudience.getAudience().getName().equals( promotionPartnerWebRulesAudience.getName() ) )
              {
                audiencePartnerIterator.remove();
              }
            }
          }
        }
      }
      request.setAttribute( "availablePartnerAudiences", partnerAvailableAudiences );
    }

    if ( promoWebRulesForm.getPromotionTypeCode().equals( PromotionType.PRODUCT_CLAIM ) )
    {
      request.setAttribute( "pageNumber", "8" );
    }
    else if ( promoWebRulesForm.getPromotionTypeCode().equals( PromotionType.RECOGNITION ) || promoWebRulesForm.getPromotionTypeCode().equals( PromotionType.NOMINATION ) )
    {
      if ( promotion.isRecognitionPromotion() )
      {
        if ( ( (RecognitionPromotion)promotion ).isIncludePurl() )
        {
          if ( ( (RecognitionPromotion)promotion ).isIncludeCelebrations() )
          {
            request.setAttribute( "pageNumber", "82" );
          }
          else
          {
            request.setAttribute( "pageNumber", "64" );
          }
          request.setAttribute( "isPurlIncluded", Boolean.TRUE );
        }
        else
        {
          if ( ( (RecognitionPromotion)promotion ).isIncludeCelebrations() )
          {
            request.setAttribute( "pageNumber", "77" );
          }
          else
          {
            request.setAttribute( "pageNumber", "10" );
          }
        }
        if ( ( (RecognitionPromotion)promotion ).isIncludeCelebrations() )
        {
          request.setAttribute( "isCelebrationsIncluded", Boolean.TRUE );
        }
      }
      else
      {
        request.setAttribute( "pageNumber", "11" );
      }
    }
    else if ( promoWebRulesForm.getPromotionTypeCode().equals( PromotionType.QUIZ ) )
    {
      request.setAttribute( "pageNumber", "6" );
    }
    else if ( promoWebRulesForm.getPromotionTypeCode().equals( PromotionType.CHALLENGE_POINT ) )
    {
      if ( promotion.getAwardType() != null && promotion.getAwardType().getCode().equalsIgnoreCase( PromotionAwardsType.POINTS ) && promotion.getPartnerAudienceType() != null )
      {
        request.setAttribute( "pageNumber", "23" );
        request.setAttribute( "isPartnersEnabled", "true" );
      }
      else
      {
        request.setAttribute( "pageNumber", "6" );
        request.setAttribute( "isPartnersEnabled", "false" );
      }
    }
    else if ( promoWebRulesForm.getPromotionTypeCode().equals( PromotionType.GOALQUEST ) )
    {
      if ( promotion.getAwardType() != null && promotion.getAwardType().getCode().equalsIgnoreCase( PromotionAwardsType.POINTS ) && ( (GoalQuestPromotion)promotion ).getPartnerAudienceType() != null )
      {
        request.setAttribute( "pageNumber", "7" );
        request.setAttribute( "isPartnersEnabled", "true" );
      }
      else
      {
        request.setAttribute( "pageNumber", "6" );
        request.setAttribute( "isPartnersEnabled", "false" );
      }

    }
    else if ( promoWebRulesForm.getPromotionTypeCode().equals( PromotionType.THROWDOWN ) )
    {
      request.setAttribute( "pageNumber", "5" );
    }

    if ( !promotion.isRecognitionPromotion() )
    {
      List localeItems = getCMAssetService().getSupportedLocales( true );
      // since we are excluding user locale,even if one locale is available, allow for translation.
      if ( localeItems != null && localeItems.size() < 1 )
      {
        request.setAttribute( "isLastPage", Boolean.TRUE );
      }
    }

  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private CMAssetService getCMAssetService()
  {
    return (CMAssetService)getService( CMAssetService.BEAN_NAME );
  }

}
