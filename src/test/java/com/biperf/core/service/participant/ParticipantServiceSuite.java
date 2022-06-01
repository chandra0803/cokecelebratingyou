/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/participant/ParticipantServiceSuite.java,v $
 */

package com.biperf.core.service.participant;

import com.biperf.core.service.participant.impl.AudienceServiceImplTest;
import com.biperf.core.service.participant.impl.AutoCompleteServiceImplTest;
import com.biperf.core.service.participant.impl.CharacteristicServiceImplTest;
import com.biperf.core.service.participant.impl.ParticipantServiceImplTest;
import com.biperf.core.service.participant.impl.ProfileServiceImplTest;
import com.biperf.core.service.participant.impl.UserNodeHistoryServiceImplTest;
import com.biperf.core.service.participant.impl.UserServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Participant Service test suite for running all participant service tests out of container.
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
 * <td>Sathish</td>
 * <td>May 27, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ParticipantServiceSuite extends TestSuite
{

  /**
   * Service Test Suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.participant.ParticipantServiceSuite" );

    suite.addTestSuite( CharacteristicServiceImplTest.class );
    suite.addTestSuite( ParticipantServiceImplTest.class );
    suite.addTestSuite( ProfileServiceImplTest.class );
    suite.addTestSuite( UserServiceImplTest.class );
    suite.addTestSuite( AudienceServiceImplTest.class );
    suite.addTestSuite( UserNodeHistoryServiceImplTest.class );
    suite.addTestSuite( AutoCompleteServiceImplTest.class );

    return suite;
  }

}
