
package com.biperf.core.service.participant.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.biperf.core.builders.BuilderUtil;
import com.biperf.core.dao.participant.ParticipantIdentifierDAO;
import com.biperf.core.domain.enums.ParticipantIdentifierType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantIdentifier;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.participant.ParticipantActivationService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.PasswordResetService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.ui.UnitTest;
import com.biperf.core.ui.user.ContactType;
import com.biperf.core.ui.user.PaxContactType;

@RunWith( MockitoJUnitRunner.class )
public class ParticipantActivationServiceImplTest extends UnitTest
{
  @Mock
  ParticipantIdentifierDAO participantIdentifierDAO;
  @Mock
  ParticipantService participantService;
  @Mock
  UserService userService;
  @Mock
  CMAssetService cmAssetService;
  @Mock
  MailingService mailingService;
  @Mock
  PasswordResetService passwordResetService;
  @Mock
  ParticipantActivationService participantActivationService;

  @InjectMocks
  private ParticipantActivationService underTest = new ParticipantActivationServiceImpl();

  @Test
  public void getSelectedParticipantIdentifiers()
  {
    when( participantIdentifierDAO.getSelected() ).thenReturn( buildParticipantIdentifiers() );
    List<ParticipantIdentifier> list = underTest.getActiveParticipantIdentifiers();
    assertNotNull( "The Pax Identifier list is null", list );
    assertTrue( "The Pax Identifier list is empty", !list.isEmpty() );
  }

  @Test
  public void getParticipantIdentifierById()
  {
    ParticipantIdentifier pi = buildParticipantIdentifier( 1L, ParticipantIdentifierType.lookup( ParticipantIdentifierType.EMAIL ) );
    when( participantIdentifierDAO.getById( anyLong() ) ).thenReturn( buildParticipantIdentifier( 1L, ParticipantIdentifierType.lookup( ParticipantIdentifierType.EMAIL ) ) );
    ParticipantIdentifier check = underTest.getParticipantIdentifier( 1L );
    assertNotNull( "The Pax Identifie", check );
    assertTrue( "The Pax Identifier not equal", check.equals( pi ) );
  }

  @Test
  public void validatePaxActivationIdentifierValidEmail()
  {
    Participant pax = BuilderUtil.buildParticipant();
    when( participantService.getParticipantById( anyLong() ) ).thenReturn( pax );
    ParticipantIdentifier pi = buildParticipantIdentifier( 1L, ParticipantIdentifierType.lookup( ParticipantIdentifierType.EMAIL ) );
    boolean valid = underTest.isValidPaxActivationIdentifier( pax, pi, "test@biworldwide.com" );
    assertTrue( valid );
  }

  @Test
  public void validatePaxActivationIdentifierInvalidEmail()
  {
    Participant pax = BuilderUtil.buildParticipant();
    when( participantService.getParticipantById( anyLong() ) ).thenReturn( pax );
    ParticipantIdentifier pi = buildParticipantIdentifier( 1L, ParticipantIdentifierType.lookup( ParticipantIdentifierType.EMAIL ) );
    boolean valid = underTest.isValidPaxActivationIdentifier( pax, pi, "bogus@biworldwide.com" );
    assertTrue( !valid );
  }

  @Test
  public void validatePaxActivationIdentifierValidDOB()
  {
    Participant pax = BuilderUtil.buildParticipant();
    Calendar bd = Calendar.getInstance();
    bd.set( 1974, 11, 10 );
    pax.setBirthDate( bd.getTime() );
    when( participantService.getParticipantById( anyLong() ) ).thenReturn( pax );
    ParticipantIdentifier pi = buildParticipantIdentifier( 1L, ParticipantIdentifierType.lookup( ParticipantIdentifierType.DOB ) );
    boolean valid = underTest.isValidPaxActivationIdentifier( pax, pi, "12/10/1974" );
    assertTrue( valid );
  }

  @Test
  public void validatePaxActivationIdentifierInvalidDOB()
  {
    Participant pax = BuilderUtil.buildParticipant();
    Calendar bd = Calendar.getInstance();
    bd.set( 1974, 11, 10 );
    pax.setBirthDate( bd.getTime() );
    when( participantService.getParticipantById( anyLong() ) ).thenReturn( pax );
    ParticipantIdentifier pi = buildParticipantIdentifier( 1L, ParticipantIdentifierType.lookup( ParticipantIdentifierType.EMAIL ) );
    boolean valid = underTest.isValidPaxActivationIdentifier( pax, pi, "11213234" );
    assertTrue( !valid );
    valid = underTest.isValidPaxActivationIdentifier( pax, pi, "11/10/1974" );
    assertTrue( !valid );
  }

  @Test
  public void validatePaxActivationIdentifierValidCity()
  {
    Participant pax = BuilderUtil.buildParticipant();
    when( participantService.getParticipantById( anyLong() ) ).thenReturn( pax );
    ParticipantIdentifier pi = buildParticipantIdentifier( 1L, ParticipantIdentifierType.lookup( ParticipantIdentifierType.CITY ) );
    boolean valid = underTest.isValidPaxActivationIdentifier( pax, pi, "edina" );
    assertTrue( valid );
  }

