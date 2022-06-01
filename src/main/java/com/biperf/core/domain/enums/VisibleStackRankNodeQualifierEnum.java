/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * .
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
 * <td>wadzinsk</td>
 * <td>May 30, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public enum VisibleStackRankNodeQualifierEnum
{

  MANAGER_OWNER( "manager_owner" ), STACK_RANK_PARTICIPANT( "participant" );

  public static final String MANAGER_OWNER_CODE = "manager_owner";
  public static final String STACK_RANK_PARTICIPANT_CODE = "participant";

  // Static initializer to map from code field to enum value for efficient lookup
  private static final Map<String, VisibleStackRankNodeQualifierEnum> codeMap = new HashMap<>();
  static
  {
    for ( VisibleStackRankNodeQualifierEnum value : VisibleStackRankNodeQualifierEnum.values() )
    {
      codeMap.put( value.code, value );
    }
  }

  private final String code;

  private VisibleStackRankNodeQualifierEnum( String code )
  {
    this.code = code;
  }

  /**
   * Static method for retrieving an enum of this type
   * 
   * @param code
   * @return UserTypeEnum
   */
  public static VisibleStackRankNodeQualifierEnum getEnum( String code )
  {
    return codeMap.get( code );
  }

  /**
   * Static method for retrieving the Enum List with the super getEnumList().
   * 
   * @return List of ClaimFormStepElementEnum
   */
  public static List<VisibleStackRankNodeQualifierEnum> getEnumList()
  {
    return Arrays.asList( VisibleStackRankNodeQualifierEnum.values() );
  }

}
