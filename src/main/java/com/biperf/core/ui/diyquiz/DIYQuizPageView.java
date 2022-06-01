
package com.biperf.core.ui.diyquiz;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.diyquiz.DIYQuiz;
import com.biperf.core.domain.diyquiz.DIYQuizParticipant;
import com.biperf.core.domain.enums.BadgeType;
import com.biperf.core.domain.enums.ClaimFormStatusType;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.PromotionCertificate;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.QuizQuestionStatusType;
import com.biperf.core.domain.enums.QuizType;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.gamification.BadgeLibrary;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.PromotionCert;
import com.biperf.core.domain.quiz.QuizLearningDetails;
import com.biperf.core.domain.quiz.QuizLearningObject;
import com.biperf.core.domain.quiz.QuizQuestion;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.quiz.QuizService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.ServiceLocator;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.QuizFileUploadValue;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * DIYQuizPageView.
 * 
 * @author kandhi
 * @since Jul 10, 2013
 * @version 1.0
 */
public class DIYQuizPageView
{
  private static final Log logger = LogFactory.getLog( DIYQuizPageView.class );
  private static final String CM_BADGE_NAME_HTML_KEY = "HTML_KEY";
  private String status; // database status
  private String quizStatus; // used by front end to determine read only
  private Long id;
  private String name;
  private String startDate;
  private String endDate;
  private String introText;
  private int passingScore;
  private boolean createAsAllowed;
  @JsonInclude( value = Include.NON_NULL )
  private DIYQuizParticipantView createAsParticipant;
  private int allowedAttempts;
  private boolean isAttemptsLimit;
  private boolean isRandomQuestionOrder;
  private Long badgeId;
  private Long certificateId;
  private boolean isNotifyParticipants;
  private String notifyText;

  private String clientState;
  private String cryptoPassword;
  private String source;
  private Long promotionId;

  private List<DIYQuizParticipantView> participants;
  private List<DIYQuizMaterialView> materials;
  private List<DIYQuizQuestionView> questions;
  private List<DIYQuizBadgeView> badges;
  private List<DIYQuizCertificateView> certificates;

  public DIYQuizPageView( Set<PromotionCert> promotionCertificates, Badge badge, Set<Participant> paxList, String siteUrlPrefix )
  {
    createBadgesView( badge, siteUrlPrefix );
    createCertificatesView( promotionCertificates, siteUrlPrefix );
    createNodeParticipantsView( paxList );
    if ( getAuthorizationService().isUserInRole( "ROLE_BI_ADMIN" ) )
    {
      this.createAsAllowed = true;
    }
    this.isAttemptsLimit = true;
  }

