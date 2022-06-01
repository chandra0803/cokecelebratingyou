/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/approvals/ApprovalsRecognitionDetailsForm.java,v $
 */

package com.biperf.core.ui.approvals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.PromotionApprovalOptionReasonType;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.recognition.state.CalculatorResult;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClaimApproveUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * RecognitionApprovalDetailsForm.
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
 * <td>zahler</td>
 * <td>Aug 30, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ApprovalsRecognitionDetailsForm extends BaseForm
{
  private static final long serialVersionUID = 1L;
  private static final Log LOG = LogFactory.getLog( ApprovalsRecognitionDetailsForm.class );

  static String RECOGNITION_APPROVAL_DETAILS_FORM = "details";

  private String method;
  private String claimId;
  private Long promoId;
  private Long paxId;
  private boolean scoredByApprover;
  private ApprovalsRecognitionForm approvalsRecognitionForm;

  private Long calculatorScore;
  private Map<Integer, CalculatorResult> calculatorResults = new HashMap<Integer, CalculatorResult>();

  public void setCalculatorScore( Long calculatorScore )
  {
    this.calculatorScore = calculatorScore;
  }

  public Long getCalculatorScore()
  {
    return calculatorScore;
  }

  public boolean isScoredByApprover()
  {
    return scoredByApprover;
  }

  public void setScoredByApprover( boolean scoredByApprover )
  {
    this.scoredByApprover = scoredByApprover;
  }

  public Long getPaxId()
  {
    return paxId;
  }

  public void setPaxId( Long paxId )
  {
    this.paxId = paxId;
  }

  public Long getPromoId()
  {
    return promoId;
  }

  public void setPromoId( Long promoId )
  {
    this.promoId = promoId;
  }

  public String getFormName()
  {
    return RECOGNITION_APPROVAL_DETAILS_FORM;
  }

  public void setApprovalsRecognitionForm( ApprovalsRecognitionForm approvalsRecognitionForm )
  {
    this.approvalsRecognitionForm = approvalsRecognitionForm;
  }

  public ApprovalsRecognitionForm getApprovalsRecognitionForm()
  {
    return approvalsRecognitionForm;
  }

  /**
   * @param claim
   */
  public void load( RecognitionClaim claim )
  {
    for ( ClaimRecipient claimRecipient : claim.getClaimRecipients() )
    {
      String approvalStatusTypeCode = claimRecipient.getApprovalStatusType() == null ? null : claimRecipient.getApprovalStatusType().getCode();

      PromotionApprovalOptionReasonType promotionApprovalOptionReasonType = claimRecipient.getPromotionApprovalOptionReasonType();
      String holdPromotionApprovalOptionReasonTypeCode = null;
      String denyPromotionApprovalOptionReasonTypeCode = null;
      if ( approvalStatusTypeCode != null && promotionApprovalOptionReasonType != null )
      {
        if ( approvalStatusTypeCode.equals( ApprovalStatusType.DENIED ) )
        {
          denyPromotionApprovalOptionReasonTypeCode = promotionApprovalOptionReasonType.getCode();
        }
        else
        {
          holdPromotionApprovalOptionReasonTypeCode = promotionApprovalOptionReasonType.getCode();
        }
      }
      PromoMerchProgramLevel level = claimRecipient.getPromoMerchProgramLevel();
      if ( level != null )
      {
        List<PromoMerchCountry> countryList = new ArrayList<PromoMerchCountry>();
        countryList.add( level.getPromoMerchCountry() );
        try
        {
          getMerchLevelService().mergeMerchLevelWithOMList( countryList );
        }
        catch( ServiceErrorException see )
        {
          LOG.error( "Could not able to get max value for level Id " + level.getId(), see );
        }
      }

      this.approvalsRecognitionForm = new ApprovalsRecognitionForm( claim.getPromotion().getId(),
                                                                    claimRecipient.getId(),
                                                                    claimRecipient.getAwardQuantity() == null ? 0L : claimRecipient.getAwardQuantity(),
                                                                    approvalStatusTypeCode,
                                                                    holdPromotionApprovalOptionReasonTypeCode,
                                                                    denyPromotionApprovalOptionReasonTypeCode,
                                                                    level,
                                                                    RECOGNITION_APPROVAL_DETAILS_FORM );
    }

  }

  /**
   * Populate the claim with the claim recipient approval details and the approver comments.
   * 
   * @param claim
   * @param approver
   */
  public void populateClaimRecipientDomainObjects( RecognitionClaim claim, User approver )
  {
    for ( ClaimRecipient claimRecipient : claim.getClaimRecipients() )
    {
      if ( approvalsRecognitionForm == null || !claimRecipient.getId().equals( approvalsRecognitionForm.getClaimRecipientId() ) )
      {
        throw new BeaconRuntimeException( "form claim recipient Id not found. claim recipient Id: " + claimRecipient.getId() );
      }

      String approvalStatusTypeCode = approvalsRecognitionForm.getApprovalStatusType();
      String holdPromotionApprovalOptionReasonTypeCode = approvalsRecognitionForm.getHoldPromotionApprovalOptionReasonType();
      String denyPromotionApprovalOptionReasonTypeCode = approvalsRecognitionForm.getDenyPromotionApprovalOptionReasonType();

      // claimRecipientPlaceholder.setAwardQuantity(recognitionApprovalForm.getAwardAmount());
      // only fetch the PromoMerchProgramLevel if required
      if ( approvalsRecognitionForm.getProgramLevel() != null && approvalsRecognitionForm.getProgramLevel().getId() != null )
      {
        claimRecipient.setPromoMerchProgramLevel( getMerchLevelService().getPromoMerchProgramLevelById( approvalsRecognitionForm.getProgramLevel().getId() ) );
      }

      ClaimApproveUtils.setClaimItemApproverDetails( approver, approvalStatusTypeCode, holdPromotionApprovalOptionReasonTypeCode, denyPromotionApprovalOptionReasonTypeCode, claimRecipient );
    }
  }

  /**
   * @return value of method property
   */
  public String getMethod()
  {
    return method;
  }

  /**
   * @param method value for method property
   */
  public void setMethod( String method )
  {
    this.method = method;
  }

  /**
   * @return value of claimId property
   */
  public String getClaimId()
  {
    return claimId;
  }

  /**
   * @param claimId value for claimId property
   */
  public void setClaimId( String claimId )
  {
    this.claimId = claimId;
  }

  public List<CalculatorResult> getCalculatorResults()
  {
    return new ArrayList<CalculatorResult>( calculatorResults.values() );
  }

  /**
   * Overridden from
   * 
   * @see org.apache.struts.validator.ValidatorForm#reset(org.apache.struts.action.ActionMapping,
   *      javax.servlet.http.HttpServletRequest)
   * @param mapping
   * @param request
   */
  @SuppressWarnings( "unchecked" )
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    super.reset( mapping, request );

    // Must recreate map form object, so we need claimproduct ids.
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );

    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map<String, Object> clientStateMap = ClientStateSerializer.deserialize( clientState, password );

      Object claimIdParam = clientStateMap.get( "claimId" );
      if ( claimIdParam != null )
      {
        Long claimId = null;
        if ( claimIdParam instanceof String )
        {
          claimId = Long.valueOf( (String)claimIdParam );
        }
        else
        {
          claimId = (Long)claimIdParam;
        }

        RecognitionClaim claim = (RecognitionClaim)getClaimService().getClaimByIdWithAssociations( claimId, associationRequestCollection );
        for ( ClaimRecipient claimRecipient : claim.getClaimRecipients() )
        {
          approvalsRecognitionForm = new ApprovalsRecognitionForm();
          approvalsRecognitionForm.setClaimRecipientId( claimRecipient.getId() );
        }
      }
      else
      {
        LOG.error( "claimId not found in client state" );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
  }

  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( mapping, request );
    if ( actionErrors.isEmpty() )
    {
      actionErrors = approvalsRecognitionForm.validate( mapping, request );
      if ( !actionErrors.isEmpty() )
      {
        request.setAttribute( "serverReturnedErrored", true );
      }
    }
    return actionErrors;
  }

  private ClaimService getClaimService()
  {
    return (ClaimService)BeanLocator.getBean( ClaimService.BEAN_NAME );
  }

  public MerchLevelService getMerchLevelService()
  {
    return (MerchLevelService)BeanLocator.getBean( MerchLevelService.BEAN_NAME );
  }
}
