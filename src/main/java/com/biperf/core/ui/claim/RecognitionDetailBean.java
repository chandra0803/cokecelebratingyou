
package com.biperf.core.ui.claim;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.ApprovableItem;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimItem;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.ProductClaimParticipant;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.domain.enums.NominationEvaluationType;
import com.biperf.core.domain.enums.PositionType;
import com.biperf.core.domain.enums.PromoRecognitionBehaviorType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.StateType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.mtc.MTCVideo;
import com.biperf.core.domain.multimedia.Card;
import com.biperf.core.domain.multimedia.ECard;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantEmployer;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.PublicRecognitionComment;
import com.biperf.core.domain.publicrecognition.PublicRecognitionShareLinkBean;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.service.claim.NominationClaimService;
import com.biperf.core.service.mtc.MTCVideoService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.recognition.SupportedEcardVideoTypes;
import com.biperf.core.utils.AddressUtil;
import com.biperf.core.utils.ArrayUtil;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.BudgetValueBean;
import com.biperf.core.value.PickListValueBean;
import com.biperf.core.value.PublicRecognitionFormattedValueBean;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.objectpartners.cms.util.ContentReaderManager;

public class RecognitionDetailBean implements Serializable
{
  private List<String> messages = new ArrayList<String>();
  private RecognitionDetail recognition;
  private String userLang;

  public RecognitionDetailBean( String userLang, String contextPath, AbstractRecognitionClaim arc, boolean includeTeamClaims, boolean isHistoryPage )
  {
    this.userLang = userLang;
    recognition = new RecognitionDetail( contextPath, arc, false, userLang, includeTeamClaims, isHistoryPage );
  }

  public RecognitionDetailBean( String userLang, String contextPath, PublicRecognitionFormattedValueBean publicRecognition, boolean includeTeamClaims, boolean isHistoryPage )
  {
    this.userLang = userLang;
    recognition = new RecognitionDetail( contextPath, publicRecognition, userLang, includeTeamClaims, isHistoryPage );
  }

  public RecognitionDetailBean( String userLang, String contextPath, Journal journal, Participant pax )
  {
    this.userLang = userLang;
    recognition = new RecognitionDetail( contextPath, userLang, journal, pax );
  }

  public RecognitionDetailBean( String userLang, String contextPath, RecognitionHistoryValueObject valueObject, Participant pax )
  {
    this.userLang = userLang;
    recognition = new RecognitionDetail( contextPath, userLang, valueObject, pax );
  }

  public List<String> getMessages()
  {
    return messages;
  }

  public String getUserLang()
  {
    return userLang;
  }

  public RecognitionDetail getRecognition()
  {
    return recognition;
  }

  @JsonInclude( value = Include.NON_NULL )
  public static class RecognitionDetail implements Serializable
  {
    private Long id;
    private boolean isDetail;
    private boolean isCumulative;

    private List<RecognitionDetailParticipant> recipients = new ArrayList<RecognitionDetailParticipant>();
    private List<ClaimDetailParticipant> recognizer = new ArrayList<ClaimDetailParticipant>();

    private String comment;
    private String commentLanguage;
    private boolean allowTranslate;
    private String purlUrl;
    private Long promotionId;
    private String promotionName;
    private String promotionType;
    private String date;
    private String behavior;
    private String badgeUrl;
    private List<String> copies = new ArrayList<String>();
    private String teamName;
    private Ecard ecard;
    private List<CustomElements> extraFields = new ArrayList<CustomElements>();

    // public recognition fields
    private boolean isPublicClaim;
    private int numLikers;
    private boolean isMine;
    private boolean isHidden;
    private boolean isLiked;
    private String time;
    private List<ClaimDetailShareLink> shareLinks;
    private List<ClaimDetailComment> comments;
    private boolean isSweepAward;

    // for budget json view
    private boolean allowAddPoints;
    private Long awardAmountMin;
    private Long awardAmountMax;
    private Long awardAmountFixed;
    private String awardAmount;
    private double countryRatio;
    private List<BudgetValueBean> budgets = new ArrayList<BudgetValueBean>();
    private boolean isBadgePromotion;

    /**
     * Returns a link to view the recipients Certificate for the given recognition claim and recipient.  If the claim does not have a certificate
     * associated with it, returns null. 
     * 
     * @param recognitionClaim
     * @param contextPath
     * @return String
     */
    private static String getViewCertificateUrlFromRecognitionClaim( RecognitionClaim recognitionClaim, Long recipientId, String contextPath )
    {
      String viewCertificateUrl = null;

      if ( recognitionClaim.getCertificateId() != null && recognitionClaim.getCertificateId() > 0 )
      {
        ApprovableItem approvableItem = (ApprovableItem)recognitionClaim.getApprovableItems().iterator().next();
        if ( approvableItem.getApprovalStatusType().getCode().equals( ApprovalStatusType.APPROVED ) )
        {
          Map<String, Object> paramMap = new HashMap<String, Object>();
          paramMap.put( "claimItemId", recipientId );
          paramMap.put( "claimId", recognitionClaim.getId() );
          paramMap.put( "promotionId", recognitionClaim.getPromotion().getId() );
          viewCertificateUrl = ClientStateUtils.generateEncodedLink( contextPath, "/claim/displayCertificate.do?method=showCertificateRecognitionDetail", paramMap );
        }
      }

      return viewCertificateUrl;
    }

