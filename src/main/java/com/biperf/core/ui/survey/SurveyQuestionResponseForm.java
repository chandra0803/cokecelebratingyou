
package com.biperf.core.ui.survey;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.SurveyQuestionStatusType;
import com.biperf.core.domain.promotion.Survey;
import com.biperf.core.domain.promotion.SurveyQuestion;
import com.biperf.core.domain.promotion.SurveyQuestionResponse;
import com.biperf.core.service.survey.SurveyService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.BeanLocator;
import com.objectpartners.cms.util.CmsResourceBundle;

public class SurveyQuestionResponseForm extends BaseForm
{
  private String method;
  private Long surveyQuestionResponseId;
  private String surveyQuestionResponseText;
  private String surveyQuestionResponseCmAssetCode;
  private String surveyQuestionResponseStatus;

  private Long surveyQuestionId;
  private String surveyQuestionText;
  private String surveyQuestionStatus;
  private String surveyQuestionStatusText;

  private String surveyName;
  private boolean surveyLive;

  private boolean surveyPromotion;

  /**
   * Load the form.
   * 
   * @param response
   */
  public void load( SurveyQuestionResponse response )
  {
    Survey survey = response.getSurveyQuestion().getSurvey();
    this.surveyName = survey.getName();
    this.surveyLive = survey.isAssigned();

    this.surveyQuestionId = response.getSurveyQuestion().getId();
    this.surveyQuestionText = response.getSurveyQuestion().getQuestionText();
    this.surveyQuestionStatus = response.getSurveyQuestion().getStatusType().getCode();
    this.surveyQuestionStatusText = response.getSurveyQuestion().getStatusType().getName();

    this.surveyQuestionResponseId = response.getId();
    this.surveyQuestionResponseCmAssetCode = response.getCmAssetCode();
    this.surveyQuestionResponseStatus = response.getStatusType().getCode();
    this.surveyQuestionResponseText = response.getQuestionResponseText();

    if ( survey.getPromotionModuleType().getCode().equals( PromotionType.SURVEY ) )
    {
      this.surveyPromotion = true;
    }
  }

