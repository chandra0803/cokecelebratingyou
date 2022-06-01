/*
 * Copyright 2005 BI, Inc. All rights reserved.
 */

package com.biperf.core.dao.participant.hibernate;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.biperf.core.dao.country.CountryDAO;
import com.biperf.core.dao.employer.EmployerDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.hierarchy.HierarchyDAO;
import com.biperf.core.dao.hierarchy.NodeDAO;
import com.biperf.core.dao.hierarchy.NodeTypeDAO;
import com.biperf.core.dao.hierarchy.hibernate.NodeDAOImplTest;
import com.biperf.core.dao.participant.AudienceDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.domain.Address;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.employer.Employer;
import com.biperf.core.domain.enums.AddressType;
import com.biperf.core.domain.enums.ContactMethod;
import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.MessageSMSGroupType;
import com.biperf.core.domain.enums.ParticipantPreferenceCommunicationsType;
import com.biperf.core.domain.enums.ParticipantRelationshipType;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.ParticipantSuspensionStatus;
import com.biperf.core.domain.enums.PhoneType;
import com.biperf.core.domain.enums.PositionType;
import com.biperf.core.domain.enums.StateType;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.enums.VerificationStatusType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantCommunicationPreference;
import com.biperf.core.domain.participant.ParticipantEmployer;
import com.biperf.core.domain.participant.PaxAudience;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.participant.ParticipantSearchCriteria;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.ui.user.ContactType;
import com.biperf.core.ui.user.PaxContactType;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.value.participant.PaxIndexData;

/**
 * @author crosenquest Apr 27, 2005
 */
public class ParticipantDAOImplTest extends BaseDAOTest
{
  /**
   * Returns the country DAO.
   *
   * @return the country DAO.
   */
  protected static CountryDAO getCountryDAO()
  {
    return (CountryDAO)ApplicationContextFactory.getApplicationContext().getBean( CountryDAO.BEAN_NAME );
  }

  /**
   * Uses the ApplicationContextFactory to look up the correct DAO implementation.
   *
   * @return ParticipantDAO
   */
  public static ParticipantDAO getParticipantDAO()
  {
    return (ParticipantDAO)getDAO( "participantDAO" );
  }

  /**
   * Get the UserDAO through bean look-up.
   *
   * @return UserDAO
   */
  protected static UserDAO getUserDAO()
  {
    return (UserDAO)ApplicationContextFactory.getApplicationContext().getBean( "userDAO" );
  }

  /**
   * Get the HierarchyDAO through look-up.
   *
   * @return HierarchyDAO
   */
  protected HierarchyDAO getHierarchyDAO()
  {
    return (HierarchyDAO)ApplicationContextFactory.getApplicationContext().getBean( "hierarchyDAO" );
  }

  /**
   * Uses the ApplicationContextFactory to look up the correct DAO implementation.
   *
   * @return EmployerDAO
   */
  protected EmployerDAO getEmployerDAO()
  {
    return (EmployerDAO)ApplicationContextFactory.getApplicationContext().getBean( "employerDAO" );
  }

  /**
   * Uses the ApplicationContextFactory to look up the correct DAO implementation.
   *
   * @return NodeTypeDAO
   */
  protected NodeTypeDAO getNodeTypeDAO()
  {
    return (NodeTypeDAO)ApplicationContextFactory.getApplicationContext().getBean( "nodeTypeDAO" );
  }

  /**
   * Uses the ApplicationContextFactory to look up the correct DAO implementation.
   *
   * @return NodeDAO
   */
  protected static NodeDAO getNodeDAO()
  {
    return (NodeDAO)ApplicationContextFactory.getApplicationContext().getBean( "nodeDAO" );
  }

  private static AudienceDAO getAudienceDAO()
  {
    return (AudienceDAO)getDAO( "audienceDAO" );
  }

  /**
   * Test selecting all participants.
   */
  public void testGetAllParticipants()
  {
    Participant participant = buildStaticParticipant();
    getParticipantDAO().saveParticipant( participant );

    List participantList = getParticipantDAO().getAll();
    assertTrue( participantList.size() > 0 );
  }

