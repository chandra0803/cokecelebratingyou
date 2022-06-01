
package com.biperf.core.ui.purl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.purl.PurlContributor;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PurlContributorInviteValue;
import com.objectpartners.cms.util.CmsResourceBundle;

public class PurlTNCForm extends BaseActionForm
{
  private PurlContributor purlContributor = new PurlContributor();
  private Boolean acceptTNC;
  private String captchaResponse;
  private String method;
  private Long userId;
  private Long purlRecipientId;

  private boolean newContributor;
  private PurlContributorInviteValue newContributorData = new PurlContributorInviteValue();

  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( actionMapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    if ( "submit".equals( method ) )
    {
      if ( newContributor )
      {
        if ( StringUtils.isEmpty( newContributorData.getFirstName() ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                            new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "purl.terms.and.conditions", "FIRST_NAME" ) ) );
        }
        if ( StringUtils.isEmpty( newContributorData.getLastName() ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                            new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "purl.terms.and.conditions", "LAST_NAME" ) ) );
        }

        EmailValidator emailValidator = EmailValidator.getInstance();
        if ( StringUtils.isEmpty( newContributorData.getEmailAddr() ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                            new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "purl.terms.and.conditions", "EMAIL_ADDR" ) ) );
        }
        else if ( !emailValidator.isValid( newContributorData.getEmailAddr().trim() ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                            new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID, CmsResourceBundle.getCmsBundle().getString( "purl.terms.and.conditions", "EMAIL_ADDR" ) ) );
        }
      }

      if ( null == acceptTNC )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                          new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "purl.tnc.error.params", "TERMS_AND_CONDITIONS" ) ) );
      }
      else if ( acceptTNC.booleanValue() && !UserManager.isUserLoggedIn() )
      {
        if ( null == captchaResponse || captchaResponse.length() == 0 )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                            new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "purl.tnc.error.params", "CAPTCHA_CODE" ) ) );
        }
      }
      if ( !actionErrors.isEmpty() )
      {
        List languages = LanguageType.getList();
        if ( languages.size() == 1 )
        {
          // only display language selection if there is more than one active language.
          languages = new ArrayList();
        }
        request.setAttribute( "languageList", languages );
      }
    }

    return actionErrors;
  }

  public PurlContributor getPurlContributor()
  {
    return purlContributor;
  }

  public void setPurlContributor( PurlContributor purlContributor )
  {
    this.purlContributor = purlContributor;
  }

  public Boolean getAcceptTNC()
  {
    return acceptTNC;
  }

  public void setAcceptTNC( Boolean acceptTNC )
  {
    this.acceptTNC = acceptTNC;
  }

  public String getCaptchaResponse()
  {
    return captchaResponse;
  }

  public void setCaptchaResponse( String captchaResponse )
  {
    this.captchaResponse = captchaResponse;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public boolean isNewContributor()
  {
    return newContributor;
  }

  public void setNewContributor( boolean newContributor )
  {
    this.newContributor = newContributor;
  }

  public PurlContributorInviteValue getNewContributorData()
  {
    return newContributorData;
  }

  public void setNewContributorData( PurlContributorInviteValue newContributorData )
  {
    this.newContributorData = newContributorData;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public Long getPurlRecipientId()
  {
    return purlRecipientId;
  }

  public void setPurlRecipientId( Long purlRecipientId )
  {
    this.purlRecipientId = purlRecipientId;
  }
}
