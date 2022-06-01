
package com.biperf.core.ui.survey;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.SurveyQuestionStatusType;
import com.biperf.core.domain.enums.SurveyResponseType;
import com.biperf.core.domain.promotion.Survey;
import com.biperf.core.domain.promotion.SurveyQuestion;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.StringUtil;
import com.objectpartners.cms.util.CmsResourceBundle;

public class SurveyQuestionForm extends BaseForm
{
  private String method;
  private Long surveyFormId;
  private String surveyName;
  private String surveyType;
  private Long surveyQuestionId;
  private String surveyQuestionCmAssetName;
  private String surveyQuestionText;
  private String surveyQuestionStatus;
  private String surveyQuestionStatusText;
  private String[] deletedResponses;
  private String newResponseSequenceNum;
  private Long surveyQuestionResponseId;
  private List answers;
  private boolean openEndedRequired;
  private boolean standardResponse;
  private boolean slider;
  private String responseType = SurveyResponseType.STANDARD_RESPONSE;

  private String startSelectionLabel;
  private String endSelectionLabel;
  private String startSelectionValue;
  private String endSelectionValue;
  private String precision;

  /**
   * Load the form
   * 
   * @param question
   */
  public void load( SurveyQuestion question )
  {
    this.surveyFormId = question.getSurvey().getId();
    this.surveyName = question.getSurvey().getName();
    this.surveyType = question.getSurvey().getSurveyType().getCode();
    this.surveyQuestionId = question.getId();
    this.surveyQuestionText = StringUtil.convertLineBreaks( question.getQuestionText() );
    this.surveyQuestionCmAssetName = question.getCmAssetName();
    this.surveyQuestionStatus = question.getStatusType().getCode();
    this.surveyQuestionStatusText = question.getStatusType().getName();
    this.answers = question.getSurveyQuestionResponses();
    this.responseType = question.getResponseType().getCode();
    this.openEndedRequired = question.isOpenEndedRequired();
    this.startSelectionLabel = question.getStartSelectionLabel();
    this.endSelectionLabel = question.getEndSelectionLabel();
    this.startSelectionValue = question.getStartSelectionValue() != null ? question.getStartSelectionValue().toString() : "";
    this.endSelectionValue = question.getEndSelectionValue() != null ? question.getEndSelectionValue().toString() : "";
    this.precision = question.getPrecisionValue() != null ? question.getPrecisionValue().toString() : "";

  }

  public void load( Survey survey )
  {
    this.surveyFormId = survey.getId();
    this.surveyName = survey.getName();
    this.surveyType = survey.getSurveyType().getCode();
  }

