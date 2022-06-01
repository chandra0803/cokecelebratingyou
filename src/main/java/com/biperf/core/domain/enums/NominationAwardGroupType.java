/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/NominationAwardGroupType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

public class NominationAwardGroupType extends PickListItem
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /**
   * Content Manager asset name
   */
  private static final String PICKLIST_ASSET = "picklist.nominationawardgrouptype";

  /*
   * Award group types
   */
  public static final String INDIVIDUAL = "individual";
  public static final String TEAM = "team";
  public static final String INDIVIDUAL_OR_TEAM = "both";
  public static final String LIMITED_SIZE_TEAM    = "limited size team";
  public static final String UNLIMITED_SIZE_TEAM  = "unlimited size team";

  // ---------------------------------------------------------------------------
  // Class Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a list of all nomination award group types.
   *
   * @return a list of all nomination award group types, as a <code>List</code>
   *         of <code>NominationAwardGroupType</code> objects.
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( NominationAwardGroupType.class );
  }

  /**
   * Returns the specified <code>NominationAwardGroupType</code> object; returns
   * null if the given code is null or invalid.
   *
   * @param code a nomination award group type code.
   * @return the specified <code>NominationAwardGroupType</code> object, or
   *         null if the given code is null or invalid.
   */
  public static NominationAwardGroupType lookup( String code )
  {
    return (NominationAwardGroupType)getPickListFactory().getPickListItem( NominationAwardGroupType.class, code );
  }

  // ---------------------------------------------------------------------------
  // Instance Methods
  // ---------------------------------------------------------------------------

  /**
   * This constructor has package access because only the {@link PickListFactory} class should
   * create instances of this class.
   */
  NominationAwardGroupType()
  {
    super();
  }

  public String getPickListAssetCode()
  {
    return PICKLIST_ASSET;
  }

  /**
   * Returns true if this <code>NominationAwardGroupType</code> object represents
   * the individual award group type; returns false otherwise.
   *
   * @return true if this <code>NominationAwardGroupType</code> object represents
   *         the individual award group type; return false otherwise.
   */
  public boolean isIndividual()
  {
    return this.equals( NominationAwardGroupType.lookup( NominationAwardGroupType.INDIVIDUAL ) );
  }

  /**
   * Returns true if this <code>NominationAwardGroupType</code> object represents
   * the limited size team award group type; returns false otherwise.
   *
   * @return true if this <code>NominationAwardGroupType</code> object represents
   *         the limited size team award group type; return false otherwise.
   */
  public boolean isTeam()
  {
    return this.equals( NominationAwardGroupType.lookup( NominationAwardGroupType.TEAM ) );
  }

  /**
   * Returns true if this <code>NominationAwardGroupType</code> object represents
   * the unlimited size team award group type; returns false otherwise.
   *
   * @return true if this <code>NominationAwardGroupType</code> object represents
   *         the unlimited size team award group type; return false otherwise.
   */
  public boolean isIndividualOrTeam()
  {
    return this.equals( NominationAwardGroupType.lookup( NominationAwardGroupType.INDIVIDUAL_OR_TEAM ) );
  }

  public String getNameOfIndividualOrTeamOnlyNotBoth()
  {
    return !INDIVIDUAL_OR_TEAM.equalsIgnoreCase( getCode() ) ? getName() : null;
  }


  /**
   * Returns true if this <code>NominationAwardGroupType</code> object represents
   * the limited size team award group type; returns false otherwise.
   *
   * @return true if this <code>NominationAwardGroupType</code> object represents
   *         the limited size team award group type; return false otherwise.
   */
  public boolean isLimitedSizeTeam()
  {
    return this.equals( NominationAwardGroupType.lookup( NominationAwardGroupType.LIMITED_SIZE_TEAM ) );
  }

  /**
   * Returns true if this <code>NominationAwardGroupType</code> object represents
   * the unlimited size team award group type; returns false otherwise.
   *
   * @return true if this <code>NominationAwardGroupType</code> object represents
   *         the unlimited size team award group type; return false otherwise.
   */
  public boolean isUnlimitedSizeTeam()
  {
    return this.equals( NominationAwardGroupType.lookup( NominationAwardGroupType.UNLIMITED_SIZE_TEAM ) );
  }
}
