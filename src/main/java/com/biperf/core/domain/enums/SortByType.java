
package com.biperf.core.domain.enums;

public enum SortByType
{
  ASC( "asc" ), DESC( "desc" );

  private String sortBy;

  private SortByType( String sortBy )
  {
    this.sortBy = sortBy;
  }

  public String getSortBy()
  {
    return sortBy;
  }

  public static SortByType getByCode( String code )
  {
    for ( SortByType sortBy : SortByType.values() )
    {
      if ( sortBy.getSortBy().equals( code ) )
      {
        return sortBy;
      }
    }
    return null;
  }

  public static SortByType getByCodeIfNotExistReturnDefaultAsASC( String code )
  {
    for ( SortByType sortBy : SortByType.values() )
    {
      if ( sortBy.getSortBy().equals( code ) )
      {
        return sortBy;
      }
    }
    return SortByType.ASC;
  }

  public static SortByType getByCodeIfNotExistReturnDefaultAsDESC( String code )
  {
    for ( SortByType sortBy : SortByType.values() )
    {
      if ( sortBy.getSortBy().equals( code ) )
      {
        return sortBy;
      }
    }
    return SortByType.DESC;
  }

}
