/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/claim/ClaimProduct.java,v $
 */

package com.biperf.core.domain.claim;

import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.product.Product;

/**
 * ClaimProduct holds the information for the association between a claim and a product assigned to
 * that claim. This object is an association and the mappings can be found in both the Claim and
 * Product hbm files.
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
 * <td>crosenquest</td>
 * <td>Jun 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class ClaimProduct extends ClaimItem
{

  private static final int COMMENTS_LENGTH = 200;
  private Product product;
  private int quantity = 0;
  private Set claimProductCharacteristics = new LinkedHashSet();

  private String comments = "";
  private boolean primary = false;

  public ClaimProduct()
  {
    // this.serialId = String.valueOf(System.currentTimeMillis() % new Random().nextInt());
  }

  public ClaimProduct( Claim claim, Product product, int quantity )
  {
    this();
    this.setClaim( claim );
    this.setProduct( product );
    this.setQuantity( quantity );
  }

  public ClaimProduct( Claim claim, Product product )
  {
    this();
    this.setClaim( claim );
    this.setProduct( product );
  }

  public ClaimProduct( Product product, int quantity )
  {
    this();
    this.setProduct( product );
    this.setQuantity( quantity );
  }

  public ClaimProduct( Product product, int quantity, Set claimProductCharacteristics )
  {
    this();
    this.setProduct( product );
    this.setQuantity( quantity );
    this.setClaimProductCharacteristics( claimProductCharacteristics );
  }

  public void setProduct( Product product )
  {
    this.product = product;
  }

  public void setQuantity( int quantity )
  {
    this.quantity = quantity;
  }

  public Product getProduct()
  {
    return this.product;
  }

  public int getQuantity()
  {
    return this.quantity;
  }

  public Set getClaimProductCharacteristics()
  {
    return this.claimProductCharacteristics;
  }

  public void setClaimProductCharacteristics( Set claimProductCharacteristics )
  {
    this.claimProductCharacteristics = claimProductCharacteristics;
  }

  public void addClaimProductCharacteristics( ClaimProductCharacteristic claimProductCharacteristic )
  {
    claimProductCharacteristic.setClaimProduct( this );
    this.claimProductCharacteristics.add( claimProductCharacteristic );
  }

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof ClaimProduct ) )
    {
      return false;
    }

    ClaimProduct claimProduct = (ClaimProduct)object;

    if ( getSerialId() != null ? !getSerialId().equals( claimProduct.getSerialId() ) : claimProduct.getSerialId() != null )
    {
      return false;
    }

    return true;

  } // end equals

  public int hashCode()
  {
    int result = 0;

    result += this.getSerialId() != null ? this.getSerialId().hashCode() : 13;

    return result;
  }

  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "ClaimProduct [" );
    buf.append( "{id=" + super.getId() + "}, " );
    buf.append( "{serialId=" + this.getSerialId() + "}, " );
    buf.append( "{quantity=" + this.getQuantity() + "}, " );
    buf.append( "{claim=" + this.getClaim() + "}, " );
    buf.append( "{product=" + this.getProduct() + "}, " );
    buf.append( "{comments=" + this.getComments() + "}, " );
    buf.append( "{primary=" + this.isPrimary() + "}, " );
    buf.append( "{claimProductCharacteristics=" + this.getClaimProductCharacteristics() + "}, " );
    buf.append( "]" );
    return buf.toString();
  }

  public String getComments()
  {
    return comments;
  }

  public String getCommentsTrimmed()
  {
    if ( comments != null && comments.length() > COMMENTS_LENGTH )
    {
      return comments.substring( 0, COMMENTS_LENGTH );
    }
    return comments;
  }

  public boolean isShowCommentsPopup()
  {
    if ( comments != null && comments.length() > COMMENTS_LENGTH )
    {
      return true;
    }
    return false;
  }

  public void setComments( String comments )
  {
    this.comments = comments;
  }

  public boolean isPrimary()
  {
    return primary;
  }

  public void setPrimary( boolean primary )
  {
    this.primary = primary;
  }

}
