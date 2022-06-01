
package com.biperf.core.service.promotion;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.biperf.core.domain.enums.NominationAwardGroupType;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.NominationPromotionTimePeriod;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.SAO;
import com.biperf.core.service.claim.NominationClaimService;
import com.biperf.core.service.claim.RecognitionClaimSubmission;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.value.nomination.EligibleNominationPromotionValueObject;
import com.biperf.core.value.nomination.NominationSubmitDataBehaviorValueBean;
import com.biperf.core.value.nomination.NominationSubmitDataDrawSettingsValueBean;
import com.biperf.core.value.nomination.NominationSubmitDataECardValueBean;
import com.biperf.core.value.nomination.NominationSubmitDataPromotionValueBean;
import com.biperf.core.value.nomination.NominationsPromotionListValueBean;
import com.biperf.core.value.nomination.NominationsSubmissionWizardTabValueBean;

/**
 * Interface specifically for nomination type promotions. Created to avoid growing PromotionService any larger.
 */
public interface NominationPromotionService extends SAO
{

  public final String BEAN_NAME = "nominationPromotionService";

  /**
   * Abstract the class cast. Returns null if promotion does not exist or is not a nomination promotion.
   */
  public NominationPromotion getNominationPromotion( Long promotionId );

  /**
   * Get a list of the tabs for the nomination submission wizard, for the given promotion
   */
  public List<NominationsSubmissionWizardTabValueBean> getSubmissionWizardTabs( Long promotionId );

  /**
   * Get a list of ecards that can be sent along with a nomination, for a given promotion
   * @param userLocale Locale to provide locale sensitive images
   */
  public List<NominationSubmitDataECardValueBean> getECards( NominationPromotion promotion, String userLocale );

  /**
   * Get the draw tool settings for the "draw your own card" feature for the given promotion
   */
  public NominationSubmitDataDrawSettingsValueBean getDrawToolSettings( Long promotionId );

  /**
   * @see NominationClaimService#getBehaviors(Long, Locale, Locale)
   */
  public List<NominationSubmitDataBehaviorValueBean> getBehaviors( Long promotionId );

  /**
   * Get a list of the behaviors for the nomination submission wizard, for the given promotion
   * @param userLocale For dependency injection. User default locale.
   * @param defaultLocale For dependency injection. System default locale.
   */
  public List<NominationSubmitDataBehaviorValueBean> getBehaviors( Long promotionId, Locale userLocale, Locale defaultLocale );

  /**
   * Get values for a new nomination (i.e. when a new claim is being submitted for a nomination promotion)
   * @param userLocale Locale to provide locale sensitive images
   * @return null if no nomination promotion with given ID
   */
  public NominationSubmitDataPromotionValueBean getNominationForSubmission( Long promotionId, String userLocale );

  /**
   * Get over-arching information for a new nomination (i.e. when a new claim is being submitted for a nomination promotion)
   * @param userLocale Locale to provide locale sensitive images
   */
  public NominationsPromotionListValueBean getNominationForSubmissionList( Long promotionId, AuthenticatedUser user, String userLocale );

  Set<NominationPromotionTimePeriod> getPromotionsTimePeriods( Long promoId );

  List<EligibleNominationPromotionValueObject> getEligibleNomPromotions( AuthenticatedUser user );

  boolean canSubmitClaimForTimePeriod( NominationPromotionTimePeriod timePeriod, Date systemDate, String submitterTimeZoneId, Long submitterId );

  int getUsedClaimSubmissionCount( NominationPromotionTimePeriod timeperiod, Long submitterId );

  public boolean canSubmitClaimToday( Long promotionId, String timeZoneID, Long submitterId );

  public List<ServiceError> validateNomineeCountByTimePeriod( RecognitionClaimSubmission submission );

  NominationPromotionTimePeriod getCurrentTimePeriod( Long promotionId, String timeZoneID, Long submitterId );

  void populateAwardsInfo( NominationPromotion promotion, NominationSubmitDataPromotionValueBean nomPromoVO );

  boolean customFormElementBeforeDefaultWhy( NominationPromotion promotion );

  public Map<String, Object> nominationsWinnersModule( Long userId );

  /**
   * The returned object will only have the cards list filled, and that list will only contain certificates with an ID and large image
   */
  public NominationSubmitDataPromotionValueBean getCertificateImages( Long promotionId, NominationAwardGroupType nominationType );

  public boolean isBehaviorBasedApproverTypeExist( Long promotionId );

}
