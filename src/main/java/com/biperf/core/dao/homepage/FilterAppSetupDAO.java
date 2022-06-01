
package com.biperf.core.dao.homepage;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.homepage.FilterAppSetup;

public interface FilterAppSetupDAO extends DAO
{

  public static final String BEAN_NAME = "filterAppSetupDAO";

  /**
  * 
  * @param filterAppSetupId
  * @return
  */
  public FilterAppSetup getFilterAppSetupById( Long filterAppSetupId );

  /**
   * Gets all of the FilterAppSetup recs.
   * 
   * @return List
   */
  public List<FilterAppSetup> getAll();

  /*
   * Gets all the FilterAppSetup records based on business criteria
   */
  public List<FilterAppSetup> getAllForHomePage();

  /**
   * save the Tile to DB.
   * 
   * @return FilterAppSetup
   */
  public FilterAppSetup save( FilterAppSetup filterAppSetup );

  /**
   * Deletes a record of FilterAppSetup
   * @param filterAppSetup
   */
  public void delete( FilterAppSetup filterAppSetup );

  public List<FilterAppSetup> getFilterAppSetupByFilterTypeCode( String code );

}