    /**
     * Returns a link to view the recipients Certificate for the given nomination claim and recipient.  If the claim does not have a certificate
     * associated with it, returns null. 
     * 
     * @param nominationClaim
     * @param recipientId
     * @param contextPath
     * @return String
     */
    private static String getViewCertificateUrlFromNominationClaim( NominationClaim nominationClaim, Long recipientId, String contextPath )
    {
      String viewCertificateUrl = null;

      if ( StringUtils.isNotBlank( nominationClaim.getPromotion().getCertificate() ) )
      {
        ApprovableItem approvableItem = (ApprovableItem)nominationClaim.getApprovableItems().iterator().next();
        if ( approvableItem.getApprovalStatusType().getCode().equals( ApprovalStatusType.WINNER ) )
        {
          Map<String, Object> paramMap = new HashMap<String, Object>();
          paramMap.put( "userId", recipientId );
          paramMap.put( "claimId", nominationClaim.getId() );
          paramMap.put( "promotionId", nominationClaim.getPromotion().getId() );
          viewCertificateUrl = ClientStateUtils.generateEncodedLink( contextPath, "/claim/displayCertificate.do?method=showCertificateNominationDetail", paramMap );
        }
      }

      return viewCertificateUrl;
    }

    /**
     * Returns a sorted recipient list.  For now, it just checks whether the logged in user is a recipient, and puts them 
     * at the top of the list.
     * 
     * @param recipientList
     * @return List<RecognitionDetailParticipant>
     */
    private static List<RecognitionDetailParticipant> getSortedRecipientList( List<RecognitionDetailParticipant> recipientList )
    {
      List<RecognitionDetailParticipant> sortedRecipientList = new ArrayList<RecognitionDetailParticipant>();
      int loggedInUserIndex = -1;

      for ( int i = 0; i < recipientList.size(); i++ )
      {
        RecognitionDetailParticipant recipient = (RecognitionDetailParticipant)recipientList.get( i );
        if ( UserManager.getUserId().equals( recipient.getId() ) )
        {
          loggedInUserIndex = i;
          break;
        }
      }

      if ( loggedInUserIndex > 0 )
      {
        RecognitionDetailParticipant loggedInRecipient = recipientList.remove( loggedInUserIndex );
        sortedRecipientList.add( loggedInRecipient );
      }

      sortedRecipientList.addAll( recipientList );
      return sortedRecipientList;
    }

    public RecognitionDetail( String contextPath, PublicRecognitionFormattedValueBean publicRecognition, String userLang, boolean includeTeamClaims, boolean isHistoryPage )
    {
      this( contextPath, publicRecognition.getAbstractRecognitionClaim(), true, userLang, includeTeamClaims, isHistoryPage );

      if ( includeTeamClaims )
      {
        this.isPublicClaim = publicRecognition.getIsPublicClaim();
      }
      this.numLikers = publicRecognition.getNumLikers();
      this.isLiked = Boolean.valueOf( publicRecognition.getIsLiked() );
      this.isHidden = publicRecognition.isHidePublicRecognition();
      this.time = publicRecognition.getRelativeClaimApprovedDate();
      this.allowAddPoints = publicRecognition.isAllowAddPoints();
      this.awardAmountFixed = publicRecognition.getFixedAwardAmount();
      this.awardAmountMin = publicRecognition.getAwardAmountMin();
      this.awardAmountMax = publicRecognition.getAwardAmountMax();
      this.budgets = publicRecognition.getBudgets();
      this.countryRatio = publicRecognition.getCountryRatio();

      if ( Boolean.valueOf( publicRecognition.getIsMine() ) )
      {
        this.isMine = true;
        this.shareLinks = new ArrayList<ClaimDetailShareLink>();

        for ( PublicRecognitionShareLinkBean socialLink : publicRecognition.getSocialLinks() )
        {
          this.shareLinks.add( new ClaimDetailShareLink( socialLink.getUrl(), socialLink.getName(), socialLink.getNameId() ) );
        }
      }

      this.comments = new ArrayList<ClaimDetailComment>();
      for ( PublicRecognitionComment comment : publicRecognition.getUserComments() )
      {
        String commentLang = comment.getCommentsLanguageType() == null ? "" : comment.getCommentsLanguageType().getLanguageCode();
        boolean translate = !userLang.equals( commentLang );
        this.comments.add( new ClaimDetailComment( comment.getId(), comment.getComments(), translate, new RecognitionDetailParticipant( comment.getUser(), null, null ) ) );
      }

    }

    public RecognitionDetail( String contextPath, String userLang, Journal journal, Participant pax )
    {
      id = journal.getId();
      isPublicClaim = false;
      isDetail = true;
      isMine = true;
      this.time = new Timestamp( journal.getTransactionDate().getTime() ).toString();
      if ( journal.getTransactionAmount() != null && journal.getTransactionAmount().longValue() != 0 )
      {
        this.awardAmount = journal.getTransactionAmount().toString() + " " + PromotionAwardsType.lookup( PromotionAwardsType.POINTS ).getName();
      }
      else
      {
        this.awardAmount = null;
      }
      this.date = DateUtils.toDisplayString( journal.getAuditCreateInfo().getDateCreated() );
      this.promotionId = journal.getPromotion().getId();
      this.promotionName = journal.getPromotion().getPromotionName();
      this.promotionType = journal.getPromotion().getPromotionType().getCode();

      recipients.add( new RecognitionDetailParticipant( pax, pax.getPrimaryUserNode().getNode(), null ) );
    }

