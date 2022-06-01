/*
 * (c) 2012 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/impl/PublicRecognitionServiceImpl.java,v $
 */

package com.biperf.core.service.promotion.impl;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.biperf.core.dao.claim.ClaimDAO;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.journal.JournalDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.dao.promotion.PublicRecognitionDAO;
import com.biperf.core.domain.audit.PayoutCalculationAudit;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.claim.RecognitionClaimSource;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.BudgetActionType;
import com.biperf.core.domain.enums.BudgetStatusType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.PromoNominationBehaviorType;
import com.biperf.core.domain.enums.PromoRecognitionBehaviorType;
import com.biperf.core.domain.enums.PromotionBehaviorType;
import com.biperf.core.domain.enums.PromotionEmailNotificationType;
import com.biperf.core.domain.enums.PublicRecognitionTabType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.multimedia.Card;
import com.biperf.core.domain.multimedia.ECard;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionNotification;
import com.biperf.core.domain.promotion.PromotionNotificationType;
import com.biperf.core.domain.promotion.PublicRecognitionComment;
import com.biperf.core.domain.promotion.PublicRecognitionLike;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.publicrecognition.PublicRecognitionParticipantView;
import com.biperf.core.domain.publicrecognition.PublicRecognitionSet;
import com.biperf.core.domain.publicrecognition.PublicRecognitionSetCountBean;
import com.biperf.core.domain.publicrecognition.PublicRecognitionShareLinkBean;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.exception.DataException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.AssociationRequest;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.TranslatedContent;
import com.biperf.core.service.audit.PayoutCalculationAuditService;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.budget.BudgetMasterToBudgetSegmentsAssociationRequest;
import com.biperf.core.service.budget.BudgetToNodeOwnersAddressAssociationRequest;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.claim.RecognitionClaimRecipient;
import com.biperf.core.service.claim.RecognitionClaimSubmission;
import com.biperf.core.service.claim.RecognitionClaimSubmissionResponse;
import com.biperf.core.service.cmx.CMXService;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.facebook.FacebookService;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.multimedia.MultimediaService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.PublicRecognitionService;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.translation.TranslationService;
import com.biperf.core.service.translation.UnexpectedTranslationException;
import com.biperf.core.service.translation.UnsupportedTranslationException;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.BudgetUtils;
import com.biperf.core.utils.CmxTranslateHelperBean;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.HtmlUtils;
import com.biperf.core.utils.LocaleUtils;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.NumberFormatUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.BudgetValueBean;
import com.biperf.core.value.ClaimInfoBean;
import com.biperf.core.value.PublicRecognitionFormattedValueBean;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.CmsUtil;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * Javadoc for PublicRecognitionServiceImpl.
 * 
 * @author veeramas
 * @since Jul 24, 2012
 * @version 1.0
 */
public class PublicRecognitionServiceImpl implements PublicRecognitionService
{

  private static final Log log = LogFactory.getLog( PublicRecognitionServiceImpl.class );
  private static final String PAGE_NUMBER = "page";

  private PublicRecognitionDAO publicRecognitionDAO;
  private ClaimDAO claimDAO;
  private UserDAO userDAO;
  private FacebookService facebookService;
  private UserService userService;
  private SystemVariableService systemVariableService;
  private ClaimService claimService;
  private AudienceService audienceService;
  private ParticipantService participantService;
  private PromotionService promotionService;
  private CountryService countryService;
  private PayoutCalculationAuditService payoutCalculationAuditService;
  private JournalDAO journalDAO;
  private PurlService purlService;
  private BudgetMasterService budgetService;
  private TranslationService translationService;
  private MailingService mailingService;
  private MessageService messageService;
  private NodeDAO nodeDAO;
  private MultimediaService multimediaService;
  private MerchLevelService merchLevelService;
  @Autowired
  private CMXService cmxService;

  public PublicRecognitionDAO getPublicRecognitionDAO()
  {
    return publicRecognitionDAO;
  }

  public void setPublicRecognitionDAO( PublicRecognitionDAO publicRecognitionDAO )
  {
    this.publicRecognitionDAO = publicRecognitionDAO;
  }

  public void setClaimDAO( ClaimDAO claimDAO )
  {
    this.claimDAO = claimDAO;
  }

  /**
   * Get User Count who likes Claim
   * @param claimId
   * @return List<PublicRecognitionSet>
   */
  public long recognitionLikeCountByClaimId( Long claimId )
  {
    return publicRecognitionDAO.getLikeCountByClaimId( claimId );
  }

  public void likePublicRecognition( Long participantId, Long claimId )
  {
    Participant user = participantService.getParticipantById( participantId );
    AbstractRecognitionClaim claim = (AbstractRecognitionClaim)claimService.getClaimById( claimId );
    List<AbstractRecognitionClaim> teamClaims = (List<AbstractRecognitionClaim>)claimService.getClaimsByTeamId( claim.getTeamId() );

    teamClaims.forEach( ( teamClaim ) ->
    {
      PublicRecognitionLike publicRecogLike = new PublicRecognitionLike();
      publicRecogLike.setClaim( teamClaim );
      publicRecogLike.setTeamId( teamClaim.getTeamId() );
      publicRecogLike.setUser( user );
      publicRecogLike.setIsLiked( true );
      publicRecognitionDAO.savePublicRecognitionLike( publicRecogLike );
    } );
  }

  public void unlikePublicRecognition( Long participantId, Long claimId )
  {
    List<PublicRecognitionLike> allLikesForClaim = publicRecognitionDAO.getUserLikesByClaim( claimId );
    for ( PublicRecognitionLike like : allLikesForClaim )
    {
      List<PublicRecognitionLike> allLikesForTeam = publicRecognitionDAO.getUserLikesByTeamId( like.getTeamId() );
      allLikesForTeam.forEach( ( teamClaim ) ->
      {
        if ( teamClaim.getUser().getId().equals( participantId ) )
        {
          // delete it!
          publicRecognitionDAO.deletePublicRecognitionLike( teamClaim );
        }
      } );
    }
  }

  /**
   * Save Public Recognition Comment
   * @param publicRecogComments
   * @return List<PublicRecognitionComment>
   */
  public List<PublicRecognitionComment> savePublicRecognitionComments( List<PublicRecognitionComment> publicRecogComments )
  {
    List<PublicRecognitionComment> savedComments = new ArrayList<PublicRecognitionComment>();
    for ( PublicRecognitionComment publicRecogComment : publicRecogComments )
    {
      publicRecogComment.setCommentsLanguageType( userService.getPreferredLanguageFor( publicRecogComment.getUser().getId() ) );
      savedComments.add( publicRecognitionDAO.savePublicRecognitionComment( publicRecogComment ) );
    }

    return savedComments;
  }

  /**
   * Get All Likes per Team
   * @param teamId
   * @return List<PublicRecognitionLike>
   */
  public List<PublicRecognitionLike> getUserLikesByTeam( Long teamId )
  {
    return publicRecognitionDAO.getUserLikesByTeam( teamId );
  }

  /**
   * Get All Comments per Team
   * @param teamId
   * @return List<PublicRecognitionComment>
   */
  public List<PublicRecognitionComment> getUserCommentsByTeam( Long teamId )
  {
    return publicRecognitionDAO.getUserCommentsByTeamId( teamId );
  }

  /**
   * Get Likes 
   * @param claimId
   * @return List<PublicRecognitionLike>
   */
  public List<PublicRecognitionLike> getLikedPaxListByClaimIdWithAssociations( Long claimId, AssociationRequestCollection associationRequests, int pageNumber )
  {
    List likers = publicRecognitionDAO.getLikedPaxListByClaimId( claimId, pageNumber );
    for ( Iterator iter = likers.iterator(); iter.hasNext(); )
    {
      Participant paxWhoLiked = ( (PublicRecognitionLike)iter.next() ).getUser();
      for ( Iterator iterator = associationRequests.iterator(); iterator.hasNext(); )
      {
        AssociationRequest req = (AssociationRequest)iterator.next();
        req.execute( paxWhoLiked );
      }
    }
    return likers;
  }

  public int getLikedPaxCount( Long claimId )
  {
    int likedPax = publicRecognitionDAO.getLikedPaxCount( claimId );
    if ( publicRecognitionDAO.isCurrentUserLikedClaim( claimId, UserManager.getUserId() ) )
    {
      return likedPax - 1;
    }
    else
    {
      return likedPax;
    }
  }

  /**
   * 
   * Return a list of public recognition formatted beans that are associated for the specified claim.
   *
   * @param claimId
   * @param userId
   *
   * @return PublicRecognitionFormattedValueBean
   */
  @Override
  public List<PublicRecognitionFormattedValueBean> getPublicRecognitionClaimsByClaimId( Long claimId, Long userId )
  {
    List<PublicRecognitionFormattedValueBean> publicRecognitions = null;
    Participant currentParticipant = this.participantService.getParticipantById( userId );

    publicRecognitions = publicRecognitionDAO.getPublicRecognitionClaimsByClaimId( claimId, userId );

    Map<Long, RecognitionPromotion> recognitionPromotionMap = new HashMap<Long, RecognitionPromotion>();
    Map<Long, NominationPromotion> nominationPromotionMap = new HashMap<Long, NominationPromotion>();
    for ( PublicRecognitionFormattedValueBean bean : publicRecognitions )
    {
      if ( bean.isRecognitionClaim() )
      {
        if ( !recognitionPromotionMap.containsKey( bean.getPromotionId() ) )
        {
          RecognitionPromotion recognitionPromotion = (RecognitionPromotion)promotionService.getPromotionById( bean.getPromotionId() );
          recognitionPromotionMap.put( bean.getPromotionId(), recognitionPromotion );
          break;
        }
      }
      else if ( bean.isNominationClaim() )
      {
        if ( !nominationPromotionMap.containsKey( bean.getPromotionId() ) )
        {
          NominationPromotion nominationPromotion = (NominationPromotion)promotionService.getPromotionById( bean.getPromotionId() );
          nominationPromotionMap.put( bean.getPromotionId(), nominationPromotion );
          break;
        }
      }
    }

    for ( PublicRecognitionFormattedValueBean publicRecognition : publicRecognitions )
    {
      List<PublicRecognitionLike> likesForClaims = getUserLikesByTeam( publicRecognition.getTeamId() );
      List<PublicRecognitionComment> commentsForClaims = getUserCommentsByTeam( publicRecognition.getTeamId() );

      String companyName = systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();
      publicRecognition.setUserLikes( likesForClaims );
      publicRecognition.setUserComments( commentsForClaims );

      if ( publicRecognition.isNominationClaim() )
      {
        NominationPromotion nominationPromotion = (NominationPromotion)nominationPromotionMap.get( publicRecognition.getPromotionId() );
        publicRecognition.setIsPublicClaim( nominationPromotion.isAllowPublicRecognition() );
      }
      else
      {
        RecognitionPromotion recognitionPromotion = (RecognitionPromotion)recognitionPromotionMap.get( publicRecognition.getPromotionId() );
        publicRecognition.setIsPublicClaim( recognitionPromotion.isAllowPublicRecognition() );
      }

      if ( currentParticipant == null )
      {
        // Bug 3359 - Reports. When the admin logs in the currentParticipant is null.
        publicRecognition.setIsLiked( "false" );
        publicRecognition.setIsMine( "false" );
        publicRecognition.setAllowAddPoints( false );
      }
      else
      {
        publicRecognition.setIsLiked( isUserLiked( currentParticipant.getId(), publicRecognition.getUserLikes() ) );
        String isMine = isMine( currentParticipant.getId(), publicRecognition );
        publicRecognition.setIsMine( isMine );
        publicRecognition = buildAddPointsSection( publicRecognition, currentParticipant, currentParticipant, recognitionPromotionMap, true );
      }
      String eCardImageUrl = getEcardImageUrlForFBPost( publicRecognition );
      // purl url will be null if not a PURL claim
      if ( publicRecognition.isRecognitionClaim() )
      {
        publicRecognition.setPurlUrl( purlService.createPurlRecipientUrlFromClaimId( publicRecognition.getClaimId() ) );
      }

      if ( Boolean.valueOf( publicRecognition.getIsMine() ) )
      {
        String formattedComments = HtmlUtils.removeFormatting( publicRecognition.getSubmitterComments() );

        publicRecognition.setSocialLinks( getSocialLinks( currentParticipant.getFirstName() + " was recognized in the " + companyName + " " + publicRecognition.getPromotionName() + " program.",
                                                          "Here is what was said:<center></center>" + formattedComments,
                                                          publicRecognition.getPurlUrl(),
                                                          eCardImageUrl ) );
      }
    }
    return publicRecognitions;
  }