  @Test
  public void validatePaxActivationIdentifierInvalidCity()
  {
    Participant pax = BuilderUtil.buildParticipant();
    when( participantService.getParticipantById( anyLong() ) ).thenReturn( pax );
    ParticipantIdentifier pi = buildParticipantIdentifier( 1L, ParticipantIdentifierType.lookup( ParticipantIdentifierType.CITY ) );
    boolean valid = underTest.isValidPaxActivationIdentifier( pax, pi, "moscow" );
    assertTrue( !valid );
  }

  @Test
  public void validatePaxActivationIdentifierValidPostalCode()
  {
    Participant pax = BuilderUtil.buildParticipant();
    when( participantService.getParticipantById( anyLong() ) ).thenReturn( pax );
    ParticipantIdentifier pi = buildParticipantIdentifier( 1L, ParticipantIdentifierType.lookup( ParticipantIdentifierType.POSTAL_CODE ) );
    boolean valid = underTest.isValidPaxActivationIdentifier( pax, pi, "12345" );
    assertTrue( valid );
  }

  @Test
  public void validatePaxActivationIdentifierInvalidPostalCode()
  {
    Participant pax = BuilderUtil.buildParticipant();
    when( participantService.getParticipantById( anyLong() ) ).thenReturn( pax );
    ParticipantIdentifier pi = buildParticipantIdentifier( 1L, ParticipantIdentifierType.lookup( ParticipantIdentifierType.POSTAL_CODE ) );
    boolean valid = underTest.isValidPaxActivationIdentifier( pax, pi, "bogus" );
    assertTrue( !valid );
  }

  @Test
  public void testSendActivationLinkToParticipantEmail() throws ServiceErrorException
  {
    User user = BuilderUtil.buildUser();
    UserEmailAddress address = BuilderUtil.buildUserEmailAddress();
    address.setUser( user );
    ContactType contactType = ContactType.EMAIL;
    when( userService.getUserEmailAddressById( any() ) ).thenReturn( address );
    when( passwordResetService.generateTokenAndSave( anyLong(), any() ) ).thenReturn( BuilderUtil.buildIssuedUserTokenForEmail( user ) );
    when( mailingService.buildPAXForgotPasswordNotification( any( long.class ), any( PaxContactType.class ), any( String.class ) ) ).thenReturn( new Mailing() );
    PaxContactType pct = underTest.sendActivationLinkToParticipant( 1L, contactType );
    assertNotNull( "The response contact destination is null", pct );
    assertNotNull( "The response contact destination VALUE is null", pct.getValue() );
    assertTrue( "The response contact destination is not correct", pct.getValue().equals( address.getEmailAddr() ) );
  }

  @Test
  public void testSendActivationLinkToParticipantPhone() throws ServiceErrorException
  {
    User user = BuilderUtil.buildUser();
    UserPhone phone = BuilderUtil.buildUserPhone();
    phone.setUser( user );
    ContactType contactType = ContactType.PHONE;
    when( userService.getUserPhoneById( any() ) ).thenReturn( phone );
    when( passwordResetService.generateTokenAndSave( anyLong(), any() ) ).thenReturn( BuilderUtil.buildIssuedUserTokenForPhone( user ) );
    when( mailingService.buildPAXForgotPasswordNotification( any( long.class ), any( PaxContactType.class ), any( String.class ) ) ).thenReturn( new Mailing() );
    PaxContactType pct = underTest.sendActivationLinkToParticipant( 1L, contactType );
    assertNotNull( "The response contact destination is null", pct );
    assertNotNull( "The response contact destination VALUE is null", pct.getValue() );
    assertTrue( "The response contact destination is not correct", pct.getValue().equals( phone.getPhoneNbr() ) );
  }

  @Test
  public void getSaveParticipantIdentifierForUpdate()
  {
    when( participantIdentifierDAO.getById( anyLong() ) ).thenReturn( buildParticipantIdentifier( 1L, ParticipantIdentifierType.lookup( ParticipantIdentifierType.EMAIL ) ) );
    List<ParticipantIdentifier> list = buildParticipantIdentifiers();
    try
    {
      when( cmAssetService.getUniqueAssetCode( any( String.class ) ) ).thenReturn( new String( "hi" ) );
      underTest.save( list );
    }
    catch( ServiceErrorException e )
    {
      fail();
    }
    System.out.println( list.get( 0 ) );
    assertNotNull( "The Pax Identifier list is null", list );
    assertTrue( "The Pax Identifier list is empty", !list.isEmpty() );
    assertTrue( "The Pax Identifier CM asset code is null", list.get( 0 ).getCmAssetCode() != null );
    assertTrue( "The Pax Identifier CM asset code is not correct", list.get( 0 ).getCmAssetCode().equals( "hi" ) );
  }

  private List<ParticipantIdentifier> buildParticipantIdentifiers()
  {
    List<ParticipantIdentifier> pis = new ArrayList<ParticipantIdentifier>();
    pis.add( buildParticipantIdentifier( 1L, ParticipantIdentifierType.lookup( ParticipantIdentifierType.EMAIL ) ) );
    return pis;
  }

  private ParticipantIdentifier buildParticipantIdentifier( Long id, ParticipantIdentifierType type )
  {
    ParticipantIdentifier pi = new ParticipantIdentifier();
    pi.setId( id );
    pi.setDescription( "description_" + id );
    pi.setLabel( "label_" + id );
    pi.setSelected( true );
    pi.setCmAssetCode( "hi" );
    pi.setParticipantIdentifierType( type );
    return pi;
  }
}