  /**
   *  Constructor called by prepare update and prepare copy methods of action
   */
  public DIYQuizPageView( Set<PromotionCert> promotionCertificates, Badge badge, DIYQuiz diyQuiz, String siteUrlPrefix, boolean isCopy )
  {
    // For copy all the id's, quiz name and dates need to be set to null
    if ( !isCopy )
    {
      this.id = diyQuiz.getId();
      this.name = diyQuiz.getName();
      this.startDate = DateUtils.toDisplayString( diyQuiz.getStartDate() );
      this.endDate = DateUtils.toDisplayString( diyQuiz.getEndDate() );
      this.createAsAllowed = false;

      // This status is used by front end to determine which fields are editable
      ClaimFormStatusType claimFormStatusType = diyQuiz.getClaimFormStatusType();
      if ( claimFormStatusType != null && ClaimFormStatusType.UNDER_CONSTRUCTION.equals( claimFormStatusType.getCode() ) )
      {
        this.quizStatus = "undrconstr";
      }
      else if ( claimFormStatusType != null && ClaimFormStatusType.COMPLETED.equals( claimFormStatusType.getCode() ) )
      {
        if ( org.apache.commons.lang3.time.DateUtils.isSameDay( DateUtils.getCurrentDate(), diyQuiz.getStartDate() ) || DateUtils.getCurrentDate().after( diyQuiz.getStartDate() ) )
        {
          this.quizStatus = "active";
        }
        else
        {
          this.quizStatus = "pending";
        }
      }
    }

    if ( diyQuiz.getBadgeRule() != null )
    {
      this.badgeId = diyQuiz.getBadgeRule().getId();
    }

    if ( diyQuiz.getCertificate() != null )
    {
      this.certificateId = diyQuiz.getCertificate().getId();
    }

    if ( !diyQuiz.isAllowUnlimitedAttempts() )
    {
      this.allowedAttempts = diyQuiz.getMaximumAttempts();
      this.isAttemptsLimit = true;
    }

    this.passingScore = diyQuiz.getPassingScore();
    this.introText = diyQuiz.getIntroductionText();

    QuizType quizType = diyQuiz.getQuizType();
    if ( QuizType.RANDOM.equals( quizType.getCode() ) )
    {
      this.isRandomQuestionOrder = true;
    }

    if ( !StringUtil.isEmpty( diyQuiz.getNotificationText() ) )
    {
      this.notifyText = diyQuiz.getNotificationText();
      this.isNotifyParticipants = true;
    }

    createBadgesView( badge, siteUrlPrefix );
    createCertificatesView( promotionCertificates, siteUrlPrefix );
    createDIYQuizParticipantsView( diyQuiz, isCopy );
    createQuizLearningObjects( diyQuiz, isCopy );
    if ( !isCopy )
    {
      createQuizOwner( diyQuiz );
    }

    if ( diyQuiz.getQuizQuestions() != null && diyQuiz.getQuizQuestions().size() > 0 )
    {
      questions = new ArrayList<DIYQuizQuestionView>( diyQuiz.getQuizQuestions().size() );
      int index = 0;
      int copyQuestionId = -100;
      for ( QuizQuestion quizQuestion : diyQuiz.getQuizQuestions() )
      {
        index++;
        if ( QuizQuestionStatusType.ACTIVE.equals( quizQuestion.getStatusType().getCode() ) )
        {
          DIYQuizQuestionView questionView = new DIYQuizQuestionView( quizQuestion, isCopy );
          if ( isCopy )
          {
            copyQuestionId--;
            // For copy quiz set id as some negative number - FE needs some id for questions
            // We are using negative numbers so that on save we can determine if it is an existing
            // question or a new one. If id greater than 0 then it is an existing one
            // Bug 4002 and 4004
            questionView.setId( Long.parseLong( String.valueOf( copyQuestionId ) ) );
          }
          else
          {
            questionView.setId( quizQuestion.getId() );
          }
          questionView.setText( quizQuestion.getQuestionText() );
          questionView.setCmAssetName( quizQuestion.getCmAssetName() );
          questionView.setNumber( index );
          questionView.setSaved( true );
          questionView.setNew( false );
          questions.add( questionView );
        }
      }
    }
  }

  private void createQuizOwner( DIYQuiz diyQuiz )
  {
    if ( diyQuiz.getOwner() != null )
    {
      createAsParticipant = new DIYQuizParticipantView();
      createAsParticipant.setId( diyQuiz.getOwner().getId() );
      createAsParticipant.setFirstName( diyQuiz.getOwner().getFirstName() );
      createAsParticipant.setLastName( diyQuiz.getOwner().getLastName() );
    }
  }

  private void createQuizLearningObjects( DIYQuiz diyQuiz, boolean isCopy )
  {
    if ( diyQuiz.getLearningObjects() != null && diyQuiz.getLearningObjects().size() > 0 )
    {
      materials = new ArrayList<DIYQuizMaterialView>();
      Iterator<QuizLearningObject> itr = diyQuiz.getLearningObjects().iterator();
      int copyMaterialId = -100;
      while ( itr.hasNext() )
      {
        QuizLearningObject learningObject = (QuizLearningObject)itr.next();
        if ( learningObject.getStatus().equals( QuizLearningObject.ACTIVE_STATUS ) )
        {
          DIYQuizMaterialView materialView = new DIYQuizMaterialView();
          if ( isCopy )
          {
            copyMaterialId--;
            // For copy quiz set id as some negative number - FE needs some id for materials
            // We are using negative numbers so that on save we can determine if it is an existing
            // material or a new one. If id greater than 0 then it is an existing one
            // Bug 4002 and 4004
            materialView.setId( Long.parseLong( String.valueOf( copyMaterialId ) ) );
          }
          else
          {
            materialView.setId( learningObject.getId() );
          }
          materialView.setCmContentResource( learningObject.getContentResourceCMCode() );
          materialView.setPageNumber( learningObject.getSlideNumber() );
          materialView.setStatus( learningObject.getStatus() );
          materialView.setIsSaved( true );
          materialView.setIsEditing( true );
          List<QuizLearningDetails> quizLearningDetails = getQuizService().getQuizLearningObjectforSlide( diyQuiz.getLearningObjects(), learningObject.getSlideNumber() );
          Iterator<QuizLearningDetails> quizLearningItr = quizLearningDetails.iterator();

          while ( quizLearningItr.hasNext() )
          {
            QuizLearningDetails quizLearningDetail = (QuizLearningDetails)quizLearningItr.next();
            if ( StringUtil.isEmpty( quizLearningDetail.getLeftColumn() ) )
            {
              materialView.setType( QuizFileUploadValue.TYPE_TEXT );
              materialView.setText( quizLearningDetail.getRightColumn() );
              // This will not create empty objects in the JSON
              materialView.setIgnore( true );
            }
            else
            {
              if ( quizLearningDetail.getLeftColumn().contains( "<p>" ) )
              {
                createImage( materialView, quizLearningDetail );
              }
              else if ( quizLearningDetail.getLeftColumn().contains( "PURLMainVideoWrapper" ) )
              {
                createVideo( materialView, quizLearningDetail );
              }
              else if ( quizLearningDetail.getLeftColumn().contains( "</a>" ) )
              {
                createPDF( materialView, quizLearningDetail );
              }
            }
          }
          materials.add( materialView );
        }
      }
    }
  }

