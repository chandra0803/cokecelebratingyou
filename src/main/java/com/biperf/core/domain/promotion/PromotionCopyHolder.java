
package com.biperf.core.domain.promotion;

/**
 * PromotionCopyHolder.
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
 * <td>Tammy Cheng</td>
 * <td>Oct 3, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionCopyHolder
{
  private String currentName;
  private String newName;

  /**
   * @return currentName
   */
  public String getCurrentName()
  {
    return currentName;
  }

  /**
   * @param currentName
   */
  public void setCurrentName( String currentName )
  {
    this.currentName = currentName;
  }

  /**
   * @return newName
   */
  public String getNewName()
  {
    return newName;
  }

  /**
   * @param newName
   */
  public void setNewName( String newName )
  {
    this.newName = newName;
  }

}
