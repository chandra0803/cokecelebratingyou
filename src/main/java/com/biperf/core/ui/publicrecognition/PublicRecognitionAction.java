
package com.biperf.core.ui.publicrecognition;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.claim.RecognitionClaimSource;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PublicRecognitionTabType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantSearchListView;
import com.biperf.core.domain.participant.ParticipantSearchView;
import com.biperf.core.domain.promotion.ParticipantFollowers;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PublicRecognitionComment;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.publicrecognition.PublicRecognitionCommentViewBean;
import com.biperf.core.domain.publicrecognition.PublicRecognitionMainView;
import com.biperf.core.domain.publicrecognition.PublicRecognitionParticipantView;
import com.biperf.core.domain.publicrecognition.PublicRecognitionSet;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.TranslatedContent;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.PublicRecognitionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.translation.UnexpectedTranslationException;
import com.biperf.core.service.translation.UnsupportedTranslationException;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.WebErrorMessageList;
import com.biperf.core.ui.client.PublicRecognitionCheersListView;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BudgetUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.UserSessionAttributes;
import com.biperf.core.utils.WebResponseConstants;
import com.biperf.core.value.PublicRecognitionFormattedValueBean;
import com.biperf.core.value.client.CheersPointValueBean;
import com.biperf.util.StringUtils;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * 
 * @author dudam
 * @since Dec 11, 2012
 * @version 1.0
 */
public class PublicRecognitionAction extends BaseDispatchAction
{

  private static final String key = "USER_FRIENDLY_SYSTEM_ERROR_MESSAGE";
  private static final String asset = "system.generalerror";
  private static final Log log = LogFactory.getLog( PublicRecognitionAction.class );
  public static final int PAGE_SIZE = 10;

  private static final String REC_SENT = "recSent";
  private static final String RECEIVED = "received";
  private static final String SENT = "sent";
  private static final String USER_ID = "userId";
  private static final String TYPE = "type";
  private static final String TIMEFRAME_MONTH_ID = "timeframeMonthId";
  private static final String NODE_IDS = "nodeId";
  private static final String ROW_NUM_END = "rowNumEnd";
  private static final String ROW_NUM_START = "rowNumStart";
  private static final String PAGE_NUMBER = "page";
  private static final String TIMEFRAME_YEAR = "timeframeYear";
  private static final String START_DATE = "startDate";
  private static final String END_DATE = "endDate";
  private static final String TIMEFRAME_TYPE = "timeframeType";
  private static final String MONTH = "month";
  
  // Client customizations for WIP #62128 starts
  private static final long MAX_POINTS = 25;
  private static final BigInteger DIVISOR = BigInteger.valueOf( 5L );
  // Client customizations for WIP #62128 ends

