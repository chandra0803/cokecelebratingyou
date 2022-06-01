/*
 * (c) 2017 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.engageprogram;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import com.biperf.core.domain.BaseDomain;

/**
 * 
 * 
 * @author palaniss
 * @since Nov 1, 2018
 * 
 */
public class EngageProgram extends BaseDomain
{
  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = 1730377028549996409L;
  private Long programId;
  private UUID externalProgramId;
  private UUID companyId;
  private String programNameCMXAssetCode;
  private String programName;
  private String programType;
  private String programStatus;
  private Date programStartDate;
  private Date programEndDate;
  private String awardType;
  private Date programCreateDate;
  private boolean allowContribution;
  private String programHeader;
  private String programHeaderCMXAsstCode;
  private String primaryColor;
  private String secondaryColor;
  private Set<ProgramAwardLevel> programsAwardLevels = new LinkedHashSet<ProgramAwardLevel>();

  public String getProgramName()
  {
    return programName;
  }

  public void setProgramName( String programName )
  {
    this.programName = programName;
  }

  public String getProgramType()
  {
    return programType;
  }

  public void setProgramType( String programType )
  {
    this.programType = programType;
  }

  public String getProgramStatus()
  {
    return programStatus;
  }

  public void setProgramStatus( String programStatus )
  {
    this.programStatus = programStatus;
  }

  public Date getProgramStartDate()
  {
    return programStartDate;
  }

  public void setProgramStartDate( Date programStartDate )
  {
    this.programStartDate = programStartDate;
  }

  public Date getProgramEndDate()
  {
    return programEndDate;
  }

  public void setProgramEndDate( Date programEndDate )
  {
    this.programEndDate = programEndDate;
  }

  public String getAwardType()
  {
    return awardType;
  }

  public void setAwardType( String awardType )
  {
    this.awardType = awardType;
  }

  public Date getProgramCreateDate()
  {
    return programCreateDate;
  }

  public void setProgramCreateDate( Date programCreateDate )
  {
    this.programCreateDate = programCreateDate;
  }

  public boolean isAllowContribution()
  {
    return allowContribution;
  }

  public void setAllowContribution( boolean allowContribution )
  {
    this.allowContribution = allowContribution;
  }

  public String getProgramHeader()
  {
    return programHeader;
  }

  public void setProgramHeader( String programHeader )
  {
    this.programHeader = programHeader;
  }

  public String getProgramHeaderCMXAsstCode()
  {
    return programHeaderCMXAsstCode;
  }

  public void setProgramHeaderCMXAsstCode( String programHeaderCMXAsstCode )
  {
    this.programHeaderCMXAsstCode = programHeaderCMXAsstCode;
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

    EngageProgram pgrm = (EngageProgram)object;
    if ( externalProgramId == null || pgrm.getExternalProgramId() == null )
    {
      return false;
    }

    if ( externalProgramId.equals( pgrm.getExternalProgramId() ) )
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
    result = prime * result + ( externalProgramId == null ? 0 : externalProgramId.hashCode() );
    return result;
  }

  public UUID getCompanyId()
  {
    return companyId;
  }

  public void setCompanyId( UUID companyId )
  {
    this.companyId = companyId;
  }

  public String getProgramNameCMXAssetCode()
  {
    return programNameCMXAssetCode;
  }

  public void setProgramNameCMXAssetCode( String programNameCMXAssetCode )
  {
    this.programNameCMXAssetCode = programNameCMXAssetCode;
  }

  public UUID getExternalProgramId()
  {
    return externalProgramId;
  }

  public void setExternalProgramId( UUID externalProgramId )
  {
    this.externalProgramId = externalProgramId;
  }

  public Long getProgramId()
  {
    return programId;
  }

  public void setProgramId( Long programId )
  {
    this.programId = programId;
  }

  public String getPrimaryColor()
  {
    return primaryColor;
  }

  public void setPrimaryColor( String primaryColor )
  {
    this.primaryColor = primaryColor;
  }

  public String getSecondaryColor()
  {
    return secondaryColor;
  }

  public void setSecondaryColor( String secondaryColor )
  {
    this.secondaryColor = secondaryColor;
  }

  public Set<ProgramAwardLevel> getProgramsAwardLevels()
  {
    return programsAwardLevels;
  }

  public void setProgramsAwardLevels( Set<ProgramAwardLevel> programsAwardLevels )
  {
    this.programsAwardLevels = programsAwardLevels;
  }

  public void addProgramAwardLevel( ProgramAwardLevel programAwardLevel )
  {
    programAwardLevel.setEngageProgram( this );
    this.programsAwardLevels.add( programAwardLevel );
  }

}