    // This method is to set participant badge details
    public RecognitionDetail( String contextPath, String userLang, RecognitionHistoryValueObject valueObject, Participant pax )
    {
      isPublicClaim = false;
      isDetail = true;
      isMine = true;
      this.time = new Timestamp( valueObject.getSubmissionDate().getTime() ).toString();
      if ( valueObject.getAwardQuantity() != null && valueObject.getAwardQuantity().longValue() != 0 )
      {
        this.awardAmount = valueObject.getAwardQuantity().toString() + " " + PromotionAwardsType.lookup( PromotionAwardsType.POINTS ).getName();
      }
      else
      {
        this.awardAmount = null;
      }
      this.date = DateUtils.toDisplayString( valueObject.getSubmissionDate() );
      this.promotionName = valueObject.getPromotion().getPromotionName();
      this.isBadgePromotion = valueObject.getIsBadgePromotion();

      recipients.add( new RecognitionDetailParticipant( pax, pax.getPrimaryUserNode().getNode(), null ) );
    }

    // This method is a good candidate for rafactoring
    public RecognitionDetail( String contextPath, AbstractRecognitionClaim arc, boolean isPublicRecognition, String userLang, boolean includeTeamClaims, boolean isHistoryPage )
    {
      id = arc.getId();
      isDetail = true;
      isPublicClaim = false;

      if ( arc.getPromotion().isNominationPromotion()
          && ( (NominationPromotion)arc.getPromotion() ).getEvaluationType().equals( NominationEvaluationType.lookup( NominationEvaluationType.CUMULATIVE ) ) )
      {
        isCumulative = true;
        if ( arc.getClaimGroup() != null )
        {
          for ( Object claimObject : arc.getClaimGroup().getClaims() )
          {
            NominationClaim nominationClaim = (NominationClaim)claimObject;
            ClaimDetailParticipant nominationDetailParticipant = new ClaimDetailParticipant( nominationClaim.getSubmitter(), arc.getNode(), null, nominationClaim, contextPath );
            recognizer.add( nominationDetailParticipant );
          }
        }
        else
        {
          buildClaimDetailParticipantList( arc, contextPath );
        }
      }
      else
      {
        isCumulative = false;
        buildClaimDetailParticipantList( arc, contextPath );
      }

      Long loggedInUserId = UserManager.getUserId();
      boolean isTeam = false;
      int totalAmount = 0;
      String awardTypeCode = getNominationAwardTypeCode( arc );

      if ( arc.isNominationClaim() && ( (NominationClaim)arc ).isTeam() )
      {
        NominationClaim claim = (NominationClaim)arc;
        if ( claim.getClaimRecipients() != null && claim.getClaimRecipients().size() > 0 )
        {
          isTeam = true;
          teamName = claim.getTeamName();
          ClaimRecipient claimRecipient = claim.getClaimRecipients().iterator().next();

          for ( ClaimRecipient teamMember : claim.getClaimRecipients() )
          {
            if ( !isPublicRecognition || isDisplayInPublicRecognition( teamMember ) )
            {
              String teamMemberCertificateUrl = getViewCertificateUrlFromNominationClaim( claim, teamMember.getRecipient().getId(), contextPath );
              recipients.add( new RecognitionDetailParticipant( teamMember.getRecipient(), teamMember.getNode(), teamMemberCertificateUrl ) );
              if ( teamMember.getRecipient().getId().equals( loggedInUserId ) )
              {
                isMine = true;
              }
            }

            if ( arc.getSubmitter().getId().equals( loggedInUserId ) && claimRecipient.getAwardQuantity() != null )
            {
              totalAmount += claimRecipient.getAwardQuantity().intValue();
            }
          }

          if ( isMine || arc.getSubmitter().getId().equals( loggedInUserId ) )
          {
            if ( claimRecipient.getAwardQuantity() != null )
            {
              if ( isMine )
              {
                if ( claimRecipient.getAwardQuantity() != null && claimRecipient.getAwardQuantity().longValue() != 0 )
                {
                  if ( claim.getPromotion().getAwardType() != null )
                  {
                    awardAmount = claimRecipient.getAwardQuantity() + " " + PromotionAwardsType.lookup( claim.getPromotion().getAwardType().getCode() ).getName();
                  }
                  else
                  {
                    awardAmount = Long.toString( claimRecipient.getAwardQuantity() );
                  }
                }
                else
                {
                  awardAmount = null;
                }
              }
            }
            else if ( claimRecipient.getPromoMerchProgramLevel() != null )
            {
              awardAmount = claimRecipient.getPromoMerchProgramLevel().getDisplayLevelName();
            }
          }

          if ( arc.getSubmitter().getId().equals( loggedInUserId ) && totalAmount > 0 )
          {
            awardAmount = totalAmount + " " + awardTypeCode;
          }
        }
      }
      else if ( !isCumulative && arc.getTeamClaims() != null && !arc.getTeamClaims().isEmpty() )
      {
        isTeam = true;

        if ( isHistoryPage )
        {
          for ( ClaimRecipient teamMember : arc.getClaimRecipients() )
          {
            if ( teamMember.getClaim().getId().equals( arc.getId() ) )
            {
              if ( !isPublicRecognition || isDisplayInPublicRecognition( teamMember ) )
              {
                String teamMemberCertificateUrl = arc.isRecognitionClaim()
                    ? //
                    getViewCertificateUrlFromRecognitionClaim( (RecognitionClaim)arc, teamMember.getId(), contextPath )
                    : getViewCertificateUrlFromNominationClaim( (NominationClaim)arc, teamMember.getId(), contextPath );

                recipients.add( new RecognitionDetailParticipant( teamMember.getRecipient(), teamMember.getNode(), teamMemberCertificateUrl ) );

                buildClaimRecipient( teamMember, arc, totalAmount );
                break;
              }
            }
          }
        }
        else
        {
          for ( AbstractRecognitionClaim teamClaim : arc.getTeamClaims() )
          {
            ClaimRecipient teamMember = teamClaim.getClaimRecipients().iterator().next();
            if ( !isPublicRecognition || isDisplayInPublicRecognition( teamMember ) )
            {
              String teamMemberCertificateUrl = arc.isRecognitionClaim()
                  ? //
                  getViewCertificateUrlFromRecognitionClaim( (RecognitionClaim)teamClaim, teamMember.getId(), contextPath )
                  : getViewCertificateUrlFromNominationClaim( (NominationClaim)teamClaim, teamMember.getId(), contextPath );

              recipients.add( new RecognitionDetailParticipant( teamMember.getRecipient(), teamMember.getNode(), teamMemberCertificateUrl ) );

              buildClaimRecipient( teamMember, arc, totalAmount );

            }
          }

          if ( arc.getTeamClaims().size() > 1 && arc.getPromotion().getAwardType().getCode().equals( PromotionAwardsType.MERCHANDISE ) )
          {
            awardAmount = null;
          }

          if ( arc.getTeamClaims().size() > 1 && !arc.getPromotion().getAwardType().getCode().equals( PromotionAwardsType.MERCHANDISE ) && arc.isReversal() )
          {
            awardAmount = null;
          }

        }

        if ( arc.getSubmitter().getId().equals( loggedInUserId ) && totalAmount > 0 )
        {
          awardAmount = totalAmount + " " + awardTypeCode;
        }
      }

      if ( arc.isRecognitionClaim() )
      {
        purlUrl = ( (RecognitionClaim)arc ).getPurlUrl();
      }

      if ( !isTeam )
      {
        for ( ClaimRecipient claimRecipient : arc.getClaimRecipients() )
        {
          if ( !includeTeamClaims && isDisplayInPublicRecognition( claimRecipient ) )
          {
            isPublicClaim = true;
          }
          String viewCertificateUrl = null;
          if ( arc.isRecognitionClaim() )
          {
            viewCertificateUrl = getViewCertificateUrlFromRecognitionClaim( (RecognitionClaim)arc, claimRecipient.getId(), contextPath );
          }
          else if ( arc.isNominationClaim() )
          {
            viewCertificateUrl = getViewCertificateUrlFromNominationClaim( (NominationClaim)arc, claimRecipient.getId(), contextPath );
          }
          recipients.add( new RecognitionDetailParticipant( claimRecipient.getRecipient(), claimRecipient.getNode(), viewCertificateUrl ) );

          if ( arc.getSubmitter().getId().equals( loggedInUserId ) || claimRecipient.getRecipient().getId().equals( loggedInUserId ) )
          {
            // Need not to show awardamount if reversed
            if ( claimRecipient.getAwardQuantity() != null && claimRecipient.getAwardQuantity().longValue() != 0 && !arc.isReversal() )
            {
              awardAmount = claimRecipient.getAwardQuantity() + " " + awardTypeCode;
            }
            else if ( claimRecipient.getPromoMerchProgramLevel() != null )
            {
              awardAmount = claimRecipient.getPromoMerchProgramLevel().getDisplayLevelName();
            }
            else if ( claimRecipient.getClaim().getClaimGroup() != null )
            {
              if ( claimRecipient.getClaim().getClaimGroup().getAwardQuantity() != null )
              {
                awardAmount = claimRecipient.getAwardQuantity() + " " + awardTypeCode;
              }
            }
          }
        }
      }

      recipients = getSortedRecipientList( recipients );

      if ( arc.getSubmitterCommentsLanguageType() != null )
      {
        commentLanguage = arc.getSubmitterCommentsLanguageType().getLanguageCode();
        if ( getSystemVariableService().getPropertyByName( SystemVariableService.MACHINE_LANGUAGE_ALLOW_TRANSLATION ).getBooleanVal() )
        {
          allowTranslate = !userLang.equals( commentLanguage );
        }
        else
        {
          allowTranslate = false;
        }
      }

      promotionId = arc.getId();
      promotionName = arc.getPromotion().getName();
      promotionType = extractPromotionType( arc );
      boolean isAdmin = getUserService().getUserById( UserManager.getUserId() ).isAdmin();

      if ( arc.getSubmitter().getId().equals( loggedInUserId ) || isAdmin )
      {
        if ( arc.isCopySender() )
        {
          copies.add( ContentReaderManager.getText( "recognition.review.send", "COPY_TO_YOU" ) );
        }

        if ( arc.isRecognitionClaim() )
        {
          RecognitionClaim recognitionClaim = (RecognitionClaim)arc;
          if ( recognitionClaim.isCopyManager() )
          {
            copies.add( ContentReaderManager.getText( "recognition.review.send", "COPY_TO_MANAGER" ) );
          }
          if ( recognitionClaim.getSendCopyToOthers() != null )
          {
            if ( recognitionClaim.getSendCopyToOthers().equals( "null" ) )
            {
              recognitionClaim.setSendCopyToOthers( "" );
            }
          }
          if ( StringUtils.isNotEmpty( recognitionClaim.getSendCopyToOthers() ) )
          {
            copies.add( ContentReaderManager.getText( "recognition.review.send", "COPY_TO_OTHERS" ) + " " + recognitionClaim.getSendCopyToOthers() );
          }
        }
      }
      else if ( recipients != null )
      {
        for ( int i = 0; i < recipients.size(); i++ )
        {
          if ( recipients.get( i ).getId().equals( loggedInUserId ) )
          {
            if ( arc.isCopySender() )
            {
              copies.add( ContentReaderManager.getText( "recognition.review.send", "RECIPIENT_COPY_TO_YOU" ) );
            }

            if ( arc.isRecognitionClaim() )
            {
              RecognitionClaim recognitionClaim = (RecognitionClaim)arc;
              if ( recognitionClaim.isCopyManager() )
              {
                copies.add( ContentReaderManager.getText( "recognition.review.send", "COPY_TO_MANAGER" ) );
              }
              if ( recognitionClaim.getSendCopyToOthers() != null )
              {
                if ( recognitionClaim.getSendCopyToOthers().equals( "null" ) )
                {
                  recognitionClaim.setSendCopyToOthers( "" );
                }
              }
              if ( StringUtils.isNotEmpty( recognitionClaim.getSendCopyToOthers() ) )
              {
                copies.add( ContentReaderManager.getText( "recognition.review.send", "COPY_TO_OTHERS" ) + " " + recognitionClaim.getSendCopyToOthers() );
              }
            }
          }
        }
      }
    }

