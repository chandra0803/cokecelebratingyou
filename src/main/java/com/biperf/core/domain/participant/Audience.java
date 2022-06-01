/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/participant/Audience.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.participant;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.AudienceType;

/**
 * Audience.
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
 * <td>sharma</td>
 * <td>Jun 13, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public abstract class Audience extends BaseDomain implements Cloneable
{
  private String name;

  public abstract AudienceType getAudienceType();

  public abstract int getSize();

  private Boolean plateauAwardsOnly = Boolean.FALSE;

  private Boolean publicAudience = Boolean.FALSE;

  public Set<AudienceRole> audienceRoles = new LinkedHashSet<AudienceRole>();

  private UUID rosterAudienceId;

  public abstract void setSize( int size );

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  /**
   * Checks equality of the object parameter to this. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof Audience ) )
    {
      return false;
    }

    final Audience audience = (Audience)object;

    if ( getName() != null ? !getName().equals( audience.getName() ) : audience.getName() != null )
    {
      return false;
    }

    if ( getPlateauAwardsOnly() != null ? !getPlateauAwardsOnly().equals( audience.getPlateauAwardsOnly() ) : audience.getPlateauAwardsOnly() != null )
    {
      return false;
    }

    return true;
  }

  /**
   * Define the hashCode from the id. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    return getName() != null ? getName().hashCode() : 0;
  }

  /**
   * Does a deep copy of the audience. This is a customized
   * implementation of Every Set and List should be accounted for in the deepCopy method, if the Set
   * or List is not used it still should be set to a new LinkedHashSet or ArrayList in the copied
   * audience (See journals and sweepstakes).
   *
   * @see java.lang.Object#clone()
   * @param cloneWithChildren
   * @param newAudienceName
   * @return Object
   * @throws CloneNotSupportedException
   */
  public Object deepCopy( boolean cloneWithChildren, String newAudienceName, List newChildAudienceNameHolders ) throws CloneNotSupportedException
  {
    Audience clonedAudience = (Audience)super.clone();

    clonedAudience.resetBaseDomain();
    // Set the Name.
    clonedAudience.setName( newAudienceName );
    clonedAudience.setPlateauAwardsOnly( plateauAwardsOnly );
    clonedAudience.setPublicAudience( publicAudience );

    // don't copy AudienceRoles
    clonedAudience.setAudienceRoles( new LinkedHashSet() );

    return clonedAudience;
  }

  public void setPlateauAwardsOnly( Boolean plateauAwardsOnly )
  {
    this.plateauAwardsOnly = plateauAwardsOnly;
  }

  public Boolean getPlateauAwardsOnly()
  {
    return plateauAwardsOnly;
  }

  public Boolean getPublicAudience()
  {
    return publicAudience;
  }

  public void setPublicAudience( Boolean publicAudience )
  {
    this.publicAudience = publicAudience;
  }

  public Set<AudienceRole> getAudienceRoles()
  {
    return audienceRoles;
  }

  public void setAudienceRoles( Set<AudienceRole> audienceRoles )
  {
    this.audienceRoles = audienceRoles;
  }

  public UUID getRosterAudienceId()
  {
    return rosterAudienceId;
  }

  public void setRosterAudienceId( UUID rosterAudienceId )
  {
    this.rosterAudienceId = rosterAudienceId;
  }

}