  public List<PublicRecognitionSet> getPublicRecognitionClaimsByTabType( Long userId, String listType, int pageSize, int pageNumber, String listValue ) throws ServiceErrorException
  {
    int rowNumStart = ( pageNumber - 1 ) * pageSize + 1;
    int rowNumEnd = rowNumStart + pageSize - 1;
  
    
    List<PublicRecognitionSet> recognitionSetValueList = null;
    List<PublicRecognitionFormattedValueBean> claims = getPublicRecognitionDAO().getPublicRecognitionList( userId, listType, rowNumStart, rowNumEnd, listValue );

    if ( claims != null )
    {
      if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
      {
        setCmxTranslateContent( claims );
      }
      // changes for recommended tab
      if ( listType.equals( PublicRecognitionTabType.lookup( PublicRecognitionTabType.RECOMMENDED_TAB ).getCode() ) )
      {
        recognitionSetValueList = getRecommendedPublicRecognitionClaims( pageSize, pageNumber, userId, claims );
      }
      else if ( listType.equals( PublicRecognitionTabType.lookup( PublicRecognitionTabType.GLOBAL_TAB ).getCode() ) )
      {
        recognitionSetValueList = getGlobalPublicRecognitionClaims( pageSize, pageNumber, userId, claims );
      }
      // Client customization for WIP #45685 starts
      else if ( listType.equals( PublicRecognitionTabType.lookup( PublicRecognitionTabType.DIVISION_TAB ).getCode() ) )
      {
        recognitionSetValueList = getDivisionPublicRecognitionClaims( pageSize, pageNumber, userId, claims );
      }
      // Client customization for WIP #45685 ends
      else if ( listType.equals( PublicRecognitionTabType.lookup( PublicRecognitionTabType.TEAM_TAB ).getCode() ) )
      {
        recognitionSetValueList = getTeamPublicRecognitionClaims( pageSize, pageNumber, userId, claims );
      }
      else if ( listType.equals( PublicRecognitionTabType.lookup( PublicRecognitionTabType.FOLLOWED_TAB ).getCode() ) )
      {
        recognitionSetValueList = getFollowedUserPublicRecognitionClaims( pageSize, pageNumber, userId, claims );
      }
      else if ( listType.equals( PublicRecognitionTabType.lookup( PublicRecognitionTabType.COUNRTY_TAB ).getCode() ) )
      {
        recognitionSetValueList = getCountryPublicRecognitionClaims( pageSize, pageNumber, userId, claims );
      }
      else if ( listType.equals( PublicRecognitionTabType.lookup( PublicRecognitionTabType.DEPARTMENT_TAB ).getCode() ) )
      {
        recognitionSetValueList = getDepartmentPublicRecognitionClaims( pageSize, pageNumber, userId, claims );
      }
      else
      {
        recognitionSetValueList = getUserPublicRecognitionClaims( pageSize, pageNumber, userId, claims );
      }
    }
    return recognitionSetValueList;
  }

  private void setCmxTranslateContent( List<PublicRecognitionFormattedValueBean> claims )
  {
    Set<String> translateKeys = new HashSet<String>();

    claims.stream().forEach( c ->
    {
      if ( StringUtils.isNotEmpty( c.getCelebrationId() ) )
      {
        translateKeys.add( c.getCelebImgDesCmxAssetCode() );
        translateKeys.add( c.getCelebMsgCmxAssetCode() );
        translateKeys.add( c.getProgramHeaderCmxCode() );
        translateKeys.add( c.getProgramNameCmxCode() );
      }
    } );

    if ( !translateKeys.isEmpty() )
    {
      try
      {
        Map<String, String> translateBundles = cmxService.getTranslation( UserManager.getUserLocale(), new ArrayList<String>( translateKeys ) );

        if ( null != translateBundles && !translateBundles.isEmpty() )
        {
          CmxTranslateHelperBean helperBean = new CmxTranslateHelperBean( translateBundles );

          for ( Iterator<PublicRecognitionFormattedValueBean> claim = claims.iterator(); claim.hasNext(); )
          {
            PublicRecognitionFormattedValueBean bean = claim.next();

            if ( StringUtils.isNotEmpty( bean.getCelebrationId() ) )
            {
              bean.setCelebMessage( helperBean.getCmxTranslatedValue( bean.getCelebMsgCmxAssetCode(), bean.getCelebMessage() ) );
              bean.setCelebImgDesc( helperBean.getCmxTranslatedValue( bean.getCelebImgDesCmxAssetCode(), bean.getCelebImgDesc() ) );
              bean.setProgramHeader( helperBean.getCmxTranslatedValue( bean.getProgramHeaderCmxCode(), bean.getProgramHeader() ) );
              bean.setPromotionName( helperBean.getCmxTranslatedValue( bean.getProgramNameCmxCode(), bean.getPromotionName() ) );
            }
          }
        }

      }
      catch( DataException e )
      {
        log.error( "Exception while Calling CMX Translation Service .Using default locale " + e.getMessage() );
      }
    }

  }

  // added as part of the
  private List<PublicRecognitionSet> getRecommendedPublicRecognitionClaims( int pageSize, int pageNumber, Long publicUserId, List<PublicRecognitionFormattedValueBean> recommendedClaims )
  {
    List<PublicRecognitionSet> recognitionSetvalueList = new ArrayList<PublicRecognitionSet>();
    PublicRecognitionSetCountBean countBean = new PublicRecognitionSetCountBean();

    if ( !recommendedClaims.isEmpty() )
    {
      countBean.setRecommendedTabCount( pageSize * pageNumber + 2 );
    }
    PublicRecognitionTabType tab = PublicRecognitionTabType.lookup( PublicRecognitionTabType.RECOMMENDED_TAB );
    PublicRecognitionSet recognitionSetvalue = createRecognitionSet( tab, countBean.getRecommendedTabCount() );

    populatePublicRecognitionSetForClaims( recommendedClaims, publicUserId, recognitionSetvalue );

    if ( getDefaultTab().equals( tab.getCode() ) )
    {
      recognitionSetvalue.setIsDefault( true );
    }

    recognitionSetvalueList.add( recognitionSetvalue );

    addGlobal( recognitionSetvalueList, countBean.getGlobalTabCount() );
    // Client customization for WIP #45685 starts
    addDivision( recognitionSetvalueList, countBean.getDivisionTabCount() );
    // Client customization for WIP #45685 ends
    addTeam( recognitionSetvalueList, countBean.getTeamTabCount() );
    addFollowed( recognitionSetvalueList, publicUserId, countBean.getFollowersTabCount() );
    addMe( recognitionSetvalueList, countBean.getMineTabCount() );
    addCountry( recognitionSetvalueList, countBean.getCountryTabCount() );
    addDepartment( recognitionSetvalueList, countBean.getDepartmentTabCount() );

    return recognitionSetvalueList;
  }

  // added as part of adding the country filter
  private List<PublicRecognitionSet> getCountryPublicRecognitionClaims( int pageSize, int pageNumber, Long publicUserId, List<PublicRecognitionFormattedValueBean> coutryClaims )
  {
    List<PublicRecognitionSet> recognitionSetvalueList = new ArrayList<PublicRecognitionSet>();
    PublicRecognitionSetCountBean countBean = new PublicRecognitionSetCountBean();

    if ( !coutryClaims.isEmpty() )
    {
      countBean.setCountryTabCount( pageSize * pageNumber + 2 );
    }
    PublicRecognitionTabType tab = PublicRecognitionTabType.lookup( PublicRecognitionTabType.COUNRTY_TAB );
    PublicRecognitionSet recognitionSetvalue = createRecognitionSet( tab, countBean.getCountryTabCount() );

    populatePublicRecognitionSetForClaims( coutryClaims, publicUserId, recognitionSetvalue );

    if ( getDefaultTab().equals( tab.getCode() ) )
    {
      recognitionSetvalue.setIsDefault( true );
    }

    recognitionSetvalueList.add( recognitionSetvalue );

    addRecommended( recognitionSetvalueList, countBean.getRecommendedTabCount() );
    addGlobal( recognitionSetvalueList, countBean.getGlobalTabCount() );
    // Client customization for WIP #45685 starts
    addDivision( recognitionSetvalueList, countBean.getDivisionTabCount() );
    // Client customization for WIP #45685 ends
    addTeam( recognitionSetvalueList, countBean.getTeamTabCount() );
    addFollowed( recognitionSetvalueList, publicUserId, countBean.getFollowersTabCount() );
    addMe( recognitionSetvalueList, countBean.getMineTabCount() );
    addDepartment( recognitionSetvalueList, countBean.getDepartmentTabCount() );

    return recognitionSetvalueList;
  }

  private List<PublicRecognitionSet> getDepartmentPublicRecognitionClaims( int pageSize, int pageNumber, Long publicUserId, List<PublicRecognitionFormattedValueBean> departmentClaims )
  {
    List<PublicRecognitionSet> recognitionSetvalueList = new ArrayList<PublicRecognitionSet>();
    PublicRecognitionSetCountBean countBean = new PublicRecognitionSetCountBean();

    if ( !departmentClaims.isEmpty() )
    {
      countBean.setDepartmentTabCount( pageSize * pageNumber + 2 );
    }
    PublicRecognitionTabType tab = PublicRecognitionTabType.lookup( PublicRecognitionTabType.DEPARTMENT_TAB );
    PublicRecognitionSet recognitionSetvalue = createRecognitionSet( tab, countBean.getDepartmentTabCount() );

    populatePublicRecognitionSetForClaims( departmentClaims, publicUserId, recognitionSetvalue );

    if ( getDefaultTab().equals( tab.getCode() ) )
    {
      recognitionSetvalue.setIsDefault( true );
    }

    recognitionSetvalueList.add( recognitionSetvalue );

    addRecommended( recognitionSetvalueList, countBean.getRecommendedTabCount() );
    addGlobal( recognitionSetvalueList, countBean.getGlobalTabCount() );
 // Client customization for WIP #45685 starts
    addDivision( recognitionSetvalueList, countBean.getDivisionTabCount() );
    // Client customization for WIP #45685 ends
    addTeam( recognitionSetvalueList, countBean.getTeamTabCount() );    
    addFollowed( recognitionSetvalueList, publicUserId, countBean.getFollowersTabCount() );
    addMe( recognitionSetvalueList, countBean.getMineTabCount() );
    addCountry( recognitionSetvalueList, countBean.getCountryTabCount() );

    return recognitionSetvalueList;
  }