    private String getNominationAwardTypeCode( AbstractRecognitionClaim arc )
    {
      String awardTypeCode;
      if ( arc.isNominationClaim() )
      {
        awardTypeCode = PromotionAwardsType.lookup( getNominationClaimService().getAwardTypeForCurrentLevel( arc.getId() ) ).getName();
      }
      else
      {
        awardTypeCode = PromotionAwardsType.lookup( arc.getPromotion().getAwardType().getCode() ).getName();
      }
      return awardTypeCode;
    }

    private void buildClaimRecipient( ClaimRecipient teamMember, AbstractRecognitionClaim arc, int totalAmount )
    {
      if ( teamMember.getRecipient().getId().equals( UserManager.getUserId() ) || arc.getSubmitter().getId().equals( UserManager.getUserId() ) )
      {
        if ( teamMember.getAwardQuantity() != null )
        {
          if ( arc.getSubmitter().getId().equals( UserManager.getUserId() ) )
          {
            totalAmount += teamMember.getAwardQuantity().intValue();
          }
          else
          {
            awardAmount = teamMember.getAwardQuantity() != null && teamMember.getAwardQuantity().longValue() != 0
                ? teamMember.getAwardQuantity() + " " + PromotionAwardsType.lookup( arc.getPromotion().getAwardType().getCode() ).getName()
                : null;
          }
        }
        else if ( teamMember.getPromoMerchProgramLevel() != null )
        {
          awardAmount = teamMember.getPromoMerchProgramLevel().getDisplayLevelName();
        }
        else if ( teamMember.getClaim().getClaimGroup() != null && teamMember.getClaim().getClaimGroup().getAwardQuantity() != null )
        {
          if ( arc.getSubmitter().getId().equals( UserManager.getUserId() ) )
          {
            totalAmount += teamMember.getClaim().getClaimGroup().getAwardQuantity().intValue();
          }
          else
          {
            awardAmount = teamMember.getClaim().getClaimGroup().getAwardQuantity() != null && teamMember.getClaim().getClaimGroup().getAwardQuantity().longValue() != 0
                ? teamMember.getClaim().getClaimGroup().getAwardQuantity() + " " + PromotionAwardsType.lookup( arc.getPromotion().getAwardType().getCode() ).getName()
                : null;
          }
        }
      }

      if ( teamMember.getRecipient().getId().equals( UserManager.getUserId() ) )
      {
        isMine = true;
      }

    }

