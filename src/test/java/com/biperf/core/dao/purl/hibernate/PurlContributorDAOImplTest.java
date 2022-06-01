
package com.biperf.core.dao.purl.hibernate;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.purl.PurlContributorDAO;
import com.biperf.core.dao.purl.PurlRecipientDAO;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.purl.PurlContributor;
import com.biperf.core.domain.purl.PurlRecipient;

public class PurlContributorDAOImplTest extends BaseDAOTest
{

  public static PurlContributor buildAndSaveUniquePaxPurlContributor( String suffix )
  {
    Participant pax = ParticipantDAOImplTest.buildUniqueParticipant( suffix );
    getParticipantDAO().saveParticipant( pax );
    flushAndClearSession();

    PurlContributor purlContributor = new PurlContributor();
    purlContributor.setId( new Long( 1 ) );
    purlContributor.setFirstName( "firstname" + suffix );
    purlContributor.setLastName( "lastname" + suffix );
    purlContributor.setUser( pax );

    PurlRecipient purlRecipient = PurlRecipientDAOImplTest.buildAndSaveUniquePurlRecipient( "purlRecipient" + suffix );

    PurlRecipient savedPurlRecipient = getPurlRecipientDAO().getPurlRecipientById( purlRecipient.getId() );
    savedPurlRecipient.addContributor( purlContributor );
    getPurlRecipientDAO().save( savedPurlRecipient );
    flushAndClearSession();

    return purlContributor;
  }

  public static PurlContributor buildAndSaveUniqueNonPaxPurlContributor( String suffix )
  {
    PurlContributor purlContributor = new PurlContributor();
    purlContributor.setId( new Long( 1 ) );
    purlContributor.setFirstName( "firstname" + suffix );
    purlContributor.setLastName( "lastname" + suffix );
    purlContributor.setEmailAddr( suffix + "emailaddress@domain.com" );

    PurlRecipient purlRecipient = PurlRecipientDAOImplTest.buildAndSaveUniquePurlRecipient( suffix );

    PurlRecipient savedPurlRecipient = getPurlRecipientDAO().getPurlRecipientById( purlRecipient.getId() );
    savedPurlRecipient.addContributor( purlContributor );
    getPurlRecipientDAO().save( savedPurlRecipient );
    flushAndClearSession();

    return purlContributor;
  }

  public void testSaveGetPaxPurlContributorById()
  {
    String uniqueString = getUniqueString();
    PurlContributor purlContributor = buildAndSaveUniquePaxPurlContributor( uniqueString );

    PurlContributor retrievedPurlContributor = getPurlContributorDAO().getPurlContributorById( purlContributor.getId() );
    assertNotNull( retrievedPurlContributor );
    assertNotNull( retrievedPurlContributor.getUser() );
    assertNull( retrievedPurlContributor.getEmailAddr() );
  }

  public void testSaveGetNonPaxPurlContributorById()
  {
    String uniqueString = getUniqueString();
    PurlContributor purlContributor = buildAndSaveUniqueNonPaxPurlContributor( uniqueString );

    PurlContributor retrievedPurlContributor = getPurlContributorDAO().getPurlContributorById( purlContributor.getId() );
    assertNotNull( retrievedPurlContributor );
    assertNull( retrievedPurlContributor.getUser() );
    assertNotNull( retrievedPurlContributor.getEmailAddr() );
  }

  public void testUpdatePurlContributorName()
  {
    String uniqueString = getUniqueString();
    PurlContributor purlContributor = buildAndSaveUniquePaxPurlContributor( uniqueString );

    PurlContributor retrievedPurlContributor = getPurlContributorDAO().getPurlContributorById( purlContributor.getId() );
    assertNotNull( retrievedPurlContributor );

    retrievedPurlContributor.setFirstName( "updatedFirstname" );
    retrievedPurlContributor.setLastName( "updatedLastname" );
    getPurlContributorDAO().save( retrievedPurlContributor );

    PurlContributor updatedPurlContributor = getPurlContributorDAO().getPurlContributorById( retrievedPurlContributor.getId() );
    assertNotNull( updatedPurlContributor );
  }

  private static ParticipantDAO getParticipantDAO()
  {
    return (ParticipantDAO)getDAO( ParticipantDAO.BEAN_NAME );
  }

  private static PurlRecipientDAO getPurlRecipientDAO()
  {
    return (PurlRecipientDAO)getDAO( PurlRecipientDAO.BEAN_NAME );
  }

  private static PurlContributorDAO getPurlContributorDAO()
  {
    return (PurlContributorDAO)getDAO( PurlContributorDAO.BEAN_NAME );
  }
}
