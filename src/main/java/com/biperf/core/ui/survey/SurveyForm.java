
package com.biperf.core.ui.survey;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.ClaimFormStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.SurveyType;
import com.biperf.core.domain.promotion.Survey;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.utils.StringUtil;
import com.biperf.util.StringUtils;
import com.objectpartners.cms.util.CmsResourceBundle;

public class SurveyForm extends BaseActionForm
{
  private static final String COPY_SURVEY_NAME_ASSET_KEY = "survey.form.NEW_NAME";
  private static final String SURVEY_NAME_ASSET_KEY = "survey.form.NAME";
  private static final String PROMO_MODULE_ASSET_KEY = "survey.form.PROMOTION_MODULE";

  private String method;
  private Long surveyFormId;
  private String surveyFormName;
  private String copySurveyFormName;
  private String description;
  private String status;
  private boolean surveyAssigned;
  private boolean active;
  private String surveyFormType;
  private String surveyFormTypeDesc;

  /**
   * Load the form
   * 
   * @param survey
   */

  public void load( Survey survey )
  {
    if ( survey != null )
    {
      this.surveyFormId = survey.getId();
      this.surveyFormName = survey.getName();
      this.description = survey.getDescription() == null ? null : convertLineBreaks( survey.getDescription() );
      this.status = survey.getClaimFormStatusType().getCode();
      this.surveyAssigned = survey.isAssigned();
      this.surveyFormType = survey.getPromotionModuleType().getCode();
      this.surveyFormTypeDesc = survey.getPromotionModuleType().getName();
    }
  }

  private String convertLineBreaks( String text )
  {
    return StringUtil.convertLineBreaks( text );
  }

  /**
   * Creates a detatched Promotion Domain Object that will later be synchronized with a looked up
   * promotion object in the service
   * 
   * @return Survey
   */

  public Survey toDomainObject()
  {
    Survey survey = new Survey();
    survey.setId( this.surveyFormId );
    survey.setName( this.surveyFormName );
    survey.setDescription( this.description );
    ClaimFormStatusType statusType = null;
    if ( this.status != null )
    {
      statusType = ClaimFormStatusType.lookup( this.status );
    }
    if ( statusType == null )
    {
      statusType = ClaimFormStatusType.lookup( ClaimFormStatusType.UNDER_CONSTRUCTION ); // default
                                                                                         // status
    }
    survey.setPromotionModuleType( PromotionType.lookup( this.surveyFormType ) );
    survey.setClaimFormStatusType( statusType );
    survey.setSurveyType( SurveyType.lookup( SurveyType.RANDOM ) );
    return survey;
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

    if ( method != null )
    {
      if ( method.equals( "copy" ) )
      {
        if ( StringUtils.isEmpty( copySurveyFormName ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( COPY_SURVEY_NAME_ASSET_KEY ) ) );
        }
        if ( StringUtils.isEmpty( surveyFormType ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( PROMO_MODULE_ASSET_KEY ) ) );
        }
      }
      else if ( method.equals( "save" ) )
      {
        if ( StringUtils.isEmpty( surveyFormName ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( SURVEY_NAME_ASSET_KEY ) ) );
        }

        if ( StringUtils.isEmpty( surveyFormType ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.REQUIRED", CmsResourceBundle.getCmsBundle().getString( PROMO_MODULE_ASSET_KEY ) ) );
        }

        if ( description != null && description.length() > 400 )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "survey.form.DESCRIPTION_SIZE_EXCEEDED" ) );
        }
      }
    }

    return actionErrors;
  }

  public String getSurveyFormName()
  {
    return surveyFormName;
  }

  public void setSurveyFormName( String surveyFormName )
  {
    this.surveyFormName = surveyFormName;
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

  public void setSurveyFormId( Long surveyFormId )
  {
    this.surveyFormId = surveyFormId;
  }

  public String getCopySurveyFormName()
  {
    return copySurveyFormName;
  }

  public void setCopySurveyFormName( String copySurveyFormName )
  {
    this.copySurveyFormName = copySurveyFormName;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public boolean isSurveyAssigned()
  {
    return surveyAssigned;
  }

  public void setSurveyAssigned( boolean surveyLive )
  {
    this.surveyAssigned = surveyLive;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive( boolean active )
  {
    this.active = active;
  }

  public String getSurveyFormType()
  {
    return surveyFormType;
  }

  public void setSurveyFormType( String surveyFormType )
  {
    this.surveyFormType = surveyFormType;
  }

  public String getSurveyFormTypeDesc()
  {
    return surveyFormTypeDesc;
  }

  public void setSurveyFormTypeDesc( String surveyFormTypeDesc )
  {
    this.surveyFormTypeDesc = surveyFormTypeDesc;
  }

}
