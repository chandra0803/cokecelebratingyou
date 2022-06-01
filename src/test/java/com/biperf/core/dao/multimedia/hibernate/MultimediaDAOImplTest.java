/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/multimedia/hibernate/MultimediaDAOImplTest.java,v $
 */

package com.biperf.core.dao.multimedia.hibernate;

import java.util.List;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.multimedia.MultimediaDAO;
import com.biperf.core.domain.multimedia.ECard;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * MultimediaDAOImplTest.
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
 * <td>zahler</td>
 * <td>Oct 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class MultimediaDAOImplTest extends BaseDAOTest
{
  /**
   * Test getAllActiveCardImages
   */
  public void testGetAllActiveECards()
  {
    List allActiveECards = getMultimediaDAO().getAllActiveECards();

    assertFalse( "No ECards in the Database.  Should always be loaded.", allActiveECards.isEmpty() );

  }

  /**
   * test Save and getECardById
   */
  public void testSaveAndGetECardById()
  {
    String uniqueString = String.valueOf( System.currentTimeMillis() % 8197178 );

    ECard ecard = buildECard( uniqueString );

    getMultimediaDAO().saveCard( ecard );

    flushAndClearSession();

    ECard expectedCard = (ECard)getMultimediaDAO().getCardById( ecard.getId() );

    assertEquals( ecard.getName(), expectedCard.getName() );
    assertEquals( ecard.getFlashName(), expectedCard.getFlashName() );

  }

  /**
   * @param uniqueString
   * @return ECard
   */
  /**
   * @param uniqueString
   * @return ECard
   */
  public static ECard buildECard( String uniqueString )
  {
    ECard eCard = new ECard();

    eCard.setName( "ECardName" + uniqueString );
    eCard.setActive( true );
    eCard.setSmallImageName( "smallImageName" + uniqueString );
    eCard.setLargeImageName( "largeImageName" + uniqueString );
    eCard.setFlashName( "flashName" + uniqueString );

    return eCard;
  }

  /**
   * Returns a {@link MultimediaDAO} object.
   * 
   * @return a {@link MultimediaDAO} object.
   */
  private static MultimediaDAO getMultimediaDAO()
  {
    return (MultimediaDAO)ApplicationContextFactory.getApplicationContext().getBean( MultimediaDAO.BEAN_NAME );
  }

}
