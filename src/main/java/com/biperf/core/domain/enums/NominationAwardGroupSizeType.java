
package com.biperf.core.domain.enums;

import java.util.List;

public class NominationAwardGroupSizeType extends PickListItem
{

  /**
   * Content Manager asset name
   */
  private static final String PICKLIST_ASSET = "picklist.nominationawardgroupsizetype";

  /*
   * Award group types
   */
  public static final String LIMITED = "limited";
  public static final String UNLIMITED = "unlimited";

  /**
   * Returns a list of all nomination award group size types.
   *
   * @return a list of all nomination award group types, as a <code>List</code>
   *         of <code>NominationAwardGroupSizeType</code> objects.
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( NominationAwardGroupSizeType.class );
  }

  /**
   * Returns the specified <code>NominationAwardGroupSizeType</code> object; returns
   * null if the given code is null or invalid.
   *
   * @param code a nomination award group size type code.
   * @return the specified <code>NominationAwardGroupSizeType</code> object, or
   *         null if the given code is null or invalid.
   */
  public static NominationAwardGroupSizeType lookup( String code )
  {
    return (NominationAwardGroupSizeType)getPickListFactory().getPickListItem( NominationAwardGroupSizeType.class, code );
  }

  // ---------------------------------------------------------------------------
  // Instance Methods
  // ---------------------------------------------------------------------------

  /**
  * This constructor has package access because only the {@link PickListFactory} class should
  * create instances of this class.
  */
  NominationAwardGroupSizeType()
  {
    super();
  }

  public String getPickListAssetCode()
  {
    return PICKLIST_ASSET;
  }

  /**
  * Returns true if this <code>NominationAwardGroupSizeType</code> object represents
  * the limited size award group type; returns false otherwise.
  *
  * @return true if this <code>NominationAwardGroupSizeType</code> object represents
  *         the limited size award group type; return false otherwise.
  */
  public boolean isLimited()
  {
    return this.equals( NominationAwardGroupSizeType.lookup( NominationAwardGroupSizeType.LIMITED ) );
  }

  /**
  * Returns true if this <code>NominationAwardGroupSizeType</code> object represents
  * the unlimited size team award group type; returns false otherwise.
  *
  * @return true if this <code>NominationAwardGroupSizeType</code> object represents
  *         the unlimited size team award group type; return false otherwise.
  */
  public boolean isUnlimited()
  {
    return this.equals( NominationAwardGroupSizeType.lookup( NominationAwardGroupSizeType.UNLIMITED ) );
  }

}
