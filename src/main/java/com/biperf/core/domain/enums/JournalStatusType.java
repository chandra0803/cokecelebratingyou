/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/JournalStatusType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The JournalStatusType is a concrete instance of a PickListItem which wrappes a type save enum
 * object of a PickList from content manager. JournalStatusType
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
 * <td>robinsra</td>
 * <td>Sep 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class JournalStatusType extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.journal.status.type";

  /**
   * Ready for posting. The DepositProcess runs periodically, attempting to deposit any
   * journals with APPROVE status.  
   */
  public static final String APPROVE = "approve";

  public static final String DENY = "deny";

  /**
   * POST - journal deposit has occurred
   */
  public static final String POST = "post";

  public static final String REVERSE = "reverse";
  public static final String VOID = "void";
  public static final String SUSPENDED = "suspended";
  public static final String PENDING_MINIMUM_QUALIFIER = "pend_min_qual";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected JournalStatusType()
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
    return getPickListFactory().getPickList( JournalStatusType.class );
  }

  public static List getListForCurrentStatus( String currentStatusCode )
  {
    List filteredList = new ArrayList();
    List allPickList = getList();
    for ( Iterator iter = allPickList.iterator(); iter.hasNext(); )
    {
      PickListItem pickListItem = (PickListItem)iter.next();
      if ( currentStatusCode.equals( APPROVE ) )
      {
        if ( pickListItem.getCode().equals( POST ) )
        {
          filteredList.add( pickListItem );
        }
        if ( pickListItem.getCode().equals( VOID ) )
        {
          filteredList.add( pickListItem );
        }
      }
      else if ( currentStatusCode.equals( POST ) )
      {
        if ( pickListItem.getCode().equals( REVERSE ) )
        {
          filteredList.add( pickListItem );
        }
      }
    }
    return filteredList;
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static JournalStatusType lookup( String code )
  {
    return (JournalStatusType)getPickListFactory().getPickListItem( JournalStatusType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static JournalStatusType getDefaultItem()
  // {
  // return (JournalStatusType)getPickListFactory().getDefaultPickListItem( JournalStatusType.class
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

}