  public void testGetGToHoneycombSyncData()
  {
    ParticipantDAO participantDAO = getParticipantDAO();
    Session session = HibernateSessionManager.getSession();

    Participant participant1 = buildStaticParticipant( "testUSERNAME1", "testFIRSTNAME1", "testLASTNAME1", "testEMAIL1" );
    participantDAO.saveParticipant( participant1 );
    session.flush();

    Participant participant2 = buildStaticParticipant( "testUSERNAME2", "testFIRSTNAME2", "testLASTNAME2", "testEMAIL2" );
    participantDAO.saveParticipant( participant2 );
    session.flush();

    List<Long> userIds = Arrays.asList( 60028L, 60029L );
    Map<String, Object> results = participantDAO.getGToHoneycombSyncPaxData( userIds );

    // Currently no stored procedure is tested. It just returns literal DUMMY values. Commented test
    // becomes valid with valid result
    // For now, if we got results back without exception, I guess?
    assertTrue( results != null );
    // assertTrue( "Both participants were not in the result set", ( (List<Object>)results.get(
    // "p_out_participant" ) ).size() == 2 );
  }
  
  public void testGetAdditionalContactMethods()
  {
    Participant pax = buildGenericUserForContactType( "test-1234@nowhere.com" );
    Participant pax2 = buildGenericUserForContactType( "test-1234@nowhere.com" );
    getParticipantDAO().saveParticipant( pax );
    getParticipantDAO().saveParticipant( pax2 );
    HibernateSessionManager.getSession().flush();
    List<PaxContactType> emailContacts = getParticipantDAO().getAdditionalContactMethodsByEmailOrPhone( "test-1234@nowhere.com" );
    List<PaxContactType> phoneContacts = getParticipantDAO().getAdditionalContactMethodsByEmailOrPhone( "090-000-0910" );
    assertTrue( "The email contact list not the expected size", emailContacts.size() == 4 );
    assertTrue( "The phone contact list not the expected size", phoneContacts.size() == 4 );
  }
  
  public void testGetContactsAutocompletePhone()
  {
    for ( int i = 0; i < 5; ++i )
    {
      Participant pax = buildGenericUserForContactType( "autotest-" + i + "@nowhere.com" );
      getParticipantDAO().saveParticipant( pax );
    }
    HibernateSessionManager.getSession().flush();
    List<PaxContactType> contacts = getParticipantDAO().getContactsAutocompletePhone( "090-000-0910", "autotest-" );
    assertTrue( "The contact list not the expected size", contacts.size() == 5 );
  }
  
  public void testGetContactsAutocompleteEmail()
  {
    for ( int i = 0; i < 5; ++i )
    {
      Participant pax = buildGenericUserForContactType( "autotest-" + i + "@nowhere.com" );
      getParticipantDAO().saveParticipant( pax );
    }
    HibernateSessionManager.getSession().flush();
    List<PaxContactType> contacts = getParticipantDAO().getContactsAutocompleteEmail( "autotest-1@nowhere.com", "autotest-" );
    assertTrue( "The contact list not the expected size", contacts.size() == 2 );
  }

  @SuppressWarnings( { "static-access" } )
  public void testGetValidUserContactMethodsByEmailWithoutUniqueResults()
  {
    Participant pax = buildGenericUserForContactType( "test-1234@nowhere.com" );
    Participant pax2 = buildGenericUserForContactType( "test-1234@nowhere.com" );
    // change a phone number so we can see we only get phone numbers
    pax2.getPrimaryPhone().setPhoneNbr( "444-434-1111" );
    this.getParticipantDAO().saveParticipant( pax );
    this.getParticipantDAO().saveParticipant( pax2 );
    HibernateSessionManager.getSession().flush();
    List<PaxContactType> contacts = getParticipantDAO().getValidUserContactMethodsByEmail( "test-1234@nowhere.com" );
    assertNotNull( "Contacts is null", contacts );
    assertTrue( "The returned contact list not the size expects", contacts.size() == 4 );
  }

  @SuppressWarnings( { "static-access", "unchecked" } )
  public void testGetValidUserContactMethodsByEmailWithoutUniqueResultsMulitpleEmails()
  {
    Participant pax = buildGenericUserForContactType( "test-1234@nowhere.com" );
    Participant pax2 = buildGenericUserForContactType( "test-1234@nowhere.com" );
    // create additional email
    UserEmailAddress emailAddress = new UserEmailAddress();
    emailAddress.setEmailAddr( "TEST-DUDE@nowhere.com" );
    emailAddress.setIsPrimary( false );
    emailAddress.setEmailType( EmailAddressType.lookup( EmailAddressType.OTHER ) );
    emailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    emailAddress.setUser( pax );
    pax.getUserEmailAddresses().add( emailAddress );
    // change a phone number so we can see we only get phone numbers
    pax2.getPrimaryPhone().setPhoneNbr( "444-434-1121" );
    this.getParticipantDAO().saveParticipant( pax );
    this.getParticipantDAO().saveParticipant( pax2 );
    HibernateSessionManager.getSession().flush();
    List<PaxContactType> contacts = getParticipantDAO().getValidUserContactMethodsByEmail( "test-1234@nowhere.com" );
    assertNotNull( "Contacts is null", contacts );
    assertTrue( "The returned contact list not the size expects", contacts.size() == 5 );
  }