  private List<PublicRecognitionSet> getGlobalPublicRecognitionClaims( int pageSize, int pageNumber, Long publicUserId, List<PublicRecognitionFormattedValueBean> globalClaims )
  {
    List<PublicRecognitionSet> recognitionSetvalueList = new ArrayList<PublicRecognitionSet>();
    PublicRecognitionSetCountBean countBean = new PublicRecognitionSetCountBean();
    if ( !globalClaims.isEmpty() )
    {
      countBean.setGlobalTabCount( pageSize * pageNumber + 2 );
    }
    PublicRecognitionTabType tab = PublicRecognitionTabType.lookup( PublicRecognitionTabType.GLOBAL_TAB );
    PublicRecognitionSet recognitionSetvalue = createRecognitionSet( tab, countBean.getGlobalTabCount() );

    populatePublicRecognitionSetForClaims( globalClaims, publicUserId, recognitionSetvalue );

    if ( getDefaultTab().equals( tab.getCode() ) )
    {
      recognitionSetvalue.setIsDefault( true );
    }
    
    recognitionSetvalueList.add( recognitionSetvalue );

    addRecommended( recognitionSetvalueList, countBean.getRecommendedTabCount() );
 // Client customization for WIP #45685 starts
    addDivision( recognitionSetvalueList, countBean.getDivisionTabCount() );
    // Client customization for WIP #45685 ends
    addTeam( recognitionSetvalueList, countBean.getTeamTabCount() );
    addFollowed( recognitionSetvalueList, publicUserId, countBean.getFollowersTabCount() );
    addMe( recognitionSetvalueList, countBean.getMineTabCount() );
    addCountry( recognitionSetvalueList, countBean.getCountryTabCount() );
    addDepartment( recognitionSetvalueList, countBean.getDepartmentTabCount() );

    return recognitionSetvalueList;
  }

  private List<PublicRecognitionSet> getTeamPublicRecognitionClaims( int pageSize, int pageNumber, Long publicUserId, List<PublicRecognitionFormattedValueBean> teamClaims )
  {
    List<PublicRecognitionSet> recognitionSetValueList = new ArrayList<PublicRecognitionSet>();

    PublicRecognitionSetCountBean countBean = new PublicRecognitionSetCountBean();

    if ( !teamClaims.isEmpty() )
    {
      countBean.setTeamTabCount( pageSize * pageNumber + 2 );
    }
    PublicRecognitionTabType tab = PublicRecognitionTabType.lookup( PublicRecognitionTabType.TEAM_TAB );
    PublicRecognitionSet recognitionSetvalue = createRecognitionSet( tab, countBean.getTeamTabCount() );

    populatePublicRecognitionSetForClaims( teamClaims, publicUserId, recognitionSetvalue );
    if ( getDefaultTab().equals( tab.getCode() ) )
    {
      recognitionSetvalue.setIsDefault( true );
    }

    addGlobal( recognitionSetValueList, countBean.getGlobalTabCount() );
    // Client customization for WIP #45685 starts
    addDivision( recognitionSetValueList, countBean.getDivisionTabCount() );
    // Client customization for WIP #45685 ends
    addRecommended( recognitionSetValueList, countBean.getRecommendedTabCount() );   
    if ( Arrays.asList( getActiveTabList() ).contains( PublicRecognitionTabType.TEAM_TAB ) )
    {
      recognitionSetValueList.add( recognitionSetvalue );
    }
    addFollowed( recognitionSetValueList, publicUserId, countBean.getFollowersTabCount() );
    addMe( recognitionSetValueList, countBean.getMineTabCount() );
    addCountry( recognitionSetValueList, countBean.getCountryTabCount() );
    addDepartment( recognitionSetValueList, countBean.getDepartmentTabCount() );

    return recognitionSetValueList;
  }
  // Client customization for WIP #45685 starts
  private List<PublicRecognitionSet> getDivisionPublicRecognitionClaims( int pageSize, int pageNumber, Long publicUserId, List<PublicRecognitionFormattedValueBean> divisionClaims )
  { 
	    List<PublicRecognitionSet> recognitionSetValueList = new ArrayList<PublicRecognitionSet>();

	    PublicRecognitionSetCountBean countBean = new PublicRecognitionSetCountBean();

	    if ( !divisionClaims.isEmpty() )
	    {
	      countBean.setDivisionTabCount( pageSize * pageNumber + 2 );
	    }
	    PublicRecognitionTabType tab = PublicRecognitionTabType.lookup( PublicRecognitionTabType.DIVISION_TAB );
	    PublicRecognitionSet recognitionSetvalue = createRecognitionSet( tab, countBean.getDivisionTabCount() );

	    populatePublicRecognitionSetForClaims( divisionClaims, publicUserId, recognitionSetvalue );
	    if ( getDefaultTab().equals( tab.getCode() ) )
	    {
	      recognitionSetvalue.setIsDefault( true );
	    }

	    addGlobal( recognitionSetValueList, countBean.getGlobalTabCount() );
	    addRecommended( recognitionSetValueList, countBean.getRecommendedTabCount() );
	    // Client customization for WIP #45685 starts
	    addTeam( recognitionSetValueList, countBean.getTeamTabCount() );
	    // Client customization for WIP #45685 ends
	    if ( Arrays.asList( getActiveTabList() ).contains( PublicRecognitionTabType.DIVISION_TAB ) )
	    {
	      recognitionSetValueList.add( recognitionSetvalue );
	    }
	    addFollowed( recognitionSetValueList, publicUserId, countBean.getFollowersTabCount() );
	    addMe( recognitionSetValueList, countBean.getMineTabCount() );
	    addCountry( recognitionSetValueList, countBean.getCountryTabCount() );
	    addDepartment( recognitionSetValueList, countBean.getDepartmentTabCount() );
	   
    return recognitionSetValueList;
  }
  // Client customization for WIP #45685 ends
  private List<PublicRecognitionSet> getFollowedUserPublicRecognitionClaims( int pageSize, int pageNumber, Long publicUserId, List<PublicRecognitionFormattedValueBean> followedUserClaims )
  {
    List<PublicRecognitionSet> recognitionSetValueList = new ArrayList<PublicRecognitionSet>();
    long countFollowers = getParticipantDAO().getFollowersCountByUserId( publicUserId );

    PublicRecognitionSetCountBean countBean = new PublicRecognitionSetCountBean();

    if ( !followedUserClaims.isEmpty() )
    {
      countBean.setFollowersTabCount( pageSize * pageNumber + 2 );
    }
    PublicRecognitionTabType tab = PublicRecognitionTabType.lookup( PublicRecognitionTabType.FOLLOWED_TAB );
    PublicRecognitionSet recognitionSetvalue = createRecognitionSet( tab, countBean.getFollowersTabCount() );

    populatePublicRecognitionSetForClaims( followedUserClaims, publicUserId, recognitionSetvalue );
    if ( getDefaultTab().equals( PublicRecognitionTabType.FOLLOWED_TAB ) )
    {
      recognitionSetvalue.setIsDefault( true );
    }

    if ( countFollowers > 0 )
    {
      recognitionSetvalue.setHasFollowees( true );
    }
    else
    {
      recognitionSetvalue.setHasFollowees( false );
    }
    addRecommended( recognitionSetValueList, countBean.getRecommendedTabCount() );
    addGlobal( recognitionSetValueList, countBean.getGlobalTabCount() );
 // Client customization for WIP #45685 starts
    addDivision( recognitionSetValueList, countBean.getDivisionTabCount() );
    // Client customization for WIP #45685 ends
    addTeam( recognitionSetValueList, countBean.getTeamTabCount() );

    if ( Arrays.asList( getActiveTabList() ).contains( PublicRecognitionTabType.FOLLOWED_TAB ) )
    {
      recognitionSetValueList.add( recognitionSetvalue );
    }

    addMe( recognitionSetValueList, countBean.getMineTabCount() );
    addCountry( recognitionSetValueList, countBean.getCountryTabCount() );
    addDepartment( recognitionSetValueList, countBean.getDepartmentTabCount() );

    return recognitionSetValueList;
  }

  private List<PublicRecognitionSet> getUserPublicRecognitionClaims( int pageSize, int pageNumber, Long publicUserId, List<PublicRecognitionFormattedValueBean> userClaims )
  {
    List<PublicRecognitionSet> recognitionSetValueList = new ArrayList<PublicRecognitionSet>();

    Long userId = publicUserId;
    if ( userId == null )
    {
      userId = UserManager.getUserId();
    }

    PublicRecognitionSetCountBean countBean = new PublicRecognitionSetCountBean();

    if ( !userClaims.isEmpty() )
    {
      countBean.setMineTabCount( pageSize * pageNumber + 2 );
    }

    PublicRecognitionTabType tab = PublicRecognitionTabType.lookup( PublicRecognitionTabType.ME_TAB );
    PublicRecognitionSet recognitionSetvalue = createRecognitionSet( tab, countBean.getMineTabCount() );

    recognitionSetvalue.setClaims( userClaims );

    String companyName = systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();

    for ( PublicRecognitionFormattedValueBean bean : userClaims )
    {
      bean.setIsLiked( isUserLiked( userId, bean.getUserLikes() ) );
      bean.setIsPublicClaim( true );

      String isMine = isMine( userId, bean );
      if ( "true".equals( isMine ) )
      {
        String eCardImageUrl = getEcardImageUrlForFBPost( bean );
        String formattedComments = HtmlUtils.removeFormatting( bean.getSubmitterComments() );
        bean.setSocialLinks( getSocialLinks( bean.getRecipientFirstName() + " was recognized in the " + companyName + " " + bean.getPromotionName() + " program.",
                                             "Here is what was said:<center></center>" + formattedComments,
                                             bean.getPurlUrl(),
                                             eCardImageUrl ) );

        // adding the eCard image URL
        bean.seteCardImageUrl( eCardImageUrl );

      }
      bean.setIsMine( isMine );
    }
    if ( getDefaultTab().equals( tab.getCode() ) )
    {
      recognitionSetvalue.setIsDefault( true );
    }

    recognitionSetValueList.add( recognitionSetvalue );
    addRecommended( recognitionSetValueList, countBean.getRecommendedTabCount() );
    addGlobal( recognitionSetValueList, countBean.getGlobalTabCount() );
    addDivision( recognitionSetValueList, countBean.getDivisionTabCount() );
    addTeam( recognitionSetValueList, countBean.getTeamTabCount() );   
    addFollowed( recognitionSetValueList, userId, countBean.getFollowersTabCount() );
    addCountry( recognitionSetValueList, countBean.getCountryTabCount() );
    addDepartment( recognitionSetValueList, countBean.getDepartmentTabCount() );
    if ( Arrays.asList( getActiveTabList() ).contains( tab.getCode() ) )
    {
      recognitionSetValueList.add( recognitionSetvalue );
    }
    return recognitionSetValueList;
  }

  private void populatePublicRecognitionSetForClaims( List<PublicRecognitionFormattedValueBean> claims, Long participantId, PublicRecognitionSet recognitionSetvalue )
  {
    recognitionSetvalue.setClaims( claims );

    String companyName = systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();
    Participant currentParticipant = this.participantService.getParticipantById( participantId );

    for ( PublicRecognitionFormattedValueBean bean : claims )
    {
      String eCardImageUrl = null;
      String isUserLiked = null;
      eCardImageUrl = getEcardImageUrlForFBPost( bean );
      // adding the eCard image URL
      bean.seteCardImageUrl( eCardImageUrl );

      isUserLiked = isUserLiked( participantId, bean.getUserLikes() );
      bean.setIsLiked( isUserLiked );

      String isMine = isMine( participantId, bean );
      if ( "true".equals( isMine ) )
      {
        String formattedComments = HtmlUtils.removeFormatting( bean.getSubmitterComments() );
        bean.setSocialLinks( getSocialLinks( bean.getRecipientFirstName() + " was recognized in the " + companyName + " " + bean.getPromotionName() + " program.",
                                             "Here is what was said:<center></center>" + formattedComments,
                                             bean.getPurlUrl(),
                                             eCardImageUrl ) );
      }
      Map<Long, RecognitionPromotion> recognitionPromotionMap = new HashMap<Long, RecognitionPromotion>();
      Map<Long, NominationPromotion> nominationPromotionMap = new HashMap<Long, NominationPromotion>();
      if ( bean.isRecognitionClaim() )
      {
        if ( !recognitionPromotionMap.containsKey( bean.getPromotionId() ) )
        {
          RecognitionPromotion recognitionPromotion = (RecognitionPromotion)promotionService.getPromotionById( bean.getPromotionId() );
          recognitionPromotionMap.put( bean.getPromotionId(), recognitionPromotion );

        }
      }
      else if ( bean.isNominationClaim() )
      {
        if ( !nominationPromotionMap.containsKey( bean.getPromotionId() ) )
        {
          NominationPromotion nominationPromotion = (NominationPromotion)promotionService.getPromotionById( bean.getPromotionId() );
          nominationPromotionMap.put( bean.getPromotionId(), nominationPromotion );

        }

        List<ClaimInfoBean> claimInfoBeans = this.claimService.getActivityTimePeriod( bean.getRecipients().iterator().next().getClaimId() );
        if ( CollectionUtils.isNotEmpty( claimInfoBeans ) )
        {
          ClaimInfoBean claimInfoBean = claimInfoBeans.get( 0 );
          bean.setTimePeriodId( claimInfoBean.getTimePeriodId() );
          bean.setActivityId( claimInfoBean.getActivityId() );
          bean.setRecipientId( claimInfoBean.getProxyUserId() );
        }
      }

      if ( bean.getReceiverCount() == 1 )
      {
        bean = buildAddPointsSection( bean, currentParticipant, currentParticipant, recognitionPromotionMap, false );
      }
      bean.setIsMine( isMine );
      bean.setIsPublicClaim( true );
    }
  }

