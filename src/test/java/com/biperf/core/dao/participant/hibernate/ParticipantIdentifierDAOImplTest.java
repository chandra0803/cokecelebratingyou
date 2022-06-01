
package com.biperf.core.dao.participant.hibernate;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import com.biperf.core.builders.BuilderUtil;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.ParticipantIdentifierDAO;
import com.biperf.core.dao.participant.UserCharacteristicDAO;
import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.participant.ParticipantIdentifier;

public class ParticipantIdentifierDAOImplTest extends BaseDAOTest
{
  public static UserCharacteristicDAO getUserCharacteristicDAO()
  {
    return (UserCharacteristicDAO)getDAO( "userCharacteristicDAO" );
  }

  public static ParticipantDAO getParticipantDAO()
  {
    return (ParticipantDAO)getDAO( "participantDAO" );
  }

  public static ParticipantIdentifierDAO getParticipantIdentifierDAO()
  {
    return (ParticipantIdentifierDAO)getDAO( "participantIdentifierDAO" );
  }

  @Test
  public void testFindAll()
  {
    List<ParticipantIdentifier> pis = getParticipantIdentifierDAO().getAll();
    assertNotNull( "The ParticipantIdentifier List is null", pis );
    assertTrue( "The ParticipantIdentifier List is empty", !pis.isEmpty() );
  }

  @Test
  public void testSaveWithUserCharacteristic()
  {
    Characteristic cha = getUserCharacteristicDAO().getAllCharacteristics().get( 0 );
    ParticipantIdentifier pi = BuilderUtil.buildParticipantIdentifier();
    pi.setCharacteristic( cha );
    pi.setParticipantIdentifierType( null );
    getParticipantIdentifierDAO().save( pi );
    Long theId = pi.getId();
    flushAndClearSession();
    pi = getParticipantIdentifierDAO().getById( pi.getId() );
    assertNotNull( "The ParticipantIdentifier is null", pi );
    assertNotNull( "The ParticipantIdentifier ID is null", pi.getId() );
    assertTrue( "The ParticipantIdentifier IDs are not equal", pi.getId().equals( theId ) );
  }

  @Test
  public void testGetSelected()
  {
    List<ParticipantIdentifier> pis = getParticipantIdentifierDAO().getSelected();
    assertNotNull( "The ParticipantIdentifier is null", pis );
    assertTrue( "The ParticipantIdentifier list is empty", !pis.isEmpty() );
    List<ParticipantIdentifier> results = pis.stream().filter( pi -> pi.getParticipantIdentifierType() != null && pi.getParticipantIdentifierType().getCode().equals( "email" ) )
        .collect( Collectors.toList() );
    assertTrue( "Missing selected values", !results.isEmpty() );
  }

  @Test
  public void testGetUnSelected()
  {
    List<ParticipantIdentifier> pis = getParticipantIdentifierDAO().getUnSelected();
    assertNotNull( "The ParticipantIdentifier is null", pis );
    assertTrue( "The ParticipantIdentifier list is empty", !pis.isEmpty() );
    List<ParticipantIdentifier> results = pis.stream().filter( pi -> pi.isSelected() ).collect( Collectors.toList() );
    assertTrue( "This list should be emtpy", results.isEmpty() );
  }
}
