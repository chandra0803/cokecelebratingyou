/**
 *
 */

package com.biperf.core.dao.promotion;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;

import com.biperf.core.dao.DAO;
import com.biperf.core.dao.promotion.hibernate.PromotionQueryConstraint;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.promotion.ApproverOption;
import com.biperf.core.domain.promotion.EngagementPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionCert;
import com.biperf.core.domain.promotion.PromotionClaimFormStepElementValidation;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.core.value.NameableBean;
import com.biperf.core.value.PromotionBean;
import com.biperf.core.value.PromotionsValueBean;
import com.biperf.core.value.celebration.CelebrationImageFillerValue;
import com.biperf.core.value.nomination.NominationAdminApprovalsBean;
import com.biperf.core.value.nomination.NominationApproverValueBean;
import com.biperf.core.value.nomination.NominationSubmitDataDrawSettingsValueBean;
import com.biperf.core.value.nomination.NominationSubmitDataECardValueBean;
import com.biperf.core.value.participant.PromoRecImageData;
import com.biperf.core.value.participant.PromoRecPictureData;
import com.biperf.core.value.promotion.RecognitionAdvisorPromotionValueBean;

/**
 * PromotionDAO.
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
 * <td>Jun 30, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 */
public interface PromotionDAO extends DAO
{

  /**
   * BEAN_NAME
   */
  public static final String BEAN_NAME = "promotionDAO";

  /**
   * Get the Promotion from the database by the id.
   *
   * @param id
   * @return Promotion
   */
  public Promotion getPromotionById( Long id );