  protected String isMine( Long participantId, PublicRecognitionFormattedValueBean bean )
  {
    String isMine = "false";
    List<PublicRecognitionParticipantView> recipients = bean.getRecipients();
    if ( recipients != null )
    {
      for ( PublicRecognitionParticipantView recipient : recipients )
      {
        if ( recipient.getId().equals( participantId ) )
        {
          isMine = "true";
          break;
        }
      }
    }
    else if ( bean.getRecipientId() != null && bean.getRecipientId().equals( participantId ) )
    {
      isMine = "true";
    }
    return isMine;
  }

  private PublicRecognitionSet createRecognitionSet( PublicRecognitionTabType tab, long count )
  {
    PublicRecognitionSet recognitionSetvalue = new PublicRecognitionSet( tab.getCode(), tab.getName(), tab.getDescription(), count );
    return recognitionSetvalue;
  }

  private void addMe( List<PublicRecognitionSet> recognitionSetValueList, long count )
  {
    PublicRecognitionTabType tab = PublicRecognitionTabType.lookup( PublicRecognitionTabType.ME_TAB );
    if ( Arrays.asList( getActiveTabList() ).contains( PublicRecognitionTabType.ME_TAB ) )
    {
      recognitionSetValueList.add( createRecognitionSet( tab, count ) );
    }
  }

  private void addTeam( List<PublicRecognitionSet> recognitionSetValueList, long count )
  {
    PublicRecognitionTabType tab = PublicRecognitionTabType.lookup( PublicRecognitionTabType.TEAM_TAB );
    if ( Arrays.asList( getActiveTabList() ).contains( PublicRecognitionTabType.TEAM_TAB ) )
    {
      recognitionSetValueList.add( createRecognitionSet( tab, count ) );
    }
  }

  private void addGlobal( List<PublicRecognitionSet> recognitionSetValueList, long count )
  {
    PublicRecognitionTabType tab = PublicRecognitionTabType.lookup( PublicRecognitionTabType.GLOBAL_TAB );
    if ( Arrays.asList( getActiveTabList() ).contains( PublicRecognitionTabType.GLOBAL_TAB ) )
    {
      recognitionSetValueList.add( createRecognitionSet( tab, count ) );
    }
  }

  private void addCountry( List<PublicRecognitionSet> recognitionSetValueList, long count )
  {
    PublicRecognitionTabType tab = PublicRecognitionTabType.lookup( PublicRecognitionTabType.COUNRTY_TAB );
    if ( Arrays.asList( getActiveTabList() ).contains( PublicRecognitionTabType.COUNRTY_TAB ) )
    {
      recognitionSetValueList.add( createRecognitionSet( tab, count ) );
    }
  }

  private void addDepartment( List<PublicRecognitionSet> recognitionSetValueList, long count )
  {
    PublicRecognitionTabType tab = PublicRecognitionTabType.lookup( PublicRecognitionTabType.DEPARTMENT_TAB );
    if ( Arrays.asList( getActiveTabList() ).contains( PublicRecognitionTabType.DEPARTMENT_TAB ) )
    {
      recognitionSetValueList.add( createRecognitionSet( tab, count ) );
    }
  }

  private void addRecommended( List<PublicRecognitionSet> recognitionSetValueList, long count )
  {
    PublicRecognitionTabType tab = PublicRecognitionTabType.lookup( PublicRecognitionTabType.RECOMMENDED_TAB );
    if ( Arrays.asList( getActiveTabList() ).contains( PublicRecognitionTabType.RECOMMENDED_TAB ) )
    {
      recognitionSetValueList.add( createRecognitionSet( tab, count ) );
    }
  }
 
  private void addFollowed( List<PublicRecognitionSet> recognitionSetValueList, Long participantId, long count )
  {
    PublicRecognitionTabType tab = PublicRecognitionTabType.lookup( PublicRecognitionTabType.FOLLOWED_TAB );
    if ( Arrays.asList( getActiveTabList() ).contains( PublicRecognitionTabType.FOLLOWED_TAB ) )
    {
      PublicRecognitionSet recognitionSet = createRecognitionSet( tab, count );
      recognitionSet.setHasFollowees( getHasFolloweesResult( participantId ) );
      recognitionSetValueList.add( recognitionSet );
    }
  }
//Client customization for WIP #45685 starts 
  private void addDivision( List<PublicRecognitionSet> recognitionSetValueList, long count )
  {
    PublicRecognitionTabType tab = PublicRecognitionTabType.lookup(PublicRecognitionTabType.DIVISION_TAB);
    if(Arrays.asList(getActiveTabList()).contains(PublicRecognitionTabType.DIVISION_TAB))
    {
        recognitionSetValueList.add( createRecognitionSet(tab, count) );
    }
  }
//Client customization for WIP #45685 ends  
  
