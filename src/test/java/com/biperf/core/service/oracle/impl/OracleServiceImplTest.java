
package com.biperf.core.service.oracle.impl;

import org.easymock.EasyMock;

import com.biperf.core.dao.oracle.OracleSequenceDAO;
import com.biperf.core.service.BaseServiceTest;

/**
 * OracleServiceImplTest.
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
 * <td>Tammy Cheng</td>
 * <td>Dec 30, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class OracleServiceImplTest extends BaseServiceTest
{
  private OracleSequenceServiceImpl classUnderTest;

  private OracleSequenceDAO oracleSequenceDAOMock;

  public void setUp() throws Exception
  {
    super.setUp();

    oracleSequenceDAOMock = EasyMock.createMock( OracleSequenceDAO.class );

    classUnderTest = new OracleSequenceServiceImpl();
    classUnderTest.setOracleSequenceDAO( oracleSequenceDAOMock );
  }

  public void testGetOracleSequence()
  {
    long nextVal = 1L;

    EasyMock.expect( oracleSequenceDAOMock.getOracleSequenceNextValue( "claim_nbr_sq" ) ).andReturn( nextVal );
    EasyMock.replay( oracleSequenceDAOMock );
  }
}
