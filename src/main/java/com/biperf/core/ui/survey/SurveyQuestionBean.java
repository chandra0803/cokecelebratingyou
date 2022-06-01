
package com.biperf.core.ui.survey;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.biperf.core.domain.enums.SurveyResponseType;
import com.biperf.core.domain.promotion.SurveyQuestion;
import com.biperf.core.domain.promotion.SurveyQuestionResponse;
import com.biperf.core.domain.survey.ParticipantSurveyResponse;

public class SurveyQuestionBean
{
  private String id;
  private String number;
  private String type;
  private String answerLimit;
  private String text;
  private boolean isAnswered;
  private boolean isOptional;
  private String selectedAnswerId;

  private List<SurveyAnswerValueBean> answers = new ArrayList<SurveyAnswerValueBean>();
  private SurveySliderValueBean slider = new SurveySliderValueBean();

  public SurveyQuestionBean( SurveyQuestion surveyQuestion )
  {
    this.setId( surveyQuestion.getId().toString() );
    this.setText( surveyQuestion.getQuestionText() );
    this.setNumber( String.valueOf( surveyQuestion.getSequenceNum() + 1 ) );

    if ( surveyQuestion.getResponseType().getCode().equals( SurveyResponseType.OPEN_ENDED ) )
    {
      this.setType( "essay" );
      this.setIsOptional( !surveyQuestion.isOpenEndedRequired() );
    }
    else if ( surveyQuestion.getResponseType().getCode().equals( SurveyResponseType.STANDARD_RESPONSE ) )
    {
      this.setType( "radio" );
    }
    else
    {
      this.setType( "range" );
    }

    if ( surveyQuestion.getResponseType().getCode().equals( SurveyResponseType.STANDARD_RESPONSE ) )
    {
      if ( !CollectionUtils.isEmpty( surveyQuestion.getActiveSurveyQuestionResponses() ) )
      {
        for ( Iterator iter = surveyQuestion.getActiveSurveyQuestionResponses().iterator(); iter.hasNext(); )
        {
          SurveyQuestionResponse surveyQuestionResponse = (SurveyQuestionResponse)iter.next();
          if ( surveyQuestionResponse.isActive() )
          {
            SurveyAnswerValueBean formBean = new SurveyAnswerValueBean( surveyQuestionResponse );
            answers.add( formBean );
          }
        }
      }
      this.setAnswers( answers );
    }

    if ( surveyQuestion.getResponseType().getCode().equals( SurveyResponseType.SLIDER_SELECTION ) )
    {
      SurveySliderValueBean valueBean = new SurveySliderValueBean( surveyQuestion );
      this.setSlider( valueBean );
      SurveyAnswerValueBean formBean = new SurveyAnswerValueBean();
      formBean.setText( surveyQuestion.getStartSelectionLabel() );
      formBean.setNumber( surveyQuestion.getStartSelectionValue().intValue() );
      formBean.setId( surveyQuestion.getStartSelectionValue() );
      answers.add( formBean );

      SurveyAnswerValueBean formBean1 = new SurveyAnswerValueBean();
      formBean1.setText( surveyQuestion.getEndSelectionLabel() );
      formBean1.setNumber( surveyQuestion.getEndSelectionValue().intValue() );
      formBean1.setId( surveyQuestion.getEndSelectionValue() );
      answers.add( formBean1 );

      this.setAnswers( answers );
    }
  }

