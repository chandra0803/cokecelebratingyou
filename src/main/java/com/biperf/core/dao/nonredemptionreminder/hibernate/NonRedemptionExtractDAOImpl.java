
package com.biperf.core.dao.nonredemptionreminder.hibernate;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.nonredemptionreminder.NonRedemptionExtractDAO;

/**
 * NonRedemptionExtractDAOImpl
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
 * <td>Krishna Mattam</td>
 * <td>June 02, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class NonRedemptionExtractDAOImpl extends BaseDAO implements NonRedemptionExtractDAO
{

  private DataSource dataSource;

  private JdbcTemplate jdbcTemplate;

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
    this.jdbcTemplate = new JdbcTemplate( dataSource );
  }

  /**
   * Calls the stored procedure to get an extract for Non redemption PAX info
   * 
   * @param promotionId
   * @param envName
   * @return Map result set
   */
  public Map nonRedemptionExtract( Long promotionId, String envName )
  {
    CallPrcNonRedemptionExtract callPrcNonRedemptionExtract = new CallPrcNonRedemptionExtract( dataSource );
    return callPrcNonRedemptionExtract.executeProcedure( promotionId, envName );
  }

}
