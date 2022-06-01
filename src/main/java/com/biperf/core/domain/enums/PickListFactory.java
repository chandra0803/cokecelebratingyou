
package com.biperf.core.domain.enums;

import java.util.Comparator;
import java.util.List;

/**
 * The PickListFactory interface builds PickLists from the Content Manager. This class is used by
 * PickListItem subclasses.
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
 * <td>dunne</td>
 * <td>Apr 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface PickListFactory
{
  /**
   * Get a PickListItem
   * 
   * @param clazz
   * @param code
   * @return <code>PickListItem</code>
   */
  public PickListItem getPickListItem( Class clazz, String code );

  /**
   * Get the default pick list item for the specified class from Content Manager. This item will be
   * an instance of the specified class which is a concrete subclass of PickListItem.
   * 
   * @param clazz
   * @return The default PickListItem
   */
  public PickListItem getDefaultPickListItem( Class clazz );

  /**
   * Get the pick list for the specified class from Content Manger. The returned List will be
   * instances of the specified class which is a concrete subclass of PickListItem. This method will
   * order the List using the PickListItemNameComparator class.
   * 
   * @see com.biperf.core.domain.enums.PickListItemNameComparator
   * @param clazz
   * @return A list of PickListItems
   */
  public List getPickList( Class clazz );

  /**
   * Get the pick list for the specified class from Content Manger. The returned List will be
   * instances of the specified class which is a concrete subclass of PickListItem. The List will be
   * sorted using the supplied Comparator.
   * 
   * @param clazz
   * @param pickListComparator
   * @return A list of PickListItems
   */
  public List getPickList( Class clazz, Comparator pickListComparator );
}