  public void setFacebookService( FacebookService facebookService )
  {
    this.facebookService = facebookService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public User getUserById( Long id )
  {
    return userDAO.getUserById( id );
  }

  public UserDAO getUserDAO()
  {
    return userDAO;
  }

  public void setUserDAO( UserDAO userDAO )
  {
    this.userDAO = userDAO;
  }

  public void setClaimService( ClaimService claimService )
  {
    this.claimService = claimService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  // Facebook Feed Url for public recognition post
  private String getFacebookFeedDialogUrlForPRPost( Long recipientId, boolean isMobile, String message, String purlUrl, String eCardImageUrl )
  {
    String DIALOG_URL_FORMAT = "http://{0}.facebook.com/dialog/feed" + "?app_id={1}" + "&redirect_uri={2}" + "&caption={3}";

    StringBuilder sb = new StringBuilder( DIALOG_URL_FORMAT );
    String trimmedMessage = StringUtils.isEmpty( message ) ? message : message.length() > 420 ? message.substring( 0, 415 ) + " ... " : message;
    if ( !StringUtils.isEmpty( eCardImageUrl ) )
    {
      sb.append( "&picture={4}" );
      sb.append( "&name={5}" );
    }
    else
    {
      sb.append( "&name={5}" );
      sb.append( "&link={6}" );
    }

    final MessageFormat formatter = new MessageFormat( sb.toString() );

    String[] parameters = new String[8];
    try
    {
      // for reference go to http://developers.facebook.com/docs/reference/dialogs/feed/
      parameters[0] = "www";
      parameters[1] = facebookService.getAppId();
      parameters[2] = URLEncoder.encode( "http://facebook.com", "UTF-8" );
      parameters[3] = trimmedMessage;

      if ( !StringUtils.isEmpty( eCardImageUrl ) )
      {
        parameters[4] = URLEncoder.encode( eCardImageUrl, "UTF-8" );
        parameters[5] = "My Recognition";
      }
      else
      {
        if ( !StringUtils.isEmpty( purlUrl ) )
        {
          parameters[5] = ContentReaderManager.getText( "purl.common", "FACEBOOK_LINK_NAME" );
          parameters[6] = URLEncoder.encode( purlUrl, "UTF-8" );
        }
        else
        {
          parameters[5] = "My Recognition";
          parameters[6] = URLEncoder.encode( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(), "UTF-8" );
        }
      }
    }
    catch( Throwable t )
    {
      // do nothing.. ?
    }

    String url = formatter.format( parameters );

    return url;
  }

  /**
   * Chatter Infor for post
   * @param purlUrl
   * @param eCardImageUrl
   * @param formattedMesg
   * @return
   */
  private String getChatterFeedDialogForPost( String purlUrl, String eCardImageUrl, String formattedMesg )
  {
    // Chatter call is failing if state is more than 1530 characters
    if ( formattedMesg.length() > 1000 )
    {
      formattedMesg = formattedMesg.substring( 0, 1000 ) + "...";
    }
    if ( !StringUtils.isEmpty( eCardImageUrl ) )
    {
      formattedMesg = formattedMesg + "||" + eCardImageUrl + "||My Recognition";
    }

    if ( purlUrl != null )
    {
      formattedMesg = formattedMesg + "||" + purlUrl + "||" + ContentReaderManager.getText( "purl.common", "FACEBOOK_LINK_NAME" );
    }
    String systemUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    String chatterUrl = systemUrl + "/participant/chatterAuthorizationSubmit.do?method=getChatterAuthorizationCode&state=" + formattedMesg;
    return chatterUrl;
  }

  /**
   *  Twitter Info
   * @param recipientId
   * @param isMobile
   * @param message
   * @return List<PublicRecognitionShareLinkBean>
   * @throws ServiceErrorException
   */
  private String getTwitterFeedDialogUrlForPRPost( Long recipientId, boolean isMobile, String message, String purlUrl )
  {
    String twitterShareUrl;
    String[] parameters = new String[2];

    try
    {
      String trimmedMessage = StringUtils.isEmpty( message ) ? message : message.length() > 140 ? message.substring( 0, 135 ) + " ... " : message;
      if ( !StringUtils.isEmpty( purlUrl ) )
      {
        twitterShareUrl = "http://twitter.com/share?url={0}&text={1}";
        parameters[0] = URLEncoder.encode( purlUrl, "UTF-8" );
        parameters[1] = URIUtil.encodeQuery( trimmedMessage );
        return new MessageFormat( twitterShareUrl ).format( parameters );
      }
      else
      {
        twitterShareUrl = "http://twitter.com/share?text={0}";
        parameters[0] = URLEncoder.encode( trimmedMessage, "UTF-8" );
        return new MessageFormat( twitterShareUrl ).format( parameters );
      }
    }
    catch( Throwable t )
    {
      // do nothing.... ?
    }
    return null;
  }

  /**
   * Linkedin Info    
   * @param recipientId
   * @param isMobile
   * @param message1
   * @param message2
   * @param purlUrl
   * @return List<PublicRecognitionShareLinkBean>
   */
  private String getLinkedinFeedDialogUrlForPRPost( Long recipientId, boolean isMobile, String message1, String message2, String purlUrl )
  {

    final String LINKEDIN_SHARE_URL = "http://www.linkedin.com/shareArticle?mini={0}&url={1}&title={2}&summary={3}";
    final MessageFormat formatter = new MessageFormat( LINKEDIN_SHARE_URL );
    String[] parameters = new String[5];
    try
    {
      if ( !StringUtils.isEmpty( purlUrl ) )
      {
        parameters[0] = "true";
        parameters[1] = purlUrl;
        parameters[2] = URIUtil.encodeQuery( ContentReaderManager.getText( "purl.common", "LINKED_IN_LINK_NAME" ) );
        parameters[3] = URLEncoder.encode( message1, "UTF-8" );
      }
      else
      {
        parameters[0] = "true";
        parameters[1] = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
        parameters[2] = URLEncoder.encode( message1, "UTF-8" );
        parameters[3] = URLEncoder.encode( message2, "UTF-8" );
      }
    }
    catch( Throwable t )
    {
      // do nothing.. ?
    }
    String url = formatter.format( parameters );

    return url;
  }

  /**
   * Get social links
   * 
   * @param message1
   * @return List<PublicRecognitionShareLinkBean>
   */
  private List<PublicRecognitionShareLinkBean> getSocialLinks( String message1, String message2, String purlUrl, String eCardImageUrl )
  {
    List<PublicRecognitionShareLinkBean> shareLinkBeanList = new ArrayList<PublicRecognitionShareLinkBean>();

    String message = null;
    if ( purlUrl != null )
    {
      // For PURL recognition only message 1 should be displayed.
      // Bug 50531
      message = message1;
    }
    else
    {
      message = message1 + message2;
    }

    if ( isPublicRecognitionFacebookAllowed() )
    {
      shareLinkBeanList.add( new PublicRecognitionShareLinkBean( "facebook", "Facebook", getFacebookFeedDialogUrlForPRPost( UserManager.getUserId(), false, message, purlUrl, eCardImageUrl ) ) );
    }

    String formattedMesg1 = message1.replace( "<center></center>", "" );
    String formattedMesg2 = message2.replace( "<center></center>", "" );

    if ( isPublicRecognitionLinkedInAllowed() )
    {
      shareLinkBeanList
          .add( new PublicRecognitionShareLinkBean( "linkedin", "Linkedin", getLinkedinFeedDialogUrlForPRPost( UserManager.getUserId(), false, formattedMesg1, formattedMesg2, purlUrl ) ) );
    }

    String formattedMesg = message.replace( "<center></center>", "" );
    if ( isPublicRecognitionTwitterAllowed() )
    {
      shareLinkBeanList.add( new PublicRecognitionShareLinkBean( "twitter", "Twitter", getTwitterFeedDialogUrlForPRPost( UserManager.getUserId(), false, formattedMesg, purlUrl ) ) );
    }

    if ( isPublicRecognitionChatterAllowed() )
    {
      shareLinkBeanList.add( new PublicRecognitionShareLinkBean( "chatter", "Chatter", getChatterFeedDialogForPost( purlUrl, eCardImageUrl, formattedMesg ) ) );
    }
    return shareLinkBeanList;
  }

  // This method will check if user is liked a particular claim, user can be session user or public
  // user
  private String isUserLiked( Long userId, List<PublicRecognitionLike> likes )
  {
    String currentUserliked = "false";
    if ( likes != null )
    {
      Long sessionUserId = UserManager.getUserId();
      if ( sessionUserId.equals( userId ) )
      {
        for ( PublicRecognitionLike prl : likes )
        {
          if ( userId.equals( prl.getUser().getId() ) )
          {
            currentUserliked = "true";
            break;
          }
        }
      }
      else
      {
        for ( PublicRecognitionLike prl : likes )
        {
          if ( sessionUserId.equals( prl.getUser().getId() ) )
          {
            currentUserliked = "true";
            break;
          }
        }
      }
    }
    return currentUserliked;
  }

  private static Comparator<PublicRecognitionComment> SORT_COMPARATOR = new Comparator<PublicRecognitionComment>()
  {
    public int compare( PublicRecognitionComment prc1, PublicRecognitionComment prc2 )
    {
      return prc1.getAuditCreateInfo().getDateCreated().compareTo( prc2.getAuditCreateInfo().getDateCreated() );
    }
  };

  /**
   * Get All Followers for Participant
   * @param participantId
   * @return List<Participant>
   */
  public List<Participant> getParticipantsIAmFollowing( Long participantId )
  {
    return getParticipantDAO().getParticipantsIAmFollowing( participantId );
  }

  /**
   * To get Active Tab List
   * @param 
   * @return String[]
   * @throws 
   */
  private String[] getActiveTabList()
  {
    PropertySetItem item = systemVariableService.getPropertyByName( SystemVariableService.PUBLIC_RECOG_ACTIVE_TABS );
    if ( item != null )
    {
      return item.getStringVal().split( "," );
    }

    return new String[0];
  }

  /**
   * To get Default Tab
   * @param 
   * @return String
   * @throws 
   */
  private String getDefaultTab()
  {
    String tabType = systemVariableService.getPropertyByName( SystemVariableService.PUBLIC_RECOG_DEFAULT_TAB_NAME ).getStringVal();
    return tabType;
  }

  /**
   * To check loggedin user has Followees
   * @param userId
   * @return boolean
   * @throws 
   */
  private boolean getHasFolloweesResult( long userId )
  {
    return getParticipantDAO().getFollowersCountByUserId( userId ) > 0;
  }

  /**
   * Update the given claim to be hidden from public recognition.  Will also hide any team claims associated with it.
   * @param claimId
   */
  @Override
  public void hidePublicRecognition( Long claimId )
  {
    List<AbstractRecognitionClaim> teamClaims = claimDAO.getTeamClaimsByClaimId( claimId );
    for ( AbstractRecognitionClaim claim : teamClaims )
    {
      Boolean idExists = false;

      idExists = claim.getClaimRecipients().stream().filter( t -> t.getRecipient().getId().equals( UserManager.getUserId() ) ).findAny().isPresent();
      if ( claim.getSubmitter().getId().equals( UserManager.getUserId() ) || idExists )
      {
        claim.setHidePublicRecognition( true );
        claimDAO.saveClaim( claim );
      }

    }
  }

  private BigDecimal getBudgetConversionRatio( Long receiverId, Long submitterId )
  {
    BigDecimal conversionRatio = BigDecimal.ONE;

    if ( receiverId != null && submitterId != null )
    {
      BigDecimal recipientMediaValue = userService.getBudgetMediaValueByUserId( receiverId );
      BigDecimal submitterMediaValue = countryService.getBudgetMediaValueByCountryCode( UserManager.getUser().getPrimaryCountryCode() );// userService.getBudgetMediaValueForUser(
                                                                                                                                        // submitterId
                                                                                                                                        // );
      conversionRatio = BudgetUtils.calculateConversionRatio( recipientMediaValue, submitterMediaValue );
    }
    return conversionRatio;
  }

  /**
   * @param claim
   * @param claimFormStepId
   * @param approverUser
   * @param forceAutoApprove
   * @param deductBudget
   * @return
   * @throws ServiceErrorException
   */
  @SuppressWarnings( "unchecked" )
  public List<PublicRecognitionComment> savePublicRecognitionClaim( Claim claim,
                                                                    Long claimFormStepId,
                                                                    User approverUser,
                                                                    boolean forceAutoApprove,
                                                                    boolean deductBudget,
                                                                    Participant participant,
                                                                    String comment,
                                                                    Long points,
                                                                    RecognitionClaim recClaim )
      throws ServiceErrorException
  {
    RecognitionPromotion recognitionPromotion = (RecognitionPromotion)this.promotionService.getPromotionById( claim.getPromotion().getId() );

    boolean budgetAvailableToAddPoints = true;
    BudgetMaster budgetMaster = recognitionPromotion.getPublicRecogBudgetMaster();
    if ( budgetMaster != null )
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
      budgetMaster = budgetService.getBudgetMasterById( budgetMaster.getId(), associationRequestCollection );

      String paxTimeZoneId = userService.getUserTimeZoneByUserCountry( approverUser.getId() );
      BudgetSegment currentBudgetSegment = budgetMaster.getCurrentBudgetSegment( paxTimeZoneId );

      List<Budget> budgets = null;
      if ( currentBudgetSegment != null )
      {
        currentBudgetSegment = budgetService.getBudgetSegmentById( currentBudgetSegment.getId(), null );
        budgets = budgetService.getAllBudgetsBySegmentIdAndUserId( currentBudgetSegment.getId(), approverUser.getId() );
      }
      else
      {
        budgetAvailableToAddPoints = false;
      }
      ClaimRecipient claimRecipient = (ClaimRecipient) ( (AbstractRecognitionClaim)claim ).getClaimRecipients().iterator().next();
      BigDecimal convertedEnteredPoints = calculateBudgetEquivalence( BigDecimal.valueOf( points.intValue() ), claim.getSubmitter(), claimRecipient.getRecipient() );

      if ( budgets != null && budgets.size() > 0 )
      {
        for ( Budget budget : budgets )
        {
          // check for pax type budget
          if ( budgetMaster.isParticipantBudget() && budgetMaster.isActive() )
          {
            if ( approverUser.getId().equals( budget.getUser().getId() ) )
            {
              if ( convertedEnteredPoints.compareTo( budget.getCurrentValue() ) == 1 )
              {
                budgetAvailableToAddPoints = false;
              }
            }
          }
          // check for node type budget
          if ( budgetMaster.isNodeBudget() && budgetMaster.isActive() )
          {
            for ( Object obj : approverUser.getUserNodes() )
            {
              UserNode userNode = (UserNode)obj;
              if ( userNode.getNode().getId().equals( budget.getNode().getId() ) )
              {
                if ( convertedEnteredPoints.compareTo( budget.getCurrentValue() ) == 1 )
                {
                  budgetAvailableToAddPoints = false;
                }
              }
            }
          }
          // check for central budget
          if ( budgetMaster.isCentralBudget() && budgetMaster.isActive() )
          {
            if ( convertedEnteredPoints.compareTo( budget.getCurrentValue() ) == 1 )
            {
              budgetAvailableToAddPoints = false;
            }
          }
        }
      }
      else
      {
        budgetAvailableToAddPoints = false;
      }
    }

    if ( !budgetAvailableToAddPoints )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.BUDGET_INSUFFICIENT );
    }

    Approvable approvable = this.claimService.saveClaim( claim, claimFormStepId, approverUser, forceAutoApprove, deductBudget );

    // deduct from public recognition budget
    calculatePublicRecognitionBudget( approvable );

    // create comment object and save - don't need to get team claims because you can only add
    // points to individual claim
    PublicRecognitionComment publicRecognitionComment = new PublicRecognitionComment();
    publicRecognitionComment.setClaim( recClaim );
    publicRecognitionComment.setUser( participant );
    publicRecognitionComment.setTeamId( recClaim.getTeamId() );
    publicRecognitionComment.setComments( comment );
    publicRecognitionComment.setCommentsLanguageType( userService.getPreferredLanguageFor( participant.getId() ) );

    List<PublicRecognitionComment> commentsToSave = new ArrayList<PublicRecognitionComment>();
    commentsToSave.add( publicRecognitionComment );
    List<PublicRecognitionComment> savedComments = savePublicRecognitionComments( commentsToSave );

    sendClaimNotification( claim, participant, comment, points, recClaim, recognitionPromotion );