  /**
   * Creates a detatched SurveyQuestion Domain Object that will later be synchronized with a looked up
   * promotion object in the service
   * 
   * @return Survey
   */
  public SurveyQuestion toDomainObject()
  {
    SurveyQuestion surveyQuestion = new SurveyQuestion();
    surveyQuestion.setId( this.surveyQuestionId );
    surveyQuestion.setCmAssetName( this.surveyQuestionCmAssetName );
    surveyQuestion.setStatusType( SurveyQuestionStatusType.lookup( this.surveyQuestionStatus ) );
    surveyQuestion.setResponseType( SurveyResponseType.lookup( getResponseType() ) );
    surveyQuestion.setOpenEndedRequired( isOpenEndedRequired() );

    if ( getResponseType().equals( SurveyResponseType.SLIDER_SELECTION ) )
    {
      surveyQuestion.setStartSelectionLabel( getStartSelectionLabel() );
      surveyQuestion.setEndSelectionLabel( getEndSelectionLabel() );
      surveyQuestion.setStartSelectionValue( new Long( getStartSelectionValue() ) );
      surveyQuestion.setEndSelectionValue( new Long( getEndSelectionValue() ) );
      surveyQuestion.setPrecisionValue( new BigDecimal( getPrecision() ) );
    }

    return surveyQuestion;
  }

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages.
   * 
   * @param actionMapping the ActionMapping used to select this instance
   * @param request the HTTP request that is being processed
   * @return org.apache.struts.action.ActionErrors - collection of errors found on validation of
   *         data in form
   */
  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( actionMapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    if ( getSurveyQuestionText().isEmpty() )
    {
      actionErrors.add( "surveyQuestionText", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "survey.question", "QUESTION" ) ) );
    }

    if ( this.responseType.equals( SurveyResponseType.SLIDER_SELECTION ) )
    {
      if ( this.startSelectionLabel.isEmpty() )
      {
        actionErrors.add( "startSelectionLabel", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "survey.question", "START_LABEL" ) ) );
      }
      if ( this.endSelectionLabel.isEmpty() )
      {
        actionErrors.add( "endSelectionLabel", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "survey.question", "END_LABEL" ) ) );
      }

      try
      {
        // required field validation
        if ( StringUtil.isEmpty( startSelectionValue ) || StringUtil.isEmpty( endSelectionValue ) || StringUtil.isEmpty( precision.toString() ) )
        {
          if ( StringUtil.isEmpty( startSelectionValue ) )
          {
            actionErrors.add( "startSelectionValue",
                              new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "survey.question", "START_VALUE" ) ) );
          }
          if ( StringUtil.isEmpty( endSelectionValue ) )
          {
            actionErrors.add( "endSelectionValue", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "survey.question", "END_VALUE" ) ) );
          }
          if ( precision == null || precision.toString().equals( "" ) )
          {
            actionErrors.add( "precision", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "survey.question", "PRECISION" ) ) );
          }
        }
        else
        {
          // make sure the data is actually valid
          long selectionMinValue = Long.parseLong( startSelectionValue );
          long selectionMaxValue = Long.parseLong( endSelectionValue );
          BigDecimal precisionValue = new BigDecimal( precision.toString() );

          // If the min or max value is less than or equal
          // to zero, then add the error
          if ( selectionMinValue < 0 )
          {
            actionErrors.add( "startSelectionValue", new ActionMessage( "survey.errors.MIN_START_VALUE" ) );
          }

          if ( selectionMaxValue > 9999 )
          {
            actionErrors.add( "endSelectionValue", new ActionMessage( "survey.errors.MAX_END_VALUE" ) );
          }

          // If the min value is greater than or equals to
          // the max value, then add the error
          if ( selectionMinValue >= selectionMaxValue )
          {
            actionErrors.add( "startSelectionValue", new ActionMessage( "survey.errors.START_END_VALIDATION" ) );
          }

          if ( precisionValue.compareTo( new BigDecimal( 0.5 ) ) == -1 || precisionValue.compareTo( new BigDecimal( selectionMaxValue ) ) == 1 )
          {
            actionErrors.add( "precision", new ActionMessage( "survey.errors.PRECISOIN_CHECK" ) );
          }

          BigDecimal selectionValuesDiff = new BigDecimal( selectionMaxValue - selectionMinValue );
          if ( ! ( precisionValue.compareTo( new BigDecimal( 0 ) ) == 0 ) && ! ( selectionValuesDiff.remainder( precisionValue ).compareTo( new BigDecimal( 0 ) ) == 0 ) )
          {
            actionErrors.add( "precision", new ActionMessage( "survey.errors.INVALID_PRECISION" ) );
          }

        }
      }
      catch( NumberFormatException e )
      {
        actionErrors.add( "awardAmountTypeFixed", new ActionMessage( "survey.errors.INVALID_VALUES" ) );
      }

    }
    return actionErrors;
  }

  public String[] getDeletedResponses()
  {
    return deletedResponses;
  }

  public void setDeletedResponses( String[] deletedResponses )
  {
    this.deletedResponses = deletedResponses;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public Long getSurveyFormId()
  {
    return surveyFormId;
  }

  public void setSurveyFormId( Long surveyId )
  {
    this.surveyFormId = surveyId;
  }

  public String getSurveyName()
  {
    return surveyName;
  }

  public void setSurveyName( String surveyName )
  {
    this.surveyName = surveyName;
  }

  public String getSurveyQuestionCmAssetName()
  {
    return surveyQuestionCmAssetName;
  }

  public void setSurveyQuestionCmAssetName( String surveyQuestionCmAssetName )
  {
    this.surveyQuestionCmAssetName = surveyQuestionCmAssetName;
  }

  public Long getSurveyQuestionId()
  {
    return surveyQuestionId;
  }

  public void setSurveyQuestionId( Long surveyQuestionId )
  {
    this.surveyQuestionId = surveyQuestionId;
  }

  public String getSurveyQuestionText()
  {
    return surveyQuestionText;
  }

  public void setSurveyQuestionText( String surveyQuestionText )
  {
    this.surveyQuestionText = surveyQuestionText;
  }

  public String getNewResponseSequenceNum()
  {
    return newResponseSequenceNum;
  }

  public void setNewResponseSequenceNum( String newResponseSequenceNum )
  {
    this.newResponseSequenceNum = newResponseSequenceNum;
  }

  public Long getSurveyQuestionResponseId()
  {
    return surveyQuestionResponseId;
  }

  public void setSurveyQuestionResponseId( Long surveyQuestionResponseId )
  {
    this.surveyQuestionResponseId = surveyQuestionResponseId;
  }

  public void setAnswers( List answers )
  {
    this.answers = answers;
  }

  public List getAnswers()
  {
    return answers;
  }

  public int getAnswersSize()
  {
    int size = 0;
    if ( answers != null )
    {
      size = answers.size();
    }
    return size;
  }

  public String getSurveyQuestionStatus()
  {
    return surveyQuestionStatus;
  }

  public void setSurveyQuestionStatus( String surveyQuestionStatus )
  {
    this.surveyQuestionStatus = surveyQuestionStatus;
  }

  public String getSurveyType()
  {
    return surveyType;
  }

  public void setSurveyType( String surveyTypeCode )
  {
    this.surveyType = surveyTypeCode;
  }

  public String getSurveyQuestionStatusText()
  {
    return surveyQuestionStatusText;
  }

  public void setSurveyQuestionStatusText( String surveyQuestionStatusText )
  {
    this.surveyQuestionStatusText = surveyQuestionStatusText;
  }

  public boolean isOpenEndedRequired()
  {
    return openEndedRequired;
  }

  public void setOpenEndedRequired( boolean openEndedRequired )
  {
    this.openEndedRequired = openEndedRequired;
  }

  public boolean isStandardResponse()
  {
    return standardResponse;
  }

  public void setStandardResponse( boolean standardResponse )
  {
    this.standardResponse = standardResponse;
  }

  public boolean isSlider()
  {
    return slider;
  }

  public void setSlider( boolean slider )
  {
    this.slider = slider;
  }

  public String getResponseType()
  {
    return responseType;
  }

  public void setResponseType( String responseType )
  {
    this.responseType = responseType;
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

  public String getStartSelectionValue()
  {
    return startSelectionValue;
  }

  public void setStartSelectionValue( String startSelectionValue )
  {
    this.startSelectionValue = startSelectionValue;
  }

  public String getEndSelectionValue()
  {
    return endSelectionValue;
  }

  public void setEndSelectionValue( String endSelectionValue )
  {
    this.endSelectionValue = endSelectionValue;
  }

  public String getPrecision()
  {
    return precision;
  }

  public void setPrecision( String precision )
  {
    this.precision = precision;
  }

}
