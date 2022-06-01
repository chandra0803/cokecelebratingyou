
package com.biperf.core.value.underArmour.v1.actigraphy.response;

public enum HistoryType
{

  DEAUTHORIZE( "deauthorize" ), AUTHORIZE( "authorize" ), REFRESH( "refresh" );

  private String name;

  HistoryType( String name )
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }

  public static HistoryType parse( String name )
  {
    for ( HistoryType history : HistoryType.values() )
    {
      if ( history.getName().equals( name ) )
      {
        return history;
      }
    }
    return null;
  }

}