  /**
   * Get the Promotion with associations from the database by the id.
   *
   * @param id
   * @param associationRequestCollection
   * @return Promotion
   */
  public Promotion getPromotionByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection );

  public List<Promotion> getPromotionListWithAssociationsForHomePage( PromotionQueryConstraint queryConstraint, AssociationRequestCollection associationRequestCollection );

  /**
   * Returns a list of promotions that meet the specified criteria. Any parameter can be left null
   * so that the query is not constrained by that parameter.
   * @param queryConstraint
   * @return List the promotion list
   */
  public List getPromotionList( PromotionQueryConstraint queryConstraint );

  /**
   * Returns a list of promotions with hydrated associations that meet the specified criteria. Any parameter can be left null
   * so that the query is not constrained by that parameter.
   * @param queryConstraint
   * @param associationRequestCollection
   * @return List the promotion list
   */
  public List<Promotion> getPromotionListWithAssociationsForRecognitions( PromotionQueryConstraint queryConstraint, AssociationRequestCollection associationRequestCollection );

  public List<Promotion> getPromotionListWithAssociations( PromotionQueryConstraint queryConstraint, AssociationRequestCollection associationRequestCollection );

  /**
   * Returns a count of promotions that meet the specified criteria. Any parameter can be left null
   * so that the query is not constrained by that parameter.
   * @param queryConstraint
   * @return int the promotion list count
   */
  public int getPromotionListCount( PromotionQueryConstraint queryConstraint );

  /**
   * Saves the promotion to the database.
   *
   * @param promotion
   * @return Promotion
   */
  public Promotion save( Promotion promotion );

  /**
   * Deletes the Promotion from the database.
   *
   * @param promotion
   */
  public void delete( Promotion promotion );

  // /**
  // * Returns a list of promotions that meet the specified criteria. Any parameter can be left null
  // * so that the query is not constrained by that parameter.
  // *
  // * TODO: add other constraints - date(s) valid constraint, etc...
  // * @param masterOrChildConstraint - if null, include master and child promos; if true, only
  // * include master promos; if false, only include child promos
  // * @param hasBudgets - if null, include promotions with and without budgets; if true, include
  // * only promotions with budgets; if false, include only promotions without budgets.
  // * @param hasSweepstakes - if null, include promotions with and without sweepstakes, if true,
  // include
  // * only promotions with sweepstakes, if false, includes promotions with AND without sweepstakes.
  // * @param promotionStatusTypesIncluded - array of PromotionStatusTypes. Only include promos
  // matching any
  // * of the types
  // * @param promotionStatusTypesExcluded - array of PromotionStatusTypes. Only include promos not
  // matching any
  // * of the types
  // * @return List of Promotion objects matching the input criteria
  // */
  // public List getPromotionList(Boolean masterOrChildConstraint,
  // Boolean hasBudgets,
  // Boolean hasSweepstakes,
  // PromotionStatusType[] promotionStatusTypesIncluded,
  // PromotionStatusType[] promotionStatusTypesExcluded);

  /**
   * Retrieves all the Promotion from the database.
   *
   * @return List a list of Promotions
   */
  public List getAll();

  /**
   * Retrieves all the live and expired promotions from the database
   *
   * @return List list of Promotions
   */
  public List getAllLiveAndExpired();

  /**
   * Retrieves all the live and expired promotions given type from the database
   *
   * @param promotionType
   * @return List list of Promotions
   */
  public List getAllLiveAndExpiredByType( String promotionType );

  /**
   * Retrieves all the Promotion from the database.
   *
   * @return List a list of Promotions
   */
  public List getAllLive();

  /**
   * Retrieves all the promotions given type from the database
   *
   * @param promotionType
   * @return List list of Promotions
   */
  public List getAllLiveByType( String promotionType );

  /**
   * Retrieves all the non expired Promotion from the database.
   *
   * @return List a list of non expired Promotions
   */
  public List getAllNonExpired();

  /**
   * Retrieves all the expired Promotion from the database.
   *
   * @return List a list of expired Promotions
   */
  public List getAllExpired();

  /**
   * Get all promotions with sweepstakes
   *
   * @return List
   */
  public List getAllWithSweepstakes();

  /**
   * Get all promotions with sweepstakes with associations
   * @param associationRequestCollection
   * @return List
   */
  public List getAllWithSweepstakesWithAssociations( AssociationRequestCollection associationRequestCollection );

  /**
   * Get the promotion claimFormStepElement validation object by the id.
   *
   * @param id
   * @return PromotionClaimFormStepElementValidation
   */
  public PromotionClaimFormStepElementValidation getPromotionClaimFormStepElementValidationById( Long id );

  /**
   * Retrieves all the promotion claimFormStepElement validation object.
   *
   * @param promotion
   * @param claimFormStep
   * @return List of PromotionClaimFormStepElementValidation objects
   */
  public List getAllPromotionClaimFormStepElementValidations( Promotion promotion, ClaimFormStep claimFormStep );

  /**
   * Gets a {@link PromotionClaimFormStepElementValidation} object by promotion and claim form step
   * element.
   *
   * @param promotion
   * @param claimFormStepElement
   * @return PromotionClaimFormStepElementValidation
   */
  public PromotionClaimFormStepElementValidation getPromotionClaimFormStepElementValidation( Promotion promotion, ClaimFormStepElement claimFormStepElement );

  /**
   * Saves the promotion claim form step element validation object.
   *
   * @param promoCFSEValidation
   * @return PromotionClaimFormStepElementValidation
   */
  public PromotionClaimFormStepElementValidation savePromotionClaimFormStepElementValidation( PromotionClaimFormStepElementValidation promoCFSEValidation );

  /**
   * Deletes a promotionClaimFormStepElementValidation.
   *
   * @param pcfsev
   */
  public void deletePromotionClaimFormStepElementValidation( PromotionClaimFormStepElementValidation pcfsev );

  /**
   * Check the database to make sure the promotion_name is unique for non-deleted promotions
   *
   * @param promotionName
   * @param currentPromotionId
   * @return boolean
   */
  public boolean isPromotionNameUnique( String promotionName, Long currentPromotionId );

  /**
   * Retrieves all the claim form step validations for the specified promotion
   *
   * @param promotion
   * @return List
   */
  public List getPromotionClaimFormStepElementValidationsByPromotion( Promotion promotion );

  /**
   * Retrieves all the child promotions for the specified promotion
   *
   * @param promotionId
   * @return List
   */
  public List getChildPromotions( Long promotionId );

  /**
   * Retrieves all the non expired child promotions for the specified promotion
   *
   * @param promotionId
   * @return List
   */
  public List getNonExpiredChildPromotions( Long promotionId );

  /**
   * Retrieves the promotion for the specified enrollProgramCode.
   * enrollProgramCode should be unique for each promotion.
   *
   * Overridden from @see com.biperf.core.dao.promotion.PromotionDAO#getPromotionByEnrollProgramCode(java.lang.String)
   * @param enrollProgramCode
   * @return List
   */
  public List getPromotionByEnrollProgramCode( String enrollProgramCode );

  /**
   * Get the the number of Reconitions submitted for merchorder or recognition types.
   * They do NOT need to be approved
   *
   * @param promotionId
   * @return long
   */
  public long getRecognitionsSubmittedForPromotion( Long promotionId );

  public long getNominationsSubmittedForPromotion( Long promotionId );

  public long getProductClaimsSubmittedForPromotion( Long promotionId );

  public long getQuizSubmittedForPromotion( Long promotionId );

  public BigDecimal getPartnerAwardAmountByPromoitonAndSequenceNo( Long promotionId, int seqNo );

  /* Bug # 34020 start */
  /**
   * @return list
   */
  public List<Promotion> getAllPromotionsWithSweepstakes();
  /* Bug # 34020 end */

  public List getPublicRecognitionPromotionsWithClaims();

  public List getMerchandisePromotionIds();

  public PromotionCert getPromoCertificateById( Long certificateId );

  public QuizPromotion getLiveDIYQuizPromotion();

  public EngagementPromotion getLiveOrCompletedEngagementPromotion();

  public List<FormattedValueBean> getAwardGenEligiblePromotionList();

  public List<FormattedValueBean> getEngagementEligiblePromotionList();

  public List<FormattedValueBean> getEngagementRecognitionPromotionsList();

  public List<Promotion> getAllBadges();

  public List<Long> getEligiblePromotionsFromPromoBadgeId( Long promoBadgeId );

  public QuizPromotion getLiveOrCompletedDIYQuizPromotion();

  public List<CelebrationImageFillerValue> getCelebrationImageFillersForPromotion( Long promotionId );

  public EngagementPromotion getLiveEngagementPromotion();

  public List getPurlRecipinentListOnPurlTile();

  public boolean isRecogPromotionInRPM( Long promotionId );

  public Integer getBadgePromotionCountForPromoId( Long promoId );

  public Integer getPromotionSelfRecognition( Long promoId );

  public List<PromotionsValueBean> getAllSortedApproverPromotions( Long userId, String promotionType );

  public List<MerchOrder> getMerchOrdersToGenerateGiftCodeByPromotionIdAndUserId( Long promotionId, Long userId );

  public boolean getMerchOrderByPromotionIdAndUserId( Long promotionId, Long userId );

  public List getPromotionBillCodes( Long promotionId, boolean sweepstakes );

  /**
   * Get a list of ecards that can be sent along with a nomination, for a given promotion
   * @param userLocale Locale to supply locale sensitive images for cards
   */
  public List<NominationSubmitDataECardValueBean> getECards( Long promotionId, String userLocale );

  /**
   * Get the draw tool settings for the "draw your own card" feature for the given promotion
   */
  public NominationSubmitDataDrawSettingsValueBean getDrawToolSettings( Long promotionId );

  /**
   * Get a list of behavior types for the given promotion. 
   * Used by service layer to get rest of submit behavior values
   */
  public List<String> getBehaviorTypes( Long promotionId );

  /**
   * Get a list of the promotion IDs that have the given behavior selected
   */
  public List<Long> getPromotionIdsForBehavior( String behaviorType );

  public List getApprovalOptionsByApproverId( Long approverId );

  List<ApproverOption> getApproverOptions( int optionId );

  List<NominationApproverValueBean> getApproverOptions( int optionId, Long promotionId );

  public List<NameableBean> getNominationPromotionListForApproverFileLoad();

  public ApproverOption getApproverTypeByLevel( Long approvalLevel, Long promotionId );

  public List<NominationAdminApprovalsBean> getNominationApprovalClaimPromotions();

  /**
   * Get the step name of the last step defined for a nomination promotion. 
   * May return null.
   */
  public String getLastWizardStepName( Long promotionId );

  public BigDecimal getTotalUnapprovedAwardQuantity( Long promotionId, Long userId, Long nodeId, Long budgetMasterId );

  public long getClaimAwardQuantity( Long claim );

  public BigDecimal getTotalUnapprovedAwardQuantityPurl( Long promotionId, Long userId, Long nodeId, Long budgetMasterId );

  public long getTotalImportPaxAwardQuantity( Long importFileId );

  public boolean isSSILivePromotionAvailable();

  public boolean checkIfAnyPointsContestsByPaxId( Long userId );

  Date getUAGoalQuestPromotionStartDate( Long userId );

  public List<String> getAllUniqueBillCodes( Long promotionId );

  // Alerts Performance Tuning
  public List<PromotionBean> getExpiredPromotions();

  public List<RecognitionAdvisorPromotionValueBean> getPromotionListForRA( Long giverId, Long receiverId );

  public boolean isBehaviorBasedApproverTypeExist( Long promotionId );

  public int deletePromoCard( Long cardId, Long promotionId );

  public List<PromoRecImageData> getNotMigratedPromRecogAvatarData();

  public void updatePromRecAvatar( Long promotionId, String defaultCelebrationAvatar, String defaultCcontributorAavatar );

  public List<String> getNonPurlAndCelebPromotionsName();

  public void updatePromRecCeleAvatar( Long promotionId, String defaultCelebrationAvatar );

  public void updatePromRecContrAvatar( Long promotionId, String defaultContributorAavatar );

  public List<PromoRecPictureData> getNotMigratedPromRecogPictureData();

  public void updateContResPic( Long promotionId );
  // Client customizations for WIP #42701 starts
  public boolean isCashPromo( Long promotionId );
  // Client customizations for WIP #42701 ends
  
//Client customization for WIP #39189 starts
 public Long getPromotionIdByClaimId( Long claimId );
 // Client customization for WIP #39189 ends
 
  // Client customizations for WIP #62128 starts
  public Long getCheersPromotionId() throws EmptyResultDataAccessException;
  // Client customizations for WIP #62128 ends


}
