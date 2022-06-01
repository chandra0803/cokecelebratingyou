/*
 * (c) 2017 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.purl.client;

import com.biperf.core.domain.BaseDomain;

/**
 * 
 * @author poddutur
 * @since Jun 1, 2017
 */
public class ExternalUnsubscribe extends BaseDomain
{
  private static final long serialVersionUID = -166709580047694423L;
  private String emailAddress;

  public String getEmailAddress()
  {
    return emailAddress;
  }

  public void setEmailAddress( String emailAddress )
  {
    this.emailAddress = emailAddress;
  }

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( ! ( object instanceof ExternalUnsubscribe ) )
    {
      return false;
    }
    if ( getClass() != object.getClass() )
    {
      return false;
    }
    ExternalUnsubscribe externalUnsubscribe = (ExternalUnsubscribe)object;
    if ( getId() == null )
    {
      if ( externalUnsubscribe.getId() != null )
        return false;
    }
    else if ( !getId().equals( externalUnsubscribe.getId() ) )
      return false;

    if ( emailAddress == null )
    {
      if ( externalUnsubscribe.emailAddress != null )
      {
        return false;
      }
    }
    else if ( !emailAddress.equals( externalUnsubscribe.emailAddress ) )
    {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( this.getId() == null ) ? 0 : this.getId().hashCode() );
    result = prime * result + ( ( emailAddress == null ) ? 0 : emailAddress.hashCode() );
    return result;
  }

}
