/**
 * 
 */

package com.biperf.core.domain.enums;

import java.util.ArrayList;
import java.util.List;

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
public class ApproverType extends PickListItem
{
  /**
   * Values defined in Content Manager
   */
  public static final String NODE_OWNER_BY_LEVEL = "node_owner";
  public static final String NODE_OWNER_BY_TYPE = "node_owner_by_type";
  public static final String SPECIFIC_APPROVERS = "specific_approv";
  public static final String SUBMITTERS_MANAGER = "submitter_mgr";
  public static final String NOMINATOR_NODE_OWNER_BY_LEVEL = "nominator_node_owner";
  public static final String NOMINATOR_NODE_OWNER_BY_TYPE = "nominator_node_owner_by_type";
  public static final String NOMINEE_NODE_OWNER_BY_LEVEL = "nominee_node_owner";
  public static final String NOMINEE_NODE_OWNER_BY_TYPE = "nominee_node_owner_by_type";
  public static final String CUSTOM_APPROVERS = "custom_approvers";
  private static List NOM_APPROVER_TYPE_LIST;
  private static List GEN_APPROVER_TYPE_LIST;

  /*
   * static { NOM_APPROVER_TYPE_LIST = new ArrayList();
   * NOM_APPROVER_TYPE_LIST.add(ApproverType.lookup(ApproverType.SUBMITTERS_MANAGER));
   * NOM_APPROVER_TYPE_LIST.add(ApproverType.lookup(ApproverType.NODE_OWNER_BY_LEVEL));
   * NOM_APPROVER_TYPE_LIST.add(ApproverType.lookup(ApproverType.NODE_OWNER_BY_TYPE)); }
   */
  synchronized private static List getRemovalList()
  {
    if ( GEN_APPROVER_TYPE_LIST == null )
    {
      GEN_APPROVER_TYPE_LIST = new ArrayList();
      GEN_APPROVER_TYPE_LIST.add( ApproverType.lookup( ApproverType.NOMINATOR_NODE_OWNER_BY_LEVEL ) );
      GEN_APPROVER_TYPE_LIST.add( ApproverType.lookup( ApproverType.NOMINATOR_NODE_OWNER_BY_TYPE ) );
      GEN_APPROVER_TYPE_LIST.add( ApproverType.lookup( ApproverType.NOMINEE_NODE_OWNER_BY_LEVEL ) );
      GEN_APPROVER_TYPE_LIST.add( ApproverType.lookup( ApproverType.NOMINEE_NODE_OWNER_BY_TYPE ) );
      GEN_APPROVER_TYPE_LIST.add( ApproverType.lookup( ApproverType.CUSTOM_APPROVERS ) );
    }
    return GEN_APPROVER_TYPE_LIST;

  }

  synchronized private static List getNominationRemovalList()
  {
    if ( NOM_APPROVER_TYPE_LIST == null )
    {
      NOM_APPROVER_TYPE_LIST = new ArrayList();
      NOM_APPROVER_TYPE_LIST.add( ApproverType.lookup( ApproverType.SUBMITTERS_MANAGER ) );
      NOM_APPROVER_TYPE_LIST.add( ApproverType.lookup( ApproverType.NODE_OWNER_BY_LEVEL ) );
      NOM_APPROVER_TYPE_LIST.add( ApproverType.lookup( ApproverType.NODE_OWNER_BY_TYPE ) );
    }
    return NOM_APPROVER_TYPE_LIST;

  }

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.promotion.approvertype";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ApproverType()
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
    return getPickListFactory().getPickList( ApproverType.class );
  }

  /**
   * Get the pick list from content manager.
   * @author kumar
   * @param code
   * @return List
   */
  public static List getList( String code )
  {
    List finalList = new ArrayList( getList() );

    if ( code.equals( PromotionType.NOMINATION ) )
    {
      finalList.removeAll( getNominationRemovalList() );
    }
    else
    {
      finalList.removeAll( getRemovalList() );
    }

    return finalList;
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ApproverType lookup( String code )
  {
    return (ApproverType)getPickListFactory().getPickListItem( ApproverType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static ApproverType getDefaultItem()
  // {
  // return (ApproverType)getPickListFactory().getDefaultPickListItem( ApproverType.class );
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
