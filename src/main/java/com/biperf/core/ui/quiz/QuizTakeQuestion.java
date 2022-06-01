
package com.biperf.core.ui.quiz;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.claim.QuizClaim;
import com.biperf.core.domain.quiz.QuizQuestionAnswer;

public class QuizTakeQuestion
{
  private Long id;
  private int number;
  private String text;
  private List<QuizTakeQuestionAnswer> answers = new ArrayList<QuizTakeQuestionAnswer>();
  private Long claimId;
  private Long selectedAnswerId;
  private boolean answeredCorrectly;

  private List<QuizTakeCorrectAnswer> correctAnswers = new ArrayList<QuizTakeCorrectAnswer>();
  private List<QuizTakeInCorrectAnswer> incorrectAnswers = new ArrayList<QuizTakeInCorrectAnswer>();

  public QuizTakeQuestion()
  {
    super();
  }

  public QuizTakeQuestion( QuizClaim quizClaim, boolean displayCorrect )
  {
    setId( quizClaim.getCurrentQuizQuestion().getId() );
    setClaimId( quizClaim.getId() );
    setText( quizClaim.getCurrentQuizQuestion().getQuestionText() );
    // Next question - Should be completed plus 1
    setNumber( quizClaim.getQuestionsCompletedCount() + 1 );
    int index = 0;
    for ( QuizQuestionAnswer answer : quizClaim.getCurrentQuizQuestion().getQuizQuestionAnswers() )
    {
      QuizTakeQuestionAnswer questionAnswer = new QuizTakeQuestionAnswer();
      questionAnswer.setNumber( index );
      questionAnswer.setId( answer.getId() );
      questionAnswer.setText( answer.getQuestionAnswerText() );
      if ( displayCorrect )
      {
        questionAnswer.setCorrect( answer.isCorrect() );
        questionAnswer.setExp( answer.getQuestionAnswerExplanation() );
        if ( answer.isCorrect() )
        {
          QuizTakeCorrectAnswer correctAnswer = new QuizTakeCorrectAnswer();
          correctAnswer.setId( answer.getId() );
          correctAnswer.setExp( answer.getQuestionAnswerExplanation() );
          correctAnswers.add( correctAnswer );
        }
      }
      answers.add( questionAnswer );
      index++;
    }
  }

  public QuizTakeQuestion( QuizClaim quizClaim, boolean displayLatestQuestion, boolean displayCorrect )
  {
    setId( quizClaim.getLatestQuestionAnswered().getId() );
    setClaimId( quizClaim.getId() );
    setText( quizClaim.getLatestQuestionAnswered().getQuestionText() );
    setNumber( quizClaim.getQuestionsCompletedCount() );
    int index = 0;
    for ( QuizQuestionAnswer answer : quizClaim.getLatestQuestionAnswered().getQuizQuestionAnswers() )
    {
      QuizTakeQuestionAnswer questionAnswer = new QuizTakeQuestionAnswer();
      questionAnswer.setNumber( index );
      questionAnswer.setId( answer.getId() );
      questionAnswer.setText( answer.getQuestionAnswerText() );
      if ( displayCorrect )
      {
        questionAnswer.setCorrect( answer.isCorrect() );
        questionAnswer.setExp( answer.getQuestionAnswerExplanation() );
        if ( quizClaim.getLatestQuestionResponse() != null && quizClaim.getLatestQuestionResponse().booleanValue() == Boolean.TRUE )
        {
          if ( answer.isCorrect() )
          {
            QuizTakeCorrectAnswer correctAnswer = new QuizTakeCorrectAnswer();
            correctAnswer.setId( answer.getId() );
            correctAnswer.setExp( answer.getQuestionAnswerExplanation() );
            correctAnswers.add( correctAnswer );
          }
        }
        else
        {
          if ( !answer.isCorrect() )
          {
            QuizTakeInCorrectAnswer inCorrectAnswer = new QuizTakeInCorrectAnswer();
            inCorrectAnswer.setId( answer.getId() );
            inCorrectAnswer.setExp( answer.getQuestionAnswerExplanation() );
            incorrectAnswers.add( inCorrectAnswer );
          }
        }
      }
      answers.add( questionAnswer );
      index++;
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

  public int getNumber()
  {
    return number;
  }

  public void setNumber( int number )
  {
    this.number = number;
  }

  public String getText()
  {
    return text;
  }

  public void setText( String text )
  {
    this.text = text;
  }

  public List<QuizTakeQuestionAnswer> getAnswers()
  {
    return answers;
  }

  public void setAnswers( List<QuizTakeQuestionAnswer> answers )
  {
    this.answers = answers;
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public Long getSelectedAnswerId()
  {
    return selectedAnswerId;
  }

  public void setSelectedAnswerId( Long selectedAnswerId )
  {
    this.selectedAnswerId = selectedAnswerId;
  }

  public boolean isAnsweredCorrectly()
  {
    return answeredCorrectly;
  }

  public void setAnsweredCorrectly( boolean answeredCorrectly )
  {
    this.answeredCorrectly = answeredCorrectly;
  }

  public List<QuizTakeCorrectAnswer> getCorrectAnswers()
  {
    return correctAnswers;
  }

  public void setCorrectAnswers( List<QuizTakeCorrectAnswer> correctAnswers )
  {
    this.correctAnswers = correctAnswers;
  }

  public List<QuizTakeInCorrectAnswer> getIncorrectAnswers()
  {
    return incorrectAnswers;
  }

  public void setIncorrectAnswers( List<QuizTakeInCorrectAnswer> incorrectAnswers )
  {
    this.incorrectAnswers = incorrectAnswers;
  }

}
