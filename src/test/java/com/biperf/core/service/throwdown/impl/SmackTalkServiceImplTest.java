
package com.biperf.core.service.throwdown.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jmock.Mock;

import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.throwdown.SmackTalkDAO;
import com.biperf.core.domain.AuditCreateInfo;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Match;
import com.biperf.core.domain.promotion.SmackTalkComment;
import com.biperf.core.domain.promotion.SmackTalkLike;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.participant.ParticipantAssociationRequest;

public class SmackTalkServiceImplTest extends BaseServiceTest
{
  SmackTalkServiceImpl smackTalkServiceImpl = new SmackTalkServiceImpl();

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public SmackTalkServiceImplTest( String test )
  {
    super( test );
  }

  /** mockSmackTalkDAO */
  private Mock mockSmackTalkDAO = null;

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    super.setUp();
    mockSmackTalkDAO = new Mock( SmackTalkDAO.class );
    smackTalkServiceImpl.setSmackTalkDAO( (SmackTalkDAO)mockSmackTalkDAO.proxy() );
  }

  public static SmackTalkComment buildSmackTalkPost()
  {
    String uniqueString = getUniqueString();

    Match match = new Match();
    match.setId( new Long( 1 ) );
    // Get the test SmackTalkComment
    SmackTalkComment smackTalkPost = new SmackTalkComment();
    Participant pax = ParticipantDAOImplTest.buildUniqueParticipant( uniqueString );
    smackTalkPost.setUser( pax );
    smackTalkPost.setMatch( match );
    smackTalkPost.setComments( "Hi this is test post" );
    smackTalkPost.setParent( null );

    AuditCreateInfo auditCreateInfo = new AuditCreateInfo();
    auditCreateInfo.setCreatedBy( new Long( 5662 ) );
    auditCreateInfo.setDateCreated( new Timestamp( new Date().getTime() ) );
    smackTalkPost.setAuditCreateInfo( auditCreateInfo );
    smackTalkPost.setVersion( new Long( 1 ) );

    return smackTalkPost;
  }

  public static SmackTalkComment buildSmackTalkComment()
  {
    String uniqueString = getUniqueString();

    SmackTalkComment parentPost = buildSmackTalkPost();
    SmackTalkComment smackTalkComment = new SmackTalkComment();
    Participant pax = ParticipantDAOImplTest.buildUniqueParticipant( uniqueString );
    smackTalkComment.setUser( pax );
    smackTalkComment.setMatch( parentPost.getMatch() );
    smackTalkComment.setComments( "Hi this is test comment" );
    smackTalkComment.setParent( parentPost );

    AuditCreateInfo auditCreateInfo = new AuditCreateInfo();
    auditCreateInfo.setCreatedBy( new Long( 5662 ) );
    auditCreateInfo.setDateCreated( new Timestamp( new Date().getTime() ) );
    smackTalkComment.setAuditCreateInfo( auditCreateInfo );
    smackTalkComment.setVersion( new Long( 1 ) );

    return smackTalkComment;
  }

  public static SmackTalkLike buildSmackTalkLike()
  {
    String uniqueString = getUniqueString();

    SmackTalkComment comment = buildSmackTalkComment();
    SmackTalkLike saveSmackTalkLike = new SmackTalkLike();
    Participant pax = ParticipantDAOImplTest.buildUniqueParticipant( uniqueString );
    saveSmackTalkLike.setUser( pax );
    saveSmackTalkLike.setSmackTalkComment( comment );
    saveSmackTalkLike.setLiked( true );

    AuditCreateInfo auditCreateInfo = new AuditCreateInfo();
    auditCreateInfo.setCreatedBy( new Long( 5662 ) );
    auditCreateInfo.setDateCreated( new Timestamp( new Date().getTime() ) );
    saveSmackTalkLike.setAuditCreateInfo( auditCreateInfo );
    saveSmackTalkLike.setVersion( new Long( 1 ) );

    return saveSmackTalkLike;

  }

  /**
   * Test saveSmackTalkComment
   */
  public void testSaveSmackTalkComment() throws ServiceErrorException
  {
    SmackTalkComment post = buildSmackTalkPost();

    mockSmackTalkDAO.expects( once() ).method( "saveSmackTalkComment" ).with( same( post ) ).will( returnValue( post ) );
    SmackTalkComment smackTalkPostReturned = this.smackTalkServiceImpl.saveSmackTalkComment( post );
    assertEquals( "Actual saved smacktalk post is equal to what was expected", post, smackTalkPostReturned );
    mockSmackTalkDAO.verify();

    SmackTalkComment comment = buildSmackTalkComment();
    mockSmackTalkDAO.expects( once() ).method( "saveSmackTalkComment" ).with( same( comment ) ).will( returnValue( comment ) );
    SmackTalkComment smackTalkCommentReturned = this.smackTalkServiceImpl.saveSmackTalkComment( comment );
    assertEquals( "Actual saved smacktalk commment is equal to what was expected", comment, smackTalkCommentReturned );
    mockSmackTalkDAO.verify();

  }

  /**
   * Test saveSmackTalkLike
   */
  public void testSaveSmackTalkLike() throws ServiceErrorException
  {
    SmackTalkLike saveSmackTalkLike = buildSmackTalkLike();
    mockSmackTalkDAO.expects( once() ).method( "saveSmackTalkLike" ).with( same( saveSmackTalkLike ) ).will( returnValue( saveSmackTalkLike ) );
    SmackTalkLike smackTalkLikeReturned = this.smackTalkServiceImpl.saveSmackTalkLike( saveSmackTalkLike );
    assertEquals( "Actual saved smacktalk like equal to what was expected", saveSmackTalkLike, smackTalkLikeReturned );
    mockSmackTalkDAO.verify();

  }

  /**
   * Test getUserLikesByComments
   */
  public void testGetUserLikesByComments() throws ServiceErrorException
  {
    SmackTalkComment comment = buildSmackTalkComment();

    List<SmackTalkLike> expectedSmackTalkLikeList = new ArrayList<SmackTalkLike>();
    SmackTalkLike like1 = buildSmackTalkLike();
    SmackTalkLike like2 = buildSmackTalkLike();
    expectedSmackTalkLikeList.add( like1 );
    expectedSmackTalkLikeList.add( like2 );

    mockSmackTalkDAO.expects( once() ).method( "getUserLikesByComments" ).will( returnValue( expectedSmackTalkLikeList ) );
    List<SmackTalkLike> actualList = smackTalkServiceImpl.getUserLikesByComments( comment.getId() );

    assertTrue( "Actual set contains expected set for getAll.", actualList.containsAll( expectedSmackTalkLikeList ) );
    mockSmackTalkDAO.verify();

  }

  /**
   * Test getSmackTalkPostsByMatch
   */
  public void testGetSmackTalkPostsByMatch() throws ServiceErrorException
  {
    List<SmackTalkComment> expectedSmackTalkPostList = new ArrayList<SmackTalkComment>();
    SmackTalkComment post1 = buildSmackTalkPost();
    SmackTalkComment post2 = buildSmackTalkPost();
    Match match = post1.getMatch();
    expectedSmackTalkPostList.add( post1 );
    expectedSmackTalkPostList.add( post2 );

    mockSmackTalkDAO.expects( once() ).method( "getSmackTalkPostsByMatch" ).will( returnValue( expectedSmackTalkPostList ) );
    List<SmackTalkComment> actualList = smackTalkServiceImpl.getSmackTalkPostsByMatch( match.getId() );

    assertTrue( "Actual set contains expected set for getAll.", actualList.containsAll( expectedSmackTalkPostList ) );
    mockSmackTalkDAO.verify();

  }

  /**
   * Test getSmackTalkPostsByMatch
   */
  public void testGetUserCommentsBySmackTalkPost() throws ServiceErrorException
  {
    List<SmackTalkComment> expectedSmackTalkCommentList = new ArrayList<SmackTalkComment>();
    SmackTalkComment post = buildSmackTalkPost();
    SmackTalkComment comment1 = buildSmackTalkComment();
    SmackTalkComment comment2 = buildSmackTalkComment();
    expectedSmackTalkCommentList.add( comment1 );
    expectedSmackTalkCommentList.add( comment2 );

    mockSmackTalkDAO.expects( once() ).method( "getUserCommentsBySmackTalkPost" ).will( returnValue( expectedSmackTalkCommentList ) );
    List<SmackTalkComment> actualList = smackTalkServiceImpl.getUserCommentsBySmackTalkPost( post.getId() );

    assertTrue( "Actual set contains expected set for getAll.", actualList.containsAll( expectedSmackTalkCommentList ) );
    mockSmackTalkDAO.verify();

  }

  /**
   * Test getLikeCountBySmackTalk
   */
  public void testGetLikeCountBySmackTalk() throws ServiceErrorException
  {
    List<SmackTalkLike> expectedSmackTalkLikeList = new ArrayList<SmackTalkLike>();
    SmackTalkLike like1 = buildSmackTalkLike();
    SmackTalkLike like2 = buildSmackTalkLike();
    SmackTalkComment comment = like1.getSmackTalkComment();
    expectedSmackTalkLikeList.add( like1 );
    expectedSmackTalkLikeList.add( like2 );

    mockSmackTalkDAO.expects( once() ).method( "getLikeCountBySmackTalk" ).will( returnValue( (long)expectedSmackTalkLikeList.size() ) );
    long expectedLikeCount = smackTalkServiceImpl.getLikeCountBySmackTalk( comment.getId() );

    assertTrue( "Size of like count is equal to two ", expectedLikeCount == 2 );
    mockSmackTalkDAO.verify();

  }

  /**
   * Test isCurrentUserLikedSmackTalk
   */
  public void testIsCurrentUserLikedSmackTalk() throws ServiceErrorException
  {
    SmackTalkLike like = buildSmackTalkLike();
    Participant pax = like.getUser();
    SmackTalkComment comment = like.getSmackTalkComment();

    mockSmackTalkDAO.expects( once() ).method( "isCurrentUserLikedSmackTalk" ).will( returnValue( like.isLiked() ) );
    boolean liked = smackTalkServiceImpl.isCurrentUserLikedSmackTalk( comment.getId(), pax.getId() );

    assertTrue( "Current User likes the post", liked );
    assertEquals( true, liked );
    mockSmackTalkDAO.verify();
  }

  /**
   * Test getSmackTalkPostsForMatch
   */
  public void testGetSmackTalkPostsForMatch() throws ServiceErrorException
  {
    SmackTalkComment post1 = buildSmackTalkPost();
    SmackTalkComment post2 = buildSmackTalkPost();

    List<SmackTalkComment> expectedSmackTalkPostList = new ArrayList<SmackTalkComment>();
    expectedSmackTalkPostList.add( post1 );
    expectedSmackTalkPostList.add( post2 );
    Long[] matchIds = new Long[1];
    matchIds[0] = post1.getMatch().getId();

    mockSmackTalkDAO.expects( once() ).method( "getSmackTalkPostsForMatch" ).will( returnValue( expectedSmackTalkPostList ) );
    List<SmackTalkComment> actualList = smackTalkServiceImpl.getSmackTalkPostsForMatch( matchIds );

    assertTrue( "Actual set contains expected set for getAll.", actualList.containsAll( expectedSmackTalkPostList ) );
    mockSmackTalkDAO.verify();
  }

  /**
   * Test getUserCommentsForSmackTalkPosts
   */
  public void testGetUserCommentsForSmackTalkPosts() throws ServiceErrorException
  {
    SmackTalkComment post = buildSmackTalkPost();

    SmackTalkComment comment1 = buildSmackTalkComment();
    SmackTalkComment comment2 = buildSmackTalkComment();
    List<SmackTalkComment> expectedSmackTalkCommentList = new ArrayList<SmackTalkComment>();
    expectedSmackTalkCommentList.add( comment1 );
    expectedSmackTalkCommentList.add( comment2 );
    Long[] smackTalkIds = new Long[1];
    smackTalkIds[0] = post.getId();

    mockSmackTalkDAO.expects( once() ).method( "getUserCommentsForSmackTalkPosts" ).will( returnValue( expectedSmackTalkCommentList ) );
    List<SmackTalkComment> actualList = smackTalkServiceImpl.getUserCommentsForSmackTalkPosts( smackTalkIds );

    assertTrue( "Actual set contains expected set for getAll.", actualList.containsAll( expectedSmackTalkCommentList ) );
    mockSmackTalkDAO.verify();

  }

  /**
   * Test getUserLikesForComments
   */
  public void testGetUserLikesForComments() throws ServiceErrorException
  {
    SmackTalkComment comment = buildSmackTalkComment();

    List<SmackTalkLike> expectedSmackTalkLikeList = new ArrayList<SmackTalkLike>();
    SmackTalkLike like1 = buildSmackTalkLike();
    SmackTalkLike like2 = buildSmackTalkLike();
    expectedSmackTalkLikeList.add( like1 );
    expectedSmackTalkLikeList.add( like2 );

    Long[] smackTalkIds = new Long[1];
    smackTalkIds[0] = comment.getId();

    mockSmackTalkDAO.expects( once() ).method( "getUserLikesForComments" ).will( returnValue( expectedSmackTalkLikeList ) );
    List<SmackTalkLike> actualList = smackTalkServiceImpl.getUserLikesForComments( smackTalkIds );

    assertTrue( "Actual set contains expected set for getAll.", actualList.containsAll( expectedSmackTalkLikeList ) );
    mockSmackTalkDAO.verify();
  }

  /**
   * Test getLikedPaxListBySmackTalkId
   */
  public void testGetLikedPaxListBySmackTalkId() throws ServiceErrorException
  {
    List<SmackTalkLike> expectedSmackTalkLikeList = new ArrayList<SmackTalkLike>();
    SmackTalkLike like1 = buildSmackTalkLike();
    SmackTalkLike like2 = buildSmackTalkLike();
    expectedSmackTalkLikeList.add( like1 );
    expectedSmackTalkLikeList.add( like2 );

    SmackTalkComment comment = like1.getSmackTalkComment();
    AssociationRequestCollection associationRequests = new AssociationRequestCollection();
    associationRequests.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.PARTICIPANT ) );

    mockSmackTalkDAO.expects( once() ).method( "getLikedPaxListBySmackTalkId" ).will( returnValue( expectedSmackTalkLikeList ) );
    List<SmackTalkLike> actualList = smackTalkServiceImpl.getLikedPaxListBySmackTalkId( comment.getId(), associationRequests, 1 );

    assertTrue( "Actual set contains expected set for getAll.", actualList.containsAll( expectedSmackTalkLikeList ) );
    mockSmackTalkDAO.verify();

  }

  // /**
  // * Test getLikedPaxCount
  // */
  // public void testGetLikedPaxCount() throws ServiceErrorException
  // {
  // List<SmackTalkLike> expectedSmackTalkLikeList = new ArrayList<SmackTalkLike>();
  // SmackTalkLike like1 = buildSmackTalkLike();
  // SmackTalkLike like2 = buildSmackTalkLike();
  // SmackTalkComment comment = like1.getSmackTalkComment();
  // expectedSmackTalkLikeList.add( like1 );
  // expectedSmackTalkLikeList.add( like2 );
  //
  // mockSmackTalkDAO.expects( once() ).method( "getLikedPaxCount" ).will( returnValue(
  // expectedSmackTalkLikeList.size()) );
  // long expectedLikeCount = smackTalkServiceImpl.getLikedPaxCount( comment.getId() );
  //
  // assertTrue( "Size of like count is equal to two ", expectedLikeCount==2 );
  // mockSmackTalkDAO.verify();
  // }

  /**
   * Test hideSmackTalkComment
   */
  public void testHideSmackTalkComment() throws ServiceErrorException
  {
    SmackTalkComment comment = buildSmackTalkComment();
    comment.setIsHidden( true );
    mockSmackTalkDAO.expects( once() ).method( "saveSmackTalkComment" ).with( same( comment ) ).will( returnValue( comment ) );
    SmackTalkComment smackTalkCommentReturned = this.smackTalkServiceImpl.saveSmackTalkComment( comment );
    assertTrue( "Current User likes the post", smackTalkCommentReturned.getIsHidden() );
    assertEquals( true, smackTalkCommentReturned.getIsHidden() );
    mockSmackTalkDAO.verify();
  }

}
