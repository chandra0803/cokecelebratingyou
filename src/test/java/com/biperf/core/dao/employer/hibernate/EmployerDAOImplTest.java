/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/employer/hibernate/EmployerDAOImplTest.java,v $
 */

package com.biperf.core.dao.employer.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.biperf.core.dao.country.CountryDAO;
import com.biperf.core.dao.employer.EmployerDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.domain.Address;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.employer.Employer;
import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.ParticipantEnrollmentSource;
import com.biperf.core.domain.enums.ParticipantRelationshipType;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.ParticipantSuspensionStatus;
import com.biperf.core.domain.enums.PositionType;
import com.biperf.core.domain.enums.StateType;
import com.biperf.core.domain.enums.UserType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantEmployer;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * EmployerDAOImplTest.
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
 * <td>Apr 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class EmployerDAOImplTest extends BaseDAOTest
{
  /**
   * Returns the country DAO.
   * 
   * @return the country DAO.
   */
  private static CountryDAO getCountryDao()
  {
    return (CountryDAO)ApplicationContextFactory.getApplicationContext().getBean( CountryDAO.BEAN_NAME );
  }

  /**
   * Uses the ApplicationContextFactory to look up the correct DAO implementation.
   * 
   * @return ParticipantDAO
   */
  protected ParticipantDAO getParticipantDAO()
  {
    return (ParticipantDAO)ApplicationContextFactory.getApplicationContext().getBean( "participantDAO" );
  }

  /**
   * Uses the ApplicationContextFactory to look up the correct DAO implementation.
   * 
   * @return EmployerDAO
   */
  private static EmployerDAO getEmployerDAO()
  {
    return (EmployerDAO)ApplicationContextFactory.getApplicationContext().getBean( "employerDAO" );
  }

  /**
   * Test Saving and getting the employer by the Id. This will also save the updated employer and
   * ensure updating is working.
   */
  public void testSaveEmployerAndGetEmployerById()
  {

    EmployerDAO employerDAO = getEmployerDAO();

    Employer expectedEmployer = new Employer();
    expectedEmployer.setName( "testEMPLOYER" );

    expectedEmployer.setAddress( buildTestAddress() );
    employerDAO.saveEmployer( expectedEmployer );

    assertEquals( "Actual employer wasn't what was expected", expectedEmployer, employerDAO.getEmployerById( expectedEmployer.getId() ) );

    expectedEmployer.setName( "testEMPLOYER-UPDATED" );
    employerDAO.saveEmployer( expectedEmployer );

    Employer actualEmployer = employerDAO.getEmployerById( expectedEmployer.getId() );
    assertEquals( "Actual Updated employer wasn't what was expected", expectedEmployer, actualEmployer );

    assertTrue( "Updated employer Name wasn't equal to the actual name", expectedEmployer.getName().equals( actualEmployer.getName() ) );

  }

  /**
   * Builds a test address;
   * 
   * @return Address
   */
  private Address buildTestAddress()
  {
    Address address = new Address();

    address.setAddr1( "3324 Main Street" );
    address.setAddr2( "Floor 33" );
    address.setAddr3( "Office 9" );
    address.setCity( "Anytown" );
    address.setCountry( getCountryDao().getCountryByCode( Country.UNITED_STATES ) );
    address.setPostalCode( "12901" );

    return address;
  }

  /**
   * Test getting all employers in the database.
   */
  public void testGetAll()
  {

    EmployerDAO employerDAO = getEmployerDAO();

    Set expectedList = new LinkedHashSet();

    Employer employer1 = new Employer();
    employer1.setName( "testGETALLNAME" );
    employerDAO.saveEmployer( employer1 );

    expectedList.add( employer1 );

    Employer employer2 = new Employer();
    employer2.setName( "testGETALLNAME2" );
    employerDAO.saveEmployer( employer2 );

    expectedList.add( employer2 );

    Employer employer3 = new Employer();
    employer3.setName( "testGETALLNAME3" );
    employerDAO.saveEmployer( employer3 );

    expectedList.add( employer3 );

    List actualList = employerDAO.getAll();

    assertTrue( "Actual set of employers after getting all from the database doesn't contain the exected set", actualList.containsAll( expectedList ) );

  }

  /**
   * Test getting the employer through the search.
   */
  public void testSearchEmployer()
  {

    List expectedList = new ArrayList();

    EmployerDAO employerDAO = getEmployerDAO();

    Employer employer1 = new Employer();
    employer1.setName( "test-ABC" );
    employerDAO.saveEmployer( employer1 );

    Employer employer2 = new Employer();
    employer2.setName( "test-XYZ" );
    employerDAO.saveEmployer( employer2 );

    Employer employer3 = new Employer();
    employer3.setName( "test-XAG" );
    employerDAO.saveEmployer( employer3 );

    expectedList.add( employer1 );
    expectedList.add( employer2 );
    expectedList.add( employer3 );

    assertTrue( "Actual list through search didn't contain the expectedList", employerDAO.searchEmployer( "test" ).containsAll( expectedList ) );

    expectedList.clear();

    expectedList.add( employer1 );
    expectedList.add( employer3 );

    assertTrue( "Actual list through search didn't contain the expectedList", employerDAO.searchEmployer( "A" ).containsAll( expectedList ) );

    expectedList.clear();

    expectedList.add( employer2 );
    expectedList.add( employer3 );

    assertTrue( "Actual list through search didn't contain the expectedList", employerDAO.searchEmployer( "x" ).containsAll( expectedList ) );

  }

  /**
   * Test the active participant count query
   */
  public void testActiveParticipantCount()
  {
    EmployerDAO employerDAO = getEmployerDAO();

    Employer employer1 = buildAndSaveEmployer( "test-ABC" );

    Participant pax = buildStaticParticipant( "testUSERNAME", "testFIRSTNAME", "testLASTNAME", "testEMAILADDR" );

    ParticipantDAO paxDAO = getParticipantDAO();
    paxDAO.saveParticipant( pax );

    pax.addParticipantEmployer( buildParticipantEmployer( employer1 ) );

    flushAndClearSession();

    Employer employer = employerDAO.getEmployerById( employer1.getId() );
    assertEquals( "Expected one active participant - there was not.", 1, employer.getActiveParticipantCount() );
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
  private Participant buildStaticParticipant( String username, String firstname, String lastname, String emailAddr )
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

  /**
   * Build a static participant for testing.
   * 
   * @return Participant
   */
  public Participant buildStaticParticipant()
  {
    return buildStaticParticipant( "testUSERNAME", "testFIRSTNAME", "testLASTNAME", "testEMAILADDR" );
  }

  /**
   * Attaches the remaining bits onto the participant. This is seperate to avoid needing to repeat
   * it each time when building a Participant.
   * 
   * @param participant
   */
  private void completeParticipant( Participant participant )
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
    participant.setEnrollmentSource( ParticipantEnrollmentSource.getDefaultItem() );
    participant.setRelationshipType( ParticipantRelationshipType.getDefaultItem() );
    participant.setSuspensionStatus( ParticipantSuspensionStatus.getDefaultItem() );
    participant.setStatus( ParticipantStatus.lookup( "active" ) );
    participant.setStatusChangeDate( new Date() );

  }

  public static Employer buildAndSaveEmployer( String name )
  {

    Employer employer = buildEmployer( name, getCountryDao().getCountryByCode( Country.UNITED_STATES ) );

    getEmployerDAO().saveEmployer( employer );

    return employer;
  }

  /**
   * Builds an employer which is attached to the Participant for testing.
   * 
   * @param name
   * @param country
   * @return Employer
   */
  public static Employer buildEmployer( String name, Country country )
  {

    Employer employer = new Employer();
    employer.setName( name );

    Address address = new Address();
    address.setAddr1( "testADDR1" );
    address.setAddr2( "testADDR2" );
    address.setAddr3( "testADDR3" );
    address.setCity( "testCITY" );
    List stateList = StateType.getList( Country.UNITED_STATES );
    if ( stateList.size() > 0 )
    {
      address.setStateType( (StateType)stateList.get( 0 ) );
    }

    address.setCountry( country );
    address.setPostalCode( "testPOSTALCODE" );
    employer.setAddress( address );

    return employer;

  }

  /**
   * Method to manage building a participantEmployer.
   * 
   * @param employer
   * @return ParticipantEmployer
   */
  public static ParticipantEmployer buildParticipantEmployer( Employer employer )
  {
    ParticipantEmployer participantEmployer = new ParticipantEmployer();
    participantEmployer.setEmployer( employer );
    participantEmployer.setDepartmentType( DepartmentType.lookup( DepartmentType.MARKETING ).getName() );
    participantEmployer.setPositionType( PositionType.lookup( PositionType.MANAGER ).getName() );
    participantEmployer.setHireDate( new Date() );
    return participantEmployer;
  }

}
