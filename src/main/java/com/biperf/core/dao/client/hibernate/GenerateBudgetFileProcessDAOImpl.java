
package com.biperf.core.dao.client.hibernate;

import java.util.Map;

import javax.sql.DataSource;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.client.GenerateBudgetFileProcessDAO;

/**
 * 
 * MCProcessDAOImpl.
 * 
 * @author bethke
 * @since Dec 29, 2011
 */
public class GenerateBudgetFileProcessDAOImpl extends BaseDAO implements GenerateBudgetFileProcessDAO
{
  private DataSource dataSource;

  /**
   * set the data source.
   * 
   * @param dataSource
   */
  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }

  @SuppressWarnings("rawtypes")
public Map callGenerateBudgetFileProc( String orgUnits, Integer level )
  {
	  CallGenerateBudgetFileProc proc = new CallGenerateBudgetFileProc(dataSource);
	  return proc.executeProcedure( orgUnits, level );
  }
  
  @SuppressWarnings("rawtypes")
public Map callGenerateBudgetFileWDProc( String orgUnits, Integer level )
  {
	  CallGenerateBudgetFileWDProc proc = new CallGenerateBudgetFileWDProc(dataSource);
	  return proc.executeProcedure( orgUnits, level );
  }  
}
