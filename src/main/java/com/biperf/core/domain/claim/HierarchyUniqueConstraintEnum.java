/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/claim/HierarchyUniqueConstraintEnum.java,v $
 */

package com.biperf.core.domain.claim;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>tennant</td>
 * <td>Jun 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public enum HierarchyUniqueConstraintEnum
{

  NONE( "NONE" ), NODE( "NODE" ), NODE_TYPE( "NODE_TYPE" ), HIERARCHY( "HIERARCHY" );

  public static final String NONE_CODE = "NONE";
  public static final String NODE_CODE = "NODE";
  public static final String NODE_TYPE_CODE = "NODE_TYPE";
  public static final String HIERARCHY_CODE = "HIERARCHY";

  // Static initializer to map from code field to enum value for efficient lookup
  private static final Map<String, HierarchyUniqueConstraintEnum> codeMap = new HashMap<>();
  static
  {
    for ( HierarchyUniqueConstraintEnum value : HierarchyUniqueConstraintEnum.values() )
    {
      codeMap.put( value.code, value );
    }
  }

  private final String code;

  private HierarchyUniqueConstraintEnum( String code )
  {
    this.code = code;
  }

  public String getCode()
  {
    return code;
  }

  /**
   * Static method for retrieving an enum of this type
   * 
   * @param code
   * @return ClaimFormStepElementEnum
   */
  public static HierarchyUniqueConstraintEnum getEnum( String code )
  {
    return codeMap.get( code );
  }

  /**
   * List of all Enums of this type
   * 
   * @return List of ClaimFormStepElementEnum
   */
  public static List<HierarchyUniqueConstraintEnum> getEnumList()
  {
    return Arrays.asList( HierarchyUniqueConstraintEnum.values() );
  }

}
