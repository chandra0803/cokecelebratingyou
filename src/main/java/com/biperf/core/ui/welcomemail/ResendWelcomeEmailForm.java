
package com.biperf.core.ui.welcomemail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.ui.promotion.PromotionAudienceFormBean;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.util.CmsResourceBundle;

public class ResendWelcomeEmailForm extends BaseActionForm
{
  public static final String FORM_NAME = "resendWelcomeEmailForm";

  public static final String OPTION_AUDIENCE_DEF = "audienceDefinition";
  public static final String OPTION_FILE_LOAD_DATE = "fileLoadDate";
  public static final String OPTION_FILE_LOAD_ID = "fileLoadId";

  private String method;

  private String selectedAudienceId;
  private String fileLoadDate;
  private String fileLoadId;
  private String option = OPTION_AUDIENCE_DEF;
  private boolean countSuccess = false;
  private boolean mailSuccess = false;

  private List<PromotionAudienceFormBean> audienceList = new ArrayList<PromotionAudienceFormBean>();

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public List<PromotionAudienceFormBean> getAudienceList()
  {
    return audienceList;
  }

  public void setAudienceList( List<PromotionAudienceFormBean> audienceList )
  {
    this.audienceList = audienceList;
  }

  public int getAudienceListCount()
  {
    if ( audienceList == null )
    {
      return 0;
    }

    return audienceList.size();
  }

  public String getSelectedAudienceId()
  {
    return selectedAudienceId;
  }

  public void setSelectedAudienceId( String selectedAudienceId )
  {
    this.selectedAudienceId = selectedAudienceId;
  }

  public String getFileLoadDate()
  {
    return fileLoadDate;
  }

  public void setFileLoadDate( String fileLoadDate )
  {
    this.fileLoadDate = fileLoadDate;
  }

  public String getFileLoadId()
  {
    return fileLoadId;
  }

  public void setFileLoadId( String fileLoadId )
  {
    this.fileLoadId = fileLoadId;
  }

  public String getOption()
  {
    return option;
  }

  public void setOption( String option )
  {
    this.option = option;
  }

  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors errors = super.validate( mapping, request );

    if ( null == errors )
    {
      errors = new ActionErrors();
    }

    if ( StringUtils.isEmpty( option ) )
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.resend.welcome.email.OPTION_REQUIRED" ) );
    }
    else if ( ! ( OPTION_AUDIENCE_DEF.equals( option ) || OPTION_FILE_LOAD_DATE.equals( option ) || OPTION_FILE_LOAD_ID.equals( option ) ) )
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.resend.welcome.email.INVALID_OPTION" ) );
    }

    if ( !StringUtils.isEmpty( fileLoadDate ) )
    {
      try
      {
        SimpleDateFormat sdf = new SimpleDateFormat( DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );
        Date fileLoadDt = sdf.parse( fileLoadDate );

        // Invalid Format
        if ( !sdf.format( fileLoadDt ).equals( fileLoadDate ) )
        {
          errors.add( "fileLoadDate", new ActionMessage( "admin.resend.welcome.email.INVALID_FILE_LOAD_DATE" ) );
        }

        // The date is after current date
        if ( fileLoadDt.after( DateUtils.toStartDate( DateUtils.getCurrentDate() ) ) )
        {
          errors.add( "fileLoadDate", new ActionMessage( "admin.resend.welcome.email.FILE_LOAD_DATE_ERROR" ) );
        }
      }
      catch( ParseException e )
      {
        errors.add( "fileLoadDate", new ActionMessage( "admin.resend.welcome.email.INVALID_FILE_LOAD_DATE" ) );
      }
    }

    if ( !StringUtils.isEmpty( fileLoadId ) )
    {
      try
      {
        Integer.parseInt( fileLoadId );
      }
      catch( NumberFormatException e )
      {
        errors.add( "fileLoadId", new ActionMessage( "admin.resend.welcome.email.INVALID_FILE_LOAD_ID" ) );
      }
    }

    if ( "addAudience".equals( method ) )
    {
      if ( StringUtils.isEmpty( selectedAudienceId ) )
      {
        errors.add( "audienceList", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "admin.resend.welcome.email.AUDIENCE_LIST" ) ) );
      }
    }
    else if ( "count".equals( method ) || "send".equals( method ) )
    {
      if ( OPTION_AUDIENCE_DEF.equals( option ) )
      {
        if ( audienceList == null || audienceList.isEmpty() )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.resend.welcome.email.AUDIENCE_LIST_EMPTY" ) );
        }
      }
      else if ( OPTION_FILE_LOAD_DATE.equals( option ) )
      {
        if ( StringUtils.isEmpty( fileLoadDate ) )
        {
          errors.add( "fileLoadDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "admin.resend.welcome.email.FILE_LOAD_DATE" ) ) );
        }
      }
      else if ( OPTION_FILE_LOAD_ID.equals( option ) )
      {
        if ( StringUtils.isEmpty( fileLoadId ) )
        {
          errors.add( "fileLoadId", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "admin.resend.welcome.email.FILE_LOAD_ID" ) ) );
        }
      }
    }

    return errors;
  }

  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    audienceList = getEmptyValueList( RequestUtils.getOptionalParamInt( request, "audienceListCount" ) );
  }

  private List<PromotionAudienceFormBean> getEmptyValueList( int valueListCount )
  {
    List<PromotionAudienceFormBean> valueList = new ArrayList<PromotionAudienceFormBean>();

    for ( int i = 0; i < valueListCount; i++ )
    {
      // create an empty PromotionAudienceFormBean
      PromotionAudienceFormBean promoAudienceBean = new PromotionAudienceFormBean();
      valueList.add( promoAudienceBean );
    }

    return valueList;
  }

  public void setCountSuccess( boolean countSuccess )
  {
    this.countSuccess = countSuccess;
  }

  public boolean isCountSuccess()
  {
    return countSuccess;
  }

  public void setMailSuccess( boolean mailSuccess )
  {
    this.mailSuccess = mailSuccess;
  }

  public boolean isMailSuccess()
  {
    return mailSuccess;
  }

}
