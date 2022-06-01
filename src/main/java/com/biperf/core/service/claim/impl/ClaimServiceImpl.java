/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/claim/impl/ClaimServiceImpl.java,v $
 */

package com.biperf.core.service.claim.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.sanselan.ImageReadException;
import org.hibernate.Hibernate;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.biperf.awardslinqDataRetriever.client.MerchLevel;
import com.biperf.core.dao.activity.ActivityDAO;
import com.biperf.core.dao.budget.BudgetMasterDAO;
import com.biperf.core.dao.claim.ClaimDAO;
import com.biperf.core.dao.claim.ClaimGroupDAO;
import com.biperf.core.dao.claim.hibernate.ApproverSeekingClaimQueryConstraint;
import com.biperf.core.dao.claim.hibernate.ApproverSeekingNominationClaimQueryConstraint;
import com.biperf.core.dao.claim.hibernate.ClaimQueryConstraint;
import com.biperf.core.dao.claim.hibernate.JournalClaimQueryConstraint;
import com.biperf.core.dao.claim.hibernate.ProductClaimClaimQueryConstraint;
import com.biperf.core.dao.claim.hibernate.QuizClaimQueryConstraint;
import com.biperf.core.dao.client.CokeClientDAO;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.journal.JournalDAO;
import com.biperf.core.dao.product.ProductCharacteristicDAO;
import com.biperf.core.dao.purl.PurlContributorDAO;
import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.activity.NominationActivity;
import com.biperf.core.domain.activity.RecognitionActivity;
import com.biperf.core.domain.audit.ClaimBasedPayoutCalculationAudit;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.calculator.CalculatorCriterion;
import com.biperf.core.domain.calculator.CalculatorCriterionRating;
import com.biperf.core.domain.calculator.CalculatorPayout;
import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.ApprovableItem;
import com.biperf.core.domain.claim.CalculatorResponse;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimApproverSnapshot;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.claim.ClaimGroup;
import com.biperf.core.domain.claim.ClaimProduct;
import com.biperf.core.domain.claim.ClaimProductCharacteristic;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.HierarchyUniqueConstraintEnum;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.claim.ProductClaimParticipant;
import com.biperf.core.domain.claim.QuizClaim;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.claim.RecognitionClaimSource;
import com.biperf.core.domain.client.TcccClaimFile;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.ApprovalConditionalAmmountOperatorType;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.ApprovalType;
import com.biperf.core.domain.enums.ApproverType;
import com.biperf.core.domain.enums.BudgetActionType;
import com.biperf.core.domain.enums.BudgetStatusType;
import com.biperf.core.domain.enums.BudgetType;
import com.biperf.core.domain.enums.ClaimFormElementType;
import com.biperf.core.domain.enums.JournalStatusType;
import com.biperf.core.domain.enums.JournalTransactionType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.NominationAwardGroupSizeType;
import com.biperf.core.domain.enums.NominationAwardGroupType;
import com.biperf.core.domain.enums.NominationAwardSpecifierType;
import com.biperf.core.domain.enums.NominationEvaluationType;
import com.biperf.core.domain.enums.ProcessFrequencyType;
import com.biperf.core.domain.enums.PromoNominationBehaviorType;
import com.biperf.core.domain.enums.PromoRecognitionBehaviorType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionBehaviorType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.PurlContributorCommentStatus;
import com.biperf.core.domain.enums.PurlContributorMediaStatus;
import com.biperf.core.domain.enums.PurlContributorState;
import com.biperf.core.domain.enums.PurlContributorVideoType;
import com.biperf.core.domain.enums.PurlMediaState;
import com.biperf.core.domain.enums.PurlRecipientState;
import com.biperf.core.domain.enums.SweepstakesClaimEligibilityType;
import com.biperf.core.domain.gamification.BadgeDetails;
import com.biperf.core.domain.gamification.ParticipantBadge;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingMessageLocale;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessSchedule;
import com.biperf.core.domain.product.ProductCharacteristicType;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.CelebrationManagerMessage;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.NominationPromotionLevel;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionParticipantSubmitter;
import com.biperf.core.domain.promotion.PromotionSweepstake;
import com.biperf.core.domain.promotion.PublicRecognitionUserConnections;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.promotion.ScheduledRecognition;
import com.biperf.core.domain.purl.PurlContributorComment;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.domain.purl.PurlRecipientCustomElement;
import com.biperf.core.domain.quiz.Quiz;
import com.biperf.core.domain.quiz.QuizLearningDetails;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.BudgetUsageOverAllocallatedException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.process.PointsDepositProcess;
import com.biperf.core.process.PurlSubmissionProcess;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.service.TranslatedContent;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.budget.BudgetMasterToBudgetSegmentsAssociationRequest;
import com.biperf.core.service.calculator.CalculatorService;
import com.biperf.core.service.celebration.CelebrationService;
import com.biperf.core.service.claim.CalculatorResponseBean;
import com.biperf.core.service.claim.ClaimApproverSnapshotService;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.claim.ClaimProcessingStrategy;
import com.biperf.core.service.claim.ClaimProcessingStrategyFactory;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.claim.PurlContributor;
import com.biperf.core.service.claim.RecognitionClaimRecipient;
import com.biperf.core.service.claim.RecognitionClaimSubmission;
import com.biperf.core.service.claim.RecognitionClaimSubmissionResponse;
import com.biperf.core.service.client.CokeClientService;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.email.EmailHeader;
import com.biperf.core.service.email.EmailNotificationService;
import com.biperf.core.service.email.EmailService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.email.TextEmailBody;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.mtc.MTCService;
import com.biperf.core.service.multimedia.MultimediaService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.scheduledrecognition.ScheduledRecognitionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.translation.TranslationService;
import com.biperf.core.service.translation.UnexpectedTranslationException;
import com.biperf.core.service.translation.UnsupportedTranslationException;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.strategy.FileUploadStrategy;
import com.biperf.core.strategy.RandomNumberStrategy;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.BudgetUtils;
import com.biperf.core.utils.CharacteristicValidationUtils;
import com.biperf.core.utils.ClaimApproveUtils;
import com.biperf.core.utils.ClaimElementValidator;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.ProxyUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.TcccClientUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.ValidationUtil;
import com.biperf.core.value.ClaimApproversValue;
import com.biperf.core.value.ClaimInfoBean;
import com.biperf.core.value.DepositProcessBean;
import com.biperf.core.value.ParticipantDIYQuizClaimHistory;
import com.biperf.core.value.ParticipantQuizClaimHistory;
import com.biperf.core.value.PersonalInfoFileUploadValue;
import com.biperf.core.value.ProductClaimPromotionsValueBean;
import com.biperf.core.value.ProductClaimStatusCountsBean;
import com.biperf.core.value.PromotionApprovableValue;
import com.biperf.core.value.client.TcccClaimFileValueBean;
import com.biperf.core.value.mtc.v1.upload.UploadResponse;
import com.biperf.core.value.nomination.TranslateCommentViewBean;
import com.biperf.core.value.nomination.TranslationFieldsViewBean;
import com.biperf.core.value.nomination.TranslationViewBean;
import com.biperf.core.value.promotion.CustomFormFieldView;
import com.biperf.core.value.promotion.CustomFormStepElementsView;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * ClaimServiceImpl.
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
 * <td>Jun 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimServiceImpl implements ClaimService
{
  private static final Log logger = LogFactory.getLog( ClaimServiceImpl.class );

  private static final String FINAL_LEVEL = "finalLevel";
  private static final String EACH_LEVEL = "eachLevel";

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------
  private EmailNotificationService emailNotificationService;

  private ClaimProcessingStrategyFactory claimProcessingStrategyFactory;

  private ClaimDAO claimDAO;
  private ClaimGroupDAO claimGroupDAO;
  private NodeDAO nodeDAO;
  private ProductCharacteristicDAO productCharacteristicDAO;
  private BudgetMasterDAO budgetMasterDAO;
  private PromotionService promotionService;
  private RandomNumberStrategy randomNumberStrategy = null;
  private ClaimApproverSnapshotService claimApproverSnapshotService;
  private AwardBanQServiceFactory awardBanQServiceFactory;
  private ScheduledRecognitionService scheduledRecognitionService;
  private UserService userService;
  private JournalDAO journalDAO;
  private ParticipantService participantService;
  private MultimediaService multimediaService;
  private MerchLevelService merchLevelService;
  private CalculatorService calculatorService;
  private BudgetMasterService budgetMasterService;
  private GamificationService gamificationService;
  private ClaimElementValidator claimElementValidator;
  private ProcessService processService;
  private TranslationService translationService;
  private PurlContributorDAO purlContributorDAO;
  private NodeService nodeService;
  private CelebrationService celebrationService;
  private MailingService mailingService;
  private CountryService countryService;
  private SystemVariableService systemVariableService;
  private AudienceService audienceService;
  @Autowired
  private MTCService mtcService;

  private ClaimFormDefinitionService claimFormDefinitionService;
  // Client customization for WIP 58122
  private EmailService emailService = null;
  private MessageService messageService;
  // Client customizations for WIP #42701 starts
  private CokeClientDAO cokeClientDAO; 
  private ActivityDAO activityDAO;
  private UserCharacteristicService userCharacteristicService;
  public  final String DIVISION_KEY="Division Key";
  // Client customizations for WIP #42701 ends
  
  // Client Customization for WIP #39189 starts
  private FileUploadStrategy appDataDirFileUploadStrategy;
  private FileUploadStrategy webdavFileUploadStrategy;
  // Client Customization for WIP #39189 ends
  
  public void setEmailNotificationService( EmailNotificationService emailNotificationService )
  {
    this.emailNotificationService = emailNotificationService;
  }

  public void setClaimProcessingStrategyFactory( ClaimProcessingStrategyFactory claimProcessingStrategyFactory )
  {
    this.claimProcessingStrategyFactory = claimProcessingStrategyFactory;
  }

  public void setClaimDAO( ClaimDAO claimDAO )
  {
    this.claimDAO = claimDAO;
  }

  public void setProductCharacteristicDAO( ProductCharacteristicDAO productCharacteristicDAO )
  {
    this.productCharacteristicDAO = productCharacteristicDAO;
  }

  public void setBudgetMasterDAO( BudgetMasterDAO budgetMasterDAO )
  {
    this.budgetMasterDAO = budgetMasterDAO;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void excecuteOnReversal( String claimId, String promotionType )
  {
    claimDAO.excecuteOnReversal( claimId, promotionType );
  }

  /**
   * @param nodeDAO value for nodeDAO property
   */
  public void setNodeDAO( NodeDAO nodeDAO )
  {
    this.nodeDAO = nodeDAO;
  }

  /**
   * Sets this object's claim element validator.
   * 
   * @param claimElementValidator a claim element validator.
   */
  public void setClaimElementValidator( ClaimElementValidator claimElementValidator )
  {
    this.claimElementValidator = claimElementValidator;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimService#getClaimById(java.lang.Long)
   * @param claimId
   * @return List of Claims by id
   */
  public Claim getClaimById( Long claimId )
  {
    return this.claimDAO.getClaimById( claimId );
  }

  /**
   * {@inheritDoc}
   */
  public Long getOpenClaimByPromotionIdAndUserId( Long promotionId, Long userId )
  {
    return this.claimDAO.getOpenClaimByPromotionIdAndUserId( promotionId, userId );
  }

  public Long getOpenClaimByPromotionIdQuizIdAndUserId( Long promotionId, Long userId, Long quizId )
  {
    return this.claimDAO.getOpenClaimByPromotionIdQuizIdAndUserId( promotionId, userId, quizId );
  }

  public Long getPassedQuizClaimByPromotionIdAndUserId( Long promotionId, Long userId )
  {
    return this.claimDAO.getPassedQuizClaimByPromotionIdAndUserId( promotionId, userId );
  }

  public Long getPassedQuizClaimByPromotionIdQuizIdAndUserId( Long promotionId, Long userId, Long quizId )
  {
    return this.claimDAO.getPassedQuizClaimByPromotionIdQuizIdAndUserId( promotionId, userId, quizId );
  }

  /**
   * Returns a count of claims that meet the specified criteria. Any parameter can be left null so
   * that the query is not constrained by that parameter. Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimService#getClaimListCount(com.biperf.core.dao.claim.hibernate.ClaimQueryConstraint)
   * @param claimQueryConstraint
   * @return int the claim list count
   */
  public int getClaimListCount( ClaimQueryConstraint claimQueryConstraint )
  {
    return claimDAO.getClaimListCount( claimQueryConstraint );
  }

  /**
   * Returns a list of claims that meet the specified criteria. Any parameter can be left null so
   * that the query is not constrained by that parameter. Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimService#getClaimList(com.biperf.core.dao.claim.hibernate.ClaimQueryConstraint)
   * @param claimQueryConstraint
   * @return List the claim list
   */
  public List getClaimList( ClaimQueryConstraint claimQueryConstraint )
  {
    return claimDAO.getClaimList( claimQueryConstraint );
  }

  /**
   * Returns a list of claims that meet the specified criteria. Any parameter can be left null so
   * that the query is not constrained by that parameter.
   * 
   * @param claimQueryConstraint
   * @param associationRequestCollection
   * @return List
   */
  public List getClaimListWithAssociations( ClaimQueryConstraint claimQueryConstraint, AssociationRequestCollection associationRequestCollection )
  {
    List claimList = claimDAO.getClaimList( claimQueryConstraint );
    List hydratedClaimList = new ArrayList();

    for ( Iterator claimListIter = claimList.iterator(); claimListIter.hasNext(); )
    {
      Claim claim = (Claim)claimListIter.next();

      if ( associationRequestCollection != null )
      {
        associationRequestCollection.process( claim );
        hydratedClaimList.add( claim );
      }
    }

    // ******** Remove the purl & celebration promotions. **********
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      List hydratedClaimListExpt = new ArrayList();

      for ( Iterator claimExpt = hydratedClaimList.iterator(); claimExpt.hasNext(); )
      {
        Claim claim = (Claim)claimExpt.next();
        Promotion promotion = claim.getPromotion();

        if ( promotion instanceof RecognitionPromotion )
        {
          RecognitionPromotion recPromotion = (RecognitionPromotion)promotion;
          if ( recPromotion.isIncludePurl() || recPromotion.isIncludeCelebrations() )
          {
            continue;
          }
        }
        hydratedClaimListExpt.add( claim );
      }
      return hydratedClaimListExpt;
    }

    return hydratedClaimList;
  }

  /**
   * Returns the claims specified by the given query constraint.
   *
   * @param queryConstraint  the query constraint.
   * @param associationRequestCollection  initializes properties of the returned {@link Claim} objects.
   * @return the specified claims, as a <code>List</code> of {@link Claim} objects.
   */
  public List getClaimList( JournalClaimQueryConstraint queryConstraint, AssociationRequestCollection associationRequestCollection )
  {
    return claimDAO.getClaimList( queryConstraint, associationRequestCollection );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimService#getEarningsForClaim(java.lang.Long,
   *      java.lang.Long)
   * @param claimId
   * @return Long
   */
  public Long getEarningsForClaim( Long claimId, Long userId )
  {

    return claimDAO.getEarningsForClaim( claimId, userId );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimService#getEarningsForProductClaim(java.lang.Long,
   *      java.lang.Long, java.lang.Long)
   * @param claimId
   * @return Long
   */
  public Long getEarningsForProductClaim( Long claimId, Long userId, Long productId )
  {

    return claimDAO.getEarningsForProductClaim( claimId, userId, productId );
  }

  /**
   * Saves the specific claim and sends submitter and approver notifications (if applicable).
   * 
   * @param claim the claim to save.
   * @param claimFormStepId required if notifications are to be sent
   * @param approverUser
   * @param forceAutoApprove
   * @throws ServiceErrorException if claim is invalid.
   */
  public Claim saveClaim( Claim claim, Long claimFormStepId, User approverUser, boolean forceAutoApprove, boolean deductBudget ) throws ServiceErrorException
  {
    Approvable approvable = saveApprovable( claim, claimFormStepId, approverUser, forceAutoApprove, 0, deductBudget, null );
    return (Claim)approvable;
  }

  /**
   * Saves the specific claim and sends submitter and approver notifications (if applicable).
   * 
   * @param claim the claim to save.
   * @param claimFormStepId required if notifications are to be sent
   * @param approverUser
   * @param forceAutoApprove
   * @param remainingBudget 
   * @throws ServiceErrorException if claim is invalid.
   */
  private void saveClaim( Claim claim, Long claimFormStepId, User approverUser, boolean forceAutoApprove, int remainingBudget ) throws ServiceErrorException
  {
    saveApprovable( claim, claimFormStepId, approverUser, forceAutoApprove, remainingBudget, true, null );
  }

  // Fix for bug#56006,55519 start
  /**
   * Saves the specific claim - when a budget sweep process is run.
   * 
   * @param claim the claim to save.
   * @param claimFormStepId required if notifications are to be sent
   * @param approverUser
   * @param forceAutoApprove
   * @param deductBudget
   * @param budgetSegment
   * @throws ServiceErrorException if claim is invalid.
   */
  public Claim saveClaim( Claim claim, Long claimFormStepId, User approverUser, boolean forceAutoApprove, boolean deductBudget, BudgetSegment budgetSegment ) throws ServiceErrorException
  {
    Approvable approvable = saveApprovable( claim, claimFormStepId, approverUser, forceAutoApprove, 0, deductBudget, budgetSegment );
    return (Claim)approvable;
  }
  // Fix for bug#56006,55519 end

  /**
   * Saves the specific approvable and sends submitter and approver notifications (if applicable).
   * 
   * @param approvable the approvable to save.
   * @param claimFormStepId required if notifications are to be sent
   * @param approverUser
   * @param forceAutoApprove
   * @throws ServiceErrorException if claim is invalid.
   */
  public void saveApprovable( Approvable approvable, Long claimFormStepId, User approverUser, boolean forceAutoApprove ) throws ServiceErrorException
  {
    saveApprovable( approvable, claimFormStepId, approverUser, forceAutoApprove, 0, true, null );
  }

  /**
   * Saves the specific approvable and sends submitter and approver notifications (if applicable).
   * 
   * @param approvable the approvable to save.
   * @param claimFormStepId required if notifications are to be sent
   * @param approverUser
   * @param forceAutoApprove
   * @param remainingBudget 
   * @throws ServiceErrorException if claim is invalid.
   */
  private Approvable saveApprovable( Approvable approvable, Long claimFormStepId, User approverUser, boolean forceAutoApprove, int remainingBudget, boolean deductBudget, BudgetSegment budgetSegment )
      throws ServiceErrorException
  {
    boolean isNewApprovable = approvable.getId() == null;
    boolean isClaim = approvable instanceof Claim;
    boolean isClaimGroup = approvable instanceof ClaimGroup;

    boolean isMoreInfo = ClaimApproveUtils.isNominationApprovalRequestMoreInfo( approvable );

    // Initialize the approval round, if new
    if ( approvable.getApprovalRound() == null )
    {
      approvable.setApprovalRound( new Long( 1 ) );
    }

    // Initialize the approvable, if new
    if ( isNewApprovable )
    {
      initializeNewApprovable( approvable );
    }

    // per bug 17066, add second check, only validate if it is a new claim
    if ( isClaim && isNewApprovable )
    {
      List validationErrors = validateClaim( (Claim)approvable );
      if ( !validationErrors.isEmpty() )
      {
        throw new ServiceErrorExceptionWithRollback( validationErrors );
      }
    }

    boolean open = true;
    /**
     * approverSourceNodeId represents (for node based approver types) the node from which the 
     * approver was determined.
     */
    Long approverSourceNodeId = null;
    if ( isNewApprovable )
    {
      // Should all claim items be marked approved
      if ( forceAutoApprove || !keepInsertedClaimOpen( approvable ) && !isApprovalDeferred( approvable ) )
      {
        // set all claim items set to approved. None will have been denied on insert, so all will be
        // approved
        ClaimApproveUtils.markUndeniedApprovableItemsApproved( approvable, null );
      }

      // if this is an initial claim group approval, load snapshot of a contributing claim to get to
      // source node.
      if ( isClaimGroup && approverUser != null && ClaimApproveUtils.isSnapshotKept( approvable ) )
      {
        ClaimGroup claimGroup = (ClaimGroup)approvable;

        Claim firstClaim = (Claim)claimGroup.getClaims().iterator().next();
        approverSourceNodeId = getSnapshotSourceNode( firstClaim, approverUser );
      }

    }
    else
    {
      // Update
      if ( !approvable.isOpen() )
      {
        throw new BeaconRuntimeException( "update not yet supported on closed claims, would likely " + "cause double payout. need to resolve payout issue beofre this can be enabled." );
      }

      // if this is an approval, load snapshot to get to source node.
      if ( approverUser != null && ClaimApproveUtils.isSnapshotKept( approvable ) )
      {
        approverSourceNodeId = getSnapshotSourceNode( approvable, approverUser );
      }
    }

    if ( !isMoreInfo && ClaimApproveUtils.isApprovalRoundOver( approvable ) )
    {
      Node sourceNode = null;
      if ( approverSourceNodeId != null )
      {
        sourceNode = nodeDAO.getNodeById( approverSourceNodeId );
      }

      ClaimApproveUtils.markApprovalRoundComplete( approvable, approverUser, sourceNode );
      // Client customizations for WIP #42960 starts
      if ( ClaimApproveUtils.isClaimDenied( approvable ) )
      {
        open = false;
      }
      else if ( approvable.getPromotion().getAdihCashOption() )
      {
        open = isAnotherApproverAvailable( approvable );
      }
      else if ( approvable.getNodeLevelsRemaining() == 0 )
      {
        open = false;
      }
      // Client customizations for WIP #42960 ends
       
    }
 
   
    approvable.setOpen( open );

      
    // Perform DAO save/update
    if ( isClaim )
    {
      approvable = claimDAO.saveClaim( (Claim)approvable );
    }
    else if ( isClaimGroup )
    {
      ClaimGroup claimGroup = (ClaimGroup)approvable;
      claimGroupDAO.saveClaimGroup( claimGroup );
      // close and save the claim group's claims if new claimGroup
      if ( isNewApprovable )
      {
        for ( Iterator iter = claimGroup.getClaims().iterator(); iter.hasNext(); )
        {
          Claim claimGroupClaim = (Claim)iter.next();
          if ( approvable.getNodeLevelsRemaining() == 0 )
          {
            ClaimApproveUtils.markUndeniedApprovableItemsApproved( claimGroupClaim, approverUser );
            claimGroupClaim.setOpen( false );
          }
        }
      }
    }
    else
    {
      throw new BeaconRuntimeException( "unknown approvable type: " + approvable.getClass().getName() );
    }
//Coke customization
    NominationPromotion nominationPromotion =null;
    if ( approvable.getPromotion().isNominationPromotion() )
    {
      nominationPromotion = (NominationPromotion)approvable.getPromotion();
    }
    
    if ( approvable.isOpen() )
    {
      // Determine approver(s). This will send out any approver notifications, and may close the
      // claim if approver
      // determination reveals that the claim should be auto-approved.
      if ( approvable.getPromotion().getAdihCashOption() )
      {
        ClaimApproveUtils.markUndeniedApprovableItemsPending( approvable, approverUser );
        claimApproverSnapshotService.updateClaimApproverSnapshot( approvable );
      }
      else if (approvable.getPromotion().isNominationPromotion() && nominationPromotion.isLevelSelectionByApprover() )
      {
        ClaimApproveUtils.markUndeniedApprovableItemsPending( approvable, approverUser );
        claimApproverSnapshotService.updateClaimApproverSnapshot( approvable );
      }
      else
      {
        claimApproverSnapshotService.updateClaimApproverSnapshot( approvable, claimFormStepId, isNewApprovable );
      }
    }
    else if ( approvable.getPromotion().getAdihCashOption() || ( approvable.getPromotion().isNominationPromotion() && nominationPromotion.isLevelSelectionByApprover() ))
    {
      claimApproverSnapshotService.deleteClaimApproverSnapshot( approvable );
    }

    if ( approvable.getPromotion().isNominationPromotion() )
    {
      if ( approvable.isOpen() && isNewApprovable )
      {
        sendSubmittedClaimNotifications( approvable, claimFormStepId ); // submit Claim
                                                                        // Notifications
      }
    }
  //Coke customization ends  
    if ( !isMoreInfo )
    {
      // Determine approver(s). This will send out any approver notifications, and may close the
      // claim if approver
      // determination reveals that the claim should be auto-approved.
      claimApproverSnapshotService.updateClaimApproverSnapshot( approvable, claimFormStepId, isNewApprovable );
    }

    if ( approvable.getPromotion().isNominationPromotion() && !isMoreInfo )
    {
      if ( approvable instanceof Claim )
      {
        Claim claim = (Claim)approvable;
        // send submit nomination email to nominee and nominee manager only when comes from submit
        // nomination page
        if ( approvable.isOpen() && claim instanceof NominationClaim && ( (NominationClaim)claim ).isSubmitNomination() || approvable.isOpen() && approvable.getPromotion().isProductClaimPromotion() )
        {
          sendSubmittedClaimNotifications( approvable, claimFormStepId ); // submit Claim
                                                                          // Notifications
        }
      }
      else if ( approvable instanceof ClaimGroup )
      {
        ClaimGroup claimGroup = (ClaimGroup)approvable;
        // send submit nomination email to nominee and nominee manager only when comes from submit
        // nomination page
        if ( claimGroup.getClaims().iterator().hasNext() )
        {
          NominationClaim nominationClaim = (NominationClaim)claimGroup.getClaims().iterator().next();
          if ( approvable.isOpen() && nominationClaim.isSubmitNomination() )
          {
            sendSubmittedClaimNotifications( approvable, claimFormStepId ); // submit Claim
                                                                            // Notifications
          }
        }
      }
    }
    else if ( isNewApprovable && !approvable.getPromotion().isProductClaimPromotion()  && !approvable.getPromotion().getAdihCashOption())
    {
      sendSubmittedClaimNotifications( approvable, claimFormStepId ); // submit Claim Notifications
    }
    else if ( approvable.getPromotion().getAdihCashOption() )
    {
        // Claim can be here only if it is approved and no awards attached to release immediately
    	 Claim claim = (Claim)approvable;
    	 sendCustomRecognizedReceivedEmail( claim );        
      }
    if ( approvable.getPromotion().isNominationPromotion() && !isMoreInfo  )
    {
      processClaim( approvable, isClaimGroup, deductBudget, budgetSegment );
    }
    else if ( !approvable.isOpen() )
    {
      processClaim( approvable, isClaimGroup, deductBudget, budgetSegment );
      if ( !isClaimGroup )
      {
        processClosedClaimNotifications( (Claim)approvable );
      }

      if ( approvable.getPromotion().isProductClaimPromotion() && ClaimApproveUtils.isClaimApproved( approvable ) )
      {
        gamificationService.populateBadgePartcipant( (Claim)approvable );
      }
    }

    if ( remainingBudget < 0 )
    {
      ClaimApproveUtils.markUndeniedApprovableItemsPending( approvable, null );
    }
 
   if (null != approvable && null != approvable.getPromotion() 
		    && ( approvable.getPromotion().isNominationPromotion() && ClaimApproveUtils.isApprovalRoundOver (approvable)) 
    		&& null != approvable.getPromotion().getApproverType()
    		&& approvable.getPromotion().getApproverType().isActive()
			&& approvable.getPromotion().getApproverType().getCode().equals(ApproverType.SPECIFIC_APPROVERS)) {
    	 processClaim( approvable, isClaimGroup, deductBudget, budgetSegment );
	}
   

    return approvable;

  }
//Coke - customization start
 private boolean isAnotherApproverAvailable( Approvable approvable )
 {
   boolean available = false;
   if ( approvable.getApprovalRound().longValue() <= 2 )
   {
     available = true;
   }
   return available;
 }
 // Coke - customization end
 
 public void sendCustomRecognizedReceivedEmail( Claim claim )
 {
   RecognitionClaim recogClaim = (RecognitionClaim)claim;
   Mailing mailing = mailingService.buildRecognitionMailingCustom( recogClaim, recogClaim.getClaimRecipients().iterator().next().getRecipient() );
   mailingService.submitMailing( mailing, null );
 }
  private void processClaim( Approvable approvable, boolean isClaimGroup, boolean deductBudget, BudgetSegment budgetSegment ) throws ServiceErrorException
  {

    // Process the claim and its activities.
    ClaimProcessingStrategy claimProcessingStrategy = claimProcessingStrategyFactory.getClaimProcessingStrategy( approvable.getPromotion().getPromotionType() );

    // Fix for bug#56006,55519 start
    if ( budgetSegment == null )
    {
      claimProcessingStrategy.processApprovable( approvable, deductBudget, null );
    }
    else
    {
      claimProcessingStrategy.processApprovable( approvable, deductBudget, budgetSegment );
    }
    // Fix for bug#56006,55519 end

    if ( !isClaimGroup && ! ( (Claim)approvable instanceof NominationClaim ) )
    {
      processClosedClaimNotifications( (Claim)approvable );
    }

    if ( approvable.getPromotion().isProductClaimPromotion() && ClaimApproveUtils.isClaimApproved( approvable ) )
    {
      gamificationService.populateBadgePartcipant( (Claim)approvable );
    }
    // else noop - there are no closed notification for a claim grouprAll;
  }

  private Long getSnapshotSourceNode( Approvable approvable, User approverUser ) throws ServiceErrorExceptionWithRollback
  {
    Long approverSourceNodeId = null;

    ClaimApproverSnapshot snapshot = claimApproverSnapshotService.getSnapshot( approverUser.getId(), approvable.getId(), approvable.getApprovableType() );
    if ( snapshot != null )
    {
      approverSourceNodeId = snapshot.getSourceNodeId();
    }
    else
    {
      ServiceError serviceError = new ServiceError( ServiceErrorMessageKeys.APPROVAL_ATTEMPTED_APPROVER_IS_NOT_CURRENT_APPROVER, approvable.getId().toString() );
      throw new ServiceErrorExceptionWithRollback( serviceError );
    }
    return approverSourceNodeId;
  }

  private void sendSubmittedClaimNotifications( Approvable approvable, Long claimFormStepId )
  {
    Claim notificationClaim;
    if ( approvable instanceof Claim )
    {
      notificationClaim = (Claim)approvable;
    }
    else
    {
      ClaimGroup claimGroup = (ClaimGroup)approvable;
      // Just use first claim, since this notification doesn't use anything unique to each claim.
      notificationClaim = (Claim)claimGroup.getClaims().iterator().next();
    }
    if ( approvable.getPromotion().isProductClaimPromotion() )
    {
      ClaimApproversValue claimApproversValue = claimApproverSnapshotService.getApprovers( approvable );
      List actualClaimApproverUsers = new ArrayList( claimApproversValue.getApproverUsers() );
      emailNotificationService.processSubmittedClaimNotifications( notificationClaim, claimFormStepId, actualClaimApproverUsers, true );
    }
    else
    {
      emailNotificationService.processSubmittedClaimNotifications( notificationClaim, claimFormStepId, new ArrayList(), false );
    }
  }

  private void initializeNewApprovable( Approvable approvable )
  {
    if ( approvable instanceof Claim )
    {
      Claim claim = (Claim)approvable;

      if ( claim.getSubmissionDate() == null )
      {
        claim.setSubmissionDate( UserManager.getCurrentDateWithTimeZoneID() );
      }

      claim.setClaimNumber( getRandomNumberStrategy().getRandomizedClaimNumber() );
    }
  }

  /**
   * Return true if an inserted claim should be keep open for the claim type of the passed in claim.
   */
  private boolean keepInsertedClaimOpen( Approvable approvable )
  {
    boolean keepInsertedClaimOpen = false;

    if ( approvable instanceof QuizClaim )
    {
      keepInsertedClaimOpen = true;
    }

    return keepInsertedClaimOpen;
  }

  /**
   * Given an approvable that has not yet been persisted (see note 1), determine whether or not approval
   * should be done automatically or whether it shoudl be deferred (for manual approval or
   * time-delayed in the ApprovalType.AUTOMATIC_DELAYED case). <br/> Note 1: Since the
   * ApprovalType.CONDITIONAL_NTH_BASED is dependent on the current state of the system, calling
   * this method at some future time after submission would yield incorrect results.
   * 
   * @param approvable
   * @return false if the
   */
  public boolean isApprovalDeferred( Approvable approvable )
  {
    boolean approvalDeferred;

    String approvalTypeCode = approvable.getPromotion().getApprovalType().getCode();
    if ( approvalTypeCode.equals( ApprovalType.MANUAL ) )
    {
      approvalDeferred = true;
    }
    else if ( approvalTypeCode.equals( ApprovalType.AUTOMATIC_DELAYED ) )
    {
      approvalDeferred = true;
    }
    else if ( approvalTypeCode.equals( ApprovalType.AUTOMATIC_IMMEDIATE ) )
    {
      approvalDeferred = false;
    }
    else if ( approvalTypeCode.equals( ApprovalType.CONDITIONAL_AMOUNT_BASED ) )
    {
      approvalDeferred = isApprovalDeferredConditionalAmountBased( (Claim)approvable );
    }
    else if ( approvalTypeCode.equals( ApprovalType.CONDITIONAL_NTH_BASED ) )
    {
      int claimsSubmittedCount = claimDAO.getClaimSubmittedCount( approvable.getPromotion().getId() ) + 1;
      approvalDeferred = claimsSubmittedCount % approvable.getPromotion().getApprovalConditionalClaimCount().intValue() == 0;
    }
    else if ( approvalTypeCode.equals( ApprovalType.CONDITIONAL_PAX_BASED ) )
    {
      approvalDeferred = false;
      for ( Iterator iter = approvable.getPromotion().getPromotionParticipantSubmitters().iterator(); iter.hasNext(); )
      {
        PromotionParticipantSubmitter promotionParticipantSubmitter = (PromotionParticipantSubmitter)iter.next();
        if ( promotionParticipantSubmitter.getParticipant().equals( ( (Claim)approvable ).getSubmitter() ) )
        {
          // list of restricted pax (those who require approvals) contains this claim's submitter
          approvalDeferred = true;
          break;
        }
      }
    }    // Client customizations for WIP #42701 starts
    else if ( approvalTypeCode.equals( ApprovalType.COKE_CUSTOM ) )
    {
      approvalDeferred = true;
    }
    // Client customizations for WIP #42701 ends
    else
    {
      throw new BeaconRuntimeException( "Unknown approval type code: " + approvalTypeCode );
    }
    return approvalDeferred;
  }

  private boolean isApprovalDeferredConditionalAmountBased( Claim claim )
  {
    boolean approvalDeferred = false;

    ClaimFormStepElement approvalClaimFormStepElement = claim.getPromotion().getApprovalConditionalAmountField();

    boolean matchingClaimElementFound = false;
    ClaimElement matchingClaimElement = null;
    for ( Iterator iter = claim.getClaimElements().iterator(); iter.hasNext(); )
    {
      ClaimElement claimElement = (ClaimElement)iter.next();
      if ( claimElement.getClaimFormStepElement().equals( approvalClaimFormStepElement ) )
      {
        matchingClaimElementFound = true;
        matchingClaimElement = claimElement;
        break;
      }
    }

    // Condition for deferred approval can only possibly be met if claim actually contains a value
    // to match against,
    // otherwise, condition is not met, which means don't defer approval.
    if ( matchingClaimElementFound && matchingClaimElement != null && matchingClaimElement.getValue() != null )
    {

      Double comparisonAmount = claim.getPromotion().getApprovalConditionalAmount();
      // only numeric fields allowed here, so can safely do valueOf() IF VALUE IS NOT NULL OR EMPTY
      // STRING.
      if ( !StringUtils.isEmpty( matchingClaimElement.getValue() ) )
      {
        double claimValue = Double.valueOf( matchingClaimElement.getValue() ).doubleValue();
        double comparisonValue = comparisonAmount.doubleValue();

        String comparisonOperatorCode = claim.getPromotion().getApprovalConditionalAmountOperator().getCode();
        if ( comparisonOperatorCode.equals( ApprovalConditionalAmmountOperatorType.LT ) )
        {
          if ( claimValue < comparisonValue )
          {
            approvalDeferred = true;
          }
        }
        else if ( comparisonOperatorCode.equals( ApprovalConditionalAmmountOperatorType.LTEQ ) )
        {
          if ( claimValue <= comparisonValue )
          {
            approvalDeferred = true;
          }
        }
        else if ( comparisonOperatorCode.equals( ApprovalConditionalAmmountOperatorType.EQ ) )
        {
          if ( claimValue == comparisonValue )
          {
            approvalDeferred = true;
          }
        }
        else if ( comparisonOperatorCode.equals( ApprovalConditionalAmmountOperatorType.GTEQ ) )
        {
          if ( claimValue >= comparisonValue )
          {
            approvalDeferred = true;
          }
        }
        else if ( comparisonOperatorCode.equals( ApprovalConditionalAmmountOperatorType.GT ) )
        {
          if ( claimValue > comparisonValue )
          {
            approvalDeferred = true;
          }
        }
        else
        {
          throw new BeaconRuntimeException( "unknown ApprovalConditionalAmmountOperatorType code: " + comparisonOperatorCode );
        }
      }
    }

    return approvalDeferred;
  }

  /**
   * Deletes the specified claim.
   * 
   * @param claimId the ID of the claim to delete.
   */
  public void deleteClaim( Long claimId )
  {
    Claim claim = claimDAO.getClaimById( claimId );
    claimDAO.deleteClaim( claim );
  }

  /**
   * Deletes the specified claims.
   * 
   * @param claimIdList the IDs of the claims to delete, as a <code>List</code> of
   *          <code>Long</code> objects.
   */
  public void deleteClaims( List claimIdList )
  {
    Iterator iter = claimIdList.iterator();
    while ( iter.hasNext() )
    {
      Long claimId = (Long)iter.next();
      deleteClaim( claimId );
    }
  }

  /**
   * Update claims and process any closed claims.
   * 
   * @param claims
   * @param claimFormStepId
   * @param approverUser
   * @param forceAutoApprove
   * @throws ServiceErrorException
   */
  public void saveClaims( List claims, Long claimFormStepId, User approverUser, boolean forceAutoApprove ) throws ServiceErrorException
  {
    for ( Iterator iter = claims.iterator(); iter.hasNext(); )
    {
      Claim claim = (Claim)iter.next();
      saveClaim( claim, claimFormStepId, approverUser, false, true );
    }
  }

  /**
   * Update claims and process any closed claims.
   * 
   * @param claims
   * @param claimFormStepId
   * @param approverUser
   * @param forceAutoApprove
   * @param remainingBudget 
   * @throws ServiceErrorException
   */
  public void saveClaims( List claims, Long claimFormStepId, User approverUser, boolean forceAutoApprove, int remainingBudget ) throws ServiceErrorException
  {
    for ( Iterator iter = claims.iterator(); iter.hasNext(); )
    {
      Claim claim = (Claim)iter.next();
      saveClaim( claim, claimFormStepId, approverUser, false, remainingBudget );
    }
  }

  /**
   * Update approvables and process any closed approvables
   * @param approvables
   * @param claimFormStepId
   * @param approverUser
   * @param forceAutoApprove
   * @throws ServiceErrorException
   */
  public void saveApprovables( List approvables, Long claimFormStepId, User approverUser, boolean forceAutoApprove ) throws ServiceErrorException
  {
    for ( Iterator iter = approvables.iterator(); iter.hasNext(); )
    {
      Approvable approvable = (Approvable)iter.next();
      saveApprovable( approvable, claimFormStepId, approverUser, false );
    }
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimService#saveNominationApprovables(java.util.List,
   *      java.lang.Long, com.biperf.core.domain.user.User, boolean,
   *      com.biperf.core.domain.promotion.Promotion)
   * @param approvables
   * @param claimFormStepId
   * @param approverUser
   * @param forceAutoApprove
   * @param promotion
   * @throws ServiceErrorException
   */
  public void saveNominationApprovables( List<Approvable> approvables, Long claimFormStepId, User approverUser, boolean forceAutoApprove, Promotion promotion, String awardType )
      throws ServiceErrorException
  {
    if ( approvables.isEmpty() )
    {
      return;
    }

    // Force award quantity to zero if the recipient has opted out of points
    if ( awardType.equals( PromotionAwardsType.POINTS ) || awardType.equals( PromotionAwardsType.CASH ) )
    {
      for ( Iterator<Approvable> approvablesIterator = approvables.iterator(); approvablesIterator.hasNext(); )
      {
        Approvable approvable = approvablesIterator.next();
        if ( approvable instanceof NominationClaim )
        {
          NominationClaim nominationClaim = (NominationClaim)approvable;
          updateNominationClaimForOptOutAwards( nominationClaim, null, awardType );
        }
        else if ( approvable instanceof ClaimGroup )
        {
          ClaimGroup claimGroup = (ClaimGroup)approvable;
          for ( Object claimObject : claimGroup.getClaims() )
          {
            NominationClaim nominationClaim = (NominationClaim)claimObject;
            updateNominationClaimForOptOutAwards( nominationClaim, claimGroup, awardType );
          }
        }
      }
    }

    if ( awardType.equals( PromotionAwardsType.POINTS ) && promotion.isBudgetUsed() || awardType.equals( PromotionAwardsType.CASH ) && promotion.isCashBudgetUsed() )
    {
      for ( Iterator approvablesIterator = approvables.iterator(); approvablesIterator.hasNext(); )
      {
        Approvable approvable = (Approvable)approvablesIterator.next();
        if ( approvable instanceof NominationClaim )
        {
          NominationClaim nominationClaim = (NominationClaim)approvable;
          NominationPromotion nominationPromotion = (NominationPromotion)nominationClaim.getPromotion();
          Participant submitter = nominationClaim.getSubmitter();
          for ( NominationPromotionLevel nominationPromotionLevel : nominationPromotion.getNominationLevels() )
          {
            if ( nominationPromotionLevel.getLevelIndex() != null && nominationPromotionLevel.getLevelIndex().equals( approvable.getApprovalRound() )
                && !nominationPromotionLevel.getAwardPayoutType().getCode().equals( PromotionAwardsType.OTHER ) )
            {
              for ( Iterator recipientsIterator = nominationClaim.getClaimRecipients().iterator(); recipientsIterator.hasNext(); )
              {
                ClaimRecipient claimRecipient = (ClaimRecipient)recipientsIterator.next();
                if ( claimRecipient.getApprovalStatusType().getCode().equals( ApprovalStatusType.WINNER ) )
                {
                  BigDecimal awardAmount = new BigDecimal( 0 );
                  BigDecimal cashAwardAmount = new BigDecimal( 0 );
                  if ( claimRecipient.getAwardQuantity() != null && claimRecipient.getAwardQuantity() != 0 )
                  {
                    awardAmount = BigDecimal.valueOf( claimRecipient.getAwardQuantity() );
                  }
                  else if ( claimRecipient.getCashAwardQuantity() != null && claimRecipient.getCashAwardQuantity().compareTo( new BigDecimal( 0 ) ) != 0 )
                  {
                    cashAwardAmount = claimRecipient.getCashAwardQuantity();
                  }

                  // award amount changed, update setCustomCashAwardQuantitybudget.
                  Budget budget = promotionService.getNominationPromotionAvailableBudgetByAwardType( promotion, submitter, null, awardType );
                  BigDecimal newBudgetCurrentValue = new BigDecimal( 0 );
                  if ( budget != null )
                  {
                    if ( awardAmount.compareTo( new BigDecimal( 0 ) ) != 0 )
                    {
                      newBudgetCurrentValue = budget.getCurrentValue().subtract( awardAmount );
                    }
                    else if ( cashAwardAmount.compareTo( new BigDecimal( 0 ) ) != 0 )
                    {
                      newBudgetCurrentValue = budget.getCurrentValue().subtract( cashAwardAmount );
                    }
                    else
                    {
                      newBudgetCurrentValue = budget.getCurrentValue();
                    }
                  }
                  /* check this logic */
                  else
                  {
                    if ( awardAmount.compareTo( new BigDecimal( 0 ) ) != 0 )
                    {
                      newBudgetCurrentValue = newBudgetCurrentValue.subtract( awardAmount );
                    }
                    else if ( cashAwardAmount.compareTo( new BigDecimal( 0 ) ) != 0 )
                    {
                      newBudgetCurrentValue = newBudgetCurrentValue.subtract( cashAwardAmount );
                    }
                  }

                  if ( newBudgetCurrentValue.doubleValue() < 0 )
                  {
                    // hard cap budget is exceeded
                    throw new ServiceErrorExceptionWithRollback( new ServiceError( ServiceErrorMessageKeys.HARD_CAP_BUDGET_EXCEEDED_NOMINATION ) );
                  }

                  budget.setCurrentValue( newBudgetCurrentValue );
                  budget.setActionType( BudgetActionType.lookup( BudgetActionType.DEDUCT ) );
                  budget.setReferenceVariableForClaimId( nominationClaim.getId() );
                  budgetMasterDAO.saveBudgetSegment( budget.getBudgetSegment() );
                }
              }
              break;
            }
          }
        }
        else if ( approvable instanceof ClaimGroup )
        {
          ClaimGroup claimGroup = (ClaimGroup)approvable;
          for ( Object claimObject : claimGroup.getClaims() )
          {
            NominationClaim nominationClaim = (NominationClaim)claimObject;
            Participant submitter = nominationClaim.getSubmitter();

            NominationPromotion nominationPromotion = (NominationPromotion)nominationClaim.getPromotion();
            for ( NominationPromotionLevel nominationPromotionLevel : nominationPromotion.getNominationLevels() )
            {
              if ( nominationPromotionLevel.getLevelIndex() != null && nominationPromotionLevel.getLevelIndex().equals( approvable.getApprovalRound() )
                  && !nominationPromotionLevel.getAwardPayoutType().getCode().equals( PromotionAwardsType.OTHER ) )
              {
                for ( Iterator recipientsIterator = nominationClaim.getClaimRecipients().iterator(); recipientsIterator.hasNext(); )
                {
                  ClaimRecipient claimRecipient = (ClaimRecipient)recipientsIterator.next();
                  if ( claimRecipient.getApprovalStatusType().getCode().equals( ApprovalStatusType.WINNER ) )
                {
                  BigDecimal awardAmount = new BigDecimal( 0 );
                  BigDecimal cashAwardAmount = new BigDecimal( 0 );
                  if ( claimGroup.getAwardQuantity() != null && claimGroup.getAwardQuantity() != 0 )
                  {
                    awardAmount = BigDecimal.valueOf( claimGroup.getAwardQuantity() );
                  }
                  else if ( claimGroup.getCashAwardQuantity() != null && claimGroup.getCashAwardQuantity().compareTo( new BigDecimal( 0 ) ) != 0 )
                  {
                    cashAwardAmount = claimGroup.getCashAwardQuantity();
                  }

                  // award amount changed, update budget.
                  Budget budget = promotionService.getNominationPromotionAvailableBudgetByAwardType( promotion, submitter, null, awardType );
                  BigDecimal newBudgetCurrentValue = new BigDecimal( 0 );
                  if ( budget != null )
                  {
                    if ( awardAmount.compareTo( new BigDecimal( 0 ) ) != 0 )
                    {
                      newBudgetCurrentValue = budget.getCurrentValue().subtract( awardAmount );
                    }
                    else if ( cashAwardAmount.compareTo( new BigDecimal( 0 ) ) != 0 )
                    {
                      newBudgetCurrentValue = budget.getCurrentValue().subtract( cashAwardAmount );
                    }
                    else
                    {
                      newBudgetCurrentValue = budget.getCurrentValue();
                    }
                  }
                  else
                  {
                    if ( awardAmount.compareTo( new BigDecimal( 0 ) ) != 0 )
                    {
                      newBudgetCurrentValue = newBudgetCurrentValue.subtract( awardAmount );
                    }
                    else if ( cashAwardAmount.compareTo( new BigDecimal( 0 ) ) != 0 )
                    {
                      newBudgetCurrentValue = newBudgetCurrentValue.subtract( cashAwardAmount );
                    }
                  }
                  if ( newBudgetCurrentValue.doubleValue() < 0 )
                  {
                    // hard cap budget is exceeded
                    throw new ServiceErrorExceptionWithRollback( new ServiceError( ServiceErrorMessageKeys.HARD_CAP_BUDGET_EXCEEDED_NOMINATION ) );
                  }
                  if ( budget != null )
                  {
                    budget.setCurrentValue( newBudgetCurrentValue );
                    budget.setActionType( BudgetActionType.lookup( BudgetActionType.DEDUCT ) );
                    budget.setReferenceVariableForClaimId( nominationClaim.getId() );
                    budgetMasterDAO.saveBudgetSegment( budget.getBudgetSegment() );
                  }
                  break;
                }
              }

            }
            }
            break; // Cumulative - stop after first claim. (Deduct only once per claimGroup.)
          }
        }
      }
    }

    saveApprovables( approvables, claimFormStepId, approverUser, forceAutoApprove );
  }

  /**
   * Set award quantity to 0 if recipient is opted out of awards
   * @param claimGroup Optional
   */
  private void updateNominationClaimForOptOutAwards( NominationClaim nominationClaim, ClaimGroup claimGroup, String awardType )
  {
    for ( Iterator<ClaimRecipient> recipientsIterator = nominationClaim.getClaimRecipients().iterator(); recipientsIterator.hasNext(); )
    {
      ClaimRecipient claimRecipient = recipientsIterator.next();
      if ( null != claimRecipient && null != claimRecipient.getRecipient() && claimRecipient.getRecipient().getOptOutAwards() )
      {
        if ( awardType.equals( PromotionAwardsType.POINTS ) )
        {
          claimRecipient.setAwardQuantity( 0L );
        }
        else
        {
          claimRecipient.setCashAwardQuantity( BigDecimal.ZERO );
        }
      }
    }

    if ( claimGroup != null && claimGroup.getParticipant().getOptOutAwards() )
    {
      if ( awardType.equals( PromotionAwardsType.POINTS ) )
      {
        claimGroup.setAwardQuantity( 0L );
      }
      else
      {
        claimGroup.setCashAwardQuantity( BigDecimal.ZERO );
      }
    }
  }
        

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimService#getClaimByIdWithAssociations(java.lang.Long,
   *      com.biperf.core.service.AssociationRequestCollection)
   * @param claimId
   * @param associationRequestCollection
   * @return Claim
   */
  public Claim getClaimByIdWithAssociations( Long claimId, AssociationRequestCollection associationRequestCollection )
  {
    return claimDAO.getClaimByIdWithAssociations( claimId, associationRequestCollection );
  }

  public void processDelayedApprovalClaims()
  {
    // TODO implement this
    logger.error( "processDelayedApprovalClaims successfully called -- implement later" );
    // if (promotionService == null) {
    // log.error("PROMOTION SERVICE IS FNULL***********");
    // } else {
    // Collection c = promotionService.getAll();
    // if (c == null) {
    // log.error(" PROMOTION SErVICE RETURNED A NULL COLLECTION! GAH!! ");
    // } else {
    // log.error("Found: "+c.size()+ " promotions !!!!!!!!!!!**!*1!11111111");
    // }
    // }
  }

  // ---------------------------------------------------------------------------
  // Validation Methods
  // ---------------------------------------------------------------------------

  /**
   * Validates the given claim.
   * 
   * @param claim the claim to be validated.
   * @return a list of validation errors, as a <code>List</code> of {@link ServiceErrorException}
   *         objects.
   */
  public List validateClaim( Claim claim )
  {
    List validationErrors = new ArrayList();

    if ( claim.isProductClaim() )
    {
      validationErrors.addAll( validateClaimProductNotEmpty( (ProductClaim)claim ) );
      validationErrors.addAll( validateClaimProductCharacteristicsUniqueness( (ProductClaim)claim ) );
      validationErrors.addAll( validateClaimProductCharacteristicDataTypeRequirements( (ProductClaim)claim ) );
    }
    else if ( !claim.isAbstractRecognitionClaim() && !claim.isQuizClaim() )
    {
      throw new BeaconRuntimeException( "Unknown claim subclass type:" + claim.getClass().getName() );
    }
    validationErrors.addAll( validateClaimDate( claim ) );

    if ( claim.getPromotion().isClaimFormUsed() )
    {
      validationErrors.addAll( validateClaimElements( claim, 0 ) );
    }

    return validationErrors;
  }

  private Collection validateClaimProductCharacteristicDataTypeRequirements( ProductClaim claim )
  {
    return CharacteristicValidationUtils.validateProductCharacteristicValueList( claim );
  }

  private List validateClaimProductNotEmpty( ProductClaim claim )
  {
    return validateClaimProductNotEmpty( claim.getClaimProducts() );
  }

  private List validateClaimProductNotEmpty( Set claimProducts )
  {
    List validationErrors = new ArrayList();

    if ( null == claimProducts || claimProducts.isEmpty() )
    {
      validationErrors.add( new ServiceError( "claims.submission.errors.MUST_ADD_PRODUCT" ) );
    }
    return validationErrors;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimService#validateClaimElements(com.biperf.core.domain.claim.Claim, int)
   */
  public List<ServiceError> validateClaimElements( Claim claim, int paxCount ) //// -------
  {
    return validateClaimElements( claim.getClaimElements(), claim.getNode(), claim.getPromotion(), paxCount );
  }

  @Override
  public List<ServiceError> validateClaimElements( List<ClaimElement> claimElementList, Node node, Promotion promotion, int paxCount )
  {
    List<ServiceError> validationErrors = new ArrayList<ServiceError>();

    validationErrors.addAll( validateClaimElementDataTypeRequirements( claimElementList, false ) );
    if ( validationErrors.isEmpty() )
    {
      validationErrors.addAll( validateClaimElementsUniqueness( claimElementList, node, promotion ) );
    }
    if ( validationErrors.isEmpty() )
    {
      validationErrors.addAll( validateClaimElementFormRules( claimElementList, promotion ) );
    }

    if ( promotion.isProductClaimPromotion() )
    {
      int teamMaxCount = getProductClaimPromotionTeamMaxCount( promotion.getId() );

      if ( teamMaxCount != 0 )
      {
        if ( paxCount > teamMaxCount )
        {
          validationErrors.add( new ServiceError( String.valueOf( teamMaxCount ), CmsResourceBundle.getCmsBundle().getString( "claims.submission.errors.MAX_TEAM_MEMBERS_YOU_CAN_ADD" ) ) );
        }
      }
    }

    return validationErrors;
  }

  /**
   * Validate that all claim elements that are required have values set, and that the values are
   * correctly formatted.
   * 
   * @return a list of validation errors, as a <code>List</code> of {@link ServiceError} objects.
   */
  private Collection<ServiceError> validateClaimElementDataTypeRequirements( List<ClaimElement> claimElementList, boolean isDraft )
  {
    List<ServiceError> validationErrors = new ArrayList<ServiceError>();

    for ( Iterator elementsIterator = claimElementList.iterator(); elementsIterator.hasNext(); )
    {
      ClaimElement claimElement = (ClaimElement)elementsIterator.next();
      ClaimFormStepElement claimFormStepElement = claimElement.getClaimFormStepElement();
      String cmAssetCode = claimFormStepElement.getClaimFormStep().getClaimForm().getCmAssetCode();

      // validate all of the required elements
      if ( claimFormStepElement.isRequired() && StringUtils.isEmpty( claimElement.getValue() ) && !isDraft )
      {
        validationErrors.add( new ServiceError( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, ContentReaderManager.getText( cmAssetCode, claimFormStepElement.getCmKeyForElementLabel() ) ) );
      }

      // validate the number fields.
      if ( claimFormStepElement.getClaimFormElementType().isNumberField() && !StringUtils.isEmpty( claimElement.getValue() ) )
      {
        try
        {
          BigDecimal bigDecimalValue = new BigDecimal( claimElement.getValue() );
          if ( bigDecimalValue.scale() > 0 && claimElement.getClaimFormStepElement().getNumberOfDecimals().intValue() == 0 )
          {
            validationErrors.add( new ServiceError( ServiceErrorMessageKeys.SYSTEM_ERRORS_INTEGER, ContentReaderManager.getText( cmAssetCode, claimFormStepElement.getCmKeyForElementLabel() ) ) );
          }
          else if ( bigDecimalValue.scale() > claimElement.getClaimFormStepElement().getNumberOfDecimals().intValue() )
          {
            validationErrors.add( new ServiceError( "system.errors.FLOAT_SCALE",
                                                    ContentReaderManager.getText( cmAssetCode, claimFormStepElement.getCmKeyForElementLabel() ),
                                                    claimElement.getClaimFormStepElement().getNumberOfDecimals().toString() ) );
          }
        }
        catch( NumberFormatException e )
        {
          validationErrors.add( new ServiceError( "system.errors.NONNUMERIC", ContentReaderManager.getText( cmAssetCode, claimFormStepElement.getCmKeyForElementLabel() ) ) );

        }
      }

      // validate address fields, not done here - done only in UI Form path since once we have a
      // claimElement the
      // address is one long string.

      // validate the text fields - alpha only, numeric only or alpha-numeric.
      if ( ( claimFormStepElement.getClaimFormElementType().isTextField() || claimFormStepElement.getClaimFormElementType().isTextBoxField() ) && !StringUtils.isEmpty( claimElement.getValue() ) )
      {
        // validate length
        String claimElementValue = claimElement.getValue() != null ? ( (String)StringUtil.skipHTML( claimElement.getValue() ) ).replace( "\"", "\"\"" ) : null;
        if ( claimFormStepElement.getMaxSize().intValue() < claimElementValue.trim().length() )
        {
          validationErrors.add( new ServiceError( "system.errors.MAXLENGTH",
                                                  ContentReaderManager.getText( cmAssetCode, claimFormStepElement.getCmKeyForElementLabel() ),
                                                  claimFormStepElement.getMaxSize().toString() ) );
        }

        // validate input format
        if ( claimFormStepElement.getTextFieldInputFormat().isNumericOnly() && !StringUtils.isNumeric( claimElementValue ) )
        {
          validationErrors.add( new ServiceError( "system.errors.NONNUMERIC", ContentReaderManager.getText( cmAssetCode, claimFormStepElement.getCmKeyForElementLabel() ) ) );
        }
        else if ( claimFormStepElement.getTextFieldInputFormat().isAlphaNumeric() && !StringUtils.isAlphanumericSpace( claimElementValue ) )
        {
          validationErrors.add( new ServiceError( "system.errors.NON_ALPHANUMERIC", ContentReaderManager.getText( cmAssetCode, claimFormStepElement.getCmKeyForElementLabel() ) ) );
        }
        else if ( claimFormStepElement.getTextFieldInputFormat().isPhoneNumber() && !ValidationUtil.isValidPhoneNumber( claimElementValue ) )
        {
          validationErrors.add( new ServiceError( "system.errors.PHONE", ContentReaderManager.getText( cmAssetCode, claimFormStepElement.getCmKeyForElementLabel() ) ) );
        }
        else if ( claimFormStepElement.getTextFieldInputFormat().isEmailAddress() && !GenericValidator.isEmail( claimElementValue ) )
        {
          validationErrors.add( new ServiceError( "system.errors.EMAIL", ContentReaderManager.getText( cmAssetCode, claimFormStepElement.getCmKeyForElementLabel() ) ) );
        }
      }
    }

    return validationErrors;
  }

  /**
   * Validates uniqueness of claimProductCharacteristics
   * 
   * @param claim the claim to be validated.
   * @return a list of validation errors, as a <code>List</code> of {@link ServiceError} objects.
   */
  private List validateClaimProductCharacteristicsUniqueness( ProductClaim claim )
  {
    List validationErrors = new ArrayList();

    // Validate Product Characteristics for uniqueness
    Iterator claimProductIter = claim.getClaimProducts().iterator();
    while ( claimProductIter.hasNext() )
    {
      ClaimProduct claimProduct = (ClaimProduct)claimProductIter.next();

      if ( claimProduct.getClaimProductCharacteristics() != null )
      {
        Iterator charIter = claimProduct.getClaimProductCharacteristics().iterator();
        while ( charIter.hasNext() )
        {
          ClaimProductCharacteristic prodChar = (ClaimProductCharacteristic)charIter.next();

          if ( prodChar.getProductCharacteristicType().getIsUnique() != null && prodChar.getProductCharacteristicType().getIsUnique().booleanValue() )
          {
            if ( !claimDAO.isClaimProductCharacteristicUnique( prodChar, claim.getPromotion() ) )
            {
              ProductCharacteristicType productCharacteristicType = (ProductCharacteristicType)productCharacteristicDAO.getCharacteristicById( prodChar.getProductCharacteristicType().getId() );
              // Bug # 35129 & Bug # 35472
              validationErrors.add( new ServiceError( ServiceErrorMessageKeys.CLAIM_PRODUCT_CHARACTERISTIC_NOT_UNIQUE,
                                                      ContentReaderManager.getText( productCharacteristicType.getCmAssetCode(), productCharacteristicType.getCmKeyName() ) ) );
            }

          }
        } // while characteristics
      } // if characteristics exist
    } // while claimProducts

    return validationErrors;
  } // end method

  /**
   * Validates the claimElements for uniqueness within node, node_type, hierarchy.
   * 
   * @param claimElementList the claim elements to be validated.
   * @return a list of validation errors, as a <code>List</code> of {@link ServiceError} objects.
   */
  private List validateClaimElementsUniqueness( List<ClaimElement> claimElementList, Node node, Promotion promotion )
  {
    List validationErrors = new ArrayList();

    Iterator claimElementIterator = claimElementList.iterator();
    while ( claimElementIterator.hasNext() )
    {
      ClaimElement claimElement = (ClaimElement)claimElementIterator.next();
      // Fix 26234
      if ( claimElement.getId() != null )
      {
        ClaimElement currentClaimElement = getClaimElementById( claimElement.getId() );
        if ( currentClaimElement != null && currentClaimElement.getValue() != null && currentClaimElement.getValue().equalsIgnoreCase( claimElement.getValue() ) )
        {
          continue;
        }
      }

      ClaimFormStepElement claimFormStepElement = claimElement.getClaimFormStepElement();

      String cmAssetCode = claimFormStepElement.getClaimFormStep().getClaimForm().getCmAssetCode();

      String uniquenessCode = claimFormStepElement.getUniquenessCode();
      if ( uniquenessCode != null )
      {
        if ( uniquenessCode.equals( HierarchyUniqueConstraintEnum.HIERARCHY_CODE ) )
        {
          if ( !claimDAO.isClaimElementValueUniqueWithinHierarchy( claimElement, node, promotion ) )
          {
            validationErrors.add( new ServiceError( ServiceErrorMessageKeys.CLAIM_ELEMENT_VALUE_NOT_UNIQUE_WITHIN_HIERARCHY,
                                                    ContentReaderManager.getText( cmAssetCode, claimFormStepElement.getCmKeyForElementLabel() ) ) );

          }
        }
        else if ( uniquenessCode.equals( HierarchyUniqueConstraintEnum.NODE_CODE ) )
        {
          if ( !claimDAO.isClaimElementValueUniqueWithinNode( claimElement, node, promotion ) )
          {
            validationErrors.add( new ServiceError( ServiceErrorMessageKeys.CLAIM_ELEMENT_VALUE_NOT_UNIQUE_WITHIN_NODE,
                                                    ContentReaderManager.getText( cmAssetCode, claimFormStepElement.getCmKeyForElementLabel() ) ) );
          }
        }
        else if ( uniquenessCode.equals( HierarchyUniqueConstraintEnum.NODE_TYPE_CODE ) )
        {
          if ( !claimDAO.isClaimElementValueUniqueWithinNodeType( claimElement, node, promotion ) )
          {
            validationErrors.add( new ServiceError( ServiceErrorMessageKeys.CLAIM_ELEMENT_VALUE_NOT_UNIQUE_WITHIN_NODE_TYPE,
                                                    ContentReaderManager.getText( cmAssetCode, claimFormStepElement.getCmKeyForElementLabel() ) ) );
          }
        }
      } // uniquenessCode != null
    } // while elements

    return validationErrors;
  } // end method

  /**
   * @param claimElementId
   * @return ClaimElement
   */
  private ClaimElement getClaimElementById( Long claimElementId )// Fix 26234
  {
    return this.claimDAO.getClaimElementById( claimElementId );
  }

  /**
   * Verifies that the promotion was active the claim was submitted.
   * 
   * @param claim the claim to be validated.
   * @return a list of validation errors, as a <code>List</code> of {@link ServiceErrorException}
   *         objects.
   */
  private List validateClaimDate( Claim claim )
  {
    List validationErrors = new ArrayList();

    boolean isDateValid = claim.getPromotion().isDateValidForSubmission( claim.getSubmissionDate() );
    if ( !isDateValid )
    {
      validationErrors.add( new ServiceError( ServiceErrorMessageKeys.CLAIM_DATE_NOT_VALID_FOR_PROMOTION ) );
    }
    return validationErrors;

  }

  /**
   * Verifies that the value of each claim element satisfies the criteria specified by the
   * associated promotion claim form step element validation.
   * 
   * @param claimElementList the claim elements will be validated.
   * @return a list of validation errors, as a <code>List</code> of {@link ServiceErrorException}
   *         objects.
   */
  private List validateClaimElementFormRules( List<ClaimElement> claimElementList, Promotion promotion )
  {
    List validationErrors = new ArrayList();
    String cmAssetCode = promotion.getClaimForm().getCmAssetCode();

    for ( Iterator iter = claimElementList.iterator(); iter.hasNext(); )
    {
      ClaimElement claimElement = (ClaimElement)iter.next();
      if ( !claimElementValidator.isValid( claimElement, promotion ) )
      {
        String cmKeyForElementLabel = claimElement.getClaimFormStepElement().getCmKeyForElementLabel();
        String elementLabel = ContentReaderManager.getText( cmAssetCode, cmKeyForElementLabel );

        validationErrors.add( new ServiceError( ServiceErrorMessageKeys.CLAIM_ELEMENT_VALUE_NOT_VALID, elementLabel ) );
      }
    }
    return validationErrors;
  }

  /**
   * Process / send processed & closed claim notifications to submitter Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimService#processClosedClaimNotifications(Claim)
   * @param claim
   */
  public void processClosedClaimNotifications( Claim claim )
  {

    emailNotificationService.processClosedClaimNotifications( claim );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimService#getParticipantQuizClaimHistoryByPromotionMap(QuizClaimQueryConstraint,
   *      AssociationRequestCollection)
   * @param quizClaimQueryConstraint
   */
  public Map getParticipantQuizClaimHistoryByPromotionMap( QuizClaimQueryConstraint quizClaimQueryConstraint, AssociationRequestCollection claimAssociationRequestCollection )
  {
    List claimList = getClaimList( quizClaimQueryConstraint );

    LinkedHashMap historyByPromotionMap = new LinkedHashMap();

    for ( Iterator iter = claimList.iterator(); iter.hasNext(); )
    {
      QuizClaim claim = (QuizClaim)iter.next();

      if ( claimAssociationRequestCollection != null )
      {
        claimAssociationRequestCollection.process( claim );
      }

      QuizPromotion promotion = (QuizPromotion)claim.getPromotion();
      ParticipantQuizClaimHistory participantQuizClaimHistory = (ParticipantQuizClaimHistory)historyByPromotionMap.get( promotion );
      if ( participantQuizClaimHistory == null )
      {
        participantQuizClaimHistory = new ParticipantQuizClaimHistory();
        participantQuizClaimHistory.setPromotion( promotion );
        historyByPromotionMap.put( promotion, participantQuizClaimHistory );
      }
      participantQuizClaimHistory.addQuizClaim( claim );
    }
    return historyByPromotionMap;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.ClaimService#getParticipantQuizClaimHistoryByPromotionMap(QuizClaimQueryConstraint,
   *      AssociationRequestCollection)
   * @param quizClaimQueryConstraint
   */
  public Map getParticipantDIYQuizClaimHistoryByQuizMap( QuizClaimQueryConstraint quizClaimQueryConstraint, AssociationRequestCollection claimAssociationRequestCollection )
  {
    List claimList = getClaimList( quizClaimQueryConstraint );

    LinkedHashMap<Long, ParticipantDIYQuizClaimHistory> historyByQuizMap = new LinkedHashMap<Long, ParticipantDIYQuizClaimHistory>();

    for ( Iterator iter = claimList.iterator(); iter.hasNext(); )
    {
      QuizClaim claim = (QuizClaim)iter.next();

      if ( claimAssociationRequestCollection != null )
      {
        claimAssociationRequestCollection.process( claim );
      }

      Quiz quiz = (Quiz)claim.getQuiz();
      ParticipantDIYQuizClaimHistory participantDIYQuizClaimHistory = (ParticipantDIYQuizClaimHistory)historyByQuizMap.get( quiz.getId() );
      if ( participantDIYQuizClaimHistory == null )
      {
        participantDIYQuizClaimHistory = new ParticipantDIYQuizClaimHistory();
        participantDIYQuizClaimHistory.setQuiz( quiz );
        participantDIYQuizClaimHistory.setPromotion( (QuizPromotion)claim.getPromotion() );
        historyByQuizMap.put( quiz.getId(), participantDIYQuizClaimHistory );
      }
      participantDIYQuizClaimHistory.addQuizClaim( claim );
    }
    return historyByQuizMap;
  }

  /**
   * Get pending claim approval count by user id. Must use getClaimsForApprovalByUser because of
   * complexity of the business rules. Overridden from
   * 
   * @param approverUserId
   * @param promotionType
   * @return pending claim count
   */
  public int getClaimsForApprovalByUserCount( Long approverUserId, PromotionType promotionType )
  {
    ApproverSeekingClaimQueryConstraint approverSnapshotClaimQueryConstraint = new ApproverSeekingClaimQueryConstraint();
    approverSnapshotClaimQueryConstraint.setApprovableUserId( approverUserId );
    approverSnapshotClaimQueryConstraint.setOpen( Boolean.TRUE );
    approverSnapshotClaimQueryConstraint.setExpired( Boolean.FALSE );
    return getClaimsByUserCount( approverSnapshotClaimQueryConstraint, approverUserId, promotionType );
  }

  public int getNominationClaimsForApprovalByUserCount( Long approverUserId, PromotionType promotionType, Long promotionId )
  {
    ApproverSeekingNominationClaimQueryConstraint approverSnapshotClaimQueryConstraint = new ApproverSeekingNominationClaimQueryConstraint();
    approverSnapshotClaimQueryConstraint.setUserId( approverUserId );
    approverSnapshotClaimQueryConstraint.setOpen( Boolean.TRUE );
    approverSnapshotClaimQueryConstraint.setExpired( Boolean.FALSE );
    approverSnapshotClaimQueryConstraint.setClaimIds( claimDAO.getClaimIdByApproverAndpromotion( approverUserId, promotionId ) );
    return getNominationClaimsByUserCount( approverSnapshotClaimQueryConstraint, approverUserId, promotionType );
  }

  /**
   * Bug 49790
   * Get the count of claims user can approve or approved
   * @param approverUserId
   * @param promotionType
   * @return claim count user can approve or approved
   */
  public int getUserInClaimsApprovalAudienceCount( Long approverUserId, PromotionType promotionType )
  {
    ApproverSeekingClaimQueryConstraint approverSnapshotClaimQueryConstraint = new ApproverSeekingClaimQueryConstraint();
    approverSnapshotClaimQueryConstraint.setApprovableUserId( approverUserId );
    approverSnapshotClaimQueryConstraint.setExpired( Boolean.FALSE );
    return getClaimsByUserCount( approverSnapshotClaimQueryConstraint, approverUserId, promotionType );
  }

  public int getUserNominationInClaimApprovalAudienceCount( Long approverUserId, PromotionType promotionType )
  {
    ApproverSeekingNominationClaimQueryConstraint approverSnapshotClaimQueryConstraint = new ApproverSeekingNominationClaimQueryConstraint();
    approverSnapshotClaimQueryConstraint.setUserId( approverUserId );
    approverSnapshotClaimQueryConstraint.setExpired( Boolean.FALSE );
    return getNominationClaimsByUserCount( approverSnapshotClaimQueryConstraint, approverUserId, promotionType );
  }

  /**
   * Get the count of claims approved by the user
   * @param approverUserId
   * @param promotionType
   * @return claim count user approved
   */
  public int getClaimsApprovedByUserCount( Long approverUserId, PromotionType promotionType )
  {
    ApproverSeekingClaimQueryConstraint claimQueryConstraint = new ApproverSeekingClaimQueryConstraint();
    claimQueryConstraint.setApprovedUserId( approverUserId );
    return getClaimsByUserCount( claimQueryConstraint, approverUserId, promotionType );
  }

  private int getClaimsByUserCount( ApproverSeekingClaimQueryConstraint claimQueryConstraint, Long approverUserId, PromotionType promotionType )
  {
    String timeZoneID = userService.getUserTimeZone( approverUserId );
    String datePattern = DateFormatterUtil.getOracleDatePattern( UserManager.getLocale().toString() );
    Date toDay = DateUtils.applyTimeZone( new Date(), timeZoneID );
    claimQueryConstraint.setToDate( DateUtils.toDisplayString( toDay ) );
    claimQueryConstraint.setDatePattern( datePattern );
    claimQueryConstraint.setClaimPromotionType( promotionType );
    return claimDAO.getClaimListCount( claimQueryConstraint );
  }

  private int getNominationClaimsByUserCount( ApproverSeekingNominationClaimQueryConstraint claimQueryConstraint, Long approverUserId, PromotionType promotionType )
  {
    String timeZoneID = userService.getUserTimeZone( approverUserId );
    String datePattern = DateFormatterUtil.getOracleDatePattern( UserManager.getLocale().toString() );
    Date toDay = DateUtils.applyTimeZone( new Date(), timeZoneID );
    claimQueryConstraint.setToDate( DateUtils.toDisplayString( toDay ) );
    claimQueryConstraint.setDatePattern( datePattern );
    claimQueryConstraint.setClaimPromotionType( promotionType );
    return claimDAO.getClaimListCount( claimQueryConstraint );
  }

  /**
   * Get all claims that the specified approverUserId can approve.
   * @param approverUserId
   * @param includedPromotionIds
   * @param isOpen - if set null, return both open and closed, and return claims already
   * approved by approverUserId
   * @param startDate
   * @param endDate
   * @param promotionType
   * @param claimAssociationRequestCollection
   * @param promotionAssociationRequestCollection
   * @param expired
   * 
   * @return List of PromotionClaimsValue object,
   */
  public List<PromotionApprovableValue> getClaimsForApprovalByUser( Long approverUserId,
                                                                    Long[] includedPromotionIds,
                                                                    Boolean isOpen,
                                                                    Date startDate,
                                                                    Date endDate,
                                                                    PromotionType promotionType,
                                                                    AssociationRequestCollection claimAssociationRequestCollection,
                                                                    AssociationRequestCollection promotionAssociationRequestCollection,
                                                                    Boolean expired,
                                                                    Boolean allApprovers )
  {
    List<PromotionApprovableValue> promotionClaimsValueList = new ArrayList<PromotionApprovableValue>();
    String timeZoneID = userService.getUserTimeZone( approverUserId );
    String datePattern = DateFormatterUtil.getOracleDatePattern( UserManager.getLocale().toString() );
    Date toDay = DateUtils.applyTimeZone( new Date(), timeZoneID );

    ApproverSeekingClaimQueryConstraint claimQueryConstraint = new ApproverSeekingClaimQueryConstraint();
    claimQueryConstraint.setToDate( DateUtils.toDisplayString( toDay ) );
    claimQueryConstraint.setDatePattern( datePattern );

    // jwe As per Bug # 13506 - All Approvers Cannot See Winners / Non-Winners, added the OR
    // BooleanUtils.isTrue( allApprovers ))
    // - If there are multiple approvers, we want to check if the approverUserId is in the group.
    if ( BooleanUtils.isFalse( expired ) && ( BooleanUtils.isTrue( isOpen ) || BooleanUtils.isTrue( allApprovers ) ) )
    {
      claimQueryConstraint.setApprovableUserId( approverUserId );
    }
    else
    {
      claimQueryConstraint.setApprovedUserId( approverUserId );
    }
    claimQueryConstraint.setEndDate( endDate );
    claimQueryConstraint.setStartDate( startDate );
    claimQueryConstraint.setOpen( isOpen );
    claimQueryConstraint.setExpired( expired );
    claimQueryConstraint.setClaimPromotionType( promotionType );
    claimQueryConstraint.setIncludedPromotionIds( includedPromotionIds );
    // Client customizations for WIP #42701 starts
    if ( includedPromotionIds != null && includedPromotionIds.length == 1 )
    {
      claimQueryConstraint.setCashPromo( promotionService.isCashPromo( includedPromotionIds[0] ) );
    }
    // Client customizations for WIP #42701 ends
    List approvableClaims = claimDAO.getClaimList( claimQueryConstraint );

    if ( approvableClaims.isEmpty() )
    {
      return Collections.EMPTY_LIST;
    }

    // Apply any associations to results

    Map claimsMapByPromotion = new HashMap();
    for ( Iterator iter = approvableClaims.iterator(); iter.hasNext(); )
    {
      Claim claim = (Claim)iter.next();

      if ( claimAssociationRequestCollection != null )
      {
        claimAssociationRequestCollection.process( claim );
      }

      Promotion claimPromotion = claim.getPromotion();
      List singlePromotionClaims = (List)claimsMapByPromotion.get( claimPromotion );
      if ( singlePromotionClaims == null )
      {
        singlePromotionClaims = new ArrayList();
        claimsMapByPromotion.put( claimPromotion, singlePromotionClaims );
      }
      singlePromotionClaims.add( claim );
    }

    for ( Iterator iter = claimsMapByPromotion.keySet().iterator(); iter.hasNext(); )
    {
      Promotion claimPromotion = (Promotion)iter.next();
      List singlePromotionClaims = (List)claimsMapByPromotion.get( claimPromotion );
      PromotionApprovableValue promotionClaimsValue = new PromotionApprovableValue();
      promotionClaimsValue.setPromotion( claimPromotion );
      promotionClaimsValue.setApprovables( singlePromotionClaims );

      // jwe As per Bug # 13506 - All Approvers Cannot See Winners / Non-Winners
      // this function is called 2 X's from the action, 1 time for the actual approver and 1 time
      // for an approver who didn't approve
      // -- if the actual approver is found when allApprovers is TRUE, don't add, it was added the
      // 1st time through
      boolean addClaim = false;
      if ( allApprovers.booleanValue() )
      {
        for ( Iterator claimsIter = promotionClaimsValue.getApprovables().iterator(); claimsIter.hasNext(); )
        {
          Approvable a = (Approvable)claimsIter.next();
          for ( Iterator approvableIter = a.getApprovableItems().iterator(); approvableIter.hasNext(); )
          {
            ApprovableItem ai = (ApprovableItem)approvableIter.next();
            if ( ai.getCurrentApproverUser() != null && !ai.getCurrentApproverUser().getId().equals( approverUserId ) )
            {
              addClaim = true;
            }
          }
        }
        if ( addClaim )
        {
          promotionClaimsValueList.add( promotionClaimsValue );
        }
      }
      else
      {
        promotionClaimsValueList.add( promotionClaimsValue );
      }
    }

    if ( promotionAssociationRequestCollection != null )
    {
      for ( Iterator iter = promotionClaimsValueList.iterator(); iter.hasNext(); )
      {
        PromotionApprovableValue promotionClaimsValue = (PromotionApprovableValue)iter.next();

        promotionAssociationRequestCollection.process( promotionClaimsValue.getPromotion() );
      }
    }
    // Copied this block from G4 Code
    for ( Iterator iter = promotionClaimsValueList.iterator(); iter.hasNext(); )
    {
      PromotionApprovableValue promotionClaimsValue = (PromotionApprovableValue)iter.next();
      List approvables = promotionClaimsValue.getApprovables();
      promotionClaimsValue.setActivePromoAppValue( false );
      for ( Iterator itermain = approvables.iterator(); itermain.hasNext(); )
      {

        Claim claimTemp = (Claim)itermain.next();
        if ( claimTemp instanceof NominationClaim )
        {

          NominationClaim claim = (NominationClaim)claimTemp;
          claim.setActiveNomClaim( false );

          if ( claim.getTeamName() != null && !claim.getTeamName().equals( "" ) )
          {
            /*for ( Iterator iter1 = claim.getClaimRecipients().iterator(); iter1.hasNext(); )
            {
              ClaimRecipient claimRecipient = (ClaimRecipient)iter1.next();
              if ( claimRecipient.getRecipient().getStatus() != null && claimRecipient.getRecipient().getStatus().isActive() )
              {
                claim.setActiveNomClaim( true );
                promotionClaimsValue.setActivePromoAppValue( true );
              }
            }*/
        	  for ( Iterator iter1 = claim.getTeamMembers().iterator(); iter1.hasNext(); )
              {
                ProductClaimParticipant productClaimParticipant = (ProductClaimParticipant)iter1.next();
                if ( productClaimParticipant.getParticipant().getStatus() != null 
                    && !productClaimParticipant.getParticipant().getStatus().getCode().equalsIgnoreCase( "Active" ) )
                {
                  //iter1.remove();
                } 
                else
                {
                  claim.setActiveNomClaim( true );
                  promotionClaimsValue.setActivePromoAppValue( true );
                }

              }
          }
          else
          {
            for ( Iterator iter1 = claim.getClaimRecipients().iterator(); iter1.hasNext(); )
            {
              ClaimRecipient claimRecipient = (ClaimRecipient)iter1.next();
              if ( claimRecipient.getRecipient().getStatus() != null && claimRecipient.getRecipient().getStatus().isActive() )
              {
                claim.setActiveNomClaim( true );
                promotionClaimsValue.setActivePromoAppValue( true );
              }
            }
          }

        }
        // TODO for recognitiona nd product claim promotions
      }
    }
    return promotionClaimsValueList;
  }

  // Alerts Performance Tuning
  public List<PromotionApprovableValue> getClaimsForApprovalByUser( Long approverUserId,
                                                                    Long[] includedPromotionIds,
                                                                    PromotionType promotionType,
                                                                    AssociationRequestCollection claimAssociationRequestCollection )
  {
    List<PromotionApprovableValue> promotionClaimsValueList = new ArrayList<PromotionApprovableValue>();
    String timeZoneID = userService.getUserTimeZone( approverUserId );
    String datePattern = DateFormatterUtil.getOracleDatePattern( UserManager.getLocale().toString() );
    Date toDay = DateUtils.applyTimeZone( new Date(), timeZoneID );

    ApproverSeekingClaimQueryConstraint claimQueryConstraint = new ApproverSeekingClaimQueryConstraint();
    claimQueryConstraint.setToDate( DateUtils.toDisplayString( toDay ) );
    claimQueryConstraint.setDatePattern( datePattern );
    claimQueryConstraint.setApprovableUserId( approverUserId );
    claimQueryConstraint.setEndDate( null );
    claimQueryConstraint.setStartDate( null );
    claimQueryConstraint.setOpen( Boolean.TRUE );
    claimQueryConstraint.setExpired( Boolean.FALSE );
    claimQueryConstraint.setClaimPromotionType( promotionType );
    claimQueryConstraint.setIncludedPromotionIds( includedPromotionIds );
    List approvableClaims = claimDAO.getClaimList( claimQueryConstraint );

    if ( approvableClaims.isEmpty() )
    {
      return Collections.EMPTY_LIST;
    }

    // Apply any associations to results
    Map claimsMapByPromotion = new HashMap();
    for ( Iterator iter = approvableClaims.iterator(); iter.hasNext(); )
    {
      Claim claim = (Claim)iter.next();
      if ( claimAssociationRequestCollection != null )
      {
        claimAssociationRequestCollection.process( claim );
      }
      Promotion claimPromotion = claim.getPromotion();
      List singlePromotionClaims = (List)claimsMapByPromotion.get( claimPromotion );
      if ( singlePromotionClaims == null )
      {
        singlePromotionClaims = new ArrayList();
        claimsMapByPromotion.put( claimPromotion, singlePromotionClaims );
      }
      singlePromotionClaims.add( claim );
    }

    for ( Iterator iter = claimsMapByPromotion.keySet().iterator(); iter.hasNext(); )
    {
      Promotion claimPromotion = (Promotion)iter.next();
      List singlePromotionClaims = (List)claimsMapByPromotion.get( claimPromotion );
      PromotionApprovableValue promotionClaimsValue = new PromotionApprovableValue();
      promotionClaimsValue.setPromotion( claimPromotion );
      promotionClaimsValue.setApprovables( singlePromotionClaims );
      promotionClaimsValueList.add( promotionClaimsValue );
    }

    // Copied this block from G4 Code
    for ( Iterator iter = promotionClaimsValueList.iterator(); iter.hasNext(); )
    {
      PromotionApprovableValue promotionClaimsValue = (PromotionApprovableValue)iter.next();
      List approvables = promotionClaimsValue.getApprovables();
      promotionClaimsValue.setActivePromoAppValue( false );
      for ( Iterator itermain = approvables.iterator(); itermain.hasNext(); )
      {
        Claim claimTemp = (Claim)itermain.next();
        if ( claimTemp instanceof NominationClaim )
        {
          NominationClaim claim = (NominationClaim)claimTemp;
          claim.setActiveNomClaim( false );
          if ( claim.getTeamName() != null && !claim.getTeamName().equals( "" ) )
          {
            for ( Iterator iter1 = claim.getClaimRecipients().iterator(); iter1.hasNext(); )
            {
              ClaimRecipient claimRecipient = (ClaimRecipient)iter1.next();
              if ( claimRecipient.getRecipient().getStatus() != null && claimRecipient.getRecipient().getStatus().isActive() )
              {
                claim.setActiveNomClaim( true );
                promotionClaimsValue.setActivePromoAppValue( true );
              }
            }
          }
          else
          {
            for ( Iterator iter1 = claim.getClaimRecipients().iterator(); iter1.hasNext(); )
            {
              ClaimRecipient claimRecipient = (ClaimRecipient)iter1.next();
              if ( claimRecipient.getRecipient().getStatus() != null && claimRecipient.getRecipient().getStatus().isActive() )
              {
                claim.setActiveNomClaim( true );
                promotionClaimsValue.setActivePromoAppValue( true );
              }
            }
          }

        }
        // TODO for recognitiona nd product claim promotions
      }
    }
    return promotionClaimsValueList;
  }

  /**
   * Get all claims that the specified approverUserId can approve.
   * @param approverUserId
   * @param includedPromotionIds
   * @param isOpen - if set null, return both open and closed, and return claims already
   * approved by approverUserId
   * @param startDate
   * @param endDate
   * @param promotionType
   * @param claimAssociationRequestCollection
   * @param promotionAssociationRequestCollection
   * @param expired
   * 
   * @return List of PromotionClaimsValue object,
   */
  public List<PromotionApprovableValue> getNominationClaimsForApprovalByUser( Long approverUserId,
                                                                              String claimIds,
                                                                              Long[] includedPromotionIds,
                                                                              Boolean isOpen,
                                                                              Date startDate,
                                                                              Date endDate,
                                                                              PromotionType promotionType,
                                                                              AssociationRequestCollection claimAssociationRequestCollection,
                                                                              AssociationRequestCollection promotionAssociationRequestCollection,
                                                                              Boolean expired,
                                                                              String filterApprovalStatusCode,
                                                                              Long approvalRound )
  {
    List<PromotionApprovableValue> promotionClaimsValueList = new ArrayList<PromotionApprovableValue>();
    String timeZoneID = userService.getUserTimeZone( approverUserId );
    String datePattern = DateFormatterUtil.getOracleDatePattern( UserManager.getLocale().toString() );
    Date toDay = DateUtils.applyTimeZone( new Date(), timeZoneID );

    ApproverSeekingNominationClaimQueryConstraint claimQueryConstraint = new ApproverSeekingNominationClaimQueryConstraint();
    claimQueryConstraint.setToDate( DateUtils.toDisplayString( toDay ) );
    claimQueryConstraint.setDatePattern( datePattern );

    if ( BooleanUtils.isFalse( expired ) && BooleanUtils.isTrue( isOpen ) && !UserManager.getUser().getAuthorities().contains( new SimpleGrantedAuthority( "ROLE_BI_ADMIN" ) ) )
    {
      claimQueryConstraint.setUserId( approverUserId );
    }
    else
    {
      if ( !UserManager.getUser().getAuthorities().contains( new SimpleGrantedAuthority( "ROLE_BI_ADMIN" ) ) )
      {
        claimQueryConstraint.setUserId( approverUserId );
      }
      claimQueryConstraint.setApprovalStatusType( filterApprovalStatusCode );
    }
    claimQueryConstraint.setEndDate( endDate );
    claimQueryConstraint.setStartDate( startDate );
    claimQueryConstraint.setOpen( isOpen );
    claimQueryConstraint.setExpired( expired );
    claimQueryConstraint.setClaimPromotionType( promotionType );
    claimQueryConstraint.setIncludedPromotionIds( includedPromotionIds );
    claimQueryConstraint.setApprovalRound( approvalRound );
    claimQueryConstraint.setClaimIds( claimIds );

    List approvableClaims = claimDAO.getClaimList( claimQueryConstraint );

    if ( approvableClaims.isEmpty() )
    {
      return Collections.EMPTY_LIST;
    }

    // Apply any associations to results

    Map claimsMapByPromotion = new HashMap();
    for ( Iterator iter = approvableClaims.iterator(); iter.hasNext(); )
    {
      Claim claim = (Claim)iter.next();

      if ( claimAssociationRequestCollection != null )
      {
        claimAssociationRequestCollection.process( claim );
      }

      Promotion claimPromotion = claim.getPromotion();
      List singlePromotionClaims = (List)claimsMapByPromotion.get( claimPromotion );
      if ( singlePromotionClaims == null )
      {
        singlePromotionClaims = new ArrayList();
        claimsMapByPromotion.put( claimPromotion, singlePromotionClaims );
      }
      singlePromotionClaims.add( claim );
    }

    for ( Iterator iter = claimsMapByPromotion.keySet().iterator(); iter.hasNext(); )
    {
      Promotion claimPromotion = (Promotion)iter.next();
      List singlePromotionClaims = (List)claimsMapByPromotion.get( claimPromotion );
      PromotionApprovableValue promotionClaimsValue = new PromotionApprovableValue();
      promotionClaimsValue.setPromotion( claimPromotion );
      promotionClaimsValue.setApprovables( singlePromotionClaims );
      promotionClaimsValueList.add( promotionClaimsValue );
    }

    if ( promotionAssociationRequestCollection != null )
    {
      for ( Iterator iter = promotionClaimsValueList.iterator(); iter.hasNext(); )
      {
        PromotionApprovableValue promotionClaimsValue = (PromotionApprovableValue)iter.next();

        promotionAssociationRequestCollection.process( promotionClaimsValue.getPromotion() );
      }
    }
    // Copied this block from G4 Code
    /*
     * for ( Iterator iter = promotionClaimsValueList.iterator(); iter.hasNext(); ) {
     * PromotionApprovableValue promotionClaimsValue = (PromotionApprovableValue)iter.next(); List
     * approvables = promotionClaimsValue.getApprovables();
     * promotionClaimsValue.setActivePromoAppValue( false ); for ( Iterator itermain =
     * approvables.iterator(); itermain.hasNext(); ) { Claim claimTemp = (Claim)itermain.next(); if
     * ( claimTemp instanceof NominationClaim ) { NominationClaim claim =
     * (NominationClaim)claimTemp; claim.setActiveNomClaim( false ); if ( claim.getTeamName() !=
     * null && !claim.getTeamName().equals( "" ) ) { for ( Iterator iter1 =
     * claim.getClaimRecipients().iterator(); iter1.hasNext(); ) { ClaimRecipient claimRecipient =
     * (ClaimRecipient)iter1.next(); if ( claimRecipient.getRecipient().getStatus() != null &&
     * !claimRecipient.getRecipient().getStatus().getCode().equalsIgnoreCase( "Active" ) ) {
     * //iter1.remove(); } else { claim.setActiveNomClaim( true );
     * promotionClaimsValue.setActivePromoAppValue( true ); } } if (
     * claim.getClaimRecipients().size() < 1 || claim.getClaimRecipients().isEmpty() ) {
     * itermain.remove(); } if ( promotionClaimsValue.getApprovables().size() < 1 ||
     * promotionClaimsValue.getApprovables().isEmpty() ) { iter.remove(); } } else { for ( Iterator
     * iter1 = claim.getClaimRecipients().iterator(); iter1.hasNext(); ) { ClaimRecipient
     * claimRecipient = (ClaimRecipient)iter1.next(); if ( claimRecipient.getRecipient().getStatus()
     * != null && !claimRecipient.getRecipient().getStatus().getCode().equalsIgnoreCase( "Active" )
     * ) { //iter1.remove(); } else { claim.setActiveNomClaim( true );
     * promotionClaimsValue.setActivePromoAppValue( true ); } } } } } }
     */
    return promotionClaimsValueList;
  }

  /**
   * Get all claims that the specified approverUserId can approve or approved.
   * This includes closed claims approved by other users when this user is part of approver list
   * @param approverUserId
   * @param includedPromotionIds
   * @param isOpen - if set null, return both open and closed, and return claims already
   * approved by approverUserId
   * @param startDate
   * @param endDate
   * @param promotionType
   * @param claimAssociationRequestCollection
   * @param promotionAssociationRequestCollection
   * @param expired
   * 
   * @return List of PromotionClaimsValue object,
   */
  public List<PromotionApprovableValue> getProductClaimsForApprovalByUser( Long approverUserId,
                                                                           Long[] includedPromotionIds,
                                                                           Boolean isOpen,
                                                                           Date startDate,
                                                                           Date endDate,
                                                                           PromotionType promotionType,
                                                                           AssociationRequestCollection claimAssociationRequestCollection,
                                                                           AssociationRequestCollection promotionAssociationRequestCollection,
                                                                           Boolean expired,
                                                                           String sortedOn,
                                                                           String sortedBy,
                                                                           int rowNumStart,
                                                                           int rowNumEnd )
  {
    List<PromotionApprovableValue> promotionClaimsValueList = new ArrayList<PromotionApprovableValue>();
    String timeZoneID = userService.getUserTimeZone( approverUserId );
    String datePattern = DateFormatterUtil.getOracleDatePattern( UserManager.getLocale().toString() );
    Date toDay = DateUtils.applyTimeZone( new Date(), timeZoneID );

    ApproverSeekingClaimQueryConstraint claimQueryConstraint = new ApproverSeekingClaimQueryConstraint();
    claimQueryConstraint.setToDate( DateUtils.toDisplayString( toDay ) );
    claimQueryConstraint.setDatePattern( datePattern );

    claimQueryConstraint.setApprovableUserId( approverUserId );
    claimQueryConstraint.setEndDate( endDate );
    claimQueryConstraint.setStartDate( startDate );
    claimQueryConstraint.setOpen( isOpen );
    claimQueryConstraint.setExpired( expired );
    claimQueryConstraint.setClaimPromotionType( promotionType );
    claimQueryConstraint.setIncludedPromotionIds( includedPromotionIds );
    claimQueryConstraint.setSortedOn( sortedOn );
    claimQueryConstraint.setSortedBy( sortedBy );
    claimQueryConstraint.setRowNumStart( rowNumStart );
    claimQueryConstraint.setRowNumEnd( rowNumEnd );

    List approvableClaims = claimDAO.getClaimList( claimQueryConstraint );

    if ( approvableClaims.isEmpty() )
    {
      return Collections.emptyList();
    }

    // Apply any associations to results
    Map claimsMapByPromotion = new HashMap();
    for ( Iterator iter = approvableClaims.iterator(); iter.hasNext(); )
    {
      Claim claim = (Claim)iter.next();

      if ( claimAssociationRequestCollection != null )
      {
        claimAssociationRequestCollection.process( claim );
      }

      Promotion claimPromotion = claim.getPromotion();
      List singlePromotionClaims = (List)claimsMapByPromotion.get( claimPromotion );
      if ( singlePromotionClaims == null )
      {
        singlePromotionClaims = new ArrayList();
        claimsMapByPromotion.put( claimPromotion, singlePromotionClaims );
      }
      singlePromotionClaims.add( claim );
    }

    for ( Iterator iter = claimsMapByPromotion.keySet().iterator(); iter.hasNext(); )
    {
      Promotion claimPromotion = (Promotion)iter.next();
      List singlePromotionClaims = (List)claimsMapByPromotion.get( claimPromotion );
      PromotionApprovableValue promotionClaimsValue = new PromotionApprovableValue();
      promotionClaimsValue.setPromotion( claimPromotion );
      promotionClaimsValue.setApprovables( singlePromotionClaims );
      promotionClaimsValueList.add( promotionClaimsValue );
    }

    if ( promotionAssociationRequestCollection != null )
    {
      for ( Iterator iter = promotionClaimsValueList.iterator(); iter.hasNext(); )
      {
        PromotionApprovableValue promotionClaimsValue = (PromotionApprovableValue)iter.next();
        promotionAssociationRequestCollection.process( promotionClaimsValue.getPromotion() );
      }
    }

    return promotionClaimsValueList;
  }

  /**
   * @return value of approverService property
   */
  public ClaimApproverSnapshotService getClaimApproverSnapshotService()
  {
    return claimApproverSnapshotService;
  }

  /**
   * @param approverService value for approverService property
   */
  public void setClaimApproverSnapshotService( ClaimApproverSnapshotService approverService )
  {
    this.claimApproverSnapshotService = approverService;
  }

  public boolean hasPendingJournalForClaim( Long recipientId, Long claimId )
  {
    return claimDAO.getNonPostedJournalCount( recipientId, claimId ) > 0;
  }

  private RandomNumberStrategy getRandomNumberStrategy()
  {
    return randomNumberStrategy;
  }

  public void setRandomNumberStrategy( RandomNumberStrategy randomNumberStrategy )
  {
    this.randomNumberStrategy = randomNumberStrategy;
  }

  /**
   * Get each open claim of the specified approverType whose submitters node name is not found in
   * the claim promotion's approval hierarchy.
   * 
   * @param approverType
   * @return List
   */
  public List getOpenClaimsWithNoMatchingNodeInApproverHierarchy( ApproverType approverType )
  {
    return claimDAO.getOpenClaimsWithNoMatchingNodeInApproverHierarchy( approverType );
  }

  /**
   * Returns list of claims
   * 
   * @param promotionId
   * @param eligibilityClaimsType
   * @param promoSweepstake
   * @return List
   */
  public List getProductClaimClaimsList( Long promotionId, String eligibilityClaimsType, PromotionSweepstake promoSweepstake )
  {
    ProductClaimClaimQueryConstraint claimQueryConstraint = new ProductClaimClaimQueryConstraint();

    claimQueryConstraint.setIncludedPromotionIds( promotionId != null ? new Long[] { promotionId } : null );
    claimQueryConstraint.setStartDate( promoSweepstake.getStartDate() );
    claimQueryConstraint.setEndDate( promoSweepstake.getEndDate() );
    claimQueryConstraint.setOpen( Boolean.FALSE );// we always need closed claims only
    if ( SweepstakesClaimEligibilityType.ALL_CLOSED_CLAIMS_WITH_ALL_ITEMS_APPROVED.equals( eligibilityClaimsType ) )
    {
      claimQueryConstraint.setEligibleClaimsAllApprovedItem( true );
    }
    else
    {
      claimQueryConstraint.setEligibleClaimsAllApprovedItem( false );
    }

    if ( SweepstakesClaimEligibilityType.ALL_CLOSED_CLAIMS_WITH_ONE_OR_MORE_ITEMS_APPROVED.equals( eligibilityClaimsType ) )
    {
      claimQueryConstraint.setEligibleClaimsAtleastOneApprovedItem( true );
    }
    else
    {
      claimQueryConstraint.setEligibleClaimsAtleastOneApprovedItem( false );
    }

    return this.claimDAO.getClaimList( claimQueryConstraint );
  }

  public void saveCalculatorClaimInfo( AbstractRecognitionClaim detachedClaim, Long awardQuantity, Long calculatorScore, PromoMerchProgramLevel promoMerchProgramLevel ) throws ServiceErrorException
  {
    AbstractRecognitionClaim attachedClaim = (AbstractRecognitionClaim)getClaimById( detachedClaim.getId() );
    ClaimRecipient attachedRecipient = (ClaimRecipient)attachedClaim.getClaimRecipients().iterator().next();
    attachedRecipient.setCalculatorScore( calculatorScore );
    attachedRecipient.setAwardQuantity( awardQuantity );
    attachedRecipient.setPromoMerchProgramLevel( promoMerchProgramLevel );

    for ( CalculatorResponse detachedResponse : detachedClaim.getCalculatorResponses() )
    {
      boolean foundAttachedResponse = false;
      for ( CalculatorResponse attachedResponse : attachedClaim.getCalculatorResponses() )
      {
        if ( attachedResponse.getCriterion().getId().equals( detachedResponse.getCriterion().getId() ) )
        {
          attachedResponse.setSelectedRating( detachedResponse.getSelectedRating() );
          attachedResponse.setCriterionWeight( detachedResponse.getCriterionWeight() );
          attachedResponse.setRatingValue( detachedResponse.getRatingValue() );

          foundAttachedResponse = true;
          break;
        }
      }
      if ( !foundAttachedResponse )
      {
        attachedClaim.addCalculatorResponse( detachedResponse );
      }
    }
  }

  public void saveClaimItems( List<ClaimRecipient> claimItems ) throws ServiceErrorException
  {
    for ( ClaimRecipient cr : claimItems )
    {
      claimDAO.saveClaimitem( cr );
    }
  }

  /**
   * Schedules Recognition to be processed on a future date
   * 
   * @param claim
   * @param deliveryDate
   * @throws ServiceErrorException
   */
  public void scheduleRecognition( RecognitionClaim claim, Date deliveryDate ) throws ServiceErrorException
  {
    RecognitionPromotion promotion = (RecognitionPromotion)claim.getPromotion();
    boolean isMerchandise = promotion.getAwardType().getCode().equals( PromotionAwardsType.MERCHANDISE );
    Long timeGap = 0L;
    int countAlreadyScheduled = 0;
    List<ScheduledRecognition> scheduledRecognitions = scheduledRecognitionService.getScheduledRecognitionsByDeliveryDate( deliveryDate );
    if ( scheduledRecognitions != null )
    {
      countAlreadyScheduled = scheduledRecognitions.size();
      timeGap = timeGap + countAlreadyScheduled * 5000;
    }
    ClaimRecipient claimRecipient = (ClaimRecipient)claim.getClaimRecipients().iterator().next();

    LinkedHashMap inputParameters = new LinkedHashMap();

    inputParameters.put( "promotionId", promotion.getId() );
    inputParameters.put( "submitterUserId", claim.getSubmitter().getId() );
    inputParameters.put( "submitterNodeId", claim.getNode().getId() );
    UserAddress recipientAddress = null;
    String recipientCountryCode = null;
    if ( claimRecipient.getRecipient() != null )
    {
      inputParameters.put( "recipientUserId", claimRecipient.getRecipient().getId() );
      // claimRecipient.getNode() is null so adding below condition
      inputParameters.put( "recipientNodeId", claimRecipient.getNode() != null ? claimRecipient.getNode().getId() : claimRecipient.getRecipient().getPrimaryUserNode().getNode().getId() );
      recipientAddress = userService.getPrimaryUserAddress( claimRecipient.getRecipient().getId() );
      if ( recipientAddress != null )
      {
        recipientCountryCode = recipientAddress.getAddress().getCountry().getCountryCode();
      }
    }
    inputParameters.put( "teamId", claim.getTeamId() );

    Long budgetId = null;
    if ( promotion.isBudgetUsed() )
    {
      budgetId = calculateBudget( promotion, claim, claimRecipient, recipientAddress, isMerchandise );
    }
    if ( budgetId != null )
    {
      inputParameters.put( "budgetId", budgetId );
    }

    if ( claim.getProxyUser() != null )
    {
      inputParameters.put( "proxyUserId", claim.getProxyUser().getId() );
    }
    inputParameters.put( "submitterComments", claim.getSubmitterComments() );
    inputParameters.put( "ownCardName", claim.getOwnCardName() );
    if ( claim.getBehavior() != null )
    {
      inputParameters.put( "behavior", claim.getBehavior().getCode() );
    }
    if ( claim.getCard() != null )
    {
      inputParameters.put( "cardId", claim.getCard().getId() );
    }
    inputParameters.put( "certificateId", claim.getCertificateId() );

    if ( claimRecipient.getAwardQuantity() != null )
    {
      inputParameters.put( "awardQuantity", claimRecipient.getAwardQuantity().toString() );
    }
    if ( claimRecipient.getCalculatorScore() != null )
    {
      inputParameters.put( "calculatorScore", claimRecipient.getCalculatorScore().toString() );
    }
    if ( claimRecipient.getPromoMerchProgramLevel() != null )
    {
      inputParameters.put( "levelId", claimRecipient.getPromoMerchProgramLevel().getId() );
    }
    inputParameters.put( "productId", claimRecipient.getProductId() );
    inputParameters.put( "recipientCountryCode", recipientCountryCode );

    inputParameters.put( "sendCopyToMe", claim.isCopySender() );
    inputParameters.put( "sendCopyToManager", claim.isCopyManager() );
    inputParameters.put( "copyOthers", claim.isCopyOthers() );
    inputParameters.put( "sendCopyToOthers", claim.getSendCopyToOthers() );
    inputParameters.put( "hidePublicRecognition", claim.isHidePublicRecognition() );
    inputParameters.put( "source", claim.getSource() );

    if ( claim.getClaimElements() != null )
    {
      inputParameters.put( "claimElementSize", new Integer( claim.getClaimElements().size() ) );
      int i = 1;
      for ( ClaimElement claimElement : claim.getClaimElements() )
      {
        if ( claimElement.getValue() != null && !claimElement.getValue().isEmpty() )
        {
          claimElement.setValue( claimElement.getValue().replace( "|", "~" ) );
        }
        inputParameters.put( "claimElement" + i, claimElement );
        i++;
      }
    }

    if ( claim.getCalculatorResponses() != null )
    {
      inputParameters.put( "calculatorResponseSize", new Integer( claim.getCalculatorResponses().size() ) );
      int i = 1;
      for ( CalculatorResponse calculatorResponse : claim.getCalculatorResponses() )
      {
        inputParameters.put( "calculatorResponse" + i, calculatorResponse );
        i++;
      }
    }
    if ( claim.getAnniversaryNumberOfDays() != null )
    {
      inputParameters.put( "anniversaryNumberOfDays", claim.getAnniversaryNumberOfDays().toString() );
    }
    if ( claim.getAnniversaryNumberOfYears() != null )
    {
      inputParameters.put( "anniversaryNumberOfYears", claim.getAnniversaryNumberOfYears().toString() );
    }

    if ( claim.getCelebrationManagerMessage() != null )
    {
      inputParameters.put( "celebrationManagerMessageId", claim.getCelebrationManagerMessage().getId() );
    }

    ScheduledRecognition scheduledRecognition = scheduledRecognitionService.createScheduledRecognition( new ScheduledRecognition(), inputParameters, deliveryDate );

    ScheduledRecognition scheduledRecognitionSaved = scheduledRecognitionService.getScheduledRecognitionById( scheduledRecognition.getId() );

    scheduledRecognitionService.scheduleDelaySendRecognitionProcess( scheduledRecognitionSaved, deliveryDate, timeGap );
    timeGap = timeGap + 5000;

  }

  /**
   * Schedules Recognitions to be processed on a future date
   * 
   * @param claims
   * @param deliveryDate
   * @throws ServiceErrorException
   */
  public void scheduleRecognitions( List<RecognitionClaim> claims, Date deliveryDate ) throws ServiceErrorException
  {
    for ( RecognitionClaim claim : claims )
    {
      scheduleRecognition( claim, deliveryDate );
    }
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.AbstractClaimProcessingStrategy#calculateBudget(java.util.Set,
   *      Approvable)
   */
  private Long calculateBudget( Promotion promotion, RecognitionClaim claim, ClaimRecipient claimRecipient, UserAddress recipientAddress, boolean isMerchandise )
  {
    Long budgetId = null;
    Budget budget = promotionService.getAvailableBudget( promotion.getId(), claim.getSubmitter().getId(), claim.getNode().getId() );
    if ( budget == null )
    {
      throw new BeaconRuntimeException( "No budget found for Claim " + claim.getClaimNumber() );
    }
    else
    {
      budgetId = budget.getId();
    }
    budget.setActionType( BudgetActionType.lookup( BudgetActionType.DEDUCT ) );
    budget.setReferenceVariableForClaimId( claim.getId() );
    BigDecimal currentBudget = budget.getCurrentValue();
    BigDecimal totalPayout = BigDecimal.ZERO;

    if ( isMerchandise )
    {
      MerchLevel omLevel = merchLevelService.getMerchLevelData( claimRecipient.getPromoMerchProgramLevel(), true, false );
      totalPayout = totalPayout.add( BigDecimal.valueOf( omLevel.getMaxValue() ) );
    }
    else
    {
      if ( claimRecipient.getAwardQuantity() != null )
      {
        BigDecimal calculatedBudgetValue = calculateBudgetEquivalence( BigDecimal.valueOf( claimRecipient.getAwardQuantity().intValue() ), claim.getSubmitter(), claimRecipient.getRecipient() );
        totalPayout = totalPayout.add( calculatedBudgetValue );
      }
    }

    // If the total payout falls within the budget, decrease the budget
    if ( totalPayout.compareTo( currentBudget ) == -1 || totalPayout.compareTo( currentBudget ) == 0 )
    {
      budget.setCurrentValue( currentBudget.subtract( totalPayout ) );
    }
    else
    {
      // Shouldn't get here. UI Layer should prevent.
      // Might want to handle this gracefully if this is a situation that occurs often.
      logger.error( "ERROR BudgetUsageOverAllocallatedException. budgetId=" + budget.getId() + " totalPayout=" + totalPayout + " currentBudget=" + currentBudget );
      List serviceErrors = new ArrayList();
      serviceErrors.add( new ServiceError( ServiceErrorMessageKeys.HARD_CAP_BUDGET_EXCEEDED_RECOGNITION, String.valueOf( currentBudget ) ) );
      throw new BeaconRuntimeException( new BudgetUsageOverAllocallatedException( serviceErrors ) );
    }

    return budgetId;
  }

  private int calculateBudgetEquivalence( int value, Participant submitter, Participant recipient, UserAddress recipientAddress )
  {
    UserAddress submitterAddress = userService.getPrimaryUserAddress( submitter.getId() );

    if ( null != submitterAddress && null != recipientAddress )
    {
      String hostCountryCode = submitterAddress.getAddress().getCountry().getCountryCode();
      String foreignCountryCode = recipientAddress.getAddress().getCountry().getCountryCode();
      Double ratio = awardBanQServiceFactory.getAwardBanQService().getMediaRatio( hostCountryCode, foreignCountryCode );
      if ( ratio != null )
      {
        value = new Double( Math.ceil( value * ratio.doubleValue() ) ).intValue();
      }
    }
    return value;
  }

  private BigDecimal calculateBudgetEquivalence( BigDecimal value, Participant submitter, Participant recipient )
  {
    if ( null != submitter.getPrimaryAddress() && null != recipient.getPrimaryAddress() )
    {
      BigDecimal usMediaValue = countryService.getBudgetMediaValueByCountryCode( Country.UNITED_STATES );
      BigDecimal recipientMediaValue = recipient.getPrimaryAddress().getAddress().getCountry().getBudgetMediaValue();
      value = BudgetUtils.applyMediaConversion( value, recipientMediaValue, usMediaValue );
    }
    return value;
  }

  /**
   * Saves the specific claim, sends submitter and approver notifications (if applicable) and updates budget_id on journal table.
   * 
   * @param claim the claim to save.
   * @param claimFormStepId required if notifications are to be sent
   * @param approverUser
   * @param forceAutoApprove
   * @throws ServiceErrorException if claim is invalid.
   */
  public void saveandUpdateRecognitionClaim( Claim claim, Long claimFormStepId, User approverUser, boolean forceAutoApprove, boolean deductBudget, Budget budget ) throws ServiceErrorException
  {
    claim = saveClaim( claim, claimFormStepId, approverUser, forceAutoApprove, deductBudget );
    if ( budget != null )
    {
      List<Journal> journals = journalDAO.getJournalsByClaimId( claim.getId() );
      for ( Journal journal : journals )
      {
        journal.setBudget( budget );
        journalDAO.saveJournalEntry( journal );
      }
    }
  }

  public void setAwardBanQServiceFactory( AwardBanQServiceFactory awardBanQServiceFactory )
  {
    this.awardBanQServiceFactory = awardBanQServiceFactory;
  }

  public void setScheduledRecognitionService( ScheduledRecognitionService scheduledRecognitionService )
  {
    this.scheduledRecognitionService = scheduledRecognitionService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  public void setClaimGroupDAO( ClaimGroupDAO claimGroupDAO )
  {
    this.claimGroupDAO = claimGroupDAO;
  }

  public void setJournalDAO( JournalDAO journalDAO )
  {
    this.journalDAO = journalDAO;
  }

  @Override
  public int getPublicRecognitionClaimsSentByUserId( Long promoId, Long paxId, Date startDate, Date endDate, String promotionType, String approvalStatus )
  {
    return this.claimDAO.getPublicRecognitionClaimsSentByUserId( promoId, paxId, startDate, endDate, promotionType, approvalStatus );
  }

  @Override
  public int getPublicRecognitionClaimsReceivedbyUserId( Long promoId, Long paxId, Date startDate, Date endDate, String promotionType, String approvalStatus )
  {
    return this.claimDAO.getPublicRecognitionClaimsReceivedbyUserId( promoId, paxId, startDate, endDate, promotionType, approvalStatus );
  }

  // Client customizations for WIP #62128 starts
  @Override
  public boolean submitCheersRecognition( AbstractRecognitionClaim claim, boolean deductBudget )
  {
    boolean success = true;
    Long teamId = getNextTeamId();
    try
    {
      claim.setTeamId( teamId );
      saveClaim( claim, null, null, false, deductBudget );
    }
    catch( Exception e )
    {
      success = false;
      if ( e.getCause() instanceof BudgetUsageOverAllocallatedException )
      {
        BudgetUsageOverAllocallatedException budgetException = (BudgetUsageOverAllocallatedException)e.getCause();
        logger.error( " BudgetUsageOverAllocallatedException while sending cheers Recogniton " + budgetException.getServiceErrors() );
      }
      else
      {
        e.printStackTrace();
        logger.error( "ServiceErrorException while sending cheers Recogniton : " + e);
      }
    }
    return success;
  }
  // Client customizations for WIP #62128 end
  
  public RecognitionClaimSubmissionResponse submitRecognition( RecognitionClaimSubmission submission ) throws ServiceErrorException
  {
    RecognitionClaimSubmissionResponse claimResponse = validate( submission );

    if ( !claimResponse.isSuccess() )
    {
      return claimResponse;
    }

    List<AbstractRecognitionClaim> claims = new ArrayList<AbstractRecognitionClaim>();
    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_MERCHANDISE_COUNTRIES ) );
    AbstractRecognitionPromotion promotion = (AbstractRecognitionPromotion)promotionService.getPromotionByIdWithAssociations( submission.getPromotionId(), promoAssociationRequestCollection );
 // Client customization for WIP #39189 starts
    Set<TcccClaimFile> claimFiles = null;
    if ( promotion.isNominationPromotion() && ( (NominationPromotion)promotion ).isEnableFileUpload() )
    {
      claimFiles = buildClaimFiles( submission );
    }
    // Client customization for WIP #39189 ends
    if ( promotion.isRecognitionPromotion() && ( (RecognitionPromotion)promotion ).isIncludePurl() )
    {
      createPurl( submission, (RecognitionPromotion)promotion );
    }
    else
    {
      // non-team - N claims, each with 1 claim recipient
      Long teamId = null;
      int recipientCount = submission.getRecognitionClaimRecipients().size();
      if ( recipientCount >= 1 )
      {
        teamId = claimDAO.getNextTeamId();
      }

      for ( RecognitionClaimRecipient recognitionClaimRecipient : removeDuplicateRecipients( submission.getRecognitionClaimRecipients() ) )
      {
        AbstractRecognitionClaim newClaim = toClaimBasics( promotion, submission );
        newClaim.setTeamId( teamId );

        ClaimRecipient claimRecipient = buildIndividualClaimRecipient( promotion, recognitionClaimRecipient );
        newClaim.addClaimRecipient( claimRecipient );

        // add calculator responses to the claim.
        if ( recognitionClaimRecipient.getCalculatorResponseBeans() != null && recognitionClaimRecipient.getCalculatorResponseBeans().size() > 0 )
        {
          for ( CalculatorResponseBean calculatorResponseBean : recognitionClaimRecipient.getCalculatorResponseBeans() )
          {
            CalculatorResponse calculatorResponse = new CalculatorResponse();

            CalculatorCriterion cc = calculatorService.getCalculatorCriterionByIdWithAssociations( calculatorResponseBean.getCriterionId(), null );
            calculatorResponse.setCriterion( cc );

            if ( cc.getCalculator().isWeightedScore() )
            {
              calculatorResponse.setCriterionWeight( new Integer( cc.getWeightValue() ) );
            }
            if ( calculatorResponseBean.getSelectedRating() != null )
            {
              CalculatorCriterionRating selectedRating = calculatorService.getCriterionRatingById( new Long( calculatorResponseBean.getSelectedRating() ) );
              calculatorResponse.setSelectedRating( selectedRating );
              calculatorResponse.setRatingValue( selectedRating.getRatingValue() );
              newClaim.addCalculatorResponse( calculatorResponse );
            }
          }
        }

        claims.add( newClaim );
      }
    }

    List validationErrors = new ArrayList();
    if ( promotion.isRecognitionPromotion() && ! ( (RecognitionPromotion)promotion ).isIncludePurl() )
    {
      String recipientName = "";
      String errorMsgHeader = "Issue with TimeZoneId for ";
      for ( AbstractRecognitionClaim claim : claims )
      {
        try
        {
          ClaimRecipient claimRecipient = (ClaimRecipient)claim.getClaimRecipients().iterator().next();
          recipientName = claimRecipient.getRecipient().getFirstName() + " " + claimRecipient.getRecipient().getLastName();
          String recipientTimeZoneID = userService.getUserTimeZoneByUserCountry( claimRecipient.getRecipient().getId() );
          Date recipientCurrentDate = DateUtils.applyTimeZone( new Date(), recipientTimeZoneID );
          Date recipientSendDate = null;
          Date sendDate = DateUtils.toDate( submission.getRecipientSendDate(), userService.getUserTimeZoneByUserCountry( UserManager.getUserId() ) );

          if ( sendDate != null )
          {
            recipientSendDate = DateUtils.applyTimeZoneWithFirstTimeOfDay( sendDate, recipientTimeZoneID );
          }
          if ( recipientSendDate != null && recipientSendDate.after( recipientCurrentDate ) )
          {
            // convert to system time zone to schedule claim
            String systemTimeZoneID = claimDAO.getDBTimeZone();
            Date deliveryDate = DateUtils.applyTimeZone( sendDate, systemTimeZoneID );

            RecognitionPromotion recognitionPromotion = (RecognitionPromotion)promotion;
            RecognitionClaim recognitionClaim = (RecognitionClaim)claim;
            if ( recognitionPromotion.isIncludeCelebrations() && recognitionPromotion.isAllowOwnerMessage() )
            {
              Participant recipient = claimRecipient.getRecipient();
              CelebrationManagerMessage celebrationManagerMessage = populateAndSendCelebrationManagerMessages( recognitionPromotion, recognitionClaim, null, recipient, sendDate );
              recognitionClaim.setCelebrationManagerMessage( celebrationManagerMessage );
            }
            if ( submission.getOwnCardName() != null && !submission.getOwnCardName().equals( "" ) )
            {
              recognitionClaim.setOwnCardName( submission.getOwnCardName() );
            }

            scheduleRecognition( recognitionClaim, deliveryDate );
          }
          else
          {
            try
            {
              // save recognition claim if it is not a delayed recognition
              saveClaim( claim, submission.getClaimFormStepId(), null, false, true );
            }
            catch( Exception e )
            {
              logger.error( e );
              if ( e.getCause() instanceof BudgetUsageOverAllocallatedException )
              {
                BudgetUsageOverAllocallatedException budgetException = (BudgetUsageOverAllocallatedException)e.getCause();
                throw new BudgetUsageOverAllocallatedException( budgetException.getServiceErrors() );
              }
              else
              {
                throw new ServiceErrorException( e.getMessage() );
              }
            }
          }
        }
        catch( BudgetUsageOverAllocallatedException be )
        {
          logger.error( be );
          String errorMessage = getBudgetUsageOverAllocallatedExceptionMessage( be );
          throw new ServiceErrorExceptionWithRollback( errorMessage );
        }
        catch( ServiceErrorException see )
        {
          logger.error( see );
          throw new ServiceErrorExceptionWithRollback( ContentReaderManager.getText( "system.errors", "SYSTEM_EXCEPTION" ) );
        }
        catch( Exception e )
        {
          // validationErrors.add( new ServiceError( "key" , errorMsgHeader+recipientName ) ); //
          // commented because time zone id validation is not required , still G5 support team will
          // do research and put in a perm fix **//
          logger.error( e );
          if ( e.getCause() instanceof BudgetUsageOverAllocallatedException ) // gets here from
                                                                              // scheduled
                                                                              // recognition
          {
            BudgetUsageOverAllocallatedException budgetException = (BudgetUsageOverAllocallatedException)e.getCause();
            String errorMessage = getBudgetUsageOverAllocallatedExceptionMessage( budgetException );
            throw new ServiceErrorExceptionWithRollback( errorMessage );
          }
          else
          {
            throw new ServiceErrorExceptionWithRollback( e.getMessage() );
          }
        }
      }
      claimResponse.addErrors( validationErrors );
    }
    if ( !claimResponse.isSuccess() )
    {
      return claimResponse;
    }

    // recognition promotion claim is already getting saved if not a delayed recognition
    if ( promotion.isNominationPromotion() )
    {
    	// Client customization for WIP #39189 starts
        for ( AbstractRecognitionClaim claim : claims )
        {
      	  NominationClaim newClaim=(NominationClaim)claim;
          if ( claimFiles != null )
          {
            for ( TcccClaimFile claimFile : claimFiles )
            {
              ( (NominationClaim)newClaim ).addClaimFile( claimFile );
            }
          }
          
          
          String[] promoIds=systemVariableService.getPropertyByName( SystemVariableService.COKE_DIV_KEY_PROMO_IDS).getStringVal().split(",");
          boolean divison_promotion=false;
          for(String promoId:promoIds){
          if(promoId.equals(promotion.getId().toString())){
          	divison_promotion=true;    
          	 saveClaim( newClaim, submission.getClaimFormStepId(), null, false, true );
          }
          
           if(!divison_promotion){
         	 saveClaim( newClaim, submission.getClaimFormStepId(), null, false, true );
         }
         
        }
        // Client customization for WIP #39189 ends
        }
      try
      {
        saveClaims( claims, submission.getClaimFormStepId(), null, false );
      }
      catch( Exception e )
      {
        throw new ServiceErrorException( e.getMessage() );
    }
    }

    Iterator claimItr = claims.iterator();

    while ( claimItr.hasNext() )
    {
      AbstractRecognitionClaim abstractClaim = (AbstractRecognitionClaim)claimItr.next();
      claimResponse.setClaimId( abstractClaim.getId() );

      // save connection
      this.saveUserConnection( abstractClaim );
    }

    return claimResponse;
  }

  private String getBudgetUsageOverAllocallatedExceptionMessage( BudgetUsageOverAllocallatedException budgetException )
  {
    String errorMessage = "";
    Iterator errorsItr = budgetException.getServiceErrors().iterator();
    while ( errorsItr.hasNext() )
    {
      ServiceError serviceError = (ServiceError)errorsItr.next();
      errorMessage = CmsResourceBundle.getCmsBundle().getString( serviceError.getKey() );
      if ( StringUtils.isNotEmpty( serviceError.getArg1() ) )
      {
        errorMessage = errorMessage.replace( "{0}", serviceError.getArg1() );
      }
    }
    return errorMessage;
  }

  public void saveUserConnection( AbstractRecognitionClaim abstractClaim )
  {
    Set<ClaimRecipient> claimRecipients = abstractClaim.getClaimRecipients();
    if ( CollectionUtils.isNotEmpty( claimRecipients ) )
    {
      for ( ClaimRecipient claimRecipient : claimRecipients )
      {
        PublicRecognitionUserConnections publicRecognitionUserConnection = new PublicRecognitionUserConnections();
        Long submitterId = abstractClaim.getSubmitter().getId();
        publicRecognitionUserConnection.setSenderId( submitterId );
        publicRecognitionUserConnection.setReceiverId( claimRecipient.getRecipient().getId() );

        claimDAO.saveUserConnection( publicRecognitionUserConnection );
      }
    }
  }

  public CelebrationManagerMessage populateAndSendCelebrationManagerMessages( RecognitionPromotion promotion,
                                                                              RecognitionClaim claim,
                                                                              PurlRecipient purlRecipient,
                                                                              Participant recipient,
                                                                              Date sendDate )
  {
    CelebrationManagerMessage celebrationManagerMessage = null;
    celebrationManagerMessage = saveManagerMessage( promotion, claim, purlRecipient, recipient, sendDate );
    // send emails to managers
    if ( celebrationManagerMessage != null )
    {
      sendCelebrationManagersMailing( celebrationManagerMessage, purlRecipient );
    }
    return celebrationManagerMessage;
  }

  private void sendCelebrationManagersMailing( CelebrationManagerMessage celebrationManagerMessage, PurlRecipient purlRecipient )
  {
    Mailing mailing = mailingService.buildCelebrationManagersMailing( celebrationManagerMessage, purlRecipient );
    if ( null != mailing )
    {
      mailingService.submitMailing( mailing, null );
    }
  }

  private CelebrationManagerMessage saveManagerMessage( RecognitionPromotion promotion, RecognitionClaim claim, PurlRecipient purlRecipient, Participant recipient, Date sendDate )
  {
    CelebrationManagerMessage managerMessage = new CelebrationManagerMessage();
    managerMessage.setRecipient( recipient );
    managerMessage.setPromotion( promotion );

    Integer anniversaryNumberOfDays = 0;
    Integer anniversaryNumberOfYears = 0;
    Date msgCollectExpireDate = null;
    if ( promotion.isIncludePurl() )
    {
      anniversaryNumberOfDays = purlRecipient.getAnniversaryNumberOfDays();
      anniversaryNumberOfYears = purlRecipient.getAnniversaryNumberOfYears();
      msgCollectExpireDate = purlRecipient.getCloseDate();
    }
    else
    {
      anniversaryNumberOfDays = claim.getAnniversaryNumberOfDays();
      anniversaryNumberOfYears = claim.getAnniversaryNumberOfYears();
      // One day before delivery date or should it expire on delivery date?
      msgCollectExpireDate = getMsgCollectExpireDate( sendDate );
    }
    managerMessage.setAnniversaryNumberOfDays( anniversaryNumberOfDays );
    managerMessage.setAnniversaryNumberOfYears( anniversaryNumberOfYears );

    managerMessage.setMsgCollectExpireDate( msgCollectExpireDate );

    // manager
    User recipientManager = getManagerForCelebrationMessage( recipient );
    managerMessage.setManager( recipientManager );

    // manager above
    if ( recipientManager != null )
    {
      User recipientManagerAbove = getManagerAboveForCelebrationMessage( recipientManager );
      // Fix : checking recipient Manager Above should not be the Purl recipient
      if ( Objects.nonNull( recipientManagerAbove ) && !recipientManagerAbove.getId().equals( recipient.getId() ) )
      {
        managerMessage.setManagerAbove( recipientManagerAbove );
      }
    }

    CelebrationManagerMessage celebrationManagerMessage = celebrationService.saveCelebrationManagerMessage( managerMessage );
    return celebrationManagerMessage;
  }

  private Date getMsgCollectExpireDate( Date sendDate )
  {
    Date msgCollectExpireDate = new Date();
    Calendar cal = Calendar.getInstance();
    cal.setTime( sendDate );
    cal.add( Calendar.DATE, -1 );
    msgCollectExpireDate = cal.getTime();
    return msgCollectExpireDate;
  }

  private User getManagerForCelebrationMessage( Participant recipient )
  {
    UserNode userNode = userService.getPrimaryUserNode( recipient.getId() );
    User recipientManager = nodeService.getNodeOwnerForUser( recipient, userNode.getNode() );
    return recipientManager;
  }

  private User getManagerAboveForCelebrationMessage( User recipientManager )
  {
    User recipientManagerAbove = null;
    if ( recipientManager != null )
    {
      UserNode userNode = userService.getPrimaryUserNode( recipientManager.getId() );
      recipientManagerAbove = nodeService.getNodeOwnerForUser( recipientManager, userNode.getNode() );
    }
    return recipientManagerAbove;
  }

  private void savePurlStandardMessage( RecognitionPromotion promotion, PurlRecipient purlRecipient )
  {
    List<PurlContributorComment> comments = new LinkedList<PurlContributorComment>();
    PurlContributorComment contributorComment = new PurlContributorComment();
    contributorComment.setComments( purlRecipient.getPromotion().getPurlStandardMessage() );
    String videoUrl = createPurlStandardMessageVideoUrl( purlRecipient.getPromotion().getContentResourceCMCode() );
    if ( !StringUtil.isEmpty( videoUrl ) )
    {
      contributorComment.setVideoStatus( PurlContributorMediaStatus.lookup( PurlContributorMediaStatus.ACTIVE ) );
      contributorComment.setVideoType( PurlContributorVideoType.lookup( PurlContributorVideoType.DIRECT ) );
      contributorComment.setVideoUrl( videoUrl );
    }
    String imageUrl = createPurlStandardMessageImageUrl( purlRecipient.getPromotion().getContentResourceCMCode() );
    contributorComment.setImageUrl( imageUrl );
    contributorComment.setMediaState( PurlMediaState.lookup( PurlMediaState.POSTED ) );
    contributorComment.setImageStatus( PurlContributorMediaStatus.lookup( PurlContributorMediaStatus.ACTIVE ) );
    contributorComment.setStatus( PurlContributorCommentStatus.lookup( PurlContributorCommentStatus.ACTIVE ) );
    // setting default language type to admin PURL comment
    contributorComment.setCommentsLanguageType( LanguageType.lookup( systemVariableService.getDefaultLanguage().getStringVal() ) );
    comments.add( contributorComment );

    com.biperf.core.domain.purl.PurlContributor contributor = new com.biperf.core.domain.purl.PurlContributor();
    contributor.setFirstName( purlRecipient.getPromotion().getDefaultContributorName() );
    contributor.setLastName( " " );
    contributor.setPurlRecipient( purlRecipient );
    contributor.setState( PurlContributorState.lookup( PurlContributorState.CONTRIBUTION ) );
    contributor.setAvatarUrl( purlRecipient.getPromotion().getDefaultContributorAvatar() );
    contributor.setAvatarState( PurlMediaState.lookup( PurlMediaState.STAGED ) );
    contributor.setComments( comments );
    purlContributorDAO.save( contributor );
  }

  private String createPurlStandardMessageImageUrl( String contentResourceCMCode )
  {
    List<QuizLearningDetails> purlStandardMessagePictureObjectsDetails = promotionService.getPurlStandardMessagePictureObjects( contentResourceCMCode );
    Iterator pictureObjectsItr = purlStandardMessagePictureObjectsDetails.iterator();
    String imagePicUrl = "";
    String videoUrl = "";
    String leftColumn = "";
    String uploadType = "image";
    while ( pictureObjectsItr.hasNext() )
    {
      QuizLearningDetails pictureObjectsDetail = (QuizLearningDetails)pictureObjectsItr.next();
      leftColumn = pictureObjectsDetail.getLeftColumn();
      if ( leftColumn.contains( "<p>" ) )
      {
        uploadType = "image";
        String s = "<img src=\"";
        int ix = leftColumn.indexOf( s ) + s.length();
        imagePicUrl = leftColumn.substring( ix, leftColumn.indexOf( "\"", ix + 1 ) );
      }
    }
    return imagePicUrl;
  }

  private String createPurlStandardMessageVideoUrl( String contentResourceCMCode )
  {
    String videoUrl = "";
    List<QuizLearningDetails> purlStandardMessageVideoObjectsDetails = promotionService.getPurlStandardMessagePictureObjects( contentResourceCMCode );
    if ( purlStandardMessageVideoObjectsDetails != null )
    {
      Iterator videoObjectsItr = purlStandardMessageVideoObjectsDetails.iterator();
      while ( videoObjectsItr.hasNext() )
      {
        QuizLearningDetails videoObjectsDetail = (QuizLearningDetails)videoObjectsItr.next();
        videoUrl = videoObjectsDetail.getVideoUrlMp4();
        if ( !StringUtil.isEmpty( videoUrl ) )
        {
          videoUrl = videoUrl.replace( ".mp4", "" );
          videoUrl = videoUrl.replace( ".webm", "" );
          videoUrl = videoUrl.replace( ".ogg", "" );
          videoUrl = videoUrl.replace( ".3gp", "" );
        }
      }
    }
    return videoUrl;
  }

  @Override
  public List<RecognitionClaimRecipient> removeDuplicateRecipients( List<RecognitionClaimRecipient> recipients )
  {
    List<Long> recipientIds = new ArrayList<Long>();

    List<RecognitionClaimRecipient> validRecipients = new ArrayList<RecognitionClaimRecipient>();
    for ( RecognitionClaimRecipient recipient : recipients )
    {
      if ( recipientIds.contains( recipient.getUserId() ) )
      {
        // log it
        logger.error( "\n\n**********************************************" + "\nWARNING: Duplicate recognition recipient detected: userId=" + recipient.getUserId()
            + "\n**********************************************\n\n" );

        // just continue so the recipient isn't added to the list of valid recipients
        continue;
      }

      // recipient doens't already exist, so add its id to the list
      // and it to the list of valid recipients
      recipientIds.add( recipient.getUserId() );
      validRecipients.add( recipient );
    }

    return validRecipients;
  }

  private AbstractRecognitionClaim toClaimBasics( AbstractRecognitionPromotion promotion, RecognitionClaimSubmission submission )
  {
    AbstractRecognitionClaim newClaim;
    if ( promotion.isRecognitionPromotion() )
    {
      newClaim = new RecognitionClaim();
      RecognitionClaim recognitionClaim = (RecognitionClaim)newClaim;

      if ( promotion.isRecognitionPromotion() )
      {
        RecognitionPromotion rp = (RecognitionPromotion)promotion;
        if ( rp.isCopyRecipientManager() )
        {
          recognitionClaim.setCopyManager( rp.isCopyRecipientManager() );
        }
        else
        {
          recognitionClaim.setCopyManager( submission.isCopyManager() );
        }
      }

      if ( submission.getCertificateId() != null )
      {
        recognitionClaim.setCertificateId( submission.getCertificateId() );
      }
      recognitionClaim.setCopySender( submission.isCopySender() );
      if ( submission.getCopyOthers() != null )
      {
        String formattedEmailIds = submission.getCopyOthers().replaceAll( ", ", "," );
        recognitionClaim.setSendCopyToOthers( formattedEmailIds );
      }
      if ( StringUtils.isNotEmpty( submission.getCopyOthers() ) )
      {
        recognitionClaim.setCopyOthers( true );
      }

      recognitionClaim.setHidePublicRecognition( submission.isPrivateRecognition() );
      recognitionClaim.setAnniversaryNumberOfDays( submission.getAnniversaryDays() );
      recognitionClaim.setAnniversaryNumberOfYears( submission.getAnniversaryYears() );
      UploadResponse uploadResponse = null;

      if ( Objects.nonNull( submission.getVideoUrl() ) )
      {
        try
        {

          uploadResponse = getMtcService().uploadVideo( new URL( submission.getVideoUrl() ) );
        }
        catch( ServiceErrorException e )
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        catch( JSONException e )
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        catch( MalformedURLException e )
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        recognitionClaim.setCardVideoUrl( submission.getVideoUrl() + ActionConstants.REQUEST_ID + uploadResponse.getRequestId() );
        recognitionClaim.setCard( null );// ( ActionConstants.REQUEST_ID +
                                         // uploadResponse.getRequestId() );

        recognitionClaim.setCardVideoImageUrl( submission.getVideoImageUrl() + ActionConstants.REQUEST_ID + uploadResponse.getRequestId() );
      }
      else
      {
        recognitionClaim.setCardVideoUrl( submission.getVideoUrl() );
        recognitionClaim.setCardVideoImageUrl( submission.getVideoImageUrl() );

      }

    }
    else
    {
      newClaim = new NominationClaim();
      NominationClaim nominationClaim = (NominationClaim)newClaim;

      nominationClaim.setTeamName( submission.getTeamName() );
      nominationClaim.setHidePublicRecognition( submission.isPrivateRecognition() );
    }
    String timeZoneID = UserManager.getUser().getTimeZoneId();
    Date referenceDate = DateUtils.applyTimeZone( new Date(), timeZoneID );

    newClaim.setSubmissionDate( referenceDate );
    newClaim.setPromotion( promotion );
    newClaim.setOpen( true );

    if ( submission.getCardId() != null )
    {
      newClaim.setCard( multimediaService.getCardById( submission.getCardId() ) );
    }

    if ( !StringUtils.isEmpty( submission.getBehavior() ) )
    {
      PromotionBehaviorType promotionBehaviorType = null;
      if ( promotion.isNominationPromotion() )
      {
        promotionBehaviorType = PromoNominationBehaviorType.lookup( submission.getBehavior() );
      }
      else
      {
        promotionBehaviorType = PromoRecognitionBehaviorType.lookup( submission.getBehavior() );
      }
      newClaim.setBehavior( promotionBehaviorType );
    }

    if ( StringUtils.isEmpty( submission.getBehavior() ) && promotionService.isEasyPromotionWithBehaviors( promotion ) )
    {
      // set it to the "none" behavior
      newClaim.setBehavior( PromoRecognitionBehaviorType.getNoneItem() );
    }

    // prevent null being displayed in the recognition certificate PDF
    newClaim.setOwnCardName( submission.getOwnCardName() );
    newClaim.setNode( nodeDAO.getNodeById( submission.getNodeId() ) );
    newClaim.setSubmitter( participantService.getParticipantById( submission.getSubmitterId() ) );

    if ( StringUtils.isEmpty( submission.getComments() ) )
    {
      newClaim.setSubmitterComments( " " );
    }
    else
    {
      newClaim.setSubmitterComments( submission.getComments() );
    }
    newClaim.setSubmitterCommentsLanguageType( userService.getPreferredLanguageFor( newClaim.getSubmitter() ) );

    if ( submission.getProxyUserId() != null )
    {
      newClaim.setProxyUser( userService.getUserById( submission.getProxyUserId() ) );
    }

    for ( ClaimElement claimElement : submission.getClaimElements() )
    {
      // create a new ClaimElent based on this one; if we don't create this
      // copy, the claimElement will be shared among all Claims which will
      // cause TransientObjectExeptions later when we save
      ClaimElement newClaimElement = new ClaimElement();
      newClaimElement.setClaimFormStepElement( claimElement.getClaimFormStepElement() );
      newClaimElement.setValue( claimElement.getValue() );

      newClaim.addClaimElement( newClaimElement );
    }

    RecognitionClaimSource source = submission.getSource() != null ? submission.getSource() : RecognitionClaimSource.UNKNOWN;
    newClaim.setSource( source );

    return newClaim;
  }

  @Override
  public Set buildTeamMembers( RecognitionClaimSubmission submission )
  {
    Set<ProductClaimParticipant> teamMembers = new LinkedHashSet<ProductClaimParticipant>();

    for ( RecognitionClaimRecipient recognitionClaimRecipient : submission.getRecognitionClaimRecipients() )
    {
      ProductClaimParticipant teamMember = new ProductClaimParticipant();

      Participant participant = participantService.getParticipantById( recognitionClaimRecipient.getUserId() );
      teamMember.setParticipant( participant );

      Long nodeId = recognitionClaimRecipient.getNodeId();
      if ( nodeId != null )
      {
        Node node = nodeDAO.getNodeById( nodeId );
        teamMember.setNode( node );
      }
      /* coke customization start */
      boolean optedOut = participantService.isOptedOut( participant.getId() );
      if (!optedOut)
      {
        teamMember.setAwardQuantity( recognitionClaimRecipient.getAwardQuantity() );
      }
      teamMember.setOptOut( new Boolean(optedOut).toString() );
      /* coke customization end */
     // teamMember.setAwardQuantity( recognitionClaimRecipient.getAwardQuantity() );
      teamMembers.add( teamMember );
    }

    return teamMembers;
  }
   

  @Override
  public ClaimRecipient buildIndividualClaimRecipient( AbstractRecognitionPromotion promotion, RecognitionClaimRecipient recognitionClaimRecipient )
  {
    ClaimRecipient claimRecipient = new ClaimRecipient();

    Participant participant = participantService.getParticipantById( recognitionClaimRecipient.getUserId() );

    claimRecipient.setRecipient( participant );

    if ( recognitionClaimRecipient.getAwardQuantity() != null && !participant.getOptOutAwards() )
    {
      claimRecipient.setAwardQuantity( recognitionClaimRecipient.getAwardQuantity() );
    }
    else if ( recognitionClaimRecipient.getAwardQuantity() != null && participant.getOptOutAwards() )
    {
      claimRecipient.setAwardQuantity( 0L );
    }
    // Client customizations for WIP #42701 starts
    if ( promotion.getAdihCashOption() !=null && promotion.getAdihCashOption().booleanValue() )
    {      
      claimRecipient.setCustomCashAwardQuantity( recognitionClaimRecipient.getCashAwardQuantity() );
      claimRecipient.setCashCurrencyCode( recognitionClaimRecipient.getCashCurrencyCode() );
      claimRecipient.setCashPaxDivisionNumber( recognitionClaimRecipient.getCashPaxDivisionNumber() );
      Long usdCashAmt = TcccClientUtils.convertToUSD( getCokeClientService(), claimRecipient.getCustomCashAwardQuantity(), claimRecipient.getCashCurrencyCode() );
      Long points = TcccClientUtils.convertToPoints( getCokeClientService(), usdCashAmt, claimRecipient.getRecipient().getId() );
      if("USD".equalsIgnoreCase(recognitionClaimRecipient.getCashCurrencyCode() )){
    	  claimRecipient.setCustomCashAwardQuantity(usdCashAmt);
      } else{
    	  claimRecipient.setCustomCashAwardQuantity(points);
      }
    }
    // Client customizations for WIP #42701 ends
    
    if ( StringUtils.isNotBlank( recognitionClaimRecipient.getCalculatorScore() ) && StringUtils.isNumeric( recognitionClaimRecipient.getCalculatorScore() ) )
    {
      claimRecipient.setCalculatorScore( new Long( recognitionClaimRecipient.getCalculatorScore() ) );
    }
    claimRecipient.setPromoMerchCountry( promotion.getPromoMerchCountryForCountryCode( recognitionClaimRecipient.getCountryCode() ) );
    if ( recognitionClaimRecipient.getAwardLevelId() != null && recognitionClaimRecipient.getAwardLevelId().longValue() > 0 )
    {
      claimRecipient.setPromoMerchProgramLevel( merchLevelService.getPromoMerchProgramLevelById( recognitionClaimRecipient.getAwardLevelId() ) );
    }

    Long nodeId = recognitionClaimRecipient.getNodeId();
    if ( nodeId != null )
    {
      claimRecipient.setNode( nodeDAO.getNodeById( nodeId ) );
    }

    claimRecipient.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.PENDING ) );

    
    // Client customization for WIP 58122
    NominationPromotion nominationPromotion=null;
    if ( promotion instanceof NominationPromotion )
        nominationPromotion= (NominationPromotion)promotion;
    /* coke customization start */
    boolean optedOut = participantService.isOptedOut( recognitionClaimRecipient.getUserId() );
    if ( !optedOut && ( promotion.getAdihCashOption() == null || !promotion.getAdihCashOption() ) )
    {
          // Client customization for WIP 58122
        if(nominationPromotion!=null && nominationPromotion.isLevelPayoutByApproverAvailable())
        {
            claimRecipient.setAwardQuantity( new Long(0) );
        }
        else if ( recognitionClaimRecipient.getAwardQuantity() != null )
        {
            claimRecipient.setAwardQuantity( recognitionClaimRecipient.getAwardQuantity() );
        }
          // Client customization for WIP 58122
      }
      claimRecipient.setOptOut( new Boolean( optedOut ).toString() );
      /* coke customization end */
      

      if ( StringUtils.isNotBlank( recognitionClaimRecipient.getCalculatorScore() ) && StringUtils.isNumeric( recognitionClaimRecipient.getCalculatorScore() ) )
      {
        claimRecipient.setCalculatorScore( new Long( recognitionClaimRecipient.getCalculatorScore() ) );
      }
      claimRecipient.setPromoMerchCountry( promotion.getPromoMerchCountryForCountryCode( recognitionClaimRecipient.getCountryCode() ) );
      if ( recognitionClaimRecipient.getAwardLevelId() != null && recognitionClaimRecipient.getAwardLevelId().longValue() > 0 )
      {
        claimRecipient.setPromoMerchProgramLevel( merchLevelService.getPromoMerchProgramLevelById( recognitionClaimRecipient.getAwardLevelId() ) );
      }
      claimRecipient.setRecipient( participantService.getParticipantById( recognitionClaimRecipient.getUserId() ) );

      /*Long nodeId = recognitionClaimRecipient.getNodeId();
      if ( nodeId != null )
      {
        claimRecipient.setNode( nodeDAO.getNodeById( nodeId ) );
      }*/

      claimRecipient.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.PENDING ) );

      return claimRecipient;

  }

  private void createPurl( RecognitionClaimSubmission submission, RecognitionPromotion promotion ) throws ServiceErrorException
  {
    RecognitionClaimRecipient claimRecipient = submission.getRecognitionClaimRecipients().get( 0 );
    PurlRecipient info = buildPurlRecipientInfo( submission, claimRecipient );

    if ( promotion.isIncludeCelebrations() && promotion.isAllowOwnerMessage() )
    {
      Participant recipient = participantService.getParticipantById( claimRecipient.getUserId() );
      CelebrationManagerMessage celebrationManagerMessage = populateAndSendCelebrationManagerMessages( promotion, null, info, recipient, null );
      info.setCelebrationManagerMessage( celebrationManagerMessage );
    }
    PurlRecipient purlRecipient = getPurlService().createPurlRecipient( info );

    List<PurlContributor> inviteList = submission.getPurlContributors();

    // Add Submitter, if NOT already on the invite list
    Participant submitter = participantService.getParticipantById( submission.getSubmitterId() );
    if ( null != submitter && !isPaxInSelectedContributors( submitter, inviteList ) )
    {
      PurlContributor inviteContributor = new PurlContributor( submitter.getId().toString(), submitter.getFirstName(), submitter.getLastName(), submitter.getAvatarSmall(), null, null, true );
      inviteList.add( inviteContributor );
    }

    // Add Node Owner, if NOT already on the invite list
    Participant nodeOwner = getPurlService().getNodeOwnerForPurlRecipient( purlRecipient.getId() );
    if ( null != nodeOwner && !isPaxInSelectedContributors( nodeOwner, inviteList ) )
    {
      PurlContributor owner = new PurlContributor( nodeOwner.getId().toString(), nodeOwner.getFirstName(), nodeOwner.getLastName(), nodeOwner.getAvatarSmall(), null, null, true );
      inviteList.add( owner );
    }

    if ( promotion.isPurlStandardMessageEnabled() )
    {
      savePurlStandardMessage( promotion, purlRecipient );
    }

    Process process = processService.createOrLoadSystemProcess( PurlSubmissionProcess.PROCESS_NAME, PurlSubmissionProcess.BEAN_NAME );

    String purlContributors = "";
    if ( !inviteList.isEmpty() && inviteList.size() > 0 )
    {
      for ( int i = 0; i < inviteList.size(); )
      {
        PurlContributor purlContributor = (PurlContributor)inviteList.get( i );
        purlContributors += purlContributor.toProcessString() + "|";
        i++;
      }
    }

    LinkedHashMap parameterValueMap = new LinkedHashMap();

    parameterValueMap.put( "purlContributors", new String[] { purlContributors } );
    parameterValueMap.put( "submitterId", new String[] { submission.getSubmitterId().toString() } );
    parameterValueMap.put( "purlRecipientId", new String[] { purlRecipient.getId().toString() } );

    ProcessSchedule processSchedule = new ProcessSchedule();
    processSchedule.setStartDate( new Date() );
    processSchedule.setTimeOfDayMillis( new Long( 0 ) );
    processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.ONE_TIME_ONLY ) );

    processService.scheduleProcess( process, processSchedule, parameterValueMap, submitter != null ? submitter.getId() : null );

  }

  private boolean isPaxInSelectedContributors( final Participant pax, final List<PurlContributor> inviteList )
  {
    if ( null != pax && null != inviteList )
    {
      for ( PurlContributor invite : inviteList )
      {
        if ( !StringUtil.isEmpty( invite.getUserId() ) )
        {
          try
          {
            long inviteUserId = Long.valueOf( invite.getUserId() );
            if ( inviteUserId == pax.getId().longValue() )
            {
              return true;
            }
          }
          catch( Exception e )
          {
          }
        }
      }
    }
    return false;
  }

  private PurlRecipient buildPurlRecipientInfo( RecognitionClaimSubmission submission, RecognitionClaimRecipient purlRecipient )
  {
    PurlRecipient info = new PurlRecipient();

    // Set the participant
    info.setUser( participantService.getParticipantById( purlRecipient.getUserId() ) );
    info.setNode( nodeDAO.getNodeById( purlRecipient.getNodeId() ) );

    // Set the submitter details
    Participant submitter = participantService.getParticipantById( submission.getSubmitterId() );
    info.setSubmitter( submitter );
    info.setSubmitterNode( nodeDAO.getNodeById( submission.getNodeId() ) );

    // Set Proxy details
    if ( null != submission.getProxyUserId() )
    {
      User proxyUser = userService.getUserById( submission.getProxyUserId() );
      info.setProxyUser( proxyUser );
    }

    // Set the promotion
    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_MERCHANDISE_COUNTRIES ) );
    AbstractRecognitionPromotion promotion = (AbstractRecognitionPromotion)promotionService.getPromotionByIdWithAssociations( submission.getPromotionId(), promoAssociationRequestCollection );
    RecognitionPromotion recognitionPromotion = (RecognitionPromotion)promotion;
    info.setPromotion( recognitionPromotion );

    // Set the invitation date, default to create date
    info.setInvitationStartDate( new Date() );
    info.setState( PurlRecipientState.lookup( PurlRecipientState.INVITATION ) );

    // Set the award date
    info.setAwardDate( DateUtils.toDate( submission.getRecipientSendDate() ) );

    // Default preload contributors
    info.setShowDefaultContributors( false );

    if ( promotion.isAwardActive() )
    {
      if ( promotion.getAwardType().isMerchandiseAwardType() && promotion.getAwardStructure().equals( RecognitionPromotion.AWARD_STRUCTURE_LEVEL ) )
      {
        if ( purlRecipient.getAwardLevelId() != null && purlRecipient.getAwardLevelId().longValue() > 0 )
        {
          info.setAwardLevel( merchLevelService.getPromoMerchProgramLevelById( purlRecipient.getAwardLevelId() ) );
        }
      }
      else
      {
        if ( purlRecipient.getAwardQuantity() != null )
        {
          info.setAwardAmount( BigDecimal.valueOf( purlRecipient.getAwardQuantity() ) );
        }

      }
    }

    if ( recognitionPromotion.isIncludeCelebrations() )
    {
      info.setAnniversaryNumberOfDays( submission.getAnniversaryDays() );
      info.setAnniversaryNumberOfYears( submission.getAnniversaryYears() );
    }

    for ( ClaimElement claimElement : submission.getClaimElements() )
    {
      PurlRecipientCustomElement customElement = new PurlRecipientCustomElement();
      customElement.setClaimFormStepElement( claimElement.getClaimFormStepElement() );
      customElement.setValue( claimElement.getValue() );
      info.addCustomElement( customElement );
    }

    return info;
  }

  public RecognitionClaimSubmissionResponse validate( RecognitionClaimSubmission submission ) throws ServiceErrorException
  {
    List<ServiceError> validationErrors = new ArrayList<ServiceError>();
    RecognitionClaimSubmissionResponse claimResponse = new RecognitionClaimSubmissionResponse();

    if ( submission.getRecognitionClaimRecipients() == null || submission.getRecognitionClaimRecipients().size() == 0 )
    {
      validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.select.recipients", "NO_RECIPIENTS" ) ) );
    }

    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_BUDGET_MASTER ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CLAIM_FORM_DEFINITION ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CLAIM_FORM_STEP_ELEMENT_VALIDATION ) );

    AbstractRecognitionPromotion promotion = (AbstractRecognitionPromotion)promotionService.getPromotionByIdWithAssociations( submission.getPromotionId(), promoAssociationRequestCollection );

    for ( RecognitionClaimRecipient recipient : submission.getRecognitionClaimRecipients() )
    {
      boolean isEligible = audienceService.isParticipantInSecondaryAudience( promotion, participantService.getParticipantById( recipient.getUserId() ) );
      if ( !isEligible || ( promotion.isRecognitionPromotion() && ! ( (RecognitionPromotion)promotion ).isSelfRecognitionEnabled() && recipient.getUserId().equals( UserManager.getUserId() ) )
          || ( promotion.isNominationPromotion() && ! ( (NominationPromotion)promotion ).isSelfNomination() && recipient.getUserId().equals( UserManager.getUserId() ) ) )
      {
        validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.select.recipients", "INELIGIBLE_ERROR" ) ) );
        break;
      }
    }

    if ( promotion.isRecognitionPromotion() && ( (RecognitionPromotion)promotion ).isIncludePurl() )
    {
      validateRecognitionWhenIncludePurlExist( submission, validationErrors, promotion );
    }
    else
    {
      validateRecognitionWhenIncludePurlNotExist( submission, validationErrors, promotion );
    }
    if ( promotion instanceof NominationPromotion )
    {
      NominationPromotion nominationPromotion = (NominationPromotion)promotion;
     

      // Client customization for WIP #59420 start
      if ( nominationPromotion.isEnableFileUpload() )
      {
        if ( ! ( nominationPromotion.getFileMinNumber().intValue() <= submission.getClaimUploads().size()
            && nominationPromotion.getFileMaxNumber().intValue() >= submission.getClaimUploads().size() ) )
        {
          String fileTypes = StringUtil.isNullOrEmpty( nominationPromotion.getAllowedFileTypes() )
              ? systemVariableService.getPropertyByName( SystemVariableService.COKE_UPLOAD_FILE_TYPES ).getStringVal()
              : nominationPromotion.getAllowedFileTypes();
          validationErrors.add( new ServiceError( "client.nominationSubmit.INVALID_FILES_CNT",
                                                  String.valueOf( nominationPromotion.getFileMinNumber() ),
                                                  String.valueOf( nominationPromotion.getFileMaxNumber() ),
                                                  fileTypes ) );
        }
      }
      // Client customization for WIP #59420 end
      
      if ( StringUtils.isBlank( submission.getComments() ) && !nominationPromotion.isHideCommentsOnNomination() )
      {
        validationErrors.add( new ServiceError( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, ContentReaderManager.getText( "recognition.select.recipients", "COMMENTS" ) ) );
      }
    }
    claimResponse.addErrors( validationErrors );
    return claimResponse;
  }

  private void validateRecognitionWhenIncludePurlNotExist( RecognitionClaimSubmission submission, List<ServiceError> validationErrors, AbstractRecognitionPromotion promotion )
  {
    Budget submitterBudget = null;
    if ( promotion.getBudgetMaster() != null && promotion.getBudgetMaster().isActive() )
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
      BudgetMaster budgetMaster = budgetMasterService.getBudgetMasterById( promotion.getBudgetMaster().getId(), associationRequestCollection );

      String paxTimeZoneId = userService.getUserTimeZoneByUserCountry( submission.getSubmitterId() );
      BudgetSegment currentBudgetSegment = budgetMaster.getCurrentBudgetSegment( paxTimeZoneId );

      if ( promotion.getBudgetMaster().getBudgetType().getCode().equals( BudgetType.PAX_BUDGET_TYPE ) )
      {
        List<Budget> budgetSet = null;
        if ( currentBudgetSegment != null )
        {
          budgetSet = budgetMasterDAO.getAllActiveInBudgetSegmentForUser( currentBudgetSegment.getId(), submission.getSubmitterId() );
        }
        if ( budgetSet != null && budgetSet.size() > 0 )
        {
          for ( Budget budget : budgetSet )
          {
            if ( budget.getUser().getId().longValue() == submission.getSubmitterId().longValue() )
            {
              submitterBudget = budget;
              if ( budget.getBudgetSegment().getEndDate() != null && truncTimeFromCurrentDate().after( budget.getBudgetSegment().getEndDate() ) )
              {
                validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.select.recipients", "BUDGET_DATE_END" ) ) );
              }
              if ( budget.getBudgetSegment().getStartDate() != null && budget.getBudgetSegment().getStartDate().after( truncTimeFromCurrentDate() ) )
              {
                validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.select.recipients", "BUDGET_DATE_START" ) ) );
              }
              if ( budget.getStatus().getCode().equals( BudgetStatusType.CLOSED ) || budget.getStatus().getCode().equals( BudgetStatusType.SUSPENDED ) )
              {
                validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.select.recipients", "BUDGET_PAX_CLOSED" ) ) );
              }
              break;
            }
          }
        }
        else
        {
          validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.select.recipients", "BUDGET_PAX_CLOSED" ) ) );
        }
      }
      else if ( promotion.getBudgetMaster().getBudgetType().getCode().equals( BudgetType.NODE_BUDGET_TYPE ) )
      {
        List<Budget> budgetSet = null;
        if ( currentBudgetSegment != null )
        {
          budgetSet = budgetMasterDAO.getAllActiveInBudgetSegmentForUserNode( currentBudgetSegment.getId(), submission.getSubmitterId() );
          
          // Client customization start wip# 25589 start (Taken from G5 code)
          if ( promotion.isUtilizeParentBudgets() )
          {
            Long nodeId = submission.getNodeId();
            if ( budgetSet == null || budgetSet.size() == 0 )
            {
              try
              {
                do
                {
                  nodeId = ( nodeService.getNodeById( nodeId ) ).getParentNode().getId();
                  budgetSet = budgetMasterDAO.getAllActiveInBudgetSegmentForParentNode( currentBudgetSegment.getId(), nodeId );
                }
                while ( budgetSet == null || budgetSet.size() == 0 );
              }
              catch( NullPointerException npe )// To handle scenario when parent node is null
              {
                budgetSet = null;
              }

            }
          }
          // Client customization wip# 25589 end
        }

        if ( budgetSet != null && budgetSet.size() > 0 )
        {
          for ( Budget budget : budgetSet )
          {
            if ( budget.getNode().getId().longValue() == submission.getNodeId().longValue() )
            {
              submitterBudget = budget;
              if ( budget.getBudgetSegment().getEndDate() != null && truncTimeFromCurrentDate().after( budget.getBudgetSegment().getEndDate() ) )
              {
                validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.select.recipients", "BUDGET_DATE_END" ) ) );
              }
              if ( budget.getBudgetSegment().getStartDate() != null && budget.getBudgetSegment().getStartDate().after( truncTimeFromCurrentDate() ) )
              {
                validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.select.recipients", "BUDGET_DATE_START" ) ) );
              }
              if ( budget.getStatus().getCode().equals( BudgetStatusType.CLOSED ) || budget.getStatus().getCode().equals( BudgetStatusType.SUSPENDED ) )
              {
                validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.select.recipients", "BUDGET_PAX_CLOSED" ) ) );
              }
              break;
            }
          }
        }
        else
        {
          validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.select.recipients", "BUDGET_PAX_CLOSED" ) ) );
        }
      }
      else if ( promotion.getBudgetMaster().getBudgetType().getCode().equals( BudgetType.CENTRAL_BUDGET_TYPE ) )
      {
        Set<Budget> budgetSet = null;
        if ( currentBudgetSegment != null )
        {
          budgetSet = currentBudgetSegment.getBudgets();
        }
        if ( budgetSet != null && budgetSet.size() > 0 )
        {
          Iterator iter = budgetSet.iterator();
          if ( iter.hasNext() )
          {
            Budget budget = (Budget)iter.next();
            submitterBudget = budget;
            if ( budget.getBudgetSegment().getEndDate() != null && truncTimeFromCurrentDate().after( budget.getBudgetSegment().getEndDate() ) )
            {
              validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.select.recipients", "BUDGET_DATE_END" ) ) );
            }
            if ( budget.getBudgetSegment().getStartDate() != null && budget.getBudgetSegment().getStartDate().after( truncTimeFromCurrentDate() ) )
            {
              validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.select.recipients", "BUDGET_DATE_START" ) ) );
            }
          }
        }
      }
    }

    if ( !submission.isManagerAward() && promotion.isBehaviorActive() && StringUtils.isBlank( submission.getBehavior() ) )
    {
      if ( promotion.isNominationPromotion() || promotion.isRecognitionPromotion() && ( (RecognitionPromotion)promotion ).isBehaviorRequired() )
      {
        validationErrors.add( new ServiceError( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, ContentReaderManager.getText( "recognition.select.recipients", "BEHAVIOR" ) ) );
      }
    }

    if ( StringUtils.isNotBlank( submission.getCopyOthers() ) )
    {
      EmailValidator emailValidator = EmailValidator.getInstance();
      for ( String emailAddress : submission.getCopyOthers().split( "[,;]" ) )
      {
        if ( !emailValidator.isValid( emailAddress.trim() ) )
        {
          validationErrors.add( new ServiceError( ContentReaderManager.getText( "help.contact.us", "EMAIL_INVALID" ) ) );
        }
      }
    }

    if ( promotion instanceof NominationPromotion )
    {
      NominationPromotion nominationPromotion = (NominationPromotion)promotion;

      if ( StringUtils.isBlank( submission.getComments() ) )
      {
        validationErrors.add( new ServiceError( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, ContentReaderManager.getText( "recognition.select.recipients", "COMMENTS" ) ) );
      }

      NominationAwardGroupType awardGroupType = nominationPromotion.getAwardGroupType();
      NominationAwardGroupSizeType awardGroupSizeType = nominationPromotion.getAwardGroupSizeType();

      if ( ( awardGroupType.isTeam() || awardGroupType.isIndividualOrTeam() ) && ( awardGroupSizeType.isLimited() || awardGroupSizeType.isUnlimited() ) )
      {
        // The submitter must specify a team name when entering a claim.
        if ( submission.getTeamName() == null || submission.getTeamName().length() == 0 )
        {
          validationErrors.add( new ServiceError( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, ContentReaderManager.getText( "nomination.select.recipients", "TEAM_NAME" ) ) );
        }
      }

      if ( ( awardGroupType.isTeam() || awardGroupType.isIndividualOrTeam() ) && awardGroupSizeType.isLimited() )
      {
        // The team size must be less than or equal to the maximum team size.
        Integer maxGroupMembers = ( (NominationPromotion)promotion ).getMaxGroupMembers();
        // Bug # 34572 fix
        if ( submission.getRecognitionClaimRecipients() != null )
        {
          if ( submission.getRecognitionClaimRecipients().size() > maxGroupMembers.intValue() )
          {
            validationErrors.add( new ServiceError( ServiceErrorMessageKeys.SYSTEM_ERROR_MAXCOUNT,
                                                    ContentReaderManager.getText( "nomination.select.recipients", "SELECTEDRECIPIENTS" ),
                                                    maxGroupMembers.toString() ) );
          }
        }
      }
    }

    if ( promotion.isClaimFormUsed() )
    {
      AbstractRecognitionClaim claim;
      if ( promotion.isRecognitionPromotion() )
      {
        claim = new RecognitionClaim();
      }
      else
      {
        claim = new NominationClaim();
      }
      claim.setPromotion( promotion );
      for ( ClaimElement claimElement : submission.getClaimElements() )
      {
        claim.addClaimElement( claimElement );
      }
      validationErrors.addAll( validateClaimElements( claim, 0 ) );
    }

    if ( promotion.isAwardActive() )
    {
      double totalAwards = 0;
      double awardQuantity = 0;
      boolean levelPayout=false;
      if(promotion.isNominationPromotion())
      {
        NominationPromotion promo=(NominationPromotion)promotion;
        if(promo.isLevelPayoutByApproverAvailable())
            levelPayout=true;
      }
      if ( promotion.isRecognitionPromotion() || promotion.isNominationPromotion() )
      {
        for ( RecognitionClaimRecipient recognitionClaimRecipient : submission.getRecognitionClaimRecipients() )
        {
          Participant participant = participantService.getParticipantById( recognitionClaimRecipient.getUserId() );
          String userName = participant.getUserName();
          String firstName = participant.getFirstName();
          String lastName = participant.getLastName();

          if ( promotion.getAwardType().isMerchandiseAwardType() )
          {
            if ( promotion.getScoreBy() == null || promotion.getScoreBy().isScoreByGiver() )
            {
              if ( recognitionClaimRecipient.getAwardLevelId() == null || recognitionClaimRecipient.getAwardLevelId().intValue() == 0 )
              {
                if ( promotion.getCalculator() == null )
                {
                  validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.select.awards", "LEVEL_NOT_SELECTED_RECIPIENT " + userName ) ) );
                }
              }
              else if ( promotion.isBudgetUsed() )
              {
                PromoMerchProgramLevel currentProgramLevel = merchLevelService.getPromoMerchProgramLevelById( new Long( recognitionClaimRecipient.getAwardLevelId() ) );
                MerchLevel omLevel = merchLevelService.getMerchLevelData( currentProgramLevel, true, false );
                awardQuantity = new Long( omLevel.getMaxValue() );
              }
            }
          }
          else if ( !promotion.isAwardAmountTypeFixed() && promotion.getCalculator() == null )
          {
            if ( null == recognitionClaimRecipient.getAwardQuantity() )
            {
              if ( !participant.getOptOutAwards() )
              if ( !participant.getOptOutAwards()&& !levelPayout)
              {
                validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.purl.submit", "ERROR_NO_AWARD_AMOUNT_RECIPIENT " + userName ) ) );
              }
            }  // Client customizations for wip #42701 starts
            else if ( !promotion.isAwardAmountTypeFixed() && promotion.getCalculator() == null )
            {
              /* coke customization start */
            if ( null != recognitionClaimRecipient){
            	if ( null != recognitionClaimRecipient.getIsOptOut() && !recognitionClaimRecipient.getIsOptOut().booleanValue() && !levelPayout)
              {
                /* coke customization end */
                if ( null == recognitionClaimRecipient.getAwardQuantity() )
                {
                  validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.purl.submit", "ERROR_NO_AWARD_AMOUNT" ) ) );
                }
                // Client customizations for wip #42701 starts
                else if ( promotion.getAdihCashOption() )
                {
                  if ( StringUtil.isNullOrEmpty( recognitionClaimRecipient.getCashCurrencyCode() ) )
                  {
                    validationErrors.add( new ServiceError( ContentReaderManager.getText( "coke.cash.recognition", "UNKNOWN_CURR" ) ) );
                  }
                  else
                  {
                    Long maxAllowed = TcccClientUtils.getMaxAllowedAward( cokeClientDAO, promotion.getAdihCashMaxAward(), recognitionClaimRecipient.getCashCurrencyCode() );
                    if ( recognitionClaimRecipient.getAwardQuantity().longValue() < 1 || recognitionClaimRecipient.getAwardQuantity().longValue() > maxAllowed )
                    {
                      validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.purl.submit", "ERROR_AWARD_AMOUNT_NOT_IN_RANGE" ),
                                                              recognitionClaimRecipient.getAwardQuantity().toString(),
                                                              String.valueOf( 1 ),
                                                              maxAllowed.toString() ) );
                    }
                  }
                }
                // Client customizations for wip #42701 ends
                else if ( recognitionClaimRecipient.getAwardQuantity().longValue() < promotion.getAwardAmountMin().longValue()
                    || recognitionClaimRecipient.getAwardQuantity().longValue() > promotion.getAwardAmountMax().longValue() )
                {
                  validationErrors
                      .add( new ServiceError( ContentReaderManager.getText( "recognition.purl.submit", "ERROR_AWARD_AMOUNT_NOT_IN_RANGE" ),
                                              recognitionClaimRecipient.getAwardQuantity().toString(),
                                              promotion.getAwardAmountMin().toString(),
                                              promotion.getAwardAmountMax().toString() ) );
                }
                awardQuantity = recognitionClaimRecipient.getAwardQuantity();
              } // coke customization
              else if(levelPayout)
              {
            	  awardQuantity=0;
              }
            	  
            }
            // Client customization for WIP 58122
            else if ( recognitionClaimRecipient.getAwardQuantity().longValue() < promotion.getAwardAmountMin().longValue()
                || recognitionClaimRecipient.getAwardQuantity().longValue() > promotion.getAwardAmountMax().longValue() )
            {
              validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.purl.submit", "ERROR_AWARD_AMT_NOT_IN_RANGE_WR" ) + String.join( " ", firstName, lastName ),
                                                      recognitionClaimRecipient.getAwardQuantity().toString(),
                                                      promotion.getAwardAmountMin().toString(),
                                                      promotion.getAwardAmountMax().toString() ) );
            }

            if ( null != recognitionClaimRecipient.getAwardQuantity() )
            {
              awardQuantity = recognitionClaimRecipient.getAwardQuantity();
            }
            else if(levelPayout)
            {
              awardQuantity=0;
            }
          }
        }
          // Client customization for WIP 58122
          else if ( promotion.isNominationPromotion())
          {
            NominationPromotion promo=(NominationPromotion)promotion;
            if(promo.isLevelPayoutByApproverAvailable())
                awardQuantity = 0;
            else if ( promotion.isAwardAmountTypeFixed() )
                awardQuantity = promotion.getAwardAmountFixed();
          }
          else if ( promotion.isAwardAmountTypeFixed() )
          {
            awardQuantity = promotion.getAwardAmountFixed();
          }

          // Calculator Validations
          if ( promotion.getCalculator() != null && promotion.getScoreBy().isScoreByGiver() )
          {
            // Bug 4673 - Should not display error when editing
            if ( StringUtils.isEmpty( recognitionClaimRecipient.getCalculatorScore() )
                && ( recognitionClaimRecipient.getAwardQuantity() == null || recognitionClaimRecipient.getAwardQuantity() == 0 ) )
            {
              if ( !participant.getOptOutAwards() )
              {
                validationErrors.add( new ServiceError( ContentReaderManager.getText( "calculator.payouts", "CALCULATOR_ALERT_MESSAGE_RECIPIENT " + userName ) ) );
              }
            }
            else if ( promotion.getCalculator().getCalculatorAwardType().isRangeAward() )
            {
              if ( null == recognitionClaimRecipient.getAwardQuantity() )
              {
                if ( !participant.getOptOutAwards() )
                {
                  validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.purl.submit", "ERROR_NO_AWARD_AMOUNT_RECIPIENT " + userName ) ) );
                }
              }
              else
              {
                awardQuantity = recognitionClaimRecipient.getAwardQuantity();
                CalculatorPayout payout = calculatorService.getCalculatorPayoutByScore( promotion.getCalculator().getId(), Integer.parseInt( recognitionClaimRecipient.getCalculatorScore() ) );

                if ( payout != null
                    && ( recognitionClaimRecipient.getAwardQuantity().intValue() < payout.getLowAward() || recognitionClaimRecipient.getAwardQuantity().intValue() > payout.getHighAward() ) )
                {
                  validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.purl.submit", "ERROR_AWARD_AMOUNT_NOT_IN_RANGE" ),
                                                          recognitionClaimRecipient.getAwardQuantity().toString(),
                                                          String.valueOf( payout.getLowAward() ),
                                                          String.valueOf( payout.getHighAward() ) ) );
                }
              }
            }
          }
          if ( promotion.getAwardType().isPointsAwardType() )
          {
            BigDecimal usMediaValue = countryService.getBudgetMediaValueByCountryCode( Country.UNITED_STATES );
            BigDecimal recipientMediaValue = participant.getPrimaryAddress().getAddress().getCountry().getBudgetMediaValue();
            awardQuantity = ( BudgetUtils.applyMediaConversion( BigDecimal.valueOf( awardQuantity ), recipientMediaValue, usMediaValue ) ).doubleValue();
          }
          totalAwards += awardQuantity;
        }

        Double roundedTotalAwards = new Double( Math.ceil( totalAwards ) );

        if ( promotion.isBudgetUsed() && submitterBudget != null )
        {
          if ( promotion.getBudgetMaster().getOverrideableType() != null && submitterBudget.getCurrentValue().intValue() < roundedTotalAwards.intValue() )
          {
            if ( promotion.getBudgetMaster().getOverrideableType().isHardCap() )
            {
              if ( promotion.isRecognitionPromotion() )
              {
                validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.select.recipients", "HARD_BUDGET_OVERDRAFT" ) ) );
              }
              else
              {
                validationErrors.add( new ServiceError( ContentReaderManager.getText( "nomination.select.recipients", "HARD_BUDGET_OVERDRAFT" ) ) );
              }
            }
          }
        }
      }
    }

    if ( promotion.isRecognitionPromotion() && !StringUtil.isEmpty( submission.getRecipientSendDate() ) )
    {
      RecognitionPromotion recognitionPromo = (RecognitionPromotion)promotion;
      try
      {
        if ( StringUtils.isNotEmpty( submission.getRecipientSendDate() ) )
        {
          String pattern = DateFormatterUtil.getDatePattern( UserManager.getLocale() );
          SimpleDateFormat sdf = new SimpleDateFormat( pattern );
          Date sendDate = null;
          sendDate = sdf.parse( submission.getRecipientSendDate().trim() );

          if ( promotion.getSubmissionEndDate() != null && promotion.getSubmissionEndDate().before( sendDate ) )
          {
            validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.select.recipients", "DELIVERY_DATE_AFTER_END_DATE" ) ) );
          }
          else if ( DateUtils.getElapsedDays( DateUtils.toStartDate( DateUtils.getCurrentDate() ), sendDate ) > recognitionPromo.getMaxDaysDelayed().intValue() )
          {
            validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.select.recipients", "DELAY_RANGE_INVALID" ), recognitionPromo.getMaxDaysDelayed().toString() ) );
          }
        }
      }

      catch( ParseException e )
      {
        validationErrors.add( new ServiceError( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, ContentReaderManager.getText( "recognition.select.recipients", "DELIVERY_DATE" ) ) );
      }
    }
  }

  private void validateRecognitionWhenIncludePurlExist( RecognitionClaimSubmission submission, List<ServiceError> validationErrors, AbstractRecognitionPromotion promotion )
  {
    if ( submission.getRecognitionClaimRecipients() != null && submission.getRecognitionClaimRecipients().size() > 1 )
    {
      validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.purl.submit", "ONE_RECIPIENT_ERROR" ) ) );
    }
    else
    {
      RecognitionClaimRecipient purlRecipient = submission.getRecognitionClaimRecipients().get( 0 );

      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.ADDRESSES ) );
      Participant participant = participantService.getParticipantByIdWithAssociations( purlRecipient.getUserId(), associationRequestCollection );

      String recipientName = participant.getFirstName() + " " + participant.getLastName();

      UserAddress userAddress = participant.getPrimaryAddress();
      if ( !userAddress.getAddress().getCountry().getStatus().isActive() )
      {
        validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.purl.submit", "RECIPIENT_NOT_IN_ACTIVE_COUNTRY" ) ) );
      }

      if ( null == purlRecipient.getNodeId() )
      {
        validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.purl.submit", "ERROR_NO_RECIPIENT_NODE" ) ) );
      }

      if ( StringUtils.isEmpty( submission.getRecipientSendDate() ) )
      {
        validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.purl.submit", "ERROR_NO_AWARD_DATE" ) ) );
      }
      else
      {
        try
        {
          String pattern = DateFormatterUtil.getDatePattern( UserManager.getLocale() );
          SimpleDateFormat sdf = new SimpleDateFormat( pattern );
          Date sendDate = null;
          sendDate = sdf.parse( submission.getRecipientSendDate().trim() );

          if ( sendDate.equals( DateUtils.toStartDate( DateUtils.getCurrentDate() ) ) || sendDate.before( DateUtils.toStartDate( DateUtils.getCurrentDate() ) ) )
          {
            validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.purl.submit", "ERROR_INVALID_AWARD_DATE" ) ) );
          }
          else if ( promotion.getSubmissionEndDate() != null && promotion.getSubmissionEndDate().before( sendDate ) )
          {
            validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.purl.submit", "PURL_RECIPIENT_DATE_INVALID" ) + " "
                + DateUtils.toDisplayString( promotion.getSubmissionEndDate() ) + '.' ) );
          }
        }

        catch( ParseException e )
        {
          validationErrors.add( new ServiceError( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, ContentReaderManager.getText( "recognitionSubmit.delivery.purl", "SET_DATE" ) ) );
        }

      }

      if ( promotion.isAwardActive() )
      {
        if ( promotion.getAwardType().isMerchandiseAwardType() )
        {
          if ( null == purlRecipient.getAwardLevelId() )
          {
            validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.purl.submit", "ERROR_NO_AWARD_LEVEL" ) ) );
          }
        }
        else if ( !promotion.isAwardAmountTypeFixed() )
        {
          if ( null == purlRecipient.getAwardQuantity() )
          {
            if ( !participant.getOptOutAwards() )
            {
              validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.purl.submit", "ERROR_NO_AWARD_AMOUNT" ) ) );
            }
          }
          else if ( purlRecipient.getAwardQuantity().longValue() < promotion.getAwardAmountMin().longValue()
              || purlRecipient.getAwardQuantity().longValue() > promotion.getAwardAmountMax().longValue() )
          {
            validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.purl.submit", "ERROR_AWARD_AMOUNT_NOT_IN_RANGE" ),
                                                    purlRecipient.getAwardQuantity().toString(),
                                                    promotion.getAwardAmountMin().toString(),
                                                    promotion.getAwardAmountMax().toString() ) );
          }
        }
        else if ( promotion.isAwardAmountTypeFixed() )
        {
          purlRecipient.setAwardQuantity( promotion.getAwardAmountFixed() );
        }

      }

      if ( submission.getPurlContributors().isEmpty() )
      {
        validationErrors.add( new ServiceError( ContentReaderManager.getText( "recognition.purl.submit", "ERROR_NO_CONTRIBUTORS" ) ) );
      }

      if ( ( (RecognitionPromotion)promotion ).isIncludeCelebrations() )
      {
        for ( ClaimElement claimElement : submission.getClaimElements() )
        {

          if ( claimElement.getClaimFormStepElement().getClaimFormElementType().isTextBoxField() || claimElement.getClaimFormStepElement().getClaimFormElementType().isTextField() )
          {

            if ( claimElement != null && claimElement.getValue() != null && StringUtils.isNotEmpty( claimElement.getValue() ) && !Character.isDigit( claimElement.getValue().charAt( 0 ) ) )
            {
              validationErrors.add( new ServiceError( ContentReaderManager.getText( claimElement.getClaimFormAssetCode(), claimElement.getClaimFormStepElement().getCmKeyForElementLabel() ) + " "
                  + ContentReaderManager.getText( "recognition.purl.submit", "FORM_ELEMENT_INVALID_VALUE" ) ) );

            }
          }
          break;
        }
      }
    }
  }

  public AbstractRecognitionClaim getRecognitionDetail( Long claimId, boolean includeTeamClaims )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CARD ) );
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.ELEMENTS ) );

    AbstractRecognitionClaim claim = (AbstractRecognitionClaim)getClaimByIdWithAssociations( claimId, associationRequestCollection );

    // hydrate the submitter
    if ( claim.getSubmitter() != null )
    {
      Hibernate.initialize( claim.getSubmitter().getParticipantEmployers() );
    }

    // hydrate recipients
    boolean isTeam = false;
    if ( includeTeamClaims )
    {
      if ( claim.isNominationClaim() && ( (NominationClaim)claim ).isTeam() )
      {
        NominationClaim nominationClaim = (NominationClaim)claim;
        if ( nominationClaim.getClaimRecipients() != null && nominationClaim.getClaimRecipients().size() > 0 )
        {
          isTeam = true;
          for ( ClaimRecipient teamMember : nominationClaim.getClaimRecipients() )
          {
            Hibernate.initialize( teamMember.getRecipient().getParticipantEmployers() );
          }
        }
      }
      else if ( claim.getTeamId() != null )
      {
        List<AbstractRecognitionClaim> teamClaims = claimDAO.getClaimsByTeamId( claim.getTeamId() );
        if ( teamClaims.size() > 1 )
        {
          isTeam = true;

          List filteredClaims = new ArrayList();
          for ( AbstractRecognitionClaim teamClaim : teamClaims )
          {
            if ( !teamClaim.getClaimRecipients().isEmpty() )
            {
              ClaimRecipient claimRecipient = teamClaim.getClaimRecipients().iterator().next();
              if ( claim.getSubmitter().getId().equals( UserManager.getUserId() ) || claimRecipient.getNotificationDate() == null
                  || claimRecipient.getNotificationDate().compareTo( DateUtils.getCurrentDate() ) <= 0 )
              {
                filteredClaims.add( teamClaim );
                Hibernate.initialize( claimRecipient.getRecipient().getParticipantEmployers() );
              }
            }
          }

          claim.setTeamClaims( filteredClaims );
        }
      }
    }

    if ( !isTeam )
    {
      if ( claim.getClaimRecipients() != null && !claim.getClaimRecipients().isEmpty() )
      {
        for ( ClaimRecipient cr : claim.getClaimRecipients() )
        {
          if ( cr.getRecipient() != null )
          {
            Hibernate.initialize( cr.getRecipient().getParticipantEmployers() );
          }
        }
      }
    }

    return claim;
  }

  public List<BadgeDetails> getBadgeDetailsFor( Long claimId )
  {
    List<BadgeDetails> badgeDetails = new ArrayList<BadgeDetails>();

    if ( claimId != null )
    {
      Claim claim = this.getClaimById( claimId );
      List<ParticipantBadge> participantBadgeList = gamificationService.getBadgesForRecognitionConfirmationScreen( claim.getPromotion().getId(), claim.getSubmitter().getId() );
      badgeDetails = gamificationService.toBadgeDetails( participantBadgeList );
    }

    return badgeDetails;
  }

  @Override
  public List<ProductClaimStatusCountsBean> getProductClaimStatusCount( Long id )
  {
    return claimDAO.getProductClaimStatusCount( id );
  }

  @Override
  public List<ProductClaimPromotionsValueBean> getEligibleProductClaimPromotions( Long userId )
  {
    return claimDAO.getEligibleProductClaimPromotions( userId );
  }

  @Override
  public int getProductClaimPromotionTeamMaxCount( Long promotionId )
  {
    return claimDAO.getProductClaimPromotionTeamMaxCount( promotionId );
  }

  /**
   * 
   * @return
   */
  public Long getNextTeamId()
  {
    return claimDAO.getNextTeamId();
  }

  private Date truncTimeFromCurrentDate()
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime( new Date() );
    calendar.set( Calendar.HOUR_OF_DAY, calendar.getActualMinimum( Calendar.HOUR_OF_DAY ) );
    calendar.set( Calendar.MINUTE, calendar.getActualMinimum( Calendar.MINUTE ) );
    calendar.set( Calendar.SECOND, calendar.getActualMinimum( Calendar.SECOND ) );
    calendar.set( Calendar.MILLISECOND, calendar.getActualMinimum( Calendar.MILLISECOND ) );
    return calendar.getTime();
  }

  public List<Long> getDelayedApprovalClaimIds()
  {
    List<Long> delayedApprovalClaimIds = null;
    delayedApprovalClaimIds = claimDAO.getDelayedApprovalClaimIds();
    return delayedApprovalClaimIds;
  }

  @Override
  public Date getMostRecentWinDate( Long promotionId, Long participantId, Long approvalLevel )
  {
    return claimDAO.getMostRecentWinDate( promotionId, participantId, approvalLevel );
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public void setMultimediaService( MultimediaService multimediaService )
  {
    this.multimediaService = multimediaService;
  }

  public void setMerchLevelService( MerchLevelService merchLevelService )
  {
    this.merchLevelService = merchLevelService;
  }

  public void setCalculatorService( CalculatorService calculatorService )
  {
    this.calculatorService = calculatorService;
  }

  public void setBudgetMasterService( BudgetMasterService budgetMasterService )
  {
    this.budgetMasterService = budgetMasterService;
  }

  /**
   * Bug # 51917
   * Since the injection is by lookup-method as defined in application context spring will
   * inject the bean. This method will be overridden by spring while creating the bean.
   * return null
   */
  public PurlService getPurlService()
  {
    return null;
  }

  public void setGamificationService( GamificationService gamificationService )
  {
    this.gamificationService = gamificationService;
  }

  public void setProcessService( ProcessService processService )
  {
    this.processService = processService;
  }

  public void setTranslationService( TranslationService translationService )
  {
    this.translationService = translationService;
  }

  public TranslatedContent getTranslatedRecognitionClaimSubmitterCommentFor( Long claimId, Long userId ) throws UnsupportedTranslationException, UnexpectedTranslationException
  {
    AbstractRecognitionClaim arc = (AbstractRecognitionClaim)getClaimById( claimId );

    LanguageType userPreferredLanguageType = userService.getPreferredLanguageFor( userId );

    TranslatedContent tc = translationService.translate( arc.getSubmitterCommentsLanguageType(), userPreferredLanguageType, arc.getSubmitterComments() );

    return tc;
  }

  public void setPurlContributorDAO( PurlContributorDAO purlContributorDAO )
  {
    this.purlContributorDAO = purlContributorDAO;
  }

  @Override
  public List<Participant> getGiversForParticipant( Long participantId, int count )
  {
    return claimDAO.getGiversForParticipant( participantId, count );
  }

  public List<Long> getCelebrationClaims( Long participantId )
  {
    return claimDAO.getCelebrationClaims( participantId );
  }

  public Long getCelebrationClaim( Long participantId )
  {
    Long claimId = null;
    List<Long> claimIds = getCelebrationClaims( participantId );
    if ( claimIds.size() > 0 )
    {
      claimId = claimIds.get( 0 );
    }
    return claimId;
  }

  @Override
  public int getEligibleUsersCountForCelebrationModule( Long claimId, Long participantId )
  {
    return claimDAO.getEligibleUsersCountForCelebrationModule( claimId, participantId );
  }

  public boolean displayCelebration( Long claimId, Long userId )
  {
    int userCount = getEligibleUsersCountForCelebrationModule( claimId, userId );
    if ( userCount > 0 )
    {
      return true;
    }
    return false;
  }

  public void setNodeService( NodeService nodeService )
  {
    this.nodeService = nodeService;
  }

  public void setCelebrationService( CelebrationService celebrationService )
  {
    this.celebrationService = celebrationService;
  }

  public MailingService getMailingService()
  {
    return mailingService;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public void setCountryService( CountryService countryService )
  {
    this.countryService = countryService;
  }

  @Override
  public void populateClaimElements( Claim claim, CustomFormStepElementsView stepElements )
  {
    List<ClaimElement> claimElementList = getClaimElementDomain( stepElements );

    // Clear the list - we may be editing an existing submission
    claim.getClaimElements().clear();

    for ( ClaimElement e : claimElementList )
    {
      e.setClaim( claim );
      claim.addClaimElement( e );
    }

  }
  
  // Client customization for WIP #39189 starts
  @Override
  public List<TcccClaimFileValueBean> getClaimFiles( Long claimId )
  {
    return claimDAO.getClaimFiles( claimId );
  }
  // Client customization for WIP #39189 end

  @Override
  public List<ClaimElement> getClaimElementDomain( CustomFormStepElementsView stepElements )
  {
    Map<Long, String> stepElementIdWithValue = stepElements.getStepElementIdWithValue();
    Set<Entry<Long, String>> entrySet = stepElementIdWithValue.entrySet();
    List<ClaimElement> claimElementList = new ArrayList<ClaimElement>();

    for ( Entry<Long, String> e : entrySet )
    {
      ClaimElement claimElement = new ClaimElement();
      claimElement.setClaimFormStepElement( claimFormDefinitionService.getClaimFormStepElementById( e.getKey() ) );
      claimElement.setValue( e.getValue() );
      claimElementList.add( claimElement );
    }
    return claimElementList;
  }

  public void setClaimFormDefinitionService( ClaimFormDefinitionService claimFormDefinitionService )
  {
    this.claimFormDefinitionService = claimFormDefinitionService;
  }

  @Override
  public CustomFormStepElementsView getClaimStepElementsWithValue( Long claimId )
  {

    Claim claim = getClaimById( claimId );
    List<ClaimElement> claimElements = claim.getClaimElements();

    CustomFormStepElementsView stepElements = promotionService.getStepElements( claim.getPromotion().getId() );

    Map<Long, CustomFormFieldView> nonGroupFields = stepElements.getNonGroupFieldsByElementStepId();

    // non group fields ..population
    for ( ClaimElement claimElement : claimElements )
    {
      CustomFormFieldView f = nonGroupFields.get( claimElement.getClaimFormStepElement().getId() );

      if ( f == null )
      { // group ..
        populateAddressGroupFields( claimElement, stepElements );
      }
      else
      { // Individual
        f.setValue( claimElement.getValue() );
      }

    }
    return stepElements;
  }

  private void populateAddressGroupFields( ClaimElement claimElement, CustomFormStepElementsView stepElements )
  {
    String value = claimElement.getValue();

    if ( StringUtil.isEmpty( value ) )
    {
      return;
    }

    Map<Long, List<CustomFormFieldView>> addressGroupsMap = stepElements.getAddressGroupsWithOutSecHeading();

    List<CustomFormFieldView> addressGroup = addressGroupsMap.get( claimElement.getClaimFormStepElement().getId() );

    String[] values = org.springframework.util.StringUtils.tokenizeToStringArray( value, "|" );

    for ( int i = 0; i < values.length; i++ )
    {
      CustomFormFieldView f = addressGroup.get( i );

      if ( ClaimFormElementType.SECTION_HEADING.equalsIgnoreCase( f.getType() ) )
      {
        continue;
      }

      f.setValue( values[i] );
    }

  }

  public Claim getClaimForPostProcess( Long claimId )
  {
    Claim claim = null;

    if ( claimId != null )
    {
      claim = claimDAO.getClaimById( claimId );
      // Insure we don't have proxied version
      claim = (Claim)ProxyUtil.deproxy( claim );
    }

    return claim;
  }

  public TranslateCommentViewBean getTranslatedComments( Long claimId, Long userId )
  {
    AbstractRecognitionClaim claim = (AbstractRecognitionClaim)getClaimById( claimId );
    LanguageType userPreferredLanguageType = userService.getPreferredLanguageFor( userId );

    TranslateCommentViewBean translateCommentViewBean = new TranslateCommentViewBean();
    TranslationViewBean translationViewBean = new TranslationViewBean();
    List<TranslationFieldsViewBean> fields = new ArrayList<TranslationFieldsViewBean>();
    List<ClaimElement> claimElements = claim.getClaimElements();
    boolean customWhy = false;
    for ( ClaimElement claimElement : claimElements )
    {
      TranslationFieldsViewBean bean = new TranslationFieldsViewBean();
      ClaimFormStepElement claimFormStepElement = claimElement.getClaimFormStepElement();
      if ( claimFormStepElement != null )
      {
        TranslatedContent tc = null;
        String comments = claimElement.getValue() != null ? ( (String)StringUtil.skipHTMLWithSpace( claimElement.getValue() ) ).replace( "\"", "\"\"" ) : null;
        try
        {
          if ( StringUtils.isNotEmpty( comments ) )
          {
            tc = translationService.translate( claim.getSubmitterCommentsLanguageType(), userPreferredLanguageType, comments );
            if ( claimFormStepElement.isWhyField() )
            {
              customWhy = true;
              bean.setName( "comment" );
              bean.setText( tc.getTranslatedContent() );
            }
            else
            {
              bean.setFieldId( claimFormStepElement.getId() );
              bean.setText( tc.getTranslatedContent() );
              bean.setName( claimFormStepElement.getClaimFormElementType().getName() );
            }
          }
        }
        catch( UnsupportedTranslationException ute )
        {
          bean.setText( ContentReaderManager.getText( "recognition.public.recognition.item", "TRANSLATION_UNAVAILABLE" ) );
        }
        catch( UnexpectedTranslationException ex )
        {
          bean.setText( ContentReaderManager.getText( "recognition.public.recognition.item", "TRANSLATION_UNAVAILABLE" ) );
        }
      }
      fields.add( bean );
    }

    if ( !customWhy )
    {
      NominationClaim nominationClaim = (NominationClaim)claim;
      TranslationFieldsViewBean bean = new TranslationFieldsViewBean();
      TranslatedContent tc;
      String comments = nominationClaim.getSubmitterComments() != null ? ( (String)StringUtil.skipHTMLWithSpace( nominationClaim.getSubmitterComments() ) ).replace( "\"", "\"\"" ) : null;
      try
      {
        if ( StringUtils.isNotEmpty( comments ) )
        {
          tc = translationService.translate( claim.getSubmitterCommentsLanguageType(), userPreferredLanguageType, comments );
          bean.setName( "comment" );
          bean.setText( tc.getTranslatedContent() );
        }
      }
      catch( UnsupportedTranslationException ute )
      {
        bean.setText( ContentReaderManager.getText( "recognition.public.recognition.item", "TRANSLATION_UNAVAILABLE" ) );
      }
      catch( UnexpectedTranslationException ex )
      {
        bean.setText( ContentReaderManager.getText( "recognition.public.recognition.item", "TRANSLATION_UNAVAILABLE" ) );
      }
      fields.add( bean );
    }
    String approverComments = claim.getApproverComments() != null ? ( (String)StringUtil.skipHTML( claim.getApproverComments() ) ).replace( "\"", "\"\"" ) : null;

    if ( !StringUtil.isEmpty( approverComments ) )
    {
      TranslationFieldsViewBean bean2 = new TranslationFieldsViewBean();
      TranslatedContent tc;
      try
      {
        tc = translationService.translate( claim.getSubmitterCommentsLanguageType(), userPreferredLanguageType, approverComments );
        bean2.setName( "approverComment" );
        bean2.setText( tc.getTranslatedContent() );
      }
      catch( UnsupportedTranslationException ute )
      {
        bean2.setText( ContentReaderManager.getText( "recognition.public.recognition.item", "TRANSLATION_UNAVAILABLE" ) );
      }
      catch( UnexpectedTranslationException ex )
      {
        bean2.setText( ContentReaderManager.getText( "recognition.public.recognition.item", "TRANSLATION_UNAVAILABLE" ) );
      }
      fields.add( bean2 );
    }

    translationViewBean.setFields( fields );
    translateCommentViewBean.setTranslation( translationViewBean );
    return translateCommentViewBean;
  }

  @Override
  public List<Long> getClaimIdList( Long submitterId, String approvalStatusType )
  {
    return claimDAO.getClaimIdList( submitterId, approvalStatusType );
  }

  @Override
  public Claim saveClaim( Claim claim )
  {
    return claimDAO.saveClaim( claim );
  }

  public Long getMinQualifierId( Long claimId, Long productId )
  {
    return claimDAO.getMinQualifierId( claimId, productId );
  }

  @Override
  public boolean pastApprovalExist( Long approverId )
  {
    return claimDAO.pastApprovalExist( approverId );
  }

  public List<ClaimInfoBean> getActivityTimePeriod( Long claimId )
  {
    return claimDAO.getActivityTimePeriod( claimId );
  }

  @Override
  public List<AbstractRecognitionClaim> getClaimsByTeamId( Long claimId )
  {
    return claimDAO.getClaimsByTeamId( claimId );
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public AudienceService getAudienceService()
  {
    return audienceService;
  }

  public void setAudienceService( AudienceService audienceService )
  {
    this.audienceService = audienceService;
  }

  public MTCService getMtcService()
  {
    return mtcService;
  }

  public void setMtcService( MTCService mtcService )
  {
    this.mtcService = mtcService;
  }

  @Override
  public String getPromoTimePeriodNameById( Long timePeriodId )
  {
    return claimDAO.getPromoTimePeriodNameById( timePeriodId );
  }

  @Override
  public List<ServiceError> validateClaimElements( List<ClaimElement> claimElementList, Node node, Promotion promotion, int paxCount, boolean isDraft )
  {
    List<ServiceError> validationErrors = new ArrayList<ServiceError>();

    validationErrors.addAll( validateClaimElementDataTypeRequirements( claimElementList, isDraft ) );
    if ( validationErrors.isEmpty() )
    {
      validationErrors.addAll( validateClaimElementsUniqueness( claimElementList, node, promotion ) );
    }
    if ( validationErrors.isEmpty() )
    {
      validationErrors.addAll( validateClaimElementFormRules( claimElementList, promotion ) );
    }

    if ( promotion.isProductClaimPromotion() )
    {
      int teamMaxCount = getProductClaimPromotionTeamMaxCount( promotion.getId() );

      if ( teamMaxCount != 0 )
      {
        if ( paxCount > teamMaxCount )
        {
          validationErrors.add( new ServiceError( String.valueOf( teamMaxCount ), CmsResourceBundle.getCmsBundle().getString( "claims.submission.errors.MAX_TEAM_MEMBERS_YOU_CAN_ADD" ) ) );
        }
      }
    }

    return validationErrors;
  }

  // Client customizations for WIP #42960 starts
  public CokeClientDAO getCokeClientDAO()
  {
    return cokeClientDAO;
  }

  public void setCokeClientDAO( CokeClientDAO cokeClientDAO )
  {
    this.cokeClientDAO = cokeClientDAO;
  }
  // Client customizations for WIP #42960 ends
  
  // Client customization for WIP #43735 starts
  public void processClaimAward( Long claimId, String awardType ) throws ServiceErrorException
  {
    AbstractRecognitionClaim claim = (AbstractRecognitionClaim)claimDAO.getClaimById( claimId );
    if ( claim == null )
    {
      List serviceErrors = new ArrayList();
      serviceErrors.add( new ServiceError( ServiceErrorMessageKeys.COKE_CLAIM_UNAVAIALBLE_ERR ) );
      throw new ServiceErrorException( serviceErrors );
    }
    else if ( !claim.isMine() )
    {
      List serviceErrors = new ArrayList();
      serviceErrors.add( new ServiceError( ServiceErrorMessageKeys.COKE_CLAIM_AWARD_REDEEM_UNAUTHORIZED_ERR ) );
      throw new ServiceErrorException( serviceErrors );
    }
    else if ( claim.isAwardClaimed() )
    {
      List serviceErrors = new ArrayList();
      serviceErrors.add( new ServiceError( ServiceErrorMessageKeys.COKE_CLAIM_AWARD_ALREADY_REDEEMED_ERR ) );
      throw new ServiceErrorException( serviceErrors );
    }
    else if ( !cashAllowed( claim ) && ( CokeClientService.REDEEM_AWARD_MODE_CASH.equals( awardType ) || CokeClientService.REDEEM_AWARD_MODE_CASH_AND_POINTS.equals( awardType ) ) )
    {
      List serviceErrors = new ArrayList();
      serviceErrors.add( new ServiceError( ServiceErrorMessageKeys.COKE_CLAIM_AWARD_CASH_NOT_ALLOWED_ERR ) );
      throw new ServiceErrorException( serviceErrors );
    }
    else
    {
      ClaimRecipient claimRecipient = getClaimRecipeint( claim );
      try
      {
        if ( CokeClientService.REDEEM_AWARD_MODE_CASH.equals( awardType ) || CokeClientService.REDEEM_AWARD_MODE_CASH_AND_POINTS.equals( awardType )
            || CokeClientService.REDEEM_AWARD_MODE_POINTS.equals( awardType ) )
        {
          Long currency = null;
          Long points = null;
          Journal journal = null;
          if ( CokeClientService.REDEEM_AWARD_MODE_CASH.equals( awardType ) )
          {
            currency = claimRecipient.getCustomCashAwardQuantity().longValue();
          }
          else if ( CokeClientService.REDEEM_AWARD_MODE_CASH_AND_POINTS.equals( awardType ) )
          {
            // Get percentage of cash amount in any currency
            currency = TcccClientUtils.convertToPercentCash( systemVariableService, claimRecipient.getCustomCashAwardQuantity().longValue() );
            // Get remaining as cash amount for points in any currency
            Long cashAmountForPoints = claimRecipient.getCustomCashAwardQuantity().longValue() - currency;
            // Convert cash amount for points currency to USD currency
            Long usdCashAmt = TcccClientUtils.convertToUSD( cokeClientDAO, cashAmountForPoints, claimRecipient.getCashCurrencyCode() );
            // Convert USD cash amount for points to actual points
            points = TcccClientUtils.convertToPoints( cokeClientDAO, usdCashAmt, claimRecipient.getRecipient().getId() );
          }
          else if ( CokeClientService.REDEEM_AWARD_MODE_POINTS.equals( awardType ) )
          {
            // Convert cash amount for points currency to USD currency
            Long usdCashAmt = TcccClientUtils.convertToUSD( cokeClientDAO, claimRecipient.getCustomCashAwardQuantity().longValue(), claimRecipient.getCashCurrencyCode() );
            // Convert USD cash amount for points to actual points
            points = TcccClientUtils.convertToPoints( cokeClientDAO, usdCashAmt, claimRecipient.getRecipient().getId() );
          }
          journal = createJournal( claim, claimRecipient, currency, points, awardType );
          claim.getClaimRecipients().iterator().next().setCashPaxClaimed( true );
          claimDAO.saveClaim( claim );
          // WIP #57795 start
          
          Activity activity = activityDAO.getRecipientClaimActivity( claimRecipient.getRecipient().getId(), claim.getId() ) ;
         
          if ( activity instanceof RecognitionActivity )
          {
            RecognitionActivity recogActivity = (RecognitionActivity)activity;
            recogActivity.setAwardQuantity( points );
            activityDAO.saveActivity( recogActivity );
          }
          else if ( activity instanceof NominationActivity )
          {
            NominationActivity recogActivity = (NominationActivity)activity;
            recogActivity.setAwardQuantity( points );
            activityDAO.saveActivity( recogActivity );
          }
         // WIP #57795 end
          
          if ( CokeClientService.REDEEM_AWARD_MODE_CASH_AND_POINTS.equals( awardType ) || CokeClientService.REDEEM_AWARD_MODE_POINTS.equals( awardType ) )
          {
            launchDepositProcess( journal, claim );
          }
        }
        else
        {
          List serviceErrors = new ArrayList();
          serviceErrors.add( new ServiceError( ServiceErrorMessageKeys.COKE_CLAIM_AWARD_UNKNOWN_AWARD_ERR ) );
          throw new ServiceErrorException( serviceErrors );
        }
      }
      catch( Exception e )
      {
        throw new ServiceErrorExceptionWithRollback( e.getMessage(), e );
      }
    }
  }

  private Journal createJournal( AbstractRecognitionClaim claim, ClaimRecipient claimRecipient, Long currencyAmt, Long pointsAmt, String awardType ) throws ServiceErrorException
  {
    String promotionName = claim.getPromotion().getPromotionName();
    if ( claimRecipient.getRecipient().getLanguageType() != null )
    {
      promotionName = promotionService.getPromotionNameByLocale( claim.getPromotion().getPromoNameAssetCode(), claimRecipient.getRecipient().getLanguageType().getCode() );
      promotionName = StringUtils.isEmpty( promotionName ) ? claim.getPromotion().getPromotionName() : promotionName;
    }
    Journal journal = new Journal();
    journal.setGuid( GuidUtils.generateGuid() );
    journal.setTransactionDate( DateUtils.getCurrentDate() );
    journal.setPromotion( claim.getPromotion() );
    journal.setParticipant( claimRecipient.getRecipient() );
    journal.setTransactionDescription( promotionName );
    journal.setTransactionType( JournalTransactionType.lookup( JournalTransactionType.DEPOSIT ) );
	journal.setAwardPayoutType(PromotionAwardsType.lookup(awardType));
    if ( CokeClientService.REDEEM_AWARD_MODE_CASH.equals( awardType ) )
    {
      // Cash Only
      journal.setTransactionAmount( 0L );
      //journal.setCashAwardQty( currencyAmt );
      journal.setCashCurrency( claimRecipient.getCashCurrencyCode() );     
      journal.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.POST ) );
    }
    else if ( CokeClientService.REDEEM_AWARD_MODE_CASH_AND_POINTS.equals( awardType ) )
    {
      // Half Cash & Half Points
      journal.setTransactionAmount( pointsAmt );
      //journal.setCashAwardQty( currencyAmt );
      journal.setCashCurrency( claimRecipient.getCashCurrencyCode() );  	  
      journal.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.APPROVE ) );
    }
    else
    {
      // Points Only
      journal.setTransactionAmount( pointsAmt );      
      journal.setJournalStatusType( JournalStatusType.lookup( JournalStatusType.APPROVE ) );
    }
    journal.setAccountNumber( claimRecipient.getRecipient().getAwardBanqNumber() );
    Set activitySet = new HashSet();
    activitySet.add( activityDAO.getRecipientClaimActivity( claimRecipient.getRecipient().getId(), claim.getId() ) );
    journal.addActivities( activitySet );
    try
    {
      journalDAO.saveJournalEntry( journal );
    }
    catch( Exception e )
    {
      logger.error( e );
      List serviceErrors = new ArrayList();
      serviceErrors.add( new ServiceError( ServiceErrorMessageKeys.COKE_CLAIM_AWARD_UNKNOWN_AWARD_ERR ) );
      throw new ServiceErrorException( serviceErrors );
    }
    return journal;
  }
  private void launchDepositProcess( Journal journal, AbstractRecognitionClaim claim )
  {
    DepositProcessBean depositProcessBean = new DepositProcessBean();
    depositProcessBean.setJournalId( journal.getId() );
    List depositProcessPointsList = new ArrayList<DepositProcessBean>();
    depositProcessPointsList.add( depositProcessBean );
    LinkedHashMap<String, Object> paramValueMap = new LinkedHashMap<String, Object>();
    paramValueMap.put( "depositProcessPointsList", depositProcessPointsList );
    paramValueMap.put( "promotionId", claim.getPromotion().getId() );
    Process process = processService.createOrLoadSystemProcess( PointsDepositProcess.PROCESS_NAME, PointsDepositProcess.BEAN_NAME );
    processService.launchProcess( process, paramValueMap, UserManager.getUserId() );
  }
  private boolean cashAllowed( AbstractRecognitionClaim claim )
  {
    String userJobGrade = userService.getUserJobGradeCharValue( getClaimRecipeint( claim ).getRecipient().getId() );
    String jobGrades = systemVariableService.getPropertyByName( SystemVariableService.COKE_CASH_JOB_GRADE_AND_BELOW ).getStringVal();
    String[] jobGradesArray = jobGrades.split( "," );
    List<String> jobGradesList = new ArrayList<>( Arrays.asList( jobGradesArray ) );
    return !StringUtil.isNullOrEmpty( userJobGrade ) && ( jobGradesList.contains( userJobGrade ) );
  }

  private ClaimRecipient getClaimRecipeint( AbstractRecognitionClaim claim )
  {
    ClaimRecipient cr = null;
    for ( ClaimRecipient claimRecipient : claim.getClaimRecipients() )
    {
      cr = claimRecipient;
      break;
    }
    return cr;
  }
  

  // Client customization for WIP 58122
  public boolean processNominationClaimAward( Long claimId, String awardType ) throws ServiceErrorException
  {
    AbstractRecognitionClaim claim = (AbstractRecognitionClaim)claimDAO.getClaimById( claimId );
    NominationClaim claim1=(NominationClaim) claim;
    SuccessResponse bean = null;
    Journal journal = null;
    if ( claim == null )
    {
      List serviceErrors = new ArrayList();
      serviceErrors.add( new ServiceError( ServiceErrorMessageKeys.COKE_CLAIM_UNAVAIALBLE_ERR ) );
      throw new ServiceErrorException( serviceErrors );
    }
    else if ( !claim.isMine() )
    {
      List serviceErrors = new ArrayList();
      serviceErrors.add( new ServiceError( ServiceErrorMessageKeys.COKE_CLAIM_AWARD_REDEEM_UNAUTHORIZED_ERR ) );
      throw new ServiceErrorException( serviceErrors );
    }
    else if ( claim.isAwardClaimed() )
    {
      List serviceErrors = new ArrayList();
      serviceErrors.add( new ServiceError( ServiceErrorMessageKeys.COKE_CLAIM_AWARD_ALREADY_REDEEMED_ERR ) );
      throw new ServiceErrorException( serviceErrors );
    }
    else
    {
      ProductClaimParticipant claimParticipant = getClaimParticipant( claim1 );
      try
      {
          //points selected
          if ( CokeClientService.REDEEM_AWARD_MODE_POINTS.equals( awardType ) )
          {
              Long points=claimParticipant.getAwardQuantity();
              journal = createJournalForNomination(claim, claimParticipant, points, awardType);
              claimParticipant.setLevelPaxClaimed( true );
              claimDAO.saveClaim( claim );
              // WIP #57795 start
              
              Activity activity = activityDAO.getRecipientClaimActivity( claimParticipant.getParticipant().getId(), claim.getId() ) ;
             
              if ( activity instanceof RecognitionActivity )
              {
                RecognitionActivity recogActivity = (RecognitionActivity)activity;
                recogActivity.setAwardQuantity( points );
                activityDAO.saveActivity( recogActivity );
              }
              else if ( activity instanceof NominationActivity )
              {
                NominationActivity recogActivity = (NominationActivity)activity;
                recogActivity.setAwardQuantity( points );
                activityDAO.saveActivity( recogActivity );
              }
                launchDepositProcess( journal, claim );
              
              return true; 
          }
          //opted for training
          else if(CokeClientService.REDEEM_AWARD_MODE_TRAINING.equals( awardType ))
          {
              claimParticipant.setAwardQuantity(new Long(0));
              claimParticipant.setLevelPaxClaimed(true);
              claimParticipant.setOptTraining(true);
              claimDAO.saveClaim( claim );
              PropertySetItem sysProperty = systemVariableService.getPropertyByName( SystemVariableService.COKE_TRAINING_CONTACT_EMAIL );
              sendMailForOptTraining( claimParticipant.getParticipant().getPrimaryEmailAddress().getEmailAddr(), "Training award opted", "Training award opted", claimParticipant.getParticipant().getFirstName(), claimParticipant.getParticipant().getLastName(), sysProperty.getStringVal() );
              return true; 
          }
          //Decline award
          else if(CokeClientService.DECLINE_AWARD.equals( awardType ))
          {
        	  claimParticipant.setOptOut(new Boolean( true ).toString());
        	  claimParticipant.setAwardQuantity(new Long(0));
        	  claimParticipant.setLevelPaxClaimed(true);
        	  claimParticipant.setOptTraining(false);
              claimDAO.saveClaim( claim );
              return true; 
          }
       
      }
      catch( Exception e )
      {
        throw new ServiceErrorExceptionWithRollback( e.getMessage(), e );
      }
    }
return false;
  }
  

  private void sendMailForOptTraining( String fromEmailAddress,
          String subject,
          String text,
          String firstName,
          String lastName,
          String destEmail)
    {
        EmailHeader header = new EmailHeader();
        header.setRecipients(new String[] { destEmail });
        header.setSender(destEmail);
        header.setReplyTo(fromEmailAddress);
        subject = "[ " + systemVariableService.getPropertyByName(SystemVariableService.CLIENT_NAME).getStringVal()
                + " ] " + subject;
        header.setSubject(subject);

        // add firstName/lastName to 'text' only if not null
        String name = "";
        StringBuffer bodyHeader = null;
        if ((firstName != null && firstName.length() > 0) || (lastName != null && lastName.length() > 0)) {
            bodyHeader = new StringBuffer();
            bodyHeader.append(CmsResourceBundle.getCmsBundle().getString("help.contact.us.HEADER_BODY"));
            bodyHeader.append(" '");
            bodyHeader.append(firstName);
            bodyHeader.append(" ");
            bodyHeader.append(lastName);
            bodyHeader.append(" (");
            bodyHeader.append(fromEmailAddress);
            bodyHeader.append(")':");
            bodyHeader.append("\n");

            name = bodyHeader.toString();
        }

        
        TextEmailBody body = new TextEmailBody(name + text );
        try {
            emailService.sendMessage(header, body);
        } catch (ServiceErrorException e) {
            throw new BeaconRuntimeException("Error sending message", e);
        }

    }
  private void sendMessageToCokeTraining( ClaimRecipient cr,PropertySetItem sysProperty)
  {
    //User recipientUser = getRunByUser();

    // Add the summary info to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "firstName", cr.getFirstName() );
    objectMap.put( "lastName", cr.getLastName() );
    objectMap.put( "clientName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME).getStringVal() );
    objectMap.put( "clientPrefix", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX).getStringVal() ); 
    
    // Compose the mailing
      Mailing mailing = composeMail( MessageService.COKE_DAY_EMAIL_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

    // Add the recipient
    MailingRecipient mr = addRecipient(sysProperty.toString());
    mailing.addMailingRecipient( mr );

    try
    {
      // Send the e-mail message with personalization
      mailingService.submitMailing( mailing, objectMap );

      

     // addComment(  "eStatement process summary email sent to user id: "+UserManager.getUserId() );
    }
    catch( Exception e )
    {
      
    }
  }
protected Mailing composeMail( String cmAssetCode, String mailingType )
  {
    Mailing mailing = composeMail();

    mailing.setMailingType( MailingType.lookup( mailingType ) );

    Message message = getMessageService().getMessageByCMAssetCode( cmAssetCode );
    mailing.setMessage( message );

    return mailing;
  }
  private Mailing composeMail()
  {
    Mailing mailing = new Mailing();

    // Needs Guid due to lack of a business key
    mailing.setGuid( GuidUtils.generateGuid() );

    // Sender
    String sender = systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS ).getStringVal();
    mailing.setSender( sender );

    // Delivery Date - Assumes Now (i.e. immediate delivery)
    Timestamp deliveryDate = new Timestamp( com.biperf.core.utils.DateUtils.getCurrentDate().getTime() );
    mailing.setDeliveryDate( deliveryDate );

    return mailing;

  }
  
  //Client customization for WIP #39189 starts
  private Set buildClaimFiles( RecognitionClaimSubmission submission )
  {
    Set<TcccClaimFile> claimFiles = new LinkedHashSet<TcccClaimFile>();
    for ( TcccClaimFileValueBean claimFile : submission.getClaimUploads() )
    {
      TcccClaimFile file = new TcccClaimFile();
      file.setFileName( claimFile.getDescription() );
      file.setFileUrl( claimFile.getUrl() );
      claimFiles.add( file );
    }
    return claimFiles;
  }
  //Client customization for WIP #39189 ends            
  // Client customization for WIP 58122
  protected MailingRecipient addRecipient(String emailAddr)
  {
      MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setPreviewEmailAddress( emailAddr );
    mailingRecipient.setLocale( MailingMessageLocale.DEFAULT_LOCALE );
    return mailingRecipient;
   // recipients.add( mailingRecipient );
  }

  private ProductClaimParticipant getClaimParticipant( NominationClaim claim )
  {
      ProductClaimParticipant cr = null;
    for ( ProductClaimParticipant claimParticipant : claim.getTeamMembers() )
    {
        if ( claimParticipant.getParticipant().getId().equals( UserManager.getUserId() ) )
        {
            cr = claimParticipant;
            break;
        }
    }
    return cr;
  }
  

  private static final class SuccessResponse
  {
    private final boolean success;

    public SuccessResponse(boolean success)
    {
      this.success = success;
    }

    public boolean getSuccess()
    {
      return success;
    }
  }
  // Client customization for WIP 58122
  private Journal createJournalForNomination( AbstractRecognitionClaim claim, ProductClaimParticipant claimParticipant, Long awardQuantity, String awardType ) throws ServiceErrorException
  {
    String promotionName = claim.getPromotion().getPromotionName();
    if ( claimParticipant.getParticipant().getLanguageType() != null )
    {
      promotionName = promotionService.getPromotionNameByLocale( claim.getPromotion().getPromoNameAssetCode(), claimParticipant.getParticipant().getLanguageType().getCode() );
      promotionName = StringUtils.isEmpty( promotionName ) ? claim.getPromotion().getPromotionName() : promotionName;
    }
	Journal journal = new Journal();
	journal.setGuid(GuidUtils.generateGuid());
	journal.setTransactionDate(DateUtils.getCurrentDate());
	journal.setPromotion(claim.getPromotion());
	journal.setParticipant(claimParticipant.getParticipant());
	journal.setTransactionDescription(promotionName);
	journal.setTransactionType(JournalTransactionType.lookup(JournalTransactionType.DEPOSIT));
	// Points Only
	if (null != awardQuantity && awardQuantity > 0) {
		journal.setTransactionAmount(awardQuantity);
	} else {
		ClaimRecipient claimRecipient = getClaimRecipient(claim);
		journal.setTransactionAmount(claimRecipient.getAwardQuantity());
	}
	journal.setJournalStatusType(JournalStatusType.lookup(JournalStatusType.APPROVE));
	journal.setAwardPayoutType(PromotionAwardsType.lookup(awardType));
    journal.setAccountNumber( claimParticipant.getParticipant().getAwardBanqNumber() );
    Set activitySet = new HashSet();
    activitySet.add( activityDAO.getRecipientClaimActivityForNomination(claimParticipant.getParticipant().getId(), claim.getId() ) );
    journal.addActivities( activitySet );
    try
    {
      journalDAO.saveJournalEntry( journal );
    }
    catch( Exception e )
    {
      logger.error( e );
      List serviceErrors = new ArrayList();
      serviceErrors.add( new ServiceError( ServiceErrorMessageKeys.COKE_CLAIM_AWARD_UNKNOWN_AWARD_ERR ) );
      throw new ServiceErrorException( serviceErrors );
    }
    return journal;
  }
  
  // Client Customization for WIP #39189 starts
  public PersonalInfoFileUploadValue uploadClaimFile( PersonalInfoFileUploadValue data ) throws ServiceErrorException, ImageReadException, IOException
  {
    ImageUtils imgInstance = new ImageUtils();
    BufferedImage inputImage = null;
    try
    {
      inputImage = imgInstance.readImage( data.getData() );
      BufferedImage full = inputImage;
      data.setFull( ImageUtils.getPersonalInfoDetailPath( data.getType(), data.getId(), data.getName() ) );
      appDataDirFileUploadStrategy.uploadFileData( data.getFull(), ImageUtils.convertToByteArray( full, ImageUtils.getFileExtension( data.getName() ) ) );
    }
    catch( ImageReadException e )
    {
      logger.error( "******Loading document...." );
      data.setFull( ImageUtils.getPersonalInfoDetailPath( data.getType(), data.getId(), data.getName() ) );
      uploadDoc( data );
    }
    moveFileToWebdav( data.getFull() );
    return data;
  }

  private boolean uploadDoc( PersonalInfoFileUploadValue data ) throws ServiceErrorException, ImageReadException, IOException
  {
    boolean success = false;
    try
    {
      success = appDataDirFileUploadStrategy.uploadFileData( data.getFull(), data.getInputStream() );
    }
    catch( ServiceErrorException e )
    {
      logger.error( "******Uploading document failed in uploadDoc. Error is " + e.getMessage() );
      throw new ServiceErrorException( "coke.claim.image.upload.FAILED" );
    }
    return success;
  }

  private boolean moveFileToWebdav( String mediaUrl )
  {
    try
    {
      byte[] media = appDataDirFileUploadStrategy.getFileData( mediaUrl );
      webdavFileUploadStrategy.uploadFileData( mediaUrl, media );

      appDataDirFileUploadStrategy.delete( mediaUrl );
      return true;
    }
    catch( Throwable e )
    {
      // Must not have the file in AppDataDir of server executing this process
    }
    return false;
  }
  
	private ClaimRecipient getClaimRecipient(AbstractRecognitionClaim claims) {
		
		ClaimRecipient claimRecipient = null;
		NominationClaim newClaim = (NominationClaim) claims;
		if(null != newClaim && null != newClaim.getClaimRecipients()){
		for (ClaimRecipient claimRecipient1 : newClaim.getClaimRecipients()) {
			if (null != claimRecipient1 && claimRecipient1.getRecipient().getId().equals(UserManager.getUserId())) {
				claimRecipient = claimRecipient1;
				break;
			}
		}
		}
		return claimRecipient;
	}

  /* coke customization start */
  public List<Long> getNomTeamClaimsWithoutJournalForActivityHistoryReceivedTab( Date startDate, Date endDate, Long promotionId, Long recipientId )
  {
    return claimDAO.getNomTeamClaimsWithoutJournalForActivityHistoryReceivedTab( startDate, endDate, promotionId, recipientId );
  }
  /* coke customization end */ 
  public void deleteClaimFile( String filePath ) throws ServiceErrorException, ImageReadException, IOException
  {
    appDataDirFileUploadStrategy.delete( filePath );
  }
  
  public void setAppDataDirFileUploadStrategy( FileUploadStrategy appDataDirFileUploadStrategy )
  {
    this.appDataDirFileUploadStrategy = appDataDirFileUploadStrategy;
  }

  public void setWebdavFileUploadStrategy( FileUploadStrategy webdavFileUploadStrategy )
  {
    this.webdavFileUploadStrategy = webdavFileUploadStrategy;
  }
  // Client Customization for WIP #39189 ends
  
  @Override
  public String getDBTimeZone()
  {
    return claimDAO.getDBTimeZone();
  }

  @Override
  public boolean isCardMapped( Long cardId )
  {
    // TODO Auto-generated method stub
    return claimDAO.isCardMapped( cardId );
  }

