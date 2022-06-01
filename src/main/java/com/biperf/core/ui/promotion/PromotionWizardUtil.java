/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/promotion/PromotionWizardUtil.java,v $
 */

package com.biperf.core.ui.promotion;

import java.util.Iterator;
import java.util.List;

import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.promotion.PromotionApprovalUpdateAssociation;
import com.biperf.core.service.promotion.PromotionPayoutService;
import com.biperf.core.service.promotion.PromotionPayoutUpdateAssociation;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.utils.ServiceLocator;

/**
 * PromotionWizardUtil.
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
 * <td>crosenquest</td>
 * <td>Aug 16, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionWizardUtil
{

  /**
   * Deletes a promotion based on the state of the promotionBasicsForm passed in. Will be called
   * throughout the promotionWizard.
   * 
   * @param promotion
   * @throws ServiceErrorException
   */
  public static void deletePromotion( Promotion promotion ) throws ServiceErrorException
  {

    // If this promotion has already been saved and the user is cancelling, delete the promotion
    if ( null != promotion.getId() )
    {
      getPromotionService().deletePromotion( promotion.getId() );
    }
  }

  /**
   * Saves a list of promotionElementViolations
   * 
   * @param promotionElementValidations
   * @return List
   */
  public static List savePromotionClaimFormStepElementValidation( List promotionElementValidations )
  {
    return getPromotionService().savePromotionClaimFormStepElementValidationList( promotionElementValidations );
  }

  /**
   * Saves a promotion
   * 
   * @param promotion
   * @return Promotion
   * @throws UniqueConstraintViolationException
   */
  public static Promotion savePromotion( Promotion promotion ) throws UniqueConstraintViolationException
  {
    return getPromotionService().savePromotion( promotion );
  }

  /**
   * Saves a promotion based on the ID and the updateAssociation for the promotionPayout.
   * 
   * @param promotionId
   * @param promotionPayoutUpdateAssociation
   * @return Promotion
   * @throws UniqueConstraintViolationException
   */
  public static Promotion savePromotion( Long promotionId, PromotionPayoutUpdateAssociation promotionPayoutUpdateAssociation ) throws UniqueConstraintViolationException
  {
    return getPromotionService().savePromotion( promotionId, promotionPayoutUpdateAssociation );
  }

  /**
   * Delete the promotionPayout in a previous selection.
   * 
   * @param promotionId
   * @param promotionPayoutGroupId
   * @param promotionPayoutId
   */
  public static void deletePromotionPayout( Long promotionId, Long promotionPayoutGroupId, Long promotionPayoutId )
  {
    getPromotionPayoutService().delete( promotionId, promotionPayoutGroupId, promotionPayoutId );
  }

  /**
   * Get the node by Id.
   * 
   * @param nodeId
   * @return Node
   */
  public static Node getNodeById( Long nodeId )
  {
    return getNodeService().getNodeById( nodeId );
  }

  /**
   * Get the claimFormStepElement by id.
   * 
   * @param claimFormStepElementId
   * @return ClaimFormStepElement
   */
  public static ClaimFormStepElement getClaimFormStepElementById( Long claimFormStepElementId )
  {
    return getClaimFormDefinitionService().getClaimFormStepElementById( claimFormStepElementId );
  }

  /**
   * Saves a promotion based on the promotionApprovalUpdateAssociation.
   * 
   * @param promotionId
   * @param promotionApprovalUpdateAssociation
   * @return Promotion
   * @throws UniqueConstraintViolationException
   */
  public static Promotion savePromotion( Long promotionId, PromotionApprovalUpdateAssociation promotionApprovalUpdateAssociation ) throws UniqueConstraintViolationException
  {
    return getPromotionService().savePromotion( promotionId, promotionApprovalUpdateAssociation );
  }

  /**
   * Saves the promotion web rules text in CM.
   * 
   * @param promotion
   * @param rulesText
   * @throws ServiceErrorException
   */
  public static void saveWebRulesCmText( Promotion promotion, String rulesText ) throws ServiceErrorException
  {
    getPromotionService().saveWebRulesCmText( promotion, rulesText );
  }

  /**
   * Check if the number of program levels for a recognition promotion is the same for all countries
   * 
   * @param promotion
   * @return boolean
   */
  public static boolean numberOfLevelsEqualForAllCountries( Promotion promotion )
  {
    if ( promotion.getPromoMerchCountries() != null && promotion.getPromoMerchCountries().size() > 1 )
    {
      int numberOfLevels = ( (PromoMerchCountry)promotion.getPromoMerchCountries().iterator().next() ).getNumberOfLevels();
      for ( Iterator promoMerchCountryIter = promotion.getPromoMerchCountries().iterator(); promoMerchCountryIter.hasNext(); )
      {
        PromoMerchCountry currentPromoMerchCountry = (PromoMerchCountry)promoMerchCountryIter.next();
        if ( currentPromoMerchCountry.getNumberOfLevels() != numberOfLevels )
        {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Get the promotionPayoutService.
   * 
   * @return PromotionPayoutService
   */
  private static PromotionPayoutService getPromotionPayoutService()
  {
    return (PromotionPayoutService)ServiceLocator.getService( PromotionPayoutService.BEAN_NAME );
  }

  /**
   * Get the promotionService.
   * 
   * @return PromotionService
   */
  private static PromotionService getPromotionService()
  {
    return (PromotionService)ServiceLocator.getService( PromotionService.BEAN_NAME );
  }

  /**
   * Get the nodeService.
   * 
   * @return NodeService
   */
  private static NodeService getNodeService()
  {
    return (NodeService)ServiceLocator.getService( NodeService.BEAN_NAME );
  }

  /**
   * Get the claimFormDefinitionService.
   * 
   * @return ClaimFormDefinitionService
   */
  private static ClaimFormDefinitionService getClaimFormDefinitionService()
  {
    return (ClaimFormDefinitionService)ServiceLocator.getService( ClaimFormDefinitionService.BEAN_NAME );
  }

}
