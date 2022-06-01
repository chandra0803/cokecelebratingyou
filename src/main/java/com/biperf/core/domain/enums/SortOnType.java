
package com.biperf.core.domain.enums;

public enum SortOnType
{

  LAST_NAME( "1", "lastName" ), WINS( "2", "wins" ), LOSSES( "3", "losses" ), TIES( "4", "ties" );

  private String key;
  private String value;

  private SortOnType( String key, String value )
  {
    this.key = key;
    this.value = value;
  }

  public String getKey()
  {
    return key;
  }

  public String getValue()
  {
    return value;
  }

  public void setKey( String key )
  {
    this.key = key;
  }

  public void setValue( String value )
  {
    this.value = value;
  }

}
