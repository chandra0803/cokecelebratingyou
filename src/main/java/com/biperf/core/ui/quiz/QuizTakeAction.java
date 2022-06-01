
package com.biperf.core.ui.quiz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.dao.claim.hibernate.QuizClaimQueryConstraint;
import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.claim.QuizClaim;
import com.biperf.core.domain.claim.QuizClaimItem;
import com.biperf.core.domain.claim.QuizResponse;
import com.biperf.core.domain.diyquiz.DIYQuiz;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.BudgetFinalPayoutRule;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.gamification.BadgeLibrary;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.quiz.Quiz;
import com.biperf.core.domain.quiz.QuizLearningDetails;
import com.biperf.core.domain.quiz.QuizLearningObject;
import com.biperf.core.domain.quiz.QuizQuestion;
import com.biperf.core.domain.quiz.QuizQuestionAnswer;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.diyquiz.DIYQuizService;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.quiz.QuizAssociationRequest;
import com.biperf.core.service.quiz.QuizService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.WebErrorMessageList;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.QuizFileUploadValue;
import com.biperf.core.value.QuizPageValueBean;
import com.biperf.util.StringUtils;

public class QuizTakeAction extends BaseDispatchAction
{
  /**
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public ActionForward fetchActiveQuiz( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    QuizClaimSubmissionForm claimSubmissionForm = (QuizClaimSubmissionForm)form;

    // get quiz promotion
    QuizPromotion quizPromotion = getQuizPromotion( request );
    Quiz quiz = getQuizFromPromotion( request, quizPromotion );
    Long quizId = null;
    if ( quizPromotion.isDIYQuizPromotion() )
    {
      quizId = quiz.getId();
    }
    QuizClaim quizClaim = getOpenClaimIfAny( claimSubmissionForm, quizId );

    // Check if the participant already passed the quiz
    boolean isPassedQuiz = isPassedQuizAlready( Long.parseLong( claimSubmissionForm.getPromotionId() ), quizId, request, response );
    if ( isPassedQuiz )
    {
      WebErrorMessageList messages = new WebErrorMessageList();
      WebErrorMessage message = new WebErrorMessage();
      message = WebErrorMessage.addServerCmd( message );
      message.setUrl( RequestUtils.getBaseURI( request ) + PageConstants.QUIZ_LIST_PAGE_URL );
      messages.getMessages().add( message );
      super.writeAsJsonToResponse( messages, response );
      return null;
    }

    // build the main view object
    QuizTakeMainView quizMainView = new QuizTakeMainView();
    // build inner view object
    QuizTakeBadge quizBadge = buildBadgeView( quizPromotion, quiz );
    List<QuizTakeMaterial> quizMaterials = buildQuizMaterials( quizPromotion, quiz );
    String retakeQuizUrl = buildRetakeQuizUrl( quizPromotion.getId(), quiz.getId() );

    int attemptsUsed = getQuizAttemptsUsed( quizPromotion, quizId );
    QuizTakeQuizView quizJson = new QuizTakeQuizView( quizPromotion, quiz, quizClaim, quizBadge, quizMaterials, attemptsUsed, retakeQuizUrl );

    // add inner view object to main view and write to response
    quizMainView.setQuizJson( quizJson );
    super.writeAsJsonToResponse( quizMainView, response );
    return null;
  }

  private String buildRetakeQuizUrl( Long quizPromotionId, Long quizId )
  {
    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    Map<String, Long> parameterMap = new HashMap<String, Long>();
    parameterMap.put( "promotionId", quizPromotionId );
    parameterMap.put( "quizId", quizId );
    String retakeQuizUrl = ClientStateUtils.generateEncodedLink( siteUrlPrefix, PageConstants.QUIZ_TAKE_PAGE_URL, parameterMap );
    return retakeQuizUrl;
  }

  private int getQuizAttemptsUsed( QuizPromotion quizPromotion, Long quizId )
  {
    QuizClaimQueryConstraint claimQueryConstraint = new QuizClaimQueryConstraint();
    claimQueryConstraint.setSubmitterId( UserManager.getUserId() );
    claimQueryConstraint.setIncludedPromotionIds( new Long[] { quizPromotion.getId() } );
    claimQueryConstraint.setOpen( Boolean.FALSE );

    if ( quizPromotion.isDIYQuizPromotion() )
    {
      claimQueryConstraint.setDiyQuizId( quizId );
    }
    int attemptsUsed = getClaimService().getClaimListCount( claimQueryConstraint );
    return attemptsUsed;
  }

  private QuizTakeBadge buildBadgeView( QuizPromotion quizPromotion, Quiz quiz )
  {
    Long points = null;
    BadgeRule selectedBadgeRule = null;
    if ( quizPromotion.isAwardActive() )
    {
      points = quizPromotion.getAwardAmount();
    }

    // Check DIY Quiz
    List<Badge> badges = new ArrayList<Badge>();
    if ( quizPromotion.isDIYQuizPromotion() )
    {
      DIYQuiz diyQuiz = (DIYQuiz)quiz;
      if ( diyQuiz.getBadgeRule() != null )
      {
        badges = getGamificationService().getBadgeByQuiz( diyQuiz.getBadgeRule().getId() );
      }
    }
    else
    {
      badges = getGamificationService().getBadgeByPromotion( quizPromotion.getId() );
    }

    if ( badges.size() > 0 )
    {
      QuizTakeBadge badgeView = new QuizTakeBadge();
      Iterator<Badge> badgeItr = badges.iterator();
      while ( badgeItr.hasNext() )
      {
        Badge badge = badgeItr.next();
        int badgeRulesSize = badge.getBadgeRules() != null ? badge.getBadgeRules().size() : 0;
        int index = 0;
        for ( BadgeRule badgeRule : badge.getBadgeRules() )
        {
          if ( quizPromotion.isDIYQuizPromotion() )
          {
            DIYQuiz diyQuiz = (DIYQuiz)quiz;
            if ( diyQuiz.getBadgeRule() != null && diyQuiz.getBadgeRule().getId().equals( badgeRule.getId() ) )
            {
              selectedBadgeRule = badgeRule;
              break;
            }
          }
          else
          {
            if ( points != null && points >= badgeRule.getMinimumQualifier() && points <= badgeRule.getMaximumQualifier() )
            {
              selectedBadgeRule = badgeRule;
              break;
            }
            else if ( !quizPromotion.isAwardActive() )
            {
              selectedBadgeRule = badgeRule;
              break;
            }
          }

          index++;
          if ( index == badgeRulesSize && selectedBadgeRule == null && points != null && points > badgeRule.getMaximumQualifier() )
          {
            selectedBadgeRule = badgeRule;
          }
        }
        if ( selectedBadgeRule != null )
        {
          String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
          String badgeImageUrl = "";
          List earnedNotEarnedImageList = getGamificationService().getEarnedNotEarnedImageList( selectedBadgeRule.getBadgeLibraryCMKey() );
          Iterator itr = earnedNotEarnedImageList.iterator();
          while ( itr.hasNext() )
          {
            BadgeLibrary badgeLib = (BadgeLibrary)itr.next();
            badgeImageUrl = siteUrlPrefix + badgeLib.getEarnedImageSmall();
          }

          badgeView.setId( selectedBadgeRule.getBadgePromotion().getId() );
          badgeView.setName( selectedBadgeRule.getBadgeNameTextFromCM() );
          badgeView.setHowToEarnText( selectedBadgeRule.getBadgeDescriptionTextFromCM() );
          badgeView.setImg( badgeImageUrl );
          badgeView.setType( selectedBadgeRule.getBadgePromotion().getBadgeType().getCode() );
        }
      }
      return badgeView;
    }

    return null;
  }

  private boolean isPassedQuizAlready( Long promotionId, Long quizId, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    boolean passedQuiz = false;
    Long claimId = null;
    if ( quizId != null )
    {
      claimId = getClaimService().getPassedQuizClaimByPromotionIdQuizIdAndUserId( promotionId, UserManager.getUserId(), quizId );
    }
    else
    {
      claimId = getClaimService().getPassedQuizClaimByPromotionIdAndUserId( promotionId, UserManager.getUserId() );
    }
    if ( claimId != null && claimId > 0 )
    {
      passedQuiz = true;
    }
    return passedQuiz;
  }

  private List<QuizTakeMaterial> buildQuizMaterials( QuizPromotion quizPromotion, Quiz quiz )
  {
    List<QuizTakeMaterial> materials = new ArrayList<QuizTakeMaterial>();

    String leftColumn = "";

    Iterator<QuizLearningObject> itr = quiz.getLearningObjects().iterator();
    while ( itr.hasNext() )
    {
      QuizLearningObject learningObject = (QuizLearningObject)itr.next();
      if ( learningObject.getStatus().equals( QuizLearningObject.ACTIVE_STATUS ) )
      {
        QuizTakeMaterial material = new QuizTakeMaterial();
        material.setId( learningObject.getId() );
        material.setPageNumber( learningObject.getSlideNumber() );

        List<QuizLearningDetails> quizLearningDetails = getQuizService().getQuizLearningObjectforSlide( quiz.getLearningObjects(), learningObject.getSlideNumber() );
        Iterator<QuizLearningDetails> quizLearningItr = quizLearningDetails.iterator();

        while ( quizLearningItr.hasNext() )
        {
          QuizLearningDetails quizLearningDetail = (QuizLearningDetails)quizLearningItr.next();
          leftColumn = quizLearningDetail.getLeftColumn();
          if ( StringUtil.isEmpty( leftColumn ) )
          {
            buildText( material, quizLearningDetail );
          }
          else if ( leftColumn.contains( "<p>" ) )
          {
            buildImage( material, quizLearningDetail );
          }
          else if ( leftColumn.contains( "</a>" ) && !quizLearningDetail.getLeftColumn().contains( "PURLMainVideoWrapper" ) )
          {
            buildPDF( material, quizLearningDetail );
          }
          else
          {
            if ( quizPromotion.isDIYQuizPromotion() )
            {
              buildDIYVideo( material, quizLearningDetail );
            }
            else
            {
              buildVideo( material, quizLearningDetail );
            }
          }
        }
        materials.add( material );
      }
    }
    return materials;
  }

  private void buildText( QuizTakeMaterial material, QuizLearningDetails quizLearningDetail )
  {
    material.setType( QuizFileUploadValue.TYPE_TEXT );
    material.setText( quizLearningDetail.getRightColumn() );
  }

  private void buildPDF( QuizTakeMaterial material, QuizLearningDetails quizLearningDetail )
  {
    material.setType( QuizFileUploadValue.TYPE_PDF );

    int numberofPdfs = org.apache.commons.lang3.StringUtils.countMatches( quizLearningDetail.getLeftColumn(), "</a>" );
    String pdfString = quizLearningDetail.getLeftColumn();
    try
    {
      List<QuizTakeLearningFile> files = new ArrayList<QuizTakeLearningFile>();
      for ( int i = 0; i < numberofPdfs; i++ )
      {
        int nextIndex = i + 1;
        int indexOccurence = pdfString.indexOf( "</a>" );
        String pdfIndividual = pdfString.substring( 0, indexOccurence + 4 );
        int imageIndex = pdfIndividual.indexOf( "<img src=" );
        String pdfDisplayName = pdfIndividual.substring( pdfIndividual.indexOf( ">" ) + 1, imageIndex );
        int pdfPathIndex = pdfIndividual.indexOf( "href=" );
        String pdfUrl = pdfIndividual.substring( pdfPathIndex + 6, pdfIndividual.indexOf( ".pdf" ) + 4 );

        if ( numberofPdfs > nextIndex )
        {
          pdfString = pdfString.substring( indexOccurence + 8, pdfString.length() );
        }

        QuizTakeLearningFile file = new QuizTakeLearningFile();
        file.setId( Long.parseLong( String.valueOf( nextIndex ) ) );
        file.setUrl( pdfUrl );
        if ( pdfUrl != null )
        {
          if ( pdfUrl.lastIndexOf( "/" ) > 0 )
          {
            String originalFileName = pdfUrl.substring( pdfUrl.lastIndexOf( "/" ) + 1 );
            file.setOriginalFilename( originalFileName );
          }
        }
        file.setName( pdfDisplayName );
        files.add( file );
      }
      material.setFiles( files );
      material.setText( quizLearningDetail.getRightColumn() );
    }
    catch( Exception e )
    {
      log.error( "Error while parsing PDFs on prepare edit:" + e );
    }
  }

  private void buildImage( QuizTakeMaterial material, QuizLearningDetails quizLearningDetail )
  {
    material.setType( QuizFileUploadValue.TYPE_IMAGE );
    List<QuizTakeLearningFile> files = new ArrayList<QuizTakeLearningFile>();
    QuizTakeLearningFile file = new QuizTakeLearningFile();
    file.setId( 1L );// Only one Image - not stored in DB

    String mediaContent = quizLearningDetail.getLeftColumn();
    int beginIndex = mediaContent.indexOf( "<img src=" ) + 10;
    int endIndex = mediaContent.indexOf( "\" alt" );

    String url = mediaContent.substring( beginIndex, endIndex );

    file.setUrl( url );
    if ( url != null )
    {
      if ( url.lastIndexOf( "/" ) > 0 )
      {
        String originalFileName = url.substring( url.lastIndexOf( "/" ) + 1 );
        file.setOriginalFilename( originalFileName );
        file.setName( originalFileName );
      }
    }
    files.add( file );
    material.setFiles( files );
    material.setText( quizLearningDetail.getRightColumn() );
  }

  private void buildVideo( QuizTakeMaterial material, QuizLearningDetails quizLearningDetail )
  {
    String videoUrlMp4 = quizLearningDetail.getVideoUrlMp4();
    String videoUrlWebm = quizLearningDetail.getVideoUrlWebm();
    String videoUrl3gp = quizLearningDetail.getVideoUrl3gp();
    String videoUrlOgg = quizLearningDetail.getVideoUrlOgg();
    String videoFileType = "";

    List<QuizTakeLearningFile> files = new ArrayList<QuizTakeLearningFile>();
    if ( !StringUtils.isEmpty( videoUrlMp4 ) )
    {
      videoFileType = "video/mp4";

      QuizTakeLearningFile file = new QuizTakeLearningFile();
      file.setId( 1L );
      file.setUrl( videoUrlMp4 );
      file.setType( videoFileType );
      file.setName( "mp4" );
      files.add( file );
    }
    if ( !StringUtils.isEmpty( videoUrlWebm ) )
    {
      videoFileType = "video/webm";

      QuizTakeLearningFile file = new QuizTakeLearningFile();
      file.setId( 2L );
      file.setUrl( videoUrlWebm );
      file.setType( videoFileType );
      file.setName( "webm" );
      files.add( file );
    }
    if ( !StringUtils.isEmpty( videoUrlOgg ) )
    {
      videoFileType = "video/ogg";

      QuizTakeLearningFile file = new QuizTakeLearningFile();
      file.setId( 3L );
      file.setUrl( videoUrlOgg );
      file.setType( videoFileType );
      file.setName( "ogg" );
      files.add( file );
    }
    if ( !StringUtils.isEmpty( videoUrl3gp ) )
    {
      videoFileType = "video/3gpp";

      QuizTakeLearningFile file = new QuizTakeLearningFile();
      file.setId( 4L );
      file.setUrl( videoUrl3gp );
      file.setType( videoFileType );
      file.setName( "3gpp" );
      files.add( file );
    }
    material.setFiles( files );
    material.setType( QuizFileUploadValue.TYPE_VIDEO );
    material.setText( quizLearningDetail.getRightColumn() );
  }

  private void buildDIYVideo( QuizTakeMaterial material, QuizLearningDetails quizLearningDetail )
  {
    String videoUrlMp4 = quizLearningDetail.getVideoUrlMp4();
    String videoUrlWebm = quizLearningDetail.getVideoUrlWebm();
    String videoUrl3gp = quizLearningDetail.getVideoUrl3gp();
    String videoUrlOgg = quizLearningDetail.getVideoUrlOgg();
    String videoFileType = "";
    String videoUrl = "";

    if ( !StringUtils.isEmpty( videoUrlMp4 ) )
    {
      videoFileType = "video/mp4";
      videoUrl = videoUrlMp4;
    }
    else if ( !StringUtils.isEmpty( videoUrlWebm ) )
    {
      videoFileType = "video/webm";
      videoUrl = videoUrlWebm;
    }
    else if ( !StringUtils.isEmpty( videoUrlOgg ) )
    {
      videoFileType = "video/ogg";
      videoUrl = videoUrlOgg;
    }
    else if ( !StringUtils.isEmpty( videoUrl3gp ) )
    {
      videoFileType = "video/3gpp";
      videoUrl = videoUrl3gp;
    }

    material.setType( QuizFileUploadValue.TYPE_VIDEO );
    List<QuizTakeLearningFile> files = new ArrayList<QuizTakeLearningFile>();
    QuizTakeLearningFile file = new QuizTakeLearningFile();
    file.setId( 1L );// Only one Video - not stored in database
    file.setUrl( videoUrl );
    file.setType( videoFileType );
    file.setName( "" );
    files.add( file );
    material.setFiles( files );
    material.setText( quizLearningDetail.getRightColumn() );
  }

  protected Quiz getQuizFromPromotion( HttpServletRequest request, QuizPromotion promotion ) throws Exception
  {
    Quiz quiz = promotion.getQuiz();
    // For DIY quiz promotion.getQuiz is null
    if ( promotion.isDIYQuizPromotion() && request.getParameter( "quizId" ) != null )
    {
      Long quizId = Long.parseLong( (String)request.getParameter( "quizId" ) );
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new QuizAssociationRequest( QuizAssociationRequest.QUIZ_QUESTION_ANSWER ) );
      quiz = getDIYQuizService().getQuizByIdWithAssociations( quizId, associationRequestCollection );
    }

    return quiz;
  }

  public ActionForward checkQuizEligibility( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    List<QuizPageValueBean> eligibleQuizList = null;
    try
    {
      eligibleQuizList = getPromotionService().getPendingQuizSubmissionList( UserManager.getUserId() );
    }
    catch( Exception e )
    {
      log.error( "Error: " + e );
    }
    if ( eligibleQuizList != null && eligibleQuizList.size() > 0 )
    {
      request.getSession().setAttribute( "eligibleQuizList", eligibleQuizList );
      return mapping.findForward( "quiz" );
    }
    else
    {
      return mapping.findForward( "home" );
    }
  }

  /**
   * When user clicks on start quiz button or next question button is clicked this method will execute
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward fetchQuestion( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long quizId = null;

    QuizClaimSubmissionForm claimSubmissionForm = (QuizClaimSubmissionForm)form;

    Participant submitter = getParticipantService().getParticipantById( UserManager.getUserId() );

    Promotion promotion = getPromotion( getPromotionId( request ) );
    QuizPromotion quizPromotion = getQuizPromotion( promotion.getId() );

    Quiz quiz = getQuizFromPromotion( request, quizPromotion );

    claimSubmissionForm.setPromotionId( promotion.getId().toString() );

    if ( promotion.isDIYQuizPromotion() )
    {
      quizId = Long.parseLong( request.getParameter( "quizId" ) );
    }

    if ( quizPromotion.isDIYQuizPromotion() )
    {
      quizId = quiz.getId();
    }
    QuizClaim claim = getOpenClaimIfAny( claimSubmissionForm, quizId );

    // Check if the participant already passed the quiz
    boolean isPassedQuiz = isPassedQuizAlready( Long.parseLong( claimSubmissionForm.getPromotionId() ), quizId, request, response );
    if ( isPassedQuiz )
    {
      WebErrorMessageList messages = new WebErrorMessageList();
      WebErrorMessage message = new WebErrorMessage();
      message = WebErrorMessage.addServerCmd( message );
      message.setUrl( RequestUtils.getBaseURI( request ) + PageConstants.QUIZ_LIST_PAGE_URL );
      messages.getMessages().add( message );
      super.writeAsJsonToResponse( messages, response );
      return null;
    }

    // Create a new quiz claim object when there is no existing claim
    claim = createNewQuizClaim( submitter, promotion, quiz, claim );

    // load the next active question
    loadNextActiveQuestion( claim, quiz );

    // save the claim when no open claims exists
    saveQuizClaim( claim );

    // Load the claim from data base with associations
    claim = getClaim( claim.getId() );

    QuizTakeQuestionView view = new QuizTakeQuestionView( claim, false );
    super.writeAsJsonToResponse( view, response );
    return null;
  }

  protected void saveQuizClaim( QuizClaim claim )
  {
    if ( claim.getId() == null )
    {
      try
      {
        getClaimService().saveClaim( claim, null, null, false, true );
      }
      catch( ServiceErrorException e )
      {
        log.error( "Error:" + e );
      }
    }
  }

  protected QuizClaim createNewQuizClaim( Participant submitter, Promotion promotion, Quiz quiz, QuizClaim claim )
  {
    if ( claim == null )
    {
      claim = new QuizClaim();
      if ( promotion.isDIYQuizPromotion() )
      {
        claim.setQuiz( quiz );
      }
      claim.setOpen( true );
      claim.setPromotion( promotion );
      claim.setQuestionCount( quiz.getActualNumberOfQuestionsAsked() );
      claim.setPassingScore( quiz.getPassingScore() );
      claim.setSubmitter( submitter );
      claim.setSubmissionDate( UserManager.getCurrentDateWithTimeZoneID() );
      claim.setNode( submitter.getPrimaryUserNode().getNode() );
    }
    return claim;
  }

  /**
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws IOException
   */
  public ActionForward saveAnswer( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    QuizClaimSubmissionForm claimSubmissionForm = (QuizClaimSubmissionForm)form;
    boolean pass = false;
    QuizTakeCertificate certificate = null;

    Long claimId = getClaimId( claimSubmissionForm, request );
    QuizClaim claim = getClaim( claimId );
    claimSubmissionForm.setClaimId( claim.getId().toString() );
    Long selectedAnswerId = new Long( claimSubmissionForm.getSelectedAnswer() );

    QuizQuestionAnswer selectedAnswer = getQuizService().getQuizQuestionAnswerById( selectedAnswerId );
    QuizResponse quizResponse = new QuizResponse();
    quizResponse.setQuizQuestion( claim.getCurrentQuizQuestion() );
    quizResponse.setSelectedQuizQuestionAnswer( selectedAnswer );
    quizResponse.setSequenceNumber( claim.getQuizResponses().size() );
    quizResponse.setCorrect( Boolean.valueOf( selectedAnswer.isCorrect() ) );

    claim.addQuizResponse( quizResponse );

    if ( BooleanUtils.isTrue( quizResponse.getCorrect() ) )
    {
      claim.setScore( claim.getScore() + 1 );
    }
    claim.setSubmissionDate( new Date() );

    Quiz quiz = getQuizFromPromotion( request, (QuizPromotion)claim.getPromotion() );

    // Switch current quiz question if not finished with test
    if ( !claim.isQuizComplete() )
    {
      loadNextActiveQuestion( claim, quiz );
    }
    else
    {
      // test complete - set pass/fail
      pass = claim.getScore() >= claim.getPassingScore();
      claim.setPass( Boolean.valueOf( pass ) );
    }

    QuizPromotion promotion = (QuizPromotion)claim.getPromotion();
    Long points = null;

    if ( UserManager.getUser().isOptOutOfAward() )
    {
      points = 0L;
    }
    else if ( promotion.isAwardActive() && !promotion.isBudgetUsed() )
    {
      points = promotion.getAwardAmount();
    }

    if ( pass )
    {
      QuizClaimItem quizClaimItem = new QuizClaimItem();

      if ( !UserManager.getUser().isOptOutOfAward() )
      {
        points = getAwardAmount( promotion, points );
      }

      // quizCheckBadge( );
      quizClaimItem.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.PASSED ) );
      quizClaimItem.setDateTaken( new Date() );
      claim.addQuizClaimItem( quizClaimItem );
      request.getSession().removeAttribute( "alertsList" );
      refreshPointBalance( request ); // clear points stored in the session #53130

