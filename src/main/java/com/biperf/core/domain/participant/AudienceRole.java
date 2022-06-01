
package com.biperf.core.domain.participant;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.user.Role;

public class AudienceRole extends BaseDomain
{

  private static final long serialVersionUID = 3905522717791630136L;

  private Role role;
  private Audience audience;

  public Role getRole()
  {
    return role;
  }

  public void setRole( Role role )
  {
    this.role = role;
  }

  public Audience getAudience()
  {
    return audience;
  }

  public void setAudience( Audience audience )
  {
    this.audience = audience;
  }

  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "AudienceRole [" );
    buf.append( "{role_id=" + this.getRole().getId() + "}, " );
    buf.append( "{audience_id=" + this.getAudience().getId() + "}" );
    buf.append( "]" );
    return buf.toString();
  }

  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof AudienceRole ) )
    {
      return false;
    }

    final AudienceRole audienceRole = (AudienceRole)o;

    if ( getRole() != null ? !getRole().equals( audienceRole.getRole() ) : audienceRole.getRole() != null )
    {
      return false;
    }
    if ( getAudience() != null ? !getAudience().equals( audienceRole.getAudience() ) : audienceRole.getAudience() != null )
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    int result;
    result = getRole() != null ? getRole().hashCode() : 0;
    result = 29 * result + ( getAudience() != null ? getAudience().hashCode() : 0 );

    return result;
  }

}
