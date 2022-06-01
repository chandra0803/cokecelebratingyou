
package com.biperf.core.ui.quiz;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.claim.QuizClaim;
import com.biperf.core.domain.claim.QuizResponse;
import com.biperf.core.domain.diyquiz.DIYQuiz;
import com.biperf.core.domain.enums.QuizType;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.quiz.Quiz;
import com.biperf.core.domain.quiz.QuizQuestionAnswer;
import com.biperf.core.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.objectpartners.cms.util.CmsResourceBundle;

public class QuizTakeQuizView
{
  private Long id;
  private Long quizId;
  private Long claimId;
  private String promotionName;
  private String name;
  private String startDate;
  private String endDate;
  private String introText;
  private int passingScore;
  private int allowedAttempts;
  private boolean attemptsLimited;
  private boolean randomQuestionOrder;
  private int attemptsUsed;
  private String quizAward;
  private int totalQuestions;
  private Long currentQuestion;
  private String retakeQuizUrl;

  private QuizTakeBadge badge = new QuizTakeBadge();
  private List<QuizTakeMaterial> materials = new ArrayList<QuizTakeMaterial>();
  // Previously answered questions
  private List<QuizTakeQuestion> questions = new ArrayList<QuizTakeQuestion>();

  public QuizTakeQuizView()
  {
  }

