
package com.biperf.core.domain.enums;

import java.util.List;

/**
 * A picklist with the options nominator and nominee - the two sides of a nomination (giver / receiver)
 */
@SuppressWarnings( "serial" )
public class NominationAudienceType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.nomination.audience.type";

  public static final String NOMINEE = "nominee";
  public static final String NOMINATOR = "nominator";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected NominationAudienceType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List
   */
  @SuppressWarnings( "unchecked" )
  public static List<NominationAudienceType> getList()
  {
    return getPickListFactory().getPickList( NominationAudienceType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static NominationAudienceType lookup( String code )
  {
    return (NominationAudienceType)getPickListFactory().getPickListItem( NominationAudienceType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  public static NominationAudienceType getDefaultItem()
  {
    return (NominationAudienceType)getPickListFactory().getDefaultPickListItem( NominationAudienceType.class );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.enums.PickListItem#getPickListAssetCode()
   * @return PICKLIST_ASSET
   */
  public String getPickListAssetCode()
  {
    return PICKLIST_ASSET;
  }
}
