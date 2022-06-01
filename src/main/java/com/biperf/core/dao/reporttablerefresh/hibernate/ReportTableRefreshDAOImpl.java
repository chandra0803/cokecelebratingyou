/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/reporttablerefresh/hibernate/ReportTableRefreshDAOImpl.java,v $
 */

package com.biperf.core.dao.reporttablerefresh.hibernate;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.reporttablerefresh.ReportTableRefreshDAO;

/**
 * ClaimDAOImpl implements the ClaimDAO interface to satisfy the requirements for processing
 * claimForm submissions.
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
 * <td>crosenquest</td>
 * <td>Jun 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class ReportTableRefreshDAOImpl extends BaseDAO implements ReportTableRefreshDAO
{
  private DataSource dataSource;

  private JdbcTemplate jdbcTemplate;

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
    this.jdbcTemplate = new JdbcTemplate( dataSource );
  }

  /**
   * Calls the stored procedure for syncing app users
   */
  public Map reportTableCoreRefresh( Long userId )
  {
    CallPrcCoreRptRefresh proc = new CallPrcCoreRptRefresh( dataSource );

    return proc.executeProcedure( userId );
  }

}
