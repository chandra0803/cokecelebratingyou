/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/strategy/impl/PasswordPolicyStrategyImplTest.java,v $
 */

package com.biperf.core.strategy.impl;

import java.util.Date;
import java.util.List;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.biperf.core.builders.JMockUtil;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.strategy.PasswordRequirements;

/**
 * PasswordPolicyStrategyImplTest.
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
 * <td>Adam</td>
 * <td>Apr 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PasswordPolicyStrategyImplTest extends MockObjectTestCase
{
  private PasswordPolicyStrategyImpl passwordPolicyStrategy = new PasswordPolicyStrategyImpl();

  private Mock systemVariableServiceMock;
  private Mock cmAssetServiceMock;

  /**
   * setUp() Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   */
  public void setUp() throws Exception
  {
    super.setUp();
    systemVariableServiceMock = new Mock( SystemVariableService.class );
    cmAssetServiceMock = new Mock( CMAssetService.class );
    passwordPolicyStrategy.setSystemVariableService( (SystemVariableService)systemVariableServiceMock.proxy() );
    passwordPolicyStrategy.setCMAssetService( (CMAssetService)cmAssetServiceMock.proxy() );
  }

  private PropertySetItem buildPropertyByKey( String key )
  {
    PropertySetItem prop = new PropertySetItem();
    prop.setKey( key );
    prop.setEntityName( key );
    return prop;
  }

  public void testIsAcceptablePasswordTooShort()
  {
    setExpectations( 3, "upper,lower,number,special", 5, false, "password,pa$word,pa55word,p@ssword,passw0rd" );
    
    List<ServiceError> results = passwordPolicyStrategy.getPasswordValidationErrors( "oldpw", "cD#3" );

    assertFalse( results.isEmpty() );
    ServiceError error = (ServiceError)results.get( 0 );
    assertEquals( ServiceErrorMessageKeys.PASSWORD_TOO_SHORT, error.getKey() );
    systemVariableServiceMock.verify();
  }

  public void testIsAcceptablePasswordReused()
  {
    setExpectations( 1, "upper,lower,number,special", 2, false, "password,pa$word,pa55word,p@ssword,passw0rd" );

    final String PASSWORD_OLD_ENCRYPTED = "{V2}EECD6D59FBA15EF606F099FE68D5CA73DA5364EF3F9E893A31B70122FC876A71";
    final String PASSWORD_NEW = "beacon1";

    List<ServiceError> results = passwordPolicyStrategy.getPasswordValidationErrors( PASSWORD_OLD_ENCRYPTED, PASSWORD_NEW );

    assertFalse( results.isEmpty() );
    ServiceError error = (ServiceError)results.get( 0 );
    assertEquals( ServiceErrorMessageKeys.PASSWORD_CANT_BE_REUSED, error.getKey() );
    systemVariableServiceMock.verify();
  }

  /**
   * isPasswordExpired
   */
  public void testIsPasswordExpiredTrue()
  {
    User user = new User();
    Date lastResetDate = new Date( System.currentTimeMillis() - 2 );
    user.setLastResetDate( lastResetDate );

    PropertySetItem propertySetItem = new PropertySetItem();
    propertySetItem.setLongVal( new Long( "1" ) );

    systemVariableServiceMock.expects( once() ).method( "getPropertyByName" ).with( same( PasswordPolicyStrategyImpl.EXPIRED_PERIOD ) ).will( returnValue( propertySetItem ) );

    assertEquals( true, passwordPolicyStrategy.isPasswordExpired( user ) );
  }

  /**
   * isPasswordExpired
   */
  public void testIsPasswordExpiredFalse()
  {
    User user = new User();
    Date lastResetDate = new Date( System.currentTimeMillis() );
    user.setLastResetDate( lastResetDate );

    PropertySetItem propertySetItem = new PropertySetItem();
    propertySetItem.setLongVal( new Long( "8000" ) );

    systemVariableServiceMock.expects( once() ).method( "getPropertyByName" ).with( same( PasswordPolicyStrategyImpl.EXPIRED_PERIOD ) ).will( returnValue( propertySetItem ) );

    assertEquals( false, passwordPolicyStrategy.isPasswordExpired( user ) );
  }

  /**
   * testGeneratePasswordWithPattern
   */
  public void testGeneratePasswordWithPattern()
  {
    PropertySetItem passwordPattern = new PropertySetItem();
    passwordPattern.setStringVal( "A###A###" );

    systemVariableServiceMock.expects( once() ).method( "getPropertyByName" ).with( same( PasswordPolicyStrategyImpl.PASSWORD_PATTERN ) ).will( returnValue( passwordPattern ) );
    assertEquals( 8, passwordPolicyStrategy.generatePassword().length() );

    char[] patternToTest = "A###A###".toCharArray();
    String results = passwordPolicyStrategy.generatePassword( patternToTest );
    assertNotNull( results );

    char[] resultsCharArray = results.toCharArray();
    Character char1 = new Character( resultsCharArray[0] );
    assertTrue( Character.isLetter( char1.charValue() ) );
    Character char2 = new Character( resultsCharArray[1] );
    assertTrue( Character.isDigit( char2.charValue() ) );
    Character char3 = new Character( resultsCharArray[2] );
    assertTrue( Character.isDigit( char3.charValue() ) );
    Character char4 = new Character( resultsCharArray[3] );
    assertTrue( Character.isDigit( char4.charValue() ) );
    Character char5 = new Character( resultsCharArray[4] );
    assertTrue( Character.isLetter( char5.charValue() ) );
    Character char6 = new Character( resultsCharArray[5] );
    assertTrue( Character.isDigit( char6.charValue() ) );
    Character char7 = new Character( resultsCharArray[6] );
    assertTrue( Character.isDigit( char7.charValue() ) );
    Character char8 = new Character( resultsCharArray[7] );
    assertTrue( Character.isDigit( char8.charValue() ) );
  }

  /**
   * testGeneratePasswordWithAWierdPattern
   */
  public void testGeneratePasswordWithAWierdPattern()
  {
    PropertySetItem passwordPattern = new PropertySetItem();
    passwordPattern.setStringVal( "$@%!A#A#" ); // an unlikely choice but test it anyway

    systemVariableServiceMock.expects( once() ).method( "getPropertyByName" ).with( same( PasswordPolicyStrategyImpl.PASSWORD_PATTERN ) ).will( returnValue( passwordPattern ) );
    assertEquals( 8, passwordPolicyStrategy.generatePassword().length() );

    char[] patternToTest = "$@%!A#A#".toCharArray();
    String results = passwordPolicyStrategy.generatePassword( patternToTest );
    assertNotNull( results );

    char[] resultsCharArray = results.toCharArray();
    Character char1 = new Character( resultsCharArray[0] );
    assertNotNull( new Character( char1.charValue() ) );
    Character char2 = new Character( resultsCharArray[1] );
    assertNotNull( new Character( char2.charValue() ) );
    Character char3 = new Character( resultsCharArray[2] );
    assertNotNull( new Character( char3.charValue() ) );
    Character char4 = new Character( resultsCharArray[3] );
    assertNotNull( new Character( char4.charValue() ) );
    Character char5 = new Character( resultsCharArray[4] );
    assertTrue( Character.isLetter( char5.charValue() ) );
    Character char6 = new Character( resultsCharArray[5] );
    assertTrue( Character.isDigit( char6.charValue() ) );
    Character char7 = new Character( resultsCharArray[6] );
    assertTrue( Character.isLetter( char7.charValue() ) );
    Character char8 = new Character( resultsCharArray[7] );
    assertTrue( Character.isDigit( char8.charValue() ) );
  }

  public void testIgnoreValidationOnly()
  {
    setExpectations( 0, "upper,lower,number,special", 2, true, "password,pa$word,pa55word,p@ssword,passw0rd" );

    PropertySetItem usePasswordRegex = this.buildPropertyByKey( PasswordPolicyStrategyImpl.PASSWORD_SHOULD_USE_REGEX );
    usePasswordRegex.setBooleanVal( Boolean.TRUE );
    systemVariableServiceMock.expects( once() ).method( "getPropertyByName" ).with( same( PasswordPolicyStrategyImpl.PASSWORD_SHOULD_USE_REGEX ) ).will( returnValue( usePasswordRegex ) );

    PasswordRequirements requirements = passwordPolicyStrategy.getPasswordRequirements();
    assertEquals( "ignore validation should be true", true, requirements.isIgnoreValidation() );
  }

  public void testMatches2CharactersSuccess()
  {
    setExpectations( 2, "upper,lower,number,special", 2, false, "password,pa$word,pa55word,p@ssword,passw0rd" );

    List<ServiceError> results = passwordPolicyStrategy.getPasswordValidationErrors( "nothing", "1Sqqqqq" );

    assertNotNull( "Error List should be null", results );
    assertTrue( "Password should be valid", results.isEmpty() );
    systemVariableServiceMock.verify();
  }

  public void testMatches2CharactersFailed()
  {
    setExpectations( 2, "upper,lower,number,special", 2, false, "password,pa$word,pa55word,p@ssword,passw0rd" );

    List<ServiceError> results = passwordPolicyStrategy.getPasswordValidationErrors( "nothing", "iamonlylowercase" );

    assertNotNull( "Error List should be null", results );
    assertTrue( "Password should be invalid", results.size() > 0 );
    systemVariableServiceMock.verify();
  }

  public void testMatches3CharactersSuccess()
  {
    setExpectations( 3, "upper,lower,number,special", 2, false, "password,pa$word,pa55word,p@ssword,passw0rd" );
    
    List<ServiceError> results = passwordPolicyStrategy.getPasswordValidationErrors( "nothing", "1Sq" );

    assertNotNull( "Error List should be null", results );
    assertTrue( "Password should be valid", results.isEmpty() );
    systemVariableServiceMock.verify();
  }

  public void testMatches3CharactersFailed()
  {
    setExpectations( 3, "upper,lower,number,special", 2, false, "password,pa$word,pa55word,p@ssword,passw0rd" );
    
    List<ServiceError> results = passwordPolicyStrategy.getPasswordValidationErrors( "nothing", "HelloupperAndLowerOnly" );

    assertNotNull( "Error List should be null", results );
    assertTrue( "Password should be invalid", results.size() > 0 );
    systemVariableServiceMock.verify();
  }

  public void testMatches4CharactersSuccess()
  {
    setExpectations( 4, "upper,lower,number,special", 2, false, "password,pa$word,pa55word,p@ssword,passw0rd" );
    
    List<ServiceError> results = passwordPolicyStrategy.getPasswordValidationErrors( "nothing", "1Sq*" );

    assertNotNull( "Error List should be null", results );
    assertTrue( "Password should be valid", results.isEmpty() );
    systemVariableServiceMock.verify();
  }

  public void testMatches4CharactersFailed()
  {
    setExpectations( 4, "upper,lower,number,special", 2, false, "password,pa$word,pa55word,p@ssword,passw0rd" );

    List<ServiceError> results = passwordPolicyStrategy.getPasswordValidationErrors( "nothing", "1!Q-BUT-NO-LOWERCASE" );

    assertNotNull( "Error List should be null", results );
    assertTrue( "Password should be invalid", results.size() > 0 );
    systemVariableServiceMock.verify();
  }
  
  public void testDisallowedCharacters()
  {
    setExpectations( 2, "upper,lower,number", 2, false, "password,pa$word,pa55word,p@ssword,passw0rd" );
    
    List<ServiceError> results = passwordPolicyStrategy.getPasswordValidationErrors( "nothing", "aaBB!" );

    assertNotNull( "Error List should be null", results );
    assertTrue( "Password should be invalid", results.size() > 0 );
    systemVariableServiceMock.verify();
  }
  
  private void setExpectations( int distinctRequired, String typesAllowed, int minLength, boolean canReuse, String disallowedVariations )
  {
    PropertySetItem distinctCharactersRequired = buildPropertyByKey( SystemVariableService.PASSWORD_DISTINCT_CHARACTER_TYPES );
    distinctCharactersRequired.setIntVal( new Integer( distinctRequired ) );
    
    PropertySetItem characterTypesAllowed = buildPropertyByKey( SystemVariableService.PASSWORD_CHARACTER_TYPES_AVAILABLE );
    characterTypesAllowed.setStringVal( typesAllowed );

    PropertySetItem passwordMinLength = buildPropertyByKey( PasswordPolicyStrategyImpl.PASSWORD_MIN_LENGTH );
    passwordMinLength.setIntVal( new Integer( minLength ) );

    PropertySetItem passwordCanReuse = buildPropertyByKey( PasswordPolicyStrategyImpl.PASSWORD_CAN_REUSE );
    passwordCanReuse.setBooleanVal( canReuse );

    PropertySetItem variationsNotAllowed = buildPropertyByKey( PasswordPolicyStrategyImpl.PASSWORD_VARIATIONS_NOT_ALLOWED );
    variationsNotAllowed.setStringVal( disallowedVariations );

    systemVariableServiceMock.expects( JMockUtil.anyTimes() ).method( "getPropertyByName" ).with( same( SystemVariableService.PASSWORD_DISTINCT_CHARACTER_TYPES ) )
        .will( returnValue( distinctCharactersRequired ) );
    systemVariableServiceMock.expects( JMockUtil.anyTimes() ).method( "getPropertyByName" ).with( same( SystemVariableService.PASSWORD_CHARACTER_TYPES_AVAILABLE ) )
        .will( returnValue( characterTypesAllowed ) );
    systemVariableServiceMock.expects( JMockUtil.anyTimes() ).method( "getPropertyByName" ).with( same( PasswordPolicyStrategyImpl.PASSWORD_CAN_REUSE ) ).will( returnValue( passwordCanReuse ) );
    systemVariableServiceMock.expects( JMockUtil.anyTimes() ).method( "getPropertyByName" ).with( same( PasswordPolicyStrategyImpl.PASSWORD_MIN_LENGTH ) ).will( returnValue( passwordMinLength ) );
    systemVariableServiceMock.expects( JMockUtil.anyTimes() ).method( "getPropertyByName" ).with( same( PasswordPolicyStrategyImpl.PASSWORD_VARIATIONS_NOT_ALLOWED ) ).will( returnValue( variationsNotAllowed ) );
    
    cmAssetServiceMock.expects( JMockUtil.anyTimes() ).method( "getTextFromCmsResourceBundle" ).withAnyArguments().will( returnValue( "CM TEXT" ) );
  }
}
