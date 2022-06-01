/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/ContactMethodsType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * The ContactMethodsType is a concrete instance of a PickListItem which wraps a type save enum
 * object of a PickList from content manager.
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
public class ContactMethodsType extends PickListItem
{

  public static final String PROGRAM_CORRESPONDENCE = "prgcorr";
  public static final String PROGRAM_CONTACT = "prgcnt";
  public static final String E_STATEMENTS = "eStatements";
  public static final String FAX = "fax";
  public static final String MAIL = "mail";
  public static final String PHONE = "phone";
  public static final String TEXTMESSAGES = "textmessages";
  public static final String SURVEY = "survey";

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.contact.methods.type";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected ContactMethodsType()
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
    return getPickListFactory().getPickList( ContactMethodsType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return ContactMethodsType
   */
  public static ContactMethodsType lookup( String code )
  {
    return (ContactMethodsType)getPickListFactory().getPickListItem( ContactMethodsType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return ContactMethodsType
   */
  // public static ContactMethodsType getDefaultItem()
  // {
  // return (ContactMethodsType)getPickListFactory().getDefaultPickListItem(
  // ContactMethodsType.class );
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
