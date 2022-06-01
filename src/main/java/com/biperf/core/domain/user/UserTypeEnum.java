/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/user/UserTypeEnum.java,v $
 */

package com.biperf.core.domain.user;

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
 * <td>leep</td>
 * <td>Jun 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public enum UserTypeEnum
{

  USER( "user" ), PARTICIPANT( "participant" );

  public static final String USER_CODE = "user";
  public static final String PARTICIPANT_CODE = "participant";

  // Static initializer to map from code field to enum value for efficient lookup
  private static final Map<String, UserTypeEnum> codeMap = new HashMap<>();
  static
  {
    for ( UserTypeEnum value : UserTypeEnum.values() )
    {
      codeMap.put( value.code, value );
    }
  }

  private final String code;

  private UserTypeEnum( String code )
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
   * @return UserTypeEnum
   */
  public static UserTypeEnum getEnum( String code )
  {
    return codeMap.get( code );
  }

  /**
   * List of all Enums of this type
   * 
   * @return List of UserTypeEnum
   */
  public static List<UserTypeEnum> getEnumList()
  {
    return Arrays.asList( UserTypeEnum.values() );
  }

}
