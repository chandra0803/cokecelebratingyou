/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/PromotionBasicsUpdateAssociation.java,v $
 */

package com.biperf.core.service.promotion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.enums.ApprovalType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.promotion.ClaimFormNotificationType;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionNotification;
import com.biperf.core.domain.promotion.PromotionPayoutGroup;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.service.UpdateAssociationRequest;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.ServiceLocator;

/**
 * PromotionBasicsUpdateAssociation is used to update promotions post-save on the Basics page.
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
 * <td>Brian Repko</td>
 * <td>Jan 13, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class PromotionBasicsUpdateAssociation extends UpdateAssociationRequest
{
  public PromotionBasicsUpdateAssociation( Promotion detachedPromotion )
  {
    super( detachedPromotion );
  }

  /**
   * Uses attached and detached versions of a domain object to perform additional validation of the
   * domain object.
   *
   * @param attachedDomain
   */
  public void validate( BaseDomain attachedDomain ) throws ServiceErrorExceptionWithRollback
  {
    List validationErrors = new ArrayList();

    // Currently, only product claim promotions can have child promotions.
    Object detachedDomain = getDetachedDomain();
    if ( attachedDomain instanceof ProductClaimPromotion && detachedDomain instanceof ProductClaimPromotion )
    {
      ProductClaimPromotion attachedPcPromotion = (ProductClaimPromotion)attachedDomain;
      ProductClaimPromotion detachedPcPromotion = (ProductClaimPromotion)detachedDomain;

      if ( attachedPcPromotion.getChildrenCount() > 0 ) // This is a parent promotion with children.
      {
        Date parentSubmissionStartDate = detachedPcPromotion.getSubmissionStartDate();
        Date parentSubmissionEndDate = detachedPcPromotion.getSubmissionEndDate();

        for ( Iterator iter = attachedPcPromotion.getChildPromotions().iterator(); iter.hasNext(); )
        {
          ProductClaimPromotion childPromotion = (ProductClaimPromotion)iter.next();

          Date childSubmissionStartDate = childPromotion.getSubmissionStartDate();
          Date childSubmissionEndDate = childPromotion.getSubmissionEndDate();

          // Constraint: The submission start date of a child promotion must be equal to or later
          // than the submission start date of its parent promotion.
          if ( childSubmissionStartDate.before( parentSubmissionStartDate ) )
          {
            validationErrors.add( new ServiceError( ServiceErrorMessageKeys.PROMOTION_INVALID_SUBMISSION_START_DATE ) );
          }

          if ( parentSubmissionEndDate != null )
          {
            // Constraint: The submission end date of a child promotion must be earlier than or
            // equal
            // to the submission end date of its parent promotion.
            if ( childSubmissionEndDate == null || childSubmissionEndDate.after( parentSubmissionEndDate ) )
            {
              validationErrors.add( new ServiceError( ServiceErrorMessageKeys.PROMOTION_INVALID_SUBMISSION_END_DATE ) );
            }
          }
        }
      }

      if ( !validationErrors.isEmpty() )
      {
        throw new ServiceErrorExceptionWithRollback( validationErrors );
      }
    }
  }

  private void deleteFromCollection( Collection list )
  {
    for ( Iterator i = list.iterator(); i.hasNext(); )
    {
      i.next();
      i.remove();
    }
  }

  /**
   * Currently this does the following:
   * <ol>
   * <li>delete form rules</li>
   * <li>if approval-type is conditional amount based, then delete all approval information</li>
   * <li>delete notifications tied to form</li>
   * <li>if marked as complete, then change to under construction</li>
   * <li>reset status of old claim form (new claim form happens with save of promotion)</li>
   * </ol>
   * <p>
   * Overridden from
   *
   * @see com.biperf.core.service.UpdateAssociationRequest#execute(com.biperf.core.domain.BaseDomain)
   * @param attachedDomain
   */
  public void execute( BaseDomain attachedDomain )
  {
    Promotion detachedPromotion = (Promotion)getDetachedDomain();
    Promotion attachedPromotion = (Promotion)attachedDomain;

    boolean markAsUnderConstruction = false;
    //
    // do what the merge would do
    //
    attachedPromotion.setName( detachedPromotion.getName() );
    attachedPromotion.setAwardType( detachedPromotion.getAwardType() );
    attachedPromotion.setTaxable( detachedPromotion.isTaxable() );
    attachedPromotion.setPromotionType( detachedPromotion.getPromotionType() );
    attachedPromotion.setPromotionStatus( detachedPromotion.getPromotionStatus() );
    attachedPromotion.setSubmissionStartDate( detachedPromotion.getSubmissionStartDate() );
    attachedPromotion.setSubmissionEndDate( detachedPromotion.getSubmissionEndDate() );
    attachedPromotion.setOnlineEntry( detachedPromotion.isOnlineEntry() );
    attachedPromotion.setFileLoadEntry( detachedPromotion.isFileLoadEntry() );
    attachedPromotion.setPurpose( detachedPromotion.getPurpose() );

    if ( attachedPromotion.isQuizPromotion() || attachedPromotion.isDIYQuizPromotion() )
    {
      attachedPromotion.setCertificate( detachedPromotion.getCertificate() );
    }
    // ProductClaimPromotion
    if ( attachedPromotion.isProductClaimPromotion() )
    {
      ProductClaimPromotion pcAttachedPromotion = (ProductClaimPromotion)attachedPromotion;
      ProductClaimPromotion pcDetachedPromotion = (ProductClaimPromotion)detachedPromotion;
      pcAttachedPromotion.setPromotionProcessingMode( pcDetachedPromotion.getPromotionProcessingMode() );
    }
    else if ( attachedPromotion.isRecognitionPromotion() )
    {
      RecognitionPromotion recAttachedPromotion = (RecognitionPromotion)attachedPromotion;
      RecognitionPromotion recDetachedPromotion = (RecognitionPromotion)detachedPromotion;
      // recAttachedPromotion.setIssuanceType( recDetachedPromotion.getIssuanceType() );
      recAttachedPromotion.setIncludeCertificate( recDetachedPromotion.isIncludeCertificate() );
      recAttachedPromotion.setCopyRecipientManager( recDetachedPromotion.isCopyRecipientManager() );
      recAttachedPromotion.setCopyOthers( recDetachedPromotion.isCopyOthers() );
      recAttachedPromotion.setAwardActive( recDetachedPromotion.isAwardActive() );
      recAttachedPromotion.setBehaviorActive( recDetachedPromotion.isBehaviorActive() );
      recAttachedPromotion.setCardActive( recDetachedPromotion.isCardActive() );
      recAttachedPromotion.setFileloadBudgetAmount( recDetachedPromotion.isFileloadBudgetAmount() );
      recAttachedPromotion.setAwardAmountTypeFixed( recDetachedPromotion.isAwardAmountTypeFixed() );
      recAttachedPromotion.setCalculator( recDetachedPromotion.getCalculator() );
      recAttachedPromotion.setScoreBy( recDetachedPromotion.getScoreBy() );
      recAttachedPromotion.setAllowManagerAward( recDetachedPromotion.isAllowManagerAward() );
      recAttachedPromotion.setMgrAwardPromotionId( recDetachedPromotion.getMgrAwardPromotionId() );
      recAttachedPromotion.setAwardStructure( recDetachedPromotion.getAwardStructure() );
      recAttachedPromotion.setApqConversion( recDetachedPromotion.isApqConversion() );
      recAttachedPromotion.setIncludePurl( recDetachedPromotion.isIncludePurl() );
      recAttachedPromotion.setAllowPublicRecognition( recDetachedPromotion.isAllowPublicRecognition() );
      if ( !recDetachedPromotion.isAllowPublicRecognition() )
      {
        recAttachedPromotion.setAllowPublicRecognitionPoints( false );
      }
      else
      {
        recAttachedPromotion.setAllowPromotionPrivate( recDetachedPromotion.isAllowPromotionPrivate() );
      }
      recAttachedPromotion.setAllowRecognitionSendDate( recDetachedPromotion.isAllowRecognitionSendDate() );
      if ( recDetachedPromotion.isAllowRecognitionSendDate() )
      {
        recAttachedPromotion.setMaxDaysDelayed( recDetachedPromotion.getMaxDaysDelayed() );
      }

      if ( recAttachedPromotion.isIncludePurl() )
      {
        recAttachedPromotion.setDisplayPurlInPurlTile( recDetachedPromotion.isDisplayPurlInPurlTile() );
        recAttachedPromotion.setPurlPromotionMediaType( recDetachedPromotion.getPurlPromotionMediaType() );
        recAttachedPromotion.setPurlMediaValue( recDetachedPromotion.getPurlMediaValue() );
      }

      // If AwardType is NOT Points
      if ( !PromotionAwardsType.lookup( PromotionAwardsType.POINTS ).equals( recDetachedPromotion.getAwardType() ) )
      {
        recAttachedPromotion.setOpenEnrollmentEnabled( false );
        recAttachedPromotion.setBudgetSweepEnabled( false );
      }
      recAttachedPromotion.setMobAppEnabled( recDetachedPromotion.isMobAppEnabled() );

      recAttachedPromotion.setIncludeCelebrations( recDetachedPromotion.isIncludeCelebrations() );

      recAttachedPromotion.setPurlStandardMessageEnabled( recDetachedPromotion.isPurlStandardMessageEnabled() );
      if ( recDetachedPromotion.isPurlStandardMessageEnabled() )
      {
        recAttachedPromotion.setPurlStandardMessage( recDetachedPromotion.getPurlStandardMessage() );
        recAttachedPromotion.setDefaultContributorAvatar( recDetachedPromotion.getDefaultContributorAvatar() );
        recAttachedPromotion.setDefaultContributorName( recDetachedPromotion.getDefaultContributorName() );
      }
    }
    else if ( attachedPromotion.isNominationPromotion() )
    {
      NominationPromotion nomAttachedPromotion = (NominationPromotion)attachedPromotion;
      NominationPromotion nomDetachedPromotion = (NominationPromotion)detachedPromotion;
      nomAttachedPromotion.setAwardGroupType( nomDetachedPromotion.getAwardGroupType() );
      nomAttachedPromotion.setEvaluationType( nomDetachedPromotion.getEvaluationType() );
      nomAttachedPromotion.setSelfNomination( nomDetachedPromotion.isSelfNomination() );
      nomAttachedPromotion.setAwardActive( nomDetachedPromotion.isAwardActive() );
      nomAttachedPromotion.setBehaviorActive( nomDetachedPromotion.isBehaviorActive() );
      nomAttachedPromotion.setCardActive( nomDetachedPromotion.isCardActive() );
      nomAttachedPromotion.setFileloadBudgetAmount( nomDetachedPromotion.isFileloadBudgetAmount() );
      nomAttachedPromotion.setAwardAmountTypeFixed( nomDetachedPromotion.isAwardAmountTypeFixed() );
      nomAttachedPromotion.setMaxGroupMembers( nomDetachedPromotion.getMaxGroupMembers() );
      nomAttachedPromotion.setAwardSpecifierType( nomDetachedPromotion.getAwardSpecifierType());
      nomAttachedPromotion.setPublicationDateActive( nomDetachedPromotion.isPublicationDateActive() );
      nomAttachedPromotion.setPublicationDate( nomDetachedPromotion.getPublicationDate() );
      nomAttachedPromotion.setAllowPublicRecognition( nomDetachedPromotion.isAllowPublicRecognition() );
      nomAttachedPromotion.setWhyNomination( nomDetachedPromotion.isWhyNomination() );

      if ( nomAttachedPromotion.isAllowPublicRecognition() )
      {
        nomAttachedPromotion.setAllowPromotionPrivate( nomDetachedPromotion.isAllowPromotionPrivate() );
      }

      // fix of BUG # 12751 -- wipe out approver selection if award group is changed
      if ( nomDetachedPromotion.getApproverType() == null )
      {
        nomAttachedPromotion.setApprovalType( null );
        nomAttachedPromotion.setApprovalConditionalAmount( null );
        nomAttachedPromotion.setApprovalConditionalAmountField( null );
        nomAttachedPromotion.setApprovalConditionalAmountOperator( null );
        nomAttachedPromotion.setApprovalAutoDelayDays( null );
        nomAttachedPromotion.setApprovalConditionalClaimCount( null );
        nomAttachedPromotion.setApprovalEndDate( null );
        nomAttachedPromotion.setApprovalNodeLevels( null );
        nomAttachedPromotion.setApprovalStartDate( null );
        nomAttachedPromotion.getPromotionParticipantApprovers().clear();
        nomAttachedPromotion.setApproverType( null );
        nomAttachedPromotion.setApproverNode( null );
      }
      // END of BUG # 12751

      // Fields that get forced to some value when the approvals are cumulative
      if ( nomDetachedPromotion.isCumulative() )
      {
        // Behavior information gets cleared
        nomAttachedPromotion.setBehaviorActive( false );
        nomAttachedPromotion.getPromotionBehaviors().clear();

        // Certificates must be "One Cert per Promotion"
        nomAttachedPromotion.setOneCertPerPromotion( true );

        // Recommended award must be 'no'
        nomAttachedPromotion.setNominatorRecommendedAward( false );
      }

      // Time period submission limits are disabled when team nomination - make sure existing values
      // are cleared
      if ( nomDetachedPromotion.isTeam() )
      {
        nomAttachedPromotion.getNominationTimePeriods().forEach( ( timePeriod ) ->
        {
          timePeriod.setMaxNominationsAllowed( null );
          timePeriod.setMaxSubmissionAllowed( null );
          timePeriod.setMaxWinsllowed( null );
        } );
      }
      
      // Client customization for WIP #39189 starts
      nomAttachedPromotion.setEnableFileUpload( nomDetachedPromotion.isEnableFileUpload() );
      nomAttachedPromotion.setFileMinNumber( nomDetachedPromotion.getFileMinNumber() );
      nomAttachedPromotion.setFileMaxNumber( nomDetachedPromotion.getFileMaxNumber() );
      // Client customization for WIP #39189 ends

   // Client customization for WIP #59420 starts
      nomAttachedPromotion.setAllowedFileTypes( nomDetachedPromotion.getAllowedFileTypes() );
      // Client customization for WIP #59420 ends
    }
    else if ( attachedPromotion.isSSIPromotion() )
    {
      SSIPromotion ssiAttachedPromotion = (SSIPromotion)attachedPromotion;
      SSIPromotion ssiDetachedPromotion = (SSIPromotion)detachedPromotion;
      ssiAttachedPromotion.setSelectedContests( ssiDetachedPromotion.getSelectedContests() );
      ssiAttachedPromotion.setBillCodesActive( ssiDetachedPromotion.isBillCodesActive() );
      ssiAttachedPromotion.setMaxContestsToDisplay( ssiDetachedPromotion.getMaxContestsToDisplay() );
      ssiAttachedPromotion.setDaysToArchive( ssiDetachedPromotion.getDaysToArchive() );
      ssiAttachedPromotion.setContestGuideUrl( ssiDetachedPromotion.getContestGuideUrl() );
    }
    if ( attachedPromotion.isGoalQuestPromotion() )
    {
      GoalQuestPromotion attachedGoalQuestPromo = (GoalQuestPromotion)attachedPromotion;
      GoalQuestPromotion detachedGoalQuestPromo = (GoalQuestPromotion)detachedPromotion;
      attachedGoalQuestPromo.setGoalCollectionStartDate( detachedGoalQuestPromo.getGoalCollectionStartDate() );
      attachedGoalQuestPromo.setGoalCollectionEndDate( detachedGoalQuestPromo.getGoalCollectionEndDate() );
      attachedGoalQuestPromo.setFinalProcessDate( detachedGoalQuestPromo.getFinalProcessDate() );
      attachedGoalQuestPromo.setProgressLoadType( detachedGoalQuestPromo.getProgressLoadType() );
    }

    // this is to make Audience (team members) and promo pay outs clean up while file load is the
    // method of entry
    if ( attachedPromotion.isProductClaimPromotion() && attachedPromotion.isFileLoadEntry() )
    {
      ProductClaimPromotion pcAttachedPromotion = (ProductClaimPromotion)attachedPromotion;
      pcAttachedPromotion.setSecondaryAudienceType( null );
      pcAttachedPromotion.setTeamUsed( false );
      pcAttachedPromotion.setTeamCollectedAsGroup( false );
      pcAttachedPromotion.setTeamMaxCount( null );
      pcAttachedPromotion.setTeamHasMax( false );

      for ( Iterator i = pcAttachedPromotion.getPromotionPayoutGroups().iterator(); i.hasNext(); )
      {
        PromotionPayoutGroup group = (PromotionPayoutGroup)i.next();
        if ( group != null )
        {
          group.setTeamMemberPayout( new Integer( 0 ) );
        }
      }
    }

    //
    // if claim form has changed then
    //
    ClaimForm detachedClaimForm = detachedPromotion.getClaimForm();
    ClaimForm attachedClaimForm = attachedPromotion.getClaimForm();
    if ( detachedClaimForm != null && attachedClaimForm != null && !detachedClaimForm.equals( attachedClaimForm ) )
    {
      //
      // 1. delete form rules
      //
      attachedPromotion.getPromotionClaimFormStepElementValidations().clear();
      //
      // 2. if approval-type is conditional amount based, then delete all approval information
      //
      ApprovalType approvalType = attachedPromotion.getApprovalType();
      String approvalTypeCode = approvalType != null ? approvalType.getCode() : null;
      if ( approvalTypeCode != null && approvalTypeCode.equals( ApprovalType.CONDITIONAL_AMOUNT_BASED ) )
      {
        // TODO - move this to a promotion method?
        attachedPromotion.setApprovalType( null );
        attachedPromotion.setApprovalConditionalAmount( null );
        attachedPromotion.setApprovalConditionalAmountField( null );
        attachedPromotion.setApprovalConditionalAmountOperator( null );
        attachedPromotion.setApprovalAutoDelayDays( null );
        attachedPromotion.setApprovalConditionalClaimCount( null );
        attachedPromotion.setApprovalEndDate( null );
        attachedPromotion.setApprovalNodeLevels( null );
        attachedPromotion.setApprovalStartDate( null );
        attachedPromotion.getPromotionParticipantApprovers().clear();
        attachedPromotion.setApproverType( null );
        attachedPromotion.setApproverNode( null );
        // don't clear out options
        // attachedPromotion.getPromotionApprovalOptions().clear();
      }
      //
      // 3. delete notifications tied to form
      //
      for ( Iterator i = attachedPromotion.getPromotionNotifications().iterator(); i.hasNext(); )
      {
        PromotionNotification notification = (PromotionNotification)i.next();
        if ( notification instanceof ClaimFormNotificationType )
        {
          i.remove();
        }
      }
      //
      // 4. if marked as complete, then change to under construction
      //
      markAsUnderConstruction = true;
      //
      // 5. reset status of old claim form (new claim form happens with save of promotion)
      //
      ClaimFormDefinitionService cfdService = (ClaimFormDefinitionService)ServiceLocator.getService( ClaimFormDefinitionService.BEAN_NAME );
      cfdService.updateClaimFormStatus( attachedClaimForm.getId() );
      //
      // 6. set the detachedClaimForm on the attachedPromotion
      //
      attachedPromotion.setClaimForm( detachedClaimForm );
    }

    if ( markAsUnderConstruction )
    {
      PromotionStatusType status = attachedPromotion.getPromotionStatus();
      String statusCode = status != null ? status.getCode() : null;
      if ( statusCode != null && statusCode.equals( PromotionStatusType.COMPLETE ) )
      {
        attachedPromotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.UNDER_CONSTRUCTION ) );
      }
    }
  }
}
