
package com.biperf.integration.service.indexing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.biperf.core.indexing.BIElasticSearchIndexerService;
import com.biperf.core.indexing.BIElasticSearchSearcher;
import com.biperf.core.indexing.BIIndex;
import com.biperf.core.indexing.BIIndexType;
import com.biperf.core.service.participant.AutoCompleteService;
import com.biperf.core.utils.Environment;
import com.biperf.core.value.indexing.ESResultWrapper;
import com.biperf.core.value.indexing.PaxIndexMappingEnum;
import com.biperf.core.value.indexing.Searchable;
import com.biperf.core.value.participant.PaxIndexData;
import com.biperf.core.value.participant.PaxIndexSearchCriteria;
import com.biperf.integration.BaseIntegrationTest;

/**
 *   This   integration test depends on Elastic search instance  to be running with default product data  to   automate the  test
 */

@Ignore
public class BIElasticSearchSearcherImplIntegTest extends BaseIntegrationTest
{
  
  // These are currently failing because they cannot connect
  // Retry / timeout is a 2 minute delay. Skipping tests for now to avoid delay.

  private @Autowired BIElasticSearchSearcher searcher;
  private @Autowired BIElasticSearchIndexerService indexer;
  private @Autowired AutoCompleteService autoCompleteService;
  boolean indexingDone = false;
  @Autowired
  BIIndex biIndex;

  @Ignore
  @BeforeClass
  public static void beforeClass()
  {
    System.setProperty( "environment.name", Environment.ENV_DEV );
  }

  @Ignore
  @Test
  /**
   * ES  should be running  and data set up should be exist  for this test.  
   */
  public void deleteById() throws Exception
  {
    boolean deleteById = indexer.deleteByIds( BIIndexType.PARTICIPANT, Arrays.asList( 5583L ) );
    assertTrue( deleteById );
  }

  @SuppressWarnings( "unused" )
  @Test
  @Ignore
  /**
   * ES  should be running  and data set up should be exist  for this test.  
   */
  public void deleteByQuery() throws Exception
  {
    Searchable paxdata = new PaxIndexData( 5584L, null, null );

  }

  @Ignore
  @Test
  public void searchStartWithPageFirstAndContinueTillLastPageThenVerifyPaginations() throws Exception
  {

    PaxIndexSearchCriteria criteria = new PaxIndexSearchCriteria();
    criteria.setName( "da" );

    long totalReceived = 0;
    boolean testNotCompleted = true;
    ESResultWrapper result = null;

    while ( testNotCompleted )
    {
      result = searcher.search( criteria, BIIndexType.PARTICIPANT );
      totalReceived = totalReceived + result.getSearchRecords().size();
      if ( result.getHits() == totalReceived )
      {
        testNotCompleted = false;
        break;
      }
      criteria.setFromIndex( totalReceived );
    }

    assertEquals( result.getHits(), totalReceived );
    assertFalse( testNotCompleted );
  }

  @Ignore
  @Test
  public void searchForSpecificFields() throws Exception
  {

    PaxIndexSearchCriteria criteria = new PaxIndexSearchCriteria();
    criteria.setName( "da" );
    criteria.setFields( Arrays.asList( PaxIndexMappingEnum.FIRST_NAME, PaxIndexMappingEnum.LAST_NAME ) );

    ESResultWrapper result = searcher.search( criteria, BIIndexType.PARTICIPANT );
    List<Searchable> searchRecords = result.getSearchRecords();

    assertTrue( searchRecords.size() > 0 );
    for ( Searchable searchable : searchRecords )
    {
      PaxIndexData pax = (PaxIndexData)searchable;

      assertNotNull( pax.getFirstname() );
      assertNotNull( pax.getLastname() );

      assertNull( pax.getUserId() );

    }

  }

  @Ignore
  @Before
  public void beforeEachTest() throws Exception
  {
    super.beforeEachTest();
    if ( !indexingDone )
    {
      biIndex.setName( Environment.getEnvironment() );
      autoCompleteService.indexAllActiveParticipants();
      indexingDone = true;
    }

  }

  @Ignore
  @After
  public void afterEachTest() throws Exception
  {
    super.afterEachTest();
  }

}