    private void buildClaimDetailParticipantList( AbstractRecognitionClaim arc, String contextPath )
    {
      ClaimDetailParticipant claimDetailParticipant = new ClaimDetailParticipant( arc.getSubmitter(), arc.getNode(), null, arc, contextPath );

      date = DateUtils.toDisplayString( arc.getSubmissionDate() );
      claimDetailParticipant.setDate( date );

      if ( arc.getSubmitterComments() != null )
      {
        comment = arc.getSubmitterComments().trim();
        claimDetailParticipant.setComment( comment );
      }
      if ( arc.getBehavior() != null && !PromoRecognitionBehaviorType.getNoneItem().equals( arc.getBehavior() ) )
      {
        behavior = arc.getBehavior().getName();
        claimDetailParticipant.setBehavior( behavior );
      }

      if ( arc.getOwnCardName() != null && arc.getOwnCardName().trim().length() > 0 )
      {
        ecard = new Ecard( arc.getOwnCardName() );
      }
      else if ( arc.getCard() != null )
      {
        ecard = new Ecard( (ECard)arc.getCard(), contextPath );
      }
      else if ( arc.getCardVideoUrl() != null && arc.getCardVideoUrl().trim().length() > 0 )
      {
        // MTC - To be changed
        MTCVideo mtcVideo = getMTCVideoService().getMTCVideoByRequestId( arc.getRequestId( arc.getCardVideoUrl() ) );
        ecard = new Ecard( mtcVideo.getOriginalFormat().equals( "mp4" ) ? mtcVideo.getMp4Url() : mtcVideo.getWebmUrl(), mtcVideo.getThumbNailImageUrl() );
      }
      claimDetailParticipant.setEcard( ecard );

      String cmAssetCode = "";
      if ( arc.getPromotion().getClaimForm() != null )
      {
        cmAssetCode = arc.getPromotion().getClaimForm().getCmAssetCode();
      }
      for ( ClaimElement claimElement : arc.getClaimElements() )
      {
        if ( !claimElement.getClaimFormStepElement().getClaimFormElementType().isCopyBlock() && !claimElement.getClaimFormStepElement().getClaimFormElementType().isLink() )
        {
          extraFields.add( new CustomElements( claimElement, cmAssetCode ) );
        }
      }
      claimDetailParticipant.setExtraFields( extraFields );
      recognizer.add( claimDetailParticipant );
    }

