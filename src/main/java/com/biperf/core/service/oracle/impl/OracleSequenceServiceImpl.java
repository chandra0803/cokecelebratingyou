
package com.biperf.core.service.oracle.impl;

import com.biperf.core.dao.oracle.OracleSequenceDAO;
import com.biperf.core.service.oracle.OracleSequenceService;

/**
 * OracleSequenceServiceImpl.
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
public class OracleSequenceServiceImpl implements OracleSequenceService
{
  private OracleSequenceDAO oracleSequenceDAO;

  /**
   * @param oracleSequenceDAO
   */
  public void setOracleSequenceDAO( OracleSequenceDAO oracleSequenceDAO )
  {
    this.oracleSequenceDAO = oracleSequenceDAO;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.oracle.OracleSequenceService#getOracleSequenceNextValue(java.lang.String)
   * @param seqName
   * @return the next val on the sequence number object on Oracle
   */
  public long getOracleSequenceNextValue( String seqName )

  {
    return this.oracleSequenceDAO.getOracleSequenceNextValue( seqName );
  }

}
