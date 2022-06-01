
package com.biperf.core.ui.profile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.client.ClientProfileLike;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.gamification.BadgeDetails;
import com.biperf.core.domain.gamification.BadgeInfo;
import com.biperf.core.domain.gamification.BadgeLibrary;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.gamification.GamificationBadgeProfileView;
import com.biperf.core.domain.gamification.ParticipantBadge;
import com.biperf.core.domain.participant.AboutMe;
import com.biperf.core.domain.participant.BadgeView;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantSearchListView;
import com.biperf.core.domain.participant.ParticipantSearchView;
import com.biperf.core.domain.promotion.PublicRecognitionLike;
import com.biperf.core.domain.promotion.SmackTalkLike;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.client.CokeCareerMomentsService;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.ProfileService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.PublicRecognitionParticipantAssociationRequest;
import com.biperf.core.service.promotion.PublicRecognitionService;
import com.biperf.core.service.promotion.impl.PromotionServiceImpl;
import com.biperf.core.service.proxy.ProxyService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.throwdown.SmackTalkParticipantAssociationRequest;
import com.biperf.core.service.throwdown.SmackTalkService;
import com.biperf.core.service.throwdown.TeamService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.core.value.PromotionPaxValue;
import com.biperf.core.value.ThrowdownPlayerStatsBean;
import com.biperf.core.value.client.ClientAboutMeValueBean;
import com.biperf.core.value.client.Contributor;
import com.biperf.core.value.client.Iaminfo;
import com.biperf.core.value.client.Recipient;
import com.objectpartners.cms.util.CmsResourceBundle;

public class PublicProfileAction extends BaseDispatchAction
{

  /**
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws IOException
   * @throws ServletException
   * 
   * This method sometimes get participantId as parameter sometimes participantIds[] and sometimes recognitionId
   */
  public ActionForward populatePax( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
  {

    // get request parameters
    String[] participantIds = request.getParameterValues( "participantIds[]" );
    String recognitionId = request.getParameter( "recognitionId" );
    String smackTalkId = request.getParameter( "smackTalkId" );
    String participantId = request.getParameter( "participantId" );

    // build view objects
    ParticipantSearchListView paxsView = new ParticipantSearchListView();
    ParticipantSearchView paxView = new ParticipantSearchView();

    int count = 1;
    List<BadgeView> paxBadges = new ArrayList<BadgeView>();
    String siteUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    boolean showBadge = getSystemVariableService().getPropertyByName( SystemVariableService.INSTALL_BADGES ).getBooleanVal();
    paxsView.setAllowBadge( showBadge );

    if ( !StringUtil.isEmpty( participantId ) || participantIds != null && participantIds.length == 1 )
    {
      if ( StringUtil.isEmpty( participantId ) )
      {
        participantId = participantIds[0];
      }
      List<ParticipantSearchView> beans = getParticipantService().getParticipatForMiniProfile( Long.parseLong( participantId ) );
      for ( ParticipantSearchView bean : beans )
      {
        if ( count == 1 )
        {
          paxView = bean;
          Map<String, Long> paramMap = new HashMap<String, Long>();
          paramMap.put( "paxId", bean.getId() );
          if ( bean.isAllowPublicInformation() )
          {
            paxView.setProfileUrl( ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.PUBLIC_PROFILE_VIEW, paramMap ) );
          }
          paxView.setFollowed( bean.getFollowed() );
          count++;
        }
        if ( bean.isAllowPublicInformation() && showBadge )
        {
          List<BadgeView> badgeInformation = bean.getBadges();
          Iterator itrBadge = badgeInformation.iterator();

          while ( itrBadge.hasNext() )
          {
            BadgeView badgeView = (BadgeView)itrBadge.next();
            BadgeView modifedBadgeView = new BadgeView( badgeView.getId(),
                                                        CmsResourceBundle.getCmsBundle().getString( badgeView.getName(), BadgeRule.BADGE_RULES_CMASSET_TYPE_KEY ),
                                                        badgeView.getBadgeLibCmKey() );
            List imageUrls = getGamificationService().getEarnedNotEarnedImageList( badgeView.getBadgeLibCmKey() );
            Iterator itr = imageUrls.iterator();
            while ( itr.hasNext() )
            {
              BadgeLibrary badgeLib = (BadgeLibrary)itr.next();
              modifedBadgeView.setBadgeImageUrl( badgeLib.getEarnedImageSmall() );
            }
            paxBadges.add( modifedBadgeView );
          }
        }
      }
      paxView.setBadges( paxBadges );
      if ( paxView.getId() == UserManager.getUserId().longValue() )
      {
        paxView.setSelf( true );
        Map<String, Long> paramMap = new HashMap<String, Long>();
        paramMap.put( "paxId", paxView.getId() );
        if ( paxView.isAllowPublicInformation() )
        {
          paxView.setProfileUrl( ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.PUBLIC_PROFILE_VIEW, paramMap ) );
          // paxView.setProfileUrl( RequestUtils.getBaseURI( request ) +
          // PageConstants.PRIVATE_PROFILE_VIEW );
        }
      }
      List paxPromotions = getPromotionService().getAllLiveAndExpiredByTypeAndUserId( PromotionType.THROWDOWN, paxView.getId() );
      if ( paxPromotions != null && !paxPromotions.isEmpty() )
      {
        for ( Iterator iter = paxPromotions.iterator(); iter.hasNext(); )
        {
          PromotionPaxValue promoPax = (PromotionPaxValue)iter.next();
          if ( promoPax.getRoleKey().equals( PromotionServiceImpl.THROWDOWN_PRIMARY ) )
          {
            iter.remove();
          }
        }
      }
      if ( paxPromotions != null && !paxPromotions.isEmpty() )
      {
        paxView.setThrowdownEnabled( true );
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put( "paxId", paxView.getId() );
        paramMap.put( "defaultTab", "playerStats" );
        if ( paxView.isAllowPublicInformation() )
        {
          paxView.setPlayerStatsUrl( ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.PUBLIC_PROFILE_VIEW, paramMap ) );
        }
      }
      paxView.setDelegate( UserManager.getUser().isDelegate() );
      if ( UserManager.getUser().isDelegate() )
      {
        boolean canRecognize = false;
        canRecognize = getProxyService().isRecognitionAllowedForDelegate();
        paxView.setCanRecognize( canRecognize );
      }

