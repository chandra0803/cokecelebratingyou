package com.biperf.core.value.event;

import com.biperf.core.domain.eventreference.EventReference;
import com.biperf.core.utils.DateUtils;

public class EventReferenceView
{
  private java.lang.Long id;
  private String companyId;
  private String applicationName;
  private String schemaName;
  private String eventName;
  private String state;
  private Long recipientId;
  private String log;
  private String data;
  private String message;
  private String comments;
  private String creatdDate;

  public EventReferenceView( EventReference eventReference )
  {
    this.id = eventReference.getId();
    this.companyId = eventReference.getCompanyId() != null ? eventReference.getCompanyId().toString() : null;
    this.applicationName = eventReference.getApplicationName();
    this.schemaName = eventReference.getSchemaName();
    this.eventName = eventReference.getEventName();
    this.state = eventReference.getState();
    this.recipientId = eventReference.getRecipientId();
    this.log = eventReference.getLog();
    this.data = eventReference.getData();
    this.message = eventReference.getMessage();
    this.comments = eventReference.getComments();
    this.creatdDate = DateUtils.toDisplayString( eventReference.getAuditCreateInfo().getDateCreated() );
  }

  public java.lang.Long getId()
  {
    return id;
  }

  public void setId( java.lang.Long id )
  {
    this.id = id;
  }

  public String getCompanyId()
  {
    return companyId;
  }

  public void setCompanyId( String companyId )
  {
    this.companyId = companyId;
  }

  public String getApplicationName()
  {
    return applicationName;
  }

  public void setApplicationName( String applicationName )
  {
    this.applicationName = applicationName;
  }

  public String getSchemaName()
  {
    return schemaName;
  }

  public void setSchemaName( String schemaName )
  {
    this.schemaName = schemaName;
  }

  public String getEventName()
  {
    return eventName;
  }

  public void setEventName( String eventName )
  {
    this.eventName = eventName;
  }

  public String getState()
  {
    return state;
  }

  public void setState( String state )
  {
    this.state = state;
  }

  public Long getRecipientId()
  {
    return recipientId;
  }

  public void setRecipientId( Long recipientId )
  {
    this.recipientId = recipientId;
  }

  public String getLog()
  {
    return log;
  }

  public void setLog( String log )
  {
    this.log = log;
  }

  public String getData()
  {
    return data;
  }

  public void setData( String data )
  {
    this.data = data;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage( String message )
  {
    this.message = message;
  }

  public String getComments()
  {
    return comments;
  }

  public void setComments( String comments )
  {
    this.comments = comments;
  }

  public String getCreatdDate()
  {
    return creatdDate;
  }

  public void setCreatdDate( String creatdDate )
  {
    this.creatdDate = creatdDate;
  }

}
