
package com.biperf.core.ui.managertoolkit;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.AlertDurationType;
import com.biperf.core.domain.managertoolkit.AlertMessage;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.StringUtil;
import com.objectpartners.cms.util.ContentReaderManager;

public class ManagerAlertForm extends BaseForm
{
  private String messageSubject;
  private String message;
  private String orgUnitSelect;
  private String duration;
  private String method;
  private boolean valid = true;
  private String userId;
  private Long orgUnitRecips;
  private Long orgUnitBelowRecips;
  private boolean sendEmail;

  public boolean isSendEmail()
  {
    return sendEmail;
  }

  public void setSendEmail( boolean sendEmail )
  {
    this.sendEmail = sendEmail;
  }

  public void load()
  {
    this.duration = AlertDurationType.TWENTY_FOUR_HRS;
  }

  public AlertMessage toDomainObject()
  {
    // Create a new Promotion since one was not passed in
    return toDomainObject( new AlertMessage() );
  }

  /**
   * Creates a detatched Promotion Domain Object that will later be synchronized with a looked up
   * promotion object in the service
   * 
   * @param promotion
   * @return Promotion
   */
  public AlertMessage toDomainObject( AlertMessage alertMessage )
  {
    alertMessage.setSubject( messageSubject );
    alertMessage.setMessageTo( orgUnitSelect );
    alertMessage.setExpiryDate( getDurationExpiryDate( duration ) );
    alertMessage.setValid( valid );
    alertMessage.setMessage( message );
    alertMessage.setSendEmail( sendEmail );
    return alertMessage;
  }

  private Date getDurationExpiryDate( String duration )
  {
    Date now = new Date();
    Date expirationDate = new Date();

    if ( duration.equals( AlertDurationType.TWENTY_FOUR_HRS ) )
    {
      expirationDate = DateUtils.addHours( now, 24 );
    }
    else if ( duration.equals( AlertDurationType.FOURTY_EIGHT_HRS ) )
    {
      expirationDate = DateUtils.addHours( now, 48 );
    }
    else if ( duration.equals( AlertDurationType.SEVEN_DAYS ) )
    {
      expirationDate = DateUtils.addDays( now, 7 );
    }

    return expirationDate;
  }

  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors errors = super.validate( mapping, request );
    if ( errors == null )
    {
      errors = new ActionErrors();
    }
    if ( messageSubject == null || this.messageSubject.isEmpty() )
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, ContentReaderManager.getText( "manager.alert.send", "SUBJECT" ) ) );
    }
    // even thought we didn't enter anything in the message text area. It is preoccupied with the
    // below text because of the rich text area.
    if ( "<p>&nbsp;</p>".equals( this.message ) )
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, ContentReaderManager.getText( "manager.alert.send", "MESSAGE" ) ) );
    }
    else
    {
      String messageWithNoHtml = (String)StringUtil.skipHTML( message );
      if ( messageWithNoHtml.length() > 1000 || messageWithNoHtml.length() <= 0 )
      {
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "manager.alert.send.CHAR_LIMIT" ) );
      }
    }
    return errors;
  }

  public String getMessageSubject()
  {
    return messageSubject;
  }

  public void setMessageSubject( String messageSubject )
  {
    this.messageSubject = messageSubject;
  }

  public String getOrgUnitSelect()
  {
    return orgUnitSelect;
  }

  public void setOrgUnitSelect( String orgUnitSelect )
  {
    this.orgUnitSelect = orgUnitSelect;
  }

  public String getDuration()
  {
    return duration;
  }

  public void setDuration( String duration )
  {
    this.duration = duration;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage( String message )
  {
    this.message = message;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public boolean isValid()
  {
    return valid;
  }

  public void setValid( boolean valid )
  {
    this.valid = valid;
  }

  public void setUserId( String userId )
  {
    this.userId = userId;
  }

  public String getUserId()
  {
    return userId;
  }

  public Long getOrgUnitRecips()
  {
    return orgUnitRecips;
  }

  public void setOrgUnitRecips( Long orgUnitRecips )
  {
    this.orgUnitRecips = orgUnitRecips;
  }

  public Long getOrgUnitBelowRecips()
  {
    return orgUnitBelowRecips;
  }

  public void setOrgUnitBelowRecips( Long orgUnitBelowRecips )
  {
    this.orgUnitBelowRecips = orgUnitBelowRecips;
  }

}