  /**
   * Creates a detatched SurveyQuestionResponse Domain Object that will later be synchronized with a
   * looked up promotion object in the service
   * 
   * @return SurveyQuestionResponse
   */
  public SurveyQuestionResponse toDomainObject()
  {
    SurveyQuestion surveyQuestion = new SurveyQuestion();
    surveyQuestion.setId( getSurveyQuestionId() );

    SurveyQuestionResponse surveyQuestionResponse = new SurveyQuestionResponse();
    surveyQuestionResponse.setId( getSurveyQuestionResponseId() );
    surveyQuestionResponse.setSurveyQuestion( surveyQuestion );
    surveyQuestionResponse.setCmAssetCode( getSurveyQuestionResponseCmAssetCode() );
    surveyQuestionResponse.setStatusType( SurveyQuestionStatusType.lookup( this.surveyQuestionResponseStatus ) );

    return surveyQuestionResponse;
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

    // yet to validate
    if ( surveyQuestionResponseText == null || surveyQuestionResponseText.isEmpty() )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                        new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "survey.question_response.RESPONSE" ) ) );
    }

    return actionErrors;
  }

  /**
   * Clear out the editable fields so this form can be re-used to populate a new entry form upon
   * "Save and Add Another."
   */
  public void clearForNewQuestionResponse()
  {
    setSurveyQuestionResponseText( "" );
    // setSurveyQuestionResponseExplanation( "" );
    setSurveyQuestionResponseId( null );
  }

  /**
   * @return value of method property
   */
  public String getMethod()
  {
    return method;
  }

  /**
   * @param method value of method property
   */
  public void setMethod( String method )
  {
    this.method = method;
  }

  /**
   * @return value of surveyQuestionResponseId property
   */
  public Long getSurveyQuestionResponseId()
  {
    return surveyQuestionResponseId;
  }

  /**
   * @param surveyQuestionResponseId value of surveyQuestionResponseId property
   */
  public void setSurveyQuestionResponseId( Long surveyQuestionResponseId )
  {
    this.surveyQuestionResponseId = surveyQuestionResponseId;
  }

  /**
   * @return value of surveyQuestionResponseText
   */
  public String getSurveyQuestionResponseText()
  {
    return surveyQuestionResponseText;
  }

  /**
   * @param surveyQuestionResponseText value of surveyQuestionResponseText property
   */
  public void setSurveyQuestionResponseText( String surveyQuestionResponseText )
  {
    this.surveyQuestionResponseText = surveyQuestionResponseText;
  }

  /**
   * @return value of surveyQuestionCmAssetKeyText property
   */
  public String getSurveyQuestionText()
  {
    return surveyQuestionText;
  }

  /**
   * @param surveyQuestionText value of surveyQuestionText property
   */
  public void setSurveyQuestionText( String surveyQuestionText )
  {
    this.surveyQuestionText = surveyQuestionText;
  }

  /**
   * @return value of surveyQuestionResponseCmAssetCode property
   */
  public String getSurveyQuestionResponseCmAssetCode()
  {
    return surveyQuestionResponseCmAssetCode;
  }

  /**
   * @param surveyQuestionResponseCmAssetCode value of surveyQuestionResponseCmAssetCode property
   */
  public void setSurveyQuestionResponseCmAssetCode( String surveyQuestionResponseCmAssetCode )
  {
    this.surveyQuestionResponseCmAssetCode = surveyQuestionResponseCmAssetCode;
  }

  /**
   * @return value of surveyQuestionId property
   */
  public Long getSurveyQuestionId()
  {
    return surveyQuestionId;
  }

  /**
   * @param surveyQuestionId value of surveyQuestionId property
   */
  public void setSurveyQuestionId( Long surveyQuestionId )
  {
    this.surveyQuestionId = surveyQuestionId;
  }

  /**
   * @return value of surveyQuestionStatus property
   */
  public String getSurveyQuestionStatus()
  {
    return surveyQuestionStatus;
  }

  /**
   * @param surveyQuestionStatus value of surveyQuestionStatus property
   */
  public void setSurveyQuestionStatus( String surveyQuestionStatus )
  {
    this.surveyQuestionStatus = surveyQuestionStatus;
  }

  /**
   * @return value of surveyName property
   */
  public String getSurveyName()
  {
    return surveyName;
  }

  /**
   * @param surveyName value of surveyName property
   */
  public void setSurveyName( String surveyName )
  {
    this.surveyName = surveyName;
  }

  /**
   * @return value of surveyLive property
   */
  public boolean isSurveyLive()
  {
    return surveyLive;
  }

  /**
   * @param surveyLive value of surveyLive property
   */
  public void setSurveyLive( boolean surveyLive )
  {
    this.surveyLive = surveyLive;
  }

  public String getSurveyQuestionResponseStatus()
  {
    return surveyQuestionResponseStatus;
  }

  public void setSurveyQuestionResponseStatus( String surveyQuestionResponseStatus )
  {
    this.surveyQuestionResponseStatus = surveyQuestionResponseStatus;
  }

  public String getSurveyQuestionStatusText()
  {
    return surveyQuestionStatusText;
  }

  public void setSurveyQuestionStatusText( String surveyQuestionStatusText )
  {
    this.surveyQuestionStatusText = surveyQuestionStatusText;
  }

  public boolean isSurveyPromotion()
  {
    return surveyPromotion;
  }

  public void setSurveyPromotion( boolean surveyPromotion )
  {
    this.surveyPromotion = surveyPromotion;
  }

  private SurveyService getSurveyService()
  {
    return (SurveyService)BeanLocator.getBean( SurveyService.BEAN_NAME );
  }

}
