
package com.biperf.core.dao.engageprogram.hibernate;

import java.util.UUID;

import org.hibernate.HibernateException;
import org.junit.Test;

import com.biperf.core.dao.engageprogram.EngageProgramDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.domain.engageprogram.EngageProgram;
import com.biperf.core.utils.ApplicationContextFactory;

public class EngageProgramDAOImplTest extends BaseDAOTest
{
  /**
   * Returns an {@link EngageProgramDAO} object.
   * 
   * @return an {@link EngageProgramDAO} object.
   */
  private static EngageProgramDAO getEngageProgramDAO()
  {
    return (EngageProgramDAO)ApplicationContextFactory.getApplicationContext().getBean( "EngageProgramDAO" );
  }

  @Test( expected = HibernateException.class )
  public void testSaveProgramDetails()
  {
    EngageProgramDAO EngageProgramDAO = getEngageProgramDAO();

    EngageProgram programInfo = new EngageProgram();

    programInfo.setId( new Long( 101 ) );

    programInfo.setCompanyId( UUID.fromString( "1001" ) );

    programInfo.setExternalProgramId( UUID.fromString( "1002" ) );

    programInfo.setProgramName( "SA Program" );

    programInfo.setProgramType( "SA" );

    EngageProgramDAO.saveEngageProgramDetails( programInfo );

  }
}
