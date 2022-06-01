/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.promotion.engine;

import com.biperf.core.dao.product.hibernate.ProductCategoryDAOImplTest;
import com.biperf.core.dao.product.hibernate.ProductDAOImplTest;
import com.biperf.core.dao.promotion.hibernate.PromotionPayoutDAOImplTest;
import com.biperf.core.domain.product.Product;
import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.domain.promotion.PromotionPayout;
import com.biperf.core.service.BaseServiceTest;

/**
 * CrossSellPayoutStrategyTest.
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
 * <td>wadzinsk</td>
 * <td>Aug 4, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SalesPayoutStrategyUtilTest extends BaseServiceTest
{

  Product product1;

  Product product3;

  ProductCategory productCategory3;
  ProductCategory productSubcategory2OfCat3;

  Product product1WithS2OfCat3;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.BaseServiceTest#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    super.setUp();

    // Build products for promotion and claim
    ProductCategory productCategory1And2 = ProductCategoryDAOImplTest.buildProductCategory( "pc1and2" );
    product1 = ProductDAOImplTest.buildStaticProductDomainObject( "p1", productCategory1And2 );

    productCategory3 = ProductCategoryDAOImplTest.buildProductCategory( "pc3" );
    product3 = ProductDAOImplTest.buildStaticProductDomainObject( "p3", productCategory3 );

    ProductCategory productSubcategory1OfCat3 = ProductCategoryDAOImplTest.buildProductCategory( "ps1ofc3" );
    productCategory3.addSubcategory( productSubcategory1OfCat3 );
    productSubcategory2OfCat3 = ProductCategoryDAOImplTest.buildProductCategory( "ps2ofc3" );
    productCategory3.addSubcategory( productSubcategory2OfCat3 );

    product1WithS2OfCat3 = ProductDAOImplTest.buildStaticProductDomainObject( "p1", productSubcategory2OfCat3 );

  }

  public void testIsProductSoldApplicableToPromotionPayoutWithProductMatchSuccess()
  {
    PromotionPayout promotionPayout = PromotionPayoutDAOImplTest.buildPromotionPayout( product1 );
    assertTrue( SalesPayoutStrategyUtil.isProductSoldApplicableToPromotionPayout( product1, promotionPayout ) );
  }

  public void testIsProductSoldApplicableToPromotionPayoutWithProductMatchFailure()
  {
    PromotionPayout promotionPayout = PromotionPayoutDAOImplTest.buildPromotionPayout( product1 );
    assertFalse( SalesPayoutStrategyUtil.isProductSoldApplicableToPromotionPayout( product3, promotionPayout ) );
  }

  public void testIsProductSoldApplicableToPromotionPayoutWithProductCategoryMatchSuccess()
  {
    PromotionPayout promotionPayout = PromotionPayoutDAOImplTest.buildPromotionPayout( productCategory3 );
    assertTrue( SalesPayoutStrategyUtil.isProductSoldApplicableToPromotionPayout( product3, promotionPayout ) );
  }

  public void testIsProductSoldApplicableToPromotionPayoutWithProductCategoryMatchFailure()
  {
    PromotionPayout promotionPayout = PromotionPayoutDAOImplTest.buildPromotionPayout( productCategory3 );
    assertFalse( SalesPayoutStrategyUtil.isProductSoldApplicableToPromotionPayout( product1, promotionPayout ) );
  }

  public void testIsProductSoldApplicableToPromotionPayoutWithSubcategoryMatchSuccess()
  {
    PromotionPayout promotionPayout = PromotionPayoutDAOImplTest.buildPromotionPayout( productCategory3 );
    assertTrue( SalesPayoutStrategyUtil.isProductSoldApplicableToPromotionPayout( product1WithS2OfCat3, promotionPayout ) );
  }

  public void testIsProductSoldApplicableToPromotionPayoutWithDoesNotMatchParentCategory()
  {
    PromotionPayout promotionPayout = PromotionPayoutDAOImplTest.buildPromotionPayout( productSubcategory2OfCat3 );
    assertFalse( SalesPayoutStrategyUtil.isProductSoldApplicableToPromotionPayout( product3, promotionPayout ) );
  }
}
