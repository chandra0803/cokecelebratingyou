/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/claim/ClaimAssociationRequest.java,v $
 */

package com.biperf.core.service.claim;

import java.util.Iterator;
import java.util.Set;

import com.biperf.core.domain.calculator.CalculatorCriterion;
import com.biperf.core.domain.calculator.CalculatorCriterionRating;
import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.ApprovableItem;
import com.biperf.core.domain.claim.ApprovableItemApprover;
import com.biperf.core.domain.claim.CalculatorResponse;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimItem;
import com.biperf.core.domain.claim.ClaimProduct;
import com.biperf.core.domain.claim.ClaimProductCharacteristic;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.claim.QuizClaim;
import com.biperf.core.domain.claim.QuizResponse;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.quiz.QuizQuestion;
import com.biperf.core.domain.quiz.QuizQuestionAnswer;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.BaseAssociationRequest;
import com.biperf.core.service.promotion.PromotionAssociationRequest;

/**
 * ClaimAssociationRequest.
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
 * <td>robinsra</td>
 * <td>Jul 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimAssociationRequest extends BaseAssociationRequest
{

  private int hydrateLevel = 0;

  public static final int ALL = 1;
  public static final int CLAIM_PRODUCTS = 2;
  public static final int PROMOTION = 3;
  public static final int ELEMENTS = 4;
  public static final int CLAIM_RECIPIENTS = 5;
  public static final int CARD = 6;
  public static final int QUIZ_RESPONSES = 7;
  public static final int CURRENT_QUIZ_QUESTION = 8;
  public final static int TEAM_MEMBERS = 9;
  public static final int CALCULATOR_RESPONSES = 12;
  public static final int CLAIM_RECIPIENTS_WITH_EMAIL = 13;
  public static final int CLAIM_SUBMITTER_WITH_EMAIL = 14;
  public static final int CLAIM_RECIPIENT_ADDRESS = 15;
  public static final int CLAIM_SUBMITTER_ADDRESS = 16;
  public static final int CLAIM_RECIPIENT_EMPLOYERS = 17;
  public static final int CLAIM_RECIPIENT_USER_NODES = 18;
  public static final int USER_NODES = 19;
  public static final int CLAIM_SUBMITTER_EMPLOYER = 20;
  public static final int CLAIM_PARTICIPANTS = 21;
  public static final int CLAIM_CELEBRATION_MGR_MESSAGE = 22;
  public static final int CLAIM_BEHAVIORS = 23;
  public final static int CLAIM_DOCUMENTS = 24;

  public ClaimAssociationRequest( int hydrateLevel )
  {
    this.hydrateLevel = hydrateLevel;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.AssociationRequest#execute(Object)
   * @param domainObject
   */
  public void execute( Object domainObject )
  {
    Claim claim = (Claim)domainObject;
    claim = initializeAndUnproxy( claim );

    switch ( hydrateLevel )
    {
      case ALL:
      {
        hydrateAll( claim );
        break;
      }
      case CLAIM_PRODUCTS:
      {
        hydrateClaimProducts( (ProductClaim)claim );
        break;
      }
      case CLAIM_PARTICIPANTS:
      {
        if ( claim.isProductClaim() )
        {
          hydrateClaimParticipants( (ProductClaim)claim );
        }
        break;
      }
      case CLAIM_RECIPIENTS:
      {
        if ( claim.isAbstractRecognitionClaim() )
        {
          hydrateClaimRecipients( (AbstractRecognitionClaim)claim, false );
        }
        break;
      }
      case PROMOTION:
      {
        hydratePromotion( claim );
        break;
      }
      case ELEMENTS:
      {
        hydrateElements( claim );
        break;
      }
      case CARD:
      {
        hydrateCard( (AbstractRecognitionClaim)claim );
        break;
      }
      case QUIZ_RESPONSES:
      {
        hydrateQuizResponses( (QuizClaim)claim );
        break;
      }
      case CURRENT_QUIZ_QUESTION:
      {
        hydrateCurrentQuizQuestion( (QuizClaim)claim );
        break;
      }
      case CALCULATOR_RESPONSES:
      {
        hydrateCalculatorResponses( (AbstractRecognitionClaim)claim );
        break;
      }
      case CLAIM_RECIPIENTS_WITH_EMAIL:
      {
        hydrateClaimRecipients( (AbstractRecognitionClaim)claim, true );
        break;
      }
      case CLAIM_SUBMITTER_WITH_EMAIL:
      {
        hydrateClaimSubmitter( claim, true );
        break;
      }
      case CLAIM_RECIPIENT_ADDRESS:
      {
        hydrateClaimRecipientAddress( (AbstractRecognitionClaim)claim );
        break;
      }
      case CLAIM_RECIPIENT_EMPLOYERS:
      {
        hydrateClaimRecipientEmployers( (AbstractRecognitionClaim)claim );
        break;
      }
      case CLAIM_SUBMITTER_ADDRESS:
      {
        hydrateClaimSubmitterAddress( claim );
        break;
      }
      case CLAIM_RECIPIENT_USER_NODES:
      {
        hydrateClaimRecipientUserNodes( (AbstractRecognitionClaim)claim );
        break;
      }
      case USER_NODES:
      {
        hydrateUserNodes( claim );
        break;
      }
      case CLAIM_SUBMITTER_EMPLOYER:
      {
        hydrateClaimSubmitterEmployer( claim );
        break;
      }
      case CLAIM_CELEBRATION_MGR_MESSAGE:
      {
        if ( claim.isRecognitionClaim() )
        {
          hydrateCelebrationManagerMessage( (RecognitionClaim)claim );
        }
        break;
      }
      case CLAIM_BEHAVIORS:
      {
        if ( claim.isNominationClaim() )
        {
          hydrateClaimBehaviors( (NominationClaim)claim );
        }
        break;
      }
      case CLAIM_DOCUMENTS:
      {
        if ( claim.isNominationClaim() )
        {
          hydrateClaimDocuments( (NominationClaim)claim );
        }
        break;
      }
      default:
      {
        break;
      }
    } // switch
  }

  /**
   * hydrate the quiz responses, including each quiz response's questions and answers
   * 
   * @param claim
   */
  private void hydrateQuizResponses( QuizClaim claim )
  {
    initialize( claim.getQuizResponses() );
    Iterator iter = claim.getQuizResponses().iterator();
    while ( iter.hasNext() )
    {
      QuizResponse quizResponse = (QuizResponse)iter.next();
      if ( quizResponse == null )
      {
        continue;
      }

      initialize( quizResponse.getSelectedQuizQuestionAnswer() );
      QuizQuestion quizQuestion = quizResponse.getQuizQuestion();
      initialize( quizQuestion );
      for ( Iterator iterator = quizQuestion.getQuizQuestionAnswers().iterator(); iterator.hasNext(); )
      {
        QuizQuestionAnswer quizQuestionAnswer = (QuizQuestionAnswer)iterator.next();
        initialize( quizQuestionAnswer );
      }
    }
  }

  private void hydrateCalculatorResponses( AbstractRecognitionClaim claim )
  {
    initialize( claim.getCalculatorResponses() );
    Iterator iter = claim.getCalculatorResponses().iterator();
    while ( iter.hasNext() )
    {
      CalculatorResponse calculatorResponse = (CalculatorResponse)iter.next();
      if ( calculatorResponse == null )
      {
        continue;
      }

      initialize( calculatorResponse.getSelectedRating() );
      CalculatorCriterion calculatorCriterion = calculatorResponse.getCriterion();
      initialize( calculatorCriterion );
      for ( Iterator iterator = calculatorCriterion.getCriterionRatings().iterator(); iterator.hasNext(); )
      {
        CalculatorCriterionRating criterionRating = (CalculatorCriterionRating)iterator.next();
        initialize( criterionRating );
      }
    }
  }

  /**
   * @param claim
   */
  private void hydrateCurrentQuizQuestion( QuizClaim claim )
  {
    QuizQuestion currentQuizQuestion = claim.getCurrentQuizQuestion();
    initialize( currentQuizQuestion.getQuizQuestionAnswers() );

  }

  /**
   * hydrates the claim products and the claim product characteristics
   * 
   * @param claim
   */
  private void hydrateClaimProducts( ProductClaim claim )
  {
    initialize( claim.getClaimProducts() );
    Iterator iter = claim.getClaimProducts().iterator();
    while ( iter.hasNext() )
    {
      ClaimProduct claimProduct = (ClaimProduct)iter.next();
      if ( claimProduct == null )
      {
        continue;
      }

      hydrateClaimProductCharacteristics( claimProduct );
      hydrateProductsAndCategories( claimProduct );
      hydrateApprover( claimProduct );
    }
  }

  /**
   * hydrates the claim recipients
   * 
   * @param claim
   */
  private void hydrateClaimRecipients( AbstractRecognitionClaim claim, boolean hydrateEmail )
  {
    Set claimRecipients = claim.getClaimRecipients();
    initialize( claimRecipients );
    for ( Iterator iter = claimRecipients.iterator(); iter.hasNext(); )
    {
      ClaimRecipient claimRecipient = (ClaimRecipient)iter.next();
      initialize( claimRecipient );
      initialize( claimRecipient.getRecipient() );
      initialize( claimRecipient.getNode() );
      if ( claimRecipient.getPromoMerchCountry() != null )
      {
        initialize( claimRecipient.getPromoMerchCountry() );
        initialize( claimRecipient.getPromoMerchCountry().getLevels() );
      }
      hydrateApprover( claimRecipient );
      if ( hydrateEmail && claimRecipient.getRecipient() != null )
      {
        initialize( claimRecipient.getRecipient().getUserEmailAddresses() );
      }
    }
  }

  private void hydrateProductsAndCategories( ClaimProduct claimProduct )
  {
    initialize( claimProduct.getProduct() );

    ProductCategory productCategory = claimProduct.getProduct().getProductCategory();

    initialize( productCategory );
    if ( productCategory.getParentProductCategory() != null )
    {
      initialize( productCategory.getParentProductCategory() );
    }
  }

  private void hydrateApprover( ClaimItem claimItem )
  {
    for ( Iterator iter = claimItem.getApprovableItemApprovers().iterator(); iter.hasNext(); )
    {
      ApprovableItemApprover claimItemApprover = (ApprovableItemApprover)iter.next();
      initialize( claimItemApprover.getApproverUser() );
    }
  }

  /**
   * hydrates the claim recipients
   * 
   * @param claim
   */
  private void hydrateClaimSubmitter( Claim claim, boolean hydrateEmail )
  {
    if ( null != claim.getSubmitter() )
    {
      initialize( claim.getSubmitter().getUserEmailAddresses() );
    }
  }

  private void hydrateClaimRecipientAddress( AbstractRecognitionClaim claim )
  {
    for ( Iterator iter = claim.getClaimRecipients().iterator(); iter.hasNext(); )
    {
      ClaimRecipient claimRecipient = (ClaimRecipient)iter.next();
      if ( claimRecipient.getRecipient() != null )
      {
        initialize( claimRecipient.getRecipient().getUserAddresses() );
      }
    }
  }

  private void hydrateClaimRecipientEmployers( AbstractRecognitionClaim claim )
  {
    for ( Iterator iter = claim.getClaimRecipients().iterator(); iter.hasNext(); )
    {
      ClaimRecipient claimRecipient = (ClaimRecipient)iter.next();
      if ( claimRecipient.getRecipient() != null )
      {
        initialize( claimRecipient.getRecipient().getParticipantEmployers() );
      }
    }
  }

  private void hydrateClaimRecipientUserNodes( AbstractRecognitionClaim claim )
  {
    for ( Iterator iter = claim.getClaimRecipients().iterator(); iter.hasNext(); )
    {
      ClaimRecipient claimRecipient = (ClaimRecipient)iter.next();
      if ( claimRecipient.getNode() != null )
      {
        initialize( claimRecipient.getNode().getUserNodes() );
      }
    }
  }

  private void hydrateClaimSubmitterAddress( Claim claim )
  {
    if ( null != claim.getSubmitter() )
    {
      initialize( claim.getSubmitter().getUserAddresses() );
    }
  }

  private void hydrateClaimSubmitterEmployer( Claim claim )
  {
    if ( null != claim.getSubmitter() )
    {
      initialize( claim.getSubmitter().getParticipantEmployers() );
    }
  }

  /**
   * hydrates the claim product characteristics
   * 
   * @param claimProduct
   */
  private void hydrateClaimProductCharacteristics( ClaimProduct claimProduct )
  {
    initialize( claimProduct.getClaimProductCharacteristics() );
    Iterator iter = claimProduct.getClaimProductCharacteristics().iterator();
    while ( iter.hasNext() )
    {
      ClaimProductCharacteristic claimProductCharacteristic = (ClaimProductCharacteristic)iter.next();
      if ( claimProductCharacteristic == null )
      {
        continue;
      }
      initialize( claimProductCharacteristic.getProductCharacteristicType() );
      initialize( claimProductCharacteristic.getClaimProduct() );
    }
  }

  /**
   * hydrates the promotion, claimForm,claimFormSteps, and claimFormStepElements
   * 
   * @param claim
   */
  private void hydratePromotion( Claim claim )
  {
    Promotion promotion = BaseAssociationRequest.initializeAndUnproxy( claim.getPromotion() );

    PromotionAssociationRequest promoRequest = new PromotionAssociationRequest( PromotionAssociationRequest.CLAIM_FORM_DEFINITION );

    promoRequest.execute( promotion );

    if ( claim.getPromotion().isRecognitionPromotion() && ( (RecognitionPromotion)promotion ).isAwardActive() && ( (RecognitionPromotion)promotion ).getAwardType() != null
        && ( (RecognitionPromotion)promotion ).getAwardType().isMerchandiseAwardType() )
    {
      PromotionAssociationRequest pcRequest = new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_MERCHANDISE_COUNTRIES );
      pcRequest.execute( promotion );
    }

    if ( promotion.isProductClaimPromotion() )
    {
      PromotionAssociationRequest pcRequest = new PromotionAssociationRequest( PromotionAssociationRequest.TEAM_POSITIONS );
      PromotionAssociationRequest pcRequest2 = new PromotionAssociationRequest( PromotionAssociationRequest.APPROVAL_PARTICIPANTS );

      pcRequest.execute( promotion );
      pcRequest2.execute( promotion );
    }
    if ( promotion.isNominationPromotion() )
    {
      int[] associations = new int[] { PromotionAssociationRequest.CERTIFICATES,
                                       PromotionAssociationRequest.NOMINATION_PROMOTION_LEVEL,
                                       PromotionAssociationRequest.PROMOTION_NOMINATION_TIME_PERIODS };
      for ( int association : associations )
      {
        PromotionAssociationRequest associationRequest = new PromotionAssociationRequest( association );
        associationRequest.execute( promotion );
      }
    }
  }

  private void hydrateUserNodes( Claim claim )
  {
    if ( claim.getNode() != null )
    {
      initialize( claim.getNode().getUserNodes() );
    }
  }

  /**
   * hydrates the claim elements, and the Element within the claimElement
   * 
   * @param claim
   */
  private void hydrateElements( Claim claim )
  {
    initialize( claim.getClaimElements() );

    Iterator iter = claim.getClaimElements().iterator();
    while ( iter.hasNext() )
    {
      ClaimElement claimElement = (ClaimElement)iter.next();
      if ( claimElement == null )
      {
        continue;
      }

      initialize( claimElement.getClaimFormStepElement() );
      // claimElement.getElement().getId();
      initialize( claimElement.getClaimFormStepElement().getClaimFormStep() );
    }
  }

  /**
   * hydrates the recognitionclaim card
   * 
   * @param recognitionClaim
   */
  private void hydrateCard( AbstractRecognitionClaim recognitionClaim )
  {
    initialize( recognitionClaim.getCard() );
  }

  /**
   * hydrates all pieces of the claim
   * 
   * @param claim
   */
  private void hydrateAll( Claim claim )
  {
    if ( claim instanceof ProductClaim )
    {
      hydrateClaimProducts( (ProductClaim)claim );
      hydrateClaimParticipants( (ProductClaim)claim );
    }
    else if ( claim instanceof AbstractRecognitionClaim )
    {
      hydrateClaimRecipients( (AbstractRecognitionClaim)claim, true );
      hydrateCard( (AbstractRecognitionClaim)claim );
      hydrateCalculatorResponses( (AbstractRecognitionClaim)claim );
      hydrateClaimSubmitterAddress( claim );
      hydrateClaimSubmitterEmployer( claim );
      hydrateClaimRecipientAddress( (AbstractRecognitionClaim)claim );
      hydrateClaimRecipientEmployers( (AbstractRecognitionClaim)claim );
      hydrateClaimRecipientUserNodes( (AbstractRecognitionClaim)claim );
      if ( claim instanceof RecognitionClaim )
      {
        hydrateCelebrationManagerMessage( (RecognitionClaim)claim );
      }
      else if ( claim.isNominationClaim() )
      {
        hydrateClaimBehaviors( (NominationClaim)claim );
      }
    }
    else if ( claim instanceof QuizClaim )
    {
      hydrateQuizResponses( (QuizClaim)claim );
      hydrateCurrentQuizQuestion( (QuizClaim)claim );
    }
    else
    {
      throw new BeaconRuntimeException( "Unknown claim subclass type:" + claim.getClass().getName() );
    }
    initialize( claim.getSubmitter() );
    hydratePromotion( claim );
    hydrateElements( claim );
    hydrateUserNodes( claim );

    hydrateApprovableItems( claim );

  }

  private void hydrateApprovableItems( Claim claim )
  {
    for ( Iterator iter = claim.getApprovableItems().iterator(); iter.hasNext(); )
    {
      ApprovableItem approvableItem = (ApprovableItem)iter.next();
      initialize( approvableItem.getApprovableItemApprovers() );
    }
  }

  private void hydrateClaimParticipants( ProductClaim claim )
  {
    initialize( claim.getClaimParticipants() );
  }

  private void hydrateCelebrationManagerMessage( RecognitionClaim claim )
  {
    initialize( claim.getCelebrationManagerMessage() );
  }

  private void hydrateClaimBehaviors( NominationClaim claim )
  {
    initialize( claim.getNominationClaimBehaviors() );
  }
  
//Client customization for WIP #39189 starts
 private void hydrateClaimDocuments( NominationClaim claim )
 {
   initialize( claim.getClaimFiles() );
 }
 // Client customization for WIP #39189 ends

}
