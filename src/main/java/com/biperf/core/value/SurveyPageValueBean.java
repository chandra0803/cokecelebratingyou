
package com.biperf.core.value;

import java.io.Serializable;

import com.biperf.core.domain.promotion.Promotion;

public class SurveyPageValueBean implements Serializable
{
  private static final long serialVersionUID = 2L;
  private Promotion promotion;
  private long surveyId;
  private boolean takeSurvey = false;
  private boolean resumeSurvey = false;
  private boolean surveyCompleted = false;
  private int timeRemaining;

  public SurveyPageValueBean()
  {
  }

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public long getSurveyId()
  {
    return surveyId;
  }

  public void setSurveyId( long surveyId )
  {
    this.surveyId = surveyId;
  }

  public boolean isTakeSurvey()
  {
    return takeSurvey;
  }

  public void setTakeSurvey( boolean takeSurvey )
  {
    this.takeSurvey = takeSurvey;
  }

  public boolean isResumeSurvey()
  {
    return resumeSurvey;
  }

  public void setResumeSurvey( boolean resumeSurvey )
  {
    this.resumeSurvey = resumeSurvey;
  }

  public boolean isSurveyCompleted()
  {
    return surveyCompleted;
  }

  public void setSurveyCompleted( boolean surveyCompleted )
  {
    this.surveyCompleted = surveyCompleted;
  }

  public int getTimeRemaining()
  {
    return timeRemaining;
  }

  public void setTimeRemaining( int timeRemaining )
  {
    this.timeRemaining = timeRemaining;
  }

}
