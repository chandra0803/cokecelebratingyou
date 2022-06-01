
package com.biperf.core.domain.promotion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.SurveyQuestionStatusType;
import com.biperf.core.domain.enums.SurveyResponseType;
import com.objectpartners.cms.util.CmsResourceBundle;

public class SurveyQuestion extends BaseDomain
{

  private Survey survey;
  private SurveyQuestionStatusType statusType;
  private String cmAssetName = "";
  private int sequenceNum;
  private String text;
  private List<SurveyQuestionResponse> surveyQuestionResponses = new ArrayList<SurveyQuestionResponse>();

  private SurveyResponseType responseType;
  private boolean openEndedRequired;

  private String startSelectionLabel;
  private String endSelectionLabel;
  private Long startSelectionValue;
  private Long endSelectionValue;
  private BigDecimal precisionValue;

  public SurveyQuestion()
  {
    super();
  }

  /**
   * Does a deep copy of the SurveyQuestion and its children if specified. This is a customized
   * implementation of java.lang.Object#clone()
   * 
   * @see java.lang.Object#clone()
   * @param cloneWithChildren
   * @return Object
   */
  public Object deepCopy( boolean cloneWithChildren )
  {
    SurveyQuestion question = new SurveyQuestion();
    question.setStatusType( this.getStatusType() );
    question.setSequenceNum( this.getSequenceNum() );
    question.setCmAssetName( this.getCmAssetName() );

    if ( cloneWithChildren )
    {
      Iterator iter = this.getSurveyQuestionResponses().iterator();
      while ( iter.hasNext() )
      {
        SurveyQuestionResponse responseToCopy = (SurveyQuestionResponse)iter.next();
        question.addSurveyQuestionResponse( (SurveyQuestionResponse)responseToCopy.deepCopy() );
      }
    }
    else
    {
      question.setSurveyQuestionResponses( new ArrayList() );
    }

    question.setResponseType( this.getResponseType() );
    question.setOpenEndedRequired( this.isOpenEndedRequired() );
    if ( this.getResponseType().getCode().equals( SurveyResponseType.SLIDER_SELECTION ) )
    {
      question.setStartSelectionLabel( this.getStartSelectionLabel() );
      question.setEndSelectionLabel( this.getEndSelectionLabel() );
      question.setStartSelectionValue( this.getStartSelectionValue() );
      question.setEndSelectionValue( this.getEndSelectionValue() );
      question.setPrecisionValue( this.getPrecisionValue() );
    }

    return question;
  }

  public List<SurveyQuestionResponse> getSurveyQuestionResponses()
  {
    return this.surveyQuestionResponses;
  }

  /**
   * Return all active responses.
   */
  public List<SurveyQuestionResponse> getActiveSurveyQuestionResponses()
  {
    ArrayList<SurveyQuestionResponse> activeResponses = new ArrayList();

    for ( Iterator iter = surveyQuestionResponses.iterator(); iter.hasNext(); )
    {
      SurveyQuestionResponse surveyQuestionResponse = (SurveyQuestionResponse)iter.next();
      if ( surveyQuestionResponse.isActive() )
      {
        activeResponses.add( surveyQuestionResponse );
      }
    }

    return activeResponses;
  }

  public void setSurveyQuestionResponses( List<SurveyQuestionResponse> surveyQuestionResponses )
  {
    this.surveyQuestionResponses = surveyQuestionResponses;
  }

  public void addSurveyQuestionResponse( SurveyQuestionResponse surveyQuestionResponse )
  {
    surveyQuestionResponse.setSurveyQuestion( this );
    this.surveyQuestionResponses.add( surveyQuestionResponse );
  }

  public String getCmAssetName()
  {
    return cmAssetName;
  }

  public void setCmAssetName( String cmAssetName )
  {
    this.cmAssetName = cmAssetName;
  }

  public int getSequenceNum()
  {
    return sequenceNum;
  }

  public void setSequenceNum( int sequenceNum )
  {
    this.sequenceNum = sequenceNum;
  }

  public SurveyQuestionStatusType getStatusType()
  {
    return statusType;
  }

  public void setStatusType( SurveyQuestionStatusType statusType )
  {
    this.statusType = statusType;
  }

  public String getQuestionText()
  {
    return CmsResourceBundle.getCmsBundle().getString( getCmAssetName(), Survey.CM_QUESTION_NAME_KEY );
  }

  public String getText()
  {
    return text;
  }

  public void setText( String text )
  {
    this.text = text;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof SurveyQuestion ) )
    {
      return false;
    }

    final SurveyQuestion surveyQuestion = (SurveyQuestion)object;

    if ( surveyQuestion.getCmAssetName() != null && !surveyQuestion.getCmAssetName().equals( this.getCmAssetName() ) )
    {
      return false;
    }

    if ( surveyQuestion.getSurvey() != null && !surveyQuestion.getSurvey().equals( this.getSurvey() ) )
    {
      return false;
    }

    return true;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int result = 0;

    result += this.getCmAssetName() != null ? this.getCmAssetName().hashCode() : 0;
    result += this.getSurvey() != null ? this.getSurvey().hashCode() * 13 : 0;

    return result;
  }

  /**
   * Builds a String representation of this.
   * 
   * @return String
   */
  public String toString()
  {
    StringBuffer sb = new StringBuffer();

    sb.append( "[SURVEYQUESTION {" );
    sb.append( "survey.id - " + this.survey.getId() + ", " );
    sb.append( "status - " + this.statusType.getCode() + ", " );
    sb.append( "cmAssetName - " + this.cmAssetName + ", " );
    sb.append( "sequenceNum - " + this.sequenceNum + ", " );
    sb.append( "openEnded - " + this.responseType + ", " );
    sb.append( "openEndedRequired - " + this.openEndedRequired );
    sb.append( "}]" );
    return sb.toString();
  }

  public boolean isActive()
  {
    return statusType.getCode().equals( SurveyQuestionStatusType.ACTIVE );
  }

  public Survey getSurvey()
  {
    return survey;
  }

  public void setSurvey( Survey survey )
  {
    this.survey = survey;
  }

  public SurveyResponseType getResponseType()
  {
    return responseType;
  }

  public void setResponseType( SurveyResponseType responseType )
  {
    this.responseType = responseType;
  }

  public boolean isOpenEndedRequired()
  {
    return openEndedRequired;
  }

  public void setOpenEndedRequired( boolean openEndedRequired )
  {
    this.openEndedRequired = openEndedRequired;
  }

  public String getStartSelectionLabel()
  {
    return startSelectionLabel;
  }

  public void setStartSelectionLabel( String startSelectionLabel )
  {
    this.startSelectionLabel = startSelectionLabel;
  }

  public String getEndSelectionLabel()
  {
    return endSelectionLabel;
  }

  public void setEndSelectionLabel( String endSelectionLabel )
  {
    this.endSelectionLabel = endSelectionLabel;
  }

  public Long getStartSelectionValue()
  {
    return startSelectionValue;
  }

  public void setStartSelectionValue( Long startSelectionValue )
  {
    this.startSelectionValue = startSelectionValue;
  }

  public Long getEndSelectionValue()
  {
    return endSelectionValue;
  }

  public void setEndSelectionValue( Long endSelectionValue )
  {
    this.endSelectionValue = endSelectionValue;
  }

  public BigDecimal getPrecisionValue()
  {
    return precisionValue;
  }

  public void setPrecisionValue( BigDecimal precisionValue )
  {
    this.precisionValue = precisionValue;
  }

}
