/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/participant/AudienceCriteriaUtility.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.participant;

import java.util.ArrayList;
import java.util.Iterator;

import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.service.SAO;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.employer.EmployerService;
import com.biperf.core.service.hierarchy.NodeTypeService;
import com.biperf.core.utils.BeanLocator;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * AudienceCriteriaUtility.
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
 * <td>leep</td>
 * <td>Jun 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AudienceCriteriaUtility
{
  // TODO use or remove logger
  // private static final Log logger = LogFactory.getLog( AudienceCriteriaUtility.class );

  private static final String ASSET = "participant.list.builder.details";
  private static final String FIRST_NAME_KEY = "FIRST_NAME";
  private static final String LAST_NAME_KEY = "LAST_NAME";
  private static final String LOGIN_ID_KEY = "LOGIN_ID";
  private static final String EMPLOYER_KEY = "EMPLOYER";
  private static final String JOB_POSITION_KEY = "JOB_POSITION";
  private static final String DEPARTMENT_KEY = "DEPARTMENT";
  private static final String LANGUAGE_KEY = "LANGUAGE";
  private static final String NODE_NAME_KEY = "NODE_NAME";
  private static final String NODE_TYPE_KEY = "NODE_TYPE";
  private static final String NODE_ROLE_KEY = "NODE_ROLE";
  private static final String CHILD_NODES_INCLUDED_KEY = "CHILD_NODES_INCLUDED";

  /**
   * Returns an ArrayList of string representing chosen criteria for an AudienceCriteria.
   * 
   * @param audienceCriteria
   * @return ArrayList
   */
  @SuppressWarnings( "unchecked" )
  public static ArrayList getCriteria( AudienceCriteria audienceCriteria )
  {
    ArrayList searchCriteriaList = new ArrayList();

    if ( audienceCriteria.getFirstName() != null )
    {
      searchCriteriaList.add( ContentReaderManager.getText( ASSET, FIRST_NAME_KEY ) + " = " + audienceCriteria.getFirstName() );
    }

    if ( audienceCriteria.getLastName() != null )
    {
      searchCriteriaList.add( ContentReaderManager.getText( ASSET, LAST_NAME_KEY ) + " = " + audienceCriteria.getLastName() );
    }

    if ( audienceCriteria.getLoginId() != null )
    {
      searchCriteriaList.add( ContentReaderManager.getText( ASSET, LOGIN_ID_KEY ) + " = " + audienceCriteria.getLastName() );
    }

    if ( audienceCriteria.getEmployerId() != null )
    {
      EmployerService employerService = (EmployerService)getService( EmployerService.BEAN_NAME );

      searchCriteriaList.add( ContentReaderManager.getText( ASSET, EMPLOYER_KEY ) + " = " + employerService.getEmployerById( audienceCriteria.getEmployerId() ).getName() );
    }

    if ( audienceCriteria.getPositionType() != null )
    {
      searchCriteriaList.add( ContentReaderManager.getText( ASSET, JOB_POSITION_KEY ) + " = " + audienceCriteria.getPositionType() );
    }

    if ( audienceCriteria.getDepartmentType() != null )
    {
      searchCriteriaList.add( ContentReaderManager.getText( ASSET, DEPARTMENT_KEY ) + " = " + audienceCriteria.getDepartmentType() );
    }

    if ( audienceCriteria.getLanguageType() != null )
    {
      searchCriteriaList
          .add( ContentReaderManager.getText( ASSET, LANGUAGE_KEY ) + " = " + DynaPickListType.lookup( LanguageType.PICKLIST_ASSET, audienceCriteria.getLanguageType().getCode() ).getName() );
    }

    if ( audienceCriteria.getNodeName() != null )
    {
      searchCriteriaList.add( ContentReaderManager.getText( ASSET, NODE_NAME_KEY ) + " = " + audienceCriteria.getNodeName() );
    }

    if ( audienceCriteria.getNodeTypeId() != null )
    {
      NodeTypeService nodeTypeService = (NodeTypeService)getService( NodeTypeService.BEAN_NAME );

      searchCriteriaList.add( ContentReaderManager.getText( ASSET, NODE_TYPE_KEY ) + " = " + nodeTypeService.getNodeTypeById( audienceCriteria.getNodeTypeId() ).getNodeTypeName() );
    }

    if ( audienceCriteria.getNodeRole() != null )
    {
      searchCriteriaList
          .add( ContentReaderManager.getText( ASSET, NODE_ROLE_KEY ) + " = " + DynaPickListType.lookup( HierarchyRoleType.PICKLIST_ASSET, audienceCriteria.getNodeRole().getCode() ).getName() );
    }

    if ( audienceCriteria.isChildNodesIncluded() )
    {
      searchCriteriaList.add( ContentReaderManager.getText( ASSET, CHILD_NODES_INCLUDED_KEY ) + " = "
          + ContentReaderManager.getText( "system.boolean.values", Boolean.toString( audienceCriteria.isChildNodesIncluded() ).toUpperCase() ) );
    }

    /* Bug # 34056 start */
    if ( audienceCriteria.getCountryId() != null )
    {
      CountryService countryService = (CountryService)getService( CountryService.BEAN_NAME );
      Country country = countryService.getCountryById( audienceCriteria.getCountryId() );

      if ( country != null )
      {
        searchCriteriaList.add( "Country" + " = " + country.getI18nCountryName() );
      }
    }
    /* Bug # 34056 end */

    for ( Iterator iter = audienceCriteria.getCharacteristicCriterias().iterator(); iter.hasNext(); )
    {
      AudienceCriteriaCharacteristic audienceCriteriaCharacteristic = (AudienceCriteriaCharacteristic)iter.next();

      if ( audienceCriteriaCharacteristic.getSearchType().equals( "include" ) )
      {

        Characteristic characteristic = audienceCriteriaCharacteristic.getCharacteristic();

        String dataType = characteristic.getCharacteristicDataType().getCode();

        if ( dataType.equals( "date" ) )
        {
          searchCriteriaList.add( characteristic.getCharacteristicName() + " - " + audienceCriteriaCharacteristic.getCharacteristicValue() );
        }
        else if ( dataType.equals( "int" ) )
        {
          searchCriteriaList.add( characteristic.getCharacteristicName() + " - " + audienceCriteriaCharacteristic.getCharacteristicValue() );
        }
        else if ( dataType.equals( "txt" ) )
        {
          searchCriteriaList.add( characteristic.getCharacteristicName() + " - " + audienceCriteriaCharacteristic.getCharacteristicValue() );
        }
        else if ( dataType.equals( "decimal" ) )
        {
          searchCriteriaList.add( characteristic.getCharacteristicName() + " - " + audienceCriteriaCharacteristic.getCharacteristicValue() );
        }
        else if ( dataType.equals( "single_select" ) )
        {
          searchCriteriaList
              .add( characteristic.getCharacteristicName() + " - " + DynaPickListType.lookup( characteristic.getPlName(), audienceCriteriaCharacteristic.getCharacteristicValue() ).getName() );
        }
        else if ( dataType.equals( "multi_select" ) )
        {
          searchCriteriaList
              .add( characteristic.getCharacteristicName() + " - " + DynaPickListType.lookup( characteristic.getPlName(), audienceCriteriaCharacteristic.getCharacteristicValue() ).getName() );
        }
        else if ( dataType.equals( "boolean" ) )
        {
          searchCriteriaList
              .add( characteristic.getCharacteristicName() + " - " + ContentReaderManager.getText( "system.boolean.values", audienceCriteriaCharacteristic.getCharacteristicValue().toUpperCase() ) );
        }
      }
    }

    return searchCriteriaList;
  }

  // get exclude criteria options
  public static ArrayList getExclusionCriteria( AudienceCriteria audienceCriteria )
  {
    ArrayList searchCriteriaList = new ArrayList();

    if ( audienceCriteria.getExcludeCountryId() != null )
    {
      CountryService countryService = (CountryService)getService( CountryService.BEAN_NAME );
      Country country = countryService.getCountryById( audienceCriteria.getExcludeCountryId() );

      if ( country != null )
      {
        searchCriteriaList.add( "Country" + " = " + country.getI18nCountryName() );
      }
    }

    if ( audienceCriteria.getExcludeNodeName() != null )
    {
      searchCriteriaList.add( ContentReaderManager.getText( ASSET, NODE_NAME_KEY ) + " = " + audienceCriteria.getExcludeNodeName() );
    }

    if ( audienceCriteria.getExcludeNodeTypeId() != null )
    {
      NodeTypeService nodeTypeService = (NodeTypeService)getService( NodeTypeService.BEAN_NAME );

      searchCriteriaList.add( ContentReaderManager.getText( ASSET, NODE_TYPE_KEY ) + " = " + nodeTypeService.getNodeTypeById( audienceCriteria.getExcludeNodeTypeId() ).getNodeTypeName() );
    }
    if ( audienceCriteria.getExcludeNodeRole() != null )
    {
      searchCriteriaList
          .add( ContentReaderManager.getText( ASSET, NODE_ROLE_KEY ) + " = " + DynaPickListType.lookup( HierarchyRoleType.PICKLIST_ASSET, audienceCriteria.getExcludeNodeRole().getCode() ).getName() );
    }

    if ( audienceCriteria.isExcludeChildNodesIncluded() )
    {
      searchCriteriaList.add( ContentReaderManager.getText( ASSET, CHILD_NODES_INCLUDED_KEY ) + " = "
          + ContentReaderManager.getText( "system.boolean.values", Boolean.toString( audienceCriteria.isExcludeChildNodesIncluded() ).toUpperCase() ) );
    }

    if ( audienceCriteria.getExcludePositionType() != null )
    {
      searchCriteriaList.add( ContentReaderManager.getText( ASSET, JOB_POSITION_KEY ) + " = " + audienceCriteria.getExcludePositionType() );
    }

    if ( audienceCriteria.getExcludeDepartmentType() != null )
    {
      searchCriteriaList.add( ContentReaderManager.getText( ASSET, DEPARTMENT_KEY ) + " = " + audienceCriteria.getExcludeDepartmentType() );
    }

    for ( Iterator iter = audienceCriteria.getCharacteristicCriterias().iterator(); iter.hasNext(); )
    {
      AudienceCriteriaCharacteristic audienceCriteriaCharacteristic = (AudienceCriteriaCharacteristic)iter.next();

      // if characterisitc search type is exclude then add to list
      if ( audienceCriteriaCharacteristic.getSearchType().equals( "exclude" ) )
      {

        Characteristic characteristic = audienceCriteriaCharacteristic.getCharacteristic();

        String dataType = characteristic.getCharacteristicDataType().getCode();

        if ( dataType.equals( "date" ) )
        {
          searchCriteriaList.add( characteristic.getCharacteristicName() + " - " + audienceCriteriaCharacteristic.getCharacteristicValue() );
        }
        else if ( dataType.equals( "int" ) )
        {
          searchCriteriaList.add( characteristic.getCharacteristicName() + " - " + audienceCriteriaCharacteristic.getCharacteristicValue() );
        }
        else if ( dataType.equals( "txt" ) )
        {
          searchCriteriaList.add( characteristic.getCharacteristicName() + " - " + audienceCriteriaCharacteristic.getCharacteristicValue() );
        }
        else if ( dataType.equals( "decimal" ) )
        {
          searchCriteriaList.add( characteristic.getCharacteristicName() + " - " + audienceCriteriaCharacteristic.getCharacteristicValue() );
        }
        else if ( dataType.equals( "single_select" ) )
        {
          searchCriteriaList
              .add( characteristic.getCharacteristicName() + " - " + DynaPickListType.lookup( characteristic.getPlName(), audienceCriteriaCharacteristic.getCharacteristicValue() ).getName() );
        }
        else if ( dataType.equals( "multi_select" ) )
        {
          searchCriteriaList
              .add( characteristic.getCharacteristicName() + " - " + DynaPickListType.lookup( characteristic.getPlName(), audienceCriteriaCharacteristic.getCharacteristicValue() ).getName() );
        }
        else if ( dataType.equals( "boolean" ) )
        {
          searchCriteriaList
              .add( characteristic.getCharacteristicName() + " - " + ContentReaderManager.getText( "system.boolean.values", audienceCriteriaCharacteristic.getCharacteristicValue().toUpperCase() ) );
        }
      }
    }

    return searchCriteriaList;
  }

  /**
   * Bean location through BeanLocator look-up.
   * 
   * @param beanName
   * @return SAO
   */
  public static SAO getService( String beanName )
  {
    return (SAO)BeanLocator.getBean( beanName );
  }

}
