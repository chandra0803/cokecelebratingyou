/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/system/impl/SystemVariableServiceImpl.java,v $
 */

package com.biperf.core.service.system.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.cache.Cache;
import com.biperf.core.dao.system.SystemVariableDAO;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.SecurityUtils;
import com.biperf.core.utils.StringUtil;

/**
 * SystemVariableService for CRUD operations for SystemVariables.
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
 * <td>kumars</td>
 * <td>Apr 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SystemVariableServiceImpl implements SystemVariableService
{
  private static final String DEV_CONTEXT_G5ALPHA = "g5alpha";
  private static final String DEV_CONTEXT_G5GAMMA = "g5gamma";
  private static final String DEV_CONTEXT_G5KITKAT = "g5kitkat";
  private static final String DEV_CONTEXT_G6BB8 = "g6bb8";
  private static final String DEV_CONTEXT_GYODA = "gyoda";
  private static final String DEV_CONTEXT_G5BETA = "g5beta";

  private static final String PREFIX = "prefix";

  private static final Log log = LogFactory.getLog( SystemVariableService.class );

  /** SystemVariableDAO */
  private SystemVariableDAO systemVariableDAO;

  private String contextName;

  private Properties webDavProperties;

  /**
   * Overridden from.
   * 
   * @see com.biperf.core.service.system.SystemVariableService#setSystemVariableDAO(com.biperf.core.dao.system.SystemVariableDAO)
   * @param systemVariableDAO
   */
  public void setSystemVariableDAO( SystemVariableDAO systemVariableDAO )
  {
    this.systemVariableDAO = systemVariableDAO;
  }

  /**
   * Overridden from.
   * 
   * @see com.biperf.core.service.system.SystemVariableService#getAllProperties()
   * @return List
   */
  public List getAllProperties()
  {
    return systemVariableDAO.getAllProperties();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.system.SystemVariableService#getPropertyByName(java.lang.String)
   * @param propertyName
   * @return <code>PropertySetItem</code>
   */
  public PropertySetItem getPropertyByName( String propertyName )
  {
    return systemVariableDAO.getPropertyByName( propertyName );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.system.SystemVariableService#getPropertyByNameAndEnvironment(java.lang.String)
   * @param propertyName
   * @return <code>PropertySetItem</code>
   */
  public PropertySetItem getPropertyByNameAndEnvironment( String propertyName )
  {
    StringBuffer systemVariableName = new StringBuffer();

    systemVariableName.append( propertyName );
    systemVariableName.append( "." );
    systemVariableName.append( Environment.getEnvironment() );

    return getPropertyByName( systemVariableName.toString() );
  }

  /**
   * Overridden from.
   * 
   * @see com.biperf.core.service.system.SystemVariableService#saveProperty(com.biperf.core.domain.system.PropertySetItem)
   * @param prop
   */
  public void saveProperty( PropertySetItem prop ) throws ServiceErrorException
  {
    List serviceErrors = new ArrayList();

    // Check to see if the record already exists in the database.
    PropertySetItem dbProperty = systemVariableDAO.getPropertyByName( prop.getEntityName() );
    if ( dbProperty != null )
    {
      // if we found a record in the database with the given name, and key, and our prop ID is
      // 0,
      // we are trying to insert a duplicate record.
      if ( dbProperty.getKey().equals( prop.getKey() ) && prop.getEntityId() == 0 )
      {
        ServiceError error = new ServiceError( ServiceErrorMessageKeys.SYSTEM_VARIABLE_DUPLICATE );
        serviceErrors.add( error );
        throw new ServiceErrorException( serviceErrors );
      }
    }
    // Currently If we create a system variable from the screen, by default entity_id value always
    // setting as 0 it is fine in creating flow,
    // but while updating the system variable from screen it is failing because in line no 135 we
    // are checking the condition like( prop.getEntityId() == 0).
    // Due to above reason here I'm setting as entity_id as -1L
    else
    {
      prop.setEntityId( -1L );
    }

    systemVariableDAO.savePropertySetItem( prop );
  }

  /**
   * Save the value of an existing PropertySetItem in OS_PROPERTYSET.
   * 
   * @param prop a PropertySetItem
   */
  public void savePropertyValue( PropertySetItem prop )
  {
    try
    {
      saveProperty( prop );
    }
    catch( ServiceErrorException e )
    {
      throw new BeaconRuntimeException( "This exception should never occur because a client should call this method only to change the value of an existing property.", e );
    }
  }

  /**
   * Overridden from.
   * 
   * @see com.biperf.core.service.system.SystemVariableService#deleteProperty(com.biperf.core.domain.system.PropertySetItem)
   * @param prop
   */
  public void deleteProperty( PropertySetItem prop )
  {
    systemVariableDAO.deletePropertySetItem( prop );
  }

  /**
   * Only use for mock testing.
   * 
   * @param systemVariableCache
   */
  public void setSystemVariableCacheForMock( Cache systemVariableCache )
  {
    // no op
  }

  public void setContextName( String contextName )
  {
    this.contextName = contextName;
  }

  public String getContextName()
  {
    return contextName;
  }

  public boolean isGDEV()
  {
    String contextName = getContextName();
    if ( StringUtil.isEmpty( contextName ) )
    {
      return false;
    }
    if ( !StringUtil.isEmpty( contextName )
        && ( contextName.toLowerCase().startsWith( DEV_CONTEXT_G5KITKAT ) || contextName.toLowerCase().startsWith( DEV_CONTEXT_G5ALPHA ) || contextName.toLowerCase().startsWith( DEV_CONTEXT_G5GAMMA )
            || contextName.toLowerCase().startsWith( DEV_CONTEXT_G6BB8 ) || contextName.toLowerCase().startsWith( DEV_CONTEXT_GYODA ) || contextName.toLowerCase().startsWith( DEV_CONTEXT_G5BETA ) ) )
    {
      return true;
    }
    return false;
  }

  public String getPrefix()
  {
    return getWebDavProperty( "webdav." + PREFIX ).toLowerCase();
  }

  private String getWebDavProperty( String propertyName )
  {
    return webDavProperties.getProperty( propertyName );
  }

  public void setWebDavProperties( Properties webDavProperties )
  {
    this.webDavProperties = webDavProperties;
  }

  @Override
  public String getAESDecryptedValue( String encryptedValue )
  {
    String decryptedValue = null;
    try
    {
      decryptedValue = SecurityUtils.decryptAES( encryptedValue, SystemVariableService.AES_KEY, SystemVariableService.AES_INIT_VECTOR );
    }
    catch( Exception e )
    {
      log.error( "Failed to decrypt [" + encryptedValue + "]", e );
    }
    return decryptedValue;
  }

  @Override
  public String getAESDecryptedValue( String encryptedValue, String aesKey, String iv )
  {
    String decryptedValue = null;
    try
    {
      decryptedValue = SecurityUtils.decryptAES( encryptedValue, getPropertyByName( aesKey ).getStringVal(), getPropertyByName( iv ).getStringVal() );
    }
    catch( Exception e )
    {
      log.error( "Failed to decrypt [" + encryptedValue + "]", e );
    }
    return decryptedValue;
  }

  @Override
  public PropertySetItem getDefaultLanguage()
  {
    return getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE );
  }

}
