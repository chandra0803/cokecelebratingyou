
package com.biperf.core.ui.user;

import com.biperf.core.exception.ExceptionView;
import com.biperf.core.strategy.PasswordRequirements;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@SuppressWarnings( "serial" )
@JsonInclude( value = Include.NON_EMPTY )
public class PasswordValidationRules extends ExceptionView
{
  private boolean ignoreValidation = false;

  private PasswordCheck lowerCase;
  private PasswordCheck upperCase;
  private PasswordCheck numeric;
  private PasswordCheck specialCharacter;
  private PasswordCheck minimumLengthCheck;
  private PasswordCheck distinctCharacterTypesCheck;
  private int minLength;
  private int distinctCharacterTypes;

  public PasswordCheck getDistinctCharacterTypesCheck()
  {
    return distinctCharacterTypesCheck;
  }

  public void setDistinctCharacterTypesCheck( PasswordCheck distinctCharacterTypesCheck )
  {
    this.distinctCharacterTypesCheck = distinctCharacterTypesCheck;
  }

  public PasswordValidationRules( PasswordRequirements requirements )
  {
    this.minLength = requirements.getMinLength();
    this.ignoreValidation = requirements.isIgnoreValidation();
  }

  public boolean isIgnoreValidation()
  {
    return ignoreValidation;
  }

  public void setIgnoreValidation( boolean ignoreValidation )
  {
    this.ignoreValidation = ignoreValidation;
  }

  public PasswordCheck getLowerCase()
  {
    return lowerCase;
  }

  public void setLowerCase( PasswordCheck lowerCase )
  {
    this.lowerCase = lowerCase;
  }

  public PasswordCheck getUpperCase()
  {
    return upperCase;
  }

  public void setUpperCase( PasswordCheck upperCase )
  {
    this.upperCase = upperCase;
  }

  public PasswordCheck getNumeric()
  {
    return numeric;
  }

  public void setNumeric( PasswordCheck numeric )
  {
    this.numeric = numeric;
  }

  public PasswordCheck getSpecialCharacter()
  {
    return specialCharacter;
  }

  public void setSpecialCharacter( PasswordCheck specialCharacter )
  {
    this.specialCharacter = specialCharacter;
  }

  public int getMinLength()
  {
    return minLength;
  }

  public void setMinLength( int minLength )
  {
    this.minLength = minLength;
  }

  public PasswordCheck getMinimumLengthCheck()
  {
    return minimumLengthCheck;
  }

  public void setMinimumLengthCheck( PasswordCheck minimumLengthCheck )
  {
    this.minimumLengthCheck = minimumLengthCheck;
  }

  public int getDistinctCharacterTypes()
  {
    return distinctCharacterTypes;
  }

  public void setDistinctCharacterTypes( int distinctCharacterTypes )
  {
    this.distinctCharacterTypes = distinctCharacterTypes;
  }
}