  @SuppressWarnings( { "static-access" } )
  public void testGetValidUserContactMethodsByUserIdWithUniqueResults()
  {
    Participant pax = buildGenericUserForContactType( "test-1234@nowhere.com" );
    Participant pax2 = buildGenericUserForContactType( "test-1234@nowhere.com" );
    // change a phone number so we can see we only get phone numbers
    pax2.getPrimaryPhone().setPhoneNbr( "444-434-1111" );
    this.getParticipantDAO().saveParticipant( pax );
    this.getParticipantDAO().saveParticipant( pax2 );
    HibernateSessionManager.getSession().flush();
    List<PaxContactType> contacts = getParticipantDAO().getValidUniqueUserContactMethodsByUserId( pax2.getId() );
    assertNotNull( "Contacts is null", contacts );
    assertTrue( "The returned contact list not the size expects", contacts.size() == 1 );
    for ( PaxContactType c : contacts )
    {
      if ( c.getContactType() == ContactType.PHONE )
      {
        String value = c.getValue();
        assertTrue( "The phone is invalid", value.equalsIgnoreCase( "444-434-1111" ) );
      }
    }
  }

  @SuppressWarnings( { "static-access", "unchecked" } )
  public void testGetValidUserContactMethodsByUserIdWithoutUniqueResultsRequiresMulitpleEmails()
  {
    Participant pax = buildGenericUserForContactType( "test-1234@nowhere.com" );
    Participant pax2 = buildGenericUserForContactType( "test-1234@nowhere.com" );
    // create additional email
    UserEmailAddress emailAddress = new UserEmailAddress();
    emailAddress.setEmailAddr( "TEST-DUDE@nowhere.com" );
    emailAddress.setIsPrimary( false );
    emailAddress.setEmailType( EmailAddressType.lookup( EmailAddressType.OTHER ) );
    emailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    emailAddress.setUser( pax );
    pax.getUserEmailAddresses().add( emailAddress );
    // change a phone number so we can see we only get phone numbers
    pax2.getPrimaryPhone().setPhoneNbr( "444-434-1121" );
    this.getParticipantDAO().saveParticipant( pax );
    this.getParticipantDAO().saveParticipant( pax2 );
    HibernateSessionManager.getSession().flush();
    List<PaxContactType> contacts = getParticipantDAO().getValidUserContactMethodsByUserId( pax.getId() );
    assertNotNull( "Contacts is null", contacts );
    assertTrue( "The returned contact list not the size expects", contacts.size() == 3 );
    boolean emailFound = false;
    for ( PaxContactType c : contacts )
    {
      if ( c.getContactType() == ContactType.EMAIL )
      {
        String value = c.getValue();
        if ( value.equalsIgnoreCase( "TEST-DUDE@nowhere.com" ) )
        {
          emailFound = true;
        }
      }
      if ( c.getContactType() == ContactType.PHONE )
      {
        String value = c.getValue();
        if ( value.equalsIgnoreCase( "444-434-1121" ) )
        {
          fail( "Should no find the '444-434-1121' phone number" );
        }
      }
    }
    if ( !emailFound )
    {
      fail( "Expected to find the 'TEST-DUDE@nowhere.com' email" );
    }
  }

  @SuppressWarnings( { "static-access", "unchecked" } )
  public void testGetValidUserContactMethodsByUserIdWithoutUniqueResultsMulitpleEmails()
  {
    Participant pax = buildGenericUserForContactType( "test-1234@nowhere.com" );
    Participant pax2 = buildGenericUserForContactType( "test-1234@nowhere.com" );
    // create additional email
    UserEmailAddress emailAddress = new UserEmailAddress();
    emailAddress.setEmailAddr( "TEST-DUDE@nowhere.com" );
    emailAddress.setIsPrimary( false );
    emailAddress.setEmailType( EmailAddressType.lookup( EmailAddressType.OTHER ) );
    emailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    emailAddress.setUser( pax );
    pax.getUserEmailAddresses().add( emailAddress );
    // change a phone number so we can see we only get phone numbers
    pax2.getPrimaryPhone().setPhoneNbr( "444-434-1121" );
    this.getParticipantDAO().saveParticipant( pax );
    this.getParticipantDAO().saveParticipant( pax2 );
    HibernateSessionManager.getSession().flush();
    List<PaxContactType> contacts = getParticipantDAO().getValidUniqueUserContactMethodsByUserId( pax.getId() );
    assertNotNull( "Contacts is null", contacts );
    assertTrue( "The returned contact list not the size expects", contacts.size() == 2 );
    boolean emailFound = false;
    for ( PaxContactType c : contacts )
    {
      if ( c.getContactType() == ContactType.EMAIL )
      {
        String value = c.getValue();
        if ( value.equalsIgnoreCase( "TEST-DUDE@nowhere.com" ) )
        {
          emailFound = true;
        }
      }
      if ( c.getContactType() == ContactType.PHONE )
      {
        String value = c.getValue();
        if ( value.equalsIgnoreCase( "444-434-1121" ) )
        {
          fail( "Should no find the '444-434-1121' phone number" );
        }
      }
    }
    if ( !emailFound )
    {
      fail( "Expected to find the 'TEST-DUDE@nowhere.com' email" );
    }
  }

