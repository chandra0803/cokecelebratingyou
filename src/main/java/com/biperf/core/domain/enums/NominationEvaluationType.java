/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.enums;

import java.util.List;

public class NominationEvaluationType extends PickListItem
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /**
   * Content Manager asset name
   */
  private static final String PICKLIST_ASSET = "picklist.nominationevaluationtype";

  /*
   * Evaluation types
   */
  public static final String INDEPENDENT = "independent";
  public static final String CUMULATIVE = "cumulative";

  // ---------------------------------------------------------------------------
  // Class Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a list of all nomination evaluation types.
   *
   * @return a list of all nomination evaluation types, as a <code>List</code>
   *         of <code>NominationEvaluationType</code> objects.
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( NominationEvaluationType.class );
  }

  /**
   * Returns the specified <code>NominationEvaluationType</code> object; returns
   * null if the given code is null or invalid.
   *
   * @param code a nomination evaluation type code.
   * @return the specified <code>NominationEvaluationType</code> object, or
   *         null if the given code is null or invalid.
   */
  public static NominationEvaluationType lookup( String code )
  {
    return (NominationEvaluationType)getPickListFactory().getPickListItem( NominationEvaluationType.class, code );
  }

  // ---------------------------------------------------------------------------
  // Instance Methods
  // ---------------------------------------------------------------------------

  /**
   * This constructor has package access because only the {@link PickListFactory} class should
   * create instances of this class.
   */
  NominationEvaluationType()
  {
    super();
  }

  public String getPickListAssetCode()
  {
    return PICKLIST_ASSET;
  }

  public static boolean isCumulative( NominationEvaluationType actual )
  {
    if ( actual == null )
    {
      return false;
    }

    return actual.equals( NominationEvaluationType.lookup( NominationEvaluationType.CUMULATIVE ) );
  }

}
