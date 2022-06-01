
package com.biperf.core.domain.enums.nomination;

import java.util.Arrays;

public enum NominationClaimsInProgressSortColumn
{

  DATE_STARTED( 4, "date_created", "dateStarted" ), PROMO_NAME( 5, "promotion_name", "nominationPromotionName" ), NOMINEE( 6, "Name", "nominee" );

  private int columnIndex;
  /** Value the oracle procedure expects for sorting */
  private String dbColumnName;
  /** Value front end sends us for sorting */
  private String displayValue;

  private NominationClaimsInProgressSortColumn( int columnIndex, String dbColumnName, String displayValue )
  {
    this.columnIndex = columnIndex;
    this.dbColumnName = dbColumnName;
    this.displayValue = displayValue;
  }

  public int getColumnIndex()
  {
    return columnIndex;
  }

  public String getDbColumnName()
  {
    return dbColumnName;
  }

  public String getDisplayValue()
  {
    return displayValue;
  }

  /**
   * Look for the enum value with the given display value. Returns DATE_STARTED if no matching value is found.
   */
  public static NominationClaimsInProgressSortColumn getByDisplayValue( String displayValue )
  {
    return Arrays.stream( values() ).filter( ( dV ) -> dV.getDbColumnName().equals( displayValue ) ).findAny().orElseGet( () -> DATE_STARTED );
  }

  public static NominationClaimsInProgressSortColumn getByIndexIfNotExistReturnDefaultAsDateStarted( int index )
  {
    for ( NominationClaimsInProgressSortColumn e : NominationClaimsInProgressSortColumn.values() )
    {
      if ( e.getColumnIndex() == index )
      {
        return e;
      }
    }
    return NominationClaimsInProgressSortColumn.DATE_STARTED;
  }

}
