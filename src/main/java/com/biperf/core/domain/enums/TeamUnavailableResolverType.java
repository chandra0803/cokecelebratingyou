
package com.biperf.core.domain.enums;

import java.util.List;

@SuppressWarnings( "serial" )
public class TeamUnavailableResolverType extends ModuleAwarePickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.throwdown.teamunavailableresolver.type";

  public static final String MINIMUM_QUALIFIER = "minimum_qualifier";
  public static final String CALCULATED_AVERAGE_EXCLUSIVE = "calc_avg_exclusive";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected TeamUnavailableResolverType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of MatchTeamOutcomeType
   */
  @SuppressWarnings( "unchecked" )
  public static List<TeamUnavailableResolverType> getList()
  {
    return getPickListFactory().getPickList( TeamUnavailableResolverType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PromotionType
   */
  public static TeamUnavailableResolverType lookup( String code )
  {
    return (TeamUnavailableResolverType)getPickListFactory().getPickListItem( TeamUnavailableResolverType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PromotionType
   */
  public static TeamUnavailableResolverType getDefaultItem()
  {
    return (TeamUnavailableResolverType)getPickListFactory().getDefaultPickListItem( TeamUnavailableResolverType.class );
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

  public boolean isMinimumQualifier()
  {
    return MINIMUM_QUALIFIER.equals( getCode() );
  }

  public boolean isCalculatedAverageExclusive()
  {
    return CALCULATED_AVERAGE_EXCLUSIVE.equals( getCode() );
  }
}
