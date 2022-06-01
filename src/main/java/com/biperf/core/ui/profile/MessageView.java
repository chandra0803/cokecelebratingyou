
package com.biperf.core.ui.profile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author dudam
 * @since Dec 15, 2012
 * @version 1.0
 * 
 * This is the sub view of AlertsAndMessagesTabView to create json for alerts & messages tab in profile pages.
 */
@JsonInclude( value = Include.NON_EMPTY )
public class MessageView
{

  private String sortDate;
  private String messageDate;
  private String messageTitle;
  private String messageText;
  private String messageLinkUrl;
  private String messageTextPlain;
  private String htmlMessageTextBodyCopy;
  private Long messageIdNum;

  @JsonProperty( "htmlMessageTextBodyCopy" )
  public String getHtmlMessageTextBodyCopy()
  {
    return htmlMessageTextBodyCopy;
  }

  public void setHtmlMessageTextBodyCopy( String htmlMessageTextBodyCopy )
  {
    this.htmlMessageTextBodyCopy = htmlMessageTextBodyCopy;
  }

  @JsonProperty( "messageId" )
  public String getAlertId()
  {
    return String.format( "%05d", messageIdNum );
  }

  @JsonIgnore
  public Long getMessageIdNum()
  {
    return messageIdNum;
  }

  public void setMessageIdNum( Long messageId )
  {
    this.messageIdNum = messageId;
  }

  public String getSortDate()
  {
    return sortDate;
  }

  public void setSortDate( String sortDate )
  {
    this.sortDate = sortDate;
  }

  public String getMessageDate()
  {
    return messageDate;
  }

  public void setMessageDate( String messageDate )
  {
    this.messageDate = messageDate;
  }

  public String getMessageTitle()
  {
    return messageTitle;
  }

  public void setMessageTitle( String messageTitle )
  {
    this.messageTitle = messageTitle;
  }

  public String getMessageText()
  {
    return messageText;
  }

  public void setMessageText( String messageText )
  {
    this.messageText = messageText;
  }

  public String getMessageLinkUrl()
  {
    return messageLinkUrl;
  }

  public void setMessageLinkUrl( String messageLinkUrl )
  {
    this.messageLinkUrl = messageLinkUrl;
  }

  public String getMessageTextPlain()
  {
    return messageTextPlain;
  }

  public void setMessageTextPlain( String messageTextPlain )
  {
    this.messageTextPlain = messageTextPlain;
  }

}
