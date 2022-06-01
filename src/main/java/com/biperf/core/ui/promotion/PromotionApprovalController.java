/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionApprovalController.java,v $
 */

package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.ApprovalConditionalAmmountOperatorType;
import com.biperf.core.domain.enums.ApprovalType;
import com.biperf.core.domain.enums.ApproverType;
import com.biperf.core.domain.enums.ClaimFormElementType;
import com.biperf.core.domain.enums.CustomApproverType;
import com.biperf.core.domain.enums.NominationAwardGroupType;
import com.biperf.core.domain.enums.NominationEvaluationType;
import com.biperf.core.domain.enums.PromotionApprovalOptionReasonType;
import com.biperf.core.domain.enums.PromotionApprovalOptionType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.hierarchy.NodeTypeService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;
import java.util.Arrays;
import javax.servlet.AsyncListener;

/**
 * PromotionApprovalController.
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
 * <td>tom</td>
 * <td>Jul 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionApprovalController extends BaseController
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
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
    PromotionApprovalForm promoApprovalForm = (PromotionApprovalForm)request.getAttribute( "promotionApprovalForm" );

    request.setAttribute( "hasParent", Boolean.valueOf( promoApprovalForm.isHasParent() ) );
    request.setAttribute( "hasChildren", Boolean.valueOf( promoApprovalForm.isHasChildren() ) );
    request.setAttribute( "promotionStatus", promoApprovalForm.getPromotionStatus() );

    if ( ObjectUtils.equals( promoApprovalForm.getPromotionStatus(), PromotionStatusType.EXPIRED ) || promoApprovalForm.isHasParent() )
    {
      request.setAttribute( "isPageEditable", Boolean.FALSE );
    }
    else
    {
      request.setAttribute( "isPageEditable", Boolean.TRUE );
    }

    if ( promoApprovalForm.getPromotionTypeCode().equals( "product_claim" ) )
    {
      request.setAttribute( "pageNumber", "6" );
    }
    if ( promoApprovalForm.getPromotionTypeCode().equals( PromotionType.ENGAGEMENT ) )
    {
      request.setAttribute( "pageNumber", "2" );
    }
    else if ( promoApprovalForm.getPromotionTypeCode().equals( "recognition" ) )
    {
      request.setAttribute( "pageNumber", "8" );
    }
    else if ( promoApprovalForm.getPromotionTypeCode().equals( PromotionType.NOMINATION ) )
    {
      request.setAttribute( "pageNumber", "6" );
    }

    // request.setAttribute( "approverTypes",
    // ApproverType.getList(promoApprovalForm.getPromotionTypeCode()) );
    request.setAttribute( "approvalConditionalAmountOperators", ApprovalConditionalAmmountOperatorType.getList() );

    request.setAttribute( "modulePath", PromotionWizardManager.getStrutsModulePath( promoApprovalForm.getPromotionTypeCode() ) );

    List numericClaimFormStepElements = buildNumbericClaimFormStepElements( new Long( promoApprovalForm.getClaimFormId() ) );
    request.setAttribute( "numericClaimFormStepElements", numericClaimFormStepElements );

    List approvalTypes = ApprovalType.getList();
    // Client customization for WIP #56492 starts
    if ( promoApprovalForm.getPromotionTypeCode().equals( "nomination" ) )
    {
      approvalTypes =null;
      approvalTypes = Arrays.asList( ApprovalType.lookup( ApprovalType.COKE_CUSTOM ), ApprovalType.lookup( ApprovalType.MANUAL ) );// Customization
    }
    // Client customization for WIP #56492 ends
    if ( numericClaimFormStepElements.isEmpty() )
    {
      approvalTypes = removeConditionalAmountApprovalType( approvalTypes );
    }
    List approverTypes = ApproverType.getList( promoApprovalForm.getPromotionTypeCode() );
    if ( promoApprovalForm.getPromotionTypeCode().equals( "nomination" ) )
    {
      approverTypes.removeAll( removalConditionalApproverList( promoApprovalForm.getAwardGroupMethod(), promoApprovalForm.getEvaluationType() ) );
      // Client customization for WIP #56492 starts
      if ( promoApprovalForm.getApprovalType().equals( ApprovalType.COKE_CUSTOM ) )
      {
          approverTypes = new ArrayList();
          approverTypes.add( ApproverType.lookup( ApproverType.SPECIFIC_APPROVERS ) );
      }
      // Client customization for WIP #56492 ends
    }
    // to fix 15541 - inorder to limit the approval types list to only automatic immediate
    // to fix 18057 - inorder to limit the approval types list to only automatic immediate
    if ( promoApprovalForm.getPromotionTypeCode().equals( "recognition" ) )
    {
      String promotionId = promoApprovalForm.getPromotionId();
      AbstractRecognitionPromotion promotion = (AbstractRecognitionPromotion)getPromotionService().getPromotionById( new Long( promotionId ) );
      if ( ( (RecognitionPromotion)promotion ).isIncludePurl() || ( (RecognitionPromotion)promotion ).isAllowRecognitionSendDate() )
      {
        approvalTypes = new ArrayList();
        approvalTypes.add( ApprovalType.lookup( ApprovalType.AUTOMATIC_IMMEDIATE ) );
      }
      //customization starts wip 42702
      if ( promoApprovalForm.getApprovalType().equals( ApprovalType.COKE_CUSTOM ) )
      {
    	  approverTypes = new ArrayList();
    	  approverTypes.add( ApproverType.lookup( ApproverType.SPECIFIC_APPROVERS ) );
      }
      ////customization end wip 42702
      if ( ! ( (RecognitionPromotion)promotion ).isIncludePurl() && ( (RecognitionPromotion)promotion ).isIncludeCelebrations() )
      {
        request.setAttribute( "pageNumber", 74 );
      }

      if ( ( (RecognitionPromotion)promotion ).isIncludePurl() )
      {
        if ( ( (RecognitionPromotion)promotion ).isIncludeCelebrations() )
        {
          request.setAttribute( "pageNumber", "80" );
        }
        else
        {
          request.setAttribute( "pageNumber", "62" );
        }
        request.setAttribute( "isBackToSweepStake", Boolean.TRUE );
        request.setAttribute( "isBackToAwards", Boolean.FALSE );
        request.setAttribute( "isPurlIncluded", Boolean.TRUE );
      }
      if ( ( (RecognitionPromotion)promotion ).isIncludeCelebrations() )
      {
        request.setAttribute( "isCelebrationsIncluded", Boolean.TRUE );
      }
    }
    List customApproverType = CustomApproverType.getList();
    request.setAttribute( "customApproverType", new ArrayList( customApproverType ) );
    request.setAttribute( "approverTypes", approverTypes );
    request.setAttribute( "evaluationType", promoApprovalForm.getEvaluationType() );
    request.setAttribute( "awardGroupMethod", promoApprovalForm.getAwardGroupMethod() );

    request.setAttribute( "approvalTypes", approvalTypes );

    request.setAttribute( "promoApprovalOptions", PromotionApprovalOptionType.getList() );
    request.setAttribute( "promoApprovalOptionReasons", PromotionApprovalOptionReasonType.getList() );

    request.setAttribute( "activeHierarchies", getHierarchyService().getActiveHierarchies() );
    request.setAttribute( "nodeTypes", getNodeTypeService().getAll() );

    if ( promoApprovalForm.getDefaultApproverId() != null && promoApprovalForm.getDefaultApproverId() != 0 )
    {
      request.setAttribute( "defaultApproverName", getParticipantService().getParticipantById( promoApprovalForm.getDefaultApproverId() ).getNameLFMWithComma() );
    }
    else if ( promoApprovalForm.getSelectedDefaultApproverUserId() != null && promoApprovalForm.getSelectedDefaultApproverUserId() != 0 )
    {
      request.setAttribute( "defaultApproverName", getParticipantService().getParticipantById( promoApprovalForm.getSelectedDefaultApproverUserId() ).getNameLFMWithComma() );
    }
  }

  private static List removalConditionalApproverList( String awardGroupType, String evalType )
  {
    List filteredList = new ArrayList();
    if ( awardGroupType.equals( NominationAwardGroupType.INDIVIDUAL ) && evalType.equals( NominationEvaluationType.CUMULATIVE ) )
    {

      filteredList.add( ApproverType.lookup( ApproverType.NOMINATOR_NODE_OWNER_BY_LEVEL ) );
      filteredList.add( ApproverType.lookup( ApproverType.NOMINATOR_NODE_OWNER_BY_TYPE ) );

    }
    if ( !awardGroupType.equals( NominationAwardGroupType.INDIVIDUAL ) && evalType.equals( NominationEvaluationType.INDEPENDENT ) )
    {
      filteredList.add( ApproverType.lookup( ApproverType.NOMINEE_NODE_OWNER_BY_LEVEL ) );
      filteredList.add( ApproverType.lookup( ApproverType.NOMINEE_NODE_OWNER_BY_TYPE ) );
    }
    return filteredList;

  }

  private List removeConditionalAmountApprovalType( List approvalTypes )
  {
    // approvalTypes List is Unmodifiable so create new list without the cond amount entry.
    List filteredApprovalTypes = new ArrayList( approvalTypes );
    filteredApprovalTypes.remove( ApprovalType.lookup( ApprovalType.CONDITIONAL_AMOUNT ) );

    return filteredApprovalTypes;
  }

  /**
   * @param claimFormId
   * @return List
   */
  private List buildNumbericClaimFormStepElements( Long claimFormId )
  {
    List numericClaimFormStepElements = new ArrayList();
    ClaimFormElementType cfet = ClaimFormElementType.lookup( ClaimFormElementType.NUMBER_FIELD );
    numericClaimFormStepElements = getClaimFormDefinitionService().getAllClaimFormStepElementsByClaimFormIdElementType( claimFormId, cfet );

    return numericClaimFormStepElements;
  }

  /**
   * Retrieves a ClaimFormDefinitionService
   * 
   * @return ClaimFormDefinitionService
   */
  private ClaimFormDefinitionService getClaimFormDefinitionService()
  {
    return (ClaimFormDefinitionService)getService( ClaimFormDefinitionService.BEAN_NAME );
  }

  private UserCharacteristicService getUserCharacteristicService()
  {
    return (UserCharacteristicService)getService( UserCharacteristicService.BEAN_NAME );
  }

  /**
   * Returns the hierarchy service.
   * 
   * @return a reference to the hierarchy service.
   */
  private HierarchyService getHierarchyService()
  {
    return (HierarchyService)getService( HierarchyService.BEAN_NAME );
  }

  /**
   * Returns the node type service.
   * 
   * @return a reference to the node type service.
   */
  private NodeTypeService getNodeTypeService()
  {
    return (NodeTypeService)getService( NodeTypeService.BEAN_NAME );
  }

  /**
   * Returns the PromotionService.
   * 
   * @return a reference to the PromotionService.
   */
  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }
}