    private boolean isDisplayInPublicRecognition( ClaimRecipient claimRecipient )
    {
      return claimRecipient.getRecipient().isAllowPublicRecognition() && //
          ( ApprovalStatusType.APPROVED.equals( claimRecipient.getApprovalStatusType().getCode() ) || ApprovalStatusType.WINNER.equals( claimRecipient.getApprovalStatusType().getCode() ) );
    }

    private boolean isDisplayInPublicRecognition( ProductClaimParticipant claimParticipant )
    {
      ClaimItem claimItem = (ClaimItem)claimParticipant.getClaim().getApprovableItems().iterator().next();
      return claimParticipant.getParticipant().isAllowPublicRecognition() && //
          ( ApprovalStatusType.APPROVED.equals( claimItem.getApprovalStatusType().getCode() ) || ApprovalStatusType.WINNER.equals( claimItem.getApprovalStatusType().getCode() ) );
    }

    public static String extractPromotionType( AbstractRecognitionClaim arc )
    {
      if ( arc.isRecognitionClaim() )
      {
        if ( StringUtils.isNotBlank( ( (RecognitionClaim)arc ).getPurlUrl() ) )
        {
          return PublicRecognitionFormattedValueBean.PROMO_TYPE_PURL;
        }

        return PublicRecognitionFormattedValueBean.PROMO_TYPE_RECOGNITION;
      }

      if ( arc.isNominationClaim() )
      {
        return PublicRecognitionFormattedValueBean.PROMO_TYPE_NOMINATION;
      }

      return null;
    }

    public Long getId()
    {
      return id;
    }

    public boolean getIsDetail()
    {
      return isDetail;
    }

    public boolean getIsBadgePromotion()
    {
      return isBadgePromotion;
    }

    public boolean getIsCumulative()
    {
      return isCumulative;
    }

    public void setCumulative( boolean isCumulative )
    {
      this.isCumulative = isCumulative;
    }

    public List<RecognitionDetailParticipant> getRecipients()
    {
      return recipients;
    }

    public List<ClaimDetailParticipant> getRecognizer()
    {
      return recognizer;
    }

    public List<String> getCopies()
    {
      return copies;
    }

    public String getComment()
    {
      return comment;
    }

    @JsonIgnore
    public String getCommentLanguage()
    {
      return commentLanguage;
    }

    public boolean isAllowTranslate()
    {
      return allowTranslate;
    }

    public String getPurlUrl()
    {
      return purlUrl;
    }

    public Long getPromotionId()
    {
      return promotionId;
    }

    public String getPromotionName()
    {
      return promotionName;
    }

    public void setPromotionType( String promotionType )
    {
      this.promotionType = promotionType;
    }

    public String getPromotionType()
    {
      return promotionType;
    }

    public String getDate()
    {
      return date;
    }

    public String getBehavior()
    {
      return behavior;
    }

    public String getBadgeUrl()
    {
      return badgeUrl;
    }

    public void setBadgeUrl( String badgeUrl )
    {
      this.badgeUrl = badgeUrl;
    }

    @JsonProperty( "eCard" )
    public Ecard getEcard()
    {
      return ecard;
    }

    public List<CustomElements> getExtraFields()
    {
      return extraFields;
    }

    public boolean getIsPublicClaim()
    {
      return isPublicClaim;
    }

    public int getNumLikers()
    {
      return numLikers;
    }

    public boolean getIsMine()
    {
      return isMine;
    }

    @JsonProperty( "isLiked" )
    public boolean isLiked()
    {
      return isLiked;
    }

    public void setLiked( boolean isLiked )
    {
      this.isLiked = isLiked;
    }

    public boolean getIsHidden()
    {
      return isHidden;
    }

    public String getTime()
    {
      return time;
    }

    public List<ClaimDetailShareLink> getShareLinks()
    {
      return shareLinks;
    }

    public List<ClaimDetailComment> getComments()
    {
      return comments;
    }

    public void setAllowAddPoints( boolean allowAddPoints )
    {
      this.allowAddPoints = allowAddPoints;
    }

    public boolean getAllowAddPoints()
    {
      return allowAddPoints;
    }

    public List<BudgetValueBean> getBudgets()
    {
      return budgets;
    }

    public Long getAwardAmountMin()
    {
      return awardAmountMin;
    }

    public Long getAwardAmountMax()
    {
      return awardAmountMax;
    }

    public Long getAwardAmountFixed()
    {
      return awardAmountFixed;
    }

    public String getAwardAmount()
    {
      return awardAmount;
    }

    public double getCountryRatio()
    {
      return countryRatio;
    }

    public String getTeamName()
    {
      return teamName;
    }

    public boolean isSweepAward()
    {
      return isSweepAward;
    }

    public void setSweepAward( boolean isSweepAward )
    {
      this.isSweepAward = isSweepAward;
    }
  }

  public static class RecognitionDetailParticipant implements Serializable
  {
    private Long id;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private String orgName;
    private String title;
    private String department;
    private String countryCode;
    private String viewCertificateUrl;