  public QuizTakeQuizView( QuizPromotion quizPromotion, Quiz quiz, QuizClaim quizClaim, QuizTakeBadge quizTakeBadge, List<QuizTakeMaterial> materials, int attemptsUsed, String retakeQuizUrl )
  {
    setId( quizPromotion.getId() );
    if ( quizPromotion.isDIYQuizPromotion() )
    {
      DIYQuiz diyQuiz = (DIYQuiz)quiz;
      setName( diyQuiz.getName() );
      setStartDate( DateUtils.toDisplayString( diyQuiz.getStartDate() ) );
      setEndDate( DateUtils.toDisplayString( diyQuiz.getEndDate() ) );
      setIntroText( diyQuiz.getIntroductionText() );
      setAllowedAttempts( diyQuiz.getMaximumAttempts() );
      setAttemptsLimited( !diyQuiz.isAllowUnlimitedAttempts() );
    }
    else
    {
      setName( quizPromotion.getName() );
      setStartDate( DateUtils.toDisplayString( quizPromotion.getSubmissionStartDate() ) );
      setEndDate( DateUtils.toDisplayString( quizPromotion.getSubmissionEndDate() ) );
      setIntroText( quizPromotion.getOverviewDetailsText() );
      setAllowedAttempts( quizPromotion.getMaximumAttempts() );
      setAttemptsLimited( !quizPromotion.isAllowUnlimitedAttempts() );
    }

    setPassingScore( quiz.getPassingScore() );
    setRandomQuestionOrder( quiz.getQuizType().getCode().equals( QuizType.RANDOM ) );
    setAttemptsUsed( attemptsUsed );
    setRetakeQuizUrl( retakeQuizUrl );

    String quizAward = "";
    if ( quizPromotion.isAwardActive() && quizPromotion.getAwardAmount() != null && quizPromotion.getAwardAmount() > 0 )
    {
      quizAward = quizPromotion.getAwardAmount() + " " + CmsResourceBundle.getCmsBundle().getString( "claims.quiz.submission.review.POINTS" );
    }
    setQuizAward( quizAward );

    // badge
    setBadge( quizTakeBadge );

    // quizMaterials
    setMaterials( materials );

    setTotalQuestions( quiz.getActualNumberOfQuestionsAsked() );

    // The following is to populate the previously answered questions
    if ( quizClaim != null )
    {
      setClaimId( quizClaim.getId() );
      int index = 0;
      for ( Object object : quizClaim.getQuizResponses() )
      {
        QuizTakeQuestion quizTakeQuestion = new QuizTakeQuestion();
        QuizResponse quizResponse = (QuizResponse)object;
        quizTakeQuestion.setClaimId( quizClaim.getId() );
        quizTakeQuestion.setSelectedAnswerId( quizResponse.getSelectedQuizQuestionAnswer().getId() );
        quizTakeQuestion.setAnsweredCorrectly( quizResponse.getCorrect() );
        quizTakeQuestion.setId( quizResponse.getQuizQuestion().getId() );
        quizTakeQuestion.setNumber( index );
        quizTakeQuestion.setText( quizResponse.getQuizQuestion().getQuestionText() );
        index++;
        int answerIndex = 0;
        for ( QuizQuestionAnswer answer : quizResponse.getQuizQuestion().getQuizQuestionAnswers() )
        {
          QuizTakeQuestionAnswer questionAnswer = new QuizTakeQuestionAnswer();
          questionAnswer.setNumber( answerIndex );
          questionAnswer.setId( answer.getId() );
          questionAnswer.setText( answer.getQuestionAnswerText() );
          questionAnswer.setCorrect( answer.isCorrect() );
          questionAnswer.setExp( answer.getQuestionAnswerExplanation() );
          quizTakeQuestion.getAnswers().add( questionAnswer );
          answerIndex++;
        }
        questions.add( quizTakeQuestion );
      }
      setCurrentQuestion( quizClaim.getCurrentQuizQuestion().getId() );
    }

  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public Long getQuizId()
  {
    return quizId;
  }

  public void setQuizId( Long quizId )
  {
    this.quizId = quizId;
  }

  @JsonIgnore
  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
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

  public int getAllowedAttempts()
  {
    return allowedAttempts;
  }

  public void setAllowedAttempts( int allowedAttempts )
  {
    this.allowedAttempts = allowedAttempts;
  }

  @JsonProperty( "isAttemptsLimit" )
  public boolean isAttemptsLimited()
  {
    return attemptsLimited;
  }

  public void setAttemptsLimited( boolean attemptsLimited )
  {
    this.attemptsLimited = attemptsLimited;
  }

  @JsonProperty( "isRandomQuestionOrder" )
  public boolean isRandomQuestionOrder()
  {
    return randomQuestionOrder;
  }

  public void setRandomQuestionOrder( boolean randomQuestionOrder )
  {
    this.randomQuestionOrder = randomQuestionOrder;
  }

  public int getAttemptsUsed()
  {
    return attemptsUsed;
  }

  public void setAttemptsUsed( int attemptsUsed )
  {
    this.attemptsUsed = attemptsUsed;
  }

  public String getQuizAward()
  {
    return quizAward;
  }

  public void setQuizAward( String quizAward )
  {
    this.quizAward = quizAward;
  }

  public QuizTakeBadge getBadge()
  {
    return badge;
  }

  public void setBadge( QuizTakeBadge badge )
  {
    this.badge = badge;
  }

  public List<QuizTakeMaterial> getMaterials()
  {
    return materials;
  }

  public void setMaterials( List<QuizTakeMaterial> materials )
  {
    this.materials = materials;
  }

  public int getTotalQuestions()
  {
    return totalQuestions;
  }

  public void setTotalQuestions( int totalQuestions )
  {
    this.totalQuestions = totalQuestions;
  }

  public Long getCurrentQuestion()
  {
    return currentQuestion;
  }

  public void setCurrentQuestion( Long currentQuestion )
  {
    this.currentQuestion = currentQuestion;
  }

  public List<QuizTakeQuestion> getQuestions()
  {
    return questions;
  }

  public void setQuestions( List<QuizTakeQuestion> questions )
  {
    this.questions = questions;
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public String getRetakeQuizUrl()
  {
    return retakeQuizUrl;
  }

  public void setRetakeQuizUrl( String retakeQuizUrl )
  {
    this.retakeQuizUrl = retakeQuizUrl;
  }

}
