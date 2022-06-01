
package com.biperf.core.ui.diyquiz;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.quiz.QuizQuestion;
import com.biperf.core.domain.quiz.QuizQuestionAnswer;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * DIYQuizQuestionView.
 * 
 * @author kandhi
 * @since Jul 10, 2013
 * @version 1.0
 */
public class DIYQuizQuestionView
{

  private Long id;
  private int number;
  private String text;
  private boolean isNew;
  private boolean isSaved;
  private String cmAssetName;
  private List<DIYQuizQuestionAnswerView> answers = new ArrayList<DIYQuizQuestionAnswerView>();

  public DIYQuizQuestionView()
  {

  }

  public DIYQuizQuestionView( QuizQuestion quizQuestion, boolean isCopy )
  {
    if ( quizQuestion.getQuizQuestionAnswers() != null && quizQuestion.getQuizQuestionAnswers().size() > 0 )
    {
      int index = 0;
      int copyQuestionAnswerId = -100;
      for ( QuizQuestionAnswer quizQuestionAnswer : quizQuestion.getQuizQuestionAnswers() )
      {
        index++;
        DIYQuizQuestionAnswerView questionAnswerView = new DIYQuizQuestionAnswerView();
        if ( isCopy )
        {
          copyQuestionAnswerId--;
          // For copy quiz set id as some negative number - FE needs some id for question answers
          // We are using negative numbers so that on save we can determine if it is an existing
          // question answers or a new one. If id greater than 0 then it is an existing one
          // Bug 4002 and 4004
          questionAnswerView.setId( Long.parseLong( String.valueOf( copyQuestionAnswerId ) ) );
        }
        else
        {
          questionAnswerView.setId( quizQuestionAnswer.getId() );
        }
        questionAnswerView.setIsCorrect( quizQuestionAnswer.isCorrect() );
        questionAnswerView.setNumber( index );
        questionAnswerView.setText( quizQuestionAnswer.getQuestionAnswerText() );
        questionAnswerView.setCmAssetName( quizQuestionAnswer.getCmAssetCode() );
        answers.add( questionAnswerView );
      }
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

  @JsonProperty( "isNew" )
  public boolean isNew()
  {
    return isNew;
  }

  public void setNew( boolean isNew )
  {
    this.isNew = isNew;
  }

  @JsonProperty( "isSaved" )
  public boolean isSaved()
  {
    return isSaved;
  }

  public void setSaved( boolean isSaved )
  {
    this.isSaved = isSaved;
  }

  public String getCmAssetName()
  {
    return cmAssetName;
  }

  public void setCmAssetName( String cmAssetName )
  {
    this.cmAssetName = cmAssetName;
  }

  public List<DIYQuizQuestionAnswerView> getAnswers()
  {
    // Temp fix for index out of bounds set to size 10 after discussing with Pramod
    if ( answers.isEmpty() )
    {
      for ( int i = 0; i < 10; i++ )
      {
        answers.add( new DIYQuizQuestionAnswerView() );
      }
    }
    return answers;
  }

  public void setAnswers( List<DIYQuizQuestionAnswerView> answers )
  {
    this.answers = answers;
  }

  public DIYQuizQuestionAnswerView getAnswers( int index )
  {
    while ( index >= answers.size() )
    {
      answers.add( new DIYQuizQuestionAnswerView() );
    }
    return (DIYQuizQuestionAnswerView)answers.get( index );
  }

  public void setAnswers( DIYQuizQuestionAnswerView answer )
  {
    answers.add( answer );
  }

  public class DIYQuizQuestionAnswerView
  {
    private int number;
    private Long id;
    private String text;
    private boolean isCorrect;
    private String cmAssetName;

    public int getNumber()
    {
      return number;
    }

    public void setNumber( int number )
    {
      this.number = number;
    }

    public Long getId()
    {
      return id;
    }

    public void setId( Long id )
    {
      this.id = id;
    }

    public String getText()
    {
      return text;
    }

    public void setText( String text )
    {
      this.text = text;
    }

    @JsonProperty( "isCorrect" )
    public boolean getIsCorrect()
    {
      return isCorrect;
    }

    public void setIsCorrect( boolean isCorrect )
    {
      this.isCorrect = isCorrect;
    }

    public String getCmAssetName()
    {
      return cmAssetName;
    }

    public void setCmAssetName( String cmAssetName )
    {
      this.cmAssetName = cmAssetName;
    }

  }
}
