/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/budget/CharacteristicFormBean.java,v $
 */

package com.biperf.core.ui.budget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.ui.BaseFormBean;

/**
 * CharacteristicFormBean.
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
 * <td>meadows</td>
 * <td>Aug 16, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CharacteristicFormBean extends BaseFormBean
{
  public static final int IS_ONE_OF = 1;
  public static final int IS_ALL_OF = 2;

  public static final int STARTS_WITH = 1;
  public static final int DOES_NOT_START_WITH = 2;
  public static final int CONTAINS = 3;
  public static final int DOES_NOT_CONTAIN = 4;
  public static final int ENDS_WITH = 5;
  public static final int DOES_NOT_END_WITH = 6;
  private static Map CONSTANT_MAP;

  private Long characteristicId;
  private String nameCmKey;
  private String cmAssetCode;
  private CharacteristicDataType dataType;
  private String lowValue;
  private String highValue;
  private List checkList;
  private String characteristicPossibleValues;
  private String value;
  private String lowDate;
  private String highDate;
  private String optionValue;
  private boolean isSelected;
  private boolean booleanSet;
  /* This characteristic should be displayed in the query results */
  private boolean displaySelected;
  private Long nodeTypeId;

  static
  {
    CONSTANT_MAP = new ConcurrentHashMap();
    CONSTANT_MAP.put( "isOneOf", new Integer( IS_ONE_OF ) );
    CONSTANT_MAP.put( "isAllOf", new Integer( IS_ALL_OF ) );
    CONSTANT_MAP.put( "startsWith", new Integer( STARTS_WITH ) );
    CONSTANT_MAP.put( "doesNotStartWith", new Integer( DOES_NOT_START_WITH ) );
    CONSTANT_MAP.put( "contains", new Integer( CONTAINS ) );
    CONSTANT_MAP.put( "doesNotContain", new Integer( DOES_NOT_CONTAIN ) );
    CONSTANT_MAP.put( "endsWith", new Integer( ENDS_WITH ) );
    CONSTANT_MAP.put( "doesNotEndWith", new Integer( DOES_NOT_END_WITH ) );
  }

  public CharacteristicFormBean()
  {
    super();
    setBooleanSet( true );
  }

  public Map getConstants()
  {
    return CONSTANT_MAP;
  }

  public Long getCharacteristicId()
  {
    return characteristicId;
  }

  public void setCharacteristicId( Long characteristicId )
  {
    this.characteristicId = characteristicId;
  }

  public CharacteristicDataType getDataType()
  {
    return dataType;
  }

  public void setDataType( CharacteristicDataType dataType )
  {
    this.dataType = dataType;
  }

  public String getHighValue()
  {
    return highValue;
  }

  public void setHighValue( String highValue )
  {
    this.highValue = highValue;
  }

  public String getLowValue()
  {
    return lowValue;
  }

  public void setLowValue( String lowValue )
  {
    this.lowValue = lowValue;
  }

  public List getCheckList()
  {
    if ( checkList == null )
    {
      checkList = new ArrayList();
    }
    return checkList;
  }

  public void setCheckList( List checkList )
  {
    this.checkList = checkList;
  }

  public String getPossibleValuesAsString()
  {
    StringBuffer buffer = new StringBuffer();
    if ( getCheckList() != null )
    {
      for ( Iterator iter = getCheckList().iterator(); iter.hasNext(); )
      {
        CheckItem checkItem = (CheckItem)iter.next();
        if ( buffer.length() > 0 )
        {
          buffer.append( ',' );
        }
        buffer.append( checkItem.getCode() );
      }
    }
    return buffer.toString();
  }

  public CheckItem getCheckItem( int index )
  {
    while ( getCheckList().size() <= index )
    {
      getCheckList().add( new CheckItem() );
    }
    return (CheckItem)getCheckList().get( index );
  }

  public String getCharacteristicPossibleValues()
  {
    return characteristicPossibleValues;
  }

  public String[] getCharacteristicPossibleValuesAsArray()
  {
    String[] possibleValueArray = parseCharacteristicValues( getCharacteristicPossibleValues() );
    return possibleValueArray;
  }

  public void setCharacteristicPossibleValues( String characteristicPossibleValues )
  {
    this.characteristicPossibleValues = characteristicPossibleValues;
  }

  public String getPossibleValue( int index )
  {
    String[] possibleValueArray = parseCharacteristicValues( getCharacteristicPossibleValues() );
    return possibleValueArray[index];
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

  public String getValue()
  {
    return value;
  }

  public void setValue( String value )
  {
    this.value = value;
  }

  public String getHighDate()
  {
    return highDate;
  }

  public void setHighDate( String highDate )
  {
    this.highDate = highDate;
  }

  public String getLowDate()
  {
    return lowDate;
  }

  public void setLowDate( String lowDate )
  {
    this.lowDate = lowDate;
  }

  public String getOptionValue()
  {
    return optionValue;
  }

  public void setOptionValue( String optionValue )
  {
    this.optionValue = optionValue;
  }

  public boolean isSelected()
  {
    return isSelected;
  }

  public void setSelected( boolean isSelected )
  {
    this.isSelected = isSelected;
  }

  public boolean isBooleanSet()
  {
    return booleanSet;
  }

  public void setBooleanSet( boolean booleanSet )
  {
    this.booleanSet = booleanSet;
  }

  public boolean isDisplaySelected()
  {
    return displaySelected;
  }

  public void setDisplaySelected( boolean displaySelected )
  {
    this.displaySelected = displaySelected;
  }

  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  public void setCmAssetCode( String cmAssetCode )
  {
    this.cmAssetCode = cmAssetCode;
  }

  public String getNameCmKey()
  {
    return nameCmKey;
  }

  public void setNameCmKey( String nameCmKey )
  {
    this.nameCmKey = nameCmKey;
  }

  public Long getNodeTypeId()
  {
    return nodeTypeId;
  }

  public void setNodeTypeId( Long nodeTypeId )
  {
    this.nodeTypeId = nodeTypeId;
  }

}
