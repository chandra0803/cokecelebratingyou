
package com.biperf.core.ui.survey;

import com.biperf.core.domain.promotion.SurveyQuestionResponse;

public class SurveyAnswerValueBean
{
  public SurveyAnswerValueBean()
  {

  }

  private int number;
  private Long id;
  private String text;
  private String comment;
  private boolean isChosen;
  private String count;
  private String percent;

  public SurveyAnswerValueBean( SurveyQuestionResponse surveyQuestionResponse )
  {
    if ( surveyQuestionResponse.isActive() )
    {
      this.setId( surveyQuestionResponse.getId() );
      this.setNumber( surveyQuestionResponse.getSequenceNum() );
      this.setText( surveyQuestionResponse.getQuestionResponseText() );
      this.setCount( String.valueOf( surveyQuestionResponse.getResponseCount() ) );
    }
  }

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

  public String getComment()
  {
    return comment;
  }

  public void setComment( String comment )
  {
    this.comment = comment;
  }

  public boolean isIsChosen()
  {
    return isChosen;
  }

  public void setIsChosen( boolean isChosen )
  {
    this.isChosen = isChosen;
  }

  public String getCount()
  {
    return count;
  }

  public void setCount( String count )
  {
    this.count = count;
  }

  public String getPercent()
  {
    return percent;
  }

  public void setPercent( String percent )
  {
    this.percent = percent;
  }

}
