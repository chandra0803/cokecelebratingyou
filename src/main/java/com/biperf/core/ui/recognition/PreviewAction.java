
package com.biperf.core.ui.recognition;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.PromoNominationBehaviorType;
import com.biperf.core.domain.enums.PromoRecognitionBehaviorType;
import com.biperf.core.domain.enums.PromotionBehaviorType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.multimedia.ECard;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.ProjectionAttribute;
import com.biperf.core.service.ProjectionCollection;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.claim.ClaimElementForm;
import com.biperf.core.ui.recognition.state.RecipientBean;
import com.biperf.core.ui.recognition.state.RecognitionStateManager;
import com.biperf.core.utils.BudgetUtils;
import com.biperf.core.utils.CertUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;

public class PreviewAction extends BaseRecognitionAction
{
  @SuppressWarnings( "unchecked" )
  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PreviewForm previewForm = (PreviewForm)actionForm;
    SendRecognitionForm sendRecognitionForm = RecognitionStateManager.get( request );

    sendRecognitionForm = getSendRecognitionForm( sendRecognitionForm );

    AssociationRequestCollection arc = new AssociationRequestCollection();
    arc.add( new PromotionAssociationRequest( PromotionAssociationRequest.ECARDS ) );
    arc.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_MERCHANDISE_COUNTRIES ) );
    arc.add( new PromotionAssociationRequest( PromotionAssociationRequest.CLAIM_FORM_DEFINITION ) );
    AbstractRecognitionPromotion promotion = (AbstractRecognitionPromotion)getPromotionService().getPromotionByIdWithAssociations( sendRecognitionForm.getPromotionId(), arc );

    previewForm.setPromotionName( promotion.getName() );

    if ( getSystemVariableService().getPropertyByName( SystemVariableService.RA_ENABLE ).getBooleanVal() && !Objects.isNull( request.getParameter( "isRARecognitionFlow" ) )
        && request.getParameter( "isRARecognitionFlow" ).toString().equals( "yes" ) )
    {
      String reporteeId = request.getParameter( "reporteeId" );
      request.setAttribute( "isRARecognitionFlow", "yes" );
      request.setAttribute( "reporteeId", reporteeId );
    }

    String submitPromotionPath = (String)request.getSession().getAttribute( "submitPromotionPath" );

    if ( submitPromotionPath.equalsIgnoreCase( "/startRecognition" ) && promotion.isRecognitionPromotion() )
    {
      previewForm.setPromotionType( "Recognition" );
    }
    else if ( submitPromotionPath.equalsIgnoreCase( "/sendRecognitionDisplay" ) && promotion.isRecognitionPromotion() )
    {
      previewForm.setPromotionType( "Recognition" );
    }
    else
    {
      previewForm.setPromotionType( "Nomination" );
    }

    resolveBehavior( promotion, sendRecognitionForm, previewForm );

    resolveCardOrCertificateUrl( promotion, previewForm, sendRecognitionForm, request.getContextPath() );

    previewForm.setAwardType( promotion.getAwardType() );

    if ( promotion.isAwardActive() )
    {
      previewForm.setAwardActive( true );

      // only show the "award" column if it's a recognition promotion with an active award,
      // OR a nomination promotion in which the nominator specifies the award.
      if ( promotion.isRecognitionPromotion() && ( promotion.getScoreBy() == null || promotion.getScoreBy().isScoreByGiver() ) || promotion.isNominationPromotion() )
      {
        previewForm.setShowAwardColumn( true );

        // calculate the budget stuff
        calculateBudgetInformation( promotion, sendRecognitionForm, previewForm );
      }
    }

    if ( promotion.getClaimForm() != null )
    {
      previewForm.getClaimElements().addAll( sendRecognitionForm.getNewClaimElementsList() );
      getClaimElementDomainObjects( sendRecognitionForm, previewForm );
    }

    if ( promotion.isRecognitionPromotion() )
    {
      if ( ( (RecognitionPromotion)promotion ).isIncludePurl() )
      {
        previewForm.setPurlEnabled( true );
      }
      else
      {
        previewForm.setPurlEnabled( false );
      }
      // Client customizations for WIP #42701 starts
      if ( promotion.getAdihCashOption() != null && promotion.getAdihCashOption() )
      {
        previewForm.setShowCurrencyColumn( true );
      }
      // Client customizations for WIP #42701 ends
    }
    if ( promotion.isRecognitionPromotion() || promotion.isNominationPromotion() )
    {
      if ( ( (AbstractRecognitionPromotion)promotion ).isAllowPublicRecognition() )
      {
        previewForm.setPublicRecognitionEnabled( true );
        if ( ( (AbstractRecognitionPromotion)promotion ).isAllowPromotionPrivate() )
        {
          previewForm.setPrivateRecognitionEnabled( true );
        }
        else
        {
          previewForm.setPrivateRecognitionEnabled( false );
        }
      }
      else
      {
        previewForm.setPublicRecognitionEnabled( false );
        previewForm.setPrivateRecognitionEnabled( false );
      }
    }

    // put the sendRecognitionForm on the request object for use by JSPs
    RecognitionStateManager.addToRequest( sendRecognitionForm, request );

    return mapping.findForward( "success" );
  }

  private void resolveBehavior( Promotion promotion, SendRecognitionForm sendRecognitionForm, PreviewForm previewForm )
  {
    if ( StringUtils.isNotEmpty( sendRecognitionForm.getSelectedBehavior() ) )
    {
      PromotionBehaviorType promotionBehaviorType = null;
      if ( promotion.isNominationPromotion() )
      {
        promotionBehaviorType = PromoNominationBehaviorType.lookup( sendRecognitionForm.getSelectedBehavior() );
      }
      else
      {
        promotionBehaviorType = PromoRecognitionBehaviorType.lookup( sendRecognitionForm.getSelectedBehavior() );
      }

      previewForm.setSelectedBehavior( promotionBehaviorType.getName() );
    }
  }

  private void calculateBudgetInformation( AbstractRecognitionPromotion promotion, SendRecognitionForm sendRecognitionForm, PreviewForm previewForm )
  {
    long availableBudget = 0;
    BigDecimal issuance = BigDecimal.ZERO;
    long remainingBudget = 0;
    long sumRoundedIndividualBudgets = 0;

    ProjectionCollection participantProjection = new ProjectionCollection();
    participantProjection.add( new ProjectionAttribute( "optOutAwards" ) );

    for ( RecipientBean bean : sendRecognitionForm.getRecipients() )
    {
      Participant pax = getParticipantService().getParticipantByIdWithProjections( bean.getUserId(), participantProjection );

      bean.setOptOutAwards( pax.getOptOutAwards() );
      // Client customization for WIP 58122 starts
  	NominationPromotion np=null;
  	if(promotion!=null && promotion.isNominationPromotion())
  	{
  		np=(NominationPromotion)promotion;
  	}
  	if(np!=null && np.isLevelPayoutByApproverAvailable())
  	{
  		bean.setAwardQuantity( new Long(0) );
  	}
      // Client customization for WIP 58122 ends
  	else if ( promotion.isAwardAmountTypeFixed() )
      {
        if ( bean.isOptOutAwards() )
        {
          bean.setAwardQuantity( 0L );
        }
        else
        {
          bean.setAwardQuantity( promotion.getAwardAmountFixed() );
        }
      }

      if ( bean.getAwardLevel() != null && bean.getAwardLevel().longValue() != 0 )
      {
        for ( PromoMerchCountry promoMerchCountry : promotion.getPromoMerchCountries() )
        {
          if ( promoMerchCountry.getCountry().getCountryCode().equals( bean.getCountryCode() ) )
          {
            for ( PromoMerchProgramLevel currentProgramLevel : promoMerchCountry.getLevels() )
            {
              if ( currentProgramLevel.getId().equals( bean.getAwardLevel() ) )
              {
                bean.setAwardLevelName( currentProgramLevel.getDisplayLevelName() );
                if ( !pax.getOptOutAwards() )
                {
                  bean.setAwardQuantity( new Long( currentProgramLevel.getMaxValue() ) );
                }
                else
                {
                  bean.setAwardQuantity( 0L );
                }
                break;
              }
            }
          }
        }
      } // level information

      issuance = issuance.add( bean.getCalculatedBudgetDeductionNotRounded() );
      sumRoundedIndividualBudgets = sumRoundedIndividualBudgets + bean.getCalculatedBudgetDeduction();

      if ( bean.getAwardLevel() != null && bean.getAwardLevel().longValue() != 0 )
      {
        bean.setAwardQuantity( null );
      }
    }

    if ( promotion.getBudgetMaster() != null )
    {
      previewForm.setBudgetActive( true );
      BigDecimal totalUnapprovedAwardQty = new BigDecimal( 0 );
      Budget budget = getPromotionService().getAvailableBudget( promotion.getId(), UserManager.getUserId(), sendRecognitionForm.getNodeId() );
      Long budgetMasterId = null;
      if ( budget.getBudgetSegment().getBudgetMaster().isMultiPromotion() )
      {
        budgetMasterId = budget.getBudgetSegment().getBudgetMaster().getId();
      }

      if ( promotion.getBudgetMaster().isCentralBudget() )
      {
        if ( ( (RecognitionPromotion)promotion ).isIncludePurl() )
        {
          totalUnapprovedAwardQty = getPromotionService().getTotalUnapprovedAwardQuantityPurl( promotion.getId(), null, null, budgetMasterId );
        }
        else
        {
          totalUnapprovedAwardQty = getPromotionService().getTotalUnapprovedAwardQuantity( promotion.getId(), null, null, budgetMasterId );
        }
      }
      else if ( promotion.getBudgetMaster().isParticipantBudget() )
      {
        if ( ( (RecognitionPromotion)promotion ).isIncludePurl() )
        {
          totalUnapprovedAwardQty = getPromotionService().getTotalUnapprovedAwardQuantityPurl( promotion.getId(), UserManager.getUserId(), null, budgetMasterId );
        }
        else
        {
          totalUnapprovedAwardQty = getPromotionService().getTotalUnapprovedAwardQuantity( promotion.getId(), UserManager.getUserId(), null, budgetMasterId );
        }
      }
      else if ( promotion.getBudgetMaster().isNodeBudget() )
      {
        if ( ( (RecognitionPromotion)promotion ).isIncludePurl() )
        {
          totalUnapprovedAwardQty = getPromotionService().getTotalUnapprovedAwardQuantityPurl( promotion.getId(), null, sendRecognitionForm.getNodeId(), budgetMasterId );
        }
        else
        {
          totalUnapprovedAwardQty = getPromotionService().getTotalUnapprovedAwardQuantity( promotion.getId(), null, sendRecognitionForm.getNodeId(), budgetMasterId );
        }
      }
      if ( budget.getCurrentValue().subtract( totalUnapprovedAwardQty ).compareTo( BigDecimal.ZERO ) > 0 )
      {
        budget.setCurrentValue( budget.getCurrentValue().subtract( totalUnapprovedAwardQty ) );
      }
      else
      {
        budget.setCurrentValue( new BigDecimal( 0 ) );
      }
      if ( budget != null )
      {
        BigDecimal convertedCurrentValue = budget.getCurrentValue();
        BudgetMaster budgetMaster = budget.getBudgetSegment().getBudgetMaster();
        if ( !budgetMaster.isCentralBudget() )
        {
          final BigDecimal US_MEDIA_VALUE = getCountryService().getBudgetMediaValueByCountryCode( Country.UNITED_STATES );
          final BigDecimal USER_MEDIA_VALUE = getUserService().getBudgetMediaValueForUser( UserManager.getUserId() );
          convertedCurrentValue = BudgetUtils.applyMediaConversion( budget.getCurrentValue(), US_MEDIA_VALUE, USER_MEDIA_VALUE );
        }
        availableBudget = BudgetUtils.getBudgetDisplayValue( convertedCurrentValue );
      }

      long roundedIssuance = BudgetUtils.getBudgetDisplayValue( issuance );
      remainingBudget = availableBudget - roundedIssuance;
      BigDecimal exactRemainingBudget = BigDecimal.valueOf( availableBudget ).subtract( issuance );

      previewForm.setAvailableBudget( availableBudget );
      previewForm.setThisIssuance( roundedIssuance );
      previewForm.setRemainingBudget( remainingBudget );

      if ( sumRoundedIndividualBudgets != roundedIssuance || BudgetUtils.getBudgetDisplayValue( exactRemainingBudget ) != remainingBudget )
      {
        previewForm.setDisplayRoundingDisclaimer( true );
      }
      else
      {
        previewForm.setDisplayRoundingDisclaimer( false );
      }
    }
  }

  private void resolveCardOrCertificateUrl( AbstractRecognitionPromotion promotion, PreviewForm previewForm, SendRecognitionForm sendRecognitionForm, String contextPath )
  {
    // set to "drawing" in ValidateAction#storeUserEditedCard...
    if ( SendRecognitionForm.CARD_TYPE_DRAWING.equals( sendRecognitionForm.getCardType() ) )
    {
      // cardType: drawing
      previewForm.setCardUrl( sendRecognitionForm.getCardUrl() );
    }
    else if ( SendRecognitionForm.CARD_TYPE_UPLOAD.equals( sendRecognitionForm.getCardType() ) )
    {
      // cardType: upload
      // Image or video?
      boolean isImageUpload = StringUtil.isEmpty( sendRecognitionForm.getVideoUrl() );
      if ( isImageUpload )
      {
        previewForm.setCardUrl( sendRecognitionForm.getCardUrl() );
      }
      else
      {
        // The preview form doesn't care whether it is video or image. It just wants a preview
        // image.
        previewForm.setCardUrl( sendRecognitionForm.getVideoImageUrl() );
      }
    }
    else if ( sendRecognitionForm.getCardId() != null && sendRecognitionForm.getCardId() > 0 )
    {
      // cardType: card
      if ( SendRecognitionForm.CARD_TYPE_CARD.equals( sendRecognitionForm.getCardType() ) )
      {
        ECard ecard = promotion.getPromotionECardBy( sendRecognitionForm.getCardId() );
        if ( ecard != null )
        {
          previewForm.setCardUrl( ecard.getLargeImageNameLocale() );
        }
      }
      else if ( SendRecognitionForm.CARD_TYPE_CERTIFICATE.equals( sendRecognitionForm.getCardType() ) )
      {
        // cardType: cert
        if ( promotion instanceof RecognitionPromotion )
        {
          previewForm.setCardUrl( EcardUtil.getRecognitionCertificateUrl( sendRecognitionForm.getCardId(), contextPath ) );
        }
        else if ( promotion instanceof NominationPromotion )
        {
          previewForm.setCardUrl( CertUtils.getFullCertificateUrl( sendRecognitionForm.getCardId(), contextPath, PromotionType.lookup( PromotionType.RECOGNITION ) ) );
        }
      }
    }
  }

  private void getClaimElementDomainObjects( SendRecognitionForm sendRecognitionForm, PreviewForm previewForm )
  {
    String claimFormAsset = null;
    Long claimFormId = null;
    if ( sendRecognitionForm.getClaimElementsList() != null )
    {
      List<ClaimElementForm> sortedList = sendRecognitionForm.getSortedClaimElementsList( sendRecognitionForm.getClaimElementsList() );
      for ( Iterator<ClaimElementForm> iter = sortedList.iterator(); iter.hasNext(); )
      {
        ClaimElementForm claimElementForm = iter.next();
        claimFormAsset = claimElementForm.getClaimFormAssetCode();
        claimFormId = claimElementForm.getClaimFormId();
      }
    }

    // All ClaimElementForms should belong to the same form (same asset and id)
    sendRecognitionForm.setClaimFormAsset( claimFormAsset );
    sendRecognitionForm.setClaimFormId( claimFormId );
  }

  // all the rich text areas in G5 are appending html tags
  private SendRecognitionForm getSendRecognitionForm( SendRecognitionForm sendRecognitionForm )
  {
    if ( sendRecognitionForm != null && sendRecognitionForm.getComments() != null )
    {
      String comments = sendRecognitionForm.getComments();
      String formattedComments = comments.replaceFirst( "&nbsp;", "" );
      sendRecognitionForm.setComments( formattedComments );
    }
    if ( sendRecognitionForm != null && sendRecognitionForm.getSendCopyToOthers() != null )
    {
      String sendCopyToOthers = sendRecognitionForm.getSendCopyToOthers();
      String formattedEmailIds = sendCopyToOthers.replaceAll( ",", ", " );
      sendRecognitionForm.setSendCopyToOthers( formattedEmailIds );
    }
    return sendRecognitionForm;
  }

  private PromotionService getPromoService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

}
