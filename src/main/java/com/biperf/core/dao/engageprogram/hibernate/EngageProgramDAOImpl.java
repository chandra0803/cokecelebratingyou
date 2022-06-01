/*
 * (c) 2017 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.engageprogram.hibernate;

import java.util.List;
import java.util.UUID;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.engageprogram.EngageProgramDAO;
import com.biperf.core.domain.engageprogram.EngageProgram;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * 
 * @author palaniss
 * @since Nov 01, 2018
 * 
 */
public class EngageProgramDAOImpl extends BaseDAO implements EngageProgramDAO
{

  @Override
  public void saveEngageProgramDetails( EngageProgram program )
  {
    getSession().saveOrUpdate( program );

    // run lock to insure that sub-objects are reattached to this session, since some of
    // the subobjects may have come from another session.
    getSession().flush();
    getSession().refresh( program );

  }

  @Override
  public EngageProgram fetchEngageProgramByExternalProgramIdandType( UUID programId, String programType )
  {
    EngageProgramQueryConstraint engageProgramQueryConstraint = new EngageProgramQueryConstraint();
    engageProgramQueryConstraint.setProgramId( programId );
    engageProgramQueryConstraint.setProgramType( programType );

    return getProgram( engageProgramQueryConstraint );

  }

  @Override
  public EngageProgram fetchEngageProgramByExternalProgramId( UUID programId )
  {
    EngageProgramQueryConstraint engageProgramQueryConstraint = new EngageProgramQueryConstraint();
    engageProgramQueryConstraint.setProgramId( programId );

    return getProgram( engageProgramQueryConstraint );
  }

  @Override
  public List<EngageProgram> fetchAllLiveEngagePrograms()
  {
    EngageProgramQueryConstraint engageProgramQueryConstraint = new EngageProgramQueryConstraint();
    engageProgramQueryConstraint.setProgramStatus( PromotionStatusType.LIVE );

    return getProgramList( engageProgramQueryConstraint );
  }

  public EngageProgram getProgram( EngageProgramQueryConstraint queryConstraint )
  {
    return (EngageProgram)HibernateUtil.getUniqueResultObject( queryConstraint );
  }

  public List<EngageProgram> getProgramList( EngageProgramQueryConstraint queryConstraint )
  {
    return HibernateUtil.getObjectList( queryConstraint );
  }

  @Override
  public List<String> fetchEligibleProgramCmxCode()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.engageprogram.EngageProgram.getProgramCmxCode" );
    return (List<String>)query.list();
  }

  @Override
  public EngageProgram fetchEngageProgramById( Long programId )
  {
    return (EngageProgram)getSession().get( EngageProgram.class, programId );
  }
}
