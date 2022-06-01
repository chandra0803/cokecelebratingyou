/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/ContactMethod.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * The ParticipantContactMethods is a concrete instance of a PickListItem which wraps a type save
 * enum object of a PickList from content manager.
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
 * <td>rosenquest</td>
 * <td>May 3, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ContactMethod extends PickListItem
{

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.participantcontactmethods";

  public static final String EMAIL = "email";
  public static final String TELEPHONE = "telephone";
  public static final String POSTAL_MAIL = "postalmail";
  public static final String FAX = "fax";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ContactMethod()
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
    return getPickListFactory().getPickList( ContactMethod.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static ContactMethod lookup( String code )
  {
    return (ContactMethod)getPickListFactory().getPickListItem( ContactMethod.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static ContactMethod getDefaultItem()
  // {
  // return (ContactMethod)getPickListFactory()
  // .getDefaultPickListItem( ContactMethod.class );
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
