
package com.biperf.core.domain.enums;

import java.util.List;

public class GraphByProductType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  public static final String PICKLIST_ASSET = "picklist.report.graphby.product.type";

  public static final String PRODUCT_BY_HIERARCHY = "byhierarchy";
  public static final String HIERARCHY_BY_PRODUCT = "byproduct";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected GraphByProductType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( GraphByProductType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static GraphByProductType lookup( String code )
  {
    return (GraphByProductType)getPickListFactory().getPickListItem( GraphByProductType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static GraphByProductType getDefaultItem()
  // {
  // return (GraphByProductType)getPickListFactory().getDefaultPickListItem(
  // GraphByProductType.class
  // );
  // }
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

  public boolean isHierarchyByProduct()
  {
    return HIERARCHY_BY_PRODUCT.equals( getCode() );
  }

  public boolean isProductByHierarchy()
  {
    return PRODUCT_BY_HIERARCHY.equals( getCode() );
  }
}
