/**
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/impl/PromotionServiceImpl.java,v $
 */

package com.biperf.core.service.promotion.impl;

import static org.apache.commons.lang3.math.NumberUtils.isNumber;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.dao.EmptyResultDataAccessException;

import com.biperf.core.dao.budget.BudgetMasterDAO;
import com.biperf.core.dao.claim.ClaimFormDAO;
import com.biperf.core.dao.claim.NominationClaimDAO;
import com.biperf.core.dao.claim.hibernate.NominationClaimQueryConstraint;
import com.biperf.core.dao.claim.hibernate.QuizClaimQueryConstraint;
import com.biperf.core.dao.client.CokeClientDAO;
import com.biperf.core.dao.diyquiz.DIYQuizDAO;
import com.biperf.core.dao.engagement.EngagementDAO;
import com.biperf.core.dao.fileload.ImportFileDAO;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.ParticipantPartnerDAO;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionQueryConstraint;
import com.biperf.core.dao.throwdown.DivisionDAO;
import com.biperf.core.dao.throwdown.TeamDAO;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.budget.PromotionBudgetSweep;
import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.claim.ClaimGroup;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.diyquiz.DIYQuiz;
import com.biperf.core.domain.enums.ActivityType;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.BadgeType;
import com.biperf.core.domain.enums.BudgetStatusType;
import com.biperf.core.domain.enums.BudgetType;
import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.domain.enums.ClaimFormElementType;
import com.biperf.core.domain.enums.CustomApproverType;
import com.biperf.core.domain.enums.CustomFormElementAddress;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.ManagerOverrideStructure;
import com.biperf.core.domain.enums.NominationClaimStatusType;
import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.domain.enums.ProcessFrequencyType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionEmailNotificationType;
import com.biperf.core.domain.enums.PromotionNotificationFrequencyType;
import com.biperf.core.domain.enums.PromotionPayoutType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.PublicRecognitionAudienceType;
import com.biperf.core.domain.enums.StateType;
import com.biperf.core.domain.enums.SweepstakesWinnerEligibilityType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.filestore.FileStore;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.gamification.BadgeLibrary;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.goalquest.PromotionGoalQuestSurvey;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.mailing.MailingRecipientData;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.mtc.MTCVideo;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.AudienceParticipant;
import com.biperf.core.domain.participant.CriteriaAudience;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantPartner;
import com.biperf.core.domain.participant.PaxAudience;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessSchedule;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.Approver;
import com.biperf.core.domain.promotion.ApproverCriteria;
import com.biperf.core.domain.promotion.ApproverOption;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.ClaimFormNotificationType;
import com.biperf.core.domain.promotion.Division;
import com.biperf.core.domain.promotion.EngagementPromotion;
import com.biperf.core.domain.promotion.EngagementPromotions;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.InstantPoll;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.NominationPromotionLevel;
import com.biperf.core.domain.promotion.NominationPromotionTimePeriod;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionApprovalOption;
import com.biperf.core.domain.promotion.PromotionCert;
import com.biperf.core.domain.promotion.PromotionClaimFormStepElementValidation;
import com.biperf.core.domain.promotion.PromotionCopyHolder;
import com.biperf.core.domain.promotion.PromotionNotification;
import com.biperf.core.domain.promotion.PromotionNotificationType;
import com.biperf.core.domain.promotion.PromotionParticipantApprover;
import com.biperf.core.domain.promotion.PromotionParticipantSubmitter;
import com.biperf.core.domain.promotion.PromotionPublicRecognitionAudience;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.promotion.Survey;
import com.biperf.core.domain.promotion.SurveyPromotion;
import com.biperf.core.domain.promotion.Team;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.domain.proxy.Proxy;
import com.biperf.core.domain.purl.PurlContributor;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.domain.quiz.Quiz;
import com.biperf.core.domain.quiz.QuizLearningDetails;
import com.biperf.core.domain.quiz.QuizLearningObject;
import com.biperf.core.domain.survey.ParticipantSurvey;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.domain.util.BaseDomainUtils;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.BaseAssociationRequest;
import com.biperf.core.service.ProjectionAttribute;
import com.biperf.core.service.ProjectionCollection;
import com.biperf.core.service.UpdateAssociationRequest;
import com.biperf.core.service.awardgenerator.AwardGeneratorService;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.budget.BudgetMasterToBudgetSegmentsAssociationRequest;
import com.biperf.core.service.calculator.CalculatorService;
import com.biperf.core.service.celebration.CelebrationService;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.claim.ClaimGroupAssociationRequest;
import com.biperf.core.service.claim.ClaimGroupService;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.claim.NominationClaimService;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.cms.CMDataElement;
import com.biperf.core.service.cmx.CMXService;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.engageprogram.EngageProgramService;
import com.biperf.core.service.filestore.FileStoreService;
import com.biperf.core.service.goalquest.GoalQuestService;
import com.biperf.core.service.goalquest.PaxGoalService;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.instantpoll.InstantPollService;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.merchorder.MerchOrderService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.mtc.MTCService;
import com.biperf.core.service.mtc.MTCVideoService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ListBuilderService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.participantsurvey.ParticipantSurveyService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.proxy.ProxyAssociationRequest;
import com.biperf.core.service.proxy.ProxyService;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.quiz.QuizService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.service.serviceanniversary.ServiceAnniversaryService;
import com.biperf.core.service.survey.SurveyAssociationRequest;
import com.biperf.core.service.survey.SurveyService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.CmxTranslateHelperBean;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.threads.CallableFactory;
import com.biperf.core.value.ActivityCenterValueBean;
import com.biperf.core.value.AlertsValueBean;
import com.biperf.core.value.AwardGeneratorManagerReminderBean;
import com.biperf.core.value.CelebrationManagerReminderBean;
import com.biperf.core.value.ClaimRecipientValueBean;
import com.biperf.core.value.EngagementPromotionData;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.core.value.MerchAwardReminderBean;
import com.biperf.core.value.NameableBean;
import com.biperf.core.value.NominationsApprovalValueBean;
import com.biperf.core.value.ParticipantDIYQuizClaimHistory;
import com.biperf.core.value.ParticipantQuizClaimHistory;
import com.biperf.core.value.PendingNominationsApprovalMainValueBean;
import com.biperf.core.value.PromotionApprovableValue;
import com.biperf.core.value.PromotionBean;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.core.value.PromotionPaxValue;
import com.biperf.core.value.PromotionsValueBean;
import com.biperf.core.value.QuizPageValueBean;
import com.biperf.core.value.RecognitionBean;
import com.biperf.core.value.SurveyPageValueBean;
import com.biperf.core.value.celebration.CelebrationImageFillerValue;
import com.biperf.core.value.mtc.v1.upload.UploadResponse;
import com.biperf.core.value.nomination.NominationAdminApprovalsBean;
import com.biperf.core.value.nomination.NominationApproverValueBean;
import com.biperf.core.value.participant.PromoRecImageData;
import com.biperf.core.value.participant.PromoRecPictureData;
import com.biperf.core.value.pastwinners.NominationMyWinners;
import com.biperf.core.value.promotion.CustomFormFieldView;
import com.biperf.core.value.promotion.CustomFormSelectListView;
import com.biperf.core.value.promotion.CustomFormStepElementsView;
import com.biperf.core.value.promotion.RecognitionAdvisorPromotionValueBean;
import com.google.common.collect.Lists;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.domain.Translations;
import com.objectpartners.cms.domain.enums.DataTypeEnum;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.CmsUtil;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * PromotionServiceImpl.
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
 * <td>Jul 4, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class PromotionServiceImpl implements PromotionService
{
  private static final String PROMOTION_ASSET_TYPE_NAME = "_PromotionData2";
  private static final String SPOTLIGHT_LEVEL_DATA_ASSET_PREFIX = "spotlight_levels_data.";
  private static final String SPOTLIGHT_LEVEL_NAME_KEY = "LEVEL_NAME";
  private static final String SPOTLIGHT_LEVEL_DATA_SECTION_CODE = "spotlight_levels_data";
  private static final String SPOTLIGHT_LEVEL_ASSET_TYPE_NAME = "_SPOTLIGHT_LEVELS_DATA";
  private static final String OUTPUT_RETURN_CODE = "p_out_return_code";

  /**
    * Stored proc returns this code when the stored procedure executed without errors
    */
  public static final String GOOD = "00";

  public static final String RECOGNITION_PRIMARY = "RECOGNITION_PRIMARY";
  public static final String RECOGNITION_SECONDARY = "RECOGNITION_SECONDARY";
  public static final String PRODUCT_CLAIM_PRIMARY = "PRODUCT_CLAIM_PRIMARY";
  public static final String PRODUCT_CLAIM_SECONDARY = "PRODUCT_CLAIM_SECONDARY";
  public static final String QUIZ_PRIMARY = "QUIZ_PRIMARY";
  public static final String SURVEY_PRIMARY = "SURVEY_PRIMARY";
  public static final String UNKNOWN_PRIMARY = "UNKNOWN_PRIMARY";
  public static final String UNKNOWN_SECONDARY = "UNKNOWN_SECONDARY";
  public static final String NOMINATION_PRIMARY = "NOMINATION_PRIMARY";
  public static final String NOMINATION_SECONDARY = "NOMINATION_SECONDARY";
  public static final String GOALQUEST_PRIMARY = "GOALQUEST_PRIMARY";
  public static final String CHALLENGEPOINT_PRIMARY = "CHALLENGEPOINT_PRIMARY"; // Fix 21285
  public static final String DIY_MANAGER = "DIY_MANAGER";
  public static final String DIY_PARTICIPANT = "DIY_PARTICIPANT";
  public static final String THROWDOWN_PRIMARY = "THROWDOWN_PRIMARY";
  public static final String THROWDOWN_SECONDARY = "THROWDOWN_SECONDARY";

  private static final Log logger = LogFactory.getLog( PromotionServiceImpl.class );
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------
  private PromotionDAO promotionDAO = null;
  private ClaimFormDefinitionService claimFormDefinitionService = null;
  private QuizService quizService = null;
  private CMAssetService cmAssetService = null;
  private AudienceService audienceService;
  private ListBuilderService listBuilderService;
  private HierarchyService hierarchyService;
  private ParticipantService participantService;
  private CalculatorService calculatorService;
  private ClaimFormDAO claimFormDAO = null;
  private ParticipantDAO participantDAO = null;
  private NodeDAO nodeDAO = null;
  private TeamDAO teamDAO = null;
  private UserService userService = null;
  private ClaimService claimService;
  private ClaimGroupService claimGroupService;
  private MessageService messageService;
  private MailingService mailingService;
  private SystemVariableService systemVariableService;
  private JournalService journalService;
  private BudgetMasterDAO budgetMasterDAO = null;
  private DIYQuizDAO diyQuizDAO = null;
  private MerchLevelService merchLevelService = null;
  private ParticipantPartnerDAO participantPartnerDAO = null;
  private FileStoreService fileStoreService = null;
  private AwardGeneratorService awardGeneratorService = null;
  private SurveyService surveyService;
  private ParticipantSurveyService participantSurveyService;
  private BudgetMasterService budgetMasterService;
  private AuthorizationService aznService;
  private CelebrationService celebrationService;
  private MerchOrderService merchOrderService;
  private DivisionDAO divisionDAO;
  private EngagementDAO engagementDAO;
  private CountryService countryService;
  private NominationClaimDAO nominationClaimDAO = null;
  private InstantPollService instantPollService = null;
  private ImportFileDAO importFileDAO = null;
  @Autowired
  private MTCService mtcService;
  @Autowired
  private MTCVideoService mtcVideoService;
  @Autowired
  private ServiceAnniversaryService serviceAnniversaryService;
  @Autowired
  private EngageProgramService engageProgramService;
  @Autowired
  private CMXService cmxService;
  // Client customization for WIP #43735 starts
  private CokeClientDAO cokeClientDAO;
  // Client customization for WIP #43735 ends
  
  public MTCVideoService getMtcVideoService()
  {
    return mtcVideoService;
  }

  public void setMtcVideoService( MTCVideoService mtcVideoService )
  {
    this.mtcVideoService = mtcVideoService;
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  /**
   * Bug # 51917
   * Since the injection is by lookup-method as defined in application context spring will
   * inject the bean. This method will be overridden by spring while creating the bean.
   * return null
   */
  public ProxyService getProxyService()
  {
    return null;
  }

  public void setParticipantPartnerDAO( ParticipantPartnerDAO participantPartnerDAO )
  {
    this.participantPartnerDAO = participantPartnerDAO;
  }

  public UserService getUserService()
  {
    return userService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  public PromotionDAO getPromotionDAO()
  {
    return promotionDAO;
  }

  public void setPromotionDAO( PromotionDAO promotionDAO )
  {
    this.promotionDAO = promotionDAO;
  }

  public void setParticipantDAO( ParticipantDAO participantDAO )
  {
    this.participantDAO = participantDAO;
  }

  public void setDiyQuizDAO( DIYQuizDAO diyQuizDAO )
  {
    this.diyQuizDAO = diyQuizDAO;
  }

  public void setClaimFormDefinitionService( ClaimFormDefinitionService claimFormDefinitionService )
  {
    this.claimFormDefinitionService = claimFormDefinitionService;
  }

  public void setQuizService( QuizService quizService )
  {
    this.quizService = quizService;
  }

  public void setListBuilderService( ListBuilderService listBuilderService )
  {
    this.listBuilderService = listBuilderService;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  public void setNodeDAO( NodeDAO nodeDAO )
  {
    this.nodeDAO = nodeDAO;
  }

  public void setMessageService( MessageService messageService )
  {
    this.messageService = messageService;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public void setAuthorizationService( AuthorizationService aznService )
  {
    this.aznService = aznService;
  }

  public void setCelebrationService( CelebrationService celebrationService )
  {
    this.celebrationService = celebrationService;
  }

  public void setMerchOrderService( MerchOrderService merchOrderService )
  {
    this.merchOrderService = merchOrderService;
  }

  /**
   * Set the ClaimFormDAO through IoC
   *
   * @param claimFormDAO
   */
  public void setClaimFormDAO( ClaimFormDAO claimFormDAO )
  {
    this.claimFormDAO = claimFormDAO;
  }

  // ---------------------------------------------------------------------------
  // Service Methods
  // ---------------------------------------------------------------------------

  /**
   * @param claimService value for claimService property
   */
  public void setClaimService( ClaimService claimService )
  {
    this.claimService = claimService;
  }

  /**
   * @param NominationClaimDAO value for NominationClaimDAO property
   */
  public void setNominationClaimDAO( NominationClaimDAO nominationClaimDAO )
  {
    this.nominationClaimDAO = nominationClaimDAO;
  }

  /**
   * @param InstantPollService value for InstantPollService property
   */
  public void setInstantPollService( InstantPollService instantPollService )
  {
    this.instantPollService = instantPollService;
  }

  /**
   * @param audienceService value for audienceService property
   */
  public void setAudienceService( AudienceService audienceService )
  {
    this.audienceService = audienceService;
  }

  public FileStoreService getFileStoreService()
  {
    return fileStoreService;
  }

  public void setFileStoreService( FileStoreService fileStoreService )
  {
    this.fileStoreService = fileStoreService;
  }

  /**
   * @param hierarchyService
   */
  public void setHierarchyService( HierarchyService hierarchyService )
  {
    this.hierarchyService = hierarchyService;
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.promotion.PromotionService#getPromotionById(java.lang.Long)
   * @param id
   * @return Promotion
   */
  public Promotion getPromotionById( Long id )
  {
    return promotionDAO.getPromotionById( id );
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.promotion.PromotionService#getPromotionByIdWithAssociations(java.lang.Long,
   *      com.biperf.core.service.AssociationRequestCollection)
   * @param id
   * @param associationRequestCollection
   * @return User
   */
  public Promotion getPromotionByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    return this.promotionDAO.getPromotionByIdWithAssociations( id, associationRequestCollection );
  }

  public Promotion getPromotionByIdWithCalcAsso( Long id, AssociationRequestCollection associationRequestCollection )
  {
    return this.promotionDAO.getPromotionByIdWithAssociations( id, associationRequestCollection );
  }

  /**
   * @param queryConstraint
   * Overridden from
   * @see com.biperf.core.service.promotion.PromotionService#getPromotionList(com.biperf.core.dao.promotion.hibernate.PromotionQueryConstraint)
   * @return List
   */
  public List getPromotionList( PromotionQueryConstraint queryConstraint )
  {
    return promotionDAO.getPromotionList( queryConstraint );
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.promotion.PromotionService#getPromotionListWithAssociations(com.biperf.core.dao.promotion.hibernate.PromotionQueryConstraint,
   *      com.biperf.core.service.AssociationRequestCollection)
   * @return List
   */
  public List getPromotionListWithAssociations( PromotionQueryConstraint queryConstraint, AssociationRequestCollection associationRequestCollection )
  {
    return promotionDAO.getPromotionListWithAssociations( queryConstraint, associationRequestCollection );
  }

  public List getPromotionListWithAssociationsForRecognitions( PromotionQueryConstraint queryConstraint, AssociationRequestCollection associationRequestCollection )
  {
    return promotionDAO.getPromotionListWithAssociationsForRecognitions( queryConstraint, associationRequestCollection );
  }

  public List getPromotionListWithAssociationsForHomePage( PromotionQueryConstraint queryConstraint, AssociationRequestCollection associationRequestCollection )
  {
    return promotionDAO.getPromotionListWithAssociationsForHomePage( queryConstraint, associationRequestCollection );
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.promotion.PromotionService#getPromotionListCount(com.biperf.core.dao.promotion.hibernate.PromotionQueryConstraint)
   * @param queryConstraint
   * @return int
   */
  public int getPromotionListCount( PromotionQueryConstraint queryConstraint )
  {
    return promotionDAO.getPromotionListCount( queryConstraint );
  }

  /**
   * Performs additional validation on the given promotion.
   *
   * @param promotionId
   * @param updateAssociationRequest
   * @throws com.biperf.core.exception.ServiceErrorExceptionWithRollback
   */
  public void validatePromotion( Long promotionId, UpdateAssociationRequest updateAssociationRequest ) throws ServiceErrorExceptionWithRollback
  {
    if ( updateAssociationRequest != null )
    {
      Promotion promotion = this.getPromotionById( promotionId );
      updateAssociationRequest.validate( promotion );
    }
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.promotion.PromotionService#savePromotion(com.biperf.core.domain.promotion.Promotion)
   * @param promotionToSave
   * @return Promotion
   * @throws UniqueConstraintViolationException
   */
  public Promotion savePromotion( Promotion promotionToSave ) throws UniqueConstraintViolationException
  {
    // Check for uniqueness of promotion_name
    boolean isPromotionNameUnique = promotionDAO.isPromotionNameUnique( promotionToSave.getName().trim(), promotionToSave.getId() );
    if ( !isPromotionNameUnique )
    {
      throw new UniqueConstraintViolationException();
    }

    Promotion persistedPromotion = promotionDAO.save( promotionToSave );

    if ( persistedPromotion.isQuizPromotion() )
    {
      updateQuizFormStatus( ( (QuizPromotion)persistedPromotion ).getQuiz().getId() );
    }
    else if ( persistedPromotion.isSurveyPromotion() )
    {
      updateSurveyFormStatus( ( (SurveyPromotion)persistedPromotion ).getSurvey().getId() );
    }
    else if ( persistedPromotion.isGoalQuestOrChallengePointPromotion() )
    {
      GoalQuestPromotion gqPromotion = (GoalQuestPromotion)persistedPromotion;
      for ( Iterator iter = gqPromotion.getPromotionGoalQuestSurveys().iterator(); iter.hasNext(); )
      {
        PromotionGoalQuestSurvey gqSurvey = (PromotionGoalQuestSurvey)iter.next();
        updateSurveyFormStatus( gqSurvey.getSurvey().getId() );
      }
    }
    else
    {
      if ( !persistedPromotion.isSurveyPromotion() && !persistedPromotion.isWellnessPromotion() && persistedPromotion.getClaimForm() != null )
      {
        updateClaimFormStatus( persistedPromotion.getClaimForm().getId() );
      }
    }

    if ( persistedPromotion.isAbstractRecognitionPromotion() )
    {
      if ( ( (AbstractRecognitionPromotion)persistedPromotion ).getCalculator() != null )
      {
        updateCalculatorStatus( ( (AbstractRecognitionPromotion)persistedPromotion ).getCalculator().getId() );
      }
    }

    return persistedPromotion;
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.promotion.PromotionService#savePromotion(Long,
   *      UpdateAssociationRequest)
   * @param promotionId
   * @param updateAssociationRequest
   * @return Promotion
   * @throws UniqueConstraintViolationException
   */
  public Promotion savePromotion( Long promotionId, UpdateAssociationRequest updateAssociationRequest ) throws UniqueConstraintViolationException
  {
    if ( updateAssociationRequest != null )
    {
      List updateAssociations = new ArrayList();
      updateAssociations.add( updateAssociationRequest );
      return savePromotion( promotionId, updateAssociations );
    }

    return null;
  }

  /**
   * Saves the promotion with payouts to the database.
   *
   * @param promotionId
   * @param updateAssociationRequests
   * @return Promotion
   * @throws com.biperf.core.exception.UniqueConstraintViolationException
   */
  public Promotion savePromotion( Long promotionId, List updateAssociationRequests ) throws UniqueConstraintViolationException
  {
    if ( updateAssociationRequests != null && updateAssociationRequests.size() > 0 )
    {
      Promotion promotion = this.getPromotionById( promotionId );

      if ( !promotion.isWellnessPromotion() && !promotion.isQuizPromotion() && !promotion.isDIYQuizPromotion() && !promotion.isSurveyPromotion() && !promotion.isGoalQuestPromotion()
          && !promotion.isChallengePointPromotion() && !promotion.isThrowdownPromotion() && !promotion.isBadgePromotion() && !promotion.isEngagementPromotion() && !promotion.isSSIPromotion() )
      {

        // This is needed for wizard functionality since lazy is false on claimFormSteps
        Hibernate.initialize( promotion.getClaimForm().getClaimFormSteps() );

      }

      for ( int i = 0; i < updateAssociationRequests.size(); i++ )
      {
        UpdateAssociationRequest request = (UpdateAssociationRequest)updateAssociationRequests.get( i );
        request.execute( promotion );
      }

      return savePromotion( promotion );
    }

    return null;
  }

  /**
   * Deletes a list of promotions. Overridden from
   *
   * @see com.biperf.core.service.promotion.PromotionService#deletePromotions(java.util.List)
   * @param promotionIdList - List of promotion.id
   * @throws ServiceErrorException
   */
  public void deletePromotions( List promotionIdList ) throws ServiceErrorException
  {
    Iterator promotionIdIter = promotionIdList.iterator();

    while ( promotionIdIter.hasNext() )
    {
      this.deletePromotion( (Long)promotionIdIter.next() );
    }

  }

  /**
   * Deletes a promotion if it is under_construction or complete, its related claimFormStep and
   * updates the claimForm status related to the promotion. If the promotion is live it cannot be
   * deleted. If the promotion is expired, it is only a logical delete. If the promotion has
   * child(ren) delete them as well following the rules applicable to the parent. Overridden from
   *
   * @see com.biperf.core.service.promotion.PromotionService#deletePromotion(Long)
   * @param promotionId
   * @throws ServiceErrorException
   */
  public void deletePromotion( Long promotionId ) throws ServiceErrorException
  {
    Promotion promotionToDelete = promotionDAO.getPromotionById( promotionId );
    Map output = new HashMap();

    // if the promotion is live, throw an exception
    if ( promotionToDelete.isLive() )
    {
      throw new ServiceErrorException( "Promotion (id: " + promotionId + ") is live - it cannot be deleted" );
    }

    // updateClaimFormStatus( promotionToDelete.getClaimForm().getId() );

    // if the promotion is expired, it is a logical delete
    if ( promotionToDelete.isExpired() )
    {
      // Change the name of the promotion so that the name can be reused
      String promoName = promotionToDelete.getName();
      Timestamp timestamp = new Timestamp( new Date().getTime() );
      promotionToDelete.setName( promoName + "-" + timestamp );
      promotionToDelete.setDeleted( true );
      try
      {
        savePromotion( promotionToDelete );
      }
      catch( UniqueConstraintViolationException ucve )
      {
        // this exception should not occur while doing a logical delete; so ignore it for now.
      }

      // if the expired promotion is a parent promotion, logical delete its children=
      if ( promotionToDelete.isProductClaimPromotion() )
      {
        if ( promotionToDelete.getChildrenCount() > 0 )
        {
          // walk thru the set of all children promotion and set delete to true
          List childrenToDelete = promotionDAO.getChildPromotions( promotionId );

          Iterator iter = childrenToDelete.iterator();

          while ( iter.hasNext() )
          {
            Promotion childToDelete = (Promotion)iter.next();
            childToDelete.setDeleted( true );
            try
            {
              savePromotion( childToDelete );
            }
            catch( UniqueConstraintViolationException ucve )
            {
              // this exception should not occur while doing a logical delete; so ignore it for now.
            }
          }
        } // END logical delete its children
      }
    }
    else if ( promotionToDelete.isDIYQuizPromotion() )
    { // Cannot do a physical delete as there is a badge and badge promotion associated
      String promoName = promotionToDelete.getName();
      Timestamp timestamp = new Timestamp( new Date().getTime() );
      promotionToDelete.setName( promoName + "-" + timestamp );
      promotionToDelete.setDeleted( true );
      try
      {
        savePromotion( promotionToDelete );
      }
      catch( UniqueConstraintViolationException ucve )
      {
        // this exception should not occur while doing a logical delete; so ignore it for now.
      }
    }
    // else the promotion is either in under construction or complete status, do a physical delete
    else
    {
      Iterator cfsevIter = this.promotionDAO.getPromotionClaimFormStepElementValidationsByPromotion( promotionToDelete ).iterator();

      while ( cfsevIter.hasNext() )
      {
        PromotionClaimFormStepElementValidation pcfsev = (PromotionClaimFormStepElementValidation)cfsevIter.next();
        this.promotionDAO.deletePromotionClaimFormStepElementValidation( pcfsev );
      }

      if ( promotionToDelete.hasParent() )
      {
        ( (ProductClaimPromotion)promotionToDelete ).getParentPromotion().getChildPromotions().remove( promotionToDelete );
      }

      if ( promotionToDelete.isThrowdownPromotion() && ( (ThrowdownPromotion)promotionToDelete ).getDivisions() != null )
      {
        ThrowdownPromotion throwdownPromotion = (ThrowdownPromotion)promotionToDelete;
        Set<Division> divisionList = throwdownPromotion.getDivisions();

        for ( Division division : divisionList )
        {
          throwdownPromotion.getDivisions().remove( division );
          this.divisionDAO.delete( division.getId() );
        }
        try
        {
          savePromotion( throwdownPromotion );
        }
        catch( UniqueConstraintViolationException ucve )
        {
          // this exception should not occur while doing a logical delete; so ignore it for now.
        }
      }

      if ( promotionToDelete.isRecognitionPromotion() && promotionToDelete.getAwardType() != null && promotionToDelete.getAwardType().getCode().equals( PromotionAwardsType.POINTS ) )
      {
        RecognitionPromotion recogPromotion = (RecognitionPromotion)promotionToDelete;
        if ( recogPromotion.isBudgetSweepEnabled() )
        {
          Set<PromotionBudgetSweep> budgetSweeps = recogPromotion.getPromotionBudgetSweeps();

          for ( PromotionBudgetSweep promotionBudgetSweep : budgetSweeps )
          {
            recogPromotion.getPromotionBudgetSweeps().remove( promotionBudgetSweep );
            this.budgetMasterDAO.saveBudgetMaster( promotionToDelete.getBudgetMaster() );
          }
        }
      }

      if ( promotionToDelete.isSSIPromotion() )
      {
        Badge badge = promotionToDelete.getBadge();
        if ( badge != null )
        {
          promotionDAO.delete( badge );
        }
      }

      if ( promotionToDelete.isEngagementPromotion() )
      {
        output = engagementDAO.deleteEngagementPromotion( promotionToDelete.getId() );
        if ( GOOD.equals( output.get( OUTPUT_RETURN_CODE ) ) )
        {
          logger.error( " *************** Promotion Deleted Successfully ************* " );
        }
        else
        {
          throw new ServiceErrorException( "Stored Procedure returned  " + output.get( OUTPUT_RETURN_CODE ) + " promotion cannot be deleted" );
        }
      }
      else
      {
        promotionDAO.delete( promotionToDelete );
      }

      // if the under construction or complete promotion is a parent promotion, physical delete its
      // children
      if ( promotionToDelete.isProductClaimPromotion() )
      {
        if ( promotionToDelete.getChildrenCount() > 0 )
        {
          // walk thru the list of all non-expired children promotion and delete each
          List childrenToDelete = promotionDAO.getNonExpiredChildPromotions( promotionId );

          Iterator iter = childrenToDelete.iterator();

          while ( iter.hasNext() )
          {
            Promotion childToDelete = (Promotion)iter.next();
            promotionDAO.delete( childToDelete );
          }
        } // END physical delete its children
      }
    }
    if ( !promotionToDelete.isQuizPromotion() && !promotionToDelete.isDIYQuizPromotion() && !promotionToDelete.isSurveyPromotion() && !promotionToDelete.isGoalQuestPromotion()
        && !promotionToDelete.isChallengePointPromotion() && !promotionToDelete.isWellnessPromotion() && !promotionToDelete.isEngagementPromotion() && !promotionToDelete.isThrowdownPromotion()
        && !promotionToDelete.isSSIPromotion() )
    {
      // This is to fix the bug 8740 when future unique promo removed from claim from
      ClaimForm claimForm = this.claimFormDAO.getClaimFormById( promotionToDelete.getClaimForm().getId() );
      claimForm.getPromotions().remove( promotionToDelete );

      updateClaimFormStatus( promotionToDelete.getClaimForm().getId() );
    }
    else if ( promotionToDelete.isQuizPromotion() )
    {
      QuizPromotion quizPromotion = (QuizPromotion)promotionToDelete;

      Quiz quiz = this.quizService.getQuizById( quizPromotion.getQuiz().getId() );
      quiz.getPromotions().remove( quizPromotion );

      updateQuizFormStatus( quizPromotion.getQuiz().getId() );
    }
    else if ( promotionToDelete.isNominationPromotion() )
    {
      List<ImportFile> importFiles = importFileDAO.getImportFileByPromotionId( promotionToDelete.getId() );

      for ( ImportFile importFile : importFiles )
      {
        importFileDAO.deleteImportFile( importFile );
      }
    }
  }

  public String getApproverTypeByLevel( Long approvalLevel, Long promotionId )
  {
    ApproverOption approverOption = promotionDAO.getApproverTypeByLevel( approvalLevel, promotionId );
    if ( approverOption != null )
    {
      return approverOption.getApproverType().getCode();
    }
    return null;
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.promotion.PromotionService#getAll()
   * @return List
   */
  public List getAll()
  {
    return promotionDAO.getAll();
  }

  /**
   * Retrieves all the live and expired promotions given type from the database
   *
   * @param promotionType
   * @return List list of Promotions
   */
  public List getAllLiveAndExpiredByType( String promotionType )
  {
    return promotionDAO.getAllLiveAndExpiredByType( promotionType );
  }

  /**
   * Retrieves all the Promotion from the database.
   *
   * @return List a list of Promotions
   */
  public List getAllLive()
  {
    return promotionDAO.getAllLive();
  }

  /**
   * Retrieves all the live promotions of the given type from the database.
   *
   * @return List a list of Promotions
   */
  public List getAllLiveByType( String promotionType )
  {
    return promotionDAO.getAllLiveByType( promotionType );
  }

  /**
   * Retrieves all the live promotions of the given type from the database.
   *
   * @return List a list of Promotions
   */
  public List getAllLiveByTypeAndUserId( String promotionType, Long userId )
  {
    List assignedPromotionList = promotionDAO.getAllLiveByType( promotionType );
    return buildPromotionPaxValueList( userId, assignedPromotionList );
  }

  /**
   * Retrieves all the live promotions from the database with existing Budget.
   *
   * @return List a list of Promotions
   */
  public List getAllLiveWithBudget()
  {
    List promotionsWithBudget = new ArrayList();
    List<Promotion> allLivePromotions = promotionDAO.getAllLive();
    for ( Promotion promotion : allLivePromotions )
    {
      if ( promotion.getBudgetMaster() != null )
      {
        promotionsWithBudget.add( promotion );
      }
    }
    return promotionsWithBudget;
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.promotion.PromotionService#getAllNonExpired()
   * @return List
   */
  public List getAllNonExpired()
  {
    return promotionDAO.getAllNonExpired();
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.promotion.PromotionService#getAllExpired()
   * @return List
   */
  public List getAllExpired()
  {
    return promotionDAO.getAllExpired();
  }

  /**
   * Filters the list of promotions by the promotionType param. Overridden from
   *
   * @see com.biperf.core.service.promotion.PromotionService#getAllExpiredByType(com.biperf.core.domain.enums.PromotionType)
   * @param promotionType
   * @return List
   */
  public List getAllExpiredByType( PromotionType promotionType )
  {

    List promotionListByType = new ArrayList();

    Promotion promotion;

    for ( Iterator promotionListIterator = getAllExpired().iterator(); promotionListIterator.hasNext(); )
    {
      promotion = (Promotion)promotionListIterator.next();
      if ( promotion.getPromotionType().equals( promotionType ) )
      {
        promotionListByType.add( promotion );
      }
    }

    return promotionListByType;
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.promotion.PromotionService#getAllWithSweepstakes()
   * @return List
   */
  public List getAllWithSweepstakes()
  {
    return promotionDAO.getAllWithSweepstakes();
  }

  /**
   * Get all promotions with sweepstakes with associations
   *
   * @param associationRequestCollection
   * @return List
   */
  public List getAllWithSweepstakesWithAssociations( AssociationRequestCollection associationRequestCollection )
  {
    return promotionDAO.getAllWithSweepstakesWithAssociations( associationRequestCollection );
  }

  /**
   * Get the validation for the claimFormStepElement by the id param. Overridden from
   *
   * @see com.biperf.core.service.promotion.PromotionService#getPromotionClaimFormStepElementValidationById(java.lang.Long)
   * @param id
   * @return PromotionClaimFormStepElementValidation
   */
  public PromotionClaimFormStepElementValidation getPromotionClaimFormStepElementValidationById( Long id )
  {
    return this.promotionDAO.getPromotionClaimFormStepElementValidationById( id );
  }

  /**
   * Save the validation for the promotion's claimFormStepElement Overridden from
   *
   * @see com.biperf.core.service.promotion.PromotionService#savePromotionClaimFormStepElementValidation(com.biperf.core.domain.promotion.PromotionClaimFormStepElementValidation)
   * @param promotionClaimFormStepElementValidation
   * @return PromotionClaimFormStepElementValidation
   */
  public PromotionClaimFormStepElementValidation savePromotionClaimFormStepElementValidation( PromotionClaimFormStepElementValidation promotionClaimFormStepElementValidation )
  {
    return this.promotionDAO.savePromotionClaimFormStepElementValidation( promotionClaimFormStepElementValidation );
  }

  /**
   * Get a list of all ClaimFormStepElement validations for the Promotion and ClaimFormStep params.
   * Overridden from
   *
   * @see com.biperf.core.service.promotion.PromotionService#getAllPromotionClaimFormStepElementValidations(com.biperf.core.domain.promotion.Promotion,
   *      com.biperf.core.domain.claim.ClaimFormStep)
   * @param promotion
   * @param claimFormStep
   * @return List
   */
  public List getAllPromotionClaimFormStepElementValidations( Promotion promotion, ClaimFormStep claimFormStep )
  {
    return this.promotionDAO.getAllPromotionClaimFormStepElementValidations( promotion, claimFormStep );
  }

  public void expirePromotions()
  {
    List promotionList = this.promotionDAO.getAllLive();
    Iterator promotionListIter = promotionList.iterator();

    while ( promotionListIter.hasNext() )
    {
      Promotion promotion = (Promotion)promotionListIter.next();
      // To fix the bug 21256
      Date todaysDate = new Date();

      if ( promotion.isBadgePromotion() )
      {
        boolean expireBadge = false;
        List<Long> assignedPromotionIds = promotionDAO.getEligiblePromotionsFromPromoBadgeId( promotion.getId() );
        if ( !assignedPromotionIds.isEmpty() )
        {
          Date badgePromoExpDate = new Date();
          for ( Long promoId : assignedPromotionIds )
          {
            Promotion promo = promotionDAO.getPromotionById( promoId );
            if ( promo.getSubmissionEndDate() == null )
            {
              expireBadge = false;
              break;
            }
            else
            {
              badgePromoExpDate = org.apache.commons.lang3.time.DateUtils.addDays( promo.getSubmissionEndDate(),
                                                                                   ( (Badge)promotion ).getDisplayEndDays() != null ? ( (Badge)promotion ).getDisplayEndDays().intValue() : 0 );
            }
            if ( !org.apache.commons.lang3.time.DateUtils.isSameDay( todaysDate, badgePromoExpDate ) && badgePromoExpDate.before( DateUtils.toDate( DateUtils.toDisplayString( new Date() ) ) ) )
            {
              expireBadge = true;
            }
          }
        }
        if ( expireBadge )
        {
          promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) );
          ( (Badge)promotion ).setStatus( Badge.BADGE_INACTIVE );
          promotionDAO.save( promotion );
        }
      }
      else
      {
        if ( promotion.getSubmissionEndDate() != null && !org.apache.commons.lang3.time.DateUtils.isSameDay( todaysDate, promotion.getSubmissionEndDate() )
            && promotion.getSubmissionEndDate().before( DateUtils.toDate( DateUtils.toDisplayString( new Date() ) ) ) )
        {
          promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) );
          // Bug fix 36166 - Start
          if ( promotion.isWebRulesActive() && promotion.getWebRulesEndDate() == null )
          {
            promotion.setWebRulesEndDate( promotion.getSubmissionEndDate() );
          }
          // Bug fix 36166 - End
          promotionDAO.save( promotion );
        }
      }
    }
    // kick off alert to hot-swap each instances eligiblePromotion list
    String processBeanName = "refreshEligiblePromotionsCacheProcess";
    Process process = getProcessService().getProcessByBeanName( processBeanName );
    if ( process != null && process.getProcessLastExecutedDate() != null )
    {
      ProcessSchedule processSchedule = new ProcessSchedule();
      processSchedule.setStartDate( new Date() );
      processSchedule.setTimeOfDayMillis( new Long( 0 ) );
      processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.ONE_TIME_ONLY ) );
      getProcessService().scheduleProcess( process, processSchedule, null, UserManager.getUserId() );
    }
    // todo, kick off the cache refresh
    audienceService.clearPromoEligibilityCache();
  }

  public void launchPromotions()
  {
    List promotionList = this.promotionDAO.getAll();
    if ( promotionList != null && promotionList.size() > 0 )
    {
      Iterator promotionListIter = promotionList.iterator();
      while ( promotionListIter.hasNext() )
      {
        Promotion promotion = (Promotion)promotionListIter.next();
        if ( promotion.getPromotionStatus().equals( PromotionStatusType.lookup( PromotionStatusType.COMPLETE ) ) )

        {
          Date todaysDate = new Date();
          if ( promotion.getSubmissionStartDate() != null && ( org.apache.commons.lang3.time.DateUtils.isSameDay( todaysDate, promotion.getSubmissionStartDate() )
              || promotion.getSubmissionStartDate().before( DateUtils.toDate( DateUtils.toDisplayString( new Date() ) ) ) ) )
          {
            promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.LIVE ) );
            promotionDAO.save( promotion );
          }

        }
      }
    }
  }

  /**
   * Saves the data into Content Manager based on the promotion argument's cmAssetCode and
   * nameCmKey. If a cmAssetCode or nameCmKey do not exist, then one will be created and set on the
   * promotion instance.
   *
   * @param promotion
   * @param data
   * @return Promotion
   * @throws ServiceErrorException
   */
  public Promotion saveWebRulesCmText( Promotion promotion, String data ) throws ServiceErrorException
  {
    return saveWebRulesCmText( promotion, data, ContentReaderManager.getCurrentLocale() );
  }

  public Promotion saveWebRulesCmText( Promotion promotion, String data, Locale locale ) throws ServiceErrorException
  {

    try
    {
      if ( promotion.getCmAssetCode() == null )
      {
        // Create Unique Asset
        String newAssetName = cmAssetService.getUniqueAssetCode( Promotion.CM_PROMOTION_DATA_WEBRULES_ASSET_PREFIX );
        promotion.setCmAssetCode( newAssetName );
      }
      promotion.setWebRulesCmKey( Promotion.CM_PROMOTION_DATA_WEBRULES_TEXT_KEY_PREFIX );
      CMDataElement cmDataElement = new CMDataElement( "Web Rules Name", promotion.getWebRulesCmKey(), data, false, DataTypeEnum.HTML );
      List elements = new ArrayList();
      elements.add( cmDataElement );

      cmAssetService.createOrUpdateAsset( Promotion.CM_PROMOTION_DATA_SECTION,
                                          PROMOTION_ASSET_TYPE_NAME,
                                          Promotion.CM_PROMOTION_DATA_WEBRULES_TEXT_KEY_DESC,
                                          promotion.getCmAssetCode(),
                                          elements,
                                          locale,
                                          null );
    }
    catch( ServiceErrorException e )
    {
      List errors = new ArrayList();
      errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
      throw e;
    }

    return promotion;
  }

  public Promotion saveWebRulesManagerCmText( Promotion promotion, String data ) throws ServiceErrorException
  {
    return saveWebRulesManagerCmText( promotion, data, ContentReaderManager.getCurrentLocale() );
  }

  public Promotion saveWebRulesManagerCmText( Promotion promotion, String data, Locale locale ) throws ServiceErrorException
  {
    GoalQuestPromotion gqPromo = (GoalQuestPromotion)promotion;

    try
    {
      if ( gqPromo.getManagerCmAssetCode() == null )
      {
        // Create Unique Asset
        String newAssetName = cmAssetService.getUniqueAssetCode( Promotion.CM_PROMOTION_DATA_WEBRULES_ASSET_PREFIX );
        gqPromo.setManagerCmAssetCode( newAssetName );
      }
      gqPromo.setManagerWebRulesCmKey( Promotion.CM_PROMOTION_DATA_WEBRULES_TEXT_KEY_PREFIX );
      CMDataElement cmDataElement = new CMDataElement( "Web Rules Name", gqPromo.getManagerWebRulesCmKey(), data, false, DataTypeEnum.HTML );
      List elements = new ArrayList();
      elements.add( cmDataElement );

      cmAssetService.createOrUpdateAsset( Promotion.CM_PROMOTION_DATA_SECTION,
                                          PROMOTION_ASSET_TYPE_NAME,
                                          Promotion.CM_PROMOTION_DATA_WEBRULES_TEXT_KEY_DESC,
                                          gqPromo.getManagerCmAssetCode(),
                                          elements,
                                          locale,
                                          null );
    }
    catch( ServiceErrorException e )
    {
      List errors = new ArrayList();
      errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
      throw e;
    }

    return promotion;
  }

  public Promotion saveWebRulesPartnerCmText( Promotion promotion, String data ) throws ServiceErrorException
  {
    return saveWebRulesPartnerCmText( promotion, data, ContentReaderManager.getCurrentLocale() );
  }

  public Promotion saveWebRulesPartnerCmText( Promotion promotion, String data, Locale locale ) throws ServiceErrorException
  {

    GoalQuestPromotion gqPromo = (GoalQuestPromotion)promotion;

    try
    {
      if ( gqPromo.getPartnerCmAssetCode() == null )
      {
        // Create Unique Asset
        String newAssetName = cmAssetService.getUniqueAssetCode( Promotion.CM_PROMOTION_DATA_WEBRULES_ASSET_PREFIX );
        gqPromo.setPartnerCmAssetCode( newAssetName );
      }
      gqPromo.setPartnerWebRulesCmKey( Promotion.CM_PROMOTION_DATA_WEBRULES_TEXT_KEY_PREFIX );
      CMDataElement cmDataElement = new CMDataElement( "Web Rules Name", gqPromo.getPartnerWebRulesCmKey(), data, false, DataTypeEnum.HTML );
      List elements = new ArrayList();
      elements.add( cmDataElement );

      cmAssetService.createOrUpdateAsset( Promotion.CM_PROMOTION_DATA_SECTION,
                                          PROMOTION_ASSET_TYPE_NAME,
                                          Promotion.CM_PROMOTION_DATA_WEBRULES_TEXT_KEY_DESC,
                                          gqPromo.getPartnerCmAssetCode(),
                                          elements,
                                          locale,
                                          null );
    }
    catch( ServiceErrorException e )
    {
      List errors = new ArrayList();
      errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
      throw e;
    }

    return promotion;
  }

  public Promotion saveDivisionNamesInCM( ThrowdownPromotion promotion, Division division ) throws ServiceErrorException
  {
    return saveDivisionNamesInCM( promotion, division, ContentReaderManager.getCurrentLocale() );
  }

  public Promotion saveDivisionNamesInCM( ThrowdownPromotion promotion, Division division, Locale locale ) throws ServiceErrorException
  {
    try
    {
      if ( StringUtils.isEmpty( division.getDivisionNameAssetCode() ) )
      {
        // Create Unique Asset
        String newDivisionNameAssetName = cmAssetService.getUniqueAssetCode( Division.DIVISION_CM_ASSET_PREFIX );
        division.setDivisionNameAssetCode( newDivisionNameAssetName );
      }

      CMDataElement cmDataElement = new CMDataElement( "Division Name", Division.DIVISION_NAME_KEY_PREFIX, division.getDivisionNameForCM(), false, DataTypeEnum.HTML );
      List elements = new ArrayList();
      elements.add( cmDataElement );

      cmAssetService.createOrUpdateAsset( Division.DIVISION_NAME_SECTION_CODE,
                                          Division.DIVISION_NAME_CM_ASSET_TYPE_NAME,
                                          Division.DIVISION_NAME_KEY_DESC,
                                          division.getDivisionNameAssetCode(),
                                          elements,
                                          locale,
                                          null );
    }
    catch( ServiceErrorException e )
    {
      List errors = new ArrayList();
      errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
      throw e;
    }
    return promotion;

  }

  // Bug # 38684 End//

  public Promotion savePromotionOverviewCmText( Promotion promotion, String overviewDetailsText ) throws ServiceErrorException
  {
    return savePromotionOverviewCmText( promotion, overviewDetailsText, ContentReaderManager.getCurrentLocale() );
  }

  public Promotion savePromotionOverviewCmText( Promotion promotion, String overviewDetailsText, Locale locale ) throws ServiceErrorException
  {

    try
    {
      if ( StringUtils.isEmpty( promotion.getOverview() ) )
      {
        // Create Unique Asset
        String newOVerviewAssetName = cmAssetService.getUniqueAssetCode( Promotion.PROMO_OVERVIEW_CM_ASSET_PREFIX );
        promotion.setOverview( newOVerviewAssetName );
      }
      CMDataElement cmDataElement = new CMDataElement( "Save Promotion Overview ", Promotion.PROMO_OVERVIEW_CM_ASSET_TYPE_KEY, overviewDetailsText, false, DataTypeEnum.HTML );
      List elements = new ArrayList();
      elements.add( cmDataElement );
      cmAssetService.createOrUpdateAsset( Promotion.PROMO_OVERVIEW_SECTION_CODE,
                                          Promotion.PROMO_OVERVIEW_CM_ASSET_TYPE_NAME,
                                          Promotion.PROMO_OVERVIEW_TEXT_KEY_DESC,
                                          promotion.getOverview(),
                                          elements,
                                          locale,
                                          null );
    }
    catch( ServiceErrorException e )
    {
      List errors = new ArrayList();
      errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
      throw e;
    }

    return promotion;
  }

  public Promotion savePromotionCelebrationCmText( Promotion promotion, String celebrationDetailsText ) throws ServiceErrorException
  {
    return savePromotionCelebrationCmText( promotion, celebrationDetailsText, ContentReaderManager.getCurrentLocale() );
  }

  public Promotion savePromotionCelebrationCmText( Promotion promotion, String celebrationDetailsText, Locale locale ) throws ServiceErrorException
  {
    RecognitionPromotion recPromo = (RecognitionPromotion)promotion;
    try
    {
      if ( StringUtils.isEmpty( recPromo.getDefaultMessage() ) || recPromo.getDefaultMessage() == null )
      {
        // Create Unique Asset
        String newCelebrationsAssetName = cmAssetService.getUniqueAssetCode( Promotion.RECOGNITION_CELEBRATIONS_MESSAGE_CM_ASSET_PREFIX );
        recPromo.setDefaultMessage( newCelebrationsAssetName );
      }
      CMDataElement cmDataElement = new CMDataElement( "Save Promotion Celebrations ", Promotion.RECOGNITION_CELEBRATIONS_MESSAGE_CM_ASSET_TYPE_KEY, celebrationDetailsText, false, DataTypeEnum.HTML );
      List elements = new ArrayList();
      elements.add( cmDataElement );
      cmAssetService.createOrUpdateAsset( Promotion.RECOGNITION_CELEBRATIONS_MESSAGE_SECTION_CODE,
                                          Promotion.RECOGNITION_CELEBRATIONS_MESSAGE_CM_ASSET_TYPE_NAME,
                                          Promotion.RECOGNITION_CELEBRATIONS_MESSAGE_TEXT_KEY_DESC,
                                          recPromo.getDefaultMessage(),
                                          elements,
                                          locale,
                                          null );
    }
    catch( ServiceErrorException e )
    {
      List errors = new ArrayList();
      errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
      throw e;
    }

    return recPromo;
  }

  public Promotion saveGoalNameAndDescriptionInCM( Promotion promotion, List<AbstractGoalLevel> goalLevelsFromAdmin ) throws ServiceErrorException
  {
    return saveGoalNameAndDescriptionInCM( promotion, goalLevelsFromAdmin, ContentReaderManager.getCurrentLocale() );
  }

  public Promotion saveGoalNameAndDescriptionInCM( Promotion promotion, List<AbstractGoalLevel> goalLevelsFromAdmin, Locale locale ) throws ServiceErrorException
  {
    GoalQuestPromotion goalQuestPromotion = (GoalQuestPromotion)promotion; // promotion From DB

    GoalLevel attachedGoalLevel = null;
    try
    {
      Iterator attachedIter = goalQuestPromotion.getGoalLevels().iterator();
      for ( Iterator<AbstractGoalLevel> goalLevelFromAdminIter = goalLevelsFromAdmin.iterator(); goalLevelFromAdminIter.hasNext(); )
      {
        GoalLevel goalLevelFromAdmin = (GoalLevel)goalLevelFromAdminIter.next();
        if ( goalLevelFromAdmin.getGoalLevelcmAssetCode() == null || goalLevelFromAdmin.getGoalLevelcmAssetCode().isEmpty() )
        {
          // Create Unique Asset
          String newAssetName = cmAssetService.getUniqueAssetCode( Promotion.CM_GOAL_DESCRIPTION_ASSET_PREFIX );
          goalLevelFromAdmin.setGoalLevelcmAssetCode( newAssetName );
        }
        if ( StringUtils.isNotBlank( goalLevelFromAdmin.getGoalLevelNameKey() ) && StringUtils.isNotBlank( goalLevelFromAdmin.getGoalLevelDescriptionKey() ) )
        {
          String goalLevelName = goalLevelFromAdmin.getGoalLevelNameKey();
          String goalLevelDescription = goalLevelFromAdmin.getGoalLevelDescriptionKey();

          while ( attachedIter.hasNext() )
          {
            attachedGoalLevel = (GoalLevel)attachedIter.next();
            if ( goalLevelFromAdmin.getId() != null && goalLevelFromAdmin.getId().longValue() != 0 )
            {
              if ( goalLevelFromAdmin.getId().equals( attachedGoalLevel.getId() ) )
              {
                attachedGoalLevel.setGoalLevelcmAssetCode( goalLevelFromAdmin.getGoalLevelcmAssetCode() );
                attachedGoalLevel.setGoalLevelNameKey( Promotion.CM_GOALS_KEY );
                attachedGoalLevel.setGoalLevelDescriptionKey( Promotion.CM_GOAL_DESCRIPTION_KEY );

                CMDataElement cmDataElementGoal = new CMDataElement( Promotion.CM_GOALS_KEY_DESC, Promotion.CM_GOALS_KEY, goalLevelName, false );
                CMDataElement cmDataElementGoalDescription = new CMDataElement( Promotion.CM_GOAL_DESCRIPTION_KEY_DESC, Promotion.CM_GOAL_DESCRIPTION_KEY, goalLevelDescription, false );
                List elementList = new ArrayList();
                elementList.add( cmDataElementGoal );
                elementList.add( cmDataElementGoalDescription );

                cmAssetService.createOrUpdateAsset( Promotion.CM_GOAL_DESCRIPTION_SECTION,
                                                    Promotion.CM_GOAL_DESCRIPTION_ASSET_TYPE,
                                                    Promotion.CM_GOALS_KEY_DESC,
                                                    attachedGoalLevel.getGoalLevelcmAssetCode(),
                                                    elementList,
                                                    locale,
                                                    null );
                break;
              }

            }
            else
            {
              attachedGoalLevel.setGoalLevelcmAssetCode( cmAssetService.getUniqueAssetCode( Promotion.CM_GOAL_DESCRIPTION_ASSET_PREFIX ) );
              attachedGoalLevel.setGoalLevelNameKey( Promotion.CM_GOALS_KEY );

              attachedGoalLevel.setGoalLevelDescriptionKey( Promotion.CM_GOAL_DESCRIPTION_KEY );

              CMDataElement cmDataElementGoal = new CMDataElement( Promotion.CM_GOALS_KEY_DESC, Promotion.CM_GOALS_KEY, goalLevelName, false );
              CMDataElement cmDataElementGoalDescription = new CMDataElement( Promotion.CM_GOAL_DESCRIPTION_KEY_DESC, Promotion.CM_GOAL_DESCRIPTION_KEY, goalLevelDescription, false );
              List elementList = new ArrayList();
              elementList.add( cmDataElementGoal );
              elementList.add( cmDataElementGoalDescription );

              cmAssetService.createOrUpdateAsset( Promotion.CM_GOAL_DESCRIPTION_SECTION,
                                                  Promotion.CM_GOAL_DESCRIPTION_ASSET_TYPE,
                                                  Promotion.CM_GOALS_KEY_DESC,
                                                  attachedGoalLevel.getGoalLevelcmAssetCode(),
                                                  elementList,
                                                  locale,
                                                  null );
              break;
            }
          }
        }
      }
    }
    catch( ServiceErrorException e )
    {
      List errors = new ArrayList();
      errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
      throw e;
    }
    return goalQuestPromotion;
  }

  public Promotion savePayoutStrutureBaseUnitInCM( Promotion promotion, String baseUnit ) throws ServiceErrorException
  {
    return savePayoutStrutureBaseUnitInCM( promotion, baseUnit, ContentReaderManager.getCurrentLocale() );
  }

  public Promotion savePayoutStrutureBaseUnitInCM( Promotion promotion, String baseUnit, Locale locale ) throws ServiceErrorException
  {
    GoalQuestPromotion goalQuestPromotion = (GoalQuestPromotion)promotion;
    try
    {
      String newBaseUnitAssetName = null;
      if ( StringUtils.isEmpty( goalQuestPromotion.getBaseUnit() ) )
      {
        // Create Unique Asset
        newBaseUnitAssetName = cmAssetService.getUniqueAssetCode( Promotion.GQ_CP_PROMO_BASE_UNIT_ASSET_PREFIX );
        goalQuestPromotion.setBaseUnit( newBaseUnitAssetName );
      }

      CMDataElement cmDataElement = new CMDataElement( "Goal Quest and Challenge Point BaseUnit", Promotion.GQ_CP_PROMO_BASE_UNIT_KEY_PREFIX, baseUnit, false, DataTypeEnum.HTML );
      List elements = new ArrayList();
      elements.add( cmDataElement );

      if ( newBaseUnitAssetName == null )
      {
        newBaseUnitAssetName = goalQuestPromotion.getBaseUnit();
      }

      cmAssetService.createOrUpdateAsset( Promotion.GQ_CP_PROMO_BASE_UNIT_SECTION_CODE,
                                          Promotion.GQ_CP_PROMO_BASE_UNIT_ASSET_TYPE_NAME,
                                          Promotion.GQ_CP_PROMO_BASE_UNIT_KEY_DESC,
                                          newBaseUnitAssetName,
                                          elements,
                                          locale,
                                          null );
    }
    catch( ServiceErrorException e )
    {
      List errors = new ArrayList();
      errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
      throw e;
    }

    return promotion;
  }

  public Promotion savePayoutStrutureBaseUnitInCM( ThrowdownPromotion promotion, String baseUnit ) throws ServiceErrorException
  {
    return savePayoutStrutureBaseUnitInCM( promotion, baseUnit, ContentReaderManager.getCurrentLocale() );
  }

  public Promotion savePayoutStrutureBaseUnitInCM( ThrowdownPromotion promotion, String baseUnit, Locale locale ) throws ServiceErrorException
  {
    // better to have different CM structure for TD promotions rather than using CP/GQ promotions
    // structure sint it will not cause issues
    // in upgrade for existing promotions
    try
    {
      String newBaseUnitAssetName = null;
      if ( StringUtils.isEmpty( promotion.getBaseUnit() ) )
      {
        // Create Unique Asset
        newBaseUnitAssetName = cmAssetService.getUniqueAssetCode( Promotion.TD_PROMO_BASE_UNIT_ASSET_PREFIX );
        promotion.setBaseUnit( newBaseUnitAssetName );
      }

      CMDataElement cmDataElement = new CMDataElement( "Throwdown BaseUnit", Promotion.TD_PROMO_BASE_UNIT_KEY_PREFIX, baseUnit, false, DataTypeEnum.HTML );
      List elements = new ArrayList();
      elements.add( cmDataElement );

      if ( newBaseUnitAssetName == null )
      {
        newBaseUnitAssetName = promotion.getBaseUnit();
      }

      cmAssetService.createOrUpdateAsset( Promotion.TD_PROMO_BASE_UNIT_SECTION_CODE,
                                          Promotion.TD_PROMO_BASE_UNIT_ASSET_TYPE_NAME,
                                          Promotion.TD_PROMO_BASE_UNIT_KEY_DESC,
                                          newBaseUnitAssetName,
                                          elements,
                                          locale,
                                          null );
    }
    catch( ServiceErrorException e )
    {
      List errors = new ArrayList();
      errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
      throw e;
    }

    return promotion;
  }

  public Promotion saveQuizDetailsCmText( Promotion promotion, String quizDetails ) throws ServiceErrorException
  {
    return saveQuizDetailsCmText( promotion, quizDetails, ContentReaderManager.getCurrentLocale() );
  }

  public Promotion saveQuizDetailsCmText( Promotion promotion, String quizDetails, Locale locale ) throws ServiceErrorException
  {
    QuizPromotion quizPromotion = (QuizPromotion)promotion;
    try
    {
      if ( StringUtils.isEmpty( quizPromotion.getQuizDetails() ) )
      {
        // Create Unique Asset
        String newQuizDetailsAssetName = cmAssetService.getUniqueAssetCode( Promotion.QUIZ_PROMOTION_DETAILS_CM_ASSET_PREFIX );
        quizPromotion.setQuizDetails( newQuizDetailsAssetName );
      }
      CMDataElement cmDataElement = new CMDataElement( "Save Quiz Promotion Details", Promotion.QUIZ_PROMOTION_DETAILS_CM_ASSET_TYPE_KEY, quizDetails, false, DataTypeEnum.HTML );
      List elements = new ArrayList();
      elements.add( cmDataElement );
      cmAssetService.createOrUpdateAsset( Promotion.QUIZ_PROMOTION_DETAILS_SECTION_CODE,
                                          Promotion.QUIZ_PROMOTION_DETAILS_CM_ASSET_NAME_TYPE,
                                          Promotion.QUIZ_PROMOTION_DETAILS_TEXT,
                                          quizPromotion.getQuizDetails(),
                                          elements,
                                          locale,
                                          null );
    }
    catch( ServiceErrorException e )
    {
      List errors = new ArrayList();
      errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
      throw e;
    }

    return promotion;
  }

  /**
   * Saves the data into Content Manager based on the promotion argument's promoNameAssetCode. If a
   * promoNameAssetCode does not exist, then one will be created and set on the promotion instance.
   *
   * @param promotion
   * @param data
   * @return Promotion
   * @throws ServiceErrorException
   */
  public Promotion savePromoNameCmText( Promotion promotion, String data ) throws ServiceErrorException, UniqueConstraintViolationException
  {
    return savePromoNameCmText( promotion, data, ContentReaderManager.getCurrentLocale(), false );
  }

  /**
   * Saves the data into Content Manager based on the locale and promotion argument's promoNameAssetCode. If a
   * promoNameAssetCode does not exist, then one will be created and set on the promotion instance.
   *
   * @param promotion
   * @param data
   * @param locale
   * @param flag
   * @return Promotion
   * @throws ServiceErrorException
   */
  public Promotion savePromoNameCmText( Promotion promotion, String data, Locale locale, boolean flag ) throws ServiceErrorException, UniqueConstraintViolationException
  {

    // Check for uniqueness of promotion_name.
    // this flag is used to skip UniqueConstraint when this method is invoked from
    // PromotionTranslationAction.
    if ( !flag )
    {
      boolean isPromotionNameUnique = promotionDAO.isPromotionNameUnique( data, promotion.getId() );
      if ( !isPromotionNameUnique )
      {
        throw new UniqueConstraintViolationException();
      }
    }
    try
    {
      String newPromoNameAssetName = null;
      if ( promotion.getPromoNameAssetCode() == null )
      {
        // Create Unique Asset
        newPromoNameAssetName = cmAssetService.getUniqueAssetCode( Promotion.PROMOTION_NAME_ASSET_PREFIX );
        promotion.setPromoNameAssetCode( newPromoNameAssetName );
      }

      CMDataElement cmDataElement = new CMDataElement( "Promotion Name", Promotion.PROMOTION_NAME_KEY_PREFIX, data, false, DataTypeEnum.HTML );
      List elements = new ArrayList();
      elements.add( cmDataElement );

      if ( newPromoNameAssetName == null )
      {
        newPromoNameAssetName = promotion.getPromoNameAssetCode();
      }

      cmAssetService.createOrUpdateAsset( Promotion.PROMOTION_NAME_SECTION_CODE,
                                          Promotion.PROMOTION_NAME_ASSET_TYPE_NAME,
                                          Promotion.PROMOTION_NAME_KEY_DESC,
                                          newPromoNameAssetName,
                                          elements,
                                          locale,
                                          null );
    }
    catch( ServiceErrorException e )
    {
      List errors = new ArrayList();
      errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
      throw e;
    }

    return promotion;
  }

  public Promotion savePromoObjectivieCmText( GoalQuestPromotion promotion, String data ) throws ServiceErrorException
  {
    return savePromoObjectivieCmText( promotion, data, ContentReaderManager.getCurrentLocale() );
  }

  public Promotion savePromoObjectivieCmText( GoalQuestPromotion promotion, String data, Locale locale ) throws ServiceErrorException
  {
    try
    {
      String newObjectiveAssetName = null;
      if ( promotion.getId() == null && promotion.getObjectiveAssetCode() == null )
      {
        // Create Unique Asset
        newObjectiveAssetName = cmAssetService.getUniqueAssetCode( Promotion.GQ_CP_PROMO_OBJECTIVE_ASSET_PREFIX );
        promotion.setObjectiveAssetCode( newObjectiveAssetName );
      }

      CMDataElement cmDataElement = new CMDataElement( "Goal Quest Promotion Obejctive", Promotion.GQ_CP_PROMO_OBJECTIVE_KEY_PREFIX, data, false, DataTypeEnum.HTML );
      List elements = new ArrayList();
      elements.add( cmDataElement );

      if ( newObjectiveAssetName == null )
      {
        newObjectiveAssetName = promotion.getObjectiveAssetCode();
      }

      cmAssetService.createOrUpdateAsset( Promotion.GQ_CP_PROMO_OBJECTIVE_SECTION_CODE,
                                          Promotion.GQ_CP_PROMO_OBJECTIVE_ASSET_TYPE_NAME,
                                          Promotion.GQ_CP_PROMO_OBJECTIVE_KEY_DESC,
                                          newObjectiveAssetName,
                                          elements,
                                          locale,
                                          null );
    }
    catch( ServiceErrorException e )
    {
      List errors = new ArrayList();
      errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
      throw e;
    }

    return promotion;
  }

  /**
   * Get all ClaimFormStepElement validations for the promotionId param. Overridden from
   *
   * @see com.biperf.core.service.promotion.PromotionService#getAllPromotionClaimFormStepElementValidations(java.lang.Long)
   * @param promotionId
   * @return List
   */
  public List getAllPromotionClaimFormStepElementValidations( Long promotionId )
  {

    // Prepare the list of validations.
    List promotionClaimFormStepElementValidations = new ArrayList();

    // Get the promotion.
    Promotion promotion = this.promotionDAO.getPromotionById( promotionId );

    // Iterate over the claimFormSteps
    Iterator claimFormStepListIterator = promotion.getClaimForm().getClaimFormSteps().iterator();

    while ( claimFormStepListIterator.hasNext() )
    {
      ClaimFormStep claimFormStep = (ClaimFormStep)claimFormStepListIterator.next();

      List validationList = this.promotionDAO.getAllPromotionClaimFormStepElementValidations( promotion, claimFormStep );

      Iterator validationListIter = validationList.iterator();

      while ( validationListIter.hasNext() )
      {
        // Add the validations for the promotion and claimFormStep to one list.
        promotionClaimFormStepElementValidations.add( validationListIter.next() );
      }

    }

    return promotionClaimFormStepElementValidations;

  }

  /**
   * Save the list of validations the promotion's claimFormStepElement. Overridden from
   *
   * @see com.biperf.core.service.promotion.PromotionService#savePromotionClaimFormStepElementValidation(com.biperf.core.domain.promotion.PromotionClaimFormStepElementValidation)
   * @param promotionClaimFormStepElementValidationList
   * @return List
   */
  public List savePromotionClaimFormStepElementValidationList( List promotionClaimFormStepElementValidationList )
  {

    List savedValidationList = new ArrayList();

    for ( Iterator validationIter = promotionClaimFormStepElementValidationList.iterator(); validationIter.hasNext(); )
    {

      PromotionClaimFormStepElementValidation pcfsev = (PromotionClaimFormStepElementValidation)validationIter.next();

      pcfsev.setPromotion( promotionDAO.getPromotionById( pcfsev.getPromotion().getId() ) );
      pcfsev.setClaimFormStepElement( claimFormDefinitionService.getClaimFormStepElementById( pcfsev.getClaimFormStepElement().getId() ) );
      savedValidationList.add( this.promotionDAO.savePromotionClaimFormStepElementValidation( pcfsev ) );
    }

    return savedValidationList;
  }

  /**
   * Update the list of validations on the promotion. Overridden from
   *
   * @see com.biperf.core.service.promotion.PromotionService#updatePromotionClaimFormElementValidations(java.lang.Long,
   *      java.util.List)
   * @param promotionId
   * @param promotionClaimFormStepElementValidationList
   * @return List
   */
  public List updatePromotionClaimFormElementValidations( Long promotionId, List promotionClaimFormStepElementValidationList )
  {

    // Delete the existing list
    Iterator validationListIterator = this.getAllPromotionClaimFormStepElementValidations( promotionId ).iterator();

    while ( validationListIterator.hasNext() )
    {
      PromotionClaimFormStepElementValidation pcfsev = (PromotionClaimFormStepElementValidation)validationListIterator.next();

      this.promotionDAO.deletePromotionClaimFormStepElementValidation( pcfsev );

      HibernateSessionManager.getSession().flush();
      HibernateSessionManager.getSession().clear();
    }

    return this.savePromotionClaimFormStepElementValidationList( promotionClaimFormStepElementValidationList );
  }

  /**
   * Update the status for the claimForm with the claimFormId param.
   *
   * @param claimFormId
   */
  private void updateClaimFormStatus( Long claimFormId )
  {
    claimFormDefinitionService.updateClaimFormStatus( claimFormId );
  }

  /**
   * Update the status for the quizForm with the quizFormId param.
   *
   * @param quizFormId
   */
  private void updateQuizFormStatus( Long quizFormId )
  {
    quizService.updateQuizFormStatus( quizFormId );
  }

  /**
   * Update the status for the calculator with the calculatorId param.
   *
   * @param calculatorId
   */
  private void updateCalculatorStatus( Long calculatorId )
  {
    calculatorService.updateCalculatorStatus( calculatorId );
  }

  /**
   * Update the status for the surveyForm with the surveyFormId param.
   *
   * @param quizFormId
   */
  private void updateSurveyFormStatus( Long surveyFormId )
  {
    surveyService.updateSurveyFormStatus( surveyFormId );
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.promotion.PromotionService#copyPromotion(java.lang.Long,
   *      java.lang.String, java.util.List)
   * @param originalPromotionId
   * @param newPromotionName
   * @param newChildPromotionNameHolders
   * @return Promotion (The copied Promotion)
   * @throws UniqueConstraintViolationException
   * @throws ServiceErrorException
   * @throws BeaconRuntimeException
   */
  public Promotion copyPromotion( Long originalPromotionId, String newPromotionName, List newChildPromotionNameHolders )
      throws UniqueConstraintViolationException, ServiceErrorException, BeaconRuntimeException
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.ALL_EXCLUDE_JOURNAL ) );
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.WEB_RULES_AUDIENCES ) );
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.WEB_RULES_MANAGERS ) );
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.WEB_RULES_PARTNERS ) );
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PARTNER_AUDIENCES ) );

    Promotion originalPromotion = promotionDAO.getPromotionByIdWithAssociations( originalPromotionId, associationRequestCollection );
    if ( originalPromotion == null )
    {
      return null;
    }

    Promotion copiedPromotion = null;

    try
    {
      if ( newChildPromotionNameHolders == null )
      {
        copiedPromotion = (Promotion)originalPromotion.deepCopy( false, newPromotionName, null );
      }
      else
      {
        copiedPromotion = (Promotion)originalPromotion.deepCopy( true, newPromotionName, newChildPromotionNameHolders );
      }
      // BugFix 18058 Dont copy the Issue_Awars_Run_Date for GQ Promotions.
      if ( copiedPromotion.isGoalQuestPromotion() )
      {
        GoalQuestPromotion copiedGQPromotion = (GoalQuestPromotion)copiedPromotion;
        copiedGQPromotion.setIssueAwardsRun( false );
        copiedGQPromotion.setIssueAwardsRunDate( null );
      }
      else if ( copiedPromotion.isRecognitionPromotion() )
      {
        RecognitionPromotion copiedRecognitionPromotion = (RecognitionPromotion)copiedPromotion;
        // setting budget sweep enabled to false to avoid issue while promotion is marked complete
        copiedRecognitionPromotion.setBudgetSweepEnabled( false );
        copiedRecognitionPromotion.setPromotionBudgetSweeps( null );
        if ( copiedRecognitionPromotion.isIncludeCelebrations() && copiedRecognitionPromotion.isAllowDefaultMessage() )
        {
          copiedRecognitionPromotion.setDefaultMessage( null );
        }
      }
      // BugFix 21513 Dont copy the Issue_Awars_Run_Date for CP Promotions.
      else if ( copiedPromotion.isChallengePointPromotion() )
      {
        ChallengePointPromotion cpPromotion = (ChallengePointPromotion)copiedPromotion;
        cpPromotion.setIssueAwardsRun( false );
        cpPromotion.setIssueAwardsRunDate( null );
        copiedPromotion = cpPromotion;

      }
    }
    catch( CloneNotSupportedException cnse )
    {
      throw new ServiceErrorException( "Promotion (id: " + originalPromotion.getId() + ") failed cloning", cnse );
    }
    // Create new promoNameAssetCode for child promotion
    int childCounter = 0;
    if ( newChildPromotionNameHolders != null )
    {
      if ( copiedPromotion.isProductClaimPromotion() )
      {
        ProductClaimPromotion pcPromotion = (ProductClaimPromotion)copiedPromotion;

        if ( pcPromotion.getChildPromotionCount() > 0 )
        {
          Iterator iter = pcPromotion.getChildPromotions().iterator();
          while ( iter.hasNext() )
          {
            Promotion child = (Promotion)iter.next();
            PromotionCopyHolder pch = (PromotionCopyHolder)newChildPromotionNameHolders.get( childCounter );
            child.setPromoNameAssetCode( null );
            savePromoNameCmText( child, pch.getNewName() );
            childCounter++;
          }
        }
      }
    }

    // Create new promoNameAssetCode for copied promotion
    if ( copiedPromotion.getPromoNameAssetCode().equals( originalPromotion.getPromoNameAssetCode() ) )
    {
      copiedPromotion.setPromoNameAssetCode( null );
      savePromoNameCmText( copiedPromotion, newPromotionName );
    }

    // Create a new asset name
    // (otherwise we'll be pointing to -and editing- the same data as the original copy)
    if ( originalPromotion.getCmAssetCode() != null )
    {
      String newAssetCode = cmAssetService.getUniqueAssetCode( Promotion.CM_PROMOTION_DATA_WEBRULES_ASSET_PREFIX );
      copiedPromotion.setCmAssetCode( newAssetCode );
      cmAssetService.copyCMAsset( originalPromotion.getCmAssetCode(), copiedPromotion.getCmAssetCode(), Promotion.CM_PROMOTION_DATA_WEBRULES_TEXT_KEY_DESC, null, false, null );
    }

    if ( originalPromotion.isGoalQuestOrChallengePointPromotion() )
    {
      GoalQuestPromotion copyPromo = (GoalQuestPromotion)copiedPromotion;
      GoalQuestPromotion orgPromo = (GoalQuestPromotion)originalPromotion;

      if ( orgPromo.getObjectiveAssetCode() != null )
      {
        String newObjectiveAssetCode = cmAssetService.getUniqueAssetCode( Promotion.GQ_CP_PROMO_OBJECTIVE_ASSET_PREFIX );
        copyPromo.setObjectiveAssetCode( newObjectiveAssetCode );
        cmAssetService.copyCMAsset( orgPromo.getObjectiveAssetCode(), copyPromo.getObjectiveAssetCode(), Promotion.GQ_CP_PROMO_OBJECTIVE_KEY_DESC, null, false, null );
      }

      if ( orgPromo.getOverview() != null )
      {
        String newOverViewAssetCode = cmAssetService.getUniqueAssetCode( Promotion.PROMO_OVERVIEW_CM_ASSET_PREFIX );
        copyPromo.setOverview( newOverViewAssetCode );
        cmAssetService.copyCMAsset( orgPromo.getOverview(), copyPromo.getOverview(), Promotion.PROMO_OVERVIEW_TEXT_KEY_DESC, null, false, null );
      }

      if ( orgPromo.getManagerCmAssetCode() != null )
      {
        String newManagerAssetCode = cmAssetService.getUniqueAssetCode( Promotion.CM_PROMOTION_DATA_WEBRULES_ASSET_PREFIX );
        copyPromo.setManagerCmAssetCode( newManagerAssetCode );
        cmAssetService.copyCMAsset( orgPromo.getManagerCmAssetCode(), copyPromo.getManagerCmAssetCode(), Promotion.CM_PROMOTION_DATA_WEBRULES_TEXT_KEY_DESC, null, false, null );
      }

      if ( orgPromo.getPromotionPartnerWebRulesAudience() != null )
      {
        if ( orgPromo.getPartnerCmAssetCode() != null )
        {
          String newPartnerAssetCode = cmAssetService.getUniqueAssetCode( Promotion.CM_PROMOTION_DATA_WEBRULES_ASSET_PREFIX );
          copyPromo.setPartnerCmAssetCode( newPartnerAssetCode );
          cmAssetService.copyCMAsset( orgPromo.getPartnerCmAssetCode(), copyPromo.getPartnerCmAssetCode(), Promotion.CM_PROMOTION_DATA_WEBRULES_TEXT_KEY_DESC, null, false, null );
        }
      }

      // to not copy cm asset of the original promotion for goal level name and description but copy
      // just the content.
      if ( ( (GoalQuestPromotion)orgPromo ).getGoalLevels() != null && ( (GoalQuestPromotion)orgPromo ).getGoalLevels().size() > 0 )
      {
        for ( Iterator<AbstractGoalLevel> orgPromoGoalLevelIter = ( (GoalQuestPromotion)orgPromo ).getGoalLevels().iterator(); orgPromoGoalLevelIter.hasNext(); )
        {
          AbstractGoalLevel orgPromoGoalLevel = orgPromoGoalLevelIter.next();
          String orgPromoCmAssetCode = orgPromoGoalLevel.getGoalLevelcmAssetCode();

          if ( orgPromoCmAssetCode != null )
          {
            for ( Iterator<AbstractGoalLevel> copiedPromoGoalLevelIter = ( (GoalQuestPromotion)copiedPromotion ).getGoalLevels().iterator(); copiedPromoGoalLevelIter.hasNext(); )
            {
              AbstractGoalLevel copiedPromoGoalLevel = copiedPromoGoalLevelIter.next();

              if ( orgPromoCmAssetCode.equals( copiedPromoGoalLevel.getGoalLevelcmAssetCode() ) )
              {
                copiedPromoGoalLevel.setGoalLevelcmAssetCode( cmAssetService.getUniqueAssetCode( Promotion.CM_GOAL_DESCRIPTION_ASSET_PREFIX ) );

                Map supercedingData = new HashMap();
                supercedingData.put( "GOAL", "Goal" );// hard coding the Goal here as in Cm it is
                                                      // Goal always for any new level added.
                cmAssetService.copyCMAsset( orgPromoCmAssetCode,
                                            copiedPromoGoalLevel.getGoalLevelcmAssetCode(),
                                            "Goal",
                                            supercedingData,
                                            true,
                                            getGoalAssetName( copiedPromoGoalLevel.getGoalLevelcmAssetCode() ) );
                copiedPromoGoalLevelIter.remove();
              }
            }
          }
        }
      }
    }

    // Bug# 32504 END
    // SSI_Phase_2
    /*
     * if ( copiedPromotion.isRecognitionPromotion() || (copiedPromotion.isSSIPromotion() &&
     * ((SSIPromotion)copiedPromotion).getAllowAwardMerchandise())) { Collection countries =
     * copiedPromotion.getPromoMerchCountries(); if ( countries != null ) { for ( Iterator iter =
     * countries.iterator(); iter.hasNext(); ) { PromoMerchCountry country =
     * (PromoMerchCountry)iter.next(); if ( country.getLevels() != null ) { for ( Iterator
     * levelsIter = country.getLevels().iterator(); levelsIter.hasNext(); ) { PromoMerchProgramLevel
     * promoMerchProgramLevel = (PromoMerchProgramLevel)levelsIter.next(); if (
     * promoMerchProgramLevel.getCmAssetKey() == null ||
     * promoMerchProgramLevel.getCmAssetKey().trim().length() == 0 )
     * promoMerchProgramLevel.setCmAssetKey( cmAssetService.getUniqueAssetCode(
     * SPOTLIGHT_LEVEL_DATA_ASSET_PREFIX ) ); // save asset for budget name CMDataElement
     * cmDataElement = new CMDataElement( "Promo Merch Program Level Name",
     * SPOTLIGHT_LEVEL_NAME_KEY, promoMerchProgramLevel.getDisplayLevelName(), false );
     * cmAssetService.createOrUpdateAsset( SPOTLIGHT_LEVEL_DATA_SECTION_CODE,
     * SPOTLIGHT_LEVEL_ASSET_TYPE_NAME, promoMerchProgramLevel.getLevelName() + " value",
     * promoMerchProgramLevel.getCmAssetKey(), cmDataElement ); }// for level } }// for country } }
     */
    if ( copiedPromotion.isRecognitionPromotion() )
    {
      Collection countries = copiedPromotion.getPromoMerchCountries();
      if ( countries != null )
      {
        for ( Iterator iter = countries.iterator(); iter.hasNext(); )
        {
          PromoMerchCountry country = (PromoMerchCountry)iter.next();
          if ( country.getLevels() != null )
          {
            for ( Iterator levelsIter = country.getLevels().iterator(); levelsIter.hasNext(); )
            {
              PromoMerchProgramLevel promoMerchProgramLevel = (PromoMerchProgramLevel)levelsIter.next();
              if ( promoMerchProgramLevel.getCmAssetKey() == null || promoMerchProgramLevel.getCmAssetKey().trim().length() == 0 )
              {
                promoMerchProgramLevel.setCmAssetKey( cmAssetService.getUniqueAssetCode( SPOTLIGHT_LEVEL_DATA_ASSET_PREFIX ) );
              }
              // save asset for budget name
              CMDataElement cmDataElement = new CMDataElement( "Promo Merch Program Level Name", SPOTLIGHT_LEVEL_NAME_KEY, promoMerchProgramLevel.getDisplayLevelName(), false );

              cmAssetService.createOrUpdateAsset( SPOTLIGHT_LEVEL_DATA_SECTION_CODE,
                                                  SPOTLIGHT_LEVEL_ASSET_TYPE_NAME,
                                                  promoMerchProgramLevel.getLevelName() + " value",
                                                  promoMerchProgramLevel.getCmAssetKey(),
                                                  cmDataElement );
            } // for level
          }
        } // for country
      }
    }
    if ( originalPromotion.isThrowdownPromotion() )
    {
      ThrowdownPromotion copyPromo = (ThrowdownPromotion)copiedPromotion;
      ThrowdownPromotion orgPromo = (ThrowdownPromotion)originalPromotion;

      if ( orgPromo.getOverview() != null )
      {
        String newOverViewAssetCode = cmAssetService.getUniqueAssetCode( Promotion.PROMO_OVERVIEW_CM_ASSET_PREFIX );
        copyPromo.setOverview( newOverViewAssetCode );
        cmAssetService.copyCMAsset( orgPromo.getOverview(), copyPromo.getOverview(), Promotion.PROMO_OVERVIEW_TEXT_KEY_DESC, null, false, null );
      }

      for ( Iterator<Division> copyDivisionIter = copyPromo.getDivisions().iterator(); copyDivisionIter.hasNext(); )
      {
        Division copyDivision = copyDivisionIter.next();
        if ( copyDivision.getDivisionNameAssetCode() != null )
        {
          String oldAsset = copyDivision.getDivisionNameAssetCode();
          String newDivisionNameAssetCode = cmAssetService.getUniqueAssetCode( Division.DIVISION_CM_ASSET_PREFIX );
          copyDivision.setDivisionNameAssetCode( newDivisionNameAssetCode );
          cmAssetService.copyCMAsset( oldAsset, copyDivision.getDivisionNameAssetCode(), Division.DIVISION_NAME_KEY_DESC, null, false, null );
        }
      }
    }

    if ( originalPromotion.isQuizPromotion() )
    {
      QuizPromotion copyPromo = (QuizPromotion)copiedPromotion;
      QuizPromotion orgPromo = (QuizPromotion)originalPromotion;

      if ( orgPromo.getOverview() != null )
      {
        String newOverViewAssetCode = cmAssetService.getUniqueAssetCode( Promotion.PROMO_OVERVIEW_CM_ASSET_PREFIX );
        copyPromo.setOverview( newOverViewAssetCode );
        cmAssetService.copyCMAsset( orgPromo.getOverview(), copyPromo.getOverview(), Promotion.PROMO_OVERVIEW_TEXT_KEY_DESC, null, false, null );
      }
    }

    // copy badge & badge rules
    if ( originalPromotion.isSSIPromotion() )
    {
      if ( originalPromotion.getBadge() != null )
      {
        Badge promotion = new Badge();
        promotion.setName( copiedPromotion.getName() + "Badge" );
        promotion.setBadgeType( BadgeType.lookup( BadgeType.EARNED_OR_NOT_EARNED ) );
        promotion.setPromotionType( PromotionType.lookup( PromotionType.BADGE ) );

        promotion = (Badge)savePromoNameCmText( promotion, copiedPromotion.getName() + " Badge" );
        promotion.setDisplayEndDays( new Long( 0 ) );
        promotion.setTileHighlightPeriod( new Long( 0 ) );
        promotion.setStatus( Badge.BADGE_ACTIVE );
        promotion.setPromotionStatus( generatePromotionStatus( originalPromotion.getSubmissionStartDate() ) );
        promotion.setSubmissionStartDate( originalPromotion.getSubmissionStartDate() );
        promotion.setAwardType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );
        promotion.setSweepstakesWinnerEligibilityType( (SweepstakesWinnerEligibilityType)PickListItem.getDefaultItem( SweepstakesWinnerEligibilityType.class ) );
        Badge badgeReturned = (Badge)savePromotion( promotion );

        List<BadgeRule> badgeRulesFinalList = new ArrayList<BadgeRule>();
        if ( originalPromotion.getBadge().getBadgeRules() != null && originalPromotion.getBadge().getBadgeRules().size() > 0 )
        {
          for ( BadgeRule badgeRule : originalPromotion.getBadge().getBadgeRules() )
          {
            BadgeRule newBadgeRule = new BadgeRule();
            String badgeNameCmAsset = saveRulesCmText( newBadgeRule.getBadgeName(), CmsResourceBundle.getCmsBundle().getString( badgeRule.getBadgeName(), BadgeRule.BADGE_RULES_CMASSET_TYPE_KEY ) );
            newBadgeRule.setBadgePromotion( badgeReturned );
            newBadgeRule.setMaximumQualifier( 0L );
            newBadgeRule.setMinimumQualifier( 0L );
            newBadgeRule.setBadgeLibraryCMKey( badgeRule.getBadgeLibraryCMKey() );
            newBadgeRule.setBadgeName( badgeNameCmAsset );
            badgeRulesFinalList.add( newBadgeRule );
          }
        }
        badgeReturned.setBadgeRules( new HashSet<BadgeRule>( badgeRulesFinalList ) );
        copiedPromotion.setBadge( badgeReturned );
      }
      // this.promotionDAO.save( copiedPromotion );
    }

    savePromotion( copiedPromotion );
    return copiedPromotion;
  }

  private String saveRulesCmText( String badgeRule, String badgeNameText ) throws ServiceErrorException
  {
    String badgeNameCmAsset = null;
    try
    {
      if ( badgeRule == null )
      {
        badgeNameCmAsset = cmAssetService.getUniqueAssetCode( BadgeRule.BADGE_RULES_CMASSET_PREFIX );
      }
      else
      {
        badgeNameCmAsset = badgeRule;
      }

      CMDataElement cmDataElement = new CMDataElement( BadgeRule.BADGE_RULES_CMASSET_NAME, BadgeRule.BADGE_RULES_CMASSET_TYPE_KEY, badgeNameText, false, DataTypeEnum.HTML );
      List elements = new ArrayList();
      elements.add( cmDataElement );

      cmAssetService.createOrUpdateAsset( BadgeRule.BADGE_RULES_SECTION_CODE,
                                          BadgeRule.BADGE_RULES_CMASSET_TYPE_NAME,
                                          BadgeRule.BADGE_RULES_CMASSET_NAME,
                                          badgeNameCmAsset,
                                          elements,
                                          UserManager.getLocale(),
                                          null );
    }
    catch( ServiceErrorException e )
    {
      List errors = new ArrayList();
      errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
      throw e;
    }

    return badgeNameCmAsset;
  }

  public PromotionStatusType generatePromotionStatus( Date badgeStartDate )
  {
    Date currentDate = new Date();
    // if the submission start date is has passed, automatically set the promotion to live
    if ( badgeStartDate.before( currentDate ) )
    {
      return PromotionStatusType.lookup( PromotionStatusType.LIVE );
    }
    else
    {
      return PromotionStatusType.lookup( PromotionStatusType.COMPLETE );
    }
  }

  public boolean isPromotionClaimableByParticipant( Long promotionId, Participant participant, Date onThisDay )
  {
    Promotion promotion = getPromotionById( promotionId );
    return isPromotionClaimableByParticipant( promotion, participant, onThisDay );
  }

  public boolean isPromotionClaimableByParticipant( Long promotionId, Participant participant )
  {
    Promotion promotion = getPromotionById( promotionId );
    return isPromotionClaimableByParticipant( promotion, participant, new Date() );
  }

  public boolean isPromotionClaimableByParticipant( Promotion promotion, Participant participant )
  {
    return isPromotionClaimableByParticipant( promotion, participant, new Date() );
  }

  public boolean isPromotionClaimableByParticipant( Promotion promotion, Participant participant, Date onThisDay )
  {
    boolean promotionIsClaimable = false;

    if ( promotion == null || participant == null )
    {
      return promotionIsClaimable;
    }
    String timeZoneID = userService.getUserTimeZone( participant.getId() );

    // 1. Promotion must be live.
    // 2. Promotion must be in date range (onThisDay is within start/end date period).
    // 3. Participant is part of the promotion audience so that he/she is allowed
    // to make a claim; or give a recognition; or make a nomination; or take a quiz or select a
    // goal, etc.
    // 4. If budget is required, promotion has budget available.
    if ( !promotion.isGoalQuestOrChallengePointPromotion() && promotion.isLive()
        && DateUtils.isDateBetween( onThisDay, promotion.getSubmissionStartDate(), promotion.getSubmissionEndDate(), timeZoneID ) && isParticipantInAudience( participant, promotion )
        && isBudgetAvailableOrNotRequired( participant, promotion ) )
    {
      promotionIsClaimable = true;
    }

    if ( promotion.isGoalQuestOrChallengePointPromotion() && promotion.isLive()
        && DateUtils.isDateBetween( onThisDay, promotion.getTileDisplayStartDate(), promotion.getTileDisplayEndDate(), timeZoneID ) && isParticipantInAudience( participant, promotion ) )
    {
      promotionIsClaimable = true;
    }

    return promotionIsClaimable;
  }

  public boolean isPromotionClaimableByParticipant( Long promotionId, Participant participant, boolean flag )
  {
    Promotion promotion = getPromotionById( promotionId );
    return isPromotionClaimableByParticipant( promotion, participant, flag );
  }

  public boolean isPromotionClaimableByParticipant( Promotion promotion, Participant participant, boolean flag )
  {
    boolean promotionIsClaimable = false;
    Date onThisDay = new Date();
    String timeZoneID = UserManager.getUser().getTimeZoneId();
    if ( timeZoneID == null )
    {
      timeZoneID = "CST";
    }
    Date toDay = DateUtils.applyTimeZone( new Date(), timeZoneID );

    if ( promotion == null || participant == null )
    {
      return promotionIsClaimable;
    }

    // 1. Promotion must be live.
    // 2. Promotion must be in date range (onThisDay is within start/end date period).
    // 3. Participant is part of the promotion audience so that he/she is allowed
    // to make a claim; or give a recognition; or make a nomination; or take a quiz or select a
    // goal, etc.
    // 4. If budget is required, promotion has budget available.
    if ( !promotion.isGoalQuestOrChallengePointPromotion() && promotion.isLive()
        && DateUtils.isDateBetween( onThisDay, promotion.getSubmissionStartDate(), promotion.getSubmissionEndDate(), timeZoneID ) && isParticipantInAudience( participant, promotion )
        && isBudgetAvailableOrNotRequired( participant, promotion ) )
    {
      promotionIsClaimable = true;
    }

    if ( promotion.isGoalQuestOrChallengePointPromotion() && promotion.isLive()
        && DateUtils.isDateBetween( onThisDay, promotion.getTileDisplayStartDate(), promotion.getTileDisplayEndDate(), timeZoneID ) && isParticipantInAudience( participant, promotion ) )
    {
      promotionIsClaimable = true;
    }

    return promotionIsClaimable;
  }

  public boolean isPromotionClaimableByNode( Promotion promotion, Node node )
  {
    boolean promotionIsClaimable = false;

    if ( node == null )
    {
      return promotionIsClaimable;
    }

    Collection nodes = new ArrayList();
    nodes.add( node );
    Set paxesInNode = participantService.getPaxInNodes( nodes, false );
    for ( Iterator paxIter = paxesInNode.iterator(); paxIter.hasNext(); )
    {
      Participant pax = (Participant)paxIter.next();
      if ( isParticipantMemberOfPromotionAudience( pax, promotion, true, null ) )
      {
        return true;
      }
    }

    return promotionIsClaimable;
  }

  /**
   * Determine if a participant is in audience to be part of the promotion.
   *
   * In the case of Goalquest Promotion, need to check both primary and secondary.
   * For GQ, pax could be primary (as in all active or specific paxs)
   * or secondary (as in self-enrolling program). Either audience indicates the pax
   * can select goals for the promotion.
   *
   * @param participant
   * @param promotion
   * @return true if the pax is in audience
   */
  public boolean isParticipantInAudience( Participant participant, Promotion promotion )
  {
    boolean submitable = false;

    // if GQ promotion and self enroll only, then pax must exists in the secondary audience.
    if ( ( promotion.isGoalQuestPromotion() || promotion.isChallengePointPromotion() ) && promotion.getPrimaryAudienceType().isAllPaxSelfEnrollOnly() )
    {
      submitable = isParticipantInSecondaryAudience( promotion, participant );

    }
    else if ( promotion.isDIYQuizPromotion() )
    {
      // Checks if the participant is in diy_quiz_participant table nothing but quiz audience
      // This is used whether to determine if the participant can take a quiz
      submitable = isParticipantInDIYQuizAudience( participant, promotion );
    }
    else
    {
      submitable = audienceService.isParticipantInPrimaryAudience( promotion, participant );
      if ( ( promotion.isGoalQuestPromotion() || promotion.isChallengePointPromotion() ) && !submitable )
      {
        submitable = isParticipantInSecondaryAudience( promotion, participant );
      }
      if ( ( promotion.isRecognitionPromotion() || promotion.isNominationPromotion() ) && promotion.isFileLoadEntry() )
      {
        submitable = isParticipantInSecondaryAudience( promotion, participant );
      }
    }
    return submitable;
  }

  /**
   * Determine if a participant is part of the DIY promotion audience.
   * This is used whether a participant can manage a diy quiz
   */
  public boolean isParticipantInDIYPromotionAudience( Participant participant )
  {
    boolean submitable = false;

    PromotionQueryConstraint promoQueryConstraint = new PromotionQueryConstraint();
    promoQueryConstraint.setPromotionTypesIncluded( new PromotionType[] { PromotionType.lookup( PromotionType.DIY_QUIZ ) } );
    promoQueryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ) } );
    promoQueryConstraint.setOrderByPromotionNameCaseInsensitive( true );

    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PRIMARY_AUDIENCES ) );

    // There can be only one live diy_quiz promotion
    List allPromoList = getPromotionListWithAssociations( promoQueryConstraint, promoAssociationRequestCollection );

    Iterator it = allPromoList.iterator();
    while ( it.hasNext() )
    {
      QuizPromotion promotion = (QuizPromotion)it.next();
      submitable = audienceService.isParticipantInPrimaryAudience( promotion, participant );
    }
    return submitable;
  }

  /**
   * Determine if the participant is in diy_quiz_participant
   * @param participant
   * @param promotion
   * @return
   */
  private boolean isParticipantInDIYQuizAudience( Participant participant, Promotion promotion )
  {
    boolean isParticipantInDIYQuizAudience = false;
    List<DIYQuiz> diyQuizList = diyQuizDAO.getActiveQuizListForParticipantByPromotion( promotion.getId(), participant.getId() );
    if ( diyQuizList != null && diyQuizList.size() > 0 )
    {
      isParticipantInDIYQuizAudience = true;
    }
    return isParticipantInDIYQuizAudience;
  }

  /**
   * Determine if a participant is in the secondary audience.
   *
   *
   * @param promotion
   * @param participant
   * @return true if the pax is in the secondary audience
   */
  private boolean isParticipantInSecondaryAudience( Promotion promotion, Participant participant )
  {
    if ( participant != null )
    {
      for ( Iterator iter = participant.getUserNodes().iterator(); iter.hasNext(); )
      {
        UserNode userNode = (UserNode)iter.next();
        if ( audienceService.isParticipantInSecondaryAudience( promotion, participant, userNode.getNode() ) )
        {
          // secondary audience found.
          return true;
        }
      }
    }
    return false;
  }

  private boolean isBudgetAvailableOrNotRequired( Participant participant, Promotion promotion )
  {
    boolean availableOrNotRequired = false;
    Set nodes = participant.getUserNodes();

    for ( Iterator iter = nodes.iterator(); iter.hasNext(); )
    {
      UserNode userNode = (UserNode)iter.next();
      if ( isBudgetAvailableOrNotRequired( participant, promotion, userNode.getNode() ) )
      {
        // available budget found.
        availableOrNotRequired = true;
        break;
      }
    }
    return availableOrNotRequired;
  }

  private boolean isBudgetAvailableOrNotRequired( Participant participant, Promotion promotion, Node node )
  {
    boolean availableOrNotRequired;

    promotion = BaseAssociationRequest.initializeAndUnproxy( promotion );

    if ( promotion.isRecognitionPromotion() )
    {
      RecognitionPromotion recognitionPromotion = (RecognitionPromotion)promotion;

      if ( !recognitionPromotion.isBudgetUsed() )
      {
        // no budget used for this recog promotion
        availableOrNotRequired = true;
      }
      else
      {
        Budget budget = getAvailableBudget( promotion, participant, node );
        if ( budget != null && BudgetStatusType.ACTIVE.equals( budget.getStatus().getCode() ) )
        {
          // available budget found.
          availableOrNotRequired = true;
        }
        else
        {
          // budget not available
          availableOrNotRequired = false;
        }
      }
    }
    else
    {
      // budget not required for this promotion type
      availableOrNotRequired = true;
    }
    return availableOrNotRequired;
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.promotion.PromotionService#getClaimablePromotionsForParticipant(com.biperf.core.domain.participant.Participant)
   * @param participant
   * @return List
   */
  public List getClaimablePromotionsForParticipant( Participant participant )
  {
    List claimablePromotions = new ArrayList();

    PromotionQueryConstraint promotionQueryConstraint = new PromotionQueryConstraint();
    // promotionQueryConstraint.setMasterOrChildConstraint( Boolean.TRUE );
    promotionQueryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ) } );
    promotionQueryConstraint.setOrderByPromotionNameCaseInsensitive( true );

    List liveMasterPromotions = getPromotionList( promotionQueryConstraint );

    for ( Iterator iter = liveMasterPromotions.iterator(); iter.hasNext(); )
    {
      Promotion promotion = (Promotion)iter.next();
      if ( isPromotionClaimableByParticipant( promotion.getId(), participant ) )
      {
        claimablePromotions.add( promotion );
      }

    }

    return claimablePromotions;
  }

  /**
   * Build the list of nodes against which a participant can submit a claim. Nodes must have a
   * budget (if a budget is required) and must be from the passed in hierarchy (which would
   * generally be the primary hierarchy).
   *
   * @param promotionId
   * @param participantId
   * @return Returns list of claimable nodes.
   */
  public List getClaimableNodes( Long promotionId, Long participantId )
  {
    Promotion promotion = promotionDAO.getPromotionById( promotionId );
    Participant participant = participantDAO.getParticipantById( participantId );
    List claimableNodes = new ArrayList();
    // List assignedNodes = userDAO.getAssignedNodes( participant.getId() );

    Hierarchy primaryHierarchy = hierarchyService.getPrimaryHierarchy();

    // Special case for Recognition Promotion - must have available budget
    for ( Iterator iter = participant.getUserNodes().iterator(); iter.hasNext(); )
    {
      UserNode userNode = (UserNode)iter.next();
      if ( !promotion.getPrimaryAudienceType().isSpecifyAudienceType()
          || audienceService.isNodeInPromotionAudiences( userNode.getNode(), promotion.getPromotionPrimaryAudiences(), userNode.getHierarchyRoleType() ) )
      {
        if ( userNode.getNode().getHierarchy().equals( primaryHierarchy ) )
        {
          if ( isBudgetAvailableOrNotRequired( participant, promotion, userNode.getNode() ) )
          {
            claimableNodes.add( userNode.getNode() );
          }
        }
      }
    }
    return claimableNodes;
  }

  /**
   * Determine if the given node ID is a claimable node for the given promotion
   * ID and participant ID.
   * @param nodeId
   * @param promotionId
   * @param participantId
   *
   * @return boolean indicating if the combination of node, promotion, and
   * participant is claimable.
   */
  public boolean isNodeClaimableForRecognitionPromotion( Long nodeId, Long promotionId, Long participantId )
  {
    List<Node> claimableNodes = getClaimableNodes( promotionId, participantId );

    boolean isValid = false;

    if ( claimableNodes != null && !claimableNodes.isEmpty() )
    {
      for ( Node node : claimableNodes )
      {
        if ( node.getId().equals( nodeId ) )
        {
          isValid = true;
          break;
        }
      }
    }

    return isValid;
  }

  /**
   * Call this from Recognition Wizard Action ONLY if the user is not proxy, much faster
   * Build the list of nodes against which a participant can submit a claim. Nodes must have a
   * budget (if a budget is required) and must be from the passed in hierarchy (which would
   * generally be the primary hierarchy).
   *
   * @param promotionId
   * @param participantId
   * @return Returns list of claimable nodes.
   */
  public List getClaimableNodesNonProxy( Long promotionId, Long participantId )
  {
    Promotion promotion = promotionDAO.getPromotionById( promotionId );
    Participant participant = participantDAO.getParticipantById( participantId );
    List claimableNodes = new ArrayList();
    // List assignedNodes = userDAO.getAssignedNodes( participant.getId() );

    Hierarchy primaryHierarchy = hierarchyService.getPrimaryHierarchy();

    // Special case for Recognition Promotion - must have available budget
    for ( Iterator iter = participant.getUserNodes().iterator(); iter.hasNext(); )
    {
      UserNode userNode = (UserNode)iter.next();
      if ( !promotion.getPrimaryAudienceType().isSpecifyAudienceType()
          || audienceService.isNodeInPromotionAudiences( userNode.getNode(), promotion.getPromotionPrimaryAudiences(), userNode.getHierarchyRoleType() ) )
      {
        if ( userNode.getNode().getHierarchy().equals( primaryHierarchy ) )
        {
          claimableNodes.add( userNode.getNode() );
        }
      }
    }
    return claimableNodes;
  }

  /**
   * Retrieves all the child promotions for the specified promotion
   *
   * @param promotionId
   * @return List
   */
  public List getChildPromotions( Long promotionId )
  {
    return promotionDAO.getChildPromotions( promotionId );
  }

  /**
   * Retrieves the promotion for the specified enrollProgramCode.
   * enrollProgramCode should be unique for each promotion.
   *
   * Overridden from @see com.biperf.core.dao.promotion.PromotionDAO#getPromotionByEnrollProgramCode(java.lang.String)
   * @param enrollProgramCode
   * @return List
   */
  public List getPromotionByEnrollProgramCode( String enrollProgramCode )
  {
    return promotionDAO.getPromotionByEnrollProgramCode( enrollProgramCode );
  }

  /**
   * Retrieves all the non expired child promotions for the specified promotion
   *
   * @param promotionId
   * @return List
   */
  public List getNonExpiredChildPromotions( Long promotionId )
  {
    return promotionDAO.getNonExpiredChildPromotions( promotionId );
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.promotion.PromotionService#getAllForApprover(Long, PromotionType)
   * @param approverUserId
   * @param promotionType
   * @return Set
   */
  public Set getAllForApprover( Long approverUserId, PromotionType promotionType )
  {
    PromotionQueryConstraint promotionQueryConstraint = new PromotionQueryConstraint();
    promotionQueryConstraint.setPromotionTypesIncluded( new PromotionType[] { promotionType } );
    promotionQueryConstraint.setPromotionStatusTypesExcluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) } );

    List nonExpiredPromotions = promotionDAO.getPromotionList( promotionQueryConstraint );
    LinkedHashSet promotionsForApprover = new LinkedHashSet();
    if ( !nonExpiredPromotions.isEmpty() )
    {
      Long[] includedPromotionIds = BaseDomainUtils.toIdArray( nonExpiredPromotions );
      List promotionClaimsValueList = new ArrayList();
      if ( promotionType.isRecognitionPromotion() )
      {
        promotionClaimsValueList = claimService.getProductClaimsForApprovalByUser( approverUserId,
                                                                                   includedPromotionIds,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   PromotionType.lookup( PromotionType.RECOGNITION ),
                                                                                   null,
                                                                                   null,
                                                                                   Boolean.FALSE,
                                                                                   null,
                                                                                   null,
                                                                                   0,
                                                                                   0 );
      }
      if ( promotionType.isProductClaimPromotion() )
      {
        promotionClaimsValueList = claimService.getProductClaimsForApprovalByUser( approverUserId,
                                                                                   includedPromotionIds,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   PromotionType.lookup( PromotionType.PRODUCT_CLAIM ),
                                                                                   null,
                                                                                   null,
                                                                                   Boolean.FALSE,
                                                                                   null,
                                                                                   null,
                                                                                   0,
                                                                                   0 );
      }

      List promotionClaimGroupsValueList = new ArrayList();
      if ( promotionType.isNominationPromotion() )
      {
        promotionClaimsValueList = claimService
            .getProductClaimsForApprovalByUser( approverUserId, includedPromotionIds, null, null, null, PromotionType.lookup( PromotionType.NOMINATION ), null, null, Boolean.FALSE, null, null, 0, 0 );
        promotionClaimGroupsValueList = claimGroupService.getClaimGroupsForApprovalByUser( approverUserId, includedPromotionIds, null, null, null, null, Boolean.FALSE, Boolean.FALSE );
      }

      List promotionApprovablesValueList = new ArrayList();
      promotionApprovablesValueList.addAll( promotionClaimsValueList );
      promotionApprovablesValueList.addAll( promotionClaimGroupsValueList );

      for ( Iterator iter = promotionApprovablesValueList.iterator(); iter.hasNext(); )
      {
        PromotionApprovableValue promotionClaimsValue = (PromotionApprovableValue)iter.next();
        promotionsForApprover.add( promotionClaimsValue.getPromotion() );
      }
    }

    return promotionsForApprover;
  }

  public List getAllLiveAndExpiredByTypeAndUserId( String promotionType, Long userId )
  {
    List assignedPromotionList = promotionDAO.getAllLiveAndExpiredByType( promotionType );
    return buildPromotionPaxValueList( userId, assignedPromotionList );
  }

  public List getAllLiveAndExpiredByUserId( Long userId )
  {
    List promotionList = promotionDAO.getAllLiveAndExpired();
    return buildPromotionPaxValueList( userId, promotionList );
  }

  /**
   * Retrieves the assigned promotions by promotionType and userId
   *
   * @param userId
   * @param promotionList
   * @return List
   */
  private List buildPromotionPaxValueList( Long userId, List promotionList )
  {

    Participant participant = participantService.getParticipantById( userId );
    List promoPaxValueList = new ArrayList();

    // iterate through list for all promotions
    // (you asked for it)
    Iterator promoIterator = promotionList.iterator();
    while ( promoIterator.hasNext() )
    {
      Promotion promotion = (Promotion)promoIterator.next();
      if ( promotion.isDIYQuizPromotion() )
      {
        boolean isPromotionAudience = audienceService.isParticipantInPrimaryAudience( promotion, participant );
        if ( isPromotionAudience )
        {
          // Display promotion name for quiz managers(promotion audience)
          PromotionPaxValue promoPaxValue = new PromotionPaxValue();
          promoPaxValue.setPromotion( promotion );
          promoPaxValue.setDisplayName( promotion.getName() );
          promoPaxValue.setModuleCode( promotion.getPromotionType().getCode() );
          promoPaxValue.setStartDate( promotion.getSubmissionStartDate() );
          promoPaxValue.setEndDate( promotion.getSubmissionEndDate() );
          promoPaxValue.setRoleKey( DIY_MANAGER );
          promoPaxValueList.add( promoPaxValue );
        }

        List<DIYQuiz> diyQuizList = diyQuizDAO.getEligibleQuizzesForParticipantByPromotion( promotion.getId(), participant.getId() );
        if ( diyQuizList != null )
        {
          for ( DIYQuiz diyQuiz : diyQuizList )
          {
            // Display quiz name for quiz participants(quiz audience)
            PromotionPaxValue promoPaxValue = new PromotionPaxValue();
            promoPaxValue.setPromotion( promotion );
            promoPaxValue.setDisplayName( diyQuiz.getName() );
            promoPaxValue.setModuleCode( promotion.getPromotionType().getCode() );
            promoPaxValue.setStartDate( diyQuiz.getStartDate() );
            promoPaxValue.setEndDate( diyQuiz.getEndDate() );
            promoPaxValue.setRoleKey( DIY_PARTICIPANT );
            promoPaxValueList.add( promoPaxValue );
          }
        }
      }

      else if ( promotion.isEngagementPromotion() )
      {
        EngagementPromotion engagementPromotion = (EngagementPromotion)promotion;
        Set<EngagementPromotions> engagementEligiblePromotions = engagementPromotion.getEngagementPromotions();
        if ( engagementEligiblePromotions != null )
        {
          Iterator iter = engagementEligiblePromotions.iterator();
          boolean isInAudience = false;
          while ( iter.hasNext() )
          {
            EngagementPromotions eligilePromotion = (EngagementPromotions)iter.next();
            if ( isParticipantInAudience( participant, eligilePromotion.getEligiblePromotion() ) || isParticipantInSecondaryAudience( eligilePromotion.getEligiblePromotion(), participant ) )
            {
              isInAudience = true;
            }
          }
          if ( isInAudience )
          {
            PromotionPaxValue promoPaxValue = new PromotionPaxValue();
            promoPaxValue.setPromotion( promotion );
            promoPaxValue.setModuleCode( promotion.getPromotionType().getCode() );
            promoPaxValue.setStartDate( promotion.getSubmissionStartDate() );
            promoPaxValue.setEndDate( promotion.getSubmissionEndDate() );
            promoPaxValue.setDisplayName( promotion.getName() );
            promoPaxValue.setRoleKey( RECOGNITION_PRIMARY );
            promoPaxValueList.add( promoPaxValue );
          }
        }
      }
      // check primary audiences
      else if ( isParticipantInAudience( participant, promotion ) )
      {
        PromotionPaxValue promoPaxValue = new PromotionPaxValue();
        promoPaxValue.setPromotion( promotion );
        promoPaxValue.setModuleCode( promotion.getPromotionType().getCode() );
        promoPaxValue.setStartDate( promotion.getSubmissionStartDate() );
        promoPaxValue.setEndDate( promotion.getSubmissionEndDate() );
        promoPaxValue.setDisplayName( promotion.getName() );

        if ( promotion.getPromotionType().getCode().equals( PromotionType.RECOGNITION ) )
        {
          promoPaxValue.setRoleKey( RECOGNITION_PRIMARY );
        }
        else if ( promotion.getPromotionType().getCode().equals( PromotionType.QUIZ ) )
        {
          promoPaxValue.setRoleKey( QUIZ_PRIMARY );
        }
        else if ( promotion.getPromotionType().getCode().equals( PromotionType.DIY_QUIZ ) )
        {
          promoPaxValue.setRoleKey( DIY_MANAGER );
        }
        else if ( promotion.getPromotionType().getCode().equals( PromotionType.PRODUCT_CLAIM ) )
        {
          promoPaxValue.setRoleKey( PRODUCT_CLAIM_PRIMARY );
        }
        else if ( promotion.getPromotionType().getCode().equals( PromotionType.NOMINATION ) )
        {
          promoPaxValue.setRoleKey( NOMINATION_PRIMARY );
        }
        else if ( promotion.getPromotionType().getCode().equals( PromotionType.SURVEY ) )
        {
          promoPaxValue.setRoleKey( SURVEY_PRIMARY );
        }
        else if ( promotion.getPromotionType().getCode().equals( PromotionType.GOALQUEST ) )
        {
          promoPaxValue.setRoleKey( GOALQUEST_PRIMARY );
        }
        else if ( promotion.getPromotionType().getCode().equals( PromotionType.CHALLENGE_POINT ) )
        {
          promoPaxValue.setRoleKey( CHALLENGEPOINT_PRIMARY );
        }
        else if ( promotion.getPromotionType().getCode().equals( PromotionType.THROWDOWN ) )
        {
          promoPaxValue.setRoleKey( THROWDOWN_PRIMARY );
        }
        promoPaxValueList.add( promoPaxValue );
      }

      // check secondary audiences if recognition or product claim or nomination type
      if ( promotion.getPromotionType().getCode().equals( PromotionType.RECOGNITION ) || promotion.getPromotionType().getCode().equals( PromotionType.PRODUCT_CLAIM )
          || promotion.getPromotionType().getCode().equals( PromotionType.NOMINATION ) )
      {
        if ( audienceService.isParticipantInSecondaryAudience( promotion, participant, null ) )
        {
          PromotionPaxValue secondaryPromoPaxValue = new PromotionPaxValue();
          secondaryPromoPaxValue.setPromotion( promotion );
          secondaryPromoPaxValue.setModuleCode( promotion.getPromotionType().getCode() );
          secondaryPromoPaxValue.setStartDate( promotion.getSubmissionStartDate() );
          secondaryPromoPaxValue.setEndDate( promotion.getSubmissionEndDate() );
          secondaryPromoPaxValue.setDisplayName( promotion.getName() );

          if ( promotion.getPromotionType().getCode().equals( PromotionType.RECOGNITION ) )
          {
            secondaryPromoPaxValue.setRoleKey( RECOGNITION_SECONDARY );
          }
          else if ( promotion.getPromotionType().getCode().equals( PromotionType.PRODUCT_CLAIM ) )
          {
            secondaryPromoPaxValue.setRoleKey( PRODUCT_CLAIM_SECONDARY );
          }
          else if ( promotion.getPromotionType().getCode().equals( PromotionType.NOMINATION ) )
          {
            secondaryPromoPaxValue.setRoleKey( NOMINATION_SECONDARY );
          }
          promoPaxValueList.add( secondaryPromoPaxValue );
        }
      }

      if ( promotion.getPromotionType().getCode().equals( PromotionType.THROWDOWN ) )
      {
        Team team = teamDAO.getTeamByUserIdForPromotion( userId, promotion.getId() );
        if ( team != null )
        {
          PromotionPaxValue secondaryPromoPaxValue = new PromotionPaxValue();
          secondaryPromoPaxValue.setPromotion( promotion );
          secondaryPromoPaxValue.setModuleCode( promotion.getPromotionType().getCode() );
          secondaryPromoPaxValue.setStartDate( promotion.getSubmissionStartDate() );
          secondaryPromoPaxValue.setEndDate( promotion.getSubmissionEndDate() );
          secondaryPromoPaxValue.setDisplayName( promotion.getName() );
          secondaryPromoPaxValue.setRoleKey( THROWDOWN_SECONDARY );
          promoPaxValueList.add( secondaryPromoPaxValue );
        }
      }
    }

    return promoPaxValueList;
  }

  /**
   * Create a child promotion with necessary data related to the following promotion sections: -
   * Basics - Form Rules - Approvals - Notifications NOTE: This method does not persist the child
   * promotion.
   *
   * @see com.biperf.core.service.promotion.PromotionService#createChildPromotion(java.lang.Long)
   * @param parentPromotionId
   * @return Promotion (The child Promotion)
   * @throws BeaconRuntimeException
   */
  public Promotion createChildPromotion( Long parentPromotionId ) throws ServiceErrorException, BeaconRuntimeException
  {

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();

    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.ALL ) );

    ProductClaimPromotion parentPromotion = (ProductClaimPromotion)promotionDAO.getPromotionByIdWithAssociations( parentPromotionId, associationRequestCollection );
    if ( parentPromotion == null )
    {
      return null;
    }

    ProductClaimPromotion childPromotion = new ProductClaimPromotion();

    // Set default data on child\
    // childPromotion.setPayoutType( parentPromotion.getPayoutType() );

    // - BASICS DATA
    childPromotion.setParentPromotion( parentPromotion );
    childPromotion.setPromotionType( parentPromotion.getPromotionType() );
    childPromotion.setSubmissionStartDate( parentPromotion.getSubmissionStartDate() );
    childPromotion.setSubmissionEndDate( parentPromotion.getSubmissionEndDate() );
    childPromotion.setPromotionProcessingMode( parentPromotion.getPromotionProcessingMode() );
    childPromotion.setClaimForm( parentPromotion.getClaimForm() );
    childPromotion.setAwardType( parentPromotion.getAwardType() );

    // - APPROVALS DATA
    childPromotion.setApprovalType( parentPromotion.getApprovalType() );
    childPromotion.setApproverType( parentPromotion.getApproverType() );
    childPromotion.setApprovalAutoDelayDays( parentPromotion.getApprovalAutoDelayDays() );
    childPromotion.setApprovalConditionalClaimCount( parentPromotion.getApprovalConditionalClaimCount() );
    childPromotion.setApprovalConditionalAmountOperator( parentPromotion.getApprovalConditionalAmountOperator() );
    childPromotion.setApprovalConditionalAmount( parentPromotion.getApprovalConditionalAmount() );
    childPromotion.setApprovalNodeLevels( parentPromotion.getApprovalNodeLevels() );
    childPromotion.setApprovalStartDate( parentPromotion.getApprovalStartDate() );
    childPromotion.setApprovalEndDate( parentPromotion.getApprovalEndDate() );

    try
    {
      // - FORM RULES
      childPromotion.setPromotionClaimFormStepElementValidations( new LinkedHashSet() );
      for ( Iterator cfseValidationsIter = parentPromotion.getPromotionClaimFormStepElementValidations().iterator(); cfseValidationsIter.hasNext(); )
      {
        PromotionClaimFormStepElementValidation promotionClaimFormStepElementValidation = (PromotionClaimFormStepElementValidation)cfseValidationsIter.next();
        childPromotion.addPromotionClaimFormStepElementValidation( (PromotionClaimFormStepElementValidation)promotionClaimFormStepElementValidation.clone() );
      }
      childPromotion.setPromotionParticipantApprovers( new ArrayList() );
      for ( Iterator promotionParticipanApproversIter = parentPromotion.getPromotionParticipantApprovers().iterator(); promotionParticipanApproversIter.hasNext(); )
      {
        PromotionParticipantApprover promotionParticipantApprover = (PromotionParticipantApprover)promotionParticipanApproversIter.next();
        childPromotion.addPromotionParticipantApprover( (PromotionParticipantApprover)promotionParticipantApprover.clone() );
      }
      // copy the promotionParticipantSubmitters
      childPromotion.setPromotionParticipantSubmitters( new ArrayList() );
      for ( Iterator promotionParticipantSubmitterIter = parentPromotion.getPromotionParticipantSubmitters().iterator(); promotionParticipantSubmitterIter.hasNext(); )
      {
        PromotionParticipantSubmitter promotionParticipantSubmitterOriginal = (PromotionParticipantSubmitter)promotionParticipantSubmitterIter.next();
        childPromotion.addPromotionParticipantSubmitter( (PromotionParticipantSubmitter)promotionParticipantSubmitterOriginal.clone() );
      }
      // copy promotionApprovalOptions
      childPromotion.setPromotionApprovalOptions( new LinkedHashSet() );
      for ( Iterator approvalOptionsIter = parentPromotion.getPromotionApprovalOptions().iterator(); approvalOptionsIter.hasNext(); )
      {
        PromotionApprovalOption promotionApprovalOption = (PromotionApprovalOption)approvalOptionsIter.next();

        childPromotion.addPromotionApprovalOption( (PromotionApprovalOption)promotionApprovalOption.clone() );
      }
      // - NOTIFICATIONS DATA
      childPromotion.setPromotionNotifications( new ArrayList() );
      for ( Iterator notificationIter = parentPromotion.getPromotionNotifications().iterator(); notificationIter.hasNext(); )
      {
        PromotionNotification promotionNotificationOriginal = (PromotionNotification)notificationIter.next();
        if ( promotionNotificationOriginal instanceof PromotionNotificationType )
        {
          childPromotion.addPromotionNotification( (PromotionNotificationType)promotionNotificationOriginal.clone() );
        }
        else
        {
          childPromotion.addPromotionNotification( (ClaimFormNotificationType)promotionNotificationOriginal.clone() );
        }
      }

      // promotionDAO.save( childPromotion );

    }
    catch( CloneNotSupportedException cnse )
    {
      throw new ServiceErrorException( "Promotion (id: " + parentPromotion.getId() + ") failed cloning", cnse );
    }

    return childPromotion;

  }

  /**
   * Overridden from @see com.biperf.core.service.promotion.PromotionService#isParticipantMemberOfPromotionAudience(com.biperf.core.domain.participant.Participant, com.biperf.core.domain.promotion.Promotion, boolean, com.biperf.core.domain.hierarchy.Node, boolean)
   * @param participant
   * @param promotion
   * @param isSubmitter
   * @param submitterNode
   * @return
   */
  public boolean isParticipantMemberOfPromotionAudience( Participant participant, Promotion promotion, boolean isSubmitter, Node submitterNode )
  {
    boolean isValid = false;

    // Confirm participant is still member of their audience
    if ( isSubmitter )
    {
      isValid = audienceService.isParticipantInPrimaryAudience( promotion, participant );
    }
    else
    {
      if ( promotion.isProductClaimPromotion() )
      {
        // Hack to skip tiered because for some reason promo setup is collecting audience for
        // tiered.
        if ( ! ( (ProductClaimPromotion)promotion ).getPayoutType().getCode().equals( PromotionPayoutType.TIERED ) )
        {
          isValid = audienceService.isParticipantInSecondaryAudience( promotion, participant, submitterNode );
        }
      }
      else if ( promotion.isGoalQuestOrChallengePointPromotion() )
      {
        GoalQuestPromotion gqPromo = (GoalQuestPromotion)promotion;
        if ( gqPromo.getPartnerAudienceType() != null && gqPromo.getAwardType() != null && gqPromo.getAwardType().getCode().equals( PromotionAwardsType.POINTS ) )
        {
          AssociationRequestCollection ascRequestCollection = new AssociationRequestCollection();
          PromotionAssociationRequest promoAsscoiationRequest = new PromotionAssociationRequest( PromotionAssociationRequest.PARTNER_AUDIENCES );
          ascRequestCollection.add( promoAsscoiationRequest );
          gqPromo = (GoalQuestPromotion)promotionDAO.getPromotionByIdWithAssociations( gqPromo.getId(), ascRequestCollection );
          isValid = audienceService.isUserInPromotionPartnerAudiences( participant, gqPromo.getPartnerAudiences() );
        }
      }
      else
      {
        isValid = audienceService.isParticipantInSecondaryAudience( promotion, participant, submitterNode );
      }
    }
    return isValid;
  }

  private boolean shouldFilterNodes( Promotion promotion )
  {
    if ( promotion.getSecondaryAudienceType() != null && ( promotion.getSecondaryAudienceType().isSpecificNodeType() || promotion.getSecondaryAudienceType().isSpecificNodeAndBelowType()
        || promotion.getSecondaryAudienceType().isSpecifyAudienceType() || promotion.getSecondaryAudienceType().isSameAsPrimaryType() && promotion.getPrimaryAudienceType().isSpecifyAudienceType() ) )
    {
      return true;
    }
    return false;
  }

  /**
   * Filters the list of promotions by the promotionType param. Overridden from
   *
   * @see com.biperf.core.service.promotion.PromotionService#getAllNonExpiredByType(com.biperf.core.domain.enums.PromotionType)
   * @param promotionType
   * @return List
   */
  public List getAllNonExpiredByType( PromotionType promotionType )
  {

    List promotionListByType = new ArrayList();

    Promotion promotion;

    for ( Iterator promotionListIterator = getAllNonExpired().iterator(); promotionListIterator.hasNext(); )
    {
      promotion = (Promotion)promotionListIterator.next();
      if ( promotion.getPromotionType().equals( promotionType ) )
      {
        promotionListByType.add( promotion );
      }
    }

    return promotionListByType;

  }

  /**
   * Builds a Set of participants for the giver audience who are not budget owners. Overridden from
   *
   * @see com.biperf.core.service.promotion.PromotionService#getParticipantsWithoutBudgetAllocationWithinPromotion(java.lang.Long)
   * @param promotionId
   * @return Set
   */
  public Set getParticipantsWithoutBudgetAllocationWithinPromotion( Long promotionId, Long budgetSegmentId )
  {

    // Get the RecognitionPromotion with the BudgetMaster in question.
    RecognitionPromotion recognitionPromotion = (RecognitionPromotion)this.promotionDAO.getPromotionById( promotionId );

    List budgetsFromBudgetMaster = budgetMasterDAO.getBudgetsByBudgetSegmentId( budgetSegmentId );

    // Get the list of users on this promotion's budgetMaster who have budgets
    List participantsWithBudgets = new ArrayList();

    for ( Iterator budgetsFromBudgetMasterIter = budgetsFromBudgetMaster.iterator(); budgetsFromBudgetMasterIter.hasNext(); )
    {
      Budget budget = (Budget)budgetsFromBudgetMasterIter.next();
      if ( budget.getUser() != null )
      {
        participantsWithBudgets.add( budget.getUser() );
      }
    }

    Set participantsInGiverAudience = new LinkedHashSet();

    // Check to see if this promotion uses all active participants or a giversAudience
    if ( recognitionPromotion.getPrimaryAudienceType().isAllActivePaxType() )
    {
      participantsInGiverAudience.addAll( this.participantDAO.getAllActive() );
    }
    else
    {

      // Get the list of users in the budgetMaster for the promotion.
      Set giverAudience = recognitionPromotion.getPromotionPrimaryAudiencesAsAudiences();

      for ( Iterator giverAudienceIter = giverAudience.iterator(); giverAudienceIter.hasNext(); )
      {

        Audience audience = (Audience)giverAudienceIter.next();

        if ( audience instanceof PaxAudience )
        {

          List audienceParticipants = ( (PaxAudience)audience ).getAudienceParticipants();

          AudienceParticipant audienceParticipant;
          for ( Iterator audienceParticipantsIter = audienceParticipants.iterator(); audienceParticipantsIter.hasNext(); )
          {
            audienceParticipant = (AudienceParticipant)audienceParticipantsIter.next();
            if ( audienceParticipant.getParticipant().isActive() )
            {
              participantsInGiverAudience.add( audienceParticipant.getParticipant() );
            }
          }

        }
        else
        {

          CriteriaAudience criteriaAudience = (CriteriaAudience)audience;

          Hierarchy primaryHierarchy = hierarchyService.getPrimaryHierarchy();
          Long hierarchyId = primaryHierarchy.getId();

          participantsInGiverAudience.addAll( listBuilderService.searchParticipants( criteriaAudience, hierarchyId, true, new LinkedHashSet(), true, true ) );

        }
      }
    }

    // Compare the list of participants in the audience with the list of participants which have
    // budgets in this promotion's budgetMaster
    Set participantsWithoutBudgets = new LinkedHashSet();

    if ( participantsWithBudgets.size() == 0 )
    {

      participantsWithoutBudgets = participantsInGiverAudience;

    }
    else if ( !participantsWithBudgets.containsAll( participantsInGiverAudience ) )
    {
      // Otherwise there are participants in the giver's audience that do not have budgets assigned.
      for ( Iterator participantsInGiverAudienceIter = participantsInGiverAudience.iterator(); participantsInGiverAudienceIter.hasNext(); )
      {
        Participant participant = (Participant)participantsInGiverAudienceIter.next();
        if ( !participantsWithBudgets.contains( participant ) )
        {
          participantsWithoutBudgets.add( participant );
        }
      }
    }

    return participantsWithoutBudgets;

  }

  /**
   * Builds a list of participants for the giver audience who are not budget owners.
   *
   * @param promotionId
   * @return List
   */
  public Set getParticipantsWithoutPublicRecogBudgetAllocationWithinPromotion( Long promotionId, Long budgetSegmentId )
  {
    // Get some information from the promotion up front, avoids this if-statement in several places
    Promotion promotion = this.promotionDAO.getPromotionById( promotionId );
    PublicRecognitionAudienceType publicRecAudienceType = null;
    Set giverAudience = null;

    if ( promotion == null )
    {
      return new HashSet<>();
    }
    else if ( promotion.isRecognitionPromotion() )
    {
      RecognitionPromotion recPromo = (RecognitionPromotion)promotion;
      publicRecAudienceType = recPromo.getPublicRecognitionAudienceType();
      giverAudience = recPromo.getPublicRecognitionAudiencesAsAudiences();
    }
    else if ( promotion.isNominationPromotion() )
    {
      NominationPromotion nomPromo = (NominationPromotion)promotion;
      publicRecAudienceType = nomPromo.getPublicRecognitionAudienceType();
      giverAudience = nomPromo.getPublicRecognitionAudiencesAsAudiences();
    }

    List budgetsFromBudgetMaster = budgetMasterDAO.getBudgetsByBudgetSegmentId( budgetSegmentId );

    // Get the list of users on this promotion's budgetMaster who have budgets
    List participantsWithBudgets = new ArrayList();

    for ( Iterator budgetsFromBudgetMasterIter = budgetsFromBudgetMaster.iterator(); budgetsFromBudgetMasterIter.hasNext(); )
    {
      Budget budget = (Budget)budgetsFromBudgetMasterIter.next();
      if ( budget.getUser() != null )
      {
        participantsWithBudgets.add( budget.getUser() );
      }
    }

    Set participantsInGiverAudience = new LinkedHashSet();

    // Check to see if this promotion uses all active participants or a giversAudience
    if ( publicRecAudienceType.equals( PublicRecognitionAudienceType.lookup( PublicRecognitionAudienceType.ALL_ACTIVE_PAX_CODE ) ) )
    {
      participantsInGiverAudience.addAll( this.participantDAO.getAllActive() );
    }
    else
    {
      for ( Iterator giverAudienceIter = giverAudience.iterator(); giverAudienceIter.hasNext(); )
      {

        Audience audience = (Audience)giverAudienceIter.next();

        if ( audience instanceof PaxAudience )
        {

          List audienceParticipants = ( (PaxAudience)audience ).getAudienceParticipants();

          AudienceParticipant audienceParticipant;
          for ( Iterator audienceParticipantsIter = audienceParticipants.iterator(); audienceParticipantsIter.hasNext(); )
          {
            audienceParticipant = (AudienceParticipant)audienceParticipantsIter.next();
            participantsInGiverAudience.add( audienceParticipant.getParticipant() );
          }

        }
        else
        {

          CriteriaAudience criteriaAudience = (CriteriaAudience)audience;

          Hierarchy primaryHierarchy = hierarchyService.getPrimaryHierarchy();
          Long hierarchyId = primaryHierarchy.getId();

          participantsInGiverAudience.addAll( listBuilderService.searchParticipants( criteriaAudience, hierarchyId, true, new LinkedHashSet(), true, true ) );

        }
      }
    }

    // Compare the list of participants in the audience with the list of participants which have
    // budgets in this promotion's budgetMaster
    Set participantsWithoutBudgets = new LinkedHashSet();

    if ( participantsWithBudgets.size() == 0 )
    {

      participantsWithoutBudgets = participantsInGiverAudience;

    }
    else if ( !participantsWithBudgets.containsAll( participantsInGiverAudience ) )
    {
      // Otherwise there are participants in the giver's audience that do not have budgets assigned.
      for ( Iterator participantsInGiverAudienceIter = participantsInGiverAudience.iterator(); participantsInGiverAudienceIter.hasNext(); )
      {
        Participant participant = (Participant)participantsInGiverAudienceIter.next();
        if ( !participantsWithBudgets.contains( participant ) )
        {
          participantsWithoutBudgets.add( participant );
        }
      }

    }

    return participantsWithoutBudgets;
  }

  /**
   * Builds a Set of nodes for the giver audience who are not budget owners. Overridden from
   *
   * @see com.biperf.core.service.promotion.PromotionService#getNodesWithoutBudgetAllocationWithinPromotion(java.lang.Long)
   * @param promotionId
   * @return Set
   */
  public Set getNodesWithoutBudgetAllocationWithinPromotion( Long promotionId, Long budgetSegmentId )
  {

    // Get the RecognitionPromotion with the BudgetMaster in question.
    RecognitionPromotion recognitionPromotion = (RecognitionPromotion)this.promotionDAO.getPromotionById( promotionId );

    List budgetsFromBudgetMaster = budgetMasterDAO.getBudgetsByBudgetSegmentId( budgetSegmentId );

    // Get the list of nodes on this promotion's budgetMaster who have budgets.
    Set nodesWithBudgets = new LinkedHashSet();

    // From the list of budgets on this promotion's budget master, build a list of nodes that have
    // assigned budgets.
    for ( Iterator budgetsFromBudgetMasterIter = budgetsFromBudgetMaster.iterator(); budgetsFromBudgetMasterIter.hasNext(); )
    {
      Budget budget = (Budget)budgetsFromBudgetMasterIter.next();
      if ( budget.getNode() != null )
      {
        nodesWithBudgets.add( budget.getNode() );
      }
    }

    Set participantNodesForParticipantsInGiverAudience = new LinkedHashSet();

    // Get the list of participants in the giverAudience.
    List participantsInParticipantAudience = new ArrayList();

    // Determine if the giver audience on the promotion is all participants or an audience
    // and build the appropriate list of participants.
    if ( recognitionPromotion.getPrimaryAudienceType().isAllActivePaxType() )
    {

      participantsInParticipantAudience.addAll( this.participantDAO.getAllActive() );

    }
    else
    {

      // Get the list of nodes which the users within the giver audience are assigned to.
      Set giverAudience = recognitionPromotion.getPromotionPrimaryAudiencesAsAudiences();

      // Loop over all audiences within the promotion's giverAudience.
      for ( Iterator giverAudienceIter = giverAudience.iterator(); giverAudienceIter.hasNext(); )
      {

        Audience audience = (Audience)giverAudienceIter.next();

        if ( audience instanceof PaxAudience )
        {

          List audienceParticipants = ( (PaxAudience)audience ).getAudienceParticipants();

          AudienceParticipant audienceParticipant;
          for ( Iterator audienceParticipantsIter = audienceParticipants.iterator(); audienceParticipantsIter.hasNext(); )
          {
            audienceParticipant = (AudienceParticipant)audienceParticipantsIter.next();
            participantsInParticipantAudience.add( audienceParticipant.getParticipant() );
          }

        }
        else
        {

          CriteriaAudience criteriaAudience = (CriteriaAudience)audience;

          Hierarchy primaryHierarchy = hierarchyService.getPrimaryHierarchy();
          Long hierarchyId = primaryHierarchy.getId();

          participantsInParticipantAudience.addAll( listBuilderService.searchParticipants( criteriaAudience, hierarchyId, true, new LinkedHashSet(), true, true ) );

        }

      }

    }

    // Get all nodes for a participant in the promotion's giverAudience.
    for ( Iterator participantsInParticipantAudienceIter = participantsInParticipantAudience.iterator(); participantsInParticipantAudienceIter.hasNext(); )
    {
      Participant participant = (Participant)participantsInParticipantAudienceIter.next();
      Set userNodes = participant.getUserNodes();
      List nodes = new ArrayList();

      for ( Iterator iter = userNodes.iterator(); iter.hasNext(); )
      {
        UserNode userNode = (UserNode)iter.next();
        if ( userNode.getNode().getHierarchy().isPrimary() )
        {
          nodes.add( userNode.getNode() );
        }
      }
      participantNodesForParticipantsInGiverAudience.addAll( nodes );
    }

    // Compare the list of nodes in the audience with the list of
    // nodes which have budgets in this promotion's budgetMaster.
    Set nodesWithoutBudgets = new LinkedHashSet();

    if ( nodesWithBudgets.size() == 0 )
    {

      // If there aren't any participants with budgets then all participants
      // in the giver's audience need to be assigned budgets.
      nodesWithoutBudgets = participantNodesForParticipantsInGiverAudience;

    }
    else if ( !nodesWithBudgets.containsAll( participantNodesForParticipantsInGiverAudience ) )
    {
      // Otherwise there are nodes assigned to participants in the giver's audience that do not have
      // budgets assigned.
      for ( Iterator participantNodesForParticipantsInGiverAudienceIter = participantNodesForParticipantsInGiverAudience.iterator(); participantNodesForParticipantsInGiverAudienceIter.hasNext(); )
      {
        Node node = (Node)participantNodesForParticipantsInGiverAudienceIter.next();
        if ( !nodesWithBudgets.contains( node ) )
        {
          nodesWithoutBudgets.add( node );
        }
      }

    }

    return nodesWithoutBudgets;

  }

  public Set getNodeWithoutPublicRecogBudgetAllocationWithinPromotion( Long promotionId, Long budgetSegmentId )
  {
    // Get some information from the promotion up front, avoids this if statement in several places
    Promotion promotion = this.promotionDAO.getPromotionById( promotionId );
    PublicRecognitionAudienceType publicRecAudienceType = null;
    Set giverAudience = null;

    if ( promotion == null )
    {
      return new HashSet<>();
    }
    else if ( promotion.isRecognitionPromotion() )
    {
      RecognitionPromotion recPromo = (RecognitionPromotion)promotion;
      publicRecAudienceType = recPromo.getPublicRecognitionAudienceType();
      giverAudience = recPromo.getPromotionPublicRecognitionAudiences();
    }
    else if ( promotion.isNominationPromotion() )
    {
      NominationPromotion nomPromo = (NominationPromotion)promotion;
      publicRecAudienceType = nomPromo.getPublicRecognitionAudienceType();
      giverAudience = nomPromo.getPromotionPublicRecognitionAudiences();
    }

    List budgetsFromBudgetMaster = budgetMasterDAO.getBudgetsByBudgetSegmentId( budgetSegmentId );

    // Get the list of nodes on this promotion's budgetMaster who have budgets.
    Set nodesWithBudgets = new LinkedHashSet();

    // From the list of budgets on this promotion's budget master, build a list of nodes that have
    // assigned budgets.
    for ( Iterator budgetsFromBudgetMasterIter = budgetsFromBudgetMaster.iterator(); budgetsFromBudgetMasterIter.hasNext(); )
    {
      Budget budget = (Budget)budgetsFromBudgetMasterIter.next();
      if ( budget.getNode() != null )
      {
        nodesWithBudgets.add( budget.getNode() );
      }
    }

    Set participantNodesForParticipantsInGiverAudience = new LinkedHashSet();

    // Get the list of participants in the giverAudience.
    List participantsInParticipantAudience = new ArrayList();

    // Determine if the giver audience on the promotion is all participants or an audience
    // and build the appropriate list of participants.
    if ( publicRecAudienceType.equals( PublicRecognitionAudienceType.lookup( PublicRecognitionAudienceType.ALL_ACTIVE_PAX_CODE ) ) )
    {
      participantsInParticipantAudience.addAll( this.participantDAO.getAllActive() );
    }
    else
    {
      // Loop over all audiences within the promotion's giverAudience.
      for ( Iterator giverAudienceIter = giverAudience.iterator(); giverAudienceIter.hasNext(); )
      {

        PromotionPublicRecognitionAudience audience = (PromotionPublicRecognitionAudience)giverAudienceIter.next();

        if ( audience.getAudience() instanceof PaxAudience )
        {

          List audienceParticipants = ( (PaxAudience)audience.getAudience() ).getAudienceParticipants();

          AudienceParticipant audienceParticipant;
          for ( Iterator audienceParticipantsIter = audienceParticipants.iterator(); audienceParticipantsIter.hasNext(); )
          {
            audienceParticipant = (AudienceParticipant)audienceParticipantsIter.next();
            participantsInParticipantAudience.add( audienceParticipant.getParticipant() );
          }

        }
        else
        {

          CriteriaAudience criteriaAudience = (CriteriaAudience)audience.getAudience();

          Hierarchy primaryHierarchy = hierarchyService.getPrimaryHierarchy();
          Long hierarchyId = primaryHierarchy.getId();

          participantsInParticipantAudience.addAll( listBuilderService.searchParticipants( criteriaAudience, hierarchyId, true, new LinkedHashSet(), true, true ) );

        }

      }

    }

    // Get all nodes for a participant in the promotion's giverAudience.
    for ( Iterator participantsInParticipantAudienceIter = participantsInParticipantAudience.iterator(); participantsInParticipantAudienceIter.hasNext(); )
    {
      Participant participant = (Participant)participantsInParticipantAudienceIter.next();
      Set userNodes = participant.getUserNodes();
      List nodes = new ArrayList();

      for ( Iterator iter = userNodes.iterator(); iter.hasNext(); )
      {
        UserNode userNode = (UserNode)iter.next();
        if ( userNode.getNode().getHierarchy().isPrimary() )
        {
          nodes.add( userNode.getNode() );
        }
      }
      participantNodesForParticipantsInGiverAudience.addAll( nodes );
    }

    // Compare the list of nodes in the audience with the list of
    // nodes which have budgets in this promotion's budgetMaster.
    Set nodesWithoutBudgets = new LinkedHashSet();

    if ( nodesWithBudgets.size() == 0 )
    {

      // If there aren't any participants with budgets then all participants
      // in the giver's audience need to be assigned budgets.
      nodesWithoutBudgets = participantNodesForParticipantsInGiverAudience;

    }
    else if ( !nodesWithBudgets.containsAll( participantNodesForParticipantsInGiverAudience ) )
    {
      // Otherwise there are nodes assigned to participants in the giver's audience that do not have
      // budgets assigned.
      for ( Iterator participantNodesForParticipantsInGiverAudienceIter = participantNodesForParticipantsInGiverAudience.iterator(); participantNodesForParticipantsInGiverAudienceIter.hasNext(); )
      {
        Node node = (Node)participantNodesForParticipantsInGiverAudienceIter.next();
        if ( !nodesWithBudgets.contains( node ) )
        {
          nodesWithoutBudgets.add( node );
        }
      }

    }

    return nodesWithoutBudgets;

  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.promotion.PromotionService#sendCardSelectionEmail(java.lang.String,
   *      java.lang.Long)
   * @param emailAddress
   * @param promotionId
   */
  public void sendCardSelectionEmail( String emailAddress, Long promotionId )
  {
    Promotion promotion = promotionDAO.getPromotionById( promotionId );

    Map parameterMap = new HashMap();
    parameterMap.put( "promotionId", promotion.getId() );
    String linkToEcardPage = ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                                   "/promotionRecognition/promotionEcard.do?method=displayClientUser",
                                                                   parameterMap );

    String linkToContactUsPage = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/contactUs.do?method=view&isFullPage=true";

    Mailing clientEcardMailing = new Mailing();

    Message message = messageService.getMessageByCMAssetCode( MessageService.ECARD_SELECTION_MESSAGE_CM_ASSET_CODE );

    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
    mailingRecipient.setPreviewEmailAddress( emailAddress );

    MailingRecipientData promotionName = new MailingRecipientData();
    promotionName.setKey( "promotionName" );
    promotionName.setValue( promotion.getName() );
    mailingRecipient.addMailingRecipientData( promotionName );

    MailingRecipientData promotionStartDate = new MailingRecipientData();
    promotionStartDate.setKey( "promotionStartDate" );
    promotionStartDate.setValue( DateUtils.toDisplayString( promotion.getSubmissionStartDate() ) );
    mailingRecipient.addMailingRecipientData( promotionStartDate );

    MailingRecipientData linkToEcards = new MailingRecipientData();
    linkToEcards.setKey( "linkToEcards" );
    linkToEcards.setValue( linkToEcardPage );
    mailingRecipient.addMailingRecipientData( linkToEcards );

    MailingRecipientData linkToContactUs = new MailingRecipientData();
    linkToContactUs.setKey( "linkToContactUs" );
    linkToContactUs.setValue( linkToContactUsPage );
    mailingRecipient.addMailingRecipientData( linkToContactUs );

    clientEcardMailing.addMailingRecipient( mailingRecipient );

    clientEcardMailing.setGuid( GuidUtils.generateGuid() );
    clientEcardMailing.setSender( "Test - This is a test statement." );
    clientEcardMailing.setMailingType( MailingType.lookup( MailingType.PROCESS_EMAIL ) );
    clientEcardMailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
    clientEcardMailing.setMessage( message );

    mailingService.submitMailing( clientEcardMailing, null );
  }

  public Budget getAvailableBudget( Long promotionId, Long participantId, Long nodeId )
  {
    // Fix for bug#56006,55519 start
    return getAvailableBudget( promotionId, participantId, nodeId, null );
    // Fix for bug#56006,55519 end
  }

  // Fix for bug#56006,55519 start
  public Budget getAvailableBudget( Long promotionId, Long participantId, Long nodeId, BudgetSegment budgetSegment )
  { // Fix for bug#56006,55519 end
    Promotion promotion = promotionDAO.getPromotionById( promotionId );
    Participant participant = null;
    if ( participantId != null )
    {
      participant = participantDAO.getParticipantById( participantId );
    }
    Node node = null;
    if ( nodeId != null )
    {
      node = nodeDAO.getNodeById( nodeId );
    }

    // Fix for bug#56006,55519 start
    if ( budgetSegment != null )
    {
      return getAvailableBudget( promotion, participant, node, budgetSegment );
    }

    return getAvailableBudget( promotion, participant, node );
    // Fix for bug#56006,55519 end

  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.promotion.PromotionService#getAvailableBudget(java.lang.Long,
   *      java.lang.Long, java.lang.Long)
   * @param promotionId
   * @param participantId
   * @param nodeId
   * @return Budget
   */
  public Budget getAvailableBudget( Promotion promotion, Participant participant, Node node )
  {
    // Fix for bug#56006,55519 start
    return getAvailableBudget( promotion, participant, node, null );
    // Fix for bug#56006,55519 end
  }

  public Budget getNominationPromotionAvailableBudgetByAwardType( Promotion promotion, Participant participant, Node node, String awardType )
  {
    // Fix for bug#56006,55519 start
    return getNominationPromotionAvailableBudget( promotion, participant, node, null, awardType );
    // Fix for bug#56006,55519 end
  }

  public Budget getNominationPromotionAvailableBudget( Promotion promotion, Participant participant, Node node, BudgetSegment budgetSegment, String awardType )
  {
    // Fix for bug#56006,55519 end
    if ( !promotion.isBudgetUsed() && !promotion.isCashBudgetUsed() )
    {
      throw new BeaconRuntimeException( "getNominationPromotionAvailableBudget() not applicable for promotions of type: " + promotion.getPromotionType() );
    }

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );

    BudgetMaster budgetMaster = null;
    if ( promotion.isCashBudgetUsed() && awardType != null && awardType.equals( PromotionAwardsType.CASH ) )
    {
      budgetMaster = budgetMasterService.getBudgetMasterById( promotion.getCashBudgetMaster().getId(), associationRequestCollection );
    }
    else
    {
      budgetMaster = budgetMasterService.getBudgetMasterById( promotion.getBudgetMaster().getId(), associationRequestCollection );
    }

    String paxTimeZoneId = this.getUserService().getUserTimeZoneByUserCountry( participant.getId() );

    // Fix for bug#56006,55519 start
    BudgetSegment currentBudgetSegment;
    if ( budgetSegment == null )
    {
      currentBudgetSegment = budgetMaster.getCurrentBudgetSegment( paxTimeZoneId );
    }
    else
    {
      currentBudgetSegment = budgetSegment;
    }
    // Fix for bug#56006,55519 end

    if ( currentBudgetSegment == null )
    {
      return null;
    }

    if ( budgetMaster.getBudgetType().getCode().equals( BudgetType.CENTRAL_BUDGET_TYPE ) )
    {
      // Get budget - Only ever one budget for CENTRAL_BUDGET_TYPE.
      // NOTE: Actually hydrating this budget was costing a very large many seconds (upwards of 10
      // seconds in some cases) to retrieve,
      // and since we only care that one EXISTS, use a nice helper method
      int numberOfBudgets = budgetMasterDAO.getBudgetCountForBudgetSegment( currentBudgetSegment.getId() );
      if ( numberOfBudgets > 0 )
      {
        // temporary fix - sometimes we need the budget values to avoid null pointer exception
        // this method will pull only data on the Budget table (no additional calls)
        return budgetMasterDAO.getActiveBudgetForCentralBudgetMasterBySegment( currentBudgetSegment.getId() );
      }
      else
      {
        return null;
      }
    }

    Budget availableBudget = null;

    // Check to see if budgetMaster even exists
    // Check to see if participant is in giver (submitter) audience
    // Check to see if user is in node that is passed
    if ( budgetMaster == null || !isParticipantMemberOfPromotionAudience( participant, promotion, true, node ) || !participant.isInNode( node ) )
    {
      return availableBudget;
    }

    // Set budgets = budgetMaster.getBudgets();
    // List budgets = budgetMasterDAO.getAllBudgetsForUser(participant.getId());

    if ( budgetMaster.getBudgetType().getCode().equals( BudgetType.NODE_BUDGET_TYPE ) )
    {
      availableBudget = budgetMasterDAO.getBudgetForNodeByBudgetSegmentId( currentBudgetSegment.getId(), node.getId() );
    } // if
    else if ( budgetMaster.getBudgetType().getCode().equals( BudgetType.PAX_BUDGET_TYPE ) )
    {
      availableBudget = budgetMasterDAO.getBudgetForUserbyBudgetSegmentId( currentBudgetSegment.getId(), participant.getId() );
    }

    return availableBudget;
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.promotion.PromotionService#getAvailableBudget(java.lang.Long,
   *      java.lang.Long, java.lang.Long, BudgetSegment)
   * @param promotionId
   * @param participantId
   * @param nodeId
   * @param budgetSegment
   * @return Budget
   */
  // Fix for bug#56006,55519 start
  public Budget getAvailableBudget( Promotion promotion, Participant participant, Node node, BudgetSegment budgetSegment )
  {
    // Fix for bug#56006,55519 end
    if ( !promotion.isBudgetUsed() )
    {
      throw new BeaconRuntimeException( "getAvailableBudget() not applicable for promotions of type: " + promotion.getPromotionType() );
    }

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
    BudgetMaster budgetMaster = budgetMasterService.getBudgetMasterById( promotion.getBudgetMaster().getId(), associationRequestCollection );

    String paxTimeZoneId = this.getUserService().getUserTimeZoneByUserCountry( participant.getId() );

    // Fix for bug#56006,55519 start
    BudgetSegment currentBudgetSegment;
    if ( budgetSegment == null )
    {
      currentBudgetSegment = budgetMaster.getCurrentBudgetSegment( paxTimeZoneId );
    }
    else
    {
      currentBudgetSegment = budgetSegment;
    }
    // Fix for bug#56006,55519 end

    if ( currentBudgetSegment == null )
    {
      return null;
    }

    if ( budgetMaster.getBudgetType().getCode().equals( BudgetType.CENTRAL_BUDGET_TYPE ) )
    {
      // Get budget - Only ever one budget for CENTRAL_BUDGET_TYPE.
      // NOTE: Actually hydrating this budget was costing a very large many seconds (upwards of 10
      // seconds in some cases) to retrieve,
      // and since we only care that one EXISTS, use a nice helper method
      int numberOfBudgets = budgetMasterDAO.getBudgetCountForBudgetSegment( currentBudgetSegment.getId() );
      if ( numberOfBudgets > 0 )
      {
        // temporary fix - sometimes we need the budget values to avoid null pointer exception
        // this method will pull only data on the Budget table (no additional calls)
        return budgetMasterDAO.getActiveBudgetForCentralBudgetMasterBySegment( currentBudgetSegment.getId() );
      }
      else
      {
        return null;
      }
    }

    Budget availableBudget = null;

    // Check to see if budgetMaster even exists
    // Check to see if participant is in giver (submitter) audience
    // Check to see if user is in node that is passed
    if ( budgetMaster == null || !isParticipantMemberOfPromotionAudience( participant, promotion, true, node ) || !participant.isInNode( node ) )
    {
      return availableBudget;
    }

    // Set budgets = budgetMaster.getBudgets();
    // List budgets = budgetMasterDAO.getAllBudgetsForUser(participant.getId());

    if ( budgetMaster.getBudgetType().getCode().equals( BudgetType.NODE_BUDGET_TYPE ) )
    {
      availableBudget = budgetMasterDAO.getBudgetForNodeByBudgetSegmentId( currentBudgetSegment.getId(), node.getId() );
      /* client customization start */
      if (promotion.isUtilizeParentBudgets())
      {
        if (availableBudget == null || availableBudget.getStatus().getCode().equals( BudgetStatusType.CLOSED ) || availableBudget.getCurrentValue().compareTo(BigDecimal.ZERO) == 0)
        {
          availableBudget = getFirstBudgetUpHierarchy(node.getId(), budgetMaster, currentBudgetSegment.getId()) ;
        }
        //set this to true for all utilize parent budget promotions so the budget owner name displays on the rec submit screen
        if(null!=availableBudget)
        {
          availableBudget.setParentBudgetUsed( true );
        }
        else 
        {
          if ( node.getParentNode() != null )
          {
            //find budget up the hierarchy starting with parent node because the node budget was looked up earlier and not found or has no balance
            //we want to return the first budget above the paxs node
            availableBudget = getFirstBudgetUpHierarchyRegardlessOfBalance(node.getParentNode().getId(), budgetMaster, currentBudgetSegment.getId()) ;
            if(null!=availableBudget)
            {
              availableBudget.setParentBudgetUsed( true );
            }
          }
        }
      }
      /* client customization end */
      
      
    } // if
    else if ( budgetMaster.getBudgetType().getCode().equals( BudgetType.PAX_BUDGET_TYPE ) )
    {
      availableBudget = budgetMasterDAO.getBudgetForUserbyBudgetSegmentId( currentBudgetSegment.getId(), participant.getId() );
    }

    return availableBudget;
  }
  
  /* client customization start */
  public Budget getFirstBudgetUpHierarchy ( Long nodeId, BudgetMaster budgetMaster, Long budgetSegmentId ) 
  {
    Node node = nodeDAO.getNodeById(nodeId);
    //Look for active budget when trying to find one in the higher hierarchy levels
    Budget budget = budgetMasterDAO.getActiveBudgetForNodebyBudgetMasterId(budgetMaster.getId(), nodeId, budgetSegmentId);
    if ( null !=budget ) {
      if ( budget.getCurrentValue().compareTo(BigDecimal.ZERO) != 0 ) 
      {
        return budget;        
      }
      else {
          if (node.getParentNode() != null) {
              return getFirstBudgetUpHierarchy(node.getParentNode().getId(), budgetMaster, budgetSegmentId) ;              
          }
          else {
              return null;
          }
      }
    }
    else {
        if (node.getParentNode() != null) {
            return getFirstBudgetUpHierarchy(node.getParentNode().getId(), budgetMaster, budgetSegmentId) ;              
        }
        else {
            return null;
        }

    }
  }

  public Budget getFirstBudgetUpHierarchyRegardlessOfBalance ( Long nodeId, BudgetMaster budgetMaster, Long budgetSegmentId ) 
  {
    Node node = nodeDAO.getNodeById(nodeId);
    //Look for active budget when trying to find one in the higher hierarchy levels
    Budget budget = budgetMasterDAO.getActiveBudgetForNodebyBudgetMasterId(budgetMaster.getId(), nodeId, budgetSegmentId);
    if ( null != budget ) 
    {
      return budget;        
    }
    else 
    {
      if ( node.getParentNode() != null ) 
      {
        return getFirstBudgetUpHierarchyRegardlessOfBalance(node.getParentNode().getId(), budgetMaster, budgetSegmentId) ;              
      }
      else 
      {
        return null;
      }
    }
  }
  /* client customization end */

  // Fix for bug 66821
  @Override
  public Budget getRecognitionAvailableBudget( Long promotionId, Long participantId, Long nodeId, BudgetSegment budgetSegment )
  {
    Promotion promotion = promotionDAO.getPromotionById( promotionId );
    Participant participant = null;
    if ( participantId != null )
    {
      participant = participantDAO.getParticipantById( participantId );
    }
    Node node = null;
    if ( nodeId != null )
    {
      node = nodeDAO.getNodeById( nodeId );
    }
    if ( !promotion.isBudgetUsed() )
    {
      throw new BeaconRuntimeException( "getAvailableBudget() not applicable for promotions of type: " + promotion.getPromotionType() );
    }

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
    BudgetMaster budgetMaster = budgetMasterService.getBudgetMasterById( promotion.getBudgetMaster().getId(), associationRequestCollection );

    String paxTimeZoneId = this.getUserService().getUserTimeZoneByUserCountry( participant.getId() );

    // Fix for bug#56006,55519 start
    BudgetSegment currentBudgetSegment;
    if ( budgetSegment == null )
    {
      currentBudgetSegment = budgetMaster.getCurrentBudgetSegment( paxTimeZoneId );
    }
    else
    {
      currentBudgetSegment = budgetSegment;
    }

    if ( currentBudgetSegment == null )
    {
      return null;
    }

    if ( budgetMaster.getBudgetType().getCode().equals( BudgetType.CENTRAL_BUDGET_TYPE ) )
    {
      // Get budget - Only ever one budget for CENTRAL_BUDGET_TYPE.
      // NOTE: Actually hydrating this budget was costing a very large many seconds (upwards of 10
      // seconds in some cases) to retrieve,
      // and since we only care that one EXISTS, use a nice helper method
      int numberOfBudgets = budgetMasterDAO.getBudgetCountForBudgetSegment( currentBudgetSegment.getId() );
      if ( numberOfBudgets > 0 )
      {
        // temporary fix - sometimes we need the budget values to avoid null pointer exception
        // this method will pull only data on the Budget table (no additional calls)
        return budgetMasterDAO.getActiveBudgetForCentralBudgetMasterBySegment( currentBudgetSegment.getId() );
      }
      else
      {
        return null;
      }
    }

    Budget availableBudget = null;

    // Check to see if budgetMaster even exists
    // Check to see if participant is in giver (submitter) audience
    if ( budgetMaster == null || !isParticipantMemberOfPromotionAudience( participant, promotion, true, node ) )
    {
      return availableBudget;
    }

    if ( budgetMaster.getBudgetType().getCode().equals( BudgetType.NODE_BUDGET_TYPE ) )
    {
      availableBudget = budgetMasterDAO.getBudgetForNodeByBudgetSegmentId( currentBudgetSegment.getId(), node.getId() );
      
      /* client customization start wip# 25589 */
      if ( promotion.isUtilizeParentBudgets() )
      {
        if ( availableBudget == null || availableBudget.getStatus().getCode().equals( BudgetStatusType.CLOSED ) || availableBudget.getCurrentValue().compareTo( BigDecimal.ZERO ) == 0 )
        {
          availableBudget = getFirstBudgetUpHierarchy( node.getId(), budgetMaster, currentBudgetSegment.getId() );
        }
        // set this to true for all utilize parent budget promotions so the budget owner name
        // displays on the rec submit screen
        if ( null != availableBudget )
        {
          availableBudget.setParentBudgetUsed( true );
        }
        else
        {
          if ( node.getParentNode() != null )
          {
            // find budget up the hierarchy starting with parent node because the node budget was
            // looked up earlier and not found or has no balance
            // we want to return the first budget above the paxs node
            availableBudget = getFirstBudgetUpHierarchyRegardlessOfBalance( node.getParentNode().getId(), budgetMaster, currentBudgetSegment.getId() );
            if ( null != availableBudget )
            {
              availableBudget.setParentBudgetUsed( true );
            }
          }
        }
      }
      /* client customization end wip# 25589 */
      
    } // if
    else if ( budgetMaster.getBudgetType().getCode().equals( BudgetType.PAX_BUDGET_TYPE ) )
    {
      availableBudget = budgetMasterDAO.getBudgetForUserbyBudgetSegmentId( currentBudgetSegment.getId(), participant.getId() );
    }

    return availableBudget;
  }
  
  // Client customizations for WIP #42701 starts
  public boolean isCashPromo( Long promotionId )
  {
    return promotionDAO.isCashPromo( promotionId );
  }
  // Client customizations for WIP #42701 ends
  
  private boolean isValidLevelsForPrograms( List programIds ) throws ServiceErrorException
  {
    java.util.Map check = new java.util.HashMap();

    for ( int i = 0; i < programIds.size(); i++ )
    {
      String programId = (String)programIds.get( i );
      java.util.Collection levels = merchLevelService.getMerchlinqLevelDataWebService( programId ).getMerchLevel();
      Integer size = new Integer( levels.size() );
      check.put( size, null );

      // the size level should always be 1, since we want all
      // programs to have the sames number of levels
      if ( check.keySet().size() > 1 )
      {
        return false;
      }
    }

    return true;
  }

  // Fix for bug#56006,55519 end

  public int getMaximumLevelForPrograms( List programIds ) throws ServiceErrorException
  {
    if ( null == programIds || programIds.isEmpty() )
    {
      return 0;
    }

    if ( isValidLevelsForPrograms( programIds ) )
    {
      String id = (String)programIds.get( 0 );
      return merchLevelService.getMerchlinqLevelDataWebService( id ).getMerchLevel().size();
    }

    return 0;
  }

  public List<ParticipantPartner> getParticipantPartnersWhereSelectionEmailNotSentByPromotion( Long promotionId )
  {
    return participantPartnerDAO.getParticipantPartnersWhereSelectionEmailNotSentByPromotion( promotionId );
  }

  public List<ParticipantPartner> getPartnersByPromotionAndParticipantWithAssociations( Long promotionId, Long participantId, ParticipantAssociationRequest ascreq )
  {
    List partnerList = participantPartnerDAO.getPartnersByPromotionAndParticipant( promotionId, participantId );
    if ( ascreq != null )
    {
      if ( partnerList != null )
      {
        for ( int i = 0; i < partnerList.size(); i++ )
        {
          ascreq.execute( ( (ParticipantPartner)partnerList.get( i ) ).getPartner() );
        }
      }
    }
    return partnerList;
  }

  public List<ParticipantPartner> getParticipantsByPromotionAndPartnerWithAssociations( Long promotionId, Long partnerId, ParticipantAssociationRequest ascreq )
  {
    List partnerList = participantPartnerDAO.getParticipantsByPromotionAndPartner( promotionId, partnerId );
    if ( ascreq != null )
    {
      if ( partnerList != null )
      {
        for ( int i = 0; i < partnerList.size(); i++ )
        {
          ascreq.execute( ( (ParticipantPartner)partnerList.get( i ) ).getPartner() );
        }
      }
    }
    return partnerList;
  }

  public BigDecimal getPartnerAwardAmountByPromotionAndSequenceNo( Long promotionId, int seqNo )
  {
    return promotionDAO.getPartnerAwardAmountByPromoitonAndSequenceNo( promotionId, seqNo );
  }

  public long getRecognitionsSubmittedForPromotion( Long promotionId )
  {
    return promotionDAO.getRecognitionsSubmittedForPromotion( promotionId );
  }

  public long getNominationsSubmittedForPromotion( Long promotionId )
  {
    return promotionDAO.getNominationsSubmittedForPromotion( promotionId );
  }

  public long getProductClaimsSubmittedForPromotion( Long promotionId )
  {
    return promotionDAO.getProductClaimsSubmittedForPromotion( promotionId );
  }

  public long getQuizSubmittedForPromotion( Long promotionId )
  {
    return promotionDAO.getQuizSubmittedForPromotion( promotionId );
  }

  public List<AlertsValueBean> getAlertsList( AuthenticatedUser user, Long userId, boolean isModalWindow, List eligiblePromotions, boolean isMessagesPage )
  {
    List<AlertsValueBean> alertList = new ArrayList<AlertsValueBean>();
    List<Callable<List<AlertsValueBean>>> callables = Lists.newArrayList();

    Participant participant = participantService.getParticipantById( userId );
    ExecutorService executor = Executors.newWorkStealingPool();

    // Alerts Performance Tuning
    Map<Long, Promotion> eligiblePromoMap = buildPromotionMap( eligiblePromotions );
    if ( eligiblePromotions != null && eligiblePromotions.size() > 0 )
    {
      // Create live expired promotions
      // Callable<List<AlertsValueBean>> promotions = createLiveOrExpiredPromotions(
      // eligiblePromotions, participant );
      // if ( promotions != null )
      // {
      callables.add( createLiveOrExpiredPromotions( eligiblePromotions, participant, isMessagesPage ) );
      // }

      // GoalQuest/ChallengePoint alerts
      if ( systemVariableService.getPropertyByName( SystemVariableService.INSTALL_GOAL_QUEST ).getBooleanVal()
          || systemVariableService.getPropertyByName( SystemVariableService.INSTALL_CHALLENGEPOINT ).getBooleanVal() )
      {
        callables.add( createGQPromotionAlerts( eligiblePromotions, participant ) );
        callables.add( createGQPromotionSurveyAlerts( eligiblePromotions, participant ) );
      }

      // Survey promotions
      if ( systemVariableService.getPropertyByName( SystemVariableService.INSTALL_SURVEYS ).getBooleanVal() )
      {
        callables.add( createSurveyPromotionAlerts( eligiblePromotions, participant ) );
      }

      // Quiz alerts
      if ( systemVariableService.getPropertyByName( SystemVariableService.INSTALL_QUIZZES ).getBooleanVal() )
      {
        callables.add( createQuizPromotionsForAlers( eligiblePromotions, participant ) );
      }

      // Recognition
      if ( systemVariableService.getPropertyByName( SystemVariableService.INSTALL_RECOGNITION ).getBooleanVal() )
      {
        callables.add( createAwardGeneratorAlertForManger( eligiblePromotions, participant ) );
        if ( !NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
        {
          callables.add( createCelebrationAlertForManager( eligiblePromotions, participant ) );
        }
      }

      // Nominations
      if ( systemVariableService.getPropertyByName( SystemVariableService.INSTALL_NOMINATIONS ).getBooleanVal() )
      {
        callables.add( createApproveReminderSummaryAlerts( participant ) );
        callables.add( createNominatorRequestMoreInfoAlerts( participant ) );

        // Nomination winner list
        callables.add( createNominationMyWinnersList( participant ) );

        // pending nominations submission list
        callables.add( createInProgressNominationClaimsCount( eligiblePromotions, participant ) );
      }
      callables.add( createAwardReminderListForAlerts( eligiblePromotions, participant, eligiblePromoMap ) );
      callables.add( createBudgetEndAlertForAlerts( eligiblePromotions, participant ) );
    }

    // only managers see filestore objects
    if ( participant.isManager() || aznService.isUserInRole( AuthorizationService.ROLE_CODE_PROCESS_TEAM ) || aznService.isUserInRole( AuthorizationService.ROLE_CODE_BI_ADMIN )
        || aznService.isUserInRole( AuthorizationService.ROLE_CODE_PROJ_MGR ) || aznService.isUserInRole( AuthorizationService.ROLE_CODE_VIEW_REPORTS ) )
    {
      callables.add( createPendingFileDownloadsForAlerts( participant ) );
    }

    // instant poll
    if ( systemVariableService.getPropertyByName( SystemVariableService.INSTANT_POLL ).getBooleanVal() )
    {
      callables.add( createInstantPollForAlerts( participant ) );
    }
    // For New SA
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      CmxTranslateHelperBean cmxTranslateHelper = null;

      if ( !CmxTranslateHelperBean.isCmxDefaultLocale() )
      {
        Map<String, String> cmxTranslatedProgramMap = getCmxTranslateProgramName();
        if ( null != cmxTranslatedProgramMap && !cmxTranslatedProgramMap.isEmpty() )
        {
          cmxTranslateHelper = new CmxTranslateHelperBean( cmxTranslatedProgramMap );
        }
      }

      int numOfDays = systemVariableService.getPropertyByName( SystemVariableService.PURL_DAYS_TO_EXP ).getIntVal();
      callables.add( createPendingSAContributionsForAlerts( participant, cmxTranslateHelper ) );
      callables.add( createSACelebrationAlerts( participant, numOfDays, cmxTranslateHelper ) );
      callables.add( createSAAwardReminderListForAlerts( participant, cmxTranslateHelper ) );
    }
    else
    {
      if ( systemVariableService.getPropertyByName( SystemVariableService.PURL_AVAILABLE ).getBooleanVal() )
      {
        callables.add( createPendingPurlInvitationsForAlerts( participant ) );
        callables.add( createPendingPurlContributionsAlertsForDefaultInvitee( participant, isModalWindow ) );
        callables.add( createPendingPurlContributionsAlertsForNonDefaultInvitee( participant, isModalWindow ) );
        callables.add( createPendingViewPurlsForAlerts( participant ) );
      }
      if ( systemVariableService.getPropertyByName( SystemVariableService.CELEBRATION ).getBooleanVal() )
      {
        // celebration alerts
        callables.add( createCelebrationAlerts( participant ) );
      }
    }

    // Client customization for WIP #43735 starts
    List<ClaimRecipientValueBean> claimRecipients = cokeClientDAO.getYetToClaimAwards( participant.getId() );
    if ( claimRecipients != null && !claimRecipients.isEmpty() )
    {
      alertList.addAll( buildAlertValueBeans( claimRecipients ) );
    }
    claimRecipients=null;
    // Client customization for WIP 58122
    claimRecipients = cokeClientDAO.getYetToClaimAwardsForNomination( participant.getId() );
    if ( claimRecipients != null && !claimRecipients.isEmpty() )
    {
      alertList.addAll( buildAlertValueBeansForNomination( claimRecipients ) );
    }
    // Client customization for WIP #43735 ends
    // Unverified recovery contacts
    callables.add( createRecoveryVerificationAlerts( participant ) );

    try
    {
      executor.invokeAll( callables ).stream().map( future ->
      {
        try
        {
          return future.get();
        }
        catch( Exception e )
        {
          throw new IllegalStateException( e );
        }
      } ).filter( entry -> entry != null ).forEach( entry -> alertList.addAll( entry ) );
    }
    catch( InterruptedException e )
    {
      logger.error( e.getMessage(), e );
    }
    finally
    {
      executor.shutdown();
    }

    return alertList;
  }

  private Map<String, String> getCmxTranslateProgramName()
  {
    List<String> programCodeKeyList = engageProgramService.getEligibleProgramCmxCode();
    Map<String, String> programCmxKeysValue = null;
    try
    {
      if ( CollectionUtils.isNotEmpty( programCodeKeyList ) )
      {
        programCmxKeysValue = cmxService.getTranslation( UserManager.getUserLocale(), programCodeKeyList );
      }
    }
    catch( Exception e )
    {
      logger.error( "Exception while Calling CMX Translation Service .Using default locale " + e.getMessage() );
    }
    return programCmxKeysValue;
  }

  // Alerts Performance Tuning
  private Map<Long, Promotion> buildPromotionMap( List eligiblePromotions )
  {
    Iterator promosIter = eligiblePromotions.iterator();
    Map<Long, Promotion> map = new HashMap<Long, Promotion>();
    while ( promosIter.hasNext() )
    {
      PromotionMenuBean promoMenuBean = (PromotionMenuBean)promosIter.next();
      map.put( promoMenuBean.getPromotion().getId(), promoMenuBean.getPromotion() );
    }
    return map;
  }

  /**
   * @param participant
   */
  public Callable<List<AlertsValueBean>> createPendingSAContributionsForAlerts( Participant contributor, CmxTranslateHelperBean cmxTranslateHelper )
  {
    return CallableFactory.createCallable( new Callable<List<AlertsValueBean>>()
    {
      public List<AlertsValueBean> call() throws Exception
      {
        return serviceAnniversaryService.getPendingSAContributionsForAlerts( contributor, cmxTranslateHelper );
      }
    } );
  }

  /**
   * @param eligiblePromotions
   * @param participant
   * @return
   */
  // Alerts Performance Tuning
  public Callable<List<AlertsValueBean>> createLiveOrExpiredPromotions( List eligiblePromotions, Participant participant, boolean isMessagesPage )
  {
    List<PromotionBean> liveAndExpiredPromotions = new ArrayList<PromotionBean>();
    Iterator eligiblePromotionsIter = eligiblePromotions.iterator();
    while ( eligiblePromotionsIter.hasNext() )
    {
      PromotionMenuBean promoMenuBean = (PromotionMenuBean)eligiblePromotionsIter.next();
      Promotion promotion = promoMenuBean.getPromotion();
      if ( promotion.isLive() )
      {
        liveAndExpiredPromotions.add( new PromotionBean( promotion.getId(), promotion.getPromotionType().getCode() ) );
      }
    }
    Callable<List<AlertsValueBean>> task = null;
    // PromotionQueryConstraint promoQueryConstraint = new PromotionQueryConstraint();
    // promoQueryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] {
    // PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) } );
    // liveAndExpiredPromotions.addAll( getPromotionList( promoQueryConstraint ) );
    List<PromotionBean> expiredPromotions = promotionDAO.getExpiredPromotions();
    liveAndExpiredPromotions.addAll( expiredPromotions );
    if ( liveAndExpiredPromotions != null && liveAndExpiredPromotions.size() > 0 )
    {
      task = CallableFactory.createCallable( new Callable<List<AlertsValueBean>>()
      {
        public List<AlertsValueBean> call() throws Exception
        {
          return getPromotionsService().getPendingApprovalsForAlerts( liveAndExpiredPromotions, eligiblePromotions, participant, isMessagesPage );
        }
      } );
    }
    return task;
  }

  /**
   * @param eligiblePromotions
   * @param participant
   * @return
   */
  public Callable<List<AlertsValueBean>> createGQPromotionAlerts( List eligiblePromotions, Participant participant )
  {
    return CallableFactory.createCallable( new Callable<List<AlertsValueBean>>()
    {
      public List<AlertsValueBean> call() throws Exception
      {
        return getPromotionsService().getGQCPPromotionsForAlerts( eligiblePromotions, participant );
      }
    } );
  }

  /**
   * @param eligiblePromotionss
   * @param participant
   */
  public Callable<List<AlertsValueBean>> createGQPromotionSurveyAlerts( List eligiblePromotions, Participant participant )
  {
    return CallableFactory.createCallable( new Callable<List<AlertsValueBean>>()
    {
      public List<AlertsValueBean> call() throws Exception
      {
        return getPromotionsService().getGQCPPromotionsSurveyForAlerts( eligiblePromotions, participant );
      }
    } );
  }

  /**
   * @param eligiblePromotionss
   * @param participant
   */
  public Callable<List<AlertsValueBean>> createSurveyPromotionAlerts( List eligiblePromotions, Participant participant )
  {
    return CallableFactory.createCallable( new Callable<List<AlertsValueBean>>()
    {
      public List<AlertsValueBean> call() throws Exception
      {
        return getPromotionsService().getSurveyPromotionsForAlerts( eligiblePromotions, participant );
      }
    } );
  }

  /**
   * @param eligiblePromotionss
   * @param participant
   */
  public Callable<List<AlertsValueBean>> createQuizPromotionsForAlers( List eligiblePromotions, Participant participant )
  {
    Callable<List<AlertsValueBean>> task = null;
    List<AlertsValueBean> quizAlerts = getQuizPromotionsForAlerts( eligiblePromotions, participant );
    if ( !quizAlerts.isEmpty() )
    {
      task = CallableFactory.createCallable( new Callable<List<AlertsValueBean>>()
      {
        public List<AlertsValueBean> call() throws Exception
        {
          return quizAlerts;
        }
      } );
    }
    else
    {
      // diy quiz alert
      task = CallableFactory.createCallable( new Callable<List<AlertsValueBean>>()
      {
        public List<AlertsValueBean> call() throws Exception
        {
          return getPromotionsService().getDIYQuizPromotionsForAlerts( eligiblePromotions, participant );
        }
      } );
    }
    return task;
  }

  /**
   * @param eligiblePromotionss
   * @param participant
   */
  @SuppressWarnings( "unchecked" )
  public Callable<List<AlertsValueBean>> createAwardGeneratorAlertForManger( List eligiblePromotions, Participant participant )
  {
    return CallableFactory.createCallable( new Callable<List<AlertsValueBean>>()
    {
      public List<AlertsValueBean> call() throws Exception
      {
        return getPromotionsService().getAwardGeneratorAlertForManager( eligiblePromotions, participant );
      }
    } );
  }

  /**
   * @param eligiblePromotionss
   * @param participant
   */
  @SuppressWarnings( "unchecked" )
  public Callable<List<AlertsValueBean>> createCelebrationAlertForManager( List eligiblePromotions, Participant participant )
  {
    return CallableFactory.createCallable( new Callable<List<AlertsValueBean>>()
    {
      public List<AlertsValueBean> call() throws Exception
      {
        return getPromotionsService().getCelebrationAlertForManager( eligiblePromotions, participant );
      }
    } );
  }

  /**
   * @param eligiblePromotionss
   * @param participant
   */
  @SuppressWarnings( "unchecked" )
  public Callable<List<AlertsValueBean>> createApproveReminderAlerts( List eligiblePromotions, Participant participant )
  {
    return CallableFactory.createCallable( new Callable<List<AlertsValueBean>>()
    {
      public List<AlertsValueBean> call() throws Exception
      {
        return getPromotionsService().getApproverReminderAlerts( eligiblePromotions, participant );
      }
    } );
  }

  @SuppressWarnings( "unchecked" )
  public Callable<List<AlertsValueBean>> createApproveReminderSummaryAlerts( Participant participant )
  {
    return CallableFactory.createCallable( new Callable<List<AlertsValueBean>>()
    {
      public List<AlertsValueBean> call() throws Exception
      {
        List<AlertsValueBean> alertList = new ArrayList<AlertsValueBean>();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put( "userId", UserManager.getUserId() );
        parameters.put( "sortedBy", "asc" );
        parameters.put( "sortedOn", "date_submitted" );
        parameters.put( "rowNumStart", 0 );
        parameters.put( "rowNumEnd", 10 + 1 );

        NominationClaimService nominationClaimService = (NominationClaimService)BeanLocator.getBean( NominationClaimService.BEAN_NAME );
        PendingNominationsApprovalMainValueBean pendingNominationsApprovalMainValueBean = nominationClaimService.getPendingNominationClaimsForApprovalByUser( parameters );
        if ( pendingNominationsApprovalMainValueBean != null )
        {
          List<NominationsApprovalValueBean> pendingNominationsApprovalslist = pendingNominationsApprovalMainValueBean.getPendingNominationsApprovalslist();
          if ( CollectionUtils.isNotEmpty( pendingNominationsApprovalslist ) && pendingNominationsApprovalslist.get( 0 ).getTotalRecords() > 0 )
          {
            AlertsValueBean alert = new AlertsValueBean();
            alert.setActivityType( ActivityType.NOMS_APPROVER_REMAINDER_SUMMARY );
            alertList.add( alert );
          }
        }
        return alertList;

      }
    } );
  }

  /**
   * @param participant
   */
  @SuppressWarnings( "unchecked" )
  public Callable<List<AlertsValueBean>> createNominatorRequestMoreInfoAlerts( Participant participant )
  {
    return CallableFactory.createCallable( new Callable<List<AlertsValueBean>>()
    {
      public List<AlertsValueBean> call() throws Exception
      {
        return getPromotionsService().getNominatorRequestMoreInfoAlerts( participant );
      }
    } );
  }

  /**
   * @param participant
   */
  @SuppressWarnings( "unchecked" )
  public Callable<List<AlertsValueBean>> createNominationMyWinnersList( Participant participant )
  {
    return CallableFactory.createCallable( new Callable<List<AlertsValueBean>>()
    {
      public List<AlertsValueBean> call() throws Exception
      {
        return getPromotionsService().getNominationMyWinnersList( participant );
      }
    } );
  }

  /**
   * @param participant
   */
  public Callable<List<AlertsValueBean>> createInProgressNominationClaimsCount( List eligiblePromotions, Participant participant )
  {
    return CallableFactory.createCallable( new Callable<List<AlertsValueBean>>()
    {
      public List<AlertsValueBean> call() throws Exception
      {
        return getPromotionsService().getInProgressNominationClaimsCount( eligiblePromotions, participant );
      }
    } );
  }

  /**
   * @param eligiblePromotionss
   * @param participant
   */
  // Alerts Performance Tuning
  @SuppressWarnings( "unchecked" )
  public Callable<List<AlertsValueBean>> createAwardReminderListForAlerts( List eligiblePromotions, Participant participant, Map<Long, Promotion> eligiblePromoMap )
  {
    return CallableFactory.createCallable( new Callable<List<AlertsValueBean>>()
    {
      public List<AlertsValueBean> call() throws Exception
      {
        return getPromotionsService().getAwardReminderListForAlerts( eligiblePromotions, participant, eligiblePromoMap );
      }
    } );
  }

  public Callable<List<AlertsValueBean>> createSAAwardReminderListForAlerts( Participant participant, CmxTranslateHelperBean cmxTranslateHelper )
  {
    return CallableFactory.createCallable( new Callable<List<AlertsValueBean>>()
    {
      public List<AlertsValueBean> call() throws Exception
      {
        return serviceAnniversaryService.getSAGiftCodeReminderListForAlerts( participant.getId(), cmxTranslateHelper );
      }
    } );
  }

  /**
   * @param eligiblePromotionss
   * @param participant
   */
  @SuppressWarnings( "unchecked" )
  public Callable<List<AlertsValueBean>> createBudgetEndAlertForAlerts( List eligiblePromotions, Participant participant )
  {
    return CallableFactory.createCallable( new Callable<List<AlertsValueBean>>()
    {
      public List<AlertsValueBean> call() throws Exception
      {
        return getPromotionsService().getBudgetEndAlertForAlerts( eligiblePromotions, participant );
      }
    } );
  }

  /**
   * @param participant
   */
  public Callable<List<AlertsValueBean>> createPendingPurlInvitationsForAlerts( Participant participant )
  {
    return CallableFactory.createCallable( new Callable<List<AlertsValueBean>>()
    {
      public List<AlertsValueBean> call() throws Exception
      {
        return getPromotionsService().getPendingPurlInvitationsForAlerts( participant );
      }
    } );
  }

  public Callable<List<AlertsValueBean>> createPendingPurlContributionsForAlerts( Participant participant, boolean isModalWindow )
  {
    return CallableFactory.createCallable( new Callable<List<AlertsValueBean>>()
    {
      public List<AlertsValueBean> call() throws Exception
      {
        return getPromotionsService().getPendingPurlContributionsForAlerts( participant, isModalWindow );
      }
    } );
  }

  /**
   * @param participant
   * @param isModalWindow
   */
  // default and non default invitee flow
  public Callable<List<AlertsValueBean>> createPendingPurlContributionsAlertsForDefaultInvitee( Participant participant, boolean isModalWindow )
  {
    return CallableFactory.createCallable( new Callable<List<AlertsValueBean>>()
    {
      public List<AlertsValueBean> call() throws Exception
      {
        return getPromotionsService().getPendingPurlContributionsAlertsForDefaultInvitee( participant, isModalWindow );
      }
    } );
  }

  public Callable<List<AlertsValueBean>> createPendingPurlContributionsAlertsForNonDefaultInvitee( Participant participant, boolean isModalWindow )
  {
    return CallableFactory.createCallable( new Callable<List<AlertsValueBean>>()
    {
      public List<AlertsValueBean> call() throws Exception
      {
        return getPromotionsService().getPendingPurlContributionsAlertsForNonDefaultInvitee( participant, isModalWindow );
      }
    } );
  }

  /**
   * @param participant
   */
  public Callable<List<AlertsValueBean>> createPendingViewPurlsForAlerts( Participant participant )
  {
    return CallableFactory.createCallable( new Callable<List<AlertsValueBean>>()
    {
      public List<AlertsValueBean> call() throws Exception
      {
        return getPromotionsService().getPendingViewPurlsForAlerts( participant );
      }
    } );
  }

  /**
   * @param participant
   */
  public Callable<List<AlertsValueBean>> createPendingFileDownloadsForAlerts( Participant participant )
  {
    return CallableFactory.createCallable( new Callable<List<AlertsValueBean>>()
    {
      public List<AlertsValueBean> call() throws Exception
      {
        return getPromotionsService().getPendingFileDownloadsForAlerts( participant );
      }
    } );
  }

  /**
   * @param participant
   */
  public Callable<List<AlertsValueBean>> createInstantPollForAlerts( Participant participant )
  {
    return CallableFactory.createCallable( new Callable<List<AlertsValueBean>>()
    {
      public List<AlertsValueBean> call() throws Exception
      {
        return getPromotionsService().getInstantPollForAlerts( participant );
      }
    } );
  }

  /**
   * @param participant
   */
  public Callable<List<AlertsValueBean>> createSACelebrationAlerts( Participant participant, int numOfDays, CmxTranslateHelperBean cmxTranslateHelper )
  {
    return CallableFactory.createCallable( new Callable<List<AlertsValueBean>>()
    {
      public List<AlertsValueBean> call() throws Exception
      {
        return serviceAnniversaryService.getSACelebrationsByRecipient( participant.getId(), numOfDays, cmxTranslateHelper );
      }
    } );
  }

  /**
   * @param participant
   */
  public Callable<List<AlertsValueBean>> createCelebrationAlerts( Participant participant )
  {
    return CallableFactory.createCallable( new Callable<List<AlertsValueBean>>()
    {
      public List<AlertsValueBean> call() throws Exception
      {
        return getPromotionsService().getCelebrationAlerts( participant );
      }
    } );
  }

  public Callable<List<AlertsValueBean>> createRecoveryVerificationAlerts( Participant participant )
  {
    return CallableFactory.createCallable( new Callable<List<AlertsValueBean>>()
    {
      public List<AlertsValueBean> call() throws Exception
      {
        return participantService.getUnverifiedRecoveryAlerts( participant );
      }
    } );
  }

  // end of the change

  private boolean checkIfShowPromoRules( Promotion promotion, Participant participant )
  {
    String timeZoneID = UserManager.getUser().getTimeZoneId();
    return promotion.isWebRulesActive() && DateUtils.isDateBetween( new Date(), promotion.getWebRulesStartDate(), promotion.getWebRulesEndDate(), timeZoneID )
        && audienceService.isParticipantInWebRulesAudience( promotion, participant );
  }

  private boolean isParticipantInPrimaryOrSecAudience( Promotion promotion, Participant participant )
  {
    return audienceService.isParticipantInPrimaryAudience( promotion, participant ) || audienceService.isParticipantInSecondaryAudience( promotion, participant, null );
  }

  private boolean isParticipantInBothReceiverAndWebRulesAudience( Promotion promotion, Participant participant )
  {
    return !audienceService.isParticipantInPrimaryAudience( promotion, participant ) && audienceService.isParticipantInSecondaryAudience( promotion, participant, null )
        && audienceService.isParticipantInWebRulesAudience( promotion, participant );
  }

  protected boolean isGoalSelected( Participant pax, Promotion promotion )
  {
    boolean isGoalSelected = false;
    PaxGoal paxGoal = getPaxGoalService().getPaxGoalByPromotionIdAndUserId( promotion.getId(), pax.getId() );
    if ( paxGoal != null && paxGoal.getGoalLevel() != null )
    {
      isGoalSelected = true;
    }
    return isGoalSelected;
  }

  protected boolean isCPSelected( Participant pax, Promotion promotion )
  {
    boolean isCPSelected = false;
    PaxGoal paxGoal = getPaxGoalService().getPaxGoalByPromotionIdAndUserId( promotion.getId(), pax.getId() );
    if ( paxGoal != null && paxGoal.getGoalLevel() != null )
    {
      isCPSelected = true;
    }
    return isCPSelected;
  }

  private List getAwardReminderList( List eligiblePromos, Participant participant )
  {
    List promoAwardRemindersList = new ArrayList();
    List merchPromos = new ArrayList();
    if ( eligiblePromos != null && eligiblePromos.size() > 0 )
    {
      if ( participant.getPrimaryAddress() != null && participant.getPrimaryAddress().getAddress() != null && participant.getPrimaryAddress().getAddress().getCountry() != null )
      {
        Country country = participant.getPrimaryAddress().getAddress().getCountry();
        // long paxId = participant.getId();

        List promoAwardReminderList = getMainContentService().getMerchAwardRemindersForAcivityList( participant.getId(), eligiblePromos, country );

        if ( promoAwardReminderList != null && promoAwardReminderList.size() > 0 )
        {
          Iterator promoAwardReminderListIter = promoAwardReminderList.iterator();
          while ( promoAwardReminderListIter.hasNext() )
          {
            MerchAwardReminderBean merchAwardReminder = (MerchAwardReminderBean)promoAwardReminderListIter.next();
            Promotion promo = getPromotionById( merchAwardReminder.getPromotionId() );
            ActivityCenterValueBean activityCenterValueBean = new ActivityCenterValueBean();
            activityCenterValueBean.setPromotion( promo );
            activityCenterValueBean.setOnlineShoppingUrl( merchAwardReminder.getOnlineShoppingUrl() );
            activityCenterValueBean.setActivityType( ActivityType.AWARD_REMINDER );
            activityCenterValueBean.setShowProgramRules( checkIfShowPromoRules( promo, participant ) );
            promoAwardRemindersList.add( activityCenterValueBean );
          }
        }
      }
    }
    return promoAwardRemindersList;
  }

  private List getPendingPurlInvitations( Participant pax )
  {
    List invitations = new ArrayList();

    if ( pax.isOwner() )
    {
      List<Long> purlManagerNodes = getPurlService().getPurlManagerNodes( pax.getId() );
      List<Promotion> purlInvitationPromotions = getPurlService().getEligiblePurlPromotionsForInvitation( pax.getId(), purlManagerNodes );
      for ( Promotion promotion : purlInvitationPromotions )
      {
        List pendingInvitations = getPurlService().getAllPendingPurlInvitationsForManager( pax.getId(), promotion.getId(), purlManagerNodes );
        if ( !pendingInvitations.isEmpty() )
        {
          ActivityCenterValueBean valueBean = new ActivityCenterValueBean();
          valueBean.setPromotion( promotion );
          valueBean.setActivityType( ActivityType.PURL_INVITATION );
          invitations.add( valueBean );
        }
      }
    }

    return invitations;
  }

  private List getPendingViewPurls( Participant pax )
  {
    List purls = new ArrayList();

    List<Promotion> purlRecipientPromotions = getPurlService().getEligiblePurlPromotionsForRecipient( pax.getId() );
    for ( Promotion promotion : purlRecipientPromotions )
    {
      List pendingPurls = getPurlService().getAllPendingPurlRecipients( pax.getId(), promotion.getId() );
      if ( !pendingPurls.isEmpty() )
      {
        ActivityCenterValueBean valueBean = new ActivityCenterValueBean();
        valueBean.setPromotion( promotion );
        valueBean.setActivityType( ActivityType.PURL_VIEW );
        purls.add( valueBean );
      }
    }

    return purls;
  }

  public List<AlertsValueBean> getGQCPPromotionsForAlerts( List eligiblePromos, Participant pax )
  {
    List<AlertsValueBean> pendingPromos = new ArrayList<AlertsValueBean>();
    Iterator promoIter = eligiblePromos.iterator();
    String timeZoneID = UserManager.getUser().getTimeZoneId();

    while ( promoIter.hasNext() )
    {
      PromotionMenuBean promoMenuBean = (PromotionMenuBean)promoIter.next();
      Promotion promo = promoMenuBean.getPromotion();
      if ( promo.isGoalQuestOrChallengePointPromotion() )
      {
        if ( promoMenuBean.isCanSubmit() )
        {
          GoalQuestPromotion gqPromo = (GoalQuestPromotion)promo;
          PaxGoal paxGoal = getPaxGoalService().getPaxGoalByPromotionIdAndUserId( gqPromo.getId(), pax.getId() );

          if ( !gqPromo.isIssueAwardsRun() ) // Fix 25646
          {
            if ( DateUtils.isDateBetween( new Date(), DateUtils.toStartDate( gqPromo.getGoalCollectionStartDate() ), DateUtils.toEndDate( gqPromo.getGoalCollectionEndDate() ), timeZoneID ) )
            {
              if ( paxGoal == null || paxGoal.getGoalLevel() == null )
              {
                AlertsValueBean alertsVB = new AlertsValueBean();
                alertsVB.setPromotion( gqPromo );
                alertsVB.setActivityType( ActivityType.SUBMIT_PROMOTION );
                if ( promo.isGoalQuestPromotion() )
                {
                  alertsVB.setGoalSelectionStartDate( DateUtils.toDisplayString( gqPromo.getGoalCollectionStartDate() ) );
                  alertsVB.setGoalSelectionEnddate( DateUtils.toDisplayString( gqPromo.getGoalCollectionEndDate() ) );
                }
                else if ( promo.isChallengePointPromotion() )
                {
                  alertsVB.setCpSelectionStartDate( DateUtils.toDisplayString( gqPromo.getGoalCollectionStartDate() ) );
                  alertsVB.setCpSelectionEndDate( DateUtils.toDisplayString( gqPromo.getGoalCollectionEndDate() ) );
                }
                pendingPromos.add( alertsVB );
              }
            }
          }
        }
      }
    }

    return pendingPromos;
  }

  public List<AlertsValueBean> getGQCPPromotionsSurveyForAlerts( List eligiblePromos, Participant pax )
  {
    List<AlertsValueBean> pendingPromos = new ArrayList<AlertsValueBean>();
    Iterator promoIter = eligiblePromos.iterator();
    String timeZoneID = UserManager.getUser().getTimeZoneId();
    Integer numberOfDays = new Integer( 14 );
    while ( promoIter.hasNext() )
    {
      PromotionMenuBean promoMenuBean = (PromotionMenuBean)promoIter.next();
      Promotion promo = promoMenuBean.getPromotion();
      if ( promo.isGoalQuestOrChallengePointPromotion() )
      {
        if ( promoMenuBean.isCanSubmit() )
        {
          GoalQuestPromotion gqPromo = (GoalQuestPromotion)promo;
          PaxGoal paxGoal = getPaxGoalService().getPaxGoalByPromotionIdAndUserId( gqPromo.getId(), pax.getId() );

          if ( paxGoal != null && paxGoal.getGoalLevel() != null )
          {
            Date surveyEndDate = new Date( DateUtils.toEndDate( gqPromo.getGoalCollectionEndDate() ).getTime() + numberOfDays.longValue() * org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY );
            Date actualSurveyEndDate = new Date();
            if ( surveyEndDate.after( DateUtils.toEndDate( gqPromo.getSubmissionEndDate() ) ) )
            {
              actualSurveyEndDate = gqPromo.getSubmissionEndDate();
            }
            else
            {
              actualSurveyEndDate = surveyEndDate;
            }
            if ( new Date().before( DateUtils.toEndDate( actualSurveyEndDate ) ) )
            {
              if ( !hasTakenSurvey( gqPromo, pax ) )
              {
                AlertsValueBean alertsVB = new AlertsValueBean();
                alertsVB.setPromotion( gqPromo );
                alertsVB.setActivityType( ActivityType.GQ_SURVEY );
                if ( promo.isGoalQuestPromotion() )
                {
                  alertsVB.setGoalSelectionStartDate( DateUtils.toDisplayString( gqPromo.getGoalCollectionStartDate() ) );
                  alertsVB.setGoalSelectionEnddate( DateUtils.toDisplayString( gqPromo.getGoalCollectionEndDate() ) );
                }
                else if ( promo.isChallengePointPromotion() )
                {
                  alertsVB.setCpSelectionStartDate( DateUtils.toDisplayString( gqPromo.getGoalCollectionStartDate() ) );
                  alertsVB.setCpSelectionEndDate( DateUtils.toDisplayString( gqPromo.getGoalCollectionEndDate() ) );
                }
                pendingPromos.add( alertsVB );
              }
            }
          }
        }
      }
    }

    return pendingPromos;
  }

  public List<AlertsValueBean> getSurveyPromotionsForAlerts( List eligiblePromos, Participant pax )
  {
    List<AlertsValueBean> pendingPromos = new ArrayList<AlertsValueBean>();
    Iterator promoIter = eligiblePromos.iterator();
    String timeZoneID = UserManager.getUser().getTimeZoneId();
    Integer numberOfDays = new Integer( 14 );
    while ( promoIter.hasNext() )
    {
      PromotionMenuBean promoMenuBean = (PromotionMenuBean)promoIter.next();
      Promotion promo = promoMenuBean.getPromotion();
      if ( promo.isSurveyPromotion() )
      {
        if ( promoMenuBean.isCanSubmit() )
        {
          SurveyPromotion surveyPromotion = (SurveyPromotion)promo;
          Survey survey = surveyService.getSurveyById( surveyPromotion.getSurvey().getId() );
          if ( survey != null )
          {
            ParticipantSurvey participantSurvey = participantSurveyService.getParticipantSurveyByPromotionAndSurveyIdAndUserId( surveyPromotion.getId(), survey.getId(), UserManager.getUserId() );
            if ( participantSurvey == null || participantSurvey != null && !participantSurvey.isCompleted() )
            {
              Date surveyEndDate = new Date();
              Date actualSurveyEndDate = new Date();
              if ( surveyPromotion.getSubmissionEndDate() != null )
              {
                surveyEndDate = new Date( DateUtils.toEndDate( surveyPromotion.getSubmissionEndDate() ).getTime() + numberOfDays.longValue() * org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY );
              }
              if ( surveyPromotion.getSubmissionEndDate() != null && surveyEndDate.after( DateUtils.toEndDate( surveyPromotion.getSubmissionEndDate() ) ) )
              {
                actualSurveyEndDate = surveyPromotion.getSubmissionEndDate();
              }
              else
              {
                actualSurveyEndDate = surveyEndDate;
              }
              if ( new Date().before( DateUtils.toEndDate( actualSurveyEndDate ) ) )
              {
                if ( !hasTakenSurveyForSurvey( surveyPromotion, pax ) )
                {
                  AlertsValueBean alertsVB = new AlertsValueBean();
                  alertsVB.setPromotion( surveyPromotion );
                  alertsVB.setActivityType( ActivityType.SURVEY_ALERT );
                  alertsVB.setSurveySelectionStartDate( DateUtils.toDisplayString( surveyPromotion.getSubmissionStartDate() ) );
                  alertsVB.setSurveySelectionEnddate( DateUtils.toDisplayString( surveyPromotion.getSubmissionEndDate() ) );
                  pendingPromos.add( alertsVB );
                  break;
                }
              }
            }
          }
        }
      }
    }

    return pendingPromos;
  }

  private boolean hasTakenSurvey( GoalQuestPromotion gqPromo, Participant pax )
  {
    boolean isSurveyTaken = false;

    List<PromotionGoalQuestSurvey> listpgqsurvey = surveyService.getPromotionGoalQuestSurveysByPromotionId( gqPromo.getId() );
    if ( listpgqsurvey == null || listpgqsurvey.isEmpty() )
    {
      // No Survey Available
      isSurveyTaken = true;
    }
    else
    {
      List<ParticipantSurvey> surveyList = participantSurveyService.getParticipantSurveyByPromotionIdAndUserId( gqPromo.getId(), pax.getId() );

      if ( surveyList != null && !surveyList.isEmpty() )
      {
        isSurveyTaken = true;
      }
    }

    return isSurveyTaken;
  }

  private boolean hasTakenSurveyForSurvey( SurveyPromotion surveyPromotion, Participant pax )
  {
    List<SurveyPromotion> listpgqsurvey = surveyService.getSurveysByPromotionId( surveyPromotion.getId() );
    boolean isSurveyTaken = false;
    for ( SurveyPromotion pGQuestSurvey : listpgqsurvey )
    {
      ParticipantSurvey participantSurvey = participantSurveyService.getParticipantSurveyByPromotionAndSurveyIdAndUserId( surveyPromotion.getId(), pGQuestSurvey.getId(), pax.getId() );
      if ( participantSurvey != null )
      {
        isSurveyTaken = true;
        break;
      }

    }
    if ( listpgqsurvey != null && !isSurveyTaken && !listpgqsurvey.isEmpty() && listpgqsurvey.size() > 0 )
    {
      isSurveyTaken = false;
    }
    else
    {
      // No Survey Available
      isSurveyTaken = true;
    }
    return isSurveyTaken;
  }

  private List<AlertsValueBean> getQuizPromotionsForAlerts( List eligiblePromos, Participant pax )
  {
    List<AlertsValueBean> pendingPromos = new ArrayList<AlertsValueBean>();
    Iterator promoIter = eligiblePromos.iterator();
    // For performance, collect all of Quiz type to run all at once:
    List<PromotionMenuBean> quizPromos = new ArrayList<PromotionMenuBean>();

    while ( promoIter.hasNext() )
    {
      PromotionMenuBean promoMenuBean = (PromotionMenuBean)promoIter.next();
      Promotion promo = promoMenuBean.getPromotion();
      if ( promo.isQuizPromotion() )
      {
        Date promoStartDate = promo.getSubmissionStartDate();
        Date promoEndDate = promo.getSubmissionEndDate();
        Date currentDate = DateUtils.getCurrentDate();
        boolean currentDateBefore = promoStartDate.before( currentDate );
        boolean endDateAfter = true;
        boolean endDateEqual = true;
        if ( promoEndDate != null )
        {
          endDateAfter = promoEndDate.after( currentDate );
          endDateEqual = DateUtils.toDisplayString( promoEndDate ).equals( DateUtils.toDisplayString( currentDate ) );
        }

        // collect this to process later in bulk
        if ( currentDateBefore && ( endDateAfter || endDateEqual ) )
        {
          quizPromos.add( promoMenuBean );
        }
      }
    }

    QuizClaimQueryConstraint submitterClaimQueryConstraint = new QuizClaimQueryConstraint();
    submitterClaimQueryConstraint.setSubmitterId( pax.getId() );
    Map participantQuizClaimHistoryByPromotionMap = claimService.getParticipantQuizClaimHistoryByPromotionMap( submitterClaimQueryConstraint, null );

    // iterate any quiz promotions
    for ( PromotionMenuBean quizMenuBean : quizPromos )
    {
      Promotion promo = quizMenuBean.getPromotion();
      ParticipantQuizClaimHistory participantQuizClaimHistory = (ParticipantQuizClaimHistory)participantQuizClaimHistoryByPromotionMap.get( promo );
      if ( participantQuizClaimHistory == null || participantQuizClaimHistory.isRetakeable( UserManager.getUser().getTimeZoneId() ) || participantQuizClaimHistory.isInProgress() )
      {
        AlertsValueBean alertsVB = new AlertsValueBean();
        alertsVB.setPromotion( promo );
        alertsVB.setActivityType( ActivityType.SUBMIT_PROMOTION );
        pendingPromos.add( alertsVB );
        break;
      }
    }

    return pendingPromos;
  }

  public List<AlertsValueBean> getDIYQuizPromotionsForAlerts( List eligiblePromos, Participant pax )
  {
    List<AlertsValueBean> pendingPromos = new ArrayList<AlertsValueBean>();
    Iterator promoIter = eligiblePromos.iterator();

    while ( promoIter.hasNext() )
    {
      PromotionMenuBean promoMenuBean = (PromotionMenuBean)promoIter.next();
      if ( promoMenuBean.getPromotion().isDIYQuizPromotion() )
      {
        List<DIYQuiz> diyQuizList = diyQuizDAO.getEligibleQuizzesForParticipantByPromotion( promoMenuBean.getPromotion().getId(), pax.getId() );
        for ( DIYQuiz diyQuiz : diyQuizList )
        {
          Date quizStartDate = diyQuiz.getStartDate();
          Date quizEndDate = diyQuiz.getEndDate();
          Date currentDate = DateUtils.getCurrentDate();

          if ( quizStartDate.before( currentDate ) && quizEndDate != null && quizEndDate.after( currentDate ) )
          {
            QuizClaimQueryConstraint submitterClaimQueryConstraint = new QuizClaimQueryConstraint();
            submitterClaimQueryConstraint.setSubmitterId( pax.getId() );
            submitterClaimQueryConstraint.setDiyQuizId( diyQuiz.getId() );
            Map participantDIYQuizClaimHistoryByQuizMap = claimService.getParticipantDIYQuizClaimHistoryByQuizMap( submitterClaimQueryConstraint, null );
            ParticipantDIYQuizClaimHistory participantQuizClaimHistory = (ParticipantDIYQuizClaimHistory)participantDIYQuizClaimHistoryByQuizMap.get( diyQuiz.getId() );
            if ( participantQuizClaimHistory == null || participantQuizClaimHistory.isInProgress() || participantQuizClaimHistory.isRetakeable( UserManager.getUser().getTimeZoneId() ) )
            {
              AlertsValueBean alertsVB = new AlertsValueBean();
              alertsVB.setPromotion( promoMenuBean.getPromotion() );
              alertsVB.setActivityType( ActivityType.SUBMIT_PROMOTION );
              pendingPromos.add( alertsVB );
              break;
            }
          }
        }
      }
    }
    return pendingPromos;
  }

  public List<AlertsValueBean> getPendingPurlInvitationsForAlerts( Participant pax )
  {
    List<AlertsValueBean> invitations = new ArrayList<AlertsValueBean>();

    if ( pax.isOwner() )
    {
      List<Promotion> purlInvitationPromotions = getPurlService().getEligiblePurlPromotionsForInvitation( pax.getId() );
      for ( Promotion promotion : purlInvitationPromotions )
      {
        List pendingInvitations = getPurlService().getAllPendingPurlInvitationsForManager( pax.getId(), promotion.getId() );
        if ( !pendingInvitations.isEmpty() )
        {
          AlertsValueBean alertsVB = new AlertsValueBean();
          alertsVB.setPromotion( promotion );
          alertsVB.setActivityType( ActivityType.PURL_INVITATION );
          if ( pendingInvitations.size() == 1 )
          {
            PurlRecipient recip = (PurlRecipient)pendingInvitations.get( 0 );
            alertsVB.setPurlIssuedDate( DateUtils.toDisplayString( recip.getInvitationStartDate() ) );
            alertsVB.setPurlExpiryDate( DateUtils.toDisplayString( recip.getCloseDate() ) );
          }
        	  invitations.add( alertsVB );          
        }
      }
    }

    return invitations;
  }

  public List<AlertsValueBean> getPendingPurlContributionsForAlerts( Participant pax, boolean isModalWindow )
  {
    List<AlertsValueBean> purlContributionList = new ArrayList<AlertsValueBean>();
    boolean excludeSelfManagerContributions = !isModalWindow;

    List<Promotion> purlContributionPromotions = getPurlService().getEligiblePurlPromotionsForContributor( pax.getId() );
    for ( Promotion promotion : purlContributionPromotions )
    {
      List contributions = getPurlService().getAllPendingPurlContributions( pax.getId(), promotion.getId(), excludeSelfManagerContributions );

      if ( !contributions.isEmpty() )
      {
        AlertsValueBean alertsVB = new AlertsValueBean();
        alertsVB.setPromotion( promotion );
        alertsVB.setActivityType( ActivityType.PURL_CONTRIBUTION );
        if ( contributions.size() == 1 )
        {
          PurlContributor purlContributor = (PurlContributor)contributions.get( 0 );
          alertsVB.setPurlContributorId( purlContributor.getId() );
          alertsVB.setDefaultInvitee( purlContributor.isDefaultInvitee() );

          PurlRecipient recip = getPurlService().getPurlRecipientById( purlContributor.getPurlRecipient().getId() );
          alertsVB.setPurlIssuedDate( DateUtils.toDisplayString( recip.getInvitationStartDate() ) );
          alertsVB.setPurlExpiryDate( DateUtils.toDisplayString( recip.getCloseDate() ) );
        }
        purlContributionList.add( alertsVB );
      }
    }

    return purlContributionList;
  }

  // Default and non-default invitee

  public List<AlertsValueBean> getPendingPurlContributionsAlertsForDefaultInvitee( Participant pax, boolean isModalWindow )
  {
    List<AlertsValueBean> purlContributionList = new ArrayList<AlertsValueBean>();
    boolean excludeSelfManagerContributions = !isModalWindow;

    List<Promotion> purlContributionPromotions = getPurlService().getEligiblePurlPromotionsForContributor( pax.getId() );
    for ( Promotion promotion : purlContributionPromotions )
    {
      List contributions = getPurlService().getAllPendingPurlContributionsForDefaultInvitee( pax.getId(), promotion.getId(), excludeSelfManagerContributions );

      if ( !contributions.isEmpty() )
      {
        AlertsValueBean alertsVB = new AlertsValueBean();
        alertsVB.setPromotion( promotion );
        alertsVB.setActivityType( ActivityType.PURL_CONTRIBUTION_DEFAULT_INVITEE );
        if ( contributions.size() == 1 )
        {
          PurlContributor purlContributor = (PurlContributor)contributions.get( 0 );
          
          alertsVB.setPurlContributorId( purlContributor.getId() );
          alertsVB.setDefaultInvitee( purlContributor.isDefaultInvitee() );                  	 
          PurlRecipient recip = getPurlService().getPurlRecipientById( purlContributor.getPurlRecipient().getId() );
          alertsVB.setPurlIssuedDate( DateUtils.toDisplayString( recip.getInvitationStartDate() ) );
          alertsVB.setPurlExpiryDate( DateUtils.toDisplayString( recip.getCloseDate() ) );         
        }         
        purlContributionList.add( alertsVB );
        }
      }


    return purlContributionList;
  }

  public List<AlertsValueBean> getPendingPurlContributionsAlertsForNonDefaultInvitee( Participant pax, boolean isModalWindow )
  {
    List<AlertsValueBean> purlContributionList = new ArrayList<AlertsValueBean>();
    boolean excludeSelfManagerContributions = !isModalWindow;

    List<Promotion> purlContributionPromotions = getPurlService().getEligiblePurlPromotionsForContributor( pax.getId() );
    for ( Promotion promotion : purlContributionPromotions )
    {
      List contributions = getPurlService().getAllPendingPurlContributionsForNonDefaultInvitee( pax.getId(), promotion.getId(), excludeSelfManagerContributions );

      if ( !contributions.isEmpty() )
      {
        AlertsValueBean alertsVB = new AlertsValueBean();
        alertsVB.setPromotion( promotion );
        alertsVB.setActivityType( ActivityType.PURL_CONTRIBUTION_NON_DEFAULT_INVITEE );
        if ( contributions.size() == 1 )
        {
          PurlContributor purlContributor = (PurlContributor)contributions.get( 0 );
          alertsVB.setPurlContributorId( purlContributor.getId() );
          alertsVB.setDefaultInvitee( purlContributor.isDefaultInvitee() );

          PurlRecipient recip = getPurlService().getPurlRecipientById( purlContributor.getPurlRecipient().getId() );
          alertsVB.setPurlIssuedDate( DateUtils.toDisplayString( recip.getInvitationStartDate() ) );
          alertsVB.setPurlExpiryDate( DateUtils.toDisplayString( recip.getCloseDate() ) );
        }
        purlContributionList.add( alertsVB );
      }
    }

    return purlContributionList;
  }

  public List<AlertsValueBean> getPendingViewPurlsForAlerts( Participant pax )
  {
    List<AlertsValueBean> purls = new ArrayList<AlertsValueBean>();
     
    List<Promotion> purlRecipientPromotions = getPurlService().getEligiblePurlPromotionsForRecipient( pax.getId() );
    for ( Promotion promotion : purlRecipientPromotions )
    {
      List<PurlRecipient> pendingPurls = getPurlService().getAllPendingPurlRecipients( pax.getId(), promotion.getId() );
      for ( PurlRecipient purlRecipient : pendingPurls )
      {
        AlertsValueBean alertsVB = new AlertsValueBean();
        alertsVB.setPromotion( promotion );
        alertsVB.setActivityType( ActivityType.PURL_VIEW );
        alertsVB.setPurlRecipientId( purlRecipient.getId() );        
        alertsVB.setPurlIssuedDate( DateUtils.toDisplayString( purlRecipient.getAwardDate() ) );
        int numOfDays = systemVariableService.getPropertyByName( SystemVariableService.PURL_DAYS_TO_EXP ).getIntVal();
        Date purlExpireDate = new Date( purlRecipient.getAwardDate().getTime() + numOfDays * org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY );
        alertsVB.setPurlExpiryDate( DateUtils.toDisplayString( purlExpireDate ) );
        purls.add( alertsVB );
      }
    }

    return purls;
  }

  public List<AlertsValueBean> getPendingFileDownloadsForAlerts( Long userId )
  {
    User user = this.getUserService().getUserById( userId );
    return getPendingFileDownloadsForAlerts( user );
  }

  public List<AlertsValueBean> getPendingFileDownloadsForAlerts( User user )
  {
    List<AlertsValueBean> pendingFileDownloads = new ArrayList<AlertsValueBean>();

    List<FileStore> files = getFileStoreService().getFileStoresForUser( user.getId() );
    SimpleDateFormat expireyFormat = new SimpleDateFormat( DateFormatterUtil.getDateTime12HourPatternWithAmPm( UserManager.getUser().getLocale() ) );
    SimpleDateFormat requestFormat = new SimpleDateFormat( DateFormatterUtil.getDatePattern( UserManager.getUser().getLocale() ) );
    for ( FileStore file : files )
    {
      AlertsValueBean fileDownloadAlertBean = new AlertsValueBean();
      fileDownloadAlertBean.setActivityType( ActivityType.FILE_DOWNLOAD );
      fileDownloadAlertBean.setFileStoreId( file.getId() );
      fileDownloadAlertBean.setFileName( file.getUserFileName() );
      Date expireyDate = new Date( file.getDateGenerated().getTime() + org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY );
      fileDownloadAlertBean.setFileDownloadExpiryDate( expireyFormat.format( DateUtils.applyTimeZone( expireyDate, UserManager.getTimeZoneID() ) ) );
      fileDownloadAlertBean.setFileDownloadRequestedDate( requestFormat.format( file.getDateGenerated() ) );

      pendingFileDownloads.add( fileDownloadAlertBean );
    }

    return pendingFileDownloads;
  }

  public List<AlertsValueBean> getInstantPollForAlerts( User user )
  {
    List<AlertsValueBean> instantPollValueBeans = new ArrayList<AlertsValueBean>();

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new SurveyAssociationRequest( SurveyAssociationRequest.ALL ) );

    List<InstantPoll> instantPolls = instantPollService.getInstantPollsForTileDisplay( user.getId(), associationRequestCollection );
    java.util.Collections.sort( instantPolls, ( InstantPoll ip1, InstantPoll ip2 ) -> ip2.getSubmissionEndDate().compareTo( ip1.getSubmissionEndDate() ) );

    if ( instantPolls != null && instantPolls.size() > 0 )
    {
      for ( InstantPoll instantPoll : instantPolls )
      {
        AlertsValueBean instantPollValueBean = new AlertsValueBean();
        instantPollValueBean.setActivityType( ActivityType.POLLS_PAX_ALERT );
        instantPollValueBean.setInstantPollId( Long.toString( instantPoll.getId() ) );
        instantPollValueBeans.add( instantPollValueBean );
      }
    }

    return instantPollValueBeans;
  }

  public List<AlertsValueBean> getCelebrationAlerts( User user )
  {
    List<AlertsValueBean> celebrationValueBeans = new ArrayList<AlertsValueBean>();

    if ( claimService.displayCelebration( null, user.getId() ) )
    {
      List<Long> claimIds = claimService.getCelebrationClaims( user.getId() );

      for ( int i = 0; i < claimIds.size(); i++ )
      {
        Claim claim = null;
        RecognitionClaim recognitionClaim = null;

        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );

        claim = claimService.getClaimByIdWithAssociations( claimIds.get( i ), associationRequestCollection );

        if ( claim instanceof RecognitionClaim )
        {
          recognitionClaim = (RecognitionClaim)claim;
          RecognitionPromotion recognitionPromotion = (RecognitionPromotion)recognitionClaim.getPromotion();

          if ( recognitionPromotion.isIncludeCelebrations() )
          {
            AlertsValueBean celebrationValueBean = new AlertsValueBean();
            celebrationValueBean.setActivityType( ActivityType.CELEBRATION_PAX_ALERT );
            celebrationValueBean.setPromotion( claim.getPromotion() );
            celebrationValueBean.setClaimId( recognitionClaim.getId() );
            celebrationValueBeans.add( celebrationValueBean );
          }
        }
      }
    }

    return celebrationValueBeans;
  }

  // Alerts Performance Tuning
  public List<AlertsValueBean> getPendingApprovalsForAlerts( List<PromotionBean> liveAndExpiredPromotions, List eligiblePromos, Participant participant, boolean isMessagesPage )
  {
    List<AlertsValueBean> promoPendingApprovals = new ArrayList<AlertsValueBean>();

    // isProductClaimPromotion
    Long[] liveAndExpiredPromotionIdsProductClaim = getPromotionsByType( liveAndExpiredPromotions, PromotionType.lookup( PromotionType.PRODUCT_CLAIM ) );
    List<PromotionApprovableValue> claimList = (List<PromotionApprovableValue>)claimService
        .getClaimsForApprovalByUser( participant.getId(), liveAndExpiredPromotionIdsProductClaim, true, null, null, PromotionType.lookup( PromotionType.PRODUCT_CLAIM ), null, null, false, false );
    if ( claimList != null && claimList.size() > 0 )
    {
      for ( PromotionApprovableValue value : claimList )
      {
        AlertsValueBean alertsVB = new AlertsValueBean();
        alertsVB.setPromotion( value.getPromotion() );
        alertsVB.setActivityType( ActivityType.PENDING_APPROVALS );
        promoPendingApprovals.add( alertsVB );
      }
    }

    // isRecognitionPromotion
    Long[] liveAndExpiredPromotionIdsRecognition = getPromotionsByType( liveAndExpiredPromotions, PromotionType.lookup( PromotionType.RECOGNITION ) );
    claimList = claimService.getClaimsForApprovalByUser( participant.getId(),
                                                         liveAndExpiredPromotionIdsRecognition,
                                                         true,
                                                         null,
                                                         null,
                                                         PromotionType.lookup( PromotionType.RECOGNITION ),
                                                         null,
                                                         null,
                                                         false,
                                                         false );
    if ( claimList != null && claimList.size() > 0 )
    {
      for ( PromotionApprovableValue value : claimList )
      {
        AlertsValueBean alertsVB = new AlertsValueBean();
        alertsVB.setPromotion( value.getPromotion() );
        alertsVB.setActivityType( ActivityType.PENDING_APPROVALS );
        promoPendingApprovals.add( alertsVB );
      }
    }

    /*
     * // nomination promoIds = getPromotionsByType( liveAndExpiredPromotions, PromotionType.lookup(
     * PromotionType.NOMINATION ) ); claimList = claimService.getNominationClaimsForApprovalByUser(
     * participant.getId(), promoIds, true, null, null, PromotionType.lookup(
     * PromotionType.NOMINATION ), null, null, false, null ); List<PromotionApprovableValue>
     * claimGroupList = claimGroupService.getClaimGroupsForApprovalByUser( participant.getId(),
     * promoIds, true, PromotionType.lookup( PromotionType.NOMINATION ), null, null, false,
     * Boolean.FALSE ); if ( claimList != null && claimList.size() > 0 ) { for (
     * PromotionApprovableValue value : claimList ) { if (value.isActivePromoAppValue()) {
     * AlertsValueBean alertsVB = new AlertsValueBean(); alertsVB.setPromotion( value.getPromotion()
     * ); alertsVB.setActivityType( ActivityType.PENDING_APPROVALS ); promoPendingApprovals.add(
     * alertsVB ); } } } if ( claimGroupList != null && claimGroupList.size() > 0 ) { for (
     * PromotionApprovableValue value : claimGroupList ) { AlertsValueBean alertsVB = new
     * AlertsValueBean(); alertsVB.setPromotion( value.getPromotion() ); alertsVB.setActivityType(
     * ActivityType.PENDING_APPROVALS ); promoPendingApprovals.add( alertsVB ); } }
     */

    // new nominations
    if ( isMessagesPage )
    {
      Map<String, Object> parameters = new HashMap<String, Object>();
      parameters.put( "userId", UserManager.getUserId() );
      parameters.put( "sortedBy", "asc" );
      parameters.put( "sortedOn", "date_submitted" );
      parameters.put( "rowNumStart", 0 );
      parameters.put( "rowNumEnd", 10 + 1 );

      PendingNominationsApprovalMainValueBean pendingNominationsApprovalMainValueBean = this.nominationClaimDAO.getPendingNominationClaimsForApprovalByUser( parameters );
      List<NominationsApprovalValueBean> pendingNominationsApprovalslist = pendingNominationsApprovalMainValueBean.getPendingNominationsApprovalslist();

      if ( CollectionUtils.isNotEmpty( pendingNominationsApprovalslist ) && pendingNominationsApprovalslist.get( 0 ).getTotalRecords() > 0 )
      {
        for ( NominationsApprovalValueBean nominationsApprovalValueBean : pendingNominationsApprovalslist )
        {
          AlertsValueBean alertsVB = new AlertsValueBean();
          alertsVB.setNominationsApprovalValueBean( nominationsApprovalValueBean );
          if ( nominationsApprovalValueBean.getPromotionId() != null )
          {
            alertsVB.setPromotion( promotionDAO.getPromotionById( nominationsApprovalValueBean.getPromotionId() ) );
          }
          alertsVB.setPromotionType( PromotionType.NOMINATION );
          alertsVB.setActivityType( ActivityType.PENDING_APPROVALS );
          promoPendingApprovals.add( alertsVB );
        }
      }
    }
    return promoPendingApprovals;
  }

  private Long[] getPromotionsByType( List liveAndExpiredPromotions, PromotionType type )
  {
    List<Long> promotionIds = new ArrayList<Long>();
    for ( int i = 0; i < liveAndExpiredPromotions.size(); i++ )
    {
      Object object = liveAndExpiredPromotions.get( i );
      if ( object instanceof Promotion )
      {
        Promotion promotion = (Promotion)object;
        if ( promotion.getPromotionType().getCode().equals( type.getCode() ) )
        {
          promotionIds.add( promotion.getId() );
        }
      }
      else
      {
        PromotionBean promotionBean = (PromotionBean)object;
        if ( promotionBean.getPromotionType().equals( type.getCode() ) )
        {
          promotionIds.add( promotionBean.getPromotionId() );
        }
      }
    }
    if ( !promotionIds.isEmpty() )
    {
      return promotionIds.toArray( new Long[0] );
    }
    else
    {
      return null;
    }
  }
  // Alerts Performance Tuning

  /**
   * Group claims into ClaimGroups grouped by node and nominee
   *
   * @param promotionClaimsValueList
   */
  protected static List extractTransientClaimGroups( List promotionClaimsValueList )
  {
    Map claimGroupByNomineeAndNodeId = new LinkedHashMap();

    List claims = extractApprovables( promotionClaimsValueList );
    for ( Iterator iter = claims.iterator(); iter.hasNext(); )
    {
      NominationClaim claim = (NominationClaim)iter.next();
      ClaimRecipient claimRecipient = (ClaimRecipient)claim.getClaimRecipients().iterator().next();
      if ( claimRecipient != null )
      {
        if ( claimRecipient.getNode() != null && claimRecipient.getRecipient() != null )
        {
          Long nomineeNodeId = claimRecipient.getNode().getId();
          Long nomineeId = claimRecipient.getRecipient().getId();
          String key = nomineeId + "-" + nomineeNodeId;

          ClaimGroup claimGroup = (ClaimGroup)claimGroupByNomineeAndNodeId.get( key );
          if ( claimGroup == null )
          {
            claimGroup = new ClaimGroup( GuidUtils.generateGuid() );
            claimGroup.setOpen( true );
            claimGroup.setNode( claimRecipient.getNode() );
            claimGroup.setParticipant( claimRecipient.getRecipient() );
            claimGroup.setPromotion( claim.getPromotion() );
            claimGroupByNomineeAndNodeId.put( key, claimGroup );
          }
          claimGroup.addClaim( claim );
        }
      }
    }
    return new ArrayList( claimGroupByNomineeAndNodeId.values() );
  }

  protected static List extractApprovables( List promotionApprovableValueList )
  {
    ArrayList approvables = new ArrayList();

    for ( Iterator iter = promotionApprovableValueList.iterator(); iter.hasNext(); )
    {
      PromotionApprovableValue promotionApprovableValue = (PromotionApprovableValue)iter.next();
      approvables.addAll( promotionApprovableValue.getApprovables() );
    }

    return approvables;
  }

  /**
   * @param approverUserId
   * @param promotion
   * @return a list of open, non-expired claims approvable by the given approver
   */
  private List getApprovableClaims( Long approverUserId, Promotion promotion )
  {
    boolean cumulativeNomination = false;
    List approvableList = new ArrayList();

    if ( promotion.isNominationPromotion() )
    {
      NominationPromotion nominationPromotion = (NominationPromotion)promotion;
      if ( nominationPromotion.isCumulative() )
      {
        cumulativeNomination = true;
      }
    }

    // Nomination cumulative claims
    if ( cumulativeNomination )
    {
      NominationPromotion nominationPromotion = (NominationPromotion)promotion;
      if ( nominationPromotion.isCumulative() )
      {
        AssociationRequestCollection claimGroupAssociationRequestCollection = new AssociationRequestCollection();
        claimGroupAssociationRequestCollection.add( new ClaimGroupAssociationRequest( ClaimGroupAssociationRequest.ALL_WITH_CLAIMS_ALL ) );

        List promotionClaimGroupsValueList = claimGroupService.getClaimGroupsForApprovalByUser( approverUserId,
                                                                                                new Long[] { promotion.getId() },
                                                                                                Boolean.TRUE, // open
                                                                                                              // claims
                                                                                                              // only
                                                                                                PromotionType.lookup( PromotionType.NOMINATION ),
                                                                                                claimGroupAssociationRequestCollection,
                                                                                                null, // don't
                                                                                                      // need
                                                                                                      // promotionAssociationRequest
                                                                                                Boolean.FALSE, // non-expired
                                                                                                               // claims
                                                                                                               // i.e.
                                                                                                               // do
                                                                                                               // not
                                                                                                               // want
                                                                                                               // claims
                                                                                                               // that
                                                                                                               // are
                                                                                                               // open
                                                                                                               // past
                                                                                                               // approval
                                                                                                               // end
                                                                                                               // date
                                                                                                Boolean.FALSE );

        approvableList.addAll( extractTransientClaimGroups( promotionClaimGroupsValueList ) );
      }
    }

    // All other claims
    if ( !cumulativeNomination )
    {
      AssociationRequestCollection claimAssociationRequestCollection = new AssociationRequestCollection();
      claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.ALL ) );

      List promotionClaimsValueList = claimService.getClaimsForApprovalByUser( approverUserId,
                                                                               new Long[] { promotion.getId() },
                                                                               Boolean.TRUE, // open
                                                                                             // claims
                                                                                             // only
                                                                               null, // no start
                                                                                     // submission
                                                                                     // date
                                                                               null, // no end
                                                                                     // submission
                                                                                     // date
                                                                               promotion.getPromotionType(),
                                                                               claimAssociationRequestCollection,
                                                                               null, // don't need
                                                                                     // promotionAssociationRequest
                                                                               Boolean.FALSE,
                                                                               Boolean.FALSE ); // non-expired
                                                                                                // claims
                                                                                                // i.e.
                                                                                                // do
                                                                                                // not
                                                                                                // want
      // claims that are open past approval end
      // date
      approvableList.addAll( extractApprovables( promotionClaimsValueList ) );
    }
    return approvableList;
  }

  /**
   * @param approverUserId
   * @param promotion
   * @return a list of open, non-expired claims approvable by the given approver
   */
  // Alerts Performance Tuning
  private List getApprovableClaims( Long approverUserId, Promotion promotion, boolean cumulativeNomination )
  {
    List approvableList = new ArrayList();
    if ( cumulativeNomination )
    {
      NominationPromotion nominationPromotion = (NominationPromotion)promotion;
      AssociationRequestCollection claimGroupAssociationRequestCollection = new AssociationRequestCollection();
      claimGroupAssociationRequestCollection.add( new ClaimGroupAssociationRequest( ClaimGroupAssociationRequest.ALL_WITH_CLAIMS_ALL ) );
      List promotionClaimGroupsValueList = claimGroupService
          .getClaimGroupsForApprovalByUser( approverUserId,
                                            new Long[] { promotion.getId() },
                                            Boolean.TRUE,
                                            PromotionType.lookup( PromotionType.NOMINATION ),
                                            claimGroupAssociationRequestCollection,
                                            null,
                                            Boolean.FALSE,
                                            Boolean.FALSE );
      approvableList.addAll( extractTransientClaimGroups( promotionClaimGroupsValueList ) );
    }
    else
    {
      AssociationRequestCollection claimAssociationRequestCollection = new AssociationRequestCollection();
      claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );
      List promotionClaimsValueList = claimService.getClaimsForApprovalByUser( approverUserId, new Long[] { promotion.getId() }, promotion.getPromotionType(), claimAssociationRequestCollection );
      approvableList.addAll( extractApprovables( promotionClaimsValueList ) );
    }
    return approvableList;
  }

  // Alerts Performance Tuning
  private List getApproverRequestMoreInfoReceivedAlerts( List eligiblePromos, Participant participant )
  {
    List alertsList = new ArrayList();
    Iterator alertsPromoIter = eligiblePromos.iterator();

    while ( alertsPromoIter.hasNext() )
    {
      PromotionMenuBean promoMenuBean = (PromotionMenuBean)alertsPromoIter.next();
      Promotion promo = promoMenuBean.getPromotion();
      if ( promo.isNominationPromotion() && promoMenuBean.isCanSubmit() )
      {
        List reqMoreInfoClaims = getApproverPendingClaimList( participant );

        if ( reqMoreInfoClaims != null && reqMoreInfoClaims.size() > 0 )
        {
          AlertsValueBean alertsVB = new AlertsValueBean();
          alertsVB.setPromotion( promo );
          alertsVB.setActivityType( ActivityType.APPROVER_REQMOREINFO_REC_ALERT );
          alertsList.add( alertsVB );
        }
      }

    }

    return alertsList;
  }

  private List getApproverPendingClaimList( Participant pax )
  {
    // Setup the query constraint.
    NominationClaimQueryConstraint queryConstraint = new NominationClaimQueryConstraint();

    queryConstraint.setSubmitterId( pax.getId() );
    // queryConstraint.setProxyUserId( proxyUserId );
    queryConstraint.setApprovalStatusType( ApprovalStatusType.PENDING );

    // Setup the association request collection.
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );

    // Get the claim list.
    return claimService.getClaimListWithAssociations( queryConstraint, associationRequestCollection );
  }

  public List getNominatorRequestMoreInfoAlerts( Participant participant )
  {
    List alertsList = new ArrayList();
    List<Long> reqMoreInfoClaims = getReqMoreInfoNominatorClaimList( participant );

    for ( Long claimId : reqMoreInfoClaims )
    {
      AlertsValueBean alertsVB = new AlertsValueBean();
      Claim claim = claimService.getClaimById( claimId );
      alertsVB.setPromotion( claim.getPromotion() );
      alertsVB.setClaimId( claimId );
      alertsVB.setUserId( participant.getId() );
      alertsVB.setActivityType( ActivityType.NOMINATOR_REQMOREINFO_ALERT );
      alertsList.add( alertsVB );
    }

    return alertsList;
  }

  private List getReqMoreInfoNominatorClaimList( Participant pax )
  {
    // Setup the query constraint.
    /*
     * NominationClaimQueryConstraint queryConstraint = new NominationClaimQueryConstraint();
     * queryConstraint.setSubmitterId( pax.getId() ); //queryConstraint.setProxyUserId( proxyUserId
     * ); queryConstraint.setApprovalStatusType( ApprovalStatusType.MORE_INFO ); // Setup the
     * association request collection. AssociationRequestCollection associationRequestCollection =
     * new AssociationRequestCollection(); associationRequestCollection.add( new
     * ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );
     * associationRequestCollection.add( new ClaimAssociationRequest(
     * ClaimAssociationRequest.CLAIM_RECIPIENTS ) );
     */

    // Get the claim list.
    return claimService.getClaimIdList( pax.getId(), ApprovalStatusType.MORE_INFO );
  }

  public List getApproverReminderAlerts( List eligiblePromos, Participant participant )
  {
    List alertsList = new ArrayList();
    String timeZoneID = UserManager.getUser().getTimeZoneId();
    Date now = DateUtils.applyTimeZone( new Date(), timeZoneID );
    Iterator alertsPromoIter = eligiblePromos.iterator();

    while ( alertsPromoIter.hasNext() )
    {
      PromotionMenuBean promoMenuBean = (PromotionMenuBean)alertsPromoIter.next();
      Promotion promo = promoMenuBean.getPromotion();

      if ( promo.isLive() && promo.isNominationPromotion() && promo.getPromotionNotifications().size() > 0 )
      {
        Iterator notificationsIter = promo.getPromotionNotifications().iterator();
        while ( notificationsIter.hasNext() )
        {
          PromotionNotification notification = (PromotionNotification)notificationsIter.next();
          if ( notification.isPromotionNotificationType() )
          {
            PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notification;
            long messageId = promotionNotificationType.getNotificationMessageId();

            // Process only when a notification has been set up on the promotion
            String notificationTypeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();
            if ( messageId > 0 && notificationTypeCode.equals( PromotionEmailNotificationType.APPROVER_REMINDER ) )
            {
              NominationPromotion nominationPromotion = (NominationPromotion)promo;
              List approvables = getApprovableClaims( participant.getId(), promo, nominationPromotion.isCumulative() );
              Date claimSubmissionDate = null;
              Date today = com.biperf.core.utils.DateUtils.toStartDate( com.biperf.core.utils.DateUtils.getCurrentDate() );

              for ( Iterator iter = approvables.iterator(); iter.hasNext(); )
              {
                Approvable approvable = (Approvable)iter.next();
                Claim approvableClaim = (Claim)approvable;
                boolean isActiveClaim = true;
                if ( approvableClaim instanceof NominationClaim )
                {
                  isActiveClaim = ( (NominationClaim)approvableClaim ).isActiveNomClaim();
                }
                if ( isActiveClaim )
                {
                  claimSubmissionDate = com.biperf.core.utils.DateUtils.toStartDate( approvableClaim.getSubmissionDate() );

                  // if it has been at least X days since submission then it's overdue
                  if ( com.biperf.core.utils.DateUtils.getElapsedDays( claimSubmissionDate, today ) >= promotionNotificationType.getNumberOfDays().intValue() )
                  {
                    if ( !needApproval( promo ) )
                    {
                      // If so, is sent frequency set to daily
                      boolean isTimeToRemind = false;
                      isTimeToRemind = isEligibleforNotification( promotionNotificationType );

                      if ( isTimeToRemind )
                      {
                        AlertsValueBean alertsVB = new AlertsValueBean();
                        alertsVB.setPromotion( promo );
                        alertsVB.setActivityType( ActivityType.APPROVER_REMINDER );
                        alertsList.add( alertsVB );
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    return alertsList;
  }

  protected boolean needApproval( Promotion promotion )
  {

    if ( promotion.isNominationPromotion() )
    {
      // if now is not within approval period
      if ( !com.biperf.core.utils.DateUtils.isTodaysDateBetween( promotion.getApprovalStartDate(), promotion.getApprovalEndDate() ) )
      {
        return false; // no need to go on
      }
    }
    return true;
  }

  // assuming number of days check alreday done before calling this method
  private boolean isEligibleforNotification( PromotionNotificationType promoNotification )
  {
    boolean isEligible = false;
    PromotionNotificationFrequencyType frequency = promoNotification.getPromotionNotificationFrequencyType();
    Calendar calendar = Calendar.getInstance();

    // null check
    if ( null == frequency )
    {
      return isEligible;
    }
    else if ( frequency.isDaily() )
    {
      return true;
    }
    else if ( frequency.isWeekly() )
    {
      // Is today the day of the week to send?
      Calendar cal = new GregorianCalendar();
      int todayOfWeek = cal.get( Calendar.DAY_OF_WEEK ); // 1=Sunday, 2=Monday, ...
      if ( promoNotification.getDayOfWeekType().getCode().equals( new Integer( todayOfWeek ).toString() ) )
      {
        return true;
      }
    }
    else if ( frequency.isMonthly() )
    {
      // Is today the day of the week to send?
      Calendar cal = new GregorianCalendar();
      int todayOfMonth = cal.get( Calendar.DAY_OF_MONTH );
      if ( promoNotification.getDayOfMonth() == todayOfMonth )
      {
        return true;
      }
    }
    return isEligible;
  }

  public List getBudgetEndAlertForAlerts( List eligiblePromos, Participant participant )
  {
    List alertsList = new ArrayList();
    String timeZoneID = UserManager.getUser().getTimeZoneId();
    Date now = DateUtils.applyTimeZone( new Date(), timeZoneID );
    Iterator alertsPromoIter = eligiblePromos.iterator();

    while ( alertsPromoIter.hasNext() )
    {
      PromotionMenuBean promoMenuBean = (PromotionMenuBean)alertsPromoIter.next();
      Promotion promo = promoMenuBean.getPromotion();
      if ( promo.isLive() && promo.isRecognitionPromotion() && promo.getBudgetMaster() != null && promo.getBudgetMaster().isActive() && promoMenuBean.isCanSubmit()
          && promo.getPromotionNotifications().size() > 0 )
      {
        Iterator notificationsIter = promo.getPromotionNotifications().iterator();
        while ( notificationsIter.hasNext() )
        {
          PromotionNotification notification = (PromotionNotification)notificationsIter.next();
          if ( notification.isPromotionNotificationType() )
          {
            PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notification;
            long messageId = promotionNotificationType.getNotificationMessageId();
            // Process only when a notification has been set up on the promotion

            String notificationTypeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();
            if ( messageId > 0 && notificationTypeCode.equals( PromotionEmailNotificationType.BUDGET_END ) )
            {
              AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
              associationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
              BudgetMaster budgetMaster = budgetMasterService.getBudgetMasterById( promo.getBudgetMaster().getId(), associationRequestCollection );
              BudgetSegment currentBudgetSegment = budgetMaster.getCurrentBudgetSegment( timeZoneID );

              Date budgetEndDate = currentBudgetSegment != null ? currentBudgetSegment.getEndDate() : null;

              if ( budgetEndDate != null )
              {
                Long budgetMasterId = budgetMaster.getId();
                Long userId = participant.getId();
                Date budgetStartDate = currentBudgetSegment.getStartDate();
                boolean showBudgetEndAlert = false;

                // Budget End Notification date : X days prior to the budget end
                Date budgetEndNotificationDate = new Date( budgetEndDate.getTime() - promotionNotificationType.getNumberOfDays().longValue() * org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY );
                if ( DateUtils.isDateBetween( new Date(), budgetEndNotificationDate, budgetEndDate, timeZoneID ) )
                {
                  showBudgetEndAlert = true;
                }

                if ( showBudgetEndAlert )
                {
                  AlertsValueBean alertsVB = new AlertsValueBean();
                  alertsVB.setPromotion( promo );
                  alertsVB.setBudgetEndDate( DateUtils.toDisplayString( budgetEndDate ) );
                  alertsVB.setBudgetSegmentStartDate( budgetStartDate );
                  alertsVB.setBudgetSegmentEndDate( budgetEndDate );
                  alertsVB.setActivityType( ActivityType.BUDGET_END );
                  alertsList.add( alertsVB );
                }
              }
            }
          }
        } // Notification iterator end
      }
    } // Promotion iterator end

    return alertsList;
  }

  private List getPendingPromos( List eligiblePromos, Participant pax )
  {
    List pendingPromos = new ArrayList();
    Iterator promoIter = eligiblePromos.iterator();
    String timeZoneID = UserManager.getUser().getTimeZoneId();
    Date referenceDate = DateUtils.applyTimeZone( new Date(), timeZoneID );
    while ( promoIter.hasNext() )
    {
      PromotionMenuBean promoMenuBean = (PromotionMenuBean)promoIter.next();
      Promotion promo = promoMenuBean.getPromotion();
      if ( promo.isGoalQuestPromotion() )
      {
        if ( promoMenuBean.isCanSubmit() )
        {
          GoalQuestPromotion gqPromo = (GoalQuestPromotion)promo;
          PaxGoal paxGoal = getPaxGoalService().getPaxGoalByPromotionIdAndUserId( gqPromo.getId(), pax.getId() );

          if ( ( pax.isOwner() && gqPromo.getOverrideStructure().equals( ManagerOverrideStructure.lookup( ManagerOverrideStructure.OVERRIDE_PERCENT ) ) || !pax.isManager() )
              && !gqPromo.isIssueAwardsRun() ) // Fix 25646
          {
            if ( DateUtils.isDateBetween( new Date(), DateUtils.toStartDate( gqPromo.getGoalCollectionStartDate() ), DateUtils.toEndDate( gqPromo.getGoalCollectionEndDate() ), timeZoneID ) )
            {
              if ( paxGoal == null || paxGoal.getGoalLevel() == null )
              {
                ActivityCenterValueBean activityCenterValueBean = new ActivityCenterValueBean();
                activityCenterValueBean.setGoalSelectable( true );
                activityCenterValueBean.setPromotion( gqPromo );
                activityCenterValueBean.setGoalSelectionEnddate( DateUtils.toDisplayString( gqPromo.getGoalCollectionEndDate() ) );
                if ( promoMenuBean.isCanViewRules() )
                {
                  activityCenterValueBean.setShowProgramRules( true );
                }
                pendingPromos.add( activityCenterValueBean );
              }
            }
          }
        }
      }
      if ( promo.isChallengePointPromotion() )
      {
        if ( promoMenuBean.isCanSubmit() )
        {
          ChallengePointPromotion cpPromo = (ChallengePointPromotion)promo;
          PaxGoal paxGoal = getPaxGoalService().getPaxGoalByPromotionIdAndUserId( cpPromo.getId(), pax.getId() );

          if ( DateUtils.isDateBetween( new Date(), cpPromo.getGoalCollectionStartDate(), cpPromo.getGoalCollectionEndDate(), timeZoneID ) )
          {
            if ( paxGoal == null || paxGoal.getGoalLevel() == null )
            {
              if ( !pax.isOwner() && !pax.isManager() || cpPromo.getManagerCanSelect().booleanValue() && pax.isOwner() )
              {
                ActivityCenterValueBean activityCenterValueBean = new ActivityCenterValueBean();
                activityCenterValueBean.setCpSelectable( true );
                activityCenterValueBean.setPromotion( cpPromo );
                activityCenterValueBean.setCpSelectionEndDate( DateUtils.toDisplayString( cpPromo.getGoalCollectionEndDate() ) );
                if ( promoMenuBean.isCanViewRules() )
                {
                  activityCenterValueBean.setShowProgramRules( true );
                }
                pendingPromos.add( activityCenterValueBean );
              }
            }

          }
        }
      }
    }
    return pendingPromos;
  }

  private List<ActivityCenterValueBean> getPendingApprovals( List<Promotion> liveAndExpiredPromotions, List eligiblePromos, Participant participant )
  {
    List<ActivityCenterValueBean> promoPendingApprovals = new ArrayList<ActivityCenterValueBean>();

    // product claim promotions
    Long[] promoIds = getPromotionsByType( liveAndExpiredPromotions, PromotionType.lookup( PromotionType.PRODUCT_CLAIM ) );
    List<PromotionApprovableValue> claimList = claimService
        .getProductClaimsForApprovalByUser( participant.getId(), promoIds, true, null, null, PromotionType.lookup( PromotionType.PRODUCT_CLAIM ), null, null, false, null, null, 0, 0 );
    Set productClaimPromotionIds = new HashSet<String>();
    if ( claimList != null && claimList.size() > 0 )
    {
      for ( PromotionApprovableValue value : claimList )
      {
        ActivityCenterValueBean activityCenterValueBean = new ActivityCenterValueBean();
        activityCenterValueBean.setPromotion( value.getPromotion() );
        activityCenterValueBean.setActivityType( ActivityType.PENDING_APPROVALS );
        activityCenterValueBean.setNumberOfApprovables( getApprovableCount( claimList ) );
        promoPendingApprovals.add( activityCenterValueBean );
        // add to promotionId set
        productClaimPromotionIds.add( value.getPromotion().getId() );
      }
    }

    // Closed product claims
    List<PromotionApprovableValue> productClaimClosedClaimList = claimService
        .getProductClaimsForApprovalByUser( participant.getId(), promoIds, false, null, null, PromotionType.lookup( PromotionType.PRODUCT_CLAIM ), null, null, false, null, null, 0, 0 );
    if ( productClaimClosedClaimList != null && productClaimClosedClaimList.size() > 0 )
    {
      for ( PromotionApprovableValue value : productClaimClosedClaimList )
      {
        if ( !productClaimPromotionIds.contains( value.getPromotion().getId() ) )
        {
          ActivityCenterValueBean activityCenterValueBean = new ActivityCenterValueBean();
          activityCenterValueBean.setPromotion( value.getPromotion() );
          activityCenterValueBean.setNumberOfApproved( getProductClaimApprovedCount( value ) );
          promoPendingApprovals.add( activityCenterValueBean );
        }
      }
    }

    // recognition promotions
    promoIds = getPromotionsByType( liveAndExpiredPromotions, PromotionType.lookup( PromotionType.RECOGNITION ) );
    claimList = claimService.getClaimsForApprovalByUser( participant.getId(), promoIds, true, null, null, PromotionType.lookup( PromotionType.RECOGNITION ), null, null, false, false );
    if ( claimList != null && claimList.size() > 0 )
    {
      for ( PromotionApprovableValue value : claimList )
      {
        ActivityCenterValueBean activityCenterValueBean = new ActivityCenterValueBean();
        activityCenterValueBean.setPromotion( value.getPromotion() );
        activityCenterValueBean.setActivityType( ActivityType.PENDING_APPROVALS );
        activityCenterValueBean.setNumberOfApprovables( getApprovableCount( claimList ) );
        promoPendingApprovals.add( activityCenterValueBean );
      }
    }

    // added for getting the count for pending nominations
    Map<String, Object> parameters = new HashMap<String, Object>();
    Long userId = UserManager.getUserId();
    parameters.put( "userId", userId );
    parameters.put( "rowNumStart", 0 );
    parameters.put( "rowNumEnd", 10 + 1 );
    parameters.put( "sortedBy", "desc" );
    parameters.put( "sortedOn", "date_submitted" );

    PendingNominationsApprovalMainValueBean pendingNominationsApprovalMainValueBean = getNominationClaimService().getPendingNominationClaimsForApprovalByUser( parameters );

    List<NominationsApprovalValueBean> pendingNominationsApprovalslist = pendingNominationsApprovalMainValueBean.getPendingNominationsApprovalslist();
    if ( !pendingNominationsApprovalslist.isEmpty() )
    {
      for ( NominationsApprovalValueBean nominationsApprovalValueBean : pendingNominationsApprovalslist )
      {
        ActivityCenterValueBean activityCenterValueBean = new ActivityCenterValueBean();
        activityCenterValueBean.setPromotion( getPromotionById( nominationsApprovalValueBean.getPromotionId() ) );
        activityCenterValueBean.setActivityType( ActivityType.PENDING_APPROVALS );
        activityCenterValueBean.setNumberOfApprovables( pendingNominationsApprovalslist.get( 0 ).getTotalRecords() );
        promoPendingApprovals.add( activityCenterValueBean );
      }
    }

    // nominations promotions
    /*
     * promoIds = getPromotionsByType( liveAndExpiredPromotions, PromotionType.lookup(
     * PromotionType.NOMINATION ) ); claimList = claimService.getNominationClaimsForApprovalByUser(
     * participant.getId(), promoIds, true, null, null, PromotionType.lookup(
     * PromotionType.NOMINATION ), null, null, false, null, null ); List<PromotionApprovableValue>
     * claimGroupList = claimGroupService.getNominationClaimGroupsForApprovalByUser(
     * participant.getId(), promoIds, Boolean.TRUE, PromotionType.lookup( PromotionType.NOMINATION
     * ), null, null, false, null,null ); List<Long> claimGroupPromoIds = new ArrayList<Long>(); Set
     * nominationPromotionIds = new HashSet<String>(); if ( claimList != null && claimList.size() >
     * 0 ) { for ( PromotionApprovableValue value : claimList ) { if (value.isActivePromoAppValue())
     * { ActivityCenterValueBean activityCenterValueBean = new ActivityCenterValueBean();
     * activityCenterValueBean.setPromotion( value.getPromotion() );
     * activityCenterValueBean.setActivityType( ActivityType.PENDING_APPROVALS );
     * activityCenterValueBean.setNumberOfApprovables( getNominationApprovableCount( value ) );
     * promoPendingApprovals.add( activityCenterValueBean ); //add to promotionId set
     * nominationPromotionIds.add( value.getPromotion().getId() ); claimGroupPromoIds.add(
     * value.getPromotion().getId() ); } } } if ( claimGroupList != null && claimGroupList.size() >
     * 0 ) { for ( PromotionApprovableValue value : claimGroupList ) { ActivityCenterValueBean
     * activityCenterValueBean = new ActivityCenterValueBean();
     * activityCenterValueBean.setPromotion( value.getPromotion() );
     * activityCenterValueBean.setNumberOfApprovables( getClaimGroupApprovableCount( value ) );
     * promoPendingApprovals.add( activityCenterValueBean ); claimGroupPromoIds.add(
     * value.getPromotion().getId() ); } } //closed nominations (this feature has been implemented
     * only for nominations) List<PromotionApprovableValue> closedClaimList =
     * claimService.getNominationClaimsForApprovalByUser( participant.getId(), promoIds, false,
     * null, null, PromotionType.lookup( PromotionType.NOMINATION ), null, null, false, null, null
     * ); List<PromotionApprovableValue> closedClaimGroupList =
     * claimGroupService.getNominationClaimGroupsForApprovalByUser( participant.getId(), promoIds,
     * false, PromotionType.lookup( PromotionType.NOMINATION ), null, null, false, null, null ); if
     * ( closedClaimList != null && closedClaimList.size() > 0 ) { for ( PromotionApprovableValue
     * value : closedClaimList ) { if ( !nominationPromotionIds.contains(
     * value.getPromotion().getId() ) && value.isActivePromoAppValue() ) { ActivityCenterValueBean
     * activityCenterValueBean = new ActivityCenterValueBean();
     * activityCenterValueBean.setPromotion( value.getPromotion() );
     * activityCenterValueBean.setNumberOfApproved( getNominationApprovedCount( value ) );
     * promoPendingApprovals.add( activityCenterValueBean ); } } } if ( closedClaimGroupList != null
     * && closedClaimGroupList.size() > 0 ) { for ( PromotionApprovableValue value :
     * closedClaimGroupList ) { if( !claimGroupPromoIds.contains( value.getPromotion().getId() ) ) {
     * ActivityCenterValueBean activityCenterValueBean = new ActivityCenterValueBean();
     * activityCenterValueBean.setPromotion( value.getPromotion() );
     * activityCenterValueBean.setNumberOfApproved( value.getApprovables().size() );
     * promoPendingApprovals.add( activityCenterValueBean ); } } } List<PromotionApprovableValue>
     * expiredClaimList = claimService.getNominationClaimsForApprovalByUser( participant.getId(),
     * promoIds, null, null, null, PromotionType.lookup( PromotionType.NOMINATION ), null, null,
     * true, null, null ); List<PromotionApprovableValue> expiredClaimGroupList =
     * claimGroupService.getNominationClaimGroupsForApprovalByUser( participant.getId(), promoIds,
     * null, PromotionType.lookup( PromotionType.NOMINATION ), null, null, true, null, null ); if (
     * expiredClaimList != null && expiredClaimList.size() > 0 ) { for ( PromotionApprovableValue
     * value : expiredClaimList ) { if ( !nominationPromotionIds.contains(
     * value.getPromotion().getId() ) ) { ActivityCenterValueBean activityCenterValueBean = new
     * ActivityCenterValueBean(); activityCenterValueBean.setPromotion( value.getPromotion() );
     * activityCenterValueBean.setNumberOfApproved( getNominationApprovedCount( value ) );
     * promoPendingApprovals.add( activityCenterValueBean ); } } } if ( expiredClaimGroupList !=
     * null && expiredClaimGroupList.size() > 0 ) { for ( PromotionApprovableValue value :
     * expiredClaimGroupList ) { if( !claimGroupPromoIds.contains( value.getPromotion().getId() ) )
     * { ActivityCenterValueBean activityCenterValueBean = new ActivityCenterValueBean();
     * activityCenterValueBean.setPromotion( value.getPromotion() );
     * activityCenterValueBean.setNumberOfApproved( value.getApprovables().size() );
     * promoPendingApprovals.add( activityCenterValueBean ); } } }
     */

    for ( int i = 0; i < liveAndExpiredPromotions.size(); i++ )
    {
      Promotion promotion = (Promotion)liveAndExpiredPromotions.get( i );
      boolean isShowProgramRules = checkIfShowPromoRules( promotion, participant );

      // ok, assign the isShowProgramRules to the standard list
      for ( ActivityCenterValueBean value : promoPendingApprovals )
      {
        if ( value.getPromotion().getId().equals( promotion.getId() ) )
        {
          value.setShowProgramRules( isShowProgramRules );
          break;
        }
      }
    }
    return promoPendingApprovals;
  }

  private long getApprovableCount( List<PromotionApprovableValue> claimList )
  {
    long approvableCount = 0;
    for ( PromotionApprovableValue claim : claimList )
    {
      approvableCount += claim.getApprovables().size();
    }
    return approvableCount;
  }

  private long getNominationApprovableCount( PromotionApprovableValue value )
  {
    int approvableCnt = 0;
    for ( Iterator iterator = value.getApprovables().iterator(); iterator.hasNext(); )
    {
      NominationClaim claim = (NominationClaim)iterator.next();
      if ( claim.isOpen() && claim.isActiveNomClaim() )
      {
        approvableCnt++;
      }
    }
    return approvableCnt;
  }

  private long getNominationApprovedCount( PromotionApprovableValue value )
  {
    int approvableCnt = 0;
    for ( Iterator iterator = value.getApprovables().iterator(); iterator.hasNext(); )
    {
      NominationClaim claim = (NominationClaim)iterator.next();
      if ( !claim.isOpen() && claim.isActiveNomClaim() )
      {
        approvableCnt++;
      }
    }
    return approvableCnt;
  }

  private long getClaimGroupApprovableCount( PromotionApprovableValue value )
  {
    int approvableCnt = 0;
    for ( Iterator iterator = value.getApprovables().iterator(); iterator.hasNext(); )
    {
      ClaimGroup claimGroup = (ClaimGroup)iterator.next();
      if ( claimGroup.isOpen() )
      {
        approvableCnt++;
      }
    }
    return approvableCnt;
  }

  /*
   * private long getClaimGroupApprovedCount( PromotionApprovableValue value ) { int approvableCnt =
   * 0; for ( Iterator iterator = value.getApprovables().iterator(); iterator.hasNext(); ) {
   * ClaimGroup claimGroup = (ClaimGroup)iterator.next(); if ( !claimGroup.isOpen() ) {
   * approvableCnt++; } } return approvableCnt; }
   */

  private long getProductClaimApprovedCount( PromotionApprovableValue value )
  {
    int approvableCnt = 0;
    for ( Iterator iterator = value.getApprovables().iterator(); iterator.hasNext(); )
    {
      ProductClaim claim = (ProductClaim)iterator.next();
      if ( !claim.isOpen() )
      {
        approvableCnt++;
      }
    }
    return approvableCnt;
  }

  public HashSet<BudgetMaster> getEligibleBudgetTransfer( AuthenticatedUser authenticatedUser )
  {
    HashSet<BudgetMaster> eligibleBudget = new HashSet<BudgetMaster>();
    List<PromotionMenuBean> allLivePromo = getMainContentService().buildEligiblePromoList( authenticatedUser );

    for ( PromotionMenuBean promoMenuBean : allLivePromo )
    {
      if ( promoMenuBean.isCanSubmit() )
      {
        Promotion promotion = promoMenuBean.getPromotion();
        if ( promotion.isRecognitionPromotion() )
        {
          RecognitionPromotion recPromo = (RecognitionPromotion)promotion;
          if ( recPromo.getBudgetMaster() != null && recPromo.getBudgetMaster().isActive() )
          {
            AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
            associationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
            BudgetMaster budgetMaster = budgetMasterService.getBudgetMasterById( recPromo.getBudgetMaster().getId(), associationRequestCollection );

            boolean isAllowBudgetReallocation = budgetMasterDAO.isAllowBudgetReallocActiveForBudgetMaster( budgetMaster.getId() );
            if ( budgetMaster.isParticipantBudget() && isAllowBudgetReallocation )
            {
              eligibleBudget.add( budgetMaster );
            }
            else if ( budgetMaster.isNodeBudget() && isAllowBudgetReallocation )
            {
              String paxTimeZoneId = userService.getUserTimeZoneByUserCountry( authenticatedUser.getUserId() );
              BudgetSegment currentBudgetSegment = budgetMaster.getCurrentBudgetSegment( paxTimeZoneId );
              if ( !budgetMasterService.getAllActiveInBudgetSegmentForOwnerUserNode( currentBudgetSegment.getId(), authenticatedUser.getUserId() ).isEmpty() )
              {
                eligibleBudget.add( budgetMaster );
              }
            }
          }
        }
      }
    }

    return eligibleBudget;
  }

  private List getCurrentPurlInvitations( Participant pax )
  {
    List invitations = new ArrayList();

    if ( pax.isOwner() )
    {
      List<Long> purlManagerNodes = getPurlService().getPurlManagerNodes( pax.getId() );
      List<Promotion> purlInvitationPromotions = getPurlService().getEligiblePurlPromotionsForInvitation( pax.getId(), purlManagerNodes );
      for ( Promotion promotion : purlInvitationPromotions )
      {
        List pendingInvitations = getPurlService().getAllPendingPurlInvitationsForManager( pax.getId(), promotion.getId(), purlManagerNodes );
        if ( pendingInvitations.isEmpty() )
        {
          List currentInvitations = getPurlService().getAllCurrentPurlInvitationsForManager( pax.getId(), promotion.getId(), purlManagerNodes );
          if ( !currentInvitations.isEmpty() )
          {
            int numberOfContributors = getPurlService().getPurlCountributorCount( promotion.getId() );
            ActivityCenterValueBean valueBean = new ActivityCenterValueBean();
            valueBean.setPromotion( promotion );
            valueBean.setActivityType( ActivityType.PURL_INVITATION );
            valueBean.setShowUpdatePurlPromo( true );
            valueBean.setNumberOfContributors( numberOfContributors );
            invitations.add( valueBean );
          }
        }
      }
    }

    return invitations;
  }

  private List getCurrentPurlContributions( Participant pax )
  {
    List purlContributionList = new ArrayList();

    List<Promotion> purlContributionPromotions = getPurlService().getEligiblePurlPromotionsForContributor( pax.getId() );
    for ( Promotion promotion : purlContributionPromotions )
    {
      List pendingContributions = getPurlService().getAllPendingPurlContributions( pax.getId(), promotion.getId(), true );
      if ( pendingContributions.isEmpty() )
      {
        List contributions = getPurlService().getAllCurrentPurlContributions( pax.getId(), promotion.getId(), true );
        if ( !contributions.isEmpty() )
        {
          ActivityCenterValueBean valueBean = new ActivityCenterValueBean();
          valueBean.setPromotion( promotion );
          valueBean.setActivityType( ActivityType.PURL_CONTRIBUTION );
          valueBean.setShowUpdatePurlPromo( true );
          purlContributionList.add( valueBean );
        }
      }
    }

    return purlContributionList;
  }

  private List getCurrentViewPurls( Participant pax )
  {
    List purls = new ArrayList();

    List<Promotion> purlRecipientPromotions = getPurlService().getEligiblePurlPromotionsForRecipient( pax.getId() );
    for ( Promotion promotion : purlRecipientPromotions )
    {
      List pendingInvitations = getPurlService().getAllPendingPurlRecipients( pax.getId(), promotion.getId() );
      if ( pendingInvitations.isEmpty() )
      {
        List currentInvitations = getPurlService().getAllCurrentPurlRecipients( pax.getId(), promotion.getId() );
        if ( !currentInvitations.isEmpty() )
        {
          int numberOfContributors = getPurlService().getPurlCountributorCount( promotion.getId() );
          ActivityCenterValueBean valueBean = new ActivityCenterValueBean();
          valueBean.setPromotion( promotion );
          valueBean.setActivityType( ActivityType.PURL_VIEW );
          valueBean.setShowUpdatePurlPromo( true );
          valueBean.setNumberOfContributors( numberOfContributors );
          purls.add( valueBean );
        }
      }
    }

    return purls;
  }

  public List getPaxPromoRules( AuthenticatedUser authenticatedUser ) throws ServiceErrorException
  {
    ArrayList rulesList = new ArrayList();

    List eligiblePromos = getMainContentService().buildEligiblePromoList( authenticatedUser, true );

    Iterator eligiblePromoIter = eligiblePromos.iterator();

    while ( eligiblePromoIter.hasNext() )
    {
      PromotionMenuBean promoMenuBean = (PromotionMenuBean)eligiblePromoIter.next();

      if ( promoMenuBean.isCanViewRules() && promoMenuBean.getPromotion().isLive() )
      {
        rulesList.add( promoMenuBean.getPromotion() );
      }
    }

    return rulesList;

  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  /**
   * @param claimGroupService value for claimGroupService property
   */
  public void setClaimGroupService( ClaimGroupService claimGroupService )
  {
    this.claimGroupService = claimGroupService;
  }

  /**
   * @param calculatorService value for calculatorService property
   */
  public void setCalculatorService( CalculatorService calculatorService )
  {
    this.calculatorService = calculatorService;
  }

  public void setAwardGeneratorService( AwardGeneratorService awardGeneratorService )
  {
    this.awardGeneratorService = awardGeneratorService;
  }

  /**
   * @param budgetMasterDAO value for budgetMasterDAO property
   */
  public void setBudgetMasterDAO( BudgetMasterDAO budgetMasterDAO )
  {
    this.budgetMasterDAO = budgetMasterDAO;
  }

  public JournalService getJournalService()
  {
    return journalService;
  }

  public void setJournalService( JournalService journalService )
  {
    this.journalService = journalService;
  }

  public MerchLevelService getMerchLevelService()
  {
    return merchLevelService;
  }

  public void setMerchLevelService( MerchLevelService merchLevelService )
  {
    this.merchLevelService = merchLevelService;
  }

  public void setBudgetMasterService( BudgetMasterService budgetMasterService )
  {
    this.budgetMasterService = budgetMasterService;
  }

  public MainContentService getMainContentService()
  {
    return (MainContentService)BeanLocator.getBean( MainContentService.BEAN_NAME );
  }

  public PaxGoalService getPaxGoalService()
  {
    return (PaxGoalService)BeanLocator.getBean( PaxGoalService.BEAN_NAME );
  }

  public ProcessService getProcessService()
  {
    return (ProcessService)BeanLocator.getBean( ProcessService.BEAN_NAME );
  }

  public PurlService getPurlService()
  {
    return (PurlService)BeanLocator.getBean( PurlService.BEAN_NAME );
  }

  public CountryService getCountryService()
  {
    return (CountryService)BeanLocator.getBean( CountryService.BEAN_NAME );
  }

  /* Bug # 34020 start */
  /**
   * @return list
   */
  public List<Promotion> getAllPromotionsWithSweepstakes()
  {
    return this.promotionDAO.getAllPromotionsWithSweepstakes();
  }

  /* Bug # 34020 end */

  public boolean isPendingQuizSubmissionsForUser( Long userId )
  {
    if ( !UserManager.getUser().isParticipant() )
    {
      return false;
    }

    List eligiblePromotionList = new ArrayList();
    PromotionQueryConstraint promoQueryConstraint = new PromotionQueryConstraint();
    promoQueryConstraint.setPromotionTypesIncluded( new PromotionType[] { PromotionType.lookup( PromotionType.QUIZ ), PromotionType.lookup( PromotionType.DIY_QUIZ ) } );
    promoQueryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ) } );
    promoQueryConstraint.setOrderByPromotionNameCaseInsensitive( true );

    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_BUDGET_MASTER ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PRIMARY_AUDIENCES ) );

    List allPromoList = getPromotionListWithAssociations( promoQueryConstraint, promoAssociationRequestCollection );
    Participant participant = participantDAO.getParticipantById( userId );

    Iterator it = allPromoList.iterator();
    while ( it.hasNext() )
    {
      QuizPromotion promotion = (QuizPromotion)it.next();
      boolean showSubmitClaim = isPromotionClaimableByParticipant( promotion.getId(), participant, false );
      if ( showSubmitClaim )
      {
        if ( promotion.isDIYQuizPromotion() )
        {
          return isPendingDIYQuizSubmissionsForUser( promotion, userId );
        }
        else
        {
          // If quiz promotions exist, also get quiz claim history for this user
          QuizClaimQueryConstraint submitterClaimQueryConstraint = new QuizClaimQueryConstraint();
          submitterClaimQueryConstraint.setSubmitterId( userId );
          Map participantQuizClaimHistoryByPromotionMap = claimService.getParticipantQuizClaimHistoryByPromotionMap( submitterClaimQueryConstraint, null );
          ParticipantQuizClaimHistory participantQuizClaimHistory = (ParticipantQuizClaimHistory)participantQuizClaimHistoryByPromotionMap.get( promotion );
          if ( participantQuizClaimHistory == null )
          {
            return true;
          }
          else if ( participantQuizClaimHistory.isInProgress() )
          {
            return true;
          }
          // check if user has pending attempts and not passed yet
          else if ( participantQuizClaimHistory.isRetakeable( UserManager.getUser().getTimeZoneId() ) )
          {
            return true;
          }
        }
      }
    }
    return false;
  }

  private boolean isPendingDIYQuizSubmissionsForUser( Promotion promotion, Long userId )
  {
    List<Long> diyQuizIdList = diyQuizDAO.getActiveQuizzesForParticipantByPromotion( promotion.getId(), userId );
    for ( Long diyQuizId : diyQuizIdList )
    {
      QuizClaimQueryConstraint submitterClaimQueryConstraint = new QuizClaimQueryConstraint();
      submitterClaimQueryConstraint.setSubmitterId( userId );
      submitterClaimQueryConstraint.setDiyQuizId( diyQuizId );
      Map participantDIYQuizClaimHistoryByQuizMap = claimService.getParticipantDIYQuizClaimHistoryByQuizMap( submitterClaimQueryConstraint, null );
      ParticipantDIYQuizClaimHistory participantQuizClaimHistory = (ParticipantDIYQuizClaimHistory)participantDIYQuizClaimHistoryByQuizMap.get( diyQuizId );
      if ( participantQuizClaimHistory == null )
      {
        return true;
      }
      else if ( participantQuizClaimHistory.isInProgress() )
      {
        return true;
      }
      // check if user has pending attempts and not passed yet
      else if ( participantQuizClaimHistory.isRetakeable( UserManager.getUser().getTimeZoneId() ) )
      {
        return true;
      }
    }
    return false;
  }

  /**
   * Get All Pending Quiz Attempts for participant
   * @param allLivePromo
   * @param participant
   * @return
   */
  public List<QuizPageValueBean> getPendingQuizSubmissionList( Long userId )
  {
    List quizPageList = new ArrayList();
    if ( UserManager.getUser().isParticipant() )
    {
      String timeZoneID = UserManager.getUser().getTimeZoneId();

      List eligiblePromotionList = new ArrayList();
      PromotionQueryConstraint promoQueryConstraint = new PromotionQueryConstraint();
      promoQueryConstraint.setPromotionTypesIncluded( new PromotionType[] { PromotionType.lookup( PromotionType.QUIZ ), PromotionType.lookup( PromotionType.DIY_QUIZ ) } );
      promoQueryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ) } );
      promoQueryConstraint.setOrderByPromotionNameCaseInsensitive( true );

      AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();

      List allPromoList = getPromotionListWithAssociations( promoQueryConstraint, promoAssociationRequestCollection );

      Participant participant = participantDAO.getParticipantById( userId );
      Iterator it = allPromoList.iterator();
      while ( it.hasNext() )
      {
        QuizPromotion promotion = (QuizPromotion)it.next();
        boolean showSubmitClaim = isPromotionClaimableByParticipant( promotion.getId(), participant, false );
        if ( showSubmitClaim )
        {
          if ( promotion.isDIYQuizPromotion() )
          {
            List<DIYQuiz> activeQuizzes = diyQuizDAO.getActiveQuizListForParticipantByPromotion( promotion.getId(), participant.getId() );
            for ( DIYQuiz diyQuiz : activeQuizzes )
            {
              QuizPageValueBean tracker = new QuizPageValueBean();
              tracker.setDiyQuizId( diyQuiz.getId() );
              tracker.setPromotion( promotion );
              tracker.setDiyQuizName( diyQuiz.getName() );
              if ( !diyQuiz.isAllowUnlimitedAttempts() )
              {
                tracker.setAttemptsRemaining( new Long( diyQuiz.getMaximumAttempts() ) );
              }
              if ( promotion.isAwardActive() )
              {
                tracker.setAwardAmount( promotion.getAwardAmount() );
              }
              if ( diyQuiz.getEndDate() != null )
              {
                tracker.setTimeRemaining( DateUtils.getElapsedDays( DateUtils.applyTimeZone( DateUtils.getCurrentDate(), timeZoneID ), DateUtils.applyTimeZone( diyQuiz.getEndDate(), timeZoneID ) ) );
              }
              // If quiz promotions exist, also get quiz claim history for this user
              QuizClaimQueryConstraint submitterClaimQueryConstraint = new QuizClaimQueryConstraint();
              submitterClaimQueryConstraint.setSubmitterId( userId );
              submitterClaimQueryConstraint.setDiyQuizId( diyQuiz.getId() );
              Map participantDIYQuizClaimHistoryByQuizMap = claimService.getParticipantDIYQuizClaimHistoryByQuizMap( submitterClaimQueryConstraint, null );

              ParticipantDIYQuizClaimHistory participantQuizClaimHistory = (ParticipantDIYQuizClaimHistory)participantDIYQuizClaimHistoryByQuizMap.get( diyQuiz.getId() );
              if ( participantQuizClaimHistory == null )
              {
                tracker.setTakeQuiz( true );
              }
              else
              {
                if ( !diyQuiz.isAllowUnlimitedAttempts() )
                {
                  tracker.setAttemptsRemaining( new Long( diyQuiz.getMaximumAttempts() - participantQuizClaimHistory.getQuizClaimsBySubmissionDate().size() ) );
                  if ( participantQuizClaimHistory.isInProgress() )
                  {
                    tracker.setAttemptsRemaining( tracker.getAttemptsRemaining() + 1 );
                  }
                }

                if ( participantQuizClaimHistory.isInProgress() )
                {
                  // in progress so offer resume quiz
                  tracker.setResumeQuiz( true );
                  if ( participantQuizClaimHistory.getMostRecentClaim().getId() != null )
                  {
                    tracker.setClaimId( participantQuizClaimHistory.getMostRecentClaim().getId() );
                  }
                }
                else if ( participantQuizClaimHistory.isRetakeable( timeZoneID ) )
                {
                  tracker.setRetakeQuiz( true );
                }
                else
                {
                  tracker.setQuizCompleted( true );
                }
              }
              if ( !tracker.isQuizCompleted() )
              {
                quizPageList.add( tracker );
              }
            }
          }
          else
          {
            long promotionId = promotion.getId();
            QuizPageValueBean tracker = new QuizPageValueBean();
            tracker.setPromotion( promotion );
            if ( !promotion.isAllowUnlimitedAttempts() )
            {
              tracker.setAttemptsRemaining( new Long( promotion.getMaximumAttempts() ) );
            }

            if ( promotion.isAwardActive() )
            {
              tracker.setAwardAmount( promotion.getAwardAmount() );
            }

            if ( promotion.getSubmissionEndDate() != null )
            {
              tracker.setTimeRemaining( DateUtils.getElapsedDays( DateUtils.applyTimeZone( DateUtils.getCurrentDate(), timeZoneID ),
                                                                  DateUtils.applyTimeZone( promotion.getSubmissionEndDate(), timeZoneID ) ) );
            }

            // If quiz promotions exist, also get quiz claim history for this user
            QuizClaimQueryConstraint submitterClaimQueryConstraint = new QuizClaimQueryConstraint();
            submitterClaimQueryConstraint.setSubmitterId( userId );
            Map participantQuizClaimHistoryByPromotionMap = claimService.getParticipantQuizClaimHistoryByPromotionMap( submitterClaimQueryConstraint, null );

            ParticipantQuizClaimHistory participantQuizClaimHistory = (ParticipantQuizClaimHistory)participantQuizClaimHistoryByPromotionMap.get( promotion );
            if ( participantQuizClaimHistory == null )
            {
              tracker.setTakeQuiz( true );
            }
            else
            {
              if ( !promotion.isAllowUnlimitedAttempts() )
              {
                tracker.setAttemptsRemaining( new Long( promotion.getMaximumAttempts() - participantQuizClaimHistory.getQuizClaimsBySubmissionDate().size() ) );
                if ( participantQuizClaimHistory.isInProgress() )
                {
                  tracker.setAttemptsRemaining( tracker.getAttemptsRemaining() + 1 );
                }
              }

              if ( participantQuizClaimHistory.isInProgress() )
              {
                // in progress so offer resume quiz
                tracker.setResumeQuiz( true );
                if ( participantQuizClaimHistory.getMostRecentClaim().getId() != null )
                {
                  tracker.setClaimId( participantQuizClaimHistory.getMostRecentClaim().getId() );
                }
              }
              else if ( participantQuizClaimHistory.isRetakeable( timeZoneID ) )
              {
                tracker.setRetakeQuiz( true );
              }
              else
              {
                tracker.setQuizCompleted( true );
              }
            }
            if ( !tracker.isQuizCompleted() )
            {
              quizPageList.add( tracker );
            }
          }
        }
      }
    }

    return quizPageList;
  }

  /**
   * Get All Pending Surveys for participant
   * @param allLivePromo
   * @param participant
   * @return
   */
  public List<SurveyPageValueBean> getPendingSurveysList( Long userId )
  {
    List surveyPageList = new ArrayList();
    if ( UserManager.getUser().isParticipant() )
    {
      String timeZoneID = UserManager.getUser().getTimeZoneId();

      List eligiblePromotionList = new ArrayList();
      PromotionQueryConstraint promoQueryConstraint = new PromotionQueryConstraint();
      promoQueryConstraint.setPromotionTypesIncluded( new PromotionType[] { PromotionType.lookup( PromotionType.SURVEY ) } );
      promoQueryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ) } );
      promoQueryConstraint.setOrderByPromotionNameCaseInsensitive( true );

      AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
      // promoAssociationRequestCollection.add( new PromotionAssociationRequest(
      // PromotionAssociationRequest.PROMOTION_BUDGET_MASTER ) );
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PRIMARY_AUDIENCES ) );

      List allPromoList = getPromotionListWithAssociations( promoQueryConstraint, promoAssociationRequestCollection );

      Participant participant = participantDAO.getParticipantById( userId );
      Iterator it = allPromoList.iterator();
      while ( it.hasNext() )
      {
        SurveyPromotion promotion = (SurveyPromotion)it.next();
        boolean showSubmitClaim = isPromotionClaimableByParticipant( promotion.getId(), participant, false );
        if ( showSubmitClaim )
        {
          if ( promotion.isSurveyPromotion() )
          {
            long promotionId = promotion.getId();
            SurveyPageValueBean tracker = new SurveyPageValueBean();
            tracker.setPromotion( promotion );

            if ( promotion.getSubmissionEndDate() != null )
            {
              tracker.setTimeRemaining( DateUtils.getElapsedDays( DateUtils.applyTimeZone( DateUtils.getCurrentDate(), timeZoneID ),
                                                                  DateUtils.applyTimeZone( promotion.getSubmissionEndDate(), timeZoneID ) ) );
            }

            ParticipantSurvey paxSurvey = participantSurveyService.getParticipantSurveyByPromotionAndSurveyIdAndUserId( promotion.getId(), promotion.getSurvey().getId(), userId );
            if ( paxSurvey == null )
            {
              tracker.setTakeSurvey( true );
            }
            else
            {
              if ( !paxSurvey.isCompleted() )
              {
                // in progress so offer resume quiz
                tracker.setResumeSurvey( true );
              }
              else
              {
                tracker.setSurveyCompleted( true );
              }

            }
            if ( !tracker.isSurveyCompleted() )
            {
              surveyPageList.add( tracker );
            }
          }
        }
      }
    }

    return surveyPageList;
  }

  /**
   * Get all Recognition and Nomination promotions that the participant can submit claims for
   * @param allLivePromo
   * @param participant
   * @return
   */
  public List<RecognitionBean> getRecognitionSubmissionList( Long userId, boolean isUserAParticipant )
  {
    List<AbstractRecognitionPromotion> recognitionSubmissionsList = new ArrayList<AbstractRecognitionPromotion>();
    if ( isUserAParticipant )
    {
      List<Promotion> allPromoList = getMainContentService().getAllLivePromotionsFromCache();

      if ( UserManager.getUser().isDelegate() )
      {
        Proxy proxy = null;
        AssociationRequestCollection proxyAssociationRequestCollection = new AssociationRequestCollection();
        proxyAssociationRequestCollection.add( new ProxyAssociationRequest( ProxyAssociationRequest.ALL ) );
        proxy = getProxyService().getProxyByUserAndProxyUserWithAssociations( UserManager.getUserId(),
                                                                              UserManager.getUser().getOriginalAuthenticatedUser().getUserId(),
                                                                              proxyAssociationRequestCollection );
        allPromoList = getProxyService().getPromotionsAllowedForDelegateEZ( allPromoList, proxy );
      }

      Participant participant = participantDAO.getParticipantById( userId );
      Iterator<Promotion> it = allPromoList.iterator();
      while ( it.hasNext() )
      {
        Promotion promotion = it.next();
        promotion = BaseAssociationRequest.initializeAndUnproxy( promotion );
        // only add Recognition and Nomination promotions that are live
        // Client customizations for WIP #62128 starts
        boolean cheersPromotion = promotion.isRecognitionPromotion() && ( (RecognitionPromotion)promotion ).isCheersPromotion();
        // Client customizations for WIP #62128 ends
        if ( !promotion.isLive() || cheersPromotion || !promotion.isNominationPromotion() && !promotion.isRecognitionPromotion() )
        {
          continue;
        }
        boolean showSubmitClaim = isPromotionClaimableByParticipant( promotion.getId(), participant, false );
        if ( showSubmitClaim && ( promotion.isNominationPromotion() || promotion.isRecognitionPromotion() && promotion.isOnlineEntry() ) )
        {
          recognitionSubmissionsList.add( (AbstractRecognitionPromotion)promotion );
        }
      }
    }
    List<RecognitionBean> beans = new ArrayList<RecognitionBean>( recognitionSubmissionsList.size() );
    for ( AbstractRecognitionPromotion arp : recognitionSubmissionsList )
    {
      boolean isEasy = isEasyPromotion( arp );
      if ( arp.isWebRulesActive() && audienceService.isViewWebRulesVisible( arp.getId(), userId ) )
      {
        beans.add( new RecognitionBean( arp, arp.getWebRulesText(), isEasy ) );
      }
      else
      {
        beans.add( new RecognitionBean( arp, isEasy ) );
      }

    }
    return beans;
  }

  /**
   * Returns only recognition promotions, not nomination promotions.
   * {@inheritDoc}
   */
  public List<RecognitionBean> getRecognitionSubmissionList( Long userId, List<PromotionMenuBean> eligiblePromotions, boolean isUserAParticipant )
  {
    List<Long> promotionIds = new ArrayList<Long>();
    List<AbstractRecognitionPromotion> recognitionSubmissionsList = new ArrayList<AbstractRecognitionPromotion>();
    if ( isUserAParticipant )
    {
      PromotionQueryConstraint promoQueryConstraint = new PromotionQueryConstraint();
      for ( PromotionMenuBean bean : eligiblePromotions )
      {
        promotionIds.add( bean.getPromotion().getId() );
      }

      List allPromoList = getMainContentService().getAllLivePromotionsFromCache();

      Participant participant = participantDAO.getParticipantById( userId );
      PromotionMenuBean currentPromotion = null;

      Iterator<Promotion> it = allPromoList.iterator();
      while ( it.hasNext() )
      {
        Promotion promotion = it.next();
        // skip this if the user doesn't have it in his/her eligible promotion list
        // Client customizations for WIP #62128 starts
        boolean cheersPromotion = promotion.isRecognitionPromotion() && ( (RecognitionPromotion)promotion ).isCheersPromotion();
        // Client customizations for WIP #62128 ends
        if ( !promotionIds.contains( promotion.getId() ) || cheersPromotion )
        {
          continue;
        }
        boolean showSubmitClaim = isPromotionClaimableByParticipant( promotion.getId(), participant, false );
        if ( showSubmitClaim && promotion.isRecognitionPromotion() && promotion.isOnlineEntry() )
        {
          promotion = BaseAssociationRequest.initializeAndUnproxy( promotion );
          recognitionSubmissionsList.add( (AbstractRecognitionPromotion)promotion );
        }
      }
    }

    List<RecognitionBean> beans = new ArrayList<RecognitionBean>( recognitionSubmissionsList.size() );
    for ( AbstractRecognitionPromotion arp : recognitionSubmissionsList )
    {
      // get the existing one in the eligible list
      Long promotionId = arp.getId();
      boolean isViewWebRules = false;
      for ( PromotionMenuBean eligiblePromotion : eligiblePromotions )
      {
        if ( promotionId.equals( eligiblePromotion.getPromotion().getId() ) )
        {
          isViewWebRules = eligiblePromotion.isCanViewRules();
          break;
        }
      }
      if ( arp.isWebRulesActive() && isViewWebRules ) // &&
                                                      // audienceService.isViewWebRulesVisible(arp.getId(),
                                                      // userId)
      {
        beans.add( new RecognitionBean( arp, arp.getWebRulesText(), false ) );
      }
      else
      {
        beans.add( new RecognitionBean( arp ) );
      }
    }
    return beans;
  }

  // public void getMobileRecognitionSubmissionList(Long userId, List<PromotionMenuBean>
  // eligiblePromotions, boolean isUserAParticipant)
  // {
  // List<RecognitionBean> recognitionPromotions = getRecognitionSubmissionList(userId,
  // eligiblePromotions, isUserAParticipant);
  // Set<UserNode> userNodes = getUserService().getUserNodes(userId);
  // UserNode primaryUserNode = getPrimaryNodeFrom(userNodes);
  //
  // for(RecognitionBean recognitionBean : recognitionPromotions)
  // {
  // EligibleRecognitionPromotion erp = new EligibleRecognitionPromotion(recognitionBean.getId(),
  // recognitionBean.getName());
  //
  // if(recognitionBean.isPointsAwardActive())
  // {
  // //iterate through all of the user's nodes, looking for a budget for each node
  // for(UserNode un : userNodes)
  // {
  // Budget b = getAvailableBudgetXXX(recognitionBean.getId(), userId, un.getNode().getId());
  // if(b != null)
  // {
  // BudgetInfo budgetInfo = new BudgetInfo(un.getNode().getId(), b);
  // erp.setBudgetInfo(budgetInfo);
  // }
  // else
  // {
  // erp.setNodeId(un.getNode().getId());
  // }
  // }
  // }
  // else
  // {
  // //use the user's primary node as the node ID
  // erp.setNodeId(primaryUserNode.getNode().getId());
  //
  // //get the total sent
  // // acti
  // }
  // }
  //
  // }
  //
  // private Budget getAvailableBudgetXXX(Long promotionId, Long participantId, Long nodeId)
  // {
  // final BigDecimal US_MEDIA_VALUE = getCountryService().getBudgetMediaValueByCountryCode(
  // Country.UNITED_STATES );
  // final BigDecimal USER_MEDIA_VALUE = getUserService().getBudgetMediaValueForUser( participantId
  // );
  //
  // Budget budget = null;
  //
  // try
  // {
  // Promotion promotion = getPromotionById( promotionId );
  // budget = getAvailableBudget( promotionId, participantId, nodeId );
  //
  // if ( !promotion.getBudgetMaster().isCentralBudget() )
  // {
  // budget.setOriginalValue( BudgetUtils.applyMediaConversion( budget.getOriginalValue(),
  // US_MEDIA_VALUE, USER_MEDIA_VALUE ) );
  // budget.setCurrentValue( BudgetUtils.applyMediaConversion( budget.getCurrentValue(),
  // US_MEDIA_VALUE, USER_MEDIA_VALUE ) );
  // }
  // }
  // catch( BeaconRuntimeException e )
  // {
  // // ignore; if the promotion has no budget this exception is thrown
  // }
  //
  // return budget;
  // }
  //
  // private UserNode getPrimaryNodeFrom(Set<UserNode> userNodes)
  // {
  // UserNode primary = null;
  // for(UserNode un : userNodes)
  // {
  // if(un.getIsPrimary())
  // {
  // primary = un;
  // break;
  // }
  // }
  // return primary;
  // }
  //
  /**
   * Get all Nomination promotions (and only Nomination) that the participant can submit claims for
   * @param userId
   * @param isUserAParticipant
   * @return
   */
  public List<RecognitionBean> getNominationSubmissionList( Long userId, boolean isUserAParticipant )
  {
    List<RecognitionBean> allPromotions = getRecognitionSubmissionList( userId, isUserAParticipant );

    List<RecognitionBean> nominations = new ArrayList<RecognitionBean>();
    for ( RecognitionBean bean : allPromotions )
    {
      if ( bean.isNomination() )
      {
        nominations.add( bean );
      }
    }

    return nominations;
  }

  /**
   * Get all Easy Recognition promotions for the given submitter and given receiver.
   * Return the list of Easy Recognitions where the submitter is a valid giver
   * and the receiver is a valid receiver.
   * Does not include nomination promotions.
   * @param allLivePromo
   * @param participant
   * @return
   */
  public List<RecognitionBean> getEasyRecognitionSubmissionList( Long submitterId, boolean isSubmitterAParticipant, Long recipientId )
  {
    // first get the list of promotions for the submitter
    List<RecognitionBean> submitterPromotions = getRecognitionSubmissionList( submitterId, isSubmitterAParticipant );
    List<Promotion> currentEligiblePromotions = getMainContentService().getAllLivePromotionsFromCache();
    // now reduce those promotions to the list of Easy promotions
    List<RecognitionBean> unionList = new ArrayList<RecognitionBean>();
    Participant recipient = participantDAO.getParticipantById( recipientId );

    for ( RecognitionBean bean : submitterPromotions )
    {
      // Included nomination promotions based on bug 77966

      AbstractRecognitionPromotion promotion = null;
      for ( Promotion cachedPromo : currentEligiblePromotions )
      {
        if ( cachedPromo.getId().equals( bean.getId() ) )
        {
          promotion = (AbstractRecognitionPromotion)cachedPromo;
          break;
        }
      }
      if ( isParticipantMemberOfPromotionAudience( recipient, promotion, false, null ) )
      {
        // boolean isEasy=isEasyPromotion( promotion );

        unionList.add( bean );
      }

    }
    return unionList;
  }

  @Override
  public boolean isEasyPromotion( AbstractRecognitionPromotion promotion )
  {
    if ( ! ( promotion instanceof RecognitionPromotion ) )
    {
      return false;
    }

    RecognitionPromotion rp = (RecognitionPromotion)promotion;
    boolean isEasy = false;

    if ( !rp.isIncludePurl() && !promotion.isAwardActive() && ( !promotion.isBehaviorActive() || promotion.isBehaviorActive() && !rp.isBehaviorRequired() )
        && !hasCustomFormFields( promotion.getClaimForm() ) && !rp.isIncludeCelebrations() )
    {
      isEasy = true;
    }

    return isEasy;
  }

  public boolean isEasyPromotionWithBehaviors( AbstractRecognitionPromotion promotion )
  {
    return isEasyPromotion( promotion ) && promotion.isBehaviorActive();
  }

  private boolean hasCustomFormFields( ClaimForm claimForm )
  {
    if ( claimForm != null && claimForm.getClaimFormSteps() != null )
    {
      for ( ClaimFormStep step : claimForm.getClaimFormSteps() )
      {
        if ( step.getClaimFormStepElements() != null && !step.getClaimFormStepElements().isEmpty() )
        {
          return true;
        }
      }
    }

    return false;
  }

  public List<ActivityCenterValueBean> getPendingAlertsList( Long userId, List eligiblePromo )
  {
    Participant pax = participantService.getParticipantById( userId );
    List liveAndExpiredPromotions = new ArrayList();

    if ( null == eligiblePromo )
    {
      eligiblePromo = getMainContentService().buildEligiblePromoList( UserManager.getUser() );
    }
    Iterator livePromosIter = eligiblePromo.iterator();
    while ( livePromosIter.hasNext() )
    {
      PromotionMenuBean promoMenuBean = (PromotionMenuBean)livePromosIter.next();
      Promotion promotion = promoMenuBean.getPromotion();
      if ( !promotion.isLive() )
      {
        livePromosIter.remove();
      }
      else
      {
        liveAndExpiredPromotions.add( promotion );
      }
    }
    PromotionQueryConstraint promoQueryConstraint = new PromotionQueryConstraint();
    List pendingApprovals = null;
    promoQueryConstraint = promoQueryConstraint.buildExpiredPromotionsConstraint();
    liveAndExpiredPromotions.addAll( getPromotionList( promoQueryConstraint ) );
    if ( liveAndExpiredPromotions != null && liveAndExpiredPromotions.size() > 0 )
    {
      pendingApprovals = getPendingApprovals( liveAndExpiredPromotions, eligiblePromo, pax );

    }

    return pendingApprovals;
  }

  public Budget getPublicRecognitionBudget( Long promotionId, Long participantId, Long nodeId )
  {
    RecognitionPromotion promotion = (RecognitionPromotion)promotionDAO.getPromotionById( promotionId );
    if ( !promotion.isPublicRecBudgetUsed() )
    {
      throw new BeaconRuntimeException( "getPublicRecognitionBudget() not applicable for promotions of type: " + promotion.getPromotionType() );
    }

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
    BudgetMaster budgetMaster = budgetMasterService.getBudgetMasterById( promotion.getPublicRecogBudgetMaster().getId(), associationRequestCollection );

    String paxTimeZoneId = this.getUserService().getUserTimeZoneByUserCountry( participantId );
    BudgetSegment currentBudgetSegment = budgetMaster.getCurrentBudgetSegment( paxTimeZoneId );

    if ( currentBudgetSegment != null && budgetMaster.getBudgetType().getCode().equals( BudgetType.CENTRAL_BUDGET_TYPE ) )
    {
      // Get budget - Only ever one budget for CENTRAL_BUDGET_TYPE.
      return (Budget)currentBudgetSegment.getBudgets().iterator().next();
    }

    Participant participant = participantDAO.getParticipantById( participantId );
    Node node = nodeDAO.getNodeById( nodeId );

    Budget availableBudget = null;

    // Check to see if budgetMaster even exists
    // Check to see if participant is in giver (submitter) audience
    // Check to see if user is in node that is passed
    if ( budgetMaster == null || !audienceService.isParticipantInPublicRecognitionAudience( promotion, participant ) || !participant.isInNode( node ) )
    {
      return availableBudget;
    }

    if ( currentBudgetSegment != null && budgetMaster.getBudgetType().getCode().equals( BudgetType.NODE_BUDGET_TYPE ) )
    {
      availableBudget = budgetMasterDAO.getBudgetForNodeByBudgetSegmentId( currentBudgetSegment.getId(), nodeId );
    } // if
    else if ( currentBudgetSegment != null && budgetMaster.getBudgetType().getCode().equals( BudgetType.PAX_BUDGET_TYPE ) )
    {
      availableBudget = budgetMasterDAO.getBudgetForUserbyBudgetSegmentId( currentBudgetSegment.getId(), participant.getId() );
    }

    return availableBudget;
  }

  public List getAwardGeneratorAlertForManager( List eligiblePromotions, Participant participant )
  {
    List<AlertsValueBean> managerReminder = new ArrayList<AlertsValueBean>();

    if ( eligiblePromotions != null && eligiblePromotions.size() > 0 )
    {
      List managerReminderList = awardGeneratorService.getAwardGeneratorManagerRemindersList( participant.getId() );

      if ( managerReminderList != null && managerReminderList.size() > 0 )
      {
        Iterator managerReminderListIter = managerReminderList.iterator();
        while ( managerReminderListIter.hasNext() )
        {
          AwardGeneratorManagerReminderBean agManagerReminder = (AwardGeneratorManagerReminderBean)managerReminderListIter.next();
          Promotion promo = getPromotionById( agManagerReminder.getPromotionId() );
          AlertsValueBean alertsVB = new AlertsValueBean();
          alertsVB.setPromotion( promo );
          alertsVB.setBatchId( agManagerReminder.getBatchId() );
          alertsVB.setAwardIssuedDate( DateUtils.toDisplayString( agManagerReminder.getCreatedDate() ) );
          alertsVB.setAwardExpiryDate( DateUtils.toDisplayString( agManagerReminder.getExpirationDate() ) );
          alertsVB.setActivityType( ActivityType.AWARD_GENERATOR_MANAGER_REMINDER );
          managerReminder.add( alertsVB );
        }
      }
    }
    return managerReminder;
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public List getNominationMyWinnersList( Participant participant )
  {
    List alertsList = new ArrayList();

    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put( "userId", participant.getId() );
    parameters.put( "rowNumStart", 0 );
    parameters.put( "rowNumEnd", 100 );
    parameters.put( "sortedBy", "asc" );
    parameters.put( "sortedOn", "date_won" );

    Map<String, Object> output = this.nominationClaimDAO.getNominationMyWinnersList( parameters );
    List<NominationMyWinners> myWinners = (List<NominationMyWinners>)output.get( "p_out_data" );

    if ( myWinners != null && myWinners.size() > 0 )
    {
      for ( NominationMyWinners myWinner : myWinners )
      {
        AlertsValueBean alertsVB = new AlertsValueBean();
        alertsVB.setActivityId( myWinner.getActivityId() );
        alertsVB.setTeamId( myWinner.getTeamId() );
        alertsVB.setUserId( participant.getId() );
        alertsVB.setActivityType( ActivityType.NOMINATION_PAX_WINNER_ALERT );
        alertsList.add( alertsVB );
      }
    }
    return alertsList;
  }

  /**
   * {@inheritDoc}
   */
  public List<AlertsValueBean> getInProgressNominationClaimsCount( List eligiblePromos, Participant participant )
  {
    List alertsList = new ArrayList();
    AlertsValueBean alertsVB = new AlertsValueBean();
    NominationClaimQueryConstraint qryConstrint = new NominationClaimQueryConstraint();
    int nominationSavedCount = 0;

    if ( eligiblePromos != null && eligiblePromos.size() > 0 )
    {
      Iterator livePromosIter = eligiblePromos.iterator();
      while ( livePromosIter.hasNext() )
      {
        PromotionMenuBean promoMenuBean = (PromotionMenuBean)livePromosIter.next();
        Promotion promotion = promoMenuBean.getPromotion();
        Date todaysDate = new Date();

        // Logic not to display saved nominations after promotion end date
        if ( promotion.isNominationPromotion() && ( promotion.getSubmissionEndDate() == null || org.apache.commons.lang3.time.DateUtils.isSameDay( todaysDate, promotion.getSubmissionEndDate() )
            || promotion.getSubmissionEndDate().after( DateUtils.toDate( DateUtils.toDisplayString( new Date() ) ) ) ) )
        {
          qryConstrint.setIncludedPromotionIds( new Long[] { promotion.getId() } );
          qryConstrint.setSubmitterId( participant.getId() );
          qryConstrint.setNominationStatusType( NominationClaimStatusType.lookup( NominationClaimStatusType.INCOMPLETE ) );
          int cnt = claimService.getClaimListCount( qryConstrint );
          if ( cnt != 0 )
          {
            nominationSavedCount = nominationSavedCount + cnt;
          }
        }
      }
    }
    if ( nominationSavedCount != 0 )
    {
      alertsVB.setActivityType( ActivityType.NOMINATION_PAX_SAVED_SUBMISSIONS );
      alertsVB.setPaxNomineeInCompleteSubmissions( nominationSavedCount );
      alertsList.add( alertsVB );
    }

    return alertsList;
  }

  // Alerts Performance Tuning
  public List getAwardReminderListForAlerts( List eligiblePromos, Participant participant, Map<Long, Promotion> eligiblePromoMap )
  {
    List<AlertsValueBean> awardReminder = new ArrayList<AlertsValueBean>();
    if ( eligiblePromos != null && eligiblePromos.size() > 0 )
    {
      Country userCountry = userService.getPrimaryUserAddressCountry( participant.getId() );
      // if ( participant.getPrimaryAddress() != null &&
      // participant.getPrimaryAddress().getAddress() != null &&
      // participant.getPrimaryAddress().getAddress().getCountry() != null )
      if ( userCountry != null )
      {
        // Country country = participant.getPrimaryAddress().getAddress().getCountry();
        List promoAwardReminderList = getMainContentService().getMerchAwardRemindersForAcivityList( participant.getId(), eligiblePromos, userCountry );
        // Remove PURL Promotion for New SA
        if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
        {
          removePurlEnabledPromotion( promoAwardReminderList );
        }
        if ( promoAwardReminderList != null && promoAwardReminderList.size() > 0 )
        {
          Iterator promoAwardReminderListIter = promoAwardReminderList.iterator();
          while ( promoAwardReminderListIter.hasNext() )
          {
            MerchAwardReminderBean merchAwardReminder = (MerchAwardReminderBean)promoAwardReminderListIter.next();
            ProjectionCollection projections = new ProjectionCollection();
            projections.add( new ProjectionAttribute( "giftCode" ) );
            MerchOrder merchOrder = merchOrderService.getMerchOrderByIdWithProjections( merchAwardReminder.getMerchOrderId(), projections );
            if ( merchOrder.getGiftCode() != null )
            {
              Promotion promo = eligiblePromoMap.get( merchAwardReminder.getPromotionId() );
              AlertsValueBean alertsVB = new AlertsValueBean();
              alertsVB.setPromotion( promo );
              alertsVB.setOnlineShoppingUrl( merchAwardReminder.getOnlineShoppingUrl() );
              alertsVB.setAwardIssuedDate( DateUtils.toDisplayString( merchAwardReminder.getSubmittedDate() ) );
              alertsVB.setAwardExpiryDate( DateUtils.toDisplayString( merchAwardReminder.getExpirationDate() ) );
              alertsVB.setActivityType( ActivityType.AWARD_REMINDER );
              awardReminder.add( alertsVB );
            }
          }
        }
      }
    }
    return awardReminder;
  }

  private void removePurlEnabledPromotion( List promoAwardReminderList )
  {
    if ( promoAwardReminderList != null && promoAwardReminderList.size() > 0 )
    {
      Iterator promoAwardReminderListIter = promoAwardReminderList.iterator();
      while ( promoAwardReminderListIter.hasNext() )
      {
        MerchAwardReminderBean merchAwardReminder = (MerchAwardReminderBean)promoAwardReminderListIter.next();
        Promotion promotion = promotionDAO.getPromotionById( merchAwardReminder.getPromotionId() );
        if ( promotion.isRecognitionPromotion() )
        {
          RecognitionPromotion recognitionPromotion = (RecognitionPromotion)promotion;
          if ( recognitionPromotion.isIncludePurl() || recognitionPromotion.isIncludeCelebrations() )
          {
            promoAwardReminderListIter.remove();
          }
        }
      }
    }
  }

	public List getCelebrationAlertForManager(List eligiblePromotions, Participant participant) {
		boolean isAllowPurlContributionsToSeeOthers = true;
		List<PurlRecipient> purlRecipientLst = null;
		Participant purlParticipant = null;
		List<AlertsValueBean> managerReminder = new ArrayList<AlertsValueBean>();
		if (eligiblePromotions != null && eligiblePromotions.size() > 0) {
			List managerReminderList = celebrationService.getCelebrationManagerRemindersList(participant.getId());
			List newRecipientLst = new ArrayList();

			if (managerReminderList != null && managerReminderList.size() > 0) {
				newRecipientLst = removeDuplicateRecipientId(managerReminderList);
				if (newRecipientLst != null && newRecipientLst.size() > 0) {
					Iterator managerReminderListIter = newRecipientLst.iterator();
					while (managerReminderListIter.hasNext()) {
						CelebrationManagerReminderBean celebrationManagerReminder = (CelebrationManagerReminderBean) managerReminderListIter
								.next();
						purlRecipientLst = getPurlService()
								.getPurlRecipientByUserId(celebrationManagerReminder.getRecipientId());

						if (null != purlRecipientLst &&  null != purlRecipientLst.get(0) ) {
							purlParticipant = (Participant) purlRecipientLst.get(0).getUser();
							isAllowPurlContributionsToSeeOthers = purlParticipant.isAllowPurlContributionsToSeeOthers();
						}
						if (isAllowPurlContributionsToSeeOthers) {
							AlertsValueBean alertsVB = new AlertsValueBean();
							alertsVB.setCelebrationManagerMessageId(celebrationManagerReminder.getManagerMessageId());
							alertsVB.setActivityType(ActivityType.CELEBRATION_MANAGER_REMINDER_ALERT);
							managerReminder.add(alertsVB);
						}
					}
				}
			}
		}
		return managerReminder;
	}

private List<CelebrationManagerReminderBean> removeDuplicateRecipientId(
			List<CelebrationManagerReminderBean> managerReminderList) {

		Set<Long> recipientSet = new HashSet<>();
		List newRecipientLst = new ArrayList();
		if (managerReminderList != null && managerReminderList.size() > 0) {
			Map<Long, CelebrationManagerReminderBean> map = new LinkedHashMap<>();
			for (CelebrationManagerReminderBean ays : managerReminderList) {
				map.put(ays.getRecipientId(), ays);
			}
			managerReminderList.clear();
			managerReminderList.addAll(map.values());
		}
		return managerReminderList;
	}

  public boolean displayPublicRecognitionTile()
  {
    boolean displayTile = false;

    List promotionsList = promotionDAO.getPublicRecognitionPromotionsWithClaims();
    if ( promotionsList != null && promotionsList.size() > 0 )
    {
      displayTile = true;
    }

    return displayTile;
  }

  public List getMerchandisePromotionIds()
  {
    return this.promotionDAO.getMerchandisePromotionIds();
  }

  public List<BadgeLibrary> buildBadgeLibraryList()
  {
    return buildBadgeLibraryList( UserManager.getLocale() );
  }

  public List<BadgeLibrary> buildBadgeLibraryList( Locale locale )
  {
    List badgeLibListFinal = new ArrayList();
    List badgeLibraryLisInitial = new ArrayList();
    ContentReader contentReader = ContentReaderManager.getContentReader();
    List badgeLibList = new ArrayList();
    badgeLibList = (List)contentReader.getContent( "promotion.badge", locale );

    Iterator it = badgeLibList.iterator();
    while ( it.hasNext() )
    {
      Content content = (Content)it.next();
      Map m = content.getContentDataMapList();
      Translations nameObject = (Translations)m.get( "NAME" );
      Translations descObject = (Translations)m.get( "DESCRIPTION" );
      Translations imgSmallObject = (Translations)m.get( "EARNED_IMAGE_SMALL" );
      String badgeLibId = nameObject.getValue();
      String badgeLibDesc = descObject.getValue();
      String badgeLibImg = imgSmallObject.getValue();
      BadgeLibrary badgeLib = new BadgeLibrary();
      badgeLib.setBadgeLibraryId( badgeLibId );
      badgeLib.setLibraryname( badgeLibDesc );
      badgeLib.setEarnedImageSmall( badgeLibImg );
      badgeLibListFinal.add( badgeLib );
    }
    PropertyComparator.sort( badgeLibListFinal, new MutableSortDefinition( "libraryname", false, true ) );
    badgeLibraryLisInitial.addAll( badgeLibListFinal );
    return badgeLibraryLisInitial;
  }

  private String getGoalAssetName( String assetCode )
  {
    // string passed in is "goal_quest_cp.goal_descrpit.xyz"
    // return string is "_GQ_CP_GOAL_DESCRIPTION_xyz"
    if ( StringUtils.isEmpty( assetCode ) || !assetCode.startsWith( Promotion.CM_GOAL_DESCRIPTION_ASSET_PREFIX ) )
    {
      throw new BeaconRuntimeException( "Claim Form asset code does not start with " + ClaimForm.CM_CLAIM_FORM_ASSET_PREFIX );
    }
    return Promotion.CM_GOAL_DESCRIPTION_ASSET_TYPE + assetCode.substring( Promotion.CM_GOAL_DESCRIPTION_ASSET_PREFIX.length() );
  }

  public List<FormattedValueBean> getAwardGenEligiblePromotionList()
  {
    return promotionDAO.getAwardGenEligiblePromotionList();
  }

  public List<FormattedValueBean> getEngagementEligiblePromotionList()
  {
    return promotionDAO.getEngagementEligiblePromotionList();
  }

  public List<FormattedValueBean> getEngagementRecognitionPromotionsList()
  {
    return promotionDAO.getEngagementRecognitionPromotionsList();
  }

  @Override
  public RecognitionPromotion savePurlStandardMessageVideo( RecognitionPromotion promotion, String videoMp4Url, String videoWebmUrl, String video3gpUrl, String videoOggUrl, String rightColumnHtml )
      throws ServiceErrorException, Exception
  {
    try
    {
      if ( StringUtils.isEmpty( promotion.getContentResourceCMCode() ) )
      {
        // Create and set asset to QuizLearningObject
        String newAssetName = cmAssetService.getUniqueAssetCode( QuizLearningObject.QUIZ_LEARNING_CMASSET_PREFIX );
        promotion.setContentResourceCMCode( newAssetName );
      }
      UploadResponse uploadResponse = getMtcService().uploadVideo( new URL( videoMp4Url ) );
      CMDataElement cmDataElementLeft = new CMDataElement( QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME,
                                                           QuizLearningObject.QUIZ_LEARNING_CMASSET_LEFT_KEY,
                                                           videoMp4Url + ActionConstants.REQUEST_ID + uploadResponse.getRequestId(),
                                                           false,
                                                           DataTypeEnum.HTML );
      CMDataElement cmDataElementRight = new CMDataElement( QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME,
                                                            QuizLearningObject.QUIZ_LEARNING_CMASSET_RIGHT_KEY,
                                                            rightColumnHtml,
                                                            false,
                                                            DataTypeEnum.HTML );
      // CMDataElement cmDataElementFilePath = new CMDataElement(
      // QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME,
      // QuizLearningObject.QUIZ_LEARNING_CMASSET_FILE_PATH_KEY, filePath, false,
      // DataTypeEnum.STRING );
      CMDataElement cmDataElementVideoMp4 = new CMDataElement( QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME,
                                                               QuizLearningObject.QUIZ_LEARNING_CMASSET_VIDEO_MP4_KEY,
                                                               videoMp4Url + ActionConstants.REQUEST_ID + uploadResponse.getRequestId(),
                                                               false,
                                                               DataTypeEnum.HTML );
      uploadResponse = getMtcService().uploadVideo( new URL( videoWebmUrl ) );
      CMDataElement cmDataElementVideoWebm = new CMDataElement( QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME,
                                                                QuizLearningObject.QUIZ_LEARNING_CMASSET_VIDEO_WEBM_KEY,
                                                                videoWebmUrl + ActionConstants.REQUEST_ID + uploadResponse.getRequestId(),
                                                                false,
                                                                DataTypeEnum.HTML );
      CMDataElement cmDataElementVideo3gp = new CMDataElement( QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME,
                                                               QuizLearningObject.QUIZ_LEARNING_CMASSET_VIDEO_3GP_KEY,
                                                               video3gpUrl,
                                                               false,
                                                               DataTypeEnum.HTML );
      CMDataElement cmDataElementVideoOgg = new CMDataElement( QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME,
                                                               QuizLearningObject.QUIZ_LEARNING_CMASSET_VIDEO_OGG_KEY,
                                                               videoOggUrl,
                                                               false,
                                                               DataTypeEnum.HTML );
      List elements = new ArrayList();
      elements.add( cmDataElementLeft );
      elements.add( cmDataElementVideoMp4 );
      elements.add( cmDataElementRight );
      elements.add( cmDataElementVideoWebm );
      elements.add( cmDataElementVideo3gp );
      elements.add( cmDataElementVideoOgg );

      cmAssetService.createOrUpdateAsset( QuizLearningObject.QUIZ_SECTION_CODE,
                                          QuizLearningObject.QUIZ_LEARNING_CMASSET_TYPE_NAME,
                                          QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME,
                                          promotion.getContentResourceCMCode(),
                                          elements,
                                          ContentReaderManager.getCurrentLocale(),
                                          null );
    }
    catch( ServiceErrorException | MalformedURLException | JSONException e )
    {
      List errors = new ArrayList();
      errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
      throw e;
    }

    return promotion;
  }

  @Override
  public RecognitionPromotion savePurlStandardMessageImage( RecognitionPromotion promotion, String leftColumnHtml, String rightColumnHtml, String mediaFilePath ) throws ServiceErrorException
  {
    try
    {
      if ( StringUtils.isEmpty( promotion.getContentResourceCMCode() ) )
      {
        // Create and set asset to QuizLearningObject
        String newAssetName = cmAssetService.getUniqueAssetCode( QuizLearningObject.QUIZ_LEARNING_CMASSET_PREFIX );
        promotion.setContentResourceCMCode( newAssetName );
      }

      CMDataElement cmDataElementLeft = new CMDataElement( QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME, QuizLearningObject.QUIZ_LEARNING_CMASSET_LEFT_KEY, leftColumnHtml, false, DataTypeEnum.HTML );
      CMDataElement cmDataElementRight = new CMDataElement( QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME,
                                                            QuizLearningObject.QUIZ_LEARNING_CMASSET_RIGHT_KEY,
                                                            rightColumnHtml,
                                                            false,
                                                            DataTypeEnum.HTML );
      CMDataElement cmDataElementFilePath = new CMDataElement( QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME,
                                                               QuizLearningObject.QUIZ_LEARNING_CMASSET_FILE_PATH_KEY,
                                                               mediaFilePath,
                                                               false,
                                                               DataTypeEnum.STRING );
      CMDataElement cmDataElementVideoMp4 = new CMDataElement( QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME, QuizLearningObject.QUIZ_LEARNING_CMASSET_VIDEO_MP4_KEY, "", false, DataTypeEnum.HTML );
      CMDataElement cmDataElementVideoWebm = new CMDataElement( QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME, QuizLearningObject.QUIZ_LEARNING_CMASSET_VIDEO_WEBM_KEY, "", false, DataTypeEnum.HTML );
      CMDataElement cmDataElementVideo3gp = new CMDataElement( QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME, QuizLearningObject.QUIZ_LEARNING_CMASSET_VIDEO_3GP_KEY, "", false, DataTypeEnum.HTML );
      CMDataElement cmDataElementVideoOgg = new CMDataElement( QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME, QuizLearningObject.QUIZ_LEARNING_CMASSET_VIDEO_OGG_KEY, "", false, DataTypeEnum.HTML );
      List elements = new ArrayList();
      elements.add( cmDataElementLeft );
      elements.add( cmDataElementRight );
      elements.add( cmDataElementFilePath );
      elements.add( cmDataElementVideoMp4 );
      elements.add( cmDataElementVideoWebm );
      elements.add( cmDataElementVideo3gp );
      elements.add( cmDataElementVideoOgg );

      cmAssetService.createOrUpdateAsset( QuizLearningObject.QUIZ_SECTION_CODE,
                                          QuizLearningObject.QUIZ_LEARNING_CMASSET_TYPE_NAME,
                                          QuizLearningObject.QUIZ_LEARNING_CMASSET_NAME,
                                          promotion.getContentResourceCMCode(),
                                          elements,
                                          ContentReaderManager.getCurrentLocale(),
                                          null );
    }
    catch( ServiceErrorException e )
    {
      List errors = new ArrayList();
      errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
      throw e;
    }

    return promotion;
  }

  @Override
  public PromotionCert getPromoCertificateById( Long certificateId )
  {
    return promotionDAO.getPromoCertificateById( certificateId );
  }

  public QuizPromotion getLiveDIYQuizPromotion()
  {
    return promotionDAO.getLiveDIYQuizPromotion();
  }

  public QuizPromotion getLiveOrCompletedDIYQuizPromotion()
  {
    return promotionDAO.getLiveOrCompletedDIYQuizPromotion();
  }

  public EngagementPromotion getLiveOrCompletedEngagementPromotion()
  {
    return promotionDAO.getLiveOrCompletedEngagementPromotion();
  }

  /**
   * Bug # 51917
   * Since the injection is by lookup-method as defined in application context spring will
   * inject the bean. This method will be overridden by spring while creating the bean.
   * return null
   */
  public GoalQuestService getGoalQuestService()
  {
    return null;
  }

  public void setSurveyService( SurveyService surveyService )
  {
    this.surveyService = surveyService;
  }

  public void setParticipantSurveyService( ParticipantSurveyService participantSurveyService )
  {
    this.participantSurveyService = participantSurveyService;
  }

  public TeamDAO getTeamDAO()
  {
    return teamDAO;
  }

  public void setTeamDAO( TeamDAO teamDAO )
  {
    this.teamDAO = teamDAO;
  }

  @Override
  public List<QuizLearningDetails> getPurlStandardMessagePictureObjects( String contentResourceCMCode )
  {
    List<QuizLearningDetails> purlStandardMessagePictureObjectsDetails = new ArrayList<QuizLearningDetails>();
    String cmCode = contentResourceCMCode;
    purlStandardMessagePictureObjectsDetails = getContentFromCM( cmCode );
    // code to get values from CM
    return purlStandardMessagePictureObjectsDetails;
  }

  public List getContentFromCM( String code )
  {
    List returnContent = new ArrayList();
    List contentList = new ArrayList();
    Content content = null;
    ContentReader contentReader = ContentReaderManager.getContentReader();

    if ( contentReader.getContent( code, Locale.ENGLISH ) instanceof java.util.List )
    {
      contentList = (List)contentReader.getContent( code, Locale.ENGLISH );
      content = (Content)contentList.get( 0 );
    }
    else
    {
      content = (Content)contentReader.getContent( code, Locale.ENGLISH );
    }

    Map m = content.getContentDataMapList();
    String leftColumn = "";
    String rightColumn = "";
    String filePath = "";
    String videoUrlMp4 = "";
    String videoUrlWebm = "";
    String videoUrl3gp = "";
    String videoUrlOgg = "";
    Translations leftObject = (Translations)m.get( "LEFT_COLUMN" );
    Translations rightObject = (Translations)m.get( "RIGHT_COLUMN" );
    Translations filePathObject = (Translations)m.get( "FILE_PATH" );
    Translations videoMp4Object = (Translations)m.get( "VIDEO_MP4_URL" );
    Translations videoWebmObject = (Translations)m.get( "VIDEO_WEBM_URL" );
    Translations video3gpObject = (Translations)m.get( "VIDEO_3GP_URL" );
    Translations videoOggObject = (Translations)m.get( "VIDEO_OGG_URL" );
    if ( leftObject != null )
    {
      leftColumn = leftObject.getValue();
      if ( leftColumn.contains( ActionConstants.REQUEST_ID ) )
      {

        MTCVideo mtcVideo = getMtcVideoService().getMTCVideoByRequestId( getRequestId( leftColumn ) );

        if ( Objects.nonNull( mtcVideo ) )
        {
          leftColumn = mtcVideo.getOriginalFormat().equals( "mp4" ) ? mtcVideo.getMp4Url() : mtcVideo.getWebmUrl();

        }
        else
        {
          leftColumn = getActualCardUrl( leftColumn );

        }

      }

    }
    if ( rightObject != null )
    {
      rightColumn = rightObject.getValue();
    }
    if ( filePathObject != null )
    {
      filePath = filePathObject.getValue();
    }
    if ( videoMp4Object != null )
    {
      videoUrlMp4 = videoMp4Object.getValue();
      if ( videoUrlMp4.contains( ActionConstants.REQUEST_ID ) )
      {

        MTCVideo mtcVideo = getMtcVideoService().getMTCVideoByRequestId( getRequestId( videoUrlMp4 ) );

        if ( Objects.nonNull( mtcVideo ) )
        {
          videoUrlMp4 = mtcVideo.getOriginalFormat().equals( "mp4" ) ? mtcVideo.getMp4Url() : mtcVideo.getWebmUrl();

        }
        else
        {
          videoUrlMp4 = getActualCardUrl( videoUrlMp4 );

        }

      }

    }
    if ( videoWebmObject != null )
    {
      videoUrlWebm = videoWebmObject.getValue();
      if ( videoUrlWebm.contains( ActionConstants.REQUEST_ID ) )
      {

        MTCVideo mtcVideo = getMtcVideoService().getMTCVideoByRequestId( getRequestId( videoUrlWebm ) );

        if ( Objects.nonNull( mtcVideo ) )
        {
          videoUrlWebm = mtcVideo.getOriginalFormat().equals( "mp4" ) ? mtcVideo.getMp4Url() : mtcVideo.getWebmUrl();

        }
        else
        {
          videoUrlWebm = getActualCardUrl( videoUrlWebm );

        }

      }

    }
    if ( video3gpObject != null )
    {
      videoUrl3gp = video3gpObject.getValue();
    }
    if ( videoOggObject != null )
    {
      videoUrlOgg = videoOggObject.getValue();
    }
    if ( !StringUtils.isEmpty( videoUrlMp4 ) )
    {
      leftColumn = getVideoHtmlString( videoUrlMp4, videoUrlWebm, videoUrl3gp, videoUrlOgg );
    }
    QuizLearningDetails quizLearn = new QuizLearningDetails();
    quizLearn.setLeftColumn( leftColumn );
    quizLearn.setRightColumn( rightColumn );
    quizLearn.setFilePath( filePath );
    quizLearn.setVideoUrlMp4( videoUrlMp4 );
    quizLearn.setVideoUrlWebm( videoUrlWebm );
    quizLearn.setVideoUrl3gp( videoUrl3gp );
    quizLearn.setVideoUrlOgg( videoUrlOgg );

    returnContent.add( quizLearn );

    return returnContent;
  }

  public String getVideoHtmlString( String videoUrlMp4, String videoUrlWebm, String videoUrl3gp, String videoUrlOgg )
  {
    StringBuilder videoHtml = new StringBuilder( "" );
    String globalUniqueId = String.valueOf( new Date().getTime() );

    if ( videoUrlMp4 != null && ( videoUrlMp4.indexOf( "http://" ) > -1 || videoUrlMp4.indexOf( "https://" ) > -1 ) && videoUrlMp4.indexOf( "-cm/cm3dam" ) < 0 )
    {
      // Note: If div id or class is changed for this then the code in DIYQuizPageView.java needs to
      // be changed to match the new value
      videoHtml.append( "<div id=\"PURLMainVideoWrapper\" class=\"PURLMainVideoWrapper\">" );
      videoHtml.append( "<a href=\"" + videoUrlMp4 + "\" target=\"_blank\">" + CmsResourceBundle.getCmsBundle().getString( "quiz.diy.form.VIDEO_LINK" ) + "</a>" );
      videoHtml.append( "</div>" );
    }
    else
    {
      videoHtml.append( "<div id=\"PURLMainVideoWrapper\" class=\"PURLMainVideoWrapper\">" );
      videoHtml.append( "<video id='example_video_1" + globalUniqueId + "' class='video-js vjs-default-skin'  controls width='250' preload='auto' data-setup='{}'>" );
      videoHtml.append( "<source type='video/mp4' src='" + videoUrlMp4 + "'/>" );
      videoHtml.append( "<source type='video/webm' src='" + videoUrlWebm + "'/>" );
      videoHtml.append( "<source type='video/ogg' src='" + videoUrlOgg + "'/>" );
      videoHtml.append( "<source type='video/3gp' src='" + videoUrl3gp + "'/>" );
      videoHtml.append( "</video>" );
      videoHtml.append( "<script>var myPlayer" + globalUniqueId + " = _V_('example_video_1" + globalUniqueId + "');</script>" );
      videoHtml.append( "</div>" );
    }
    return videoHtml.toString();
  }

  @Override
  public List<Promotion> getAllBadges()
  {
    return promotionDAO.getAllBadges();
  }

  public List<Long> getEligiblePromotionsFromPromoBadgeId( Long promoBadgeId )
  {
    return promotionDAO.getEligiblePromotionsFromPromoBadgeId( promoBadgeId );
  }

  public boolean isPromotionNameUnique( String promotionName, Long currentPromotionId )
  {
    return promotionDAO.isPromotionNameUnique( promotionName, currentPromotionId );
  }

  public List<CelebrationImageFillerValue> getCelebrationImageFillersForPromotion( Long promotionId )
  {
    return promotionDAO.getCelebrationImageFillersForPromotion( promotionId );
  }

  // returns promotion name for a specific locale
  public String getPromotionNameByLocale( String promoNameAssetCode, String userLocale )
  {
    Locale locale = CmsUtil.getLocale( userLocale );
    return cmAssetService.getString( promoNameAssetCode, Promotion.PROMOTION_NAME_KEY_PREFIX, locale, true );
  }

  @Override
  public Promotion updatePromotionBudgetSweepAndSavePromotion( Set<PromotionBudgetSweep> promotionBudgetSweeps, Long promotionId, List updateAssociations ) throws UniqueConstraintViolationException
  {
    for ( PromotionBudgetSweep promotionBudgetSweep : promotionBudgetSweeps )
    {
      budgetMasterDAO.updatePromotionBudgetSweep( promotionBudgetSweep );
    }
    return savePromotion( promotionId, updateAssociations );
  }

  @Override
  public EngagementPromotion getLiveEngagementPromotion()
  {
    return promotionDAO.getLiveEngagementPromotion();
  }

  @Override
  public boolean displayPurlCelebrationTile()
  {
    boolean displayTile = false;

    List purlRecipientList = promotionDAO.getPurlRecipinentListOnPurlTile();
    if ( purlRecipientList != null && purlRecipientList.size() > 0 )
    {
      displayTile = true;
    }

    return displayTile;
  }

  public boolean isRecogPromotionInRPM( Long promotionId )
  {
    return promotionDAO.isRecogPromotionInRPM( promotionId );
  }

  public void setDivisionDAO( DivisionDAO divisionDAO )
  {
    this.divisionDAO = divisionDAO;
  }

  public void setEngagementDAO( EngagementDAO engagementDAO )
  {
    this.engagementDAO = engagementDAO;
  }

  public Integer getBadgePromotionCountForPromoId( Long promoId )
  {
    return promotionDAO.getBadgePromotionCountForPromoId( promoId );
  }

  public Integer getPromotionSelfRecognition( Long promoId )
  {
    return promotionDAO.getPromotionSelfRecognition( promoId );
  }

  @Override
  public List<PromotionsValueBean> getAllSortedApproverPromotions( Long userId, String promotionType )
  {
    return promotionDAO.getAllSortedApproverPromotions( userId, promotionType );
  }

  public List<MerchOrder> getMerchOrdersToGenerateGiftCodeByPromotionIdAndUserId( Long promotionId, Long userId )
  {
    return promotionDAO.getMerchOrdersToGenerateGiftCodeByPromotionIdAndUserId( promotionId, userId );
  }

  public boolean getMerchOrderByPromotionIdAndUserId( Long promotionId, Long userId )
  {
    return promotionDAO.getMerchOrderByPromotionIdAndUserId( promotionId, userId );
  }

  @Override
  public List getPromotionBillCodes( Long promotionId, boolean sweetStakesBillCode )
  {
    return promotionDAO.getPromotionBillCodes( promotionId, sweetStakesBillCode );
  }

  @Override
  public List getApprovalOptionsByApproverId( Long approverId )
  {
    return promotionDAO.getApprovalOptionsByApproverId( approverId );
  }

  @Override
  public CustomFormStepElementsView getStepElements( Long pomotionId )
  {
    CustomFormStepElementsView form = new CustomFormStepElementsView();
    CustomFormFieldView field = null;
    CustomFormSelectListView selectList;

    Promotion promotion = getPromotionById( pomotionId );

    if ( !promotion.isClaimFormUsed() || promotion.getClaimForm() == null )
    {
      return form;
    }

    ClaimForm claimForm = promotion.getClaimForm();
    if ( claimForm == null || !claimForm.hasCustomFormElements() )
    {
      return form;
    }

    form.setFormId( claimForm.getId() );
    List<ClaimFormStep> claimFormSteps = claimForm.getClaimFormSteps();

    Long customWhyId = null;
    Integer customWhyCharCount = null;
    for ( ClaimFormStep step : claimFormSteps )
    {

      String cmAssetCode = step.getClaimForm().getCmAssetCode();
      List<ClaimFormStepElement> elements = step.getClaimFormStepElements();
      for ( ClaimFormStepElement stepElement : elements )
      {
        field = new CustomFormFieldView();
        field.setId( stepElement.getId() );
        field.setSequenceNumber( stepElement.getSequenceNumber() );
        field.setLabel( stepElement.getI18nLabel() );
        field.setCopyValue( stepElement.getI18nCopy() );
        field.setName( stepElement.getI18nHeading() );
        field.setIsRequired( stepElement.isRequired() );
        field.setMaxDecimal( stepElement.getNumberOfDecimals() != null ? stepElement.getNumberOfDecimals().longValue() : null );
        field.setMask( stepElement.isMaskedOnEntry() );
        if ( customWhyId == null && stepElement.isWhyField() )
        {
          customWhyId = stepElement.getId();
          customWhyCharCount = stepElement.getMaxSize();
        }

        if ( stepElement.isWhyField() && promotion.isNominationPromotion() )
        {
          field.setIsRequired( true );
        }
        field.setCustomWhyCharCount( stepElement.getMaxSize() );
        String trueLabel = ContentReaderManager.getText( cmAssetCode, stepElement.getCmKeyForLabelTrue() );
        String falseLabel = ContentReaderManager.getText( cmAssetCode, stepElement.getCmKeyForLabelFalse() );

        field.setTrueLabel( trueLabel );
        field.setFalseLabel( falseLabel );
        field.setSize( stepElement.getMaxSize() );

        ClaimFormElementType type = stepElement.getClaimFormElementType();

        if ( type.isLink() )
        {
          field.setLinkUrl( stepElement.getLinkURL() );
          field.setLinkName( ContentReaderManager.getText( cmAssetCode, stepElement.getCmKeyForLinkName() ) );
        }

        if ( type.isAddressBlock() )
        {
          populateAddressGroup( form, stepElement );
        }
        else if ( type.isSelectField() || type.isMultiSelectField() )
        {
          String selectionPickListName = stepElement.getSelectionPickListName();
          List<PickListItem> items = DynaPickListType.getList( selectionPickListName );
          field.setSelectList( getSelectList( items, null ) );
          field.setType( type.getCode() );
          form.getFields().add( field );
        }
        else
        {
          field.setType( type.getCode() );
          form.getFields().add( field );
        }
      }
    }
    form.setCustomWhyCharCount( customWhyCharCount );
    form.setCustomWhyId( customWhyId );
    return form;
  }

  @Override
  public void populateAddressGroup( CustomFormStepElementsView form, ClaimFormStepElement stepElement )
  {
    CustomFormElementAddress[] values = CustomFormElementAddress.values();

    CustomFormFieldView field;

    for ( CustomFormElementAddress address : values )
    {
      field = new CustomFormFieldView();
      field.setId( stepElement.getId() );
      field.setSequenceNumber( stepElement.getSequenceNumber() );

      if ( address.isSecHead() )
      {
        field.setLabel( stepElement.getI18nLabel() );
      }
      else
      {
        field.setLabel( cmAssetService.getTextFromCmsResourceBundle( address.getCmAssetCode() ) );
      }

      field.setName( address.getSubType() );
      field.setType( address.getType() );
      field.setSubType( address.getSubType() );
      field.setFieldGroup( CustomFormElementAddress.FIELD_GROUP );
      field.setIsRequired( address.isRequired() );

      if ( address.isSelectField() )
      {

        if ( address.isCountryField() )
        {
          List<Country> countryList = countryService.getAllActive();
          field.setSelectList( getCountrySelectList( countryList ) );
        }
        else if ( address.isState() )
        {
          List<Country> countryList = countryService.getAllActive();

          for ( Country country : countryList )
          {
            String countryCode = country.getCountryCode();

            List<PickListItem> stateList = StateType.getList( countryCode );
            field.getSelectList().addAll( getSelectList( stateList, countryCode ) );
            // field.setSelectList( getSelectList( stateList, countryCode ) );
          }
        }
      }

      form.getFields().add( field );
    }
  }

  private List<CustomFormSelectListView> getCountrySelectList( List<Country> countryList )
  {
    List<CustomFormSelectListView> list = new ArrayList<CustomFormSelectListView>();
    CustomFormSelectListView select;

    for ( Country country : countryList )
    {
      select = new CustomFormSelectListView();
      select.setName( country.getI18nCountryName() );
      select.setId( country.getCountryCode() );
      select.setCountryCode( country.getCountryCode() );
      list.add( select );
    }
    return list;
  }

  @Override
  public List<CustomFormSelectListView> getSelectList( List<PickListItem> stateList, String countryCode )
  {
    List<CustomFormSelectListView> list = new ArrayList<CustomFormSelectListView>();
    for ( PickListItem item : stateList )
    {
      CustomFormSelectListView select = new CustomFormSelectListView();
      select.setId( item.getCode() );
      select.setName( item.getName() );
      select.setCountryCode( countryCode );
      list.add( select );
    }
    return list;
  }

  @Override
  public List<Long> getPromotionIdsForBehavior( String behaviorType )
  {
    return promotionDAO.getPromotionIdsForBehavior( behaviorType );
  }

  public void setCountryService( CountryService countryService )
  {
    this.countryService = countryService;
  }

  @Override
  public Promotion saveLevelLabelCmText( NominationPromotion promotion ) throws ServiceErrorException, UniqueConstraintViolationException
  {
    return saveLevelLabelCmText( promotion, ContentReaderManager.getCurrentLocale() );
  }

  private Promotion saveLevelLabelCmText( NominationPromotion promotion, Locale locale ) throws ServiceErrorException, UniqueConstraintViolationException
  {
    for ( NominationPromotionLevel nominationPromotionLevel : promotion.getNominationLevels() )
    {
      String data = nominationPromotionLevel.getLevelLabel();
      if ( !StringUtils.isBlank( data ) )
      {
        try
        {
          String newPromoLevelLabelNameAssetName = null;
          if ( nominationPromotionLevel.getLevelLabelAssetCode() == null )
          {
            // Create Unique Asset
            newPromoLevelLabelNameAssetName = cmAssetService.getUniqueAssetCode( Promotion.PROMOTION_LEVEL_LABEL_NAME_ASSET_PREFIX );
            nominationPromotionLevel.setLevelLabelAssetCode( newPromoLevelLabelNameAssetName );
          }

          CMDataElement cmDataElement = new CMDataElement( "Nomination Promotion Level Label Name", Promotion.PROMOTION_LEVEL_LABEL_NAME_KEY_PREFIX, data, false, DataTypeEnum.HTML );
          List elements = new ArrayList();
          elements.add( cmDataElement );

          if ( newPromoLevelLabelNameAssetName == null )
          {
            newPromoLevelLabelNameAssetName = nominationPromotionLevel.getLevelLabelAssetCode();
          }

          cmAssetService.createOrUpdateAsset( Promotion.PROMOTION_LEVEL_LABEL_NAME_SECTION_CODE,
                                              Promotion.PROMOTION_LEVEL_LABEL_NAME_ASSET_TYPE_NAME,
                                              Promotion.PROMOTION_LEVEL_LABEL_NAME_KEY_DESC,
                                              newPromoLevelLabelNameAssetName,
                                              elements,
                                              locale,
                                              null );
        }
        catch( ServiceErrorException e )
        {
          List errors = new ArrayList();
          errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
          throw e;
        }
      }
    }

    return promotion;
  }

  @Override
  public Promotion savePayoutDescriptionCmText( NominationPromotion promotion ) throws ServiceErrorException, UniqueConstraintViolationException
  {
    return savePayoutDescriptionCmText( promotion, ContentReaderManager.getCurrentLocale() );
  }

  private Promotion savePayoutDescriptionCmText( NominationPromotion promotion, Locale locale ) throws ServiceErrorException, UniqueConstraintViolationException
  {
    for ( NominationPromotionLevel nominationPromotionLevel : promotion.getNominationLevels() )
    {
      String data = nominationPromotionLevel.getPayoutDescription();
      if ( !StringUtils.isBlank( data ) )
      {
        try
        {
          String newPayoutDescriptionAssetName = null;
          if ( nominationPromotionLevel.getPayoutDescriptionAssetCode() == null )
          {
            // Create Unique Asset
            newPayoutDescriptionAssetName = cmAssetService.getUniqueAssetCode( Promotion.PAYOUT_DESCRIPTION_ASSET_PREFIX );
            nominationPromotionLevel.setPayoutDescriptionAssetCode( newPayoutDescriptionAssetName );
          }

          CMDataElement cmDataElement = new CMDataElement( "Payout Description", Promotion.PAYOUT_DESCRIPTION_KEY_PREFIX, data, false, DataTypeEnum.HTML );
          List elements = new ArrayList();
          elements.add( cmDataElement );

          if ( newPayoutDescriptionAssetName == null )
          {
            newPayoutDescriptionAssetName = nominationPromotionLevel.getPayoutDescriptionAssetCode();
          }

          cmAssetService.createOrUpdateAsset( Promotion.PAYOUT_DESCRIPTION_SECTION_CODE,
                                              Promotion.PAYOUT_DESCRIPTION_ASSET_TYPE_NAME,
                                              Promotion.PAYOUT_DESCRIPTION_KEY_DESC,
                                              newPayoutDescriptionAssetName,
                                              elements,
                                              locale,
                                              null );
        }
        catch( ServiceErrorException e )
        {
          List errors = new ArrayList();
          errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
          throw e;
        }
      }
    }

    return promotion;
  }

  @Override
  public Promotion saveBudgetSegmentNameCmText( Promotion promotion ) throws ServiceErrorException, UniqueConstraintViolationException
  {
    return saveLevelLabelCmText( promotion, ContentReaderManager.getCurrentLocale() );
  }

  private Promotion saveLevelLabelCmText( Promotion promotion, Locale locale ) throws ServiceErrorException, UniqueConstraintViolationException
  {
    for ( BudgetSegment budgetSegment : promotion.getBudgetMaster().getBudgetSegments() )
    {
      String data = budgetSegment.getName();
      if ( !StringUtils.isBlank( data ) )
      {
        try
        {
          String newbudgetSegmentNameAssetName = null;
          if ( budgetSegment.getCmAssetCode() == null )
          {
            // Create Unique Asset
            newbudgetSegmentNameAssetName = cmAssetService.getUniqueAssetCode( BudgetSegment.BUDGET_PERIOD_NAME_DATA_ASSET_PREFIX );
            budgetSegment.setCmAssetCode( newbudgetSegmentNameAssetName );
          }

          CMDataElement cmDataElement = new CMDataElement( "Budget Segment Name", BudgetSegment.BUDGET_PERIOD_NAME_KEY, data, false, DataTypeEnum.HTML );
          List elements = new ArrayList();
          elements.add( cmDataElement );

          if ( newbudgetSegmentNameAssetName == null )
          {
            newbudgetSegmentNameAssetName = budgetSegment.getCmAssetCode();
          }

          cmAssetService.createOrUpdateAsset( BudgetSegment.BUDGET_PERIOD_DATA_SECTION_CODE,
                                              BudgetSegment.BUDGET_PERIOD_ASSET_TYPE_NAME,
                                              BudgetSegment.BUDGET_PERIOD_NAME_SUFFIX,
                                              newbudgetSegmentNameAssetName,
                                              elements,
                                              locale,
                                              null );
        }
        catch( ServiceErrorException e )
        {
          List errors = new ArrayList();
          errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
          throw e;
        }
      }
    }

    return promotion;
  }

  @Override
  public Promotion saveTimePeriodNameCmText( NominationPromotion promotion ) throws ServiceErrorException, UniqueConstraintViolationException
  {
    return saveTimePeriodNameCmText( promotion, ContentReaderManager.getCurrentLocale() );
  }

  private Promotion saveTimePeriodNameCmText( NominationPromotion promotion, Locale locale ) throws ServiceErrorException, UniqueConstraintViolationException
  {
    // this flag is used to skip UniqueConstraint when this method is invoked from
    // PromotionTranslationAction.
    for ( NominationPromotionTimePeriod nominationPromotionTimePeriod : promotion.getNominationTimePeriods() )
    {
      String data = nominationPromotionTimePeriod.getTimePeriodName();
      if ( !StringUtils.isBlank( data ) )
      {
        try
        {
          String newPromoTimePeriodNameAssetName = null;
          if ( nominationPromotionTimePeriod.getTimePeriodNameAssetCode() == null )
          {
            // Create Unique Asset
            newPromoTimePeriodNameAssetName = cmAssetService.getUniqueAssetCode( Promotion.PROMOTION_TIME_PERIOD_NAME_ASSET_PREFIX );
            nominationPromotionTimePeriod.setTimePeriodNameAssetCode( newPromoTimePeriodNameAssetName );
          }

          CMDataElement cmDataElement = new CMDataElement( "Nomination Promotion Time Period Name", Promotion.PROMOTION_TIME_PERIOD_NAME_KEY_PREFIX, data, false, DataTypeEnum.HTML );
          List elements = new ArrayList();
          elements.add( cmDataElement );

          if ( newPromoTimePeriodNameAssetName == null )
          {
            newPromoTimePeriodNameAssetName = nominationPromotionTimePeriod.getTimePeriodNameAssetCode();
          }

          cmAssetService.createOrUpdateAsset( Promotion.PROMOTION_TIME_PERIOD_NAME_SECTION_CODE,
                                              Promotion.PROMOTION_TIME_PERIOD_NAME_ASSET_TYPE_NAME,
                                              Promotion.PROMOTION_TIME_PERIOD_NAME_KEY_DESC,
                                              newPromoTimePeriodNameAssetName,
                                              elements,
                                              locale,
                                              null );
        }
        catch( ServiceErrorException e )
        {
          List errors = new ArrayList();
          errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
          throw e;
        }
      }
    }

    return promotion;
  }

  @Override
  public List<NominationApproverValueBean> getCustomApproverList( int optionId )
  {
    List<NominationApproverValueBean> result = new ArrayList<NominationApproverValueBean>();
    NominationApproverValueBean approverVo;
    String approverValue = null;
    List<ApproverOption> approverOptions = promotionDAO.getApproverOptions( optionId );
    return getNominationApproverValueBean( approverOptions );

  }

  @Override
  public List<NominationApproverValueBean> getCustomApproverList( int levelId, Long promotionId )
  {
    return promotionDAO.getApproverOptions( levelId, promotionId );

  }

  @Override
  public Date getUAGoalQuestPromotionStartDate( Long userId )
  {
    return promotionDAO.getUAGoalQuestPromotionStartDate( userId );
  }

  private List<NominationApproverValueBean> getNominationApproverValueBean( List<ApproverOption> approverOptions )
  {

    List<NominationApproverValueBean> result = new ArrayList<NominationApproverValueBean>();
    String approverValue = "";
    NominationApproverValueBean approverVo;
    for ( ApproverOption option : approverOptions )
    {
      Set<ApproverCriteria> approverCriteria = option.getApproverCriteria();

      for ( ApproverCriteria criteria : approverCriteria )
      {
        Set<Approver> approvers = criteria.getApprovers();

        for ( Approver approver : approvers )
        {
          Participant pax = approver.getParticipant();
          CustomApproverType approverType = option.getApproverType();

          switch ( approverType.getCode() )
          {
            case CustomApproverType.AWARD:
              Integer minVal = criteria.getMinVal();
              Integer maxVal = criteria.getMaxVal();
              if ( isNumber( minVal + "" ) && isNumber( maxVal + "" ) && minVal.equals( maxVal ) )
              {
                approverValue = maxVal + "";
              }
              else
              {
                approverValue = minVal + "-" + maxVal;
              }
              break;
            case CustomApproverType.BEHAVIOR:
              approverValue = criteria.getApproverValue();
              break;
            case CustomApproverType.CHARACTERISTIC:
              // approverValue = getCharacteristicsValue( approver, criteria, option );
              approverValue = criteria.getApproverValue();
              break;
            default:
              break;
          }

          approverVo = new NominationApproverValueBean( pax.getUserName(), pax.getLastName(), pax.getFirstName(), approverType.getName(), approverValue );
          result.add( approverVo );
        }

      }

    }

    return result;
  }

  private String getCharacteristicsValue( Approver approver, ApproverCriteria criteria, ApproverOption option )
  {
    Long characteristicId = option.getCharacteristicId();
    String value = null;

    // Characteristic characteristics = characteristicDAO.getCharacteristicById( characteristicId );
    Characteristic characteristics = null;
    CharacteristicDataType type = characteristics.getCharacteristicDataType();
    String cmkey = characteristics.getCmAssetCode() + characteristics.getNameCmKey();
    BigDecimal minValue = characteristics.getMinValue();
    BigDecimal maxValue = characteristics.getMaxValue();

    switch ( type.getCode() )
    {
      case CharacteristicDataType.INTEGER:

        value = cmAssetService.getTextFromCmsResourceBundle( cmkey ) + minValue + " -" + maxValue;
        break;

      case CharacteristicDataType.TEXT:
        value = cmAssetService.getTextFromCmsResourceBundle( cmkey );
        break;

      case CharacteristicDataType.DATE:
        value = cmAssetService.getTextFromCmsResourceBundle( cmkey ) + characteristics.getDateStart() + " -" + characteristics.getDateEnd();
        break;

      case CharacteristicDataType.SINGLE_SELECT:
        value = cmAssetService.getTextFromCmsResourceBundle( cmkey ) + "--" + getPickListValues( characteristics.getPlName() );

        break;

      case CharacteristicDataType.MULTI_SELECT:
        value = cmAssetService.getTextFromCmsResourceBundle( cmkey ) + "--" + getPickListValues( characteristics.getPlName() );
        break;

      default:
        break;
    }

    return value;
  }

  private String getPickListValues( String plName )
  {
    StringBuilder strBuilder = new StringBuilder();
    List<PickListItem> list = DynaPickListType.getList( plName );

    for ( PickListItem item : list )
    {
      strBuilder.append( item.getName() ).append( "-" );
    }

    return strBuilder.toString();
  }

  @Override
  public List<NameableBean> getNominationPromotionListForApproverFileLoad()
  {
    return promotionDAO.getNominationPromotionListForApproverFileLoad();

  }

  @Override
  public List<NominationAdminApprovalsBean> getNominationApprovalClaimPromotions()
  {
    return promotionDAO.getNominationApprovalClaimPromotions();
  }

  @Override
  public String getLastWizardStepName( Long promotionId )
  {
    return promotionDAO.getLastWizardStepName( promotionId );
  }

  private PromotionService getPromotionsService()
  {
    return (PromotionService)BeanLocator.getBean( PromotionService.BEAN_NAME );
  }

  @Override
  public boolean isSSILivePromotionAvailable()
  {
    return promotionDAO.isSSILivePromotionAvailable();
  }

  @Override
  public boolean checkIfAnyPointsContestsByPaxId( Long userId )
  {
    return promotionDAO.checkIfAnyPointsContestsByPaxId( userId );

  }

  private NominationClaimService getNominationClaimService()
  {
    return (NominationClaimService)BeanLocator.getBean( NominationClaimService.BEAN_NAME );
  }

  public ImportFileDAO getImportFileDAO()
  {
    return importFileDAO;
  }

  public void setImportFileDAO( ImportFileDAO importFileDAO )
  {
    this.importFileDAO = importFileDAO;
  }

  public BigDecimal getTotalUnapprovedAwardQuantity( Long promotionId, Long userId, Long nodeId, Long budgetMasterId )
  {
    return promotionDAO.getTotalUnapprovedAwardQuantity( promotionId, userId, nodeId, budgetMasterId );
  }

  public BigDecimal getTotalUnapprovedAwardQuantityPurl( Long promotionId, Long userId, Long nodeId, Long budgetMasterId )
  {
    return promotionDAO.getTotalUnapprovedAwardQuantityPurl( promotionId, userId, nodeId, budgetMasterId );
  }

  public long getClaimAwardQuantity( Long claimId )
  {
    return promotionDAO.getClaimAwardQuantity( claimId );
  }

  public long getTotalImportPaxAwardQuantity( Long importFileId )
  {
    return promotionDAO.getTotalImportPaxAwardQuantity( importFileId );
  }

  public List<String> getAllUniqueBillCodes( Long contestId )
  {
    return promotionDAO.getAllUniqueBillCodes( contestId );
  }

  @Override
  public List<RecognitionAdvisorPromotionValueBean> getPromotionListForRA( Long userId, Long receiverId )
  {
    return promotionDAO.getPromotionListForRA( userId, receiverId );
  }

  public String getRequestId( String videoUrl )
  {

    return videoUrl.substring( videoUrl.lastIndexOf( ":" ) + 1 );
  }

  public String getActualCardUrl( String path )
  {
    return path.substring( 0, path.lastIndexOf( ActionConstants.REQUEST_ID ) );
  }

  public MTCService getMtcService()
  {
    return mtcService;
  }

  public void setMtcService( MTCService mtcService )
  {
    this.mtcService = mtcService;
  }

  public int deletePromoCard( Long cardId, Long promotionId )
  {
    return promotionDAO.deletePromoCard( cardId, promotionId );
  }

  @Override
  public List<PromoRecImageData> getNotMigratedPromRecogAvatarData()
  {
    return getPromotionDAO().getNotMigratedPromRecogAvatarData();
  }

  @Override
  public void updatePromRecAvatar( Long promotionId, String defaultCelebrationAvatar, String defaultCcontributorAavatar )
  {
    getPromotionDAO().updatePromRecAvatar( promotionId, defaultCelebrationAvatar, defaultCcontributorAavatar );
  }

  @Override
  public List<String> getNonPurlAndCelebPromotionsName()
  {
    return getPromotionDAO().getNonPurlAndCelebPromotionsName();
  }

  public List<PromotionPaxValue> getAllLiveEngagementByUserId( Long userId, List<PromotionMenuBean> eligiblePromotions )
  {

    EngagementPromotionData engagementPromotionData = engagementDAO.getLiveEngagementPromotionData();
    return buildEngagementPromotionPaxValueList( userId, eligiblePromotions, engagementPromotionData );
  }

  private List<PromotionPaxValue> buildEngagementPromotionPaxValueList( Long userId, List<PromotionMenuBean> eligiblePromotions, EngagementPromotionData engagementPromotionData )
  {

    List promoPaxValueList = new ArrayList();
    if ( engagementPromotionData != null )
    {
      Set<Long> engagementEligiblePromotions = engagementDAO.getAllEligiblePromotionIds( engagementPromotionData.getPromotionId() );
      if ( engagementEligiblePromotions != null )
      {

        boolean isInAudience = false;

        if ( CollectionUtils.isNotEmpty( eligiblePromotions ) )
        {
          for ( PromotionMenuBean promotionMenuBean : eligiblePromotions )
          {

            if ( engagementEligiblePromotions.contains( promotionMenuBean.getPromotion().getId() ) )
            {
              if ( promotionMenuBean.getPromotion().isAbstractRecognitionPromotion() && ( promotionMenuBean.isCanSubmit() || promotionMenuBean.isCanReceive() ) )
              {
                isInAudience = true;
                break;
              }
            }
          }
        }
        if ( isInAudience )
        {
          PromotionPaxValue promoPaxValue = new PromotionPaxValue();

          promoPaxValue.setModuleCode( PromotionType.ENGAGEMENT );

          promoPaxValue.setRoleKey( RECOGNITION_PRIMARY );
          promoPaxValueList.add( promoPaxValue );
        }
      }
    }

    return promoPaxValueList;
  }

  @Override
  public void updatePromRecCeleAvatar( Long promotionId, String defaultCelebrationAvatar )
  {
    getPromotionDAO().updatePromRecCeleAvatar( promotionId, defaultCelebrationAvatar );
  }

  @Override
  public void updatePromRecContrAvatar( Long promotionId, String defaultContributorAavatar )
  {
    getPromotionDAO().updatePromRecContrAvatar( promotionId, defaultContributorAavatar );
  }

  @Override
  public List<PromoRecPictureData> getNotMigratedPromRecogPictureData()
  {
    return getPromotionDAO().getNotMigratedPromRecogPictureData();
  }

  
  private List<AlertsValueBean> buildAlertValueBeans( List<ClaimRecipientValueBean> claimRecipients )
  {
    List<AlertsValueBean> valueBeans = new ArrayList<AlertsValueBean>();
    for ( ClaimRecipientValueBean cr : claimRecipients )
    {
      AlertsValueBean valueBean = new AlertsValueBean();
      valueBean.setActivityType( ActivityType.PENDING_CLAIM_AWARD );
      valueBean.setClaimItemId( cr.getClaimItemId() );
      valueBean.setPromotionName( cr.getPromotionName() );
      valueBean.setSubmissionDate( cr.getSubmissionDate() );
      valueBean.setClaimId( cr.getClaimId() );
      valueBean.setApprovedOn( cr.getApprovedOn() );
      valueBeans.add( valueBean );
    }
    return valueBeans;
  }
  
  // Client customization for WIP 58122
  private List<AlertsValueBean> buildAlertValueBeansForNomination( List<ClaimRecipientValueBean> claimRecipients )
  {
    List<AlertsValueBean> valueBeans = new ArrayList<AlertsValueBean>();
    for ( ClaimRecipientValueBean cr : claimRecipients )
    {
      AlertsValueBean valueBean = new AlertsValueBean();
      valueBean.setActivityType( ActivityType.PENDING_CLAIM_AWARD_NOMINATION );
      valueBean.setClaimItemId( cr.getClaimItemId() );
      valueBean.setPromotionName( cr.getPromotionName() );
      valueBean.setSubmissionDate( cr.getSubmissionDate() );
      valueBean.setClaimId( cr.getClaimId() );
      valueBean.setApprovedOn( cr.getApprovedOn() );
      valueBeans.add( valueBean );
    }
    return valueBeans;
  }
  // Client customization for WIP 58122 ends
  @Override
  public void updateContResPic( Long promotionId )
  {
    getPromotionDAO().updateContResPic( promotionId );
  }
  
  public void setCokeClientDAO( CokeClientDAO cokeClientDAO )
  {
    this.cokeClientDAO = cokeClientDAO;
  }
  
  // Client customization for WIP #39189 starts
  public Long getPromotionIdByClaimId( Long claimId )
  {
    return promotionDAO.getPromotionIdByClaimId( claimId );
  }
  // Client customization for WIP #39189 ends
  
  // Client customizations for WIP #62128 starts
  public Long getCheersPromotionId()
  {
    Long promotionId;
    try
    {
      promotionId = promotionDAO.getCheersPromotionId();
    }
    catch( EmptyResultDataAccessException e )
    {
      promotionId = new Long( 0 );
    }
    return promotionId;
  }
  // Client customizations for WIP #62128 ends

}
