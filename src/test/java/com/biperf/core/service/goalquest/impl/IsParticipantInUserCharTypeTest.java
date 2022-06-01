
package com.biperf.core.service.goalquest.impl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Test;

import com.biperf.core.dao.system.CharacteristicDAO;
import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.characteristic.UserCharacteristicType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.utils.UserManager;

public class IsParticipantInUserCharTypeTest extends BaseGQTest
{
  Participant participant;
  GoalQuestPromotion promotion;
  String chars;
  List<Characteristic> characteristics;
  Characteristic characteristicToCheck;
  AuthenticatedUser originalUser, activeUser;
  Long userId;
  CharacteristicDAO mockCharacteristicDAO;

  User user = new User();
  UserCharacteristicType char1 = new UserCharacteristicType();
  UserCharacteristicType char2 = new UserCharacteristicType();
  UserCharacteristic userChar1 = new UserCharacteristic();
  UserCharacteristic userChar2 = new UserCharacteristic();
  Set<UserCharacteristic> userChars = new LinkedHashSet<UserCharacteristic>();
  List<UserCharacteristic> userCharsList = new ArrayList<UserCharacteristic>();

  @Override
  protected void setUp() throws Exception
  {
    super.setUp();
    participant = new Participant();
    promotion = new GoalQuestPromotion();
    chars = "1";
    promotion.setPreSelectedPartnerChars( chars );
    characteristicToCheck = null;
    activeUser = new AuthenticatedUser();
    userId = 5L;
    activeUser.setUserId( userId );

    mockCharacteristicDAO = mockControl.createMock( CharacteristicDAO.class );
    testInstance.setCharacteristicDAO( mockCharacteristicDAO );
    originalUser = UserManager.getUser();
    UserManager.setUser( activeUser );

    // Default initializations below
    user = new User();
    char1 = new UserCharacteristicType();
    char2 = new UserCharacteristicType();
    userChar1 = new UserCharacteristic();
    userChar2 = new UserCharacteristic();
    // Initialized in each test case
    userChars = new LinkedHashSet<UserCharacteristic>();
    userCharsList = new ArrayList<UserCharacteristic>();
    characteristics = new ArrayList<Characteristic>();

    // Default initializations, so we don't have to re-initialize for each test.
    user.setId( userId );
    char1.setId( userId );
    char1.setCharacteristicName( "Batman" );
    userChar1.setId( userId );
    userChar1.setUserCharacteristicType( char1 );
    userChar1.setCharacteristicValue( "nananananananana" );
    userChar1.setUser( user );

    char2.setId( 2L );
    char2.setCharacteristicName( "Spiderman" );
    userChar2.setId( 2L );
    userChar2.setUserCharacteristicType( char2 );
    userChar2.setCharacteristicValue( "Friendy neighborhood" );

  }

  @Override
  protected void tearDown() throws Exception
  {
    super.tearDown();
    UserManager.setUser( originalUser );
  }

  /**
   * <b>Goal:</b>
   * <br/><br/>
   * <b>Setup:</b>
   * <br/><br/>
   * <b>Expected Behavior:</b>
   */
  @Test
  public void testValid()
  {
    userChars.add( userChar1 );
    userChars.add( userChar2 );
    userCharsList.add( userChar1 );
    userCharsList.add( userChar2 );
    characteristics.add( char1 );
    characteristics.add( char2 );

    characteristicToCheck = char1;
    participant.setId( 1L );
    participant.setUserCharacteristics( userChars );

    EasyMock.expect( mockCharacteristicDAO.getAllCharacteristics() ).andReturn( characteristics );
    EasyMock.expect( mockCharacteristicDAO.getCharacteristicById( 1L ) ).andReturn( characteristicToCheck );
    EasyMock.expect( mockParticipantService.getParticipantByIdWithAssociations( EasyMock.eq( participant.getId() ), (AssociationRequestCollection)EasyMock.anyObject() ) ).andReturn( participant )
        .once();
    EasyMock.expect( mockParticipantService.getAllForCharIDAndValue( char1.getId(), userChar1.getCharacteristicValue() ) ).andReturn( userCharsList );

    mockControl.replay();
    Boolean result = testInstance.isParticipantInUserCharType( participant, promotion );
    mockControl.verify();
    assertTrue( result );
  }

  /**
   * <b>Goal:</b>
   * <br/><br/>
   * <b>Setup:</b>
   * <br/><br/>
   * <b>Expected Behavior:</b>
   */
  @Test
  public void testCharacteristicsListNull()
  {
    // This sets characteristics = null
    EasyMock.expect( mockCharacteristicDAO.getAllCharacteristics() ).andReturn( null );
    // We won't use the value of charac, so just nulling it.
    EasyMock.expect( mockCharacteristicDAO.getCharacteristicById( 1L ) ).andReturn( null );
    mockControl.replay();
    Boolean result = testInstance.isParticipantInUserCharType( participant, promotion );
    mockControl.verify();
    assertFalse( result );
  }

  /**
   * <b>Goal:</b>
   * <br/><br/>
   * <b>Setup:</b>
   * <br/><br/>
   * <b>Expected Behavior:</b>
   */
  @Test
  public void testNameMismatch()
  {
    userChars.add( userChar1 );
    userCharsList.add( userChar1 );
    characteristics.add( char1 );

    characteristicToCheck = char2;
    participant.setId( 1L );
    participant.setUserCharacteristics( userChars );

    EasyMock.expect( mockCharacteristicDAO.getAllCharacteristics() ).andReturn( characteristics );
    EasyMock.expect( mockCharacteristicDAO.getCharacteristicById( 1L ) ).andReturn( characteristicToCheck );

    mockControl.replay();
    Boolean result = testInstance.isParticipantInUserCharType( participant, promotion );
    mockControl.verify();
    assertFalse( result );
  }

  /**
   * <b>Goal:</b>
   * <br/><br/>
   * <b>Setup:</b>
   * <br/><br/>
   * <b>Expected Behavior:</b>
   */
  @Test
  public void testPaxCharValueNull()
  {
    userChars.add( userChar1 );
    userChars.add( userChar2 );
    userCharsList.add( userChar1 );
    userCharsList.add( userChar2 );
    characteristics.add( char1 );
    characteristics.add( char2 );

    // Note that we didn't call participant.setUserCharacteristics (Makes paxCharValue null)
    characteristicToCheck = char1;
    participant.setId( 1L );
    // participant.setUserCharacteristics( userChars );

    EasyMock.expect( mockCharacteristicDAO.getAllCharacteristics() ).andReturn( characteristics );
    EasyMock.expect( mockCharacteristicDAO.getCharacteristicById( 1L ) ).andReturn( characteristicToCheck );
    EasyMock.expect( mockParticipantService.getParticipantByIdWithAssociations( EasyMock.eq( participant.getId() ), (AssociationRequestCollection)EasyMock.anyObject() ) ).andReturn( participant )
        .once();

    mockControl.replay();
    Boolean result = testInstance.isParticipantInUserCharType( participant, promotion );
    mockControl.verify();
    assertFalse( result );
  }
}