    public RecognitionDetailParticipant( Participant pax, Node org, String viewCertificateUrl )
    {
      if ( pax != null )
      {
        id = pax.getId();
        firstName = pax.getFirstName();
        lastName = pax.getLastName();
        avatarUrl = pax.getAvatarSmallFullPath( null );

        UserAddress primaryUserAddress = getUserService().getPrimaryUserAddress( pax.getId() );
        if ( primaryUserAddress != null )
        {
          countryCode = primaryUserAddress.getAddress().getCountry().getCountryCode();
        }

        if ( org != null )
        {
          orgName = org.getName();

          for ( ParticipantEmployer paxEmployer : pax.getParticipantEmployers() )
          {
            PickListValueBean pickListDeptValueBean = getUserService()
                .getPickListValueFromCMView( DepartmentType.PICKLIST_ASSET + ".items",
                                             pax.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : pax.getLanguageType().getCode(),
                                             paxEmployer.getDepartmentType() );
            PickListValueBean pickListPositionValueBean = getUserService()
                .getPickListValueFromCMView( PositionType.PICKLIST_ASSET + ".items",
                                             pax.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : pax.getLanguageType().getCode(),
                                             paxEmployer.getPositionType() );
            if ( paxEmployer.getTerminationDate() == null )
            {
              if ( paxEmployer.getPositionType() != null )
              {
                title = pickListPositionValueBean.getName();
              }
              if ( paxEmployer.getDepartmentType() != null )
              {
                department = pickListDeptValueBean.getName();
              }
              break;
            }
          }
        }
      }

      /*
       * if( submitterComment != null ) { this.comment = submitterComment.trim(); }
       */

      if ( StringUtils.isNotBlank( viewCertificateUrl ) )
      {
        this.viewCertificateUrl = viewCertificateUrl;
      }
    }

    public Long getId()
    {
      return id;
    }

    public String getFirstName()
    {
      return firstName;
    }

    public String getLastName()
    {
      return lastName;
    }

    public String getAvatarUrl()
    {
      return avatarUrl;
    }

    public String getOrgName()
    {
      return orgName;
    }

    public String getTitle()
    {
      return title;
    }

    public String getDepartment()
    {
      return department;
    }

    public String getCountryCode()
    {
      return countryCode;
    }

    public String getViewCertificateUrl()
    {
      return viewCertificateUrl;
    }
  }

  public static class Ecard implements Serializable
  {
    private final String id;
    private final String name;
    private final String imgUrl;
    private final String imgThumbUrl;
    private final String type;
    private final String videoFileType;
    private final String videoSrc;

    private static final String ECARD_TYPE_IMAGE = "image";
    private static final String ECARD_TYPE_VIDEO = "video";

    public Ecard( Card card, String contextPath )
    {
      id = card.getId() == null ? "" : card.getId().toString();
      name = card.getName();
      imgUrl = card.getLargeImageName();
      imgThumbUrl = card.getSmallImageName();
      type = ECARD_TYPE_IMAGE;
      videoFileType = null;
      videoSrc = null;
    }

    public Ecard( ECard card, String contextPath )
    {
      id = card.getId() == null ? "" : card.getId().toString();
      name = card.getName();
      imgUrl = card.getLargeImageNameLocale();
      imgThumbUrl = card.getSmallImageNameLocale();
      type = ECARD_TYPE_IMAGE;
      videoFileType = null;
      videoSrc = null;
    }

    public Ecard( String ownCardName )
    {
      id = "0";
      name = ownCardName;
      imgUrl = ownCardName;
      imgThumbUrl = ownCardName;
      type = SupportedEcardVideoTypes.isSupportedVideo( ownCardName ) ? ECARD_TYPE_VIDEO : ECARD_TYPE_IMAGE;

      if ( ECARD_TYPE_VIDEO.equals( type ) )
      {
        videoFileType = SupportedEcardVideoTypes.getVideoFileTypeFor( name );
        videoSrc = ownCardName;
      }
      else
      {
        videoFileType = null;
        videoSrc = null;
      }
    }

    public Ecard( String videoUrl, String videoThumbUrl )
    {
      id = "0";
      name = "video";
      type = ECARD_TYPE_VIDEO;
      videoFileType = SupportedEcardVideoTypes.getVideoFileTypeFor( videoUrl );
      videoSrc = videoUrl;

      // Let the image also be the video, I guess? FE seems to expect this
      imgUrl = videoUrl;

      imgThumbUrl = videoThumbUrl;
    }

    public String getId()
    {
      return id;
    }

    public String getName()
    {
      return name;
    }

    public String getImgUrl()
    {
      return imgUrl;
    }

    public String getImgThumbUrl()
    {
      return imgThumbUrl;
    }

    public String getType()
    {
      return type;
    }

    public String getVideoFileType()
    {
      return videoFileType;
    }

    public String getVideoSrc()
    {
      return videoSrc;
    }
  }

  public static class ClaimDetailShareLink implements Serializable
  {
    private String url;
    private String name;
    private String nameId;

    public ClaimDetailShareLink( String url, String name, String nameId )
    {
      this.url = url;
      this.name = name;
      this.nameId = nameId;
    }

    public String getUrl()
    {
      return url;
    }

    public String getName()
    {
      return name;
    }

    public String getNameId()
    {
      return nameId;
    }
  }

  public static class ClaimDetailComment implements Serializable
  {
    private final Long id;
    private final String comment;
    private final boolean allowTranslate;
    private final RecognitionDetailParticipant commenter;
    private boolean isMine = false;

    public ClaimDetailComment( Long id, String comment, boolean allowTranslate, RecognitionDetailParticipant commenter )
    {
      this.id = id;
      this.comment = comment;
      this.allowTranslate = allowTranslate;
      this.commenter = commenter;
      if ( UserManager.getUserId().equals( commenter.getId() ) )
      {
        this.isMine = true;
      }
    }

