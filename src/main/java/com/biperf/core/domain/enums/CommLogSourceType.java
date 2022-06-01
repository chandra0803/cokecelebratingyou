/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/enums/CommLogSourceType.java,v $
 *
 */

package com.biperf.core.domain.enums;

import java.util.List;

/**
 * CommLogSourceType <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Nov 18, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class CommLogSourceType extends PickListItem
{

  public static String EMAIL_BLAST = "emailblast";
  public static String CLIENT_EMAIL = "clientemail";
  public static String SYS_GEN = "sysgen";
  public static String PAX_CALLED = "paxcalled";

  /*
   * Online Communication from Participant Online communication from the participant Participant
   * Called CSR Participant Called into the CSR Participant Emailed CSR CSR received email from a
   * participant General Internal Request For General Requests used to develop Auto Comm Fulfillment
   * records Promo Engine Created Comm Log Creation of Communication Log by Promo Engine Procedure
   * Claims Created Comm Log Creation of communication log by Claims Processing PAX FILE LOAD
   * Participant file load Email Blast Request Client requested bulk email using BoldFish System
   * Generated System generated communication Client Email Client email from PerformanceLinQ.XL
   * screen
   */

  /**
   * Asset name used in Content Manager
   */
  private static final String PICKLIST_ASSET = "picklist.commlog.source.type";

  /**
   * This constructor is protected - only the PickListFactory class creates these instances.
   */
  protected CommLogSourceType()
  {
    super();
  }

  /**
   * Get the pick list from content manager.
   * 
   * @return List of CommLogSourceType
   */
  public static List getList()
  {
    return getPickListFactory().getPickList( CommLogSourceType.class );
  }

  /**
   * Returns null if the code is null or invalid for this list.
   * 
   * @param code
   * @return CommLogSourceType
   */
  public static CommLogSourceType lookup( String code )
  {
    return (CommLogSourceType)getPickListFactory().getPickListItem( CommLogSourceType.class, code );
  }

  /**
   * Returns null if the defaultItem is not defined - or invalid
   * 
   * @return CommLogSourceType
   */
  public static CommLogSourceType getDefaultItem()
  {
    return (CommLogSourceType)getPickListFactory().getDefaultPickListItem( CommLogSourceType.class );
  }

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
