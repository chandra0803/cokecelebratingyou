
package com.biperf.core.ui.diyquiz;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;

import com.biperf.core.domain.diyquiz.DIYQuiz;
import com.biperf.core.domain.diyquiz.DIYQuizParticipant;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.QuizQuestionStatusType;
import com.biperf.core.domain.enums.QuizType;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.PromotionCert;
import com.biperf.core.domain.quiz.QuizQuestion;
import com.biperf.core.domain.quiz.QuizQuestionAnswer;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.diyquiz.DIYQuizQuestionView.DIYQuizQuestionAnswerView;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.QuizFileUploadValue;

public class DIYQuizForm extends BaseForm
{
  private static final long serialVersionUID = 1L;
  private String method;
  private String name;
  private String startDate = DateUtils.displayDateFormatMask;
  private String endDate = DateUtils.displayDateFormatMask;
  private String introText;
  private String initializationJson = "";
  private Long promotionId;
  private Long id;
  private Long badgeId;
  private Long certificateId;
  private int passingScore;
  private int allowedAttempts;
  private boolean isAttemptsLimit;
  private boolean isRandomQuestionOrder;
  private boolean isNotifyParticipants;
  private String notifyText;
  private String type;
  private String text;
  private int pageNumber;
  private FormFile materialFile;
  private Long ownerId;
  private DIYQuizParticipantView createAsParticipant = new DIYQuizParticipantView();

  private String source;
  private String clientState;
  private String cryptoPassword;

  private String[] deleteQuizzesIncomplete;
  private String[] deleteQuizzesPending;
  private String[] deleteQuizzesActive;
  private String fileType;

  private ArrayList<DIYQuizParticipantView> participants = new ArrayList<DIYQuizParticipantView>();
  private ArrayList<DIYQuizQuestionView> questions = new ArrayList<DIYQuizQuestionView>();
  private ArrayList<DIYQuizMaterialView> materials = new ArrayList<DIYQuizMaterialView>();

  public String getFileType()
  {
    return fileType;
  }

  public void setFileType( String fileType )
  {
    this.fileType = fileType;
  }

  public String getSource()
  {
    return source;
  }