    public Long getId()
    {
      return id;
    }

    public boolean getIsMine()
    {
      return isMine;
    }

    public String getComment()
    {
      return comment;
    }

    public boolean getAllowTranslate()
    {
      return allowTranslate;
    }

    public RecognitionDetailParticipant getCommenter()
    {
      return commenter;
    }
  }

  public static class CustomElements implements Serializable
  {
    private String name;
    private String value;

    public CustomElements( ClaimElement claimElement, String cmAssetCode )
    {
      this.name = ContentReaderManager.getText( cmAssetCode, claimElement.getClaimFormStepElement().getCmKeyForElementLabel() );

      if ( claimElement.getClaimFormStepElement().getClaimFormElementType().isSectionHeading() )
      {
        this.name = ContentReaderManager.getText( cmAssetCode, claimElement.getClaimFormStepElement().getCmKeyForHeading() );
      }

      if ( StringUtils.isNotEmpty( claimElement.getValue() ) )
      {
        if ( claimElement.getClaimFormStepElement().getClaimFormElementType().isSelectField() )
        {
          this.value = DynaPickListType.lookup( claimElement.getClaimFormStepElement().getSelectionPickListName(), claimElement.getValue() ).getName();
        }
        else if ( claimElement.getClaimFormStepElement().getClaimFormElementType().isMultiSelectField() )
        {
          this.value = "";
          Iterator pickListCodes = ArrayUtil.convertDelimitedStringToList( claimElement.getValue(), "," ).iterator();
          while ( pickListCodes.hasNext() )
          {
            String code = (String)pickListCodes.next();
            value += DynaPickListType.lookup( claimElement.getClaimFormStepElement().getSelectionPickListName(), code ).getName();
            if ( pickListCodes.hasNext() )
            {
              value += ", ";
            }
          }
        }
        else if ( claimElement.getClaimFormStepElement().getClaimFormElementType().isBooleanField() )
        {
          if ( claimElement.getValue().equals( "true" ) )
          {
            this.value = ContentReaderManager.getText( cmAssetCode, claimElement.getClaimFormStepElement().getCmKeyForLabelTrue() );
          }
          else
          {
            this.value = ContentReaderManager.getText( cmAssetCode, claimElement.getClaimFormStepElement().getCmKeyForLabelFalse() );
          }
        }
        else if ( claimElement.getClaimFormStepElement().getClaimFormElementType().isAddressBlock() )
        {
          StringBuffer buffer = new StringBuffer();
          if ( claimElement.getValue().contains( "~" ) )
          {
            claimElement.setValue( claimElement.getValue().replace( "~", "|" ) );
          }
          String addr1 = claimElement.getValue().substring( 0, claimElement.getValue().indexOf( "|" ) );
          String addr2 = claimElement.getValue().substring( claimElement.getValue().indexOf( "|" ) + 1, claimElement.getValue().length() );
          String addr3 = addr2.substring( addr2.indexOf( "|" ) + 1, addr2.length() );
          String addr4 = addr3.substring( addr3.indexOf( "|" ) + 1, addr3.length() );
          String addr5 = addr4.substring( addr4.indexOf( "|" ) + 1, addr4.length() );
          String addr6 = addr5.substring( addr5.indexOf( "|" ) + 1, addr5.length() );
          String addr7 = addr6.substring( addr6.indexOf( "|" ) + 1, addr6.length() );
          String stateCode = addr5.substring( 0, addr5.indexOf( "|" ) );
          String countryCode = addr7.substring( 0, addr7.indexOf( "|" ) );
          if ( !StringUtils.isEmpty( countryCode ) )
          {
            // this is to get state informatiom
            if ( AddressUtil.isNorthAmerican( countryCode ) && !StringUtils.isEmpty( stateCode ) )
            {
              stateCode = StateType.lookup( stateCode ).getName();
            }
          }
          buffer.append( addr1 ).append( " " );
          buffer.append( addr2.substring( 0, addr2.indexOf( "|" ) ) ).append( " " );
          buffer.append( addr3.substring( 0, addr3.indexOf( "|" ) ) ).append( " " );
          buffer.append( addr4.substring( 0, addr4.indexOf( "|" ) ) ).append( " " );
          buffer.append( stateCode ).append( " " );
          buffer.append( addr6.substring( 0, addr6.indexOf( "|" ) ) ).append( " " );

          this.value = buffer.toString();
        }
        else
        {
          if ( claimElement.getClaimFormStepElement().isMaskedOnEntry() )
          {
            this.value = "*************";
          }
          else
          {
            this.value = claimElement.getValue();
          }
        }
      }
    }

    public String getName()
    {
      return name;
    }

    public String getValue()
    {
      return value;
    }
  }

  private static UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }

  public void mergeTeamClaims( List<PublicRecognitionFormattedValueBean> publicRecognitions )
  {
    for ( PublicRecognitionFormattedValueBean publicRecognition : publicRecognitions )
    {
      if ( Boolean.valueOf( publicRecognition.getIsMine() ) )
      {
        this.getRecognition().isMine = true;
      }
    }
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

  public static NominationClaimService getNominationClaimService()
  {
    return (NominationClaimService)BeanLocator.getBean( NominationClaimService.BEAN_NAME );
  }

  public static MTCVideoService getMTCVideoService()
  {
    return (MTCVideoService)BeanLocator.getBean( MTCVideoService.BEAN_NAME );
  }
}