  /**
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws IOException
   * @throws ServletException
   */
  public ActionForward fetchPublicRecognitions( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
  {
    String tabType = request.getParameter( "recognitionSetNameId" );
    String participantId = request.getParameter( "participantId" );
    String listValue = request.getParameter( "listValue" );
    Long userId;
    List<PublicRecognitionSet> recognitionSetValueList = null;

    PublicRecognitionMainView view = new PublicRecognitionMainView();

    if ( participantId != null )
    {
      userId = Long.parseLong( participantId );
    }
    else
    {
      userId = UserManager.getUserId();
    }
    try
    {
      if ( StringUtil.isEmpty( participantId ) )
      {
        if ( tabType == null )
        {
          tabType = getSystemVariableService().getPropertyByName( SystemVariableService.PUBLIC_RECOG_DEFAULT_TAB_NAME ).getStringVal();
          if ( tabType == null )
          {
            tabType = PublicRecognitionTabType.lookup( PublicRecognitionTabType.RECOMMENDED_TAB ).getCode();
          }
        }        
        recognitionSetValueList = getPublicRecognitionService().getPublicRecognitionClaimsByTabType( userId, tabType, PAGE_SIZE, getPageNumber( request ), listValue );
      }
      else
      {
        tabType = PublicRecognitionTabType.lookup( PublicRecognitionTabType.ME_TAB ).getCode();
        recognitionSetValueList = getPublicRecognitionService().getPublicRecognitionClaimsByTabType( userId, tabType, PAGE_SIZE, getPageNumber( request ), listValue );
        userId = UserManager.getUserId();

        // taking mine tab content in to local variable beans
        List<PublicRecognitionFormattedValueBean> beans = null;
        for ( PublicRecognitionSet set : recognitionSetValueList )
        {
          if ( set.getNameId().equals( PublicRecognitionTabType.ME_TAB ) )
          {
            beans = set.getClaims();
          }
        }
        Long cheersPromotionId = getPromotionService().getCheersPromotionId();
        // moving local variable beans content i.e (mine tab content into global tab) and making
        // mine tab content to empty
        for ( PublicRecognitionSet set : recognitionSetValueList )
        {
          for ( PublicRecognitionFormattedValueBean valueBean : set.getClaims() )
          {
            // these changes have been made to fix the public recognition issue when it is a group
            // claim. #56447
            if ( valueBean.getRecipients() != null && !valueBean.getRecipients().isEmpty() )
            {
              List<PublicRecognitionParticipantView> recipients = valueBean.getRecipients();
              for ( PublicRecognitionParticipantView recipient : recipients )
              {
                valueBean.setIsMine( "false" );
                if ( recipient.getId().equals( userId ) )
                {
                  valueBean.setIsMine( "true" );
                  break;
                }
              }
            }
          }
          if ( set.getNameId().equals( PublicRecognitionTabType.GLOBAL_TAB ) )
          {
            set.setClaims( beans );
          }
          if ( set.getNameId().equals( PublicRecognitionTabType.ME_TAB ) )
          {
            set.setClaims( new ArrayList<PublicRecognitionFormattedValueBean>() );
          }
        }
      }
      long start = System.currentTimeMillis();

      final BigDecimal US_MEDIA_VALUE = getCountryService().getBudgetMediaValueByCountryCode( Country.UNITED_STATES );
      final BigDecimal USER_MEDIA_VALUE = getUserService().getBudgetMediaValueForUser( UserManager.getUserId() );

      // could also use UserService#getPreferredLanguageFor to determine the user's language; the
      // rest of
      // translation uses it
      view = new PublicRecognitionMainView( UserManager.getUserLanguage(), recognitionSetValueList, RequestUtils.getBaseURI( request ), US_MEDIA_VALUE, USER_MEDIA_VALUE, listValue );

      long end = System.currentTimeMillis();
      log.debug( "Time took to build public recognition main view object is: " + ( end - start ) + " milliseconds." );
    }
    catch( ServiceErrorException se )
    {
      WebErrorMessage message = new WebErrorMessage();
      message.setSuccess( false );
      message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
      message.setText( se.getServiceErrorsCMText().get( 0 ) );
      view.getMessages().add( message );
    }

    // New Service Anniversary Celebration Module Enabling.
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      view.setNewSAEnabled( true );
    }

    super.writeAsJsonToResponse( view, response );
    return null;
  }