    return savedComments;
  }

  private void sendClaimNotification( Claim claim, Participant participant, String comment, Long points, RecognitionClaim recClaim, RecognitionPromotion recognitionPromotion )
  {
    if ( points != null && points > 0 && recognitionPromotion.isAllowPublicRecognitionPoints() )
    {
      Mailing mailing = new Mailing();
      Message message = null;

      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );
      claim = claimService.getClaimByIdWithAssociations( claim.getId(), associationRequestCollection );
      RecognitionClaim recoClaim = (RecognitionClaim)claim;

      ClaimRecipient claimRecipient = recoClaim.getClaimRecipients().iterator().next();

      Promotion promotion = recoClaim.getPromotion();
      if ( promotion.getPromotionNotifications().size() > 0 )
      {
        Iterator<PromotionNotification> notificationsIter = promotion.getPromotionNotifications().iterator();
        while ( notificationsIter.hasNext() )
        {
          PromotionNotification notification = notificationsIter.next();
          if ( notification.isPromotionNotificationType() )
          {
            PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notification;
            long messageId = promotionNotificationType.getNotificationMessageId();

            String notificationTypeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();

            if ( messageId > 0 && notificationTypeCode.equals( PromotionEmailNotificationType.PUBLIC_RECOGNITION_ADD_POINTS ) )
            {
              message = messageService.getMessageById( messageId );
              break;
            }
          }
        }
      }

      if ( message != null )
      {
        mailing.setGuid( GuidUtils.generateGuid() );
        mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
        mailing.setMessage( message );
        mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
        String name = participant.getLastName() + " ," + participant.getFirstName();
        mailing.setSender( name );

        Set<MailingRecipient> mailingRecipient = buildPublicRecognitionAddPointsMailing( claimRecipient, participant, comment, points, recClaim );

        mailing.addMailingRecipients( mailingRecipient );
        mailingService.submitMailing( mailing, null );
      }
    }
  }

  private Set<MailingRecipient> buildPublicRecognitionAddPointsMailing( ClaimRecipient claimRecipient, Participant participant, String comment, Long points, AbstractRecognitionClaim recClaim )
  {
    Set<MailingRecipient> mailingRecipients = new HashSet<MailingRecipient>();
    MailingRecipient mailingRecipient = new MailingRecipient();

    boolean copyManager = ( (RecognitionPromotion)recClaim.getPromotion() ).isCopyRecipientManager();

    mailingRecipient = buildPublicRecognitionAddPointsMailingRecipient( claimRecipient, participant, claimRecipient.getRecipient(), comment, points, recClaim, copyManager, false, true );
    mailingRecipients.add( mailingRecipient );

    if ( copyManager )
    {
      Set<MailingRecipient> copiedRecipients = getCopiedPublicRecognitionRecipients( claimRecipient, participant, comment, points, recClaim, copyManager );
      mailingRecipients.addAll( copiedRecipients );
    }

    return mailingRecipients;
  }

  private Set<MailingRecipient> getCopiedPublicRecognitionRecipients( ClaimRecipient claimRecipient,
                                                                      Participant participant,
                                                                      String comment,
                                                                      Long points,
                                                                      AbstractRecognitionClaim recClaim,
                                                                      boolean copyManager )
  {
    Set<MailingRecipient> recipients = new HashSet<MailingRecipient>();
    Participant senderParticipant = claimRecipient.getRecipient();
    Set<User> recipientsManagers = new HashSet<User>();

    try
    {
      Node node = nodeDAO.getNodeById( claimRecipient.getNode().getId() );
      @SuppressWarnings( "unchecked" )
      Set<User> managers = node.getNodeManagersForUser( senderParticipant );
      recipientsManagers.addAll( managers );

    }
    catch( Exception e )
    {
      //
    }

    for ( Iterator managersIter = recipientsManagers.iterator(); managersIter.hasNext(); )
    {
      User manager = (User)managersIter.next();
      if ( senderParticipant == null || !senderParticipant.equals( manager ) )
      {
        MailingRecipient managerRecipient = buildPublicRecognitionAddPointsMailingRecipient( claimRecipient, participant, manager, comment, points, recClaim, copyManager, true, false );
        recipients.add( managerRecipient );
      }
    }

    return recipients;
  }

  private MailingRecipient buildPublicRecognitionAddPointsMailingRecipient( ClaimRecipient claimRecipient,
                                                                            Participant participant,
                                                                            User userToSend,
                                                                            String comment,
                                                                            Long points,
                                                                            AbstractRecognitionClaim recClaim,
                                                                            boolean copyManager,
                                                                            boolean isManager,
                                                                            boolean isSelf )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    Map<String, String> dataMap = new HashMap<String, String>();

    Participant receiver = claimRecipient.getRecipient();

    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setUser( userToSend );
    if ( userToSend.getLanguageType() != null )
    {
      mailingRecipient.setLocale( userToSend.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : userToSend.getLanguageType().getCode() );
    }
    else
    {
      mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
    }

    Locale locale = CmsUtil.getLocale( mailingRecipient.getLocale() );
    ContentReader contentReader = ContentReaderManager.getContentReader();
    Content content = (Content)contentReader.getContent( "recognition.public.recognition.item", locale );
    Content content1 = (Content)contentReader.getContent( "recognition.detail", locale );
    String subjectLine = StringEscapeUtils.unescapeHtml4( (String)content.getContentDataMap().get( "RECIPIENT_MAIL_SUBJECT" ) );
    if ( !isSelf )
    {
      subjectLine = StringEscapeUtils.unescapeHtml4( (String)content1.getContentDataMap().get( "MAIL_SUBJECT1" ) ) + " " + receiver.getFirstName() + " " + receiver.getLastName() + " "
          + StringEscapeUtils.unescapeHtml4( (String)content.getContentDataMap().get( "MANAGER_MAIL_SUBJECT" ) );
    }

    dataMap.put( "subject", subjectLine );
    dataMap.put( "originalSender", recClaim.getSubmitter().getFirstName() + " " + recClaim.getSubmitter().getLastName() );
    dataMap.put( "recipientName", receiver.getFirstName() + " " + receiver.getLastName() );
    dataMap.put( "recipientFirstName", receiver.getFirstName() );
    dataMap.put( "currentGiver", participant.getFirstName() + " " + participant.getLastName() );
    dataMap.put( "originalComment", recClaim.getSubmitterComments() );
    dataMap.put( "giverComment", comment );
    dataMap.put( "programName", recClaim.getPromotion().getName() );
    dataMap.put( "companyName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    dataMap.put( "siteUrl", systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/participantProfilePage.do#tab/Statement" );

    String awardAmount = NumberFormatUtil.getUserLocaleBasedNumberFormat( points, LocaleUtils.getLocale( mailingRecipient.getLocale().toString() ) );
    dataMap.put( "awardAmount", String.valueOf( awardAmount ) );
    if ( copyManager )
    {
      dataMap.put( "copyManager", "TRUE" );
    }

    mailingRecipient.addMailingRecipientDataFromMap( dataMap );

    return mailingRecipient;
  }

  // This method is used to build the Add Points section in the public recognition page.
  private PublicRecognitionFormattedValueBean buildAddPointsSection( PublicRecognitionFormattedValueBean bean,
                                                                     User sessionUser,
                                                                     Participant currentParticipant,
                                                                     Map recognitionPromotionMap,
                                                                     boolean includeBudgets )
  {
    if ( bean.isRecognitionClaim() )
    {
      RecognitionPromotion recognitionPromotion = (RecognitionPromotion)recognitionPromotionMap.get( bean.getPromotionId() );

      if ( null != recognitionPromotion && recognitionPromotion.isAllowPublicRecognitionPoints() )
      {
        if ( audienceService.isParticipantInPublicRecognitionAudience( recognitionPromotion, currentParticipant ) )
        {
          // these values are available if the above check is true. So set them
          bean.setAwardAmountMin( recognitionPromotion.getPublicRecogAwardAmountMin() );
          bean.setAwardAmountMax( recognitionPromotion.getPublicRecogAwardAmountMax() );
          bean.setAwardAmountFixed( recognitionPromotion.isPublicRecogAwardAmountTypeFixed() );
          bean.setFixedAwardAmount( recognitionPromotion.getPublicRecogAwardAmountFixed() );
          if ( Double.compare( bean.getCountryRatio(), 0 ) == 0 )
          {
            bean.setCountryRatio( getBudgetConversionRatio( bean.getRecipientId(), UserManager.getUserId() ).doubleValue() );
          }

          // Add Points link will be available in the Public recognition if
          // bean.setAllowAddPoints(recognitionPromotion.isAllowPublicRecognitionPoints() ) set to
          // true
          if ( recognitionPromotion.getPublicRecogBudgetMaster() != null )
          {
            BudgetSegment currentBudgetSegment = getCurrentBudgetSegmentWithBudgets( recognitionPromotion.getPublicRecogBudgetMaster().getId(), sessionUser.getId() );

            if ( currentBudgetSegment != null )
            {
              // check for pax type budget
              if ( recognitionPromotion.getPublicRecogBudgetMaster().isParticipantBudget() && recognitionPromotion.getPublicRecogBudgetMaster().isActive() )
              {
                Budget budget = budgetService.getAvailableUserBudgetByBudgetSegmentAndUserId( currentBudgetSegment.getId(), sessionUser.getId() );
                if ( budget != null )
                {
                  budget.setBudgetSegment( currentBudgetSegment );
                  if ( includeBudgets )
                  {
                    bean = buildBudgetsSection( bean, budget, sessionUser.getId() );
                  }
                  bean = buildAllowPointsSection( bean, recognitionPromotion.getPublicRecogBudgetMaster(), currentBudgetSegment, null, null );
                }
                else
                {
                  bean.setAllowAddPoints( false );
                }
              }

              // check for node type budget
              if ( recognitionPromotion.getPublicRecogBudgetMaster().isNodeBudget() && recognitionPromotion.getPublicRecogBudgetMaster().isActive() )
              {
                for ( Object obj : sessionUser.getUserNodes() )
                {
                  UserNode userNode = (UserNode)obj;
                  if ( userNode.getHierarchyRoleType().isManager() || userNode.getHierarchyRoleType().isOwner() )
                  {
                    Budget budget = budgetService.getAvailableNodeBudgetByBudgetSegmentAndNodeId( currentBudgetSegment.getId(), userNode.getNode().getId() );
                    if ( budget != null )
                    {
                      budget.setBudgetSegment( currentBudgetSegment );
                      if ( includeBudgets )
                      {
                        bean = buildBudgetsSection( bean, budget, sessionUser.getId() );
                      }
                      bean = buildAllowPointsSection( bean, recognitionPromotion.getPublicRecogBudgetMaster(), currentBudgetSegment, userNode.getNode().getId(), null );
                    }
                    else
                    {
                      bean.setAllowAddPoints( false );
                    }
                    // break;
                  }
                  /* Bug #71300 - Removing add point link for pax with no budget */
                  else
                  {
                    bean.setAllowAddPoints( false );
                  }
                  /* Bug #71300 - Removing add point link for pax with no budget */
                }
              }

              // check for central type budget
              if ( recognitionPromotion.getPublicRecogBudgetMaster().isCentralBudget() && recognitionPromotion.getPublicRecogBudgetMaster().isActive() )
              {
                Budget budget = budgetService.getCentralBudgetByMasterIdAndSegmentId( recognitionPromotion.getPublicRecogBudgetMaster().getId(), currentBudgetSegment.getId() );
                if ( budget != null )
                {
                  budget.setBudgetSegment( currentBudgetSegment );
                  if ( includeBudgets )
                  {
                    bean = buildBudgetsSection( bean, budget, sessionUser.getId() );
                  }
                  bean = buildAllowPointsSection( bean, recognitionPromotion.getPublicRecogBudgetMaster(), currentBudgetSegment, null, budget.getId() );
                }
                else
                {
                  bean.setAllowAddPoints( false );
                }
              }
            }
            else
            {
              bean.setAllowAddPoints( false );
            }
          }
          else
          {
            // public recognition allow add points with no budget. Allow user to add points even
            // with no budget
            bean.setAllowAddPoints( recognitionPromotion.isAllowPublicRecognitionPoints() );
          }
        }
      }
      else
      {
        bean.setAllowAddPoints( false );
      }
    }
    return bean;
  }

  private BudgetSegment getCurrentBudgetSegmentWithBudgets( Long budgetMasterId, Long userId )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
    BudgetMaster budgetMaster = budgetService.getBudgetMasterById( budgetMasterId, associationRequestCollection );

    String paxTimeZoneId = userService.getUserTimeZoneByUserCountry( userId );
    BudgetSegment currentBudgetSegment = budgetMaster.getCurrentBudgetSegment( paxTimeZoneId );

    if ( currentBudgetSegment != null )
    {
      currentBudgetSegment = budgetService.getBudgetSegmentById( currentBudgetSegment.getId(), null );
    }
    return currentBudgetSegment;
  }

  private PublicRecognitionFormattedValueBean buildBudgetsSection( PublicRecognitionFormattedValueBean bean, Budget budget, Long userId )
  {
    final BigDecimal US_MEDIA_VALUE = countryService.getBudgetMediaValueByCountryCode( Country.UNITED_STATES );
    final BigDecimal USER_MEDIA_VALUE = userService.getBudgetMediaValueForUser( userId );

    BudgetMaster budgetMaster = budget.getBudgetSegment().getBudgetMaster();
    BudgetValueBean budgetValueBean = new BudgetValueBean();
    budgetValueBean.setId( budget.getId() );
    budgetValueBean.setName( budgetMaster.getBudgetName() );
    budgetValueBean.setRemaining( BudgetUtils.applyMediaConversion( budget.getCurrentValue(), US_MEDIA_VALUE, USER_MEDIA_VALUE ) );
    budgetValueBean.setOriginalValue( BudgetUtils.applyMediaConversion( budget.getOriginalValue(), US_MEDIA_VALUE, USER_MEDIA_VALUE ) );
    budgetValueBean.setHardCap( true );
    bean.getBudgets().add( budgetValueBean );
    return bean;
  }

  private PublicRecognitionFormattedValueBean buildAllowPointsSection( PublicRecognitionFormattedValueBean bean, BudgetMaster budgetMaster, BudgetSegment budgetSegment, Long nodeId, Long budgetId )
  {
    Budget budget = null;
    if ( budgetMaster.isNodeBudget() )
    {
      AssociationRequestCollection budgetAssociationRequestCollection = new AssociationRequestCollection();
      budgetAssociationRequestCollection.add( new BudgetToNodeOwnersAddressAssociationRequest() );
      budget = budgetService.getAvailableNodeBudgetByBudgetSegmentAndNodeId( budgetSegment.getId(), nodeId, budgetAssociationRequestCollection );
    }
    else if ( budgetMaster.isParticipantBudget() )
    {
      budget = budgetService.getAvailableUserBudgetByBudgetSegmentAndUserId( budgetSegment.getId(), UserManager.getUserId() );
    }

    else if ( budgetMaster.isCentralBudget() )
    {
      budget = budgetService.getBudget( budgetSegment.getId(), budgetId );
    }

    if ( budget != null && BudgetStatusType.ACTIVE.equals( budget.getStatus().getCode() ) && !budgetSegment.isExpired() && budget.getCurrentValue() != null
        && budget.getCurrentValue().compareTo( BigDecimal.ZERO ) > 0 )
    {
      bean.setAllowAddPoints( true );
    }
    else
    {
      bean.setAllowAddPoints( false );
    }

    return bean;
  }

  /**
   * To calculate the payout for public recognition budget
   * @param approvable
   */
  public void calculatePublicRecognitionBudget( Approvable approvable ) throws ServiceErrorException
  {
    RecognitionPromotion promotion = (RecognitionPromotion)approvable.getPromotion();

    Claim claim = (Claim)approvable;
    // If promotion doesn't use budget, no calculation is necessary.
    if ( promotion.isPublicRecBudgetUsed() )
    {
      List payoutCalculationAudits = payoutCalculationAuditService.getPayoutCalculationAuditsByClaimId( claim.getId() );

      // if no payout for this claim, no calculation is necessary.
      if ( payoutCalculationAudits.isEmpty() )
      {
        throw new ServiceErrorException( "payoutCalculationAudits set is empty" );
      }

      Budget budget = promotionService.getPublicRecognitionBudget( promotion.getId(), claim.getSubmitter().getId(), claim.getNode().getId() );
      if ( budget == null )
      {
        throw new ServiceErrorException( "No budget found for Claim " + claim.getClaimNumber() );
      }

      BigDecimal currentBudget = budget.getCurrentValue();
      BigDecimal totalPayout = BigDecimal.ZERO;
      List journalList = new ArrayList();
      for ( Iterator iter = payoutCalculationAudits.iterator(); iter.hasNext(); )
      {
        PayoutCalculationAudit payoutCalculationAudit = (PayoutCalculationAudit)iter.next();
        Journal journal = payoutCalculationAudit.getJournal();

        if ( journal != null )
        {
          // set budget on journal records as we go
          journal.setBudget( budget );
          if ( journal.getTransactionAmount() != null )
          {
            BigDecimal calculatedBudgetValue = calculateBudgetEquivalence( BigDecimal.valueOf( journal.getTransactionAmount() ), claim.getSubmitter(), journal.getParticipant() );
            totalPayout = totalPayout.add( calculatedBudgetValue );

            // Set the Budget Amount in Journal
            journal.setBudgetValue( calculatedBudgetValue );
          }
          journal = journalDAO.saveJournalEntry( journal );

          journalList.add( journal );
        }
      }
      budget.setReferenceVariableForClaimId( claim.getId() );

      // If the total payout falls within the budget, decrease the budget and mark budget on journal
      // Budget Internationalization - allow budget to go as low as -1 because of rounding
      // Fix for bug :54017
      // Removed +1 to allow the budget not to go below zero.
      if ( totalPayout.doubleValue() <= currentBudget.doubleValue() )
      {
        budget.setCurrentValue( currentBudget.subtract( totalPayout ) );
        budget.setActionType( BudgetActionType.lookup( BudgetActionType.DEDUCT ) );
      }

      else
      {
        // Shouldn't get here. UI Layer should prevent.
        // Might want to handle this gracefully if this is a situation that occurs often.
        // BugFix 18327 Add one service error for exceeding the Budget limits Of an Approver for Pax
        // Budget Type
        List serviceErrors = new ArrayList();
        serviceErrors.add( new ServiceError( ServiceErrorMessageKeys.HARD_CAP_BUDGET_EXCEEDED_RECOGNITION, String.valueOf( currentBudget ) ) );
        throw new ServiceErrorException( serviceErrors );
      }
    }
  }

  private BigDecimal calculateBudgetEquivalence( BigDecimal value, Participant submitter, Participant recipient )
  {
    UserAddress recipientAddress = userService.getPrimaryUserAddress( recipient.getId() );
    if ( recipientAddress != null )
    {
      BigDecimal usMediaValue = countryService.getBudgetMediaValueByCountryCode( Country.UNITED_STATES );
      BigDecimal recipientMediaValue = recipientAddress.getAddress().getCountry().getBudgetMediaValue();
      value = BudgetUtils.applyMediaConversion( value, recipientMediaValue, usMediaValue );
    }
    return value;
  }

  private String getEcardImageUrlForFBPost( PublicRecognitionFormattedValueBean bean )
  {
    String eCardImageUrl = null;
    if ( bean.iseCardUsed() != null && bean.iseCardUsed() && bean.getCard() != null )
    {
      if ( bean.getCard().getId() != null && bean.getCard().getId() > 0 )
      {
        if ( bean.getCard().getImgUrl() != null )
        {
          Participant user = participantService.getParticipantById( UserManager.getUserId() );
          Card card = multimediaService.getCardById( bean.getCard().getId() );
          ECard eCard = (ECard)card;

          // fetching the locale specific ecard name if ecard is translatable and recipient locale
          // is
          // not en_US
          if ( eCard.isTranslatable() && user.getLanguageType() != null && !Locale.US.equals( CmsUtil.getLocale( user.getLanguageType().getCode() ) ) )
          {
            eCardImageUrl = card.getLargeImageNameLocale( user.getLanguageType().getCode() );
          }
          else
          {
            eCardImageUrl = bean.getCard().getImgUrl();
          }
        }
        else
        {
          eCardImageUrl = null;
        }
      }
      else
      {
        eCardImageUrl = bean.getCard().getImgUrl();
      }
    }
    return eCardImageUrl;
  }

  public TranslatedContent getTranslatedPublicRecognitionCommentFor( Long publicRecognitionCommentId, Long userId ) throws UnsupportedTranslationException, UnexpectedTranslationException
  {
    PublicRecognitionComment prc = publicRecognitionDAO.getPublicRecognitionCommentBy( publicRecognitionCommentId );

    LanguageType userPreferredLanguage = userService.getPreferredLanguageFor( userId );

    TranslatedContent tc = translationService.translate( prc.getCommentsLanguageType(), userPreferredLanguage, prc.getComments() );

    return tc;
  }

  @Override
  public void deletePublicRecognitionComment( Long commentId, Long participantId ) throws ServiceErrorException
  {
    PublicRecognitionComment prc = publicRecognitionDAO.getPublicRecognitionCommentBy( commentId );
    List<PublicRecognitionComment> allCommentForTeam = publicRecognitionDAO.getUserCommentsByTeam( prc.getTeamId() );
    allCommentForTeam.forEach( ( teamCommentClaim ) ->
    {
      if ( teamCommentClaim.getUser().getId().equals( participantId ) && teamCommentClaim.getComments().equals( prc.getComments() ) && teamCommentClaim.getId().equals( prc.getId() ) )
      {
        // delete it!
        publicRecognitionDAO.deletePublicRecognitionComment( teamCommentClaim );
      }
    } );
  }

  private Boolean isPublicRecognitionFacebookAllowed()
  {
    return getBooleanPropertyValue( SystemVariableService.PUBLIC_RECOG_ALLOW_FACEBOOK );
  }

  private Boolean isPublicRecognitionTwitterAllowed()
  {
    return getBooleanPropertyValue( SystemVariableService.PUBLIC_RECOG_ALLOW_TWITTER );
  }

  private Boolean isPublicRecognitionLinkedInAllowed()
  {
    return getBooleanPropertyValue( SystemVariableService.PUBLIC_RECOG_ALLOW_LINKED_IN );
  }

  private Boolean isPublicRecognitionChatterAllowed()
  {
    return getBooleanPropertyValue( SystemVariableService.PUBLIC_RECOG_ALLOW_CHATTER );
  }

  private Boolean getBooleanPropertyValue( String propertyName )
  {
    PropertySetItem property = systemVariableService.getPropertyByName( propertyName );
    return property != null ? new Boolean( property.getBooleanVal() ) : Boolean.FALSE;
  }

  /**
  * @param promotionService value for promotionService property
  */
  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setAudienceService( AudienceService audienceService )
  {
    this.audienceService = audienceService;
  }

  /**
  * @param countryService value for countryService property
  */
  public void setCountryService( CountryService countryService )
  {
    this.countryService = countryService;
  }

  public void setPayoutCalculationAuditService( PayoutCalculationAuditService payoutCalculationAuditService )
  {
    this.payoutCalculationAuditService = payoutCalculationAuditService;
  }

  public void setJournalDAO( JournalDAO journalDAO )
  {
    this.journalDAO = journalDAO;
  }

  public void setPurlService( PurlService purlService )
  {
    this.purlService = purlService;
  }

  public void setBudgetService( BudgetMasterService budgetService )
  {
    this.budgetService = budgetService;
  }

  public void setTranslationService( TranslationService translationService )
  {
    this.translationService = translationService;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public void setMessageService( MessageService messageService )
  {
    this.messageService = messageService;
  }

  public void setNodeDAO( NodeDAO nodeDAO )
  {
    this.nodeDAO = nodeDAO;
  }

  @Override
  public List<PublicRecognitionSet> getDashboardRecognitionClaimsByType( Map<String, Object> queryParams, int pageSize ) throws ServiceErrorException
  {
    List<PublicRecognitionSet> recognitionSetValueList = null;
    List<PublicRecognitionFormattedValueBean> claims = getPublicRecognitionDAO().getDashboardRecognitionClaimsByType( queryParams );

    if ( claims != null )
    {
      // Passing user id from user manager instead of the request participant id because it is
      // incorrectly displaying the hide button when manager is drilling down to user level for RPM
      // promotion if we pass the request partipant id.
      recognitionSetValueList = getGlobalPublicRecognitionClaims( pageSize, (Integer)queryParams.get( PAGE_NUMBER ), UserManager.getUserId(), claims );
    }
    return recognitionSetValueList;
  }
  
  // Specific to SEA promotion only - This method sends recognition and add points separately (if
  // points needed to send) through public recognition to support zero points for SEA
  /* @author Ravi Kancherla */
  public List<Long> submitRecognitionWithPubRecAddPoints( Participant sender,
                                                          List<Participant> recipients,
                                                          RecognitionPromotion promotion,
                                                          String comments,
                                                          String behavior,
                                                          int points ) throws ServiceErrorException
  {
    List<Long> claimIds = new ArrayList<Long>();
    try
    {
      // adding authenticated user
      addAuthenticUser( sender );

      Long teamId = null;
      if ( recipients.size() >= 1 )
      {
        teamId = claimDAO.getNextTeamId();
      }

      for ( Participant participant : recipients )
      {
        RecognitionClaimSubmission submission = new RecognitionClaimSubmission( RecognitionClaimSource.EMAIL, sender.getId(), sender.getPrimaryUserNode().getNode().getId(), promotion.getId() );

        // Limiting recognition text to 4000
        if(comments.length() >= 4000)
        {
          submission.setComments( comments.substring( 0, 4000 ) );
        }
        else
        {
          submission.setComments( comments );
        }
        submission.setBehavior( behavior );
        submission.setCopyManager( promotion.isCopyRecipientManager() );
        submission.setRecipientSendDate( "" );
        submission.addRecognitionClaimRecipient( participant.getId(), participant.getPrimaryUserNode().getNode().getId(), Long.valueOf( points ), new Long( 0 ), participant.getPrimaryCountryCode());

        RecognitionClaimSubmissionResponse submissionResponse = savePublicRecognitionClaim( submission, Long.valueOf( points ), sender, false, false, teamId );
        claimIds.add( submissionResponse.getClaimId() );

      }
    }
    finally
    {
      UserManager.removeUser();
    }
    return claimIds;
  }

  private void addAuthenticUser( Participant sender )
  {
    AuthenticatedUser authUser = new AuthenticatedUser();
    authUser.setUserId( sender.getId() );
    authUser.setTimeZoneId( sender.getPrimaryAddress().getAddress().getCountry().getTimeZoneId().getCode() );
    UserManager.setUser( authUser );
  }
  
  @SuppressWarnings( "unchecked" )
  public RecognitionClaim buildRecognitionClaim( Long claimId, Long points, String comment, RecognitionClaimSource recognitionClaimSource ) throws ServiceErrorException
  {
    AssociationRequestCollection requestCollection = new AssociationRequestCollection();
    requestCollection.add( new UserAssociationRequest( UserAssociationRequest.NODE ) );
    User curUser = this.userService.getUserByIdWithAssociations( UserManager.getUser().getUserId(), requestCollection );

    AssociationRequestCollection claimAssociationRequestCollection = new AssociationRequestCollection();
    claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );

    AbstractRecognitionClaim abstractClaim = (AbstractRecognitionClaim)this.claimService.getClaimByIdWithAssociations( claimId, claimAssociationRequestCollection );

    AbstractRecognitionClaim newClaim = new RecognitionClaim();
    RecognitionClaim recognitionClaim = (RecognitionClaim)newClaim;

    String timeZoneID = UserManager.getUser().getTimeZoneId();
    Date referenceDate = DateUtils.applyTimeZone( new Date(), timeZoneID );

    Set<ClaimRecipient> savedClaimRecipients = abstractClaim.getClaimRecipients();
    ClaimRecipient saveClaimRecipient = new ArrayList<ClaimRecipient>( savedClaimRecipients ).get( 0 );
    Participant claimPax = this.participantService.getParticipantById( saveClaimRecipient.getRecipient().getId() );

    ClaimRecipient claimRecipient = new ClaimRecipient();
    claimRecipient.setRecipient( claimPax );

    if ( claimPax.getOptOutAwards() )
    {
      claimRecipient.setAwardQuantity( 0L );
    }
    else
    {
      claimRecipient.setAwardQuantity( points );
    }

    claimRecipient.setNode( saveClaimRecipient.getNode() );
    claimRecipient.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.APPROVED ) );

    recognitionClaim.addClaimRecipient( claimRecipient );
    recognitionClaim.setSubmitterComments( comment );
    recognitionClaim.setSubmitterCommentsLanguageType( this.userService.getPreferredLanguageFor( this.participantService.getParticipantById( curUser.getId() ) ) );
    recognitionClaim.setNode( (Node)curUser.getUserNodesAsNodes().get( 0 ) );
    recognitionClaim.setSubmitter( this.participantService.getParticipantById( curUser.getId() ) );
    recognitionClaim.setApprovalRound( new Long( 2 ) );
    recognitionClaim.setSubmissionDate( referenceDate );
    recognitionClaim.setPromotion( abstractClaim.getPromotion() );
    recognitionClaim.setHidePublicRecognition( true );
    recognitionClaim.setOpen( true );
    recognitionClaim.setCopyManager( true );
    recognitionClaim.setCopyOthers( false );
    recognitionClaim.setSendCopyToOthers( "" );
    recognitionClaim.setAddPointsClaim( true );
    recognitionClaim.setTeamId( this.claimService.getNextTeamId() );
    recognitionClaim.setSource( recognitionClaimSource );

    return recognitionClaim;
  }

  @Override
  public RecognitionClaimSubmissionResponse savePublicRecognitionClaim( RecognitionClaimSubmission submission,
                                                                        Long points,
                                                                        Participant participant,
                                                                        boolean forceAutoApprove,
                                                                        boolean deductBudget,
                                                                        Long teamId )
      throws ServiceErrorException
  {
    RecognitionClaimSubmissionResponse claimResponse = new RecognitionClaimSubmissionResponse();
    List<AbstractRecognitionClaim> claims = new ArrayList<AbstractRecognitionClaim>();

    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_MERCHANDISE_COUNTRIES ) );
    AbstractRecognitionPromotion promotion = (AbstractRecognitionPromotion)promotionService.getPromotionByIdWithAssociations( submission.getPromotionId(), promoAssociationRequestCollection );

    if ( points > 0 )
    {
      budgetAvailableToAddPoints( (User)participant, points, promotion );
    }

    for ( RecognitionClaimRecipient recognitionClaimRecipient : removeDuplicateRecipients( submission.getRecognitionClaimRecipients() ) )
    {
      AbstractRecognitionClaim newClaim = toClaimBasics( promotion, submission );
      newClaim.setTeamId( teamId );

      ClaimRecipient claimRecipient = buildIndividualClaimRecipient( promotion, recognitionClaimRecipient );
      newClaim.addClaimRecipient( claimRecipient );
      claims.add( newClaim );
    }

    if ( promotion.isRecognitionPromotion() && ! ( (RecognitionPromotion)promotion ).isIncludePurl() )
    {
      Approvable approvable = null;
      for ( AbstractRecognitionClaim abstractClaim : claims )
      {
        ClaimRecipient claimRecipient = (ClaimRecipient)abstractClaim.getClaimRecipients().iterator().next();
        String recipientTimeZoneID = userService.getUserTimeZoneByUserCountry( claimRecipient.getRecipient().getId() );
        Date recipientCurrentDate = DateUtils.applyTimeZone( new Date(), recipientTimeZoneID );
        Date recipientSendDate = null;
        Date sendDate = DateUtils.toDate( submission.getRecipientSendDate() );
        if ( sendDate != null )
        {
          recipientSendDate = DateUtils.applyTimeZoneWithFirstTimeOfDay( sendDate, recipientTimeZoneID );
        }

        approvable = claimService.saveClaim( abstractClaim, submission.getClaimFormStepId(), (User)participant, forceAutoApprove, deductBudget );
        sendClaimNotification( abstractClaim, participant, abstractClaim.getSubmitterComments(), points, (RecognitionClaim)abstractClaim, (RecognitionPromotion)promotion );
      }

      // deduct from public recognition budget
      if ( points > 0 )
      {
        calculatePublicRecognitionBudget( approvable );
      }

    }

    Iterator claimItr = claims.iterator();

    while ( claimItr.hasNext() )
    {
      AbstractRecognitionClaim abstractClaim = (AbstractRecognitionClaim)claimItr.next();
      claimResponse.setClaimId( abstractClaim.getId() );
    }

    return claimResponse;

  }

  private void budgetAvailableToAddPoints( User approverUser, Long points, AbstractRecognitionPromotion promotion ) throws ServiceErrorException
  {
    boolean budgetAvailableToAddPoints = true;
    BudgetMaster budgetMaster = ( (RecognitionPromotion)promotion ).getPublicRecogBudgetMaster();
    if ( budgetMaster != null )
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
      budgetMaster = budgetService.getBudgetMasterById( budgetMaster.getId(), associationRequestCollection );

      String paxTimeZoneId = userService.getUserTimeZoneByUserCountry( approverUser.getId() );
      BudgetSegment currentBudgetSegment = budgetMaster.getCurrentBudgetSegment( paxTimeZoneId );

      List<Budget> budgets = null;
      if ( currentBudgetSegment != null )
      {
        currentBudgetSegment = budgetService.getBudgetSegmentById( currentBudgetSegment.getId(), null );
        budgets = budgetService.getAllBudgetsBySegmentIdAndUserId( currentBudgetSegment.getId(), approverUser.getId() );
      }
      if ( budgets != null && budgets.size() > 0 )
      {
        for ( Budget budget : budgets )
        {
          // check for pax type budget
          if ( budgetMaster.isParticipantBudget() && budgetMaster.isActive() )
          {
            if ( approverUser.getId().equals( budget.getUser().getId() ) )
            {
              if ( points > budget.getCurrentValue().longValue() )
              {
                budgetAvailableToAddPoints = false;
              }
            }
          }
          // check for node type budget
          if ( budgetMaster.isNodeBudget() && budgetMaster.isActive() )
          {
            for ( Object obj : approverUser.getUserNodes() )
            {
              UserNode userNode = (UserNode)obj;
              if ( userNode.getNode().getId().equals( budget.getNode().getId() ) )
              {
                if ( points > budget.getCurrentValue().longValue() )
                {
                  budgetAvailableToAddPoints = false;
                }
              }
            }
          }
          // check for central budget
          if ( budgetMaster.isCentralBudget() && budgetMaster.isActive() )
          {
            if ( points > budget.getCurrentValue().longValue() )
            {
              budgetAvailableToAddPoints = false;
            }
          }
        }
      }
    }

    if ( !budgetAvailableToAddPoints )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.BUDGET_INSUFFICIENT );
    }
  }

  private List<RecognitionClaimRecipient> removeDuplicateRecipients( List<RecognitionClaimRecipient> recipients )
  {
    List<Long> recipientIds = new ArrayList<Long>();

    List<RecognitionClaimRecipient> validRecipients = new ArrayList<RecognitionClaimRecipient>();
    for ( RecognitionClaimRecipient recipient : recipients )
    {
      if ( recipientIds.contains( recipient.getUserId() ) )
      {
        // log it
        log.error( "\n\n**********************************************" + "\nWARNING: Duplicate recognition recipient detected: userId=" + recipient.getUserId()
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

  private ClaimRecipient buildIndividualClaimRecipient( AbstractRecognitionPromotion promotion, RecognitionClaimRecipient recognitionClaimRecipient )
  {
    ClaimRecipient claimRecipient = new ClaimRecipient();

    if ( recognitionClaimRecipient.getAwardQuantity() != null )
    {
      if ( claimRecipient.getRecipient().getOptOutAwards() )
      {
        claimRecipient.setAwardQuantity( 0L );
      }
      else
      {
        claimRecipient.setAwardQuantity( recognitionClaimRecipient.getAwardQuantity() );
      }
    }
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

    Long nodeId = recognitionClaimRecipient.getNodeId();
    if ( nodeId != null )
    {
      claimRecipient.setNode( nodeDAO.getNodeById( nodeId ) );
    }

    claimRecipient.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.PENDING ) );

    return claimRecipient;
  }

  
  public void setMultimediaService( MultimediaService multimediaService )
  {
    this.multimediaService = multimediaService;
  }

  public void setMerchLevelService( MerchLevelService merchLevelService )
  {
    this.merchLevelService = merchLevelService;
  }

  private ParticipantDAO getParticipantDAO()
  {
    return (ParticipantDAO)BeanLocator.getBean( ParticipantDAO.BEAN_NAME );
  }

}