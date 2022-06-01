
package com.biperf.core.dao.homepage;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.homepage.CrossPromotionalModuleApp;
import com.biperf.core.domain.homepage.ModuleApp;

public interface ModuleAppDAO extends DAO
{
  public static final String BEAN_NAME = "moduleAppDAO";

  public List<ModuleApp> getAllForHomePage();

  public List<ModuleApp> getAllForDelegateHomePage();

  public List<ModuleApp> getAllAvailableModuleApps();

  /**
  * 
  * @param moduleAppId
  * @return
  */
  public ModuleApp getModuleAppById( Long moduleAppId );

  /**
   * Gets all of the ModuleApp recs.
   * 
   * @return List
   */
  public List<ModuleApp> getAllAudienceSpecificTiles();

  /**
   * save the Tile to DB.
   * 
   * @return ModuleApp
   */
  public ModuleApp save( ModuleApp moduleApp );

  /**
   * Deletes a record of ModuleApp
   * @param moduleApp
   */
  public void delete( ModuleApp moduleApp );

  public List getAudienceforModuleApp( Long moduleAppId );

  public List<ModuleApp> getModuleByPriorityOne();

  public List<ModuleApp> getModuleByPriorityTwo();

  public List<ModuleApp> getAll();

  public List<ModuleApp> getAllForAdminSetup();

  public List<CrossPromotionalModuleApp> getCrossPromotionalModuleApps();

  /**   
   * get the module app object based on the tile mapping type 
   * @return List
   */
  public List<ModuleApp> getModuleAppByTileMappingType( String tileMappingType );

  public void updateMEPlusFilterPageSetup( boolean svValue );

  public void updateRecognitionOnlyFilterPageSetup( boolean svValue );

  public ModuleApp getModuleByAppName( String appName );

  public void updateSalesMakerFilterPageSetup( boolean svValue );
}
