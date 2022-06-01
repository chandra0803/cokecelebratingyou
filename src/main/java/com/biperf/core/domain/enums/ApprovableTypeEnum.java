/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * ApproverType.
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
 * <td>asondgeroth</td>
 * <td>Jul 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public enum ApprovableTypeEnum
{

  CLAIM( "claim" ), CLAIM_GROUP( "claim_group" );

  private String code;

  // Static initializer to map from code field to enum value for efficient lookup
  private static final Map<String, ApprovableTypeEnum> codeMap = new HashMap<>();
  static
  {
    for ( ApprovableTypeEnum value : ApprovableTypeEnum.values() )
    {
      codeMap.put( value.code, value );
    }
  }

  private ApprovableTypeEnum( String code )
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
  public static ApprovableTypeEnum getEnum( String code )
  {
    return codeMap.get( code );
  }

  public boolean isClaim()
  {
    return this.equals( CLAIM );
  }

  public boolean isClaimGroup()
  {
    return this.equals( CLAIM_GROUP );
  }

}