  private void createVideo( DIYQuizMaterialView materialView, QuizLearningDetails quizLearningDetail )
  {
    materialView.setType( QuizFileUploadValue.TYPE_VIDEO );
    List<DIYQuizMaterialFileView> files = new ArrayList<DIYQuizMaterialFileView>();
    DIYQuizMaterialFileView file = new DIYQuizMaterialFileView();
    file.setId( 1L );// Only one Video - not stored in database
    file.setUrl( quizLearningDetail.getVideoUrl3gp() );
    file.setName( "" );
    files.add( file );
    materialView.setFiles( files );
    materialView.setText( quizLearningDetail.getRightColumn() );
  }

  private void createPDF( DIYQuizMaterialView materialView, QuizLearningDetails quizLearningDetail )
  {
    materialView.setType( QuizFileUploadValue.TYPE_PDF );

    int numberofPdfs = StringUtils.countMatches( quizLearningDetail.getLeftColumn(), "</a>" );
    String pdfString = quizLearningDetail.getLeftColumn();
    try
    {
      List<DIYQuizMaterialFileView> files = new ArrayList<DIYQuizMaterialFileView>();
      for ( int i = 0; i < numberofPdfs; i++ )
      {
        int nextIndex = i + 1;
        int indexOccurence = pdfString.indexOf( "</a>" );
        String pdfIndividual = pdfString.substring( 0, indexOccurence + 4 );
        int imageIndex = pdfIndividual.indexOf( "<img src=" );
        String pdfDisplayName = pdfIndividual.substring( pdfIndividual.indexOf( ".pdf" ) + 6, imageIndex );

        int pdfPathIndex = pdfIndividual.indexOf( "href=" );
        String pdfUrl = pdfIndividual.substring( pdfPathIndex + 6, pdfIndividual.indexOf( ".pdf" ) + 4 );

        if ( numberofPdfs > nextIndex )
        {
          pdfString = pdfString.substring( indexOccurence + 8, pdfString.length() );
        }

        DIYQuizMaterialFileView file = new DIYQuizMaterialFileView();
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
      materialView.setFiles( files );
      materialView.setText( quizLearningDetail.getRightColumn() );
    }
    catch( Exception e )
    {
      logger.error( "Error while parsing PDFs on prepare edit:" + e );
    }
  }

  private void createImage( DIYQuizMaterialView materialView, QuizLearningDetails quizLearningDetail )
  {
    materialView.setType( QuizFileUploadValue.TYPE_IMAGE );
    List<DIYQuizMaterialFileView> files = new ArrayList<DIYQuizMaterialFileView>();
    DIYQuizMaterialFileView file = new DIYQuizMaterialFileView();
    file.setId( 1L );// Only one Image - not stored in DB
    String url = quizLearningDetail.getFilePath();
    file.setUrl( url );
    if ( url != null )
    {
      if ( url.lastIndexOf( "/" ) > 0 )
      {
        String originalFileName = url.substring( url.lastIndexOf( "/" ) + 1 );
        file.setOriginalFilename( originalFileName );
      }
    }
    file.setName( "" );
    files.add( file );
    materialView.setFiles( files );
    materialView.setText( quizLearningDetail.getRightColumn() );
  }

  /**
   * Creates the participant view for the existing quiz participants
   * @param quiz
   */
  private void createDIYQuizParticipantsView( DIYQuiz quiz, boolean isCopy )
  {
    Set<DIYQuizParticipant> paxList = quiz.getParticipants();
    if ( paxList != null )
    {
      participants = new ArrayList<DIYQuizParticipantView>( paxList.size() );
      for ( DIYQuizParticipant diyParticipant : paxList )
      {
        Participant participant = diyParticipant.getParticipant();
        if ( ParticipantStatus.ACTIVE.equals( diyParticipant.getStatusType() ) )
        {
          if ( isCopy )
          {
            // Set diy_quiz_participant_id null for copy
            participants.add( new DIYQuizParticipantView( null,
                                                          participant.getId(),
                                                          participant.getFirstName(),
                                                          participant.getLastName(),
                                                          participant.getPaxOrgName(),
                                                          participant.getPrimaryCountryCode(),
                                                          participant.getPrimaryCountryName(),
                                                          participant.getAvatarSmall(),
                                                          participant.getDepartmentType(),
                                                          participant.getPositionType() ) );
          }
          else
          {
            participants.add( new DIYQuizParticipantView( diyParticipant.getId(),
                                                          participant.getId(),
                                                          participant.getFirstName(),
                                                          participant.getLastName(),
                                                          participant.getPaxOrgName(),
                                                          participant.getPrimaryCountryCode(),
                                                          participant.getPrimaryCountryName(),
                                                          participant.getAvatarSmall(),
                                                          participant.getDepartmentType(),
                                                          participant.getPositionType() ) );
          }
        }
      }
    }
  }

  /**
   * Creates the participants for user node of the quiz owner
   * @param paxList
   */
  private void createNodeParticipantsView( Set<Participant> paxList )
  {
    if ( paxList != null )
    {
      participants = new ArrayList<DIYQuizParticipantView>( paxList.size() );
      for ( Participant participant : paxList )
      {
        participants.add( new DIYQuizParticipantView( null,
                                                      participant.getId(),
                                                      participant.getFirstName(),
                                                      participant.getLastName(),
                                                      participant.getPaxOrgName(),
                                                      null,
                                                      null,
                                                      participant.getAvatarSmall(),
                                                      participant.getDepartmentType(),
                                                      participant.getPositionType() ) );
      }
    }
  }

  /**
   * Populate certificates dropdown
   * @param promotionCertificates
   */
  private void createCertificatesView( Set<PromotionCert> promotionCertificates, String siteUrlPrefix )
  {
    if ( promotionCertificates != null && !promotionCertificates.isEmpty() )
    {
      certificates = new ArrayList<DIYQuizCertificateView>( promotionCertificates.size() );
      List<Content> certificateList = PromotionCertificate.getList( PromotionType.DIY_QUIZ );
      for ( PromotionCert promotionCert : promotionCertificates )
      {
        for ( Content content : certificateList )
        {
          if ( content.getContentDataMap().get( "ID" ).equals( promotionCert.getCertificateId() ) )
          {
            certificates.add( new DIYQuizCertificateView( promotionCert.getId(),
                                                          (String)content.getContentDataMap().get( "NAME" ),
                                                          siteUrlPrefix + "/assets/img/certificates/" + (String)content.getContentDataMap().get( "THUMBNAIL_IMAGE" ),
                                                          siteUrlPrefix + "/assets/img/certificates/" + (String)content.getContentDataMap().get( "BACKGROUND_IMG" ) ) );
          }
        }
      }
    }
  }

  /**
   * Populate badges drop down
   * @param badge
   */
  private void createBadgesView( Badge badge, String siteUrlPrefix )
  {
    if ( badge != null && badge.getBadgeRules() != null && !badge.getBadgeRules().isEmpty() )
    {
      badges = new ArrayList<DIYQuizBadgeView>( badge.getBadgeRules().size() );
      Set<BadgeRule> badgeRulesList = badge.getBadgeRules();
      List<BadgeLibrary> badgeList = getPromotionService().buildBadgeLibraryList();
      for ( BadgeRule badgeRule : badgeRulesList )
      {
        for ( BadgeLibrary badgeLibrary : badgeList )
        {
          if ( badgeRule.getBadgeLibraryCMKey().equals( badgeLibrary.getBadgeLibraryId() ) )
          {
            String badgeName = CmsResourceBundle.getCmsBundle().getString( badgeRule.getBadgeName().trim(), CM_BADGE_NAME_HTML_KEY );
            badges.add( new DIYQuizBadgeView( badgeRule.getId(), badgeLibrary.getLibraryname(), badgeName, BadgeType.EARNED_OR_NOT_EARNED, siteUrlPrefix + badgeLibrary.getEarnedImageSmall() ) );
          }
        }
      }
    }
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public String getQuizStatus()
  {
    return quizStatus;
  }

  public void setQuizStatus( String quizStatus )
  {
    this.quizStatus = quizStatus;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getStartDate()
  {
    return startDate;
  }

  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

  public String getEndDate()
  {
    return endDate;
  }

  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

  public String getIntroText()
  {
    return introText;
  }

  public void setIntroText( String introText )
  {
    this.introText = introText;
  }

  public int getPassingScore()
  {
    return passingScore;
  }

  public void setPassingScore( int passingScore )
  {
    this.passingScore = passingScore;
  }

  public boolean isCreateAsAllowed()
  {
    return createAsAllowed;
  }

  public void setCreateAsAllowed( boolean createAsAllowed )
  {
    this.createAsAllowed = createAsAllowed;
  }

  public int getAllowedAttempts()
  {
    return allowedAttempts;
  }

  public void setAllowedAttempts( int allowedAttempts )
  {
    this.allowedAttempts = allowedAttempts;
  }

  @JsonProperty( "isAttemptsLimit" )
  public boolean getIsAttemptsLimit()
  {
    return isAttemptsLimit;
  }

  public void setIsAttemptsLimit( boolean isAttemptsLimit )
  {
    this.isAttemptsLimit = isAttemptsLimit;
  }

  @JsonProperty( "isRandomQuestionOrder" )
  public boolean getIsRandomQuestionOrder()
  {
    return isRandomQuestionOrder;
  }

  public void setIsRandomQuestionOrder( boolean isRandomQuestionOrder )
  {
    this.isRandomQuestionOrder = isRandomQuestionOrder;
  }

  public Long getBadgeId()
  {
    return badgeId;
  }

  public void setBadgeId( Long badgeId )
  {
    this.badgeId = badgeId;
  }

  public Long getCertificateId()
  {
    return certificateId;
  }

  public void setCertificateId( Long certificateId )
  {
    this.certificateId = certificateId;
  }

  @JsonProperty( "isNotifyParticipants" )
  public boolean getIsNotifyParticipants()
  {
    return isNotifyParticipants;
  }

  public void setIsNotifyParticipants( boolean isNotifyParticipants )
  {
    this.isNotifyParticipants = isNotifyParticipants;
  }

  public String getNotifyText()
  {
    return notifyText;
  }

  public void setNotifyText( String notifyText )
  {
    this.notifyText = notifyText;
  }

  public List<DIYQuizParticipantView> getParticipants()
  {
    return participants;
  }

  public void setParticipants( List<DIYQuizParticipantView> participants )
  {
    this.participants = participants;
  }

  public List<DIYQuizMaterialView> getMaterials()
  {
    return materials;
  }

  public void setMaterials( List<DIYQuizMaterialView> materials )
  {
    this.materials = materials;
  }

  public List<DIYQuizQuestionView> getQuestions()
  {
    return questions;
  }

  public void setQuestions( List<DIYQuizQuestionView> questions )
  {
    this.questions = questions;
  }

  public List<DIYQuizBadgeView> getBadges()
  {
    return badges;
  }

  public void setBadges( List<DIYQuizBadgeView> badges )
  {
    this.badges = badges;
  }

  public String getClientState()
  {
    return clientState;
  }

  public void setClientState( String clientState )
  {
    this.clientState = clientState;
  }

  public String getCryptoPassword()
  {
    return cryptoPassword;
  }

  public void setCryptoPassword( String cryptoPassword )
  {
    this.cryptoPassword = cryptoPassword;
  }

  public String getSource()
  {
    return source;
  }

  public void setSource( String source )
  {
    this.source = source;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public List<DIYQuizCertificateView> getCertificates()
  {
    return certificates;
  }

  public void setCertificates( List<DIYQuizCertificateView> certificates )
  {
    this.certificates = certificates;
  }

  public DIYQuizParticipantView getCreateAsParticipant()
  {
    return createAsParticipant;
  }

  public void setCreateAsParticipant( DIYQuizParticipantView createAsParticipant )
  {
    this.createAsParticipant = createAsParticipant;
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)ServiceLocator.getService( PromotionService.BEAN_NAME );
  }

  protected AuthorizationService getAuthorizationService()
  {
    return (AuthorizationService)ServiceLocator.getService( AuthorizationService.BEAN_NAME );
  }

  private QuizService getQuizService()
  {
    return (QuizService)ServiceLocator.getService( QuizService.BEAN_NAME );
  }

}
