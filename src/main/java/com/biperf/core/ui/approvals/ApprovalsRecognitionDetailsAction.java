/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/approvals/ApprovalsRecognitionDetailsAction.java,v $
 */

package com.biperf.core.ui.approvals;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.springframework.util.StringUtils;

import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.calculator.Calculator;
import com.biperf.core.domain.calculator.CalculatorCriterion;
import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.ApprovableItem;
import com.biperf.core.domain.claim.CalculatorResponse;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.ScoreBy;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.BudgetUsageOverAllocallatedException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.calculator.CalculatorAssociationRequest;
import com.biperf.core.service.calculator.CalculatorService;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.client.CokeClientService;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.claim.AbstractRecognitionApprovalAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.recognition.state.CalculatorResult;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.TcccClientUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ApprovalsNoEmailBean;

/**
 * RecognitionApprovalDetailsAction.
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
public class ApprovalsRecognitionDetailsAction extends AbstractRecognitionApprovalAction
{
  private static final Log log = LogFactory.getLog( ApprovalsRecognitionDetailsAction.class );

  protected ActionForward cancelled( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return mapping.findForward( ActionConstants.CANCEL_FORWARD );
  }

  /**
   * Prepares anything necessary before displaying the update screen.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  @SuppressWarnings( "unchecked" )
  public ActionForward prepareUpdate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws UnsupportedEncodingException
  {
    ActionMessages errors = new ActionMessages();

    Long claimId = getClaimIdFromClientState( request );
    if ( claimId == null )
    {
      errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "claimId and userId as part of clientState" ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.UPDATE_FORWARD );
    }

    AssociationRequestCollection claimAssociationRequestCollection = new AssociationRequestCollection();
    claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.ALL ) );
    RecognitionClaim claim = (RecognitionClaim)getClaimService().getClaimByIdWithAssociations( claimId, claimAssociationRequestCollection );

    // Reset promotion with hydrated version.
    AssociationRequestCollection promotionAssociationRequestCollection = new AssociationRequestCollection();
    promotionAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.APPROVAL_OPTION ) );
    claim.setPromotion( getPromotionService().getPromotionByIdWithAssociations( claim.getPromotion().getId(), promotionAssociationRequestCollection ) );

    /* Client customizations for WIP #42701 starts */
    if ( claim.getPromotion().getAdihCashOption() )
    {
      CokeClientService cokeClientService = (CokeClientService)getService( CokeClientService.BEAN_NAME );
      for ( ClaimRecipient claimRecipient : claim.getClaimRecipients() )
      {
        if ( "USD".equals( claimRecipient.getCashCurrencyCode() ) )
        {
          claimRecipient.setDisplayUSDAwardQuantity( claimRecipient.getCustomCashAwardQuantity() + " " + claimRecipient.getCashCurrencyCode() );
        }
        else
        {
        	if(null != claimRecipient.getAwardQuantity() && null != claimRecipient.getCashCurrencyCode()){
				claimRecipient.setDisplayUSDAwardQuantity(TcccClientUtils.convertToUSDString(cokeClientService,claimRecipient.getAwardQuantity(), claimRecipient.getCashCurrencyCode()));
			}else{
				if (StringUtils.isEmpty(claimRecipient.getAwardQuantity()))		claimRecipient.setAwardQuantity(0L);
				if (StringUtils.isEmpty(claimRecipient.getCashCurrencyCode())) {					
					claimRecipient.setCashCurrencyCode(getUserService().getUserCurrencyCharValue( claimRecipient.getRecipient().getId()));
				}
				claimRecipient.setDisplayUSDAwardQuantity( claimRecipient.getAwardQuantity() + " " + claimRecipient.getCashCurrencyCode() );
			}
       	}
      }
    }
    /* Client customizations for WIP #42701 ends */
    request.setAttribute( "claimDetails", claim );

    String userLanguage = UserManager.getUserLocale();
    if ( userLanguage != null && claim.getSubmitterCommentsLanguageType() != null )
    {
      if ( getSystemVariableService().getPropertyByName( SystemVariableService.MACHINE_LANGUAGE_ALLOW_TRANSLATION ).getBooleanVal() )
      {
        if ( !userLanguage.equals( claim.getSubmitterCommentsLanguageType().getCode() ) )
        {
          request.setAttribute( "allowTranslate", true );
        }
        else
        {
          request.setAttribute( "allowTranslate", false );
        }

      }
      else
      {
        request.setAttribute( "allowTranslate", false );
      }

      request.setAttribute( "translateClientState",
                            "clientState=" + URLEncoder.encode( RequestUtils.getRequiredParamString( request, "clientState" ), "UTF-8" ) + "&cryptoPass="
                                + RequestUtils.getOptionalParamString( request, "cryptoPass" ) );
    }

    ApprovalsRecognitionDetailsForm approvalsRecognitionDetailsForm = (ApprovalsRecognitionDetailsForm)form;
    approvalsRecognitionDetailsForm.load( claim );
    approvalsRecognitionDetailsForm.setScoredByApprover( ScoreBy.lookup( ScoreBy.APPROVER ).equals( claim.getPromotion().getScoreBy() ) );

    return mapping.findForward( ActionConstants.UPDATE_FORWARD );
  }

  /**
   * Prepares anything necessary before displaying the update screen.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  @SuppressWarnings( "unchecked" )
  public ActionForward saveApprovals( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    ActionMessages errors = new ActionMessages();
    ApprovalsRecognitionDetailsForm recognitionApprovalDetailsForm = (ApprovalsRecognitionDetailsForm)form;

    Long claimId = getClaimIdFromClientState( request );
    Long awardAmount = recognitionApprovalDetailsForm.getApprovalsRecognitionForm().getAwardAmount();
    Long awardLevelId = recognitionApprovalDetailsForm.getApprovalsRecognitionForm().getAwardLevelId();

    List serviceErrors = new ArrayList();

    if ( claimId == null )
    {
      errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "claimId and userId as part of clientState" ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_UPDATE );
    }

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.ALL ) );
    RecognitionClaim claim = (RecognitionClaim)getClaimService().getClaimByIdWithAssociations( claimId, associationRequestCollection );

    User approver = getUserService().getUserById( UserManager.getUserId() );
    recognitionApprovalDetailsForm.populateClaimRecipientDomainObjects( claim, approver );

    // set award amount or award level
    ClaimRecipient claimRecipient = claim.getClaimRecipients().iterator().next();
    if ( awardLevelId == null )
    {
      claimRecipient.setAwardQuantity( awardAmount );
      try
      {
        ApprovableItem approvableItem = (ApprovableItem)claim.getApprovableItems().iterator().next();
        if ( approvableItem.getApprovalStatusType().getCode().equals( ApprovalStatusType.APPROVED ) )
        {
          if ( claim.getPromotion().isBudgetUsed() )
          {
            Budget budget = getPromotionService().getAvailableBudget( claim.getPromotion().getId(), claim.getSubmitter().getId(), claim.getNode().getId() );
            BigDecimal claimAward = BigDecimal.valueOf( awardAmount );
            BigDecimal totalApprovedAwardAmount = new BigDecimal( 0 );

            totalApprovedAwardAmount = totalApprovedAwardAmount.add( claimAward );
            if ( budget == null )
            {
              if ( serviceErrors.isEmpty() )
              {
                String budgetMsg = " " + claim.getPromotion().getName();
                serviceErrors.add( new ServiceError( ServiceErrorMessageKeys.HARD_CAP_BUDGET_EXCEEDED_RECOGNITION, budgetMsg ) );
                throw new BeaconRuntimeException( new BudgetUsageOverAllocallatedException( serviceErrors ) );
              }
            }
            else
            {
              BudgetMaster budgetMaster = claim.getPromotion().getBudgetMaster();
              BigDecimal calculatedBudgetValue = budgetMaster.isCentralBudget()
                  ? totalApprovedAwardAmount
                  : calculateBudgetEquivalence( BigDecimal.valueOf( totalApprovedAwardAmount.intValue() ), claimRecipient.getRecipient() );
              log.error( "claimId=" + claim.getId() + " calculatedBudgetValue=" + calculatedBudgetValue );
              if ( ( budget.getCurrentValue().subtract( calculatedBudgetValue ) ).compareTo( BigDecimal.ZERO ) < 0 )

              {
                if ( serviceErrors.isEmpty() )
                {
                  String budgetMsg = " " + claim.getPromotion().getName();
                  serviceErrors.add( new ServiceError( ServiceErrorMessageKeys.HARD_CAP_BUDGET_EXCEEDED_RECOGNITION, budgetMsg ) );
                  throw new BeaconRuntimeException( new BudgetUsageOverAllocallatedException( serviceErrors ) );
                }
              }
            }
          }
        } // end of approved
      }
      catch( BeaconRuntimeException beaconException )
      {
        if ( beaconException.indexOfThrowable( ServiceErrorException.class ) != -1 )
        {
          ServiceErrorException serviceErrorException = (ServiceErrorException)beaconException.getThrowable( beaconException.indexOfThrowable( ServiceErrorException.class ) );
          handleServiceErrorException( request, serviceErrorException );
          return mapping.findForward( ActionConstants.FAIL_UPDATE );
        }
      }
    }
    else
    {
      PromoMerchProgramLevel awardLevel = getMerchLevelService().getPromoMerchProgramLevelById( awardLevelId );
      claimRecipient.setPromoMerchProgramLevel( awardLevel );
    }

    // update calculator responses
    Calculator calculator = claim.getPromotion().getCalculator();
    if ( calculator != null && ScoreBy.lookup( ScoreBy.APPROVER ).equals( claim.getPromotion().getScoreBy() ) )
    {
      claimRecipient.setCalculatorScore( recognitionApprovalDetailsForm.getCalculatorScore() );

      AssociationRequestCollection arc = new AssociationRequestCollection();
      arc.add( new CalculatorAssociationRequest( CalculatorAssociationRequest.CALCULATOR_CRITERION ) );
      arc.add( new CalculatorAssociationRequest( CalculatorAssociationRequest.CALCULATOR_CRITERION_RATING ) );
      calculator = getCalculatorService().getCalculatorByIdWithAssociations( calculator.getId(), arc );

      claim.getCalculatorResponses().clear();
      for ( int i = 0; i < recognitionApprovalDetailsForm.getCalculatorResults().size(); i++ )
      {
        CalculatorResult calculatorResult = recognitionApprovalDetailsForm.getCalculatorResults().get( i );
        CalculatorCriterion criterion = calculator.getCriterion( calculatorResult.getCriteriaId() );

        CalculatorResponse calculatorResponse = new CalculatorResponse();
        calculatorResponse.setClaim( claim );
        calculatorResponse.setCriterion( criterion );
        calculatorResponse.setSelectedRating( criterion.getRating( calculatorResult.getRatingId() ) );
        calculatorResponse.setRatingValue( calculatorResult.getCriteriaRating().intValue() );
        calculatorResponse.setCriterionWeight( criterion.getWeightValue() );
        calculatorResponse.setSequenceNumber( i );
        claim.addCalculatorResponse( calculatorResponse );
      }
    }

    // save claim
    try
    {
      getClaimService().saveApprovable( claim, null, approver, false );

      List<AbstractRecognitionClaim> claims = new ArrayList<AbstractRecognitionClaim>();
      claims.add( claim );
      List<ApprovalsNoEmailBean> noEmailBeans = getApprovedFormattedValueBeansForRecipientsWithNoEmail( claims );
      if ( !noEmailBeans.isEmpty() )
      {
        Map<String, Object> clientStateParameterMap = new HashMap<String, Object>();
        clientStateParameterMap.put( "noEmailList", noEmailBeans );
        return ActionUtils.forwardWithParameters( mapping, "no_emails", new String[] { ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap ) } );
      }
    }
    catch( ServiceErrorException e )
    {
      handleServiceErrorException( request, e );
      return mapping.findForward( ActionConstants.FAIL_UPDATE );
    }
    catch( BeaconRuntimeException beaconException )
    {
      if ( ExceptionUtils.indexOfThrowable( beaconException, ServiceErrorException.class ) != -1 )
      {
        ServiceErrorException serviceErrorException = (ServiceErrorException)ExceptionUtils.getThrowables( beaconException )[ExceptionUtils.indexOfThrowable( beaconException,
                                                                                                                                                              ServiceErrorException.class )];
        handleServiceErrorException( request, serviceErrorException );
        return mapping.findForward( ActionConstants.FAIL_UPDATE );
      }
    }

    Map<String, Object> clientStateParameterMap = new HashMap<String, Object>();
    clientStateParameterMap.put( "saveOccurred", Boolean.TRUE );
    return ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_FORWARD, new String[] { ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap ) } );
  }

  @SuppressWarnings( "unchecked" )
  private static Long getClaimIdFromClientState( HttpServletRequest request )
  {
    Long claimId = null;
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

      // Get the claim.
      Object claimIdParam = clientStateMap.get( "claimId" );
      if ( claimIdParam != null )
      {
        if ( claimIdParam instanceof String )
        {
          claimId = Long.valueOf( (String)claimIdParam );
        }
        else
        {
          claimId = (Long)claimIdParam;
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    return claimId;
  }

  protected static CalculatorService getCalculatorService()
  {
    return (CalculatorService)getService( CalculatorService.BEAN_NAME );
  }

  protected static MerchLevelService getMerchLevelService()
  {
    return (MerchLevelService)getService( MerchLevelService.BEAN_NAME );
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

}
