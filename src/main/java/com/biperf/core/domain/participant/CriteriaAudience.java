/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/participant/CriteriaAudience.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.participant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.enums.AudienceType;

/**
 * CriteriaAudience.
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
 * <td>Jun 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CriteriaAudience extends Audience
{
  private List audienceParticipants = new ArrayList();
  private Set audienceCriterias = new LinkedHashSet();
  private int size;

  public CriteriaAudience()
  {
    super();
  }

  public CriteriaAudience( AudienceCriteria audienceCriteria )
  {
    super();
    addAudienceCriteria( audienceCriteria );
  }

  public Set getAudienceCriterias()
  {
    return audienceCriterias;
  }

  public void setAudienceCriterias( Set audienceCriterias )
  {
    this.audienceCriterias = audienceCriterias;
  }

  public void addAudienceCriteria( AudienceCriteria audienceCriteria )
  {
    audienceCriteria.setAudience( this );
    audienceCriterias.add( audienceCriteria );
  }

  public AudienceType getAudienceType()
  {
    return AudienceType.lookup( AudienceType.SEARCH_CRITERIA_TYPE );
  }

  /**
   * Returns true if any of the AudienceCriteria objects has query constraints
   * 
   * @return boolean
   * @see AudienceCriteria#hasConstraints()
   */
  public boolean hasConstraints()
  {
    boolean hasConstraints = false;

    for ( Iterator iter = audienceCriterias.iterator(); iter.hasNext(); )
    {
      AudienceCriteria audienceCriteria = (AudienceCriteria)iter.next();
      if ( audienceCriteria.hasConstraints() )
      {
        hasConstraints = true;
        break;
      }
    }

    return hasConstraints;
  }

  public boolean hasExcludeConstraints()
  { 
    boolean hasConstraints = false;

    for ( Iterator iter = audienceCriterias.iterator(); iter.hasNext(); )
    {
      AudienceCriteria audienceCriteria = (AudienceCriteria)iter.next();
      if ( audienceCriteria.hasExcludeConstraints() )
      {
        hasConstraints = true;
        break;
      }
    }

    return hasConstraints;
  }

  /**
   * Holds the size of this audience.
   * 
   * @param size
   */
  public void setSize( int size )
  {
    this.size = size;

  }

  /**
   * the size of this audience.
   * 
   * @return int
   */
  public int getSize()
  {
    return this.size;
  }

  /**
   * Returns a deep copy of this object.
   *
   * @param cloneWithChildren
   * @param newAudienceName   the audience name to be assigned to the copy.
   * @throws CloneNotSupportedException if one of this objects component objects
   *         does not support cloning.
   */
  public Object deepCopy( boolean cloneWithChildren, String newAudienceName, List newChildAudienceNameHolders ) throws CloneNotSupportedException
  {
    CriteriaAudience clonedCritAudience = (CriteriaAudience)super.deepCopy( cloneWithChildren, newAudienceName, newChildAudienceNameHolders );
    clonedCritAudience.setSize( this.size );
    clonedCritAudience.setAudienceCriterias( new LinkedHashSet() );
    if ( null != audienceCriterias && this.audienceCriterias.size() > 0 )
    {
      for ( Iterator iter = getAudienceCriterias().iterator(); iter.hasNext(); )
      {
        AudienceCriteria audienceCriteria = (AudienceCriteria)iter.next();
        AudienceCriteria clone = audienceCriteria.deepCopy();
        clonedCritAudience.addAudienceCriteria( clone );
      }
    }
    clonedCritAudience.setAudienceParticipants( new ArrayList() );
    if ( null != audienceParticipants && this.audienceParticipants.size() > 0 )
    {
      for ( Iterator iter = getAudienceParticipants().iterator(); iter.hasNext(); )
      {
        AudienceParticipant audienceParticipant = (AudienceParticipant)iter.next();
        AudienceParticipant clone = audienceParticipant.deepCopy();
        clonedCritAudience.addPaxAudience( clone );
      }
    }
    return clonedCritAudience;
  }

  public void setAudienceParticipants( List audienceParticipants )
  {
    this.audienceParticipants = audienceParticipants;
    this.setSize( audienceParticipants.size() );
  }

  public List getAudienceParticipants()
  {
    return audienceParticipants;
  }

  public void addPaxAudience( AudienceParticipant audienceParticipant )
  {
    audienceParticipant.setCriteriaAudience( this );
    this.audienceParticipants.add( audienceParticipant );
  }

}