  @SuppressWarnings( { "static-access", "unchecked" } )
  public void testGetValidUniqueUserContactMethodsByEmail()
  {
    Participant pax = buildGenericUserForContactType( "test-1234@nowhere.com" );
    Participant pax2 = buildGenericUserForContactType( "test-1234@nowhere.com" );
    // create additional email
    UserEmailAddress emailAddress = new UserEmailAddress();
    emailAddress.setEmailAddr( "TEST-DUDE@nowhere.com" );
    emailAddress.setIsPrimary( false );
    emailAddress.setEmailType( EmailAddressType.lookup( EmailAddressType.OTHER ) );
    emailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    emailAddress.setUser( pax );
    pax.getUserEmailAddresses().add( emailAddress );
    // change a phone number so we can see we only get phone numbers
    pax2.getPrimaryPhone().setPhoneNbr( "444-434-1121" );
    this.getParticipantDAO().saveParticipant( pax );
    this.getParticipantDAO().saveParticipant( pax2 );
    HibernateSessionManager.getSession().flush();
    List<PaxContactType> contacts = getParticipantDAO().getValidUniqueUserContactMethodsByEmailOrPhone( "test-1234@nowhere.com" );
    assertNotNull( "Contacts is null", contacts );
    assertTrue( "The returned contact list not the size expects", contacts.size() == 3 );
    boolean emailFound = false;
    boolean phoneFound = false;
    for ( PaxContactType c : contacts )
    {
      if ( c.getContactType() == ContactType.EMAIL )
      {
        String value = c.getValue();
        if ( value.equalsIgnoreCase( "TEST-DUDE@nowhere.com" ) )
        {
          emailFound = true;
        }
      }
      if ( c.getContactType() == ContactType.PHONE )
      {
        String value = c.getValue();
        if ( value.equalsIgnoreCase( "444-434-1121" ) )
        {
          phoneFound = true;
        }
      }
    }
    if ( !emailFound || !phoneFound )
    {
      fail( "Expected to find the 'TEST-DUDE@nowhere.com' email and 444-434-1121 Phone" );
    }
  }

  public void testGetValidUserContactMethodsByUserId()
  {
    Participant pax = buildGenericUserForContactType( "mytest@eeemail.com" );
    getParticipantDAO().saveParticipant( pax );
    HibernateSessionManager.getSession().flush();
    List<PaxContactType> contacts = getParticipantDAO().getValidUniqueUserContactMethodsByUserId( pax.getId() );
    assertNotNull( "Contacts is null", contacts );
    assertTrue( "The returned contact list is empty", !contacts.isEmpty() );
  }

  public void testFailGetValidUserContactMethodsByUserId()
  {
    List<PaxContactType> contacts = getParticipantDAO().getValidUniqueUserContactMethodsByUserId( -12312L );
    assertNotNull( "Contacts is null", contacts );
    assertTrue( "The returned contact list is noe empty", contacts.isEmpty() );
  }

  @SuppressWarnings( "unchecked" )
  private static Participant buildGenericUserForContactType( String email )
  {
    Participant pax = buildStaticParticipant( buildUniqueString(), "chad", "vader", email );
    // creat phone
    UserPhone phone = new UserPhone();
    phone.setIsPrimary( true );
    phone.setPhoneNbr( "090-000-0910" );
    phone.setPhoneType( PhoneType.lookup( PhoneType.MOBILE ) );
    phone.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    phone.setUser( pax );
    pax.getUserPhones().add( phone );

    // create email
    UserEmailAddress emailAddress = new UserEmailAddress();
    emailAddress.setEmailAddr( email );
    emailAddress.setIsPrimary( true );
    emailAddress.setEmailType( EmailAddressType.lookup( EmailAddressType.HOME ) );
    emailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    emailAddress.setUser( pax );
    pax.getUserEmailAddresses().add( emailAddress );

    // create a userAddress
    UserAddress address = new UserAddress();
    address.setAddressType( AddressType.lookup( AddressType.HOME_TYPE ) );
    address.setIsPrimary( true );
    address.setUser( pax );
    Address addr = new Address();
    addr.setAddr1( "nothing" );
    addr.setCity( "nothing" );

    addr.setCountry( getCountryDAO().getCountryByCode( "us" ) );
    address.setAddress( addr );
    pax.getUserAddresses().add( address );
    return pax;
  }