  public SurveyQuestionBean( ParticipantSurveyResponse participantSurveyResponse, SurveyQuestion surveyQuestion )
  {
    this.setId( participantSurveyResponse.getSurveyQuestion().getId().toString() );
    this.setText( participantSurveyResponse.getSurveyQuestion().getQuestionText() );
    this.setNumber( String.valueOf( participantSurveyResponse.getSurveyQuestion().getSequenceNum() + 1 ) );

    if ( surveyQuestion.getResponseType().getCode().equals( SurveyResponseType.STANDARD_RESPONSE ) )
    {
      if ( !CollectionUtils.isEmpty( surveyQuestion.getActiveSurveyQuestionResponses() ) )
      {
        for ( Iterator iter1 = surveyQuestion.getActiveSurveyQuestionResponses().iterator(); iter1.hasNext(); )
        {
          SurveyQuestionResponse questionResponse = (SurveyQuestionResponse)iter1.next();
          SurveyAnswerValueBean formBean = new SurveyAnswerValueBean( questionResponse );
          if ( participantSurveyResponse.getSurveyQuestionResponse().getId().equals( questionResponse.getId() ) )
          {
            formBean.setIsChosen( true );
          }
          answers.add( formBean );
        }
        this.setIsAnswered( true );
      }
      this.setType( "radio" );
    }
    else if ( surveyQuestion.getResponseType().getCode().equals( SurveyResponseType.OPEN_ENDED ) )
    {
      this.setType( "essay" );
      this.setIsOptional( !surveyQuestion.isOpenEndedRequired() );

      if ( participantSurveyResponse.getOpenEndedResponse() != null && !participantSurveyResponse.getOpenEndedResponse().equals( "" ) )
      {
        SurveyAnswerValueBean formBean = new SurveyAnswerValueBean();
        formBean.setText( participantSurveyResponse.getOpenEndedResponse() );
        formBean.setId( participantSurveyResponse.getId() );
        formBean.setNumber( participantSurveyResponse.getId().intValue() );

        answers.add( formBean );
        this.setIsAnswered( true );
      }
    }
    else if ( surveyQuestion.getResponseType().getCode().equals( SurveyResponseType.SLIDER_SELECTION ) )
    {
      SurveySliderValueBean valueBean = new SurveySliderValueBean( surveyQuestion, participantSurveyResponse );
      this.setSlider( valueBean );
      SurveyAnswerValueBean formBean = new SurveyAnswerValueBean();
      formBean.setText( surveyQuestion.getStartSelectionLabel() );
      formBean.setNumber( surveyQuestion.getStartSelectionValue().intValue() );
      formBean.setId( surveyQuestion.getStartSelectionValue() );
      answers.add( formBean );

      SurveyAnswerValueBean formBean1 = new SurveyAnswerValueBean();
      formBean1.setText( surveyQuestion.getEndSelectionLabel() );
      formBean1.setNumber( surveyQuestion.getEndSelectionValue().intValue() );
      formBean1.setId( surveyQuestion.getEndSelectionValue() );
      answers.add( formBean1 );

      if ( valueBean.getValue() != null )
      {
        this.setIsAnswered( true );
      }

      this.setAnswers( answers );
      this.setType( "range" );
    }
  }

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getNumber()
  {
    return number;
  }

  public void setNumber( String number )
  {
    this.number = number;
  }

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public String getAnswerLimit()
  {
    return answerLimit;
  }

  public void setAnswerLimit( String answerLimit )
  {
    this.answerLimit = answerLimit;
  }

  public String getText()
  {
    return text;
  }

  public void setText( String text )
  {
    this.text = text;
  }

  public boolean isIsAnswered()
  {
    return isAnswered;
  }

  public void setIsAnswered( boolean isAnswered )
  {
    this.isAnswered = isAnswered;
  }

  public List<SurveyAnswerValueBean> getAnswers()
  {
    return answers;
  }

  public void setAnswers( List<SurveyAnswerValueBean> answers )
  {
    this.answers = answers;
  }

  public boolean isIsOptional()
  {
    return isOptional;
  }

  public void setIsOptional( boolean isOptional )
  {
    this.isOptional = isOptional;
  }

  public String getSelectedAnswerId()
  {
    return selectedAnswerId;
  }

  public void setSelectedAnswerId( String selectedAnswerId )
  {
    this.selectedAnswerId = selectedAnswerId;
  }

  public SurveySliderValueBean getSlider()
  {
    return slider;
  }

  public void setSlider( SurveySliderValueBean slider )
  {
    this.slider = slider;
  }

}
