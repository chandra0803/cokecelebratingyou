
package com.biperf.core.service.email.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.biperf.core.dao.challengepoint.ChallengepointAwardDAO;
import com.biperf.core.dao.claim.ClaimDAO;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.mailing.MailingBatchDAO;
import com.biperf.core.dao.mailing.MailingDAO;
import com.biperf.core.dao.merchandise.hibernate.MerchOrderActivityQueryConstraint;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.ApprovableItem;
import com.biperf.core.domain.claim.ApprovableItemApprover;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.claim.ClaimGroup;
import com.biperf.core.domain.claim.ClaimProduct;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.claim.QuizClaim;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.client.RecognizeAnyone;
import com.biperf.core.domain.commlog.CommLog;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.diyquiz.DIYQuiz;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.BadgeLevelType;
import com.biperf.core.domain.enums.BadgeType;
import com.biperf.core.domain.enums.BaseUnitPosition;
import com.biperf.core.domain.enums.BudgetFinalPayoutRule;
import com.biperf.core.domain.enums.BudgetMasterAwardType;
import com.biperf.core.domain.enums.ChallengePointAwardType;
import com.biperf.core.domain.enums.ClaimFormStepEmailNotificationType;
import com.biperf.core.domain.enums.CommLogCategoryType;
import com.biperf.core.domain.enums.CommLogReasonType;
import com.biperf.core.domain.enums.CommLogSourceType;
import com.biperf.core.domain.enums.CommLogStatusType;
import com.biperf.core.domain.enums.CommLogUrgencyType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.ManagerOverrideStructure;
import com.biperf.core.domain.enums.MessageSMSGroupType;
import com.biperf.core.domain.enums.MessageType;
import com.biperf.core.domain.enums.NominationEvaluationType;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.domain.enums.ProcessFrequencyType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionEmailNotificationType;
import com.biperf.core.domain.enums.PurlPromotionMediaType;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.enums.SsoLoginEnum;
import com.biperf.core.domain.enums.SupportedEcardVideoTypes;
import com.biperf.core.domain.enums.SweepstakesWinnerEligibilityType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.filestore.FileStore;
import com.biperf.core.domain.gamification.BadgeDetails;
import com.biperf.core.domain.gamification.BadgeInfo;
import com.biperf.core.domain.gamification.BadgeLibrary;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.gamification.ParticipantBadge;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.leaderboard.LeaderBoard;
import com.biperf.core.domain.leaderboard.LeaderBoardParticipant;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingAttachmentInfo;
import com.biperf.core.domain.mailing.MailingBatch;
import com.biperf.core.domain.mailing.MailingMessageLocale;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.mailing.MailingRecipientData;
import com.biperf.core.domain.managertoolkit.AlertMessage;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.mtc.MTCVideo;
import com.biperf.core.domain.multimedia.ECard;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.AudienceParticipant;
import com.biperf.core.domain.participant.CriteriaAudience;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantCommunicationPreference;
import com.biperf.core.domain.participant.ParticipantPartner;
import com.biperf.core.domain.participant.PaxAudience;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessSchedule;
import com.biperf.core.domain.promotion.CelebrationManagerMessage;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Match;
import com.biperf.core.domain.promotion.MatchTeamOutcome;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.NominationPromotionLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionNotification;
import com.biperf.core.domain.promotion.PromotionNotificationType;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.promotion.Round;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.domain.purl.PurlContributor;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestApprover;
import com.biperf.core.domain.ssi.SSIContestAwardThemNow;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.domain.ssi.SSIContestPaxClaim;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.exception.ServiceErrorExceptionNoRollback;
import com.biperf.core.mobileapp.recognition.service.MobileNotificationService;
import com.biperf.core.process.MailingProcess;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.service.TranslatedContent;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.cashcurrency.CashCurrencyService;
import com.biperf.core.service.celebration.CelebrationManagerMessageAssociationRequest;
import com.biperf.core.service.celebration.CelebrationService;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.commlog.CommLogService;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.email.EmailBody;
import com.biperf.core.service.email.EmailHeader;
import com.biperf.core.service.email.EmailService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.email.PersonalizationService;
import com.biperf.core.service.email.TextEmailBody;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.maincontent.DesignThemeService;
import com.biperf.core.service.merchorder.MerchOrderService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.mtc.MTCVideoService;
import com.biperf.core.service.multimedia.MultimediaService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.engine.ChallengePointCalculationResult;
import com.biperf.core.service.promotion.engine.GoalCalculationResult;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.security.EncryptionService;
import com.biperf.core.service.ssi.SSIContestParticipantService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.translation.TranslationService;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.user.ContactType;
import com.biperf.core.ui.user.PaxContactType;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ChallengepointUtil;
import com.biperf.core.utils.ClaimApproveUtils;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.HtmlUtils;
import com.biperf.core.utils.LocaleUtils;
import com.biperf.core.utils.NumberFormatUtil;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.SecurityUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ChallengepointPaxAwardValueBean;
import com.biperf.core.value.ChallengepointPaxValueBean;
import com.biperf.core.value.ssi.SSIContestPaxProgressDetailValueBean;
import com.biperf.core.value.ssi.SSIContestProgressValueBean;
import com.biperf.core.value.ssi.SSIContestStackRankPaxValueBean;
import com.biperf.core.value.ssi.SSIContestStackRankPayoutValueBean;
import com.biperf.core.value.ssi.SSIContestUniqueCheckValueBean;
import com.biperf.core.value.ssi.SSIPaxContestLevelValueBean;
import com.biperf.core.value.ssi.SSIPaxDTGTActivityProgressValueBean;
import com.biperf.messenger.client.DestinationCountry;
import com.biperf.messenger.client.InvalidClientApplicationCodeResult;
import com.biperf.messenger.client.NumberBlacklistedResult;
import com.biperf.messenger.client.NumberSuspendedResult;
import com.biperf.messenger.client.SendMessageFailure;
import com.biperf.messenger.client.SendMessageResult;
import com.biperf.messenger.client.SmsMessageSender;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.CmsUtil;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * MailingServiceImpl
 * 
 *
 */
public class MailingServiceImpl implements MailingService
{
  private static final Log log = LogFactory.getLog( MailingServiceImpl.class );

  public static final String PARAGRAPH_START = "<p>";
  public static final String PARAGRAPH_END = "</p>";
  private static final String WHITESPACE = " ";
  public static final String LINE_BREAK = "<br>";
  public static final String SSI_CONTEST_PAX_CM_ASSET_CODE = "ssi_contest.participant";
  private static final String ECARD_TYPE_IMAGE = "image";
  private static final String ECARD_TYPE_VIDEO = "video";
  private static final String DEFAULT_VIDEO_IMAGE = "/assets/skins/default/img/email_videoThumb.jpg";

  protected static final String KEY_IS_POINTS = "isPoints";
  protected static final String KEY_PAYOUT_AMOUNT = "payoutAmount";
  protected static final String KEY_IS_OTHER = "isOther";
  protected static final String KEY_PAYOUT_DESCRIPTION = "payoutDescription";

  private MailingDAO mailingDAO = null;
  private SystemVariableService systemVariableService = null;
  private CommLogService commLogService = null;
  private ProcessService processService = null;
  private PersonalizationService personalizationService = null;
  private EmailService emailService = null;
  private MessageService messageService = null;
  private DesignThemeService designThemeService = null;
  private NodeDAO nodeDAO = null;
  private ChallengepointAwardDAO challengepointAwardDAO;
  private SmsMessageSender smsMessageSender;
  private MailingBatchDAO mailingBatchDAO = null;
  private CMAssetService cmAssetService = null;
  private ClaimDAO claimDAO = null;
  private TranslationService translationService;
  private DestinationCountryCache destinationCountryCache;
  private CelebrationService celebrationService;
  private EncryptionService encryptionService;
  @Autowired
  private MTCVideoService mtcVideoService;

  public MTCVideoService getMtcVideoService()
  {
    return mtcVideoService;
  }

  public void setMtcVideoService( MTCVideoService mtcVideoService )
  {
    this.mtcVideoService = mtcVideoService;
  }

  /**
   * Submit mailing. Mailing object has to built with mailingRecipients and also have message.
   * Additionally: MailingRecipients need to have any recipient specific data that needs to be
   * populated. ObjectMap needs to have any mailing level data that needs to be populated.
   * 
   * @param mailing Mailing object - for most purposes (except email wizard mailings) it needs to
   *          contain: mailingRecipients and message
   * @param mailingLevelPersonalizationData Needs to have any mailing level data that needs to be
   *          populated. example - key = "color" | value = "blue" - the template would contain
   *          something like "The car is ${color}." and it becomes "The car is blue." * Take
   *          specific note of the token in the template v. the representation in the map!
   * @return Mailing returns mailing if successfully saved and scheduled for mailing (*** this does
   *         not mean it has been sent yet! To verify that a mailing recipient has been sent a
   *         mailing check the mailing recipient sentDate after MailingProcess has run).
   */
  @Override
  public Mailing submitMailing( Mailing mailing, Map mailingLevelPersonalizationData )
  {
    if ( mailing != null )
    {
      return submitMailing( mailing, mailingLevelPersonalizationData, UserManager.getUserId() );
    }
    else
    {
      log.error( "Mailing is null and can't submit mailing." );
      return null;
    }
  }

  /**
   * submitMailing with RunAsUser user ID as an additional argument for Welcome Summary Email
   * Process
   * 
   * @param mailing
   * @param mailingLevelPersonalizationData
   * @param runByUserId
   * @return Mailing
   */
  @Override
  public Mailing submitMailing( Mailing mailing, Map mailingLevelPersonalizationData, Long runByUserId )
  {
    try
    {
      // set mailing level data
      // have to have mailing guid set for mailingMessageLocales
      Mailing prePersonalizedMailing = personalizationService.preProcessMailing( mailing, mailingLevelPersonalizationData );
      if ( prePersonalizedMailing.getMailingRecipients().size() > 0 )
      {
        return saveAndScheduleMailing( prePersonalizedMailing, runByUserId );
      }
      else
      {
        return null;
      }
    }
    catch( Exception e )
    {
      log.error( "An error occured while submitMailing." + "For mailingId: " + mailing.getId(), e );
      return null;
    }

  }

  /**
   * Resubmit mailing - used to resubmit a mailing for a user to resend it
   * 
   * @param prevMailing Previous mailing object (loaded from DB)
   * @param user User mailing is going to be resent to.
   * @param newEmailAddress if this is set then the email will be resent to the newEmailAddress;
   *                        ; otherwise the email will be resent to the same email address.
   * @param createCommLog if true a new CommLog will be created;
   * @return Mailing returns mailing if successfully saved and scheduled for mailing (*** this does
   *         not mean it has been sent yet! To verify that a mailing recipient has been sent a
   *         mailing check the mailing recipient sentDate after MailingProcess has run).
   */
  @Override
  public Mailing reSubmitMailing( Mailing prevMailing, User user, String newEmailAddress, boolean createCommLog )
  {

    Mailing mailing = new Mailing();
    Set prevMailingRecipients = prevMailing.getMailingRecipients();

    String sender = new String( prevMailing.getSender() );
    mailing.setSender( sender );
    mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );

    // have to iterate through previous mailing to get previous mailing
    // recipient specific data
    Long userId = null;
    if ( user != null )
    {
      userId = user.getId();
    }
    MailingRecipient prevMailingRecipient = null;
    MailingRecipient mailingRecipient = null;

    Iterator mailingRecipientsIter = prevMailingRecipients.iterator();
    while ( mailingRecipientsIter.hasNext() )
    {
      prevMailingRecipient = (MailingRecipient)mailingRecipientsIter.next();
      Long mailingRecipientUserId = null;
      if ( prevMailingRecipient.getUser() != null )
      {
        mailingRecipientUserId = prevMailingRecipient.getUser().getId();
      }
      if ( userId == null && mailingRecipientUserId == null || userId != null && userId.equals( mailingRecipientUserId ) )
      {
        String emailAddress = newEmailAddress;
        if ( StringUtils.isBlank( emailAddress ) )
        {
          emailAddress = getMailingRecipientEmailAddress( prevMailingRecipient );
        }
        if ( StringUtils.isNotBlank( emailAddress ) )
        {
          mailingRecipient = buildPreviewMailingRecipient( emailAddress );
        }
        else
        {
          sendPMErrorMailing( " [ " + MailingType.lookup( MailingType.RESEND ).getName() + " Id: NONE" + "] Mailing Issues",
                              buildErrorLogString( user, " - No primary e-mail address configured.", "<p/>" ),
                              buildErrorLogString( user, " - No primary e-mail address configured.", "\n" ) );
          return null;
        }
        mailingRecipient.setUser( prevMailingRecipient.getUser() );
        mailingRecipient.setLocale( prevMailingRecipient.getLocale() );
        mailingRecipient.setClaimRecipientId( prevMailingRecipient.getClaimRecipientId() );
        // populate dataset
        Iterator mailingRecipientDataSetIter = prevMailingRecipient.getMailingRecipientDataSet().iterator();
        while ( mailingRecipientDataSetIter.hasNext() )
        {
          MailingRecipientData prevMailingRecipientData = (MailingRecipientData)mailingRecipientDataSetIter.next();
          MailingRecipientData mailingRecipientData = new MailingRecipientData();
          mailingRecipientData.setKey( prevMailingRecipientData.getKey() );
          mailingRecipientData.setValue( prevMailingRecipientData.getValue() );
          mailingRecipient.addMailingRecipientData( mailingRecipientData );
        }
        mailing.addMailingRecipient( mailingRecipient );
        break;
      }
      else
      {
        continue;
      }
    }
    mailing.setMessage( prevMailing.getMessage() );

    String subject = "";
    String htmlBody = "";
    String plainBody = "";
    String smsBody = "";
    StringBuffer htmlBodyWithWrapper = new StringBuffer( "" );

    Iterator mailingMessageLocaleIter = prevMailing.getMailingMessageLocales().iterator();
    while ( mailingMessageLocaleIter.hasNext() )
    {
      MailingMessageLocale prevMailingMessageLocale = (MailingMessageLocale)mailingMessageLocaleIter.next();

      if ( user != null && user.getLanguageType() == null || prevMailingMessageLocale.getLocale() == null )
      {
        sendPMErrorMailing( " [ " + MailingType.lookup( MailingType.RESEND ).getName() + " Id: NONE" + " ] Mailing Issues",
                            buildErrorLogString( user, " - No language preference configured.", "<p/>" ),
                            buildErrorLogString( user, " - No language preference configured.", "\n" ) );
        return null;
      }

      if ( user == null || user.getLanguageType().getCode().equals( prevMailingMessageLocale.getLocale() ) )
      {
        subject = personalizationService.personalize( mailingRecipient, prevMailingMessageLocale.getSubject() );
        htmlBody = personalizationService.personalize( mailingRecipient, prevMailingMessageLocale.getHtmlMessage() );
        // Put wrapper around htmlBody
        String wrapperHeader = systemVariableService.getPropertyByName( SystemVariableService.EMAIL_WRAPPER_HEADER ).getStringVal();

        String css = getEmailCssForUser( mailingRecipient.getUser() );
        wrapperHeader = wrapperHeader.replaceAll( "\\$\\{css\\}", css );

        String emailClientLogo = getEmailClientLogo();
        wrapperHeader = wrapperHeader.replaceAll( "\\$\\{emailClientLogo\\}", emailClientLogo );

        String siteURL = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
        wrapperHeader = wrapperHeader.replaceAll( "\\$\\{siteURL\\}", siteURL );

        String wrapperFooterData = systemVariableService.getPropertyByName( SystemVariableService.EMAIL_WRAPPER_FOOTER ).getStringVal();
        String wrapperFooter = MessageFormat.format( wrapperFooterData, new Object[] { cmAssetService.getString( "system.generalerror",
                                                                                                                 "DONOT_REPLY",
                                                                                                                 CmsUtil.getLocale( mailingRecipient.getUser().getLanguageType() != null
                                                                                                                     ? mailingRecipient.getUser().getLanguageType().getCode()
                                                                                                                     : systemVariableService.getDefaultLanguage().getStringVal() ),
                                                                                                                 true ) } );
        String emailBodyPhoto = getEmailBodyPhoto();
        wrapperFooter = wrapperFooter.replaceAll( "\\$\\{emailPhoto\\}", emailBodyPhoto );

        htmlBodyWithWrapper.append( wrapperHeader ).append( htmlBody ).append( wrapperFooter );
        plainBody = personalizationService.personalize( mailingRecipient, prevMailingMessageLocale.getPlainMessage() );
        smsBody = personalizationService.personalize( mailingRecipient, prevMailingMessageLocale.getTextMessage() );
        break;
      }
    }

    // mailing must have GUID set before adding mailingMessageLocale
    mailing.setGuid( GuidUtils.generateGuid() );

    MailingMessageLocale mailingMessageLocale = new MailingMessageLocale();
    mailingMessageLocale.setHtmlMessage( htmlBodyWithWrapper.toString() );
    mailingMessageLocale.setTextMessage( smsBody );
    mailingMessageLocale.setPlainMessage( plainBody );
    mailingMessageLocale.setSubject( subject );
    if ( user == null )
    {
      mailingMessageLocale.setLocale( MailingMessageLocale.DEFAULT_LOCALE );
    }
    else
    {
      mailingMessageLocale.setLocale( user.getLanguageType() != null ? user.getLanguageType().getCode() : systemVariableService.getDefaultLanguage().getStringVal() );
    }
    mailing.addMailingMessageLocale( mailingMessageLocale );

    if ( createCommLog )
    {
      mailing.setMailingType( MailingType.lookup( MailingType.RESEND_AND_LOG ) );
    }
    else
    {
      mailing.setMailingType( MailingType.lookup( MailingType.RESEND ) );
    }

    try
    {
      return saveAndScheduleMailing( mailing, UserManager.getUserId() );
    }
    catch( Exception e )
    {

      sendPMErrorMailing( buildErrorMailingSubject( mailing ),
                          buildErrorLogString( user, " - Error occurred while processing a resubmit mailing for submittal.", "<p/>" ),
                          buildErrorLogString( user, " - Error occurred while processing a resubmit mailing for submittal.", "\n" ) );

      log.error( "An error occured while saving and scheduling a system message." + "For mailingId: " + mailing.getId(), e );
      return null;
    }

  }

  /**
   * Submit message for system mailing (PM mailing)
   * 
   * @param subject Subject text
   * @param textBody Body text
   * @param htmlBody Body html
   * @return Mailing returns mailing if successfully saved and scheduled for mailing (*** this does
   *         not mean it has been sent yet! To verify that a mailing recipient has been sent a
   *         mailing check the mailing recipient sentDate after MailingProcess has run).
   */
  @Override
  public Mailing submitSystemMailing( String subject, String htmlBody, String textBody )
  {
    Mailing mailing = new Mailing();

    mailing.setSender( systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_PERSONAL_DISPLAY_NAME ).getStringVal() );
    mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );

    Set mailingRecipients = new HashSet();
    String[] systemEmailAddresses = this.getSystemEmailAddress().split( "," );
    for ( int i = 0; i < systemEmailAddresses.length; i++ )
    {
      String currentSystemEmailAddress = systemEmailAddresses[i];
      if ( StringUtils.isNotBlank( currentSystemEmailAddress ) )
      {
        MailingRecipient mailingRecipient = this.buildPreviewMailingRecipient( currentSystemEmailAddress.trim() );
        mailingRecipients.add( mailingRecipient );
      }
    }

    mailing.addMailingRecipients( mailingRecipients );
    // Bug# 27740 Start
    subject = "[ " + systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() + " ] " + subject;
    textBody = "[ " + systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() + " ] " + "[ "
        + systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() + " ] " + "\n" + textBody;
    htmlBody = "[ " + systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() + " ] " + "[ "
        + systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() + " ] " + "\n" + htmlBody;
    // Bug# 27740 End
    mailing.setGuid( GuidUtils.generateGuid() );
    MailingMessageLocale mailingMessageLocale = new MailingMessageLocale();
    mailingMessageLocale.setHtmlMessage( htmlBody );
    mailingMessageLocale.setPlainMessage( textBody );
    mailingMessageLocale.setTextMessage( "" );
    mailingMessageLocale.setSubject( subject );
    mailingMessageLocale.setLocale( systemVariableService.getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal() );

    mailing.addMailingMessageLocale( mailingMessageLocale );

    mailing.setMailingType( MailingType.lookup( MailingType.SYSTEM ) );
    try
    {
      return saveAndScheduleMailing( mailing, UserManager.getUserId() );
    }
    catch( Exception e )
    {

      // Do not send another mail on exception to prevent endless loops
      log.error( "An error occured while saving and scheduling a system message:\n" + subject + "\n" + textBody + "\n", e );
      return null;
    }

  }

  /**
   * THIS JOB IS USED BY THE MAIL PROCESS ONLY (DO NOT USE) Process mailing given mailing id
   * 
   * @param mailingId
   */
  @Override
  public void processMailing( Long mailingId )
  {

    StringBuffer textErrorLog = new StringBuffer();
    StringBuffer htmlErrorLog = new StringBuffer();

    // load mailing from database
    Mailing mailing = mailingDAO.getMailingById( mailingId );
    Set mailingMessageLocales = mailing.getMailingMessageLocales();
    Iterator mailingMessageLocalesIter = mailingMessageLocales.iterator();

    // load mailingLocales set into map
    HashMap mailingLocalesMap = new HashMap();
    while ( mailingMessageLocalesIter.hasNext() )
    {
      MailingMessageLocale mailingMessageLocale = (MailingMessageLocale)mailingMessageLocalesIter.next();
      mailingLocalesMap.put( mailingMessageLocale.getLocale(), mailingMessageLocale );
    }

    Set mailingRecipients = mailing.getMailingRecipients();
    Iterator mailingRecipientsIter = mailingRecipients.iterator();

    // build & send
    while ( mailingRecipientsIter.hasNext() )
    {
      MailingRecipient mailingRecipient = (MailingRecipient)mailingRecipientsIter.next();

      // check mailing recipients - if they do not have user objects attached
      // then they are preview messages
      if ( mailing.isNonStandardMailing() )
      {

        processNonStandardMailing( mailing, mailingRecipient, htmlErrorLog, textErrorLog );

      }
      else
      {
        processStandardMailing( mailing, mailingRecipient, mailingLocalesMap, htmlErrorLog, textErrorLog );
      }

    }

    // Update mailing object with mailingRecipients timestamped
    mailingDAO.saveMailing( mailing );

    // see if we have any errors to send to PM
    if ( textErrorLog.length() > 0 && !isSystemMailing( mailing ) )
    {
      sendPMErrorMailing( buildErrorMailingSubject( mailing ), htmlErrorLog.toString(), textErrorLog.toString() );
    }

  }

  private boolean isSystemMailing( Mailing mailing )
  {
    return mailing.getMailingType().getCode().equals( MailingType.SYSTEM );
  }

  private void processStandardMailing( Mailing mailing, MailingRecipient mailingRecipient, Map mailingLocalesMap, StringBuffer htmlErrorLog, StringBuffer textErrorLog )
  {
    MailingMessageLocale mailingMessageLocale = (MailingMessageLocale)mailingLocalesMap.get( mailingRecipient.getLocale() );

    if ( mailingMessageLocale == null )
    {
      Set mailingMessageLocales = mailing.getMailingMessageLocales();
      Iterator mailingMessageLocalesIter = mailingMessageLocales.iterator();
      while ( mailingMessageLocalesIter.hasNext() )
      {
        mailingMessageLocale = (MailingMessageLocale)mailingMessageLocalesIter.next();
      }

    }

    /** EMAIL * */
    if ( !mailing.isSendSMSOnly() )
    {
      mailing.setMessageTypeSMS( false );
      String userEmailAddress = getMailingRecipientEmailAddress( mailingRecipient );
      // BugFix 20962,Do not mark up with the Headers and Footers For email which is being
      // resending,mailing data has already contains HTML wrapper and footer wrapperd around.
      EmailBody htmlBody = null;
      String htmlBodyCommLog = null;
      if ( userEmailAddress != null )
      {
        EmailHeader emailHeader = new EmailHeader();
        emailHeader.setPersonal( systemVariableService.getPropertyByName( SystemVariableService.INCENTIVE_PERSONAL_DISPLAY_NAME ).getStringVal() );
        emailHeader.setSender( getSystemIncentiveEmailAddress() );
        emailHeader.setRecipients( new String[] { userEmailAddress } );
        emailHeader.setSubject( getSubjectPrefix() + personalizationService.personalize( mailingRecipient, mailingMessageLocale.getSubject() ) );
        EmailBody textBody = new TextEmailBody( personalizationService.personalize( mailingRecipient, mailingMessageLocale.getPlainMessage() ) );

        String textBodyCommLog = personalizationService.personalize( mailingRecipient, mailingMessageLocale.getPlainMessage(), true );
        String wrapperHeader = systemVariableService.getPropertyByName( SystemVariableService.EMAIL_WRAPPER_HEADER ).getStringVal();

        String css = getEmailCssForUser( mailingRecipient.getUser() );
        wrapperHeader = wrapperHeader.replaceAll( "\\$\\{css\\}", css );

        String emailClientLogo = getEmailClientLogo();
        wrapperHeader = wrapperHeader.replaceAll( "\\$\\{emailClientLogo\\}", emailClientLogo );

        String siteURL = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
        wrapperHeader = wrapperHeader.replaceAll( "\\$\\{siteURL\\}", siteURL );

        String wrapperFooterData = systemVariableService.getPropertyByName( SystemVariableService.EMAIL_WRAPPER_FOOTER ).getStringVal();
        String wrapperFooter = MessageFormat.format( wrapperFooterData, new Object[] { cmAssetService.getString( "system.generalerror",
                                                                                                                 "DONOT_REPLY",
                                                                                                                 CmsUtil.getLocale( mailingMessageLocale.getLocale() != null
                                                                                                                     ? mailingMessageLocale.getLocale()
                                                                                                                     : systemVariableService.getDefaultLanguage().getStringVal() ),
                                                                                                                 true ) } );
        String emailBodyPhoto = getEmailBodyPhoto();
        wrapperFooter = wrapperFooter.replaceAll( "\\$\\{emailPhoto\\}", emailBodyPhoto );

        String personalizedHtmlMessage = personalizationService.personalize( mailingRecipient, mailingMessageLocale.getHtmlMessage() );
        String personalizedHtmlMessageCommLog = personalizationService.personalize( mailingRecipient, mailingMessageLocale.getHtmlMessage(), true );

        // BugFix 20962,Do not mark up with the Headers and Footers For email which is being
        // resending,mailing data has already contains HTML wrapper and footer wrapperd around.
        if ( mailing.getMailingType() != null && mailing.getMailingType().getCode().equals( MailingType.RESEND_AND_LOG ) )
        {
          htmlBody = new TextEmailBody( personalizedHtmlMessage );
          htmlBodyCommLog = personalizedHtmlMessageCommLog;
        }
        else
        {
          htmlBody = new TextEmailBody( wrapperHeader + personalizedHtmlMessage + wrapperFooter );
          htmlBodyCommLog = wrapperHeader + personalizedHtmlMessageCommLog + wrapperFooter;
        }

        mailMessage( emailHeader, textBody, htmlBody, mailing, mailingRecipient, false, true, htmlErrorLog, textErrorLog, textBodyCommLog, htmlBodyCommLog );

      }
      else
      {

        // write error and log if this is not a resend or smsonly
        if ( !mailing.getMailingType().getCode().equals( MailingType.RESEND ) && !mailing.isSendSMSOnly() )
        {
          buildErrorLogString( mailingRecipient.getUser(), " - The primary email address is not configured for this user and e-mail was not sent.", htmlErrorLog, textErrorLog );
        }

        // store text version of mailing for comm log - not html
        this.saveCommLog( mailing,
                          personalizationService.personalize( mailingRecipient, mailingMessageLocale.getSubject(), true ),
                          mailingRecipient,
                          personalizationService.personalize( mailingRecipient, mailingMessageLocale.getHtmlMessage(), true ),
                          true,
                          personalizationService.personalize( mailingRecipient, mailingMessageLocale.getPlainMessage() ) );
      }

    }

    /** Send Text Message * */
    // if user configured for text mesgs & also message sms group type set
    if ( mailing.getMessage() != null && mailing.getMessage().getMessageSMSGroupType() != null
        && ( mailing.getMessage().getMessageSMSGroupType().getCode().equals( MessageSMSGroupType.BUDGET_END_ALERT )
            || mailing.getMessage().getMessageSMSGroupType().getCode().equals( MessageSMSGroupType.PROMOTIONAL_MESSAGE )
            || mailing.getMessage().getMessageSMSGroupType().getCode().equals( MessageSMSGroupType.DEPOSIT_NOTIFICATION )
            || mailing.getMessage().getMessageSMSGroupType().getCode().equals( MessageSMSGroupType.RECOGNITION_RECEIVED )
            || mailing.getMessage().getMessageSMSGroupType().getCode().equals( MessageSMSGroupType.GOAL_REMINDERS ) )
        && isUserConfiguredForSMSMessage( mailing, mailingRecipient ) )
    {
      String mobileNumber = getMailingRecipientMobilePhone( mailingRecipient.getUser() );
      if ( mobileNumber != null )
      {

        EmailBody smsBody = new TextEmailBody( personalizationService.personalize( mailingRecipient, mailingMessageLocale.getTextMessage() ) );
        String smsSubject = personalizationService.personalize( mailingRecipient, mailingMessageLocale.getSubject() );
        int totalCharacterCount = smsBody.getBodyText().length();

        if ( totalCharacterCount > 160 )
        {
          buildErrorLogString( mailingRecipient.getUser(),
                               " - The text email address (SMS) is too long - can't be over 160 combined subject/body [character count: " + totalCharacterCount + "].",
                               htmlErrorLog,
                               textErrorLog );
          mailing.setMessageTypeSMS( true );
          this.saveCommLog( mailing,
                            mailingMessageLocale.getSubject(),
                            mailingRecipient,
                            personalizationService.personalize( mailingRecipient, mailingMessageLocale.getHtmlMessage() ),
                            true,
                            personalizationService.personalize( mailingRecipient, mailingMessageLocale.getPlainMessage() ) );
        }

        sendSmsMessage( mailing, smsSubject, mailingRecipient, smsBody.getBodyText(), true );
      }
      else
      {
        if ( !mailing.getMailingType().getCode().equals( MailingType.RESEND ) )
        {
          buildErrorLogString( mailingRecipient.getUser(), " - Participant mobile phone number  is not configured and this user has been configured for text messaging.", htmlErrorLog, textErrorLog );
          mailing.setMessageTypeSMS( true );
          // save commLog message - get what message body would've been
          this.saveCommLog( mailing,
                            mailingMessageLocale.getSubject(),
                            mailingRecipient,
                            personalizationService.personalize( mailingRecipient, mailingMessageLocale.getHtmlMessage() ),
                            true,
                            personalizationService.personalize( mailingRecipient, mailingMessageLocale.getPlainMessage() ) );

        }
      }
    }
  }

  private void processNonStandardMailing( Mailing mailing, MailingRecipient mailingRecipient, StringBuffer htmlErrorLog, StringBuffer textErrorLog )
  {
    EmailHeader emailHeader = null;
    EmailBody textBody = null;
    EmailBody htmlBody = null;

    String userEmailAddress = getMailingRecipientEmailAddress( mailingRecipient );

    if ( userEmailAddress == null )
    {
      // get out of if address is null
      this.buildErrorLogString( mailingRecipient.getUser(), "- E-mail address is not configured.", htmlErrorLog, textErrorLog );
      // don't write comm log if this mailing type

      return;
    }

    MailingMessageLocale mailingMessageLocale = (MailingMessageLocale)mailing.getMailingMessageLocales().iterator().next();

    emailHeader = new EmailHeader();
    emailHeader.setPersonal( mailing.getSender() );
    emailHeader.setSender( getSystemIncentiveEmailAddress() );
    emailHeader.setRecipients( new String[] { userEmailAddress } );

    emailHeader.setSubject( getSubjectPrefix() + mailingMessageLocale.getSubject() );

    textBody = new TextEmailBody( mailingMessageLocale.getPlainMessage() );
    htmlBody = new TextEmailBody( mailingMessageLocale.getHtmlMessage() );

    mailMessage( emailHeader, textBody, htmlBody, mailing, mailingRecipient, false, false, htmlErrorLog, textErrorLog );
    /** SMS Mail * */
    // if mailing is a promotion mailing type check for SMS mail and user is configured for SMS
    if ( mailing.getMailingType() != null && !mailing.getMailingType().getCode().equals( MailingType.SYSTEM ) && isUserConfiguredForSMSMessage( mailing, mailingRecipient ) )
    {
      String mobileNumber = getMailingRecipientMobilePhone( mailingRecipient.getUser() );
      mailing.setMessageTypeSMS( true );
      if ( mobileNumber != null )
      {

        EmailBody smsBody = new TextEmailBody( personalizationService.personalize( mailingRecipient, mailingMessageLocale.getTextMessage() ) );

        int totalCharacterCount = smsBody.getBodyText().length();

        if ( totalCharacterCount > 160 )
        {
          buildErrorLogString( mailingRecipient.getUser(),
                               " - The text email address (SMS) is too long - can't be over 160 combined subject/body [character count: " + totalCharacterCount + "].",
                               new StringBuffer( "[ " + systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() + " ]" + "[ "
                                   + systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() + " ] " + htmlErrorLog ),
                               textErrorLog );

          this.saveCommLog( mailing,
                            mailingMessageLocale.getSubject(),
                            mailingRecipient,
                            personalizationService.personalize( mailingRecipient, mailingMessageLocale.getHtmlMessage() ),
                            true,
                            personalizationService.personalize( mailingRecipient, mailingMessageLocale.getPlainMessage() ) );
        }

        sendSmsMessage( mailing, mailingMessageLocale.getSubject(), mailingRecipient, smsBody.getBodyText(), true );
      }
      else
      {
        if ( !mailing.getMailingType().getCode().equals( MailingType.RESEND ) )
        {
          buildErrorLogString( mailingRecipient.getUser(), " - Participant mobile phone number  is not configured and this user has been configured for text messaging.", htmlErrorLog, textErrorLog );

          // save commLog message - get what message body would've been
          this.saveCommLog( mailing,
                            mailingMessageLocale.getSubject(),
                            mailingRecipient,
                            personalizationService.personalize( mailingRecipient, mailingMessageLocale.getHtmlMessage() ),
                            true,
                            personalizationService.personalize( mailingRecipient, mailingMessageLocale.getPlainMessage() ) );

        }
      }
    }
  }

  private boolean isUserConfiguredForSMSMessage( Mailing mailing, MailingRecipient mailingRecipient )
  {

    boolean isUserConfiguredForSMSMessage = false;

    if ( ! ( mailingRecipient.getUser() instanceof Participant ) )
    {
      return isUserConfiguredForSMSMessage;
    }

    // then check to make sure that user wants SMS for this message
    Participant participant = (Participant)mailingRecipient.getUser();
    Set participantCommunicationPrefs = null;
    if ( mailingRecipient.getUser() instanceof Participant )
    {
      participant = (Participant)mailingRecipient.getUser();
      participantCommunicationPrefs = participant.getParticipantCommunicationPreferences();
    }

    if ( participantCommunicationPrefs != null )
    {
      Iterator commPrefsIter = participantCommunicationPrefs.iterator();

      while ( commPrefsIter.hasNext() && !isUserConfiguredForSMSMessage )
      {

        ParticipantCommunicationPreference partCommunicationPref = (ParticipantCommunicationPreference)commPrefsIter.next();

        if ( partCommunicationPref.getMessageSMSGroupType() != null )
        {
          if ( mailing.getMessage() == null )
          {
            // Ad-hoc message
            if ( partCommunicationPref.getMessageSMSGroupType().equals( MessageSMSGroupType.lookup( "promotionalMessages" ) ) )
            {
              isUserConfiguredForSMSMessage = true;
            }
          }
          else if ( partCommunicationPref.getMessageSMSGroupType().equals( mailing.getMessage().getMessageSMSGroupType() ) )
          {
            isUserConfiguredForSMSMessage = true;
          }
        }

      }
    }

    return isUserConfiguredForSMSMessage;
  }

  /**
   * Helper method to build preview mailing recipient
   * 
   * @param previewEmail preview email address
   * @return MailingRecipient mailing recipient
   */
  @Override
  public MailingRecipient buildPreviewMailingRecipient( String previewEmail )
  {
    return buildMailingRecipient( null, null, previewEmail );
  }

  /**
   * Helper method to build mailing recipient
   * 
   * @param user User to be attached to mailingRecipient (has e-mail addresses)
   * @param mailingRecipientData
   * @return MailingRecipient
   */
  @Override
  public MailingRecipient buildMailingRecipient( User user, Set mailingRecipientData )
  {
    return buildMailingRecipient( user, mailingRecipientData, null );
  }

  /**
   * Get mailing by id
   * 
   * @param id
   * @return Mailing
   */
  @Override
  public Mailing getMailingById( Long id )
  {
    return mailingDAO.getMailingById( id );
  }

  /**
   * Save mailing
   * 
   * @param mailing
   * @return Mailing
   */
  @Override
  public Mailing saveMailing( Mailing mailing )
  {
    return mailingDAO.saveMailing( mailing );
  }

  /**
   * Get all mailing attachment info for mailings that have been sent in which contains fullFileName
   * which can be used to delete temp files previously written on the app server
   * 
   * @return List of MailingAttachmentInfo
   */
  @Override
  public List getAllMailingAttachmentInfoAlreadySent()
  {
    return mailingDAO.getAllMailingAttachmentInfoAlreadySent();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.mailing.MailingDAO#getUsersWhoReceivedMessage(java.lang.Long)
   * @param messageId
   * @return List of User who have received the message
   */
  @Override
  public List getUsersWhoReceivedMessage( Long messageId )
  {
    return mailingDAO.getUsersWhoReceivedMessage( messageId );
  }

  /**
   * @param mailingId
   * @return List of User who have successfully received the message (dateSent not null on the
   *         Mailing Recipient)
   */
  @Override
  public List getUsersWhoSuccessfullyReceivedMessage( Long mailingId )
  {
    return mailingDAO.getUsersWhoSuccessfullyReceivedMessage( mailingId );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.email.MailingService#getRunAsUserIdByMailingId(java.lang.Long)
   * @param mailingId
   * @return Long - the user Id of the admin user who launched the Welcome Email Process
   */
  @Override
  public Long getRunAsUserIdByMailingId( Long mailingId )
  {
    return mailingDAO.getRunAsUserIdByMailingId( mailingId );
  }

  /**
   * Overridden from
   * 
   * @param claim
   * @param participant
   * @param awardAmount
   * @param processStartDate
   * @param processEndDate
   * @return Set of MailingRecipients
   */
  @Override
  public MailingRecipient buildMailingRecipientForProductClaimEmail( ProductClaim claim, Participant participant, Long awardAmount, Date processStartDate, Date processEndDate, Promotion promotion )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();

    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setUser( participant );
    if ( participant.getLanguageType() != null )
    {
      mailingRecipient.setLocale( participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode() );
    }
    else
    {
      mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
    }

    Map dataMap = new HashMap();
    dataMap.put( "firstName", participant.getFirstName() );
    dataMap.put( "lastName", participant.getLastName() );
    if ( promotion.getPromoNameAssetCode() != null )
    {
      Locale locale = CmsUtil.getLocale( mailingRecipient.getLocale() );
      String promotionName = cmAssetService.getString( promotion.getPromoNameAssetCode(), Promotion.PROMOTION_NAME_KEY_PREFIX, locale, true );
      dataMap.put( "programName", StringEscapeUtils.unescapeHtml4( promotionName ) );
    }
    if ( processStartDate != null )
    {
      dataMap.put( "startDate", com.biperf.core.utils.DateUtils.toDisplayString( processStartDate, LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );
    }
    if ( processEndDate != null )
    {
      dataMap.put( "endDate", com.biperf.core.utils.DateUtils.toDisplayString( processEndDate, LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );
    }

    if ( awardAmount != null )
    {
      String awardAmt = awardAmount.toString();
      if ( awardAmount.longValue() > 1 )
      {
        dataMap.put( "manyAwardAmount", "TRUE" );
        awardAmt = NumberFormatUtil.getUserLocaleBasedNumberFormat( awardAmount, LocaleUtils.getLocale( mailingRecipient.getLocale() ) );
      }
      dataMap.put( "awardAmount", String.valueOf( awardAmt ) );
      dataMap.put( "mediaType", promotion.getAwardType().getAbbr() );
    }

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "id", claim.getId() );
    clientStateParameterMap.put( "submitterId", participant.getId() );

    String link = ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                        "/claim/productClaimDetail.do?method=display",
                                                        clientStateParameterMap );
    dataMap.put( "siteLink", link );
    dataMap.put( "claimNumber", claim.getClaimNumber() );
    dataMap.put( "promotionId", promotion.getId().toString() );

    // Keep track if product were approved or denied, email needs to show a different message
    // if all products were denied, approved or a combination of both.
    boolean anyClaimProductApproved = false;
    boolean anyClaimProductDenied = false;
    for ( Iterator iter = claim.getClaimProducts().iterator(); iter.hasNext(); )
    {
      ClaimProduct claimProduct = (ClaimProduct)iter.next();
      if ( claimProduct.getApprovalStatusType().getCode().equals( ApprovalStatusType.APPROVED ) )
      {
        anyClaimProductApproved = true;
      }
      else if ( claimProduct.getApprovalStatusType().getCode().equals( ApprovalStatusType.DENIED ) )
      {
        anyClaimProductDenied = true;
      }
    } // for ClaimProducts

    // if either is false, then show the status that was true
    if ( !anyClaimProductApproved || !anyClaimProductDenied )
    {
      if ( anyClaimProductApproved )
      {
        dataMap.put( "approvalStatus", "approved" );
      }
      else if ( anyClaimProductDenied )
      {
        dataMap.put( "approvalStatus", "denied" );
      }
    }

    mailingRecipient.addMailingRecipientDataFromMap( dataMap );

    return mailingRecipient;
  }

  @Override
  public Mailing buildReportExtractNotificationMailing( FileStore fileStore, Message message )
  {
    Mailing mailing = new Mailing();
    mailing.setMessage( message );
    mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
    mailing.setSender( "Report Extraction System Mailbox" );
    mailing.setMailingType( MailingType.lookup( MailingType.EMAIL_WIZARD ) );
    mailing.setGuid( GuidUtils.generateGuid() );

    MailingRecipient mailRecipient = buildMailingRecipientForReportExtract( fileStore );
    mailing.addMailingRecipient( mailRecipient );
    addMailingLocales( mailing.getMailingRecipients(), message, mailing );
    return mailing;
  }

  @SuppressWarnings( "unchecked" )
  private MailingRecipient buildMailingRecipientForReportExtract( FileStore fileStore )
  {

    MailingRecipient mailingReceipient = new MailingRecipient();
    if ( fileStore.getUser().getLanguageType() != null )
    {
      mailingReceipient.setLocale( fileStore.getUser().getLanguageType().getCode() );
    }
    else
    {
      mailingReceipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
    }
    mailingReceipient.setGuid( GuidUtils.generateGuid() );

    Date nextDay = com.biperf.core.utils.DateUtils.getNextDay( fileStore.getDateGenerated() );
    String programName = "";
    String programUrl = "";

    programName = systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();
    programUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

    Map dataMap = new HashMap();

    dataMap.put( "reportName", fileStore.getUserFileName() );
    dataMap.put( "firstName", fileStore.getUser().getFirstName() );
    dataMap.put( "lastName", fileStore.getUser().getLastName() );
    dataMap.put( "extractDate", com.biperf.core.utils.DateUtils.toDisplayTimeStringWithoutSeconds( nextDay, UserManager.getLocale() ) );
    dataMap.put( "programName", programName );
    dataMap.put( "programUrl", programUrl );
    mailingReceipient.addMailingRecipientDataFromMap( dataMap );
    mailingReceipient.setUser( fileStore.getUser() );
    return mailingReceipient;
  }

  @Override
  public Mailing buildLeaderboardNotificationMailing( LeaderBoard leaderBoard, LeaderBoardParticipant lbPax, int paxCount, Message message, boolean createNewLB, String notifyMessage )
  {

    Mailing mailing = new Mailing();
    mailing.setMessage( message );
    // bug 3047 - Message should be sent for start date
    mailing.setDeliveryDate( new Timestamp( leaderBoard.getStartDate().getTime() ) );
    mailing.setSender( "Incentive System Mailbox" );
    mailing.setMailingType( MailingType.lookup( MailingType.EMAIL_WIZARD ) );
    mailing.setGuid( GuidUtils.generateGuid() );

    MailingRecipient mailRecipient = buildMailingRecipientForLeaderBoardCreateUpdateEmail( leaderBoard, lbPax, paxCount, createNewLB, notifyMessage );
    mailing.addMailingRecipient( mailRecipient );
    addMailingLocales( mailing.getMailingRecipients(), message, mailing );
    return mailing;
  }

  /**
   * @param leaderBoard
   * @param participant 
   * @return MailingRecipient
   */
  @SuppressWarnings( "unchecked" )
  private MailingRecipient buildMailingRecipientForLeaderBoardCreateUpdateEmail( LeaderBoard leaderBoard, LeaderBoardParticipant lbPax, int totalPax, boolean createNewLB, String notifyMessage )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    if ( lbPax.getUser().getLanguageType() != null )
    {
      mailingRecipient.setLocale( lbPax.getUser().getLanguageType().getCode() );
    }
    else
    {
      mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
    }
    mailingRecipient.setGuid( GuidUtils.generateGuid() );

    Locale locale = CmsUtil.getLocale( mailingRecipient.getLocale() );
    String createLabel = cmAssetService.getString( "leaderboard.label", "CREATED", locale, true );
    Map dataMap = new HashMap();
    String createUpdateText = createLabel;
    String newlyCreatedLB = createNewLB ? "true" : "false";
    // dataMap.put( "newlyCreatedLB", newlyCreatedLB );
    // dataMap.put( "updatedLB", updateLb );
    dataMap.put( "notificationMessage", notifyMessage );
    dataMap.put( "createUpdateText", createUpdateText );
    dataMap.put( "promotionName", leaderBoard.getName() );
    dataMap.put( "firstName", lbPax.getUser().getFirstName() );
    dataMap.put( "lastName", lbPax.getUser().getLastName() );
    dataMap.put( "asOfDate", com.biperf.core.utils.DateUtils.toDisplayDateString( lbPax.getAsOfDate(), com.biperf.core.utils.LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );
    dataMap.put( "score", lbPax.getScore() );
    dataMap.put( "rank", Integer.toString( lbPax.getLeaderBoardPaxRank() ) );
    dataMap.put( "total", Integer.toString( totalPax ) );
    dataMap.put( "ownerLastName", leaderBoard.getUser().getLastName() );
    dataMap.put( "ownerFirstName", leaderBoard.getUser().getFirstName() );
    dataMap.put( "create", newlyCreatedLB );

    Map clientStateParameterMap = new HashMap();
    String link = ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                        "/leaderBoardsDetailPage.do?method=getUserRole",
                                                        clientStateParameterMap );
    dataMap.put( "here", link );
    mailingRecipient.addMailingRecipientDataFromMap( dataMap );
    mailingRecipient.setUser( lbPax.getUser() );
    return mailingRecipient;
  }

  private void addMailingLocales( Set recipients, Message message, Mailing mailing )
  {
    for ( Iterator iter = recipients.iterator(); iter.hasNext(); )
    {
      MailingRecipient temp = (MailingRecipient)iter.next();
      MailingMessageLocale locale = new MailingMessageLocale();
      locale.setHtmlMessage( message.getI18nHtmlBody( temp.getLocale() ) );
      locale.setPlainMessage( message.getI18nPlainTextBody( temp.getLocale() ) );
      locale.setTextMessage( message.getI18nTextBody( temp.getLocale() ) );
      locale.setSubject( message.getI18nSubject( temp.getLocale() ) );
      locale.setLocale( temp.getLocale() );
      mailing.addMailingMessageLocale( locale );
    }
  }

  /**
   * 
   * @param alertMessage
   * @param participant
   * @return mailingRecipient
   */
  @Override
  public MailingRecipient buildMailingRecipientForManagerAlert( AlertMessage alertMessage, Participant participant, String companyName, String companyWebsite )
  {
    MailingRecipient mailingReceipient = new MailingRecipient();
    if ( participant.getLanguageType() != null )
    {
      mailingReceipient.setLocale( participant.getLanguageType().getCode() );
    }
    else
    {
      mailingReceipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
    }
    mailingReceipient.setGuid( GuidUtils.generateGuid() );
    Map<String, String> dataMap = new HashMap<String, String>();
    dataMap.put( "rFirstName", participant.getFirstName() );
    dataMap.put( "sFirstName", alertMessage.getSubmitter().getFirstName() );
    dataMap.put( "sLastName", alertMessage.getSubmitter().getLastName() );
    dataMap.put( "subject", alertMessage.getSubject() );
    dataMap.put( "message", alertMessage.getMessage() );
    dataMap.put( "companyName", companyName );
    dataMap.put( "websiteLink", companyWebsite );
    mailingReceipient.addMailingRecipientDataFromMap( dataMap );
    mailingReceipient.setUser( participant );
    return mailingReceipient;
  }

  /**
   * @param promotion
   * @param participant
   * @param goalCalculationResult
   * @param paxPartners 
   * @param importFile 
   * @return MailingRecipient
   */
  @Override
  public MailingRecipient buildMailingRecipientForGoalQuestEmail( GoalQuestPromotion promotion,
                                                                  Participant participant,
                                                                  GoalCalculationResult goalCalculationResult,
                                                                  List paxPartners,
                                                                  ImportFile importFile )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setUser( participant );
    if ( participant.getLanguageType() != null )
    {
      mailingRecipient.setLocale( participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode() );
    }
    else
    {
      mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
    }
    Locale locale = CmsUtil.getLocale( mailingRecipient.getLocale() );
    Map dataMap = new HashMap();
    if ( promotion.getPromoNameAssetCode() != null )
    {
      String promotionName = cmAssetService.getString( promotion.getPromoNameAssetCode(), Promotion.PROMOTION_NAME_KEY_PREFIX, locale, true );
      dataMap.put( "programName", StringEscapeUtils.unescapeHtml4( promotionName ) );
    }
    dataMap.put( "firstName", participant.getFirstName() );
    dataMap.put( "lastName", participant.getLastName() );
    if ( importFile != null && importFile.getProgressEndDate() != null )
    {
      dataMap.put( "date", com.biperf.core.utils.DateUtils.toDisplayString( importFile.getProgressEndDate(), locale ) );
    }
    else
    {
      dataMap.put( "date", com.biperf.core.utils.DateUtils.toDisplayString( com.biperf.core.utils.DateUtils.getCurrentDate(), locale ) );
    }
    if ( paxPartners != null && paxPartners.size() > 0 )
    {
      StringBuffer partnerNames = new StringBuffer();
      for ( int i = 0; i < paxPartners.size(); i++ )
      {
        // Bug Fix 20215.
        ParticipantPartner paxPartner = (ParticipantPartner)paxPartners.get( i );
        partnerNames.append( paxPartner.getPartner().getFirstName() ).append( " " ).append( paxPartner.getPartner().getLastName() );
        if ( i != paxPartners.size() - 1 )
        {
          partnerNames.append( "," ).append( " " ); // Fix 22568
        }
      }
      dataMap.put( "partnerNames", partnerNames.toString() );
    }
    if ( goalCalculationResult.isOwner() )
    {
      dataMap.put( "mgr", "true" );
    }
    if ( promotion.getAwardType().getCode().equals( PromotionAwardsType.POINTS ) )
    {
      dataMap.put( "points", PromotionAwardsType.POINTS );
    }
    // BugFix 18492 starts
    if ( promotion.getAwardType().getCode().equals( PromotionAwardsType.POINTS ) )
    {
      dataMap.put( "pointsType", "TRUE" );
    }
    if ( promotion.getAwardType().getCode().equals( PromotionAwardsType.MERCHANDISE ) )
    {
      dataMap.put( "merchandiseType", "TRUE" );
    }
    if ( promotion.getAwardType().getCode().equals( PromotionAwardsType.TRAVEL_AWARD ) )
    {
      dataMap.put( "travelAwardType", "TRUE" );
    }
    // BugFix 18492 ends
    if ( promotion.getGoalCollectionStartDate() != null )
    {
      dataMap.put( "registrationStartDate", com.biperf.core.utils.DateUtils.toDisplayString( promotion.getGoalCollectionStartDate(), locale ) );
    }
    if ( promotion.getGoalCollectionEndDate() != null )
    {
      dataMap.put( "registrationEndDate", com.biperf.core.utils.DateUtils.toDisplayString( promotion.getGoalCollectionEndDate(), locale ) );
    }
    // BugFix 17918 format the goal,base fields as comma seperated values
    if ( goalCalculationResult.getBaseObjective() != null && goalCalculationResult.getBaseObjective().floatValue() != 0 )
    {
      String base = NumberFormatUtil.getLocaleBasedBigDecimalFormat( goalCalculationResult.getBaseObjective(),
                                                                     promotion.getAchievementPrecision().getPrecision(),
                                                                     LocaleUtils.getLocale( mailingRecipient.getLocale() ) );
      dataMap.put( "base", formatBasevalue( base, promotion, locale ) );

    }
    else
    {
      dataMap.put( "base", formatBasevalue( "0", promotion, locale ) );
    }
    if ( goalCalculationResult.getGoalLevel() != null )
    {
      dataMap.put( "goalLevel", ChallengepointUtil.getGoalLevelName( goalCalculationResult.getGoalLevel().getGoalLevelcmAssetCode(), locale ) );
      dataMap.put( "goalLevelAmount", ChallengepointUtil.getGoalLevelDescription( goalCalculationResult.getGoalLevel().getGoalLevelcmAssetCode(), locale ) );

    }
    // The formatBaseValue is called for bug 17935 to add the Goal Value
    // code fix to the bug 18710 -the display % if the pax is node owner and manager override is
    // enabled in the promotion
    if ( goalCalculationResult.getAmountToAchieve() != null && goalCalculationResult.getAmountToAchieve().floatValue() != 0 && participant.isOwner() && promotion.getOverrideStructure() != null )
    {
      StringBuffer formatBase = new StringBuffer( "" );
      if ( promotion.getBaseUnitText() != null && promotion.getBaseUnitText().trim().length() > 0 )
      {
        if ( promotion.getBaseUnitPosition() != null && promotion.getBaseUnitPosition().getCode() != null && promotion.getBaseUnitPosition().getCode().equals( "before" ) )
        {
          if ( promotion.getOverrideStructure() != null && promotion.getOverrideStructure().getCode().equals( ManagerOverrideStructure.OVERRIDE_PERCENT ) )
          {
            formatBase.append( "%" );
          }
          else
          {
            formatBase.append( promotion.getBaseUnitText().replace( "&nbsp;", " " ) );
          }
          formatBase.append( "&nbsp;" );
          String amountToAchieve = NumberFormatUtil.getLocaleBasedBigDecimalFormat( goalCalculationResult.getAmountToAchieve(),
                                                                                    promotion.getAchievementPrecision().getPrecision(),
                                                                                    LocaleUtils.getLocale( mailingRecipient.getLocale() ) );
          formatBase.append( amountToAchieve );

        }
        if ( promotion.getBaseUnitPosition() != null && promotion.getBaseUnitPosition().getCode() != null && promotion.getBaseUnitPosition().getCode().equals( "after" ) )
        {
          String amountToAchieve = NumberFormatUtil.getLocaleBasedBigDecimalFormat( goalCalculationResult.getAmountToAchieve(),
                                                                                    promotion.getAchievementPrecision().getPrecision(),
                                                                                    LocaleUtils.getLocale( mailingRecipient.getLocale() ) );
          formatBase.append( amountToAchieve );
          formatBase.append( "&nbsp;" );
          if ( promotion.getOverrideStructure() != null && promotion.getOverrideStructure().getCode().equals( ManagerOverrideStructure.OVERRIDE_PERCENT ) )
          {
            formatBase.append( "%" );
          }
          else
          {
            formatBase.append( promotion.getBaseUnitText().replace( "&nbsp;", " " ) );
          }
        }
      }
      else
      {
        String amountToAchieve = NumberFormatUtil.getLocaleBasedBigDecimalFormat( goalCalculationResult.getAmountToAchieve(),
                                                                                  promotion.getAchievementPrecision().getPrecision(),
                                                                                  LocaleUtils.getLocale( mailingRecipient.getLocale() ) );
        formatBase.append( amountToAchieve );
        // formatBase.append( promotion.getBaseUnit().replace("&nbsp;", " " ) );
      }
      dataMap.put( "totalGoalValue", formatBase.toString() );
    }
    else
    {
      if ( goalCalculationResult.getAmountToAchieve() != null && goalCalculationResult.getAmountToAchieve().floatValue() != 0 )
      {
        // BugFix 17918 format the goal,base fields as comma seperated values
        String amountToAchieve = NumberFormatUtil.getLocaleBasedBigDecimalFormat( goalCalculationResult.getAmountToAchieve(),
                                                                                  promotion.getAchievementPrecision().getPrecision(),
                                                                                  LocaleUtils.getLocale( mailingRecipient.getLocale() ) );
        dataMap.put( "totalGoalValue", formatBasevalue( amountToAchieve, promotion, locale ) );
      }
      else
      {
        if ( promotion.getAchievementPrecision().getCode().equals( "two" ) )
        {
          dataMap.put( "totalGoalValue", formatBasevalue( "0.00", promotion, locale ) );
        }
        else
        {
          dataMap.put( "totalGoalValue", formatBasevalue( "0", promotion, locale ) );
        }
      }
    }
    if ( goalCalculationResult.getTotalPerformance() != null && goalCalculationResult.getTotalPerformance().floatValue() != 0 )
    {
      // BugFix 17918 bug fix:38085format the goal,base fields as comma seperated values
      String totalPerformance = NumberFormatUtil.getLocaleBasedBigDecimalFormat( goalCalculationResult.getTotalPerformance(),
                                                                                 promotion.getAchievementPrecision().getPrecision(),
                                                                                 LocaleUtils.getLocale( mailingRecipient.getLocale() ) );
      dataMap.put( "actualResults", formatBasevalue( totalPerformance, promotion, locale ) );
    }
    else
    {
      if ( promotion.getAchievementPrecision().getCode().equals( "two" ) )
      {
        dataMap.put( "actualResults", formatBasevalue( "0.00", promotion, locale ) );
      }
      else
      {
        dataMap.put( "actualResults", formatBasevalue( "0", promotion, locale ) );
      }
    }
    if ( goalCalculationResult.getAmountToAchieve() != null && goalCalculationResult.getTotalPerformance() != null )
    {
      int roundingType = promotion != null && promotion.getRoundingMethod() != null ? promotion.getRoundingMethod().getBigDecimalRoundingMode() : BigDecimal.ROUND_HALF_UP;
      BigDecimal percentToGoal = goalCalculationResult.getTotalPerformance().divide( goalCalculationResult.getAmountToAchieve(), 2, roundingType ).movePointRight( 2 );
      dataMap.put( "percentToGoal", percentToGoal.toString() );
    }
    else
    {
      dataMap.put( "percentToGoal", "0" );
    }
    if ( goalCalculationResult.isAchieved() )
    {
      if ( goalCalculationResult.getCalculatedPayout() != null )
      {
        // BugFix 17918 format the goal,base fields as comma seperated values
        String calculatedPayout = NumberFormatUtil.getLocaleBasedBigDecimalFormat( goalCalculationResult.getCalculatedPayout(), 0, LocaleUtils.getLocale( mailingRecipient.getLocale() ) );
        dataMap.put( "rewardAmount", calculatedPayout );
      }
    }
    List<ParticipantBadge> participantBadges = getGamificationService().getBadgeByParticipantEarnedForPromotion( promotion.getId(), participant.getId() );
    if ( participantBadges != null && participantBadges.size() > 0 )
    {
      dataMap.put( "showBadges", "TRUE" );
      Map paxBadgeMap = new HashMap();
      paxBadgeMap = buildBadgeHtmlString( participantBadges );
      String paxBadgeEarnedString = "";
      String paxBadgeProgressString = "";
      if ( paxBadgeMap.get( "paxBadgeEarnedString" ) != null )
      {
        paxBadgeEarnedString = paxBadgeMap.get( "paxBadgeEarnedString" ).toString();
      }
      if ( paxBadgeMap.get( "paxBadgeProgressString" ) != null )
      {
        paxBadgeProgressString = paxBadgeMap.get( "paxBadgeProgressString" ).toString();
      }
      if ( !StringUtils.isEmpty( paxBadgeEarnedString ) )
      {
        String paxBadgeEarnedNoHtml = "";
        paxBadgeEarnedNoHtml = HtmlUtils.removeFormatting( paxBadgeEarnedString );
        if ( !StringUtils.isEmpty( paxBadgeEarnedNoHtml.trim() ) )
        {
          dataMap.put( "showEarnedBadges", "TRUE" );
          dataMap.put( "paxBadgeEarnedString", paxBadgeEarnedString );
          dataMap.put( "paxBadgeEarnedStringPlain", HtmlUtils.removeFormatting( paxBadgeEarnedString ) );
        }
      }
      if ( !StringUtils.isEmpty( paxBadgeProgressString ) )
      {
        String paxBadgeProgressNoHtml = "";
        paxBadgeProgressNoHtml = HtmlUtils.removeFormatting( paxBadgeProgressString );
        if ( !StringUtils.isEmpty( paxBadgeProgressNoHtml.trim() ) )
        {
          dataMap.put( "showProgressBar", "TRUE" );
          dataMap.put( "paxBadgeProgressString", paxBadgeProgressString );
          dataMap.put( "paxBadgeProgressStringPlain", HtmlUtils.removeFormatting( paxBadgeProgressString ) );
        }
      }
    }
    String link = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    dataMap.put( "siteLink", link );
    mailingRecipient.addMailingRecipientDataFromMap( dataMap );

    return mailingRecipient;
  }

  /**
   * Build the Mailing list for this participant. 
   * @param Promotion
   * @param participant
   * @param ChallengepointPaxValueBean
   * @param importFile 
   * @return MailingRecipient
   */
  @Override
  public MailingRecipient buildMailingRecipientForChallengepointEmail( ChallengePointPromotion promotion,
                                                                       Participant participant,
                                                                       ChallengepointPaxValueBean cpPaxValueBean,
                                                                       ImportFile importFile,
                                                                       List paxPartners )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setUser( participant );
    if ( participant.getLanguageType() != null )
    {
      mailingRecipient.setLocale( participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode() );
    }
    else
    {
      mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
    }
    Locale locale = CmsUtil.getLocale( mailingRecipient.getLocale() );
    Map dataMap = new HashMap();
    if ( promotion.getPromoNameAssetCode() != null )
    {
      String promotionName = cmAssetService.getString( promotion.getPromoNameAssetCode(), Promotion.PROMOTION_NAME_KEY_PREFIX, locale, true );
      dataMap.put( "programName", StringEscapeUtils.unescapeHtml4( promotionName ) );
    }
    dataMap.put( "firstName", participant.getFirstName() );
    dataMap.put( "lastName", participant.getLastName() );
    String siteLink = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    dataMap.put( "siteLink", siteLink );
    if ( importFile != null && importFile.getProgressEndDate() != null )
    {
      dataMap.put( "date", com.biperf.core.utils.DateUtils.toDisplayString( importFile.getProgressEndDate(), locale ) );
    }
    else
    {
      dataMap.put( "date", com.biperf.core.utils.DateUtils.toDisplayString( com.biperf.core.utils.DateUtils.getCurrentDate(), locale ) );
    }
    if ( participant.isOwner() )
    {
      dataMap.put( "mgr", "true" );
    }
    if ( promotion.getChallengePointAwardType().getCode().equals( ChallengePointAwardType.POINTS ) )
    {
      dataMap.put( "points", PromotionAwardsType.POINTS );
    }
    // BugFix 18492 starts
    if ( promotion.getChallengePointAwardType().getCode().equals( ChallengePointAwardType.POINTS ) )
    {
      dataMap.put( "pointsType", "TRUE" );
    }
    if ( promotion.getChallengePointAwardType().getCode().equals( ChallengePointAwardType.MERCHTRAVEL ) )
    {
      dataMap.put( "merchandiseTravelType", "TRUE" );
    }
    // BugFix 18492 ends
    if ( promotion.getGoalCollectionStartDate() != null )
    {
      dataMap.put( "registrationStartDate", com.biperf.core.utils.DateUtils.toDisplayString( promotion.getGoalCollectionStartDate(), locale ) );
    }
    if ( promotion.getGoalCollectionEndDate() != null )
    {
      dataMap.put( "registrationEndDate", com.biperf.core.utils.DateUtils.toDisplayString( promotion.getGoalCollectionEndDate(), locale ) );
    }

    dataMap.put( "promotionStartDate", com.biperf.core.utils.DateUtils.toDisplayString( promotion.getSubmissionStartDate(), locale ) );
    Date promotEndDate = promotion.getSubmissionEndDate();
    if ( promotEndDate != null )
    {
      dataMap.put( "isPromotionEndDate", String.valueOf( Boolean.TRUE ) );
      dataMap.put( "promotionEndDate", com.biperf.core.utils.DateUtils.toDisplayString( promotEndDate ) );
    }
    if ( cpPaxValueBean.getProgressSubmissionDate() != null )
    {
      dataMap.put( "progressSubmissionDate", com.biperf.core.utils.DateUtils.toDisplayString( cpPaxValueBean.getProgressSubmissionDate(), locale ) );
    }
    else
    {
      dataMap.put( "progressSubmissionDate", com.biperf.core.utils.DateUtils.toDisplayString( com.biperf.core.utils.DateUtils.getCurrentDate(), locale ) );
    }
    // BugFix 17918 format the goal,base fields as comma seperated values
    if ( cpPaxValueBean.getBaseAmount() != null && cpPaxValueBean.getBaseAmount().floatValue() != 0 )
    {
      dataMap.put( "base", formatCPBasevalue( NumberFormat.getInstance().format( cpPaxValueBean.getBaseAmount() ).toString(), promotion, locale ) );
    }
    else
    {
      dataMap.put( "base", formatCPBasevalue( "0", promotion, locale ) );
    }
    if ( cpPaxValueBean.getPaxGoal() != null )
    {
      dataMap.put( "challengepointLevel", ChallengepointUtil.getGoalLevelName( cpPaxValueBean.getPaxGoal().getGoalLevel().getGoalLevelcmAssetCode(), locale ) );
      dataMap.put( "challengepointLevelDescription", ChallengepointUtil.getGoalLevelDescription( cpPaxValueBean.getPaxGoal().getGoalLevel().getGoalLevelcmAssetCode(), locale ) );
      dataMap.put( "challengepointLevelAmount", ( (GoalLevel)cpPaxValueBean.getPaxGoal().getGoalLevel() ).getAchievementAmount().toString() );
      dataMap.put( "challengepointLevelAward", ( (GoalLevel)cpPaxValueBean.getPaxGoal().getGoalLevel() ).getAward().toString() );
    }
    if ( promotion != null && promotion.getAwardPerIncrement() != null )
    {
      dataMap.put( "primaryAward", promotion.getAwardPerIncrement().toString() );
    }
    else
    {
      dataMap.put( "primaryAward", "" );
    }

    if ( cpPaxValueBean.getCalculatedIncrementAmount() != null )
    {
      StringBuffer formatAwardIncrement = new StringBuffer( "" );
      if ( promotion.getBaseUnitText() != null && promotion.getBaseUnitText().trim().length() > 0 )
      {
        if ( promotion.getBaseUnitPosition() != null && BaseUnitPosition.UNIT_BEFORE.equals( promotion.getBaseUnitPosition().getCode() ) )
        {
          formatAwardIncrement.append( promotion.getBaseUnitText() );
          formatAwardIncrement.append( "&#160;" );
          formatAwardIncrement.append( cpPaxValueBean.getCalculatedIncrementAmount() );// 22332
        }
        if ( promotion.getBaseUnitPosition() != null && BaseUnitPosition.UNIT_AFTER.equals( promotion.getBaseUnitPosition().getCode() ) )
        {
          formatAwardIncrement.append( cpPaxValueBean.getCalculatedIncrementAmount() );
          formatAwardIncrement.append( "&#160;" );
          formatAwardIncrement.append( promotion.getBaseUnitText() );
        }
        dataMap.put( "primaryAwardIncrement", formatAwardIncrement.toString() );
      }
      else
      {
        dataMap.put( "primaryAwardIncrement", cpPaxValueBean.getCalculatedIncrementAmount().toString() );
      }
    }
    else
    {
      dataMap.put( "primaryAwardIncrement", "" );
    }
    if ( cpPaxValueBean.getCalculatedThreshold() != null && cpPaxValueBean.getCalculatedThreshold().floatValue() != 0 )
    {
      dataMap.put( "isThreshold", "true" );
      StringBuffer formatThreshold = new StringBuffer( "" );
      if ( promotion.getBaseUnitText() != null && promotion.getBaseUnitText().trim().length() > 0 )
      {
        if ( promotion.getBaseUnitPosition() != null && BaseUnitPosition.UNIT_BEFORE.equals( promotion.getBaseUnitPosition().getCode() ) )
        {
          formatThreshold.append( promotion.getBaseUnitText() );
          formatThreshold.append( "&#160;" );
          formatThreshold.append( cpPaxValueBean.getCalculatedThreshold() );
        }
        if ( promotion.getBaseUnitPosition() != null && BaseUnitPosition.UNIT_AFTER.equals( promotion.getBaseUnitPosition().getCode() ) )
        {
          formatThreshold.append( cpPaxValueBean.getCalculatedThreshold() );
          formatThreshold.append( "&#160;" );
          formatThreshold.append( promotion.getBaseUnitText() );
        }
        dataMap.put( "primaryAwardThreshold", formatThreshold.toString() );
      }
      else
      {
        dataMap.put( "primaryAwardThreshold", cpPaxValueBean.getCalculatedThreshold().toString() ); // Fix
                                                                                                    // 22274
      }
    }
    else
    {
      dataMap.put( "primaryAwardThreshold", "" );
    }

    if ( paxPartners != null && paxPartners.size() > 0 )
    {
      StringBuffer partnerNames = new StringBuffer();
      for ( int i = 0; i < paxPartners.size(); i++ )
      {
        // Bug Fix 20215.
        ParticipantPartner paxPartner = (ParticipantPartner)paxPartners.get( i );
        partnerNames.append( paxPartner.getPartner().getFirstName() ).append( " " ).append( paxPartner.getPartner().getLastName() );
        if ( i != paxPartners.size() - 1 )
        {
          partnerNames.append( "," ).append( " " ); // Fix 22568
        }
      }
      dataMap.put( "partnerNames", partnerNames.toString() );
    }
    // The formatBaseValue is called for bug 17935 to add the Goal Value
    // code fix to the bug 18710 -the display % if the pax is node owner and manager override is
    // enabled in the promotion
    if ( cpPaxValueBean.getAmountToAchieve() != null && cpPaxValueBean.getAmountToAchieve().floatValue() != 0 && participant.isOwner() && promotion.getOverrideStructure() != null
        && !promotion.getOverrideStructure().getCode().equals( ManagerOverrideStructure.NONE ) )
    {
      StringBuffer formatBase = new StringBuffer( "" );
      if ( promotion.getBaseUnitText() != null && promotion.getBaseUnitText().trim().length() > 0 )
      {
        if ( promotion.getBaseUnitPosition() != null && BaseUnitPosition.UNIT_BEFORE.equals( promotion.getBaseUnitPosition().getCode() ) )
        {
          if ( promotion.getOverrideStructure() != null && promotion.getOverrideStructure().getCode().equals( ManagerOverrideStructure.OVERRIDE_PERCENT ) )
          {
            formatBase.append( "%" );
          }
          else
          {
            formatBase.append( promotion.getBaseUnitText() );
          }
          formatBase.append( "&#160;" );
          formatBase.append( cpPaxValueBean.getAmountToAchieve() );

        }
        if ( promotion.getBaseUnitPosition() != null && BaseUnitPosition.UNIT_AFTER.equals( promotion.getBaseUnitPosition().getCode() ) )
        {
          formatBase.append( cpPaxValueBean.getAmountToAchieve() );
          formatBase.append( "&#160;" );
          if ( promotion.getOverrideStructure() != null && promotion.getOverrideStructure().getCode().equals( ManagerOverrideStructure.OVERRIDE_PERCENT ) )
          {
            formatBase.append( "%" );
          }
          else
          {
            formatBase.append( promotion.getBaseUnitText() );
          }
        }
      }
      else
      {
        formatBase.append( promotion.getBaseUnitText() );
      }
      dataMap.put( "totalChallengepointValue", formatBase.toString() );
    }
    else
    {
      if ( cpPaxValueBean.getAmountToAchieve() != null && cpPaxValueBean.getAmountToAchieve().floatValue() != 0 )
      {
        // BugFix 17918 format the goal,base fields as comma seperated values
        dataMap.put( "totalChallengepointValue",
                     formatCPBasevalue( cpPaxValueBean.getAmountToAchieve().setScale( promotion.getAchievementPrecision().getPrecision() ).toString(), promotion, locale ) );
      }
      else
      {
        if ( promotion.getAchievementPrecision().getCode().equals( "two" ) )
        {
          dataMap.put( "totalChallengepointValue", formatCPBasevalue( "0.00", promotion, locale ) );
        }
        else
        {
          dataMap.put( "totalChallengepointValue", formatCPBasevalue( "0", promotion, locale ) );
        }
      }
    }

    if ( cpPaxValueBean.getPaxGoal() != null && cpPaxValueBean.getPaxGoal().getCurrentValue() != null && cpPaxValueBean.getPaxGoal().getCurrentValue().floatValue() != 0 )
    {
      // BugFix 17918 format the goal,base fields as comma seperated values
      dataMap.put( "actualResults", formatCPBasevalue( cpPaxValueBean.getPaxGoal().getCurrentValue().setScale( promotion.getAchievementPrecision().getPrecision() ).toString(), promotion, locale ) );

    }
    else
    {
      if ( promotion.getAchievementPrecision().getCode().equals( "two" ) )
      {
        dataMap.put( "actualResults", formatCPBasevalue( "0.00", promotion, locale ) );
      }
      else
      {
        dataMap.put( "actualResults", formatCPBasevalue( "0", promotion, locale ) );
      }
    }

    if ( cpPaxValueBean.getPercentAchieved() != null )
    {

      BigDecimal percentToGoal = cpPaxValueBean.getPercentAchieved();

      dataMap.put( "percentToChallengepoint", percentToGoal.toString() );
    }
    else
    {
      dataMap.put( "percentToChallengepoint", "0" );
    }
    if ( cpPaxValueBean.getTotalAwardDeposited() != null )
    {
      // BugFix 17918 format the goal,base fields as comma seperated values
      dataMap.put( "rewardAmount", NumberFormat.getInstance().format( cpPaxValueBean.getTotalAwardDeposited() ) );
    }
    else
    {
      dataMap.put( "rewardAmount", "" );
    }

    if ( cpPaxValueBean.getInterimAwardDeposited() != null )
    {
      // BugFix 17918 format the goal,base fields as comma seperated values
      dataMap.put( "interimAmount", NumberFormat.getInstance().format( cpPaxValueBean.getInterimAwardDeposited() ) );
    }
    else
    {
      dataMap.put( "interimAmount", "" );
    }
    String link = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    dataMap.put( "siteLink", link );
    dataMap.put( "contactUsUrl",
                 systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal()
                     + systemVariableService.getPropertyByName( SystemVariableService.CLIENT_CONTACT_URL ).getStringVal() );
    mailingRecipient.addMailingRecipientDataFromMap( dataMap );

    return mailingRecipient;
  }

  /**
   * Build the Mailing list for this participant. 
   * @param Promotion
   * @param participant
   * @param ChallengepointPaxAwardValueBean
   * @param importFile 
   * @return MailingRecipient
   */
  @Override
  public MailingRecipient buildMailingRecipientForChallengepointEmail( ChallengePointPromotion promotion,
                                                                       Participant participant,
                                                                       ChallengepointPaxAwardValueBean cpPaxAwardValueBean,
                                                                       ImportFile importFile )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setUser( participant );
    if ( participant.getLanguageType() != null )
    {
      mailingRecipient.setLocale( participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode() );
    }
    else
    {
      mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
    }

    Locale locale = CmsUtil.getLocale( mailingRecipient.getLocale() );
    Map dataMap = new HashMap();
    if ( promotion.getPromoNameAssetCode() != null )
    {
      String promotionName = cmAssetService.getString( promotion.getPromoNameAssetCode(), Promotion.PROMOTION_NAME_KEY_PREFIX, locale, true );
      dataMap.put( "programName", StringEscapeUtils.unescapeHtml4( promotionName ) );
    }
    dataMap.put( "firstName", participant.getFirstName() );
    dataMap.put( "lastName", participant.getLastName() );
    if ( importFile != null && importFile.getProgressEndDate() != null )
    {
      dataMap.put( "date", com.biperf.core.utils.DateUtils.toDisplayString( importFile.getProgressEndDate(), locale ) );
    }
    else
    {
      dataMap.put( "date", com.biperf.core.utils.DateUtils.toDisplayString( com.biperf.core.utils.DateUtils.getCurrentDate(), locale ) );
    }
    if ( participant.isOwner() )
    {
      dataMap.put( "mgr", "true" );
    }
    else
    {
      dataMap.put( "nonmgr", "true" );
    }
    if ( promotion.getChallengePointAwardType().getCode().equals( ChallengePointAwardType.POINTS ) )
    {
      dataMap.put( "points", PromotionAwardsType.POINTS );
    }
    // BugFix 18492 starts
    if ( promotion.getChallengePointAwardType().getCode().equals( ChallengePointAwardType.POINTS ) )
    {
      dataMap.put( "pointsType", "TRUE" );
    }
    if ( promotion.getChallengePointAwardType().getCode().equals( ChallengePointAwardType.MERCHTRAVEL ) )
    {
      dataMap.put( "merchandiseTravelType", "TRUE" );
    }
    // BugFix 18492 ends
    if ( promotion.getGoalCollectionStartDate() != null )
    {
      dataMap.put( "registrationStartDate", com.biperf.core.utils.DateUtils.toDisplayString( promotion.getGoalCollectionStartDate(), locale ) );
    }
    if ( promotion.getGoalCollectionEndDate() != null )
    {
      dataMap.put( "registrationEndDate", com.biperf.core.utils.DateUtils.toDisplayString( promotion.getGoalCollectionEndDate(), locale ) );
    }

    dataMap.put( "promotionStartDate", com.biperf.core.utils.DateUtils.toDisplayString( promotion.getSubmissionStartDate(), locale ) );
    Date promotEndDate = promotion.getSubmissionEndDate();
    if ( promotEndDate != null )
    {
      dataMap.put( "isPromotionEndDate", String.valueOf( Boolean.TRUE ) );
      dataMap.put( "promotionEndDate", com.biperf.core.utils.DateUtils.toDisplayString( promotEndDate, locale ) );
    }
    // BugFix 17918 format the goal,base fields as comma seperated values
    if ( cpPaxAwardValueBean.getBaseQuantity() != null && cpPaxAwardValueBean.getBaseQuantity().floatValue() != 0 )
    {
      String baseQuantity = NumberFormatUtil.getLocaleBasedBigDecimalFormat( cpPaxAwardValueBean.getBaseQuantity(),
                                                                             promotion.getAchievementPrecision().getPrecision(),
                                                                             LocaleUtils.getLocale( mailingRecipient.getLocale() ) );
      dataMap.put( "base", formatCPBasevalue( baseQuantity, promotion, locale ) );
    }
    else
    {
      dataMap.put( "base", formatCPBasevalue( "0", promotion, locale ) );
    }

    dataMap.put( "calculationapprovedate", com.biperf.core.utils.DateUtils.toDisplayString( com.biperf.core.utils.DateUtils.getCurrentDate(), LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );
    long primaryEarnings = 0;
    if ( cpPaxAwardValueBean.getEmailTotalAwardIssued() != null && cpPaxAwardValueBean.getAwardEarned() != null )
    {
      if ( cpPaxAwardValueBean.getEmailTotalAwardIssued().longValue() > 0 && cpPaxAwardValueBean.getAwardEarned() != null )
      {
        primaryEarnings = cpPaxAwardValueBean.getEmailTotalAwardIssued().longValue() - cpPaxAwardValueBean.getAwardEarned().longValue();
      }
      else if ( cpPaxAwardValueBean.getEmailTotalAwardIssued().longValue() > 0 && cpPaxAwardValueBean.getAwardEarned() == null )
      {
        primaryEarnings = cpPaxAwardValueBean.getEmailTotalAwardIssued().longValue();
      }
    }
    if ( primaryEarnings >= 0 )
    {
      String tempPrimaryEarnings = NumberFormatUtil.getUserLocaleBasedNumberFormat( primaryEarnings, LocaleUtils.getLocale( mailingRecipient.getLocale() ) );
      dataMap.put( "primaryEarnings", tempPrimaryEarnings );
    }
    else
    {
      dataMap.put( "primaryEarnings", "" );
    }

    long interimAmount = 0;
    if ( cpPaxAwardValueBean.getTotalAwardIssued() != null && cpPaxAwardValueBean.getAwardEarned() != null )
    {
      if ( cpPaxAwardValueBean.getTotalAwardIssued().longValue() > 0 && cpPaxAwardValueBean.getAwardEarned() != null )
      {
        interimAmount = cpPaxAwardValueBean.getTotalAwardIssued().longValue() - cpPaxAwardValueBean.getAwardEarned().longValue();
      }
      else if ( cpPaxAwardValueBean.getTotalAwardIssued().longValue() > 0 && cpPaxAwardValueBean.getAwardEarned() == null )
      {
        interimAmount = cpPaxAwardValueBean.getTotalAwardIssued().longValue();
      }
    }
    if ( cpPaxAwardValueBean.getTotalAwardIssued() == null && cpPaxAwardValueBean.getEmailTotalAwardIssued() != null )
    {
      interimAmount = cpPaxAwardValueBean.getEmailTotalAwardIssued().longValue();
    }
    if ( interimAmount >= 0 )
    {

      String tempInterimAmount = NumberFormatUtil.getUserLocaleBasedNumberFormat( interimAmount, LocaleUtils.getLocale( mailingRecipient.getLocale() ) );
      dataMap.put( "interimAmount", tempInterimAmount );
    }
    else
    {

      dataMap.put( "interimAmount", "" );
    }
    long totalRewardAmount = 0;
    if ( cpPaxAwardValueBean.getEmailTotalAwardIssued() != null && cpPaxAwardValueBean.getEmailTotalAwardIssued().longValue() > 0 )
    {
      totalRewardAmount = cpPaxAwardValueBean.getEmailTotalAwardIssued().longValue();
      String tempTotalRewardAmount = NumberFormatUtil.getUserLocaleBasedNumberFormat( totalRewardAmount, LocaleUtils.getLocale( mailingRecipient.getLocale() ) );
      dataMap.put( "totalRewardAmount", tempTotalRewardAmount );
    }
    else
    {
      dataMap.put( "totalRewardAmount", "" );
    }

    long totalRemaingAmount = 0;
    BigDecimal remainingAmount = new BigDecimal( 0 );
    if ( totalRewardAmount > 0 || interimAmount > 0 )
    {
      if ( totalRewardAmount >= interimAmount )
      {
        BigDecimal amount = ( (GoalLevel)cpPaxAwardValueBean.getPaxGoal().getGoalLevel() ).getAward();
        totalRemaingAmount = totalRewardAmount - interimAmount;
        remainingAmount = new BigDecimal( totalRemaingAmount ).subtract( amount );
      }
    }
    if ( remainingAmount.compareTo( new BigDecimal( 0 ) ) >= 0 )
    {
      String tempRemaingAmount = NumberFormatUtil.getLocaleBasedBigDecimalFormat( remainingAmount, 0, LocaleUtils.getLocale( mailingRecipient.getLocale() ) );
      dataMap.put( "remainingPoints", tempRemaingAmount );
      String tempprimaryEarnings = dataMap.get( "primaryEarnings" ).toString();
      if ( !tempprimaryEarnings.equals( "" ) )
      {
        BigDecimal primaryEarningsBg = new BigDecimal( tempprimaryEarnings );
        String tempPrimaryAmountAddition = NumberFormatUtil.getLocaleBasedBigDecimalFormat( remainingAmount.add( primaryEarningsBg ), 0, LocaleUtils.getLocale( mailingRecipient.getLocale() ) );
        dataMap.put( "primaryEarnings", tempPrimaryAmountAddition );
      }
    }
    else
    {
      dataMap.put( "remainingPoints", "" );
    }
    if ( promotion.getAwardThresholdType().equalsIgnoreCase( ChallengePointPromotion.PRIMARY_AWARD_THRESHOLD_PCT_BASE ) )
    {
      if ( cpPaxAwardValueBean.getInterimAward() != null && cpPaxAwardValueBean.getInterimAward().longValue() > 0 )
      {
        String interimAward = NumberFormatUtil.getUserLocaleBasedNumberFormat( cpPaxAwardValueBean.getInterimAward(), LocaleUtils.getLocale( mailingRecipient.getLocale() ) );
        dataMap.put( "threshold", interimAward );
      }
      else
      {
        dataMap.put( "threshold", "" );
      }
    }
    else
    {
      if ( cpPaxAwardValueBean.getChallengePointPromotion().getAwardThresholdValue() != null && cpPaxAwardValueBean.getChallengePointPromotion().getAwardThresholdValue().intValue() > 0 )
      {
        String awardThresholdValue = NumberFormatUtil.getUserLocaleBasedNumberFormat( cpPaxAwardValueBean.getChallengePointPromotion().getAwardThresholdValue(),
                                                                                      LocaleUtils.getLocale( mailingRecipient.getLocale() ) );
        dataMap.put( "threshold", awardThresholdValue );
      }
      else
      {
        dataMap.put( "threshold", "" );
      }
    }
    if ( cpPaxAwardValueBean.getPaxGoal() != null )
    {
      dataMap.put( "challengepointLevel", ChallengepointUtil.getGoalLevelName( cpPaxAwardValueBean.getPaxGoal().getGoalLevel().getGoalLevelcmAssetCode(), locale ) );
      dataMap.put( "challengepointLevelDescription", ChallengepointUtil.getGoalLevelDescription( cpPaxAwardValueBean.getPaxGoal().getGoalLevel().getGoalLevelcmAssetCode(), locale ) );

      BigDecimal tempChallengepointLevelAmount = new BigDecimal( ( (GoalLevel)cpPaxAwardValueBean.getPaxGoal().getGoalLevel() ).getAchievementAmount().toString() );
      String achievementAmount = NumberFormatUtil.getLocaleBasedBigDecimalFormat( tempChallengepointLevelAmount,
                                                                                  promotion.getAchievementPrecision().getPrecision(),
                                                                                  LocaleUtils.getLocale( mailingRecipient.getLocale() ) );
      dataMap.put( "challengepointLevelAmount", achievementAmount );

      BigDecimal tempChallengepointLevelAward = new BigDecimal( ( (GoalLevel)cpPaxAwardValueBean.getPaxGoal().getGoalLevel() ).getAward().toString() );
      String award = NumberFormatUtil.getLocaleBasedBigDecimalFormat( tempChallengepointLevelAward, 0, LocaleUtils.getLocale( mailingRecipient.getLocale() ) );
      dataMap.put( "challengepointLevelAward", award );
    }
    // The formatBaseValue is called for bug 17935 to add the Goal Value
    // code fix to the bug 18710 -the display % if the pax is node owner and manager override is
    // enabled in the promotion
    if ( participant.isOwner() && promotion.getOverrideStructure() != null && !promotion.getOverrideStructure().getCode().equals( ManagerOverrideStructure.NONE )
        && cpPaxAwardValueBean.getAwardType().equals( "manageroverride" ) )
    {
      StringBuffer formatBase = new StringBuffer( "" );
      BigDecimal awardAmount = new BigDecimal( 0 );
      if ( cpPaxAwardValueBean.getAmountToAchieve() != null && cpPaxAwardValueBean.getAmountToAchieve().floatValue() != 0 )
      {
        awardAmount = cpPaxAwardValueBean.getAmountToAchieve();
      }
      else if ( cpPaxAwardValueBean.getCalculatedAchievement() != null && cpPaxAwardValueBean.getCalculatedAchievement().floatValue() != 0 )
      {
        awardAmount = cpPaxAwardValueBean.getCalculatedAchievement();
      }
      if ( promotion.getBaseUnitText() != null && promotion.getBaseUnitText().trim().length() > 0 )
      {
        if ( promotion.getBaseUnitPosition() != null && BaseUnitPosition.UNIT_BEFORE.equals( promotion.getBaseUnitPosition().getCode() ) )
        {
          if ( promotion.getOverrideStructure() != null && promotion.getOverrideStructure().getCode().equals( ManagerOverrideStructure.OVERRIDE_PERCENT ) )
          {
            formatBase.append( "%" );
          }
          else
          {
            formatBase.append( promotion.getBaseUnitText() );
          }
          formatBase.append( "&#160;" );
          String amountToAchieve = NumberFormatUtil.getLocaleBasedBigDecimalFormat( awardAmount,
                                                                                    promotion.getAchievementPrecision().getPrecision(),
                                                                                    LocaleUtils.getLocale( mailingRecipient.getLocale() ) );
          formatBase.append( amountToAchieve );

        }
        if ( promotion.getBaseUnitPosition() != null && BaseUnitPosition.UNIT_AFTER.equals( promotion.getBaseUnitPosition().getCode() ) )
        {
          String amountToAchieve = NumberFormatUtil.getLocaleBasedBigDecimalFormat( awardAmount,
                                                                                    promotion.getAchievementPrecision().getPrecision(),
                                                                                    LocaleUtils.getLocale( mailingRecipient.getLocale() ) );
          formatBase.append( amountToAchieve );
          formatBase.append( "&#160;" );
          if ( promotion.getOverrideStructure() != null && promotion.getOverrideStructure().getCode().equals( ManagerOverrideStructure.OVERRIDE_PERCENT ) )
          {
            formatBase.append( "%" );
          }
          else
          {
            formatBase.append( promotion.getBaseUnitText() );
          }
        }
      }
      else
      {
        String amountToAchieve = NumberFormatUtil.getLocaleBasedBigDecimalFormat( awardAmount,
                                                                                  promotion.getAchievementPrecision().getPrecision(),
                                                                                  LocaleUtils.getLocale( mailingRecipient.getLocale() ) );
        formatBase.append( amountToAchieve );
        formatBase.append( "&#160;" );
        if ( promotion.getOverrideStructure() != null && promotion.getOverrideStructure().getCode().equals( ManagerOverrideStructure.OVERRIDE_PERCENT ) )
        {
          formatBase.append( "%" );
        }
      }
      dataMap.put( "totalChallengepointValue", formatBase.toString() );
    }
    else
    {
      if ( cpPaxAwardValueBean.getAmountToAchieve() != null && cpPaxAwardValueBean.getAmountToAchieve().floatValue() != 0 )
      {
        // BugFix 17918 format the goal,base fields as comma seperated values
        String amountToAchieve = NumberFormatUtil.getLocaleBasedBigDecimalFormat( cpPaxAwardValueBean.getAmountToAchieve(),
                                                                                  promotion.getAchievementPrecision().getPrecision(),
                                                                                  LocaleUtils.getLocale( mailingRecipient.getLocale() ) );
        dataMap.put( "totalChallengepointValue", formatCPBasevalue( amountToAchieve, promotion, locale ) );
      }
      else
      {
        if ( promotion.getAchievementPrecision().getCode().equals( "two" ) )
        {
          dataMap.put( "totalChallengepointValue", formatCPBasevalue( "0.00", promotion, locale ) );
        }
        else
        {
          dataMap.put( "totalChallengepointValue", formatCPBasevalue( "0", promotion, locale ) );
        }
      }
    }
    if ( cpPaxAwardValueBean.getResult() != null && cpPaxAwardValueBean.getResult().floatValue() != 0 )
    {
      // BugFix 17918 format the goal,base fields as comma seperated values
      String result = NumberFormatUtil.getLocaleBasedBigDecimalFormat( cpPaxAwardValueBean.getResult(),
                                                                       promotion.getAchievementPrecision().getPrecision(),
                                                                       LocaleUtils.getLocale( mailingRecipient.getLocale() ) );
      dataMap.put( "actualResults", formatCPBasevalue( result, promotion, locale ) );

    }
    else
    {
      if ( promotion.getAchievementPrecision().getCode().equals( "two" ) )
      {
        dataMap.put( "actualResults", formatCPBasevalue( "0.00", promotion, locale ) );
      }
      else
      {
        dataMap.put( "actualResults", formatCPBasevalue( "0", promotion, locale ) );
      }
    }

    if ( cpPaxAwardValueBean.getAmountToAchieve() != null && cpPaxAwardValueBean.getResult() != null )
    {

      BigDecimal percentToGoal = cpPaxAwardValueBean.getResult().divide( cpPaxAwardValueBean.getAmountToAchieve(), 2, BigDecimal.ROUND_DOWN ).movePointRight( 2 );
      dataMap.put( "percentToChallengepoint", percentToGoal.toString() );
    }
    else
    {
      dataMap.put( "percentToChallengepoint", "0" );
    }

    if ( cpPaxAwardValueBean.isAchieved() )
    {
      // BugFix 17918 format the goal,base fields as comma seperated values
      if ( cpPaxAwardValueBean.getAwardEarned() != null && cpPaxAwardValueBean.getAwardEarned().toString().trim().length() > 0 )
      {
        String awardEarned = NumberFormatUtil.getUserLocaleBasedNumberFormat( cpPaxAwardValueBean.getAwardEarned(), LocaleUtils.getLocale( mailingRecipient.getLocale() ) );
        dataMap.put( "rewardAmount", awardEarned );
      }
      else
      {
        dataMap.put( "rewardAmount", "" );
      }
    }
    else
    {
      if ( cpPaxAwardValueBean.getEmailTotalAwardIssued() != null && cpPaxAwardValueBean.getEmailTotalAwardIssued().longValue() > 0 )
      {
        dataMap.put( "hasAward", "true" );
      }
      else
      {
        dataMap.put( "notAchieved", "true" );
      }
    }

    if ( totalRewardAmount > 0 )

    {
      String totalPreviousDeposit = NumberFormatUtil.getUserLocaleBasedNumberFormat( interimAmount, LocaleUtils.getLocale( mailingRecipient.getLocale() ) );
      dataMap.put( "totalPreviousDeposit", totalPreviousDeposit );
    }
    else
    {
      dataMap.put( "totalPreviousDeposit", "" );
    }

    List<ParticipantBadge> participantBadges = getGamificationService().getBadgeByParticipantEarnedForPromotion( promotion.getId(), participant.getId() );
    if ( participantBadges != null && participantBadges.size() > 0 )
    {
      dataMap.put( "showBadges", "TRUE" );
      Map paxBadgeMap = new HashMap();
      paxBadgeMap = buildBadgeHtmlString( participantBadges );
      String paxBadgeEarnedString = "";
      String paxBadgeProgressString = "";
      if ( paxBadgeMap.get( "paxBadgeEarnedString" ) != null )
      {
        paxBadgeEarnedString = paxBadgeMap.get( "paxBadgeEarnedString" ).toString();
      }
      if ( paxBadgeMap.get( "paxBadgeProgressString" ) != null )
      {
        paxBadgeProgressString = paxBadgeMap.get( "paxBadgeProgressString" ).toString();
      }
      if ( !StringUtils.isEmpty( paxBadgeEarnedString ) )
      {
        String paxBadgeEarnedNoHtml = "";
        paxBadgeEarnedNoHtml = HtmlUtils.removeFormatting( paxBadgeEarnedString );
        if ( !StringUtils.isEmpty( paxBadgeEarnedNoHtml.trim() ) )
        {
          dataMap.put( "showEarnedBadges", "TRUE" );
          dataMap.put( "paxBadgeEarnedString", paxBadgeEarnedString );
          dataMap.put( "paxBadgeEarnedStringPlain", HtmlUtils.removeFormatting( paxBadgeEarnedString ) );
        }
      }
      if ( !StringUtils.isEmpty( paxBadgeProgressString ) )
      {
        String paxBadgeProgressNoHtml = "";
        paxBadgeProgressNoHtml = HtmlUtils.removeFormatting( paxBadgeProgressString );
        if ( !StringUtils.isEmpty( paxBadgeProgressNoHtml.trim() ) )
        {
          dataMap.put( "showProgressBar", "TRUE" );
          dataMap.put( "paxBadgeProgressString", paxBadgeProgressString );
          dataMap.put( "paxBadgeProgressStringPlain", HtmlUtils.removeFormatting( paxBadgeProgressString ) );
        }
      }
    }

    String link = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    dataMap.put( "siteLink", link );
    dataMap.put( "contactUsUrl",
                 systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal()
                     + systemVariableService.getPropertyByName( SystemVariableService.CLIENT_CONTACT_URL ).getStringVal() );
    mailingRecipient.addMailingRecipientDataFromMap( dataMap );

    return mailingRecipient;
  }

  /**
   * Build the Mailing list for this participant. 
   * @param Promotion
   * @param participant
   * @param MatchTeamProgress
   * @param importFile 
   * @return MailingRecipient
   */
  @Override
  public MailingRecipient buildMailingRecipientForThrowdownEmail( ThrowdownPromotion promotion,
                                                                  Participant participant,
                                                                  Match match,
                                                                  MatchTeamOutcome currentPlayerOutcome,
                                                                  MatchTeamOutcome oppositionPlayerOutcome,
                                                                  Integer rank,
                                                                  Integer payout,
                                                                  Integer totalPaxInRanking,
                                                                  BigDecimal cumulativeProgress,
                                                                  Long rankingPayout,
                                                                  String progressEndDate,
                                                                  BigDecimal shadowScore )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setUser( participant );
    if ( participant.getLanguageType() != null )
    {
      mailingRecipient.setLocale( participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode() );
    }
    else
    {
      mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
    }

    Map dataMap = new HashMap();
    if ( promotion.getPromoNameAssetCode() != null )
    {
      Locale locale = CmsUtil.getLocale( mailingRecipient.getLocale() );
      String promotionName = cmAssetService.getString( promotion.getPromoNameAssetCode(), Promotion.PROMOTION_NAME_KEY_PREFIX, locale, true );
      dataMap.put( "programName", StringEscapeUtils.unescapeHtml4( promotionName ) );
      dataMap.put( "promotionName", StringEscapeUtils.unescapeHtml4( promotionName ) );
    }

    if ( !"".equals( getAfterUnitLabel( promotion ) ) )
    {
      dataMap.put( "isUnitPositionAfter", "TRUE" );
    }
    else if ( !"".equals( getBeforeUnitLabel( promotion ) ) )
    {
      dataMap.put( "isUnitPositionBefore", "TRUE" );
    }

    if ( promotion.getBaseUnit() != null )
    {
      dataMap.put( "unitLabel", promotion.getBaseUnitText() );
    }
    else
    {
      dataMap.put( "unitLabel", "" );
    }

    if ( promotion.isDisplayTeamProgress() )
    {
      dataMap.put( "displayTeamProgress", "TRUE" );
    }
    dataMap.put( "firstName", participant.getFirstName() );
    dataMap.put( "lastName", participant.getLastName() );
    dataMap.put( "standingNumber", NumberFormatUtil.getUserLocaleBasedNumberFormat( new Long( rank ), LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );
    dataMap.put( "eligiblePax", String.valueOf( totalPaxInRanking ) );
    dataMap.put( "totalResults", String.valueOf( cumulativeProgress ) );
    Round round = match.getRound();
    dataMap.put( "roundNumber", NumberFormatUtil.getUserLocaleBasedNumberFormat( new Long( round.getRoundNumber() ), LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );
    dataMap.put( "endDate", com.biperf.core.utils.DateUtils.toDisplayString( round.getEndDate(), LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );
    dataMap.put( "progressEndDate", progressEndDate );
    dataMap
        .put( "date",
              com.biperf.core.utils.DateUtils.toDisplayString( new Timestamp( com.biperf.core.utils.DateUtils.getCurrentDate().getTime() ), LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );

    dataMap.put( "actualResults",
                 currentPlayerOutcome != null && currentPlayerOutcome.getCurrentValueWithPrecisionAndRounding() != null
                     ? NumberFormatUtil.getLocaleBasedBigDecimalFormat( currentPlayerOutcome.getCurrentValueWithPrecisionAndRounding(),
                                                                        promotion.getAchievementPrecision().getPrecision(),
                                                                        LocaleUtils.getLocale( mailingRecipient.getLocale() ) )
                     : "0" );

    ContentReader contentReader = ContentReaderManager.getContentReader();
    List contentList = (List)contentReader.getContent( "picklist.promotion.awardstype.items", CmsUtil.getLocale( mailingRecipient.getLocale() ) );

    for ( Iterator iter = contentList.iterator(); iter.hasNext(); )
    {
      Content content = (Content)iter.next();
      String code = (String)content.getContentDataMap().get( PickListItem.ITEM_CODE_KEY );
      String status = (String)content.getContentDataMap().get( PickListItem.ITEM_STATUS_KEY );
      if ( promotion.getAwardType().getCode().equalsIgnoreCase( code ) && status.equals( "true" ) )
      {
        String mediaName = (String)content.getContentDataMap().get( PickListItem.ITEM_NAME_KEY );
        dataMap.put( "mediaName", StringEscapeUtils.unescapeHtml4( mediaName ) );
        break;
      }
    }

    if ( payout != null )
    {
      if ( payout > 0 )
      {
        dataMap.put( "showRewardAmount", "TRUE" );
        dataMap.put( "rewardAmount", NumberFormatUtil.getUserLocaleBasedNumberFormat( new Long( payout ), LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );
      }
      List<BadgeInfo> badges = getGamificationService().getTDRankingBadgesEarnable( promotion.getId(), getHierarchyPlaceHolderNodeType(), rank, BadgeLevelType.lookup( BadgeLevelType.STACK_STAND ) );

      if ( !badges.isEmpty() && !badges.iterator().next().getBadgeDetails().isEmpty() )
      {
        dataMap.put( "showBadges", "TRUE" );
        Map paxBadgeMap = new HashMap();

        paxBadgeMap = buildTDBadgeHtmlString( badges.iterator().next().getBadgeDetails().iterator().next() );
        String paxBadgeEarnedString = "";
        String paxBadgeProgressString = "";
        if ( paxBadgeMap.get( "paxBadgeEarnedString" ) != null )
        {
          paxBadgeEarnedString = paxBadgeMap.get( "paxBadgeEarnedString" ).toString();
        }
        if ( !StringUtils.isEmpty( paxBadgeEarnedString ) )
        {
          String paxBadgeEarnedNoHtml = "";
          paxBadgeEarnedNoHtml = HtmlUtils.removeFormatting( paxBadgeEarnedString );
          if ( !StringUtils.isEmpty( paxBadgeEarnedNoHtml.trim() ) )
          {
            dataMap.put( "badge", paxBadgeEarnedString );
            dataMap.put( "badgePlain", HtmlUtils.removeFormatting( paxBadgeEarnedString ) );
          }
        }
      }
    }

    if ( currentPlayerOutcome != null && currentPlayerOutcome.getCurrentValueWithPrecisionAndRounding() != null && match.getRound().getDivision().getMinimumQualifier() != null
        && ( currentPlayerOutcome.getCurrentValueWithPrecisionAndRounding().compareTo( match.getRound().getDivision().getMinimumQualifier() ) == 0
            || currentPlayerOutcome.getCurrentValueWithPrecisionAndRounding().compareTo( match.getRound().getDivision().getMinimumQualifier() ) == 1 ) )
    {
      dataMap.put( "minimumQualifierReached", "true" );
    }
    else
    {
      dataMap.put( "minimumQualifierReached", "false" );
    }

    if ( rankingPayout != null && rankingPayout > 0 )
    {
      dataMap.put( "standingAwards", "TRUE" );
      dataMap.put( "standingRewardAmount", NumberFormatUtil.getUserLocaleBasedNumberFormat( rankingPayout, LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );
    }

    if ( oppositionPlayerOutcome != null )
    {
      if ( oppositionPlayerOutcome.getTeam().isShadowPlayer() )
      {
        dataMap.put( "opponentName", promotion.getTeamUnavailableResolverType().getName() );
        dataMap.put( "opponentActualResults", String.valueOf( shadowScore ) );
      }
      else
      {
        dataMap.put( "opponentName", oppositionPlayerOutcome.getTeam().getParticipant().getNameFLNoComma() );
        dataMap.put( "opponentActualResults", String.valueOf( oppositionPlayerOutcome.getCurrentValueWithPrecisionAndRounding() ) );
      }
    }
    if ( promotion.isSmackTalkAvailable() )
    {
      dataMap.put( "smackTalk", String.valueOf( Boolean.TRUE ) );
    }
    String link = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    dataMap.put( "siteLink", link );
    dataMap.put( "contactUsUrl",
                 systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal()
                     + systemVariableService.getPropertyByName( SystemVariableService.CLIENT_CONTACT_URL ).getStringVal() );
    mailingRecipient.addMailingRecipientDataFromMap( dataMap );

    return mailingRecipient;
  }

  private NodeType getHierarchyPlaceHolderNodeType()
  {
    NodeType all = new NodeType();
    all.setId( -1L );
    all.setCmAssetCode( "system.general" );
    all.setNameCmKey( "ALL" );
    return all;
  }

  @Override
  public MailingRecipient buildMailingRecipientForThrowdownEmail( ThrowdownPromotion promotion, String sb, Round round, Date date, User manager )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setUser( manager );

    if ( manager.getLanguageType() != null )
    {
      mailingRecipient.setLocale( manager.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : manager.getLanguageType().getCode() );
    }
    else
    {
      mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
    }

    Map dataMap = new HashMap();
    if ( promotion.getPromoNameAssetCode() != null )
    {
      Locale locale = CmsUtil.getLocale( mailingRecipient.getLocale() );
      String promotionName = cmAssetService.getString( promotion.getPromoNameAssetCode(), Promotion.PROMOTION_NAME_KEY_PREFIX, locale, true );
      dataMap.put( "programName", StringEscapeUtils.unescapeHtml4( promotionName ) );
      dataMap.put( "promotionName", StringEscapeUtils.unescapeHtml4( promotionName ) );
    }
    dataMap.put( "firstName", manager.getFirstName() );
    dataMap.put( "lastName", manager.getLastName() );

    dataMap.put( "roundNumber", NumberFormatUtil.getUserLocaleBasedNumberFormat( new Long( round.getRoundNumber() ), LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );
    dataMap.put( "roundStartDate", com.biperf.core.utils.DateUtils.toDisplayString( round.getStartDate(), LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );
    dataMap.put( "roundEndDate", com.biperf.core.utils.DateUtils.toDisplayString( round.getEndDate(), LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );
    dataMap.put( "date", com.biperf.core.utils.DateUtils.toDisplayString( new Timestamp( date.getTime() ), LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );

    dataMap.put( "teamProgressTable", sb );
    String link = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    dataMap.put( "siteLink", link );
    dataMap.put( "contactUsUrl",
                 systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal()
                     + systemVariableService.getPropertyByName( SystemVariableService.CLIENT_CONTACT_URL ).getStringVal() );
    if ( promotion.isSmackTalkAvailable() )
    {
      dataMap.put( "smackTalk", String.valueOf( Boolean.TRUE ) );
    }
    mailingRecipient.addMailingRecipientDataFromMap( dataMap );

    return mailingRecipient;
  }

  /**
   * Overridden from @see com.biperf.core.service.email.MailingService#buildMailingRecipientForPartnerGoalQuestEmail(com.biperf.core.domain.promotion.GoalQuestPromotion, com.biperf.core.domain.participant.Participant, com.biperf.core.service.promotion.engine.GoalCalculationResult, com.biperf.core.domain.participant.Participant)
   * @param promotion
   * @param participant
   * @param goalCalculationResult
   * @param partner
   * @return HashMap
   * */

  @Override
  public HashMap buildMailingRecipientForPartnerGoalQuestEmail( GoalQuestPromotion promotion, Participant participant, GoalCalculationResult goalCalculationResult, Participant partner )
  {

    // BugFix 20477
    HashMap dataMap = new HashMap();
    Locale locale = LocaleUtils.getLocale( participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode() );
    dataMap.put( "partnerFirstName", partner.getFirstName() );
    dataMap.put( "PartnerFirstName", partner.getFirstName() );
    dataMap.put( "partnerLastName", partner.getLastName() );
    dataMap.put( "PartnerLastName", partner.getLastName() );
    dataMap.put( "PaxName", participant.getNameLFMWithComma() );
    dataMap.put( "programName", promotion.getName() );
    dataMap.put( "firstName", participant.getFirstName() );
    dataMap.put( "FirstName", participant.getFirstName() );
    dataMap.put( "lastName", participant.getLastName() );
    dataMap.put( "LastName", participant.getLastName() );
    dataMap.put( "user", partner.getUserName() );
    dataMap.put( "password", partner.getPassword() );
    dataMap.put( "date",
                 com.biperf.core.utils.DateUtils
                     .toDisplayString( com.biperf.core.utils.DateUtils.getCurrentDate(),
                                       LocaleUtils.getLocale( participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode() ) ) );
    if ( promotion.getAwardType() != null && promotion.getAwardType().getCode().equalsIgnoreCase( PromotionAwardsType.POINTS ) )
    {
      dataMap.put( "points", "true" );
    }
    else
    {
      dataMap.put( "points", "false" );
    }

    if ( goalCalculationResult.getGoalLevel() != null )
    {
      dataMap.put( "goalLevel", goalCalculationResult.getGoalLevel().getGoalLevelName() );
      /*
       * if( goalCalculationResult.getAmountToAchieve() != null ) { dataMap.put( "goalLevelAmount",
       * goalCalculationResult.getAmountToAchieve().toString() ); }else{ dataMap.put(
       * "goalLevelAmount", "0"); }
       */
      dataMap.put( "goalLevelAmount", goalCalculationResult.getGoalLevel().getGoalLevelDescription() );
    }
    if ( goalCalculationResult.getAmountToAchieve() != null && goalCalculationResult.getAmountToAchieve().floatValue() != 0 )
    {
      String amountToAchieve = NumberFormatUtil.getLocaleBasedBigDecimalFormat( goalCalculationResult.getAmountToAchieve(),
                                                                                promotion.getAchievementPrecision().getPrecision(),
                                                                                LocaleUtils.getLocale( participant.getLanguageType() == null
                                                                                    ? UserManager.getDefaultLocale().toString()
                                                                                    : participant.getLanguageType().getCode() ) );
      dataMap.put( "totalGoalValue", formatBasevalue( amountToAchieve, promotion, locale ) );
    }
    else
    {
      dataMap.put( "totalGoalValue", formatBasevalue( "0", promotion, locale ) );
    }

    if ( goalCalculationResult.isAchieved() && goalCalculationResult.getCalculatedPayout() != null )
    {
      String calculatedPayout = NumberFormatUtil
          .getLocaleBasedBigDecimalFormat( goalCalculationResult.getCalculatedPayout(),
                                           0,
                                           LocaleUtils.getLocale( participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode() ) );
      dataMap.put( "partnerReward", calculatedPayout );
    }
    else
    {
      dataMap.put( "partnerReward", "0" );
    }

    if ( goalCalculationResult.getTotalPerformance() != null && goalCalculationResult.getTotalPerformance().floatValue() != 0 )
    {
      // BugFix 17918 format the goal,base fields as comma seperated values
      String totalPerformance = NumberFormatUtil.getLocaleBasedBigDecimalFormat( goalCalculationResult.getTotalPerformance(),
                                                                                 promotion.getAchievementPrecision().getPrecision(),
                                                                                 LocaleUtils.getLocale( participant.getLanguageType() == null
                                                                                     ? UserManager.getDefaultLocale().toString()
                                                                                     : participant.getLanguageType().getCode() ) );
      dataMap.put( "actualResults", formatBasevalue( totalPerformance, promotion, locale ) );

    }
    else
    {
      dataMap.put( "actualResults", formatBasevalue( "0", promotion, locale ) );
    }
    if ( goalCalculationResult.getAmountToAchieve() != null && goalCalculationResult.getTotalPerformance() != null )
    {

      BigDecimal percentToGoal = goalCalculationResult.getTotalPerformance().divide( goalCalculationResult.getAmountToAchieve(), 2, BigDecimal.ROUND_DOWN ).movePointRight( 2 );
      dataMap.put( "percentToGoal", percentToGoal.toString() );
    }
    else
    {
      dataMap.put( "percentToGoal", "0" );
    }

    List<ParticipantBadge> participantBadges = getGamificationService().getBadgeByParticipantEarnedForPromotion( promotion.getId(), partner.getId() );
    if ( participantBadges != null && participantBadges.size() > 0 )
    {
      dataMap.put( "showBadges", "TRUE" );
      Map paxBadgeMap = new HashMap();
      paxBadgeMap = buildBadgeHtmlString( participantBadges );
      String paxBadgeEarnedString = "";
      String paxBadgeProgressString = "";
      if ( paxBadgeMap.get( "paxBadgeEarnedString" ) != null )
      {
        paxBadgeEarnedString = paxBadgeMap.get( "paxBadgeEarnedString" ).toString();
      }
      if ( paxBadgeMap.get( "paxBadgeProgressString" ) != null )
      {
        paxBadgeProgressString = paxBadgeMap.get( "paxBadgeProgressString" ).toString();
      }
      if ( !StringUtils.isEmpty( paxBadgeEarnedString ) )
      {
        String paxBadgeEarnedNoHtml = "";
        paxBadgeEarnedNoHtml = HtmlUtils.removeFormatting( paxBadgeEarnedString );
        if ( !StringUtils.isEmpty( paxBadgeEarnedNoHtml.trim() ) )
        {
          dataMap.put( "showEarnedBadges", "TRUE" );
          dataMap.put( "paxBadgeEarnedString", paxBadgeEarnedString );
          dataMap.put( "paxBadgeEarnedStringPlain", HtmlUtils.removeFormatting( paxBadgeEarnedString ) );
        }
      }
      if ( !StringUtils.isEmpty( paxBadgeProgressString ) )
      {
        String paxBadgeProgressNoHtml = "";
        paxBadgeProgressNoHtml = HtmlUtils.removeFormatting( paxBadgeProgressString );
        if ( !StringUtils.isEmpty( paxBadgeProgressNoHtml.trim() ) )
        {
          dataMap.put( "showProgressBar", "TRUE" );
          dataMap.put( "paxBadgeProgressString", paxBadgeProgressString );
          dataMap.put( "paxBadgeProgressStringPlain", HtmlUtils.removeFormatting( paxBadgeProgressString ) );
        }
      }
    }

    String systemUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    dataMap.put( "company", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    dataMap.put( "website", systemUrl );
    dataMap.put( "websiteUrl", systemUrl );
    dataMap.put( "siteLink", systemUrl );
    dataMap.put( "url", systemUrl );
    dataMap.put( "link", systemUrl );
    dataMap.put( "siteUrl", systemUrl );
    dataMap.put( "siteURL", systemUrl );
    dataMap.put( "contactUsUrl",
                 systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal()
                     + systemVariableService.getPropertyByName( SystemVariableService.CLIENT_CONTACT_URL ).getStringVal() );

    return dataMap;
  }

  /**
   * Overridden from @see com.biperf.core.service.email.MailingService#buildMailingRecipientForPartnerGoalQuestEmail(com.biperf.core.domain.promotion.GoalQuestPromotion, com.biperf.core.domain.participant.Participant, com.biperf.core.service.promotion.engine.GoalCalculationResult, com.biperf.core.domain.participant.Participant)
   * @param promotion
   * @param participant
   * @param goalCalculationResult
   * @param partner
   * @return HashMap
   * */

  @Override
  public HashMap buildMailingRecipientForPartnerCPEmail( ChallengePointPromotion promotion, Participant participant, ChallengePointCalculationResult cpCalculationResult, Participant partner )
  {

    HashMap dataMap = new HashMap();
    Locale locale = LocaleUtils.getLocale( participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode() );
    dataMap.put( "partnerFirstName", partner.getFirstName() );
    dataMap.put( "PartnerFirstName", partner.getFirstName() );
    dataMap.put( "partnerLastName", partner.getLastName() );
    dataMap.put( "PartnerLastName", partner.getLastName() );
    dataMap.put( "PaxName", participant.getNameLFMWithComma() );
    dataMap.put( "programName", promotion.getName() );
    dataMap.put( "firstName", participant.getFirstName() );
    dataMap.put( "FirstName", participant.getFirstName() );
    dataMap.put( "lastName", participant.getLastName() );
    dataMap.put( "LastName", participant.getLastName() );
    dataMap.put( "user", partner.getUserName() );
    dataMap.put( "password", partner.getPassword() );
    dataMap.put( "date",
                 com.biperf.core.utils.DateUtils
                     .toDisplayString( com.biperf.core.utils.DateUtils.getCurrentDate(),
                                       LocaleUtils.getLocale( participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode() ) ) );
    if ( promotion.getAwardType() != null && promotion.getAwardType().getCode().equalsIgnoreCase( PromotionAwardsType.POINTS ) )
    {
      dataMap.put( "points", "true" );
    }
    else
    {
      dataMap.put( "points", "false" );
    }

    if ( cpCalculationResult.getGoalLevel() != null )
    {
      dataMap.put( "goalLevel", cpCalculationResult.getGoalLevel().getGoalLevelName() );
      dataMap.put( "goalLevelAmount", cpCalculationResult.getGoalLevel().getGoalLevelDescription() );
    }
    if ( cpCalculationResult.getAmountToAchieve() != null && cpCalculationResult.getAmountToAchieve().floatValue() != 0 )
    {
      String amountToAchieve = NumberFormatUtil.getLocaleBasedBigDecimalFormat( cpCalculationResult.getAmountToAchieve(),
                                                                                promotion.getAchievementPrecision().getPrecision(),
                                                                                LocaleUtils.getLocale( participant.getLanguageType() == null
                                                                                    ? UserManager.getDefaultLocale().toString()
                                                                                    : participant.getLanguageType().getCode() ) );
      dataMap.put( "totalGoalValue", formatBasevalue( amountToAchieve, promotion, locale ) );
    }
    else
    {
      dataMap.put( "totalGoalValue", formatBasevalue( "0", promotion, locale ) );
    }

    if ( cpCalculationResult.isAchieved() && cpCalculationResult.getCalculatedAchievement() != null )
    {
      String calculatedPayout = NumberFormatUtil
          .getLocaleBasedBigDecimalFormat( cpCalculationResult.getCalculatedAchievement(),
                                           0,
                                           LocaleUtils.getLocale( participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode() ) );
      dataMap.put( "partnerReward", calculatedPayout );
    }
    else
    {
      dataMap.put( "partnerReward", "0" );
    }

    if ( cpCalculationResult.getTotalPerformance() != null && cpCalculationResult.getTotalPerformance().floatValue() != 0 )
    {
      String totalPerformance = NumberFormatUtil.getLocaleBasedBigDecimalFormat( cpCalculationResult.getTotalPerformance(),
                                                                                 promotion.getAchievementPrecision().getPrecision(),
                                                                                 LocaleUtils.getLocale( participant.getLanguageType() == null
                                                                                     ? UserManager.getDefaultLocale().toString()
                                                                                     : participant.getLanguageType().getCode() ) );
      dataMap.put( "actualResults", formatBasevalue( totalPerformance, promotion, locale ) );

    }
    else
    {
      dataMap.put( "actualResults", formatBasevalue( "0", promotion, locale ) );
    }
    if ( cpCalculationResult.getAmountToAchieve() != null && cpCalculationResult.getTotalPerformance() != null )
    {

      BigDecimal percentToGoal = cpCalculationResult.getTotalPerformance().divide( cpCalculationResult.getAmountToAchieve(), 2, BigDecimal.ROUND_DOWN ).movePointRight( 2 );
      dataMap.put( "percentToGoal", percentToGoal.toString() );
    }
    else
    {
      dataMap.put( "percentToGoal", "0" );
    }

    List<ParticipantBadge> participantBadges = getGamificationService().getBadgeByParticipantEarnedForPromotion( promotion.getId(), partner.getId() );
    if ( participantBadges != null && participantBadges.size() > 0 )
    {
      dataMap.put( "showBadges", "TRUE" );
      Map paxBadgeMap = new HashMap();
      paxBadgeMap = buildBadgeHtmlString( participantBadges );
      String paxBadgeEarnedString = "";
      String paxBadgeProgressString = "";
      if ( paxBadgeMap.get( "paxBadgeEarnedString" ) != null )
      {
        paxBadgeEarnedString = paxBadgeMap.get( "paxBadgeEarnedString" ).toString();
      }
      if ( paxBadgeMap.get( "paxBadgeProgressString" ) != null )
      {
        paxBadgeProgressString = paxBadgeMap.get( "paxBadgeProgressString" ).toString();
      }
      if ( !StringUtils.isEmpty( paxBadgeEarnedString ) )
      {
        String paxBadgeEarnedNoHtml = "";
        paxBadgeEarnedNoHtml = HtmlUtils.removeFormatting( paxBadgeEarnedString );
        if ( !StringUtils.isEmpty( paxBadgeEarnedNoHtml.trim() ) )
        {
          dataMap.put( "showEarnedBadges", "TRUE" );
          dataMap.put( "paxBadgeEarnedString", paxBadgeEarnedString );
          dataMap.put( "paxBadgeEarnedStringPlain", HtmlUtils.removeFormatting( paxBadgeEarnedString ) );
        }
      }
      if ( !StringUtils.isEmpty( paxBadgeProgressString ) )
      {
        String paxBadgeProgressNoHtml = "";
        paxBadgeProgressNoHtml = HtmlUtils.removeFormatting( paxBadgeProgressString );
        if ( !StringUtils.isEmpty( paxBadgeProgressNoHtml.trim() ) )
        {
          dataMap.put( "showProgressBar", "TRUE" );
          dataMap.put( "paxBadgeProgressString", paxBadgeProgressString );
          dataMap.put( "paxBadgeProgressStringPlain", HtmlUtils.removeFormatting( paxBadgeProgressString ) );
        }
      }
    }

    String systemUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    dataMap.put( "company", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    dataMap.put( "website", systemUrl );
    dataMap.put( "websiteUrl", systemUrl );
    dataMap.put( "siteLink", systemUrl );
    dataMap.put( "url", systemUrl );
    dataMap.put( "link", systemUrl );
    dataMap.put( "siteUrl", systemUrl );
    dataMap.put( "siteURL", systemUrl );
    dataMap.put( "contactUsUrl",
                 systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal()
                     + systemVariableService.getPropertyByName( SystemVariableService.CLIENT_CONTACT_URL ).getStringVal() );

    return dataMap;
  }

  public MailingRecipient buildMailingRecipientForPurlManagerInvitation( PurlRecipient purlRecipient )
  {
    Participant nodeOwner = getPurlService().getNodeOwnerForPurlRecipient( purlRecipient );
    if ( null == nodeOwner )
    {
      return null;
    }

    MailingRecipient mailingRecipient = createMailingRecipientForPurl( nodeOwner );

    Map dataMap = new HashMap();
    dataMap.put( "managerFirstName", nodeOwner.getFirstName() );
    dataMap.put( "managerLastName", nodeOwner.getLastName() );
    dataMap.put( "firstName", purlRecipient.getUser().getFirstName() );
    dataMap.put( "lastName", purlRecipient.getUser().getLastName() );

    StringBuffer purlRecipientName = new StringBuffer();

    purlRecipientName.append( purlRecipient.getUser().getFirstName() ).append( " " ).append( purlRecipient.getUser().getLastName() ).append( " " );
    // if ( purlRecipient.getAwardAmount() != null )
    // {
    // String awardAmount =
    // NumberFormatUtil.getLocaleBasedBigDecimalFormat(purlRecipient.getAwardAmount(), 0,
    // LocaleUtils.getLocale(mailingRecipient.getLocale().toString()));
    // purlRecipientName.append( awardAmount );
    // purlRecipientName.append( " " );
    // purlRecipientName.append( purlRecipient.getPromotion().getAwardType().getAbbr() );
    // }
    // if ( purlRecipient.getAwardLevel() != null )
    // {
    // purlRecipientName.append( purlRecipient.getAwardLevel().getDisplayLevelName() );
    // }
    // if ( purlRecipient.getAwardAmount() != null || purlRecipient.getAwardLevel() != null )
    // {
    // purlRecipientName.append( " " );
    // }
    /*
     * if ( purlRecipient.getAwardDate() != null ) { purlRecipientName.append(
     * com.biperf.core.utils.DateUtils.toDisplayString(purlRecipient.getAwardDate(),
     * LocaleUtils.getLocale(mailingRecipient.getLocale()) )); }
     */

    dataMap.put( "awardDate", com.biperf.core.utils.DateUtils.toDisplayString( purlRecipient.getAwardDate(), LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );

    dataMap.put( "purlRecipientName", purlRecipientName.toString() );

    if ( purlRecipient.getPromotion().getPromoNameAssetCode() != null )
    {
      Locale locale = CmsUtil.getLocale( mailingRecipient.getLocale() );
      String promotionName = cmAssetService.getString( purlRecipient.getPromotion().getPromoNameAssetCode(), Promotion.PROMOTION_NAME_KEY_PREFIX, locale, true );
      dataMap.put( "promotionName", StringEscapeUtils.unescapeHtml4( promotionName ) );
    }
    // Purl Invitation End Notification date : purl contribution end date
    dataMap.put( "purlInvitationEndDate", com.biperf.core.utils.DateUtils.toDisplayString( purlRecipient.getCloseDate(), LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );

    Map clientStateParamMap = new HashMap();
    // QC bug #3373
    clientStateParamMap.put( "purlRecipientId", purlRecipient.getId() );
    clientStateParamMap.put( "purlReturnUrl", null );
    String purlMaintenanceLink = ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                                       "/recognitionWizard/purlInviteContributors.do?method=display",
                                                                       clientStateParamMap );

    dataMap.put( "purlMaintenanceLink", purlMaintenanceLink );

    if ( purlRecipient.getCustomElements() != null && !purlRecipient.getCustomElements().isEmpty() )
    {
      for ( int i = 0; i < purlRecipient.getCustomElements().size(); i++ )
      {
        dataMap.put( "formElement" + ( i + 1 ), purlRecipient.getCustomElement( i ).getDisplayValue() );
      }
    }



 /*Customization for WIP 32479 starts here*/
    
    ContentReader contentReader = ContentReaderManager.getContentReader();
    List<Content> resourceCenter = (List<Content>)contentReader.getContent( "participant.termsAndCondition", UserManager.getDefaultLocale() );
    String privacyPolicyUrl = null;
    for( Content content : resourceCenter )
    {
      privacyPolicyUrl = (String)content.getContentDataMap().get( "URL" );
      break;
    }
    String privacyPolicyUrlLink = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/" + privacyPolicyUrl;
    dataMap.put( "privacyPolicyUrlLink", privacyPolicyUrlLink );
    
    String termsAndConditionsUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/assets/client/resource/PURL_T&C.pdf";
    dataMap.put( "termsAndConditionsUrl", termsAndConditionsUrl );
    
    String externalUnsubscribeLink = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/purl/externalUnsubscribe.do?method=unsubscribeDisplay";
    dataMap.put( "externalUnsubscribeLink", externalUnsubscribeLink );
    
    /*Customization for WIP 32479 ends here*/

    mailingRecipient.addMailingRecipientDataFromMap( dataMap );

    return mailingRecipient;
  }



  /* custom for wip #46293 */
  public MailingRecipient buildMailingRecipientForPurlContributorInvitation( PurlContributor purlContributor, Long nonContributorUserId, boolean isAutoInvite )

  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    Map dataMap = new HashMap();

    if ( purlContributor.getUser() != null )
    {
      Participant participant = getParticipantService().getParticipantById( purlContributor.getUser().getId() );
      mailingRecipient = createMailingRecipientForPurl( participant );
    }
    else
    {
      mailingRecipient.setGuid( GuidUtils.generateGuid() );
      mailingRecipient.setPreviewEmailAddress( purlContributor.getEmailAddr() );
      mailingRecipient.setLocale( MailingMessageLocale.DEFAULT_LOCALE );
    }

    if ( !Objects.isNull( purlContributor.getFirstName() ) && !purlContributor.getFirstName().isEmpty() )
    {
      dataMap.put( "contributorFirstName", purlContributor.getFirstName() );
      dataMap.put( "contributorLastName", purlContributor.getLastName() );
    }
    else
    {
      dataMap.put( "contributorFirstName", "" );
      dataMap.put( "contributorLastName", "" );
    }

    if ( purlContributor.getInvitedContributor() != null )
    {
      dataMap.put( "invitedContributorFirstName", purlContributor.getInvitedContributor().getFirstName() );
      dataMap.put( "invitedContributorLastName", purlContributor.getInvitedContributor().getLastName() );
    }
    else if ( null != nonContributorUserId )
    {
      Participant nonContributorUser = getParticipantService().getParticipantById( nonContributorUserId );
      if ( null != nonContributorUser )
      {
        dataMap.put( "invitedContributorFirstName", nonContributorUser.getFirstName() );
        dataMap.put( "invitedContributorLastName", nonContributorUser.getLastName() );
      }
    }
    if ( purlContributor.getPurlRecipient().getPromotion().getPromoNameAssetCode() != null )
    {
      Locale locale = CmsUtil.getLocale( mailingRecipient.getLocale() );
      String promotionName = cmAssetService.getString( purlContributor.getPurlRecipient().getPromotion().getPromoNameAssetCode(), Promotion.PROMOTION_NAME_KEY_PREFIX, locale, true );
      dataMap.put( "promotionName", StringEscapeUtils.unescapeHtml4( promotionName ) );
    }

    StringBuffer purlRecipientFullName = new StringBuffer();
    purlRecipientFullName.append( purlContributor.getPurlRecipient().getUser().getFirstName() ).append( " " ).append( purlContributor.getPurlRecipient().getUser().getLastName() ).append( " " );

    dataMap.put( "purlRecipientName", purlRecipientFullName.toString() );

    dataMap.put( "recipientFirstName", purlContributor.getPurlRecipient().getUser().getFirstName() );
    dataMap.put( "recipientLastName", purlContributor.getPurlRecipient().getUser().getLastName() );

    dataMap.put( "invitationStartDate", com.biperf.core.utils.DateUtils.toDisplayString( purlContributor.getPurlRecipient().getAwardDate(), LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );
    dataMap.put( "contributorCloseDate", com.biperf.core.utils.DateUtils.toDisplayString( purlContributor.getPurlRecipient().getCloseDate(), LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );
    dataMap.put( "defaultInvitee", purlContributor.isDefaultInvitee() );

    Map clientStateParamMap = new HashMap();
    clientStateParamMap.put( "promotionId", purlContributor.getPurlRecipient().getPromotion().getId() );
    if ( purlContributor.getId() != null )
    {
      clientStateParamMap.put( "purlContributorId", purlContributor.getId() );
    }
    String purlContributorLink = ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                                       "/purl/purlTNC.do?method=display",
                                                                       clientStateParamMap );

    dataMap.put( "purlContributorLink", purlContributorLink );
    String encodedLink = null;
    try
    {
      encodedLink = URLEncoder.encode( purlContributorLink, "UTF-8" );
    }
    catch( UnsupportedEncodingException e )
    {
      log.error( "<<<<ERROR>>>>>" + e );
    }
    dataMap.put( "shortLink", getShortUrl( encodedLink ) );

    dataMap.put( "companyName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );

    if ( purlContributor.getPurlRecipient().getCustomElements() != null && !purlContributor.getPurlRecipient().getCustomElements().isEmpty() )
    {
      for ( int i = 0; i < purlContributor.getPurlRecipient().getCustomElements().size(); i++ )
      {
        dataMap.put( "formElement" + ( i + 1 ), purlContributor.getPurlRecipient().getCustomElement( i ).getDisplayValue() );
      }
    }



 /*Customization for WIP 32479 starts here*/
    
    ContentReader contentReader = ContentReaderManager.getContentReader();
    List<Content> resourceCenter = (List<Content>)contentReader.getContent( "participant.termsAndCondition", UserManager.getDefaultLocale() );
    String privacyPolicyUrl = null;
    for( Content content : resourceCenter )
    {
      privacyPolicyUrl = (String)content.getContentDataMap().get( "URL" );
      break;
    }
    String privacyPolicyUrlLink = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/" + privacyPolicyUrl;
    dataMap.put( "privacyPolicyUrlLink", privacyPolicyUrlLink );
    
    String termsAndConditionsUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/assets/client/resource/PURL_T&C.pdf";
    dataMap.put( "termsAndConditionsUrl", termsAndConditionsUrl );
    
    String externalUnsubscribeLink = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/purl/externalUnsubscribe.do?method=unsubscribeDisplay";
    dataMap.put( "externalUnsubscribeLink", externalUnsubscribeLink );
    
    /*Customization for WIP 32479 ends here*/

    mailingRecipient.addMailingRecipientDataFromMap( dataMap );

    if ( purlContributor.getUser() != null )
    {
      // Send purl notification
      String milestone = purlContributor.getPurlRecipient().getCustomFormElementInfo() + " " + purlContributor.getPurlRecipient().getPromotion().getName();
      getMobileNotificationService().purlContributorInviteNotification( purlContributor.getUser().getLanguageType() != null ? purlContributor.getUser().getLanguageType().getCode() : null,
                                                                        purlContributor.getPurlRecipient().getUser().getFirstName(),
                                                                        purlContributor.getPurlRecipient().getUser().getLastName(),
                                                                        purlContributor.getPurlRecipient().getId(),
                                                                        milestone,
                                                                        purlContributor.getUser().getId(),
                                                                        false );
    }

    return mailingRecipient;
  }



 /*Custom for wip # 46293* starts/ */
  
  public MailingRecipient buildMailingRecipientForCokeCustomPurlContributorInvitation( PurlContributor purlContributor, Long nonContributorUserId, boolean isAutoInvite )
  {
	  MailingRecipient mailingRecipient = new MailingRecipient();
	    Map dataMap = new HashMap();
	    
	    if ( purlContributor.getUser() != null )
	    {
	      Participant participant = getParticipantService().getParticipantById( purlContributor.getUser().getId() );
	      mailingRecipient = createMailingRecipientForPurl( participant );
	    }
	    else
	    {
	      mailingRecipient.setGuid( GuidUtils.generateGuid() );
	      mailingRecipient.setPreviewEmailAddress( purlContributor.getEmailAddr() );
	      mailingRecipient.setLocale( MailingMessageLocale.DEFAULT_LOCALE );
	    }

	    dataMap.put( "contributorFirstName", purlContributor.getFirstName() );
	    dataMap.put( "contributorLastName", purlContributor.getLastName() );

	    if ( purlContributor.getInvitedContributor() != null )
	    {
	      dataMap.put( "invitedContributorFirstName", purlContributor.getInvitedContributor().getFirstName() );
	      dataMap.put( "invitedContributorLastName", purlContributor.getInvitedContributor().getLastName() );
	    }
	    else if ( null != nonContributorUserId )
	    {
	      Participant nonContributorUser = getParticipantService().getParticipantById( nonContributorUserId );
	      if ( null != nonContributorUser )
	      {
	        dataMap.put( "invitedContributorFirstName", nonContributorUser.getFirstName() );
	        dataMap.put( "invitedContributorLastName", nonContributorUser.getLastName() );
	      }
	    }
	  User user =   purlContributor.getPurlRecipient().getUser();
	  Node node =   purlContributor.getPurlRecipient().getUser().getPrimaryUserNode().getNode();
	  User manager =  getNodeService().getNodeOwnerForUser(user, node);
	 
	
	  
	    if( manager != null )
	    {
	    	dataMap.put( "ManagerFirstName", manager.getFirstName() );
	    	dataMap.put( "ManagerLastName", manager.getLastName() );
	    }
	    
	    
	    
	    if ( purlContributor.getPurlRecipient().getPromotion().getPromoNameAssetCode() != null )
	    {
	      Locale locale = CmsUtil.getLocale( mailingRecipient.getLocale() );
	      String promotionName = cmAssetService.getString( purlContributor.getPurlRecipient().getPromotion().getPromoNameAssetCode(), Promotion.PROMOTION_NAME_KEY_PREFIX, locale, true );
	      dataMap.put( "promotionName", StringEscapeUtils.unescapeHtml4( promotionName ) );
	    }

	    StringBuffer purlRecipientFullName = new StringBuffer();
	    purlRecipientFullName.append( purlContributor.getPurlRecipient().getUser().getFirstName() ).append( " " ).append( purlContributor.getPurlRecipient().getUser().getLastName() ).append( " " );

	    dataMap.put( "purlRecipientName", purlRecipientFullName.toString() );

	    dataMap.put( "recipientFirstName", purlContributor.getPurlRecipient().getUser().getFirstName() );
	    dataMap.put( "recipientLastName", purlContributor.getPurlRecipient().getUser().getLastName() );

	    //dataMap.put("invitationStartDate", com.biperf.core.utils.DateUtils.toDisplayString( purlContributor.getPurlRecipient().getAwardDate(), LocaleUtils.getLocale(mailingRecipient.getLocale())  ) );
	    dataMap.put("contributorCloseDate", com.biperf.core.utils.DateUtils.toDisplayString( purlContributor.getPurlRecipient().getCloseDate(), LocaleUtils.getLocale(mailingRecipient.getLocale())  ) );

	    Map clientStateParamMap = new HashMap();
	    clientStateParamMap.put( "promotionId", purlContributor.getPurlRecipient().getPromotion().getId() );
	    if ( purlContributor.getId() != null )
	    {
	      clientStateParamMap.put( "purlContributorId", purlContributor.getId() );
	    }
	    String purlContributorLink = ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
	                                                                       "/purl/purlTNC.do?method=display",
	                                                                       clientStateParamMap );

	    dataMap.put( "purlContributorLink", purlContributorLink );
	    String encodedLink = null;
	    try
	    {
	      encodedLink = URLEncoder.encode( purlContributorLink, "UTF-8" );
	    }
	    catch( UnsupportedEncodingException e )
	    {
	      log.error( "<<<<ERROR>>>>>" + e );
	    }
	    dataMap.put( "shortLink", getShortUrl( encodedLink ) );

	    dataMap.put( "companyName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );

	    if ( purlContributor.getPurlRecipient().getCustomElements() != null && !purlContributor.getPurlRecipient().getCustomElements().isEmpty() )
	    {
	      for ( int i = 0; i < purlContributor.getPurlRecipient().getCustomElements().size(); i++ )
	      {
	        dataMap.put( "formElement" + ( i + 1 ), purlContributor.getPurlRecipient().getCustomElement( i ).getDisplayValue() );
	      }
	    }
	    /*Customization for WIP 32479 starts here*/
	    
	    ContentReader contentReader = ContentReaderManager.getContentReader();
	    List<Content> resourceCenter = (List<Content>)contentReader.getContent( "participant.termsAndCondition", UserManager.getDefaultLocale() );
	    String privacyPolicyUrl = null;
	    for( Content content : resourceCenter )
	    {
	      privacyPolicyUrl = (String)content.getContentDataMap().get( "URL" );
	      break;
	    }
	    String privacyPolicyUrlLink = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/" + privacyPolicyUrl;
	    dataMap.put( "privacyPolicyUrlLink", privacyPolicyUrlLink );
	    
	    String termsAndConditionsUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/assets/client/resource/PURL_T&C.pdf";
	    dataMap.put( "termsAndConditionsUrl", termsAndConditionsUrl );
	    
	    String externalUnsubscribeLink = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/purl/externalUnsubscribe.do?method=unsubscribeDisplay";
	    dataMap.put( "externalUnsubscribeLink", externalUnsubscribeLink );
	    
	    /*Customization for WIP 32479 ends here*/
	    mailingRecipient.addMailingRecipientDataFromMap( dataMap );
	  
	return mailingRecipient;
	  
  }
  /*Custom for wip # 46293* end/ */
  

  public MailingRecipient buildMailingRecipientForPurlRecipientInvitation( PurlRecipient purlRecipient )
  {
    Promotion promotion = purlRecipient.getPromotion();
    Participant participant = getParticipantService().getParticipantById( purlRecipient.getUser().getId() );
    MailingRecipient mailingRecipient = createMailingRecipientForPurl( participant );

    // Fixed 56312
    Set claimRecipients = ( (RecognitionClaim)purlRecipient.getClaim() ).getClaimRecipients();

    for ( Iterator iter = claimRecipients.iterator(); iter.hasNext(); )
    {
      ClaimRecipient claimRecipient = (ClaimRecipient)iter.next();

      mailingRecipient.setClaimRecipientId( claimRecipient.getId() );

    }

    Map dataMap = new HashMap();
    if ( promotion.getPromoNameAssetCode() != null )
    {
      Locale locale = CmsUtil.getLocale( mailingRecipient.getLocale() );
      String promotionName = cmAssetService.getString( promotion.getPromoNameAssetCode(), Promotion.PROMOTION_NAME_KEY_PREFIX, locale, true );
      dataMap.put( "promotionName", StringEscapeUtils.unescapeHtml4( promotionName ) );
    }

    if ( promotion.getAwardType().getCode().equals( PromotionAwardsType.POINTS ) )
    {
      dataMap.put( "pointsType", "TRUE" );
    }
    if ( promotion.getAwardType().getCode().equals( PromotionAwardsType.MERCHANDISE ) )
    {
      dataMap.put( "merchandiseType", "TRUE" );
    }
    dataMap.put( "recipientFirstName", purlRecipient.getUser().getFirstName() );

    int numOfDays = systemVariableService.getPropertyByName( SystemVariableService.PURL_DAYS_TO_EXP ).getIntVal();
    Date purlExpireDate = new Date( purlRecipient.getAwardDate().getTime() + numOfDays * DateUtils.MILLIS_PER_DAY );
    dataMap.put( "purlExpireDate", com.biperf.core.utils.DateUtils.toDisplayString( purlExpireDate, LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );

    if ( purlRecipient.getAwardAmount() != null && !new BigDecimal( 0 ).equals( purlRecipient.getAwardAmount() ) )
    {
      Long awardQuantity = purlRecipient.getAwardAmount().longValue();
      if ( awardQuantity > 1 )
      {
        dataMap.put( "manyAwardAmount", "TRUE" );
      }
      String awardAmount = NumberFormatUtil.getLocaleBasedBigDecimalFormat( purlRecipient.getAwardAmount(), 0, LocaleUtils.getLocale( mailingRecipient.getLocale().toString() ) );
      dataMap.put( "awardAmount", String.valueOf( awardAmount ) );

      Locale locale = CmsUtil.getLocale( mailingRecipient.getLocale() );
      ContentReader contentReader = ContentReaderManager.getContentReader();

      List contentList = (List)contentReader.getContent( "picklist.promotion.awardstype.items", locale );

      for ( Iterator iter = contentList.iterator(); iter.hasNext(); )
      {
        Content content = (Content)iter.next();
        String code = (String)content.getContentDataMap().get( PickListItem.ITEM_CODE_KEY );
        String status = (String)content.getContentDataMap().get( PickListItem.ITEM_STATUS_KEY );
        if ( promotion.getAwardType().getCode().equalsIgnoreCase( code ) && status.equals( "true" ) )
        {
          String mediaType = (String)content.getContentDataMap().get( PickListItem.ITEM_NAME_KEY );
          dataMap.put( "mediaType", StringEscapeUtils.unescapeHtml4( mediaType ) );
          break;
        }
      }
    }
    else if ( purlRecipient.getAwardLevel() != null )
    {
      dataMap.put( "awardLevel", purlRecipient.getAwardLevel().getDisplayLevelName() );
    }
    String purlUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.PURL_URL_PREFIX ).getStringVal();
    StringBuffer purlRecipientLink = new StringBuffer( purlUrl );
    purlRecipientLink.append( '/' );
    purlRecipientLink.append( purlRecipient.getUser().getFirstName().replace( PurlService.PURL_RECIPIENT_URL_NAME, PurlService.PURL_RECIPIENT_URL_DELIMITER ) );
    purlRecipientLink.append( '.' );
    purlRecipientLink.append( purlRecipient.getUser().getLastName().replace( PurlService.PURL_RECIPIENT_URL_NAME, PurlService.PURL_RECIPIENT_URL_DELIMITER ) );
    purlRecipientLink.append( '.' );
    purlRecipientLink.append( purlRecipient.getId() );
    purlRecipientLink.append( "?viewingId=" + purlRecipient.getUser().getId() );

    dataMap.put( "purlRecipientLink", purlRecipientLink.toString() );
    String encodedLink = null;
    try
    {
      encodedLink = URLEncoder.encode( purlRecipientLink.toString(), "UTF-8" );
    }
    catch( UnsupportedEncodingException e )
    {
      log.error( "<<<<ERROR>>>>>" + e );
    }
    dataMap.put( "shortLink", getShortUrl( encodedLink ) );

    if ( purlRecipient.getCustomElements() != null && !purlRecipient.getCustomElements().isEmpty() )
    {
      for ( int i = 0; i < purlRecipient.getCustomElements().size(); i++ )
      {
        dataMap.put( "formElement" + ( i + 1 ), purlRecipient.getCustomElement( i ).getDisplayValue() );
      }
    }

    mailingRecipient.addMailingRecipientDataFromMap( dataMap );

    return mailingRecipient;
  }

  /**
   * @param merchOrder
   * @return MailingRecipient
   */
  public MailingRecipient buildMailingRecipientForMerchOrderGiftCodeRefundEmail( MerchOrder merchOrder, String emailAddress, String message )
  {
    Participant participant = merchOrder.getParticipant();
    Promotion promotion = merchOrder.getPromoMerchProgramLevel().getPromoMerchCountry().getPromotion();
    String newGiftCode = merchOrder.getFullGiftCode();
    String giftCodeIssueDate = com.biperf.core.utils.DateUtils
        .toDisplayString( merchOrder.getAuditCreateInfo().getDateCreated(),
                          LocaleUtils.getLocale( participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode() ) );

    MailingRecipient mailingRecipient = createMailingRecipientForGiftCodeRefundEmail( participant, emailAddress );

    if ( merchOrder.getClaim() instanceof AbstractRecognitionClaim )
    {
      AbstractRecognitionClaim claim = (AbstractRecognitionClaim)merchOrder.getClaim();
      ClaimRecipient claimRecipient = getClaimRecipientForRecognition( claim, participant );
      mailingRecipient.setClaimRecipientId( claimRecipient.getId() );
    }

    Map dataMap = new HashMap();
    dataMap.put( "levelName", merchOrder.getPromoMerchProgramLevel().getDisplayLevelName() );
    dataMap.put( "phoneNumber", systemVariableService.getPropertyByName( SystemVariableService.MERCHLINQ_ORDER_PHONE ).getStringVal() );
    if ( StringUtils.isNotBlank( merchOrder.getReferenceNumber() ) )
    {
      dataMap.put( "referenceNumber", merchOrder.getReferenceNumber() );
    }
    if ( participant != null )
    {
      dataMap.put( "firstName", participant.getFirstName() );
      dataMap.put( "lastName", participant.getLastName() );
    }
    if ( promotion.getPromoNameAssetCode() != null )
    {
      Locale locale = CmsUtil.getLocale( mailingRecipient.getLocale() );
      String promotionName = cmAssetService.getString( promotion.getPromoNameAssetCode(), Promotion.PROMOTION_NAME_KEY_PREFIX, locale, true );
      dataMap.put( "promotionName", StringEscapeUtils.unescapeHtml4( promotionName ) );
    }
    if ( StringUtils.isNotEmpty( newGiftCode ) )
    {
      String aesEncryptionKey = systemVariableService.getPropertyByName( SystemVariableService.SSO_AES256_KEY ).getStringVal();
      String aesInitVector = systemVariableService.getPropertyByName( SystemVariableService.SSO_INIT_VECTOR ).getStringVal();
      String newGiftCodeEncrypted = null;
      log.debug( "Gift code to encrypt: " + newGiftCode );
      try
      {
        newGiftCodeEncrypted = SecurityUtils.encryptAES( newGiftCode, aesEncryptionKey, aesInitVector );
        log.debug( "Gift code after encryption and before sending email: " + newGiftCodeEncrypted );
      }
      catch( Exception e )
      {
        log.error( "Unable to encrypt gift code: " + newGiftCode, e );
      }
      dataMap
          .put( "levelLink",
                systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/merchLevelShopping.do?method=shopOnline&gc=" + newGiftCodeEncrypted );
      dataMap.put( "giftCode", newGiftCode );
    }
    dataMap.put( "issueDate", giftCodeIssueDate );
    if ( null != merchOrder.getExpirationDate() )
    {
      dataMap.put( "expirationDate", com.biperf.core.utils.DateUtils.toDisplayString( merchOrder.getExpirationDate(), LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );
    }
    if ( StringUtils.isNotBlank( message ) )
    {
      dataMap.put( "message", message );
    }
    mailingRecipient.addMailingRecipientDataFromMap( dataMap );

    return mailingRecipient;
  }

  /**
   * @param journalEntry
   * @return MailingRecipient
   */
  private MailingRecipient createMailingRecipientForGiftCodeRefundEmail( Participant participant, String emailAddress )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setUser( participant );
    mailingRecipient.setPreviewEmailAddress( emailAddress );

    if ( participant != null && participant.getLanguageType() != null )
    {
      mailingRecipient.setLocale( participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode() );
    }
    else
    {
      mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
    }

    return mailingRecipient;
  }

  private MailingRecipient createMailingRecipientForPurl( Participant participant )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setUser( participant );
    // mailingRecipient.setPreviewEmailAddress( emailAddress );

    if ( participant != null && participant.getLanguageType() != null )
    {
      mailingRecipient.setLocale( participant.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : participant.getLanguageType().getCode() );
    }
    else
    {
      mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
    }

    Map dataMap = new HashMap();
    dataMap.put( "companyName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    mailingRecipient.addMailingRecipientDataFromMap( dataMap );

    return mailingRecipient;
  }

  private String buildMerchLinqUrlForGiftCodeRefundEmail( String newGiftCode, Long promotionId )
  {
    String siteUrlPrefix = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

    Map params = new HashMap();
    params.put( "giftcode", newGiftCode );
    // CR - Convert to PerQs - START
    params.put( "promotionId", promotionId );
    // CR - Convert to PerQs - END

    return ClientStateUtils.generateEncodedLink( siteUrlPrefix, "/shopping.do?method=displayMerchLinq", params );
  }

  /**
   * @param claim
   * @return Set of MailingRecipients
   */
  private Set buildMailingRecipientsForQuizEmail( QuizClaim claim, boolean isCalculationSuccessful, Long depositedPoints )
  {
    Set recipients = new HashSet();

    MailingRecipient mailingRecipient = new MailingRecipient();

    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setUser( claim.getSubmitter() );
    if ( claim.getSubmitter().getLanguageType() != null )
    {
      mailingRecipient.setLocale( claim.getSubmitter().getLanguageType().getCode() );
    }
    else
    {
      mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
    }

    QuizPromotion quizPromotion = (QuizPromotion)claim.getPromotion();

    Map dataMap = new HashMap();
    dataMap.put( "firstName", claim.getSubmitter().getFirstName() );
    dataMap.put( "lastName", claim.getSubmitter().getLastName() );

    if ( quizPromotion.isDIYQuizPromotion() && claim.getQuiz() != null )
    {
      dataMap.put( "programName", claim.getQuiz().getName() );
    }
    else
    {
      dataMap.put( "programName", quizPromotion.getName() );
    }

    Long points = null;

    if ( !quizPromotion.isDIYQuizPromotion() && quizPromotion.isAwardActive() && quizPromotion.isBudgetUsed() && isCalculationSuccessful )
    {
      Budget budget = getPromotionService().getAvailableBudget( quizPromotion.getId(), UserManager.getUserId(), null );
      BudgetMaster budgetMaster = getBudgetMasterService().getBudgetMasterById( quizPromotion.getBudgetMaster().getId(), new AssociationRequestCollection() );

      if ( budget != null && budget.getCurrentValue() != null )
      {
        if ( budgetMaster != null )
        {
          long currentBudgetAvailable = budget.getCurrentValue().longValue();
          long depositedPts = depositedPoints.longValue();
          if ( currentBudgetAvailable + depositedPts < quizPromotion.getAwardAmount() )
          {
            if ( BudgetFinalPayoutRule.PARTIAL_PAYOUT.equals( budgetMaster.getFinalPayoutRule().getCode() ) )
            {
              points = depositedPoints;
            }
            else if ( BudgetFinalPayoutRule.NO_PAYOUT.equals( budgetMaster.getFinalPayoutRule().getCode() ) )
            {
              points = 0L;
            }
            else if ( BudgetFinalPayoutRule.FULL_PAYOUT.equals( budgetMaster.getFinalPayoutRule().getCode() ) )
            {
              points = quizPromotion.getAwardAmount();
            }
          }
          else
          {
            points = quizPromotion.getAwardAmount();
          }
        }
      }
    }
    else if ( quizPromotion.isAwardActive() && !quizPromotion.isBudgetUsed() )
    {
      points = quizPromotion.getAwardAmount();
    }
    if ( points != null && points.longValue() > 1 )
    {
      dataMap.put( "manyAwardAmount", "TRUE" );
      String awardAmount = NumberFormatUtil.getUserLocaleBasedNumberFormat( points, LocaleUtils.getLocale( mailingRecipient.getLocale().toString() ) );
      if ( claim.getSubmitter().getOptOutAwards() )
      {
        dataMap.put( "awardAmount", String.valueOf( new Long( 0 ) ) );
      }
      else
      {
        dataMap.put( "awardAmount", String.valueOf( awardAmount ) );
      }

      dataMap.put( "mediaType", quizPromotion.getAwardType().getAbbr() );
    }
    if ( quizPromotion.isDIYQuizPromotion() && quizPromotion.isIncludePassingQuizCertificate() )
    {
      DIYQuiz diyQuiz = (DIYQuiz)claim.getQuiz();
      if ( diyQuiz.getCertificate() != null && diyQuiz.getCertificate().getCertificateId() != null )
      {
        createQuizCertificateLink( claim, mailingRecipient, quizPromotion, dataMap );
      }
    }
    else if ( quizPromotion.isIncludePassingQuizCertificate() )
    {
      createQuizCertificateLink( claim, mailingRecipient, quizPromotion, dataMap );
    }

    buildQuizBadge( claim, quizPromotion, dataMap );

    dataMap.put( "claimNumber", claim.getClaimNumber() );

    // If sweepstakes is active.
    if ( quizPromotion.isSweepstakesActive() )
    {
      dataMap.put( "showSweeps", "TRUE" );
    }

    mailingRecipient.addMailingRecipientDataFromMap( dataMap );

    recipients.add( mailingRecipient );

    return recipients;
  }

  private void createQuizCertificateLink( QuizClaim claim, MailingRecipient mailingRecipient, QuizPromotion quizPromotion, Map dataMap )
  {
    dataMap.put( "showCertificate", "TRUE" );

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "userId", claim.getSubmitter().getId() );
    clientStateParameterMap.put( "claimId", claim.getId() );
    clientStateParameterMap.put( "promotionId", quizPromotion.getId() );

    String certificatelink = ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                                   "/claim/displayCertificate.do?method=showCertificateQuizDetail",
                                                                   clientStateParameterMap );

    dataMap.put( "certificateLink", certificatelink + "&cmsLocaleCode=" + mailingRecipient.getLocale() );
  }

  private void buildQuizBadge( QuizClaim claim, QuizPromotion quizPromotion, Map dataMap )
  {
    if ( quizPromotion.isDIYQuizPromotion() && claim.getQuiz() != null )
    {
      DIYQuiz diyQuiz = (DIYQuiz)claim.getQuiz();
      if ( diyQuiz.getBadgeRule() != null && diyQuiz.getBadgeRule().getBadgeDescriptionTextFromCM() != null )
      {
        dataMap.put( "showBadges", "TRUE" );
        String badgeImageUrl = "";
        List earnedNotEarnedImageList = getGamificationService().getEarnedNotEarnedImageList( diyQuiz.getBadgeRule().getBadgeLibraryCMKey() );
        Iterator itr = earnedNotEarnedImageList.iterator();
        String siteUrlPrefix = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
        while ( itr.hasNext() )
        {
          BadgeLibrary badgeLib = (BadgeLibrary)itr.next();
          badgeImageUrl = siteUrlPrefix + badgeLib.getEarnedImageSmall();
        }
        StringBuilder paxBadgesEarnedStr = new StringBuilder();
        paxBadgesEarnedStr.append( "<br />" );
        paxBadgesEarnedStr.append( "<table>" );
        paxBadgesEarnedStr.append( "<tr align='center'><td>" );
        paxBadgesEarnedStr.append( "<img src='" + badgeImageUrl + "'/>" );
        paxBadgesEarnedStr.append( "</td></tr><tr align='center'><td>" );
        paxBadgesEarnedStr.append( diyQuiz.getBadgeRule().getBadgeNameTextFromCM() + "</td></tr></table><br />" );
        dataMap.put( "badgesEarned", paxBadgesEarnedStr.toString() );
        dataMap.put( "badgesEarnedPlain", HtmlUtils.removeFormatting( paxBadgesEarnedStr.toString() ) );
      }
    }
    else
    {
      List<ParticipantBadge> paxBadges = getGamificationService().getBadgeByParticipantEarnedForPromotion( quizPromotion.getId(), claim.getSubmitter().getId() );
      if ( paxBadges != null && paxBadges.size() > 0 )
      {
        dataMap.put( "showBadges", "TRUE" );
        Map paxBadgeMap = new HashMap();
        paxBadgeMap = buildBadgeHtmlString( paxBadges );
        String paxBadgeEarnedString = "";
        String paxBadgeProgressString = "";
        if ( paxBadgeMap.get( "paxBadgeEarnedString" ) != null )
        {
          paxBadgeEarnedString = paxBadgeMap.get( "paxBadgeEarnedString" ).toString();
        }
        if ( !StringUtils.isEmpty( paxBadgeEarnedString ) )
        {
          String paxBadgeEarnedNoHtml = "";
          paxBadgeEarnedNoHtml = HtmlUtils.removeFormatting( paxBadgeEarnedString );
          if ( !StringUtils.isEmpty( paxBadgeEarnedNoHtml.trim() ) )
          {
            dataMap.put( "showBadges", "TRUE" );
            dataMap.put( "badgesEarned", paxBadgeEarnedString );
            dataMap.put( "badgesEarnedPlain", HtmlUtils.removeFormatting( paxBadgeEarnedString ) );
          }
        }
      }
    }
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.email.MailingService#buildQuizMailing(com.biperf.core.domain.claim.QuizClaim)
   * @param claim
   * @return Mailing
   */
  @Override
  public Mailing buildQuizMailing( QuizClaim claim, boolean isCalculationSuccessful, Long depositedPoints )
  {
    Mailing mailing = buildEmails( MailingType.PROMOTION, getSystemIncentiveEmailAddress(), messageService.getMessageByCMAssetCode( MessageService.PASSED_QUIZ_MESSAGE_CM_ASSET_CODE ), false );
    Set mailingRecipients = buildMailingRecipientsForQuizEmail( claim, isCalculationSuccessful, depositedPoints );
    mailing.addMailingRecipients( mailingRecipients );
    return mailing;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.email.MailingService#buildRecognitionMailing(com.biperf.core.domain.claim.RecognitionClaim,
   *      com.biperf.core.domain.participant.Participant)
   * @param claim
   * @param recipient
   * @return Mailing
   */
  @Override
  public Mailing buildRecognitionMailing( RecognitionClaim claim, Participant recipient )
  {
    boolean isGiftCodeGenerated = true;
    RecognitionPromotion recogPromo = (RecognitionPromotion)claim.getPromotion();
    if ( !claim.isAddPointsClaim() )
    {
      if ( !recogPromo.isNoNotification() )
      {
        Message message = null;

        Promotion promotion = claim.getPromotion();
        if ( promotion.getPromotionNotifications().size() > 0 )
        {
          Iterator notificationsIter = promotion.getPromotionNotifications().iterator();
          while ( notificationsIter.hasNext() )
          {
            PromotionNotification notification = (PromotionNotification)notificationsIter.next();
            if ( notification.isPromotionNotificationType() )
            {
              PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notification;
              long messageId = promotionNotificationType.getNotificationMessageId();

              String notificationTypeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();

              if ( messageId > 0 && notificationTypeCode.equals( PromotionEmailNotificationType.RECOGNITION_RECEIVED ) )
              {
                message = messageService.getMessageById( messageId );
                break;
              }
            }
          }
        }
        Mailing mailing = buildEmails( MailingType.PROMOTION, getSystemIncentiveEmailAddress(), message, false );
        Set recipients = buildMailingRecipientsForRecognitionEmail( claim, recipient, null );
        mailing.addMailingRecipients( recipients );
        return mailing;
      }
    }
    return null;
  }

  @Override
  public Mailing buildCelebrationManagersMailing( CelebrationManagerMessage celebrationManagerMessage, PurlRecipient purlRecipient )
  {
    Mailing mailing = null;
    RecognitionPromotion promotion = celebrationManagerMessage.getPromotion();
    if ( promotion.getPromotionNotifications().size() > 0 )
    {
      Iterator notificationsIter = promotion.getPromotionNotifications().iterator();
      while ( notificationsIter.hasNext() )
      {
        PromotionNotification notification = (PromotionNotification)notificationsIter.next();
        if ( notification.isPromotionNotificationType() )
        {
          PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notification;
          long messageId = promotionNotificationType.getNotificationMessageId();
          // Process only when a notification has been set up on the promotion

          String notificationTypeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();

          if ( messageId > 0 && notificationTypeCode.equals( PromotionEmailNotificationType.CELEBRATION_MANAGER_NOTIFICATION ) )
          {
            // Send Manager notification
            mailing = buildCelebrationsManagerNotificationMailing( messageId, celebrationManagerMessage, purlRecipient );
          }
        }
      }
    }
    return mailing;
  }

  private Mailing buildCelebrationsManagerNotificationMailing( long messageId, CelebrationManagerMessage celebrationManagerMessage, PurlRecipient purlRecipient )
  {
    Set mailingRecipients = buildMailingRecipientsForCelebrationManagerEmail( celebrationManagerMessage, purlRecipient );
    if ( mailingRecipients.size() == 0 )
    {
      return null;
    }
    Mailing mailing = buildEmails( MailingType.PROMOTION, getSystemIncentiveEmailAddress(), messageService.getMessageById( messageId ), false );
    mailing.addMailingRecipients( mailingRecipients );
    return mailing;
  }

  private Set buildMailingRecipientsForCelebrationManagerEmail( CelebrationManagerMessage celebrationManagerMessage, PurlRecipient purlRecipient )
  {
    Set mailingRecipients = new HashSet();

    Set recipients = new HashSet();
    User recipientManager = celebrationManagerMessage.getManager();
    if ( recipientManager != null )
    {
      recipients.add( recipientManager );
    }
    User managerAbove = celebrationManagerMessage.getManagerAbove();
    if ( managerAbove != null )
    {
      recipients.add( managerAbove );
    }

    for ( Iterator claimRecipientIter = recipients.iterator(); claimRecipientIter.hasNext(); )
    {
      User manager = (User)claimRecipientIter.next();
      MailingRecipient mailingRecipient = buildCelebrationManagerMailingRecipient( manager, celebrationManagerMessage, purlRecipient );
      mailingRecipients.add( mailingRecipient );
    }

    return mailingRecipients;
  }

  private MailingRecipient buildCelebrationManagerMailingRecipient( User sendToUser, CelebrationManagerMessage celebrationManagerMessage, PurlRecipient purlRecipient )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();

    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setUser( sendToUser );
    String localeCode = null;
    if ( sendToUser != null && sendToUser.getLanguageType() != null )
    {
      localeCode = sendToUser.getLanguageType().getCode();
    }
    else
    {
      localeCode = systemVariableService.getDefaultLanguage().getStringVal();
    }
    mailingRecipient.setLocale( localeCode );

    Map dataMap = new HashMap();
    Participant recipient = celebrationManagerMessage.getRecipient();
    RecognitionPromotion promotion = celebrationManagerMessage.getPromotion();

    Locale locale = CmsUtil.getLocale( mailingRecipient.getLocale() );
    ContentReader contentReader = ContentReaderManager.getContentReader();
    Content content = (Content)contentReader.getContent( "recognition.detail", locale );
    String subjectLine = StringEscapeUtils.unescapeHtml4( (String)content.getContentDataMap().get( "CELEBRATION_MANAGER_MAIL_SUBJECT" ) ) + " " + recipient.getFirstName() + " "
        + recipient.getLastName();
    dataMap.put( "subject", subjectLine );

    if ( recipient != null )
    {
      dataMap.put( "recipientFirstName", recipient.getFirstName() );
      dataMap.put( "recipientLastName", recipient.getLastName() );
    }

    if ( promotion.isIncludePurl() && purlRecipient != null )
    {
      Date awardDate = purlRecipient.getAwardDate();
      dataMap.put( "awardDate", com.biperf.core.utils.DateUtils.toDisplayString( awardDate, LocaleUtils.getLocale( localeCode ) ) );

      if ( purlRecipient.getCustomElements() != null && !purlRecipient.getCustomElements().isEmpty() )
      {
        for ( int i = 0; i < purlRecipient.getCustomElements().size(); i++ )
        {
          dataMap.put( "formElement" + ( i + 1 ), purlRecipient.getCustomElement( i ).getDisplayValue() );
        }
      }

      if ( promotion.isServiceAnniversary() )
      {
        if ( promotion.getAnniversaryInYears() )
        {
          dataMap.put( "anniversaryYear", purlRecipient.getAnniversaryNumberOfYears() != null ? purlRecipient.getAnniversaryNumberOfYears().toString() : null );
        }
        else
        {
          dataMap.put( "anniversaryDay", purlRecipient.getAnniversaryNumberOfDays() != null ? purlRecipient.getAnniversaryNumberOfDays().toString() : null );
        }
      }
    }
    else
    {
      dataMap.put( "awardDate",
                   com.biperf.core.utils.DateUtils.toDisplayString( getCelebrationDeliveryDate( celebrationManagerMessage.getMsgCollectExpireDate() ), LocaleUtils.getLocale( localeCode ) ) );
      if ( promotion.isServiceAnniversary() )
      {
        if ( promotion.getAnniversaryInYears() )
        {
          dataMap.put( "anniversaryYear", celebrationManagerMessage.getAnniversaryNumberOfYears().toString() );
        }
        else
        {
          dataMap.put( "anniversaryDay", celebrationManagerMessage.getAnniversaryNumberOfDays().toString() );
        }
      }
    }
    if ( sendToUser != null )
    {
      dataMap.put( "managerFirstName", sendToUser.getFirstName() );
      dataMap.put( "managerLastName", sendToUser.getLastName() );
    }

    if ( promotion != null )
    {
      if ( promotion.getPromoNameAssetCode() != null )
      {
        String promotionName = cmAssetService.getString( promotion.getPromoNameAssetCode(), Promotion.PROMOTION_NAME_KEY_PREFIX, locale, true );
        dataMap.put( "promotionName", StringEscapeUtils.unescapeHtml4( promotionName ) );
      }
    }
    dataMap.put( "programWebsiteManagerMessage", getCelebrationManagerMessagePageUrl( celebrationManagerMessage.getId() ) );
    dataMap.put( "managerContributionEndDate", com.biperf.core.utils.DateUtils.toDisplayString( celebrationManagerMessage.getMsgCollectExpireDate(), LocaleUtils.getLocale( localeCode ) ) );
    dataMap.put( "companyName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );

    mailingRecipient.addMailingRecipientDataFromMap( dataMap );

    return mailingRecipient;
  }

  private String getCelebrationManagerMessagePageUrl( Long managerMessageId )
  {
    Map<String, String> paramMap = new HashMap<String, String>();
    paramMap.put( "managerMessageId", managerMessageId.toString() );
    String celebrationPageUrl = ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                                      "/celebration/managerMessageCollect.do?method=display",
                                                                      paramMap );
    return celebrationPageUrl;
  }

  private Date getCelebrationDeliveryDate( Date msgCollectExpireDate )
  {
    Date deliveryDate = msgCollectExpireDate != null ? msgCollectExpireDate : new Date();
    Calendar cal = Calendar.getInstance();
    cal.setTime( deliveryDate );
    cal.add( Calendar.DATE, 1 );
    deliveryDate = cal.getTime();
    return deliveryDate;
  }

  @Override
  public Mailing buildPurlManagerNotificationMailing( long messageId, PurlRecipient purlRecipient )
  {
    MailingRecipient recipient = buildMailingRecipientForPurlManagerInvitation( purlRecipient );
    if ( null == recipient )
    {
      return null;
    }

    Mailing mailing = buildEmails( MailingType.PROMOTION, getSystemIncentiveEmailAddress(), messageService.getMessageById( messageId ), false );
    mailing.addMailingRecipient( recipient );

    return mailing;
  }

  private MailingRecipient buildMailingRecipientForSSIProgressLoadNotification( Participant participant )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    Map dataMap = new HashMap();

    mailingRecipient.addMailingRecipientDataFromMap( dataMap );

    return mailingRecipient;
  }




  public Mailing buildPurlContributorNotificationMailing( long messageId, PurlContributor purlContributor, Long nonContributorUserId, boolean isAutoInvite )
  {
	  /* custom for wip #46293 */
    Mailing mailing = new Mailing();

    Message message = messageService.getMessageById( messageId );

    mailing.setGuid( GuidUtils.generateGuid() );
    mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
    mailing.setMessage( message );
    mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
    mailing.setSender( getSystemIncentiveEmailAddress() );
    
    
    // custom for wip # 46293
    if ( isAutoInvite )
    {
    MailingRecipient recipient = buildMailingRecipientForPurlContributorInvitation( purlContributor, nonContributorUserId, true );
    mailing.addMailingRecipient( recipient );
    }
    else
    {
    	 MailingRecipient recipient = buildMailingRecipientForCokeCustomPurlContributorInvitation( purlContributor, nonContributorUserId, false );
    	 mailing.addMailingRecipient( recipient );	
    }
    // custom for wip # 46293
    return mailing;
  }
  

  @Override
  public Mailing buildPurlContributorNotificationMailing( long messageId, PurlContributor purlContributor, Long nonContributorUserId )
  {
    Mailing mailing = buildEmails( MailingType.PROMOTION, getSystemIncentiveEmailAddress(), messageService.getMessageById( messageId ), false );


    mailing.addMailingRecipient( buildMailingRecipientForPurlContributorInvitation( purlContributor, nonContributorUserId, true ) );


    return mailing;
  }

  @Override
  public Mailing buildPurlRecipientInvitationMailing( long messageId, PurlRecipient purlRecipient )
  {
    Mailing mailing = buildEmails( MailingType.PROMOTION, getSystemIncentiveEmailAddress(), messageService.getMessageById( messageId ), false );
    mailing.addMailingRecipient( buildMailingRecipientForPurlRecipientInvitation( purlRecipient ) );
    return mailing;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.email.MailingService#buildMerchOrderGiftCodeRefundMailing(MerchOrder)
   * @param merchOrder
   * @return Mailing
   */
  @Override
  public Mailing buildMerchOrderGiftCodeRefundMailing( MerchOrder merchOrder, String emailAddress, String messageText )
  {
    Mailing mailing = buildEmails( MailingType.PROMOTION,
                                   getSystemIncentiveEmailAddress(),
                                   messageService.getMessageByCMAssetCode( MessageService.MERCH_ORDER_GIFT_CODE_REFUND_MESSAGE_CM_ASSET_CODE ),
                                   false );
    mailing.addMailingRecipient( buildMailingRecipientForMerchOrderGiftCodeRefundEmail( merchOrder, emailAddress, messageText ) );
    return mailing;
  }

  // get nomination claim comment from claim table or from claim element if nom promo use is why in
  // form library
  private String getNominationClaimComment( NominationClaim nomClaim )
  {
    String comment = null;
    NominationPromotion nomPromo = (NominationPromotion)nomClaim.getPromotion();
    if ( !nomPromo.isWhyNomination() )
    {
      for ( Iterator iter = nomClaim.getClaimElements().iterator(); iter.hasNext(); )
      {
        ClaimElement claimElement = (ClaimElement)iter.next();
        ClaimFormStepElement claimFormStepElement = claimElement.getClaimFormStepElement();
        if ( claimFormStepElement.isWhyField() )
        {
          comment = claimElement.getValue();
        }
      }
    }
    else
    {
      comment = nomClaim.getSubmitterComments();
    }

    return comment;
  }

  // Bug # 37498 - added userId
  /**
   * @param claim
   * @return List
   */
  @Override
  public List buildNominationMailing( NominationClaim claim, Long userId )
  {
    ArrayList mailings = new ArrayList();

    NominationPromotion promotion = (NominationPromotion)claim.getPromotion();
    int  currentApprovalLevel = claim.getApprovableItemApproversSize();
    NominationEvaluationType evaluationType = promotion.getEvaluationType(); // independent or
    // cumulative
	 
    for ( Iterator iter = promotion.getPromotionNotifications().iterator(); iter.hasNext(); )
    {
      PromotionNotification notification = (PromotionNotification)iter.next();
      PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notification;

      // If a Notification Message has been selected on the promotion setup, build it
      if ( notification.getNotificationMessageId() > 0 )
      {
        long messageId = promotionNotificationType.getNotificationMessageId();
        String typeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();

     // build winner notification to winning nominee
        if ( typeCode.equals( ClaimFormStepEmailNotificationType.TO_NOMINEE_WHEN_WINNER ) )
        {
          /* Bug # 36806 start */
          ApprovableItem approvableItem = (ApprovableItem)claim.getApprovableItems().iterator().next();
          if ( approvableItem.getApprovalStatusType().getCode().equals( ApprovalStatusType.WINNER ) )
          {
            /* Bug # 36806 end */
            Mailing mailing = new Mailing();
            Message message = messageService.getMessageByCMAssetCode( MessageService.WINNING_NOMINEE_MESSAGE_CM_ASSET_CODE );
            mailing.setGuid( GuidUtils.generateGuid() );
            mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
            mailing.setMessage( message );
            mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
            mailing.setSender( getSystemIncentiveEmailAddress() );
            Set winningNomineeRecipients = new HashSet();
            // Bug # 37498 - added userId
            winningNomineeRecipients = buildMailingRecipientsForWinningNominee( claim, evaluationType, userId );
            mailing.addMailingRecipients( winningNomineeRecipients );
            mailings.add( mailing );
          } // Bug # 36806
        }
        if ( typeCode.equals( ClaimFormStepEmailNotificationType.CLAIM_SUBMITTED ) || typeCode.equals( ClaimFormStepEmailNotificationType.RECOGNITION_SUBMITTED )
                || typeCode.equals( PromotionEmailNotificationType.TO_APPROVER_WHEN_CLAIM_SUBMITTED ) && currentApprovalLevel == 0) 
            {        	 
        	  Mailing mailing = new Mailing();
        	  Message message = messageService.getMessageByCMAssetCode( MessageService.NOMINATION_NOMINEE_NOTIFICATION );
              mailing.setGuid( GuidUtils.generateGuid() );
              mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
              mailing.setMessage( message );
              mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
              mailing.setSender( getSystemIncentiveEmailAddress() );
              Set<MailingRecipient> approverNomineeRecipients = new HashSet<MailingRecipient>(); 
              approverNomineeRecipients = buildMailingRecipientsForApproverRecipients( claim, evaluationType, userId ); 
              mailing.addMailingRecipients( approverNomineeRecipients );
              mailings.add( mailing ); 
            }
        // build winner notification to nominator
        if ( typeCode.equals( ClaimFormStepEmailNotificationType.TO_NOMINATOR_WHEN_WINNER ) )
        {
          /* Bug # 36806 start */
          ApprovableItem approvableItem = (ApprovableItem)claim.getApprovableItems().iterator().next();
          if ( approvableItem.getApprovalStatusType().getCode().equals( ApprovalStatusType.WINNER ) )
          {
            /* Bug # 36806 end */
            Mailing mailing = new Mailing();
            Message message = messageService.getMessageByCMAssetCode( MessageService.WINNING_NOMINEES_NOMINATOR_MESSAGE_CM_ASSET_CODE );
            mailing.setGuid( GuidUtils.generateGuid() );
            mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
            mailing.setMessage( message );
            mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
            mailing.setSender( getSystemIncentiveEmailAddress() );
            Set winnersNominatorRecipients = new HashSet();
            winnersNominatorRecipients = buildMailingRecipientsForWinnersNominator( claim, evaluationType, userId );
            mailing.addMailingRecipients( winnersNominatorRecipients );
            mailings.add( mailing );
          } // Bug # 36806
        }
     // build winner notification to winner's manager
        if ( typeCode.equals( ClaimFormStepEmailNotificationType.TO_NOMINEES_MANAGER_WHEN_WINNER ) )
        {
          /* Bug # 36806 start */
          ApprovableItem approvableItem = (ApprovableItem)claim.getApprovableItems().iterator().next();
          if ( approvableItem.getApprovalStatusType().getCode().equals( ApprovalStatusType.WINNER ) )
          {
            /* Bug # 36806 end */
            Mailing mailing = new Mailing();
            Message message = messageService.getMessageByCMAssetCode( MessageService.WINNING_NOMINEE_MANAGER_MESSAGE_CM_ASSET_CODE );
            mailing.setGuid( GuidUtils.generateGuid() );
            mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
            mailing.setMessage( message );
            mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
            mailing.setSender( getSystemIncentiveEmailAddress() );
            Set winnersManagerRecipients = new HashSet();
            // Bug # 37498 - added userId
            winnersManagerRecipients = buildMailingRecipientsForWinnersManager( claim, evaluationType, userId );
            mailing.addMailingRecipients( winnersManagerRecipients );
            mailings.add( mailing );
          }
        }
      }
    }
    return mailings;
  }

  /**
   * Build Winner Notification to the Winning Nominee.
   * 
   * @param evaluationType - independently or cumulatively
   */
  // Bug # 37498 - added userId

  public Set<MailingRecipient> buildMailingRecipientsForWinningNominee( NominationClaim claim, NominationEvaluationType evaluationType, Long userId )
  {
    Set<MailingRecipient> mailingRecipients = new HashSet<>();

    NominationPromotion nominationPromotion = (NominationPromotion)claim.getPromotion();

    // Send to intended recipients - if given a userId, only send to that person
    Iterator<ClaimRecipient> claimRecipientIterator = claim.getClaimRecipients().stream().filter( ( recipient ) -> userId == null || recipient.getRecipient().getId().equals( userId ) ).iterator();
    while ( claimRecipientIterator.hasNext() )
    {
      ClaimRecipient claimRecipient = claimRecipientIterator.next();
      MailingRecipient mailingRecipient = new MailingRecipient();
      Map<String, String> dataMap = new HashMap<>();
      Participant nominee = claimRecipient.getRecipient();
      NominationPromotionLevel currentLevel = getWinningLevel( nominationPromotion, claim );
      String recipientLanguageType = systemVariableService.getDefaultLanguage().getStringVal();
      if ( null != nominee && nominee.getLanguageType() != null )
      {
        recipientLanguageType = nominee.getLanguageType().getCode();
      }
      Locale recipientLocale = CmsUtil.getLocale( recipientLanguageType );
      
      mailingRecipient.setUser( nominee );
      mailingRecipient.setLocale( recipientLanguageType );
      mailingRecipient.setGuid( GuidUtils.generateGuid() );

      String nomineeFirstName =  null != nominee ? nominee.getFirstName():"";
      dataMap.put( "nomineeFirstName", nomineeFirstName );
      dataMap.put( "programName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
      dataMap.put( "siteUrl", systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );

      boolean isTeam = claim.hasTeamName();
      boolean isTimePeriod = claim.getTimPeriod() != null;
      boolean isEachLevel = nominationPromotion.getPayoutLevel().equals( "eachLevel" );
      boolean isFinalApprover = ClaimApproveUtils.isFinalApprover(claim);
      
      if(isFinalApprover){
    	  dataMap.put( "isFinalApprover", "TRUE" );
      }
      else if ( isTeam && !isTimePeriod && isEachLevel )
      {
        dataMap.put( "teamNTimeEach", "TRUE" );
      }
      else if ( !isTeam && !isTimePeriod && isEachLevel )
      {
        dataMap.put( "nTeamNTimeEach", "TRUE" );
      }
      else if ( isTeam && isTimePeriod )
      {
        dataMap.put( "teamTime", "TRUE" );
      }
      else if ( !isTeam && isTimePeriod )
      {
        dataMap.put( "nTeamTime", "TRUE" );
      }
      else if ( isTeam && !isTimePeriod )
      {
        dataMap.put( "teamNTime", "TRUE" );
      }
      else if ( !isTeam && !isTimePeriod )
      {
        dataMap.put( "nTeamNTime", "TRUE" );
      }

      String promotionName = getPromotionService().getPromotionNameByLocale( nominationPromotion.getPromoNameAssetCode(), recipientLanguageType );
      promotionName = StringUtils.isBlank( promotionName ) ? nominationPromotion.getPromotionName() : promotionName;
      dataMap.put( "promotionName", promotionName );
      if ( isTeam )
      {
        dataMap.put( "teamName", claim.getTeamName() );
      }
      if ( isEachLevel )
      {
        String levelName = currentLevel.getLevelLabel();
        if ( currentLevel.getLevelLabelAssetCode() != null )
        {
          levelName = cmAssetService.getString( currentLevel.getLevelLabelAssetCode(), Promotion.PROMOTION_LEVEL_LABEL_NAME_KEY_PREFIX, recipientLocale, true );
        }
        dataMap.put( "levelName", levelName );
      }

      // Begin awards
      if ( nominationPromotion.isAwardActive() && isFinalApprover)
      {
        dataMap.put( "showAward", "TRUE" );

        if ( currentLevel.getAwardPayoutType().getCode().equals( PromotionAwardsType.POINTS ) )
        {
          dataMap.put( "points", "TRUE" );
          dataMap.put( "mediaType", currentLevel.getAwardPayoutType().getAbbr() );

          Long awardQuantity = null;
          if ( evaluationType.equals( NominationEvaluationType.lookup( NominationEvaluationType.CUMULATIVE ) ) )
          {
            awardQuantity = claim.getClaimGroup().getAwardQuantity();
          }
          else
          {
            awardQuantity = claimRecipient.getAwardQuantity();
          }

          String formattedAwardQuantity = "0";
          if ( awardQuantity != null && awardQuantity.longValue() > 1 )
          {
            formattedAwardQuantity = NumberFormatUtil.getUserLocaleBasedNumberFormat( awardQuantity, recipientLocale );
          }
          else
          {
            formattedAwardQuantity = String.valueOf( awardQuantity );
          }

          dataMap.put( "awardAmount", formattedAwardQuantity );
        }
        else if ( currentLevel.getAwardPayoutType().getCode().equals( PromotionAwardsType.CASH ) )
        {
          dataMap.put( "cash", "TRUE" );

          BigDecimal awardQuantity = null;
          if ( evaluationType.equals( NominationEvaluationType.lookup( NominationEvaluationType.CUMULATIVE ) ) )
          {
            awardQuantity = claim.getClaimGroup().getCashAwardQuantity();
          }
          else
          {
            awardQuantity = claimRecipient.getCashAwardQuantity();
          }

          String recipientCurrencyCode =  null != nominee ? nominee.getPrimaryAddress().getAddress().getCountry().getCurrencyCode():"USD";
          if ( recipientCurrencyCode != null )
          {
            BigDecimal awardAmountConverted = getCashCurrencyService().convertCurrency( "USD", recipientCurrencyCode, awardQuantity, null );
            String formattedAwardQuantityConverted = NumberFormatUtil.getLocaleBasedBigDecimalFormat( awardAmountConverted, 2, recipientLocale );
            dataMap.put( "cashAwardAmount", formattedAwardQuantityConverted );
            dataMap.put( "currencyType", recipientCurrencyCode );
          }
          else
          {
            String formattedAwardQuantityConverted = NumberFormatUtil.getLocaleBasedBigDecimalFormat( awardQuantity, 2, recipientLocale );
            dataMap.put( "cashAwardAmount", formattedAwardQuantityConverted );
            dataMap.put( "currencyType", "USD" );
          }
        }
        else if ( currentLevel.getAwardPayoutType().getCode().equals( PromotionAwardsType.OTHER ) )
        {
          dataMap.put( "otherAward", "TRUE" );
          String otherAwardDescription = currentLevel.getPayoutDescription();
          if ( currentLevel.getPayoutDescriptionAssetCode() != null )
          {
            otherAwardDescription = cmAssetService.getString( currentLevel.getPayoutDescriptionAssetCode(), Promotion.PAYOUT_DESCRIPTION_KEY_PREFIX, recipientLocale, true );
          }
          dataMap.put( "otherAwardDescription", otherAwardDescription );
        }
      }
      // End awards

      // Begin behaviors
      if ( claim.getNominationClaimBehaviors() != null && !claim.getNominationClaimBehaviors().isEmpty() )
      {
        buildBehaviorsForNominationNotification( claim, dataMap, recipientLocale );
      }
      // End behaviors

      dataMap.put( "message", getNominationClaimComment( claim ) );
      dataMap.put( "nominatorFirstName", claim.getSubmitter().getFirstName() );
      dataMap.put( "nominatorLastName", claim.getSubmitter().getLastName() );

      // Begin card
      if ( StringUtils.isNotBlank( claim.getOwnCardName() ) )
      {
        dataMap.put( "showECard", "TRUE" );
        dataMap.put( "ecardTypeImage", "TRUE" );
        dataMap.put( "eCardImg", claim.getOwnCardName() );
      }
      else if ( claim.getCard() != null )
      {
        dataMap.put( "showECard", "TRUE" );
        dataMap.put( "ecardTypeImage", "TRUE" );
        String eCardImg = claim.getCard().getLargeImageNameLocale();
        dataMap.put( "eCardImg", eCardImg );
      }
      else if ( StringUtils.isNotBlank( claim.getCardVideoUrl() ) )
      {
        dataMap.put( "showECard", "TRUE" );
        dataMap.put( "ecardTypeVideo", "TRUE" );
        // MTC- To be changed
        if ( claim.getCardVideoUrl().contains( ActionConstants.REQUEST_ID ) )
        {

          MTCVideo mtcVideo = getMtcVideoService().getMTCVideoByRequestId( claim.getRequestId( claim.getCardVideoUrl() ) );
          String eCardVideoLink = null;
          if ( Objects.nonNull( mtcVideo ) )
          {
            eCardVideoLink = mtcVideo.getOriginalFormat().equals( "mp4" ) ? mtcVideo.getMp4Url() : mtcVideo.getWebmUrl();
          }
          else
          {
            eCardVideoLink = claim.getActualCardUrl( claim.getCardVideoUrl() );
          }
          dataMap.put( "eCardVideoLink", eCardVideoLink );
        }
        else
        {
          dataMap.put( "eCardVideoLink", claim.getCardVideoUrl() );
        }
        // Generic play button thumbnail
        String eCardVideoImg = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + DEFAULT_VIDEO_IMAGE;
        dataMap.put( "eCardVideoImg", eCardVideoImg );
      }
      // End card

      // Begin badges
      if(null != nominee){
    	  buildBadgesForNominationPromotionNotification( dataMap, claim, nominee.getId(), true );
      }
      // End badges

      // Begin certificate
      if ( claim.getCertificateId() != null )
      {
        Map<String, Object> clientStateParameterMap = new HashMap<>();
        clientStateParameterMap.put( "userId", nominee.getId() );
        clientStateParameterMap.put( "claimId", claim.getId() );
        clientStateParameterMap.put( "promotionId", nominationPromotion.getId() );
        String certificatelink = ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                                       "/claim/displayCertificate.do?method=showCertificateNominationDetail",
                                                                       clientStateParameterMap );
        dataMap.put( "showCertificate", "TRUE" );
        dataMap.put( "certificateLink", certificatelink + "&cmsLocaleCode=" + mailingRecipient.getLocale() );
      }
      // End certificate

      // If sweepstakes is active and the Nominee is eligible.
      if ( nominationPromotion.isSweepstakesActive() && ( nominationPromotion.getSweepstakesWinnerEligibilityType().getCode().equals( SweepstakesWinnerEligibilityType.NOMINEES_ONLY_CODE )
          || nominationPromotion.getSweepstakesWinnerEligibilityType().getCode().equals( SweepstakesWinnerEligibilityType.NOMINATORS_AND_NOMINEES_COMBINED_CODE )
          || nominationPromotion.getSweepstakesWinnerEligibilityType().getCode().equals( SweepstakesWinnerEligibilityType.NOMINATORS_AND_NOMINEES_SEPARATE_CODE ) ) )
      {
        dataMap.put( "showSweeps", "TRUE" );
      }

      // Approver message
      ApprovableItemApprover approverItem = getApproverForLevel( claim, claimRecipient, currentLevel, evaluationType );
      if ( approverItem != null && StringUtils.isNotBlank( approverItem.getApproverComments() ) )
      {
        dataMap.put( "showApproverMessage", "TRUE" );
        User approver = approverItem.getApproverUser();
        dataMap.put( "approverFirstName", approver.getFirstName() );
        dataMap.put( "approverLastName", approver.getLastName() );
        dataMap.put( "approverMessage", approverItem.getApproverComments() );
      }
      if ( isTimePeriod && !Objects.isNull( approverItem ) )
      {
        String timePeriodNameAssetCode = getClaimService().getPromoTimePeriodNameById( approverItem.getTimePeriodId() );
        if ( !Objects.isNull( timePeriodNameAssetCode ) )
        {
          dataMap.put( "timePeriodName", cmAssetService.getString( timePeriodNameAssetCode, Promotion.PROMOTION_TIME_PERIOD_NAME_KEY_PREFIX, recipientLocale, true ) );
        }
      }
      // Client customization for WIP #56492 starts
      if ( nominationPromotion.isLevelSelectionByApprover() )
      {
        dataMap.put( "isLevelSelectionByApprover", "TRUE" );
        ApprovableItem approvableItem = (ApprovableItem)claim.getApprovableItems().iterator().next();
        dataMap.put( "levelSelect", approvableItem.getLevelSelect() );
      }

      // Account balance link
      dataMap.put( "siteLink", systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/participantProfilePage.do?fromEmail=true#tab/Statement" );

      // Reference Number
      dataMap.put( "claimNumber", claim.getClaimNumber() );  
      // Client customization for WIP #56492 ends

      mailingRecipient.addMailingRecipientDataFromMap( dataMap );
      mailingRecipients.add( mailingRecipient );
          
  } 

    return mailingRecipients;
  }

  /*// Client customization for WIP 58122
  public Set<MailingRecipient> buildMailingRecipientsForWinningNominee( NominationClaim claim, NominationEvaluationType evaluationType, Long userId )
  {
	    Set mailingRecipients = new HashSet();

	    Iterator recipientIterator = null;

	    Iterator claimRecipientIteratorForTeam = null;

	    Long awardQuantity = null;

	    if ( claim.isTeam() )
	    {
	      recipientIterator = claim.getTeamMembers().iterator(); // maybe n nominees

	      claimRecipientIteratorForTeam = claim.getClaimRecipients().iterator(); // this is to get the
	      // award qty
	    }
	    else
	    {
	      recipientIterator = claim.getClaimRecipients().iterator(); // always 1 nominee
	    }

	    // Determine award amount
	    if ( claimRecipientIteratorForTeam != null )
	    {
	      while ( claimRecipientIteratorForTeam.hasNext() )
	      {
	        ClaimRecipient claimRecipient = (ClaimRecipient)claimRecipientIteratorForTeam.next();
	        awardQuantity = claimRecipient.getAwardQuantity(); // the same award qty for each team
	        // member
	      }

	    }
	    if ( evaluationType.equals( NominationEvaluationType.lookup( NominationEvaluationType.CUMULATIVE ) ) )
	    {
	      awardQuantity = claim.getClaimGroup().getAwardQuantity(); // the cumulative award qty for
	      // the 1 nominee
	    }
	    // loops only if claim is a team-based
	    while ( recipientIterator.hasNext() )
	    {
	      User user = null;
	      String firstName = null;
	      String lastName = null;

	      if ( claim.isTeam() )
	      {
	        ProductClaimParticipant claimRecipient = (ProductClaimParticipant)recipientIterator.next();
	         Bug # 37498 - added userId 
	        if ( userId == null || claimRecipient.getParticipant().getId().equals( userId ) )
	        {
	           Bug # 37498 - added userId 
	          user = claimRecipient.getParticipant();
	          firstName = claimRecipient.getParticipant().getFirstName();
	          lastName = claimRecipient.getParticipant().getLastName();
	        } // Bug # 37498 - added userId
	      }
	      else
	      {
	        ClaimRecipient claimRecipient = (ClaimRecipient)recipientIterator.next();
	        user = claimRecipient.getRecipient();
	        firstName = claimRecipient.getRecipient().getFirstName();
	        lastName = claimRecipient.getRecipient().getLastName();
	        if ( awardQuantity == null )
	          awardQuantity = claimRecipient.getAwardQuantity();
	      }

	       Bug # 37498 - added userIdt 
	      if ( user != null )
	      {
	         Bug # 37498 - added userId 
	        MailingRecipient mailingRecipient = new MailingRecipient();

	        mailingRecipient.setUser( user );
	        if ( user.getLanguageType() != null )
	        {
	          mailingRecipient.setLocale( user.getLanguageType().getCode() );
	        }
	        else
	        {
	          mailingRecipient.setLocale( LanguageType.ENGLISH );
	        }
	        mailingRecipient.setGuid( GuidUtils.generateGuid() );

	        Map dataMap = new HashMap();

	        dataMap.put( "firstName", firstName );
	        dataMap.put( "lastName", lastName );
	        
	        Locale locale = CmsUtil.getLocale( mailingRecipient.getLocale() );
	        
	        if ( claim.getPromotion().getPromoNameAssetCode() != null )
	        {
	          String promotionName = cmAssetService.getString( claim.getPromotion().getPromoNameAssetCode(), Promotion.PROMOTION_NAME_KEY_PREFIX, locale, true );
	          dataMap.put( "programName", StringEscapeUtils.unescapeHtml4( promotionName ) );
	        }

	        if ( claim.getBehavior() != null )
	        {
	          ContentReader contentReader = ContentReaderManager.getContentReader();
	          String behaviorName = claim.getBehavior().getCode();
	          List contentList = (List)contentReader.getContent( "picklist.promo.nomination.behavior.items", locale );

	          for ( Iterator iter = contentList.iterator(); iter.hasNext(); )
	          {
	            Content content = (Content)iter.next();
	            String code = (String)content.getContentDataMap().get( PickListItem.ITEM_CODE_KEY );
	            if ( claim.getBehavior().getCode().equalsIgnoreCase( code ) )
	            {
	              String category = (String)content.getContentDataMap().get( PickListItem.ITEM_NAME_KEY );
	              dataMap.put( "category", StringEscapeUtils.unescapeHtml4( category ) );
	              break;
	            }
	          }
	        }

	        if ( claim.isTeam() )
	        {
	          dataMap.put( "team", "TRUE" );
	        }
	        else
	        {
	          dataMap.put( "individual", "TRUE" );
	        }
	        dataMap.put( "message", claim.getSubmitterComments() );

	        NominationPromotion nominationPromotion = (NominationPromotion)claim.getPromotion();

	        // Award
	        if ( nominationPromotion.isAwardActive() )
	        {
	          String tempAwardQuantity = awardQuantity.toString();
	          if ( ( awardQuantity != null ) && ( awardQuantity.longValue() > 1 ) )
	          {
	              dataMap.put( "manyAwardAmount", "TRUE" );
	              tempAwardQuantity =  NumberFormatUtil.getUserLocaleBasedNumberFormat(awardQuantity, LocaleUtils.getLocale(mailingRecipient.getLocale().toString()));
	          }
	          dataMap.put( "awardAmount", String.valueOf( tempAwardQuantity ) );
	          dataMap.put( "mediaType", nominationPromotion.getAwardType().getAbbr() );
	        }

	        // If sweepstakes is active and the Nominee is eligible.
	        if ( nominationPromotion.isSweepstakesActive()
	            && ( nominationPromotion.getSweepstakesWinnerEligibilityType().getCode().equals( SweepstakesWinnerEligibilityType.NOMINEES_ONLY_CODE )
	                || nominationPromotion.getSweepstakesWinnerEligibilityType().getCode().equals( SweepstakesWinnerEligibilityType.NOMINATORS_AND_NOMINEES_COMBINED_CODE ) || nominationPromotion
	                .getSweepstakesWinnerEligibilityType().getCode().equals( SweepstakesWinnerEligibilityType.NOMINATORS_AND_NOMINEES_SEPARATE_CODE ) ) )
	        {
	          dataMap.put( "showSweeps", "TRUE" );
	        }

	        // Certificate link
	        if ( nominationPromotion.isIncludeCertificate() )
	        {
	          dataMap.put( "showCertificate", "TRUE" );

	          Map clientStateParameterMap = new HashMap();
	          clientStateParameterMap.put( "userId", user.getId() );
	          clientStateParameterMap.put( "claimId", claim.getId() );
	          clientStateParameterMap.put( "promotionId", nominationPromotion.getId() );

	          String certificatelink = ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
	                                                                         "/claim/displayCertificate.do?method=showCertificateNominationDetail",
	                                                                         clientStateParameterMap );

	          dataMap.put( "certificateLink", certificatelink + "&cmsLocaleCode=" + mailingRecipient.getLocale() );
	          
	          String certificateCompleteLink = certificatelink + "&cmsLocaleCode=" + mailingRecipient.getLocale();
	          String encodedLink = null;
	          try
	          {
	            encodedLink = URLEncoder.encode( certificateCompleteLink, "UTF-8" );
	          }
	          catch( UnsupportedEncodingException e )
	          {
	            log.error( "<<<<ERROR>>>>>" + e );
	          }
	          String certificateShortLink = getShortUrl( encodedLink );
	          dataMap.put( "shortLink", certificateShortLink );

	        }
	        // Client customization for WIP #56492 starts
	        if ( nominationPromotion.isLevelSelectionByApprover() )
	        {
	          dataMap.put( "isLevelSelectionByApprover", "TRUE" );
	          ApprovableItem approvableItem = (ApprovableItem)claim.getApprovableItems().iterator().next();
	          dataMap.put( "levelSelect", approvableItem.getLevelSelect() );
	        }
	        // Client customization for WIP #56492 ends
	        if ( nominationPromotion.isLevelPayoutByApproverAvailable())
	        {
	          dataMap.put( "isCustomLevelPayout", "TRUE" );
	          ApprovableItem approvableItem = (ApprovableItem)claim.getApprovableItems().iterator().next();
	          dataMap.put( "levelSelect", approvableItem.getLevelSelect() );
	        }
	        // ECard
	        if ( claim.getCard() != null )
	        {
	          dataMap.put( "showECard", "TRUE" );
	          Map clientStateParameterMap = new HashMap();
	          clientStateParameterMap.put( "claimId", claim.getId() );

	          String link = ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
	                                                              "/claim/viewrecognition.do",
	                                                              clientStateParameterMap );

	          dataMap.put( "eCardLink", link + "&cmsLocaleCode=" + mailingRecipient.getLocale() );
	          
	          String cardCompleteLink = link + "&cmsLocaleCode=" + mailingRecipient.getLocale();
	          String encodedLink = null;
	          try
	          {
	            encodedLink = URLEncoder.encode( cardCompleteLink, "UTF-8" );
	          }
	          catch( UnsupportedEncodingException e )
	          {
	            log.error( "<<<<ERROR>>>>>" + e );
	          }
	          String cardShortLink = getShortUrl( encodedLink );
	          dataMap.put( "shortLink", cardShortLink);
	          
	          ECard eCard = (ECard)claim.getCard();
	          String eCardImg = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + ECard.CARDS_FOLDER + claim.getCard().getLargeImageNameLocale();
	          dataMap.put( "eCardImg", eCardImg );

	          if ( ( claim.getCard() instanceof ECard && ( (ECard)claim.getCard() ).isFlashNeeded() ) )
	          {
	            dataMap.put( "flashNeeded", "TRUE" );
	          }
	        }
	        
	        if ( claim.getOwnCardName() != null && claim.getOwnCardName().length() > 0 )
	        {
	          dataMap.put( "showECard", "TRUE" );

	          Map clientStateParameterMap = new HashMap();
	          clientStateParameterMap.put( "claimId", claim.getId() );

	          String link = ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
	                                                              "/claim/viewrecognition.do",
	                                                              clientStateParameterMap );

	          String cardCompleteLink = link + "&cmsLocaleCode=" + mailingRecipient.getLocale();
	          dataMap.put( "eCardLink", cardCompleteLink );
	          
	          dataMap.put( "eCardImg", claim.getOwnCardName() );
	          
	          String encodedLink = null;
	          try
	          {
	            encodedLink = URLEncoder.encode( cardCompleteLink, "UTF-8" );
	          }
	          catch( UnsupportedEncodingException e )
	          {
	            log.error( "<<<<ERROR>>>>>" + e );
	          }
	          String cardShortLink = getShortUrl( encodedLink );
	          dataMap.put( "shortLink", cardShortLink );
	        }
	        
	        if( !nominationPromotion.isIncludeCertificate() && claim.getCard() == null && claim.getOwnCardName() == null )
	        {
	          Map parameterMap = new HashMap();
	          parameterMap.put( "claimId", claim.getId() );
	          String linkForTextMesg = ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
	                                                              "/claim/viewrecognition.do",
	                                                              parameterMap );
	          String completeLink = linkForTextMesg + "&cmsLocaleCode=" + mailingRecipient.getLocale();
	          String encodedLink = null;
	          try
	          {
	            encodedLink = URLEncoder.encode( completeLink, "UTF-8" );
	          }
	          catch( UnsupportedEncodingException e )
	          {
	            log.error( "<<<<ERROR>>>>>" + e );
	          }
	          String shortLink = getShortUrl( encodedLink );
	          dataMap.put( "shortLink", shortLink );
	        }
	        
	        // Account balance link
	        dataMap.put( "siteLink", systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/participantProfilePage.do?fromEmail=true#tab/Statement" );

	        // Reference Number
	        dataMap.put( "claimNumber", claim.getClaimNumber() );

	        mailingRecipient.addMailingRecipientDataFromMap( dataMap );

	        mailingRecipients.add( mailingRecipient );
	      } // Bug # 37498 - added userId
	    }

	    return mailingRecipients;
	  }
*/
  /**
   * Build Winner Notification to the nominators of winning nominees.
   * 
   * @param evaluationType - independently or cumulatively
   */
  // Bug # 37498 - added userId
  private Set<MailingRecipient> buildMailingRecipientsForApproverRecipients( NominationClaim claim, NominationEvaluationType evaluationType, Long userId )
  { 
	  	Long pendingNominationCount = new Long("0");
	    Set<MailingRecipient> mailingRecipients = new HashSet<>();
	    Iterator<ClaimRecipient> recipientIterator = null;
	    recipientIterator = claim.getClaimRecipients().iterator();

	    NominationPromotion nominationPromotion = (NominationPromotion)claim.getPromotion();
	     
	         
	    while ( recipientIterator.hasNext() )
	    {
	      User user = null;
	      String firstName = null;
	      String lastName = null;
	      Long paxId = null;

	      ClaimRecipient claimRecipient = (ClaimRecipient)recipientIterator.next(); 
	      MailingRecipient mailingRecipient = new MailingRecipient();
	   
    // Send to intended recipients - if given a userId, only send to that person
	   /* Iterator<ClaimRecipient> claimRecipientIterator = claim.getClaimRecipients().stream().filter( ( recipient ) -> userId == null || recipient.getRecipient().getId().equals( userId ) ).iterator();
    while ( claimRecipientIterator.hasNext() )
   {
      ClaimRecipient claimRecipient = claimRecipientIterator.next();
      MailingRecipient mailingRecipient = new MailingRecipient(); */
      Map<String, String> dataMap = new HashMap<>();
      Participant nominee = claimRecipient.getRecipient();
      Participant nominator = claim.getSubmitter();
      NominationPromotionLevel currentLevel = getWinningLevel( nominationPromotion, claim );
      String recipientLanguageType = systemVariableService.getDefaultLanguage().getStringVal();
      
      if ( nominee.getLanguageType() != null )
      {
        recipientLanguageType = nominee.getLanguageType().getCode();
      }
      Locale recipientLocale = CmsUtil.getLocale( recipientLanguageType );
      mailingRecipient.setId(nominee.getId());
      mailingRecipient.setUser( nominee );
      mailingRecipient.setLocale( recipientLanguageType );
      mailingRecipient.setGuid( GuidUtils.generateGuid() );

      dataMap.put( "nomineeFirstName", nominee.getFirstName() );
      dataMap.put( "firstName", nominee.getFirstName() );
      dataMap.put( "nomineeLastName", nominee.getLastName() );      
      dataMap.put( "nominator", nominator.getNameLFMWithComma());
      dataMap.put( "programName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
      dataMap.put( "siteUrl", systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );
      dataMap.put( "approvalPageLink", systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );
	  
      dataMap.put( "nominationSubmissionDate", claim.getDisplaySubmissionDate());
  	
      pendingNominationCount = getParticipantService().getPendingNominationCountForApprover( nominee.getId() );
      pendingNominationCount = pendingNominationCount + 1;
 
      boolean isTeam = claim.hasTeamName();
      boolean isTimePeriod = claim.getTimPeriod() != null;
      boolean isEachLevel = nominationPromotion.getPayoutLevel().equals( "eachLevel" );

      if ( isTeam && !isTimePeriod && isEachLevel )
      {
        dataMap.put( "teamNTimeEach", "TRUE" );
      }
      else if ( !isTeam && !isTimePeriod && isEachLevel )
      {
        dataMap.put( "nTeamNTimeEach", "TRUE" );
      }
      else if ( isTeam && isTimePeriod )
      {
        dataMap.put( "teamTime", "TRUE" );
      }
      else if ( !isTeam && isTimePeriod )
      {
        dataMap.put( "nTeamTime", "TRUE" );
      }
      else if ( isTeam && !isTimePeriod )
      {
        dataMap.put( "teamNTime", "TRUE" );
      }
      else if ( !isTeam && !isTimePeriod )
      {
        dataMap.put( "nTeamNTime", "TRUE" );
      }

      if ( isTeam )
      {
        dataMap.put( "team", "TRUE" );
      }
      else
      {
        dataMap.put( "individual", "TRUE" );
      }

      String promotionName = getPromotionService().getPromotionNameByLocale( nominationPromotion.getPromoNameAssetCode(), recipientLanguageType );
      promotionName = StringUtils.isBlank( promotionName ) ? nominationPromotion.getPromotionName() : promotionName;
      dataMap.put( "promotionName", promotionName );
      if ( isTeam )
      {
        dataMap.put( "teamName", claim.getTeamName() );
      }
      if ( isEachLevel )
      {
        String levelName = currentLevel.getLevelLabel();
        if ( currentLevel.getLevelLabelAssetCode() != null )
        {
          levelName = cmAssetService.getString( currentLevel.getLevelLabelAssetCode(), Promotion.PROMOTION_LEVEL_LABEL_NAME_KEY_PREFIX, recipientLocale, true );
        }
        dataMap.put( "levelName", levelName );
      }

      // Begin behaviors
      if ( claim.getNominationClaimBehaviors() != null && !claim.getNominationClaimBehaviors().isEmpty() )
      {
        buildBehaviorsForNominationNotification( claim, dataMap, recipientLocale );
      }
      // End behaviors

      dataMap.put( "message", getNominationClaimComment( claim ) );
      dataMap.put( "nominatorFirstName", nominator.getFirstName() );
      dataMap.put( "nominatorLastName", nominator.getLastName() );
      
      dataMap.put( "pendingNominationCount", pendingNominationCount.toString() );
      if ( pendingNominationCount > 1 )
      {
        dataMap.put( "multipleNominationsPending", "TRUE" );
      }

      // Begin card
      if ( StringUtils.isNotBlank( claim.getOwnCardName() ) )
      {
        dataMap.put( "showECard", "TRUE" );
        dataMap.put( "ecardTypeImage", "TRUE" );
        dataMap.put( "eCardImg", claim.getOwnCardName() );
      }
      else if ( claim.getCard() != null )
      {
        dataMap.put( "showECard", "TRUE" );
        dataMap.put( "ecardTypeImage", "TRUE" );
        String eCardImg = claim.getCard().getLargeImageNameLocale();
        dataMap.put( "eCardImg", eCardImg );
      }
      else if ( StringUtils.isNotBlank( claim.getCardVideoUrl() ) )
      {
        dataMap.put( "showECard", "TRUE" );
        dataMap.put( "ecardTypeVideo", "TRUE" );
        // MTC- To be changed

        if ( claim.getCardVideoUrl().contains( ActionConstants.REQUEST_ID ) )
        {

          MTCVideo mtcVideo = getMtcVideoService().getMTCVideoByRequestId( claim.getRequestId( claim.getCardVideoUrl() ) );
          String eCardVideoLink = null;
          if ( Objects.nonNull( mtcVideo ) )
          {
            eCardVideoLink = mtcVideo.getOriginalFormat().equals( "mp4" ) ? mtcVideo.getMp4Url() : mtcVideo.getWebmUrl();
          }
          else
          {
            eCardVideoLink = claim.getActualCardUrl( claim.getCardVideoUrl() );
          }
          dataMap.put( "eCardVideoLink", eCardVideoLink );
        }
        else
        {
          dataMap.put( "eCardVideoLink", claim.getCardVideoUrl() );
        }
        // Generic play button thumbnail
        String eCardVideoImg = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + DEFAULT_VIDEO_IMAGE;
        dataMap.put( "eCardVideoImg", eCardVideoImg );
      }
      // End card

      // Begin badges
      buildBadgesForNominationPromotionNotification( dataMap, claim, nominee.getId(), true );
      // End badges

      if ( nominationPromotion.isSweepstakesActive() && ( nominationPromotion.getSweepstakesWinnerEligibilityType().getCode().equals( SweepstakesWinnerEligibilityType.NOMINEES_ONLY_CODE )
          || nominationPromotion.getSweepstakesWinnerEligibilityType().getCode().equals( SweepstakesWinnerEligibilityType.NOMINATORS_AND_NOMINEES_COMBINED_CODE )
          || nominationPromotion.getSweepstakesWinnerEligibilityType().getCode().equals( SweepstakesWinnerEligibilityType.NOMINATORS_AND_NOMINEES_SEPARATE_CODE ) ) )
      {
        dataMap.put( "showSweeps", "TRUE" );
      }

      // Approver message
      ApprovableItemApprover approverItem = getApproverForLevel( claim, claimRecipient, currentLevel, evaluationType );
      if ( approverItem != null && StringUtils.isNotBlank( approverItem.getApproverComments() ) )
      {
        dataMap.put( "showApproverMessage", "TRUE" );
        User approver = approverItem.getApproverUser();
        dataMap.put( "approverFirstName", approver.getFirstName() );
        dataMap.put( "approverLastName", approver.getLastName() );
        dataMap.put( "approverMessage", approverItem.getApproverComments() );
      }
      if ( isTimePeriod && !Objects.isNull( approverItem ) )
      {
        String timePeriodNameAssetCode = getClaimService().getPromoTimePeriodNameById( approverItem.getTimePeriodId() );
        if ( !Objects.isNull( timePeriodNameAssetCode ) )
        {
          dataMap.put( "timePeriodName", cmAssetService.getString( timePeriodNameAssetCode, Promotion.PROMOTION_TIME_PERIOD_NAME_KEY_PREFIX, recipientLocale, true ) );
        }
      }

      mailingRecipient.addMailingRecipientDataFromMap( dataMap );
      mailingRecipients.add( mailingRecipient );

    }

    return mailingRecipients;
  }

  /**
   * Build Winner Notification to the nominators of winning nominees.
   * 
   * @param evaluationType - independently or cumulatively
   */
  // Bug # 37498 - added userId
  private Set<MailingRecipient> buildMailingRecipientsForWinnersNominator( NominationClaim claim, NominationEvaluationType evaluationType, Long userId )
  {
    Set<MailingRecipient> mailingRecipients = new HashSet<>();

    NominationPromotion nominationPromotion = (NominationPromotion)claim.getPromotion();

    // Send to intended recipients - if given a userId, only send to that person
    Iterator<ClaimRecipient> claimRecipientIterator = claim.getClaimRecipients().stream().filter( ( recipient ) -> userId == null || recipient.getRecipient().getId().equals( userId ) ).iterator();
    while ( claimRecipientIterator.hasNext() )
    {
      ClaimRecipient claimRecipient = claimRecipientIterator.next();
      MailingRecipient mailingRecipient = new MailingRecipient();
      Map<String, String> dataMap = new HashMap<>();
      Participant nominee = claimRecipient.getRecipient();
      Participant nominator = claim.getSubmitter();
      NominationPromotionLevel currentLevel = getWinningLevel( nominationPromotion, claim );
      String recipientLanguageType = systemVariableService.getDefaultLanguage().getStringVal();
      if ( nominator.getLanguageType() != null )
      {
        recipientLanguageType = nominator.getLanguageType().getCode();
      }
      Locale recipientLocale = CmsUtil.getLocale( recipientLanguageType );

      mailingRecipient.setUser( nominator );
      mailingRecipient.setLocale( recipientLanguageType );
      mailingRecipient.setGuid( GuidUtils.generateGuid() );

      dataMap.put( "nomineeFirstName", nominee.getFirstName() );
      dataMap.put( "nomineeLastName", nominee.getLastName() );
      dataMap.put( "programName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
      dataMap.put( "siteUrl", systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );

      boolean isTeam = claim.hasTeamName();
      boolean isTimePeriod = claim.getTimPeriod() != null;
      boolean isEachLevel = nominationPromotion.getPayoutLevel().equals( "eachLevel" );

      if ( isTeam && !isTimePeriod && isEachLevel )
      {
        dataMap.put( "teamNTimeEach", "TRUE" );
      }
      else if ( !isTeam && !isTimePeriod && isEachLevel )
      {
        dataMap.put( "nTeamNTimeEach", "TRUE" );
      }
      else if ( isTeam && isTimePeriod )
      {
        dataMap.put( "teamTime", "TRUE" );
      }
      else if ( !isTeam && isTimePeriod )
      {
        dataMap.put( "nTeamTime", "TRUE" );
      }
      else if ( isTeam && !isTimePeriod )
      {
        dataMap.put( "teamNTime", "TRUE" );
      }
      else if ( !isTeam && !isTimePeriod )
      {
        dataMap.put( "nTeamNTime", "TRUE" );
      }

      if ( isTeam )
      {
        dataMap.put( "team", "TRUE" );
      }
      else
      {
        dataMap.put( "individual", "TRUE" );
      }

      String promotionName = getPromotionService().getPromotionNameByLocale( nominationPromotion.getPromoNameAssetCode(), recipientLanguageType );
      promotionName = StringUtils.isBlank( promotionName ) ? nominationPromotion.getPromotionName() : promotionName;
      dataMap.put( "promotionName", promotionName );
      if ( isTeam )
      {
        dataMap.put( "teamName", claim.getTeamName() );
      }
      if ( isEachLevel )
      {
        String levelName = currentLevel.getLevelLabel();
        if ( currentLevel.getLevelLabelAssetCode() != null )
        {
          levelName = cmAssetService.getString( currentLevel.getLevelLabelAssetCode(), Promotion.PROMOTION_LEVEL_LABEL_NAME_KEY_PREFIX, recipientLocale, true );
        }
        dataMap.put( "levelName", levelName );
      }

      // Begin behaviors
      if ( claim.getNominationClaimBehaviors() != null && !claim.getNominationClaimBehaviors().isEmpty() )
      {
        buildBehaviorsForNominationNotification( claim, dataMap, recipientLocale );
      }
      // End behaviors

      dataMap.put( "message", getNominationClaimComment( claim ) );
      dataMap.put( "nominatorFirstName", nominator.getFirstName() );
      dataMap.put( "nominatorLastName", nominator.getLastName() );

      // Begin card
      if ( StringUtils.isNotBlank( claim.getOwnCardName() ) )
      {
        dataMap.put( "showECard", "TRUE" );
        dataMap.put( "ecardTypeImage", "TRUE" );
        dataMap.put( "eCardImg", claim.getOwnCardName() );
      }
      else if ( claim.getCard() != null )
      {
        dataMap.put( "showECard", "TRUE" );
        dataMap.put( "ecardTypeImage", "TRUE" );
        String eCardImg = claim.getCard().getLargeImageNameLocale();
        dataMap.put( "eCardImg", eCardImg );
      }
      else if ( StringUtils.isNotBlank( claim.getCardVideoUrl() ) )
      {
        dataMap.put( "showECard", "TRUE" );
        dataMap.put( "ecardTypeVideo", "TRUE" );
        // MTC- To be changed

        if ( claim.getCardVideoUrl().contains( ActionConstants.REQUEST_ID ) )
        {

          MTCVideo mtcVideo = getMtcVideoService().getMTCVideoByRequestId( claim.getRequestId( claim.getCardVideoUrl() ) );
          String eCardVideoLink = null;
          if ( Objects.nonNull( mtcVideo ) )
          {
            eCardVideoLink = mtcVideo.getOriginalFormat().equals( "mp4" ) ? mtcVideo.getMp4Url() : mtcVideo.getWebmUrl();
          }
          else
          {
            eCardVideoLink = claim.getActualCardUrl( claim.getCardVideoUrl() );
          }
          dataMap.put( "eCardVideoLink", eCardVideoLink );
        }
        else
        {
          dataMap.put( "eCardVideoLink", claim.getCardVideoUrl() );
        }
        // Generic play button thumbnail
        String eCardVideoImg = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + DEFAULT_VIDEO_IMAGE;
        dataMap.put( "eCardVideoImg", eCardVideoImg );
      }
      // End card

      // Begin badges
      buildBadgesForNominationPromotionNotification( dataMap, claim, nominee.getId(), true );
      // End badges

      if ( nominationPromotion.isSweepstakesActive() && ( nominationPromotion.getSweepstakesWinnerEligibilityType().getCode().equals( SweepstakesWinnerEligibilityType.NOMINEES_ONLY_CODE )
          || nominationPromotion.getSweepstakesWinnerEligibilityType().getCode().equals( SweepstakesWinnerEligibilityType.NOMINATORS_AND_NOMINEES_COMBINED_CODE )
          || nominationPromotion.getSweepstakesWinnerEligibilityType().getCode().equals( SweepstakesWinnerEligibilityType.NOMINATORS_AND_NOMINEES_SEPARATE_CODE ) ) )
      {
        dataMap.put( "showSweeps", "TRUE" );
      }

      // Approver message
      ApprovableItemApprover approverItem = getApproverForLevel( claim, claimRecipient, currentLevel, evaluationType );
      if ( approverItem != null && StringUtils.isNotBlank( approverItem.getApproverComments() ) )
      {
        dataMap.put( "showApproverMessage", "TRUE" );
        User approver = approverItem.getApproverUser();
        dataMap.put( "approverFirstName", approver.getFirstName() );
        dataMap.put( "approverLastName", approver.getLastName() );
        dataMap.put( "approverMessage", approverItem.getApproverComments() );
      }
      if ( isTimePeriod && !Objects.isNull( approverItem ) )
      {
        String timePeriodNameAssetCode = getClaimService().getPromoTimePeriodNameById( approverItem.getTimePeriodId() );
        if ( !Objects.isNull( timePeriodNameAssetCode ) )
        {
          dataMap.put( "timePeriodName", cmAssetService.getString( timePeriodNameAssetCode, Promotion.PROMOTION_TIME_PERIOD_NAME_KEY_PREFIX, recipientLocale, true ) );
        }
      }

      mailingRecipient.addMailingRecipientDataFromMap( dataMap );
      mailingRecipients.add( mailingRecipient );

    }

    return mailingRecipients;
  }

  /**
   * Build Winner Notification to the manager of winning nominees.
   * 
   * @param evaluationType - independently or cumulatively
   */
  // Bug # 37498 - added userId
  private Set<MailingRecipient> buildMailingRecipientsForWinnersManager( NominationClaim claim, NominationEvaluationType evaluationType, Long userId )
  {

    Set<MailingRecipient> mailingRecipients = new HashSet<>();

    NominationPromotion nominationPromotion = (NominationPromotion)claim.getPromotion();

    // Send to intended recipients - if given a userId, only send to that person
    Iterator<ClaimRecipient> claimRecipientIterator = claim.getClaimRecipients().stream().filter( ( recipient ) -> userId == null || recipient.getRecipient().getId().equals( userId ) ).iterator();
    while ( claimRecipientIterator.hasNext() )
    {
      ClaimRecipient claimRecipient = claimRecipientIterator.next();
      Participant nominee = claimRecipient.getRecipient();
      NominationPromotionLevel currentLevel = getWinningLevel( nominationPromotion, claim );

      Iterator<User> managerIterator = claimRecipient.getNode().getNodeManagersForUser( nominee ).iterator();
      if ( managerIterator != null )
      {
        while ( managerIterator.hasNext() )
        {
          User manager = managerIterator.next();
          MailingRecipient mailingRecipient = new MailingRecipient();
          Map<String, String> dataMap = new HashMap<>();
          String recipientLanguageType = systemVariableService.getDefaultLanguage().getStringVal();
          if ( manager.getLanguageType() != null )
          {
            recipientLanguageType = manager.getLanguageType().getCode();
          }
          Locale recipientLocale = CmsUtil.getLocale( recipientLanguageType );

          mailingRecipient.setUser( manager );
          mailingRecipient.setLocale( recipientLanguageType );
          mailingRecipient.setGuid( GuidUtils.generateGuid() );

          dataMap.put( "managerFirstName", manager.getFirstName() );
          dataMap.put( "nomineeFirstName", nominee.getFirstName() );
          dataMap.put( "nomineeLastName", nominee.getLastName() );
          dataMap.put( "programName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
          dataMap.put( "siteUrl", systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );

          boolean isTeam = claim.hasTeamName();
          boolean isTimePeriod = claim.getTimPeriod() != null;
          boolean isEachLevel = nominationPromotion.getPayoutLevel().equals( "eachLevel" );

          if ( isTeam && !isTimePeriod && isEachLevel )
          {
            dataMap.put( "teamNTimeEach", "TRUE" );
          }
          else if ( !isTeam && !isTimePeriod && isEachLevel )
          {
            dataMap.put( "nTeamNTimeEach", "TRUE" );
          }
          else if ( isTeam && isTimePeriod )
          {
            dataMap.put( "teamTime", "TRUE" );
          }
          else if ( !isTeam && isTimePeriod )
          {
            dataMap.put( "nTeamTime", "TRUE" );
          }
          else if ( isTeam && !isTimePeriod )
          {
            dataMap.put( "teamNTime", "TRUE" );
          }
          else if ( !isTeam && !isTimePeriod )
          {
            dataMap.put( "nTeamNTime", "TRUE" );
          }

          if ( isTeam )
          {
            dataMap.put( "team", "TRUE" );
          }
          else
          {
            dataMap.put( "individual", "TRUE" );
          }

          String promotionName = getPromotionService().getPromotionNameByLocale( nominationPromotion.getPromoNameAssetCode(), recipientLanguageType );
          promotionName = StringUtils.isBlank( promotionName ) ? nominationPromotion.getPromotionName() : promotionName;
          dataMap.put( "promotionName", promotionName );
          if ( isTeam )
          {
            dataMap.put( "teamName", claim.getTeamName() );
          }
          if ( isEachLevel )
          {
            String levelName = currentLevel.getLevelLabel();
            if ( currentLevel.getLevelLabelAssetCode() != null )
            {
              levelName = cmAssetService.getString( currentLevel.getLevelLabelAssetCode(), Promotion.PROMOTION_LEVEL_LABEL_NAME_KEY_PREFIX, recipientLocale, true );
            }
            dataMap.put( "levelName", levelName );
          }

          // Begin awards
          if ( nominationPromotion.isAwardActive() )
          {
            dataMap.put( "showAward", "TRUE" );

            if ( currentLevel.getAwardPayoutType().getCode().equals( PromotionAwardsType.POINTS ) )
            {
              dataMap.put( "points", "TRUE" );
              dataMap.put( "mediaType", currentLevel.getAwardPayoutType().getAbbr() );

              Long awardQuantity = null;
              if ( evaluationType.equals( NominationEvaluationType.lookup( NominationEvaluationType.CUMULATIVE ) ) )
              {
                awardQuantity = claim.getClaimGroup().getAwardQuantity();
              }
              else
              {
                awardQuantity = claimRecipient.getAwardQuantity();
              }

              String formattedAwardQuantity = "0";
              if ( awardQuantity != null && awardQuantity.longValue() > 1 )
              {
                formattedAwardQuantity = NumberFormatUtil.getUserLocaleBasedNumberFormat( awardQuantity, recipientLocale );
              }
              else
              {
                formattedAwardQuantity = String.valueOf( awardQuantity );
              }

              dataMap.put( "awardAmount", formattedAwardQuantity );
            }
            else if ( currentLevel.getAwardPayoutType().getCode().equals( PromotionAwardsType.CASH ) )
            {
              dataMap.put( "cash", "TRUE" );

              BigDecimal awardQuantity = null;
              if ( evaluationType.equals( NominationEvaluationType.lookup( NominationEvaluationType.CUMULATIVE ) ) )
              {
                awardQuantity = claim.getClaimGroup().getCashAwardQuantity();
              }
              else
              {
                awardQuantity = claimRecipient.getCashAwardQuantity();
              }

              String recipientCurrencyCode = nominee.getPrimaryAddress().getAddress().getCountry().getCurrencyCode();
              if ( recipientCurrencyCode != null )
              {
                BigDecimal awardAmountConverted = getCashCurrencyService().convertCurrency( "USD", recipientCurrencyCode, awardQuantity, null );
                String formattedAwardQuantityConverted = NumberFormatUtil.getLocaleBasedBigDecimalFormat( awardAmountConverted, 2, recipientLocale );
                dataMap.put( "cashAwardAmount", formattedAwardQuantityConverted );
                dataMap.put( "currencyType", recipientCurrencyCode );
              }
              else
              {
                String formattedAwardQuantityConverted = NumberFormatUtil.getLocaleBasedBigDecimalFormat( awardQuantity, 2, recipientLocale );
                dataMap.put( "cashAwardAmount", formattedAwardQuantityConverted );
                dataMap.put( "currencyType", "USD" );
              }
            }
            else if ( currentLevel.getAwardPayoutType().getCode().equals( PromotionAwardsType.OTHER ) )
            {
              dataMap.put( "otherAward", "TRUE" );
              String otherAwardDescription = currentLevel.getPayoutDescription();
              if ( currentLevel.getPayoutDescriptionAssetCode() != null )
              {
                otherAwardDescription = cmAssetService.getString( currentLevel.getPayoutDescriptionAssetCode(), Promotion.PAYOUT_DESCRIPTION_KEY_PREFIX, recipientLocale, true );
              }
              dataMap.put( "otherAwardDescription", otherAwardDescription );
            }
          }
          // End awards

          // Begin behaviors
          if ( claim.getNominationClaimBehaviors() != null && !claim.getNominationClaimBehaviors().isEmpty() )
          {
            buildBehaviorsForNominationNotification( claim, dataMap, recipientLocale );
          }
          // End behaviors

          dataMap.put( "message", getNominationClaimComment( claim ) );
          dataMap.put( "nominatorFirstName", claim.getSubmitter().getFirstName() );
          dataMap.put( "nominatorLastName", claim.getSubmitter().getLastName() );

          // Begin card
          if ( StringUtils.isNotBlank( claim.getOwnCardName() ) )
          {
            dataMap.put( "showECard", "TRUE" );
            dataMap.put( "ecardTypeImage", "TRUE" );
            dataMap.put( "eCardImg", claim.getOwnCardName() );
          }
          else if ( claim.getCard() != null )
          {
            dataMap.put( "showECard", "TRUE" );
            dataMap.put( "ecardTypeImage", "TRUE" );
            String eCardImg = claim.getCard().getLargeImageNameLocale();
            dataMap.put( "eCardImg", eCardImg );
          }
          else if ( StringUtils.isNotBlank( claim.getCardVideoUrl() ) )
          {
            dataMap.put( "showECard", "TRUE" );
            dataMap.put( "ecardTypeVideo", "TRUE" );
            // MTC- To be changed

            if ( claim.getCardVideoUrl().contains( ActionConstants.REQUEST_ID ) )
            {

              MTCVideo mtcVideo = getMtcVideoService().getMTCVideoByRequestId( claim.getRequestId( claim.getCardVideoUrl() ) );
              String eCardVideoLink = null;
              if ( Objects.nonNull( mtcVideo ) )
              {
                eCardVideoLink = mtcVideo.getOriginalFormat().equals( "mp4" ) ? mtcVideo.getMp4Url() : mtcVideo.getWebmUrl();
              }
              else
              {
                eCardVideoLink = claim.getActualCardUrl( claim.getCardVideoUrl() );
              }
              dataMap.put( "eCardVideoLink", eCardVideoLink );
            }
            else
            {
              dataMap.put( "eCardVideoLink", claim.getCardVideoUrl() );
            }
            // Generic play button thumbnail
            String eCardVideoImg = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + DEFAULT_VIDEO_IMAGE;
            dataMap.put( "eCardVideoImg", eCardVideoImg );
          }
          // End card

          // Begin badges
          buildBadgesForNominationPromotionNotification( dataMap, claim, nominee.getId(), true );
          // End badges

          // Begin certificate
          if ( claim.getCertificateId() != null )
          {
            Map<String, Object> clientStateParameterMap = new HashMap<>();
            clientStateParameterMap.put( "userId", nominee.getId() );
            clientStateParameterMap.put( "claimId", claim.getId() );
            clientStateParameterMap.put( "promotionId", nominationPromotion.getId() );
            String certificatelink = ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                                           "/claim/displayCertificate.do?method=showCertificateNominationDetail",
                                                                           clientStateParameterMap );
            dataMap.put( "showCertificate", "TRUE" );
            dataMap.put( "certificateLink", certificatelink + "&cmsLocaleCode=" + mailingRecipient.getLocale() );
          }
          // End certificate

          if ( nominationPromotion.isSweepstakesActive() && ( nominationPromotion.getSweepstakesWinnerEligibilityType().getCode().equals( SweepstakesWinnerEligibilityType.NOMINEES_ONLY_CODE )
              || nominationPromotion.getSweepstakesWinnerEligibilityType().getCode().equals( SweepstakesWinnerEligibilityType.NOMINATORS_AND_NOMINEES_COMBINED_CODE )
              || nominationPromotion.getSweepstakesWinnerEligibilityType().getCode().equals( SweepstakesWinnerEligibilityType.NOMINATORS_AND_NOMINEES_SEPARATE_CODE ) ) )
          {
            dataMap.put( "showSweeps", "TRUE" );
          }

          // Approver message
          ApprovableItemApprover approverItem = getApproverForLevel( claim, claimRecipient, currentLevel, evaluationType );
          if ( approverItem != null && StringUtils.isNotBlank( approverItem.getApproverComments() ) )
          {
            dataMap.put( "showApproverMessage", "TRUE" );
            User approver = approverItem.getApproverUser();
            dataMap.put( "approverFirstName", approver.getFirstName() );
            dataMap.put( "approverLastName", approver.getLastName() );
            dataMap.put( "approverMessage", approverItem.getApproverComments() );
          }
          if ( isTimePeriod && !Objects.isNull( approverItem ) )
          {
            String timePeriodNameAssetCode = getClaimService().getPromoTimePeriodNameById( approverItem.getTimePeriodId() );
            if ( !Objects.isNull( timePeriodNameAssetCode ) )
            {
              dataMap.put( "timePeriodName", cmAssetService.getString( timePeriodNameAssetCode, Promotion.PROMOTION_TIME_PERIOD_NAME_KEY_PREFIX, recipientLocale, true ) );
            }
          }

          mailingRecipient.addMailingRecipientDataFromMap( dataMap );
          mailingRecipients.add( mailingRecipient );
        }
      }
    }

    return mailingRecipients;
  }

	    

  /**
   * Find which level a win was at for the winner emails. Needs to account for multiple level scenarios where 
   * the current level might already have been advanced.
   */
  private NominationPromotionLevel getWinningLevel( NominationPromotion promotion, NominationClaim claim )
  {
    NominationPromotionLevel winningLevel = null;

    Long approvalRound = claim.getApprovalRound();
    if ( approvalRound != null && claim.getApprovalRound() >= 1 )
    {
      // If the claim is open, it's been advanced to the next level. Step back to the previous to
      // get the one that was won.
      if ( claim.isOpen() && approvalRound >= 2 )
      {
        approvalRound--;
      }

      Long winningRound = approvalRound;
      winningLevel = promotion.getNominationLevels().stream().filter( ( level ) -> level.getLevelIndex().equals( winningRound ) ).findAny().get();
    }

    return winningLevel;
  }

  /**
   * Return the approver which marked a claim as a winner at the given level, or null
   */
  private ApprovableItemApprover getApproverForLevel( NominationClaim claim, ClaimRecipient claimRecipient, NominationPromotionLevel currentLevel, NominationEvaluationType evaluationType )
  {
    ApprovableItemApprover approverItem = null;
    if ( evaluationType.equals( NominationEvaluationType.lookup( NominationEvaluationType.CUMULATIVE ) ) )
    {
      if ( claim.getClaimGroup().getApprovableItemApprovers() != null && currentLevel != null )
      {
        approverItem = (ApprovableItemApprover)claim.getClaimGroup().getApprovableItemApprovers().stream()
            .filter( ( aia ) -> ( (ApprovableItemApprover)aia ).getApprovalRound().equals( currentLevel.getLevelIndex() ) ).findAny().orElse( null );
      }
    }
    else
    {
      if ( claimRecipient.getApprovableItemApprovers() != null && currentLevel != null )
      {
        approverItem = (ApprovableItemApprover)claimRecipient.getApprovableItemApprovers().stream()
            .filter( ( aia ) -> ( (ApprovableItemApprover)aia ).getApprovalRound().equals( currentLevel.getLevelIndex() ) ).findAny().orElse( null );
      }
    }
    return approverItem;
  }

  /**
   * @param claim
   * @param recipient
   * @return Set of MailingRecipients
   */
  private Set buildMailingRecipientsForRecognitionEmail( RecognitionClaim claim, Participant recipient, String customAward )
  {
    Set mailingRecipients = new HashSet();

    Set claimRecipients = new HashSet();

    // if a specific recipient was passed in, they are the only MailingRecipient(plus copies)
    // if no recipient is passed, all ClaimRecipients(plus copies) should be sent.
    if ( recipient != null )
    {
      ClaimRecipient claimRecipient = getClaimRecipientForRecognition( claim, recipient );
      if ( claimRecipient.getApprovalStatusType() == null || !claimRecipient.getApprovalStatusType().isAbstractDenied() )
      {
        claimRecipients.add( claimRecipient );
      }
    }
    else
    {
      for ( Iterator iter = claim.getClaimRecipients().iterator(); iter.hasNext(); )
      {
        ClaimRecipient claimRecipient = (ClaimRecipient)iter.next();
        if ( claimRecipient.getApprovalStatusType() == null || !claimRecipient.getApprovalStatusType().isAbstractDenied() )
        {
          claimRecipients.add( claimRecipient );
        }
      }
    }

    for ( Iterator claimRecipientIter = claimRecipients.iterator(); claimRecipientIter.hasNext(); )
    {
      ClaimRecipient claimRecipient = (ClaimRecipient)claimRecipientIter.next();

      Participant recipientPax = claimRecipient.getRecipient();

      MailingRecipient mailingRecipient = buildRecognitionMailingRecipient( claim, claimRecipient, recipientPax, true, false, null, customAward );
      mailingRecipients.add( mailingRecipient );

      // get everyone who should be copied on this email.
      Set copiedRecipients = getCopiedRecognitionClaimRecipients( claim, claimRecipient, customAward );

      mailingRecipients.addAll( copiedRecipients );
    }

    return mailingRecipients;
  }

  /**
   * @param claim
   * @param claimRecipient
   * @return Set of MailingRecipients
   */
  private Set getCopiedRecognitionClaimRecipients( RecognitionClaim claim, ClaimRecipient claimRecipient, String customAward )
  {
    Set recipients = new HashSet();

    Participant recipientPax = claimRecipient.getRecipient();

    Participant senderParticipant = null;

    if ( claim.isCopySender() )
    {
      senderParticipant = claim.getSubmitter();

      MailingRecipient senderRecipient = buildRecognitionMailingRecipient( claim, claimRecipient, senderParticipant, false, false, null, customAward );
      senderRecipient.setValidRecipient( true );
      recipients.add( senderRecipient );
    }

    if ( claim.isCopyManager() || ( (RecognitionPromotion)claim.getPromotion() ).isCopyRecipientManager() )
    {
      Set recipientsManagers = new HashSet();

      // Per Bug # 14390 - Manager Award Email: only selected node manager/owner should receive the
      // email
      // Only send the manager email to the node selected when submitting a recognition.
      // for ( Iterator userNodeIter = recipientPax.getUserNodes().iterator();
      // userNodeIter.hasNext(); )
      // {
      // UserNode userNode = (UserNode)userNodeIter.next();

      // AssociationRequestCollection nodeAssociationRequestCollection = new
      // AssociationRequestCollection();
      // nodeAssociationRequestCollection.add( new NodeToUsersAssociationRequest() );
      try
      {
        // Node node = nodeDAO.getNodeWithAssociationsById( claimRecipient.getNode().getId(),
        // nodeAssociationRequestCollection );
        Node node = nodeDAO.getNodeById( claimRecipient.getNode().getId() );

        long startTime = System.currentTimeMillis();
        Set managers = getNodeService().getNodeManagersForUser( recipientPax, node );
        long endTime = System.currentTimeMillis();
        if ( log.isDebugEnabled() )
        {
          log.debug( "Took " + ( endTime - startTime ) + " ms" );
        }

        recipientsManagers.addAll( managers );

      }
      catch( Exception e )
      {
        //
      }
      // }

      // Copy all of the recipients managers on the email.
      for ( Iterator managersIter = recipientsManagers.iterator(); managersIter.hasNext(); )
      {
        User manager = (User)managersIter.next();

        // if the manager was already copied (as sender) don't add them as a recipient.
        if ( senderParticipant == null || !senderParticipant.equals( manager ) )
        {
          MailingRecipient managerRecipient = buildRecognitionMailingRecipient( claim, claimRecipient, manager, false, true, null, customAward );
          managerRecipient.setValidRecipient( true );
          recipients.add( managerRecipient );
        } // if manager is not a copied sender.
      } // for managers
    }

    // Send cc to others
    if ( ( (RecognitionPromotion)claim.getPromotion() ).isCopyOthers() )
    {
      if ( claim.getSendCopyToOthers() != null && claim.getSendCopyToOthers().length() > 0 )
      {
        for ( String emailAddress : claim.getSendCopyToOthers().split( "[,;]" ) )
        {
          String ccOthersEmailAddress = emailAddress.trim();
          MailingRecipient ccRecipient = buildRecognitionMailingRecipient( claim, claimRecipient, null, false, false, ccOthersEmailAddress, customAward );
          ccRecipient.setValidRecipient( true );
          recipients.add( ccRecipient );
        }
      }
    }

    return recipients;
  }

  /**
   * @param recognitionClaim
   * @param participant
   * @return ClaimRecipient
   */
  private ClaimRecipient getClaimRecipientForRecognition( AbstractRecognitionClaim recognitionClaim, Participant participant )
  {
    ClaimRecipient foundClaimRecipient = null;

    for ( Iterator iter = recognitionClaim.getClaimRecipients().iterator(); iter.hasNext(); )
    {
      ClaimRecipient claimRecipient = (ClaimRecipient)iter.next();
      if ( participant == null && claimRecipient.getRecipient() == null || participant != null && participant.equals( claimRecipient.getRecipient() ) )
      {
        foundClaimRecipient = claimRecipient;
      }
    }

    return foundClaimRecipient;
  }

  /**
   * @param claim
   * @param claimRecipient
   * @param sendToUser User
   * @return MailingRecipient
   */
  private MailingRecipient buildRecognitionMailingRecipient( RecognitionClaim claim,
                                                             ClaimRecipient claimRecipient,
                                                             User sendToUser,
                                                             boolean isClaimRecipientThePersonBeingRecognized,
                                                             boolean isManager,
                                                             String ccOthersEmailAddress,
                                                             String customAward)
  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    // Code Fix for Bug#17574 The message library checks for the value of showManagerLink and
    // according to the condition the body of the e-mail varies.
    boolean showManagerLink = false;
    String behaviorName = "";

    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setUser( sendToUser );
    mailingRecipient.setClaimRecipientId( claimRecipient.getId() );
    String localeCode = getLocaleCode( claimRecipient, sendToUser, ccOthersEmailAddress );
    if ( !localeCode.isEmpty() )
    {
      mailingRecipient.setLocale( localeCode );
    }
    else
    {
      mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
    }
    if ( sendToUser == null )
    {
      if ( ccOthersEmailAddress != null )
      {
        mailingRecipient.setPreviewEmailAddress( ccOthersEmailAddress );
      }
    }

    RecognitionPromotion recognitionPromotion = (RecognitionPromotion)claim.getPromotion();
    boolean includeCelebrations = recognitionPromotion.isIncludeCelebrations();
    boolean includePurl = recognitionPromotion.isIncludePurl();
    Map dataMap = new HashMap();

    if ( ccOthersEmailAddress != null && recognitionPromotion.isCopyOthers() )
    {
      sendToUser = getUserService().getUserByPrimaryEmailAddr( ccOthersEmailAddress );
      dataMap.put( "isManager", "TRUE" );
      dataMap.put( "ccOthersEmailAddress", "TRUE" );
    }
    // Fix 17025 Add new assets in CM for recognition mail subject messages

    // Bug # 35471 Fix
    Locale locale = CmsUtil.getLocale( mailingRecipient.getLocale() );
    ContentReader contentReader = ContentReaderManager.getContentReader();
    Content content = (Content)contentReader.getContent( "recognition.detail", locale );
    if ( includeCelebrations )
    {
      String promoNameAssetCode = claim.getPromotion().getPromoNameAssetCode();
      String subjectLine = populateCelebrationNonPurlSubjectLine( promoNameAssetCode, locale, content, contentReader, claimRecipient, isClaimRecipientThePersonBeingRecognized );
      dataMap.put( "subject", subjectLine );
    }
    else
    {
      String subjectLine = StringEscapeUtils.unescapeHtml4( (String)content.getContentDataMap().get( "MAIL_SUBJECT" ) );
      if ( !isClaimRecipientThePersonBeingRecognized )
      {
        subjectLine = StringEscapeUtils.unescapeHtml4( (String)content.getContentDataMap().get( "MAIL_SUBJECT1" ) ) + " " + claimRecipient.getFirstName() + " " + claimRecipient.getLastName() + " "
            + StringEscapeUtils.unescapeHtml4( (String)content.getContentDataMap().get( "MAIL_SUBJECT2" ) );
      }

      dataMap.put( "subject", subjectLine );
      dataMap.put( "customSubject", subjectLine );
    }

    if ( includePurl && recognitionPromotion.getPurlPromotionMediaType() != null && recognitionPromotion.getPurlPromotionMediaType().getCode().equals( PurlPromotionMediaType.VIDEO )
        && recognitionPromotion.getPurlMediaValue() != null )
    {
      String mediaName = recognitionPromotion.getPurlMediaValue().getCode();
      String purlVideoUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/videos/purl/" + mediaName + "/" + mediaName;
      dataMap.put( "purlVideoUrl", purlVideoUrl );
    }
    // populate Celebration Message data map
    if ( includeCelebrations )
    {
      populateCelebrationMessageMap( dataMap, sendToUser, recognitionPromotion, claimRecipient, claim.getId(), includePurl, claim );
    }

    dataMap.put( "firstName", claimRecipient.getFirstName() );
    dataMap.put( "lastName", claimRecipient.getLastName() );
    if ( claim.getPromotion().getPromoNameAssetCode() != null )
    {
      String promotionName = cmAssetService.getString( claim.getPromotion().getPromoNameAssetCode(), Promotion.PROMOTION_NAME_KEY_PREFIX, locale, true );
      dataMap.put( "programName", StringEscapeUtils.unescapeHtml4( promotionName ) );
    }
    dataMap.put( "sender", claim.getSubmitter().getFirstName() + " " + claim.getSubmitter().getLastName() );
    dataMap.put( "companyName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    if ( claim.getBehavior() != null )
    {
      behaviorName = claim.getBehavior().getCode();
      List contentList = (List)contentReader.getContent( "picklist.promo.recognition.behavior.items", locale );

      for ( Iterator iter = contentList.iterator(); iter.hasNext(); )
      {
        content = (Content)iter.next();
        String code = (String)content.getContentDataMap().get( PickListItem.ITEM_CODE_KEY );
        if ( claim.getBehavior().getCode().equalsIgnoreCase( code ) )
        {
          String category = (String)content.getContentDataMap().get( PickListItem.ITEM_NAME_KEY );
          dataMap.put( "category", StringEscapeUtils.unescapeHtml4( category ) );
          break;
        }
      }
    }
    /* Bug # 31879 start */
    if ( claim.getSubmitterComments() != null && !claim.getSubmitterComments().trim().equals( "" ) )
    /* Bug # 31879 end */
    {
      dataMap.put( "showComment", "TRUE" );
      dataMap.put( "message", claim.getSubmitterComments() );
      dataMap.put( "comments", claim.getSubmitterComments() );

      if ( sendToUser != null && sendToUser.getLanguageType() != null && claim.getSubmitterCommentsLanguageType() != null && claim.getSubmitterComments() != null
          && claim.getSubmitterComments().trim().length() > 0 && !sendToUser.getLanguageType().equals( claim.getSubmitterCommentsLanguageType() ) )
      {
        try
        {
          TranslatedContent tc = translationService.translate( claim.getSubmitterCommentsLanguageType(), sendToUser.getLanguageType(), claim.getSubmitterComments() );
          dataMap.put( "translatedMessage", tc.getTranslatedContent() );
        }
        catch( Throwable t )
        {
          StringBuilder sb = new StringBuilder();
          sb.append( "\n**********************************************************" );
          sb.append( "\n* ERROR in method buildRecognitionMailingRecipient" );
          sb.append( "\n* while translating content" );
          sb.append( "\n**********************************************************" );
          sb.append( "\nsendTouser ID: " ).append( sendToUser.getId() );
          sb.append( "\nclaim ID: " ).append( claim.getId() );
          sb.append( "\nsendToUser languageType:" ).append( sendToUser.getLanguageType() );
          sb.append( "\nsubmitter comments languageType: " ).append( claim.getSubmitterCommentsLanguageType() );
          sb.append( "\nerror message: " ).append( t.getMessage() );
          sb.append( "\n**********************************************************" );
          sb.append( "\n* END ERROR" );
          sb.append( "\n**********************************************************" );

          log.error( sb.toString() );
        }
      }
    }

    if ( ( recognitionPromotion.isAwardActive() || recognitionPromotion.isAllowPublicRecognitionPoints() ) && claimRecipient.getAwardQuantity() != null
        && claimRecipient.getAwardQuantity().longValue() > 0 )
    {
      Long awardQuantity = claimRecipient.getAwardQuantity();
      if ( awardQuantity != null && awardQuantity.longValue() > 1 )
      {
        dataMap.put( "manyAwardAmount", "TRUE" );
      }
      String tempAwardQuantity = NumberFormatUtil.getUserLocaleBasedNumberFormat( claimRecipient.getAwardQuantity(), LocaleUtils.getLocale( mailingRecipient.getLocale() ) );
      if( recognitionPromotion.getAdihCashOption()){    	  
    	  dataMap.put( "awardAmount", String.valueOf(customAward) );
      }else{
    	  dataMap.put( "awardAmount", String.valueOf( tempAwardQuantity ) );
      }
      List contentList = (List)contentReader.getContent( "picklist.promotion.awardstype.items", locale );

      for ( Iterator iter = contentList.iterator(); iter.hasNext(); )
      {
        content = (Content)iter.next();
        String code = (String)content.getContentDataMap().get( PickListItem.ITEM_CODE_KEY );
        String status = (String)content.getContentDataMap().get( PickListItem.ITEM_STATUS_KEY );
        if ( recognitionPromotion.getAwardType().getCode().equalsIgnoreCase( code ) && status.equals( "true" ) )
        {
          String mediaType = (String)content.getContentDataMap().get( PickListItem.ITEM_NAME_KEY );
          dataMap.put( "mediaType", StringEscapeUtils.unescapeHtml4( mediaType ) );
          break;
        }
      }
    }

    // Get recipient's node owner
    boolean isNodeOwner = false;
    // AssociationRequestCollection associationRequestCollection = new
    // AssociationRequestCollection();
    // associationRequestCollection.add( new NodeToUsersAssociationRequest() );
    try
    {
      // Node node = nodeDAO.getNodeWithAssociationsById( claimRecipient.getNode().getId(),
      // associationRequestCollection );
      Node node = nodeDAO.getNodeById( claimRecipient.getNode().getId() );

      User nodeOwner = getParticipantService().getNodeOwner( node.getId() );
      // Bug # 35458 Fix & Bug # 35469 fix
      if ( nodeOwner != null && nodeOwner.equals( sendToUser ) )
      {
        isNodeOwner = true;
      }

    }
    catch( Exception e )
    { /* Exception */
      log.error( "Exception while checking for node owner", e );
    }

    // If on the recognition promotion setup 'copy manager' is selected
    // OR; if the recognition giver selected 'copy manager' on the claim
    // and the recipient is not a manager or a node owner then get the 'copy manager' text
    if ( recognitionPromotion.isCopyRecipientManager() && !isManager && !isNodeOwner || claim.isCopyManager() && !isManager && !isNodeOwner )
    {
      dataMap.put( "copyManager", "TRUE" );
    }

    // If sweepstakes is active and the Reciever is eligible.
    if ( recognitionPromotion.isSweepstakesActive()
        && ( recognitionPromotion.getSweepstakesWinnerEligibilityType().getCode().equals( SweepstakesWinnerEligibilityType.GIVERS_AND_RECEIVERS_COMBINED_CODE )
            || recognitionPromotion.getSweepstakesWinnerEligibilityType().getCode().equals( SweepstakesWinnerEligibilityType.GIVERS_AND_RECEIVERS_SEPARATE_CODE )
            || recognitionPromotion.getSweepstakesWinnerEligibilityType().getCode().equals( SweepstakesWinnerEligibilityType.RECEIVERS_ONLY_CODE ) ) )
    {
      dataMap.put( "showSweeps", "TRUE" );
    }

    if ( claim.getCertificateId() != null )
    {
      dataMap.put( "showCertificate", "TRUE" );

      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "claimItemId", claimRecipient.getId() );
      clientStateParameterMap.put( "claimId", claim.getId() );
      clientStateParameterMap.put( "promotionId", recognitionPromotion.getId() );

      String certificatelink = ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                                     "/claim/displayCertificate.do?method=showCertificateRecognitionDetail",
                                                                     clientStateParameterMap );

      String certificateCompleteLink = certificatelink + "&cmsLocaleCode=" + mailingRecipient.getLocale();
      dataMap.put( "certificateLink", certificateCompleteLink );
      log.debug( "Certificate Complete Url: " + certificateCompleteLink );
      String encodedLink = null;
      try
      {
        encodedLink = URLEncoder.encode( certificateCompleteLink, "UTF-8" );
      }
      catch( UnsupportedEncodingException e )
      {
        log.error( "<<<<ERROR>>>>>" + e );
      }
      String certificateShortLink = getShortUrl( encodedLink );
      log.debug( "Certificate Short Url After Encoded: " + certificateShortLink );
      dataMap.put( "shortLink", certificateShortLink );
    }

    if ( claim.getCard() != null )
    {
      dataMap.put( "showECard", "TRUE" );

      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "claimId", claim.getId() );

      String link = ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                          "/claim/viewrecognition.do",
                                                          clientStateParameterMap );

      dataMap.put( "eCardLink", link + "&cmsLocaleCode=" + mailingRecipient.getLocale() );

      ECard eCard = (ECard)claim.getCard();
      String eCardImg = null;

      // fetching the locale specific ecard name if ecard is translatable and recipient locale is
      // not en_US
      if ( eCard.isTranslatable() && !Locale.US.equals( CmsUtil.getLocale( mailingRecipient.getLocale() ) ) )
      {
        eCardImg = claim.getCard().getLargeImageNameLocale( mailingRecipient.getLocale() );

      }
      else
      {
        eCardImg = claim.getCard().getLargeImageNameLocale( mailingRecipient.getLocale() );
      }

      dataMap.put( "eCardImg", eCardImg );
      dataMap.put( "eCardTypeImage", "TRUE" );

      String cardCompleteLink = link + "&cmsLocaleCode=" + mailingRecipient.getLocale();
      String encodedLink = null;
      try
      {
        encodedLink = URLEncoder.encode( cardCompleteLink, "UTF-8" );
      }
      catch( UnsupportedEncodingException e )
      {
        log.error( "<<<<ERROR>>>>>" + e );
      }
      String cardShortLink = getShortUrl( encodedLink );
      dataMap.put( "shortLink", cardShortLink );
      if ( claim.getCard() instanceof ECard && ( (ECard)claim.getCard() ).isFlashNeeded() )
      {
        dataMap.put( "flashNeeded", "TRUE" );
      }
    }

    if ( claim.getOwnCardName() != null && claim.getOwnCardName().length() > 0 )
    {
      dataMap.put( "showECard", "TRUE" );

      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "claimId", claim.getId() );

      String link = ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                          "/claim/viewrecognition.do",
                                                          clientStateParameterMap );

      dataMap.put( "eCardLink", link + "&cmsLocaleCode=" + mailingRecipient.getLocale() );

      String type = SupportedEcardVideoTypes.isSupportedVideo( claim.getOwnCardName() ) ? ECARD_TYPE_VIDEO : ECARD_TYPE_IMAGE;

      if ( type.equals( ECARD_TYPE_IMAGE ) )
      {
        dataMap.put( "eCardImg", claim.getOwnCardName() );
        dataMap.put( "eCardTypeImage", "TRUE" );
      }
      else
      {
        dataMap.put( "eCardVideoLink", link + "&cmsLocaleCode=" + mailingRecipient.getLocale() );
        String eCardVideoImg = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + DEFAULT_VIDEO_IMAGE;
        dataMap.put( "eCardVideoImg", eCardVideoImg );
        dataMap.put( "eCardTypeVideo", "TRUE" );
      }

      String cardCompleteLink = link + "&cmsLocaleCode=" + mailingRecipient.getLocale();
      String encodedLink = null;
      try
      {
        encodedLink = URLEncoder.encode( cardCompleteLink, "UTF-8" );
      }
      catch( UnsupportedEncodingException e )
      {
        log.error( "<<<<ERROR>>>>>" + e );
      }
      String cardShortLink = getShortUrl( encodedLink );
      dataMap.put( "shortLink", cardShortLink );
    }

    if ( claim.getCardVideoUrl() != null && claim.getCardVideoUrl().length() > 0 )
    {
      dataMap.put( "showECard", "TRUE" );

      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "claimId", claim.getId() );

      String link = ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                          "/claim/viewrecognition.do",
                                                          clientStateParameterMap );

      dataMap.put( "eCardLink", link + "&cmsLocaleCode=" + mailingRecipient.getLocale() );

      // This appears to give a default video icon image thing, which when clicked will lead to a
      // page to view the video
      // Now, a link directly to the video and an actual thumbnail image could be used
      dataMap.put( "eCardVideoLink", link + "&cmsLocaleCode=" + mailingRecipient.getLocale() );
      String eCardVideoImg = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + DEFAULT_VIDEO_IMAGE;
      dataMap.put( "eCardVideoImg", eCardVideoImg );
      dataMap.put( "eCardTypeVideo", "TRUE" );

      String cardCompleteLink = link + "&cmsLocaleCode=" + mailingRecipient.getLocale();
      String encodedLink = null;
      try
      {
        encodedLink = URLEncoder.encode( cardCompleteLink, "UTF-8" );
      }
      catch( UnsupportedEncodingException e )
      {
        log.error( "<<<<ERROR>>>>>" + e );
      }
      String cardShortLink = getShortUrl( encodedLink );
      dataMap.put( "shortLink", cardShortLink );
    }

    if ( claim.getCertificateId() == null && claim.getCard() == null && claim.getOwnCardName() == null )
    {
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "claimId", claim.getId() );

      String link = ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                          "/claim/viewrecognition.do",
                                                          clientStateParameterMap );
      String mesgCompleteLink = link + "&cmsLocaleCode=" + mailingRecipient.getLocale();
      String encodedLink = null;
      try
      {
        encodedLink = URLEncoder.encode( mesgCompleteLink, "UTF-8" );
      }
      catch( UnsupportedEncodingException e )
      {
        log.error( "<<<<ERROR>>>>>" + e );
      }
      String mesgShortLink = getShortUrl( encodedLink );
      dataMap.put( "shortLink", mesgShortLink );
    }

    String promotionName = cmAssetService.getString( claim.getPromotion().getPromoNameAssetCode(), Promotion.PROMOTION_NAME_KEY_PREFIX, locale, true );
    String programName = StringEscapeUtils.unescapeHtml4( promotionName );

    if ( isManager )
    {
      dataMap.put( "isManager", "TRUE" );

      if ( includeCelebrations || includePurl )
      {
        String managerSubjectLine = StringEscapeUtils.unescapeHtml4( ContentReaderManager.getText( "recognition.detail", "CELEBRATION_OTHER_MAIL_SUBJECT1" ) ) + " " + claimRecipient.getFirstName()
            + " " + StringEscapeUtils.unescapeHtml4( ContentReaderManager.getText( "recognition.detail", "CELEBRATION_OTHER_MAIL_SUBJECT2" ) ) + " " + programName + " "
            + StringEscapeUtils.unescapeHtml4( ContentReaderManager.getText( "recognition.detail", "CELEBRATION_OTHER_MAIL_SUBJECT3" ) );
        dataMap.put( "purlcelebrationMailSubject", managerSubjectLine );
      }

      Long mgrPaxId = sendToUser.getId();
      if ( recognitionPromotion.isAllowManagerAward() )
      {
        String managerNodeId = null;
        Participant recipientPax = claimRecipient.getRecipient();

        // Set recipientsManagers = new HashSet();

        // AssociationRequestCollection nodeAssociationRequestCollection = new
        // AssociationRequestCollection();
        // nodeAssociationRequestCollection.add( new NodeToUsersAssociationRequest() );
        try
        {
          // Node node = nodeDAO.getNodeWithAssociationsById( claimRecipient.getNode().getId(),
          // nodeAssociationRequestCollection );

          // get the manager's node id for this participant and manager
          managerNodeId = sendToUser.getPrimaryUserNode().getNode().getId().toString();

        }
        catch( Exception e )
        {

          log.error( "Exception in finding Manager's Node ID" );
        }

        // Code Fix for Bug#17574 Checking whether the manager is a giver of the discretionary award
        // promotion
        // if he is the giver showManagerLink is set true.
        Promotion mgrAwardPromotion = getPromotionService().getPromotionById( recognitionPromotion.getMgrAwardPromotionId() );
        if ( mgrAwardPromotion.getPrimaryAudienceType().isAllActivePaxType() )
        {
          showManagerLink = true;
        }
        else
        {
          Set primAudienceSet = mgrAwardPromotion.getPrimaryAudiences();
          for ( Iterator iter = primAudienceSet.iterator(); iter.hasNext(); )
          {
            // Bug # 34007 fix
            Object promoObject = iter.next();

            if ( promoObject instanceof CriteriaAudience )
            {
              // pax is in crit audience if in any Audience Criteria
              // BELOW PIECE OF CODE IS COMMENTED AS THERE WAS PERFORMANCE ISSUE FOR PRMOTIONS THAT
              // HAVE MANAGER ADD POINTS TURNED ON.
              CriteriaAudience criteriaAudience = (CriteriaAudience)promoObject;
              // List participants = listBuilderDAO.searchParticipants( criteriaAudience, null,
              // false, new HashSet(), true, false );
              // for ( Iterator paxIter = participants.iterator(); paxIter.hasNext(); )
              // {
              // FormattedValueBean paxFormattedValueBean = (FormattedValueBean)paxIter.next();
              // if ( paxFormattedValueBean.getId().longValue() == mgrPaxId.longValue() )
              // {
              // showManagerLink = true;
              // }
              //
              // }

              Audience audience = criteriaAudience;
              if ( getAudienceService().isParticipantInAudience( mgrPaxId, audience ) )
              {
                showManagerLink = true;
              }
            }
            else if ( promoObject instanceof PaxAudience )
            {
              List audPaxlist = ( (PaxAudience)promoObject ).getAudienceParticipants();
              for ( Iterator iterator = audPaxlist.iterator(); iterator.hasNext(); )
              {
                AudienceParticipant audPax = (AudienceParticipant)iterator.next();
                if ( audPax.getParticipant().getId().longValue() == mgrPaxId.longValue() )
                {
                  showManagerLink = true;
                }
              }
            }

          }
        }

        if ( showManagerLink )
        {
          try
          {
            // the manager must have an available budget; if not, do not show the link
            Budget budget = getPromotionService().getAvailableBudget( recognitionPromotion.getMgrAwardPromotionId(), mgrPaxId, new Long( managerNodeId ) );
            if ( budget == null )
            {
              showManagerLink = false;
            }
          }
          catch( Throwable t )
          {
            showManagerLink = false;
          }
        }

        if ( showManagerLink )
        {
          dataMap.put( "showManagerLink", "TRUE" );

          Map clientStateParameterMap = new HashMap();
          clientStateParameterMap.put( "promotionId", recognitionPromotion.getId() );
          clientStateParameterMap.put( "managerPromotionId", recognitionPromotion.getMgrAwardPromotionId() );
          clientStateParameterMap.put( "recipientId", claimRecipient.getRecipient().getId() );
          clientStateParameterMap.put( "claimId", claim.getId() );
          clientStateParameterMap.put( "managerNodeId", managerNodeId );

          String managerLink = ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                                     "/claim/managerAddPoints.do",
                                                                     clientStateParameterMap );
          dataMap.put( "managerLink", managerLink );
        }
      }

      List<ParticipantBadge> paxBadges = getGamificationService().getBehaviorBadgesForRecognitionEmail( recognitionPromotion.getId(), claimRecipient.getRecipient().getId(), behaviorName );
      if ( paxBadges != null && paxBadges.size() > 0 )
      {
        dataMap.put( "showBadges", "TRUE" );
        Map paxBadgeMap = new HashMap();
        paxBadgeMap = buildBehaviorBadgeHtmlString( paxBadges );
        String paxBadgeEarnedString = "";
        if ( paxBadgeMap.get( "paxBadgeEarnedString" ) != null )
        {
          paxBadgeEarnedString = paxBadgeMap.get( "paxBadgeEarnedString" ).toString();
        }
        if ( !StringUtils.isEmpty( paxBadgeEarnedString ) )
        {
          String paxBadgeEarnedNoHtml = "";
          paxBadgeEarnedNoHtml = HtmlUtils.removeFormatting( paxBadgeEarnedString );
          if ( !StringUtils.isEmpty( paxBadgeEarnedNoHtml.trim() ) )
          {
            dataMap.put( "showEarnedBadges", "TRUE" );
            dataMap.put( "paxBadgeEarnedString", paxBadgeEarnedString );
            dataMap.put( "paxBadgeEarnedStringPlain", HtmlUtils.removeFormatting( paxBadgeEarnedString ) );
          }
        }
        if ( paxBadgeMap.get( "badgePoints" ) != null && (Long)paxBadgeMap.get( "badgePoints" ) > 1 )
        {
          dataMap.put( "manyBadgePoints", "TRUE" );
        }

        if ( paxBadgeMap.get( "badgePoints" ) != null )
        {
          String tempBadgeAwardQuantity = NumberFormatUtil.getUserLocaleBasedNumberFormat( (Long)paxBadgeMap.get( "badgePoints" ), LocaleUtils.getLocale( mailingRecipient.getLocale() ) );
          dataMap.put( "badgePoints", String.valueOf( tempBadgeAwardQuantity ) );
          dataMap.put( "badgeMediaType", paxBadgeMap.get( "badgeMediaType" ) );
        }
        if ( paxBadgeMap.get( "siteUrlPrefix" ) != null )
        {
          dataMap.put( "programUrl", paxBadgeMap.get( "siteUrlPrefix" ) );
        }
      }
    }
    else
    {
      if ( includeCelebrations || includePurl )
      {
        String recipientSubjectLine = StringEscapeUtils.unescapeHtml4( ContentReaderManager.getText( "recognition.detail", "CELEBRATION_MAIL_SUBJECT1" ) ) + " " + programName + " "
            + StringEscapeUtils.unescapeHtml4( ContentReaderManager.getText( "recognition.detail", "CELEBRATION_RECOGNITION" ) );
        dataMap.put( "purlcelebrationMailSubject", recipientSubjectLine );
      }
    }

    if ( isClaimRecipientThePersonBeingRecognized )
    {
      dataMap.put( "isClaimRecipient", "TRUE" );
      if ( !recognitionPromotion.getAwardType().getCode().equals( PromotionAwardsType.MERCHANDISE ) )
      {
        dataMap.put( "showAcctLink", "TRUE" );
        // dataMap.put( "siteLink", systemVariableService.getPropertyByNameAndEnvironment(
        // SystemVariableService.SITE_URL_PREFIX ).getStringVal() +
        // "/participant/myAccountDisplay.do?method=display" );
        dataMap.put( "siteLink",
                     systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/participantProfilePage.do?fromEmail=true#tab/Statement" );
      }
      else
      { // bug# if no awards, don't lookup merch orders
        if ( recognitionPromotion.isAwardActive() && recognitionPromotion.getAwardStructure() != null && recognitionPromotion.getAwardStructure().equals( RecognitionPromotion.AWARD_STRUCTURE_LEVEL ) )
        {
          // find merchorder by claim ID
          HibernateSessionManager.getSession().flush();
          MerchOrderActivityQueryConstraint constraint = new MerchOrderActivityQueryConstraint();
          constraint.setClaimId( claimRecipient.getClaim().getId() );

          List orders = getMerchOrderService().getMerchOrderList( constraint );
          MerchOrder merchOrder = (MerchOrder)orders.get( 0 );
          if ( merchOrder.getFullGiftCode() == null )
          {
            mailingRecipient.setValidRecipient( false );
          }
          else
          {
            mailingRecipient.setValidRecipient( true );
          }

          // override the claim number to be the gift code's reference number
          dataMap.put( "claimNumber", merchOrder.getReferenceNumber() );
          dataMap.put( "showLevelLink", "TRUE" );

          String aesEncryptionKey = systemVariableService.getPropertyByName( SystemVariableService.SSO_AES256_KEY ).getStringVal();
          String aesInitVector = systemVariableService.getPropertyByName( SystemVariableService.SSO_INIT_VECTOR ).getStringVal();
          String giftCodeEncrypted = null;
          try
          {
            giftCodeEncrypted = SecurityUtils.encryptAES( merchOrder.getFullGiftCode(), aesEncryptionKey, aesInitVector );
            log.debug( "Gift code after encryption and before sending email: " + giftCodeEncrypted );
          }
          catch( Exception e )
          {
            log.error( "Unable to encrypt gift code: " + merchOrder.getFullGiftCode(), e );
          }

          dataMap.put( "levelLink", getMerchLevelPageUrl( claimRecipient.getClaim().getId(), claimRecipient.getRecipient().getId(), giftCodeEncrypted ) );

          // bug#18756 need to add gift code and phone number in the email
          dataMap.put( "showGiftCode", "TRUE" );
          dataMap.put( "giftCode", merchOrder.getFullGiftCode() );
          dataMap.put( "phoneNumber", systemVariableService.getPropertyByName( SystemVariableService.MERCHLINQ_ORDER_PHONE ).getStringVal() );
        }
      }

      List<ParticipantBadge> paxBadges = getGamificationService().getBadgesForRecognitionEmail( recognitionPromotion.getId(), claimRecipient.getRecipient().getId(), behaviorName );
      if ( paxBadges != null && paxBadges.size() > 0 )
      {
        dataMap.put( "showBadges", "TRUE" );
        Map paxBadgeMap = new HashMap();
        paxBadgeMap = buildBadgeHtmlString( paxBadges );
        String paxBadgeEarnedString = "";
        String paxBadgeProgressString = "";
        if ( paxBadgeMap.get( "paxBadgeEarnedString" ) != null )
        {
          paxBadgeEarnedString = paxBadgeMap.get( "paxBadgeEarnedString" ).toString();
        }
        if ( paxBadgeMap.get( "paxBadgeProgressString" ) != null )
        {
          paxBadgeProgressString = paxBadgeMap.get( "paxBadgeProgressString" ).toString();
        }
        if ( !StringUtils.isEmpty( paxBadgeEarnedString ) )
        {
          String paxBadgeEarnedNoHtml = "";
          paxBadgeEarnedNoHtml = HtmlUtils.removeFormatting( paxBadgeEarnedString );
          if ( !StringUtils.isEmpty( paxBadgeEarnedNoHtml.trim() ) )
          {
            dataMap.put( "showEarnedBadges", "TRUE" );
            dataMap.put( "paxBadgeEarnedString", paxBadgeEarnedString );
            dataMap.put( "paxBadgeEarnedStringPlain", HtmlUtils.removeFormatting( paxBadgeEarnedString ) );
          }
        }
        if ( !StringUtils.isEmpty( paxBadgeProgressString ) )
        {
          String paxBadgeProgressNoHtml = "";
          paxBadgeProgressNoHtml = HtmlUtils.removeFormatting( paxBadgeProgressString );
          if ( !StringUtils.isEmpty( paxBadgeProgressNoHtml.trim() ) )
          {
            dataMap.put( "showProgressBar", "TRUE" );
            dataMap.put( "paxBadgeProgressString", paxBadgeProgressString );
            dataMap.put( "paxBadgeProgressStringPlain", HtmlUtils.removeFormatting( paxBadgeProgressString ) );
          }
        }
        if ( paxBadgeMap.get( "badgePoints" ) != null && (Long)paxBadgeMap.get( "badgePoints" ) > 1 )
        {
          dataMap.put( "manyBadgePoints", "TRUE" );
        }

        if ( paxBadgeMap.get( "badgePoints" ) != null )
        {
          String tempBadgeAwardQuantity = NumberFormatUtil.getUserLocaleBasedNumberFormat( (Long)paxBadgeMap.get( "badgePoints" ), LocaleUtils.getLocale( mailingRecipient.getLocale() ) );
          dataMap.put( "badgePoints", String.valueOf( tempBadgeAwardQuantity ) );
          dataMap.put( "badgeMediaType", paxBadgeMap.get( "badgeMediaType" ) );
        }
        if ( paxBadgeMap.get( "siteUrlPrefix" ) != null )
        {
          dataMap.put( "programUrl", paxBadgeMap.get( "siteUrlPrefix" ) );
        }
      }

    }

    // override the claimNumber if this promotion is not a recognition that is
    // award level based.
    if ( recognitionPromotion.getAwardStructure() == null || !recognitionPromotion.getAwardStructure().equals( RecognitionPromotion.AWARD_STRUCTURE_LEVEL ) )
    {
      dataMap.put( "claimNumber", claim.getClaimNumber() );
    }

    mailingRecipient.addMailingRecipientDataFromMap( dataMap );

    return mailingRecipient;
  }

  private String populateCelebrationNonPurlSubjectLine( String promoNameAssetCode,
                                                        Locale locale,
                                                        Content content,
                                                        ContentReader contentReader,
                                                        ClaimRecipient claimRecipient,
                                                        boolean isClaimRecipientThePersonBeingRecognized )
  {
    String programName = "";
    if ( promoNameAssetCode != null )
    {
      String promotionName = cmAssetService.getString( promoNameAssetCode, Promotion.PROMOTION_NAME_KEY_PREFIX, locale, true );
      programName = StringEscapeUtils.unescapeHtml4( promotionName );
    }

    String subjectLine = StringEscapeUtils.unescapeHtml4( cmAssetService.getString( "recognition.detail", "CELEBRATION_MAIL_SUBJECT1", locale, true ) ) + " " + programName + " "
        + StringEscapeUtils.unescapeHtml4( cmAssetService.getString( "recognition.detail", "CELEBRATION_MAIL_SUBJECT2", locale, true ) );


    if ( !isClaimRecipientThePersonBeingRecognized )
    {
      subjectLine = StringEscapeUtils.unescapeHtml4( ContentReaderManager.getText( "recognition.detail", "CELEBRATION_OTHER_MAIL_SUBJECT1" ) ) + " " + claimRecipient.getFirstName() + " "
          + claimRecipient.getLastName() + " " + StringEscapeUtils.unescapeHtml4( ContentReaderManager.getText( "recognition.detail", "CELEBRATION_OTHER_MAIL_SUBJECT2" ) + " " + programName + " "
              + StringEscapeUtils.unescapeHtml4( ContentReaderManager.getText( "recognition.detail", "CELEBRATION_OTHER_MAIL_SUBJECT3" ) ) );
    }
    return subjectLine;
  }

  private String getCelebrationExpireDateForRecipient( Participant recipient, Long displayPeriod, String localeCode )
  {
    String datePattern = DateFormatterUtil.getOracleDatePattern( localeCode );
    SimpleDateFormat sdf = new SimpleDateFormat( datePattern );
    Calendar c = Calendar.getInstance();
    c.setTime( new Date() );
    c.add( Calendar.DATE, displayPeriod.intValue() );
    String recipientTimeZoneID = getUserService().getUserTimeZoneByUserCountry( recipient.getId() );
    Date recipientExpirationDate = com.biperf.core.utils.DateUtils.applyTimeZone( c.getTime(), recipientTimeZoneID );
    String expireDateStr = com.biperf.core.utils.DateUtils.toDisplayString( recipientExpirationDate, CmsUtil.getLocale( localeCode ) );
    return expireDateStr;
  }

  // This is TODO since the screens to capture this values are not ready
  private void populateCelebrationMessageMap( Map dataMap,
                                              User sendToUser,
                                              RecognitionPromotion recognitionPromotion,
                                              ClaimRecipient claimRecipient,
                                              Long claimId,
                                              boolean includePurl,
                                              RecognitionClaim recClaim )
  {
    String localeCode = systemVariableService.getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();
    Participant recipient = claimRecipient.getRecipient();
    if ( recipient.getLanguageType() != null )
    {
      localeCode = recipient.getLanguageType().getCode();
    }
    if ( includePurl )
    {
      PurlRecipient purlRecipient = getPurlService().getPurlRecipientByClaimId( claimId );

      int numOfDays = systemVariableService.getPropertyByName( SystemVariableService.PURL_DAYS_TO_EXP ).getIntVal();
      Date purlExpireDate = new Date( purlRecipient.getAwardDate().getTime() + numOfDays * DateUtils.MILLIS_PER_DAY );
      dataMap.put( "purlExpireDate", com.biperf.core.utils.DateUtils.toDisplayString( purlExpireDate, LocaleUtils.getLocale( localeCode ) ) );

      if ( purlRecipient.getCustomElements() != null && !purlRecipient.getCustomElements().isEmpty() )
      {
        for ( int i = 0; i < purlRecipient.getCustomElements().size(); i++ )
        {
          dataMap.put( "formElement" + ( i + 1 ), purlRecipient.getCustomElement( i ).getDisplayValue() );
        }
      }

      String purlUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.PURL_URL_PREFIX ).getStringVal();
      StringBuffer purlRecipientLink = new StringBuffer( purlUrl );
      purlRecipientLink.append( '/' );
      purlRecipientLink.append( purlRecipient.getUser().getFirstName().replace( PurlService.PURL_RECIPIENT_URL_NAME, PurlService.PURL_RECIPIENT_URL_DELIMITER ) );
      purlRecipientLink.append( '.' );
      purlRecipientLink.append( purlRecipient.getUser().getLastName().replace( PurlService.PURL_RECIPIENT_URL_NAME, PurlService.PURL_RECIPIENT_URL_DELIMITER ) );
      purlRecipientLink.append( '.' );
      purlRecipientLink.append( purlRecipient.getId() );
      purlRecipientLink.append( "?viewingId=" + purlRecipient.getUser().getId() );

      dataMap.put( "purlRecipientLink", purlRecipientLink.toString() );
      String encodedLink = null;
      try
      {
        encodedLink = URLEncoder.encode( purlRecipientLink.toString(), "UTF-8" );
      }
      catch( UnsupportedEncodingException e )
      {
        log.error( "<<<<ERROR>>>>>" + e );
      }
      dataMap.put( "shortLink", getShortUrl( encodedLink ) );

    }
    if ( Objects.nonNull( sendToUser ) )
    {
      dataMap.put( "recipientFirstName", sendToUser.getFirstName() );
    }
    else
    {
      dataMap.put( "recipientFirstName", "" );
    }

    boolean serviceAnniversaryEnabled = false;
    if ( recognitionPromotion.isIncludeCelebrations() && recognitionPromotion.isServiceAnniversary() )
    {
      serviceAnniversaryEnabled = true;
    }
    if ( serviceAnniversaryEnabled )
    {
      dataMap.put( "serviceAnniversary", "TRUE" );
    }
    if ( !serviceAnniversaryEnabled )
    {
      dataMap.put( "noServiceAnniversary", "TRUE" );
    }

    CelebrationManagerMessage celebrationManagerMessage = null;
    if ( recClaim.getCelebrationManagerMessage() != null )
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new CelebrationManagerMessageAssociationRequest( CelebrationManagerMessageAssociationRequest.CELEBRATION_MANAGER ) );
      associationRequestCollection.add( new CelebrationManagerMessageAssociationRequest( CelebrationManagerMessageAssociationRequest.CELEBRATION_MANAGER_ABOVE ) );
      celebrationManagerMessage = celebrationService.getCelebrationManagerMessageById( recClaim.getCelebrationManagerMessage().getId(), associationRequestCollection );
    }

    if ( serviceAnniversaryEnabled )
    {
      if ( recognitionPromotion.getAnniversaryInYears() )
      {
        dataMap.put( "anniversaryYear", recClaim.getAnniversaryNumberOfYears().toString() );
      }
      else
      {
        dataMap.put( "anniversaryDay", recClaim.getAnniversaryNumberOfDays().toString() );
      }
    }
    if ( celebrationManagerMessage != null )
    {
      String managerMessage = celebrationManagerMessage.getManagerMessage();
      if ( StringUtils.isNotEmpty( managerMessage ) )
      {
        dataMap.put( "managerMessage", "TRUE" );
        dataMap.put( "managerFirstName", celebrationManagerMessage.getManager().getFirstName() );
        dataMap.put( "managerLastName", celebrationManagerMessage.getManager().getLastName() );
        dataMap.put( "managerMessageText", managerMessage );
      }

      String managerAboveMessage = celebrationManagerMessage.getManagerAboveMessage();
      if ( StringUtils.isNotEmpty( managerAboveMessage ) )
      {
        dataMap.put( "managerAboveMessage", "TRUE" );
        dataMap.put( "managerAboveFirstName", celebrationManagerMessage.getManagerAbove().getFirstName() );
        dataMap.put( "managerAboveLastName", celebrationManagerMessage.getManagerAbove().getLastName() );
        dataMap.put( "managerAboveMessageText", managerAboveMessage );
      }
    }
    dataMap.put( "celebrationLink", getCelebrationPageUrl( claimId, recipient.getId(), sendToUser ) );

    if ( recognitionPromotion.getVideoPath() != null )
    {
      String mediaName = recognitionPromotion.getVideoPath().getCode();
      String celebrationCorporateVideoUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/videos/celebration/" + mediaName + "/"
          + mediaName;
      dataMap.put( "celebrationCorporateVideoUrl", celebrationCorporateVideoUrl );
    }
    if ( recognitionPromotion.isAwardActive() )
    {
      dataMap.put( "award", "TRUE" );
    }

    Long displayPeriod = recognitionPromotion.getCelebrationDisplayPeriod();
    if ( displayPeriod != null )
    {
      String expireDateStr = getCelebrationExpireDateForRecipient( recipient, displayPeriod, localeCode );
      dataMap.put( "expireDate", expireDateStr );
    }
  }

  private String getCelebrationPageUrl( Long claimId, Long recipientId, User sendToUser )
  {
    Map<String, String> paramMap = new HashMap<String, String>();
    paramMap.put( "claimId", claimId.toString() );
    paramMap.put( "recipientId", recipientId.toString() );
    if ( Objects.nonNull( sendToUser ) )
    {
      paramMap.put( "sendToUserId", sendToUser.getId().toString() );
    }
    String celebrationPageUrl = ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                                      "/celebration/celebrationPage.do",
                                                                      paramMap );
    return celebrationPageUrl;
  }

  private String getMerchLevelPageUrl( Long claimId, Long recipientId, String giftCodeEncrypted )
  {
    Map<String, String> paramMap = new HashMap<String, String>();
    paramMap.put( "claimId", claimId.toString() );
    paramMap.put( "recipientId", recipientId.toString() );
    paramMap.put( "giftCodeEncrypted", giftCodeEncrypted );
    String celebrationPageUrl = ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                                      "/merchLevelShopping.do?method=shopOnline&gc=" + giftCodeEncrypted,
                                                                      paramMap );
    return celebrationPageUrl;
  }

  private MailingRecipient buildMailingRecipient( User user, Set mailingRecipientDataSet, String previewEmail )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    if ( user != null )
    {
      mailingRecipient.setUser( user );
    }
    if ( previewEmail != null )
    {
      mailingRecipient.setPreviewEmailAddress( previewEmail );
      mailingRecipient.setLocale( systemVariableService.getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal() );
    }
    else
    {
      mailingRecipient.setLocale( user.getLanguageType() != null ? user.getLanguageType().getCode() : systemVariableService.getDefaultLanguage().getStringVal() );
    }

    if ( mailingRecipientDataSet != null )
    {
      mailingRecipient.addMailingRecipientDataSet( mailingRecipientDataSet );
    }

    return mailingRecipient;
  }

  //
  // get destination email address out of a MailingRecipient.
  // first try preview email address then user primary email address
  // note that preview email address is also used for sendCardSelectionEmail & replaceGiftCode
  // the latter uses the preview email address to override the user email address
  // hence the need to first look at preview email address
  //
  private String getMailingRecipientEmailAddress( MailingRecipient mailingRecipient )
  {
    if ( mailingRecipient == null )
    {
      return null;
    }

    String emailAddress = mailingRecipient.getPreviewEmailAddress();
    if ( StringUtils.isNotBlank( emailAddress ) )
    {
      return emailAddress;
    }

    if ( mailingRecipient.getUser() == null )
    {
      return null;
    }

    /*
     * for Password Reset and Forgot username functionality - emails may be sent to non-primary
     * addresses
     */
    if ( mailingRecipient.getAlternateEmailAddrId() != null )
    {
      UserEmailAddress email = getUserService().getUserEmailAddressById( mailingRecipient.getAlternateEmailAddrId() );
      if ( null != email )
      {
        return email.getEmailAddr();
      }
    }

    try
    {
      UserEmailAddress primaryUserEmailAddress = null;
      primaryUserEmailAddress = mailingRecipient.getUser().getPrimaryEmailAddress();
      if ( primaryUserEmailAddress == null )
      {
        return null;
      }

      return primaryUserEmailAddress.getEmailAddr();
    }
    catch( Exception e )
    {
      // unlikely but possible - no email addresses configured
      log.error( "An error occured while submitMailing." + "No email address for mailingId: " + mailingRecipient.getMailing().getId(), e );
      return null;
    }

  }

  private String getMailingRecipientSMSAddress( Mailing mailing, User mailingRecipientUser, boolean sendError )
  {

    if ( mailingRecipientUser != null )
    {
      UserEmailAddress smsAddress = mailingRecipientUser.getTextMessageAddress();
      if ( smsAddress != null )
      {
        try
        {
          return mailingRecipientUser.getTextMessageAddress().getEmailAddr();
        }
        catch( Exception e )
        {
          // unlikely but possible - no email addresses configured
          log.error( "An error occured while submitMailing." + "No email address for mailingId: " + mailing.getId(), e );
          return null;
        }
      }
    }

    return null;

  }

  private String getMailingRecipientMobilePhone( User mailingRecipientUser )
  {
    String mobileNumber = null;
    UserPhone mobilePhone = getUserService().getUserPhone( mailingRecipientUser.getId(), PhoneType.MOBILE );
    if ( mobilePhone != null )
    {
      mobileNumber = mobilePhone.getPhoneNbr();
    }
    return mobileNumber;
  }

  private void mailSMSMessage( EmailHeader emailHeader,
                               EmailBody textBody,
                               Mailing mailing,
                               MailingRecipient mailingRecipient,
                               boolean logMailing,
                               StringBuffer htmlErrorLog,
                               StringBuffer textErrorLog )
  {

    mailMessage( emailHeader, textBody, null, mailing, mailingRecipient, true, logMailing, htmlErrorLog, textErrorLog );

  }

  private void sendSmsMessage( Mailing mailing, String subject, MailingRecipient mailingRecipient, String messageText, boolean logSendAttempt )
  {
    if ( messageText == null || messageText.trim().length() == 0 )
    {
      return;
    }

    UserPhone userPhone = getUserService().getUserPhone( mailingRecipient.getUser().getId(), PhoneType.MOBILE );
    if ( userPhone == null )
    {
      return;
    }
    boolean success = false;
    // this check is added to prevent sms message submit to the manager, when the manager is sending
    // recognition to his/her teammates and checked the checkbox "Copy to Me".
    // We need only copy of email is submited to the manager not the sms.
    if ( mailing.getMessage().getMessageTypeCode() != null && mailing.getMessage().getMessageTypeCode().getCode().equals( MessageType.RECOGNITION_RECEIVED ) )
    {
      if ( mailingRecipient.getClaimRecipientId() != null )
      {
        ClaimRecipient claimRecipient = this.claimDAO.getClaimRecipientById( mailingRecipient.getClaimRecipientId() );
        if ( claimRecipient != null && mailingRecipient.getUser().getId().equals( claimRecipient.getRecipient().getId() ) )
        {
          success = sendSmsMessage( mailingRecipient.getUser().getId(), userPhone.getCountryPhoneCode(), userPhone.getPhoneNbr(), messageText );
        }
      }
    }
    else
    {
      success = sendSmsMessage( mailingRecipient.getUser().getId(), userPhone.getCountryPhoneCode(), userPhone.getPhoneNbr(), messageText );
    }
    if ( success )
    {
      mailingRecipient.setSMSDateSent( new Timestamp( System.currentTimeMillis() ) );
    }

    if ( logSendAttempt )
    {
      mailing.setMessageTypeSMS( true );
      saveCommLog( mailing, subject, mailingRecipient, messageText, !success, messageText );
    }
  }

  @Override
  public boolean sendSmsMessage( Long userId, String destinationCountryAbbreviation, String destination, String message )
  {
    User user = getUserService().getUserById( userId );
    if ( user == null )
    {
      return false;
    }

    if ( StringUtils.isEmpty( destination ) || StringUtils.isEmpty( message ) )
    {
      return false;
    }

    // check the phone to see if SMS is even enabled
    Country country = getCountryService().getCountryByCode( destinationCountryAbbreviation );
    if ( null == country || !country.isSmsCapable() )
    {
      return false;
    }

    String applicationCode = systemVariableService.getPropertyByName( SystemVariableService.MESSENGER_APPLICATION_CODE ).getStringVal();

    // get the recipient's campaign number
    String campaignNumber = null;
    if ( user != null && user.getPrimaryAddress() != null && user.getPrimaryAddress().getAddress() != null && user.getPrimaryAddress().getAddress().getCountry() != null )
    {
      campaignNumber = user.getPrimaryAddress().getAddress().getCountry().getCampaignNbr();
    }
    else if ( Objects.nonNull( user.getPrimaryPhone() ) )
    {
      campaignNumber = getCountryService().getCountryByCode( user.getPrimaryPhone().getCountryPhoneCode() ).getCampaignNbr();
    }

    // get the DestinationCountry from the country abbreviation
    DestinationCountry destinationCountry = destinationCountryCache.getDestinationCountryFor( destinationCountryAbbreviation );
    if ( destinationCountry == null )
    {
      StringBuilder sb = new StringBuilder();
      sb.append( "\n\n**********************************************************************************" );
      sb.append( "\n* WARNING: destinationCountryCache did not return a country \n* for destinationCountryAbbreviation: " ).append( destinationCountryAbbreviation );
      sb.append( "\n**********************************************************************************\n\n" );

      log.error( sb.toString() );

      return false;
    }

    if ( StringUtils.isEmpty( campaignNumber ) )
    {
      return false;
    }

    boolean errors = false;
    SendMessageResult result = smsMessageSender.sendMessage( applicationCode, campaignNumber, destinationCountry, destination, message );
    if ( result.isSuccess() )
    {
      errors = false;
      StringBuilder body = new StringBuilder();
      body.append( "Recipient user ID: " ).append( userId ).append( "\n" );
      body.append( "Destination Mobile Number: " ).append( destination ).append( "\n" );
      body.append( "Message Text: " ).append( message ).append( "\n" );
      body.append( "Result Class: " ).append( result.getClass().getName() );
      body.append( "Message Sent Successfully! " );
      log.debug( body.toString() );
    }
    else
    {
      errors = true;
      StringBuilder body = new StringBuilder();
      body.append( "Recipient user ID: " ).append( userId ).append( "\n" );
      body.append( "Destination Mobile Number: " ).append( destination ).append( "\n" );
      body.append( "Message Text: " ).append( message ).append( "\n" );
      body.append( "Result Class: " ).append( result.getClass().getName() );
      if ( InvalidClientApplicationCodeResult.class.equals( result.getClass() ) )
      {
        body.append( "\nMessage: Double check the Messenger service to that is is configured to allow this application.  Check this app's 'messenger.app.code' system variable to make sure it's using the right client application code." );
      }
      else if ( NumberBlacklistedResult.class.equals( result.getClass() ) || NumberSuspendedResult.class.equals( result.getClass() ) )
      {
        // number is blacklisted; remove all text message preferences
        getParticipantService().updateTextMessagePreferences( userId, null );
      }
      else if ( SendMessageFailure.class.equals( result.getClass() ) )
      {
        SendMessageFailure failure = (SendMessageFailure)result;
        body.append( "\n\nFailure Code : " ).append( failure.getFailureCode() );
        body.append( "\nFailure Text : " ).append( failure.getFailureText() );
        body.append( "\nSubscriberResultText: " ).append( failure.getSubscriberText() );
      }

      log.error( body.toString() );
    }

    return !errors;
  }

  private void mailMessage( EmailHeader emailHeader,
                            EmailBody textBody,
                            EmailBody htmlBody,
                            Mailing mailing,
                            MailingRecipient mailingRecipient,
                            boolean smsMessage,
                            boolean logMailing,
                            StringBuffer htmlErrorLog,
                            StringBuffer textErrorLog )
  {
    mailMessage( emailHeader, textBody, htmlBody, mailing, mailingRecipient, smsMessage, logMailing, htmlErrorLog, textErrorLog, textBody.getBodyText(), htmlBody.getBodyText() );
  }

  private void mailMessage( EmailHeader emailHeader,
                            EmailBody textBody,
                            EmailBody htmlBody,
                            Mailing mailing,
                            MailingRecipient mailingRecipient,
                            boolean smsMessage,
                            boolean logMailing,
                            StringBuffer htmlErrorLog,
                            StringBuffer textErrorLog,
                            String textMessageCommLog,
                            String htmlBodyCommLog )
  {

    boolean errors = false;
    try
    {
      emailService.sendMessage( emailHeader, textBody, htmlBody, mailing.getMailingAttachmentInfos() );

      if ( smsMessage )
      {
        mailing.setMessageTypeSMS( true );
        mailingRecipient.setSMSDateSent( new Timestamp( System.currentTimeMillis() ) );
      }
      else
      {
        mailingRecipient.setDateSent( new Timestamp( System.currentTimeMillis() ) );
      }

      if ( log.isDebugEnabled() )
      {
        log.debug( buildMailLogString( emailHeader.getPersonal(),
                                       emailHeader.getSender(),
                                       emailHeader.getRecipients(),
                                       emailHeader.getSubject(),
                                       textBody.getBodyText(),
                                       mailing.getMailingType().getName(),
                                       mailing.getMailingAttachmentInfos() ) );
      }
    }
    catch( ServiceErrorExceptionNoRollback e )
    {
      log.error( "Error occurred while attempting to send message to recipient.", e );
      if ( htmlErrorLog != null && textErrorLog != null && logMailing )
      {
        buildErrorLogString( mailingRecipient.getUser(),
                             " - Error occurred while attempting to send message to recipient. " + "Exception:" + ExceptionUtils.getStackTrace( e.getCause() ),
                             htmlErrorLog,
                             textErrorLog );
      }
      errors = true;
    }
    finally
    {
      if ( logMailing )
      {
        saveCommLog( mailing, emailHeader.getSubject(), mailingRecipient, htmlBodyCommLog, errors, textMessageCommLog );
      }
    }
  }

  private Mailing sendPMErrorMailing( String subject, String htmlBody, String textBody )
  {

    try
    {

      return submitSystemMailing( subject, htmlBody, textBody );
    }
    catch( Exception e )
    {
      log.error( "*** Error sending system e-mail *** Attempted message follows:\n " + subject + "\n" + subject + "\n\n" + "TextBody \n" + textBody + "\n\n" + "Html Body\n" + htmlBody );
    }

    return null;
  }

  private String buildErrorMailingSubject( Mailing mailing )
  {
    return "[" + mailing.getMailingType().getName() + " MailingId:" + mailing.getId() + "] Mailing Issues";
  }

  private Mailing saveAndScheduleMailing( Mailing mailing, Long runByUserId )
  {
    try
    {
      // save mailing
      mailing = this.saveMailing( mailing );

      if ( null == mailing.getMailingBatch() )
      {
        // schedule job
        Process process = processService.createOrLoadSystemProcess( MailingProcess.PROCESS_NAME, MailingProcess.BEAN_NAME );
        LinkedHashMap parameterValueMap = new LinkedHashMap();
        parameterValueMap.put( "mailingId", new String[] { mailing.getId().toString() } );

        ProcessSchedule processSchedule = new ProcessSchedule();
        processSchedule.setStartDate( mailing.getDeliveryDate() );
        processSchedule.setTimeOfDayMillis( new Long( 0 ) );

        processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.ONE_TIME_ONLY ) );

        processService.scheduleProcess( process, processSchedule, parameterValueMap, runByUserId );
      }

      return mailing;
    }
    catch( Exception e )
    {
      log.error( "An error occured method saveAndScheduleMailing." + "For mailingId: " + mailing.getId() );
      e.printStackTrace();
      return null;
    }

  }

  /**
   * Save comm log for every user comm log is associated with
   * 
   * @param mailing
   * @param mailingRecipient
   * @param renderedMessage
   * @param error
   */
  private void saveCommLog( Mailing mailing, String subject, MailingRecipient mailingRecipient, String renderedMessage, boolean error, String plainMessage )
  {

    CommLog commLog = new CommLog();

    Date mailingDate = com.biperf.core.utils.DateUtils.getDateFromTimeStamp( mailing.getDeliveryDate() );
    Date currentDate = new Date();

    if ( mailing.getMailingType().getCode().equals( MailingType.PROMOTION ) || mailing.getMailingType().getCode().equals( MailingType.WELCOME_LOGIN_EMAIL )
        || mailing.getMailingType().getCode().equals( MailingType.WELCOME_PASSWORD_EMAIL ) || mailing.getMailingType().getCode().equals( MailingType.WELCOME_BOTH_EMAIL )
        || mailing.getMailingType().getCode().equals( MailingType.PROCESS_EMAIL ) || mailing.getMailingType().getCode().equals( MailingType.RESEND_AND_LOG )
        || com.biperf.core.utils.DateUtils.getZeroTimeDate( mailingDate ).compareTo( com.biperf.core.utils.DateUtils.getZeroTimeDate( currentDate ) ) == 0 )
    {
      commLog.setCommLogCategoryType( CommLogCategoryType.lookup( CommLogCategoryType.PROMO_MAILING ) );
      commLog.setCommLogSourceType( CommLogSourceType.lookup( CommLogSourceType.SYS_GEN ) );
      commLog.setCommLogStatusType( CommLogStatusType.lookup( CommLogStatusType.CLOSED_CODE ) );
    }
    else
    {

      commLog.setCommLogCategoryType( CommLogCategoryType.lookup( CommLogCategoryType.PROMO_MAILING ) );
      commLog.setCommLogSourceType( CommLogSourceType.lookup( CommLogSourceType.SYS_GEN ) );
      commLog.setCommLogStatusType( CommLogStatusType.lookup( CommLogStatusType.OPEN_CODE ) );
    }
    if ( mailing.isMessageTypeSMS() )
    {
      commLog.setMessageType( CommLog.MESSAGE_TYPE_SMS );
    }
    else
    {
      commLog.setMessageType( CommLog.MESSAGE_TYPE_EMAIL );
    }
    commLog.setGuid( GuidUtils.generateGuid() );
    commLog.setCommLogReasonType( getCommLogReasonType( mailing, error ) );
    commLog.setCommLogUrgencyType( CommLogUrgencyType.lookup( CommLogUrgencyType.NORMAL_CODE ) );
    commLog.setUser( mailingRecipient.getUser() );

    commLog.setDateInitiated( new Timestamp( new Date().getTime() ) );

    commLog.setMailing( mailing );
    commLog.setMessage( renderedMessage );
    commLog.setPlainMessage( plainMessage );
    commLog.setSubject( subject );

    commLogService.saveCommLog( commLog );

  }

  private CommLogReasonType getCommLogReasonType( Mailing mailing, boolean error )
  {
    if ( mailing.getMailingType().getCode().equals( MailingType.EMAIL_WIZARD ) )
    {
      if ( !error )
      {
        return CommLogReasonType.lookup( CommLogReasonType.EMAIL_WIZARD );
      }
      return CommLogReasonType.lookup( CommLogReasonType.EMAIL_WIZARD_FAILURE );

    }
    else if ( mailing.getMailingType().getCode().equals( MailingType.PROMOTION ) || mailing.getMailingType().getCode().equals( MailingType.WELCOME_LOGIN_EMAIL )
        || mailing.getMailingType().getCode().equals( MailingType.WELCOME_PASSWORD_EMAIL ) || mailing.getMailingType().getCode().equals( MailingType.WELCOME_BOTH_EMAIL ) )
    {
      if ( !error )
      {
        return CommLogReasonType.lookup( CommLogReasonType.EMAIL_PROMOTION );
      }
      return CommLogReasonType.lookup( CommLogReasonType.EMAIL_PROMOTION_FAILURE );

    }
    else
    {
      if ( !error )
      {
        return CommLogReasonType.lookup( CommLogReasonType.EMAIL_OTHER );
      }
      return CommLogReasonType.lookup( CommLogReasonType.EMAIL_OTHER_FAILURE );
    }
  }

  private String buildMailLogString( String personal, String sender, String[] recipients, String subject, String body, String type, Set mailingAttachmentInfo )
  {

    StringBuffer buffer = new StringBuffer();
    buffer.append( "\n\n" );
    buffer.append( "Email Type: " ).append( type ).append( "\n" );
    buffer.append( "Begin E-mail...........................................\n" );
    buffer.append( "Sender: " ).append( personal ).append( " " ).append( sender ).append( "\n" );
    if ( recipients.length > 0 )
    {
      buffer.append( "Recipients: " ).append( recipients[0] );
      if ( recipients.length > 1 )
      {
        buffer.append( "  [More..." ).append( String.valueOf( recipients.length ) ).append( "]" );
      }
      buffer.append( "\n" );
    }
    else
    {
      buffer.append( "Recipients: NONE" );
    }

    buffer.append( "Subject: " ).append( subject ).append( "\n" );
    buffer.append( "\n" );
    buffer.append( "BODY\n" );
    buffer.append( "Body: " ).append( body ).append( "\n" );

    if ( !mailingAttachmentInfo.isEmpty() )
    {
      buffer.append( "\n\n" );
      Iterator iter = mailingAttachmentInfo.iterator();
      while ( iter.hasNext() )
      {
        MailingAttachmentInfo attachmentInfo = (MailingAttachmentInfo)iter.next();
        buffer.append( "Attachment:" ).append( attachmentInfo.getFullFileName() ).append( "\n" );
      }
      buffer.append( "\n" );
    }
    buffer.append( "End E-mail.............................................\n" );
    buffer.append( "\n\n" );

    return buffer.toString();
  }

  private void buildErrorLogString( User user, String issue, StringBuffer htmlErrorLogString, StringBuffer textErrorLogString )
  {

    htmlErrorLogString.append( buildErrorLogString( user, issue, "<p/>" ) );
    textErrorLogString.append( buildErrorLogString( user, issue, "/n" ) );

  }

  private String buildErrorLogString( User user, String issue, String terminator )
  {
    String userId = "No User";
    String userFirstName = "No";
    String userLastName = "User";

    if ( user != null )
    {
      userId = user.getId().toString();
      userFirstName = user.getFirstName();
      userLastName = user.getLastName();
    }

    return " UserId: " + userId + " " + userFirstName + " " + userLastName + issue + terminator;
  }

  private String getSystemEmailAddress()
  {
    String systemEmailAddress = systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS ).getStringVal();
    PropertySetItem additionalEmailAddressPropertySet = systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS_ADDL );

    if ( additionalEmailAddressPropertySet != null )
    {
      String additionalEmailAddress = additionalEmailAddressPropertySet.getStringVal();
      if ( StringUtils.isNotBlank( additionalEmailAddress ) )
      {
        return systemEmailAddress + ',' + additionalEmailAddress;
      }
    }
    return systemEmailAddress;
  }

  private String getSystemIncentiveEmailAddress()
  {
    String systemIncentiveEmailAddress = systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS_INCENTIVE ).getStringVal();

    return systemIncentiveEmailAddress;
  }

  /**
   * Get prefix if include subject prefix is true
   * 
   * @return String
   */
  private String getSubjectPrefix()
  {

    boolean displaySubjectPrefix = false;

    String subjectPrefix = systemVariableService.getPropertyByName( SystemVariableService.SUBJECT_PREFIX ).getStringVal();

    displaySubjectPrefix = systemVariableService.getPropertyByName( SystemVariableService.SUBJECT_PREFIX_DISPLAY ).getBooleanVal();

    if ( displaySubjectPrefix && subjectPrefix != null )
    {
      return subjectPrefix;
    }

    return "";

  }

  /**
   * Submit mailing - this is the main mailing method. Mailing object has to built with
   * mailingRecipients and also have message. Additionally: MailingRecipients need to have any
   * recipient specific data that needs to be populated. ObjectMap needs to have any mailing level
   * data that needs to be populated. Mail is sent synchronously without scheduling. 
   * 
   * @param mailing
   * @param mailingLevelPersonalizationData
   * @return
   */
  @Override
  public Mailing submitMailingWithoutScheduling( Mailing mailing, Map mailingLevelPersonalizationData )
  {
    try
    {
      // set mailing level data have to have mailing guid set for mailingMessageLocales
      Mailing prePersonalizedMailing = personalizationService.preProcessMailing( mailing, mailingLevelPersonalizationData );

      // save mailing
      mailing = this.saveMailing( prePersonalizedMailing );

    }
    catch( Exception e )
    {
      log.error( "An error occured while saving message." + "For mailingId: " + mailing.getId(), e );
    }
    return mailing;
  }

  @Override
  public boolean isBatchEmailEnabled()
  {
    PropertySetItem prop = systemVariableService.getPropertyByName( SystemVariableService.EMAIL_BATCH_ENABLE );
    return null == prop ? false : prop.getBooleanVal();
  }

  @Override
  public MailingBatch createMailingBatch( MailingBatch mailingBatch )
  {
    return mailingBatchDAO.saveMailingBatch( mailingBatch );
  }

  /**
   * Set MailingDAO
   * 
   * @param mailingDAO
   */
  public void setMailingDAO( MailingDAO mailingDAO )
  {
    this.mailingDAO = mailingDAO;
  }

  /**
   * Set CommLogService
   * 
   * @param commLogService
   */
  public void setCommLogService( CommLogService commLogService )
  {
    this.commLogService = commLogService;
  }

  /**
   * Set ProcessService
   * 
   * @param processService
   */
  public void setProcessService( ProcessService processService )
  {
    this.processService = processService;
  }

  /**
   * Set PersonalizationService
   * 
   * @param personalizationService
   */
  public void setPersonalizationService( PersonalizationService personalizationService )
  {
    this.personalizationService = personalizationService;
  }

  /**
   * Set SystemVariableService
   * 
   * @param systemVariableService
   */
  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  /**
   * Set Email Service
   * 
   * @param emailService
   */
  public void setEmailService( EmailService emailService )
  {
    this.emailService = emailService;
  }

  /**
   * Set MessageService
   * 
   * @param messageService
   */
  public void setMessageService( MessageService messageService )
  {
    this.messageService = messageService;
  }

  /**
   * @param user
   * @return String
   */
  @Override
  public String getEmailCssForUser( User user )
  {
    String css = "";

    String designTheme = designThemeService.getDesignTheme( UserManager.getUser() );

    StringBuffer styleSheetPath = new StringBuffer();
    styleSheetPath.append( DesignThemeService.SKINS_FOLDER );
    styleSheetPath.append( designTheme );
    styleSheetPath.append( DesignThemeService.STYLES_FOLDER + "email.css" );

    ServletContext servletContext = ApplicationContextFactory.getContentManagerServletContext();

    if ( log.isDebugEnabled() )
    {
      log.debug( "Calling getResourceAsStream with argument: " + styleSheetPath.toString() );
    }

    InputStream is = null;
    if ( null != servletContext )
    {
      is = servletContext.getResourceAsStream( styleSheetPath.toString() );
    }
    else
    {
      log.warn( "servletContext was null " );
    }

    if ( is == null )
    {
      log.warn( "Called getResourceAsStream with argument: " + styleSheetPath.toString() );
      log.warn( "InputStream was null " );
    }

    if ( is == null )
    {
      return css;
    }

    String line = null;
    try
    {
      BufferedReader br = new BufferedReader( new InputStreamReader( is ) );

      while ( ( line = br.readLine() ) != null )
      {
        if ( line != null && line.trim().length() > 0 )
        {
          css += line;
        }
      }
      br.close();
    }
    catch( IOException e )
    {
      log.error( "Error reading email.css file.", e );
      return null;
    }
    finally
    {
      try
      {
        if ( is != null )
        {
          is.close();
        }
      }
      catch( IOException e )
      {
        log.error( e.getMessage(), e );
      }
    }

    String domain = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

    css = css.replaceAll( "\\$\\{domain\\}", domain );

    return css;
  }

  @Override
  public String getEmailClientLogo()
  {
    String emailHeaderUrl = "";

    String defaultDesignTheme = designThemeService.getDesignTheme( UserManager.getUser() );

    String emailHeaderImage = designThemeService.getSkinContentByKey( defaultDesignTheme, DesignThemeService.EMAIL_LOGO );

    if ( emailHeaderImage == null )
    {
      return emailHeaderUrl;
    }

    String urlPrefix = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

    emailHeaderUrl = urlPrefix + DesignThemeService.SKINS_FOLDER + defaultDesignTheme + DesignThemeService.IMAGES_FOLDER + emailHeaderImage;

    return emailHeaderUrl;
  }

  @Override
  public String getEmailBodyPhoto()
  {
    String emailPhoto = "";
    String defaultDesignTheme = designThemeService.getDesignTheme( UserManager.getUser() );

    String recognitionPhoto = designThemeService.getSkinContentByKey( defaultDesignTheme, DesignThemeService.EMAIL_BACKGROUND );
    if ( recognitionPhoto == null )
    {
      return emailPhoto;
    }

    String urlPrefix = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

    emailPhoto = urlPrefix + DesignThemeService.SKINS_FOLDER + defaultDesignTheme + DesignThemeService.IMAGES_FOLDER + recognitionPhoto;

    return emailPhoto;
  }

  private String getDesignTheme()
  {
    return designThemeService.getDesignTheme( UserManager.getUser() );
  }

  /**
   * Sets DesignThemeService.
   * 
   * @param designThemeService
   */
  public void setDesignThemeService( DesignThemeService designThemeService )
  {
    this.designThemeService = designThemeService;
  }

  /**
   * Sets NodeDAO.
   * 
   * @param nodeDAO
   */
  public void setNodeDAO( NodeDAO nodeDAO )
  {
    this.nodeDAO = nodeDAO;
  }

  /**
   * Bean location through BeanLocator look-up.
   * 
   * @param beanName
   * @return SAO
   */
  protected static SAO getService( String beanName )
  {
    return (SAO)BeanLocator.getBean( beanName );
  }

  /**
   * Gets PromotionService.
   * 
   * @return PromotionService
   */
  protected static PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  /**
   * Gets BudgetMasterService.
   * 
   * @return BudgetMasterService
   */
  protected static BudgetMasterService getBudgetMasterService()
  {
    return (BudgetMasterService)getService( BudgetMasterService.BEAN_NAME );
  }

  // BugFix 17935 Format the base value withthe given Unit at the mentioned Position
  private String formatBasevalue( String inputBase, GoalQuestPromotion gqPromo, Locale locale )
  {
    StringBuffer formatBase = new StringBuffer( "" );
    String baseUnit = ChallengepointUtil.getBaseUnitText( gqPromo.getBaseUnit(), locale );
    if ( baseUnit != null && baseUnit.trim().length() > 0 )
    {
      if ( gqPromo.getBaseUnitPosition() != null && gqPromo.getBaseUnitPosition().getCode() != null && gqPromo.getBaseUnitPosition().getCode().equals( "before" ) )
      {
        formatBase.append( baseUnit.replace( "&nbsp;", " " ) );// fix 37320 $ sign
        formatBase.append( " " );
        formatBase.append( inputBase );

      }
      if ( gqPromo.getBaseUnitPosition() != null && gqPromo.getBaseUnitPosition().getCode() != null && gqPromo.getBaseUnitPosition().getCode().equals( "after" ) )
      {
        formatBase.append( inputBase );
        formatBase.append( " " );
        formatBase.append( baseUnit.replace( "&nbsp;", " " ) );
      }
    }
    else
    {
      formatBase.append( inputBase );
    }
    return formatBase.toString();
  }

  // BugFix 17935 Format the base value withthe given Unit at the mentioned Position
  private String formatCPBasevalue( String inputBase, ChallengePointPromotion cpPromo, Locale locale )
  {
    StringBuffer formatBase = new StringBuffer( "" );
    String baseUnit = ChallengepointUtil.getBaseUnitText( cpPromo.getBaseUnit(), locale );
    if ( baseUnit != null && baseUnit.trim().length() > 0 )
    {
      if ( cpPromo.getBaseUnitPosition() != null && BaseUnitPosition.UNIT_BEFORE.equals( cpPromo.getBaseUnitPosition().getCode() ) )
      {
        formatBase.append( baseUnit );
        formatBase.append( " " );
        formatBase.append( inputBase );

      }
      if ( cpPromo.getBaseUnitPosition() != null && BaseUnitPosition.UNIT_AFTER.equals( cpPromo.getBaseUnitPosition().getCode() ) )
      {
        formatBase.append( inputBase );
        formatBase.append( " " );
        formatBase.append( baseUnit );
      }
    }
    else
    {
      formatBase.append( inputBase );
    }
    return formatBase.toString();
  }

  public MerchOrderService getMerchOrderService()
  {
    return (MerchOrderService)BeanLocator.getBean( MerchOrderService.BEAN_NAME );
  }

  public CashCurrencyService getCashCurrencyService()
  {
    return (CashCurrencyService)BeanLocator.getBean( CashCurrencyService.BEAN_NAME );
  }

  public ChallengepointAwardDAO getChallengepointAwardDAO()
  {
    return challengepointAwardDAO;
  }

  public void setChallengepointAwardDAO( ChallengepointAwardDAO challengepointAwardDAO )
  {
    this.challengepointAwardDAO = challengepointAwardDAO;
  }

  public void setSmsMessageSender( SmsMessageSender smsMessageSender )
  {
    this.smsMessageSender = smsMessageSender;
    this.destinationCountryCache = new DestinationCountryCache( this.smsMessageSender );
  }

  public MobileNotificationService getMobileNotificationService()
  {
    return (MobileNotificationService)BeanLocator.getBean( MobileNotificationService.BEAN_NAME );
  }

  /**
   * Bug # 51917
   * Since the injection is by lookup-method as defined in application context spring will
   * inject the bean. This method will be overridden by spring while creating the bean.
   * return null
   */
  protected ParticipantService getParticipantService()
  {
    return null;
  }

  /**
   * Bug # 51917
   * Since the injection is by lookup-method as defined in application context spring will
   * inject the bean. This method will be overridden by spring while creating the bean.
   * return null
   */
  protected UserService getUserService()
  {
    return null;
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

  /**
   * Bug # 51917
   * Since the injection is by lookup-method as defined in application context spring will
   * inject the bean. This method will be overridden by spring while creating the bean.
   * return null
   */
  public GamificationService getGamificationService()
  {
    return null;
  }

  public NodeService getNodeService()
  {
    return null;
  }

  public SSIContestParticipantService getSSIContestParticipantService()
  {
    return null;
  }

  public AudienceService getAudienceService()
  {
    return null;
  }

  public void setMailingBatchDAO( MailingBatchDAO mailingBatchDAO )
  {
    this.mailingBatchDAO = mailingBatchDAO;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  @Override
  public List getMailingsForBatchId( Long batchId )
  {
    // load mailing from database
    List mailingBatch = mailingBatchDAO.getMailingsForBatchId( batchId );
    return mailingBatch;
  }

  @Override
  public MailingBatch getMailingBatch( Long batchId )
  {
    // load mailing from database
    MailingBatch mailingBatch = mailingBatchDAO.getMailingBatchById( batchId );
    return mailingBatch;
  }

  @Override
  public void updateMailingBatch( Long batchId )
  {
    if ( batchId != null )
    {
      // load mailing from database
      MailingBatch mailingBatch = mailingBatchDAO.getMailingBatchById( batchId );
      mailingBatchDAO.saveMailingBatch( mailingBatch );
    }

  }

  /**
   * Check for Email Batch Enabled and Add description to Mailing Batch and create a new MailingBatch.
   * 
   * @param processName
   * 
   * @return MailingBatch
   */
  @Override
  public MailingBatch applyBatch( String processName )
  {
    // BatchEmail
    // Check for batch Email is Enabled?
    if ( isBatchEmailEnabled() )
    {
      MailingBatch mailingBatch = new MailingBatch();
      mailingBatch.setDescription( processName );
      return createMailingBatch( mailingBatch );
    }
    // BatchEmail

    return null;
  }

  public Map buildBadgeHtmlString( List<ParticipantBadge> paxBadges )
  {
    StringBuilder paxBadgesEarnedStr = new StringBuilder( "" );
    StringBuilder paxBadgesProgressStr = new StringBuilder( "" );
    Iterator badgeItr = paxBadges.iterator();
    String badgeImageUrl = "";
    Map paxBadgeMap = new HashMap();
    paxBadgesEarnedStr.append( "<br />" );
    while ( badgeItr.hasNext() )
    {
      ParticipantBadge paxBadge = (ParticipantBadge)badgeItr.next();
      BadgeRule rule = paxBadge.getBadgeRule();
      String languageCode = "";
      if ( paxBadge.getParticipant().getLanguageType() != null )
      {
        languageCode = paxBadge.getParticipant().getLanguageType().getCode();
      }
      else
      {
        languageCode = systemVariableService.getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();
      }

      List earnedNotEarnedImageList = getGamificationService().getEarnedNotEarnedImageList( rule.getBadgeLibraryCMKey(), LocaleUtils.getLocale( languageCode ) );
      Iterator itr = earnedNotEarnedImageList.iterator();
      String siteUrlPrefix = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

      int recordExist = getGamificationService().canCreateJournal( paxBadge.getParticipant().getId(),
                                                                   rule.getId(),
                                                                   rule.getBadgePromotion().getId(),
                                                                   rule.getBadgePromotion().getBadgeType().getCode() );

      if ( recordExist > 1 )
      {
        paxBadgeMap.put( "pointsEarnedBefore", true );
      }
      else
      {
        if ( paxBadge.getIsEarned() )
        {
          paxBadgeMap.put( "badgePoints", rule.getBadgePoints() );
        }

        paxBadgeMap.put( "badgeMediaType", rule.getBadgePromotion().getAwardType().getAbbr() );
        paxBadgeMap.put( "pointsEarnedBefore", false );
      }

      paxBadgeMap.put( "siteUrlPrefix", siteUrlPrefix );
      while ( itr.hasNext() )
      {
        BadgeLibrary badgeLib = (BadgeLibrary)itr.next();
        badgeImageUrl = siteUrlPrefix + "/" + badgeLib.getEarnedImageLarge();
      }

      String localeBasedBadgeNameFromCM = cmAssetService.getString( rule.getBadgeName(), BadgeRule.BADGE_RULES_CMASSET_TYPE_KEY, LocaleUtils.getLocale( languageCode ), true );

      if ( paxBadge.getBadgePromotion().getBadgeType().getCode().equalsIgnoreCase( BadgeType.PROGRESS ) && !paxBadge.getIsEarned() )
      {
        Long progressNumerator = 0L;
        Long progressDenominator = 0L;
        String badgeCountType = paxBadge.getBadgePromotion().getBadgeCountType().getCode();

        if ( badgeCountType != null && badgeCountType.equalsIgnoreCase( "given" ) )
        {
          progressNumerator = paxBadge.getSentCount();
        }
        else if ( badgeCountType != null && badgeCountType.equalsIgnoreCase( "received" ) )
        {
          progressNumerator = paxBadge.getReceivedCount();
        }
        else if ( badgeCountType != null && badgeCountType.equalsIgnoreCase( "total" ) )
        {
          progressNumerator = paxBadge.getSentCount() + paxBadge.getReceivedCount();
        }
        progressDenominator = paxBadge.getBadgeRule().getMaximumQualifier();
        int progressStatus = new Double( progressNumerator * 100 / progressDenominator ).intValue();
        int progressRemaining = 100 - progressStatus;
        paxBadgesProgressStr.append( "<table class='badge-item earned-true' id='badge'>" );
        paxBadgesProgressStr.append( "<tr><td style='text-align:center'><img src='" + badgeImageUrl + "' align='middle' width='248px'/></td></tr>" );
        paxBadgesProgressStr.append( "<tr><td class='badge-name' style='text-align:center; color: #ffffff; font-size: 30px;'>" + localeBasedBadgeNameFromCM );
        paxBadgesProgressStr.append( "</td></tr><tr><td><table class='progress' width='100%' style='border: 1px solid #666;'><tr>" );
        paxBadgesProgressStr.append( "<td class='bar'  style='width:" + progressStatus + "%;background: #ccc;' width='" + progressStatus + "%'>" );
        paxBadgesProgressStr.append( progressNumerator + "/" + progressDenominator );
        paxBadgesProgressStr.append( "</td>" );
        paxBadgesProgressStr.append( "<td style='width:" + progressRemaining + "%;' width='" + progressRemaining + "%'> </td></tr>" );
        paxBadgesProgressStr.append( "</table></td></tr>" );
        paxBadgesProgressStr.append( "</table>" );
      }
      else
      {
        paxBadgesEarnedStr.append( "<table class='badge-item earned-true' id='badge'>" );
        paxBadgesEarnedStr.append( "<tr><td style='text-align:center'><img src='" + badgeImageUrl + "' align='middle' width='248px'/></td></tr>" );
        paxBadgesEarnedStr.append( "<tr><td class='badge-name' style='text-align:center; color: #ffffff; font-size: 30px;'>" );
        paxBadgesEarnedStr.append( localeBasedBadgeNameFromCM );
        paxBadgesEarnedStr.append( "</td></tr></table>" );
      }
    }
    paxBadgeMap.put( "paxBadgeEarnedString", paxBadgesEarnedStr );
    paxBadgeMap.put( "paxBadgeProgressString", paxBadgesProgressStr );

    return paxBadgeMap;
  }

  public Map buildBehaviorBadgeHtmlString( List<ParticipantBadge> paxBadges )
  {
    StringBuilder paxBadgesEarnedStr = new StringBuilder( "" );
    Iterator badgeItr = paxBadges.iterator();
    String badgeImageUrl = "";
    Map paxBadgeMap = new HashMap();
    paxBadgesEarnedStr.append( "<br />" );
    while ( badgeItr.hasNext() )
    {
      ParticipantBadge paxBadge = (ParticipantBadge)badgeItr.next();
      BadgeRule rule = paxBadge.getBadgeRule();
      String languageCode = "";
      if ( paxBadge.getParticipant().getLanguageType() != null )
      {
        languageCode = paxBadge.getParticipant().getLanguageType().getCode();
      }
      else
      {
        languageCode = systemVariableService.getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();
      }

      List earnedNotEarnedImageList = getGamificationService().getEarnedNotEarnedImageList( rule.getBadgeLibraryCMKey(), LocaleUtils.getLocale( languageCode ) );
      Iterator itr = earnedNotEarnedImageList.iterator();
      String siteUrlPrefix = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

      int recordExist = getGamificationService().canCreateJournal( paxBadge.getParticipant().getId(),
                                                                   rule.getId(),
                                                                   rule.getBadgePromotion().getId(),
                                                                   rule.getBadgePromotion().getBadgeType().getCode() );

      if ( recordExist > 1 )
      {
        paxBadgeMap.put( "pointsEarnedBefore", true );
      }
      else
      {
        if ( paxBadge.getIsEarned() )
        {
          paxBadgeMap.put( "badgePoints", rule.getBadgePoints() );
        }

        paxBadgeMap.put( "badgeMediaType", rule.getBadgePromotion().getAwardType().getAbbr() );
        paxBadgeMap.put( "pointsEarnedBefore", false );
      }

      paxBadgeMap.put( "siteUrlPrefix", siteUrlPrefix );
      while ( itr.hasNext() )
      {
        BadgeLibrary badgeLib = (BadgeLibrary)itr.next();
        badgeImageUrl = siteUrlPrefix + "/" + badgeLib.getEarnedImageLarge();
      }

      String localeBasedBadgeNameFromCM = cmAssetService.getString( rule.getBadgeName(), BadgeRule.BADGE_RULES_CMASSET_TYPE_KEY, LocaleUtils.getLocale( languageCode ), true );

      paxBadgesEarnedStr.append( "<table class='badge-item earned-true' id='badge'>" );
      paxBadgesEarnedStr.append( "<tr><td style='text-align:center'><img src='" + badgeImageUrl + "' align='middle' width='248px'/></td></tr>" );
      paxBadgesEarnedStr.append( "<tr><td class='badge-name' style='text-align:center; color: #ffffff; font-size: 30px;'>" );
      paxBadgesEarnedStr.append( localeBasedBadgeNameFromCM );
      paxBadgesEarnedStr.append( "</td></tr></table>" );
    }
    paxBadgeMap.put( "paxBadgeEarnedString", paxBadgesEarnedStr );

    return paxBadgeMap;
  }

  public Map buildTDBadgeHtmlString( BadgeDetails badgeDetail )
  {
    StringBuilder paxBadgesEarnedStr = new StringBuilder( "" );
    String badgeImageUrl = "";
    Map paxBadgeMap = new HashMap();

    paxBadgesEarnedStr.append( "<br />" );
    String siteUrlPrefix = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    badgeImageUrl = siteUrlPrefix + "/" + badgeDetail.getImg();
    paxBadgesEarnedStr.append( "<br />" );
    paxBadgesEarnedStr.append( "<p>" );
    paxBadgesEarnedStr.append( "<img src='" + badgeImageUrl + "'/>" );
    paxBadgesEarnedStr.append( "</p><p>" );
    paxBadgesEarnedStr.append( badgeDetail.getBadgeName() + "</p>" );

    paxBadgeMap.put( "paxBadgeEarnedString", paxBadgesEarnedStr );
    return paxBadgeMap;
  }

  public Map<String, Object> buildNominationBadgeHtmlString( List<ParticipantBadge> paxBadges )
  {
    StringBuilder paxBadgesEarnedStr = new StringBuilder( "" );
    StringBuilder paxBadgesBehaviorStr = new StringBuilder( "" );
    Iterator<ParticipantBadge> badgeItr = paxBadges.iterator();
    Map<String, Object> paxBadgeMap = new HashMap<>();

    paxBadgesEarnedStr.append( "<br />" );

    while ( badgeItr.hasNext() )
    {
      String badgeImageUrl = "";
      ParticipantBadge paxBadge = badgeItr.next();
      BadgeRule rule = paxBadge.getBadgeRule();
      String languageCode = "";
      if ( paxBadge.getParticipant().getLanguageType() != null )
      {
        languageCode = paxBadge.getParticipant().getLanguageType().getCode();
      }
      else
      {
        languageCode = systemVariableService.getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();
      }
      String localeBasedBadgeNameFromCM = cmAssetService.getString( rule.getBadgeName(), BadgeRule.BADGE_RULES_CMASSET_TYPE_KEY, LocaleUtils.getLocale( languageCode ), true );
      String siteUrlPrefix = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
      List earnedNotEarnedImageList = getGamificationService().getEarnedNotEarnedImageList( rule.getBadgeLibraryCMKey(), LocaleUtils.getLocale( languageCode ) );
      Iterator itr = earnedNotEarnedImageList.iterator();
      while ( itr.hasNext() )
      {
        BadgeLibrary badgeLib = (BadgeLibrary)itr.next();
        badgeImageUrl = siteUrlPrefix + "/" + badgeLib.getEarnedImageLarge();
      }

      if ( paxBadge.getBadgePromotion().getBadgeType().getCode().equalsIgnoreCase( BadgeType.EARNED_OR_NOT_EARNED ) )
      {
        paxBadgesEarnedStr.append( "<center><table class='badge-item earned-true' id='badge'>" );
        paxBadgesEarnedStr.append( "<tr><td style='text-align:center'><img src='" + badgeImageUrl + "' align='middle' width='248px'/></td></tr>" );
        paxBadgesEarnedStr.append( "<tr><td class='badge-name' style='text-align:center; color: #ffffff; font-size: 30px;'>" );
        paxBadgesEarnedStr.append( localeBasedBadgeNameFromCM );
        paxBadgesEarnedStr.append( "</td></tr></table></center>" );
      }
      else if ( paxBadge.getBadgePromotion().getBadgeType().getCode().equalsIgnoreCase( BadgeType.BEHAVIOR ) )
      {
        paxBadgesBehaviorStr.append( "<center><table class='badge-item earned-true' id='badge'>" );
        paxBadgesBehaviorStr.append( "<tr><td style='text-align:center'><img src='" + badgeImageUrl + "' align='middle' width='248px'/></td></tr>" );
        paxBadgesBehaviorStr.append( "<tr><td class='badge-name' style='text-align:center; color: #ffffff; font-size: 30px;'>" );
        paxBadgesBehaviorStr.append( localeBasedBadgeNameFromCM );
        paxBadgesBehaviorStr.append( "</td></tr></table></center>" );
      }
    }
    paxBadgeMap.put( "paxBadgeEarnedString", paxBadgesEarnedStr );
    paxBadgeMap.put( "paxBadgeBehaviorString", paxBadgesBehaviorStr );

    return paxBadgeMap;
  }

  @Override
  public String getShortUrl( String url )
  {
    return getShortUrl( url, null );
  }

  /*
   * NOTE: if a title is NOT passed to the service, the service will proactively call the URL passed
   * in to try and obtain the page title. This is not desireable in many cases.
   */
  @Override
  public String getShortUrl( String url, String title )
  {
    String returnUrl = "";
    String initialUrl = "";
    String signature = "";
    String xmlString = "";
    BufferedReader br = null;
    try
    {
      initialUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.URLSHORTNER_URL ).getStringVal();
      signature = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.URLSHORTNER_SIGNATURE ).getStringVal();
      StringBuilder sb = new StringBuilder();
      sb.append( initialUrl );
      sb.append( "?signature=" + signature );
      if ( !StringUtils.isBlank( title ) )
      {
        String encodedTitle = URLEncoder.encode( title, "UTF-8" );
        sb.append( "&title=" + encodedTitle );
      }
      sb.append( "&action=shorturl&url=" + url );
      URL shortUrl = new URL( sb.toString() );

      URLConnection uc = shortUrl.openConnection( Environment.buildProxy() );

      br = new BufferedReader( new InputStreamReader( uc.getInputStream() ) );
      xmlString = br.readLine();
      br.close();
      DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      InputSource is = new InputSource();
      is.setCharacterStream( new StringReader( xmlString ) );

      Document doc = db.parse( is );
      NodeList nodes = doc.getElementsByTagName( "result" );

      for ( int i = 0; i < nodes.getLength(); i++ )
      {
        Element element = (Element)nodes.item( i );

        NodeList name = element.getElementsByTagName( "shorturl" );
        Element line = (Element)name.item( 0 );
        returnUrl = getCharacterDataFromElement( line );
      }
    }
    catch( Exception e )
    {
      returnUrl = "";
      log.error( "Url we are trying to shorten is: " + url, e );
      log.error( "Error occurred getting shortened url:" + e );
    }
    finally
    {
      try
      {
        if ( br != null )
        {
          br.close();
        }
      }
      catch( IOException ex )
      {
        log.error( "Error while closing br connection in finally block: " + ex );
      }
    }
    return returnUrl;
  }

  public static String getCharacterDataFromElement( Element e )
  {
    org.w3c.dom.Node child = e.getFirstChild();
    if ( child instanceof CharacterData )
    {
      CharacterData cd = (CharacterData)child;
      return cd.getData();
    }
    return "";
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.email.MailingService#buildRecognitionMailing(com.biperf.core.domain.claim.RecognitionClaim,
   *      com.biperf.core.domain.participant.Participant)
   * @param claim
   * @param recipient
   * @return Mailing
   */
  @Override
  public Mailing buildRecognitionCelebrationMailing( RecognitionClaim claim, Participant recipient, boolean isPurl )
  {
    if ( !claim.isAddPointsClaim() )
    {
      Message message = null;

      Promotion promotion = claim.getPromotion();
      if ( promotion.getPromotionNotifications().size() > 0 )
      {
        Iterator notificationsIter = promotion.getPromotionNotifications().iterator();
        while ( notificationsIter.hasNext() )
        {
          PromotionNotification notification = (PromotionNotification)notificationsIter.next();
          if ( notification.isPromotionNotificationType() )
          {
            PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notification;
            long messageId = promotionNotificationType.getNotificationMessageId();

            String notificationTypeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();

            if ( isPurl )
            {
              if ( messageId > 0 && notificationTypeCode.equals( PromotionEmailNotificationType.CELEBRATION_PURL_RECIPIENT_INVITATION ) )
              {
                message = messageService.getMessageById( messageId );
                break;
              }
            }
            else
            {
              if ( messageId > 0 && notificationTypeCode.equals( PromotionEmailNotificationType.CELEBRATION_RECOGNITION_RECEIVED ) )
              {
                message = messageService.getMessageById( messageId );
                break;
              }
            }
          }
        }
      }
      Mailing mailing = buildEmails( MailingType.PROMOTION, getSystemIncentiveEmailAddress(), message, false );

      Set recipients = buildMailingRecipientsForRecognitionEmail( claim, recipient, null );

      mailing.addMailingRecipients( recipients );
      return mailing;
    }
    return null;
  }

  public void setClaimDAO( ClaimDAO claimDAO )
  {
    this.claimDAO = claimDAO;
  }

  public void setTranslationService( TranslationService translationService )
  {
    this.translationService = translationService;
  }

  public void setCelebrationService( CelebrationService celebrationService )
  {
    this.celebrationService = celebrationService;
  }

  private static final class DestinationCountryCache
  {
    private final SmsMessageSender smsMessageSender;

    private long lastRefresh = 0L;
    private static final long TIME_TO_LIVE = 60 /* minutes/hour */ * 60 /* seconds/minute */
        * 1000 /* milliseconds/second */ * 1 /* hour */;

    private Map<String, DestinationCountry> destinationCountries;

    public DestinationCountryCache( SmsMessageSender smsMessageSender )
    {
      this.smsMessageSender = smsMessageSender;
    }

    public DestinationCountry getDestinationCountryFor( String countryAbbrev )
    {
      if ( countryAbbrev == null || countryAbbrev.trim().length() == 0 )
      {
        return null;
      }

      DestinationCountry country = null;
      synchronized ( this )
      {
        refreshContent();

        country = destinationCountries.get( countryAbbrev );
      }

      if ( country == null )
      {
        synchronized ( this )
        {
          // iterate through the countries and attempt to find a match in case
          // the Map uses a different key from what is passed in.
          for ( DestinationCountry c : destinationCountries.values() )
          {
            if ( c.matches( countryAbbrev ) )
            {
              country = c;
              break;
            }
          }
        }
      }

      return country;
    }

    private void refreshContent()
    {
      if ( isContentExpired() )
      {
        getRefreshedContent();
      }
    }

    private void getRefreshedContent()
    {
      destinationCountries = new HashMap<String, DestinationCountry>();

      // get the stuff...
      List<DestinationCountry> countries = smsMessageSender.findDestinationCountries();

      if ( countries == null || countries.isEmpty() )
      {
        StringBuilder sb = new StringBuilder();
        sb.append( "\n\n**********************************************************************************" );
        sb.append( "\n* WARNING: smsMessageSender.findDestinationCountries() returned no countries" );
        sb.append( "\n**********************************************************************************\n\n" );

        log.error( sb.toString() );
      }

      if ( countries != null )
      {
        for ( DestinationCountry country : countries )
        {
          // g5 stores the country code as lower case, so use lower case here too as the key
          destinationCountries.put( country.getIsoAlpha2Code().toLowerCase(), country );
        }
      }

      // reset the lastRefresh
      lastRefresh = System.currentTimeMillis();
    }

    private boolean isContentExpired()
    {
      if ( System.currentTimeMillis() - lastRefresh > TIME_TO_LIVE )
      {
        return true;
      }
      return false;
    }
  }

  private String getAfterUnitLabel( ThrowdownPromotion promotion )
  {
    if ( promotion.getBaseUnit() != null && promotion.getBaseUnitPosition() != null && promotion.getBaseUnitPosition().getCode().equals( BaseUnitPosition.UNIT_AFTER ) )
    {
      return promotion.getBaseUnitText();
    }
    else
    {
      return "";
    }
  }

  private String getBeforeUnitLabel( ThrowdownPromotion promotion )
  {
    if ( promotion.getBaseUnit() != null && promotion.getBaseUnitPosition() != null && promotion.getBaseUnitPosition().getCode().equals( BaseUnitPosition.UNIT_BEFORE ) )
    {
      return promotion.getBaseUnitText();
    }
    else
    {
      return "";
    }
  }

  @Override
  public int reScheduleEmailFailures( Date startDate, Date endDate, Long runByUserId )
  {
    List mailingIds = mailingDAO.getFailedMailingIds( startDate, endDate );
    log.error( "no of ids is " + mailingIds.size() );
    int totalEmailsPushedThru = 0;
    Iterator iter = mailingIds.iterator();
    while ( iter.hasNext() )
    {
      Long mailingId = (Long)iter.next();
      Timestamp deliveryDate = new Timestamp( com.biperf.core.utils.DateUtils.getCurrentDate().getTime() );
      // schedule job
      Process process = processService.createOrLoadSystemProcess( MailingProcess.PROCESS_NAME, MailingProcess.BEAN_NAME );

      LinkedHashMap parameterValueMap = new LinkedHashMap();
      parameterValueMap.put( "mailingId", new String[] { mailingId.toString() } );

      ProcessSchedule processSchedule = new ProcessSchedule();
      processSchedule.setStartDate( deliveryDate );
      processSchedule.setTimeOfDayMillis( new Long( 0 ) );

      processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.ONE_TIME_ONLY ) );

      processService.scheduleProcess( process, processSchedule, parameterValueMap, runByUserId );
      totalEmailsPushedThru++;
    }
    log.error( "****totalEmailsPushedThru=" + totalEmailsPushedThru );
    return totalEmailsPushedThru;
  }

  @Override
  public Mailing buildContestProgressLoadCreatorMailing( SSIContest ssiContest,
                                                         Participant creator,
                                                         SSIContestUniqueCheckValueBean contestUniqueCheckValueBean,
                                                         List<SSIContestProgressValueBean> contestProgressData,
                                                         List<SSIContestStackRankPaxValueBean> ssiContestStackRankPaxValueBeanList,
                                                         SSIPromotion ssiPromotion,
                                                         String isSSIAdmin )
  {
    Mailing mailing = null;
    if ( ssiPromotion.getPromotionNotifications().size() > 0 )
    {
      Iterator notificationsIter = ssiPromotion.getPromotionNotifications().iterator();
      while ( notificationsIter.hasNext() )
      {
        PromotionNotification notification = (PromotionNotification)notificationsIter.next();
        if ( notification.isPromotionNotificationType() )
        {
          PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notification;
          long messageId = promotionNotificationType.getNotificationMessageId();
          // Process only when a notification has been set up on the promotion

          String notificationTypeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();

          if ( messageId > 0
              && ( notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_OBJECTIVES ) && SSIContestType.OBJECTIVES.equals( ssiContest.getContestType().getCode() )
                  || notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_DO_THIS_GET_THAT )
                      && SSIContestType.DO_THIS_GET_THAT.equals( ssiContest.getContestType().getCode() )
                  || notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_STEP_IT_UP )
                      && SSIContestType.STEP_IT_UP.equals( ssiContest.getContestType().getCode() )
                  || notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_CREATOR_STACK_RANK )
                      && SSIContestType.STACK_RANK.equals( ssiContest.getContestType().getCode() ) ) )
          {
            // Send Creator or Manager notification
            mailing = buildContestProgressLoadCreatorManagerNotificationMailing( messageId,
                                                                                 ssiContest,
                                                                                 creator,
                                                                                 contestUniqueCheckValueBean,
                                                                                 contestProgressData,
                                                                                 ssiContestStackRankPaxValueBeanList,
                                                                                 isSSIAdmin );
          }
        }
      }
    }
    return mailing;
  }

  @Override
  public Mailing buildContestProgressLoadManagerMailing( SSIContest ssiContest,
                                                         Participant manager,
                                                         SSIContestUniqueCheckValueBean contestUniqueCheckValueBean,
                                                         List<SSIContestProgressValueBean> contestProgressData,
                                                         List<SSIContestStackRankPaxValueBean> ssiContestStackRankPaxValueBeanList,
                                                         SSIPromotion ssiPromotion )
  {
    Mailing mailing = null;
    if ( ssiPromotion.getPromotionNotifications().size() > 0 )
    {
      Iterator notificationsIter = ssiPromotion.getPromotionNotifications().iterator();
      while ( notificationsIter.hasNext() )
      {
        PromotionNotification notification = (PromotionNotification)notificationsIter.next();
        if ( notification.isPromotionNotificationType() )
        {
          PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notification;
          long messageId = promotionNotificationType.getNotificationMessageId();
          // Process only when a notification has been set up on the promotion

          String notificationTypeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();

          if ( messageId > 0
              && ( notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_OBJECTIVES ) && SSIContestType.OBJECTIVES.equals( ssiContest.getContestType().getCode() )
                  || notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_DO_THIS_GET_THAT )
                      && SSIContestType.DO_THIS_GET_THAT.equals( ssiContest.getContestType().getCode() )
                  || notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_STEP_IT_UP ) && SSIContestType.STEP_IT_UP.equals( ssiContest.getContestType().getCode() )
                  || notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_MGR_STACK_RANK )
                      && SSIContestType.STACK_RANK.equals( ssiContest.getContestType().getCode() ) ) )
          {
            // Send Creator or Manager notification
            mailing = buildContestProgressLoadCreatorManagerNotificationMailing( messageId,
                                                                                 ssiContest,
                                                                                 manager,
                                                                                 contestUniqueCheckValueBean,
                                                                                 contestProgressData,
                                                                                 ssiContestStackRankPaxValueBeanList,
                                                                                 null );
          }
        }
      }
    }
    return mailing;
  }

  @Override
  public Mailing buildContestProgressLoadCompleteCreatorMailing( SSIContest ssiContest,
                                                                 Participant creator,
                                                                 Long importFileId,
                                                                 String fileName,
                                                                 int totalRecords,
                                                                 int totalProcessedRecords,
                                                                 SSIPromotion ssiPromotion,
                                                                 String isSSIAdmin )
  {
    Mailing mailing = null;
    if ( ssiPromotion.getPromotionNotifications().size() > 0 )
    {
      Iterator notificationsIter = ssiPromotion.getPromotionNotifications().iterator();
      while ( notificationsIter.hasNext() )
      {
        PromotionNotification notification = (PromotionNotification)notificationsIter.next();
        if ( notification.isPromotionNotificationType() )
        {
          PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notification;
          long messageId = promotionNotificationType.getNotificationMessageId();
          // Process only when a notification has been set up on the promotion

          String notificationTypeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();

          if ( messageId > 0 && notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_PROGRESS_STATUS_NOTIFY_CREATOR ) )
          {
            mailing = buildContestProgressLoadCompleteNotificationMailing( messageId, ssiContest, creator, importFileId, fileName, totalRecords, totalProcessedRecords, isSSIAdmin );
          }
        }
      }
    }
    return mailing;
  }

  private Mailing buildContestProgressLoadCompleteNotificationMailing( long messageId,
                                                                       SSIContest ssiContest,
                                                                       Participant creator,
                                                                       Long importFileId,
                                                                       String fileName,
                                                                       int totalRecords,
                                                                       int totalProcessedRecords,
                                                                       String isSSIAdmin )
  {
    Set mailingRecipients = buildMailingRecipientsForContestProgressLoadCompleteEmail( ssiContest, creator, importFileId, fileName, totalRecords, totalProcessedRecords, isSSIAdmin );
    if ( mailingRecipients.size() == 0 )
    {
      return null;
    }
    Mailing mailing = buildEmails( MailingType.PROMOTION, getSystemIncentiveEmailAddress(), messageService.getMessageById( messageId ), false );
    mailing.addMailingRecipients( mailingRecipients );
    return mailing;
  }

  private Mailing buildContestProgressLoadCreatorManagerNotificationMailing( long messageId,
                                                                             SSIContest ssiContest,
                                                                             Participant creatorOrManager,
                                                                             SSIContestUniqueCheckValueBean contestUniqueCheckValueBean,
                                                                             List<SSIContestProgressValueBean> contestProgressData,
                                                                             List<SSIContestStackRankPaxValueBean> ssiContestStackRankPaxValueBeanList,
                                                                             String isSSIAdmin )
  {
    Set mailingRecipients = buildMailingRecipientsForContestProgressLoadCreatorManagerEmail( ssiContest,
                                                                                             creatorOrManager,
                                                                                             contestUniqueCheckValueBean,
                                                                                             contestProgressData,
                                                                                             ssiContestStackRankPaxValueBeanList,
                                                                                             isSSIAdmin );
    if ( mailingRecipients.size() == 0 )
    {
      return null;
    }
    Mailing mailing = buildEmails( MailingType.PROMOTION, getSystemIncentiveEmailAddress(), messageService.getMessageById( messageId ), false );
    mailing.addMailingRecipients( mailingRecipients );
    return mailing;
  }

  private Set buildMailingRecipientsForContestProgressLoadCompleteEmail( SSIContest ssiContest,
                                                                         Participant creator,
                                                                         Long importFileId,
                                                                         String fileName,
                                                                         int totalRecords,
                                                                         int totalProcessedRecords,
                                                                         String isSSIAdmin )
  {
    Set mailingRecipients = new HashSet();

    Set recipients = new HashSet();
    recipients.add( creator );

    for ( Iterator recipientIter = recipients.iterator(); recipientIter.hasNext(); )
    {
      Participant recipient = (Participant)recipientIter.next();
      MailingRecipient mailingRecipient = null;
      if ( SSIContestUtil.canAddSSIAdminInEMailNotificxations( ssiContest.getId() ) != null && isSSIAdmin.equals( "true" ) )
      {
        mailingRecipient = buildContestProgressLoadCompleteMailingRecipientData( ssiContest, creator, importFileId, fileName, totalRecords, totalProcessedRecords, false, true );
      }
      else
      {
        mailingRecipient = buildContestProgressLoadCompleteMailingRecipientData( ssiContest, creator, importFileId, fileName, totalRecords, totalProcessedRecords, false, false );
      }

      mailingRecipients.add( mailingRecipient );
    }
    MailingRecipient mailingSSIAdminRecipient = null;
    if ( SSIContestUtil.canAddSSIAdminInEMailNotificxations( ssiContest.getId() ) != null )
    {
      if ( isSSIAdmin.equals( "false" ) )
      {
        mailingSSIAdminRecipient = buildContestProgressLoadCompleteMailingRecipientData( ssiContest, creator, importFileId, fileName, totalRecords, totalProcessedRecords, true, false );
      }
      else
      {
        mailingSSIAdminRecipient = buildContestProgressLoadCompleteMailingRecipientData( ssiContest, creator, importFileId, fileName, totalRecords, totalProcessedRecords, false, false );
      }
      mailingSSIAdminRecipient
          .setPreviewEmailAddress( getUserService().getPrimaryUserEmailAddress( SSIContestUtil.canAddSSIAdminInEMailNotificxations( ssiContest.getId() ).getUserID() ).getEmailAddr() );
      mailingSSIAdminRecipient.setUser( null );
      mailingRecipients.add( mailingSSIAdminRecipient );
    }

    return mailingRecipients;
  }

  private Set buildMailingRecipientsForContestProgressLoadCreatorManagerEmail( SSIContest ssiContest,
                                                                               Participant creatorOrManager,
                                                                               SSIContestUniqueCheckValueBean contestUniqueCheckValueBean,
                                                                               List<SSIContestProgressValueBean> contestProgressData,
                                                                               List<SSIContestStackRankPaxValueBean> ssiContestStackRankPaxValueBeanList,
                                                                               String isSSIAdmin )
  {
    Set mailingRecipients = new HashSet();

    Set recipients = new HashSet();
    recipients.add( creatorOrManager );

    for ( Iterator recipientIter = recipients.iterator(); recipientIter.hasNext(); )
    {
      Participant recipient = (Participant)recipientIter.next();
      MailingRecipient mailingRecipient = null;
      if ( SSIContestUtil.canAddSSIAdminInEMailNotificxations( ssiContest.getId() ) != null && isSSIAdmin != null && isSSIAdmin.equals( "true" ) )
      {
        mailingRecipient = buildContestProgressLoadCreatorManagerMailingRecipientData( recipient,
                                                                                       ssiContest,
                                                                                       contestUniqueCheckValueBean,
                                                                                       contestProgressData,
                                                                                       ssiContestStackRankPaxValueBeanList,
                                                                                       false,
                                                                                       true );
      }
      else
      {
        mailingRecipient = buildContestProgressLoadCreatorManagerMailingRecipientData( recipient,
                                                                                       ssiContest,
                                                                                       contestUniqueCheckValueBean,
                                                                                       contestProgressData,
                                                                                       ssiContestStackRankPaxValueBeanList,
                                                                                       false,
                                                                                       false );
      }

      mailingRecipients.add( mailingRecipient );
      MailingRecipient mailingSSIAdminRecipient = null;
      if ( SSIContestUtil.canAddSSIAdminInEMailNotificxations( ssiContest.getId() ) != null && isSSIAdmin != null )
      {
        if ( isSSIAdmin.equals( "false" ) )
        {
          mailingSSIAdminRecipient = buildContestProgressLoadCreatorManagerMailingRecipientData( recipient,
                                                                                                 ssiContest,
                                                                                                 contestUniqueCheckValueBean,
                                                                                                 contestProgressData,
                                                                                                 ssiContestStackRankPaxValueBeanList,
                                                                                                 true,
                                                                                                 false );
        }
        else
        {
          mailingSSIAdminRecipient = buildContestProgressLoadCreatorManagerMailingRecipientData( recipient,
                                                                                                 ssiContest,
                                                                                                 contestUniqueCheckValueBean,
                                                                                                 contestProgressData,
                                                                                                 ssiContestStackRankPaxValueBeanList,
                                                                                                 false,
                                                                                                 false );
        }

        mailingSSIAdminRecipient
            .setPreviewEmailAddress( getUserService().getPrimaryUserEmailAddress( SSIContestUtil.canAddSSIAdminInEMailNotificxations( ssiContest.getId() ).getUserID() ).getEmailAddr() );
        mailingSSIAdminRecipient.setUser( null );
        mailingRecipients.add( mailingSSIAdminRecipient );
      }
    }

    return mailingRecipients;
  }

  private MailingRecipient buildContestProgressLoadCompleteMailingRecipientData( SSIContest ssiContest,
                                                                                 Participant creator,
                                                                                 Long importFileId,
                                                                                 String fileName,
                                                                                 int totalRecords,
                                                                                 int totalProcessedRecords,
                                                                                 boolean ssiAdminExtraText,
                                                                                 boolean ssiCreatorExtraText )
  {
    MailingRecipient mailingRecipient = buildContestProgressLoadMailingRecipientData( creator );
    Locale locale = CmsUtil.getLocale( mailingRecipient.getLocale() );
    String contestName = getContestName( ssiContest, locale );
    String programName = systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();
    String programUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    Map<String, Object> dataMap = new HashMap<String, Object>();

    if ( creator != null )
    {
      dataMap.put( "firstName", creator.getFirstName() );
      dataMap.put( "lastName", creator.getLastName() );
    }
    dataMap.put( "contestName", contestName );
    if ( totalRecords != 0 )
    {
      int totalFailureRecords = totalRecords - totalProcessedRecords;
      if ( totalFailureRecords == 0 )
      {
        dataMap.put( "successLoad", String.valueOf( Boolean.TRUE ) );
      }
      else if ( totalFailureRecords > 0 )
      {
        dataMap.put( "partialLoad", String.valueOf( Boolean.TRUE ) );
      }
      else if ( totalFailureRecords == totalRecords )
      {
        dataMap.put( "failureLoad", String.valueOf( Boolean.TRUE ) );
      }
      dataMap.put( "totalRecords", String.valueOf( totalRecords ) );
      dataMap.put( "totalProcessedRecords", String.valueOf( totalProcessedRecords ) );
      dataMap.put( "totalFailureRecords", String.valueOf( totalFailureRecords ) );
    }
    else
    {
      dataMap.put( "failureLoad", String.valueOf( Boolean.TRUE ) );
    }
    dataMap.put( "programName", programName );
    dataMap.put( "programUrl", programUrl );
    if ( ssiAdminExtraText )
    {
      dataMap.put( "ssiAdminEMailCopyNotes", String.valueOf( ssiAdminExtraText ) );
    }
    if ( ssiCreatorExtraText )
    {
      dataMap.put( "ssiCreatorEMailCopyNotes", String.valueOf( ssiCreatorExtraText ) );
    }
    mailingRecipient.addMailingRecipientDataFromMap( dataMap );
    return mailingRecipient;
  }

  // contest related info
  private Map populateContestProgressLoadGeneralData( Map dataMap, Participant recipient, SSIContest ssiContest, Locale locale, String contestName, int decimalPrecision )
  {
    if ( recipient != null )
    {
      dataMap.put( "firstName", recipient.getFirstName() );
      dataMap.put( "lastName", recipient.getLastName() );
    }

    Locale recipientLocale = recipient.getLanguageType() != null ? CmsUtil.getLocale( recipient.getLanguageType().getCode() ) : UserManager.getLocale();
    String endDate = com.biperf.core.utils.DateUtils.toDisplayString( ssiContest.getEndDate(), recipientLocale );
    Integer daysToEnd = com.biperf.core.utils.DateUtils.getDaysToGoFromToday( ssiContest.getEndDate() );
    boolean endDatePassed = daysToEnd != null && daysToEnd <= 0 ? Boolean.TRUE : Boolean.FALSE;
    String programName = systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();

    Date contestProgressLastUpdated = com.biperf.core.utils.DateUtils.toDate( SSIContestUtil.getContestProgressUpdateDate( ssiContest ) );
    dataMap.put( "contestProgressLastUpdated", com.biperf.core.utils.DateUtils.toDisplayString( contestProgressLastUpdated, recipientLocale ) );
    dataMap.put( "contestName", contestName );
    dataMap.put( "daysToEnd", String.valueOf( daysToEnd ) );
    dataMap.put( "contestEndDate", endDate );
    dataMap.put( "contestGoal", SSIContestUtil.getFormattedValue( ssiContest.getContestGoal(), decimalPrecision ) );
    dataMap.put( "programName", programName );

    if ( endDatePassed )
    {
      dataMap.put( "endDatePassed", String.valueOf( Boolean.TRUE ) );
    }
    if ( ssiContest.isIncludeStackRank() && ssiContest.getLastProgressUpdateDate() != null )
    {
      dataMap.put( "isIncludeStackRank", String.valueOf( Boolean.TRUE ) );
    }
    if ( ssiContest.isIncludeBonus() )
    {
      dataMap.put( "isIncludeBonus", String.valueOf( Boolean.TRUE ) );
    }
    return dataMap;
  }

  private Map populateContestProgressCreatorManagerLink( Map dataMap, Participant recipient, SSIContest ssiContest )
  {
    dataMap.put( "showCreatorDetailPageLink", String.valueOf( Boolean.TRUE ) );
    dataMap.put( "creatorDetailPageLink", SSIContestUtil.populateCreatorDetailPageUrl( ssiContest.getId(), recipient.getId() ) );
    dataMap.put( "showManagerDetailPageLink", String.valueOf( Boolean.TRUE ) );
    dataMap.put( "managerDetailPageLink", SSIContestUtil.populateManagerDetailPageUrl( ssiContest.getId(), recipient.getId() ) );
    return dataMap;
  }

  private Map populateContestProgressParticipantLink( Map dataMap, Participant recipient, SSIContest ssiContest )
  {
    dataMap.put( "showPaxDetailPageLink", String.valueOf( Boolean.TRUE ) );
    dataMap.put( "paxDetailPageLink", SSIContestUtil.populateParticipantDetailPageUrl( ssiContest.getId(), recipient.getId() ) );
    return dataMap;
  }

  private Map setContestProgressData( Map dataMap, SSIContest ssiContest, SSIContestProgressValueBean contestProgressValueBean, int decimalPrecision )
  {
    String activityDescription = ssiContest.getSameObjectiveDescription() ? ssiContest.getActivityDescription() : contestProgressValueBean.getActivityDescription();
    String goal = SSIContestUtil.getFormattedValue( contestProgressValueBean.getGoal(), decimalPrecision ) + getActivitySuffix( ssiContest );
    String totalObjectiveAmount = SSIContestUtil.getFormattedValue( contestProgressValueBean.getTotalObjectiveAmount(), decimalPrecision ) + getActivitySuffix( ssiContest );
    String progress = "";
    if ( contestProgressValueBean.getTeamActivity() != null )
    {
      progress = SSIContestUtil.getFormattedValue( contestProgressValueBean.getTeamActivity(), decimalPrecision ) + getActivitySuffix( ssiContest );
    }
    else
    {
      progress = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.generalInfo.NO_ACTIVITY_TO_DATE" );
    }
    double remaining = 0;
    String remainingFormatted = null;
    if ( contestProgressValueBean.getGoal() - contestProgressValueBean.getTeamActivity() > new Double( 0 ) )
    {
      remaining = contestProgressValueBean.getGoal() - contestProgressValueBean.getTeamActivity();
      remainingFormatted = SSIContestUtil.getFormattedValue( remaining, decimalPrecision ) + getActivitySuffix( ssiContest );
      dataMap.put( "showTotalAmountToGo", "TRUE" );
    }

    Long percentageAchieved = (long)Math.floor( contestProgressValueBean.getTeamActivity() / contestProgressValueBean.getGoal() * 100 );
    String percentProgress = percentageAchieved != null ? String.valueOf( percentageAchieved ) + SSIContestUtil.PERCENTAGE : SSIContestUtil.DEFAULT_ZERO + SSIContestUtil.PERCENTAGE;

    dataMap.put( "activityDescription", activityDescription );
    dataMap.put( "objectivesTotal", totalObjectiveAmount );
    dataMap.put( "totalPercentage", percentProgress );
    dataMap.put( "percentageToGoal", percentProgress );
    dataMap.put( "progressToDate", progress );
    dataMap.put( "totalAmountToGo", remainingFormatted );
    return dataMap;
  }

  private Map setContestProgressStepItUpData( Map dataMap, SSIContest ssiContest, SSIContestProgressValueBean contestProgressValueBean, int decimalPrecision )
  {
    String activityDescription = ssiContest.getActivityDescription();
    String goal = SSIContestUtil.getFormattedValue( contestProgressValueBean.getContestGoal(), decimalPrecision ) + getActivitySuffix( ssiContest );
    String progressToDate = "";
    if ( contestProgressValueBean.getActivity() != null )
    {
      progressToDate = SSIContestUtil.getFormattedValue( contestProgressValueBean.getActivity(), decimalPrecision ) + getActivitySuffix( ssiContest );
    }
    else
    {
      progressToDate = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.generalInfo.NO_ACTIVITY_TO_DATE" );
    }
    String remaining = SSIContestUtil.getFormattedValue( contestProgressValueBean.getTogo(), decimalPrecision ) + getActivitySuffix( ssiContest );
    Long percentageAchieved = null;
    if ( contestProgressValueBean.getActivity() != null && contestProgressValueBean.getContestGoal() != null )
    {
      percentageAchieved = (long)Math.floor( contestProgressValueBean.getActivity() / contestProgressValueBean.getContestGoal() * 100 );
    }
    String percentProgress = percentageAchieved != null ? String.valueOf( percentageAchieved ) + SSIContestUtil.PERCENTAGE : SSIContestUtil.DEFAULT_ZERO + SSIContestUtil.PERCENTAGE;

    dataMap.put( "activityDescription", activityDescription );
    dataMap.put( "progressToDate", progressToDate );
    dataMap.put( "goal", goal );
    dataMap.put( "totalPercentage", percentProgress );
    dataMap.put( "totalAmountToGo", remaining );

    return dataMap;
  }

  private Map setContestProgressStackRankData( Map dataMap, SSIContest ssiContest, SSIContestProgressValueBean contestProgressValueBean, int decimalPrecision )
  {
    String activityDescription = ssiContest.getActivityDescription();
    String goal = SSIContestUtil.getFormattedValue( contestProgressValueBean.getGoal(), decimalPrecision ) + getActivitySuffix( ssiContest );
    String progressToDate = "";
    if ( contestProgressValueBean.getProgress() != null )
    {
      progressToDate = SSIContestUtil.getFormattedValue( contestProgressValueBean.getProgress(), decimalPrecision ) + getActivitySuffix( ssiContest );
    }
    else
    {
      progressToDate = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.generalInfo.NO_ACTIVITY_TO_DATE" );
    }
    String remaining = SSIContestUtil.getFormattedValue( contestProgressValueBean.getTogo(), decimalPrecision ) + getActivitySuffix( ssiContest );
    Long percentageAchieved = (long)Math.floor( contestProgressValueBean.getProgress() / contestProgressValueBean.getGoal() * 100 );
    String percentProgress = percentageAchieved != null ? String.valueOf( percentageAchieved ) + SSIContestUtil.PERCENTAGE : SSIContestUtil.DEFAULT_ZERO + SSIContestUtil.PERCENTAGE;

    dataMap.put( "activityDescription", activityDescription );
    dataMap.put( "progressToDate", progressToDate );
    dataMap.put( "goal", goal );
    dataMap.put( "totalPercentage", percentProgress );
    dataMap.put( "totalAmountToGo", remaining );

    return dataMap;
  }

  private Map setCreatorMgrObjectiveData( Map dataMap,
                                          SSIContest ssiContest,
                                          SSIContestUniqueCheckValueBean contestUniqueCheckValueBean,
                                          List<SSIContestProgressValueBean> contestProgressData,
                                          int decimalPrecision )
  {
    if ( contestProgressData != null && contestProgressData.size() > 0 )
    {
      SSIContestProgressValueBean contestProgressValueBean = contestProgressData.get( 0 );
      if ( contestUniqueCheckValueBean != null && contestUniqueCheckValueBean.isActivityDescSame() )
      {
        dataMap.put( "isObjectiveSame", String.valueOf( Boolean.TRUE ) );
        setContestProgressData( dataMap, ssiContest, contestProgressValueBean, decimalPrecision );
      }
      dataMap.put( "participantsCount", String.valueOf( contestProgressValueBean.getTotalParticipant() ) );
      dataMap.put( "achievedParticipantsCount", String.valueOf( contestProgressValueBean.getParticipantAchieved() ) );
    }
    return dataMap;
  }

  private Map setCreatorMgrStepItUpData( Map dataMap, SSIContest ssiContest, List<SSIContestProgressValueBean> contestProgressData, int decimalPrecision )
  {
    if ( contestProgressData != null && contestProgressData.size() > 0 )
    {
      SSIContestProgressValueBean contestProgressValueBean = contestProgressData.get( 0 );
      setContestProgressStepItUpData( dataMap, ssiContest, contestProgressValueBean, decimalPrecision );
    }
    return dataMap;
  }

  private Map setCreatorMgrStackRankData( Map dataMap,
                                          SSIContest ssiContest,
                                          Locale locale,
                                          List<SSIContestProgressValueBean> contestProgressData,
                                          List<SSIContestStackRankPaxValueBean> ssiContestStackRankPaxValueBeanList,
                                          int decimalPrecision )
  {
    if ( contestProgressData != null && contestProgressData.size() > 0 )
    {
      SSIContestProgressValueBean contestProgressValueBean = contestProgressData.get( 0 );
      setContestProgressStackRankData( dataMap, ssiContest, contestProgressValueBean, decimalPrecision );
      setContestProgressStackRankData( dataMap, ssiContestStackRankPaxValueBeanList, locale, decimalPrecision );
    }
    return dataMap;
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public Map<String, Object> setCreatorMgrDoThisGetThatData( SSIContest contest, Map<String, Object> dataMap, List<SSIContestProgressValueBean> contestProgressData, int decimalPrecision )
  {
    int counter = 0;
    if ( contest.getStatus().isFinalizeResults() )
    {
      dataMap.put( "isFinalizeResults", String.valueOf( true ) );
    }
    if ( contest.getStatus().isFinalizeResults() )
    {
      if ( contest.getPayoutType().isPoints() )
      {
        dataMap.put( "isPoints", String.valueOf( true ) );
      }
    }
    for ( SSIContestProgressValueBean contestProgressValueBean : contestProgressData )
    {
      counter++;
      String activityDescription = contestProgressValueBean.getActivityDescription();
      String progressToDate = "";
      if ( contestProgressValueBean.getTeamActivity() != null )
      {
        progressToDate = SSIContestUtil.getFormattedValue( contestProgressValueBean.getTeamActivity(), decimalPrecision ) + getActivitySuffix( contest );
        dataMap.put( "progressToDate[" + counter + "]", progressToDate );
      }
      String goal = SSIContestUtil.getFormattedValue( contestProgressValueBean.getGoal(), decimalPrecision ) + getActivitySuffix( contest );
      long percentageToGoal = (long)Math.floor( contestProgressValueBean.getTeamActivity() / contestProgressValueBean.getGoal() * 100 );
      String toGo = SSIContestUtil.getFormattedValue( contestProgressValueBean.getGoal() - contestProgressValueBean.getTeamActivity(), decimalPrecision );
      dataMap.put( "description[" + counter + "]", activityDescription );
      dataMap.put( "goal[" + counter + "]", goal );
      dataMap.put( "percentageToGoal[" + counter + "]", percentageToGoal + "%" );
      if ( contest.getStatus().isFinalizeResults() )
      {
        if ( contest.getPayoutType().isPoints() )
        {
          dataMap.put( "potentialPayout[" + counter + "]", SSIContestUtil.getFormattedValue( contestProgressValueBean.getPotentialPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
        }
        else
        {
          dataMap.put( "potentialPayout[" + counter + "]",
                       SSIContestUtil.getFormattedValue( contestProgressValueBean.getPotentialPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + getPayoutSuffix( contest ) );
        }
      }
      else
      {
        dataMap.put( "potentialPayout[" + counter + "]", toGo + getActivitySuffix( contest ) );
      }
    }
    return dataMap;
  }

  private MailingRecipient buildContestProgressLoadMailingRecipientData( Participant recipient )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();

    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setUser( recipient );
    String localeCode = null;
    if ( recipient != null && recipient.getLanguageType() != null )
    {
      localeCode = recipient.getLanguageType().getCode();
    }
    else
    {
      localeCode = systemVariableService.getDefaultLanguage().getStringVal();
    }
    mailingRecipient.setLocale( localeCode );
    return mailingRecipient;
  }

  /**
   * creator or manager progress update email
   * @param participant
   * @param ssiContest
   * @param contestUniqueCheckValueBean
   * @param contestProgressData
   * @param ssiContestStackRankPaxValueBeanList
   * @return
   */
  private MailingRecipient buildContestProgressLoadCreatorManagerMailingRecipientData( Participant participant,
                                                                                       SSIContest ssiContest,
                                                                                       SSIContestUniqueCheckValueBean contestUniqueCheckValueBean,
                                                                                       List<SSIContestProgressValueBean> contestProgressData,
                                                                                       List<SSIContestStackRankPaxValueBean> ssiContestStackRankPaxValueBeanList,
                                                                                       boolean ssiAdminExtraText,
                                                                                       boolean ssiCreatorExtraText )
  {
    MailingRecipient mailingRecipient = buildContestProgressLoadMailingRecipientData( participant );
    Locale locale = CmsUtil.getLocale( mailingRecipient.getLocale() );
    int decimalPrecision = SSIContestUtil.getPrecision( ssiContest.getActivityMeasureType().getCode() );
    String contestName = getContestName( ssiContest, locale );

    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap = populateContestProgressLoadGeneralData( dataMap, participant, ssiContest, locale, contestName, decimalPrecision );
    dataMap = populateContestProgressCreatorManagerLink( dataMap, participant, ssiContest );

    if ( SSIContestType.OBJECTIVES.equals( ssiContest.getContestType().getCode() ) )
    {
      dataMap = setCreatorMgrObjectiveData( dataMap, ssiContest, contestUniqueCheckValueBean, contestProgressData, decimalPrecision );
    }
    else if ( SSIContestType.DO_THIS_GET_THAT.equals( ssiContest.getContestType().getCode() ) )
    {
      // dataMap.put("locale", locale);
      dataMap = setCreatorMgrDoThisGetThatData( ssiContest, dataMap, contestProgressData, decimalPrecision );
    }
    else if ( SSIContestType.STEP_IT_UP.equals( ssiContest.getContestType().getCode() ) )
    {
      dataMap = setCreatorMgrStepItUpData( dataMap, ssiContest, contestProgressData, decimalPrecision );
    }
    else if ( SSIContestType.STACK_RANK.equals( ssiContest.getContestType().getCode() ) )
    {
      dataMap = setCreatorMgrStackRankData( dataMap, ssiContest, locale, contestProgressData, ssiContestStackRankPaxValueBeanList, decimalPrecision );
    }
    if ( ssiAdminExtraText )
    {
      dataMap.put( "ssiAdminEMailCopyNotes", String.valueOf( ssiAdminExtraText ) );
    }
    if ( ssiCreatorExtraText )
    {
      dataMap.put( "ssiCreatorEMailCopyNotes", String.valueOf( ssiCreatorExtraText ) );
    }
    mailingRecipient.addMailingRecipientDataFromMap( dataMap );

    return mailingRecipient;
  }

  /**
   * Participant progress update email
   * @param participant
   * @param ssiContest
   * @param paxProgressValueBean
   * @param badgeImageUrlSuffix
   * @return
   */
  private MailingRecipient buildContestProgressLoadParticipantMailingRecipientData( Participant participant,
                                                                                    SSIContest ssiContest,
                                                                                    SSIContestPaxProgressDetailValueBean paxProgressValueBean,
                                                                                    String badgeImageUrlSuffix )
  {
    MailingRecipient mailingRecipient = buildContestProgressLoadMailingRecipientData( participant );
    Locale locale = CmsUtil.getLocale( mailingRecipient.getLocale() );
    int decimalPrecision = SSIContestUtil.getPrecision( ssiContest.getActivityMeasureType().getCode() );
    String contestName = getContestName( ssiContest, locale );

    Map dataMap = new HashMap();
    dataMap = populateContestProgressLoadGeneralData( dataMap, participant, ssiContest, locale, contestName, decimalPrecision );
    if ( SSIContestType.OBJECTIVES.equals( ssiContest.getContestType().getCode() ) )
    {
      dataMap = setContestProgressPaxData( dataMap, participant, ssiContest, paxProgressValueBean, decimalPrecision );
      if ( ssiContest.getPayoutType().isPoints() )
      {
        dataMap.put( KEY_IS_POINTS, String.valueOf( Boolean.TRUE ) );
        dataMap.put( KEY_PAYOUT_AMOUNT, SSIContestUtil.getFormattedValue( paxProgressValueBean.getObjectivePayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + getPayoutSuffix( ssiContest ) );
      }
      if ( ssiContest.getPayoutType().isOther() )
      {
        dataMap.put( KEY_IS_OTHER, String.valueOf( Boolean.TRUE ) );
        dataMap.put( KEY_PAYOUT_DESCRIPTION, paxProgressValueBean.getObjectivePayoutDescription() );
      }

      if ( ssiContest.getBadgeRule() != null )
      {
        String siteUrlPrefix = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
        dataMap.put( "badgeImage", "<img src=\"" + siteUrlPrefix + badgeImageUrlSuffix + "\"/>" );
        dataMap.put( "badgeName", ssiContest.getBadgeRule().getBadgeNameTextFromCM() );
      }

      if ( ssiContest.isIncludeBonus() )
      {
        dataMap.put( "bonusIncrement", SSIContestUtil.getFormattedValue( paxProgressValueBean.getObjectiveBonusIncrement(), decimalPrecision ) );
        dataMap.put( "bonusPayout", SSIContestUtil.getFormattedValue( paxProgressValueBean.getObjectiveBonusPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
      }
    }
    else if ( SSIContestType.DO_THIS_GET_THAT.equals( ssiContest.getContestType().getCode() ) )
    {
      dataMap = setContestProgressPaxData( dataMap, participant, ssiContest, paxProgressValueBean, decimalPrecision );
      dataMap.putAll( setContestProgressDoThisGetThatData( ssiContest, dataMap, paxProgressValueBean, locale ) );
    }
    else if ( SSIContestType.STEP_IT_UP.equals( ssiContest.getContestType().getCode() ) )
    {
      dataMap = setContestProgressStepItUpPaxData( dataMap, paxProgressValueBean, ssiContest, locale, decimalPrecision, participant );
    }
    else if ( SSIContestType.STACK_RANK.equals( ssiContest.getContestType().getCode() ) )
    {
      dataMap = setContestProgressStackRankPaxData( dataMap, participant, ssiContest, paxProgressValueBean, decimalPrecision );
    }

    mailingRecipient.addMailingRecipientDataFromMap( dataMap );

    return mailingRecipient;
  }

  public String getContestName( SSIContest ssiContest, Locale locale )
  {
    return cmAssetService.getString( ssiContest.getCmAssetCode(), SSIContest.CONTEST_CMASSET_NAME, locale, true );
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public Mailing buildContestProgressLoadParticipantMailing( SSIContest ssiContest,
                                                             Participant participant,
                                                             SSIContestPaxProgressDetailValueBean paxProgressValueBean,
                                                             String badgeImageUrlSuffix,
                                                             SSIPromotion ssiPromotion )
  {
    Mailing mailing = null;
    if ( ssiPromotion.getPromotionNotifications().size() > 0 )
    {
      Iterator notificationsIter = ssiPromotion.getPromotionNotifications().iterator();
      while ( notificationsIter.hasNext() )
      {
        PromotionNotification notification = (PromotionNotification)notificationsIter.next();
        if ( notification.isPromotionNotificationType() )
        {
          PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notification;
          long messageId = promotionNotificationType.getNotificationMessageId();
          // Process only when a notification has been set up on the promotion

          String notificationTypeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();

          if ( messageId > 0
              && ( notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_OBJECTIVES ) && SSIContestType.OBJECTIVES.equals( ssiContest.getContestType().getCode() )
                  || notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_DO_THIS_GET_THAT )
                      && SSIContestType.DO_THIS_GET_THAT.equals( ssiContest.getContestType().getCode() )
                  || notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_STEP_IT_UP ) && SSIContestType.STEP_IT_UP.equals( ssiContest.getContestType().getCode() )
                  || notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_PROGRESS_NOTIFY_PAX_STACK_RANK )
                      && SSIContestType.STACK_RANK.equals( ssiContest.getContestType().getCode() ) ) )
          {
            // Send Participant notification
            mailing = buildContestProgressLoadParticipantNotificationMailing( messageId, ssiContest, participant, paxProgressValueBean, badgeImageUrlSuffix );
          }
        }
      }
    }
    return mailing;
  }

  /**
   * 
   * @param messageId
   * @param ssiContest
   * @param participant
   * @param paxProgressValueBean
   * @param badgeImageUrlSuffix
   * @return
   */
  private Mailing buildContestProgressLoadParticipantNotificationMailing( long messageId,
                                                                          SSIContest ssiContest,
                                                                          Participant participant,
                                                                          SSIContestPaxProgressDetailValueBean paxProgressValueBean,
                                                                          String badgeImageUrlSuffix )
  {
    Set mailingRecipients = buildMailingRecipientsForContestProgressLoadParticipantEmail( ssiContest, participant, paxProgressValueBean, badgeImageUrlSuffix );
    if ( mailingRecipients.size() == 0 )
    {
      return null;
    }
    Mailing mailing = buildEmails( MailingType.PROMOTION, getSystemIncentiveEmailAddress(), messageService.getMessageById( messageId ), false );
    mailing.addMailingRecipients( mailingRecipients );
    return mailing;
  }

  private Set buildMailingRecipientsForContestProgressLoadParticipantEmail( SSIContest ssiContest,
                                                                            Participant participant,
                                                                            SSIContestPaxProgressDetailValueBean paxProgressValueBean,
                                                                            String badgeImageUrlSuffix )
  {
    Set mailingRecipients = new HashSet();

    Set recipients = new HashSet();
    recipients.add( participant );
    for ( Iterator recipientIter = recipients.iterator(); recipientIter.hasNext(); )
    {
      Participant recipient = (Participant)recipientIter.next();
      MailingRecipient mailingRecipient = buildContestProgressLoadParticipantMailingRecipientData( recipient, ssiContest, paxProgressValueBean, badgeImageUrlSuffix );
      mailingRecipients.add( mailingRecipient );
    }

    return mailingRecipients;
  }

  /**
   * 
   * @param dataMap
   * @param paxProgressValueBean
   * @param ssiContest
   * @param locale
   * @param decimalPrecision
   * @return
   */
  private Map setContestProgressStepItUpPaxData( Map dataMap,
                                                 SSIContestPaxProgressDetailValueBean paxProgressValueBean,
                                                 SSIContest ssiContest,
                                                 Locale locale,
                                                 int decimalPrecision,
                                                 Participant participant )
  {
    int precision = SSIContestUtil.getPrecision( ssiContest.getActivityMeasureType().getCode() );
    String nextHighestLevel = "";
    SSIPaxContestLevelValueBean levelAchieved = null;
    SSIPaxContestLevelValueBean currentLevel = null;
    SSIPaxContestLevelValueBean highestLevel = paxProgressValueBean.getLevels().get( paxProgressValueBean.getLevels().size() - 1 );
    String stackRank = paxProgressValueBean.getLevels() != null ? SSIContestUtil.HASHTAG + String.valueOf( paxProgressValueBean.getLevels().get( 0 ).getStackRank() ) : null;
    String participantCount = paxProgressValueBean.getLevels() != null ? String.valueOf( paxProgressValueBean.getLevels().get( 0 ).getParticipantsCount() ) : null;

    // filtering the current level
    boolean currentLevelFound = false;
    for ( SSIPaxContestLevelValueBean vb : paxProgressValueBean.getLevels() )
    {
      if ( currentLevelFound )
      {
        vb.setCurrentLevel( false );
      }
      if ( vb.getCurrentLevel() != null && vb.getCurrentLevel() )
      {
        currentLevelFound = true;
      }
    }

    // finding current level
    for ( SSIPaxContestLevelValueBean level : paxProgressValueBean.getLevels() )
    {
      if ( level.getCurrentLevel() )
      {
        currentLevel = level;
        break;
      }
    }

    // finding highest level achieved
    if ( currentLevel != null )
    {
      // if achieved some level
      if ( Integer.parseInt( currentLevel.getName() ) != 1 )
      {
        if ( Integer.parseInt( currentLevel.getName() ) == Integer.parseInt( highestLevel.getName() ) && currentLevel.getCompleted() )
        {
          levelAchieved = currentLevel;// or highest level achieved
        }
        else
        {
          levelAchieved = paxProgressValueBean.getLevels().get( Integer.parseInt( currentLevel.getName() ) - 2 );
        }
      }
    }

    // completed highest level
    if ( currentLevel == null && highestLevel.getCompleted() )
    {
      dataMap.put( "achievedHighestLevel", String.valueOf( Boolean.TRUE ) );
      nextHighestLevel = MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.detail.LEVEL" ), new Object[] { highestLevel.getName() } );
      String highestLevelName = MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.detail.LEVEL" ), new Object[] { highestLevel.getName() } );
      dataMap.put( "nextHighestLevelName", nextHighestLevel );
      dataMap.put( "highestLevelName", highestLevelName );
      dataMap.put( "isLevelAchieved", String.valueOf( Boolean.TRUE ) );
      dataMap.put( "levelAchievedName", highestLevelName );
    }
    else if ( levelAchieved != null )
    {
      // if atleast one level achieved
      nextHighestLevel = MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.detail.LEVEL" ), new Object[] { currentLevel.getName() } );
      String highestLevelName = MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.detail.LEVEL" ), new Object[] { highestLevel.getName() } );
      dataMap.put( "isLevelAchieved", String.valueOf( Boolean.TRUE ) );
      dataMap.put( "isLevelsRemaining", String.valueOf( Boolean.TRUE ) );
      dataMap.put( "levelAchievedName", MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.detail.LEVEL" ), new Object[] { levelAchieved.getName() } ) );
      dataMap.put( "highestLevelName", highestLevelName );
      dataMap.put( "nextHighestLevelName", nextHighestLevel );
    }
    else
    {
      // not even one level achieved, progressing towards first level
      dataMap.put( "noneAchieved", String.valueOf( Boolean.TRUE ) );
      nextHighestLevel = MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.detail.LEVEL" ),
                                               new Object[] { paxProgressValueBean.getLevels().get( 0 ).getName() } );
      dataMap.put( "nextHighestLevelName", nextHighestLevel );
    }

    // amount and progress to date
    dataMap.put( "activityDescription", ssiContest.getActivityDescription() );
    dataMap.put( "amountForNextLevel",
                 SSIContestUtil.getFormattedValue( currentLevel != null ? currentLevel.getRemaining() : paxProgressValueBean.getLevels().get( 0 ).getRemaining(), precision )
                     + getActivitySuffix( ssiContest ) );

    String progressToDate = "";
    if ( currentLevel != null )
    {
      if ( currentLevel.getProgress() != null )
      {
        progressToDate = SSIContestUtil.getFormattedValue( currentLevel.getProgress(), precision ) + getActivitySuffix( ssiContest );
      }
      else
      {
        progressToDate = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.generalInfo.NO_ACTIVITY_TO_DATE" );
      }
    }
    else
    {
      if ( highestLevel.getProgress() != null )
      {
        progressToDate = SSIContestUtil.getFormattedValue( highestLevel.getProgress(), precision ) + getActivitySuffix( ssiContest );
      }
      else
      {
        progressToDate = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.generalInfo.NO_ACTIVITY_TO_DATE" );
      }
    }
    dataMap.put( "progressToDate", progressToDate );

    String highestLevelName = MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.detail.LEVEL" ), new Object[] { highestLevel.getName() } );
    dataMap.put( "highestLevelName", highestLevelName );

    // bonus enabled add bonus values
    if ( ssiContest.isIncludeBonus() )
    {
      dataMap.put( "forEvery", SSIContestUtil.getFormattedValue( ssiContest.getStepItUpBonusIncrement(), decimalPrecision ) + getActivitySuffix( ssiContest ) );
      dataMap.put( "bonusPayout", SSIContestUtil.getFormattedValue( ssiContest.getStepItUpBonusPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + getPayoutSuffix( ssiContest ) );
    }

    // stack rank data
    if ( ssiContest.isIncludeStackRank() && ssiContest.getLastProgressUpdateDate() != null )
    {
      dataMap.put( "isIncludeStackRank", String.valueOf( Boolean.TRUE ) );
      if ( stackRank != null && participantCount != null )
      {
        dataMap.put( "individualStackRank", stackRank );
        dataMap.put( "participantCount", participantCount );
      }
    }
    dataMap.put( "programUrl", SSIContestUtil.populateParticipantDetailPageUrl( ssiContest.getId(), participant.getId() ) );
    return dataMap;
  }

  private String getActivitySuffix( SSIContest contest )
  {
    if ( contest.getActivityMeasureType().isCurrency() )
    {
      return WHITESPACE + contest.getActivityMeasureCurrencyCode().toUpperCase();
    }
    else
    {
      return "";
    }
  }

  private String getPayoutSuffix( SSIContest contest )
  {
    if ( contest.getPayoutType().isPoints() )
    {
      return WHITESPACE + contest.getPayoutType().getName();
    }
    else if ( contest.getPayoutType().isOther() )
    {
      return WHITESPACE + contest.getPayoutOtherCurrencyCode().toUpperCase();
    }
    else
    {
      return WHITESPACE;
    }
  }

  /**
   * 
   * {@inheritDoc}
   */
  @Override
  public Map<String, Object> setContestProgressDoThisGetThatData( SSIContest contest, Map<String, Object> dataMap, SSIContestPaxProgressDetailValueBean paxProgressValueBean, Locale locale )
  {
    Locale cmLocale = null;
    if ( locale != null )
    {
      cmLocale = ContentReaderManager.getCurrentLocale();
      ContentReaderManager.getContentReader().setLocale( locale );
    }

    if ( contest.getLastProgressUpdateDate() != null && contest.isIncludeStackRank() )
    {
      dataMap.put( "includeStackRank", String.valueOf( true ) );
    }
    int counter = 0;
    for ( SSIPaxDTGTActivityProgressValueBean dtgtActivityProgressValueBean : paxProgressValueBean.getActivities() )
    {
      counter++;
      String activityDescription = dtgtActivityProgressValueBean.getActivityDescription();
      String minQualifier = dtgtActivityProgressValueBean.getMinQualifier() != null ? String.valueOf( dtgtActivityProgressValueBean.getMinQualifier() ) : "";
      String progress = dtgtActivityProgressValueBean.getProgress() != null
          ? String.valueOf( dtgtActivityProgressValueBean.getProgress() ) + getActivitySuffix( contest )
          : CmsResourceBundle.getCmsBundle().getString( "ssi_contest.generalInfo.NO_ACTIVITY_TO_DATE" );
      String rank = dtgtActivityProgressValueBean.getStackRank() != null && dtgtActivityProgressValueBean.getStackRank().getRank() != null
          ? String.valueOf( dtgtActivityProgressValueBean.getStackRank().getRank() )
          : "";
      String paxCount = dtgtActivityProgressValueBean.getStackRank() != null && dtgtActivityProgressValueBean.getStackRank().getParticipantsCount() != null
          ? String.valueOf( dtgtActivityProgressValueBean.getStackRank().getParticipantsCount() )
          : "";

      // html
      dataMap.put( "description[" + counter + "]", activityDescription );
      dataMap.put( "minQualifier[" + counter + "]", minQualifier + getActivitySuffix( contest ) );
      dataMap.put( "progress[" + counter + "]", progress );
      if ( contest.getLastProgressUpdateDate() != null && contest.isIncludeStackRank() )
      {
        dataMap.put( "rank[" + counter + "]", SSIContestUtil.HASHTAG + " " + rank );
        dataMap.put( "paxCount[" + counter + "]", paxCount );
      }
    }
    if ( locale != null )
    {
      ContentReaderManager.getContentReader().setLocale( cmLocale );
      cmLocale = null;
    }
    return dataMap;
  }

  /**
   * 
   * @param dataMap
   * @param ssiContestStackRankPaxValueBeanList
   * @param locale
   * @param decimalPrecision
   * @return
   */
  private Map setContestProgressStackRankData( Map dataMap, List<SSIContestStackRankPaxValueBean> ssiContestStackRankPaxValueBeanList, Locale locale, int decimalPrecision )
  {
    String rankHeader = cmAssetService.getString( SSI_CONTEST_PAX_CM_ASSET_CODE, "RANK", locale, true );
    String amount = cmAssetService.getString( SSI_CONTEST_PAX_CM_ASSET_CODE, "ACTIVITY_AMOUNT", locale, true );
    String paxname = cmAssetService.getString( SSI_CONTEST_PAX_CM_ASSET_CODE, "PAX_NAME", locale, true );
    String hashTag = cmAssetService.getString( SSI_CONTEST_PAX_CM_ASSET_CODE, "HASHTAG", locale, true );

    if ( StringUtils.isBlank( rankHeader ) )
    {
      rankHeader = "Rank";
    }
    if ( StringUtils.isBlank( amount ) )
    {
      rankHeader = " Activity Amount";
    }
    if ( StringUtils.isBlank( paxname ) )
    {
      rankHeader = "Participant Name";
    }

    StringBuffer stackRankToppersTable = new StringBuffer();
    StringBuffer stackRankToppersPlain = new StringBuffer();

    if ( ssiContestStackRankPaxValueBeanList.size() > 0 )
    {

      stackRankToppersTable.append( "<table style=\"width:100%\" border=\"1\">" ).append( "<thead><tr>" ).append( "<th>" ).append( rankHeader ).append( "</th><th>" ).append( paxname )
          .append( "</th><th>" ).append( amount ).append( "</th>" ).append( "</tr></thead><tbody>" );

      for ( SSIContestStackRankPaxValueBean contestStackRankPaxValueBean : ssiContestStackRankPaxValueBeanList )
      {
        Integer rank = contestStackRankPaxValueBean.getRank();
        String firstName = contestStackRankPaxValueBean.getFirstName();
        String lastName = contestStackRankPaxValueBean.getLastName();
        String activityAmount = SSIContestUtil.getFormattedValue( contestStackRankPaxValueBean.getScore(), decimalPrecision );

        // html
        stackRankToppersTable.append( "<tr align=\"center\" ><td width=\"(100/3)%\">" );
        stackRankToppersTable.append( "<b>&nbsp;" ).append( hashTag ).append( String.valueOf( rank ) ).append( "</b>" );
        // stackRankToppersTable.append( LINE_BREAK );
        // stackRankToppersTable.append( "&nbsp;" );
        stackRankToppersTable.append( "</td><td width=\"(100/3)%\">" );
        stackRankToppersTable.append( firstName ).append( " " ).append( lastName );
        // stackRankToppersTable.append( LINE_BREAK );
        // stackRankToppersTable.append( "&nbsp;" );
        stackRankToppersTable.append( "</td><td width=\"(100/3)%\">" );
        stackRankToppersTable.append( activityAmount );
        stackRankToppersTable.append( "</td><tr>" );

        // text
        stackRankToppersPlain.append( hashTag ).append( String.valueOf( rank ) );
        stackRankToppersPlain.append( firstName ).append( "/" ).append( lastName );
        stackRankToppersPlain.append( activityAmount );
      }
      stackRankToppersTable.append( "</tbody>" ).append( "</table>" );
    }
    dataMap.put( "stackRankToppersTable", String.valueOf( stackRankToppersTable ) );
    dataMap.put( "stackRankToppersPlain", String.valueOf( stackRankToppersPlain ) );
    return dataMap;
  }

  /**
   * contest pax related info
   * @param dataMap
   * @param participant
   * @param ssiContest
   * @param paxProgressValueBean
   * @param decimalPrecision
   * @return
   */
  private Map setContestProgressPaxData( Map dataMap, Participant participant, SSIContest ssiContest, SSIContestPaxProgressDetailValueBean paxProgressValueBean, int decimalPrecision )
  {
    String activityDescription = ssiContest.getActivityDescription();
    Set<SSIContestParticipant> contestParticipants = ssiContest.getContestParticipants();

    SSIContestParticipant contestParticipant = (SSIContestParticipant)contestParticipants.stream().filter( p -> p.getParticipant().getId().equals( participant.getId() ) ).findAny().orElse( null );
    if ( activityDescription == null )
    {
      if ( contestParticipant != null )
      {
        activityDescription = contestParticipant.getActivityDescription();
      }
      else
      {
        activityDescription = "";
      }
    }
    String progress = "";
    if ( paxProgressValueBean.getActivityAmount() != null )
    {
      progress = SSIContestUtil.getFormattedValue( paxProgressValueBean.getActivityAmount(), decimalPrecision ) + getActivitySuffix( ssiContest );
    }
    else
    {
      progress = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.generalInfo.NO_ACTIVITY_TO_DATE" );
    }
    String goal = SSIContestUtil.getFormattedValue( paxProgressValueBean.getObjectiveAmount(), decimalPrecision ) + getActivitySuffix( ssiContest );
    String remaining = SSIContestUtil.getFormattedValue( paxProgressValueBean.getToGoAmount(), decimalPrecision ) + getActivitySuffix( ssiContest );
    String percentProgress = paxProgressValueBean.getPercentageAcheived() != null
        ? String.valueOf( paxProgressValueBean.getPercentageAcheived() ) + SSIContestUtil.PERCENTAGE
        : SSIContestUtil.DEFAULT_ZERO + SSIContestUtil.PERCENTAGE;
    String stackRank = paxProgressValueBean.getStackRank() != null ? SSIContestUtil.HASHTAG + String.valueOf( paxProgressValueBean.getStackRank() ) : null;
    String participantCount = paxProgressValueBean.getTotalPax() != null ? String.valueOf( paxProgressValueBean.getTotalPax() ) : null;
    if ( paxProgressValueBean.getToGoAmount() != null && paxProgressValueBean.getToGoAmount() > new Double( 0 ) )
    {
      dataMap.put( "showTotalAmountToGo", "TRUE" );
    }

    dataMap.put( "activityDescription", activityDescription );
    dataMap.put( "objectiveAmount", goal );
    dataMap.put( "percentageToGoal", percentProgress );
    dataMap.put( "progressToDate", progress );
    dataMap.put( "totalAmountToGo", remaining );
    if ( stackRank != null && participantCount != null )
    {
      dataMap.put( "individualStackRank", stackRank );
      dataMap.put( "participantCount", participantCount );
    }

    populateContestProgressParticipantLink( dataMap, participant, ssiContest );

    if ( SSIContestType.OBJECTIVES.equals( ssiContest.getContestType().getCode() ) || SSIContestType.STACK_RANK.equals( ssiContest.getContestType().getCode() ) )
    {
      if ( new Double( 0 ).equals( paxProgressValueBean.getToGoAmount() ) )
      {
        dataMap.put( "isAchievedObjective ", String.valueOf( Boolean.TRUE ) );
      }
    }

    return dataMap;
  }

  private Map setContestProgressStackRankPaxData( Map dataMap, Participant participant, SSIContest ssiContest, SSIContestPaxProgressDetailValueBean paxProgressValueBean, int decimalPrecision )
  {
    String activityDescription = ssiContest.getActivityDescription();
    String progress = "";
    if ( paxProgressValueBean.getActivityAmount() != null )
    {
      progress = SSIContestUtil.getFormattedValue( paxProgressValueBean.getActivityAmount(), decimalPrecision ) + getActivitySuffix( ssiContest );
    }
    else
    {
      progress = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.generalInfo.NO_ACTIVITY_TO_DATE" );
    }
    String stackRank = paxProgressValueBean.getStackRank() != null ? SSIContestUtil.HASHTAG + String.valueOf( paxProgressValueBean.getStackRank() ) : null;
    String participantCount = paxProgressValueBean.getTotalPax() != null ? String.valueOf( paxProgressValueBean.getTotalPax() ) : null;
    String payoutAmount;

    for ( SSIContestStackRankPayoutValueBean contestStackRankPayoutValueBean : paxProgressValueBean.getPayouts() )
    {
      if ( contestStackRankPayoutValueBean.getRank().intValue() == paxProgressValueBean.getStackRank().intValue() && contestStackRankPayoutValueBean.getPayout() != null )
      {
        dataMap.put( "isRankEligibleToPayout", String.valueOf( Boolean.TRUE ) );
        break;
      }
    }
    if ( ssiContest.getPayoutType().isPoints() )
    {
      payoutAmount = SSIContestUtil.getFormattedValue( paxProgressValueBean.getPayoutAmount(), 0 ) + getPayoutSuffix( ssiContest );
    }
    else
    {
      payoutAmount = SSIContestUtil.getFormattedValue( paxProgressValueBean.getPayoutAmount(), decimalPrecision ) + getPayoutSuffix( ssiContest );
    }
    dataMap.put( "activityDescription", activityDescription );
    dataMap.put( "progressToDate", progress );
    dataMap.put( "payoutAmount", payoutAmount );
    if ( stackRank != null && participantCount != null )
    {
      dataMap.put( "individualStackRank", stackRank );
      dataMap.put( "participantCount", participantCount );
    }
    populateContestProgressParticipantLink( dataMap, participant, ssiContest );
    return dataMap;
  }

  @Override
  public Mailing buildSSIContestApprovalNotification( SSIContest contest, Set<SSIContestApprover> contestApprovers, Participant creator, boolean level1Approval, Long level1ApproverId )
  {
    SSIPromotion promotion = contest.getPromotion();
    Mailing mailing = null;
    if ( promotion.getPromotionNotifications().size() > 0 )
    {
      Iterator notificationsIter = promotion.getPromotionNotifications().iterator();
      while ( notificationsIter.hasNext() )
      {
        PromotionNotification notification = (PromotionNotification)notificationsIter.next();
        if ( notification.isPromotionNotificationType() )
        {
          PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notification;
          long messageId = promotionNotificationType.getNotificationMessageId();
          String notificationTypeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();
          if ( contest.getContestType().isAwardThemNow() )
          {
            if ( messageId > 0 && notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_NOTIFY_APPROVER_AWARD_THEM_NOW ) )
            {
              mailing = buildEmails( MailingType.PROMOTION, getSystemIncentiveEmailAddress(), messageService.getMessageById( messageId ), false );
              Set mailingRecipients = new HashSet();
              String programUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
              String programName = systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();
              for ( SSIContestApprover contestApprover : contestApprovers )
              {
                // build mailing recipient for all level 1 approvers and exclude level 1 approver
                // for
                // level 2 approver recipients
                if ( level1Approval || !contestApprover.getApprover().getId().equals( level1ApproverId ) )
                {
                  MailingRecipient mailingRecipient = buildMailingRecipient( contestApprover.getApprover().getId() );
                  String contestName = getContestName( contest, CmsUtil.getLocale( mailingRecipient.getLocale() ) );
                  Map<String, Object> dataMap = buildDataMapForApprovalNotification( contestApprover.getApprover(), contest, contestName, programUrl, programName, creator );
                  mailingRecipient.addMailingRecipientDataFromMap( dataMap );
                  mailingRecipients.add( mailingRecipient );
                }
              }
              mailing.addMailingRecipients( mailingRecipients );
              break;
            }
          }
          else
          {
            if ( messageId > 0 && notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_APPROVAL_NOTIFY_APPROVER ) )
            {
              mailing = buildEmails( MailingType.PROMOTION, getSystemIncentiveEmailAddress(), messageService.getMessageById( messageId ), false );
              Set mailingRecipients = new HashSet();
              String programUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
              String programName = systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();
              for ( SSIContestApprover contestApprover : contestApprovers )
              {
                // build mailing recipient for all level 1 approvers and exclude level 1 approver
                // for
                // level 2 approver recipients
                if ( level1Approval || !contestApprover.getApprover().getId().equals( level1ApproverId ) )
                {
                  MailingRecipient mailingRecipient = buildMailingRecipient( contestApprover.getApprover().getId() );
                  String contestName = getContestName( contest, CmsUtil.getLocale( mailingRecipient.getLocale() ) );
                  Map<String, Object> dataMap = buildDataMapForApprovalNotification( contestApprover.getApprover(), contest, contestName, programUrl, programName, creator );
                  mailingRecipient.addMailingRecipientDataFromMap( dataMap );
                  mailingRecipients.add( mailingRecipient );
                }
              }
              mailing.addMailingRecipients( mailingRecipients );
              break;
            }
          }
        }
      }
    }
    return mailing;
  }

  private Map<String, Object> buildDataMapForApprovalNotification( Participant recipient, SSIContest contest, String contestName, String programUrl, String programName, Participant creator )
  {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put( "contestName", contestName );
    dataMap.put( "firstName", recipient.getFirstName() );
    dataMap.put( "contestCreatorName", creator.getNameFLNoComma() );
    dataMap.put( "programUrl", programUrl );
    dataMap.put( "programName", programName );
    dataMap.put( "contestStartDate", com.biperf.core.utils.DateUtils.toDisplayString( contest.getStartDate() ) );
    return dataMap;
  }

  @Override
  public Mailing buildSSIContestApprovalStatusNotification( SSIContest contest, Participant creator, String approverName, SSIContestAwardThemNow contestAwardThemNow )
  {
    Mailing mailing = null;
    SSIPromotion promotion = contest.getPromotion();
    if ( promotion.getPromotionNotifications().size() > 0 )
    {
      Iterator notificationsIter = promotion.getPromotionNotifications().iterator();
      while ( notificationsIter.hasNext() )
      {
        PromotionNotification notification = (PromotionNotification)notificationsIter.next();
        if ( notification.isPromotionNotificationType() )
        {
          PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notification;
          long messageId = promotionNotificationType.getNotificationMessageId();
          String notificationTypeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();
          if ( messageId > 0 && notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_APPROVAL_STATUS_NOTIFY_CREATOR ) )
          {
            mailing = buildEmails( MailingType.PROMOTION, getSystemIncentiveEmailAddress(), messageService.getMessageById( messageId ), false );
            Set mailingRecipients = new HashSet();
            String programName = systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();
            MailingRecipient mailingRecipient = buildMailingRecipient( creator.getId() );
            String contestName = getContestName( contest, CmsUtil.getLocale( mailingRecipient.getLocale() ) );
            Map<String, Object> dataMap = buildDataMapForApprovalStatusNotification( creator, contest, contestAwardThemNow, contestName, programName, approverName );
            mailingRecipient.addMailingRecipientDataFromMap( dataMap );
            mailingRecipients.add( mailingRecipient );
            if ( SSIContestUtil.canAddSSIAdminInEMailNotificxations( contest.getId() ) != null )
            {
              // SSI Admin
              MailingRecipient mailingRecipientAdmin = new MailingRecipient();
              mailingRecipientAdmin.setGuid( GuidUtils.generateGuid() );
              mailingRecipientAdmin.setUser( null );
              mailingRecipientAdmin
                  .setPreviewEmailAddress( getUserService().getPrimaryUserEmailAddress( SSIContestUtil.canAddSSIAdminInEMailNotificxations( contest.getId() ).getUserID() ).getEmailAddr() );
              mailingRecipientAdmin.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
              contestName = getContestName( contest, CmsUtil.getLocale( systemVariableService.getDefaultLanguage().getStringVal() ) );
              dataMap = buildDataMapForApprovalStatusNotification( creator, contest, contestAwardThemNow, contestName, programName, approverName );
              mailingRecipientAdmin.addMailingRecipientDataFromMap( dataMap );
              mailingRecipients.add( mailingRecipientAdmin );
            }
            mailing.addMailingRecipients( mailingRecipients );
            break;
          }
        }
      }
    }
    return mailing;
  }

  private Map<String, Object> buildDataMapForApprovalStatusNotification( Participant creator,
                                                                         SSIContest contest,
                                                                         SSIContestAwardThemNow contestAwardThemNow,
                                                                         String contestName,
                                                                         String programName,
                                                                         String approverName )
  {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put( "contestName", contestName );
    dataMap.put( "creatorFirstName", creator.getFirstName() );
    dataMap.put( "approverName", approverName );

    if ( contest.getContestType().isAwardThemNow() )
    {
      if ( contestAwardThemNow.getIssuanceStatusType().isDenied() )
      {
        dataMap.put( "denied", String.valueOf( Boolean.TRUE ) );
        dataMap.put( "denialReason", contestAwardThemNow.getDenialReason() );
      }
      else
      {
        dataMap.put( "approved", String.valueOf( Boolean.TRUE ) );
      }
      dataMap.put( "programUrl", SSIContestUtil.populateCreatorDetailPageUrl( contest.getId(), creator.getId(), contest.getContestType().getCode() ) );
    }
    else
    {
      if ( contest.getStatus().isDenied() )
      {
        dataMap.put( "denied", String.valueOf( Boolean.TRUE ) );
        dataMap.put( "denialReason", contest.getDenialReason() );
      }
      else
      {
        dataMap.put( "approved", String.valueOf( Boolean.TRUE ) );
      }
      dataMap.put( "programUrl", SSIContestUtil.populateCreatorDetailPageUrl( contest.getId(), creator.getId() ) );
    }
    dataMap.put( "contestStartDate", com.biperf.core.utils.DateUtils.toDisplayString( contest.getStartDate() ) );

    dataMap.put( "programName", programName );
    return dataMap;
  }

  @Override
  public Mailing buildSSIContestUpdatedAfterApprovalNotification( SSIContest contest, Participant approver, String creatorName, Date approvalDate )
  {
    Mailing mailing = null;
    SSIPromotion promotion = contest.getPromotion();
    if ( promotion.getPromotionNotifications().size() > 0 )
    {
      Iterator notificationsIter = promotion.getPromotionNotifications().iterator();
      while ( notificationsIter.hasNext() )
      {
        PromotionNotification notification = (PromotionNotification)notificationsIter.next();
        if ( notification.isPromotionNotificationType() )
        {
          PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notification;
          long messageId = promotionNotificationType.getNotificationMessageId();
          String notificationTypeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();
          if ( messageId > 0 && notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_UPDATE_AFTER_APPROVAL_STATUS_NOTIFY_APPROVER ) )
          {
            mailing = buildEmails( MailingType.PROMOTION, getSystemIncentiveEmailAddress(), messageService.getMessageById( messageId ), false );
            Set mailingRecipients = new HashSet();
            MailingRecipient mailingRecipient = buildMailingRecipient( approver.getId() );
            String contestName = getContestName( contest, CmsUtil.getLocale( mailingRecipient.getLocale() ) );
            Map<String, Object> dataMap = buildDataMapForContestUpdatedAfterApprovalNotification( approver, contest, contestName, creatorName, approvalDate );
            mailingRecipient.addMailingRecipientDataFromMap( dataMap );
            mailingRecipients.add( mailingRecipient );
            mailing.addMailingRecipients( mailingRecipients );
            break;
          }
        }
      }
    }
    return mailing;
  }

  private Map<String, Object> buildDataMapForContestUpdatedAfterApprovalNotification( Participant approver, SSIContest contest, String contestName, String creatorName, Date approvalDate )
  {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put( "firstName", approver.getFirstName() );
    dataMap.put( "contestCreatorName", creatorName );
    dataMap.put( "contestName", contestName );
    dataMap.put( "approvalDate", com.biperf.core.utils.DateUtils.toDisplayString( approvalDate ) );
    dataMap.put( "contestStartDate", com.biperf.core.utils.DateUtils.toDisplayString( contest.getStartDate() ) );
    return dataMap;
  }

  private Map<String, Object> buildDataMapForContestEditNotification( String contestName, String creatorName, String previewEmailId )
  {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put( "firstName", creatorName );
    dataMap.put( "contestName", contestName );
    dataMap.put( "programName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    if ( previewEmailId != null )
    {
      dataMap.put( "ssiAdminEMailCopyNotes", String.valueOf( true ) );
    }
    else
    {
      dataMap.put( "ssiCreatorEMailCopyNotes", String.valueOf( true ) );
    }

    return dataMap;
  }

  private Map<String, Object> buildDataMapForPAXOptOutOfAwardsNotification( MailingRecipient mailingRecipient, Participant participant )
  {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put( "firstName", mailingRecipient.getUser().getFirstName() );
    if ( participant != null )
    {
      dataMap.put( "paxFirstName", participant.getNameFLNoComma() );
    }
    dataMap.put( "processDate", com.biperf.core.utils.DateUtils.toDisplayString( new Date(), LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );
    dataMap.put( "programName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    String programUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    dataMap.put( "programUrl", programUrl );

    return dataMap;
  }

  @Override
  public Mailing buildContestClaimApprovalNotification( SSIContest contest, Participant approver )
  {
    Mailing mailing = null;
    SSIPromotion promotion = contest.getPromotion();
    if ( promotion.getPromotionNotifications().size() > 0 )
    {
      Iterator notificationsIter = promotion.getPromotionNotifications().iterator();
      while ( notificationsIter.hasNext() )
      {
        PromotionNotification notification = (PromotionNotification)notificationsIter.next();
        if ( notification.isPromotionNotificationType() )
        {
          PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notification;
          long messageId = promotionNotificationType.getNotificationMessageId();
          String notificationTypeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();
          if ( messageId > 0 && notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_CLAIM_APPROVAL_NOTIFY_APPROVER ) )
          {
            mailing = buildEmails( MailingType.PROMOTION, getSystemIncentiveEmailAddress(), messageService.getMessageById( messageId ), false );
            Set mailingRecipients = new HashSet();
            MailingRecipient mailingRecipient = buildMailingRecipient( approver.getId() );
            String contestName = getContestName( contest, CmsUtil.getLocale( mailingRecipient.getLocale() ) );
            Map<String, Object> dataMap = buildDataMapForContestClaimApprover( approver, contest, contestName );
            mailingRecipient.addMailingRecipientDataFromMap( dataMap );
            mailingRecipients.add( mailingRecipient );
            Message message = messageService.getMessageById( messageId );
            mailing.addMailingRecipients( mailingRecipients );
            break;
          }
        }
      }
    }
    return mailing;
  }

  private Map<String, Object> buildDataMapForContestClaimApprover( Participant approver, SSIContest contest, String contestName )
  {
    String programName = systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();
    String programUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put( "firstName", approver.getFirstName() );
    dataMap.put( "contestName", contestName );
    dataMap.put( "programName", programName );
    dataMap.put( "programUrl", programUrl );
    return dataMap;
  }

  @Override
  public Mailing buildContestClaimApprovalUpdateStatusNotification( SSIContestPaxClaim paxClaim, String approverName, SSIContest contest, Participant submitter, String activityDescription )
  {
    SSIPromotion promotion = contest.getPromotion();
    if ( promotion.getPromotionNotifications().size() > 0 )
    {
      Iterator notificationsIter = promotion.getPromotionNotifications().iterator();
      while ( notificationsIter.hasNext() )
      {
        PromotionNotification notification = (PromotionNotification)notificationsIter.next();
        if ( notification.isPromotionNotificationType() )
        {
          PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notification;
          long messageId = promotionNotificationType.getNotificationMessageId();
          String notificationTypeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();
          if ( messageId > 0 && notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_CLAIM_ACTION_NOTIFY_SUBMITTER ) )
          {
            Message message = this.messageService.getMessageById( messageId );
            return buildContestClaimApprovalUpdateStatusNotification( paxClaim, approverName, message, contest, submitter, activityDescription );
          }
        }
      }
    }
    return null;
  }

  @Override
  public Mailing buildContestClaimApprovalUpdateStatusNotification( SSIContestPaxClaim paxClaim,
                                                                    String approverName,
                                                                    Message message,
                                                                    SSIContest contest,
                                                                    Participant submitter,
                                                                    String activityDescription )
  {
    Mailing mailing = null;
    if ( message != null )
    {
      mailing = buildEmails( MailingType.PROMOTION, getSystemIncentiveEmailAddress(), message, false );
      Set mailingRecipients = new HashSet();
      MailingRecipient mailingRecipient = buildMailingRecipient( submitter.getId() );
      String contestName = getContestName( contest, CmsUtil.getLocale( mailingRecipient.getLocale() ) );
      Map<String, Object> dataMap = buildDataMapForContestClaimSubmitter( paxClaim, contestName, approverName, contest, submitter, activityDescription );
      mailingRecipient.addMailingRecipientDataFromMap( dataMap );
      mailingRecipients.add( mailingRecipient );
      mailing.addMailingRecipients( mailingRecipients );
    }
    return mailing;
  }

  private Map<String, Object> buildDataMapForContestClaimSubmitter( SSIContestPaxClaim paxClaim,
                                                                    String contestName,
                                                                    String approverName,
                                                                    SSIContest contest,
                                                                    Participant submitter,
                                                                    String activityDescription )
  {
    String programName = systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();
    String programUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    Map<String, Object> dataMap = new HashMap<String, Object>();
    if ( paxClaim.getStatus().isApproved() )
    {
      dataMap.put( "isApproved", String.valueOf( Boolean.TRUE ) );
    }
    dataMap.put( "firstName", submitter.getFirstName() );
    dataMap.put( "claimNumber", paxClaim.getClaimNumber() );
    dataMap.put( "activityName", activityDescription );
    dataMap.put( "contestName", contestName );
    dataMap.put( "approverFullName", approverName );
    dataMap.put( "denialReason", paxClaim.getDeniedReason() );
    dataMap.put( "programName", programName );
    dataMap.put( "programUrl", programUrl );
    return dataMap;
  }

  @Override
  public void buildNominatorRequestForMoreInfo( Approvable claim, User approver, Message message, User nominee, String moreInfo )
  {
    Set<Claim> claims = new HashSet<Claim>( 1 );
    if ( claim instanceof ClaimGroup )
    {
      claims = ( (ClaimGroup)claim ).getClaims();
    }
    else
    {
      claims.add( (NominationClaim)claim );
    }

    for ( Claim claimObject : claims )
    {
      Mailing mailing = buildEmails( MailingType.PROMOTION, nominee.getNameFLNoComma(), message, false );
      NominationClaim nomClaim = (NominationClaim)claimObject;
      Set mailingRecipients = new HashSet();
      MailingRecipient mailingRecipient = buildMailingRecipient( nomClaim.getSubmitter().getId() );

      Map<String, Object> dataMap = buildDataMapNominatorRequestForMoreInfo( nomClaim, approver, nominee, moreInfo );
      mailingRecipient.addMailingRecipientDataFromMap( dataMap );
      mailingRecipients.add( mailingRecipient );
      mailing.addMailingRecipients( mailingRecipients );
      submitMailing( mailing, null );
    }
  }

  private Map<String, Object> buildDataMapNominatorRequestForMoreInfo( NominationClaim claim, User approver, User nominee, String moreInfo )
  {
    Map<String, Object> dataMap = new HashMap<String, Object>();

    dataMap.put( "programName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    dataMap.put( "siteUrl", systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );
    dataMap.put( "nominatorFirstName", claim.getSubmitter().getFirstName() );
    // dataMap.put( "nominatorLastName", claim.getSubmitter().getLastName() );
    if ( claim.hasTeamName() )
    {
      dataMap.put( "team", "TRUE" );
      dataMap.put( "teamName", claim.getTeamName() );
    }
    else
    {
      dataMap.put( "individual", "TRUE" );
    }
    dataMap.put( "approverFirstName", approver.getFirstName() );
    dataMap.put( "approverLastName", approver.getLastName() );
    dataMap.put( "nomineeFirstName", nominee.getFirstName() );
    dataMap.put( "nomineeLastName", nominee.getLastName() );
    dataMap.put( "promotionName", claim.getPromotion().getName() );
    dataMap.put( "requestMoreInfoMessage", moreInfo );

    return dataMap;
  }

  @Override
  public Mailing buildApproverRequestForMoreInfoReceived( Approvable claim, Participant approver, Participant nominee, Message message, String moreInfo, boolean defaultApprover )
  {
    Mailing mailing = buildEmails( MailingType.PROMOTION, getSystemIncentiveEmailAddress(), message, false );

    NominationClaim nomClaim = (NominationClaim)claim;

    Set mailingRecipients = new HashSet();
    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setUser( approver );
    mailingRecipient.setLocale( nomClaim.getSubmitter().getLanguageType() != null ? nomClaim.getSubmitter().getLanguageType().getCode() : systemVariableService.getDefaultLanguage().getStringVal() );
    Map<String, Object> dataMap = buildDataMapApproverRequestForMoreInfoReceived( nomClaim, approver, nominee, moreInfo, defaultApprover );
    mailingRecipient.addMailingRecipientDataFromMap( dataMap );
    mailingRecipients.add( mailingRecipient );
    mailing.addMailingRecipients( mailingRecipients );
    return mailing;
  }

  private Map<String, Object> buildDataMapApproverRequestForMoreInfoReceived( NominationClaim claim, Participant approver, Participant nominee, String moreInfo, boolean defaultApprover )
  {
    Map<String, Object> dataMap = new HashMap<String, Object>();

    dataMap.put( "programName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    dataMap.put( "siteUrl", systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );
    dataMap.put( "nominatorFirstName", claim.getSubmitter().getFirstName() );
    dataMap.put( "nominatorLastName", claim.getSubmitter().getLastName() );
    if ( claim.hasTeamName() )
    {
      dataMap.put( "team", "TRUE" );
      dataMap.put( "teamName", claim.getTeamName() );
    }
    else
    {
      dataMap.put( "individual", "TRUE" );
    }
    dataMap.put( "approverFirstName", approver.getFirstName() );
    dataMap.put( "nomineeFirstName", nominee.getFirstName() );
    dataMap.put( "nomineeLastName", nominee.getLastName() );
    dataMap.put( "promotionName", claim.getPromotion().getName() );
    dataMap.put( "requestMoreInfoMessage", moreInfo );

    if ( defaultApprover )
    {
      dataMap.put( "defaultApprover", "TRUE" );
    }

    return dataMap;
  }

  @Override
  public Mailing buildNominationRequestMoreBudgetMailing( NominationPromotion promotion,
                                                          PromotionAwardsType awardType,
                                                          Participant claimApprover,
                                                          String claimApproverTimeZoneId,
                                                          BigDecimal amountRequested )
  {
    Mailing mailing = buildEmails( MailingType.PROMOTION, getSystemIncentiveEmailAddress(), messageService.getMessageByCMAssetCode( MessageService.NOMINATION_REQUEST_MORE_BUDGET ), false );

    Set<MailingRecipient> mailingRecipients = new HashSet<>();

    BudgetMaster budgetMaster = null;
    String pointsAmount = null;
    String formattedAmountUSD = null;
    String formattedAmountLocal = null;
    String currencyLabel = null;
    Participant budgetApprover = null;
    boolean defaultApprover = false;

    // Fall back to the default approver if we cannot use the budget approver
    if ( promotion.getBudgetApprover() == null || !promotion.getBudgetApprover().isActive() )
    {
      budgetApprover = promotion.getDefaultApprover();
      defaultApprover = true;
    }
    else
    {
      budgetApprover = promotion.getBudgetApprover();
    }

    // The claim approver object we get passed already has these hydrated. However, budget
    // approver does not. Let's do that...
    AssociationRequestCollection budgetApproverARC = new AssociationRequestCollection();
    budgetApproverARC.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.EMAILS ) );
    budgetApproverARC.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.ADDRESSES ) );
    budgetApprover = getParticipantService().getParticipantByIdWithAssociations( budgetApprover.getId(), budgetApproverARC );

    MailingRecipient mailingRecipient = buildMailingRecipient( budgetApprover.getId() );

    if ( PromotionAwardsType.POINTS.equals( awardType.getCode() ) )
    {
      budgetMaster = promotion.getBudgetMaster();
      pointsAmount = NumberFormatUtil.getUserLocaleBasedNumberFormat( amountRequested.longValue(), LocaleUtils.getLocale( mailingRecipient.getLocale().toString() ) );
    }
    else if ( PromotionAwardsType.CASH.equals( awardType.getCode() ) )
    {
      budgetMaster = promotion.getCashBudgetMaster();
      String claimApproverCurrencyLabel = claimApprover.getPrimaryAddress().getAddress().getCountry().getCurrencyCode();
      String budgetApproverCurrencyLabel = budgetApprover.getPrimaryAddress().getAddress().getCountry().getCurrencyCode();

      // No matter what, we need a USD amount. Even if no conversion necessary, service will give
      // us a scaled number
      BigDecimal convertedAmountUSD = getCashCurrencyService().convertCurrency( claimApproverCurrencyLabel, "USD", amountRequested, null );
      formattedAmountUSD = NumberFormatUtil.getUserLocaleBasedNumberFormat( convertedAmountUSD.longValue(), LocaleUtils.getLocale( mailingRecipient.getLocale().toString() ) );

      // However, we'll leave the localized amount null if it would also be USD
      if ( !budgetApproverCurrencyLabel.equalsIgnoreCase( "USD" ) )
      {
        BigDecimal convertedAmountLocal = getCashCurrencyService().convertCurrency( claimApproverCurrencyLabel, budgetApproverCurrencyLabel, amountRequested, null );
        formattedAmountLocal = NumberFormatUtil.getUserLocaleBasedNumberFormat( convertedAmountLocal.longValue(), LocaleUtils.getLocale( mailingRecipient.getLocale().toString() ) );
      }
    }

    Map<String, Object> dataMap = buildRequestMoreBudgetDataMap( promotion,
                                                                 claimApprover,
                                                                 claimApproverTimeZoneId,
                                                                 budgetMaster,
                                                                 currencyLabel,
                                                                 pointsAmount,
                                                                 formattedAmountUSD,
                                                                 formattedAmountLocal,
                                                                 defaultApprover );

    mailingRecipient.addMailingRecipientDataFromMap( dataMap );
    mailingRecipients.add( mailingRecipient );
    mailing.addMailingRecipients( mailingRecipients );

    return mailing;
  }

  private Map<String, Object> buildRequestMoreBudgetDataMap( NominationPromotion promotion,
                                                             Participant claimApprover,
                                                             String claimApproverTimeZoneId,
                                                             BudgetMaster budgetMaster,
                                                             String localCurrencyType,
                                                             String amountRequested,
                                                             String amountRequestedUSD,
                                                             String amountRequestedLocal,
                                                             boolean defaultApprover )
  {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put( "approverFirstName", claimApprover.getFirstName() );
    dataMap.put( "approverLastName", claimApprover.getLastName() );

    dataMap.put( "promotionName", promotion.getName() );

    if ( defaultApprover )
    {
      dataMap.put( "defaultApprover", "TRUE" );
    }
    else
    {
      dataMap.put( "budgetApprover", "TRUE" );
      dataMap.put( "budgetApproverFirstName", promotion.getBudgetApprover().getFirstName() );
    }

    if ( budgetMaster.getAwardType().getCode().equals( BudgetMasterAwardType.POINTS ) )
    {
      dataMap.put( "amountRequested", amountRequested );
      dataMap.put( "points", "TRUE" );
      dataMap.put( "pointsBudgetMasterName", budgetMaster.getBudgetMasterName() );
    }
    else if ( budgetMaster.getAwardType().getCode().equals( BudgetMasterAwardType.CASH ) )
    {
      dataMap.put( "cash", "TRUE" );
      if ( StringUtil.isEmpty( amountRequestedLocal ) )
      {
        dataMap.put( "cashInUSD", "TRUE" );
        dataMap.put( "amountRequestedUSD", amountRequestedUSD );
        dataMap.put( "cashBudgetMasterName", budgetMaster.getBudgetMasterName() );
      }
      else
      {
        dataMap.put( "cashInOther", "TRUE" );
        dataMap.put( "amountRequestedLocal", amountRequestedLocal );
        dataMap.put( "localCurrencyType", localCurrencyType );
        dataMap.put( "amountRequestedUSD", amountRequestedUSD );
      }
    }

    dataMap.put( "budgetPeriodName", budgetMaster.getCurrentBudgetSegment( claimApproverTimeZoneId ).getName() );

    return dataMap;
  }

  /**
   * Place values into the dataMap for displaying badges.
   * @param userId Nominee user ID
   * @param includeEarned True if earned/not earned type badges should be included as well.
   */
  @Override
  public void buildBadgesForNominationPromotionNotification( Map<String, String> dataMap, NominationClaim claim, Long userId, boolean includeEarned )
  {
    // Obtain list of badges to include
    List<ParticipantBadge> participantBadges = new ArrayList<>();
    if ( includeEarned )
    {
      List<ParticipantBadge> earnedBadges = getGamificationService().getBadgeByParticipantEarnedForPromotion( claim.getPromotion().getId(), userId );
      if ( earnedBadges != null && !earnedBadges.isEmpty() )
      {
        participantBadges.addAll( earnedBadges );
      }
    }
    if ( claim.getNominationClaimBehaviors() != null && !claim.getNominationClaimBehaviors().isEmpty() )
    {
      List<String> behaviorCodes = claim.getNominationClaimBehaviors().stream().map( ( behavior ) -> behavior.getBehavior().getCode() ).collect( Collectors.toList() );
      List<ParticipantBadge> behaviorBadges = getGamificationService().getBehaviorParticipantBadges( userId, claim.getPromotion().getId(), behaviorCodes );
      if ( behaviorBadges != null && !behaviorBadges.isEmpty() )
      {
        participantBadges.addAll( behaviorBadges );
      }
    }

    // De-duplicate badges, the queries will give multiple participant badges when someone has won
    // multiple times.
    Map<Long, ParticipantBadge> uniqueBadges = new HashMap<>();
    participantBadges.forEach( ( badge ) -> uniqueBadges.put( badge.getBadgeRule().getId(), badge ) );
    participantBadges.clear();
    participantBadges.addAll( uniqueBadges.values() );

    // Have badges. Obtain data values and place in map.
    if ( !participantBadges.isEmpty() )
    {
      dataMap.put( "showBadges", "TRUE" );

      Map<String, Object> paxBadgeMap = new HashMap<>();
      paxBadgeMap = buildNominationBadgeHtmlString( participantBadges );
      String paxBadgeEarnedString = "";
      String paxBadgeBehaviorString = "";
      if ( paxBadgeMap.get( "paxBadgeEarnedString" ) != null )
      {
        paxBadgeEarnedString = paxBadgeMap.get( "paxBadgeEarnedString" ).toString();
      }
      if ( paxBadgeMap.get( "paxBadgeBehaviorString" ) != null )
      {
        paxBadgeBehaviorString = paxBadgeMap.get( "paxBadgeBehaviorString" ).toString();
      }
      if ( !StringUtils.isEmpty( paxBadgeEarnedString ) )
      {
        String paxBadgeEarnedNoHtml = "";
        paxBadgeEarnedNoHtml = HtmlUtils.removeFormatting( paxBadgeEarnedString );
        if ( !StringUtils.isEmpty( paxBadgeEarnedNoHtml.trim() ) )
        {
          dataMap.put( "showEarnedBadges", "TRUE" );
          dataMap.put( "paxBadgeEarnedString", paxBadgeEarnedString );
          dataMap.put( "paxBadgeEarnedStringPlain", paxBadgeEarnedNoHtml );
        }
      }
      if ( !StringUtils.isEmpty( paxBadgeBehaviorString ) )
      {
        String paxBadgeBehaviorNoHtml = "";
        paxBadgeBehaviorNoHtml = HtmlUtils.removeFormatting( paxBadgeBehaviorString );
        if ( !StringUtils.isEmpty( paxBadgeBehaviorNoHtml.trim() ) )
        {
          dataMap.put( "showBehaviorBadges", "TRUE" );
          dataMap.put( "paxBadgeBehaviorString", paxBadgeBehaviorString );
          dataMap.put( "paxBadgeBehaviorStringPlain", HtmlUtils.removeFormatting( paxBadgeBehaviorString ) );
        }
      }
    }
  }

  private void buildBehaviorsForNominationNotification( NominationClaim claim, Map<String, String> dataMap, Locale recipientLocale )
  {
    if ( claim.getNominationClaimBehaviors() != null && !claim.getNominationClaimBehaviors().isEmpty() )
    {
      dataMap.put( "showCategory", "TRUE" );

      ContentReader contentReader = ContentReaderManager.getContentReader();
      List<Content> contentList = (List<Content>)contentReader.getContent( "picklist.promo.nomination.behavior.items", recipientLocale );
      // Map of behavior code to translated behavior name
      Map<String, String> behaviorContentMap = contentList.stream().map( ( content ) -> content.getContentDataMap() )
          .collect( Collectors.toMap( ( contentMap ) -> (String)contentMap.get( PickListItem.ITEM_CODE_KEY ), ( contentMap ) -> (String)contentMap.get( PickListItem.ITEM_NAME_KEY ) ) );
      // List of translated behavior names
      List<String> behaviorNames = claim.getNominationClaimBehaviors().stream()
          .map( ( behavior ) -> behaviorContentMap.getOrDefault( behavior.getBehavior().getCode(), behavior.getBehavior().getName() ) ).collect( Collectors.toList() );
      // Comma separated list of behavior names
      String behaviorString = StringUtil.convertListToCommaSeparated( behaviorNames );
      dataMap.put( "category", behaviorString );
    }
  }

  @Override
  public void buildNominatorNonWinnerNotification( Approvable claim, Message message, String reasonType, User nominee, String levelName )
  {
    Mailing mailing = buildEmails( MailingType.PROMOTION, nominee.getNameFLNoComma(), message, false );
    NominationClaim nomClaim = (NominationClaim)claim;

    Set mailingRecipients = new HashSet();
    MailingRecipient mailingRecipient = buildMailingRecipient( nomClaim.getSubmitter().getId() );

    Map<String, Object> dataMap = nonWinnerNotification( (NominationPromotion)nomClaim.getPromotion(), reasonType, nomClaim, nominee, levelName );
    mailingRecipient.addMailingRecipientDataFromMap( dataMap );
    mailingRecipients.add( mailingRecipient );
    mailing.addMailingRecipients( mailingRecipients );

    submitMailing( mailing, null );
  }

  public Map<String, Object> nonWinnerNotification( NominationPromotion promotion, String reasonType, NominationClaim claim, User nominee, String levelName )
  {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put( "nomineeFirstName", nominee.getFirstName() );
    dataMap.put( "nomineeLastName", nominee.getLastName() );
    dataMap.put( "promotionName", promotion.getName() );
    dataMap.put( "levelName", levelName );
    dataMap.put( "nonWinnerReason", reasonType );
    dataMap.put( "message", claim.getSubmitterComments() );

    boolean isTeam = claim.hasTeamName();
    boolean isPayoutEachLevel = promotion.getPayoutLevel().equals( "eachLevel" );

    // These two are used just for the subject
    if ( isTeam )
    {
      dataMap.put( "toTeam", "TRUE" );
    }
    else
    {
      dataMap.put( "indv", "TRUE" );
    }

    if ( !isTeam )
    {
      dataMap.put( "individual", "TRUE" );
    }

    if ( isTeam && isPayoutEachLevel )
    {
      dataMap.put( "teamEach", "TRUE" );
    }
    else if ( !isTeam && isPayoutEachLevel )
    {
      dataMap.put( "nTeamEach", "TRUE" );
    }
    else if ( isTeam )
    {
      dataMap.put( "team", "TRUE" );
    }
    else if ( !isTeam )
    {
      dataMap.put( "nTeam", "TRUE" );
    }

    if ( isTeam )
    {
      dataMap.put( "teamName", claim.getTeamName() );
    }
    return dataMap;
  }

  private String getLocaleBasedEcardName( Long cardId, String ecardFullImageName, String recipientLocale )
  {
    Boolean isEcardAvilable = getMultimediaService().isEcardExistForLocale( cardId, recipientLocale );
    if ( isEcardAvilable )
    {
      String ecardImageExtension = ecardFullImageName.substring( ecardFullImageName.lastIndexOf( '.' ), ecardFullImageName.length() );
      String ecardImageName = ecardFullImageName.substring( 0, ecardFullImageName.lastIndexOf( '.' ) );
      return ecardImageName + "_" + recipientLocale + ecardImageExtension;
    }
    return ecardFullImageName;
  }

  private MultimediaService getMultimediaService()
  {
    return (MultimediaService)getService( MultimediaService.BEAN_NAME );
  }

  @Override
  public Mailing buildSSIContestEditNotification( SSIContest ssiContest, String previewEmailId )
  {
    Mailing mailing = buildEmails( MailingType.PROMOTION, getSystemIncentiveEmailAddress(), messageService.getMessageByCMAssetCode( MessageService.SSI_CONTEST_EDIT_NOTIFY_CREATOR ), false );

    Set mailingRecipients = new HashSet();
    MailingRecipient mailingRecipient = buildMailingRecipient( ssiContest.getCreatorId() );
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    if ( previewEmailId != null )
    {
      mailingRecipient.setPreviewEmailAddress( previewEmailId );
    }

    String contestName = getContestName( ssiContest, CmsUtil.getLocale( mailingRecipient.getLocale() ) );
    Map<String, Object> dataMap = buildDataMapForContestEditNotification( contestName, mailingRecipient.getUser().getFirstName(), previewEmailId );
    mailingRecipient.addMailingRecipientDataFromMap( dataMap );
    mailingRecipients.add( mailingRecipient );
    // Message message = messageService.getMessageById( messageId );
    mailing.addMailingRecipients( mailingRecipients );

    return mailing;
  }

  @Override
  public Mailing buildPAXOptOutOfAwardsNotification( Long mailingRecipientId, boolean isOwnerorManagerCopy, Participant participant )
  {
    Message message = isOwnerorManagerCopy
        ? messageService.getMessageByCMAssetCode( MessageService.OPT_OUT_OF_AWARDS_NOTIFY_MANAGER )
        : messageService.getMessageByCMAssetCode( MessageService.OPT_OUT_OF_AWARDS_NOTIFY_PAX );

    Mailing mailing = buildEmails( MailingType.PROMOTION, getSystemIncentiveEmailAddress(), message, false );

    Set mailingRecipients = new HashSet();
    MailingRecipient mailingRecipient = buildMailingRecipient( mailingRecipientId );

    Map<String, Object> dataMap = buildDataMapForPAXOptOutOfAwardsNotification( mailingRecipient, participant );
    mailingRecipient.addMailingRecipientDataFromMap( dataMap );
    mailingRecipients.add( mailingRecipient );
    mailing.addMailingRecipients( mailingRecipients );

    return mailing;
  }

  @Override
  public Mailing buildPAXForgotLoginIDNotification( Long mailingRecipientId, PaxContactType paxContactType, String previewEmailAddress )
  {
    Mailing mailing = buildEmails( MailingType.PROMOTION, getSystemIncentiveEmailAddress(), messageService.getMessageByCMAssetCode( MessageService.FORGOT_LOGIN_ID ), false );

    Set mailingRecipients = new HashSet();
    MailingRecipient mailingRecipient = buildMailingRecipient( mailingRecipientId );

    if ( previewEmailAddress != null )
    {
      mailingRecipient.setPreviewEmailAddress( previewEmailAddress );
    }

    // NOTE: the contact methods MAY NOT be those that are primary for the user, so assign them here
    if ( paxContactType.getContactType() == ContactType.EMAIL )
    {
      mailingRecipient.setAlternateEmailAddrId( paxContactType.getContactId() );
    }
    else
    {
      mailingRecipient.setAlternatePhoneId( paxContactType.getContactId() );
    }

    Map<String, Object> dataMap = buildDataMapForPAXForgotLoginIDNotification( mailingRecipient );
    mailingRecipient.addMailingRecipientDataFromMap( dataMap );
    mailingRecipients.add( mailingRecipient );
    mailing.addMailingRecipients( mailingRecipients );

    return mailing;
  }

  @Override
  public Mailing buildPaxForgotLoginIDSentNotification( UserEmailAddress email, boolean sentToEmail, boolean sharedContact )
  {
    Mailing mailing = buildEmails( MailingType.PROMOTION, getSystemIncentiveEmailAddress(), messageService.getMessageByCMAssetCode( MessageService.FORGOT_LOGIN_ID_ALERT ), false );

    Set mailingRecipients = new HashSet();
    MailingRecipient mailingRecipient = buildMailingRecipient( email.getUser().getId() );
    mailingRecipient.setAlternateEmailAddrId( email.getId() );
    Map<String, Object> dataMap = buildDataMapForPAXForgotLoginIDSentNotification( mailingRecipient, sentToEmail, sharedContact );
    mailingRecipient.addMailingRecipientDataFromMap( dataMap );
    mailingRecipients.add( mailingRecipient );
    mailing.addMailingRecipients( mailingRecipients );

    return mailing;
  }

  /*
   * This method will also send notifications to all registered email addresses for this user
   */
  @Override
  public Mailing buildAccountOrPasswordChangeNotification( Long mailingRecipientId, boolean sharedContact, String message )
  {
    Mailing mailing = buildEmails( MailingType.PROMOTION, getSystemIncentiveEmailAddress(), messageService.getMessageByCMAssetCode( message ), false );

    Set mailingRecipients = new HashSet();
    MailingRecipient mailingRecipient = buildMailingRecipient( mailingRecipientId );
    Map<String, Object> dataMap = buildDataMapForAccountOrPasswordChange( mailingRecipient, sharedContact );
    Set<UserEmailAddress> userEmails = mailingRecipient.getUser().getUserEmailAddresses();
    List<UserEmailAddress> userEmailList = userEmails.stream().collect( Collectors.toList() );
    List<UserEmailAddress> distinctUserEmails = userEmailList.stream().filter( distinctByKey( e -> e.getEmailAddr() ) ).collect( Collectors.toList() );
    // build the rest of the emails for the user akerts
    for ( UserEmailAddress email : distinctUserEmails )
    {
      MailingRecipient alternateEmailRecipient = buildMailingRecipient( email.getUser().getId() );
      if ( !email.isPrimary() )
      {
        alternateEmailRecipient.setAlternateEmailAddrId( email.getId() );
      }
      alternateEmailRecipient.addMailingRecipientDataFromMap( dataMap );
      mailingRecipients.add( alternateEmailRecipient );
    }
    mailing.addMailingRecipients( mailingRecipients );

    return mailing;
  }

  private Map<String, Object> buildDataMapForAccountOrPasswordChange( MailingRecipient mailingRecipient, boolean sharedContact )
  {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put( "firstName", mailingRecipient.getUser().getFirstName() );
    dataMap.put( "programName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    String programUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    dataMap.put( "accountLockUrl", buildAccountLockUrl( mailingRecipient.getUser(), programUrl ) );
    dataMap.put( "isSharedContact", sharedContact ? "TRUE" : "FALSE" );
    return dataMap;
  }

  private String buildAccountLockUrl( User user, String programUrl )
  {
    String encryptedId = encryptionService.getEncryptedValue( String.valueOf( user.getId() ) );
    return programUrl + "/account/lock.action?key=" + encryptedId.replace( "{AES}", "" );
  }

  @Override
  public String buildAccountOrPasswordChangeText( User user, String message )
  {
    String programName = systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();
    String formattedMessage = getTextMessageString( message ).replace( "${programName}", programName );
    String accountLockUrl = buildAccountLockUrl( user, systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );
    formattedMessage = formattedMessage.replace( "${accountLockShortUrl}", getShortUrl( accountLockUrl, programName ) );
    return formattedMessage;
  }

  @Override
  public Mailing buildPAXForgotPasswordNotification( Long mailingRecipientId, PaxContactType paxContactType, String userToken )
  {
    Mailing mailing = buildEmails( MailingType.PROMOTION, getSystemIncentiveEmailAddress(), messageService.getMessageByCMAssetCode( MessageService.FORGOT_PASSWORD ), false );
    Set mailingRecipients = new HashSet();
    MailingRecipient mailingRecipient = buildMailingRecipient( mailingRecipientId );

    // NOTE: the contact methods MAY NOT be those that are primary for the user, so assign them here
    if ( paxContactType.getContactType() == ContactType.EMAIL )
    {
      mailingRecipient.setAlternateEmailAddrId( paxContactType.getContactId() );
    }
    else
    {
      mailingRecipient.setAlternatePhoneId( paxContactType.getContactId() );
    }

    Map<String, Object> dataMap = buildDataMapForPAXForgotPasswordNotification( mailingRecipient, userToken );
    mailingRecipient.addMailingRecipientDataFromMap( dataMap );
    mailingRecipients.add( mailingRecipient );
    mailing.addMailingRecipients( mailingRecipients );

    return mailing;
  }

  @Override
  public Mailing buildPAXActivationNotification( Long mailingRecipientId, Long contactId, String userToken )
  {
    Message message = messageService.getMessageByCMAssetCode( MessageService.ACCOUNT_ACTIVATION_EMAIL );
    Mailing mailing = buildEmails( MailingType.PROMOTION, getSystemIncentiveEmailAddress(), message, false );

    Set mailingRecipients = new HashSet();
    MailingRecipient mailingRecipient = this.buildMailingRecipient( mailingRecipientId );
    // NOTE: the contact methods MAY NOT be those that are primary for the user, so assign them here
    mailingRecipient.setAlternateEmailAddrId( contactId );

    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put( "firstName", mailingRecipient.getUser().getFirstName() );
    dataMap.put( "userToken", userToken );
    dataMap.put( "programName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    dataMap.put( "userName", mailingRecipient.getUser().getUserName() );
    String loginUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

    // Fixed for the pure SSO client.
    UserEmailAddress email = getUserService().getUserEmailAddressById( contactId );
    User user = email.getUser();
    Participant pax = null;
    if ( user instanceof Participant )
    {
      pax = (Participant)user;
    }
    if ( SsoLoginEnum.SSO.toString().equalsIgnoreCase( systemVariableService.getPropertyByName( SystemVariableService.SSO_LOGIN_TYPE ).getStringVal() )
        && ( Objects.nonNull( pax ) ? !isTermedUserAndInActive( pax ) : false ) )
    {
      String ssoUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SSO_LOGIN_URL ).getStringVal();
      dataMap.put( "programUrl", ssoUrl );
      dataMap.put( "loginUrl", ssoUrl );

    }
    else
    {
      dataMap.put( "programUrl", buildUserTokenURL( userToken ) + "&activation=true" );
      dataMap.put( "loginUrl", loginUrl );

    }

    mailingRecipient.addMailingRecipientDataFromMap( dataMap );
    mailingRecipients.add( mailingRecipient );
    mailing.addMailingRecipients( mailingRecipients );

    return mailing;
  }

  @Override
  public Mailing buildRecoveryVerificationMailing( Long recipientId, PaxContactType paxContactType, String userToken )
  {
    Message message = messageService.getMessageByCMAssetCode( MessageService.RECOVERY_VERIFICATION_EMAIL );
    Mailing mailing = buildEmails( MailingType.PROMOTION, getSystemIncentiveEmailAddress(), message, false );
    Set mailingRecipients = new HashSet();
    MailingRecipient mailingRecipient = buildMailingRecipient( recipientId );

    // NOTE: the contact methods MAY NOT be those that are primary for the user, so assign them here
    if ( paxContactType.getContactType() == ContactType.EMAIL )
    {
      mailingRecipient.setAlternateEmailAddrId( paxContactType.getContactId() );
    }
    else
    {
      mailingRecipient.setAlternatePhoneId( paxContactType.getContactId() );
    }

    Map<String, Object> dataMap = buildDataMapForRecoveryVerificationMailing( mailingRecipient, userToken );
    mailingRecipient.addMailingRecipientDataFromMap( dataMap );
    mailingRecipients.add( mailingRecipient );
    mailing.addMailingRecipients( mailingRecipients );

    return mailing;
  }

  private Map<String, Object> buildDataMapForRecoveryVerificationMailing( MailingRecipient mailingRecipient, String userToken )
  {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put( "userToken", userToken );
    dataMap.put( "programName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    String programUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    dataMap.put( "programUrl", programUrl );

    return dataMap;
  }

  @Override
  public Mailing buildRecoveryNotificationMailing( Long recipientId, PaxContactType paxContactType )
  {
    Message message = messageService.getMessageByCMAssetCode( MessageService.RECOVERY_CHANGE_NOTIFICATION );
    Mailing mailing = buildEmails( MailingType.PROMOTION, getSystemIncentiveEmailAddress(), message, false );
    Set mailingRecipients = new HashSet();
    MailingRecipient mailingRecipient = buildMailingRecipient( recipientId );

    // NOTE: the contact methods MAY NOT be those that are primary for the user, so assign them here
    if ( paxContactType.getContactType() == ContactType.EMAIL )
    {
      mailingRecipient.setPreviewEmailAddress( paxContactType.getValue() );
    }
    else
    {
      mailingRecipient.setPreviewSMSAddress( paxContactType.getValue() );
    }

    Map<String, Object> dataMap = buildDataMapForRecoveryNotificationMailing( mailingRecipient, paxContactType );
    mailingRecipient.addMailingRecipientDataFromMap( dataMap );
    mailingRecipients.add( mailingRecipient );
    mailing.addMailingRecipients( mailingRecipients );

    return mailing;
  }

  private Map<String, Object> buildDataMapForRecoveryNotificationMailing( MailingRecipient mailingRecipient, PaxContactType paxContactType )
  {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put( "firstName", mailingRecipient.getUser().getFirstName() );
    dataMap.put( "programName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    String programUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    dataMap.put( "accountLockUrl", buildAccountLockUrl( mailingRecipient.getUser(), programUrl ) );
    dataMap.put( "isSharedContact", paxContactType.isUnique() ? "FALSE" : "TRUE" );

    return dataMap;
  }

  @Override
  public String buildRecoveryNotificationText( User user )
  {
    String programName = systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();
    String message = MessageService.RECOVERY_CHANGE_NOTIFICATION;
    String formattedMessage = getTextMessageString( message ).replace( "${programName}", programName );
    String accountLockUrl = buildAccountLockUrl( user, systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );
    formattedMessage = formattedMessage.replace( "${accountLockShortUrl}", getShortUrl( accountLockUrl, programName ) );
    return formattedMessage;
  }

  @Override
  public void buildPAXActivationText( UserPhone userPhone, String userToken )
  {
    String message = getTextMessageString( MessageService.ACCOUNT_ACTIVATION_EMAIL );
    String formattedMessage = message.replace( "${programName}", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    formattedMessage = formattedMessage.replace( "${userToken}", userToken );
    sendSmsMessage( userPhone.getUser().getId(), userPhone.getCountryPhoneCode(), userPhone.getPhoneNbr(), formattedMessage );
  }

  private MailingRecipient buildMailingRecipient( Long mailingRecipientId )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setUser( getUserService().getUserById( mailingRecipientId ) );
    mailingRecipient
        .setLocale( mailingRecipient.getUser().getLanguageType() != null ? mailingRecipient.getUser().getLanguageType().getCode() : systemVariableService.getDefaultLanguage().getStringVal() );
    return mailingRecipient;
  }

  private Map<String, Object> buildDataMapForPAXForgotLoginIDNotification( MailingRecipient mailingRecipient )
  {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put( "firstName", mailingRecipient.getUser().getFirstName() );
    dataMap.put( "loginid", mailingRecipient.getUser().getUserName() );
    dataMap.put( "programName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    String programUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    dataMap.put( "programUrl", programUrl );

    return dataMap;
  }

  private Map<String, Object> buildDataMapForPAXForgotLoginIDSentNotification( MailingRecipient mailingRecipient, boolean sentToEmail, boolean sharedContact )
  {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put( "firstName", mailingRecipient.getUser().getFirstName() );
    dataMap.put( "loginid", mailingRecipient.getUser().getUserName() );
    dataMap.put( "isEmail", sentToEmail ? "TRUE" : "FALSE" );
    dataMap.put( "isSharedContact", sharedContact ? "TRUE" : "FALSE" );
    dataMap.put( "programName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    dataMap.put( "programUrl", systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/login.do?forgotPass=true" );

    return dataMap;
  }

  private Map<String, Object> buildDataMapForPAXForgotPasswordNotification( MailingRecipient mailingRecipient, String userToken )
  {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put( "firstName", mailingRecipient.getUser().getFirstName() );
    dataMap.put( "userToken", userToken );
    dataMap.put( "programName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    dataMap.put( "programUrl", buildUserTokenURL( userToken ) );

    return dataMap;
  }

  @Override
  public String buildUserTokenURL( String userToken )
  {
    return systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/login.do?userToken=" + userToken;
  }

  private Mailing buildEmails( String mailingType, String senderEmailAddress, Message message, boolean messageTypeSMS )
  {
    Mailing mailing = new Mailing();
    mailing.setGuid( GuidUtils.generateGuid() );
    mailing.setSender( senderEmailAddress );
    mailing.setMailingType( MailingType.lookup( mailingType ) );
    mailing.setMessage( message );
    mailing.setMessageTypeSMS( messageTypeSMS );
    mailing.setDeliveryDate( Timestamp.from( Instant.now() ) );
    return mailing;
  }

  protected String getTextMessageString( String cmKey )
  {
    return CmsResourceBundle.getCmsBundle().getString( cmKey, "TEXT_MSG" );
  }

  public CountryService getCountryService()
  {
    return null;
  }

  public EncryptionService getEncryptionService()
  {
    return encryptionService;
  }

  public void setEncryptionService( EncryptionService encryptionService )
  {
    this.encryptionService = encryptionService;
  }

  @Override
  public Mailing buildAccountOrPasswordChangeNotificationToTermedUser( Long mailingRecipientId, boolean sharedContact, String message )
  {
    Mailing mailing = buildEmails( MailingType.PROMOTION, getSystemIncentiveEmailAddress(), messageService.getMessageByCMAssetCode( message ), false );

    Set mailingRecipients = new HashSet();
    MailingRecipient mailingRecipient = buildMailingRecipient( mailingRecipientId );
    Map<String, Object> dataMap = buildDataMapForAccountOrPasswordChange( mailingRecipient, sharedContact );
    UserEmailAddress email = mailingRecipient.getUser().getPrimaryEmailAddress();
    MailingRecipient alternateEmailRecipient = buildMailingRecipient( email.getUser().getId() );
    alternateEmailRecipient.addMailingRecipientDataFromMap( dataMap );
    mailingRecipients.add( alternateEmailRecipient );
    mailing.addMailingRecipients( mailingRecipients );
    return mailing;

  }

  private boolean isTermedUserAndInActive( Participant pax )
  {
    if ( Objects.nonNull( pax.getTerminationDate() ) && Objects.nonNull( pax.getStatus() ) && Objects.nonNull( pax.getStatus().getCode() )
        && pax.getStatus().getCode().equals( ParticipantStatus.INACTIVE ) )
    {
      return true;
    }
    return false;
  }

  private String getLocaleCode( ClaimRecipient claimRecipient, User sendToUser, String ccOthersEmailAddress )
  {
    String localeCode = "";
    if ( sendToUser != null && sendToUser.getLanguageType() != null )
    {
      localeCode = sendToUser.getLanguageType().getCode();
    }
    else if ( Objects.isNull( sendToUser ) && !Objects.isNull( ccOthersEmailAddress ) )
    {
      User ccOtherUser = getUserService().getUserByPrimaryEmailAddr( ccOthersEmailAddress );
      if ( !Objects.isNull( ccOtherUser ) && !Objects.isNull( ccOtherUser.getLanguageType() ) )
      {
        localeCode = ccOtherUser.getLanguageType().getCode();
      }
      else if ( !Objects.isNull( claimRecipient.getRecipient().getLanguageType() ) )
      {
        localeCode = claimRecipient.getRecipient().getLanguageType().getCode();
      }
    }
    return localeCode;
  }

  @Override
  public Mailing buildUnLockAccountNotification( Long mailingRecipientId, boolean sharedContact, String message )
  {
    Mailing mailing = buildEmails( MailingType.PROMOTION, getSystemIncentiveEmailAddress(), messageService.getMessageByCMAssetCode( message ), false );

    Set mailingRecipients = new HashSet();
    MailingRecipient mailingRecipient = buildMailingRecipient( mailingRecipientId );
    Map<String, Object> dataMap = buildDataMapForUnLockAccountForPax( mailingRecipient, sharedContact );
    // build the rest of the email's for the user alerts

    Set<UserEmailAddress> userEmails = mailingRecipient.getUser().getUserEmailAddresses();

    List<UserEmailAddress> userEmailList = userEmails.stream().collect( Collectors.toList() );
    List<UserEmailAddress> distinctUserEmails = userEmailList.stream().filter( distinctByKey( e -> e.getEmailAddr() ) ).collect( Collectors.toList() );

    for ( UserEmailAddress email : distinctUserEmails )
    {
      MailingRecipient alternateEmailRecipient = buildMailingRecipient( email.getUser().getId() );
      if ( !email.isPrimary() )
      {
        alternateEmailRecipient.setAlternateEmailAddrId( email.getId() );
      }
      alternateEmailRecipient.addMailingRecipientDataFromMap( dataMap );
      mailingRecipients.add( alternateEmailRecipient );
    }
    mailing.addMailingRecipients( mailingRecipients );

    return mailing;
  }

  private static <T> Predicate<T> distinctByKey( Function<? super T, Object> keyExtractor )
  {
    Map<Object, Boolean> map = new ConcurrentHashMap<>();
    return t -> map.putIfAbsent( keyExtractor.apply( t ), Boolean.TRUE ) == null;
  }

  private Map<String, Object> buildDataMapForUnLockAccountForPax( MailingRecipient mailingRecipient, boolean sharedContact )
  {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put( "firstName", mailingRecipient.getUser().getFirstName() );
    dataMap.put( "programName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    String programUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    dataMap.put( "accountLockUrl", programUrl + "/login.do?forgotPass=true" );
    dataMap.put( "isSharedContact", sharedContact ? "TRUE" : "FALSE" );
    return dataMap;
  }

  public String buildUnLockAccountMobileNotification( String message )
  {
    String programName = systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();
    String formattedMessage = getTextMessageString( message ).replace( "${programName}", programName );
    String programUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    formattedMessage = formattedMessage.replace( "${accountLockShortUrl}", getShortUrl( programUrl + "/login.do?forgotPass=true", programName ) );
    log.error( "Short URL : " + getShortUrl( programUrl + "/login.do?forgotPass=true", programName ) );
    return formattedMessage;
  }
  
// Client customizations for WIP #42701 starts
public Mailing buildCashRecognitionPendingApprovalMailing( Participant recipient )
{
Mailing mailing = new Mailing();
Set<MailingRecipient> mailingRecipients = new HashSet<>();
mailing.setGuid( GuidUtils.generateGuid() );
mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) ); 
mailing.setMessage( messageService.getMessageByCMAssetCode( MessageService.PENDING_AWARD_APPROVAL_MESSAGE_CM_ASSET_CODE ) );
mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
mailing.setSender( getSystemIncentiveEmailAddress() );

MailingRecipient mailingRecipient = new MailingRecipient();
mailingRecipient.setGuid( GuidUtils.generateGuid() );
mailingRecipient.setUser( recipient );
mailingRecipient.setLocale( mailingRecipient.getUser().getLanguageType() != null ? mailingRecipient.getUser().getLanguageType().getCode() : LanguageType.ENGLISH );

Map<String, Object> dataMap = new HashMap<String, Object>();
dataMap.put( "firstName", recipient.getFirstName() );
dataMap.put( "lastName", recipient.getLastName() );
dataMap.put( "company", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
dataMap.put( "approvalPageLink", systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );
mailingRecipient.addMailingRecipientDataFromMap( dataMap );

mailingRecipients.add( mailingRecipient );
mailing.addMailingRecipients( mailingRecipients );

return mailing;
}
// Client customizations for WIP #42701 ends


public Mailing buildSendRecognizeAnyoneMailing( RecognizeAnyone recognizeAnyone )
{
	Mailing mailing = new Mailing();
	Set<MailingRecipient> mailingRecipients = new HashSet<>();
	mailing.setGuid( GuidUtils.generateGuid() );
	mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );  
	mailing.setMessage( messageService.getMessageByCMAssetCode( MessageService.COKE_SEND_RECOGNIZE_ANYONE_MESSAGE_CM_ASSET_CODE ) );
	mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
	mailing.setSender( getSystemIncentiveEmailAddress() );
	Participant participant = getParticipantService().getParticipantById(recognizeAnyone.getUserId() );
	MailingRecipient mailingRecipient = new MailingRecipient();
	mailingRecipient.setGuid( GuidUtils.generateGuid() );
	boolean copySender = systemVariableService.getPropertyByName( SystemVariableService.COKE_RECOG_ANYONE_COPY_SENDER ).getBooleanVal();
	if(copySender){
		mailingRecipient.setUser( participant );	
	}
	mailingRecipient.setLocale(LanguageType.ENGLISH );
	mailingRecipient.setPreviewEmailAddress( recognizeAnyone.getReceiverEmail());
	Map<String, Object> dataMap = new HashMap<String, Object>();
	dataMap.put( "receiverFirstName", recognizeAnyone.getReceiverFirstName() );
	dataMap.put( "receiverLastName", recognizeAnyone.getReceiverLastName() );
	dataMap.put( "senderFirstName", participant.getFirstName() );
	dataMap.put( "senderLastName", participant.getLastName() );
	dataMap.put( "comments", recognizeAnyone.getComments() );
	StringBuilder receiverEcarddStr = new StringBuilder( "" );
	receiverEcarddStr.append( "<table class='badge-item earned-true' id='badge'>" );
	receiverEcarddStr.append( "<tr><td style='text-align:center'><img src='" + recognizeAnyone.getCardImageURL() + "' align='middle' width='248px'/></td></tr>" );
	receiverEcarddStr.append( "<tr><td class='badge-name' style='text-align:center; color: #ffffff; font-size: 30px;'>" );
	receiverEcarddStr.append( "</td></tr></table>" );
	dataMap.put( "receiverEcarddStr", receiverEcarddStr.toString() );
	mailingRecipient.addMailingRecipientDataFromMap( dataMap );
	mailingRecipients.add( mailingRecipient );
	mailing.addMailingRecipients( mailingRecipients );

return mailing;
}

  private ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }
  
//Coke customization start
 public Mailing buildRecognitionMailingCustom( RecognitionClaim claim, Participant recipient )
 {
   Mailing mailing = new Mailing();
   Message message = messageService.getMessageByCMAssetCode( MessageService.COKE_RECOGNIZED_RECEIVED_CUSTOM_MESSAGE_CM_ASSET_CODE );
   mailing.setGuid( GuidUtils.generateGuid() );
   mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
   mailing.setMessage( message );
   mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
   mailing.setSender( getSystemIncentiveEmailAddress() );
   Set recipients = buildMailingRecipientsForRecognitionEmail( claim, recipient, null );
   mailing.addMailingRecipients( recipients );
   return mailing;
 }

 public Mailing buildRecognitionMailingCustomOnlyPoints( RecognitionClaim claim, String award )
 {
   Mailing mailing = new Mailing();
   Message message = messageService.getMessageByCMAssetCode( MessageService.COKE_RECOGNIZED_RECEIVED_POINTS_ONLY_CUSTOM_MESSAGE_CM_ASSET_CODE );
   mailing.setGuid( GuidUtils.generateGuid() );
   mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
   mailing.setMessage( message );
   mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
   mailing.setSender( getSystemIncentiveEmailAddress() );
   Set recipients = buildMailingRecipientsForRecognitionEmail( claim, null, award );
   mailing.addMailingRecipients( recipients );
   return mailing;
 }


}