  public void testGetValidUserContactMethodsByEmail()
  {
    Participant pax = buildGenericUserForContactType( "mytest@email.com" );
    getParticipantDAO().saveParticipant( pax );
    HibernateSessionManager.getSession().flush();
    List<PaxContactType> contacts = getParticipantDAO().getValidUserContactMethodsByEmail( "mytest@email.com" );
    assertNotNull( "Contacts is null", contacts );
    assertTrue( "The returned contact list is empty", !contacts.isEmpty() );
  }

  public void testFailGetValidUserContactMethodsByEmail()
  {
    List<PaxContactType> contacts = getParticipantDAO().getValidUserContactMethodsByEmail( "BOGUS" );
    assertNotNull( "Contacts is null", contacts );
    assertTrue( "The returned contact list is noe empty", contacts.isEmpty() );
  }

  /**
   * Tests searching for a list of participants with an association to a Node param.
   */
  public void testParticipantsForNode()
  {

    ParticipantDAO participantDAO = getParticipantDAO();
    HierarchyDAO hierarchyDAO = getHierarchyDAO();

    NodeDAO nodeDAO = getNodeDAO();

    NodeType nodeToSave = new NodeType();
    nodeToSave.setCmAssetCode( "asset1" );
    nodeToSave.setNameCmKey( "key50" );
    getNodeTypeDAO().saveNodeType( nodeToSave );

    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "testNAME" );
    hierarchy.setCmAssetCode( "testASSET" );
    hierarchy.setNameCmKey( "testKEY" );
    hierarchyDAO.save( hierarchy );

    Session session = HibernateSessionManager.getSession();

    Node node1 = new Node();
    node1.setName( "testNODENAME1" );
    node1.setDescription( "testNODEDESCRIPTION1" );
    node1.setNodeType( nodeToSave );
    node1.setHierarchy( hierarchy );
    node1.setPath( "path" );
    nodeDAO.saveNode( node1 );

    Node node2 = NodeDAOImplTest.buildUniqueNode( "Node2", nodeToSave, hierarchy );
    nodeDAO.saveNode( node2 );

    Participant participant1 = buildStaticParticipant( "testUSERNAME1", "testFIRSTNAME1", "testLASTNAME1", "testEMAIL1" );
    participantDAO.saveParticipant( participant1 );
    session.flush();

