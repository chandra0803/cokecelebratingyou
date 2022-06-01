
package com.biperf.core.dao.purl.hibernate;

import java.util.List;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.promotion.CelebrationManagerMessageDAO;
import com.biperf.core.domain.promotion.CelebrationManagerMessage;

public class CelebrationManagerMessageDAOImplTest extends BaseDAOTest
{
  public void testGetCelebrationManagerByPromotion()
  {
    Long promotionId = new Long( 250 );
    List<CelebrationManagerMessage> messageList = getCelebrationManagerMessageDAO().getCelebrationManagerByPromotion( promotionId );
    // System.out.println("messageList: " + messageList.get( 0 ).getId());
    // assertTrue( messageList.size() > 0 );
  }

  private static CelebrationManagerMessageDAO getCelebrationManagerMessageDAO()
  {
    return (CelebrationManagerMessageDAO)getDAO( "celebrationManagerMessageDAO" );
  }
}
