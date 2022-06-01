
package com.biperf.core.dao.serviceanniversary.hibernate;

import java.util.UUID;

import org.hibernate.HibernateException;
import org.junit.Test;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.serviceanniversary.ServiceAnniversaryDAO;
import com.biperf.core.domain.serviceanniversary.SACelebrationInfo;
import com.biperf.core.utils.ApplicationContextFactory;

public class SAInvitationDAOImplTest extends BaseDAOTest
{
  /**
   * Returns an {@link ServiceAnniversaryDAO} object.
   * 
   */
  private static ServiceAnniversaryDAO getSAInvitationDAO()
  {
    return (ServiceAnniversaryDAO)ApplicationContextFactory.getApplicationContext().getBean( "serviceAnniversaryDAO" );
  }

  @Test( expected = HibernateException.class )
  public void testSaveProgramDetails()
  {
    ServiceAnniversaryDAO serviceAnniversaryDAO = getSAInvitationDAO();

    SACelebrationInfo saInvitationInfo = new SACelebrationInfo();

    saInvitationInfo.setId( new Long( 101 ) );

    saInvitationInfo.setCompanyId( UUID.fromString( "1001" ) );

    saInvitationInfo.setProgramId( new Long( 105 ) );

    saInvitationInfo.setRecipientId( new Long( 105 ) );

    saInvitationInfo.setCelebrationId( UUID.fromString( "100UUID" ) );

    serviceAnniversaryDAO.saveSAInvitationInfo( saInvitationInfo );

  }
}
