
package com.biperf.core.ui.quiz;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuizTakeQuestionAnswer
{
  private Long id;
  private int number;
  private String text;
  private boolean correct;
  private String exp;

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

  @JsonProperty( "isCorrect" )
  public boolean isCorrect()
  {
    return correct;
  }

  public void setCorrect( boolean correct )
  {
    this.correct = correct;
  }

  public String getExp()
  {
    return exp;
  }

  public void setExp( String exp )
  {
    this.exp = exp;
  }

}
