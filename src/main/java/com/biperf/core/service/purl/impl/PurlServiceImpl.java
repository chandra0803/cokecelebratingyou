
package com.biperf.core.service.purl.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.jcodec.api.awt.FrameGrab;
import org.jcodec.common.io.ByteBufferSeekableByteChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.biperf.core.dao.activity.ActivityDAO;
import com.biperf.core.dao.merchandise.MerchOrderDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.purl.PurlContributorCommentDAO;
import com.biperf.core.dao.purl.PurlContributorDAO;
import com.biperf.core.dao.purl.PurlContributorMediaDAO;
import com.biperf.core.dao.purl.PurlRecipientDAO;
import com.biperf.core.dao.purl.hibernate.PurlContributorCommentQueryConstraint;
import com.biperf.core.dao.purl.hibernate.PurlContributorMediaQueryConstraint;
import com.biperf.core.dao.purl.hibernate.PurlContributorQueryConstraint;
import com.biperf.core.dao.purl.hibernate.PurlRecipientQueryConstraint;
import com.biperf.core.domain.activity.MerchOrderActivity;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.claim.RecognitionClaimSource;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.DivisionType;
import com.biperf.core.domain.enums.EnvironmentTypeEnum;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.PromotionEmailNotificationType;
import com.biperf.core.domain.enums.PurlCelebrationTabType;
import com.biperf.core.domain.enums.PurlContributorCommentStatus;
import com.biperf.core.domain.enums.PurlContributorMediaStatus;
import com.biperf.core.domain.enums.PurlContributorMediaType;
import com.biperf.core.domain.enums.PurlContributorState;
import com.biperf.core.domain.enums.PurlContributorVideoType;
import com.biperf.core.domain.enums.PurlMediaState;
import com.biperf.core.domain.enums.PurlRecipientState;
import com.biperf.core.domain.enums.PurlRecipientType;
import com.biperf.core.domain.enums.SupportedEcardVideoTypes;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingMessageLocale;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.mailing.MailingRecipientData;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.mtc.MTCVideo;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantInfoView;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionNotification;
import com.biperf.core.domain.promotion.PromotionNotificationType;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.purl.PurlCelebration;
import com.biperf.core.domain.purl.PurlCelebrationSet;
import com.biperf.core.domain.purl.PurlCelebrationSetCountBean;
import com.biperf.core.domain.purl.PurlCelebrationTableColumnsView;
import com.biperf.core.domain.purl.PurlCelebrationView;
import com.biperf.core.domain.purl.PurlCelebrationsView;
import com.biperf.core.domain.purl.PurlContributor;
import com.biperf.core.domain.purl.PurlContributorComment;
import com.biperf.core.domain.purl.PurlContributorMedia;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.domain.purl.PurlRecipientCustomElement;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.Role;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.exception.DataException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.process.PurlInvitePostProcess;
import com.biperf.core.process.PurlMediaUploadProcess;
import com.biperf.core.service.AssociationRequest;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.service.TranslatedContent;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.cmx.CMXService;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.facebook.FacebookService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.imageservice.ImageServiceRepositoryFactory;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.mtc.MTCVideoService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.profileavatar.ProfileAvatarRepositoryFactory;
import com.biperf.core.service.purl.PurlContributorCommentValidationException;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.purl.TranslatedPurlContributorComment;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.service.security.RoleService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.translation.TranslationService;
import com.biperf.core.service.translation.UnexpectedTranslationException;
import com.biperf.core.service.translation.UnsupportedTranslationException;
import com.biperf.core.service.twitter.TwitterService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.strategy.FileUploadStrategy;
import com.biperf.core.strategy.ImageCropStrategy;
import com.biperf.core.strategy.ImageResizeStrategy;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RosterUtil;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.CmxTranslateHelperBean;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.ProfileAvatarUploadUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PurlAwardValue;
import com.biperf.core.value.PurlContributorInviteValue;
import com.biperf.core.value.PurlFileUploadValue;
import com.biperf.core.value.PurlMediaUploadValue;
import com.biperf.core.value.contributor.ContributionsList;
import com.biperf.core.value.contributor.Contributor;
import com.biperf.core.value.contributor.Media;
import com.biperf.core.value.participant.PaxAvatarData;
import com.biperf.core.value.participant.PurlContributorAvatarData;
import com.biperf.core.value.participant.PurlContributorImageData;
import com.biperf.core.value.participant.PurlNotMigratedPaxData;
import com.biperf.core.value.recognition.PurlRecipientValue;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.ContentReaderManager;

public class PurlServiceImpl implements PurlService
{
  private static final Log logger = LogFactory.getLog( PurlServiceImpl.class );

  private static final int PURL_DAYS_TO_ARCHIVE_AFTER_EXPIRE = 60;
  private static final int MEGABYTES_TO_BYTES_MULTIPLIER = 1024 * 1024;
  public static final String GIF = "gif";

  private PurlRecipientDAO purlRecipientDAO;
  private PurlContributorDAO purlContributorDAO;
  private PurlContributorMediaDAO purlContributorMediaDAO;
  private PurlContributorCommentDAO purlContributorCommentDAO;
  private MailingService mailingService;
  private ParticipantService participantService;
  private ClaimService claimService;
  private UserService userService;
  private MerchOrderDAO merchOrderDAO;
  private ActivityDAO activityDAO;
  private FileUploadStrategy appDataDirFileUploadStrategy;
  private FileUploadStrategy webdavFileUploadStrategy;
  private ImageCropStrategy imageCropStrategy;
  private ImageResizeStrategy imageResizeStrategy;
  private SystemVariableService systemVariableService;
  private TwitterService twitterService;
  private FacebookService facebookService;
  private ProcessService processService;
  private NodeService nodeService;
  private TranslationService translationService;
  private List<String> acceptableExtentions;
  private CMAssetService cmassetService;
  private RoleService roleService;
  @Autowired
  private MTCVideoService mtcVideoService;
  @Autowired
  private ProfileAvatarRepositoryFactory profileAvatarRepositoryFactory;
  @Autowired
  private CMXService cmxService;
  private MessageService messageService;
  @Autowired
  ImageServiceRepositoryFactory imageServiceRepositoryFactory;

  private static final String UPCOMING = PurlRecipientType.UPCOMING.getCode();
  private static final String PAST = PurlRecipientType.PAST.getCode();

  public List<Long> getPurlManagerNodes( Long userId )
  {
    List<Long> ownerNodeIds = new ArrayList<Long>();

    // Find all owner nodes for the user
    List<Long> nodeIds = nodeService.findOwnerNodeIds( userId );

    // For each owner node
    // (1) find all child nodes without owner, and
    // (2) the last child node with owner
    for ( Long nodeId : nodeIds )
    {
      ownerNodeIds.addAll( findPurlManagerNodeIds( nodeId, true ) );
    }

    return ownerNodeIds;
  }

  private List<Long> findPurlManagerNodeIds( Long nodeId, boolean isFirstLevel )
  {
    List<Long> nodeIds = new ArrayList<Long>();

    // Add the current node
    nodeIds.add( nodeId );

    // If first level or node does not have an owner
    if ( isFirstLevel || !nodeService.nodeHasOwner( nodeId ) )
    {
      // Process all child nodes
      List<Long> childNodeIds = nodeService.findChildNodeIds( nodeId );
      for ( Long childNodeId : childNodeIds )
      {
        nodeIds.addAll( findPurlManagerNodeIds( childNodeId, false ) );
      }
    }

    return nodeIds;
  }

  private boolean isNodeOwnerForPurlRecipient( PurlRecipient purlRecipient, Long userId )
  {
    Participant purlManager = getNodeOwnerForPurlRecipient( purlRecipient );
    return null != purlManager && purlManager.getId().equals( userId );
  }

  public Participant getNodeOwnerForPurlRecipient( Long purlRecipientId )
  {
    PurlRecipient purlRecipient = getPurlRecipientById( purlRecipientId );
    return getNodeOwnerForPurlRecipient( purlRecipient );
  }

  public Participant getNodeOwnerForPurlRecipient( PurlRecipient purlRecipient )
  {
    if ( null == purlRecipient )
    {
      return null;
    }
    return getNodeOwnerForPurlRecipient( purlRecipient.getUser(), purlRecipient.getNode() );
  }

  public Participant getNodeOwnerForPurlRecipient( User purlRecipientUser, Node purlRecipientNode )
  {
    Long parentNodeId = null;
    if ( null == purlRecipientUser || null == purlRecipientNode )
    {
      return null;
    }

    User nodeOwner = participantService.getNodeOwner( purlRecipientNode.getId() );
    // Node does not have an owner, find owner up the level
    if ( null == nodeOwner )
    {
      nodeOwner = nodeService.getNodeOwnerByLevel( purlRecipientNode.getId() );
    }
    // PURL recipient is the node owner, find owner up the level
    else if ( nodeOwner.getId().equals( purlRecipientUser.getId() ) )
    {
      if ( null == purlRecipientNode.getParentNode() )
      {
        nodeOwner = null;
      }
      else
      {
        nodeOwner = nodeService.getNodeOwnerByLevel( purlRecipientNode.getParentNode().getId() );
        parentNodeId = purlRecipientNode.getParentNode().getId();
      }
    }

    if ( nodeOwner != null && nodeOwner.getId().equals( purlRecipientUser.getId() ) )
    {
      Node purlRecipientParentNode = nodeService.getNodeById( parentNodeId );
      nodeOwner = getNodeOwnerForPurlRecipient( nodeOwner, purlRecipientParentNode );
    }
    return (Participant)nodeOwner;
  }

  public PurlRecipient savePurlRecipient( PurlRecipient info )
  {
    return purlRecipientDAO.save( info );
  }

