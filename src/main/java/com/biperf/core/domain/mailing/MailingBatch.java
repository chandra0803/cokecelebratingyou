/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/mailing/MailingBatch.java,v $
 */

package com.biperf.core.domain.mailing;

import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;

public class MailingBatch extends BaseDomain
{

  private String description;

  private Set<Mailing> mailings = new LinkedHashSet<Mailing>();

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public Set<Mailing> getMailings()
  {
    return mailings;
  }

  public void setMailings( Set<Mailing> mailings )
  {
    this.mailings = mailings;
  }

  public void addMailing( Mailing mailing )
  {
    this.mailings.add( mailing );
  }

  @Override
  public boolean equals( Object object )
  {

    if ( this == object )
    {
      return true;
    }
    if ( ! ( object instanceof MailingBatch ) )
    {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    return getDescription() != null ? getDescription().hashCode() : 0;
  }
}
