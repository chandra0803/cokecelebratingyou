/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/activity/SalesActivity.java,v $
 */

package com.biperf.core.domain.activity;

import com.biperf.core.domain.product.Product;
import com.biperf.core.utils.GuidUtils;

/**
 * SalesActivity.
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
 * <td>OPI Admin</td>
 * <td>Jul 12, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SalesActivity extends Activity
{
  private Product product;
  private boolean isCarryover;
  private boolean isSubmitter;

  // This is currently not persisted, it is only used to help with promotion engine calculations
  private SalesActivity originalActivity;

  /**
   * For a "claim product activity", quantity represents number of products sold.
   */
  private Long quantity;

  // ---------------------------------------------------------------------------
  // Constructors
  // ---------------------------------------------------------------------------
  public SalesActivity()
  {
    // empty constructor
  }

  public SalesActivity( String guid )
  {
    super( guid );
  }

  /**
   * @param product
   * @param quantity
   */
  public SalesActivity( Product product, Long quantity )
  {
    super( GuidUtils.generateGuid() );

    this.product = product;
    this.quantity = quantity;
  }

  /**
   * Construct an activity copying properties of the input sourceActivity, creating a transient
   * SalesActivity with a new guid.
   * 
   * @param sourceActivity
   */
  public SalesActivity( SalesActivity sourceActivity )
  {
    super( GuidUtils.generateGuid() );

    claim = sourceActivity.getClaim();
    isCarryover = sourceActivity.isCarryover();
    isPosted = sourceActivity.isPosted();
    node = sourceActivity.getNode();
    participant = sourceActivity.getParticipant();
    product = sourceActivity.getProduct();
    promotion = sourceActivity.getPromotion();
    quantity = sourceActivity.getQuantity();
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public Product getProduct()
  {
    return product;
  }

  public void setProduct( Product product )
  {
    this.product = product;
  }

  public Long getQuantity()
  {
    return quantity;
  }

  public void setQuantity( Long quantity )
  {
    this.quantity = quantity;
  }

  /**
   * @return value of isCarryover property
   */
  public boolean isCarryover()
  {
    return isCarryover;
  }

  /**
   * @param isCarryover value for isCarryover property
   */
  public void setCarryover( boolean isCarryover )
  {
    this.isCarryover = isCarryover;
  }

  /**
   * @return value of isSubmitter property
   */
  public boolean isSubmitter()
  {
    return isSubmitter;
  }

  /**
   * @param isSubmitter value for isSubmitter property
   */
  public void setSubmitter( boolean isSubmitter )
  {
    this.isSubmitter = isSubmitter;
  }

  public SalesActivity getOriginalActivity()
  {
    return originalActivity;
  }

  public void setOriginalActivity( SalesActivity originalActivity )
  {
    this.originalActivity = originalActivity;
  }

}