  public PurlRecipient createPurlRecipient( PurlRecipient info ) throws ServiceErrorException
  {
    // Create PURL recipient
    PurlRecipient attachedInfo = savePurlRecipient( info );

    RecognitionPromotion promotion = info.getPromotion();
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
          // Process only when a notification has been set up on the
          // promotion

          String notificationTypeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();

          if ( messageId > 0 && notificationTypeCode.equals( PromotionEmailNotificationType.PURL_MANAGER_NOTIFICATION ) )
          {
            // Send Manager notification
            Mailing mailing = mailingService.buildPurlManagerNotificationMailing( messageId, attachedInfo );
            if ( null != mailing )
            {
              mailingService.submitMailing( mailing, null );
            }
          }
        }
      }
    }

    return attachedInfo;
  }

  public PurlRecipient getPurlRecipientById( Long id )
  {
    return purlRecipientDAO.getPurlRecipientById( id );
  }

  public PurlRecipient getPurlRecipientByClaimIdWithAssociations( Long claimId, AssociationRequestCollection associationRequests )
  {
    PurlRecipient purlRecipient = purlRecipientDAO.getPurlRecipientByClaimId( claimId );
    if ( associationRequests != null )
    {
      associationRequests.process( purlRecipient );
    }
    return purlRecipient;
  }

  public PurlRecipient getPurlRecipientByClaimId( Long claimId )
  {
    PurlRecipient purlRecipient = purlRecipientDAO.getPurlRecipientByClaimId( claimId );
    return purlRecipient;
  }

  public void deletePurlRecipient( PurlRecipient info )
  {
    purlRecipientDAO.delete( info );
  }

  public PurlRecipient getPurlRecipientById( Long id, AssociationRequestCollection associationRequests )
  {
    PurlRecipient info = getPurlRecipientById( id );
    if ( associationRequests != null )
    {
      associationRequests.process( info );
    }
    return info;
  }

  public PurlContributor getPurlContributorById( Long id )
  {
    return purlContributorDAO.getPurlContributorById( id );
  }

  public PurlContributor getPurlContributorById( Long id, AssociationRequestCollection associationRequests )
  {
    PurlContributor info = getPurlContributorById( id );
    if ( associationRequests != null )
    {
      associationRequests.process( info );
    }
    return info;
  }

  public PurlContributor getPurlContributorByUserId( Long userId, Long purlRecipientId )
  {
    PurlContributorState[] purlContributorStates = { PurlContributorState.lookup( PurlContributorState.INVITATION ), PurlContributorState.lookup( PurlContributorState.CONTRIBUTION ) };

    PurlContributorQueryConstraint constraint = new PurlContributorQueryConstraint();
    constraint.setPurlContributorStates( purlContributorStates );
    constraint.setContributorUserId( userId );
    constraint.setPurlRecipientId( purlRecipientId );

    List<PurlContributor> list = purlContributorDAO.getContributors( constraint );
    if ( !list.isEmpty() && list.size() == 1 )
    {
      return list.get( 0 );
    }
    return null;
  }

  public PurlContributor getPurlContributorByPromotionIdandUserId( Long userId, Long purlRecipientId, Long promotionId )
  {
    PurlContributorState[] purlContributorStates = { PurlContributorState.lookup( PurlContributorState.INVITATION ), PurlContributorState.lookup( PurlContributorState.CONTRIBUTION ) };

    PurlContributorQueryConstraint constraint = new PurlContributorQueryConstraint();
    constraint.setPurlContributorStates( purlContributorStates );
    constraint.setContributorUserId( userId );
    constraint.setPurlRecipientId( purlRecipientId );
    constraint.setPromotionId( promotionId );

    List<PurlContributor> list = purlContributorDAO.getContributors( constraint );
    if ( !list.isEmpty() && list.size() == 1 )
    {
      return list.get( 0 );
    }
    return null;
  }

  public PurlContributor getPurlContributorByEmailAddr( String emailAddr, Long purlRecipientId )
  {
    PurlContributor contributor = null;
    PurlContributorState[] purlContributorStates = { PurlContributorState.lookup( PurlContributorState.INVITATION ), PurlContributorState.lookup( PurlContributorState.CONTRIBUTION ) };

    PurlContributorQueryConstraint constraint = new PurlContributorQueryConstraint();
    constraint.setPurlContributorStates( purlContributorStates );
    constraint.setPurlRecipientId( purlRecipientId );

    List<PurlContributor> list = purlContributorDAO.getContributors( constraint );

    // if more than one contributor has same email address then create a new
    // contributor otherwise
    // update the single contributor
    int matchingEmailContributorCount = 0;
    for ( PurlContributor purlContributor : list )
    {
      String contributorEmailAddress = null != purlContributor.getUser() ? getPrimaryEmailAddress( purlContributor.getUser() ) : purlContributor.getEmailAddr();
      if ( null != emailAddr && null != contributorEmailAddress && emailAddr.toLowerCase().equals( contributorEmailAddress.toLowerCase() ) )
      {
        matchingEmailContributorCount++;
        contributor = purlContributor;
      }
    }

    if ( matchingEmailContributorCount == 1 )
    {
      return contributor;
    }
    else
    {
      return null;
    }
  }

  public PurlContributor savePurlContributor( PurlContributor contributor )
  {
    // Set the AuditModifyInfo date explicitly for Non-Pax contributors
    // which will be used for visit
    // date
    if ( !UserManager.isUserLoggedIn() )
    {
      contributor.getAuditUpdateInfo().setDateModified( new Timestamp( System.currentTimeMillis() ) );
    }
    return purlContributorDAO.save( contributor );
  }

  public void deletePurlContributor( PurlContributor contributor )
  {
    purlContributorDAO.delete( contributor );
  }

  public void deletePurlContributor( Long purlContributorId )
  {
    PurlContributor contributor = purlContributorDAO.getPurlContributorById( purlContributorId );
    if ( null != contributor )
    {
      purlContributorDAO.delete( contributor );
    }
  }

  public List<PurlRecipient> getAllPurlInvitationsForManager( Long userId, Long promotionId )
  {
    return getAllPurlInvitationsForManager( userId, promotionId, null );
  }

  public List<PurlRecipient> getAllPurlInvitationsForManager( Long userId, Long promotionId, AssociationRequestCollection collectionReq )
  {
    List<PurlRecipient> purlRecipients = getPurlInvitationsForManager( userId, promotionId );
    if ( collectionReq != null )
    {
      for ( PurlRecipient purlRecipient : purlRecipients )
      {
        collectionReq.process( purlRecipient );
      }
    }
    return purlRecipients;
  }

  private List<PurlRecipient> getPurlInvitationsForManager( Long userId, Long promotionId )
  {
    PurlRecipientState[] purlRecipientStates = { PurlRecipientState.lookup( PurlRecipientState.INVITATION ), PurlRecipientState.lookup( PurlRecipientState.CONTRIBUTION ) };

    List<Long> purlManagerNodes = getPurlManagerNodes( userId );
    PurlRecipientQueryConstraint constraint = new PurlRecipientQueryConstraint();
    constraint.setPurlRecipientStates( purlRecipientStates );
    if ( promotionId != null )
    {
      constraint.setPromotionId( promotionId );
    }
    constraint.setNodeIds( purlManagerNodes );

    List<PurlRecipient> list = purlRecipientDAO.getPurlRecipientList( constraint );
    return filterManagerPurls( list, userId );
  }

  private List<PurlRecipient> filterManagerPurls( List<PurlRecipient> list, Long userId )
  {
    if ( !list.isEmpty() )
    {
      Iterator<PurlRecipient> iList = list.iterator();
      while ( iList.hasNext() )
      {
        PurlRecipient purlRecipient = iList.next();
        if ( !purlRecipient.getUser().isActive() )
        {
          iList.remove();
        }
        else if ( !purlRecipient.getCloseDate().after( new Date() ) )
        {
          iList.remove();
        }
        else if ( !isNodeOwnerForPurlRecipient( purlRecipient, userId ) )
        {
          iList.remove();
        }
      }
    }
    return list;
  }

  private List<PurlContributor> filterContributionPurls( List<PurlContributor> list )
  {
    if ( !list.isEmpty() )
    {
      Iterator<PurlContributor> iList = list.iterator();
      while ( iList.hasNext() )
      {
        if ( !isValidForContribution( iList.next() ) )
        {
          iList.remove();
        }
      }
    }
    return list;
  }

  private List<PurlRecipient> filterRecipientPurls( List<PurlRecipient> list )
  {
    int daysToExpire = systemVariableService.getPropertyByName( SystemVariableService.PURL_DAYS_TO_EXP ).getIntVal();
    if ( !list.isEmpty() )
    {
      Iterator<PurlRecipient> iList = list.iterator();
      while ( iList.hasNext() )
      {
        PurlRecipient recipient = iList.next();
        if ( !recipient.getUser().isActive() )
        {
          iList.remove();
        }
        else
        {
          Date cutoffDate = new Date( recipient.getAwardDate().getTime() + daysToExpire * org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY );
          if ( cutoffDate.before( new Date() ) )
          {
            iList.remove();
          }
        }
      }
    }
    return list;
  }

  private List<PurlRecipient> filterPendingInvitationPurls( List<PurlRecipient> list )
  {
    if ( !list.isEmpty() )
    {
      Iterator<PurlRecipient> iList = list.iterator();
      while ( iList.hasNext() )
      {
        PurlRecipient purlRecipient = iList.next();
        if ( !purlRecipient.getUser().isActive() )
        {
          iList.remove();
        }
      }
    }
    return list;
  }

  private List<PurlContributor> filterPendingContributionPurls( List<PurlContributor> list )
  {
    if ( !list.isEmpty() )
    {
      Iterator<PurlContributor> iList = list.iterator();
      while ( iList.hasNext() )
      {
        if ( isContributionsAvailable( iList.next() ) )
        {
          iList.remove();
        }
      }
    }
    return list;
  }

  public List<PurlRecipient> getAllPendingPurlInvitations( Long promotionId )
  {
    PurlRecipientState[] purlRecipientStates = { PurlRecipientState.lookup( PurlRecipientState.INVITATION ), PurlRecipientState.lookup( PurlRecipientState.CONTRIBUTION ) };

    PurlRecipientQueryConstraint constraint = new PurlRecipientQueryConstraint();
    constraint.setPurlRecipientStates( purlRecipientStates );
    constraint.setPromotionId( promotionId );
    List<PurlRecipient> list = purlRecipientDAO.getPurlRecipientList( constraint );
    return filterPendingInvitationPurls( list );
  }

  public List<PurlRecipient> getAllPendingPurlInvitationsForManager( Long userId, Long promotionId )
  {
    List<Long> purlManagerNodes = getPurlManagerNodes( userId );
    return getAllPendingPurlInvitationsForManager( userId, promotionId, purlManagerNodes );
  }

  public List<PurlRecipient> getAllPendingPurlInvitationsForManager( Long userId, Long promotionId, List<Long> purlManagerNodes )
  {
    PurlRecipientState[] purlRecipientStates = { PurlRecipientState.lookup( PurlRecipientState.INVITATION ), PurlRecipientState.lookup( PurlRecipientState.CONTRIBUTION ) };

    PurlRecipientQueryConstraint constraint = new PurlRecipientQueryConstraint();
    constraint.setPurlRecipientStates( purlRecipientStates );
    constraint.setPromotionId( promotionId );
    constraint.setNodeIds( purlManagerNodes );

    List<PurlRecipient> list = purlRecipientDAO.getPurlRecipientList( constraint );
    List<PurlRecipient> filteredListForManager = filterManagerPurls( list, userId );
    return filterPendingInvitationPurls( filteredListForManager );
  }

  public List<PurlRecipient> getAllCurrentPurlInvitationsForManager( Long userId, Long promotionId )
  {
    List<Long> purlManagerNodes = getPurlManagerNodes( userId );
    return getAllCurrentPurlInvitationsForManager( userId, promotionId, purlManagerNodes );
  }

  public List<PurlRecipient> getAllCurrentPurlInvitationsForManager( Long userId, Long promotionId, List<Long> purlManagerNodes )
  {
    PurlRecipientState[] purlRecipientStates = { PurlRecipientState.lookup( PurlRecipientState.CONTRIBUTION ) };

    PurlRecipientQueryConstraint constraint = new PurlRecipientQueryConstraint();
    constraint.setPurlRecipientStates( purlRecipientStates );
    constraint.setPromotionId( promotionId );
    constraint.setNodeIds( purlManagerNodes );

    List<PurlRecipient> list = purlRecipientDAO.getPurlRecipientList( constraint );
    return filterManagerPurls( list, userId );
  }

  public List<PurlContributor> getAllPurlContributions( Long userId, Long promotionId, boolean isDefaultInvitee )
  {
    PurlContributorState[] purlContributorStates = { PurlContributorState.lookup( PurlContributorState.INVITATION ), PurlContributorState.lookup( PurlContributorState.CONTRIBUTION ) };

    PurlContributorQueryConstraint constraint = new PurlContributorQueryConstraint();
    constraint.setPurlContributorStates( purlContributorStates );
    constraint.setContributorUserId( userId );
    constraint.setPromotionId( promotionId );
    constraint.setDefaultInvitee( isDefaultInvitee );

    List<PurlContributor> list = purlContributorDAO.getContributors( constraint );
    return filterContributionPurls( list );
  }

  public List<PurlContributor> getAllPurlContributions( Long userId, Long promotionId, boolean isDefaultInvitee, AssociationRequestCollection collectionReq )
  {
    List<PurlContributor> list = getAllPurlContributions( userId, promotionId, isDefaultInvitee );
    if ( collectionReq != null )
    {
      for ( PurlContributor purlContributor : list )
      {
        collectionReq.process( purlContributor );
      }
    }
    return list;
  }

  public List<PurlContributor> getAllPurlContributions( Long purlRecipientId, AssociationRequestCollection collectionReq )
  {
    PurlContributorState[] purlContributorStates = { PurlContributorState.lookup( PurlContributorState.INVITATION ), PurlContributorState.lookup( PurlContributorState.CONTRIBUTION ) };
    PurlContributorQueryConstraint constraint = new PurlContributorQueryConstraint();
    constraint.setPurlContributorStates( purlContributorStates );
    constraint.setPurlRecipientId( purlRecipientId );
    List<PurlContributor> list = purlContributorDAO.getContributors( constraint );
    list = filterContributionPurls( list );
    if ( collectionReq != null )
    {
      for ( PurlContributor purlContributor : list )
      {
        collectionReq.process( purlContributor );
      }
    }
    return list;
  }

  public List<PurlContributor> getAllPendingPurlContributions( Long promotionId )
  {
    PurlContributorState[] purlContributorStates = { PurlContributorState.lookup( PurlContributorState.INVITATION ), PurlContributorState.lookup( PurlContributorState.CONTRIBUTION ) };

    PurlContributorQueryConstraint constraint = new PurlContributorQueryConstraint();
    constraint.setPurlContributorStates( purlContributorStates );
    constraint.setPromotionId( promotionId );

    List<PurlContributor> list = purlContributorDAO.getContributors( constraint );
    List<PurlContributor> filteredListForContribution = filterContributionPurls( list );
    return filterPendingContributionPurls( filteredListForContribution );
  }

  public List<PurlContributor> getAllPendingPurlContributions( Long userId, Long promotionId )
  {
    return getAllPendingPurlContributions( userId, promotionId, false );
  }

  public List<PurlContributor> getAllPendingPurlContributions( Long userId, Long promotionId, boolean excludeSelfManagerContributions )
  {
    PurlContributorState[] purlContributorStates = { PurlContributorState.lookup( PurlContributorState.INVITATION ), PurlContributorState.lookup( PurlContributorState.CONTRIBUTION ) };
    
    PurlContributorQueryConstraint constraint = new PurlContributorQueryConstraint();
    constraint.setPurlContributorStates( purlContributorStates );
    constraint.setContributorUserId( userId );
    constraint.setPromotionId( promotionId );

    List<PurlContributor> list = purlContributorDAO.getContributors( constraint );
    list = filterContributionPurls( list );
    list = filterPendingContributionPurls( list );
    if ( excludeSelfManagerContributions )
    {
      list = filterSelfManagerContributionPurls( list );
    }
    return list;
  }

  @Override
  public List<PurlContributor> getAllPendingPurlContributionsForDefaultInvitee( Long userId, Long promotionId, boolean excludeSelfManagerContributions )
  {
    PurlContributorState[] purlContributorStates = { PurlContributorState.lookup( PurlContributorState.INVITATION ), PurlContributorState.lookup( PurlContributorState.CONTRIBUTION ) };
    boolean isAllowPurlContributionsToSeeOthers = true;
  	
    PurlContributorQueryConstraint constraint = new PurlContributorQueryConstraint();
    constraint.setPurlContributorStates( purlContributorStates );
    constraint.setContributorUserId( userId );
    constraint.setPromotionId( promotionId );
    constraint.setDefaultInvitee( true );

    List<PurlContributor> list = purlContributorDAO.getContributors( constraint );
    list = filterContributionPurls( list );
    list = filterPendingContributionPurls( list );
    Iterator<PurlContributor> iList = list.iterator();
    while ( iList.hasNext() )
    {
    	PurlContributor	purlContributor =(PurlContributor)iList.next();
    	Participant participant = (Participant) purlContributor.getPurlRecipient().getUser();
    	isAllowPurlContributionsToSeeOthers = participant.isAllowPurlContributionsToSeeOthers();
    }
      
    if ( excludeSelfManagerContributions && isAllowPurlContributionsToSeeOthers)
    {
      list = filterSelfManagerContributionPurls( list );
    }
    return list;
  }

  @Override
  public List<PurlContributor> getAllPendingPurlContributionsForNonDefaultInvitee( Long userId, Long promotionId, boolean excludeSelfManagerContributions )
  {
    PurlContributorState[] purlContributorStates = { PurlContributorState.lookup( PurlContributorState.INVITATION ), PurlContributorState.lookup( PurlContributorState.CONTRIBUTION ) };

    PurlContributorQueryConstraint constraint = new PurlContributorQueryConstraint();
    constraint.setPurlContributorStates( purlContributorStates );
    constraint.setContributorUserId( userId );
    constraint.setPromotionId( promotionId );
    constraint.setDefaultInvitee( false );

    List<PurlContributor> list = purlContributorDAO.getContributors( constraint );
    list = filterContributionPurls( list );
    list = filterPendingContributionPurls( list );
    if ( excludeSelfManagerContributions )
    {
      list = filterSelfManagerContributionPurls( list );
    }
    return list;
  }

  public List<PurlContributor> getAllCurrentPurlContributions( Long userId, Long promotionId, boolean excludeSelfManagerContributions )
  {
    PurlContributorState[] purlContributorStates = { PurlContributorState.lookup( PurlContributorState.CONTRIBUTION ) };

    PurlContributorQueryConstraint constraint = new PurlContributorQueryConstraint();
    constraint.setPurlContributorStates( purlContributorStates );
    constraint.setContributorUserId( userId );
    constraint.setPromotionId( promotionId );

    List<PurlContributor> list = purlContributorDAO.getContributors( constraint );
    list = filterContributionPurls( list );
    if ( excludeSelfManagerContributions )
    {
      list = filterSelfManagerContributionPurls( list );
    }
    return list;
  }

  private List<PurlContributor> filterSelfManagerContributionPurls( List<PurlContributor> list )
  {
    if ( !list.isEmpty() )
    {
      Iterator<PurlContributor> iList = list.iterator();
      while ( iList.hasNext() )
      {
        if ( isSelfManagerContribution( iList.next() ) )
        {
          iList.remove();
        }
      }
    }
    return list;
  }

  private boolean isSelfManagerContribution( PurlContributor purlContributor )
  {
    return isNodeOwnerForPurlRecipient( purlContributor.getPurlRecipient(), purlContributor.getUser().getId() );
  }

  public boolean isValidForContribution( Long purlContributorId )
  {
    PurlContributor contributor = getPurlContributorById( purlContributorId );
    return isValidForContribution( contributor );
  }

  private boolean isValidForContribution( PurlContributor contributor )
  {
    if ( null == contributor || contributor.getPurlRecipient() == null || !contributor.getPurlRecipient().getUser().isActive() )
    {
      return false;
    }
    Date closeDate = contributor.getPurlRecipient().getCloseDate();
    return closeDate.after( new Date() );
  }

  public List<PurlContributorMedia> getPhotoUploads( Long purlRecipientId )
  {
    PurlContributorMediaStatus status = PurlContributorMediaStatus.lookup( PurlContributorMediaStatus.ACTIVE );
    PurlContributorMediaType mediaType = PurlContributorMediaType.lookup( PurlContributorMediaType.PICTURE );

    PurlContributorMediaQueryConstraint constraint = new PurlContributorMediaQueryConstraint();
    constraint.setPurlRecipientId( purlRecipientId );
    constraint.setPurlContributorMediaTypeIncluded( new PurlContributorMediaType[] { mediaType } );
    constraint.setPurlContributorMediaStatusTypesIncluded( new PurlContributorMediaStatus[] { status } );
    return purlContributorMediaDAO.getMediaUploads( constraint );
  }

  public List<PurlContributorMedia> getVideoUploads( Long purlRecipientId )
  {
    PurlContributorMediaStatus status = PurlContributorMediaStatus.lookup( PurlContributorMediaStatus.ACTIVE );
    PurlContributorMediaType[] mediaTypes = { PurlContributorMediaType.lookup( PurlContributorMediaType.VIDEO ), PurlContributorMediaType.lookup( PurlContributorMediaType.VIDEO_URL ) };

    PurlContributorMediaQueryConstraint constraint = new PurlContributorMediaQueryConstraint();
    constraint.setPurlRecipientId( purlRecipientId );
    constraint.setPurlContributorMediaTypeIncluded( mediaTypes );
    constraint.setPurlContributorMediaStatusTypesIncluded( new PurlContributorMediaStatus[] { status } );
    return purlContributorMediaDAO.getMediaUploads( constraint );
  }

  public List<PurlContributorComment> getComments( Long purlRecipientId )
  {
    return getComments( purlRecipientId, false, 0, 0 );
  }

  public List<PurlContributorComment> getComments( Long purlRecipientId, boolean orderDescending, int rowNumStart, int rowNumEnd )
  {
    PurlContributorCommentStatus status = PurlContributorCommentStatus.lookup( PurlContributorCommentStatus.ACTIVE );
    PurlContributorCommentQueryConstraint constraint = new PurlContributorCommentQueryConstraint();
    constraint.setPurlRecipientId( purlRecipientId );
    constraint.setPurlContributorCommentStatusTypesIncluded( new PurlContributorCommentStatus[] { status } );
    constraint.setOrderDescending( orderDescending );
    constraint.setRowNumStart( rowNumStart );
    constraint.setRowNumEnd( rowNumEnd );
    // constraint.setClaimId( claimId );
    return purlContributorCommentDAO.getComments( constraint );
  }

  public int getPhotoUploadCount( Long purlRecipientId )
  {
    PurlContributorMediaType mediaType = PurlContributorMediaType.lookup( PurlContributorMediaType.PICTURE );

    PurlContributorMediaQueryConstraint constraint = new PurlContributorMediaQueryConstraint();
    constraint.setPurlRecipientId( purlRecipientId );
    constraint.setPurlContributorMediaTypeIncluded( new PurlContributorMediaType[] { mediaType } );
    return purlContributorMediaDAO.getMediaUploadCount( constraint );
  }

  public int getVideoUploadCount( Long purlRecipientId )
  {
    PurlContributorMediaType[] mediaTypes = { PurlContributorMediaType.lookup( PurlContributorMediaType.VIDEO ), PurlContributorMediaType.lookup( PurlContributorMediaType.VIDEO_URL ) };

    PurlContributorMediaQueryConstraint constraint = new PurlContributorMediaQueryConstraint();
    constraint.setPurlRecipientId( purlRecipientId );
    constraint.setPurlContributorMediaTypeIncluded( mediaTypes );
    return purlContributorMediaDAO.getMediaUploadCount( constraint );
  }

  public int getCommentCount( Long purlRecipientId )
  {
    PurlContributorCommentQueryConstraint constraint = new PurlContributorCommentQueryConstraint();
    constraint.setPurlRecipientId( purlRecipientId );
    return purlContributorCommentDAO.getCommentCount( constraint );
  }

  public int getPurlCountributorCount( Long promotionId )
  {
    PurlContributorQueryConstraint constraint = new PurlContributorQueryConstraint();
    constraint.setPromotionId( promotionId );
    return purlContributorDAO.getContributorCount( constraint );
  }

  public PurlContributorMedia getPurlContributorMediaById( Long id )
  {
    return purlContributorMediaDAO.getPurlContributorMediaById( id );
  }

  public PurlContributorComment getPurlContributorCommentById( Long id )
  {
    return purlContributorCommentDAO.getPurlContributorCommentById( id );
  }

  public PurlContributorMedia savePurlContributorMedia( PurlContributorMedia media )
  {
    return purlContributorMediaDAO.save( media );
  }

  public PurlContributorComment savePurlContributorComment( PurlContributorComment comment )
  {
    return purlContributorCommentDAO.save( comment );
  }

  public List<PurlRecipient> getAllCompletedPurlRecipients( Long userId, Long promotionId )
  {
    PurlRecipientState[] purlRecipientStates = { PurlRecipientState.lookup( PurlRecipientState.COMPLETE ), PurlRecipientState.lookup( PurlRecipientState.RECOGNITION ) };

    PurlRecipientQueryConstraint constraint = new PurlRecipientQueryConstraint();
    constraint.setPurlRecipientStates( purlRecipientStates );
    constraint.setUserId( userId );
    constraint.setPromotionId( promotionId );

    List<PurlRecipient> list = purlRecipientDAO.getPurlRecipientList( constraint );
    return filterRecipientPurls( list );
  }

  public List<PurlRecipient> getAllPendingPurlRecipients( Long userId, Long promotionId )
  {
    PurlRecipientState[] purlRecipientStates = { PurlRecipientState.lookup( PurlRecipientState.RECOGNITION ) };

    PurlRecipientQueryConstraint constraint = new PurlRecipientQueryConstraint();
    constraint.setPurlRecipientStates( purlRecipientStates );
    constraint.setUserId( userId );
    constraint.setPromotionId( promotionId );

    List<PurlRecipient> list = purlRecipientDAO.getPurlRecipientList( constraint );
    return filterRecipientPurls( list );
  }

  public List<PurlRecipient> getAllCurrentPurlRecipients( Long userId, Long promotionId )
  {
    PurlRecipientState[] purlRecipientStates = { PurlRecipientState.lookup( PurlRecipientState.COMPLETE ) };

    PurlRecipientQueryConstraint constraint = new PurlRecipientQueryConstraint();
    constraint.setPurlRecipientStates( purlRecipientStates );
    constraint.setUserId( userId );
    constraint.setPromotionId( promotionId );

    List<PurlRecipient> list = purlRecipientDAO.getPurlRecipientList( constraint );
    return filterRecipientPurls( list );
  }

  public List<PurlRecipient> getAllPurlRecipientsForAwardIssuance()
  {
    PurlRecipientState[] purlRecipientStates = { PurlRecipientState.lookup( PurlRecipientState.INVITATION ), PurlRecipientState.lookup( PurlRecipientState.CONTRIBUTION ) };

    PurlRecipientQueryConstraint constraint = new PurlRecipientQueryConstraint();
    constraint.setPurlRecipientStates( purlRecipientStates );
    constraint.setDate( new Date() );
    constraint.setAfter( Boolean.TRUE );
    return purlRecipientDAO.getPurlRecipientList( constraint );
  }

  public List<PurlRecipient> getAllNonExpiredPurlRecipients( Long userId, Long promotionId )
  {
    PurlRecipientState[] purlRecipientStates = { PurlRecipientState.lookup( PurlRecipientState.INVITATION ),
                                                 PurlRecipientState.lookup( PurlRecipientState.CONTRIBUTION ),
                                                 PurlRecipientState.lookup( PurlRecipientState.COMPLETE ),
                                                 PurlRecipientState.lookup( PurlRecipientState.RECOGNITION ) };

    PurlRecipientQueryConstraint constraint = new PurlRecipientQueryConstraint();
    constraint.setPurlRecipientStates( purlRecipientStates );
    constraint.setUserId( userId );
    constraint.setPromotionId( promotionId );

    return purlRecipientDAO.getPurlRecipientList( constraint );
  }

  public PurlAwardValue processAward( Long purlRecipientId ) throws ServiceErrorException
  {
    PurlAwardValue purlAwardValue = new PurlAwardValue();
    PurlRecipient purlRecipient = getPurlRecipientById( purlRecipientId );

    if ( !isPurlValidForAward( purlRecipient ) )
    {
      throw new ServiceErrorException( "Purl invalid for award processing" );
    }
    RecognitionPromotion promotion = purlRecipient.getPromotion();
    boolean includeCelebrations = promotion.isIncludeCelebrations();
    boolean contributionsAvailable = isContributionsAvailable( purlRecipient );
    Long purlRecipientMessageId = getPurlRecipientEmailMessageId( purlRecipient.getPromotion() );
    // if it is celebration recognition then celebration purl recipient
    // message is used
    boolean skipStandardRecognition = contributionsAvailable && ( includeCelebrations || !includeCelebrations && null != purlRecipientMessageId ) || !contributionsAvailable && includeCelebrations;

    // Submit the claim
    Claim claim = buildClaim( purlRecipient, skipStandardRecognition );
    claimService.saveClaim( claim, null, null, false, true );
    purlAwardValue.setStandardEmailSent( true );

    // Set the Claim ID into the recipient
    purlRecipient.setClaim( claim );
    savePurlRecipient( purlRecipient );

    logger.error( "purlAwardProcess claimId=" + claim.getId() + " includeCelebrations=" + includeCelebrations + " skipStandardRecognition=" + skipStandardRecognition + " contributionsAvailable="
        + contributionsAvailable + " purlRecipientMessageId=" + purlRecipientMessageId );

    if ( purlRecipient.getAwardAmount() != null )
    {
      purlAwardValue.setTotalPointsDeposited( purlRecipient.getAwardAmount().intValue() );
    }
    else if ( purlRecipient.getAwardLevel() != null )
    {
      purlAwardValue.setNumberOfLevels( true );
    }

    // If contributions available, email PURL URL
    if ( contributionsAvailable )
    {
      // Change the status for the recipient
      purlRecipient.setState( PurlRecipientState.lookup( PurlRecipientState.RECOGNITION ) );
      savePurlRecipient( purlRecipient );

      // Change the status for all contributors
      for ( PurlContributor purlContributor : purlRecipient.getContributors() )
      {
        PurlContributor contributor = getPurlContributorById( purlContributor.getId() );
        contributor.setState( PurlContributorState.lookup( PurlContributorState.COMPLETE ) );
        savePurlContributor( contributor );
      }

      if ( !includeCelebrations )
      {
        if ( null != purlRecipientMessageId )
        {
          Mailing mailing = mailingService.buildPurlRecipientInvitationMailing( purlRecipientMessageId.longValue(), purlRecipient );
          mailingService.submitMailing( mailing, null );
        }
      }
      else
      {
        if ( !promotion.getAwardType().isMerchandiseAwardType() )
        {
          Participant participant = participantService.getParticipantById( purlRecipient.getUser().getId() );
          Mailing mailing = mailingService.buildRecognitionCelebrationMailing( (RecognitionClaim)claim, participant, true );
          mailingService.submitMailing( mailing, null );
        }
      }
      purlAwardValue.setPurlEmailSent( true );
    }
    else if ( !contributionsAvailable && includeCelebrations )
    {
      if ( !promotion.getAwardType().isMerchandiseAwardType() )
      {
        Participant participant = participantService.getParticipantById( purlRecipient.getUser().getId() );
        Mailing mailing = mailingService.buildRecognitionCelebrationMailing( (RecognitionClaim)claim, participant, false );
        mailingService.submitMailing( mailing, null );
      }
      processExpirePurl( purlRecipient.getId() );
    }
    else
    {
      // Expire it as it is not valid
      processExpirePurl( purlRecipient.getId() );
    }

    return purlAwardValue;
  }

  private Long getPurlRecipientEmailMessageId( RecognitionPromotion promotion )
  {
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
          // Process only when a notification has been set up on the
          // promotion

          String notificationTypeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();

          if ( messageId > 0 && notificationTypeCode.equals( PromotionEmailNotificationType.PURL_RECIPIENT_INVITATION ) )
          {
            return new Long( messageId );
          }
        }
      }
    }
    return null;
  }

  private boolean isContributionsAvailable( PurlRecipient purlRecipient )
  {
    for ( PurlContributor purlContributor : purlRecipient.getContributors() )
    {
      if ( isContributionsAvailable( purlContributor ) )
      {
        return true;
      }
    }
    return false;
  }

  private boolean isContributionsAvailable( PurlContributor purlContributor )
  {
    return null != purlContributor && !purlContributor.getComments().isEmpty();
  }

  private boolean isPurlValidForAward( PurlRecipient purlRecipient )
  {
    if ( !purlRecipient.getPromotion().isLive() )
    {
      return false;
    }

    if ( !purlRecipient.getPromotion().isDateValidForSubmission( new Date() ) )
    {
      return false;
    }

    if ( null != purlRecipient.getClaim() )
    {
      return false;
    }

    if ( !purlRecipient.getUser().isActive() )
    {
      return false;
    }

    return PurlRecipientState.INVITATION.equals( purlRecipient.getState().getCode() ) || PurlRecipientState.CONTRIBUTION.equals( purlRecipient.getState().getCode() );
  }

  private Claim buildClaim( PurlRecipient purlRecipient, boolean skipStandardRecognitionEmail ) throws ServiceErrorException
  {
    RecognitionPromotion promotion = purlRecipient.getPromotion();
    Participant submitter = getPurlSubmitter( purlRecipient );
    Node submitterNode = getPurlSubmitterNode( purlRecipient );
    if ( null == submitter )
    {
      throw new ServiceErrorException( "Unable to find submitter for PURL as there are NO owners" );
    }

    RecognitionClaim claim = new RecognitionClaim();
    ClaimRecipient claimRecipient = new ClaimRecipient();
    Participant recipient = (Participant)purlRecipient.getUser();

    if ( null != purlRecipient.getAwardLevel() )
    {
      claimRecipient.setPromoMerchProgramLevel( purlRecipient.getAwardLevel() );
      // Populate Promo Merch country to avoid nullPointerException
      claimRecipient.setPromoMerchCountry( promotion.getPromoMerchCountryForCountryCode( recipient.getPrimaryCountryCode() ) );
    }
    else if ( null != purlRecipient.getAwardAmount() )
    {
      // If the recipient is opted out then we need to set the award quantity to 0
      if ( recipient.getOptOutAwards() )
      {
        claimRecipient.setAwardQuantity( 0L );
      }
      else
      {
        claimRecipient.setAwardQuantity( new Long( purlRecipient.getAwardAmount().longValue() ) );
      }
    }
    Node recipientNode = purlRecipient.getNode();
    claimRecipient.setRecipient( recipient );
    claimRecipient.setNode( recipientNode );
    claimRecipient.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.PENDING ) );
    claim.addClaimRecipient( claimRecipient );

    if ( promotion.isIncludeCelebrations() )
    {
      claim.setCopyManager( true ); // if celebration purl promotion then
      // copy manager
    }
    else
    {
      claim.setCopyManager( promotion.isCopyRecipientManager() ); // Send an
      // email
      // copy
      // to
      // manager
      // based
      // on
      // promotion
      // settings
    }
    claim.setSubmissionDate( new Timestamp( DateUtils.getCurrentDateAsLong() ) );
    claim.setPromotion( promotion );
    claim.setOpen( true );
    claim.setCopySender( false ); // Do not send an email copy to sender, as
    // it would flood senders
    // email box
    claim.setSubmitter( submitter );
    claim.setNode( submitterNode );
    claim.setCard( null );
    claim.setSubmitterComments( null ); // This is required, otherwise the
    // Activity History throws
    // NPE
    claim.setSubmitterCommentsLanguageType( userService.getPreferredLanguageFor( submitter ) );
    claim.setSkipStandardRecognitionEmail( skipStandardRecognitionEmail );

    claim.setAnniversaryNumberOfDays( purlRecipient.getAnniversaryNumberOfDays() );
    claim.setAnniversaryNumberOfYears( purlRecipient.getAnniversaryNumberOfYears() );
    claim.setSource( RecognitionClaimSource.WEB );
    // Set Proxy details
    if ( null != purlRecipient.getProxyUser() )
    {
      claim.setProxyUser( purlRecipient.getProxyUser() );
    }

    for ( PurlRecipientCustomElement customElement : purlRecipient.getCustomElements() )
    {
      ClaimElement claimElement = new ClaimElement();
      claimElement.setClaimFormStepElement( customElement.getClaimFormStepElement() );
      claimElement.setValue( customElement.getValue() );
      claim.addClaimElement( claimElement );
    }

    claim.setTeamId( claimService.getNextTeamId() );

    if ( promotion.isIncludeCelebrations() && promotion.isAllowOwnerMessage() )
    {
      claim.setCelebrationManagerMessage( purlRecipient.getCelebrationManagerMessage() );
    }
    return claim;
  }

  private Participant getPurlSubmitter( PurlRecipient purlRecipient )
  {
    Participant submitter = purlRecipient.getSubmitter();
    // Backward compatibility
    if ( null == submitter )
    {
      submitter = getNodeOwnerForPurlRecipient( purlRecipient );
    }
    return submitter;
  }

  private Node getPurlSubmitterNode( PurlRecipient purlRecipient )
  {
    Node submitterNode = purlRecipient.getSubmitterNode();
    // Backward compatibility
    if ( null == submitterNode )
    {
      submitterNode = purlRecipient.getNode();
    }
    return submitterNode;
  }

  public String getGiftcodeForRecipient( Long purlRecipientId )
  {
    PurlRecipient purlRecipient = getPurlRecipientById( purlRecipientId );

    // Get merch order for claim and recipient
    List activities = activityDAO.getMerchOrderActivitiesByClaimAndUserId( purlRecipient.getClaim().getId(), purlRecipient.getUser().getId() );

    // If list is empty, it might be a point award
    if ( activities.isEmpty() )
    {
      return null;
    }

    // Get the first merch order and return the giftcode(There should be
    // ONLY one merch order)
    MerchOrderActivity activity = (MerchOrderActivity)activities.get( 0 );
    MerchOrder merchOrder = merchOrderDAO.getMerchOrderById( activity.getMerchOrder().getId() );
    return merchOrder.getFullGiftCode();
  }

  public List<PurlContributorInviteValue> sendContributorInvitationByManager( Long purlRecipientId, List<PurlContributorInviteValue> inviteList, boolean saveForLater )
  {
    List<PurlContributor> purlContributorEmailList = new ArrayList<PurlContributor>();
    PurlRecipient purlRecipient = getPurlRecipientById( purlRecipientId );
    Participant nodeOwner = getNodeOwnerForPurlRecipient( purlRecipient );

    // Add contributors
    for ( PurlContributorInviteValue invite : inviteList )
    {
      try
      {
        if ( saveForLater )
        {
          invite.setSendLater( true );
        }
        invite = sendContributorInvitationByManager( purlRecipient, invite, nodeOwner, purlContributorEmailList );
      }
      catch( Throwable e )
      {
        invite.setStatus( PurlContributorInviteValue.STATUS_FAIL );
      }
    }
    if ( !purlContributorEmailList.isEmpty() )
    {
      Process process = processService.createOrLoadSystemProcess( PurlInvitePostProcess.PROCESS_NAME, PurlInvitePostProcess.BEAN_NAME );
      LinkedHashMap<String, Object> parameterValueMap = new LinkedHashMap<String, Object>();
      parameterValueMap.put( "purlRecipientId", purlRecipient.getId() );
      parameterValueMap.put( "purlContributorEmailList", purlContributorEmailList );
      parameterValueMap.put( "nonContributorUserId", null );
      processService.launchProcess( process, parameterValueMap, UserManager.getUserId() );
    }
    return inviteList;
  }

  public List<PurlContributorInviteValue> sendContributorInvitationByContributor( List<PurlContributorInviteValue> contributorInvites, Long purlContributorId )
  {
    List<PurlContributor> purlContributorEmailList = new ArrayList<PurlContributor>();
    PurlContributor invitingContributor = getPurlContributorById( purlContributorId );
    PurlRecipient purlRecipient = invitingContributor.getPurlRecipient();

    for ( PurlContributorInviteValue invite : contributorInvites )
    {
      try
      {
        invite = sendContributorInvitationByContributor( purlRecipient, invite, invitingContributor, purlContributorEmailList );
      }
      catch( Throwable e )
      {
        logger.error( e );
        invite.setStatus( PurlContributorInviteValue.STATUS_FAIL );
      }
    }
    if ( !purlContributorEmailList.isEmpty() )
    {
      Long runByUserId = UserManager.getUserId();
      if ( runByUserId == null )
      {
        runByUserId = purlRecipient.getSubmitter().getId();
      }
      Process process = processService.createOrLoadSystemProcess( PurlInvitePostProcess.PROCESS_NAME, PurlInvitePostProcess.BEAN_NAME );
      LinkedHashMap<String, Object> parameterValueMap = new LinkedHashMap<String, Object>();
      parameterValueMap.put( "purlRecipientId", purlRecipient.getId() );
      parameterValueMap.put( "purlContributorEmailList", purlContributorEmailList );
      parameterValueMap.put( "nonContributorUserId", null );
      processService.launchProcess( process, parameterValueMap, runByUserId );
    }
    return contributorInvites;
  }

  private PurlContributorInviteValue sendContributorInvitationByManager( PurlRecipient purlRecipient,
                                                                         PurlContributorInviteValue invite,
                                                                         Participant nodeOwner,
                                                                         List<PurlContributor> purlContributorEmailList )
  {
    // Disable displaying default contributors
    if ( purlRecipient.isShowDefaultContributors() )
    {
      purlRecipient.setShowDefaultContributors( false );
      purlRecipient = savePurlRecipient( purlRecipient );
    }

    PurlContributor invitingContributor = null;
    if ( null != nodeOwner )
    {
      invitingContributor = getManagerContribution( purlRecipient, nodeOwner );
      if ( invitingContributor == null )
      {
        // Construct manager contributor object
        invitingContributor = new PurlContributor();
        invitingContributor.setState( PurlContributorState.lookup( PurlContributorState.INVITATION ) );
        invitingContributor.setUser( nodeOwner );
        invitingContributor.setFirstName( nodeOwner.getFirstName() );
        invitingContributor.setLastName( nodeOwner.getLastName() );
        invitingContributor.setEmailAddr( getPrimaryEmailAddress( nodeOwner ) );
        invitingContributor.setDefaultInvitee( true );
        // Add manager as contributor
        purlRecipient.addContributor( invitingContributor );
      }
    }

    return sendContributorInvitation( invite, purlRecipient, nodeOwner, invitingContributor, purlContributorEmailList );
  }

  private PurlContributorInviteValue sendContributorInvitationByContributor( PurlRecipient purlRecipient,
                                                                             PurlContributorInviteValue invite,
                                                                             PurlContributor invitingContributor,
                                                                             List<PurlContributor> purlContributorEmailList )
  {
    Participant nodeOwner = getNodeOwnerForPurlRecipient( purlRecipient );
    if ( !Objects.isNull( nodeOwner ) )
    {
      PurlContributor managerContributor = getManagerContribution( purlRecipient, nodeOwner );
      if ( managerContributor == null )
      {
        // Construct manager contributor object
        managerContributor = new PurlContributor();
        managerContributor.setState( PurlContributorState.lookup( PurlContributorState.INVITATION ) );
        managerContributor.setUser( nodeOwner );
        managerContributor.setFirstName( nodeOwner.getFirstName() );
        managerContributor.setLastName( nodeOwner.getLastName() );
        managerContributor.setAvatarUrl( nodeOwner.getAvatarSmall() );
        managerContributor.setEmailAddr( getPrimaryEmailAddress( nodeOwner ) );
        managerContributor.setDefaultInvitee( true );
        // Add manager as contributor
        purlRecipient.addContributor( managerContributor );
      }
    }

    return sendContributorInvitation( invite, purlRecipient, nodeOwner, invitingContributor, purlContributorEmailList );
  }

  public PurlContributorInviteValue sendContributorInvitationBySubmitter( Long submitterUserId, PurlRecipient purlRecipient, PurlContributorInviteValue invite ) throws ServiceErrorException
  {
    return sendContributorInvitationEmail( invite, purlRecipient, submitterUserId );
  }

  // Send contributor invitation email by submitter
  private PurlContributorInviteValue sendContributorInvitationEmail( PurlContributorInviteValue invite, PurlRecipient purlRecipient, Long nonContributorUserId ) throws ServiceErrorException
  {
    String existingContributorEmailAddress = getExistingContributorEmailAddress( purlRecipient, invite );
    if ( null != existingContributorEmailAddress )
    {
      invite.setEmailAddr( existingContributorEmailAddress );
      invite.setStatus( PurlContributorInviteValue.STATUS_EXISTS );
      invite.setDefaultInvitee( true );
    }
    else
    {
      PurlContributor purlContributor = constructPurlContributor( invite, null );

      purlRecipient.addContributor( purlContributor );
      // Change recipient state to contribution, if not already!
      /*
       * if ( PurlRecipientState.INVITATION.equals( purlRecipient.getState().getCode() ) ) {
       * purlRecipient.setState( PurlRecipientState.lookup( PurlRecipientState.CONTRIBUTION ) ); }
       */
      savePurlRecipient( purlRecipient );

      long startTime = System.currentTimeMillis();
      // if(purlContributor.getEmailAddr()!=null)
      /* custom for wip #46293 */
      sendPurlContributionEmail( purlRecipient, purlContributor, nonContributorUserId, false );
      /* custom for wip #46293 */
 
      long endTime = System.currentTimeMillis();
          logger.debug( "******************Time took for sendPurlContributionEmail() for each contributor:" + ( endTime - startTime ) );

      invite.setStatus( PurlContributorInviteValue.STATUS_SUCCESS );
    }

    return invite;
  }

  // Send contributor invitation to pax or non pax by contributor and Manager
  private PurlContributorInviteValue sendContributorInvitation( PurlContributorInviteValue invite,
                                                                PurlRecipient purlRecipient,
                                                                Participant nodeOwner,
                                                                PurlContributor invitingContributor,
                                                                List<PurlContributor> purlContributorEmailList )
  {
    String existingContributorEmailAddress = getExistingContributorEmailAddress( purlRecipient, invite );

    if ( invitingContributor != null )
    {
      invite.setInvitingContributorId( invitingContributor.getId() );
    }

    if ( invite.getPaxId() != null )
    {
      invite.setEmailAddr( getPrimaryEmailAddress( userService.getUserById( invite.getPaxId() ) ) );
    }
    if ( invite.getContributorId() == null && null != existingContributorEmailAddress )
    {
      invite.setEmailAddr( existingContributorEmailAddress );
      invite.setStatus( PurlContributorInviteValue.STATUS_EXISTS );
    }
    else if ( invite.getContributorId() == null && isContributorOwnerOrRecipient( nodeOwner, purlRecipient, invite ) )
    {
      invite.setStatus( PurlContributorInviteValue.STATUS_FAIL );
    } //Coke upgrade - unSubscribed mails should be ignored.
    else if (purlContributorDAO.isExternalContributorExists( invite.getEmailAddr() ) )
    {
    	try {
			throw new ServiceErrorException(invite.getEmailAddr()+"!!This email id is already unsubscribed!!");
		} catch (Exception e) {
		}
    	invite.setStatus( PurlContributorInviteValue.STATUS_FAIL );	
    }
    else
    {
      PurlContributor purlContributor = constructPurlContributor( invite, invitingContributor );

      purlRecipient.addContributor( purlContributor );
      // Change recipient state to contribution, if not already!
      if ( PurlRecipientState.INVITATION.equals( purlRecipient.getState().getCode() ) )
      {
        purlRecipient.setState( PurlRecipientState.lookup( PurlRecipientState.CONTRIBUTION ) );
      }
      savePurlRecipient( purlRecipient );
      if ( !purlContributor.isSendLater() )
      {
        purlContributorEmailList.add( purlContributor );
      }

      invite.setStatus( PurlContributorInviteValue.STATUS_SUCCESS );
    }

    return invite;
  }

  private PurlContributor constructPurlContributor( PurlContributorInviteValue invite, PurlContributor invitingContributor )
  {
    PurlContributor purlContributor = null;
    User invitedContributor = null;
    String defaultAvatarUrl = null;

    if ( invite.getContributorId() != null )
    {
      purlContributor = getPurlContributorById( invite.getContributorId() );
    }
    else
    {
      purlContributor = new PurlContributor();
    }

    purlContributor.setState( PurlContributorState.lookup( PurlContributorState.INVITATION ) );
    purlContributor.setInvitedContributor( invitingContributor );
    purlContributor.setSendLater( invite.isSendLater() );

    if ( null != invite.getPaxId() )
    {
      invitedContributor = userService.getUserById( invite.getPaxId() );
      if ( invitedContributor != null && invitedContributor instanceof Participant )
      {
        defaultAvatarUrl = ( (Participant)invitedContributor ).getAvatarSmall();
      }
    }
    else if ( null != invite.getEmailAddr() )
    {
      invitedContributor = userService.getUserByEmailAddr( invite.getEmailAddr().toLowerCase() );
      if ( invitedContributor != null && invitedContributor instanceof Participant )
      {
        defaultAvatarUrl = ( (Participant)invitedContributor ).getAvatarSmall();
      }
    }

    if ( invitedContributor != null )
    {
      purlContributor.setUser( invitedContributor );
      purlContributor.setFirstName( invitedContributor.getFirstName() );
      purlContributor.setLastName( invitedContributor.getLastName() );
      purlContributor.setEmailAddr( getPrimaryEmailAddress( invitedContributor ) );
      purlContributor.setDefaultInvitee( true );
    }
    else
    {
      purlContributor.setFirstName( invite.getFirstName() );
      purlContributor.setLastName( invite.getLastName() );
      purlContributor.setEmailAddr( invite.getEmailAddr() );
    }

    if ( StringUtils.isNotBlank( defaultAvatarUrl ) )
    {
      purlContributor.setAvatarUrl( defaultAvatarUrl );
      purlContributor.setAvatarState( PurlMediaState.lookup( PurlMediaState.STAGED ) );
    }

    return purlContributor;
  }

  private boolean isContributorOwnerOrRecipient( Participant nodeOwner, PurlRecipient purlRecipient, PurlContributorInviteValue invite )
  {
    if ( null != invite.getPaxId() )
    {
      // Purl owner cannot be added explicitly as a contributor
      if ( null != nodeOwner && invite.getPaxId().equals( nodeOwner.getId() ) )
      {
        return true;
      }
      // Purl recipient cannot be added explicitly as a contributor
      if ( invite.getPaxId().equals( purlRecipient.getUser().getId() ) )
      {
        return true;
      }
    }
    else if ( null != invite.getEmailAddr() )
    {

      User contributor = userService.getUserByPrimaryEmailAddr( invite.getEmailAddr().toLowerCase() );
      if ( null != contributor )
      {
        // Purl owner cannot be added explicitly as a contributor
        if ( null != nodeOwner && contributor.getId().equals( nodeOwner.getId() ) )
        {
          return true;
        }
        // Purl recipient cannot be added explicitly as a contributor
        if ( contributor.getId().equals( purlRecipient.getUser().getId() ) )
        {
          return true;
        }
      }
    }
    return false;
  }

  private String getPrimaryEmailAddress( User invitedContributor )
  {
    Set<UserEmailAddress> emailAddresses = invitedContributor.getUserEmailAddresses();
    for ( UserEmailAddress emailAddress : emailAddresses )
    {
      if ( emailAddress.isPrimary() )
      {
        return emailAddress.getEmailAddr();
      }
    }
    return null;
  }

  private PurlContributor getManagerContribution( PurlRecipient purlRecipient, Participant nodeOwner )
  {
    for ( PurlContributor purlContributor : purlRecipient.getContributors() )
    {
      if ( purlContributor.getUser() != null && purlContributor.getUser().getId().equals( nodeOwner.getId() ) )
      {
        return purlContributor;
      }
    }
    return null;
  }

  public void sendPurlContributionEmail( PurlRecipient purlRecipient, PurlContributor purlContributor, Long nonContributorUserId , boolean isAutoInvite )
  {
    PurlContributor attachedContributor = null;
    boolean externalContributor = false; /* Customization for WIP 32479 */
    for ( PurlContributor domainContributor : purlRecipient.getContributors() )
    {
      if ( domainContributor.getUser() != null && purlContributor.getUser() != null )
      {
        if ( domainContributor.getUser().getId().equals( purlContributor.getUser().getId() ) )
        {
          attachedContributor = domainContributor;
          break;
        }
      }
      else if ( domainContributor.getEmailAddr() != null )
      {
        // CAUTION:in test environment if QA test with same email id for
        // contributors/recipient then the below code will messed up the
        // email recipient
        if ( domainContributor.getEmailAddr().equals( purlContributor.getEmailAddr() ) )
        {
          attachedContributor = domainContributor;
          externalContributor = true; /* Customization for WIP 32479 */
          break;
        }
      }
    }

    if ( null != attachedContributor )
    {
      if ( attachedContributor.getPurlRecipient() != null )
      {
        RecognitionPromotion promotion = attachedContributor.getPurlRecipient().getPromotion();
        if ( promotion.getPromotionNotifications().size() > 0 )
        {
          Iterator notificationsIter = promotion.getPromotionNotifications().iterator();
          while ( notificationsIter.hasNext() )
          {
            PromotionNotification notification = (PromotionNotification)notificationsIter.next();
            if ( notification.isPromotionNotificationType() )
            {
              PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notification;
              //long messageId = promotionNotificationType.getNotificationMessageId();
              long messageId = 0; /* Customization for WIP 32479 */
              // Process only when a notification has been set up
              // on the promotion

              String notificationTypeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();
              /* customization for wip # 46293 */
              if ( notificationTypeCode.equals( PromotionEmailNotificationType.PURL_CONTRIBUTOR_INVITATION )
                      || notificationTypeCode.equals( PromotionEmailNotificationType.PURL_EXTERNAL_CONTRIBUTOR_INVITATION ) || notificationTypeCode.equals( PromotionEmailNotificationType.COKE_PURL_MANAGER_CONTRIBUTOR_INVITATION ) )
              {
            	  /* customization for wip # 46293  ends */
                if ( externalContributor && !purlContributorDAO.isExternalContributorExists( attachedContributor.getEmailAddr() ) )
                {
                  Message message = messageService.getMessageByCMAssetCode( MessageService.PURL_EXTERNAL_CONTRIBUTOR_NOTIFICATION_MESSAGE_CM_ASSET_CODE );
                  messageId = message.getId();
                }
                else if ( !externalContributor  && !isAutoInvite )
                {
                	Message message = messageService.getMessageByCMAssetCode( MessageService.COKE_PURL_MANAGER_INVITE_CONTRIBUTORS_CM_ASSET_CODE );
                    messageId = message.getId();
                    
                    isAutoInvite = false;
                }
                else
                {
                	 messageId = promotionNotificationType.getNotificationMessageId();
                }              
              //if ( messageId > 0 ){
              if ( messageId > 0 && notificationTypeCode.equals( PromotionEmailNotificationType.PURL_CONTRIBUTOR_INVITATION ) )
              {
                Mailing mailing = mailingService.buildPurlContributorNotificationMailing( messageId, attachedContributor, nonContributorUserId );
                User owner = null;
                // If the user is not logged in, then
                // UserManager.getUser will be null
                if ( UserManager.getUser() == null )
                {
                  owner = attachedContributor.getUser();
                }
                // if contributor is non-pax, then owner will be
                // null. Use node-owner as the sender
                if ( owner == null )
                {
                  owner = participantService.getNodeOwner( attachedContributor.getPurlRecipient().getNode().getId() );
                  if ( owner != null )
                  {
                    mailingService.submitMailing( mailing, null, owner.getId() );
                  }
                  else
                  {
                    Role role = getRoleService().getRoleByCode( AuthorizationService.ROLE_CODE_BI_ADMIN );

                    if ( role != null )
                    {
                      List<Long> biAdminUserIds = getRoleService().getBiAdminUserIdsByRoleId( role.getId() );
                      if ( !Objects.isNull( biAdminUserIds ) && !biAdminUserIds.isEmpty() )
                      {
                        mailingService.submitMailing( mailing, null, biAdminUserIds.get( 0 ) );
                        // Getting first admin user id since mail have to send only once
                      }

                    }

                  }
                }
                else if ( UserManager.getUser() == null )
                {
                  // Pax who is not logged in..
                  mailingService.submitMailing( mailing, null, owner.getId() );
                }
                else
                {
                  // Pax wwho has logged in
                  mailingService.submitMailing( mailing, null );
                }
              }
            }
          }
        }
      }
     }
    }
  }

  private String getExistingContributorEmailAddress( PurlRecipient purlRecipient, PurlContributorInviteValue invite )
  {
    for ( PurlContributor contributor : purlRecipient.getContributors() )
    {
      if ( !contributor.isSendLater() )
      {
        if ( null == contributor.getUser() )
        {
          // Non-Pax email address comparison(Manager, Contributor)
          if ( null != contributor.getEmailAddr() && null != invite.getEmailAddr() && contributor.getEmailAddr().toLowerCase().equals( invite.getEmailAddr().toLowerCase() ) )
          {
            return invite.getEmailAddr();
          }
        }
        else
        {
          // Pax ID comparison(Manager)
          if ( null != invite.getPaxId() )
          {
            if ( contributor.getUser().getId().equals( invite.getPaxId() ) )
            {
              return getPrimaryEmailAddress( contributor.getUser() );
            }
          }
          // Pax email address comparison(Contributor)
          else
          {
            Set<UserEmailAddress> userEmailAddress = null;
            if ( Hibernate.isInitialized( contributor.getUser().getUserEmailAddresses() ) )
            {
              userEmailAddress = contributor.getUser().getUserEmailAddresses();
            }
            else
            {
              userEmailAddress = this.userService.getUserEmailAddresses( contributor.getUser().getId() );
            }
            for ( UserEmailAddress emailAddress : userEmailAddress )
            {
              if ( null != emailAddress.getEmailAddr() && null != invite.getEmailAddr() && emailAddress.getEmailAddr().toLowerCase().equals( invite.getEmailAddr().toLowerCase() ) )
              {
                return invite.getEmailAddr();
              }
            }
          }
        }
      } // if it is send later, then we can send it to the contributor
    }
    return null;
  }

  @Override
  public PurlContributorComment postCommentForContributor( Long purlContributorId, PurlContributorComment comment ) throws PurlContributorCommentValidationException, ServiceErrorException
  {
    validatePurlContributorComment( comment );
    PurlContributor purlContributor = getPurlContributorById( purlContributorId );

    PurlContributorComment purlComment = new PurlContributorComment();

    purlComment.setComments( comment.getComments() );
    if ( purlContributor.getUser() != null )
    {
      purlComment.setCommentsLanguageType( userService.getPreferredLanguageFor( purlContributor.getUser().getId() ) );
    }

    purlComment.setStatus( PurlContributorCommentStatus.lookup( PurlContributorCommentStatus.ACTIVE ) );
    if ( !StringUtil.isEmpty( comment.getImageUrl() ) )
    {
      String imageServiceUrl;
      purlComment.setMediaState( PurlMediaState.lookup( PurlMediaState.STAGED ) );
      purlComment.setImageStatus( PurlContributorMediaStatus.lookup( PurlContributorMediaStatus.ACTIVE ) );

      Long runByUser = purlContributor.getPurlRecipient().getUser().getId();
      if ( !StringUtil.isEmpty( comment.getImageUrl() ) )
      {
        // launchMediaUploadProcess( comment.getImageUrl(), runByUser );
        imageServiceUrl = moveFileToImageService( comment.getImageUrl(), String.valueOf( purlContributorId ) );
        purlComment.setImageUrl( imageServiceUrl );

      }
      if ( !StringUtil.isEmpty( comment.getImageUrlThumb() ) )
      {
        // launchMediaUploadProcess( comment.getImageUrlThumb(),
        // runByUser );
        imageServiceUrl = moveFileToImageService( comment.getImageUrlThumb(), String.valueOf( purlContributorId ) );
        purlComment.setImageUrlThumb( imageServiceUrl );
      }
    }
    if ( UserManager.isUserLoggedIn() )
    {
      Participant participant = participantService.getParticipantById( UserManager.getUserId() );
      if ( purlContributor.getAvatarUrl() == null )
      {
        if ( participant.getAvatarSmall() != null )
        {
          purlContributor.setAvatarUrl( participant.getAvatarSmall() );
        }
      }
    }
    if ( comment.getVideoUrl() != null && !comment.getVideoUrl().equals( "" ) )
    {

      purlComment.setVideoUrl( comment.getVideoUrl() );
      purlComment.setVideoUrlThumb( comment.getVideoUrlThumb() );
      purlComment.setVideoUrlExtension( comment.getVideoUrlExtension() );
      purlComment.setVideoType( comment.getVideoType() );
      purlComment.setVideoStatus( PurlContributorMediaStatus.lookup( PurlContributorMediaStatus.ACTIVE ) );
    }

    purlContributor.addComment( purlComment );
    PurlRecipient purlRecipient = purlContributor.getPurlRecipient();
    if ( PurlRecipientState.INVITATION.equals( purlRecipient.getState().getCode() ) )
    {
      purlRecipient.setState( PurlRecipientState.lookup( PurlRecipientState.CONTRIBUTION ) );
      savePurlRecipient( purlRecipient );
    }
    savePurlContributor( purlContributor );

    return purlComment;
  }

  private String moveFileToImageService( String imageUrl, String purlContributorId ) throws ServiceErrorException
  {
    String imageServiceUrl;
    byte[] imageByte = appDataDirFileUploadStrategy.getFileData( imageUrl );
    String contentType = ProfileAvatarUploadUtil.getContentType( ImageUtils.getFilename( imageUrl ) );
    String encodedImage = Base64.getEncoder().encodeToString( imageByte );

    imageServiceUrl = imageServiceRepositoryFactory.getRepo().uploadImage( ProfileAvatarUploadUtil.getImageData( contentType, encodedImage ), purlContributorId, "purl_picture" );

    if ( !StringUtil.isEmpty( imageServiceUrl ) && imageServiceUrl.contains( "cloud/v1/images" ) )
    {
      appDataDirFileUploadStrategy.delete( imageUrl );
    }

    return imageServiceUrl;

  }

  public int getMaxCommentLength()
  {
    PropertySetItem prop = systemVariableService.getPropertyByName( SystemVariableService.PURL_COMMENT_SIZE_LIMIT );
    int maxLength = 500;// default
    if ( null != prop )
    {
      maxLength = prop.getIntVal();
    }
    return maxLength;
  }

  private void validatePurlContributorComment( PurlContributorComment comment ) throws PurlContributorCommentValidationException
  {
    int maxLength = getMaxCommentLength();
    // validate the comment length
    if ( StringUtils.isNotEmpty( comment.getComments() ) && comment.getComments().length() > maxLength )
    {
      String error = cmassetService.getTextFromCmsResourceBundle( ServiceErrorMessageKeys.PURL_COMMENT_LENGTH_ERROR );
      error = MessageFormat.format( error, maxLength );
      throw new PurlContributorCommentValidationException( error );
    }
  }

  /*
   * This method should really not by used to move files around. Use an Asynch annotated method
   * instead
   */
  @Deprecated
  private void launchMediaUploadProcess( String mediaFilePath, Long runByUser )
  {
    Map<String, String[]> processParams = new HashMap<String, String[]>();
    processParams.put( "mediaFilePath", new String[] { mediaFilePath } );
    processParams.put( "serverInstance", new String[] { getServerInstance() } );
    Process process = processService.createOrLoadSystemProcess( PurlMediaUploadProcess.PROCESS_NAME, PurlMediaUploadProcess.BEAN_NAME );
    processService.launchProcess( process, processParams, runByUser );
  }

  private String getServerInstance()
  {
    // return Environment.isCtech() ? Environment.getHostServer() +
    // ".biperf.com:51204" : "localhost:7001";
    return Environment.isCtech() ? Environment.getHostServer() + systemVariableService.getPropertyByName( SystemVariableService.SERVER_INSTANCE ).getStringVal() : "localhost:7001";
  }

  /*
   * This method is demarcated as Aynchronous. Caution - there are some issues with calling services
   * that require DAOs as this annotation seems to bypass the stack
   */
  @Async
  public void moveFileToWebdavAsynchronously( String mediaUrl )
  {
    moveFileToWebdav( mediaUrl );
  }

  public boolean moveFileToWebdav( String mediaUrl )
  {
    if ( logger.isInfoEnabled() )
    {
      logger.info( "transfering media [" + mediaUrl + "] to WebDav " );
    }
    try
    {
      byte[] media = appDataDirFileUploadStrategy.getFileData( mediaUrl );
      webdavFileUploadStrategy.uploadFileData( mediaUrl, media );

      appDataDirFileUploadStrategy.delete( mediaUrl );
      return true;
    }
    catch( Throwable e )
    {
      // Must not have the file in AppDataDir of server executing this
      // process
      logger.error( "Error transfering media [" + mediaUrl + "] to WebDav.  Message: " + e.getMessage() );
    }
    return false;
  }

  public PurlContributor updatePurlContributorName( PurlContributor contributor )
  {
    PurlContributor purlContributor = getPurlContributorById( contributor.getId() );

    purlContributor.setFirstName( contributor.getFirstName() );
    purlContributor.setLastName( contributor.getLastName() );

    return savePurlContributor( purlContributor );
  }

  public List<PurlMediaUploadValue> postMediaForContributor( Long purlContributorId, List<PurlMediaUploadValue> mediaUploads )
  {
    for ( PurlMediaUploadValue mediaUpload : mediaUploads )
    {
      mediaUpload = postMediaForContributor( purlContributorId, mediaUpload );
    }

    return mediaUploads;
  }

  public PurlMediaUploadValue postMediaForContributor( Long purlContributorId, PurlMediaUploadValue mediaUpload )
  {
    PurlContributor purlContributor = getPurlContributorById( purlContributorId );

    PurlContributorMedia purlContributorMedia = new PurlContributorMedia();
    purlContributorMedia.setCaption( mediaUpload.getCaption() );
    purlContributorMedia.setUrl( mediaUpload.getFull() );
    purlContributorMedia.setUrlThumb( mediaUpload.getThumb() );
    purlContributorMedia.setType( PurlContributorMediaType.lookup( mediaUpload.getMedia() ) );
    purlContributorMedia.setStatus( PurlContributorMediaStatus.lookup( PurlContributorMediaStatus.ACTIVE ) );
    if ( PurlContributorMediaType.PICTURE.equals( mediaUpload.getMedia() ) )
    {
      purlContributorMedia.setState( PurlMediaState.lookup( PurlMediaState.STAGED ) );
    }
    else
    {
      purlContributorMedia.setState( PurlMediaState.lookup( PurlMediaState.POSTED ) );
    }
    purlContributor.addMedia( purlContributorMedia );

    try
    {
      savePurlContributor( purlContributor );
      mediaUpload.setStatus( PurlMediaUploadValue.STATUS_SUCCESS );
    }
    catch( Throwable e )
    {
      mediaUpload.setStatus( PurlMediaUploadValue.STATUS_FAIL );
    }

    if ( PurlContributorMediaType.PICTURE.equals( mediaUpload.getMedia() ) )
    {
      Long runByUser = purlContributor.getPurlRecipient().getUser().getId();
      if ( !StringUtil.isEmpty( mediaUpload.getFull() ) )
      {
        launchMediaUploadProcess( mediaUpload.getFull(), runByUser );
      }
      if ( !StringUtil.isEmpty( mediaUpload.getThumb() ) )
      {
        launchMediaUploadProcess( mediaUpload.getThumb(), runByUser );
      }
    }

    return mediaUpload;
  }

  public PurlFileUploadValue uploadAvatarForContributor( PurlFileUploadValue data, boolean isFromProcess ) throws ServiceErrorException
  {
    if ( validFileData( data ) )
    {
      try
      {
        // Upload Avatar to AppDataDir
        ImageUtils imgInstance = new ImageUtils();
        BufferedImage thumb = imgInstance.readImage( data.getData() );
        int targetCropDimension = thumb.getHeight() < thumb.getWidth() ? thumb.getHeight() : thumb.getWidth();
        thumb = imageCropStrategy.process( thumb, targetCropDimension, targetCropDimension );
        thumb = imageResizeStrategy.process( thumb, PurlFileUploadValue.THUMB_DIMENSION, PurlFileUploadValue.THUMB_DIMENSION );

        PurlContributor purlContributor = purlContributorDAO.getPurlContributorById( data.getId() );
        if ( purlContributor != null )
        {
          String imageInBytes = Base64.getEncoder().encodeToString( ImageUtils.convertToByteArray( thumb, ImageUtils.getFileExtension( data.getName() ) ) );

          String imageServiceUrl = imageServiceRepositoryFactory.getRepo().uploadImage( ProfileAvatarUploadUtil.getImageData( data.getContentType(), imageInBytes ),
                                                                                        RosterUtil.getRandomUUId().toString(),
                                                                                        "purl_picture" );

          data = setUploadValue( data, imageServiceUrl );

        }
        else
        {
          throwServiceException();
        }
      }
      catch( Exception e )
      {
        throwServiceExceptionForContribution();
      }
    }
    else
    {
      throwServiceExceptionForContribution();
    }
    return data;
  }

  public void postAvatarForContributor( Long purlContributorId, String avatarUrl )
  {
    // Save Avatar
    PurlContributor purlContributor = getPurlContributorById( purlContributorId );
    purlContributor.setAvatarUrl( avatarUrl );
    purlContributor.setAvatarState( PurlMediaState.lookup( PurlMediaState.STAGED ) );
    savePurlContributor( purlContributor );

    // Launch Upload process to Webdav
    // Long runByUser = purlContributor.getPurlRecipient().getUser().getId();
    // launchMediaUploadProcess( avatarUrl, runByUser );
    // getPurlService().moveFileToWebdavAsynchronously( avatarUrl );
  }

  public boolean deleteAvatarForContributor( Long purlContributorId )
  {
    // Remove Avatar
    PurlContributor purlContributor = getPurlContributorById( purlContributorId );
    String avatarUrl = purlContributor.getAvatarUrl();
    purlContributor.setAvatarUrl( null );
    purlContributor.setAvatarState( null );
    savePurlContributor( purlContributor );

    // Don'te Delete Avatar from Webdav if user profile still references it
    if ( purlContributor.getUser() != null && purlContributor.getUser() instanceof Participant //
        && avatarUrl != null && avatarUrl.equals( ( (Participant)purlContributor.getUser() ).getAvatarSmall() ) )
    {
      return true;
    }

    // Delete Avatar from Webdav
    return deleteMediaFromWebdav( purlContributor.getAvatarUrl() );
  }

  public PurlFileUploadValue uploadPhotoForContributor( PurlFileUploadValue data ) throws ServiceErrorException
  {
    if ( validFileData( data ) )
    {
      try
      {
        ImageUtils imgInstance = new ImageUtils();
        BufferedImage full = imgInstance.readImage( data.getData() );
        byte[] dataGif = null;
        if ( ImageUtils.getFileExtension( data.getName() ).equalsIgnoreCase( GIF ) )
        {
          int targetCropDimension = full.getHeight() < full.getWidth() ? full.getHeight() : full.getWidth();
          dataGif = imageCropStrategy.process( data.getData(), targetCropDimension, targetCropDimension );
          data.setData( dataGif );
          full = imgInstance.readImage( dataGif );
        }
        else
        {
          full = imageResizeStrategy.process( full, PurlFileUploadValue.DETAIL_DIMENSION );
        }
        data.setFull( ImageUtils.getPurlDetailPath( data.getType(), data.getId(), data.getName() ) );
        if ( dataGif != null )
        {
          appDataDirFileUploadStrategy.uploadFileData( data.getFull(), dataGif );
        }
        else
        {
          appDataDirFileUploadStrategy.uploadFileData( data.getFull(), ImageUtils.convertToByteArray( full, ImageUtils.getFileExtension( data.getName() ) ) );
        }
        BufferedImage thumb = imgInstance.readImage( data.getData() );
        int targetCropDimension = thumb.getHeight() < thumb.getWidth() ? thumb.getHeight() : thumb.getWidth();
        thumb = imageCropStrategy.process( thumb, targetCropDimension, targetCropDimension );
        thumb = imageResizeStrategy.process( thumb, PurlFileUploadValue.THUMB_DIMENSION, PurlFileUploadValue.THUMB_DIMENSION );
        data.setThumb( ImageUtils.getPurlThumbPath( data.getType(), data.getId(), data.getName() ) );
        appDataDirFileUploadStrategy.uploadFileData( data.getThumb(), ImageUtils.convertToByteArray( thumb, ImageUtils.getFileExtension( data.getName() ) ) );
      }
      catch( Exception e )
      {
        logger.error( "Image upload failed:", e );
        throw new ServiceErrorException( "purl.contribution.IMAGE_UPLOAD_FAILED" );
      }
    }
    else
    {
      throw new ServiceErrorException( "purl.contribution.IMAGE_UPLOAD_INVALID" );
    }
    return data;
  }

  public PurlFileUploadValue uploadVideoForContributor( PurlFileUploadValue data ) throws ServiceErrorException
  {
    if ( validVideoFileData( data ) )
    {
      try
      {
        // FrameGrab does not work with webm files. Use a default
        // thumbnail.
        String thumbnailUrl = null;
        String fileExtension = FilenameUtils.getExtension( data.getName() );
        if ( SupportedEcardVideoTypes.WEBM.getInformalName().equals( fileExtension ) )
        {
          thumbnailUrl = getDefaultVideoImageUrl();
        }
        else
        {
          // Create and upload thumbnail
          String thumbnailFilename = data.getName() + ".video.jpeg";
          ByteBuffer videoDataBuffer = ByteBuffer.wrap( data.getData() );
          ByteBufferSeekableByteChannel videoDataByteChannel = new ByteBufferSeekableByteChannel( videoDataBuffer );
          BufferedImage thumbnailImage = FrameGrab.getFrame( videoDataByteChannel, 1 );
          ByteArrayOutputStream thumbnailOutputStream = new ByteArrayOutputStream( 256 * 1024 );
          ImageIO.write( thumbnailImage, "jpeg", thumbnailOutputStream );
          byte[] thumbnailData = thumbnailOutputStream.toByteArray();
          WebDavUploadResult thumbnailUploadResult = uploadToWebdav( thumbnailFilename, thumbnailData, data.getId() );

          // Failing early if thumbnail upload failed
          if ( !thumbnailUploadResult.isSuccess() )
          {
            logger.error( "Video thumbnail upload failed" );
            throw new ServiceErrorException( "purl.contribution.VIDEO_UPLOAD_FAILED" );
          }
          else
          {
            thumbnailUrl = thumbnailUploadResult.getWebDavUrl();
          }
        }

        // Upload video
        WebDavUploadResult videoUploadResult = uploadToWebdav( data.getName(), data.getData(), data.getId() );

        if ( !videoUploadResult.isSuccess() )
        {
          logger.error( "Video upload failed" );
          throw new ServiceErrorException( "purl.contribution.VIDEO_UPLOAD_FAILED" );
        }

        data.setThumb( thumbnailUrl );
        data.setFull( videoUploadResult.getWebDavUrl() );
      }
      catch( Exception e )
      {
        logger.error( "Video upload failed:", e );
        throw new ServiceErrorException( "purl.contribution.VIDEO_UPLOAD_FAILED" );
      }
    }
    else
    {
      throw new ServiceErrorException( "purl.contribution.VIDEO_UPLOAD_INVALID" );
    }
    return data;
  }

  public void sendThankyouToContributors( Long purlRecipientId, String subject, String comments ) throws ServiceErrorException
  {
    List<PurlContributor> contributors = getContributorsForThankyouMessage( purlRecipientId );

    Mailing mailing = createAdhocMailing( contributors, subject, comments );
    Map<String, String> objectMap = new HashMap<String, String>();
    try
    {
      mailing = mailingService.submitMailing( mailing, objectMap );
      if ( mailing == null )
      {
        throw new ServiceErrorException( "admin.send.message.SEND_MAILING_FAILED" );
      }
    }
    catch( Exception e )
    {
      throw new ServiceErrorException( "admin.send.message.SEND_MAILING_FAILED" );
    }
  }

  private List<PurlContributor> getContributorsForThankyouMessage( Long purlRecipientId )
  {
    PurlContributorState[] purlContributorStates = { PurlContributorState.lookup( PurlContributorState.COMPLETE ) };

    PurlContributorQueryConstraint constraint = new PurlContributorQueryConstraint();
    constraint.setPurlContributorStates( purlContributorStates );
    constraint.setPurlRecipientId( purlRecipientId );
    List<PurlContributor> contributors = purlContributorDAO.getContributors( constraint );

    // Exclude all contributors who have not contributed
    Iterator<PurlContributor> iContributor = contributors.iterator();
    while ( iContributor.hasNext() )
    {
      if ( !isContributionsAvailable( iContributor.next() ) )
      {
        iContributor.remove();
      }
    }

    return contributors;
  }

  private Mailing createAdhocMailing( List<PurlContributor> contributors, String subject, String message )
  {
    Mailing mailing = new Mailing();

    String sender = systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS ).getStringVal();
    mailing.setGuid( GuidUtils.generateGuid() );
    mailing.setSender( sender );
    mailing.setDeliveryDate( new Timestamp( DateUtils.getCurrentDate().getTime() ) );

    Set<MailingRecipient> recipients = getMailingRecipients( contributors );
    mailing.addMailingRecipients( recipients );
    mailing.setMailingType( MailingType.lookup( MailingType.EMAIL_WIZARD_PREVIEW ) );

    MailingMessageLocale locale = getMailingLocale( subject, null, message, null );
    mailing.addMailingMessageLocale( locale );
    return mailing;
  }

  private Set<MailingRecipient> getMailingRecipients( List<PurlContributor> contributors )
  {
    Set<MailingRecipient> recipients = new HashSet<MailingRecipient>();

    for ( PurlContributor purlContributor : contributors )
    {
      MailingRecipient mailingRecipient = new MailingRecipient();
      mailingRecipient.setGuid( GuidUtils.generateGuid() );

      MailingRecipientData firstName = new MailingRecipientData();
      firstName.setKey( "firstName" );
      firstName.setValue( purlContributor.getFirstName() );
      firstName.setMailingRecipient( mailingRecipient );
      mailingRecipient.addMailingRecipientData( firstName );

      MailingRecipientData lastName = new MailingRecipientData();
      lastName.setKey( "lastName" );
      lastName.setValue( purlContributor.getLastName() );
      lastName.setMailingRecipient( mailingRecipient );
      mailingRecipient.addMailingRecipientData( lastName );

      // Non-PAX contributors
      if ( null == purlContributor.getUser() )
      {
        mailingRecipient.setPreviewEmailAddress( purlContributor.getEmailAddr() );
        mailingRecipient.setLocale( MailingMessageLocale.DEFAULT_LOCALE );
      }
      else
      {
        AssociationRequestCollection reqCollection = new AssociationRequestCollection();
        reqCollection.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );
        Participant pax = participantService.getParticipantByIdWithAssociations( purlContributor.getUser().getId(), reqCollection );
        mailingRecipient.setLocale( pax.getLanguageType() != null ? pax.getLanguageType().getCode() : systemVariableService.getDefaultLanguage().getStringVal() );
        if ( pax.getPrimaryEmailAddress() != null )
        {
          mailingRecipient.setPreviewEmailAddress( pax.getPrimaryEmailAddress().getEmailAddr() );
        }
        mailingRecipient.setUser( pax );
      }

      recipients.add( mailingRecipient );
    }

    return recipients;
  }

  private MailingMessageLocale getMailingLocale( String subject, String html, String plain, String text )
  {
    MailingMessageLocale locale = new MailingMessageLocale();
    locale.setHtmlMessage( html == null || html.equals( "" ) ? plain : html );
    locale.setPlainMessage( plain );
    locale.setTextMessage( text );
    locale.setSubject( subject );
    locale.setLocale( MailingMessageLocale.DEFAULT_LOCALE );
    return locale;
  }

  public boolean postPurlUrlOnTwitter( Long userId, Long purlRecipientId )
  {
    // TODO : Get message off purlRecipientId
    String message = "postPurlUrlOnTwitter";
    try
    {
      twitterService.tweet( userId, message );
    }
    catch( ServiceErrorException e )
    {
      logger.error( "Error posting message to twitter for user[" + userId + "]", e );
      return false;
    }
    return true;
  }

  public boolean postPurlUrlOnFacebook( Long userId, Long purlRecipientId )
  {
    String message = getFacebookMessage( purlRecipientId );
    return facebookService.postMessageToWall( userId, message );
  }

  protected String getFacebookMessage( Long purlReceipientId )
  {
    // TODO : Get message off purlRecipientId
    String message = "postPurlUrlOnFacebook yay!";
    return message;
  }

  public String getFacebookFeedDialogUrlForPurlPost( PurlRecipient purlRecipient, boolean isMobile )
  {
    final String DIALOG_URL_FORMAT = "http://{0}.facebook.com/dialog/feed" + "?app_id={1}" + "&link={2}" + "&name={3}" + "&caption={4}" + "&redirect_uri={5}";

    final MessageFormat formatter = new MessageFormat( DIALOG_URL_FORMAT );

    String[] parameters = new String[6];
    try
    {
      String encodedPurlRecipeintUrl = URLEncoder.encode( createPurlRecipientUrl( purlRecipient ), "UTF-8" );

      parameters[0] = "www"; // isMobile ? "m" : "www"; I file a bug with
      // Facebook saying the m
      // subdomain doesn't work; default to www
      // for now
      // (http://bugs.developers.facebook.net/show_bug.cgi?id=15709)
      parameters[1] = facebookService.getAppId();
      parameters[2] = encodedPurlRecipeintUrl;
      parameters[3] = URIUtil.encodeQuery( ContentReaderManager.getText( "purl.common", "FACEBOOK_LINK_NAME" ) );
      parameters[4] = URIUtil.encodeQuery( getSocialFeedPurlMessage( purlRecipient ) );
      parameters[5] = URLEncoder.encode( "http://facebook.com", "UTF-8" );
    }
    catch( Throwable t )
    {
      // do nothing.. ?
    }

    String url = formatter.format( parameters );

    return url;
  }

  private String getSocialFeedPurlMessage( PurlRecipient purlRecipient )
  {
    String[] parameters = new String[3];
    String message = ContentReaderManager.getText( "purl.common", "PURL_SOCIAL_COMMON_CAPTION" );
    try
    {
      parameters[0] = purlRecipient.getUser().getFirstName();
      parameters[1] = systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();
      parameters[2] = purlRecipient.getPromotion().getName();
    }
    catch( Throwable t )
    {
      // do nothing.... ?
    }
    return new MessageFormat( message ).format( parameters );
  }

  public String createPurlRecipientUrlFromClaimId( Long claimId )
  {
    AssociationRequestCollection asc = new AssociationRequestCollection();
    asc.add( new PurlRecipientAssociationRequest( PurlRecipientAssociationRequest.PURL_CONTRIBUTOR ) );
    PurlRecipient purlRecipient = getPurlRecipientByClaimIdWithAssociations( claimId, asc );
    if ( purlRecipient != null )
    {
      boolean contributionsAvailable = isContributionsAvailable( purlRecipient );
      if ( contributionsAvailable )
      {
        return createPurlRecipientUrl( purlRecipient );
      }
    }

    return null;
  }

  public String createPurlRecipientUrl( PurlRecipient purlRecipient )
  {
    String purlUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.PURL_URL_PREFIX ).getStringVal();
    StringBuilder sb = new StringBuilder( purlUrl );
    sb.append( '/' );
    sb.append( purlRecipient.getUser().getFirstName().replace( PurlService.PURL_RECIPIENT_URL_NAME, PurlService.PURL_RECIPIENT_URL_DELIMITER ) );
    sb.append( '.' );
    sb.append( purlRecipient.getUser().getLastName().replace( PurlService.PURL_RECIPIENT_URL_NAME, PurlService.PURL_RECIPIENT_URL_DELIMITER ) );
    sb.append( '.' );
    sb.append( purlRecipient.getId() );
    return sb.toString();
  }

  public List<PurlRecipient> getAllPurlRecipientsToExpire()
  {
    PurlRecipientState[] purlRecipientStates = { PurlRecipientState.lookup( PurlRecipientState.INVITATION ),
                                                 PurlRecipientState.lookup( PurlRecipientState.CONTRIBUTION ),
                                                 PurlRecipientState.lookup( PurlRecipientState.RECOGNITION ),
                                                 PurlRecipientState.lookup( PurlRecipientState.COMPLETE ) };

    int daysToExpire = systemVariableService.getPropertyByName( SystemVariableService.PURL_DAYS_TO_EXP ).getIntVal();
    Date adjustedAwardDate = new Date( new Date().getTime() - daysToExpire * org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY );

    PurlRecipientQueryConstraint constraint = new PurlRecipientQueryConstraint();
    constraint.setPurlRecipientStates( purlRecipientStates );
    constraint.setDate( adjustedAwardDate );
    constraint.setAfter( Boolean.TRUE );
    return purlRecipientDAO.getPurlRecipientList( constraint );
  }

  public boolean processExpirePurl( Long purlRecipientId ) throws ServiceErrorException
  {
    PurlRecipient purlRecipient = getPurlRecipientById( purlRecipientId );

    // Change the status for all contributors
    for ( PurlContributor purlContributor : purlRecipient.getContributors() )
    {
      PurlContributor contributor = getPurlContributorById( purlContributor.getId() );
      contributor.setState( PurlContributorState.lookup( PurlContributorState.EXPIRED ) );
      savePurlContributor( contributor );
    }

    // Change the status for the recipient
    purlRecipient.setState( PurlRecipientState.lookup( PurlRecipientState.EXPIRED ) );
    savePurlRecipient( purlRecipient );

    return true;
  }

  public List<PurlRecipient> getAllPurlRecipientsToArchive()
  {
    PurlRecipientState[] purlRecipientStates = { PurlRecipientState.lookup( PurlRecipientState.INVITATION ),
                                                 PurlRecipientState.lookup( PurlRecipientState.CONTRIBUTION ),
                                                 PurlRecipientState.lookup( PurlRecipientState.RECOGNITION ),
                                                 PurlRecipientState.lookup( PurlRecipientState.COMPLETE ),
                                                 PurlRecipientState.lookup( PurlRecipientState.EXPIRED ) };

    int daysToArchive = PURL_DAYS_TO_ARCHIVE_AFTER_EXPIRE + systemVariableService.getPropertyByName( SystemVariableService.PURL_DAYS_TO_EXP ).getIntVal();
    Date adjustedAwardDate = new Date( new Date().getTime() - daysToArchive * org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY );

    PurlRecipientQueryConstraint constraint = new PurlRecipientQueryConstraint();
    constraint.setPurlRecipientStates( purlRecipientStates );
    constraint.setDate( adjustedAwardDate );
    constraint.setAfter( Boolean.TRUE );
    return purlRecipientDAO.getPurlRecipientList( constraint );
  }

  public boolean processArchivePurl( Long purlRecipientId ) throws ServiceErrorException
  {
    PurlRecipient purlRecipient = getPurlRecipientById( purlRecipientId );

    // Change the status for all contributors
    boolean allRecipientDeleteSuccess = true;
    for ( PurlContributor purlContributor : purlRecipient.getContributors() )
    {
      if ( !PurlContributorState.ARCHIVED.equals( purlContributor.getState().getCode() ) )
      {
        boolean allContributorDeleteSuccess = true;
        for ( PurlContributorComment purlCommentMedia : purlContributor.getComments() )
        {
          if ( null != purlCommentMedia.getMediaState() && !PurlMediaState.DELETED.equals( purlCommentMedia.getMediaState().getCode() ) )
          {
            boolean success = true;
            if ( null != purlCommentMedia.getImageUrl() )
            {
              // Delete Media
              success = deleteMediaFromWebdav( purlCommentMedia.getImageUrl() );
            }

            if ( null != purlCommentMedia.getImageUrlThumb() )
            {
              // Delete Media
              success = deleteMediaFromWebdav( purlCommentMedia.getImageUrlThumb() ) && success;

            }
            if ( success )
            {
              PurlContributorComment commentMedia = getPurlContributorCommentById( purlCommentMedia.getId() );
              commentMedia.setMediaState( PurlMediaState.lookup( PurlMediaState.DELETED ) );
              savePurlContributorComment( commentMedia );
            }
            else
            {
              allContributorDeleteSuccess = false;
            }
          }
        }

        if ( purlContributor.getAvatarState() != null && !PurlMediaState.DELETED.equals( purlContributor.getAvatarState().getCode() ) )
        {
          boolean success = true;
          if ( null != purlContributor.getAvatarUrl() )
          {
            // Don'te Delete Avatar if avatar url is referencing
            // personal/avatar folder
            if ( purlContributor.getAvatarUrl().contains( "purl/avatar" ) )
            {
              success = deleteMediaFromWebdav( purlContributor.getAvatarUrl() );
            }
            else
            {
              success = true;
            }

          }
          if ( success )
          {
            PurlContributor contributor = getPurlContributorById( purlContributor.getId() );
            contributor.setAvatarState( PurlMediaState.lookup( PurlMediaState.DELETED ) );
            savePurlContributor( contributor );
          }
          else
          {
            allContributorDeleteSuccess = false;
          }
        }

        if ( allContributorDeleteSuccess )
        {
          PurlContributor contributor = getPurlContributorById( purlContributor.getId() );
          contributor.setState( PurlContributorState.lookup( PurlContributorState.ARCHIVED ) );
          savePurlContributor( contributor );
        }
        else
        {
          allRecipientDeleteSuccess = false;
        }
      }
    }

    if ( allRecipientDeleteSuccess )
    {
      // Change the status for the recipient
      purlRecipient.setState( PurlRecipientState.lookup( PurlRecipientState.ARCHIVED ) );
      savePurlRecipient( purlRecipient );
      return true;
    }

    return false;
  }

  public String createPostPurlToTwitterUrl( PurlRecipient purlRecipient )
  {
    final String TWITTER_SHARE_URL = "http://twitter.com/share?url={0}&text={1}";

    String[] parameters = new String[2];
    try
    {
      parameters[0] = URLEncoder.encode( createPurlRecipientUrl( purlRecipient ), "UTF-8" );
      parameters[1] = URIUtil.encodeQuery( getSocialFeedPurlMessage( purlRecipient ) );
    }
    catch( Throwable t )
    {
      // do nothing.... ?
    }

    return new MessageFormat( TWITTER_SHARE_URL ).format( parameters );
  }

  public String createPostPurlToLinkedInUrl( HttpServletRequest request, PurlRecipient purlRecipient )
  {
    final String LINKEDIN_SHARE_URL = "http://www.linkedin.com/shareArticle?mini={0}&url={1}&title={2}&summary={3}";

    String[] parameters = new String[4];
    try
    {
      parameters[0] = "true";
      parameters[1] = URLEncoder.encode( createPurlRecipientUrl( purlRecipient ), "UTF-8" );
      parameters[2] = URIUtil.encodeQuery( ContentReaderManager.getText( "purl.common", "LINKED_IN_LINK_NAME" ) );
      parameters[3] = URIUtil.encodeQuery( getSocialFeedPurlMessage( purlRecipient ) );
    }
    catch( Throwable t )
    {
      // do nothing.... ?
    }

    return new MessageFormat( LINKEDIN_SHARE_URL ).format( parameters );
  }

  /**
   * This is an internal URL which renders the login page. Unlike Face book or
   * Twitter, Chatter does not have a direct URL. Chatter response is a full
   * HTML of login page instead. When user logs in chatter response has the
   * access code from chatter.
   * 
   * @param request
   * @param purlRecipientId
   * @return
   */
  public String createPostPurlToChatterUrl( HttpServletRequest request, PurlRecipient purlRecipient )
  {
    String systemUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    String linkUrl = createPurlRecipientUrl( purlRecipient );
    String linkName = ContentReaderManager.getText( "purl.common", "FACEBOOK_LINK_NAME" );
    String messagetext = getSocialFeedPurlMessage( purlRecipient );
    // Chatter call is failing if state is more than 1530 characters
    if ( messagetext.length() > 1000 )
    {
      messagetext = messagetext.substring( 0, 1000 ) + "...";
    }
    String state = messagetext + "||" + linkUrl + "||" + linkName;
    return systemUrl + "/participant/chatterAuthorizationSubmit.do?method=getChatterAuthorizationCode&state=" + state;
  }

  public boolean deleteMediaFromAppDataDir( String filePath )
  {
    if ( StringUtil.isEmpty( filePath ) )
    {
      return false;
    }

    try
    {
      // Delete Media from Stage(WebDav)
      return appDataDirFileUploadStrategy.delete( filePath );
    }
    catch( Throwable e )
    {
      return false;
    }
  }

  public boolean deleteMediaFromWebdav( String filePath )
  {
    if ( StringUtil.isEmpty( filePath ) )
    {
      return false;
    }

    try
    {
      try
      {
        byte[] data = webdavFileUploadStrategy.getFileData( filePath );
        if ( null == data )
        {
          // If file data does not exist, there is nothing to delete
          // Might have got deleted in previous attempt
          return true;
        }
      }
      catch( Throwable e )
      {
        // If file data does not exist, there is nothing to delete
        // Might have got deleted in previous attempt
        return true;
      }

      // Delete Media from Stage(WebDav)
      return webdavFileUploadStrategy.delete( filePath );
    }
    catch( Throwable e )
    {
      return false;
    }
  }

  public boolean validFileData( PurlFileUploadValue data )
  {
    // Check file type
    String extension = ImageUtils.getFileExtension( data.getName() );
    if ( !acceptableExtentions.contains( extension.toLowerCase() ) )
    {
      return false;
    }

    // Check file size
    if ( PurlFileUploadValue.TYPE_PICTURE.equals( data.getType() ) || PurlFileUploadValue.TYPE_AVATAR.equals( data.getType() ) )
    {
      int sizeLimit = MEGABYTES_TO_BYTES_MULTIPLIER * systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_IMG_SIZE_LIMIT ).getIntVal();
      if ( data.getSize() > sizeLimit )
      {
        return false;
      }
    }

    return true;
  }

  public boolean validVideoFileData( PurlFileUploadValue data )
  {
    if ( PurlFileUploadValue.TYPE_VIDEO.equals( data.getType() ) )
    {
      int sizeLimit = MEGABYTES_TO_BYTES_MULTIPLIER * systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_VIDEO_SIZE_LIMIT ).getIntVal();
      if ( data.getSize() > sizeLimit )
      {
        return false;
      }
    }

    return true;
  }

  public List<Promotion> getEligiblePurlPromotionsForInvitation( Long userId )
  {
    List<Long> purlManagerNodes = getPurlManagerNodes( userId );
    return getEligiblePurlPromotionsForInvitation( userId, purlManagerNodes );
  }

  public List<Promotion> getEligiblePurlPromotionsForInvitation( Long userId, List<Long> purlManagerNodes )
  {
    // Get list of all PurlRecipient
    PurlRecipientQueryConstraint constraint = new PurlRecipientQueryConstraint();
    constraint.setNodeIds( purlManagerNodes );
    List<PurlRecipient> list = purlRecipientDAO.getPurlRecipientList( constraint );

    // Filter PurlRecipient list for Manager
    List<PurlRecipient> filteredList = filterManagerPurls( list, userId );

    // To avoid duplicates processing
    Set<Long> inclusionList = new HashSet<Long>();
    Set<Long> exclusionList = new HashSet<Long>();

    // Find unique Promotions for which Manager is eligible
    List<Promotion> purlPromotions = new ArrayList<Promotion>();
    for ( PurlRecipient purlRecipient : filteredList )
    {
      Promotion promotion = purlRecipient.getPromotion();
      if ( !exclusionList.contains( promotion.getId() ) && !inclusionList.contains( promotion.getId() ) )
      {
        if ( isPromotionValid( promotion ) )
        {
          inclusionList.add( promotion.getId() );
          purlPromotions.add( promotion );
        }
        else
        {
          exclusionList.add( promotion.getId() );
        }
      }
    }
    return purlPromotions;
  }

  public List<Promotion> getEligiblePurlPromotionsForContributor( Long userId )
  {
    // Get list of all PurlContributor
    PurlContributorQueryConstraint constraint = new PurlContributorQueryConstraint();
    constraint.setContributorUserId( userId );
    List<PurlContributor> list = purlContributorDAO.getContributors( constraint );

    // To avoid duplicates processing
    Set<Long> inclusionList = new HashSet<Long>();
    Set<Long> exclusionList = new HashSet<Long>();

    // Find unique Promotions for which Contributor is eligible
    List<Promotion> purlPromotions = new ArrayList<Promotion>();
    for ( PurlContributor purlContributor : list )
    {
      // TODO have to remove this call and get this from SQL
      // has to get list of valid promotion to which purl contributor is
      // attached to
      Promotion promotion = purlContributor.getPurlRecipient().getPromotion();
      if ( !exclusionList.contains( promotion.getId() ) && !inclusionList.contains( promotion.getId() ) )
      {
        if ( isPromotionValid( promotion ) )
        {
          inclusionList.add( promotion.getId() );
          purlPromotions.add( promotion );
        }
        else
        {
          exclusionList.add( promotion.getId() );
        }
      }
    }
    return purlPromotions;
  }

  public List<Promotion> getEligiblePurlPromotionsForRecipient( Long userId )
  {
    // Get list of all PurlRecipient
    PurlRecipientQueryConstraint constraint = new PurlRecipientQueryConstraint();
    constraint.setUserId( userId );
    List<PurlRecipient> list = purlRecipientDAO.getPurlRecipientList( constraint );

    // To avoid duplicates processing
    Set<Long> inclusionList = new HashSet<Long>();
    Set<Long> exclusionList = new HashSet<Long>();

    // Find unique Promotions for which Recipient is eligible
    List<Promotion> purlPromotions = new ArrayList<Promotion>();
    for ( PurlRecipient purlRecipient : list )
    {
      Promotion promotion = purlRecipient.getPromotion();
      if ( !exclusionList.contains( promotion.getId() ) && !inclusionList.contains( promotion.getId() ) )
      {
        if ( isPromotionValid( promotion ) )
        {
          inclusionList.add( promotion.getId() );
          purlPromotions.add( promotion );
        }
        else
        {
          exclusionList.add( promotion.getId() );
        }
      }
    }
    return purlPromotions;
  }

  private boolean isPromotionValid( Promotion promotion )
  {
    // Promotion should be live
    if ( !promotion.isLive() )
    {
      return false;
    }

    // Promotion start and end date is valid
    if ( !promotion.isDateValidForSubmission( new Date() ) )
    {
      return false;
    }

    // Promotion should be recognition type
    if ( !promotion.isRecognitionPromotion() )
    {
      return false;
    }

    RecognitionPromotion recognitionPromotion = (RecognitionPromotion)promotion;
    // Promotion should be include PURL
    if ( !recognitionPromotion.isIncludePurl() )
    {
      return false;
    }

    return true;
  }

  public void deleteContributorsNotInvited( Long purlRecipientId, List<PurlContributorInviteValue> inviteList )
  {
    List<Long> contributorIds = new ArrayList<Long>();
    for ( PurlContributorInviteValue invite : inviteList )
    {
      if ( null != invite.getContributorId() )
      {
        contributorIds.add( invite.getContributorId() );
      }
    }

    PurlRecipient recipient = getPurlRecipientById( purlRecipientId );
    Iterator<PurlContributor> iContributor = recipient.getContributors().iterator();
    while ( iContributor.hasNext() )
    {
      PurlContributor contributor = iContributor.next();
      if ( contributor.isSendLater() && !contributorIds.contains( contributor.getId() ) )
      {
        iContributor.remove();
      }
    }
    savePurlRecipient( recipient );
  }

  @SuppressWarnings( "unchecked" )
  public List<Participant> getPreSelectedContributors( Long recipientUserId, Long recipientNodeId, AssociationRequestCollection associationRequests )
  {
    List<Participant> list = new ArrayList<Participant>();

    // null check for node Id. QC bug #3024. This is because recipientNodeId
    // is not properly posted from all detail pages when trying to send
    // recognition from ez reco model.
    if ( recipientNodeId == null || recipientNodeId == 0 )
    {
      User user = userService.getUserById( recipientUserId );
      recipientNodeId = user.getPrimaryUserNode().getNode().getId();
    }

    // All Users in Recipient Nodes
    List<User> activeUserList = nodeService.getNodeById( recipientNodeId ).getActiveUserList();
    for ( User user : activeUserList )
    {
      // Exclude Self and who are already invited
      if ( user.isParticipant() && !user.getId().equals( recipientUserId ) )
      {
        list.add( (Participant)user );
      }
    }

    for ( Participant pax : list )
    {
      for ( Iterator i = associationRequests.iterator(); i.hasNext(); )
      {
        AssociationRequest ar = (AssociationRequest)i.next();
        ar.execute( pax );
      }
    }

    return list;
  }

  public List<Participant> getPreSelectedContributors( Long purlRecipientId )
  {
    List<Participant> list = new ArrayList<Participant>();

    PurlRecipient recipient = getPurlRecipientById( purlRecipientId );

    // Contributors in System who are already invited
    Set<Long> contributorUserIds = new HashSet<Long>();
    for ( PurlContributor contributor : recipient.getContributors() )
    {
      if ( null != contributor.getUser() )
      {
        contributorUserIds.add( contributor.getUser().getId() );
      }
    }

    // All Users in Recipient Nodes
    List<User> activeUserList = recipient.getNode().getActiveUserList();
    Set<Long> teamUserIds = new HashSet<Long>();

    for ( User user : activeUserList )
    {
      // Exclude Self and who are already invited
      if ( user.isParticipant() && !user.getId().equals( recipient.getUser().getId() ) && !contributorUserIds.contains( user.getId() ) )
      {
        // Using an association request might not be preferred this way
        AssociationRequest request = new UserAssociationRequest( UserAssociationRequest.EMAIL );
        request.execute( user );
        list.add( (Participant)user );
        teamUserIds.add( user.getId() );
      }
    }

    // Pax Social group(Follow list + Recognition Sent + Recognition
    // Received)
    List<Participant> preSelectedContributors = participantService.getAllPaxWhoHaveGivenOrReceivedRecognition( recipient.getUser().getId() );

    for ( Participant preSelectedContributor : preSelectedContributors )
    {
      // Exclude Self, Who are already invited and Who is not in team(team
      // is selected in the above
      // loop)
      if ( !preSelectedContributor.getId().equals( recipient.getUser().getId() ) && !contributorUserIds.contains( preSelectedContributor.getId() )
          && !teamUserIds.contains( preSelectedContributor.getId() ) )
      {
        list.add( preSelectedContributor );
      }
    }

    return list;
  }

  public List<TranslatedPurlContributorComment> getTranslatedContributorCommentsFor( Long purlRecipientId )
  {
    PurlRecipient recipient = getPurlRecipientById( purlRecipientId );
    LanguageType preferredLanguage = userService.getPreferredLanguageFor( recipient.getUser().getId() );

    // get all of the comments
    List<PurlContributorComment> comments = this.getComments( purlRecipientId );
    List<PurlContributorComment> commentsToTranslate = new ArrayList<PurlContributorComment>();

    // collect the comments that are eligible for translation
    for ( PurlContributorComment comment : comments )
    {
      if ( comment.getCommentsLanguageType() != null && !preferredLanguage.equals( comment.getCommentsLanguageType() ) )
      {
        commentsToTranslate.add( comment );
      }
    }

    List<TranslatedPurlContributorComment> translatedComments = new ArrayList<TranslatedPurlContributorComment>( commentsToTranslate.size() );

    // now iterate through the commentsToTranslate and translate them
    for ( PurlContributorComment comment : commentsToTranslate )
    {
      try
      {
        TranslatedContent tc = translationService.translate( comment.getCommentsLanguageType(), preferredLanguage, comment.getComments() );
        translatedComments.add( new TranslatedPurlContributorComment( comment.getId(), tc.getTranslatedContent() ) );
      }
      catch( UnsupportedTranslationException ex )
      {
        translatedComments.add( new TranslatedPurlContributorComment( comment.getId(), ContentReaderManager.getText( "purl.recipient", "TRANSLATION_UNAVAILABLE" ) ) );
      }
      catch( UnexpectedTranslationException ex )
      {
        translatedComments.add( new TranslatedPurlContributorComment( comment.getId(), ContentReaderManager.getText( "purl.recipient", "TRANSLATION_UNAVAILABLE" ) ) );
      }
    }

    return translatedComments;
  }

  public PurlContributor getContributorStepElementById( Long contributorId )
  {
    PurlContributor element = purlContributorDAO.getContributorStepElementById( contributorId );
    return element;
  }

  public List<Long> getPurlRecipientsForAutoInvite( Long promotionId )
  {
    return purlRecipientDAO.getPurlRecipientsForAutoInvite( promotionId );
  }

  public int getPurlRecipientsCountForAutoInvite( Long promotionId, Long purlRecipientId )
  {
    return purlRecipientDAO.getPurlRecipientsCountForAutoInvite( promotionId, purlRecipientId );
  }

  public void setPurlRecipientDAO( PurlRecipientDAO purlRecipientDAO )
  {
    this.purlRecipientDAO = purlRecipientDAO;
  }

  public void setPurlContributorDAO( PurlContributorDAO purlContributorDAO )
  {
    this.purlContributorDAO = purlContributorDAO;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public void setPurlContributorMediaDAO( PurlContributorMediaDAO purlContributorMediaDAO )
  {
    this.purlContributorMediaDAO = purlContributorMediaDAO;
  }

  public void setPurlContributorCommentDAO( PurlContributorCommentDAO purlContributorCommentDAO )
  {
    this.purlContributorCommentDAO = purlContributorCommentDAO;
  }

  public void setClaimService( ClaimService claimService )
  {
    this.claimService = claimService;
  }

  public void setMerchOrderDAO( MerchOrderDAO merchOrderDAO )
  {
    this.merchOrderDAO = merchOrderDAO;
  }

  public void setActivityDAO( ActivityDAO activityDAO )
  {
    this.activityDAO = activityDAO;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  public void setAppDataDirFileUploadStrategy( FileUploadStrategy appDataDirFileUploadStrategy )
  {
    this.appDataDirFileUploadStrategy = appDataDirFileUploadStrategy;
  }

  public void setWebdavFileUploadStrategy( FileUploadStrategy webdavFileUploadStrategy )
  {
    this.webdavFileUploadStrategy = webdavFileUploadStrategy;
  }

  public void setImageCropStrategy( ImageCropStrategy imageCropStrategy )
  {
    this.imageCropStrategy = imageCropStrategy;
  }

  public void setImageResizeStrategy( ImageResizeStrategy imageResizeStrategy )
  {
    this.imageResizeStrategy = imageResizeStrategy;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public void setTwitterService( TwitterService twitterService )
  {
    this.twitterService = twitterService;
  }

  public void setFacebookService( FacebookService facebookService )
  {
    this.facebookService = facebookService;
  }

  public void setProcessService( ProcessService processService )
  {
    this.processService = processService;
  }

  public void setNodeService( NodeService nodeService )
  {
    this.nodeService = nodeService;
  }

  public void setTranslationService( TranslationService translationService )
  {
    this.translationService = translationService;
  }

  public void setAcceptableExtentions( List<String> acceptableExtentions )
  {
    this.acceptableExtentions = acceptableExtentions;
  }

  public boolean isValidForInvitation( Long purlRecipientId )
  {
    PurlRecipient purlRecipient = getPurlRecipientById( purlRecipientId );
    if ( !purlRecipient.getUser().isActive() )
    {
      return false;
    }

    /* Purl invitation end check should be same as contribution end date */
    Date purlInvitationEndNotificationDate = purlRecipient.getCloseDate();
    return purlInvitationEndNotificationDate.after( new Date() );
  }

  @Override
  public PurlFileUploadValue uploadVideoForStandardMessage( PurlFileUploadValue data ) throws ServiceErrorException
  {
    try
    {
      data.setFull( ImageUtils.getPurlDetailPath( data.getType(), data.getId(), data.getName() ) );
      String filePath = data.getFull().substring( 0, data.getFull().lastIndexOf( "." ) );
      appDataDirFileUploadStrategy.uploadFileData( data.getFull(), data.getData() );
    }
    catch( Exception e )
    {
      throw new ServiceErrorException( "quiz.learning.IMAGE_UPLOAD_FAILED:" + e );
    }

    return data;
  }

  @Override
  public List<PurlCelebrationSet> getUpcomingPurlRecipients( String tabType, int pageSize, String sortedBy, String sortedOn, int startIndex, String lastName, String tile, String listValue )
      throws ServiceErrorException
  {
    Map<String, Object> searchParams = new HashMap<String, Object>();
    List<PurlCelebrationsView> purlRecipients = new ArrayList<PurlCelebrationsView>();
    List<PurlRecipient> purlRecipnts = new ArrayList<PurlRecipient>();
    //List<PurlRecipient> purlRecipients = new ArrayList<PurlRecipient>();
    String userLocale = UserManager.getUserLocale();
    List<PurlCelebrationSet> celebrationSetValueList = new ArrayList<PurlCelebrationSet>();
    Long userId = UserManager.getUserId();
    String purlPastPresentSelect = UPCOMING;
    List<PurlCelebrationSet> customSortedValueList = new ArrayList<PurlCelebrationSet>(); //Customization for the WIP#51332 
    List<PurlRecipient> sortedPurlRecipientsList = new ArrayList<PurlRecipient>();  //Customization for the WIP#51332
  
    int rowNumStart = ( startIndex - 1 ) * pageSize;
    int rowNumEnd = ( rowNumStart % 50 == 0 ) ? ( rowNumStart - 1 + pageSize ) : ( rowNumStart + pageSize );
    Long charaterticsDivisionId = getSystemVariableService().getPropertyByName( SystemVariableService.COKE_USER_DIVISION_KEY_CHAR ).getLongVal();
    
    searchParams.put( "pastOrUpcoming", purlPastPresentSelect );
    searchParams.put( "tabType", tabType );
    searchParams.put( "listValue", listValue );
    searchParams.put( "userLocale", userLocale );
    searchParams.put( "userId", userId );
    searchParams.put( "lastName", lastName );
    searchParams.put( "rowNumStart", rowNumStart + 1 );
    searchParams.put( "rowNumEnd", rowNumEnd + 1 );
    searchParams.put( "sortedBy", sortedBy );
    searchParams.put( "sortedOn", getPurlCelebrationsPageSortedOn( sortedOn ) );

    /* Relevant Tab */
    if ( tabType.equals( PurlCelebrationTabType.lookup( PurlCelebrationTabType.RECOMMENDED_TAB ).getCode() ) )
    {
      purlRecipients = purlRecipientDAO.getPurlRecipientsForCelebrationPage( searchParams );
      int globalTabCount = 0;

      if ( CollectionUtils.isNotEmpty( purlRecipients ) )
      {
        globalTabCount = purlRecipients.stream().findFirst().get().getTotalRecords();
      }
      celebrationSetValueList = getRelevantCelebrationSetFromPurlRecipients( purlPastPresentSelect, pageSize, startIndex, sortedBy, sortedOn, purlRecipients, globalTabCount, tile, listValue );
    }

    /* Global Tab */
    else if ( tabType.equals( PurlCelebrationTabType.lookup( PurlCelebrationTabType.GLOBAL_TAB ).getCode() ) )
    {
      int globalTabCount = 0;
      /*purlRecipients = purlRecipientDAO.getPurlRecipientsForCelebrationPage( searchParams );
      if ( CollectionUtils.isNotEmpty( purlRecipients ) )
      {
        globalTabCount = purlRecipients.stream().findFirst().get().getTotalRecords();
      }*/
      purlRecipients = purlRecipientDAO.getCustomSortOfUpcomingCelebration( charaterticsDivisionId ,userId, startIndex, pageSize );
      
      sortedPurlRecipientsList = customSortOfUpcomingCelebration(  purlRecipnts, userId ) ; //Customization for the WIP#51332
      if ( purlRecipients != null && purlRecipients.size() > 0 )
      {
        globalTabCount = purlRecipientDAO.getGlobalUpcomingPurlRecipientsCount();
      }      
      celebrationSetValueList = getGlobalCelebrationSetFromPurlRecipients( purlPastPresentSelect, pageSize, startIndex, sortedBy, sortedOn, purlRecipients, globalTabCount, tile  );
    }

    /* Team Tab */
    else if ( tabType.equals( PurlCelebrationTabType.lookup( PurlCelebrationTabType.TEAM_TAB ).getCode() ) )
    {
      Set<UserNode> userNodes = userService.getUserNodes( userId );
      List<Long> nodeIds = new ArrayList<Long>();
      int teamTabCount = 0;
      if ( userNodes != null )
      {
        for ( UserNode userNode : userNodes )
        {
          if ( userNode != null && userNode.getNode() != null )
          {
            nodeIds.add( userNode.getNode().getId() );
          }
        }
      }
      purlRecipients = purlRecipientDAO.getPurlRecipientsForCelebrationPage( searchParams );
      if ( CollectionUtils.isNotEmpty( purlRecipients ) )
      {
        teamTabCount = purlRecipients.stream().findFirst().get().getTotalRecords();
      }
      celebrationSetValueList = getTeamCelebrationSetFromPurlRecipients( purlPastPresentSelect, pageSize, startIndex, sortedBy, sortedOn, purlRecipients, teamTabCount, listValue );
    }

    /* Followed Tab */
    else if ( tabType.equals( PurlCelebrationTabType.lookup( PurlCelebrationTabType.FOLLOWED_TAB ).getCode() ) )
    {
      List<Long> followedUserIdsList = getFollowersByUserId( userId );
      int followedTabCount = 0;
      if ( followedUserIdsList.size() > 0 )
      {
        purlRecipients = purlRecipientDAO.getPurlRecipientsForCelebrationPage( searchParams );
      }
      if ( CollectionUtils.isNotEmpty( purlRecipients ) )
      {
        followedTabCount = purlRecipients.stream().findFirst().get().getTotalRecords();
      }
      celebrationSetValueList = getFollowedCelebrationSetFromPurlRecipients( purlPastPresentSelect, pageSize, startIndex, sortedBy, sortedOn, purlRecipients, followedTabCount, listValue );
    }

    else if ( tabType.equals( PurlCelebrationTabType.lookup( PurlCelebrationTabType.COUNTRY_TAB ).getCode() ) )
    {
      purlRecipients = purlRecipientDAO.getPurlRecipientsForCelebrationPage( searchParams );
      celebrationSetValueList = getCountryCelebrationSetFromPurlRecipients( purlPastPresentSelect, pageSize, startIndex, sortedBy, sortedOn, purlRecipients, 0, tile );
    }

    else if ( tabType.equals( PurlCelebrationTabType.lookup( PurlCelebrationTabType.DEPARTMENT_TAB ).getCode() ) )
    {
      purlRecipients = purlRecipientDAO.getPurlRecipientsForCelebrationPage( searchParams );
      celebrationSetValueList = getDepartmentCelebrationSetFromPurlRecipients( purlPastPresentSelect, pageSize, startIndex, sortedBy, sortedOn, purlRecipients, 0, tile,listValue );
    }
    else if ( tabType.equals( PurlCelebrationTabType.lookup( PurlCelebrationTabType.DIVISION_TAB ).getCode() ) )
    {
      purlRecipients = purlRecipientDAO.getPurlRecipientsForCelebrationPage( searchParams );
      celebrationSetValueList = getDivisionCelebrationSetFromPurlRecipients( purlPastPresentSelect, pageSize, startIndex, sortedBy, sortedOn, purlRecipients, 0, tile,listValue );
    }
     
    
    // New SA
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() && !CmxTranslateHelperBean.isCmxDefaultLocale() )
    {
      setCmxTranslateContent( celebrationSetValueList );
    }
    return celebrationSetValueList;
  }

  private void setCmxTranslateContent( List<PurlCelebrationSet> celebrationSetValueList )
  {
    if ( !celebrationSetValueList.isEmpty() )
    {
      List<String> translateKeys = getTranslateKeys( celebrationSetValueList );

      if ( !translateKeys.isEmpty() )
      {
        try
        {
          Map<String, String> translateBundles = cmxService.getTranslation( UserManager.getUserLocale(), translateKeys );

          if ( null != translateBundles && !translateBundles.isEmpty() )
          {
            CmxTranslateHelperBean helperBean = new CmxTranslateHelperBean( translateBundles );
            for ( Iterator<PurlCelebrationSet> celebrationSetItr = celebrationSetValueList.iterator(); celebrationSetItr.hasNext(); )
            {
              PurlCelebrationSet celebrationSetBean = celebrationSetItr.next();

              for ( Iterator<PurlCelebrationView> celebrationViewItr = celebrationSetBean.getCelebrations().iterator(); celebrationViewItr.hasNext(); )
              {
                PurlCelebrationView bean = celebrationViewItr.next();

                if ( StringUtils.isNotEmpty( bean.getCelebrationId() ) )
                {
                  bean.setAnniversary( helperBean.getCmxTranslatedValue( bean.getCelebLabelCmxCode(), bean.getAnniversary() ) );
                  bean.setPromotion( helperBean.getCmxTranslatedValue( bean.getProgramNameCmxCode(), bean.getPromotion() ) );
                }
              }
            }
          }

        }
        catch( DataException e )
        {
          logger.error( "Exception while Calling CMX Translation Service .Using default locale " + e.getMessage() );
        }
      }
    }
  }

  private List<String> getTranslateKeys( List<PurlCelebrationSet> celebrationSetValueList )
  {
    Set<String> translateKeys = new HashSet<String>();

    celebrationSetValueList.stream().forEach( set ->
    {
      if ( CollectionUtils.isNotEmpty( set.getCelebrations() ) )
      {
        set.getCelebrations().stream().forEach( c ->
        {
          if ( StringUtils.isNotEmpty( c.getCelebrationId() ) )
          {
            translateKeys.add( c.getCelebLabelCmxCode() );
            translateKeys.add( c.getProgramNameCmxCode() );
          }
        } );
      }

    } );

    return new ArrayList<String>( translateKeys );
  }

  @Override
  public List<PurlCelebration> getUpcomingPurlRecipients( int pageSize, String sortedBy, String sortedOn, int startIndex ) throws ServiceErrorException
  {
    List<PurlRecipient> purlRecipients = new ArrayList<PurlRecipient>();
    List<PurlCelebration> celebrationList = new ArrayList<PurlCelebration>();

    int rowNumStart = ( startIndex - 1 ) * pageSize;
    int rowNumEnd = ( rowNumStart % 50 == 0 ) ? ( rowNumStart - 1 + pageSize ) : ( rowNumStart + pageSize );

    purlRecipients = purlRecipientDAO.getGlobalUpcomingPurlRecipients( rowNumStart, rowNumEnd, sortedBy, sortedOn, pageSize );

    for ( PurlRecipient purlRecipient : purlRecipients )
    {
      PurlCelebration purlCelebration = new PurlCelebration();
      ParticipantInfoView participant = new ParticipantInfoView();

      participant.setId( purlRecipient.getUser().getId() );
      participant.setFirstName( purlRecipient.getUser().getFirstName() );
      participant.setLastName( purlRecipient.getUser().getLastName() );
      participant.setAvatarUrl( participantService.getParticipantById( purlRecipient.getUser().getId() ).getAvatarSmall() );

      purlCelebration.setId( purlRecipient.getId() );
      purlCelebration.setParticipant( participant );
      purlCelebration.setMilestone( purlRecipient.getCustomFormElementInfo() + " " + purlRecipient.getPromotion().getName() );
      purlCelebration.setContributeBy( DateUtils.toDisplayString( purlRecipient.getCloseDate() ) );

      celebrationList.add( purlCelebration );

    }

    return celebrationList;

  }

  @Override
  public int getUpcomingPurlRecipientsCount()
  {
    return purlRecipientDAO.getGlobalUpcomingPurlRecipientsCount();
  }

  private List<Long> getFollowersByUserId( Long userId )
  {
    List<Long> followedUserIdsList = getParticipantDAO().getFollowersByUserId( userId );
    return followedUserIdsList;
  }

  private List<PurlCelebrationSet> getRelevantCelebrationSetFromPurlRecipients( String purlPastPresentSelect,
                                                                                int pageSize,
                                                                                int startIndex,
                                                                                String sortedBy,
                                                                                String sortedOn,
                                                                                List<PurlCelebrationsView> purlRecipients,
                                                                                int globalTabCount,
                                                                                String tile,
                                                                                String listValue)
  {
    List<PurlCelebrationSet> celebrationSetValueList = new ArrayList<PurlCelebrationSet>();
    List<String> activeTabList = new ArrayList<String>();
    PurlCelebrationSetCountBean countBean = new PurlCelebrationSetCountBean();
    countBean.setRelevantCount( globalTabCount );
    String description;

    if ( purlPastPresentSelect.equals( UPCOMING ) )
    {
      if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
      {
        description = CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.UPCOMING_SA_DESC" );
      }
      else
      {
        description = CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.UPCOMING_PURLS_DESC" );
      }
    }
    else
    {
      if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
      {
        description = CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.PAST_SA_DESC" );
      }
      else
      {
        description = CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.PAST_PURLS_DESC" );
      }
    }
    
    PurlCelebrationTabType tab = PurlCelebrationTabType.lookup( PurlCelebrationTabType.RECOMMENDED_TAB );
    PurlCelebrationSet celebrationSetValue = createPurlCelebrationSet( tab, countBean.getRelevantCount(), description );
    celebrationSetValue.setCurrentPage( startIndex );
    celebrationSetValue.setItemsPerPage( pageSize );
    celebrationSetValue.setSortedOn( sortedOn );
    celebrationSetValue.setSortedBy( sortedBy );
    celebrationSetValue.setSortedBy( listValue );
    
    populatePurlCelebrationTableColumns( purlPastPresentSelect, celebrationSetValue, sortedBy, sortedOn );

    populatePurlCelebrationSetForRecipients( purlPastPresentSelect, PurlCelebrationTabType.RECOMMENDED_TAB, purlRecipients, celebrationSetValue, tile );

    if ( tile != null )
    {
      String[] activeTabs = getActiveTabList();
      for ( int i = 0; i < activeTabs.length; i++ )
      {
        activeTabList.add( activeTabs[i] );
      }
      if ( !activeTabList.contains( PurlCelebrationTabType.RECOMMENDED_TAB ) )
      {
        activeTabList.add( PurlCelebrationTabType.RECOMMENDED_TAB );
      }
    }
    else
    {
      activeTabList = Arrays.asList( getActiveTabList() );
    }
    if ( activeTabList.contains( PurlCelebrationTabType.RECOMMENDED_TAB ) )
    {
      celebrationSetValueList.add( celebrationSetValue );
    }
    addGlobal( celebrationSetValueList, countBean.getGlobalTabCount(), description );
    addTeam( celebrationSetValueList, countBean.getTeamTabCount(), description );
    addFollowed( celebrationSetValueList, UserManager.getUserId(), countBean.getFollowersTabCount(), description );
    addCountry( celebrationSetValueList, countBean.getCountryCount(), description );
    addDepartment( celebrationSetValueList, countBean.getDepartmentCount(), description );
    addDiviion( celebrationSetValueList, countBean.getDivisionCount(), description );
    return celebrationSetValueList;
  }

  private List<PurlCelebrationSet> getGlobalCelebrationSetFromPurlRecipients( String purlPastPresentSelect,
                                                                              int pageSize,
                                                                              int startIndex,
                                                                              String sortedBy,
                                                                              String sortedOn,
                                                                              List<PurlCelebrationsView> purlRecipients,
                                                                              int globalTabCount,
                                                                              String tile  )
  {
    List<PurlCelebrationSet> celebrationSetValueList = new ArrayList<PurlCelebrationSet>();
    List<String> activeTabList = new ArrayList<String>();
    PurlCelebrationSetCountBean countBean = new PurlCelebrationSetCountBean();
    countBean.setGlobalTabCount( globalTabCount );
    String description;
    if ( purlPastPresentSelect.equals( UPCOMING ) )
    {
      if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
      {
        description = CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.UPCOMING_SA_DESC" );
      }
      else
      {
        description = CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.UPCOMING_PURLS_DESC" );
      }
    }
    else
    {
      if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
      {
        description = CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.PAST_SA_DESC" );
      }
      else
      {
        description = CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.PAST_PURLS_DESC" );
      }
    }
  
    PurlCelebrationTabType tab = PurlCelebrationTabType.lookup( PurlCelebrationTabType.GLOBAL_TAB );
    PurlCelebrationSet celebrationSetValue = createPurlCelebrationSet( tab, countBean.getGlobalTabCount(), description );
    celebrationSetValue.setCurrentPage( startIndex );
    celebrationSetValue.setItemsPerPage( pageSize );
    celebrationSetValue.setSortedOn( sortedOn );
    celebrationSetValue.setSortedBy( sortedBy );
 
    
    populatePurlCelebrationTableColumns( purlPastPresentSelect, celebrationSetValue, sortedBy, sortedOn );

    populatePurlCelebrationSetForRecipients( purlPastPresentSelect, PurlCelebrationTabType.GLOBAL_TAB, purlRecipients, celebrationSetValue, tile );

    if ( tile != null )
    {
      String[] activeTabs = getActiveTabList();
      for ( int i = 0; i < activeTabs.length; i++ )
      {
        activeTabList.add( activeTabs[i] );
      }
      if ( !activeTabList.contains( PurlCelebrationTabType.GLOBAL_TAB ) )
      {
        activeTabList.add( PurlCelebrationTabType.GLOBAL_TAB );
      }
    }
    else
    {
      activeTabList = Arrays.asList( getActiveTabList() );
    }
    if ( activeTabList.contains( PurlCelebrationTabType.GLOBAL_TAB ) )
    {
      celebrationSetValueList.add( celebrationSetValue );
    }
    addTeam( celebrationSetValueList, countBean.getTeamTabCount(), description );
    addFollowed( celebrationSetValueList, UserManager.getUserId(), countBean.getFollowersTabCount(), description );
    addRelevant( celebrationSetValueList, countBean.getRelevantCount(), description );
    addCountry( celebrationSetValueList, countBean.getCountryCount(), description );
    addDepartment( celebrationSetValueList, countBean.getDepartmentCount(), description );
    addDiviion( celebrationSetValueList, countBean.getDivisionCount(), description );
    
    return celebrationSetValueList;
  }

  private List<PurlCelebrationSet> getFollowedCelebrationSetFromPurlRecipients( String purlPastPresentSelect,
                                                                                int pageSize,
                                                                                int startIndex,
                                                                                String sortedBy,
                                                                                String sortedOn,
                                                                                List<PurlCelebrationsView> purlRecipients,
                                                                                int followedTabCount,
                                                                                String listValue  )
  {
    List<PurlCelebrationSet> celebrationSetValueList = new ArrayList<PurlCelebrationSet>();
    PurlCelebrationSetCountBean countBean = new PurlCelebrationSetCountBean();
    countBean.setFollowersTabCount( followedTabCount );
    String description;
    if ( purlPastPresentSelect.equals( UPCOMING ) )
    {
      if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
      {
        description = CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.UPCOMING_SA_DESC" );
      }
      else
      {
        description = CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.UPCOMING_PURLS_DESC" );
      }
    }
    else
    {
      if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
      {
        description = CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.PAST_SA_DESC" );
      }
      else
      {
        description = CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.PAST_PURLS_DESC" );
      }
    }
     
    PurlCelebrationTabType tab = PurlCelebrationTabType.lookup( PurlCelebrationTabType.FOLLOWED_TAB );
    PurlCelebrationSet celebrationSetValue = createPurlCelebrationSet( tab, countBean.getFollowersTabCount(), description );
    celebrationSetValue.setCurrentPage( startIndex );
    celebrationSetValue.setItemsPerPage( pageSize );
    celebrationSetValue.setSortedOn( sortedOn );
    celebrationSetValue.setSortedBy( sortedBy );
    celebrationSetValue.setSortedBy( listValue );
    
    populatePurlCelebrationTableColumns( purlPastPresentSelect, celebrationSetValue, sortedBy, sortedOn );

    populatePurlCelebrationSetForRecipients( purlPastPresentSelect, PurlCelebrationTabType.FOLLOWED_TAB, purlRecipients, celebrationSetValue, null );

    addGlobal( celebrationSetValueList, countBean.getGlobalTabCount(), description );
    addTeam( celebrationSetValueList, countBean.getTeamTabCount(), description );
    if ( Arrays.asList( getActiveTabList() ).contains( PurlCelebrationTabType.FOLLOWED_TAB ) )
    {
      celebrationSetValueList.add( celebrationSetValue );
    }
    addRelevant( celebrationSetValueList, countBean.getRelevantCount(), description );
    addCountry( celebrationSetValueList, countBean.getCountryCount(), description );
    addDepartment( celebrationSetValueList, countBean.getDepartmentCount(), description );
    addDiviion( celebrationSetValueList, countBean.getDivisionCount(), description );
    
    return celebrationSetValueList;
  }

  private List<PurlCelebrationSet> getTeamCelebrationSetFromPurlRecipients( String purlPastPresentSelect,
                                                                            int pageSize,
                                                                            int startIndex,
                                                                            String sortedBy,
                                                                            String sortedOn,
                                                                            List<PurlCelebrationsView> purlRecipients,
                                                                            int teamTabCount,
                                                                            String listValue  )
  {
    List<PurlCelebrationSet> celebrationSetValueList = new ArrayList<PurlCelebrationSet>();

    PurlCelebrationSetCountBean countBean = new PurlCelebrationSetCountBean();
    countBean.setTeamTabCount( teamTabCount );
    String description;
    if ( purlPastPresentSelect.equals( UPCOMING ) )
    {
      if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
      {
        description = CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.UPCOMING_SA_DESC" );
      }
      else
      {
        description = CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.UPCOMING_PURLS_DESC" );
      }
    }
    else
    {
      if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
      {
        description = CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.PAST_SA_DESC" );
      }
      else
      {
        description = CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.PAST_PURLS_DESC" );
      }
    }
    
    PurlCelebrationTabType tab = PurlCelebrationTabType.lookup( PurlCelebrationTabType.TEAM_TAB );
    PurlCelebrationSet celebrationSetValue = createPurlCelebrationSet( tab, countBean.getTeamTabCount(), description );
    celebrationSetValue.setCurrentPage( startIndex );
    celebrationSetValue.setItemsPerPage( pageSize );
    celebrationSetValue.setSortedOn( sortedOn );
    celebrationSetValue.setSortedBy( sortedBy );
    celebrationSetValue.setSortedBy( listValue );
    
    populatePurlCelebrationTableColumns( purlPastPresentSelect, celebrationSetValue, sortedBy, sortedOn );

    populatePurlCelebrationSetForRecipients( purlPastPresentSelect, PurlCelebrationTabType.TEAM_TAB, purlRecipients, celebrationSetValue, null );

    addGlobal( celebrationSetValueList, countBean.getGlobalTabCount(), description );
    if ( Arrays.asList( getActiveTabList() ).contains( PurlCelebrationTabType.TEAM_TAB ) )
    {
      celebrationSetValueList.add( celebrationSetValue );
    }
    addFollowed( celebrationSetValueList, UserManager.getUserId(), countBean.getFollowersTabCount(), description );
    addRelevant( celebrationSetValueList, countBean.getRelevantCount(), description );
    addCountry( celebrationSetValueList, countBean.getCountryCount(), description );
    addDepartment( celebrationSetValueList, countBean.getDepartmentCount(), description );
    addDiviion( celebrationSetValueList, countBean.getDivisionCount(), description );
    
    return celebrationSetValueList;
  }

  private List<PurlCelebrationSet> getCountryCelebrationSetFromPurlRecipients( String purlPastPresentSelect,
                                                                               int pageSize,
                                                                               int startIndex,
                                                                               String sortedBy,
                                                                               String sortedOn,
                                                                               List<PurlCelebrationsView> purlRecipients,
                                                                               int countryTabCount,
                                                                               String tile )
  {
    List<PurlCelebrationSet> celebrationSetValueList = new ArrayList<PurlCelebrationSet>();
    List<String> activeTabList = new ArrayList<String>();
    PurlCelebrationSetCountBean countBean = new PurlCelebrationSetCountBean();
    countBean.setCountryCount( countryTabCount );
    String description;
    if ( purlPastPresentSelect.equals( UPCOMING ) )
    {
      if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
      {
        description = CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.UPCOMING_SA_DESC" );
      }
      else
      {
        description = CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.UPCOMING_PURLS_DESC" );
      }
    }
    else
    {
      if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
      {
        description = CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.PAST_SA_DESC" );
      }
      else
      {
        description = CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.PAST_PURLS_DESC" );
      }
    }

    PurlCelebrationTabType tab = PurlCelebrationTabType.lookup( PurlCelebrationTabType.COUNTRY_TAB );
    PurlCelebrationSet celebrationSetValue = createPurlCelebrationSet( tab, countBean.getCountryCount(), description );
    celebrationSetValue.setCurrentPage( startIndex );
    celebrationSetValue.setItemsPerPage( pageSize );
    celebrationSetValue.setSortedOn( sortedOn );
    celebrationSetValue.setSortedBy( sortedBy );

    populatePurlCelebrationTableColumns( purlPastPresentSelect, celebrationSetValue, sortedBy, sortedOn );

    populatePurlCelebrationSetForRecipients( purlPastPresentSelect, PurlCelebrationTabType.COUNTRY_TAB, purlRecipients, celebrationSetValue, tile );

    if ( tile != null )
    {
      String[] activeTabs = getActiveTabList();
      for ( int i = 0; i < activeTabs.length; i++ )
      {
        activeTabList.add( activeTabs[i] );
      }
      if ( !activeTabList.contains( PurlCelebrationTabType.COUNTRY_TAB ) )
      {
        activeTabList.add( PurlCelebrationTabType.COUNTRY_TAB );
      }
    }
    else
    {
      activeTabList = Arrays.asList( getActiveTabList() );
    }
    if ( activeTabList.contains( PurlCelebrationTabType.COUNTRY_TAB ) )
    {
      celebrationSetValueList.add( celebrationSetValue );
    }
    addGlobal( celebrationSetValueList, countBean.getGlobalTabCount(), description );
    addTeam( celebrationSetValueList, countBean.getTeamTabCount(), description );
    addFollowed( celebrationSetValueList, UserManager.getUserId(), countBean.getFollowersTabCount(), description );
    addRelevant( celebrationSetValueList, countBean.getRelevantCount(), description );
    addDepartment( celebrationSetValueList, countBean.getDepartmentCount(), description );
    addDiviion( celebrationSetValueList, countBean.getDivisionCount(), description );
    
    return celebrationSetValueList;
  }

  private List<PurlCelebrationSet> getDepartmentCelebrationSetFromPurlRecipients( String purlPastPresentSelect,
                                                                                  int pageSize,
                                                                                  int startIndex,
                                                                                  String sortedBy,
                                                                                  String sortedOn,
                                                                                  List<PurlCelebrationsView> purlRecipients,
                                                                                  int departmentTabCount,
                                                                                  String tile,
                                                                                  String listValue)
  {
    List<PurlCelebrationSet> celebrationSetValueList = new ArrayList<PurlCelebrationSet>();
    List<String> activeTabList = new ArrayList<String>();
    PurlCelebrationSetCountBean countBean = new PurlCelebrationSetCountBean();
    countBean.setDepartmentCount( departmentTabCount );
    String description;
    if ( purlPastPresentSelect.equals( UPCOMING ) )
    {
      if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
      {
        description = CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.UPCOMING_SA_DESC" );
      }
      else
      {
        description = CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.UPCOMING_PURLS_DESC" );
      }
    }
    else
    {
      if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
      {
        description = CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.PAST_SA_DESC" );
      }
      else
      {
        description = CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.PAST_PURLS_DESC" );
      }
    }
   
    PurlCelebrationTabType tab = PurlCelebrationTabType.lookup( PurlCelebrationTabType.DEPARTMENT_TAB );
    PurlCelebrationSet celebrationSetValue = createPurlCelebrationSet( tab, countBean.getDepartmentCount(), description );
    celebrationSetValue.setCurrentPage( startIndex );
    celebrationSetValue.setItemsPerPage( pageSize );
    celebrationSetValue.setSortedOn( sortedOn );
    celebrationSetValue.setSortedBy( sortedBy );
    celebrationSetValue.setSortedBy( listValue );
    
    populatePurlCelebrationTableColumns( purlPastPresentSelect, celebrationSetValue, sortedBy, sortedOn );

    populatePurlCelebrationSetForRecipients( purlPastPresentSelect, PurlCelebrationTabType.DEPARTMENT_TAB, purlRecipients, celebrationSetValue, tile );

    if ( tile != null )
    {
      String[] activeTabs = getActiveTabList();
      for ( int i = 0; i < activeTabs.length; i++ )
      {
        activeTabList.add( activeTabs[i] );
      }
      if ( !activeTabList.contains( PurlCelebrationTabType.DEPARTMENT_TAB ) )
      {
        activeTabList.add( PurlCelebrationTabType.DEPARTMENT_TAB );
      }
    }
    else
    {
      activeTabList = Arrays.asList( getActiveTabList() );
    }
    if ( activeTabList.contains( PurlCelebrationTabType.DEPARTMENT_TAB ) )
    {
      celebrationSetValueList.add( celebrationSetValue );
    }
    addGlobal( celebrationSetValueList, countBean.getGlobalTabCount(), description );
    addTeam( celebrationSetValueList, countBean.getTeamTabCount(), description );
    addFollowed( celebrationSetValueList, UserManager.getUserId(), countBean.getFollowersTabCount(), description );
    addRelevant( celebrationSetValueList, countBean.getRelevantCount(), description );
    addCountry( celebrationSetValueList, countBean.getCountryCount(), description );
    addDiviion( celebrationSetValueList, countBean.getDivisionCount(), description );
    
    return celebrationSetValueList;
  }

  
  private List<PurlCelebrationSet> getSearchByUserCelebrationSetFromPurlRecipients( String purlPastPresentSelect,
                                                                                    int pageSize,
                                                                                    int startIndex,
                                                                                    String sortedBy,
                                                                                    String sortedOn,
                                                                                    List<PurlCelebrationsView> purlRecipients,
                                                                                    int searchTabCount )
  {
    List<PurlCelebrationSet> celebrationSetValueList = new ArrayList<PurlCelebrationSet>();

    PurlCelebrationSetCountBean countBean = new PurlCelebrationSetCountBean();
    countBean.setSearchTabCount( searchTabCount );

    PurlCelebrationTabType tab = PurlCelebrationTabType.lookup( PurlCelebrationTabType.SEARCH_TAB );
    PurlCelebrationSet celebrationSetValue = createPurlCelebrationSet( tab,
                                                                       countBean.getSearchTabCount(),
                                                                       CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.SEARCH_UPCOMING_PURLS_DESC" ) );
    celebrationSetValue.setCurrentPage( startIndex );
    celebrationSetValue.setItemsPerPage( pageSize );
    celebrationSetValue.setSortedOn( sortedOn );
    celebrationSetValue.setSortedBy( sortedBy );

    if ( purlRecipients != null && purlRecipients.size() > 0 )
    {
      populatePurlCelebrationTableColumns( purlPastPresentSelect, celebrationSetValue, sortedBy, sortedOn );
    }

    populatePurlCelebrationSetForRecipients( purlPastPresentSelect, PurlCelebrationTabType.SEARCH_TAB, purlRecipients, celebrationSetValue, null );

    if ( Arrays.asList( getActiveTabList() ).contains( PurlCelebrationTabType.SEARCH_TAB ) )
    {
      celebrationSetValueList.add( celebrationSetValue );
    }

    return celebrationSetValueList;
  }


  private List<PurlCelebrationSet> getDivisionCelebrationSetFromPurlRecipients( String purlPastPresentSelect,
                                                                                  int pageSize,
                                                                                  int startIndex,
                                                                                  String sortedBy,
                                                                                  String sortedOn,
                                                                                  List<PurlCelebrationsView> purlRecipients,
                                                                                  int divisionTabCount,
                                                                                  String tile,
                                                                                  String listValue)
  {
    List<PurlCelebrationSet> celebrationSetValueList = new ArrayList<PurlCelebrationSet>();
    List<String> activeTabList = new ArrayList<String>();
    PurlCelebrationSetCountBean countBean = new PurlCelebrationSetCountBean();
    countBean.setDivisionCount( divisionTabCount );
    String description;
    if ( purlPastPresentSelect.equals( UPCOMING ) )
    {
      if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
      {
        description = CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.UPCOMING_SA_DESC" );
      }
      else
      {
        description = CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.UPCOMING_PURLS_DESC" );
      }
    }
    else
    {
      if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
      {
        description = CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.PAST_SA_DESC" );
      }
      else
      {
        description = CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.PAST_PURLS_DESC" );
      }
    }
   
    PurlCelebrationTabType tab = PurlCelebrationTabType.lookup( PurlCelebrationTabType.DIVISION_TAB );
    PurlCelebrationSet celebrationSetValue = createPurlCelebrationSet( tab, countBean.getDepartmentCount(), description );
    celebrationSetValue.setCurrentPage( startIndex );
    celebrationSetValue.setItemsPerPage( pageSize );
    celebrationSetValue.setSortedOn( sortedOn );
    celebrationSetValue.setSortedBy( sortedBy );
    celebrationSetValue.setSortedBy( listValue );
    
    populatePurlCelebrationTableColumns( purlPastPresentSelect, celebrationSetValue, sortedBy, sortedOn );

    populatePurlCelebrationSetForRecipients( purlPastPresentSelect, PurlCelebrationTabType.DIVISION_TAB, purlRecipients, celebrationSetValue, tile );

    if ( tile != null )
    {
      String[] activeTabs = getActiveTabList();
      for ( int i = 0; i < activeTabs.length; i++ )
      {
        activeTabList.add( activeTabs[i] );
      }
      if ( !activeTabList.contains( PurlCelebrationTabType.DIVISION_TAB ) )
      {
        activeTabList.add( PurlCelebrationTabType.DIVISION_TAB );
      }
    }
    else
    {
      activeTabList = Arrays.asList( getActiveTabList() );
    }
    if ( activeTabList.contains( PurlCelebrationTabType.DIVISION_TAB ) )
    {
      celebrationSetValueList.add( celebrationSetValue );
    }
    addGlobal( celebrationSetValueList, countBean.getGlobalTabCount(), description );
    addTeam( celebrationSetValueList, countBean.getTeamTabCount(), description );
    addFollowed( celebrationSetValueList, UserManager.getUserId(), countBean.getFollowersTabCount(), description );
    addRelevant( celebrationSetValueList, countBean.getRelevantCount(), description );
    addCountry( celebrationSetValueList, countBean.getCountryCount(), description );
    addDepartment( celebrationSetValueList, countBean.getDepartmentCount(), description );
    
    return celebrationSetValueList;
  }
  
  private void populatePurlCelebrationTableColumns( String purlPastPresentSelect, PurlCelebrationSet celebrationSetValue, String sortedBy, String sortedOn )
  {
    List<PurlCelebrationTableColumnsView> tableColumns = new ArrayList<PurlCelebrationTableColumnsView>();
    Map<String, Object> clientStateParameterMap1 = new HashMap<String, Object>();
    Map<String, Object> clientStateParameterMap2 = new HashMap<String, Object>();
    Map<String, Object> clientStateParameterMap3 = new HashMap<String, Object>();
    Map<String, Object> clientStateParameterMap4 = new HashMap<String, Object>();

    if ( sortedOn.equals( "1" ) && sortedBy.equals( "asc" ) )
    {
      clientStateParameterMap1.put( "sortedOn", "1" );
      clientStateParameterMap1.put( "sortedBy", "desc" );
      clientStateParameterMap2.put( "sortedOn", "2" );
      clientStateParameterMap2.put( "sortedBy", "asc" );
      clientStateParameterMap3.put( "sortedOn", "3" );
      clientStateParameterMap3.put( "sortedBy", "asc" );
      clientStateParameterMap4.put( "sortedOn", "4" );
      clientStateParameterMap4.put( "sortedBy", "asc" );
    }
    else if ( sortedOn.equals( "1" ) && sortedBy.equals( "desc" ) )
    {
      clientStateParameterMap1.put( "sortedOn", "1" );
      clientStateParameterMap1.put( "sortedBy", "asc" );
      clientStateParameterMap2.put( "sortedOn", "2" );
      clientStateParameterMap2.put( "sortedBy", "asc" );
      clientStateParameterMap3.put( "sortedOn", "3" );
      clientStateParameterMap3.put( "sortedBy", "asc" );
      clientStateParameterMap4.put( "sortedOn", "4" );
      clientStateParameterMap4.put( "sortedBy", "asc" );
    }
    else if ( sortedOn.equals( "2" ) && sortedBy.equals( "asc" ) )
    {
      clientStateParameterMap1.put( "sortedOn", "1" );
      clientStateParameterMap1.put( "sortedBy", "asc" );
      clientStateParameterMap2.put( "sortedOn", "2" );
      clientStateParameterMap2.put( "sortedBy", "desc" );
      clientStateParameterMap3.put( "sortedOn", "3" );
      clientStateParameterMap3.put( "sortedBy", "asc" );
      clientStateParameterMap4.put( "sortedOn", "4" );
      clientStateParameterMap4.put( "sortedBy", "asc" );
    }
    else if ( sortedOn.equals( "2" ) && sortedBy.equals( "desc" ) )
    {
      clientStateParameterMap1.put( "sortedOn", "1" );
      clientStateParameterMap1.put( "sortedBy", "asc" );
      clientStateParameterMap2.put( "sortedOn", "2" );
      clientStateParameterMap2.put( "sortedBy", "asc" );
      clientStateParameterMap3.put( "sortedOn", "3" );
      clientStateParameterMap3.put( "sortedBy", "asc" );
      clientStateParameterMap4.put( "sortedOn", "4" );
      clientStateParameterMap4.put( "sortedBy", "asc" );
    }
    else if ( sortedOn.equals( "3" ) && sortedBy.equals( "asc" ) )
    {
      clientStateParameterMap1.put( "sortedOn", "1" );
      clientStateParameterMap1.put( "sortedBy", "asc" );
      clientStateParameterMap2.put( "sortedOn", "2" );
      clientStateParameterMap2.put( "sortedBy", "asc" );
      clientStateParameterMap3.put( "sortedOn", "3" );
      clientStateParameterMap3.put( "sortedBy", "desc" );
      clientStateParameterMap4.put( "sortedOn", "4" );
      clientStateParameterMap4.put( "sortedBy", "asc" );
    }
    else if ( sortedOn.equals( "3" ) && sortedBy.equals( "desc" ) )
    {
      clientStateParameterMap1.put( "sortedOn", "1" );
      clientStateParameterMap1.put( "sortedBy", "asc" );
      clientStateParameterMap2.put( "sortedOn", "2" );
      clientStateParameterMap2.put( "sortedBy", "asc" );
      clientStateParameterMap3.put( "sortedOn", "3" );
      clientStateParameterMap3.put( "sortedBy", "asc" );
      clientStateParameterMap4.put( "sortedOn", "4" );
      clientStateParameterMap4.put( "sortedBy", "asc" );
    }
    else if ( sortedOn.equals( "4" ) && sortedBy.equals( "asc" ) )
    {
      clientStateParameterMap1.put( "sortedOn", "1" );
      clientStateParameterMap1.put( "sortedBy", "asc" );
      clientStateParameterMap2.put( "sortedOn", "2" );
      clientStateParameterMap2.put( "sortedBy", "asc" );
      clientStateParameterMap3.put( "sortedOn", "3" );
      clientStateParameterMap3.put( "sortedBy", "asc" );
      clientStateParameterMap4.put( "sortedOn", "4" );
      clientStateParameterMap4.put( "sortedBy", "desc" );
    }
    else if ( sortedOn.equals( "4" ) && sortedBy.equals( "desc" ) )
    {
      clientStateParameterMap1.put( "sortedOn", "1" );
      clientStateParameterMap1.put( "sortedBy", "asc" );
      clientStateParameterMap2.put( "sortedOn", "2" );
      clientStateParameterMap2.put( "sortedBy", "asc" );
      clientStateParameterMap3.put( "sortedOn", "3" );
      clientStateParameterMap3.put( "sortedBy", "asc" );
      clientStateParameterMap4.put( "sortedOn", "4" );
      clientStateParameterMap4.put( "sortedBy", "asc" );
    }
    String siteUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    String sortUrl1 = ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.PURL_CELEBRATION_PAGE_URL, clientStateParameterMap1 );
    String sortUrl2 = ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.PURL_CELEBRATION_PAGE_URL, clientStateParameterMap2 );
    String sortUrl3 = ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.PURL_CELEBRATION_PAGE_URL, clientStateParameterMap3 );
    String sortUrl4 = ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.PURL_CELEBRATION_PAGE_URL, clientStateParameterMap4 );

    PurlCelebrationTableColumnsView column1 = new PurlCelebrationTableColumnsView();

    column1.setId( (long)1 );
    column1.setName( "recipient" );
    column1.setDisplayName( CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.RECIPIENT" ) );
    column1.setSortable( true );
    column1.setSortUrl( sortUrl1 );
    tableColumns.add( column1 );

    PurlCelebrationTableColumnsView column2 = new PurlCelebrationTableColumnsView();

    column2.setId( (long)2 );
    column2.setName( "aniversary" );
    column2.setDisplayName( CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.ANNIVERSARY_NO_OF" ) );
    column2.setSortable( true );
    column2.setSortUrl( sortUrl2 );
    tableColumns.add( column2 );

    PurlCelebrationTableColumnsView column3 = new PurlCelebrationTableColumnsView();

    column3.setId( (long)3 );
    column3.setName( "promotion" );
    column3.setDisplayName( CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.PROMOTION" ) );
    column3.setSortable( true );
    column3.setSortUrl( sortUrl3 );
    tableColumns.add( column3 );

    PurlCelebrationTableColumnsView column4 = new PurlCelebrationTableColumnsView();

    column4.setId( (long)4 );
    if ( purlPastPresentSelect.equals( UPCOMING ) )
    {
      if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
      {
        column4.setName( "celebration" );
        column4.setDisplayName( CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.CELEBRATION_DATE" ) );
      }
      else
      {
        column4.setName( "contribute by" );
        column4.setDisplayName( CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.CONTRIBUTE_BY" ) );
      }
    }
    else
    {
      column4.setName( "view by" );
      column4.setDisplayName( CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.VIEW_BY" ) );
    }
    column4.setSortable( true );
    column4.setSortUrl( sortUrl4 );
    tableColumns.add( column4 );

    PurlCelebrationTableColumnsView column5 = new PurlCelebrationTableColumnsView();

    column5.setId( (long)4 );
    if ( purlPastPresentSelect.equals( UPCOMING ) )
    {
      column5.setName( "contribute action" );
    }
    else
    {
      column5.setName( "view action" );
    }
    column5.setSortable( false );
    column5.setSortUrl( "" );
    tableColumns.add( column5 );

    celebrationSetValue.setTableColumns( tableColumns );
  }

  private void populatePurlCelebrationSetForRecipients( String purlPastPresentSelect, String tabType, List<PurlCelebrationsView> purlRecipients, PurlCelebrationSet celebrationSetValue, String tile )
  {
    List<PurlCelebrationView> celebrations = new ArrayList<PurlCelebrationView>();
    int contributorCount = 0;
    String url = null;

    for ( PurlCelebrationsView purlRecipient : purlRecipients )
    {
      PurlCelebrationView purlCelebrationView = new PurlCelebrationView();

      purlCelebrationView.setId( purlRecipient.getUserId() );
      purlCelebrationView.setFirstName( purlRecipient.getFirstName() );
      purlCelebrationView.setLastName( purlRecipient.getLastName() );
      purlCelebrationView.setAvatarUrl( purlRecipient.getRecipientAvatar() );
      Map<String, Object> clientStateParameterMap = new HashMap<String, Object>();
      if ( purlRecipient.getPromotionId() != null )
      {
        clientStateParameterMap.put( "promotionId", purlRecipient.getPromotionId() );
      }
      if ( purlPastPresentSelect != null && purlPastPresentSelect.equals( UPCOMING ) )
      {
        if ( purlRecipient.getContributorId() != null )
        {
          clientStateParameterMap.put( "purlContributorId", purlRecipient.getContributorId() );
          contributorCount++;
        }
        if ( contributorCount == 0 )
        {
          clientStateParameterMap.put( "purlRecipientId", purlRecipient.getRecipientId() );
          clientStateParameterMap.put( "userId", UserManager.getUserId() );
        }
        contributorCount = 0;
        url = ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                    PageConstants.ALERT_DETAIL_PURL_CONTRIBUTION_SINGLE,
                                                    clientStateParameterMap );
        purlCelebrationView.setContributeUrl( url );
        if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
        {
          purlCelebrationView.setExpirationDate( DateUtils.toDisplayString( purlRecipient.getAwardDate() ) );
        }
        else
        {
          purlCelebrationView.setExpirationDate( DateUtils.toDisplayString( getContributionCloseDate( purlRecipient.getAwardDate() ) ) );
        }
        purlCelebrationView.setPromotion( purlRecipient.getPromotionNameFromCM() );
        purlCelebrationView.setAnniversary( purlRecipient.getAnniversary() );
        purlCelebrationView.setPositionType( purlRecipient.getPositionType() );
        purlCelebrationView.setPrimaryColor( purlRecipient.getPrimaryColor() );
        purlCelebrationView.setSecondaryColor( purlRecipient.getSecondaryColor() );
        purlCelebrationView.setCelebrationId( purlRecipient.getCelebrationId() );
        purlCelebrationView.setCelebLabelCmxCode( purlRecipient.getCelebLabelCmxCode() );
        purlCelebrationView.setProgramNameCmxCode( purlRecipient.getProgramNameCmxCode() );
      }
      else
      {
        clientStateParameterMap.put( "purlRecipientId", purlRecipient.getRecipientId() );
        url = ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                    PageConstants.ALERT_DETAIL_PURL_RECIPIENT,
                                                    clientStateParameterMap );
        purlCelebrationView.setViewUrl( url );

        Calendar c = Calendar.getInstance();
        c.setTime( purlRecipient.getAwardDate() );
        c.add( Calendar.DATE, systemVariableService.getPropertyByName( SystemVariableService.PURL_DAYS_TO_EXP ).getIntVal() );
        purlCelebrationView.setExpirationDate( DateUtils.toDisplayString( DateUtils.toEndDate( c.getTime() ) ) );
        purlCelebrationView.setPromotion( purlRecipient.getPromotionNameFromCM() );
        purlCelebrationView.setAnniversary( purlRecipient.getAnniversary() );
        purlCelebrationView.setPositionType( purlRecipient.getPositionType() );
        purlCelebrationView.setPrimaryColor( purlRecipient.getPrimaryColor() );
        purlCelebrationView.setSecondaryColor( purlRecipient.getSecondaryColor() );
        purlCelebrationView.setCelebrationId( purlRecipient.getCelebrationId() );
        purlCelebrationView.setCelebLabelCmxCode( purlRecipient.getCelebLabelCmxCode() );
        purlCelebrationView.setProgramNameCmxCode( purlRecipient.getProgramNameCmxCode() );
        /*
         * if ( tile != null ) { purlCelebrationView.setPromotion(
         * purlRecipient.getPromotionNameFromCM() ); } // kumarp added else { if (
         * purlRecipient.getCustomFormElementInfo() != null ) { purlCelebrationView.setPromotion(
         * purlRecipient.getCustomFormElementInfo() + " " + promotionName ); } else {
         * purlCelebrationView.setPromotion( promotionName ); } }
         */
      }

      if ( tabType.equals( PurlCelebrationTabType.RECOMMENDED_TAB ) || tabType.equals( PurlCelebrationTabType.SEARCH_TAB ) || tabType.equals( PurlCelebrationTabType.FOLLOWED_TAB )
          || tabType.equals( PurlCelebrationTabType.TEAM_TAB ) || tabType.equals( PurlCelebrationTabType.GLOBAL_TAB ) )
      {
        int numberOfDaysLeft = DateUtils.getNumberOfDaysLeft( DateUtils.getCurrentDate(), purlRecipient.getAwardDate() );
        String timeLeftMessage = null;
        if ( numberOfDaysLeft == 1 )
        {
          timeLeftMessage = CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.TODAY" );
          purlCelebrationView.setToday( true );
        }
        else
        {
          timeLeftMessage = MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "purl.celebration.module.DAYS_LEFT" ), new Object[] { numberOfDaysLeft } );
        }
        purlCelebrationView.setTimeLeft( timeLeftMessage );
      }

      celebrations.add( purlCelebrationView );
    }

    celebrationSetValue.setCelebrations( celebrations );
  }

  public static void main( String[] args )
  {
    System.out.println( DateUtils.getNumberOfDaysLeft( DateUtils.getCurrentDate(), DateUtils.getDateAfterNumberOfDays( DateUtils.getCurrentDate(), 1 ) ) );
  }

  private PurlCelebrationSet createPurlCelebrationSet( PurlCelebrationTabType tab, long count, String description )
  {
    String defaultTab = systemVariableService.getPropertyByName( SystemVariableService.PURL_CELEBRATION_DEFAULT_TAB_NAME ).getStringVal();

    PurlCelebrationSet celebrationSetValue = new PurlCelebrationSet( tab.getCode(), tab.getName(), count, description, tab.getCode().equals( defaultTab ) );
    return celebrationSetValue;
  }

  private void addGlobal( List<PurlCelebrationSet> celebrationSetValueList, long count, String description )
  {
    PurlCelebrationTabType tab = PurlCelebrationTabType.lookup( PurlCelebrationTabType.GLOBAL_TAB );
    if ( Arrays.asList( getActiveTabList() ).contains( PurlCelebrationTabType.GLOBAL_TAB ) )
    {
      celebrationSetValueList.add( createPurlCelebrationSet( tab, count, description ) );
    }
  }

  private void addTeam( List<PurlCelebrationSet> celebrationSetValueList, long count, String description )
  {
    PurlCelebrationTabType tab = PurlCelebrationTabType.lookup( PurlCelebrationTabType.TEAM_TAB );
    if ( Arrays.asList( getActiveTabList() ).contains( PurlCelebrationTabType.TEAM_TAB ) )
    {
      celebrationSetValueList.add( createPurlCelebrationSet( tab, count, description ) );
    }
  }

  private void addFollowed( List<PurlCelebrationSet> celebrationSetValueList, Long participantId, long count, String description )
  {
    PurlCelebrationTabType tab = PurlCelebrationTabType.lookup( PurlCelebrationTabType.FOLLOWED_TAB );
    if ( Arrays.asList( getActiveTabList() ).contains( PurlCelebrationTabType.FOLLOWED_TAB ) )
    {
      PurlCelebrationSet purlCelebrationSet = createPurlCelebrationSet( tab, count, description );
      celebrationSetValueList.add( purlCelebrationSet );
    }
  }

  private void addRelevant( List<PurlCelebrationSet> celebrationSetValueList, long count, String description )
  {
    PurlCelebrationTabType tab = PurlCelebrationTabType.lookup( PurlCelebrationTabType.RECOMMENDED_TAB );
    if ( Arrays.asList( getActiveTabList() ).contains( PurlCelebrationTabType.RECOMMENDED_TAB ) )
    {
      celebrationSetValueList.add( createPurlCelebrationSet( tab, count, description ) );
    }
  }

  private void addCountry( List<PurlCelebrationSet> celebrationSetValueList, long count, String description )
  {
    PurlCelebrationTabType tab = PurlCelebrationTabType.lookup( PurlCelebrationTabType.COUNTRY_TAB );
    if ( Arrays.asList( getActiveTabList() ).contains( PurlCelebrationTabType.COUNTRY_TAB ) )
    {
      PurlCelebrationSet purlCelebrationSet = createPurlCelebrationSet( tab, count, description );
      purlCelebrationSet.setList( getCountryService().getAllActiveCountriesCodesAbbrevs() );
      celebrationSetValueList.add( purlCelebrationSet );
    }
  }

  private void addDepartment( List<PurlCelebrationSet> celebrationSetValueList, long count, String description )
  {
    PurlCelebrationTabType tab = PurlCelebrationTabType.lookup( PurlCelebrationTabType.DEPARTMENT_TAB );
    if ( Arrays.asList( getActiveTabList() ).contains( PurlCelebrationTabType.DEPARTMENT_TAB ) )
    {
      PurlCelebrationSet purlCelebrationSet = createPurlCelebrationSet( tab, count, description );
      purlCelebrationSet.setList( getParticipantService().getAllActiveDepartmentsForPublicRecognition());//DepartmentType.getList() );
      celebrationSetValueList.add( purlCelebrationSet );
    }
  }


  private void addDiviion( List<PurlCelebrationSet> celebrationSetValueList, long count, String description )
  {
    PurlCelebrationTabType tab = PurlCelebrationTabType.lookup( PurlCelebrationTabType.DIVISION_TAB );
    if ( Arrays.asList( getActiveTabList() ).contains( PurlCelebrationTabType.DIVISION_TAB ) )
    {
      PurlCelebrationSet purlCelebrationSet = createPurlCelebrationSet( tab, count, description );
      purlCelebrationSet.setList( DivisionType.getList() );
      if(null!= purlCelebrationSet && null!= purlCelebrationSet.getList()){
    	  celebrationSetValueList.add( purlCelebrationSet );
      }
    }
  }
  /**
   * To get Active Tab List
   * 
   * @param
   * @return String[]
   * @throws
   */
  private String[] getActiveTabList()
  {

    PropertySetItem item = systemVariableService.getPropertyByName( SystemVariableService.PURL_CELEBRATION_ACTIVE_TABS );

    if ( item != null )
    {
      return item.getStringVal().split( "," );
    }

    return new String[0];
  }

  @Override
  public List<PurlCelebrationSet> getAwardedPurlRecipients( String tabType, int pageSize, String sortedBy, String sortedOn, int startIndex, String lastName, String tile, String listValue )
      throws ServiceErrorException
  {
    Map<String, Object> searchParams = new HashMap<String, Object>();
    List<PurlCelebrationsView> purlRecipients = new ArrayList<PurlCelebrationsView>();
    String userLocale = UserManager.getUserLocale();
    List<PurlCelebrationSet> celebrationSetValueList = new ArrayList<PurlCelebrationSet>();
    Long userId = UserManager.getUserId();
    String purlPastPresentSelect = PAST;

    int rowNumStart = ( startIndex - 1 ) * pageSize;
    int rowNumEnd = ( rowNumStart % 50 == 0 ) ? ( rowNumStart - 1 + pageSize ) : ( rowNumStart + pageSize );

    searchParams.put( "pastOrUpcoming", purlPastPresentSelect );
    searchParams.put( "tabType", tabType );
    searchParams.put( "listValue", listValue );
    searchParams.put( "userLocale", userLocale );
    searchParams.put( "userId", userId );
    searchParams.put( "lastName", lastName );
    searchParams.put( "rowNumStart", rowNumStart + 1 );
    searchParams.put( "rowNumEnd", rowNumEnd + 1 );
    searchParams.put( "sortedBy", sortedBy );
    searchParams.put( "sortedOn", getPurlCelebrationsPageSortedOn( sortedOn ) );

    /* Relevant Tab */
    if ( tabType.equals( PurlCelebrationTabType.lookup( PurlCelebrationTabType.RECOMMENDED_TAB ).getCode() ) )
    {
      int globalTabCount = 0;
      purlRecipients = purlRecipientDAO.getPurlRecipientsForCelebrationPage( searchParams );
      if ( purlRecipients != null && purlRecipients.size() > 0 )
      {
        globalTabCount = purlRecipientDAO.getRecommendedPurlRecipientsCountForGivenContributor( userId, PurlRecipientType.PAST );
      }
      celebrationSetValueList = getRelevantCelebrationSetFromPurlRecipients( purlPastPresentSelect, pageSize, startIndex, sortedBy, sortedOn, purlRecipients, globalTabCount, tile, listValue );
    }

    /* Global Tab */
    if ( tabType.equals( PurlCelebrationTabType.lookup( PurlCelebrationTabType.GLOBAL_TAB ).getCode() ) )
    {
      int globalTabCount = 0;
      purlRecipients = purlRecipientDAO.getPurlRecipientsForCelebrationPage( searchParams );
      if ( purlRecipients != null && purlRecipients.size() > 0 )
      {
        globalTabCount = purlRecipientDAO.getGlobalAwardedPurlRecipientsCount();
      }
      celebrationSetValueList = getGlobalCelebrationSetFromPurlRecipients( purlPastPresentSelect, pageSize, startIndex, sortedBy, sortedOn, purlRecipients, globalTabCount, tile );
    }

    /* Team Tab */
    else if ( tabType.equals( PurlCelebrationTabType.lookup( PurlCelebrationTabType.TEAM_TAB ).getCode() ) )
    {
      Set<UserNode> userNodes = userService.getUserNodes( userId );
      List<Long> nodeIds = new ArrayList<Long>();
      int teamTabCount = 0;
      if ( userNodes != null )
      {
        for ( UserNode userNode : userNodes )
        {
          if ( userNode != null && userNode.getNode() != null )
          {
            nodeIds.add( userNode.getNode().getId() );
          }
        }
      }
      purlRecipients = purlRecipientDAO.getPurlRecipientsForCelebrationPage( searchParams );
      if ( purlRecipients != null && purlRecipients.size() > 0 )
      {
        teamTabCount = purlRecipientDAO.getTeamAwardedPurlRecipientsCount( nodeIds );
      }
      celebrationSetValueList = getTeamCelebrationSetFromPurlRecipients( purlPastPresentSelect, pageSize, startIndex, sortedBy, sortedOn, purlRecipients, teamTabCount,listValue );
    }

    /* Followed Tab */
    else if ( tabType.equals( PurlCelebrationTabType.lookup( PurlCelebrationTabType.FOLLOWED_TAB ).getCode() ) )
    {
      List<Long> followedUserIdsList = getFollowersByUserId( userId );
      int followedTabCount = 0;
      if ( followedUserIdsList.size() > 0 )
      {
        purlRecipients = purlRecipientDAO.getPurlRecipientsForCelebrationPage( searchParams );
      }
      if ( purlRecipients != null && purlRecipients.size() > 0 )
      {
        followedTabCount = purlRecipientDAO.getFollowedUserAwardedPurlRecipientsCount( followedUserIdsList );
      }
      celebrationSetValueList = getFollowedCelebrationSetFromPurlRecipients( purlPastPresentSelect, pageSize, startIndex, sortedBy, sortedOn, purlRecipients, followedTabCount,listValue );
    }

    else if ( tabType.equals( PurlCelebrationTabType.lookup( PurlCelebrationTabType.COUNTRY_TAB ).getCode() ) )
    {
      int countryTabCount = 0;
      purlRecipients = purlRecipientDAO.getPurlRecipientsForCelebrationPage( searchParams );
      if ( purlRecipients != null && purlRecipients.size() > 0 )
      {
        // TODO
        countryTabCount = purlRecipientDAO.getRecommendedPurlRecipientsCountForGivenContributor( userId, PurlRecipientType.PAST );
      }
      celebrationSetValueList = getCountryCelebrationSetFromPurlRecipients( purlPastPresentSelect, pageSize, startIndex, sortedBy, sortedOn, purlRecipients, countryTabCount, tile );
    }

    else if ( tabType.equals( PurlCelebrationTabType.lookup( PurlCelebrationTabType.DEPARTMENT_TAB ).getCode() ) )
    {
      int departmentTabCount = 0;
      purlRecipients = purlRecipientDAO.getPurlRecipientsForCelebrationPage( searchParams );
      if ( purlRecipients != null && purlRecipients.size() > 0 )
      {
        // TODO
        departmentTabCount = purlRecipientDAO.getRecommendedPurlRecipientsCountForGivenContributor( userId, PurlRecipientType.PAST );
      }
      celebrationSetValueList = getDepartmentCelebrationSetFromPurlRecipients( purlPastPresentSelect, pageSize, startIndex, sortedBy, sortedOn, purlRecipients, departmentTabCount, tile,listValue );
    }

    return celebrationSetValueList;
  }

 /*Customization for the WIP#51332 Start */
  
  public List getCustomSortOfUpcomingCelebration( Long charaterticsDivisionId , Long userId , int pageNumber, int pageSize )
    {
	  	  return this.purlRecipientDAO.getCustomSortOfUpcomingCelebration( charaterticsDivisionId, userId, pageNumber, pageSize  );
   }
  
    @SuppressWarnings("unchecked")
	private List<PurlRecipient> customSortOfUpcomingCelebration( List<PurlRecipient> celebrationSetValueList, Long userId )
  {
	  List<PurlRecipient> customSortedValueList = new ArrayList<PurlRecipient>();
	  AssociationRequestCollection userAssociations = new AssociationRequestCollection();
	  userAssociations.add( new UserAssociationRequest( UserAssociationRequest.CHARACTERISTIC ) );
	    Participant pax = getParticipantService().getParticipantByIdWithAssociations( userId, userAssociations );
	    Long charaterticsDivisionId = getSystemVariableService().getPropertyByName( SystemVariableService.COKE_USER_DIVISION_KEY_CHAR ).getLongVal();
	    
	    List<PurlRecipient> myDivisionList = new ArrayList <PurlRecipient>();
	    List<PurlRecipient>  otherDivisionList = new ArrayList <PurlRecipient>();
	    String  loggedInUserCharValue  = "" ;
	    if( pax.getUserCharacteristics() != null )
	    {
	    	for ( UserCharacteristic userCharObj : pax.getUserCharacteristics() )
	    	{
	    		if( userCharObj.getUserCharacteristicType().getId().equals( charaterticsDivisionId ))
	    		{
	    			loggedInUserCharValue =	userCharObj.getCharacteristicValue();
	    			break ;
	    		}	    		
	    	}
	    	String  recipientUserCharValue  = "" ;
	    	if ( loggedInUserCharValue != null )
	    	{
	    		for ( PurlRecipient purlRecipient : celebrationSetValueList )
	    		{
	    			if( purlRecipient.getUser().getUserCharacteristics() != null )
	    			{
	    			for( UserCharacteristic userCharObj1 : purlRecipient.getUser().getUserCharacteristics() )
	    			{
	    			if ( userCharObj1.getUserCharacteristicType().getId().equals(charaterticsDivisionId ) )
	    			{
	    				recipientUserCharValue = userCharObj1.getCharacteristicValue();
	    				if(recipientUserCharValue.equalsIgnoreCase(loggedInUserCharValue))
	    				{
	    					myDivisionList.add( purlRecipient );	
	    				}
	    				else
	    				{
	    					otherDivisionList.add( purlRecipient );
	    				}
	    			}
	    			}
	    			}
	    		}
	    	}
	        Collections.sort( myDivisionList, new PurlRecipientAwardDateComparator() );
	    	customSortedValueList.addAll( myDivisionList );
	    	Collections.sort( otherDivisionList, new PurlRecipientAwardDateComparator() );
	    	customSortedValueList.addAll( otherDivisionList );
	    }
	    return  customSortedValueList;
	   
   }
  /*Customization for the WIP#51332 End */

    private ParticipantService getParticipantService()
    {
      return (ParticipantService)BeanLocator.getBean( ParticipantService.BEAN_NAME );
    }
    
   private static SystemVariableService getSystemVariableService()
   {
     return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
   }
    
  public PurlRecipient getPurlRecipientByCelebrationManagerMessageId( Long id )
  {
    return purlRecipientDAO.getPurlRecipientByCelebrationManagerMessageId( id );
  }

  public List<PurlContributor> getPurlContributors( Long managerId, Long purlRecipientId )
  {
    PurlContributorQueryConstraint constraint = new PurlContributorQueryConstraint();
    constraint.setPurlRecipientId( purlRecipientId );
    constraint.setCreatedBy( managerId );

    return purlContributorDAO.getContributors( constraint );
  }

  private int contributorsAddedByManager( Long managerId, Long purlRecipientId )
  {
    List<PurlContributor> addedByManager = null;
    addedByManager = this.getPurlContributors( managerId, purlRecipientId );
    return addedByManager != null && addedByManager.size() > 0 ? addedByManager.size() : 0;
  }

  private PurlService getPurlService()
  {
    return (PurlService)BeanLocator.getBean( PurlService.BEAN_NAME );
  }

  public Contributor updateParticipant( Participant participant )
  {
    Contributor contributor = new Contributor();
    contributor.setId( participant.getId() );
    contributor.setFirstName( participant.getFirstName() );
    contributor.setLastName( participant.getLastName() );
    contributor.setAvatarUrl( participant.getAvatarSmallFullPath() );
    return contributor;
  }

  public List<ContributionsList> contributorCommentsProcess( List<PurlContributorComment> comments, String finalImagePrefixUrl )
  {

    List<ContributionsList> contributionList = new ArrayList<ContributionsList>();

    for ( PurlContributorComment commentItem : comments )
    {
      ContributionsList contributions = new ContributionsList();

      Contributor contributor = new Contributor();
      contributions.setId( commentItem.getId() );
      contributor.setFirstName( commentItem.getPurlContributor().getFirstName() );
      contributor.setId( commentItem.getPurlContributor().getId() );
      contributor.setLastName( commentItem.getPurlContributor().getLastName() );
      contributor.setAvatarUrl( commentItem.getPurlContributor().getDisplayAvatarUrl() );
      Media media = new Media();

      // Web Video
      if ( commentItem.getVideoType() != null && PurlContributorVideoType.WEB.equals( commentItem.getVideoType().getCode() ) && commentItem.getVideoStatus() != null
          && PurlContributorMediaStatus.ACTIVE.equals( commentItem.getVideoStatus().getCode() ) )
      {
        media.setType( "video" );
        media.setUrl( finalImagePrefixUrl + commentItem.getVideoUrl() );
        media.setThumbnailUrl( commentItem.getDisplayImageUrlThumb() );

      }
      // Direct Video
      else if ( commentItem.getVideoType() != null && PurlContributorVideoType.DIRECT.equals( commentItem.getVideoType().getCode() ) && commentItem.getVideoStatus() != null
          && PurlContributorMediaStatus.ACTIVE.equals( commentItem.getVideoStatus().getCode() ) )
      {
        // Some of the URL stuff is hacked together right now to get the
        // 5.6.3 release out
        String eCardVideoLink = null;
        if ( commentItem.getVideoUrl().contains( ActionConstants.REQUEST_ID ) )
        {
          MTCVideo mtcVideo = getMtcVideoService().getMTCVideoByRequestId( commentItem.getRequestId( commentItem.getVideoUrl() ) );

          if ( Objects.nonNull( mtcVideo ) )
          {
            eCardVideoLink = mtcVideo.getOriginalFormat().equals( "mp4" ) ? mtcVideo.getMp4Url() : mtcVideo.getWebmUrl();

          }
          else
          {
            eCardVideoLink = commentItem.getActualCardUrl( commentItem.getVideoUrl() );

          }

          // eCardVideoLink = FilenameUtils.removeExtension( eCardVideoLink );

        }
        else
        {
          eCardVideoLink = commentItem.getVideoUrl();
        }

        media.setType( "video" );
        media.setUrl( eCardVideoLink );
        media.setThumbnailUrl( commentItem.getVideoUrlThumb() );
      }
      // Photo
      else if ( StringUtils.isNotBlank( commentItem.getImageUrl() ) && commentItem.getImageStatus() != null && PurlContributorMediaStatus.ACTIVE.equals( commentItem.getImageStatus().getCode() ) )
      {
        media.setType( "image" );
        media.setUrl( commentItem.getImageUrl() );
        media.setThumbnailUrl( "" );
      }
      contributions.setContributor( contributor );
      contributions.setMessage( commentItem.getComments() );
      contributions.setMedia( media );
      contributionList.add( contributions );
    }
    return contributionList;
  }

  private String getPurlCelebrationsPageSortedOn( String sortedOn )
  {
    Map<String, String> sortedOnMapping = new HashMap<String, String>();
    sortedOnMapping.put( "1", "recipient_name" );
    sortedOnMapping.put( "2", "ANNIVERSARY" );
    sortedOnMapping.put( "3", "PROMOTION_NAME" );
    sortedOnMapping.put( "4", "EXPIRATION_DATE" );

    return sortedOnMapping.get( sortedOn ) != null ? sortedOnMapping.get( sortedOn ) : "EXPIRATION_DATE";
  }

  private Date getContributionCloseDate( Date awardDate )
  {
    Date closeDate = new Date();
    closeDate.setTime( awardDate.getTime() - 1 * org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY );
    return DateUtils.toEndDate( closeDate );

  }

  private WebDavUploadResult uploadToWebdav( String originalFileName, byte[] data, Long contributorId )
  {
    // user id will be null in case of external contributor
    String ownCardNameTemp = ( UserManager.getUserId() != null ? UserManager.getUserId() : contributorId ) + "_" + DateUtils.getCurrentDateAsLong() + "_" + originalFileName;
    String filePath = "purl" + '/' + ownCardNameTemp;

    try
    {
      boolean success = appDataDirFileUploadStrategy.uploadFileData( filePath, data );
      if ( !success )
      {
        return new WebDavUploadResult();
      }
    }
    catch( ServiceErrorException t )
    {
      return new WebDavUploadResult();
    }

    try
    {
      boolean ismovedtowebdav = moveFileToWebdav( filePath );
      if ( !ismovedtowebdav )
      {
        return new WebDavUploadResult();
      }
    }
    catch( Throwable t )
    {
      return new WebDavUploadResult();
    }

    // we made it this far so determine the full webDav URL to the media
    // we just uploaded, and return
    String ownCardWebDavPath = getPurlVideoUrl( ownCardNameTemp );

    return new WebDavUploadResult( true, ownCardNameTemp, ownCardWebDavPath );
  }

  public String getPurlVideoUrl( String ownCardName )
  {
    String siteUrlPrefix = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    if ( !Environment.isCtech() )
    {
      siteUrlPrefix = systemVariableService.getPropertyByName( SystemVariableService.SITE_URL_PREFIX + "." + Environment.ENV_QA ).getStringVal();
    }
    String ownCardUrl = null;
    if ( EnvironmentTypeEnum.isAws() )
    {
      siteUrlPrefix = systemVariableService.getContextName();
    }
    ownCardUrl = siteUrlPrefix + "-cm/cm3dam/purl" + '/' + ownCardName;

    return ownCardUrl;
  }

  protected String getDefaultVideoImageUrl()
  {
    return getSiteUrlPrefix() + "/assets/img/placeHolderVid.jpg";
  }

  protected String getSiteUrlPrefix()
  {
    return systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
  }

  protected static final class WebDavUploadResult
  {
    private final boolean success;
    private final String uploadedCardFileName;
    private final String webDavUrl;

    public WebDavUploadResult()
    {
      success = false;
      uploadedCardFileName = "";
      webDavUrl = "";
    }

    public WebDavUploadResult( boolean success, String uploadedCardFileName, String webDavUrl )
    {
      this.success = success;
      this.uploadedCardFileName = uploadedCardFileName;
      this.webDavUrl = webDavUrl;
    }

    public boolean isSuccess()
    {
      return success;
    }

    public String getUploadedCardFileName()
    {
      return uploadedCardFileName;
    }

    public String getWebDavUrl()
    {
      return webDavUrl;
    }
  }

  private ParticipantDAO getParticipantDAO()
  {
    return (ParticipantDAO)BeanLocator.getBean( ParticipantDAO.BEAN_NAME );
  }

  @Override
  public List<PurlRecipientValue> getUpComingCelebrationList()
  {
    return purlRecipientDAO.getUpComingCelebrationList();
  }

  private static CountryService getCountryService()
  {
    return (CountryService)BeanLocator.getBean( CountryService.BEAN_NAME );
  }

  public CMAssetService getCmassetService()
  {
    return cmassetService;
  }

  public void setCmassetService( CMAssetService cmassetService )
  {
    this.cmassetService = cmassetService;
  }

  @Override
  public List<PurlRecipient> getPurlRecipientByUserId( Long userId )
  {
    return purlRecipientDAO.getPurlRecipientByUserId( userId );
  }

  public RoleService getRoleService()
  {
    return roleService;
  }

  public void setRoleService( RoleService roleService )
  {
    this.roleService = roleService;
  }

  public MTCVideoService getMtcVideoService()
  {
    return mtcVideoService;
  }

  public void setMtcVideoService( MTCVideoService mtcVideoService )
  {
    this.mtcVideoService = mtcVideoService;
  }

  private void throwServiceException() throws ServiceErrorException
  {
    throw new ServiceErrorException( "personalInfo.IMAGE_UPLOAD_FAILED" );
  }

  private void throwServiceExceptionForContribution() throws ServiceErrorException
  {
    
	  throw new ServiceErrorException( "purl.contribution.IMAGE_UPLOAD_FAILED" );
  }

  private UserCharacteristicService getUserCharacteristicService()
  {
    return (UserCharacteristicService)getService( UserCharacteristicService.BEAN_NAME );
  }

  /**
   * Bean location through BeanLocator look-up returns the service interface.
   * 
   * @param beanName
   * @return SAO
   */
  protected static SAO getService( String beanName )
  {
    return (SAO)BeanLocator.getBean( beanName );
  }

  private PurlFileUploadValue setUploadValue( PurlFileUploadValue data, String imgUrl ) throws ServiceErrorException
  {
    if ( Objects.nonNull( imgUrl ) )
    {
      data.setThumb( imgUrl );
    }
    else
    {
      throwServiceException();
    }
    return data;

  }

  @Override
  public List<PaxAvatarData> getNotMigratedPurlContributorAvatarData()
  {
    return purlContributorDAO.getNotMigratedPurlContributorAvatarData();
  }

  @Override
  public void removeAvatarForContributor( Long purlContributorId )
  {
    // Remove Avatar
    PurlContributor purlContributor = getPurlContributorById( purlContributorId );
    purlContributor.setAvatarUrl( null );
    purlContributor.setAvatarState( null );
    savePurlContributor( purlContributor );

  }

  public List<PurlContributor> getAllPendingPurlContributionsProActive( Long promotionId, Long numberOfDays )
  {
    List<PurlContributor> list = purlContributorDAO.getAllPendingPurlContributionsProActive( promotionId, numberOfDays );
    List<PurlContributor> filteredListForContributions = filterContributionPurls( list );
    return filterPendingContributionPurls( filteredListForContributions );
  }

  @Override
  public void updateMigratedPurlContributorAvatar( Long userId, String avatarUrl )
  {
    purlContributorDAO.updateMigratedPurlContributorAvatar( userId, avatarUrl );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PurlContributorImageData> getNotMigratedPurlContributorImgPaxData()
  {
    return purlContributorCommentDAO.getNotMigratedPurlContributorImgPaxData();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateMigratedPurlContributorImage( Long purlContributorCommentId, String imageUrl, String imageUrlThumb )
  {
    purlContributorCommentDAO.updateMigratedPurlContributorImage( purlContributorCommentId, imageUrl, imageUrlThumb );

  }

  public int getPurlAwardDate( Long userId, Long purlRecipientId )
  {
    return purlRecipientDAO.getPurlAwardDate( userId, purlRecipientId );
  }

  public int getCelebrationAwardDate( Long userId, Long claimId )

  {
    return purlRecipientDAO.getCelebrationAwardDate( userId, claimId );

  }

  @Override
  public List<Long> getAllInternalPurlContributorUser()
  {
    return purlContributorDAO.getAllInternalPurlContributorUser();
  }

  @Override
  public List<PurlContributorAvatarData> getNotSyncPurlContrbUserAvatarData( List<Long> userIds )
  {
    return purlContributorDAO.getNotSyncPurlContrbUserAvatarData( userIds );
  }

  @Override
  public void updateMigratedPurlContrPaxAvatar( Long userId, String avatarUrl )
  {
    purlContributorDAO.updateMigratedPurlContrPaxAvatar( userId, avatarUrl );
  }

  @Override
  public List<Long> getAllPurlUsersAvatarMigrated( List<Long> userIds )
  {
    return purlContributorDAO.getAllPurlUsersAvatarMigrated( userIds );
  }

  @Override
  public List<PurlNotMigratedPaxData> getNotMigratedPurlUserAvatar( List<Long> userIds )
  {
    return purlContributorDAO.getNotMigratedPurlUserAvatar( userIds );
  }

  public List<Long> getPurlRecipientsForAutoInvite( Long promotionId, Long numberOfDays )
  {
    return purlRecipientDAO.getPurlRecipientsForAutoInvite( promotionId, numberOfDays );
  }
 
  @Override
  public List<Long> getAllPurlContrbUsersToCopyTheUrl()
  {
    return purlContributorDAO.getAllPurlContrbUsersToCopyTheUrl();
  }
  // //client customization start - wip 52159
  public List<Participant> getUsersByCharacteristicIdAndValue( Long charId, String charValue )
  {
	  return participantService.getUsersByCharacteristicIdAndValue(charId, charValue);
  }
  //client customization end - wip 52159
//client customization end - wip 52159
  /* Customization for WIP 32479 starts here */

  public void setMessageService( MessageService messageService )
  {
    this.messageService = messageService;
  }
  @Override
  public void unsubscribeExternalUser( String emailAddress )
  {
    purlContributorDAO.unsubscribeExternalUser( emailAddress );
  }
  /* Customization for WIP 32479 ends here */
  
  @Override
  public List<Participant> getNodeMemberForPurlMgrRecipient( Long purlRecipientId )
  {
    return participantService.getNodeMemberForPurlMgrRecipient( purlRecipientId );
  }
  
  class PurlRecipientAwardDateComparator implements Comparator
  {
    public int compare( Object o1, Object o2 )
    {
      if ( ! ( o1 instanceof PurlRecipient ) || ! ( o2 instanceof PurlRecipient ) )
      {
        throw new ClassCastException( "Object is not a PurlRecipient object!" );
      }
      PurlRecipient p1 = (PurlRecipient)o1;
      PurlRecipient p2 = (PurlRecipient)o2;
      Date date1 = p1.getAwardDate();
      Date date2 = p2.getAwardDate();
      return date1.compareTo( date2 );
    }
  }
}
