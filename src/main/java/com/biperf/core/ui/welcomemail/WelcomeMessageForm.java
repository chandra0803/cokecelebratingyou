
package com.biperf.core.ui.welcomemail;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.welcomemail.WelcomeMessage;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.ui.promotion.PromotionAudienceFormBean;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * WelcomeMessageForm.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>Ramesh Kunasekaran</td>
 * <td>Sep 18, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 */
public class WelcomeMessageForm extends BaseActionForm
{

  private long welcomeMessageId;

  private long version;

  private long dateCreated;

  private String createdBy;

  private String method;

  private String[] delete;

  private String notificationDate;

  private boolean editable;

  private List audienceList = new ArrayList();

  private String selectedAudienceId;

  private String messageId;

  /**
   * Load the WelcomeMessage to the form
   * 
   * @param WelcomeMessage
   */
  public void load( WelcomeMessage welcomeMessage )
  {
    this.notificationDate = welcomeMessage.getNotificationDate().toString();
    this.messageId = welcomeMessage.getMessageId().toString();
    this.createdBy = welcomeMessage.getAuditCreateInfo().getCreatedBy().toString();
    this.dateCreated = welcomeMessage.getAuditCreateInfo().getDateCreated().getTime();
    this.version = welcomeMessage.getVersion().longValue();
    this.editable = this.isEditable();
  } // end load

  /**
   * Builds a domain object from the form.
   * 
   * @return WelcomeMessage
   */
  public WelcomeMessage toInsertedDomainObject()
  {
    WelcomeMessage welcomeMessage = new WelcomeMessage();
    welcomeMessage.setMessageId( new Long( this.messageId ) );
    welcomeMessage.setNotificationDate( DateUtils.toDate( this.notificationDate ) );
    welcomeMessage.setAudienceList( this.audienceList );
    welcomeMessage.setVersion( new Long( this.version ) );
    return welcomeMessage;
  } // end toInsertedDomainObject

  /**
   * Builds a full domain object from the form.
   * 
   * @return WelcomeMessage
   */
  public WelcomeMessage toFullDomainObject()
  {
    WelcomeMessage welcomeMessage = new WelcomeMessage();
    welcomeMessage.setId( new Long( this.welcomeMessageId ) );
    welcomeMessage.setMessageId( new Long( this.messageId ) );
    welcomeMessage.setNotificationDate( DateUtils.toDate( this.notificationDate ) );
    welcomeMessage.setAudienceList( this.audienceList );
    welcomeMessage.setVersion( new Long( this.version ) );
    welcomeMessage.getAuditCreateInfo().setCreatedBy( Long.valueOf( this.createdBy ) );
    welcomeMessage.getAuditCreateInfo().setDateCreated( new Timestamp( this.dateCreated ) );
    return welcomeMessage;

  } // end toInsertedDomainObject

  /**
   * Validate the form before submitting Overridden from
   * 
   * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping,
   *      javax.servlet.http.HttpServletRequest)
   * @param mapping
   * @param request
   * @return ActionErrors
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors errors = super.validate( mapping, request );

    SimpleDateFormat sdf = new SimpleDateFormat( DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );
    Date notificationDate = null;

    if ( isEmpty( getMessageId() ) )
    {
      errors.add( "messageId", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "admin.welcomemessage.MESSAGE" ) ) );
    }

    if ( getAudienceList() == null || getAudienceList().size() == 0 )
    {
      errors.add( "audienceList", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.audience.AUDIENCE_LIST_LABEL" ) ) );
    }
    if ( isEmpty( getNotificationDate() ) )
    {
      errors.add( "notificationDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "admin.welcomemessage.NOTIFICATION_DATE" ) ) );
    }

    if ( getNotificationDate() != null && getNotificationDate().length() != 0 )
    {
      try
      {
        notificationDate = sdf.parse( getNotificationDate() );
        if ( notificationDate.before( DateUtils.toStartDate( DateUtils.getCurrentDate() ) ) )
        {
          // The date is before current date
          errors.add( "notificationDate", new ActionMessage( "admin.welcomemessage.NOTIFICATION_DATE_ERROR" ) );
        }
      }
      catch( ParseException e )
      {
        errors.add( "notificationDate", new ActionMessage( "promotion.basics.errors.INVALID_DATE_FORMAT" ) );
      }
      if ( !sdf.format( notificationDate ).equals( getNotificationDate() ) )
      {
        errors.add( "notificationDate", new ActionMessage( "promotion.basics.errors.INVALID_DATE_FORMAT" ) );
      }
    }

    return errors;
  }

  private static boolean isEmpty( String str )
  {
    return str == null || str.length() == 0;
  }

  public String getMessageId()
  {
    return messageId;
  }

  public void setMessageId( String messageId )
  {
    this.messageId = messageId;
  }

  /**
   * Accessor for the number of PromotionAudienceFormBean objects in the list.
   * 
   * @return int
   */
  public int getAudienceListCount()
  {
    if ( audienceList == null )
    {
      return 0;
    }

    return audienceList.size();
  }

  /**
   * Reset all properties to their default values.
   * 
   * @param mapping The mapping used to select this instance
   * @param request The servlet request we are processing
   */
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    audienceList = getEmptyValueList( RequestUtils.getOptionalParamInt( request, "audienceListCount" ) );
  }

  /**
   * resets the notificationList with empty PromotionNotificationForm beans
   * 
   * @param valueListCount
   * @return List
   */
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

  public List getAudienceList()
  {
    return audienceList;
  }

  public void setAudienceList( List audienceList )
  {
    this.audienceList = audienceList;
  }

  /**
   * Accessor for the value list
   * 
   * @param index
   * @return Single instance of PromotionAudienceFormBean from the value list
   */
  public PromotionAudienceFormBean getAudienceList( int index )
  {
    try
    {
      return (PromotionAudienceFormBean)audienceList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public String getCreatedBy()
  {
    return createdBy;
  }

  public void setCreatedBy( String createdBy )
  {
    this.createdBy = createdBy;
  }

  public long getDateCreated()
  {
    return dateCreated;
  }

  public void setDateCreated( long dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  public String[] getDelete()
  {
    return delete;
  }

  public void setDelete( String[] delete )
  {
    this.delete = delete;
  }

  public boolean isEditable()
  {
    return editable;
  }

  public void setEditable( boolean editable )
  {
    this.editable = editable;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public long getVersion()
  {
    return version;
  }

  public void setVersion( long version )
  {
    this.version = version;
  }

  public String getSelectedAudienceId()
  {
    return selectedAudienceId;
  }

  public void setSelectedAudienceId( String selectedAudienceId )
  {
    this.selectedAudienceId = selectedAudienceId;
  }

  public String getNotificationDate()
  {
    return notificationDate;
  }

  public void setNotificationDate( String notificationDate )
  {
    this.notificationDate = notificationDate;
  }

  public long getWelcomeMessageId()
  {
    return welcomeMessageId;
  }

  public void setWelcomeMessageId( long welcomeMessageId )
  {
    this.welcomeMessageId = welcomeMessageId;
  }
}
