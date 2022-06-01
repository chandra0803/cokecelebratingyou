/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/promotion/PromotionClaimFormStepBean.java,v $
 */

package com.biperf.core.ui.promotion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * PromotionClaimFormStepBean.
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
 * <td>sedey</td>
 * <td>Aug 11, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionClaimFormStepBean implements Serializable
{
  private Long claimFormStepId;
  private String cmAssetCode;
  private String cmName;

  private List claimFormStepElementList = new ArrayList();
  private List claimFormNotificationList = new ArrayList();

  /**
   * empty constructor
   */
  public PromotionClaimFormStepBean()
  {
    // empty constructor
  }

  /**
   * @return claimFormStepId
   */
  public Long getClaimFormStepId()
  {
    return claimFormStepId;
  }

  /**
   * @param claimFormStepId
   */
  public void setClaimFormStepId( Long claimFormStepId )
  {
    this.claimFormStepId = claimFormStepId;
  }

  /**
   * @return cmAssetCode
   */
  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  /**
   * @param cmAssetCode
   */
  public void setCmAssetCode( String cmAssetCode )
  {
    this.cmAssetCode = cmAssetCode;
  }

  /**
   * @return cmName
   */
  public String getCmName()
  {
    return cmName;
  }

  /**
   * @param cmName
   */
  public void setCmName( String cmName )
  {
    this.cmName = cmName;
  }

  /**
   * @return List of PromotionClaimFormStepElementBean objects
   */
  public List getClaimFormStepElementValueList()
  {
    return claimFormStepElementList;
  }

  /**
   * @param claimFormStepElementValueList
   */
  public void setClaimFormStepElementValueList( List claimFormStepElementValueList )
  {
    this.claimFormStepElementList = claimFormStepElementValueList;
  }

  /**
   * Accessor for the number of PromotionClaimFormStepElementBean objects in the list.
   * 
   * @return int
   */
  public int getClaimFormStepElementValueListCount()
  {
    if ( claimFormStepElementList == null )
    {
      return 0;
    }

    return claimFormStepElementList.size();
  }

  /**
   * Accessor for the value list
   * 
   * @param index
   * @return Single instance of PromotionClaimFormStepElementBean from the value list
   */
  public PromotionClaimFormStepElementBean getClaimFormStepElementValueList( int index )
  {
    try
    {
      return (PromotionClaimFormStepElementBean)claimFormStepElementList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  /**
   * @return List of PromotionNotificationFormBean objects
   */
  public List getClaimFormNotificationList()
  {
    return claimFormNotificationList;
  }

  /**
   * @param claimFormNotificationList
   */
  public void setClaimFormNotificationList( List claimFormNotificationList )
  {
    this.claimFormNotificationList = claimFormNotificationList;
  }

  /**
   * Accessor for the number of PromotionNotificationFormBean objects in the list.
   * 
   * @return int
   */
  public int getClaimFormNotificationListCount()
  {
    if ( claimFormNotificationList == null )
    {
      return 0;
    }

    return claimFormNotificationList.size();
  }

  /**
   * Accessor for the value list
   * 
   * @param index
   * @return Single instance of PromotionNotificationFormBean from the value list
   */
  public PromotionNotificationFormBean getClaimFormNotificationList( int index )
  {
    try
    {
      return (PromotionNotificationFormBean)claimFormNotificationList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

}
