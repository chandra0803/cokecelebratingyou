
package com.biperf.core.ui.profile;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.MetaDataBean;
import com.biperf.core.domain.WebErrorMessage;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author dudam
 * @since Dec 15, 2012
 * @version 1.0
 * 
 * This is the main view to create json for alerts and messages tab in profile pages.
 */
@JsonInclude( value = Include.NON_NULL )
public class AlertsAndMessagesTabView
{
  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
  private List<MessageView> messageView = new ArrayList<MessageView>();
  private List<AlertView> alertView = new ArrayList<AlertView>();
  private MetaDataBean meta = new MetaDataBean();
  private String messageCounter;

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  @JsonProperty( "ProfilePageAlertsTabMessages" )
  public List<MessageView> getMessageView()
  {
    return messageView;
  }

  public void setMessageView( List<MessageView> messageView )
  {
    this.messageView = messageView;
  }

  @JsonProperty( "ProfilePageAlertsTabAlerts" )
  public List<AlertView> getAlertView()
  {
    return alertView;
  }

  public void setAlertView( List<AlertView> alertView )
  {
    this.alertView = alertView;
  }

  public void setMeta( MetaDataBean meta )
  {
    this.meta = meta;
  }

  public MetaDataBean getMeta()
  {
    return meta;
  }

  public String getMessageCounter()
  {
    return messageCounter;
  }

  public void setMessageCounter( String messageCounter )
  {
    this.messageCounter = messageCounter;
  }

}
