/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/participant/ParticipantDAOSuite.java,v $
 */

package com.biperf.core.dao.participant;

import com.biperf.core.dao.hibernate.CharacteristicDAOImplTest;
import com.biperf.core.dao.participant.hibernate.AudienceDAOImplTest;
import com.biperf.core.dao.participant.hibernate.ParticipantDAOImplTest;
import com.biperf.core.dao.participant.hibernate.UserDAOImplAddressTest;
import com.biperf.core.dao.participant.hibernate.UserDAOImplCharacteristicTest;
import com.biperf.core.dao.participant.hibernate.UserDAOImplEmailAddressTest;
import com.biperf.core.dao.participant.hibernate.UserDAOImplPhoneTest;
import com.biperf.core.dao.participant.hibernate.UserDAOImplTest;
import com.biperf.core.dao.participant.hibernate.UserNodeHistoryDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * ParticipantDAOSuite is a container for running all Participant DAO tests out of container.
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
public class ParticipantDAOSuite extends TestSuite
{

  /**
   * suite to run all Hierarchy DAO Tests.
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.participant.ParticipantDAOSuite" );

    suite.addTestSuite( AudienceDAOImplTest.class );
    suite.addTestSuite( CharacteristicDAOImplTest.class );
    suite.addTestSuite( ParticipantDAOImplTest.class );
    suite.addTestSuite( UserDAOImplTest.class );
    suite.addTestSuite( UserDAOImplAddressTest.class );
    suite.addTestSuite( UserDAOImplCharacteristicTest.class );
    suite.addTestSuite( UserDAOImplPhoneTest.class );
    suite.addTestSuite( UserDAOImplEmailAddressTest.class );
    suite.addTestSuite( UserNodeHistoryDAOImplTest.class );
    suite.addTestSuite( ParticipantDAOImplTest.class );

    return suite;
  }

}
