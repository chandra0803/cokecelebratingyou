/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/participant/impl/ParticipantServiceImplTest.java,v $
 */

package com.biperf.core.service.participant.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.hibernate.Session;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.junit.Test;

import com.biperf.core.dao.employer.EmployerDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.promotion.PublicRecognitionDAO;
import com.biperf.core.domain.employer.Employer;
import com.biperf.core.domain.enums.ContactMethod;
import com.biperf.core.domain.enums.EmailAddressType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.MessageSMSGroupType;
import com.biperf.core.domain.enums.MockPickListFactory;
import com.biperf.core.domain.enums.ParticipantEnrollmentSource;
import com.biperf.core.domain.enums.ParticipantPreferenceCommunicationsType;
import com.biperf.core.domain.enums.ParticipantRelationshipType;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.ParticipantSuspensionStatus;
import com.biperf.core.domain.enums.ParticipantTermsAcceptance;
import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.domain.enums.VerificationStatusType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantCommunicationPreference;
import com.biperf.core.domain.participant.ParticipantContactMethod;
import com.biperf.core.domain.participant.ParticipantEmployer;
import com.biperf.core.domain.promotion.ParticipantFollowers;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.domain.user.UserTNCHistory;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.participant.ParticipantSearchCriteria;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.MockContentReader;
import com.biperf.core.utils.ResourceManager;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.hc.AccountSyncParticipantDetails;
import com.biperf.core.value.hc.AccountSyncRequest;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * ParticipantServiceImplTest.
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
 * <td>crosenquest</td>
 * <td>May 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ParticipantServiceImplTest extends MockObjectTestCase
{

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public ParticipantServiceImplTest( String test )
  {
    super( test );
  }

  /** ParticipantServiceImpl - the class we are testing */
  private ParticipantServiceImpl participantServiceImpl = new ParticipantServiceImpl();

  /** mockParticipantDAO - Mocks the DAO interface for this service test. */
  private Mock mockParticipantDAO = null;

  /** mockEmployerDAO - Mocks the DAO interface for this service test. */
  private Mock mockEmployerDAO = null;
  private Mock mockUserService = null;

  private Mock mockawardBanQServiceFactory = null;
  private Mock mockAwardBanQService = null;

  private Mock mockPublicRecognitionDAO = null;

  private Mock mockSystemVariableService = null;

  private AuthenticatedUser user;

  /**
   * Mock representation of a participant's status. This is normally represented in a PickList item.
   */
  private static final ParticipantStatus mockParticipantStatus = new ParticipantStatus()
  {
    public String getCode()
    {
      return "active";
    }
  };

  /**
   * Mock representation of a participant's suspension status. This is normally represented in a
   * PickList item.
   */
  private static final ParticipantSuspensionStatus mockParticipantSuspensionStatus = new ParticipantSuspensionStatus()
  {
    public String getCode()
    {
      return "none";
    }
  };

  /**
   * Mock representation of a participant's relationship to other participants. This is normally
   * represented in a PickList item.
   */
  private static ParticipantRelationshipType mockParticipantRelationship = new ParticipantRelationshipType()
  {
    public String getCode()
    {
      return "none";
    }
  };

  /**
   * Mock representation of a participant's enrollment source. This is normally represented in a
   * PickList item.
   */
  private static final ParticipantEnrollmentSource mockParticipantEnrollmentSource = new ParticipantEnrollmentSource()
  {
    public String getCode()
    {
      return "web";
    }
  };

  /**
   * Mock representation of a participant's communication preference type.
   */
  private static final ParticipantPreferenceCommunicationsType mockParticipantPreferenceCommunicationsTypeTextMessage = new ParticipantPreferenceCommunicationsType()
  {
    public String getCode()
    {
      return ParticipantPreferenceCommunicationsType.TEXT_MESSAGES;
    }
  };

  /**
   * Mock representation of a participant's communication preference type.
   */
  private static final ParticipantPreferenceCommunicationsType mockParticipantPreferenceCommunicationsTypeEStatement = new ParticipantPreferenceCommunicationsType()
  {
    public String getCode()
    {
      return ParticipantPreferenceCommunicationsType.E_STATEMENTS;
    }
  };

  /**
   * Mock representation of a participant's contact method type.
   */
  private static final ContactMethod mockContactMethod = new ContactMethod()
  {
    public String getCode()
    {
      return "email";
    }
  };

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {

    // Prepares the pickListFactory
    PickListItem.setPickListFactory( new MockPickListFactory() );
    // check if the ContentReader is already set - true if we are in container.
    if ( ContentReaderManager.getContentReader() == null )
    {
      ContentReaderManager.setContentReader( new MockContentReader() );
    }

    mockParticipantDAO = new Mock( ParticipantDAO.class );
    mockEmployerDAO = new Mock( EmployerDAO.class );
    mockPublicRecognitionDAO = new Mock( PublicRecognitionDAO.class );
    mockSystemVariableService = new Mock( SystemVariableService.class );
    participantServiceImpl.setParticipantDAO( (ParticipantDAO)mockParticipantDAO.proxy() );
    participantServiceImpl.setEmployerDAO( (EmployerDAO)mockEmployerDAO.proxy() );
    participantServiceImpl.setPublicRecognitionDAO( (PublicRecognitionDAO)mockPublicRecognitionDAO.proxy() );
    //participantServiceImpl.setSystemVariableService( (SystemVariableService)mockSystemVariableService.proxy() );
    mockawardBanQServiceFactory = new Mock( AwardBanQServiceFactory.class );
    participantServiceImpl.setAwardBanQServiceFactory( (AwardBanQServiceFactory)mockawardBanQServiceFactory.proxy() );

    mockAwardBanQService = new Mock( AwardBanQService.class );
    mockUserService = new Mock( UserService.class );
    participantServiceImpl.setUserService( (UserService)mockUserService.proxy() );

    user = new AuthenticatedUser();
    user.setLocale( Locale.US );
    user.setPrimaryCountryCode( "us" ); // US

    UserManager.setUser( user );
  }

  /**
   * Test saveParticipant.
   */
  /*
   * public void testSaveParticipant() { Participant expectedParticipant = buildStaticParticipant();
   * mockParticipantDAO.expects( once() ).method( "saveParticipant" ) .with( same(
   * expectedParticipant ) ).will( returnValue( expectedParticipant ) ); Participant
   * actualParticipant = this.participantServiceImpl .saveParticipant( expectedParticipant );
   * assertEquals( "Actual Participant wasn't equals to what was expected", expectedParticipant,
   * actualParticipant ); }
   */
  /**
   * Test saveParticipant.
   * 
   * @throws ServiceErrorException
   */
  public void testSaveParticipant() throws ServiceErrorException
  {
    Participant initialParticipant = buildStaticParticipant();
    initialParticipant.setFirstName( "Mike" );
    initialParticipant.setLastName( "Jones" );
    initialParticipant.setAwardBanqNumber( "1234" );

    Participant expectedParticipant = buildStaticParticipant();
    expectedParticipant.setAwardBanqNumber( "1234" );

    mockParticipantDAO.expects( atLeastOnce() ).method( "getParticipantOverviewById" ).will( returnValue( initialParticipant ) );

    mockParticipantDAO.expects( atLeastOnce() ).method( "saveParticipant" ).with( same( expectedParticipant ) ).will( returnValue( expectedParticipant ) );

    mockawardBanQServiceFactory.expects( atLeastOnce() ).method( "getAwardBanQService" ).will( returnValue( mockAwardBanQService.proxy() ) );

    mockAwardBanQService.expects( atLeastOnce() ).method( "updateParticipantInAwardBanQWebService" ).with( same( initialParticipant ) );

    UserTNCHistory userTNCHistory = addPaxTNCHistory( expectedParticipant );
    mockParticipantDAO.expects( atLeastOnce() ).method( "saveTNCHistory" );
    Participant actualParticipant = this.participantServiceImpl.saveParticipantWithTNCHistory( expectedParticipant, userTNCHistory );

    Participant actualParticipantWithTNCHistory = this.participantServiceImpl.saveParticipantWithTNCHistory( expectedParticipant, userTNCHistory );

    assertEquals( "Actual Participant wasn't equals to what was expected", expectedParticipant, actualParticipant );

    assertEquals( "Actual Participant wasn't equals to what was expected", expectedParticipant, actualParticipantWithTNCHistory );

  }

  /**
   * TODO Commented out until I figure out how to mock the save call with different input and output
   * for each call. Test createParticipant.
   */
  /*
   * public void testCreateFullParticipant() { Participant strippedParticipant =
   * buildStaticParticipant(); Participant fullParticipant = buildStaticParticipant(); UserAddress
   * userAddress = new UserAddress(); userAddress.setAddressType( mockHomAddressType ); Address
   * address = new Address(); address.setCountryType( mockUSCountryType ); address.setAddr1( "addr1"
   * ); address.setCity( "city" ); address.setStateType( mockMNStateType ); address.setPostalCode(
   * "12345" ); userAddress.setAddress( address ); userAddress.setIsPrimary( Boolean.valueOf( true
   * )); fullParticipant.getUserAddresses().add( userAddress ); UserEmailAddress userEmail = new
   * UserEmailAddress(); userEmail.setEmailType( mockHomeEmailAddressType ); userEmail.setEmailAddr(
   * "test@test.com" ); userEmail.setIsPrimary( Boolean.valueOf( true ));
   * fullParticipant.getUserEmailAddresses().add( userEmail ); UserPhone userPhone = new
   * UserPhone(); userPhone.setPhoneType( mockHomePhoneType ); userPhone.setPhoneNbr( "1234567894"
   * ); userPhone.setIsPrimary( Boolean.valueOf( true )); fullParticipant.getUserPhones().add(
   * userPhone ); // Note: since saveParticipant is called 2 times (once for saving participant
   * only, once for all children), // using the "onConsecutiveCalls" mockParticipantDAO.expects(
   * once() ).method( "saveParticipant" ) .with( same( strippedParticipant ) ) .will( returnValue(
   * strippedParticipant ) ); mockParticipantDAO.expects( once() ).method( "saveParticipant" )
   * .with( same( fullParticipant ) ) .will( returnValue( fullParticipant ) );
   * //mockParticipantDAO.expects( atLeastOnce() ).method( "saveParticipant" ) //.with( same(
   * fullParticipant ) ) //.will( onConsecutiveCalls( // returnValue( strippedParticipant ), //
   * returnValue( fullParticipant ) // )); Participant actualParticipant =
   * participantServiceImpl.createFullParticipant(fullParticipant); assertEquals( "Actual
   * Participant wasn't equals to what was expected", fullParticipant, actualParticipant );
   * //assertEquals( "Phone Number not inserted", 1, actualParticipant.getUserPhones().size());
   * //assertEquals( "Address not inserted", 1, actualParticipant.getUserAddresses().size());
   * //assertEquals( "EmailAddress not inserted", 1,
   * actualParticipant.getUserEmailAddresses().size()); }
   */
  /**
   * Test getting the participant by the ID.
   */
  public void testGetParticipantById()
  {

    Participant expectedParticipant = buildStaticParticipant();

    mockParticipantDAO.expects( once() ).method( "getParticipantById" ).with( same( expectedParticipant.getId() ) ).will( returnValue( expectedParticipant ) );

    Participant actualParticipant = this.participantServiceImpl.getParticipantById( expectedParticipant.getId() );

    assertEquals( "Actual Participant wasn't equals to what was expected", expectedParticipant, actualParticipant );

  }

  /**
   * Test searching for the participant with the given search criteria.
   */
  public void testSearchParticipant()
  {

    Participant expectedParticipant = buildStaticParticipant();
    List<Participant> expectedParticipantList = new ArrayList<Participant>();
    expectedParticipantList.add( expectedParticipant );

    ParticipantSearchCriteria searchCriteria = new ParticipantSearchCriteria();
    searchCriteria.setUserName( expectedParticipant.getUserName() );

    mockParticipantDAO.expects( once() ).method( "searchParticipant" ).with( same( searchCriteria ) ).will( returnValue( expectedParticipantList ) );

    List actualParticipantList = this.participantServiceImpl.searchParticipant( searchCriteria );

    assertTrue( "Actual Participant list from search doesn't contain the expected participant", actualParticipantList.contains( expectedParticipant ) );

  }

  /**
   * Test the getCurrentParticipantEmployer service method.
   */
  public void testGetCurrentParticipantEmployer()
  {
    Participant expectedParticipant = buildStaticParticipant();
    ParticipantEmployer expectedParticipantEmployer = (ParticipantEmployer)expectedParticipant.getParticipantEmployers().get( 0 );

    mockParticipantDAO.expects( once() ).method( "getParticipantById" ).with( same( expectedParticipant.getId() ) ).will( returnValue( expectedParticipant ) );

    ParticipantEmployer actualParticipantEmployer = this.participantServiceImpl.getCurrentParticipantEmployer( expectedParticipant.getId() );

    assertEquals( "Actual ParticipantEmployer wasn't equals to what was expected", expectedParticipantEmployer, actualParticipantEmployer );
  }

  /**
   * Test the saveParticipantEmployer service method
   */
  public void testSaveParticipantEmployer()
  {
    Participant expectedParticipant = buildStaticParticipant();
    expectedParticipant.setAwardBanqNumber( null );
    ParticipantEmployer expectedParticipantEmployer = (ParticipantEmployer)expectedParticipant.getParticipantEmployers().get( 0 );

    Employer expectedEmployer = buildStaticEmployer();
    mockParticipantDAO.expects( atLeastOnce() ).method( "getParticipantById" ).with( same( expectedParticipant.getId() ) ).will( returnValue( expectedParticipant ) );
    mockParticipantDAO.expects( once() ).method( "saveParticipant" ).with( same( expectedParticipant ) ).will( returnValue( expectedParticipant ) );
    mockEmployerDAO.expects( once() ).method( "getEmployerById" ).with( eq( expectedEmployer.getId() ) ).will( returnValue( expectedEmployer ) );

    mockUserService.expects( atLeastOnce() ).method( "updateUserNodeStatus" );

    PropertySetItem ret = new PropertySetItem();
    ret.setBooleanVal( true );
    mockSystemVariableService.expects( once() ).method( "getPropertyByName" ).with( eq( SystemVariableService.TERMS_CONDITIONS_USED ) ).will( returnValue( ret ) );

    Participant actualParticipant = this.participantServiceImpl.saveParticipantEmployer( expectedParticipant.getId(), expectedParticipantEmployer );

    assertEquals( "Actual ParticipantEmployer wasn't equals to what was expected", expectedParticipant, actualParticipant );
    assertEquals( "Expect that the list of employers does not change", expectedParticipant.getParticipantEmployers(), actualParticipant.getParticipantEmployers() );
  }

  /**
   * Test the saveParticipantEmployer service method that also sets the termination date of the
   * previous ParticipantEmployer record.
   */
  public void testSaveParticipantEmployerWithPerviousTerminationDate()
  {
    Participant expectedParticipant = buildStaticParticipant();
    expectedParticipant.setAwardBanqNumber( null );
    ParticipantEmployer expectedParticipantEmployer = buildStaticParticipantEmployer();

    Employer expectedEmployer = buildStaticEmployer();
    mockParticipantDAO.expects( atLeastOnce() ).method( "getParticipantById" ).with( same( expectedParticipant.getId() ) ).will( returnValue( expectedParticipant ) );
    mockParticipantDAO.expects( atLeastOnce() ).method( "saveParticipant" ).with( eq( expectedParticipant ) ).will( returnValue( expectedParticipant ) );
    mockEmployerDAO.expects( once() ).method( "getEmployerById" ).with( eq( expectedEmployer.getId() ) ).will( returnValue( expectedEmployer ) );

    mockUserService.expects( atLeastOnce() ).method( "updateUserNodeStatus" );

    PropertySetItem ret = new PropertySetItem();
    ret.setBooleanVal( true );
    mockSystemVariableService.expects( atLeastOnce() ).method( "getPropertyByName" ).with( eq( SystemVariableService.TERMS_CONDITIONS_USED ) ).will( returnValue( ret ) );

    Participant actualParticipant = this.participantServiceImpl.saveParticipantEmployer( expectedParticipant.getId(), expectedParticipantEmployer, new Date() );

    Participant expectedParticipant2 = buildStaticParticipant();

    assertEquals( "Actual ParticipantEmployer wasn't equals to what was expected", expectedParticipant2, actualParticipant );
    assertEquals( "Expect that the list of employers has one more", expectedParticipant2.getParticipantEmployers().size() + 1, actualParticipant.getParticipantEmployers().size() );
  }

  /**
   * Test the updating of the participants Communication Preferences
   */
  public void testUpdateParticipantPreferences()
  {

    Participant expectedParticipant = buildStaticParticipant();

    ParticipantFollowers paxFollower = buildParticipantFollowers( expectedParticipant );

    mockParticipantDAO.expects( once() ).method( "getParticipantById" ).with( same( expectedParticipant.getId() ) ).will( returnValue( expectedParticipant ) );

    mockParticipantDAO.expects( once() ).method( "getParticipantsWhoAreFollowingMe" ).with( same( paxFollower.getParticipant().getId() ) ).will( returnValue( new ArrayList<ParticipantFollowers>() ) );

    final Mock mockHibernateSession = new Mock( Session.class );
    Object previousHibernateSession = ResourceManager.getResource( "HibernateSession" );
    ResourceManager.setResource( "HibernateSession", mockHibernateSession.proxy() );
    mockHibernateSession.expects( atLeastOnce() ).method( "flush" );

    Set<ParticipantCommunicationPreference> newPromotionCommunicationPreferences = new LinkedHashSet<ParticipantCommunicationPreference>();

    // build some new participantCommunicationPreferences and replace the existing ones.
    ParticipantCommunicationPreference newPcp1 = new ParticipantCommunicationPreference();
    newPcp1.setParticipant( expectedParticipant );
    newPcp1.setParticipantPreferenceCommunicationsType( ParticipantPreferenceCommunicationsType.lookup( ParticipantPreferenceCommunicationsType.E_STATEMENTS ) );

    ParticipantCommunicationPreference newPcp2 = new ParticipantCommunicationPreference();
    newPcp2.setParticipant( expectedParticipant );
    newPcp2.setParticipantPreferenceCommunicationsType( ParticipantPreferenceCommunicationsType.lookup( ParticipantPreferenceCommunicationsType.TEXT_MESSAGES ) );
    newPcp2.setMessageSMSGroupType( MessageSMSGroupType.lookup( "432" ) );

    ParticipantCommunicationPreference newPcp3 = new ParticipantCommunicationPreference();
    newPcp3.setParticipant( expectedParticipant );
    newPcp3.setParticipantPreferenceCommunicationsType( ParticipantPreferenceCommunicationsType.lookup( ParticipantPreferenceCommunicationsType.TEXT_MESSAGES ) );
    newPcp3.setMessageSMSGroupType( MessageSMSGroupType.lookup( "577" ) );

    newPromotionCommunicationPreferences.add( newPcp1 );
    newPromotionCommunicationPreferences.add( newPcp2 );
    newPromotionCommunicationPreferences.add( newPcp3 );

    // Create a set of contact methods
    Set<ParticipantContactMethod> participantContactMethods = new LinkedHashSet<ParticipantContactMethod>();

    ParticipantContactMethod paxContactMethod1 = new ParticipantContactMethod();
    paxContactMethod1.setParticipant( expectedParticipant );
    paxContactMethod1.setContactMethodCode( ContactMethod.lookup( "email" ) );
    paxContactMethod1.setAuditCreateInfo( expectedParticipant.getAuditCreateInfo() );
    paxContactMethod1.setPrimary( Boolean.valueOf( false ) );
    participantContactMethods.add( paxContactMethod1 );

    ParticipantContactMethod paxContactMethod2 = new ParticipantContactMethod();
    paxContactMethod2.setParticipant( expectedParticipant );
    paxContactMethod2.setContactMethodCode( ContactMethod.lookup( "fax" ) );
    paxContactMethod2.setAuditCreateInfo( expectedParticipant.getAuditCreateInfo() );
    paxContactMethod2.setPrimary( Boolean.valueOf( false ) );
    participantContactMethods.add( paxContactMethod2 );

    // Set email address
    UserEmailAddress emailAddress = new UserEmailAddress();
    emailAddress.setEmailAddr( "foo@bar.com" );
    emailAddress.setEmailType( EmailAddressType.lookup( EmailAddressType.HOME ) );
    emailAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    emailAddress.setIsPrimary( new Boolean( true ) );
    expectedParticipant.addUserEmailAddress( emailAddress );

    // Set text address
    UserEmailAddress textAddress = new UserEmailAddress();
    textAddress.setEmailAddr( "text@bar.com" );
    textAddress.setEmailType( EmailAddressType.lookup( EmailAddressType.SMS ) );
    textAddress.setVerificationStatus( VerificationStatusType.lookup( VerificationStatusType.UNVERIFIED ) );
    expectedParticipant.addUserEmailAddress( textAddress );

    // Set language
    expectedParticipant.setLanguageType( LanguageType.lookup( "en" ) );

    String[] contactMethodTypes = { ParticipantPreferenceCommunicationsType.E_STATEMENTS };

    String[] textMessages = { "432", "577" };

    String[] contactMethods = { "email", "fax" };
    boolean allowpublicrecognition = false;
    boolean allowPublicInformation = false;
    boolean allowPublicBirthDate = false;
    boolean allowPublicHireDate = false;
    // Client customizations for wip #26532 starts
    boolean allowSharePurlToOutsiders = false;
    boolean allowPurlContributionsToSeeOthers = false;
    this.participantServiceImpl.updateParticipantPreferences( expectedParticipant.getId(),
                                                              contactMethodTypes,
                                                              textMessages,
                                                              contactMethods,
                                                              "en",
                                                              emailAddress,
                                                              textAddress,
                                                              allowpublicrecognition,
                                                              allowPublicInformation,
                                                              allowPublicBirthDate,
                                                              allowPublicHireDate,
                                                              allowSharePurlToOutsiders,
                                                              allowPurlContributionsToSeeOthers );
    // Client customizations for wip #26532 ends
     
    System.out.println( "Expected: " + newPromotionCommunicationPreferences );
    System.out.println( "Actual: + " + expectedParticipant.getParticipantCommunicationPreferences() );

    assertEquals( "Actual Participant Communication Preferences were not equal", expectedParticipant.getParticipantCommunicationPreferences(), newPromotionCommunicationPreferences );

    assertEquals( "Actual Participant Contact Methods were not equal", expectedParticipant.getParticipantContactMethods(), participantContactMethods );

    assertEquals( "Actual Participant Email Address's were not equal", expectedParticipant.getPrimaryEmailAddress(), emailAddress );

    assertEquals( "Actual Participant Text Address's were not equal", expectedParticipant.getTextMessageAddress(), textAddress );

    // Now test removal of preferences
    mockParticipantDAO.expects( once() ).method( "getParticipantById" ).with( same( expectedParticipant.getId() ) ).will( returnValue( expectedParticipant ) );
    mockParticipantDAO.expects( once() ).method( "getParticipantsWhoAreFollowingMe" ).with( same( paxFollower.getParticipant().getId() ) ).will( returnValue( new ArrayList<ParticipantFollowers>() ) );

    newPromotionCommunicationPreferences.remove( newPcp1 );
    newPromotionCommunicationPreferences.remove( newPcp3 );

    participantContactMethods.remove( paxContactMethod2 );

    String[] newContactMethodTypes = null;

    String[] newTextMessages = { "432" };

    String[] newContactMethods = { "email" };

    UserEmailAddress newTextAddress = null;

    // Client customizations for wip #26532 starts
    this.participantServiceImpl.updateParticipantPreferences( expectedParticipant.getId(),
                                                              newContactMethodTypes,
                                                              newTextMessages,
                                                              newContactMethods,
                                                              "en",
                                                              emailAddress,
                                                              newTextAddress,
                                                              allowpublicrecognition,
                                                              allowPublicInformation,
                                                              allowPublicBirthDate,
                                                              allowPublicHireDate,
                                                              allowSharePurlToOutsiders,
                                                              allowPurlContributionsToSeeOthers );
    // Client customizations for wip #26532 ends
    ResourceManager.setResource( "HibernateSession", previousHibernateSession );

    assertEquals( "Actual Participant Communication Preferences were not equal (after remove)", expectedParticipant.getParticipantCommunicationPreferences(), newPromotionCommunicationPreferences );

    assertEquals( "Actual Participant Contact Methods were not equal (after remove)", expectedParticipant.getParticipantContactMethods(), participantContactMethods );

    assertEquals( "Actual Participant Email Address's were not equal (after remove)", expectedParticipant.getPrimaryEmailAddress(), emailAddress );

    assertNull( "Actual Participant Text Address was not null (after remove)", expectedParticipant.getTextMessageAddress() );
  }

  public void testGetGToHoneycombSyncData()
  {
    List<Long> userIds = Arrays.asList( 60028L, 60029L );
    Map<String, Object> procedureResults = new HashMap<String, Object>();
    Map<String, Object> firstAppUserAttributeMap = buildAttributeMap( 123L, "user123" );
    procedureResults.put( "p_out_return_code", 0 );
    procedureResults.put( "p_out_application_user", Arrays.asList( firstAppUserAttributeMap, buildAttributeMap( 456L, "user456" ) ) );
    procedureResults.put( "p_out_participant", Arrays.asList( buildAttributeMap( 123L, "user123" ), buildAttributeMap( 456L, "user456" ) ) );
    mockParticipantDAO.expects( once() ).method( "getGToHoneycombSyncPaxData" ).with( same( userIds ) ).will( returnValue( procedureResults ) );

    AccountSyncRequest actualResult = participantServiceImpl.getGToHoneycombSyncPaxData( userIds );

    assertTrue( "Number of results did not match. Not all participants represented", actualResult.getParticipantDetails().size() == 2 );
    AccountSyncParticipantDetails firstParticipantDetails = actualResult.getParticipantDetails().stream()
        .filter( ( detail ) -> detail.getUserName().equals( firstAppUserAttributeMap.get( "user_name" ) ) ).findFirst().get();
    assertTrue( "Username did not match", firstAppUserAttributeMap.get( "user_name" ).equals( firstParticipantDetails.getUserName() ) );
    assertTrue( "Username did not match", firstParticipantDetails.getAttributeMaps().get( "p_out_application_user" ).get( "user_name" ).equals( firstParticipantDetails.getUserName() ) );
    assertTrue( "favorite_number field did not match",
                firstAppUserAttributeMap.get( "favorite_number" ).equals( firstParticipantDetails.getAttributeMaps().get( "p_out_application_user" ).get( "favorite_number" ) ) );
    assertTrue( "participant result set did not have right number of values", firstParticipantDetails.getAttributeMaps().get( "p_out_participant" ).size() == 5 );
  }

  public static Map<String, Object> buildAttributeMap( Long userId, String username )
  {
    Random random = new Random();
    Map<String, Object> attributeMap = new HashMap<>();
    attributeMap.put( "user_id", new BigDecimal( userId ) );
    attributeMap.put( "user_name", username );
    attributeMap.put( "first_name", "bob" + random.nextInt( 100 ) );
    attributeMap.put( "last_name", "jones" + random.nextInt( 100 ) );
    attributeMap.put( "favorite_number", random.nextInt( 10000 ) );
    return attributeMap;
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

    // from User
    participant.setFirstName( firstname );
    participant.setLastName( lastname );
    participant.setUserName( username );

    participant.addParticipantEmployer( buildStaticParticipantEmployer() );

    return participant;

  }

  /**
   * Build a static participant for testing.
   * 
   * @return Participant
   */
  public static Participant buildStaticParticipant()
  {

    Participant participant = buildStaticParticipant( "testUSERNAME", "testFIRSTNAME", "testLASTNAME", "testEMAILADDR" );
    completeParticipant( participant );

    return participant;
  }

  public UserTNCHistory addPaxTNCHistory( Participant pax )
  {
    UserTNCHistory userTNCHistory = new UserTNCHistory();
    userTNCHistory.setUser( pax );

    userTNCHistory.setTncAction( ParticipantTermsAcceptance.lookup( ParticipantTermsAcceptance.ACCEPTED ).getCode() );

    userTNCHistory.setHistoryCreatedBy( new Long( 5662 ) );

    userTNCHistory.setHistoryDateCreated( new Timestamp( System.currentTimeMillis() ) );

    return userTNCHistory;
  }

  public Participant buildStaticParticipantWithTNCHistory()
  {

    Participant participant = buildStaticParticipant();
    addPaxTNCHistory( participant );
    return participant;
  }

  public static ParticipantFollowers buildParticipantFollowers( Participant participant )
  {
    Participant follower = buildStaticParticipant();
    ParticipantFollowers participantFollowers = new ParticipantFollowers();
    participantFollowers.setId( new Long( 100 ) );
    participantFollowers.setParticipant( participant );
    participantFollowers.setFollower( follower );
    return participantFollowers;
  }

  /**
   * Build a static ParticipantEmployer for testing.
   * 
   * @return ParticipantEmployer
   */
  private static ParticipantEmployer buildStaticParticipantEmployer()
  {
    ParticipantEmployer paxEmpl = new ParticipantEmployer();
    paxEmpl.setEmployer( buildStaticEmployer() );
    paxEmpl.setHireDate( Calendar.getInstance().getTime() );
    return paxEmpl;
  }

  /**
   * Build a static Employer for testing
   * 
   * @return Employer
   */
  private static Employer buildStaticEmployer()
  {
    Employer employer = new Employer();
    employer.setId( new Long( 1 ) );
    employer.setVersion( new Long( 1 ) );
    employer.setName( "ServiceTestNAME" );
    return employer;
  }

  /**
   * Attaches the remaining bits onto the participant. This is seperate to avoid needing to repeat
   * it each time when building a Participant.
   * 
   * @param participant
   */
  private static void completeParticipant( Participant participant )
  {

    participant.setId( new Long( System.currentTimeMillis() % 229302 ) );
    participant.setPassword( "testPASSWORD" );

    participant.setActive( Boolean.TRUE );
    participant.setMasterUserId( new Long( 1 ) );
    participant.setLoginFailuresCount( new Integer( 0 ) );
    participant.setLastResetDate( new Date() );

    // from Participant
    participant.setAwardBanqExtractDate( new Date() );
    participant.setAwardBanqNumber( "testABN" );
    participant.setCentraxId( "testCXID" );
    participant.setEnrollmentSource( mockParticipantEnrollmentSource );
    participant.setRelationshipType( mockParticipantRelationship );
    participant.setSuspensionStatus( mockParticipantSuspensionStatus );
    participant.setStatus( mockParticipantStatus );
    participant.setStatusChangeDate( new Date() );
    // participant.setUserNodes( userNodes );

    // Add Communication Preferences
    ParticipantCommunicationPreference pcp1 = new ParticipantCommunicationPreference();
    pcp1.setParticipantPreferenceCommunicationsType( mockParticipantPreferenceCommunicationsTypeTextMessage );
    pcp1.setMessageSMSGroupType( MessageSMSGroupType.lookup( "goalReminders" ) );

    ParticipantCommunicationPreference pcp2 = new ParticipantCommunicationPreference();
    pcp2.setParticipantPreferenceCommunicationsType( mockParticipantPreferenceCommunicationsTypeTextMessage );
    pcp2.setMessageSMSGroupType( MessageSMSGroupType.lookup( "goalReminders" ) );

    ParticipantCommunicationPreference pcp3 = new ParticipantCommunicationPreference();
    pcp3.setParticipantPreferenceCommunicationsType( mockParticipantPreferenceCommunicationsTypeEStatement );

    participant.addParticipantCommunicationPreference( pcp1 );
    participant.addParticipantCommunicationPreference( pcp2 );
    participant.addParticipantCommunicationPreference( pcp3 );

    // Add Contact Methods
    participant.addContactMethod( mockContactMethod, new Boolean( true ) );

  }

  public void testGetHeroModuleAudienceTypeByUserId()
  {
    mockParticipantDAO.expects( once() ).method( "getHeroModuleAppAudienceTypeByUserId" );
    participantServiceImpl.getHeroModuleAudienceTypeByUserId( 5588L );
    mockParticipantDAO.verify();

  }

  @Test
  public void getAllOwnerAndManagerForRA()
  {
    // return participantDAO.getAllOwnerAndManagerForRA();
    String uniqueName = String.valueOf( System.currentTimeMillis() % 29930291 );

    NodeType nodeType1 = new NodeType();
    nodeType1.setName( "testNodeType" + uniqueName );
    nodeType1.setCmAssetCode( "test.asset" );
    nodeType1.setNameCmKey( "testkey" );

    Hierarchy hierarchy1 = new Hierarchy();
    hierarchy1.setName( "Test NodeHierarchy2" + uniqueName );
    hierarchy1.setDescription( "description goes here" );
    hierarchy1.setPrimary( false );
    hierarchy1.setActive( true );
    hierarchy1.setCmAssetCode( "CM name ASSET" );
    hierarchy1.setNameCmKey( "CM name KEY" );

    Node parentNode = new Node();
    parentNode.setName( "testParentNAME" + uniqueName );
    parentNode.setDescription( "testParentDESCRIPTION" );
    parentNode.setParentNode( null );
    parentNode.setPath( "testParentPATH" );
    parentNode.setNodeType( nodeType1 );
    parentNode.setHierarchy( hierarchy1 );

    Node node1 = new Node();
    node1.setName( "Test NodeHierarchy1" + uniqueName );
    node1.setDescription( "description goes here" );
    node1.setParentNode( parentNode );
    node1.setNodeType( nodeType1 );
    node1.setHierarchy( hierarchy1 );
    node1.setPath( parentNode.getPath() + node1.getName() );

    User loggedInUser = new User();
    UserNode userNode = new UserNode();
    userNode.setNode( node1 );
    userNode.setIsPrimary( true );
    userNode.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.OWNER ) );

    UserNode userNode1 = new UserNode();
    userNode1.setNode( node1 );
    userNode1.setIsPrimary( true );
    userNode.setHierarchyRoleType( HierarchyRoleType.lookup( HierarchyRoleType.MANAGER ) );

    loggedInUser.addUserNode( userNode );
    loggedInUser.addUserNode( userNode1 );

    /*
     * List<UserNode> userNodes = new ArrayList<UserNode>(); userNodes.add( userNode1 );
     * userNodes.add( userNode );
     */

    mockParticipantDAO.expects( once() ).method( "getAllOwnerAndManagerForRA" ).will( returnValue( loggedInUser.getUserNodes() ) );

    List<UserNode> listOfUserNodes = this.participantServiceImpl.getAllManagerAndOwner();
    assertNotNull( listOfUserNodes );

  }

}
