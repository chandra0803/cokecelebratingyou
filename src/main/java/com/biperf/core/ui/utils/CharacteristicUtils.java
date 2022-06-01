/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/utils/CharacteristicUtils.java,v $
 */

package com.biperf.core.ui.utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;

import com.biperf.core.domain.characteristic.NodeTypeCharacteristicType;
import com.biperf.core.domain.characteristic.UserCharacteristicType;
import com.biperf.core.domain.claim.ClaimProductCharacteristic;
import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.domain.hierarchy.NodeCharacteristic;
import com.biperf.core.domain.product.ProductCharacteristicType;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.utils.CharacteristicValidationUtils;
import com.biperf.core.value.CharacteristicValueBean;

/**
 * CharacteristicUtils - Utility class for maintaining the UserCharacteristics.
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
 * <td>zahler</td>
 * <td>May 11, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CharacteristicUtils
{

  private static final String DELETE_VALUE = "delete_option";

  /**
   * @param existingNodeCharacteristics
   * @param availableNodeTypeCharacteristicTypes
   * @return List
   */
  public static List getNodeCharacteristicValueList( Set existingNodeCharacteristics, List availableNodeTypeCharacteristicTypes )
  {
    List valueList = new ArrayList();

    // Keep track of the existing NodeTypeCharacteristicTypes used
    List existingNodeTypeCharacteristicTypes = new ArrayList();

    Iterator it = existingNodeCharacteristics.iterator();
    while ( it.hasNext() )
    {
      CharacteristicValueBean formBean = new CharacteristicValueBean();

      NodeCharacteristic nodeCharacteristic = (NodeCharacteristic)it.next();

      formBean.setJoinTableId( nodeCharacteristic.getId() );
      formBean.setVersion( nodeCharacteristic.getVersion() );

      // Since the characteristic value for multi_select characteristics are stored
      // as comma separated strings, a String[] needs to be created so that the screen
      // can show the multi select list with the values in the String[] selected.
      if ( nodeCharacteristic.getNodeTypeCharacteristicType().getCharacteristicDataType().getCode().equals( "multi_select" ) )
      {
        formBean.setCharacteristicValues( parseCharacteristicValues( nodeCharacteristic.getCharacteristicValue() ) );
      }
      formBean.setCharacteristicValue( nodeCharacteristic.getCharacteristicValue() );
      formBean.setDomainId( nodeCharacteristic.getNodeTypeCharacteristicType().getDomainId() );

      if ( nodeCharacteristic.getRosterNodeCharId() != null && !StringUtils.isEmpty( nodeCharacteristic.getRosterNodeCharId().toString() ) )
      {
        formBean.setRosterId( nodeCharacteristic.getRosterNodeCharId().toString() );
      }
      CharacteristicValidationUtils.loadCharacteristicValueBeanData( formBean, nodeCharacteristic.getNodeTypeCharacteristicType(), nodeCharacteristic.getRosterNodeCharId() );

      // This one is being used already, using this list when comparing to available types
      existingNodeTypeCharacteristicTypes.add( nodeCharacteristic.getNodeTypeCharacteristicType() );

      valueList.add( formBean );
    }

    // Remove the ones already used from the available list
    availableNodeTypeCharacteristicTypes.removeAll( existingNodeTypeCharacteristicTypes );

    // add these available CharTypes to the list
    Iterator iter = availableNodeTypeCharacteristicTypes.iterator();
    while ( iter.hasNext() )
    {
      CharacteristicValueBean formBean = new CharacteristicValueBean();

      formBean.setCharacteristicValue( "" );

      NodeTypeCharacteristicType characteristic = (NodeTypeCharacteristicType)iter.next();
      formBean.setDomainId( characteristic.getDomainId() );
      CharacteristicValidationUtils.loadCharacteristicValueBeanData( formBean, characteristic, null );

      valueList.add( formBean );
    } // while

    return valueList;
  }

  /**
   * @param existingUserCharacteristics
   * @param availableUserCharacteristicTypes
   * @return List
   */
  public static List getUserCharacteristicValueList( Set existingUserCharacteristics, List availableUserCharacteristicTypes )
  {
    List valueList = new ArrayList();

    // Keep track of the existing UserCharacteristicTypes used
    List existingUserCharacteristicTypes = new ArrayList();

    Iterator it = existingUserCharacteristics.iterator();
    while ( it.hasNext() )
    {
      UserCharacteristic userCharacteristic = (UserCharacteristic)it.next();

      if ( userCharacteristic.getUserCharacteristicType().isActive() )
      {
        CharacteristicValueBean formBean = new CharacteristicValueBean();

        formBean.setJoinTableId( userCharacteristic.getId() );
        formBean.setVersion( userCharacteristic.getVersion() );

        if ( userCharacteristic.getRosterUserCharId() != null && !StringUtils.isEmpty( userCharacteristic.getRosterUserCharId().toString() ) )
        {
          formBean.setRosterId( userCharacteristic.getRosterUserCharId().toString() );
        }

        // Since the characteristic value for multi_select characteristics are stored
        // as comma separated strings, a String[] needs to be created so that the screen
        // can show the multi select list with the values in the String[] selected.
        if ( userCharacteristic.getUserCharacteristicType().getCharacteristicDataType().getCode().equals( "multi_select" ) )
        {
          formBean.setCharacteristicValues( parseCharacteristicValues( userCharacteristic.getCharacteristicValue() ) );
        }
        formBean.setCharacteristicValue( userCharacteristic.getCharacteristicValue() );

        CharacteristicValidationUtils.loadCharacteristicValueBeanData( formBean, userCharacteristic.getUserCharacteristicType(), userCharacteristic.getRosterUserCharId() );

        // This one is being used already, using this list when comparing to available types
        existingUserCharacteristicTypes.add( userCharacteristic.getUserCharacteristicType() );

        valueList.add( formBean );
      }
    }

    // Remove the ones already used from the available list
    availableUserCharacteristicTypes.removeAll( existingUserCharacteristicTypes );

    // add these available CharTypes to the list
    Iterator iter = availableUserCharacteristicTypes.iterator();
    while ( iter.hasNext() )
    {
      CharacteristicValueBean formBean = new CharacteristicValueBean();

      formBean.setCharacteristicValue( "" );

      UserCharacteristicType characteristic = (UserCharacteristicType)iter.next();
      CharacteristicValidationUtils.loadCharacteristicValueBeanData( formBean, characteristic, null );

      valueList.add( formBean );
    } // while

    return valueList;
  }

  /**
   * @param existingClaimProductCharacteristics
   * @param availableProductCharacteristicTypes
   * @return List
   */
  public static List getProductCharacteristicValueList( Set existingClaimProductCharacteristics, List availableProductCharacteristicTypes )
  {
    List valueList = new ArrayList();

    // Keep track of the existing existingProductCharacteristicTypes used
    List existingProductCharacteristicTypes = new ArrayList();

    Iterator it = existingClaimProductCharacteristics.iterator();
    while ( it.hasNext() )
    {
      CharacteristicValueBean formBean = new CharacteristicValueBean();

      ClaimProductCharacteristic claimProductCharacteristic = (ClaimProductCharacteristic)it.next();

      formBean.setJoinTableId( claimProductCharacteristic.getId() );
      formBean.setVersion( claimProductCharacteristic.getVersion() );

      // Since the characteristic value for multi_select characteristics are stored
      // as comma separated strings, a String[] needs to be created so that the screen
      // can show the multi select list with the values in the String[] selected.
      if ( claimProductCharacteristic.getProductCharacteristicType().getCharacteristicDataType().getCode().equals( "multi_select" ) )
      {
        formBean.setCharacteristicValues( parseCharacteristicValues( claimProductCharacteristic.getValue() ) );
      }
      formBean.setCharacteristicValue( claimProductCharacteristic.getValue() );
      formBean.setIsUnique( claimProductCharacteristic.getProductCharacteristicType().getIsUnique() );

      CharacteristicValidationUtils.loadCharacteristicValueBeanData( formBean, claimProductCharacteristic.getProductCharacteristicType(), null );
      formBean.setVersion( claimProductCharacteristic.getVersion() );
      // This one is being used already, using this list when comparing to available types
      existingProductCharacteristicTypes.add( claimProductCharacteristic.getProductCharacteristicType() );

      valueList.add( formBean );
    }

    // Remove the ones already used from the available list
    availableProductCharacteristicTypes.removeAll( existingProductCharacteristicTypes );

    // add these available CharTypes to the list
    Iterator iter = availableProductCharacteristicTypes.iterator();
    while ( iter.hasNext() )
    {
      CharacteristicValueBean formBean = new CharacteristicValueBean();

      formBean.setCharacteristicValue( "" );

      ProductCharacteristicType characteristic = (ProductCharacteristicType)iter.next();
      CharacteristicValidationUtils.loadCharacteristicValueBeanData( formBean, characteristic, null );
      formBean.setIsUnique( characteristic.getIsUnique() );
      formBean.setVersion( characteristic.getVersion() );
      valueList.add( formBean );
    } // while

    return valueList;
  }

  /**
   * Checks if there are any Unique Product Characteristics in the list of Characteristic Form
   * Beans.
   * 
   * @param productCharacteristicValueList
   * @return boolean - true if there are any characteristicFormBeans in the list with the unique
   *         char set to true. - false if all characteristicFormBeans in the list have the unique
   *         char set to false or null. - false if there are no characteristicFormBeans in the list
   */
  public static boolean isAnyUniqueProductCharacteristics( List productCharacteristicValueList )
  {
    boolean isAnyUniqueChar = false;
    Iterator iter = productCharacteristicValueList.iterator();
    while ( iter.hasNext() )
    {
      CharacteristicValueBean characteristicFormBean = (CharacteristicValueBean)iter.next();

      Boolean isUnique = characteristicFormBean.getIsUnique();
      if ( isUnique != null )
      {
        if ( isUnique.equals( Boolean.TRUE ) )
        {
          isAnyUniqueChar = true;
          break;
        }
      }
    } // while
    return isAnyUniqueChar;
  }

  /**
   * Load value list with the available characteristic information. There will not be a
   * characteristic value for these because they don't exist as UserCharacteristics yet.
   * 
   * @param formBeans
   * @param valueList
   */
  public static void loadExistingValues( List formBeans, List valueList )
  {
    if ( valueList == null )
    {
      return;
    }

    Iterator it = formBeans.iterator();
    while ( it.hasNext() )
    {
      CharacteristicValueBean formBean = (CharacteristicValueBean)it.next();

      Iterator valueListIter = valueList.iterator();
      while ( valueListIter.hasNext() )
      {
        CharacteristicValueBean valueFormBean = (CharacteristicValueBean)valueListIter.next();

        if ( valueFormBean.getCharacteristicId() != null && valueFormBean.getCharacteristicId().compareTo( formBean.getCharacteristicId() ) == 0 )
        {
          formBean.setCharacteristicValue( valueFormBean.getCharacteristicValue() );
          formBean.setCharacteristicValues( valueFormBean.getCharacteristicValues() );
        }
      } // while
    } // while
  }

  /**
   * This will create a list of UserCharacteristic objects consisting of a characteristic object
   * (containing only characteristicId, the object will be looked up in the service), and a
   * characteristicValue. The User object will aslo be looked up in the service.
   * 
   * @param valueList
   * @return Collection
   */
  public static Collection toListOfUserCharacteristicDomainObjects( List valueList )
  {
    List userCharacteristics = new ArrayList();

    // loop through the valueList to get values from the form.
    for ( int i = 0; i < valueList.size(); i++ )
    {
      // get each CharacteristicFormBean from the valueList
      CharacteristicValueBean formBean = getValueInfo( i, valueList );

      if ( formBean.getCharacteristicValue() != null && formBean.getCharacteristicValue().trim().length() == 0
          || formBean.getCharacteristicValue() == null && formBean.getCharacteristicDataType().equals( "multi_select" ) )
      {
        // Bug # 34625 fix
        formBean.setCharacteristicValue( DELETE_VALUE );
      }

      // Create a new UserCharacteristic object if there is a characteristic value
      // tied to the object.
      if ( formBean.getCharacteristicValue() != null && !formBean.getCharacteristicValue().equals( "" ) || formBean.getCharacteristicValues() != null && formBean.getCharacteristicValues().length > 0 )
      {
        // Create the new UserCharacteristic Object
        UserCharacteristic userCharacteristic = new UserCharacteristic();

        // Create a new Characteristic object to put into the userCharacteristic object
        UserCharacteristicType characteristic = new UserCharacteristicType();

        // Set the Characteristic ID from the formBean.
        characteristic.setId( formBean.getCharacteristicId() );
        characteristic.setCharacteristicName( formBean.getCharacteristicName() );
        characteristic.setCmAssetCode( formBean.getCmAssetCode() );
        characteristic.setNameCmKey( formBean.getNameCmKey() );

        // Set the Characteristic object, and the userCharacteristic info into the
        // userCharacteristic Object, no need to set the user object here,
        // that will be handled in the Service.
        userCharacteristic.setUserCharacteristicType( characteristic );

        if ( formBean.getJoinTableId() != null && formBean.getJoinTableId().longValue() != 0 )
        {
          userCharacteristic.setId( formBean.getJoinTableId() );
        }
        if ( formBean.getRosterId() != null && !StringUtils.isEmpty( formBean.getRosterId() ) )
        {
          userCharacteristic.setRosterUserCharId( UUID.fromString( formBean.getRosterId() ) );
        }
        userCharacteristic.setVersion( formBean.getVersion() );

        // Multi_Select types will have their values in a String[] characteristicValues
        // so loop through the String[] and store them as a "," separated string.
        if ( formBean.getCharacteristicDataType().equals( "multi_select" ) )
        {
          String characteristicValue = "";
          for ( int j = 0; formBean.getCharacteristicValues() != null && j < formBean.getCharacteristicValues().length; j++ )
          {
            if ( characteristicValue.equals( "" ) )
            {
              characteristicValue = formBean.getCharacteristicValues()[j];
            }
            else
            {
              characteristicValue = characteristicValue + "," + formBean.getCharacteristicValues()[j];
            }
          }

          if ( characteristicValue.equals( "" ) )
          {
            userCharacteristic.setCharacteristicValue( formBean.getCharacteristicValue() );
          }
          else
          {
            userCharacteristic.setCharacteristicValue( characteristicValue );
          }
        }
        else
        {
          userCharacteristic.setCharacteristicValue( formBean.getCharacteristicValue() );
        }

        // Add this userCharacteristic object to the list.
        userCharacteristics.add( userCharacteristic );
      }
    }

    return userCharacteristics;
  }

  public static Collection returnListOfUserCharacteristicDomainObjects( List valueList )
  {
    List userCharacteristics = new ArrayList();

    // loop through the valueList to get values from the form.
    for ( int i = 0; i < valueList.size(); i++ )
    {
      // get each CharacteristicFormBean from the valueList
      CharacteristicValueBean formBean = getValueInfo( i, valueList );

      if ( formBean.getCharacteristicValue() != null && formBean.getCharacteristicValue().equalsIgnoreCase( DELETE_VALUE ) )
      {
        formBean.setCharacteristicValue( "" );
      }

      // Create a new UserCharacteristic object if there is a characteristic value
      // tied to the object.
      if ( formBean.getCharacteristicValue() != null && !formBean.getCharacteristicValue().equals( "" ) || formBean.getCharacteristicValues() != null && formBean.getCharacteristicValues().length > 0 )
      {
        // Create the new UserCharacteristic Object
        UserCharacteristic userCharacteristic = new UserCharacteristic();

        // Create a new Characteristic object to put into the userCharacteristic object
        UserCharacteristicType characteristic = new UserCharacteristicType();

        // Set the Characteristic ID from the formBean.
        characteristic.setId( formBean.getCharacteristicId() );
        characteristic.setCharacteristicName( formBean.getCharacteristicName() );
        characteristic.setCmAssetCode( formBean.getCmAssetCode() );
        characteristic.setNameCmKey( formBean.getNameCmKey() );

        // Set the Characteristic object, and the userCharacteristic info into the
        // userCharacteristic Object, no need to set the user object here,
        // that will be handled in the Service.
        userCharacteristic.setUserCharacteristicType( characteristic );

        if ( formBean.getJoinTableId() != null && formBean.getJoinTableId().longValue() != 0 )
        {
          userCharacteristic.setId( formBean.getJoinTableId() );
        }
        if ( formBean.getRosterId() != null && !StringUtils.isEmpty( formBean.getRosterId() ) )
        {
          userCharacteristic.setRosterUserCharId( UUID.fromString( formBean.getRosterId() ) );
        }
        userCharacteristic.setVersion( formBean.getVersion() );

        // Multi_Select types will have their values in a String[] characteristicValues
        // so loop through the String[] and store them as a "," separated string.
        if ( formBean.getCharacteristicDataType().equals( "multi_select" ) )
        {
          String characteristicValue = "";
          for ( int j = 0; formBean.getCharacteristicValues() != null && j < formBean.getCharacteristicValues().length; j++ )
          {
            if ( characteristicValue.equals( "" ) )
            {
              characteristicValue = formBean.getCharacteristicValues()[j];
            }
            else
            {
              characteristicValue = characteristicValue + "," + formBean.getCharacteristicValues()[j];
            }
          }
          userCharacteristic.setCharacteristicValue( characteristicValue );
        }
        else
        {
          userCharacteristic.setCharacteristicValue( formBean.getCharacteristicValue() );
        }

        // Add this userCharacteristic object to the list.
        userCharacteristics.add( userCharacteristic );
      }
    }

    return userCharacteristics;
  }

  /**
   * This will create a list of UserCharacteristic objects consisting of a characteristic object
   * (containing only characteristicId, the object will be looked up in the service), and a
   * characteristicValue. The User object will aslo be looked up in the service.
   * 
   * @param valueList
   * @return Collection
   */
  public static Collection toListOfNodeCharacteristicDomainObjects( List valueList )
  {
    List nodeCharacteristics = new ArrayList();

    // loop through the valueList to get values from the form.
    for ( int i = 0; i < valueList.size(); i++ )
    {
      // get each CharacteristicFormBean from the valueList
      CharacteristicValueBean formBean = getValueInfo( i, valueList );

      // Create a new NodeCharacteristic object if there is a characteristic value
      // tied to the object.
      if ( formBean.getCharacteristicValue() != null && !formBean.getCharacteristicValue().equals( "" ) || formBean.getCharacteristicValues() != null && formBean.getCharacteristicValues().length > 0 )
      {
        // Create the new NodeCharacteristic Object
        NodeCharacteristic nodeCharacteristic = new NodeCharacteristic();

        // Create a new Characteristic object to put into the nodeCharacteristic object
        NodeTypeCharacteristicType characteristic = new NodeTypeCharacteristicType();

        // Set the Characteristic ID from the formBean.
        characteristic.setId( formBean.getCharacteristicId() );
        characteristic.setCharacteristicName( formBean.getCharacteristicName() );
        characteristic.setCmAssetCode( formBean.getCmAssetCode() );
        characteristic.setNameCmKey( formBean.getNameCmKey() );
        // Set the Characteristic object, and the nodeCharacteristic info into the
        // nodeCharacteristic Object, no need to set the node object here,
        // that will be handled in the Service.
        nodeCharacteristic.setNodeTypeCharacteristicType( characteristic );

        if ( formBean.getJoinTableId() != null && formBean.getJoinTableId().longValue() != 0 )
        {
          nodeCharacteristic.setId( formBean.getJoinTableId() );
        }
        if ( formBean.getRosterId() != null && !StringUtils.isEmpty( formBean.getRosterId() ) )
        {
          nodeCharacteristic.setRosterNodeCharId( UUID.fromString( formBean.getRosterId() ) );
        }
        nodeCharacteristic.setVersion( formBean.getVersion() );
        nodeCharacteristic.getAuditCreateInfo().setCreatedBy( Long.valueOf( formBean.getCreatedBy() ) );
        nodeCharacteristic.getAuditCreateInfo().setDateCreated( new Timestamp( formBean.getDateCreated() ) );

        // Multi_Select types will have their values in a String[] characteristicValues
        // so loop through the String[] and store them as a "," separated string.
        if ( formBean.getCharacteristicDataType().equals( "multi_select" ) )
        {
          String characteristicValue = "";
          for ( int j = 0; j < formBean.getCharacteristicValues().length; j++ )
          {
            if ( characteristicValue.equals( "" ) )
            {
              characteristicValue = formBean.getCharacteristicValues()[j];
            }
            else
            {
              characteristicValue = characteristicValue + "," + formBean.getCharacteristicValues()[j];
            }
          }
          nodeCharacteristic.setCharacteristicValue( characteristicValue );
        }
        else
        {
          nodeCharacteristic.setCharacteristicValue( formBean.getCharacteristicValue() );
        }

        // Add this nodeCharacteristic object to the list.
        if ( nodeCharacteristic.getCharacteristicValue() != null && !nodeCharacteristic.getCharacteristicValue().equals( "" ) )
        {
          nodeCharacteristics.add( nodeCharacteristic );
        }
      }
    }

    return nodeCharacteristics;
  }

  /**
   * This will create a list of ClaimProductCharacteristic objects consisting of a characteristic
   * object (containing only characteristicId, the object will be looked up in the service), and a
   * characteristicValue.
   * 
   * @param valueList
   * @return Collection
   */
  public static Collection toListOfClaimProductCharacteristicDomainObjects( List valueList )
  {
    List claimProductCharacteristics = new ArrayList();

    // loop through the valueList to get values from the form.
    for ( int i = 0; i < valueList.size(); i++ )
    {
      // get each CharacteristicFormBean from the valueList
      CharacteristicValueBean formBean = getValueInfo( i, valueList );

      // Create a new ClaimProductCharacteristic object if there is a characteristic value
      // tied to the object.
      // if ( (formBean.getCharacteristicValue() != null &&
      // ! formBean.getCharacteristicValue().equals("")) ||
      // (formBean.getCharacteristicValues() != null &&
      // formBean.getCharacteristicValues().length > 0) )
      // {
      // Create the new ClaimProductCharacteristic Object
      ClaimProductCharacteristic claimProductCharacteristic = new ClaimProductCharacteristic();

      // Create a new Characteristic object to put into the claimProductCharacteristic object
      ProductCharacteristicType productCharacteristicType = new ProductCharacteristicType();

      // Set the Characteristic ID from the formBean.
      productCharacteristicType.setId( formBean.getCharacteristicId() );
      productCharacteristicType.setVersion( formBean.getVersion() );
      productCharacteristicType.setCharacteristicName( formBean.getCharacteristicName() );
      productCharacteristicType.setCmAssetCode( formBean.getCmAssetCode() );
      productCharacteristicType.setNameCmKey( formBean.getNameCmKey() );
      productCharacteristicType.setCharacteristicDataType( CharacteristicDataType.lookup( formBean.getCharacteristicDataType() ) );
      // set the unique (for products)
      productCharacteristicType.setIsUnique( formBean.getIsUnique() );
      productCharacteristicType.setIsRequired( formBean.getIsRequired() );

      // Set the Characteristic object, and the claimProductCharacteristic info into the
      // claimProductCharacteristic Object, no need to set the product object here,
      // that will be handled in the Service.
      claimProductCharacteristic.setProductCharacteristicType( productCharacteristicType );

      if ( formBean.getJoinTableId() != null && formBean.getJoinTableId().longValue() != 0 )
      {
        claimProductCharacteristic.setId( formBean.getJoinTableId() );
      }
      claimProductCharacteristic.setVersion( formBean.getVersion() );
      claimProductCharacteristic.getAuditCreateInfo().setCreatedBy( Long.valueOf( formBean.getCreatedBy() ) );
      claimProductCharacteristic.getAuditCreateInfo().setDateCreated( new Timestamp( formBean.getDateCreated() ) );
      // Multi_Select types will have their values in a String[] characteristicValues
      // so loop through the String[] and store them as a "," separated string.
      if ( formBean.getCharacteristicDataType().equals( "multi_select" ) )
      {
        if ( formBean.getCharacteristicValues() != null )
        {
          String characteristicValue = "";
          for ( int j = 0; j < formBean.getCharacteristicValues().length; j++ )
          {
            if ( characteristicValue.equals( "" ) )
            {
              characteristicValue = formBean.getCharacteristicValues()[j];
            }
            else
            {
              characteristicValue = characteristicValue + "," + formBean.getCharacteristicValues()[j];
            }
          }
          claimProductCharacteristic.setValue( characteristicValue );
        }
      }
      else
      {
        claimProductCharacteristic.setValue( formBean.getCharacteristicValue() );
      }

      // Add this claimProductCharacteristic object to the list.
      claimProductCharacteristics.add( claimProductCharacteristic );

      // } // end if
    } // end for

    return claimProductCharacteristics;
  }

  /**
   * Validate the values entered for UserCharacteristics. Made this public static so it can be used
   * by other forms to validate characteristics.
   * 
   * @param characteristicValueList is a list of UserCharacteristicFormBeans
   * @param actionErrors
   */
  public static void validateCharacteristicValueList( List characteristicValueList, ActionErrors actionErrors )
  {
    List serviceErrors = CharacteristicValidationUtils.validateCharacteristicValueList( characteristicValueList );
    ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, actionErrors );
  }

  /**
   * @param values
   * @return String array of the characteristic values
   */
  private static String[] parseCharacteristicValues( String values )
  {
    String[] characteristicValues;

    if ( values.indexOf( "," ) != -1 )
    {
      StringTokenizer tokens = new StringTokenizer( values, "," );
      characteristicValues = new String[tokens.countTokens()];
      int i = 0;
      while ( tokens.hasMoreTokens() )
      {
        characteristicValues[i] = tokens.nextToken();
        i++;
      }
    }
    else
    {
      characteristicValues = new String[1];
      characteristicValues[0] = values;
    }

    return characteristicValues;
  }

  /**
   * resets the value list with empty UserCharacteristicFormBeans
   * 
   * @param valueListCount
   * @return List
   */
  public static List getEmptyValueList( int valueListCount )
  {
    List valueList = new ArrayList();

    for ( int i = 0; i < valueListCount; i++ )
    {
      valueList.add( new CharacteristicValueBean() );
    }

    return valueList;
  }

  /**
   * Accessor for the value list
   * 
   * @param index
   * @param valueList
   * @return Single instance of CharacteristicFormBean from the value list
   */
  public static CharacteristicValueBean getValueInfo( int index, List valueList )
  {
    try
    {
      return (CharacteristicValueBean)valueList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }
}
