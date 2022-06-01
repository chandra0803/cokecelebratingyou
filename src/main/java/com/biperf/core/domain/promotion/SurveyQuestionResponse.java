
package com.biperf.core.domain.promotion;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.SurveyQuestionStatusType;
import com.objectpartners.cms.util.CmsResourceBundle;

public class SurveyQuestionResponse extends BaseDomain
{
  private SurveyQuestion surveyQuestion = null;
  private String cmAssetCode = "";
  private String text;
  private int sequenceNum;
  // SurveyQuestionAnswer Status Type.
  private SurveyQuestionStatusType statusType;
  private Long responseCount;

  public SurveyQuestionResponse()
  {
    super();
  }

  /**
   * Does a deep copy of the SurveyQuestionResponse. This is a customized implementation of
   * java.lang.Object#clone()
   * 
   * @see java.lang.Object#clone()
   * @return Object
   */
  public Object deepCopy()
  {
    SurveyQuestionResponse response = new SurveyQuestionResponse();
    response.setCmAssetCode( this.getCmAssetCode() );
    response.setStatusType( this.statusType );

    return response;
  }

  public Long getResponseCount()
  {
    return responseCount;
  }

  public void setResponseCount( Long responseCount )
  {
    this.responseCount = responseCount;
  }

  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  public void setCmAssetCode( String cmAssetCode )
  {
    this.cmAssetCode = cmAssetCode;
  }

  public int getSequenceNum()
  {
    return sequenceNum;
  }

  public void setSequenceNum( int sequenceNum )
  {
    this.sequenceNum = sequenceNum;
  }

  public String getQuestionResponseText()
  {
    return CmsResourceBundle.getCmsBundle().getString( getCmAssetCode(), Survey.CM_QUESTION_RESPONSE_KEY );
  }

  public String getText()
  {
    return text;
  }

  public void setText( String text )
  {
    this.text = text;
  }

  public SurveyQuestion getSurveyQuestion()
  {
    return surveyQuestion;
  }

  public void setSurveyQuestion( SurveyQuestion surveyQuestion )
  {
    this.surveyQuestion = surveyQuestion;
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

    if ( ! ( object instanceof SurveyQuestionResponse ) )
    {
      return false;
    }

    final SurveyQuestionResponse surveyQuestionResponse = (SurveyQuestionResponse)object;

    if ( surveyQuestionResponse.getCmAssetCode() != null && !surveyQuestionResponse.getCmAssetCode().equals( this.getCmAssetCode() ) )
    {
      return false;
    }

    if ( surveyQuestionResponse.getSurveyQuestion() != null && !surveyQuestionResponse.getSurveyQuestion().equals( this.getSurveyQuestion() ) )
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

    result += this.getCmAssetCode() != null ? this.getCmAssetCode().hashCode() : 0;
    result += this.getSurveyQuestion() != null ? this.getSurveyQuestion().hashCode() * 13 : 0;

    return result;
  }

  /**
   * Builds and returns a String representation of this. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  public String toString()
  {
    StringBuffer sb = new StringBuffer();

    sb.append( "[SURVEYQUESTIONRESPONSE {" );
    sb.append( "id - " + this.getId() + ", " );
    sb.append( "surveyQuestion.id - " + this.surveyQuestion.getId() + ", " );
    sb.append( "cmAssetCode - " + this.cmAssetCode + ", " );
    sb.append( "status - " + this.statusType.getCode() );
    sb.append( "}]" );

    return sb.toString();
  }

  public SurveyQuestionStatusType getStatusType()
  {
    return statusType;
  }

  public void setStatusType( SurveyQuestionStatusType statusType )
  {
    this.statusType = statusType;
  }

  public boolean isActive()
  {
    return statusType.getCode().equals( SurveyQuestionStatusType.ACTIVE );
  }

}
