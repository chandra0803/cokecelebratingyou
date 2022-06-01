/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/enums/MessageModuleType.java,v $
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * MessageModuleType is a concrete instance of a PickListItem which wrapes a type save enum object
 * of a PickList from content manager.
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
 * <td>Jun 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class MessageModuleType extends ModuleAwarePickListItem
{
  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.messagemoduletype";

  public static final String QUIZ = "quiz";
  public static final String CLAIM = "clm";
  public static final String SIX_SIGMA = "sixsig";
  public static final String RECOGNITION = "rec";
  public static final String PRODUCT_CLAIM = "prd";
  public static final String IDEAS = "idea";
  public static final String NOMINATIONS = "nom";
  public static final String SURVEY = "svy";
  public static final String GENERAL = "general";
  public static final String COMMUNICATIONS = "comm";
  public static final String GOALQUEST = "gq";
  public static final String CHALLENGEPOINT = "cp";
  public static final String WELCOME_MESSAGE = "wm";
  public static final String THROWDOWN = "td";
  public static final String ENGAGEMENT = "eng";
  public static final String SSI = "ssi";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected MessageModuleType()
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
    return getPickListFactory().getPickList( MessageModuleType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return PickListItem
   */
  public static MessageModuleType lookup( String code )
  {
    return (MessageModuleType)getPickListFactory().getPickListItem( MessageModuleType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return default PickListItem
   */
  // public static MessageModuleType getDefaultItem()
  // {
  // return (MessageModuleType)getPickListFactory().getDefaultPickListItem(
  // MessageModuleType.class );
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
} // end MessageModuleType
