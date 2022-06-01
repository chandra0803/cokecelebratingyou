
package com.biperf.core.strategy;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.enums.RegexType;

public class PasswordRequirements
{
  private boolean ignoreValidation = false;
  private int distinctCharacterTypesRequired = 0;
  private int minLength = 12;

  private boolean lowerCaseAvailable = false;
  private boolean upperCaseAvailable = false;
  private boolean numericAvailable = false;
  private boolean specialCharacterAvailable = false;

  public boolean isIgnoreValidation()
  {
    return ignoreValidation;
  }

  public void setIgnoreValidation( boolean ignoreValidation )
  {
    this.ignoreValidation = ignoreValidation;
  }

  public int getDistinctCharacterTypesRequired()
  {
    return distinctCharacterTypesRequired;
  }

  public void setDistinctCharacterTypesRequired( int distinctCharacterTypesRequired )
  {
    this.distinctCharacterTypesRequired = distinctCharacterTypesRequired;
  }

  public int getMinLength()
  {
    return minLength;
  }

  public void setMinLength( int minLength )
  {
    this.minLength = minLength;
  }

  public boolean isLowerCaseAvailable()
  {
    return lowerCaseAvailable;
  }

  public void setLowerCaseAvailable( boolean lowerCaseAvailable )
  {
    this.lowerCaseAvailable = lowerCaseAvailable;
  }

  public boolean isUpperCaseAvailable()
  {
    return upperCaseAvailable;
  }

  public void setUpperCaseAvailable( boolean upperCaseAvailable )
  {
    this.upperCaseAvailable = upperCaseAvailable;
  }

  public boolean isNumericAvailable()
  {
    return numericAvailable;
  }

  public void setNumericAvailable( boolean numericAvailable )
  {
    this.numericAvailable = numericAvailable;
  }

  public boolean isSpecialCharacterAvailable()
  {
    return specialCharacterAvailable;
  }

  public void setSpecialCharacterAvailable( boolean specialCharacterAvailable )
  {
    this.specialCharacterAvailable = specialCharacterAvailable;
  }

  public List<RegexType> getTypesAvailable()
  {
    List<RegexType> types = new ArrayList<RegexType>();
    if ( this.isLowerCaseAvailable() )
    {
      types.add( RegexType.LOWER_CASE );
    }
    if ( this.isUpperCaseAvailable() )
    {
      types.add( RegexType.UPPER_CASE );
    }
    if ( this.isNumericAvailable() )
    {
      types.add( RegexType.NUMERIC );
    }
    if ( this.isSpecialCharacterAvailable() )
    {
      types.add( RegexType.SPECIAL_CHARACTERS );
    }

    return types;
  }
}
