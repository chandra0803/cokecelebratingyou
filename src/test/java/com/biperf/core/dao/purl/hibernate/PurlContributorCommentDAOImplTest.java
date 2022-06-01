
package com.biperf.core.dao.purl.hibernate;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.purl.PurlContributorCommentDAO;
import com.biperf.core.dao.purl.PurlContributorDAO;
import com.biperf.core.domain.enums.PurlContributorCommentStatus;
import com.biperf.core.domain.purl.PurlContributor;
import com.biperf.core.domain.purl.PurlContributorComment;

public class PurlContributorCommentDAOImplTest extends BaseDAOTest
{
  public static PurlContributorComment buildAndSaveUniquePurlContributorComment( String suffix )
  {
    PurlContributorComment purlContributorComment = new PurlContributorComment();
    purlContributorComment.setId( new Long( 1 ) );
    purlContributorComment.setComments( "Comments" + suffix );
    purlContributorComment.setStatus( PurlContributorCommentStatus.lookup( PurlContributorCommentStatus.ACTIVE ) );

    PurlContributor purlContributor = PurlContributorDAOImplTest.buildAndSaveUniquePaxPurlContributor( suffix );

    PurlContributor savedPurlContributor = getPurlContributorDAO().getPurlContributorById( purlContributor.getId() );
    savedPurlContributor.addComment( purlContributorComment );
    getPurlContributorDAO().save( savedPurlContributor );
    flushAndClearSession();

    return purlContributorComment;
  }

  public void testPostComment()
  {
    String uniqueString = getUniqueString();
    PurlContributorComment purlComment = buildAndSaveUniquePurlContributorComment( uniqueString );

    PurlContributorComment savedPurlComment = getPurlContributorCommentDAO().getPurlContributorCommentById( purlComment.getId() );
    assertNotNull( savedPurlComment );
    assertNotNull( savedPurlComment.getComments() );
  }

  private static PurlContributorDAO getPurlContributorDAO()
  {
    return (PurlContributorDAO)getDAO( PurlContributorDAO.BEAN_NAME );
  }

  private static PurlContributorCommentDAO getPurlContributorCommentDAO()
  {
    return (PurlContributorCommentDAO)getDAO( PurlContributorCommentDAO.BEAN_NAME );
  }

}