  /**
   * Fetch the recognitions received or given based on type for the engagement user dash board
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws IOException
   * @throws ServletException
   */
  public ActionForward fetchRecognitionsForEngagementDashboard( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
  {
    Map<String, Object> queryParams = populateEngagementQueryParams( request );

    PublicRecognitionMainView view = new PublicRecognitionMainView();
    try
    {
      List<PublicRecognitionSet> recognitionSetValueList = getPublicRecognitionService().getDashboardRecognitionClaimsByType( queryParams, PAGE_SIZE );
      final BigDecimal US_MEDIA_VALUE = getCountryService().getBudgetMediaValueByCountryCode( Country.UNITED_STATES );
      final BigDecimal USER_MEDIA_VALUE = getUserService().getBudgetMediaValueForUser( UserManager.getUserId() );
      String listValue = request.getParameter( "listValue" );

      view = new PublicRecognitionMainView( UserManager.getUserLanguage(), recognitionSetValueList, RequestUtils.getBaseURI( request ), US_MEDIA_VALUE, USER_MEDIA_VALUE, listValue );
    }
    catch( ServiceErrorException se )
    {
      WebErrorMessage message = new WebErrorMessage();
      message.setSuccess( false );
      message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
      message.setText( se.getServiceErrorsCMText().get( 0 ) );
      view.getMessages().add( message );
    }
    super.writeAsJsonToResponse( view, response );
    return null;
  }

  private Map<String, Object> populateEngagementQueryParams( HttpServletRequest request )
  {
    String type = request.getParameter( "type" );
    String participantId = request.getParameter( "participantId" );
    Date currentDate = DateUtils.getCurrentDate();

    String selectedNodes = StringUtils.isEmpty( request.getParameter( NODE_IDS ) ) ? null : request.getParameter( NODE_IDS );
    int timeframeMonthId = StringUtils.isEmpty( request.getParameter( TIMEFRAME_MONTH_ID ) )
        ? DateUtils.getMonthFromDate( currentDate )
        : Integer.parseInt( request.getParameter( TIMEFRAME_MONTH_ID ) );
    String timeframeType = StringUtils.isEmpty( request.getParameter( TIMEFRAME_TYPE ) ) ? MONTH : request.getParameter( TIMEFRAME_TYPE );
    int timeframeYear = StringUtils.isEmpty( request.getParameter( TIMEFRAME_YEAR ) ) ? DateUtils.getYearFromDate( currentDate ) : Integer.parseInt( request.getParameter( TIMEFRAME_YEAR ) );

    int pageNumber = getPageNumber( request );
    int rowNumStart = ( pageNumber - 1 ) * PAGE_SIZE + 1;
    int rowNumEnd = rowNumStart + PAGE_SIZE - 1;

    // Calculate the rolling period start date and the end date
    Date nextStartDate = DateUtils.getRollingPeriodStartDate( timeframeMonthId, timeframeYear, timeframeType, null );
    Date nextEndDate = DateUtils.getRollingPeriodEndDate( timeframeMonthId, timeframeYear, timeframeType, null );

    Map<String, Object> queryParams = new HashMap<String, Object>();
    queryParams.put( NODE_IDS, selectedNodes );
    queryParams.put( USER_ID, participantId );
    queryParams.put( START_DATE, nextStartDate );
    queryParams.put( END_DATE, nextEndDate );
    queryParams.put( PAGE_NUMBER, pageNumber );
    queryParams.put( ROW_NUM_START, rowNumStart );
    queryParams.put( ROW_NUM_END, rowNumEnd );
    if ( REC_SENT.equals( type ) )
    {
      queryParams.put( TYPE, SENT );
    }
    else
    {
      queryParams.put( TYPE, RECEIVED );
    }
    return queryParams;
  }

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
   * This method is used to follow or unfollow pax from the mini profile popover.
   */
  public ActionForward followUnFollowPax( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
  {

    String followUnFollowPaxIds = request.getParameter( "participantIds" );
    String isFollowed = request.getParameter( "isFollowed" );
    String paxIds = request.getParameter( "participantId" );

    User participant = getUserService().getUserById( UserManager.getUserId() );

    String paxRequest = null;

    if ( !StringUtils.isEmpty( followUnFollowPaxIds ) && StringUtils.isEmpty( paxIds ) )
    {
      paxRequest = followUnFollowPaxIds;
    }
    else if ( !StringUtils.isEmpty( paxIds ) && StringUtils.isEmpty( followUnFollowPaxIds ) )
    {
      paxRequest = paxIds;
    }

    List<String> splitByComma = Stream.of( paxRequest ).map( w -> w.split( "," ) ).flatMap( Arrays::stream ).collect( Collectors.toList() );

    WebErrorMessageList messages = new WebErrorMessageList();

    List<Long> paxFollowersList = (List<Long>)request.getSession().getAttribute( UserSessionAttributes.PARTICIPANT_FOLLOWERS );

    for ( String followUnFollowPaxId : splitByComma )
    {
      User followUnFollowPax = null;
      followUnFollowPax = getUserService().getUserById( Long.parseLong( followUnFollowPaxId ) );

      ParticipantFollowers addRemovePax = new ParticipantFollowers();
      addRemovePax.setFollower( followUnFollowPax );
      addRemovePax.setParticipant( participant );

      WebErrorMessage message = new WebErrorMessage();

      if ( Boolean.valueOf( isFollowed ) )
      {
        try
        {
          addRemovePax = getParticipantService().getById( participant.getId(), followUnFollowPax.getId() );
          getParticipantService().removeParticipantFollowee( addRemovePax );
          message.setSuccess( true );
          paxFollowersList.remove( Long.parseLong( followUnFollowPaxId ) );
        }
        catch( Exception e )
        {
          message.setSuccess( false );
          message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
          message.setText( ContentReaderManager.getText( asset, key ) );
        }
      }
      else
      {
        try
        {
          getParticipantService().addParticipantFollowee( addRemovePax );
          message.setSuccess( true );
          paxFollowersList.add( Long.parseLong( followUnFollowPaxId ) );
        }
        catch( Exception e )
        {
          message.setSuccess( false );
          message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
          message.setText( ContentReaderManager.getText( asset, key ) );
        }
      }
      messages.getMessages().add( message );
    }

    super.writeAsJsonToResponse( messages, response );
    return null;
  }

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
   * This method is used to fetch the list of already following users
   */
  public ActionForward fetchFollowingPax( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
  {

    User user = getUserService().getUserById( UserManager.getUserId() );
    List<Participant> participantFollowerList = getPublicRecognitionService().getParticipantsIAmFollowing( user.getId() );
    ParticipantSearchListView participantsView = new ParticipantSearchListView();
    for ( Participant pf : participantFollowerList )
    {
      participantsView.getParticipants().add( new ParticipantSearchView( pf, pf.getPrimaryCountryCode(), pf.getPrimaryCountryName() ) );
    }
    super.writeAsJsonToResponse( participantsView, response );
    return null;

  }

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
   * This method is used in public recognition detail page. When user clicks on hide button this method will invoke.
   * @throws ServiceErrorException 
   */
  public ActionForward hide( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException, ServiceErrorException
  {
    final Long commentId = RequestUtils.getOptionalParamLong( request, "commentId" );
    final Long recognitionId = RequestUtils.getRequiredParamLong( request, "recognitionId" );
    if ( commentId != null && commentId.longValue() != 0L )
    {
      getPublicRecognitionService().deletePublicRecognitionComment( commentId, UserManager.getUserId() );
      super.writeAsJsonToResponse( new PublicRecognitionHideValueBean(), response );
    }
    else
    {
      if ( form != null )
      {
        PublicRecognitionHideForm hideForm = (PublicRecognitionHideForm)form;
        Long claimId = hideForm.getRecognitionId();

        getPublicRecognitionService().hidePublicRecognition( claimId );
        super.writeAsJsonToResponse( new PublicRecognitionHideValueBean(), response );
      }
    }
    return null;
  }

  public ActionForward translateComment( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    final Long RECOGNITION_ID = RequestUtils.getRequiredParamLong( request, "recognitionId" );
    final Long COMMENT_ID = RequestUtils.getOptionalParamLong( request, "commentId" );

    if ( COMMENT_ID == null || COMMENT_ID.longValue() == 0 )
    {
      translateSubmitterRecognitionMessage( RECOGNITION_ID, response );
    }
    else
    {
      translatePublicRecognitionComment( COMMENT_ID, response );
    }

    return null;
  }

  private void translateSubmitterRecognitionMessage( Long recognitionId, HttpServletResponse response ) throws IOException
  {
    PublicRecognitionCommentViewBean bean = new PublicRecognitionCommentViewBean();
    try
    {
      TranslatedContent tc = getClaimService().getTranslatedRecognitionClaimSubmitterCommentFor( recognitionId, UserManager.getUserId() );
      bean.setComment( tc.getTranslatedContent() );
    }
    catch( UnsupportedTranslationException ute )
    {
      bean.setComment( ContentReaderManager.getText( "recognition.public.recognition.item", "TRANSLATION_UNAVAILABLE" ) );
    }
    catch( UnexpectedTranslationException ex )
    {
      bean.setComment( ContentReaderManager.getText( "recognition.public.recognition.item", "TRANSLATION_UNAVAILABLE" ) );
    }

    super.writeAsJsonToResponse( bean, response );
  }

  private void translatePublicRecognitionComment( Long commentId, HttpServletResponse response ) throws IOException
  {
    PublicRecognitionCommentViewBean bean = new PublicRecognitionCommentViewBean();
    try
    {
      TranslatedContent tc = getPublicRecognitionService().getTranslatedPublicRecognitionCommentFor( commentId, UserManager.getUserId() );
      bean.setComment( tc.getTranslatedContent() );
    }
    catch( UnsupportedTranslationException ute )
    {
      bean.setComment( ContentReaderManager.getText( "recognition.public.recognition.item", "TRANSLATION_UNAVAILABLE" ) );
    }
    catch( UnexpectedTranslationException ex )
    {
      bean.setComment( ContentReaderManager.getText( "recognition.public.recognition.item", "TRANSLATION_UNAVAILABLE" ) );
    }

    super.writeAsJsonToResponse( bean, response );
  }

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
   * This method is used to post a comment on the recognition
   */
  public ActionForward postComment( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
  {
    // Get the form
    PublicRecognitionCommentForm commentForm = (PublicRecognitionCommentForm)form;
    String comment = commentForm.getComment();
    String claimId = commentForm.getRecognitionId();

    // json view object
    PublicRecognitionCommentView view = new PublicRecognitionCommentView();

    // Get all team claims and post duplicate comments for each of them
    AbstractRecognitionClaim claim = (AbstractRecognitionClaim)getClaimService().getClaimById( Long.valueOf( claimId ) );
    Participant participant = getParticipantService().getParticipantById( UserManager.getUserId() );
    List<AbstractRecognitionClaim> teamClaims = (List<AbstractRecognitionClaim>)getClaimService().getClaimsByTeamId( claim.getTeamId() );
    // Build domain objects
    List<PublicRecognitionComment> commentsToSave = new ArrayList<PublicRecognitionComment>();
    teamClaims.forEach( teamClaim ->
    {
      PublicRecognitionComment publicRecogComment = new PublicRecognitionComment();
      publicRecogComment.setComments( comment );
      publicRecogComment.setClaim( teamClaim );
      publicRecogComment.setTeamId( teamClaim.getTeamId() );
      publicRecogComment.setUser( participant );
      commentsToSave.add( publicRecogComment );
    } );

    // Save domain objects and write response
    try
    {
      List<PublicRecognitionComment> savedComments = getPublicRecognitionService().savePublicRecognitionComments( commentsToSave );
      Long userId = UserManager.getUserId();
      // if success build success view object
      PublicRecognitionCommentViewBean commentBean = new PublicRecognitionCommentViewBean();
      commentBean.setId( savedComments.iterator().next().getId() ); // just use any of the saved
                                                                    // comment ids
      commentBean.setRecognitionId( Long.valueOf( claimId ) );
      commentBean.setComment( comment );
      commentBean.setMine( userId.equals( savedComments.iterator().next().getUser().getId() ) ? true : false );

      PublicRecognitionParticipantView commenter = new PublicRecognitionParticipantView( participant );
      commentBean.setCommenter( commenter );
      view.setComment( commentBean );
    }
    catch( Exception e )
    {
      // if failure build failure view object
      WebErrorMessage message = new WebErrorMessage();
      message.setSuccess( false );
      message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
      message.setText( ContentReaderManager.getText( asset, key ) );
      view.getMessages().add( message );
    }

    super.writeAsJsonToResponse( view, response );
    return null;
  }

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
   * This method is used to like a claim or recognition
   */
  public ActionForward like( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
  {
    // view object
    PublicRecognitionLikeView view = new PublicRecognitionLikeView();

    // get claim from form
    PublicRecognitionLikeForm commentForm = (PublicRecognitionLikeForm)form;
    Long claimId = Long.valueOf( commentForm.getRecognitionId() );

    // to avoid null pointer if bi-admin likes any recognition
    if ( UserManager.getUser().isParticipant() )
    {
      try
      {
        getPublicRecognitionService().likePublicRecognition( UserManager.getUserId(), claimId );
        view.setNumberOfLikers( getPublicRecognitionService().recognitionLikeCountByClaimId( claimId ) );
      }
      catch( Exception e )
      {
        // if failure build failure view object
        WebErrorMessage message = new WebErrorMessage();
        message.setSuccess( false );
        message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
        message.setText( ContentReaderManager.getText( asset, key ) );
        view.getMessages().add( message );
      }
    }
    super.writeAsJsonToResponse( view, response );
    return null;
  }

  public ActionForward unlike( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
  {
    // view object
    PublicRecognitionLikeView view = new PublicRecognitionLikeView();

    // get claim from form
    PublicRecognitionLikeForm commentForm = (PublicRecognitionLikeForm)form;
    Long claimId = Long.valueOf( commentForm.getRecognitionId() );

    // to avoid null pointer if bi-admin likes any recognition
    if ( UserManager.getUser().isParticipant() )
    {
      try
      {
        getPublicRecognitionService().unlikePublicRecognition( UserManager.getUserId(), claimId );
        view.setNumberOfLikers( getPublicRecognitionService().recognitionLikeCountByClaimId( claimId ) );
      }
      catch( Exception e )
      {
        // if failure build failure view object
        WebErrorMessage message = new WebErrorMessage();
        message.setSuccess( false );
        message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
        message.setText( ContentReaderManager.getText( asset, key ) );
        view.getMessages().add( message );
      }
    }
    super.writeAsJsonToResponse( view, response );
    return null;
  }

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
   * This method is used to add points a participant who got recognition
   */
  public ActionForward addPoints( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
  {
    // create view
    PublicRecognitionCommentView view = new PublicRecognitionCommentView();

    // Read form values
    PublicRecognitionAddPointsForm addPointsForm = (PublicRecognitionAddPointsForm)form;
    String claimId = addPointsForm.getRecognitionId();
    String comment = addPointsForm.getComment();
    Long points = Long.valueOf( addPointsForm.getPoints() );

    // get user
    Participant participant = getParticipantService().getParticipantById( UserManager.getUserId() );
    PublicRecognitionParticipantView commenter = new PublicRecognitionParticipantView( participant );
    Long formId = 5000L;

    RecognitionClaim recClaim = (RecognitionClaim)getClaimService().getClaimById( Long.valueOf( claimId ) );

    try
    {
      // build RecognitionClaim
      RecognitionClaim recognitionClaim = buildRecognitionClaim( Long.valueOf( claimId ), comment, points );

      // save recognitionClaim as new claim
      List<PublicRecognitionComment> savedComments = getPublicRecognitionService()
          .savePublicRecognitionClaim( recognitionClaim, formId, recognitionClaim.getSubmitter(), false, false, participant, comment, points, recClaim );

      // if success build success view object
      PublicRecognitionCommentViewBean commentBean = new PublicRecognitionCommentViewBean();
      commentBean.setId( savedComments.iterator().next().getId() );
      commentBean.setRecognitionId( Long.valueOf( claimId ) );
      commentBean.setComment( comment );
      commentBean.setMine( true );
      commentBean.setCommenter( commenter );
      view.setComment( commentBean );
    }
    catch( ServiceErrorException e )
    {
      log.error( "Error: " + e );
      // if failure build failure view object
      WebErrorMessage message = new WebErrorMessage();
      message.setSuccess( false );
      message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
      message.setText( (String)e.getServiceErrorsCMText().get( 0 ) );
      view.getMessages().add( message );
    }
    super.writeAsJsonToResponse( view, response );
    return null;
  }
  
  // Client customizations for WIP #62128 starts
  public ActionForward fetchCheersPoints( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
  {
    // create view
    PublicRecognitionCheersListView listView = new PublicRecognitionCheersListView();
    PublicRecognitionCheersForm cheersForm = (PublicRecognitionCheersForm)form;
    Long cheersPromotionId = getPromotionService().getCheersPromotionId();

    if ( Objects.nonNull( cheersPromotionId ) )
    {
      Promotion cheersPromotion = getPromotionService().getPromotionById( cheersPromotionId );
      long giverAvailableBudget = getAvailableBudget( cheersPromotion, UserManager.getPrimaryNodeId() );
      long maxUsableBudget = giverAvailableBudget > MAX_POINTS ? MAX_POINTS : giverAvailableBudget;

      if ( maxUsableBudget == 0 || ( maxUsableBudget > 0 && maxUsableBudget < DIVISOR.longValue() ) )
      {
        CheersPointValueBean bean = new CheersPointValueBean();
        bean.setId( 0L );
        bean.setPoints( 0L );
        listView.getPoints().add( bean );
      }
      else
      {
        listView.setPoints( processPoints( maxUsableBudget ) );
      }
      
      if ( Objects.nonNull( cheersForm.getRecipientId() ) )
      {
        Participant recipient = getParticipantService().getParticipantById( Long.parseLong( cheersForm.getRecipientId() ) );
        listView.mapRecipient( recipient );
      }
    }

    super.writeAsJsonToResponse( listView, response );
    return null;
  }

  public ActionForward postCheers( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    PublicRecognitionCheersForm cheersForm = (PublicRecognitionCheersForm)form;
    Participant submitter = getParticipantService().getParticipantById( UserManager.getUserId() );
    Long cheersPromtionId = getPromotionService().getCheersPromotionId();
    RecognitionPromotion cheersPromotion = (RecognitionPromotion)getPromotionService().getPromotionById( cheersPromtionId );
    Budget budget = null;

    if ( cheersPromotion.isAwardActive() )
    {
      budget = getPromotionService().getAvailableBudget( cheersPromtionId, UserManager.getUserId(), UserManager.getPrimaryNodeId() );
    }
    boolean deductBudget = Objects.nonNull( budget ) ? Boolean.TRUE : Boolean.FALSE;

    AbstractRecognitionClaim claim = buildClaim( submitter, cheersPromotion, cheersForm );
    boolean isSuccess = getClaimService().submitCheersRecognition( claim, deductBudget );
    request.setAttribute( "success", isSuccess );
    String siteUrl = isSuccess ? getSiteUrlPrefix() : org.apache.commons.lang3.StringUtils.EMPTY;
    request.setAttribute( "siteUrlPrefix", siteUrl );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private AbstractRecognitionClaim buildClaim( Participant submitter, RecognitionPromotion promotion, PublicRecognitionCheersForm cheersForm )
  {
    Participant recipient = getParticipantService().getParticipantById( Long.parseLong( cheersForm.getRecipientId() ) );
    RecognitionClaim claim = new RecognitionClaim();
    ClaimRecipient claimRecipient = new ClaimRecipient();

    String comments = cheersForm.getComments();

    Long submitterNodeId = UserManager.getPrimaryNodeId();

    if ( promotion.isAwardActive() && promotion.getAwardType().getCode().equals( PromotionAwardsType.POINTS ) )
    {
      claimRecipient.setAwardQuantity( Long.parseLong( cheersForm.getPoints() ) );
    }
    claimRecipient.setRecipient( recipient );
    claimRecipient.setNode( getRecipientNode( recipient ) );
    claimRecipient.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.PENDING ) );
    
    String timeZoneID = getUserService().getUserTimeZone( submitter.getId() );
    Date referenceDate = DateUtils.applyTimeZone( new Date(), timeZoneID );
    claim.setSubmissionDate( referenceDate );

    claim.addClaimRecipient( claimRecipient );
    claim.setCopyManager( false );
    claim.setSubmissionDate( new Timestamp( DateUtils.getCurrentDateAsLong() ) );
    claim.setPromotion( promotion );
    claim.setOpen( true );
    claim.setSubmitter( submitter );
    claim.setNode( getNodeService().getNodeById( submitterNodeId ) );
    claim.setCard( null );
    claim.setSource( RecognitionClaimSource.WEB );
    claim.setSubmitterComments( comments );
    claim.setSubmitterCommentsLanguageType( getUserService().getPreferredLanguageFor( submitter ) );

    return claim;
  }
  
  private String getSiteUrlPrefix()
  {
    return getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
  }

  private Node getRecipientNode( Participant recipient )
  {
    UserNode userPrimaryNode = getUserService().getPrimaryUserNode( recipient.getId() );
    return userPrimaryNode.getNode();
  }

  private long getAvailableBudget( Promotion promotion, Long nodeId )
  {
    long availableBudget = 0;
    if ( promotion.getBudgetMaster() != null )
    {
      BigDecimal totalUnapprovedAwardQty = new BigDecimal( 0 );

      Budget budget = getPromotionService().getAvailableBudget( promotion.getId(), UserManager.getUserId(), nodeId );
      Long budgetMasterId = null;

      if ( Objects.nonNull( budget ) )
      {
        if ( budget.getBudgetSegment().getBudgetMaster().isMultiPromotion() )
        {
          budgetMasterId = budget.getBudgetSegment().getBudgetMaster().getId();
        }

        if ( promotion.getBudgetMaster().isParticipantBudget() )
        {
          totalUnapprovedAwardQty = getPromotionService().getTotalUnapprovedAwardQuantity( promotion.getId(), UserManager.getUserId(), null, budgetMasterId );
        }
        else if ( promotion.getBudgetMaster().isCentralBudget() )
        {
          totalUnapprovedAwardQty = getPromotionService().getTotalUnapprovedAwardQuantity( promotion.getId(), null, null, budgetMasterId );
        }

        if ( budget.getCurrentValue().subtract( totalUnapprovedAwardQty ).compareTo( BigDecimal.ZERO ) > 0 )
        {
          budget.setCurrentValue( budget.getCurrentValue().subtract( totalUnapprovedAwardQty ) );
        }
        else
        {
          budget.setCurrentValue( new BigDecimal( 0 ) );
        }

        BigDecimal convertedCurrentValue = budget.getCurrentValue();
        BudgetMaster budgetMaster = budget.getBudgetSegment().getBudgetMaster();
        if ( !budgetMaster.isCentralBudget() )
        {
          final BigDecimal US_MEDIA_VALUE = getCountryService().getBudgetMediaValueByCountryCode( Country.UNITED_STATES );
          final BigDecimal USER_MEDIA_VALUE = getUserService().getBudgetMediaValueForUser( UserManager.getUserId() );
          convertedCurrentValue = BudgetUtils.applyMediaConversion( budget.getCurrentValue(), US_MEDIA_VALUE, USER_MEDIA_VALUE );
        }
        availableBudget = BudgetUtils.getBudgetDisplayValue( convertedCurrentValue );
      }
      else
      {
        availableBudget = BigDecimal.ZERO.longValue();
      }
    }
    return availableBudget;
  }

  private List<CheersPointValueBean> processPoints( Long budget )
  {
    List<CheersPointValueBean> pointsList = new ArrayList<CheersPointValueBean>();
    CheersPointValueBean bean = new CheersPointValueBean();
    bean.setId( 0L );
    bean.setPoints( 0L );
    pointsList.add( bean );

    int initialBudget = budget.intValue();
    BigInteger budgetPoint = BigInteger.valueOf( budget );

    // Find Mod of 5 if zero the budget is multiple of 5
    if ( budgetPoint.mod( DIVISOR ).intValue() != 0 )
    {
      int remainder = budgetPoint.remainder( DIVISOR ).intValue();
      initialBudget = initialBudget - remainder;
    }
    // Spliting into 0,5,10 etc add it in bean
    while ( initialBudget != 0 )
    {
      CheersPointValueBean cheersPointValueBean = new CheersPointValueBean();
      cheersPointValueBean.setId( new Long( initialBudget ) );
      cheersPointValueBean.setPoints( new Long( initialBudget ) );
      pointsList.add( cheersPointValueBean );
      initialBudget = initialBudget - DIVISOR.intValue();
    }

    return pointsList.stream().sorted( Comparator.comparingLong( CheersPointValueBean::getPoints ) ).collect( Collectors.toList() );
  }
  
  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }
  
  private NodeService getNodeService()
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }
  
  // Client customizations for WIP #62128 ends

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }

  private PublicRecognitionService getPublicRecognitionService()
  {
    return (PublicRecognitionService)getService( PublicRecognitionService.BEAN_NAME );
  }

  private ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  // build RecognitionClaim
  private RecognitionClaim buildRecognitionClaim( Long claimId, String comment, Long points ) throws ServiceErrorException
  {
    RecognitionClaimSource recognitionClaimSource = getRecognitionClaimSource();
    return getPublicRecognitionService().buildRecognitionClaim( claimId, points, comment, recognitionClaimSource );
  }

  protected RecognitionClaimSource getRecognitionClaimSource()
  {
    return RecognitionClaimSource.WEB;
  }
      
  /**
   * 
   * @param request
   * @return pageNumber
   */
  private int getPageNumber( HttpServletRequest request )
  {
    String pageNumber = request.getParameter( "pageNumber" );
    if ( !StringUtil.isEmpty( pageNumber ) )
    {
      return Integer.parseInt( pageNumber );
    }
    return 1;
  }

}
