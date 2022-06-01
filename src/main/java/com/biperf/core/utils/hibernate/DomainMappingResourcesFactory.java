/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/utils/hibernate/DomainMappingResourcesFactory.java,v $
 */

package com.biperf.core.utils.hibernate;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import com.biperf.core.utils.ArrayUtil;

/**
 * DomainMappingResourcesFactory is to build the list of domain mapping .hbm XMLs for the core
 * system and all the modules installed. <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>Sathish</td>
 * <td>Dec 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class DomainMappingResourcesFactory
{
  private static final String DELIMITER_TOKEN = ",\n\t ";

  private Properties modulesInstalled;

  private Properties moduleDomainMappings;

  public Properties getModulesInstalled()
  {
    return modulesInstalled;
  }

  public void setModulesInstalled( Properties modulesInstalled )
  {
    this.modulesInstalled = modulesInstalled;
  }

  public Properties getModuleDomainMappings()
  {
    return moduleDomainMappings;
  }

  public void setModuleDomainMappings( Properties moduleDomainMappings )
  {
    this.moduleDomainMappings = moduleDomainMappings;
  }

  /**
   * Accessor method to return the list of domain mapping resources for the installed modules for
   * the hibernate Session Factory bean. Remove duplicates so two installed modules can 
   * use the same resource that is not in core.  
   * 
   * @return String[]
   */
  public String[] getDomainMappingResources()
  {
    Enumeration keys = modulesInstalled.keys();

    Set resources = new LinkedHashSet();

    while ( keys.hasMoreElements() )
    {
      String moduleKey = (String)keys.nextElement();
      Boolean moduleInstalled = Boolean.valueOf( (String)modulesInstalled.get( moduleKey ) );
      if ( moduleInstalled.booleanValue() )
      {
        // get the mapping resources for each installed module
        String mappingResource = (String)moduleDomainMappings.get( moduleKey );

        // convert the comma delimited string of domain mapping resources into a list of strings
        resources.addAll( ArrayUtil.convertDelimitedStringToList( mappingResource, DELIMITER_TOKEN ) );
      }
    }

    String[] returnArr = ArrayUtil.convertListToStringArray( new ArrayList( resources ) );

    return returnArr;
  }
}
