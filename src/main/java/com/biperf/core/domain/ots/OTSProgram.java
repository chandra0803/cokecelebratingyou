/*
 * (c) 2017 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.ots;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.OTSProgramStatusType;
import com.biperf.core.domain.enums.PrimaryAudienceType;

/**
 * 
 * 
 * @author rajadura
 * @since Nov 22, 2017
 * 
 */
public class OTSProgram extends BaseDomain implements Cloneable
{

  private Long programNumber;
  private OTSProgramStatusType programStatus;
  private Set<ProgramAudience> programAudience;
  private String clientName;
  private PrimaryAudienceType audienceType;
  private String description;
  

  public Set<ProgramAudience> getProgramAudience()
  {
    return programAudience;
  }

  public void setProgramAudience( Set<ProgramAudience> programAudience )
  {
    this.programAudience = programAudience;
  }

  public Long getProgramNumber()
  {
    return programNumber;
  }

  public void setProgramNumber( Long programNumber )
  {
    this.programNumber = programNumber;
  }

  public OTSProgramStatusType getProgramStatus()
  {
    return programStatus;
  }

  public void setProgramStatus( OTSProgramStatusType programStatus )
  {
    this.programStatus = programStatus;
  }

  public String getClientName()
  {
    return clientName;
  }

  public void setClientName( String clientName )
  {
    this.clientName = clientName;
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

    OTSProgram otsPgrm = (OTSProgram)object;
    if ( programNumber == null || otsPgrm.getProgramNumber() == null )
    {
      return false;
    }

    if ( programNumber.equals( otsPgrm.getProgramNumber() ) )
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
    result = prime * result + ( programNumber == null ? 0 : programNumber.hashCode() );
    return result;
  }

  public PrimaryAudienceType getAudienceType()
  {
    return audienceType;
  }

  public void setAudienceType( PrimaryAudienceType audienceType )
  {
    this.audienceType = audienceType;
  }

  
  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public Set getAudiences()
  {
    Set primaryAudiences = new HashSet();

    for ( Iterator iter = programAudience.iterator(); iter.hasNext(); )
    {
      ProgramAudience programAudience = (ProgramAudience)iter.next();
      primaryAudiences.add( programAudience.getAudience() );
    }

    return primaryAudiences;
  }

  public void addProgramAudience( ProgramAudience programAudience )
  {
    programAudience.setOtsProgram( this );
    this.programAudience.add( programAudience );
  }

}
