
package com.biperf.core.ui.survey;

import com.biperf.core.domain.promotion.SurveyQuestion;
import com.biperf.core.domain.survey.ParticipantSurveyResponse;

public class SurveySliderValueBean
{
  public SurveySliderValueBean()
  {

  }

  private Integer min;
  private Integer max;
  private Double step;
  private Double value;
  private String tooltip;

  public SurveySliderValueBean( SurveyQuestion surveyQuestion )
  {
    this.setMin( surveyQuestion.getStartSelectionValue().intValue() );
    this.setMax( surveyQuestion.getEndSelectionValue().intValue() );
    this.setStep( surveyQuestion.getPrecisionValue().doubleValue() );
    this.setValue( null );
    this.setTooltip( "show" );
  }

  public SurveySliderValueBean( SurveyQuestion surveyQuestion, ParticipantSurveyResponse participantSurveyResponse )
  {
    this.setMin( surveyQuestion.getStartSelectionValue().intValue() );
    this.setMax( surveyQuestion.getEndSelectionValue().intValue() );
    this.setStep( surveyQuestion.getPrecisionValue().doubleValue() );
    this.setValue( participantSurveyResponse.getSliderResponse() != null ? participantSurveyResponse.getSliderResponse().doubleValue() : null );
    this.setTooltip( "show" );
  }

  public Integer getMin()
  {
    return min;
  }

  public void setMin( Integer min )
  {
    this.min = min;
  }

  public Integer getMax()
  {
    return max;
  }

  public void setMax( Integer max )
  {
    this.max = max;
  }

  public Double getStep()
  {
    return step;
  }

  public void setStep( Double step )
  {
    this.step = step;
  }

  public Double getValue()
  {
    return value;
  }

  public void setValue( Double value )
  {
    this.value = value;
  }

  public String getTooltip()
  {
    return tooltip;
  }

  public void setTooltip( String tooltip )
  {
    this.tooltip = tooltip;
  }

}
