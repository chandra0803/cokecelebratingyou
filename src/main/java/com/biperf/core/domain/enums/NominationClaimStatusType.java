
package com.biperf.core.domain.enums;

import java.util.List;

public class NominationClaimStatusType extends PickListItem
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  private static final long serialVersionUID = 1L;

  /**
   * Content Manager asset name
   */
  private static final String PICKLIST_ASSET = "picklist.nomination.status.type";

  public static final String INCOMPLETE = "in-complete";
  public static final String COMPLETE = "complete";

  // ---------------------------------------------------------------------------
  // Class Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a list of all nomination status types.
   *
   * @return a list of all nomination status types, as a <code>List</code>
   *         of <code>NominationStatusType</code> objects.
   */
  @SuppressWarnings( "unchecked" )
  public static List<NominationClaimStatusType> getList()
  {
    return getPickListFactory().getPickList( NominationClaimStatusType.class );
  }

  /**
   * Returns the specified <code>NominationStatusType</code> object; returns
   * null if the given code is null or invalid.
   *
   * @param code a nomination status type code.
   * @return the specified <code>NominationStatusType</code> object, or
   *         null if the given code is null or invalid.
   */
  public static NominationClaimStatusType lookup( String code )
  {
    return (NominationClaimStatusType)getPickListFactory().getPickListItem( NominationClaimStatusType.class, code );
  }

  // ---------------------------------------------------------------------------
  // Instance Methods
  // ---------------------------------------------------------------------------

  /**
   * This constructor has package access because only the {@link PickListFactory} class should
   * create instances of this class.
   */
  NominationClaimStatusType()
  {
    super();
  }

  public String getPickListAssetCode()
  {
    return PICKLIST_ASSET;
  }

  public boolean isComplete()
  {
    return this.getCode().equals( COMPLETE );
  }

}
