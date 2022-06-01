/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.enums;

import java.util.List;

public class NominationAwardSpecifierType extends PickListItem
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /**
   * Content Manager asset name
   */
  private static final String PICKLIST_ASSET = "picklist.nominationawardspecifiertype";

  /*
   * Award specifier types
   */
  public static final String APPROVER = "approver";
  public static final String APPROVER_AND_NOMINATOR = "nominator";

  // ---------------------------------------------------------------------------
  // Class Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a list of all nomination award specifier types.
   *
   * @return a list of all nomination award vtypes, as a <code>List</code>
   *         of <code>NominationAwardSpecifierType</code> objects.
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( NominationAwardSpecifierType.class );
  }

  /**
   * Returns the specified <code>NominationAwardSpecifierType</code> object;
   * returns null if the given code is null or invalid.
   *
   * @param code a nomination award specifier type code.
   * @return the specified <code>NominationAwardSpecifierType</code> object,
   *         or null if the given code is null or invalid.
   */
  public static NominationAwardSpecifierType lookup( String code )
  {
    return (NominationAwardSpecifierType)getPickListFactory().getPickListItem( NominationAwardSpecifierType.class, code );
  }

  // ---------------------------------------------------------------------------
  // Instance Methods
  // ---------------------------------------------------------------------------

  /**
   * This constructor has package access because only the {@link PickListFactory} class should
   * create instances of this class.
   */
  NominationAwardSpecifierType()
  {
    super();
  }

  public String getPickListAssetCode()
  {
    return PICKLIST_ASSET;
  }

  /**
   * Returns true if this <code>NominationAwardSpecifierType</code> object
   * represents the approver award specifier type; returns false otherwise.
   *
   * @return true if this <code>NominationAwardSpecifierType</code> object
   *         represents the approver award specifier type; returns false
   *         otherwise.
   */
  public boolean isApprover()
  {
    return this.equals( NominationAwardSpecifierType.lookup( NominationAwardSpecifierType.APPROVER ) );
  }

  /**
   * Returns true if this <code>NominationAwardSpecifierType</code> object
   * represents the approver and nominator award specifier type; returns
   * false otherwise.
   *
   * @return true if this <code>NominationAwardSpecifierType</code> object
   *         represents the approver and nominator award specifier type;
   *         returns false otherwise.
   */
  public boolean isApproverAndNominator()
  {
    return this.equals( NominationAwardSpecifierType.lookup( NominationAwardSpecifierType.APPROVER_AND_NOMINATOR ) );
  }
}
