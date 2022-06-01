/*
 * (c) 2017 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.ots.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.ots.OTSProgramDAO;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.domain.enums.OTSProgramStatusType;
import com.biperf.core.domain.enums.PrimaryAudienceType;
import com.biperf.core.domain.ots.OTSBatch;
import com.biperf.core.domain.ots.OTSProgram;
import com.biperf.core.domain.ots.ProgramAudience;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.OTSUtil;
import com.biperf.core.vo.ots.OTSProgramVO;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * TODO Javadoc for OTSProgramDAOImpl.
 * 
 * @author rajadura
 * @since Nov 22, 2017
 * 
 */
public class OTSProgramDAOImpl extends BaseDAO implements OTSProgramDAO
{

  private UserDAO userDAO;

  @Override
  public List<OTSProgramVO> getOTSPrograms()
  {
    List<OTSProgramVO> otsPrgmVOLst = new ArrayList<>();
    Criteria criteria = getSession().createCriteria( OTSProgram.class );
    List<OTSProgram> otsProgramLst = criteria.list();

    for ( OTSProgram otsProgram : otsProgramLst )
    {
      OTSProgramVO otsPrgmVO = new OTSProgramVO();
      otsPrgmVO.setProgramNumber( OTSUtil.checkLength( otsProgram.getProgramNumber().toString() ) );
      otsPrgmVO.setProgramStatus( otsProgram.getProgramStatus().isCompleted() ? OTSProgramStatusType.COMPLETED : OTSProgramStatusType.INCOMPLETED );
      otsPrgmVO.setLastEditedDate( otsProgram.getAuditUpdateInfo().getDateModified() );
      if ( !Objects.isNull( otsProgram.getAuditUpdateInfo().getModifiedBy() ) )
      {
        otsPrgmVO.setLastEditedBy( getUserDAO().getUserById( otsProgram.getAuditUpdateInfo().getModifiedBy() ).getUserName() );
      }
      if ( !Objects.isNull( otsPrgmVO.getProgramStatus() ) && otsProgram.getProgramStatus().isCompleted() )
      {

        if ( !Objects.isNull( otsProgram.getAudienceType() ) && otsProgram.getAudienceType().equals( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) ) )
        {
          otsPrgmVO.setRecieverAudience( CmsResourceBundle.getCmsBundle().getString( "promotion.audience.ALL_ACTIVE_PAX" ) );
        }

        else if ( !Objects.isNull( otsProgram.getAudienceType() ) )
        {
          Set<ProgramAudience> programAudienceSet = otsProgram.getProgramAudience();
          StringBuffer audienceName = new StringBuffer( "" );
          for ( ProgramAudience programAudience : programAudienceSet )
          {
            audienceName.append( programAudience.getAudience().getName() + " ," );
          }
          audienceName = audienceName.deleteCharAt( audienceName.lastIndexOf( "," ) );
          otsPrgmVO.setRecieverAudience( audienceName.toString() );
        }
      }

      otsPrgmVOLst.add( otsPrgmVO );
    }
    return otsPrgmVOLst;
  }

  public UserDAO getUserDAO()
  {
    return userDAO;
  }

  public void setUserDAO( UserDAO userDAO )
  {
    this.userDAO = userDAO;
  }

  @Override
  public void save( OTSProgram program )
  {
    getSession().saveOrUpdate( program );

  }

  @Override
  public void saveBatch( OTSBatch otsBatch )
  {
    getSession().saveOrUpdate( otsBatch );

  }

  @Override
  public OTSProgram getOTSProgramByProgramNumber( Long programNumber )
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( OTSProgram.class );
    criteria.add( Restrictions.eq( "programNumber", programNumber ) );

    return (OTSProgram)criteria.uniqueResult();
  }

  public ProgramAudience getOTSProgramAudienceByProgramNumberAndAudienceId( Long ProgramId, Long AudienceId )
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( ProgramAudience.class );
    criteria.add( Restrictions.eq( "audience.id", AudienceId ) );
    criteria.add( Restrictions.eq( "otsProgram.id", ProgramId ) );
    return (ProgramAudience)criteria.uniqueResult();
  }

  @Override
  public OTSBatch getOTSBatchByBatchNumber( Long batchNumber )
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( OTSBatch.class );
    criteria.add( Restrictions.eq( "batchNumber", batchNumber ) );

    return (OTSBatch)criteria.uniqueResult();
  }

  public OTSProgram getOTSProgramByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    Session session = HibernateSessionManager.getSession();
    OTSProgram program = (OTSProgram)session.get( OTSProgram.class, id );

    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( program );
    }

    return program;
  }

}
