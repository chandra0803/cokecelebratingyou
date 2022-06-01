/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/participant/hibernate/AudienceDAOImplTest.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.dao.participant.hibernate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.hierarchy.NodeTypeCharacteristicDAO;
import com.biperf.core.dao.participant.AudienceDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.characteristic.NodeTypeCharacteristicType;
import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.domain.enums.ParticipantRelationshipType;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.ParticipantSuspensionStatus;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.AudienceCriteria;
import com.biperf.core.domain.participant.AudienceCriteriaCharacteristic;
import com.biperf.core.domain.participant.CriteriaAudience;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.PaxAudience;
import com.biperf.core.utils.HibernateSessionManager;

/**
 * AudienceDAOImplTest.
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
public class AudienceDAOImplTest extends BaseDAOTest
{

  public void testSaveCriteriaAudience()
  {
    Audience audience = getCriteriaAudience();
    getAudienceDAO().save( audience );

    flushAndClearSession();

    Audience retrievedAudience = getAudienceDAO().getAudienceById( audience.getId() );

    assertTrue( "Criteria Audience could not be read from database", retrievedAudience != null );
    assertDomainObjectEquals( "Criteria Audience read from db is different", retrievedAudience, audience );
  }

  public void testSavePaxAudience()
  {
    Audience audience = getPaxAudience();
    getAudienceDAO().save( audience );

    flushAndClearSession();

    Audience retrievedAudience = getAudienceDAO().getAudienceById( audience.getId() );

    assertTrue( "Pax Audience could not be read from database", retrievedAudience != null );
    assertDomainObjectEquals( "Pax Audience read from db is different", retrievedAudience, audience );
  }

  public void testGetAllCriteriaAudiences()
  {
    Audience audience = getCriteriaAudience();
    getAudienceDAO().save( audience );
    flushAndClearSession();

    List criteriaAudiences = getAudienceDAO().getAllCriteriaAudiences();
    assertTrue( "Expected at least one criteria audience.  There was not.", criteriaAudiences.size() > 0 );
    for ( int i = 0; i < criteriaAudiences.size(); i++ )
    {
      Audience audience1 = (Audience)criteriaAudiences.get( i );
      assertTrue( "Expected all audiences to be of type CriteriaAudince.", audience1 instanceof CriteriaAudience );
    }
  }

  public static Audience getPaxAudience( Participant participant )
  {
    String uniqueName = String.valueOf( System.currentTimeMillis() % 29930291 );
    return getPaxAudience( participant, uniqueName );
  }

  public static Audience getPaxAudience( Participant participant, String uniqueName )
  {

    PaxAudience audience = new PaxAudience();
    audience.setName( "testAudience" + uniqueName );
    audience.addParticipant( participant );

    return audience;
  }

  private Audience getPaxAudience()
  {
    String uniqueName = String.valueOf( System.currentTimeMillis() % 29930291 );

    PaxAudience audience = new PaxAudience();
    audience.setName( "testAudience" + uniqueName );
    setParticipants( audience );

    return audience;
  }

  // public static Audience getPaxAudience()
  // {
  // String uniqueName = String.valueOf( System.currentTimeMillis() % 29930291 );
  //
  // PaxAudience audience = new PaxAudience();
  // audience.setName( "testAudience" + uniqueName );
  // audience.addParticipant(ParticipantDAOImplTest.buildUniqueParticipant("PAX1_" + uniqueName));
  // audience.addParticipant(ParticipantDAOImplTest.buildUniqueParticipant("PAX2_" + uniqueName));
  // audience.addParticipant(ParticipantDAOImplTest.buildUniqueParticipant("PAX3_" + uniqueName));
  // audience.addParticipant(ParticipantDAOImplTest.buildUniqueParticipant("PAX4_" + uniqueName));
  //
  // return audience;
  // }

  public static Audience buildStaticCriteriaAudience()
  {
    return getCriteriaAudience( null );
  }

  public static Audience getCriteriaAudience( NodeTypeCharacteristicDAO nodeTypeCharacteristicDAO )
  {
    String uniqueName = String.valueOf( System.currentTimeMillis() % 29930291 );

    CriteriaAudience audience = new CriteriaAudience();
    audience.setName( "testAudience" + uniqueName );
    setAudienceCriteria( audience, nodeTypeCharacteristicDAO );

    return audience;
  }

  private Audience getCriteriaAudience()
  {
    NodeTypeCharacteristicDAO characteristicDAO = getNodeTypeCharacteristicDAO();
    return getCriteriaAudience( characteristicDAO );
  }

  public static AudienceCriteria setAudienceCriteria( CriteriaAudience audience, NodeTypeCharacteristicDAO nodeTypeCharacteristicDAO )
  {
    AudienceCriteria audienceCriteria = new AudienceCriteria();
    audienceCriteria.setAudience( audience );
    audience.getAudienceCriterias().add( audienceCriteria );

    audienceCriteria.setChildNodesIncluded( false );
    audienceCriteria.setDepartmentType( "mkt" );

    setAudienceCriteriaCharacteristic( audienceCriteria, nodeTypeCharacteristicDAO );
    return audienceCriteria;
  }

  public static AudienceCriteriaCharacteristic setAudienceCriteriaCharacteristic( AudienceCriteria audienceCriteria, NodeTypeCharacteristicDAO nodeTypeCharacteristicDAO )
  {
    AudienceCriteriaCharacteristic audienceCriteriaCharacteristic = new AudienceCriteriaCharacteristic();
    audienceCriteriaCharacteristic.setAudienceCriteria( audienceCriteria );
    audienceCriteria.getCharacteristicCriterias().add( audienceCriteriaCharacteristic );

    audienceCriteriaCharacteristic.setCharacteristic( getCharacteristic( nodeTypeCharacteristicDAO ) );

    return audienceCriteriaCharacteristic;
  }

  public static Characteristic getCharacteristic( NodeTypeCharacteristicDAO nodeTypeCharacteristicDAO )
  {

    NodeTypeCharacteristicType nodeTypeChar = new NodeTypeCharacteristicType();
    loadCharacteristicValues( nodeTypeChar );
    nodeTypeChar.setDomainId( new Long( 1 ) );

    if ( nodeTypeCharacteristicDAO != null )
    {
      nodeTypeCharacteristicDAO.saveCharacteristic( nodeTypeChar );
      HibernateSessionManager.getSession().flush();
    }
    return nodeTypeChar;
  }

  /**
   * Loads the common charactertistic info into the object
   * 
   * @param characteristic
   */
  public static void loadCharacteristicValues( Characteristic characteristic )
  {
    String uniqueName = String.valueOf( Math.random() % 29930291 );

    characteristic.setDescription( "Desc of New Char" + uniqueName );
    characteristic.setCharacteristicName( "New Char" + uniqueName );
    characteristic.setCharacteristicDataType( CharacteristicDataType.lookup( CharacteristicDataType.TEXT ) );
    characteristic.setMinValue( new BigDecimal( "1.5" ) );
    characteristic.setMaxValue( new BigDecimal( "10.4" ) );
    characteristic.setMaxSize( new Long( 5 ) );
    characteristic.setPlName( "new picklist" );
    characteristic.setNameCmKey( "asset_key" );
    characteristic.setCmAssetCode( "test.asset.name" + uniqueName );
    characteristic.setIsRequired( Boolean.valueOf( true ) );
    characteristic.setActive( true );
  }

  private void setParticipants( PaxAudience paxAudience )
  {
    String uniqueName = String.valueOf( Math.random() % 29930291 );

    Participant participant = new Participant();

    // Set the param data onto the participant
    participant.setUserType( UserType.lookup( UserType.PARTICIPANT ) );
    participant.setFirstName( "testFIRSTNAME" );
    participant.setLastName( "testLASTNAME" );
    participant.setUserName( "testUSERNAME" + uniqueName );
    participant.setPassword( "testPASSWORD" );

    participant.setActive( Boolean.TRUE );
    participant.setWelcomeEmailSent( Boolean.FALSE );
    participant.setMasterUserId( new Long( 1 ) );
    participant.setLoginFailuresCount( new Integer( 0 ) );
    participant.setLastResetDate( new Date() );

    // from Participant
    participant.setAwardBanqExtractDate( new Date() );
    participant.setAwardBanqNumber( "testABN" );
    participant.setCentraxId( "testCXID" );
    participant.setRelationshipType( ParticipantRelationshipType.getDefaultItem() );
    participant.setSuspensionStatus( ParticipantSuspensionStatus.getDefaultItem() );
    participant.setStatus( ParticipantStatus.getDefaultItem() );
    participant.setStatusChangeDate( new Date() );

    getParticipantDAO().saveParticipant( participant );
    flushAndClearSession();
    paxAudience.addParticipant( participant );
  }

  /**
   * Builds a participant, saves the participant, builds a paxAudience and returns the saved
   * paxAudience.
   * 
   * @param uniqueString
   * @return Audience
   */
  public static Audience getSavedPaxAudience( String uniqueString )
  {
    Participant participant = ParticipantDAOImplTest.getSavedParticipantForTesting( uniqueString );
    return getAudienceDAO().save( AudienceDAOImplTest.getPaxAudience( participant ) );
  }

  /**
   * Builds and returns a saved PaxAudience from an already saved Participant.
   * 
   * @param participant
   * @return Audience
   */
  public static Audience getSavedPaxAudience( Participant participant )
  {
    return getAudienceDAO().save( getPaxAudience( participant ) );
  }

  private ParticipantDAO getParticipantDAO()
  {
    return (ParticipantDAO)getDAO( "participantDAO" );
  }

  private static AudienceDAO getAudienceDAO()
  {
    return (AudienceDAO)getDAO( "audienceDAO" );
  }

  private NodeTypeCharacteristicDAO getNodeTypeCharacteristicDAO()
  {
    return (NodeTypeCharacteristicDAO)getDAO( "nodeTypeCharacteristicDAO" );
  }

}