      paxsView.setTotalCount( 1 );
      paxsView.getParticipants().add( paxView );
    }
    else
    {
      if ( !StringUtil.isEmpty( recognitionId ) )
      {
        String pageNumberString = request.getParameter( "pageNumber" );
        int pageNumber = 1;
        Long sessionUserId = UserManager.getUserId();
        if ( !StringUtil.isEmpty( pageNumberString ) )
        {
          pageNumber = Integer.parseInt( pageNumberString );
        }
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new PublicRecognitionParticipantAssociationRequest( PublicRecognitionParticipantAssociationRequest.EMPLOYEE ) );
        associationRequestCollection.add( new PublicRecognitionParticipantAssociationRequest( PublicRecognitionParticipantAssociationRequest.ADDRESS ) );
        List<PublicRecognitionLike> listOfLikedPax = getPublicRecognitionService().getLikedPaxListByClaimIdWithAssociations( Long.parseLong( recognitionId ),
                                                                                                                             associationRequestCollection,
                                                                                                                             pageNumber );
        int likedPaxCount = getPublicRecognitionService().getLikedPaxCount( Long.parseLong( recognitionId ) );
        for ( PublicRecognitionLike prl : listOfLikedPax )
        {
          if ( !sessionUserId.equals( prl.getUser().getId() ) )
          {
            Map<String, Long> paramMap = new HashMap<String, Long>();
            paramMap.put( "paxId", prl.getUser().getId() );
            paxsView.getParticipants().add( new ParticipantSearchView( prl.getUser(),
                                                                       ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.PUBLIC_PROFILE_VIEW, paramMap ),
                                                                       prl.getUser().getPrimaryCountryCode(),
                                                                       prl.getUser().getPrimaryCountryName() ) );
          }
        }
        paxsView.setTotalCount( likedPaxCount );
      }
      else if ( !StringUtil.isEmpty( smackTalkId ) )
      {
        String pageNumberString = request.getParameter( "pageNumber" );
        int pageNumber = 1;
        Long sessionUserId = UserManager.getUserId();
        if ( !StringUtil.isEmpty( pageNumberString ) )
        {
          pageNumber = Integer.parseInt( pageNumberString );
        }
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new SmackTalkParticipantAssociationRequest( SmackTalkParticipantAssociationRequest.EMPLOYEE ) );
        associationRequestCollection.add( new SmackTalkParticipantAssociationRequest( SmackTalkParticipantAssociationRequest.ADDRESS ) );
        List<SmackTalkLike> listOfLikedPax = getSmackTalkService().getLikedPaxListBySmackTalkId( Long.parseLong( smackTalkId ), associationRequestCollection, pageNumber );
        int likedPaxCount = getSmackTalkService().getLikedPaxCount( Long.parseLong( smackTalkId ) );
        for ( SmackTalkLike prl : listOfLikedPax )
        {
          if ( !sessionUserId.equals( prl.getUser().getId() ) )
          {
            Map<String, Long> paramMap = new HashMap<String, Long>();
            paramMap.put( "paxId", prl.getUser().getId() );
            paxsView.getParticipants().add( new ParticipantSearchView( prl.getUser(),
                                                                       ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.PUBLIC_PROFILE_VIEW, paramMap ),
                                                                       prl.getUser().getPrimaryCountryCode(),
                                                                       prl.getUser().getPrimaryCountryName() ) );
          }
        }
        paxsView.setTotalCount( likedPaxCount );
      }
      else
      {
        // converting array of pax ids string
        String paxIds = com.biperf.core.service.util.StringUtil.convertArrayToINQueryString( participantIds );

        // get list of pax for the given array of pax ids
        List<ParticipantSearchView> beans = getParticipantService().getParticipatForMiniProfile( paxIds );
        for ( ParticipantSearchView bean : beans )
        {
          paxView = bean;
          Map<String, Long> paramMap = new HashMap<String, Long>();
          paramMap.put( "paxId", bean.getId() );
          if ( bean.isAllowPublicInformation() )
          {
            paxView.setProfileUrl( ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.PUBLIC_PROFILE_VIEW_MORE_PAX, paramMap ) );
          }
          paxsView.getParticipants().add( paxView );
        }
        paxsView.setTotalCount( participantIds.length );
      }
    }
    super.writeAsJsonToResponse( paxsView, response );
    return null;
  }

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String forwardTo = ActionConstants.SHEET_VIEW;
    String siteUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();
      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      Long paxId = (Long)clientStateMap.get( "paxId" );

      String isFullPage = request.getParameter( "isFullPage" );
      String defaultTab = (String)clientStateMap.get( "defaultTab" );
      if ( isFullPage == null )
      {
        isFullPage = (String)clientStateMap.get( "isFullPage" );
      }
      if ( !StringUtil.isEmpty( isFullPage ) )
      {
        if ( Boolean.valueOf( isFullPage ) )
        {
          forwardTo = ActionConstants.FULL_VIEW;
        }
      }

      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new PublicRecognitionParticipantAssociationRequest( PublicRecognitionParticipantAssociationRequest.EMPLOYEE ) );
      associationRequestCollection.add( new PublicRecognitionParticipantAssociationRequest( PublicRecognitionParticipantAssociationRequest.ADDRESS ) );
      Participant pax = getParticipantService().getParticipantByIdWithAssociations( paxId, associationRequestCollection );
      boolean recognizeOther = UserManager.getUserId().equals( paxId ) ? false : true;
      request.setAttribute( "paxId", paxId );
      request.setAttribute( "pax", pax );

      List<AboutMe> aboutMeList = getProfileService().getAllAboutMeByUserId( paxId );
      List<AboutMe> filteredAboutMe = new ArrayList<AboutMe>();
      //Client customization start
      ClientAboutMeValueBean jsonBean = getAboutMeJson( paxId, aboutMeList );
      request.setAttribute( "jsonBean", super.toJson( jsonBean ) );
      //Client customization end
      if ( aboutMeList != null && aboutMeList.size() > 0 )
      {
        
        for ( Iterator<AboutMe> iterator = aboutMeList.iterator(); iterator.hasNext(); )
        {
          AboutMe aboutMe = iterator.next();
          if ( aboutMe.getAboutMeQuestionType() != null )
          {
            filteredAboutMe.add( aboutMe );
          }
        }
      }
      request.setAttribute( "aboutMe", filteredAboutMe );

      request.setAttribute( "deptName", pax.getPaxDeptName() != null ? pax.getPaxDeptName() : null );
      request.setAttribute( "positionName", pax.getPaxJobName() != null ? pax.getPaxJobName() : null );
      request.setAttribute( "recognizeOther", recognizeOther );
      if ( recognizeOther )
      {
        request.setAttribute( "followed", getParticipantService().isParticipantFollowed( paxId, UserManager.getUserId() ) );
      }
      request.setAttribute( "defaultTab", defaultTab );
      Long promotionId = request.getParameter( "promotionId" ) != null ? new Long( request.getParameter( "promotionId" ) ) : null;
      List paxPromotions = getPromotionService().getAllLiveAndExpiredByTypeAndUserId( PromotionType.THROWDOWN, paxId );
      Map<String, Long> paramMap = new HashMap<String, Long>();
      paramMap.put( "paxId", paxId );
      request.setAttribute( "profileUrl", ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.PUBLIC_PROFILE_VIEW, paramMap ) );
      if ( !paxPromotions.isEmpty() )
      {
        for ( Iterator iter = paxPromotions.iterator(); iter.hasNext(); )
        {
          PromotionPaxValue promoPax = (PromotionPaxValue)iter.next();
          if ( promoPax.getRoleKey().equals( PromotionServiceImpl.THROWDOWN_PRIMARY ) )
          {
            iter.remove();
          }
        }
      }

      PromotionMenuBean promoBean = reorderEligiblePromotions( request, promotionId );
      paxPromotions = reorderPaxPromotions( request, promoBean, paxPromotions );
      Date progressEndDate = null;
      if ( !paxPromotions.isEmpty() && promotionId == null )
      {
        PromotionPaxValue promoPax = (PromotionPaxValue)paxPromotions.get( 0 );
        ThrowdownPromotion promotion = (ThrowdownPromotion)promoPax.getPromotion();
        ThrowdownPlayerStatsBean playerStatsBean = getTeamService().getPlayerStats( paxId, promotion.getId() );

        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put( "matchId", playerStatsBean.getMatches().get( 0 ).getMatchId() );
        playerStatsBean.getMatches().get( 0 )
            .setMatchUrl( ClientStateUtils.generateEncodedLink( "", request.getServletContext().getContextPath() + "/throwdown/matchDetail.do?method=detail", parameterMap ) );
        progressEndDate = getTeamService().getLastFileLoadDateForPromotion( promotion.getId() );
        playerStatsBean.getMatches().get( 0 ).setAsOfDate( DateUtils.toDisplayString( progressEndDate ) );
        request.setAttribute( "playerStats", playerStatsBean );
      }
      else
      {
        if ( promotionId != null )
        {
          ThrowdownPlayerStatsBean playerStatsBean = getTeamService().getPlayerStats( paxId, promotionId );
          forwardTo = ActionConstants.THROWDOWN_VIEW;

          Map<String, Object> parameterMap = new HashMap<String, Object>();
          parameterMap.put( "matchId", playerStatsBean.getMatches().get( 0 ).getMatchId() );
          playerStatsBean.getMatches().get( 0 )
              .setMatchUrl( ClientStateUtils.generateEncodedLink( "", request.getServletContext().getContextPath() + "/throwdown/matchDetail.do?method=detail", parameterMap ) );
          progressEndDate = getTeamService().getLastFileLoadDateForPromotion( promotionId );
          playerStatsBean.getMatches().get( 0 ).setAsOfDate( DateUtils.toDisplayString( progressEndDate ) );
          request.setAttribute( "playerStats", playerStatsBean );
        }
      }
      request.setAttribute( "JstlDatePattern", DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );
    }
    catch( Exception e )
    {
      e.printStackTrace();
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    return mapping.findForward( forwardTo );

  }
  
  //Client customization start
  private ClientAboutMeValueBean getAboutMeJson(Long paxId, List<AboutMe> aboutMeList)
  {
    ClientAboutMeValueBean bean = new ClientAboutMeValueBean();
    
    Contributor contributor = new Contributor();
    Participant pax = null;
    contributor.setId( UserManager.getUserId() );

    pax = getParticipantService().getParticipantById( UserManager.getUserId() );

    contributor.setFirstName( UserManager.getUser().getFirstName() );
    contributor.setLastName( UserManager.getUser().getLastName() );
    contributor.setEmail( "" );
    contributor.setAvatarUrl( pax.getAvatarOriginal() );

    bean.setContributor( contributor );
    
    Recipient recipient = new Recipient();
    recipient.setId( paxId );
    bean.setRecipient( recipient );
    
    List<Iaminfo> iaminfo = new ArrayList<Iaminfo>();
    if(aboutMeList!=null && aboutMeList.size()>0)
    {
      Map<Long, Long> likesMap = getCokeCareerMomentsService().getAboutMeLikesByUserId( paxId, aboutMeList );
      Map<Long, Boolean> myLikesMap = getCokeCareerMomentsService().getMyAboutMeLikes( aboutMeList, pax );
      for(AboutMe aboutMe:aboutMeList)
      {
        Iaminfo iamInfo = new Iaminfo();
        iamInfo.setId( aboutMe.getId() );
        iamInfo.setIamComment( aboutMe.getAnswer() );
        iamInfo.setNumLikers(0L);
        if(Objects.nonNull( likesMap ))
        {
          iamInfo.setNumLikers( likesMap.get( aboutMe.getId() ) );
        }
        if(Objects.nonNull( myLikesMap ))
        {
          iamInfo.setLiked( myLikesMap.get( aboutMe.getId() ) );
        }
        
        iaminfo.add( iamInfo );
      }
    }
    bean.setIaminfo( iaminfo );
    
    return bean;
  }

  private PromotionMenuBean reorderEligiblePromotions( HttpServletRequest request, Long promotionId )
  {
    List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( request );
    if ( eligiblePromotions != null && !eligiblePromotions.isEmpty() )
    {
      if ( promotionId != null )
      {
        List<PromotionMenuBean> reorderedPromotions = new ArrayList<PromotionMenuBean>();
        List<PromotionMenuBean> otherPromotions = new ArrayList<PromotionMenuBean>();

        for ( PromotionMenuBean promotionBean : eligiblePromotions )
        {
          if ( promotionBean.getPromotion().getId().equals( promotionId ) )
          {
            reorderedPromotions.add( promotionBean );
          }
          else
          {
            otherPromotions.add( promotionBean );
          }
        }
        reorderedPromotions.addAll( otherPromotions );
        request.getSession().setAttribute( "eligiblePromotions", reorderedPromotions );
        return reorderedPromotions.get( 0 );
      }
      else
      {
        return eligiblePromotions.get( 0 );
      }
    }
    return null;
  }

  private List<PromotionPaxValue> reorderPaxPromotions( HttpServletRequest request, PromotionMenuBean promotionMenuBean, List<PromotionPaxValue> paxpromolist )
  {
    List<PromotionPaxValue> reorderedPromotions = new ArrayList<PromotionPaxValue>();
    List<PromotionPaxValue> otherPromotions = new ArrayList<PromotionPaxValue>();

    for ( PromotionPaxValue paxpromo : paxpromolist )
    {
      if ( paxpromo.getPromotion().getId().equals( promotionMenuBean != null ? promotionMenuBean.getPromotion().getId() : null ) )
      {
        reorderedPromotions.add( paxpromo );
      }
      else
      {
        otherPromotions.add( paxpromo );
      }
    }
    reorderedPromotions.addAll( otherPromotions );
    request.setAttribute( "promotions", reorderedPromotions );
    return reorderedPromotions;
  }

  public ActionForward fetchBadgesForPublicProfile( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String paxId = request.getParameter( "participantId" );
    List<ParticipantBadge> paxBadges = getGamificationService().getBadgeParticipantHistory( Long.parseLong( paxId ) );
    GamificationBadgeProfileView badgesView = new GamificationBadgeProfileView();
    int count = 0;
    for ( ParticipantBadge paxBadge : paxBadges )
    {
      BadgeDetails badgeDetail = new BadgeDetails();
      badgeDetail.setBadgeId( paxBadge.getId() );
      badgeDetail.setBadgeName( paxBadge.getBadgeRule().getBadgeNameTextFromCM() );
      badgeDetail.setBadgeType( paxBadge.getBadgePromotion().getBadgeType().getCode() );
      badgeDetail.setDateEarned( DateUtils.toDisplayString( paxBadge.getEarnedDate() ) );
      List earnedNotEarnedImageList = getGamificationService().getEarnedNotEarnedImageList( paxBadge.getBadgeRule().getBadgeLibraryCMKey() );
      Iterator itr = earnedNotEarnedImageList.iterator();
      while ( itr.hasNext() )
      {
        BadgeLibrary badgeLib = (BadgeLibrary)itr.next();
        badgeDetail.setImg( badgeLib.getEarnedImageSmall() );
        badgeDetail.setImgLarge( badgeLib.getEarnedImageMedium() );
      }
      SSIContest contest = paxBadge.getContest();
      if ( contest != null )
      {
        badgeDetail.setContestName( getContestName( contest, UserManager.getLocale() ) );
        badgeDetail.setBadgeDescription( null );
      }
      else
      {
        badgeDetail.setBadgeDescription( paxBadge.getBadgeRule().getBadgeDescriptionTextFromCM() );
      }
      BadgeInfo badgeInfo = new BadgeInfo();
      if ( count < 1 )
      {
        badgeInfo.setHeaderTitle( CmsResourceBundle.getCmsBundle().getString( "gamification.admin.labels.HISTORY_LABEL" ) );
      }
      badgeInfo.getBadgeDetails().add( badgeDetail );
      badgesView.getBadgeGroups().add( badgeInfo );
      count = count + 1;
    }
    super.writeAsJsonToResponse( badgesView, response );
    return null;
  }

  public String getContestName( SSIContest ssiContest, Locale locale )
  {
    return getCMAssetService().getString( ssiContest.getCmAssetCode(), SSIContest.CONTEST_CMASSET_NAME, locale, true );
  }

  public ParticipantSearchView copyProperties( ParticipantSearchView bean, ParticipantSearchView paxView )
  {
    return paxView;
  }

  private PublicRecognitionService getPublicRecognitionService()
  {
    return (PublicRecognitionService)getService( PublicRecognitionService.BEAN_NAME );
  }

  private SmackTalkService getSmackTalkService()
  {
    return (SmackTalkService)getService( SmackTalkService.BEAN_NAME );
  }

  private static ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private ProfileService getProfileService()
  {
    return (ProfileService)getService( ProfileService.BEAN_NAME );
  }

  private GamificationService getGamificationService()
  {
    return (GamificationService)getService( GamificationService.BEAN_NAME );
  }

  private ProxyService getProxyService()
  {
    return (ProxyService)getService( ProxyService.BEAN_NAME );
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private TeamService getTeamService()
  {
    return (TeamService)getService( TeamService.BEAN_NAME );
  }

  private CMAssetService getCMAssetService()
  {
    return (CMAssetService)getService( CMAssetService.BEAN_NAME );
  }
  
  //Client customization start
  private CokeCareerMomentsService getCokeCareerMomentsService()
  {
    return (CokeCareerMomentsService)getService( CokeCareerMomentsService.BEAN_NAME );
  }
  //Client customization end
}
