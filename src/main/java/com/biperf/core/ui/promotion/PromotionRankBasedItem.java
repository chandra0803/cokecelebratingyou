/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.promotion;

import com.objectpartners.cms.util.ContentReaderManager;

/**
 * PromotionRankBasedItemBean.
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
 * <td>Ashok</td>
 * <td>March 13, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionRankBasedItem
{
  private Long id;
  private String cmKeyForElementLabel;
  private String cmAssetCode;

  /**
   * empty constructor
   */
  public PromotionRankBasedItem()
  {
    // empty constructor
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  public void setCmAssetCode( String cmAssetCode )
  {
    this.cmAssetCode = cmAssetCode;
  }

  public String getCmKeyForElementLabel()
  {
    return cmKeyForElementLabel;
  }

  public void setCmKeyForElementLabel( String cmKeyForElementLabel )
  {
    this.cmKeyForElementLabel = cmKeyForElementLabel;
  }

  public String getDisplayLabel()
  {
    return ContentReaderManager.getText( cmAssetCode, cmKeyForElementLabel );
  }

}
