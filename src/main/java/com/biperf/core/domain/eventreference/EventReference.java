/*
 * (c) 2017 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.eventreference;

import java.util.UUID;

import com.biperf.core.domain.BaseDomain;

/**
 * 
 * 
 * @author palaniss
 * @since Nov 1, 2018
 * 
 */
public class EventReference extends BaseDomain
{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private UUID companyId;
  private String applicationName;
  private String schemaName;
  private String eventName;
  private String state;
  private Long recipientId;
  private String log;
  private String data;
  private String message;
  private String comments;
  private String checksum;

  public UUID getCompanyId()
  {
    return companyId;
  }

  public void setCompanyId( UUID companyId )
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

  public String getChecksum()
  {
    return checksum;
  }

  public void setChecksum( String checksum )
  {
    this.checksum = checksum;
  }

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( object == null )
    {
      return false;
    }

    EventReference pgrm = (EventReference)object;
    if ( companyId == null || pgrm.getCompanyId() == null )
    {
      return false;
    }

    if ( companyId.equals( pgrm.getCompanyId() ) )
    {
      return true;
    }

    return false;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( companyId == null ? 0 : companyId.hashCode() );
    return result;
  }

}