    participant1.addNode( node1, Boolean.TRUE, HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ), false );
    participantDAO.saveParticipant( participant1 );

    Participant participant2 = buildStaticParticipant( "testUSERNAME2", "testFIRSTNAME2", "testLASTNAME2", "testEMAIL2" );
    participant2.addNode( node1, Boolean.TRUE, HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ), false );
    participantDAO.saveParticipant( participant2 );

    Participant participant3 = buildStaticParticipant( "testUSERNAME3", "testFIRSTNAME3", "testLASTNAME3", "testEMAIL3" );
    participant3.addNode( node1, Boolean.TRUE, HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ), false );
    participantDAO.saveParticipant( participant3 );

    Participant participant4 = buildStaticParticipant( "testUSERNAME4", "testFIRSTNAME4", "testLASTNAME4", "testEMAIL4" );
    participant4.addNode( node2, Boolean.TRUE, HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ), false );
    participantDAO.saveParticipant( participant4 );
    session.flush();

    session.evict( node1 );
    Node savedNode1 = nodeDAO.getNodeById( node1.getId() );

    assertNotNull( savedNode1 );
    assertNotNull( savedNode1.getUserNodes() );
    System.out.println( " saved: " + savedNode1.getUserNodes().size() );

    assertTrue( "Participant list from search for node wasn't what was expected", savedNode1.getUserNodes().size() >= 3 );
    // assertTrue("Participant list from search for node wasn't what was expected",
    // participantDAO.searchParticipantByNode(node2).size() >= 1);

  }

  /**
   * Test creating and saving a participant to the database. The get the participant by the
   * generated id to ensure it had been saved correctly.
   */
  public void testSaveParticipantAndGetParticipantById()
  {

    ParticipantDAO participantDAO = getParticipantDAO();

    Participant expectedParticipant = buildStaticParticipant();

    participantDAO.saveParticipant( expectedParticipant );

    HibernateSessionManager.getSession().flush();

    // Add the participant contact methods
    expectedParticipant.addContactMethod( ContactMethod.lookup( ContactMethod.EMAIL ), Boolean.TRUE );
    expectedParticipant.addContactMethod( ContactMethod.lookup( ContactMethod.TELEPHONE ), Boolean.FALSE );

    ParticipantPreferenceCommunicationsType textType = ParticipantPreferenceCommunicationsType.lookup( "text" );

    ParticipantPreferenceCommunicationsType eStatementType = ParticipantPreferenceCommunicationsType.lookup( "estatement" );

    ParticipantPreferenceCommunicationsType notSure = ParticipantPreferenceCommunicationsType.lookup( ParticipantPreferenceCommunicationsType.TEXT_MESSAGES );

    ParticipantCommunicationPreference pcp = new ParticipantCommunicationPreference();
    pcp.setParticipantPreferenceCommunicationsType( textType );
    pcp.setParticipant( expectedParticipant );
    pcp.setMessageSMSGroupType( MessageSMSGroupType.lookup( "goalReminders" ) );

    ParticipantCommunicationPreference pcp2 = new ParticipantCommunicationPreference();
    pcp2.setParticipantPreferenceCommunicationsType( eStatementType );
    pcp2.setParticipant( expectedParticipant );

    ParticipantCommunicationPreference pcp3 = new ParticipantCommunicationPreference();
    pcp3.setParticipantPreferenceCommunicationsType( notSure );
    pcp3.setMessageSMSGroupType( MessageSMSGroupType.lookup( "goalReminders" ) );

    // TODO: Call Correct Setters
    // expectedParticipant.addCommunicationPreference(pcp);
    // expectedParticipant.addCommunicationPreference(pcp2);
    // expectedParticipant.addCommunicationPreference(pcp3);

    // Add a few employers onto the participant
    expectedParticipant.addParticipantEmployer( buildParticipantEmployer( buildEmployer( "testNAME1" ) ) );
    expectedParticipant.addParticipantEmployer( buildParticipantEmployer( buildEmployer( "testNAME2" ) ) );
    expectedParticipant.addParticipantEmployer( buildParticipantEmployer( buildEmployer( "testNAME3" ) ) );

    participantDAO.saveParticipant( expectedParticipant );

    HibernateSessionManager.getSession().flush();
    expectedParticipant.getParticipantEmployers().remove( 0 );
    HibernateSessionManager.getSession().flush();

    Participant actualParticipant = participantDAO.getParticipantById( expectedParticipant.getId() );
    System.out.println( "Participant: " + actualParticipant );
    assertTrue( "Actual participant wasn't equals to expected.", expectedParticipant.equals( actualParticipant ) );

  }

  /**
   * Test searching the database through the DAO to find a list of Participants with the parameters
   */
  public void testSearchParticipant()
  {

    ParticipantDAO participantDAO = getParticipantDAO();

    Participant participant1 = buildStaticParticipant( "testABC", "testABC", "testABC", "testABC" );
    participantDAO.saveParticipant( participant1 );

    Participant participant2 = buildStaticParticipant( "testXYZ", "testXYZ", "testXYZ", "testXYZ" );
    participantDAO.saveParticipant( participant2 );

    Participant participant3 = buildStaticParticipant( "testXAZ", "testXAZ", "testXAZ", "testXAZ" );
    participantDAO.saveParticipant( participant3 );

    ParticipantSearchCriteria searchCriteria = new ParticipantSearchCriteria();
    searchCriteria.setUserName( "A" );

    List actualList = participantDAO.searchParticipant( searchCriteria );

    assertTrue( "Actual list didn't contain expected participant1", actualList.contains( participant1 ) );

  }

  /**
   * Test searching the database through the DAO to find a list of Participants with the parameters
   */
  public void testSearchParticipantInNodes()
  {

    ParticipantDAO participantDAO = getParticipantDAO();
    HierarchyDAO hierarchyDAO = getHierarchyDAO();

    NodeDAO nodeDAO = getNodeDAO();

    NodeType nodeToSave = new NodeType();
    nodeToSave.setCmAssetCode( "asset1" );
    nodeToSave.setNameCmKey( "key50" );
    getNodeTypeDAO().saveNodeType( nodeToSave );

    Hierarchy hierarchy = new Hierarchy();
    hierarchy.setName( "testNAME" );
    hierarchy.setCmAssetCode( "testASSET" );
    hierarchy.setNameCmKey( "testKEY" );
    hierarchyDAO.save( hierarchy );

    Session session = HibernateSessionManager.getSession();

    Node node1 = new Node();
    node1.setName( "testNODENAME1" );
    node1.setDescription( "testNODEDESCRIPTION1" );
    node1.setNodeType( nodeToSave );
    node1.setHierarchy( hierarchy );
    node1.setPath( "path" );
    nodeDAO.saveNode( node1 );

    Participant participant1 = buildStaticParticipant( "testUSERNAME1", "testFIRSTNAME1", "testLASTNAME1", "testEMAIL1" );
    participantDAO.saveParticipant( participant1 );
    session.flush();

    participant1.addNode( node1, Boolean.TRUE, HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ), false );
    participantDAO.saveParticipant( participant1 );

    List nodeList = new ArrayList();
    nodeList.add( node1 );
    ParticipantSearchCriteria searchCriteria = new ParticipantSearchCriteria();
    searchCriteria.setNodeList( nodeList );

    List actualList = participantDAO.searchParticipant( searchCriteria );

    assertTrue( "Actual list didn't contain expected participant1", actualList.contains( participant1 ) );

  }

  public void testGetPaxAudienceParticipants()
  {
    Participant testPax = ParticipantDAOImplTest.buildUniqueParticipant( "testPax1" );
    Participant otherPax = ParticipantDAOImplTest.buildUniqueParticipant( "otherPax1" );

    List expectedPaxAudiences = new ArrayList();

    getParticipantDAO().saveParticipant( testPax );
    getParticipantDAO().saveParticipant( otherPax );

    PaxAudience paxAudienceWithJustTestPax1 = (PaxAudience)AudienceDAOImplTest.getPaxAudience( testPax, "1" );

    PaxAudience paxAudienceWithJustTestPax2 = (PaxAudience)AudienceDAOImplTest.getPaxAudience( testPax, "2" );

    PaxAudience paxAudienceWithTestPaxAndOtherPax = (PaxAudience)AudienceDAOImplTest.getPaxAudience( testPax, "3" );
    paxAudienceWithTestPaxAndOtherPax.addParticipant( otherPax );

    PaxAudience paxAudienceWithJustOtherPax = (PaxAudience)AudienceDAOImplTest.getPaxAudience( otherPax, "4" );

    getAudienceDAO().save( paxAudienceWithJustTestPax1 );
    getAudienceDAO().save( paxAudienceWithJustTestPax2 );
    getAudienceDAO().save( paxAudienceWithTestPaxAndOtherPax );
    getAudienceDAO().save( paxAudienceWithJustOtherPax );

    expectedPaxAudiences.add( paxAudienceWithJustTestPax1 );
    expectedPaxAudiences.add( paxAudienceWithJustTestPax2 );
    expectedPaxAudiences.add( paxAudienceWithTestPaxAndOtherPax );

    flushAndClearSession();

    // Now confirm pax audiences

    List actualPaxAudiences = getAudienceDAO().getAllPaxAudiencesByParticipantId( testPax.getId() );
    // assertEquals( 3, actualPaxAudiences.size() );

    assertEquals( expectedPaxAudiences, actualPaxAudiences );

  }

  public void testGetAllActivePaxNotWithCommunicationPreference()
  {
    AssociationRequestCollection paxAssociationRequestCollection = new AssociationRequestCollection();
    paxAssociationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.ADDRESS ) );

    getParticipantDAO().getAllActivePaxWithCommunicationPreferenceInCampaign( "11040",
                                                                              ParticipantPreferenceCommunicationsType.lookup( ParticipantPreferenceCommunicationsType.E_STATEMENTS ),
                                                                              paxAssociationRequestCollection,
                                                                              1,
                                                                              25 );
  }

  public void testGetParticipantIndexData()
  {
    List<PaxIndexData> participantIndexData = getParticipantDAO().getParticipantIndexData( Arrays.asList( 1263L, 1264L ) );
    assertTrue( participantIndexData.size() > 0 );
  }

  /**
   * Builds a participant with the unique params.
   *
   * @param username
   * @param firstname
   * @param lastname
   * @param emailAddr
   * @return Participant
   */
  private static Participant buildStaticParticipant( String username, String firstname, String lastname, String emailAddr )
  {
    Participant participant = new Participant();

    // Set the param data onto the participant
    participant.setUserType( UserType.lookup( UserType.PARTICIPANT ) );
    participant.setFirstName( firstname );
    participant.setLastName( lastname );
    participant.setUserName( username );

    // Add the remaining bits onto the participant
    completeParticipant( participant );

    return participant;

  }

  public static Participant buildAndSaveParticipant( String uniqueString )
  {
    Participant participant = buildUniqueParticipant( uniqueString );

    // Associate the participant with a node.
    Node node = NodeDAOImplTest.buildUniqueNode( buildUniqueString() );
    getNodeDAO().saveNode( node );

    UserNode userNode = new UserNode( participant, node );
    userNode.setIsPrimary( true );
    userNode.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.MEMBER ) );
    participant.addUserNode( userNode );

    // Save the participant.
    getUserDAO().saveUser( participant );

    return participant;
  }

  public static Participant buildUniqueParticipant( String uniqueString )
  {

    Participant participant = new Participant();

    // Set the param data onto the participant
    participant.setUserType( UserType.lookup( UserType.PARTICIPANT ) );
    participant.setFirstName( "firstname" + uniqueString );
    participant.setLastName( "lastname" + uniqueString );
    participant.setUserName( "username" + uniqueString );

    // Add the remaining bits onto the participant
    completeParticipant( participant );

    return participant;
  }

  /**
   * Build a static participant for testing.
   *
   * @return Participant
   */
  public static Participant buildStaticParticipant()
  {
    return buildStaticParticipant( "testUSERNAME" + System.currentTimeMillis(), "testFIRSTNAME", "testLASTNAME", "testEMAILADDR" );
  }

  /**
   * Attaches the remaining bits onto the participant. This is seperate to avoid needing to repeat
   * it each time when building a Participant.
   *
   * @param participant
   */
  private static void completeParticipant( Participant participant )
  {

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
    participant.setRelationshipType( ParticipantRelationshipType.lookup( ParticipantRelationshipType.SELF ) );
    participant.setSuspensionStatus( ParticipantSuspensionStatus.lookup( ParticipantSuspensionStatus.NONE ) );
    participant.setStatus( ParticipantStatus.getDefaultItem() );
    participant.setStatusChangeDate( new Date() );
    participant.setSsn( "111223333" );
  }

  /**
   * Builds an employer which is attached to the Participant for testing.
   *
   * @param name
   * @return Employer
   */
  private Employer buildEmployer( String name )
  {

    Employer employer = new Employer();
    employer.setName( name );

    Address address = new Address();
    address.setAddr1( "testADDR1" );
    address.setAddr2( "testADDR2" );
    address.setAddr3( "testADDR3" );
    address.setCity( "testCITY" );
    address.setCountry( getCountryDAO().getCountryByCode( Country.UNITED_STATES ) );
    address.setPostalCode( "testPOSTALCODE" );
    List stateList = StateType.getList( address.getCountry().getCountryCode() );
    if ( stateList.size() > 0 )
    {
      address.setStateType( (StateType)stateList.get( 0 ) );
    }
    employer.setAddress( address );

    getEmployerDAO().saveEmployer( employer );

    return employer;

  }

  /**
   * Method to manage building a participantEmployer.
   *
   * @param employer
   * @return ParticipantEmployer
   */
  private ParticipantEmployer buildParticipantEmployer( Employer employer )
  {
    ParticipantEmployer participantEmployer = new ParticipantEmployer();
    participantEmployer.setEmployer( employer );
    participantEmployer.setDepartmentType( DepartmentType.lookup( DepartmentType.MARKETING ).getName() );
    participantEmployer.setPositionType( PositionType.lookup( PositionType.MANAGER ).getName() );
    participantEmployer.setHireDate( new Date() );
    return participantEmployer;
  }

  public static Participant getSavedParticipantForTesting( String uniqueString )
  {
    Participant participant = ParticipantDAOImplTest.buildUniqueParticipant( uniqueString );
    return getParticipantDAO().saveParticipant( participant );
  }

  public void testPaxIdsWhoDisabledPublicProfile()
  {

    long testPaxId = 5583L;
    List<Long> disbaledPaxIdsList = getParticipantDAO().findPaxIdsWhoDisabledPublicProfile( Arrays.asList( testPaxId ) );
    assertTrue( isEmpty( disbaledPaxIdsList ) );

    Participant participant = getParticipantDAO().getParticipantById( testPaxId );
    participant.setAllowPublicInformation( false );
    flushAndClearSession();

    disbaledPaxIdsList = getParticipantDAO().findPaxIdsWhoDisabledPublicProfile( Arrays.asList( testPaxId ) );
    assertTrue( isNotEmpty( disbaledPaxIdsList ) );

    participant = getParticipantDAO().getParticipantById( testPaxId );
    participant.setAllowPublicInformation( true );
    flushAndClearSession();

  }

  public void testIsParticipantRecoveryOptionsAvailableFail()
  {
    Long testPaxId = 5583L;
    boolean hasContacts = getParticipantDAO().isParticipantRecoveryContactsAvailable( testPaxId );
    assertTrue( !hasContacts );
  }
}
