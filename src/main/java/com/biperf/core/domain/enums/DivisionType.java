package com.biperf.core.domain.enums;

import java.util.List;

public class DivisionType extends PickListItem
{

	  /**
	   * Asset name used in Content Manager
	   */
	  public static final String PICKLIST_ASSET = "picklist.division.type";

	  public static final String CODE = "code";
	  public static final String DIVISION = "division"; 
	  /**
	   * This constructor is protected - only the PickListFactory class creates these instances.
	   */
	  protected DivisionType()
	  {
	    super();
	  }

	  /**
	   * Get the pick list from content manager.
	   * 
	   * @return List of DepartmentType
	   */
	  public static List getList()
	  {
	    return getPickListFactory().getPickList( DivisionType.class );
	  }

	  /**
	   * Returns null if the code is null or invalid for this list.
	   * 
	   * @param code
	   * @return DepartmentType
	   */
	  public static DivisionType lookup( String code )
	  {
	    return (DivisionType)getPickListFactory().getPickListItem( DivisionType.class, code );
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
