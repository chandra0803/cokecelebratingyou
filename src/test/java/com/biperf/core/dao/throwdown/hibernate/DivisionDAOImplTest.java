/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/throwdown/hibernate/DivisionDAOImplTest.java,v $
 */

package com.biperf.core.dao.throwdown.hibernate;

import java.math.BigDecimal;
import java.util.List;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.dao.throwdown.DivisionDAO;
import com.biperf.core.domain.promotion.Division;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.utils.ApplicationContextFactory;

public class DivisionDAOImplTest extends BaseDAOTest
{

  /***  Junit for getDivision( Long id ) and also save( Division division ).
   ***  This method check to see if the correct division is fetched.
   ***/
  public void testDivisionsDAOMethods()
  {
    DivisionDAO divisionDAO = getDivisionDAO();
    PromotionDAO promoDAO = getPromotionDAO();

    // building a promotion
    ThrowdownPromotion tdPromo1 = new ThrowdownPromotion();
    tdPromo1 = PromotionDAOImplTest.buildThrowdownPromotion( "TD Promo13" );

    promoDAO.save( tdPromo1 );

    // For the above promotion creating a division
    Division division1 = new Division();
    division1 = buildDivision( "Division3" );

    division1.setPromotion( tdPromo1 );

    // saving the above division for the promotion
    divisionDAO.save( division1 );

    // creating a another division for same promotion as above.
    Division division2 = new Division();
    division2 = buildDivision( "Divsion3" );

    division2.setPromotion( tdPromo1 );

    // saving the above division for the promotion
    divisionDAO.save( division2 );

    // Test written to check method " public Division getDivision( Long id )"
    // Also includes the check for method "public Division save( Division division )"
    // passing division id and getting the associated division object.
    Division expectedDivision1 = divisionDAO.getDivision( division1.getId() );
    Division expectedDivision2 = divisionDAO.getDivision( division2.getId() );

    assertEquals( "Division is Fetched Correctly", expectedDivision1, division1 );
    assertEquals( "Division is Fetched Correctly", expectedDivision2, division2 );
    assertNotSame( "Division is not Same", expectedDivision1, division2 );

    // Test written to check method "public List<Division> getDivisionsByPromotionId( Long
    // promotionId )"
    // passing the promotion id and getting the division list
    List<Division> listDivision = divisionDAO.getDivisionsByPromotionId( tdPromo1.getId() );

    // checking to see if the number of division fetched for a promotion are correct.
    assertFalse( "Size of Division Should Be two or more.", listDivision.size() <= 1 );
    assertTrue( "Size of Division is two or more.", listDivision.size() >= 2 );

    // Test written to check method " public void delete( Long divisionId )"
    // passing the division id to delete the division record.
    divisionDAO.delete( division2.getId() );

    Division expectedDeletedDivision2 = divisionDAO.getDivision( division2.getId() );
    List<Division> listAfterDeletionOfDivision = divisionDAO.getDivisionsByPromotionId( tdPromo1.getId() );
    assertTrue( "Division deleted successfully", expectedDeletedDivision2 == null );
    assertNotSame( "Second Confirmation that Division is deleted", expectedDeletedDivision2, division2 );
    assertTrue( "After Deletion of Division now the size for the given promotin is one or less", listAfterDeletionOfDivision.size() <= 1 );
    assertFalse( "After Deletion of Division now the size for the given promotin should be one or less", listAfterDeletionOfDivision.size() >= 2 );

  }

  /**
   * Builds a static division for testing.
   * 
   * @param uniqueString
   * @return division
   */
  public static Division buildDivision( String uniqueString )
  {
    Division division = new Division();

    division.setDivisionName( uniqueString );
    division.setMinimumQualifier( new BigDecimal( 10 ) );

    return division;
  }

  /**
   * Get the DivisionDAO.
   * 
   * @return DivisionDAO
   */
  private static DivisionDAO getDivisionDAO()
  {
    return (DivisionDAO)ApplicationContextFactory.getApplicationContext().getBean( "divisionDAO" );
  }

  /**
   * Get the PromotionDAO.
   * 
   * @return PromotionDAO
   */
  private static PromotionDAO getPromotionDAO()
  {
    return (PromotionDAO)ApplicationContextFactory.getApplicationContext().getBean( "promotionDAO" );
  }
}
