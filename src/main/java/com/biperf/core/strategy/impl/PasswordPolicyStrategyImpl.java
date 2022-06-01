/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/strategy/impl/PasswordPolicyStrategyImpl.java,v $
 */

package com.biperf.core.strategy.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.biperf.core.domain.enums.RegexType;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.strategy.BaseStrategy;
import com.biperf.core.strategy.PasswordPolicyStrategy;
import com.biperf.core.strategy.PasswordRequirements;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.crypto.PasswordGenerator;
import com.biperf.core.utils.crypto.SHA256Hash;

/**
 * This class will contain methods relating to management of passwords.
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
 * <td>tennant</td>
 * <td>Apr 11, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PasswordPolicyStrategyImpl extends BaseStrategy implements PasswordPolicyStrategy
{
  /** EXPIRED_PERIOD */
  public static final String EXPIRED_PERIOD = SystemVariableService.PASSWORD_EXPIRED_PERIOD;

  /** System Variable name of where to find the minimum password length */
  protected static final String PASSWORD_MIN_LENGTH = SystemVariableService.PASSWORD_MIN_LENGTH;
  /**
   * System Variable name - true/false whether or not we should check the password for mixed
   * character sets. (i.e. some mixture of lower case, upper case, numbers, punctuation).
   */
  protected static final String PASSWORD_SHOULD_USE_REGEX = SystemVariableService.PASSWORD_SHOULD_USE_REGEX;

  /** System Variable name of where to find the regex for a pattern the password should  match */
  protected static final String PASSWORD_MUST_MATCH_REGEX = "password.match.regex";

  /**
   * System Variable name of where to find true/false if users can re-use their last password when
   * doing a change password
   */
  protected static final String PASSWORD_CAN_REUSE = SystemVariableService.PASSWORD_CAN_REUSE;

  /**
   * System Variable name of the specified password pattern to use when generating random password
   */
  protected static final String PASSWORD_PATTERN = SystemVariableService.PASSWORD_PATTERN;

  /**
  * System Variable name of the password variations not allowed for password
  */
  protected static final String PASSWORD_VARIATIONS_NOT_ALLOWED = SystemVariableService.PASSWORD_VARIATIONS_NOT_ALLOWED;

  private static final String PASSWORD_CHAR_TYPE_CODE = "profile.security.tab";

  private CMAssetService cmAssetService;

  /**
   * This method will check the given password to see if it fits the validation rules. If not, it
   * will return a List of validationErrors, which are defined as statics on this class.
   * @param newPassword must not be null
   * @param oldPasswordEncrypted must not be null
   * @return the list of errors, or an empty list if no errors
   */
  public List<ServiceError> getPasswordValidationErrors( String oldPasswordEncrypted, String newPassword )
  {
    List<ServiceError> results = new ArrayList<ServiceError>();

    // number of required distinct character types (upper, lower, number, special chars)
    int distinctCharactertTypes = getPasswordCharacterTypesRequiredCount();

    // look at the password.character.types.required value. If it's not between 1 and 4, then
    // use the regex
    if ( distinctCharactertTypes > 0 && distinctCharactertTypes < 5 )
    {
      PasswordRequirements characterTypeAvailability = getCharacterTypeAvailability();
      if ( !isValidCharacterCombination( newPassword, characterTypeAvailability ) )
      {
        // Build message with available character types appended
        String availableTypes = buildCharacterTypesAvailableList( characterTypeAvailability );
        results.add( new ServiceError( ServiceErrorMessageKeys.PASSWORD_MUST_BE_MIXED, String.valueOf( distinctCharactertTypes ), availableTypes ) );
      }
      if ( containsDisallowedCharacters( newPassword, characterTypeAvailability ) )
      {
        results.add( new ServiceError( ServiceErrorMessageKeys.PASSWORD_USED_DISALLOWED_CHARACTERS ) );
      }
    }
    else
    {
      // check the regex
      String passwordRegex = getPasswordRegularExpression();
      if ( getSystemVariableService().getPropertyByName( PASSWORD_SHOULD_USE_REGEX ).getBooleanVal() )
      {
        if ( !Pattern.compile( passwordRegex ).matcher( newPassword ).find() )
        {
          // The intent is this CM message is updated when using regex, so that it matches the regex
          results.add( new ServiceError( ServiceErrorMessageKeys.PASSW0RD_MUST_MATCH_REGEX ) );
        }
      }
    }

    // length
    int minPasswordLength = getMinimumPasswordLength();

    if ( newPassword.length() < minPasswordLength )
    {
      results.add( new ServiceError( ServiceErrorMessageKeys.PASSWORD_TOO_SHORT, Integer.toString( minPasswordLength ) ) );
    }

    // reused
    if ( !getSystemVariableService().getPropertyByName( PASSWORD_CAN_REUSE ).getBooleanVal() )
    {
      /* START MD5 to SHA256 conversion code: TO BE UPDATED LATER */
      // String newPassMD5 = new MD5Hash().encryptDefault( newPassword );
      String newPassMD5 = new SHA256Hash().encryptDefault( newPassword );
      /* END MD5 to SHA256 conversion code: TO BE UPDATED LATER */
      if ( newPassMD5.equals( oldPasswordEncrypted ) )
      {
        results.add( new ServiceError( ServiceErrorMessageKeys.PASSWORD_CANT_BE_REUSED ) );
      }
    }

    // Variations of password not allowed - BUG 3244
    String passwordVariationsNotAllowed = getSystemVariableService().getPropertyByName( PASSWORD_VARIATIONS_NOT_ALLOWED ).getStringVal();

    if ( passwordVariationsNotAllowed != null )
    {
      String[] passwordArray = passwordVariationsNotAllowed.split( "," );
      for ( String variation : passwordArray )
      {
        if ( newPassword.toLowerCase().contains( variation.toLowerCase() ) )
        {
          results.add( new ServiceError( ServiceErrorMessageKeys.PWD_INVALID ) );
          break;
        }
      }
    }
    return results;
  }

  /**
   * @return A comma separated list of character types available in passwords
   */
  @Override
  public String buildCharacterTypesAvailableList( PasswordRequirements characterTypeAvailability )
  {
    StringBuilder availableTypes = new StringBuilder();
    List<RegexType> types = characterTypeAvailability.getTypesAvailable();
    int count = 1;
    for ( RegexType type : types )
    {
      availableTypes.append( getCMAssetService().getTextFromCmsResourceBundle( PASSWORD_CHAR_TYPE_CODE + "." + type.name() ) );

      if ( count < types.size() )
      {
        availableTypes.append( ", " );
      }
      count++;
    }
    return availableTypes.toString();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.strategy.PasswordPolicyStrategy#isPasswordExpired(com.biperf.core.domain.user.User)
   * @param user
   * @return boolean
   */
  public boolean isPasswordExpired( User user )
  {
    Date lastResetDate = user.getLastResetDate();
    if ( lastResetDate != null )
    {
      long expiredPeriod = getSystemVariableService().getPropertyByName( EXPIRED_PERIOD ).getLongVal();
      return System.currentTimeMillis() - user.getLastResetDate().getTime() > expiredPeriod;
    }
    return false;
  }

  /**
   * @return a random password based on a user specified pattern (if found in system variable) if no
   *         pattern specified, then use the current default implementation of PasswordGenerator
   */
  public String generatePassword()
  {
    String passwordPattern = null;

    passwordPattern = getSystemVariableService().getPropertyByName( PASSWORD_PATTERN ).getStringVal();

    if ( passwordPattern != null )
    {
      return this.generatePassword( passwordPattern.toCharArray() );
    }
    return PasswordGenerator.generatePassword(); // uses java.security.SecureRandom
  }

  /**
   * @param pattern
   * @return a random password based on a user specified pattern. Pattern Character in pattern[]
   *         Returns ================================ =================== "A" or "B" or "z" or any
   *         alphabet in upper/lower case A random letter Any number or the character "#: A random
   *         digit Any other characters A random letter or digit (determined by SecureRandom) For
   *         example: ============================================================ To specific a 8
   *         digit-long alphanumeric password with a letter, 3 digits, a letter and 3 digits use the
   *         pattern "A###A###" or "y134J095" To specific a 6 digit-long numeric password with no
   *         specific order "837287" or "######" To specific a 7 digit-long random password
   *         "$@%&*!&"
   */
  public String generatePassword( char[] pattern )
  {
    StringBuffer randomizedPassword = new StringBuffer();

    // Assemble based on pattern one character at a time
    for ( int i = 0; i < pattern.length; i++ )
    {
      Character currentPatternCharacter = new Character( pattern[i] );
      if ( Character.isLetter( currentPatternCharacter.charValue() ) )
      {
        Character randomCharacter = PasswordGenerator.generateCharacter();
        randomizedPassword.append( randomCharacter );
      }
      else if ( Character.isDigit( currentPatternCharacter.charValue() ) || currentPatternCharacter.equals( new Character( '#' ) ) )
      {
        Integer randomInteger = PasswordGenerator.generateInteger( 10 );
        randomizedPassword.append( randomInteger );
      }
      else
      {
        randomizedPassword.append( PasswordGenerator.generatePassword( 1 ) );
      }
    }
    return randomizedPassword.toString();
  }

  @Override
  public List<ServiceError> isValidOldPassword( String oldPasswordEncrypted, String oldPasswordProvided )
  {
    List<ServiceError> results = new ArrayList<ServiceError>();
    // if SSO, this can be null for both
    if ( StringUtils.isEmpty( oldPasswordEncrypted ) && StringUtils.isEmpty( oldPasswordProvided ) )
    {
      return results;
    }

    String pwdEncrypted = new SHA256Hash().encryptDefault( oldPasswordProvided );
    if ( !pwdEncrypted.equals( oldPasswordEncrypted ) )
    {
      results.add( new ServiceError( ServiceErrorMessageKeys.INVALID_PASSWORD_ENTERED ) );
    }
    return results;
  }

  @Override
  public PasswordRequirements getPasswordRequirements()
  {
    PasswordRequirements requirements = new PasswordRequirements();

    if ( getSystemVariableService().getPropertyByName( PASSWORD_SHOULD_USE_REGEX ).getBooleanVal() )
    {
      int requiredDistinctCount = getPasswordCharacterTypesRequiredCount();
      requirements.setDistinctCharacterTypesRequired( requiredDistinctCount );

      boolean customConfig = ! ( requiredDistinctCount > 0 && requiredDistinctCount < 5 );

      if ( customConfig )
      {
        requirements.setIgnoreValidation( true );
      }
    }
    else
    {
      requirements.setIgnoreValidation( false );
    }
    requirements.setMinLength( getMinimumPasswordLength() );

    PasswordRequirements characterAvailabilities = getCharacterTypeAvailability();
    requirements.setUpperCaseAvailable( characterAvailabilities.isUpperCaseAvailable() );
    requirements.setLowerCaseAvailable( characterAvailabilities.isLowerCaseAvailable() );
    requirements.setNumericAvailable( characterAvailabilities.isNumericAvailable() );
    requirements.setSpecialCharacterAvailable( characterAvailabilities.isSpecialCharacterAvailable() );

    return requirements;
  }

  /**
   * @return An object with only the character type availabilities set
   */
  @Override
  public PasswordRequirements getCharacterTypeAvailability()
  {
    PasswordRequirements requirements = new PasswordRequirements();
    String characterTypeList = getSystemVariableService().getPropertyByName( SystemVariableService.PASSWORD_CHARACTER_TYPES_AVAILABLE ).getStringVal();
    if ( !StringUtils.isEmpty( characterTypeList ) )
    {
      String[] characterTypes = characterTypeList.split( "," );
      boolean upperAvailable = RegexType.UPPER_CASE.containsLabel( characterTypes );
      boolean lowerAvailable = RegexType.LOWER_CASE.containsLabel( characterTypes );
      boolean numberAvailable = RegexType.NUMERIC.containsLabel( characterTypes );
      boolean specialAvailable = RegexType.SPECIAL_CHARACTERS.containsLabel( characterTypes );
      requirements.setUpperCaseAvailable( upperAvailable );
      requirements.setLowerCaseAvailable( lowerAvailable );
      requirements.setNumericAvailable( numberAvailable );
      requirements.setSpecialCharacterAvailable( specialAvailable );
    }
    return requirements;
  }

  private int getMinimumPasswordLength()
  {
    return getSystemVariableService().getPropertyByName( PASSWORD_MIN_LENGTH ).getIntVal();
  }

  private String getPasswordRegularExpression()
  {
    return getSystemVariableService().getPropertyByName( PASSWORD_MUST_MATCH_REGEX ).getStringVal();
  }

  private int getPasswordCharacterTypesRequiredCount()
  {
    PropertySetItem required = getSystemVariableService().getPropertyByName( SystemVariableService.PASSWORD_DISTINCT_CHARACTER_TYPES );
    if ( Objects.isNull( required ) )
    {
      return -1;
    }
    return required.getIntVal();
  }

  private boolean isValidCharacterCombination( String newPassword, PasswordRequirements characterTypeAvailability )
  {
    int requiredTypes = getPasswordCharacterTypesRequiredCount();
    int typesRepresented = 0;
    if ( characterTypeAvailability.isUpperCaseAvailable() && Pattern.compile( RegexType.UPPER_CASE.getPattern() ).matcher( newPassword ).find() )
    {
      typesRepresented++;
    }
    if ( characterTypeAvailability.isLowerCaseAvailable() && Pattern.compile( RegexType.LOWER_CASE.getPattern() ).matcher( newPassword ).find() )
    {
      typesRepresented++;
    }
    if ( characterTypeAvailability.isNumericAvailable() && Pattern.compile( RegexType.NUMERIC.getPattern() ).matcher( newPassword ).find() )
    {
      typesRepresented++;
    }
    if ( characterTypeAvailability.isSpecialCharacterAvailable() && Pattern.compile( RegexType.SPECIAL_CHARACTERS.getPattern() ).matcher( newPassword ).find() )
    {
      typesRepresented++;
    }

    return typesRepresented >= requiredTypes;
  }

  private boolean containsDisallowedCharacters( String newPassword, PasswordRequirements characterTypeAvailability )
  {
    // Check for characters not explicitly allowed
    StringBuilder disallowedCharactersPattern = new StringBuilder( "[" );
    if ( !characterTypeAvailability.isUpperCaseAvailable() )
    {
      disallowedCharactersPattern.append( RegexType.UPPER_CASE.getText() );
    }
    if ( !characterTypeAvailability.isLowerCaseAvailable() )
    {
      disallowedCharactersPattern.append( RegexType.LOWER_CASE.getText() );
    }
    if ( !characterTypeAvailability.isNumericAvailable() )
    {
      disallowedCharactersPattern.append( RegexType.NUMERIC.getText() );
    }
    if ( !characterTypeAvailability.isSpecialCharacterAvailable() )
    {
      disallowedCharactersPattern.append( RegexType.SPECIAL_CHARACTERS.getText() );
    }
    disallowedCharactersPattern.append( "]" );

    String patternString = disallowedCharactersPattern.toString();
    if ( !patternString.equals( "[]" ) && Pattern.compile( patternString ).matcher( newPassword ).find() )
    {
      return true;
    }
    return false;
  }

  private CMAssetService getCMAssetService()
  {
    if ( cmAssetService == null )
    {
      cmAssetService = (CMAssetService)ApplicationContextFactory.getApplicationContext().getBean( CMAssetService.BEAN_NAME );
    }
    return cmAssetService;
  }

  public void setCMAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }
}
