
package com.biperf.core.service.journal.impl;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Mock;
import org.junit.Test;

import com.biperf.core.dao.journal.JournalDAO;
import com.biperf.core.domain.activity.SalesActivity;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.journal.ActivityJournal;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.utils.GuidUtils;

/**
 * JournalServiceImplTest.
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
 * <td>robinsra</td>
 * <td>Sep 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class JournalServiceImplTest extends BaseServiceTest
{

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public JournalServiceImplTest( String test )
  {
    super( test );
  }

  /** journalServiceImplementation */
  private JournalServiceImpl journalService = new JournalServiceImpl();

  /** mocks */
  private Mock mockJournalDAO = null;

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    super.setUp();
    mockJournalDAO = new Mock( JournalDAO.class );
    journalService.setJournalDAO( (JournalDAO)mockJournalDAO.proxy() );

  }

  /**
   * Test getting the Journal by id.
   */
  public void testGetJournalById()
  {
    // Get the test Journal.
    Journal journal = new Journal();
    journal.setId( new Long( 1 ) );
    journal.setComments( "This is a comment" );

    // CountryDAO expected to call getCountryById once with the CountryId which will return the
    // Country expected
    mockJournalDAO.expects( once() ).method( "getJournalById" ).with( same( journal.getId() ) ).will( returnValue( journal ) );

    journalService.getJournalById( journal.getId() );

    mockJournalDAO.verify();
  }

  /**
   * Test getting the Journal by id.
   */
  public void testGetJournalByClaimIdAndUserId()
  {
    // Get the test Journal.
    Journal journal = new Journal();
    journal.setId( new Long( 1 ) );
    journal.setComments( "This is a comment" );

    ActivityJournal activityJournal = new ActivityJournal();
    SalesActivity salesActivity = new SalesActivity( GuidUtils.generateGuid() );
    Participant participant = new Participant();
    participant.setId( new Long( 1 ) );

    salesActivity.setParticipant( participant );
    ProductClaim claim = new ProductClaim();

    claim.setId( new Long( "100" ) );
    salesActivity.setId( new Long( "101" ) );
    salesActivity.setClaim( claim );
    activityJournal.setActivity( salesActivity );
    activityJournal.setJournal( journal );

    List journalList = new ArrayList();
    journalList.add( new Journal() );

    mockJournalDAO.expects( once() ).method( "getJournalsByClaimIdAndUserId" ).with( eq( claim.getId() ), eq( salesActivity.getParticipant().getId() ) ).will( returnValue( journalList ) );

    List returnedJournalList = journalService.getJournalsByClaimIdAndUserId( new Long( 100 ), new Long( 1 ), null );

    assertTrue( returnedJournalList.size() == 1 );

    mockJournalDAO.verify();
  }

  @Test
  public void testGetJournalIdForReversedClaim()
  {
    // The method under test simply goes through to DAO to get a long
    final Long expectedResult = 1234L;

    mockJournalDAO.expects( once() ).method( "getJournalIdForReversedClaim" ).withAnyArguments().will( returnValue( expectedResult ) );

    Long actualResult = journalService.getJournalIdForReversedClaim( 9876L, 5432L, 123L );

    mockJournalDAO.verify();
    assertTrue( actualResult.equals( expectedResult ) );
  }
}