      String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
      if ( promotion.isDIYQuizPromotion() )
      {
        DIYQuiz diyQuiz = (DIYQuiz)claim.getQuiz();
        if ( diyQuiz != null && diyQuiz.getCertificate() != null )
        {
          String certificateUrl = buildQuizCertificateUrl( request, claim, siteUrlPrefix );
          certificate = new QuizTakeCertificate( new Long( diyQuiz.getCertificate().getCertificateId() ), siteUrlPrefix, certificateUrl );
        }
      }
      else if ( promotion.getCertificate() != null )
      {
        String certificateUrl = buildQuizCertificateUrl( request, claim, siteUrlPrefix );
        certificate = new QuizTakeCertificate( new Long( promotion.getCertificate() ), siteUrlPrefix, certificateUrl );
      }
    }
    else if ( !pass && claim.isQuizComplete() )
    {
      QuizClaimQueryConstraint claimQueryConstraint = new QuizClaimQueryConstraint();
      claimQueryConstraint.setSubmitterId( claim.getSubmitter().getId() );
      claimQueryConstraint.setIncludedPromotionIds( new Long[] { promotion.getId() } );

      if ( promotion.isDIYQuizPromotion() )
      {
        claimQueryConstraint.setDiyQuizId( claim.getQuiz().getId() );
        int quizClaimCountForThisSubmitter = getClaimService().getClaimListCount( claimQueryConstraint );
        DIYQuiz diyQuiz = (DIYQuiz)claim.getQuiz();
        if ( !diyQuiz.isAllowUnlimitedAttempts() )
        {
          int attemptsRemaining = diyQuiz.getMaximumAttempts() - quizClaimCountForThisSubmitter;
          if ( attemptsRemaining < 1 )
          {
            request.getSession().removeAttribute( "alertsList" );
          }
        }
      }
      else
      {
        int quizClaimCountForThisSubmitter = getClaimService().getClaimListCount( claimQueryConstraint );
        if ( !promotion.isAllowUnlimitedAttempts() )
        {
          int attemptsRemaining = promotion.getMaximumAttempts() - quizClaimCountForThisSubmitter;
          if ( attemptsRemaining < 1 )
          {
            request.getSession().removeAttribute( "alertsList" );
          }
        }
      }
    }

    // Immediately update claim since we need to persist state of the question/answer process
    try
    {
      getClaimService().saveClaim( claim, null, null, false, true );
    }
    catch( ServiceErrorException e )
    {
      log.error( "Error: " + e );
    }

    // reload claim to insure we have fully hydrated version after save.
    claim = getClaim( claimId );
    request.setAttribute( "claimSubmissionForm", claimSubmissionForm );

    QuizTakeAnswerView view = new QuizTakeAnswerView( (QuizClaim)claim, true, certificate, points );
    super.writeAsJsonToResponse( view, response );
    return null;
  }

  private Long getAwardAmount( QuizPromotion promotion, Long points ) throws Exception
  {

    if ( !promotion.isDIYQuizPromotion() && promotion.isAwardActive() && promotion.isBudgetUsed() )
    {
      Budget budget = getPromotionService().getAvailableBudget( promotion.getId(), UserManager.getUserId(), null );
      if ( budget != null && budget.getCurrentValue() != null && promotion.getBudgetMaster() != null )
      {
        BudgetMaster budgetMaster = getBudgetMasterService().getBudgetMasterById( promotion.getBudgetMaster().getId(), new AssociationRequestCollection() );
        if ( budgetMaster != null )
        {
          long currentBudgetAvailable = budget.getCurrentValue().longValue();
          if ( currentBudgetAvailable > 0 )
          {
            if ( currentBudgetAvailable < promotion.getAwardAmount() )
            {
              if ( BudgetFinalPayoutRule.PARTIAL_PAYOUT.equals( budgetMaster.getFinalPayoutRule().getCode() ) )
              {
                points = currentBudgetAvailable;
              }
              else if ( BudgetFinalPayoutRule.NO_PAYOUT.equals( budgetMaster.getFinalPayoutRule().getCode() ) )
              {
                points = 0L;
              }
              else if ( BudgetFinalPayoutRule.FULL_PAYOUT.equals( budgetMaster.getFinalPayoutRule().getCode() ) )
              {
                points = promotion.getAwardAmount();
              }
            }
            else
            {
              points = promotion.getAwardAmount();
            }
          }
        }
      }
    }
    return points;
  }

  /**
   * 
   * @param request
   * @return Quiz Promotion
   * @throws Exception
   */
  private QuizPromotion getQuizPromotion( HttpServletRequest request ) throws Exception
  {
    // get promo id from request
    String promotionId = request.getParameter( "promotionId" );

    // get quiz promo with question and answer association
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.QUIZ_QUESTIONS_AND_ANSWERS ) );
    QuizPromotion quizPromotion = (QuizPromotion)getPromotionService().getPromotionByIdWithAssociations( Long.parseLong( promotionId ), associationRequestCollection );

    return quizPromotion;
  }

  /**
   * 
   * @param request
   * @param claim
   * @return String quizCertificate Url
   */
  private String buildQuizCertificateUrl( HttpServletRequest request, QuizClaim claim, String siteUrlPrefix )
  {
    Map<String, Long> paramMap = new HashMap<String, Long>();
    paramMap.put( "claimId", claim.getId() );
    paramMap.put( "promotionId", claim.getPromotion().getId() );
    paramMap.put( "userId", claim.getSubmitter().getId() );

    return ClientStateUtils.generateEncodedLink( siteUrlPrefix, PageConstants.QUIZ_CERTIFICATE_URL, paramMap );
  }

  /**
   * 
   * @param request
   * @return promotionId
   */
  private Long getPromotionId( HttpServletRequest request )
  {
    return new Long( request.getParameter( "promotionId" ) );
  }

  /**
   * 
   * @param promotionId
   * @return promotion
   */
  private Promotion getPromotion( Long promotionId )
  {
    Promotion promotion = null;
    AssociationRequestCollection assocRequestCollection = new AssociationRequestCollection();
    assocRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_BUDGET_MASTER ) );
    try
    {
      promotion = getPromotionService().getPromotionByIdWithAssociations( promotionId, assocRequestCollection );
    }
    catch( Exception e )
    {
      log.error( "Error: " + e );
    }
    return promotion;
  }

  /**
   * 
   * @param promotionId
   * @return quizPromotion
   */
  private QuizPromotion getQuizPromotion( Long promotionId )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.QUIZ_QUESTIONS_AND_ANSWERS ) );
    QuizPromotion quizPromotion = null;
    try
    {
      quizPromotion = (QuizPromotion)getPromotionService().getPromotionByIdWithAssociations( promotionId, associationRequestCollection );
    }
    catch( Exception e )
    {
      log.error( "Error: " + e );
    }
    return quizPromotion;
  }

  /**
   * 
   * @param claimId
   * @return quizClaim
   */
  private QuizClaim getClaim( Long claimId )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.QUIZ_RESPONSES ) );
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CURRENT_QUIZ_QUESTION ) );
    QuizClaim claim = (QuizClaim)getClaimService().getClaimByIdWithAssociations( claimId, associationRequestCollection );
    return claim;
  }

  /**
   * 
   * @param claim
   */
  private void loadNextActiveQuestion( QuizClaim claim, Quiz quiz )
  {
    List alreadyAskedQuestions = claim.getQuizResponsesAsQuestions();
    QuizQuestion nextActiveQuestion = getQuizService().getNextActiveQuestion( alreadyAskedQuestions, quiz.getId() );

    if ( nextActiveQuestion == null )
    {
      throw new BeaconRuntimeException( "No next active question found.  This method should only be called when there are existing questions "
          + "available, given the business rules about inactivating questions" );
    }
    claim.setCurrentQuizQuestion( nextActiveQuestion );
  }

  private Long getClaimId( QuizClaimSubmissionForm claimSubmissionForm, HttpServletRequest request )
  {
    Long claimId = null;

    if ( claimSubmissionForm != null && claimSubmissionForm.getClaimId() != null )
    {
      return Long.parseLong( claimSubmissionForm.getClaimId() );
    }
    else
    {
      return claimId;
    }
  }

  private QuizClaim getOpenClaimIfAny( QuizClaimSubmissionForm quizClaimSubmissionForm, Long quizId )
  {
    String claimId = quizClaimSubmissionForm.getClaimId();
    Long openClaimId = null;
    if ( !StringUtils.isEmpty( claimId ) )
    {
      openClaimId = Long.parseLong( claimId );
    }
    if ( openClaimId == null )
    {
      if ( quizId != null )
      {
        openClaimId = getClaimService().getOpenClaimByPromotionIdQuizIdAndUserId( Long.parseLong( quizClaimSubmissionForm.getPromotionId() ), UserManager.getUserId(), quizId );
      }
      else
      {
        openClaimId = getClaimService().getOpenClaimByPromotionIdAndUserId( Long.parseLong( quizClaimSubmissionForm.getPromotionId() ), UserManager.getUserId() );
      }
    }
    if ( openClaimId == null || openClaimId == 0 )
    {
      return null;
    }
    else
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.QUIZ_RESPONSES ) );
      associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CURRENT_QUIZ_QUESTION ) );
      QuizClaim claim = (QuizClaim)getClaimService().getClaimByIdWithAssociations( openClaimId, associationRequestCollection );
      return claim;
    }
  }

  private PromotionService getPromotionService() throws Exception
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private DIYQuizService getDIYQuizService() throws Exception
  {
    return (DIYQuizService)getService( DIYQuizService.BEAN_NAME );
  }

  private ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }

  private QuizService getQuizService()
  {
    return (QuizService)getService( QuizService.BEAN_NAME );
  }

  private GamificationService getGamificationService()
  {
    return (GamificationService)getService( GamificationService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private BudgetMasterService getBudgetMasterService()
  {
    return (BudgetMasterService)getService( BudgetMasterService.BEAN_NAME );
  }

}