//Client customization for WIP 58122
public void setEmailService( EmailService emailService )
{
  this.emailService = emailService;
}


/*private void sendMailForOptTraining( String fromEmailAddress,
        String subject,
        String text,
        String firstName,
        String lastName,
        String destEmail)
    {
        EmailHeader header = new EmailHeader();
        header.setRecipients(new String[] { destEmail });
        header.setSender(destEmail);
        header.setReplyTo(fromEmailAddress);
        subject = "[ " + systemVariableService.getPropertyByName(SystemVariableService.CLIENT_NAME).getStringVal()
                + " ] " + subject;
        header.setSubject(subject);

        // add firstName/lastName to 'text' only if not null
        String name = "";
        StringBuffer bodyHeader = null;
        if ((firstName != null && firstName.length() > 0) || (lastName != null && lastName.length() > 0)) {
            bodyHeader = new StringBuffer();
            bodyHeader.append(CmsResourceBundle.getCmsBundle().getString("help.contact.us.HEADER_BODY"));
            bodyHeader.append(" '");
            bodyHeader.append(firstName);
            bodyHeader.append(" ");
            bodyHeader.append(lastName);
            bodyHeader.append(" (");
            bodyHeader.append(fromEmailAddress);
            bodyHeader.append(")':");
            bodyHeader.append("\n");

            name = bodyHeader.toString();
        }

        
        TextEmailBody body = new TextEmailBody(name + text );
        try {
            emailService.sendMessage(header, body);
        } catch (ServiceErrorException e) {
            throw new BeaconRuntimeException("Error sending message", e);
        }

    }*/


protected static SAO getService( String beanName )
{
  return (SAO)BeanLocator.getBean( beanName );
}


public CokeClientService getCokeClientService()
{
  return (CokeClientService)getService( CokeClientService.BEAN_NAME );
}

public MessageService getMessageService() {
    return messageService;
}

public void setMessageService(MessageService messageService) {
    this.messageService = messageService;
}
//Client customization for WIP 58122

public void setActivityDAO( ActivityDAO activityDAO )
{
  this.activityDAO = activityDAO;
}

public void setUserCharacteristicService( UserCharacteristicService userCharacteristicService )
{
  this.userCharacteristicService = userCharacteristicService;
}

}
