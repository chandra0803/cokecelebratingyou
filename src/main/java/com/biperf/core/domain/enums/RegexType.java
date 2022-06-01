package com.biperf.core.domain.enums;

public enum RegexType
{
  UPPER_CASE( "\\p{Upper}", "upper" ), 
  LOWER_CASE( "\\p{Lower}", "lower" ), 
  NUMERIC( "\\p{Digit}", "number" ), 
  SPECIAL_CHARACTERS( "\\p{Punct}", "special" );
  
  private static final String PATTERN = "(?=.*?{CLASS})";

  /** Regex pattern */
  private final String text;
  /** Label used in system variable definition */
  private final String label;

  /**
   * @param text
   */
  private RegexType( final String text, final String label )
  {
    this.text = text;
    this.label = label;
  }

  @Override
  public String toString()
  {
    return text;
  }
  
  public String getText()
  {
    return text;
  }
  
  public String getPattern()
  {
    return PATTERN.replace( "{CLASS}", text );
  }
  
  public String getLabel()
  {
    return label;
  }
  
  /**
   * Check to see this RegexType's label is contained in the given list. Ignores case.
   */
  public boolean containsLabel( String[] labels )
  {
    for ( String listLabel : labels )
    {
      if ( label.equalsIgnoreCase( listLabel.trim() ) )
      {
        return true;
      }
    }
    return false;
  }
}