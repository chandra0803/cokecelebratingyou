
package com.biperf.core.ui.budget;

/**
 * BudgetCharacteristic.
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
 * <td>Kaiden Vo</td>
 * <td>Aug 22, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BudgetCharacteristic
{
  private String name;
  private String value;
  private String cmAssetCode;
  private String cmAssetKey;

  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  public void setCmAssetCode( String cmAssetCode )
  {
    this.cmAssetCode = cmAssetCode;
  }

  public String getCmAssetKey()
  {
    return cmAssetKey;
  }

  public void setCmAssetKey( String cmAssetKey )
  {
    this.cmAssetKey = cmAssetKey;
  }

  /**
   * @return String
   */
  public String getName()
  {
    return name;
  }

  /**
   * @param name
   */
  public void setName( String name )
  {
    this.name = name;
  }

  /**
   * @return String
   */
  public String getValue()
  {
    return value;
  }

  /**
   * @param value
   */
  public void setValue( String value )
  {
    this.value = value;
  }
}