  public void setSource( String source )
  {
    this.source = source;
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

  public Long getOwnerId()
  {
    return ownerId;
  }

  public void setOwnerId( Long ownerId )
  {
    this.ownerId = ownerId;
  }

  public String[] getDeleteQuizzesIncomplete()
  {
    return deleteQuizzesIncomplete;
  }

  public void setDeleteQuizzesIncomplete( String[] deleteQuizzesIncomplete )
  {
    this.deleteQuizzesIncomplete = deleteQuizzesIncomplete;
  }

  public String[] getDeleteQuizzesPending()
  {
    return deleteQuizzesPending;
  }

  public void setDeleteQuizzesPending( String[] deleteQuizzesPending )
  {
    this.deleteQuizzesPending = deleteQuizzesPending;
  }

  public String[] getDeleteQuizzesActive()
  {
    return deleteQuizzesActive;
  }

  public void setDeleteQuizzesActive( String[] deleteQuizzesActive )
  {
    this.deleteQuizzesActive = deleteQuizzesActive;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
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

  public int getPassingScore()
  {
    return passingScore;
  }

  public void setPassingScore( int passingScore )
  {
    this.passingScore = passingScore;
  }

  public int getAllowedAttempts()
  {
    return allowedAttempts;
  }

  public void setAllowedAttempts( int allowedAttempts )
  {
    this.allowedAttempts = allowedAttempts;
  }

  public boolean getIsAttemptsLimit()
  {
    return isAttemptsLimit;
  }

  public void setIsAttemptsLimit( boolean isAttemptsLimit )
  {
    this.isAttemptsLimit = isAttemptsLimit;
  }

  public boolean getIsRandomQuestionOrder()
  {
    return isRandomQuestionOrder;
  }

  public void setIsRandomQuestionOrder( boolean isRandomQuestionOrder )
  {
    this.isRandomQuestionOrder = isRandomQuestionOrder;
  }

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

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public String getText()
  {
    return text;
  }

  public void setText( String text )
  {
    this.text = text;
  }

  public int getPageNumber()
  {
    return pageNumber;
  }

  public void setPageNumber( int pageNumber )
  {
    this.pageNumber = pageNumber;
  }

  public FormFile getMaterialFile()
  {
    return materialFile;
  }

  public void setMaterialFile( FormFile materialFile )
  {
    this.materialFile = materialFile;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public DIYQuizParticipantView getCreateAsParticipant()
  {
    return createAsParticipant;
  }

  public void setCreateAsParticipant( DIYQuizParticipantView createAsParticipant )
  {
    this.createAsParticipant = createAsParticipant;
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

  public String getInitializationJson()
  {
    return initializationJson;
  }

  public void setInitializationJson( String initializationJson )
  {
    this.initializationJson = initializationJson;
  }

  public ArrayList<DIYQuizParticipantView> getParticipantsAsList()
  {
    return participants;
  }

  public DIYQuizParticipantView getParticipants( int index )
  {
    while ( index >= participants.size() )
    {
      participants.add( new DIYQuizParticipantView() );
    }
    return participants.get( index );
  }

  public void setParticipantsAsList( DIYQuizParticipantView participant )
  {
    participants.add( participant );
  }

  public ArrayList<DIYQuizQuestionView> getQuestionsAsList()
  {
    return questions;
  }

  public DIYQuizQuestionView getQuestions( int index )
  {
    while ( index >= questions.size() )
    {
      questions.add( new DIYQuizQuestionView() );
    }
    return questions.get( index );
  }

  public void setQuestionsAsList( DIYQuizQuestionView question )
  {
    questions.add( question );
  }

  public ArrayList<DIYQuizMaterialView> getMaterialsAsList()
  {
    return materials;
  }

  public DIYQuizMaterialView getMaterials( int index )
  {
    while ( index >= materials.size() )
    {
      materials.add( new DIYQuizMaterialView() );
    }
    return materials.get( index );
  }

  public void setMaterialsAsList( DIYQuizMaterialView material )
  {
    materials.add( material );
  }

  protected void toDomainObj( DIYQuiz diyQuiz )
  {
    if ( getId() == null || getId() == 0 )
    {
      if ( createAsParticipant != null && createAsParticipant.getId() != null && createAsParticipant.getId() > 0 )
      {
        User owner = getUserService().getUserById( createAsParticipant.getId() );
        diyQuiz.setOwner( owner );
      }
      else
      {
        User owner = getUserService().getUserById( UserManager.getUserId() );
        diyQuiz.setOwner( owner );
      }
    }
    if ( id != null && !id.equals( 0L ) )
    {
      diyQuiz.setId( id );
    }
    diyQuiz.setStartDate( DateUtils.toDate( startDate ) );
    diyQuiz.setEndDate( DateUtils.toDate( endDate ) );
    diyQuiz.setAllowUnlimitedAttempts( !isAttemptsLimit );
    diyQuiz.setMaximumAttempts( allowedAttempts );
    diyQuiz.setName( name );
    diyQuiz.setIntroductionText( introText );
    diyQuiz.setPassingScore( passingScore );

    if ( isNotifyParticipants )
    {
      diyQuiz.setNotificationText( notifyText );
    }

    if ( isRandomQuestionOrder )
    {
      diyQuiz.setQuizType( QuizType.lookup( QuizType.RANDOM ) );
      if ( questions != null )
      {
        diyQuiz.setNumberOfQuestionsAsked( questions.size() );
      }
    }
    else
    {
      diyQuiz.setQuizType( QuizType.lookup( QuizType.FIXED ) );
    }

    if ( badgeId != null && badgeId != 0 )
    {
      BadgeRule badgeRule = new BadgeRule();
      badgeRule.setId( badgeId );
      diyQuiz.setBadgeRule( badgeRule );
    }

    if ( certificateId != null && certificateId != 0 )
    {
      PromotionCert promotionCert = new PromotionCert();
      promotionCert.setId( certificateId );
      diyQuiz.setCertificate( promotionCert );
    }

    for ( DIYQuizParticipantView formPax : participants )
    {
      Participant pax = getParticipantService().getParticipantById( formPax.getId() );
      DIYQuizParticipant diyQuizParticipant = new DIYQuizParticipant();
      // DIY_QUIZ_PARTICIPANT_ID column in DIYQUIZ_PARTICIPANT table not PAX_ID
      if ( formPax.getQuizParticipantId() != null && !formPax.getQuizParticipantId().equals( 0L ) )
      {
        diyQuizParticipant.setId( formPax.getQuizParticipantId() );
      }
      diyQuizParticipant.setStatusType( ParticipantStatus.ACTIVE );
      diyQuizParticipant.setParticipant( pax );
      diyQuiz.addParticipant( diyQuizParticipant );
    }

    for ( DIYQuizQuestionView question : questions )
    {
      QuizQuestion quizQuestion = new QuizQuestion();
      if ( question.getId() != null && question.getId() > 0 )
      {
        quizQuestion.setId( question.getId() );
      }
      quizQuestion.setSequenceNum( question.getNumber() );
      quizQuestion.setRequired( true );
      quizQuestion.setStatusType( QuizQuestionStatusType.lookup( QuizQuestionStatusType.ACTIVE ) );
      quizQuestion.setText( question.getText() );
      quizQuestion.setCmAssetName( question.getCmAssetName() );
      for ( DIYQuizQuestionAnswerView viewQuestionAnswer : question.getAnswers() )
      {
        if ( viewQuestionAnswer != null && !StringUtil.isEmpty( viewQuestionAnswer.getText() ) )
        {
          QuizQuestionAnswer quizQuestionAnswer = new QuizQuestionAnswer();
          if ( viewQuestionAnswer.getId() != null && viewQuestionAnswer.getId() > 0 )
          {
            quizQuestionAnswer.setId( viewQuestionAnswer.getId() );
          }
          quizQuestionAnswer.setCmAssetCode( viewQuestionAnswer.getCmAssetName() );
          quizQuestionAnswer.setText( viewQuestionAnswer.getText() );
          quizQuestionAnswer.setCorrect( viewQuestionAnswer.getIsCorrect() );
          quizQuestion.addQuizQuestionAnswer( quizQuestionAnswer );
        }
      }
      diyQuiz.addQuizQuestion( quizQuestion );
    }
  }

  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( mapping, request );
    if ( "save".equals( method ) || "saveDraft".equals( method ) )
    {
      if ( actionErrors == null )
      {
        actionErrors = new ActionErrors();
      }
      if ( DateUtils.toDate( startDate ) != null && DateUtils.toDate( startDate ).before( DateUtils.getCurrentDateTrimmed() ) )
      {
        if ( id == null || id.equals( 0L ) )
        { // Validate only when new quiz
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_END_BEFORE_TO_DATE ) );
        }
      }
      if ( DateUtils.toDate( endDate ) != null )
      {
        if ( DateUtils.toDate( startDate ) != null && DateUtils.toDate( endDate ) != null && DateUtils.toDate( startDate ).after( DateUtils.toDate( endDate ) ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_END_BEFORE_START_DATE ) );
        }
      }

      if ( introText != null && introText.length() > 4000 )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "quiz.diy.errors.INTRO_TEXT_SIZE_EXCEEDED" ) );
      }

      if ( notifyText != null && notifyText.length() > 4000 )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "quiz.diy.errors.INTRO_TEXT_SIZE_EXCEEDED" ) );
      }

      if ( questions != null && questions.size() > 20 )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "quiz.diy.errors.QUESTIONS_SIZE_EXCEEDED" ) );
      }

      if ( materials != null && materials.size() > 10 )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "quiz.diy.errors.MATERIALS_SIZE_EXCEEDED" ) );
      }

      if ( materials != null )
      {
        for ( DIYQuizMaterialView materialView : materials )
        {
          if ( QuizFileUploadValue.TYPE_PDF.equalsIgnoreCase( materialView.getType() ) )
          {
            if ( materialView.getFiles() != null )
            {
              if ( materialView.getFiles().size() > 10 )
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "quiz.diy.errors.PDFS_SIZE_EXCEEDED" ) );
              }
            }
          }
        }
      }
    }
    return actionErrors;
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)BeanLocator.getBean( ParticipantService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }

}
