
package com.biperf.core.service.product;

import com.biperf.core.domain.product.Product;
import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.service.BaseAssociationRequest;

/**
 * ProductAssociationRequest.
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
 * <td>gadapa</td>
 * <td>Jan 24, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class ProductAssociationRequest extends BaseAssociationRequest
{
  protected int hydrateLevel = 0;

  public static final int ALL = 1;
  public static final int PRODUCT_CATEGORY = 2;
  public static final int PARENT_PRODUCT_CATEGORY = 3;

  /**
   * Constructor with hydrateLevel as arg
   * 
   * @param hydrateLevel
   */
  public ProductAssociationRequest( int hydrateLevel )
  {
    super();
    this.hydrateLevel = hydrateLevel;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.AssociationRequest#execute(Object)
   * @param domainObject
   */
  public void execute( Object domainObject )
  {
    Product product = (Product)domainObject;

    switch ( hydrateLevel )
    {
      case ALL:
        hydrateAll( product );
        break;

      case PRODUCT_CATEGORY:
        hydrateProductCategory( product );
        break;

      case PARENT_PRODUCT_CATEGORY:
        hydrateProductCategory( product );
        hydrateParentProductCategory( product.getProductCategory() );
        break;

      default:
        break;
    }
  }

  private void hydrateAll( Product product )
  {
    initialize( product.getProductCategory() );
    initialize( product.getProductCategory().getParentProductCategory() );
  }

  private void hydrateProductCategory( Product product )
  {
    initialize( product.getProductCategory() );
  }

  private void hydrateParentProductCategory( ProductCategory productCategory )
  {
    initialize( productCategory.getParentProductCategory() );
  }

}
