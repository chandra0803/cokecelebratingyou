/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.engageprogram.hibernate;

import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseQueryConstraint;
import com.biperf.core.domain.engageprogram.EngageProgram;

/**
 * TODO Javadoc for EngageProgramQueryConstraint.
 * 
 * @author sivanand
 * @since Jan 2, 2019
 * @version 1.0
 */
public class EngageProgramQueryConstraint extends BaseQueryConstraint
{
  private UUID programId;
  private String programType;
  private String programStatus;

  /**
   * {@inheritDoc}
   */
  @Override
  public Class getResultClass()
  {
    return EngageProgram.class;
  }

  public Criteria buildCriteria()
  {
    Criteria criteria = super.buildCriteria();

    if ( Objects.nonNull( programId ) )
    {
      criteria.add( Restrictions.eq( "externalProgramId", programId ) );
    }
    if ( !StringUtils.isEmpty( programType ) )
    {
      criteria.add( Restrictions.eq( "programType", programType ) );
    }
    if ( !StringUtils.isEmpty( programStatus ) )
    {
      criteria.add( Restrictions.eq( "programStatus", programStatus ) );
    }

    return criteria;

  }

  public UUID getProgramId()
  {
    return programId;
  }

  public void setProgramId( UUID programId )
  {
    this.programId = programId;
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

}
