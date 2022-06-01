/**
 * 
 */

package com.biperf.core.value;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author poddutur
 *
 */
public class ProductClaimCharacteristicsPreviewBean implements Serializable
{
  private String name;
  private String value;
  private String[] values;
  private String type;
  private Long maxSize;
  private Long id;
  private Boolean required;
  private Boolean unique;
  private String dateStart;
  private String dateEnd;
  private Long min;
  private Long max;
  private ArrayList<CharacteristicsTypeValuePreviewBean> list = new ArrayList<CharacteristicsTypeValuePreviewBean>();

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue( String value )
  {
    this.value = value;
  }

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public String[] getValues()
  {
    return values;
  }

  public void setValues( String[] values )
  {
    this.values = values;
  }

  public Long getMaxSize()
  {
    return maxSize;
  }

  public void setMaxSize( Long maxSize )
  {
    this.maxSize = maxSize;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public Boolean getRequired()
  {
    return required;
  }

  public void setRequired( Boolean required )
  {
    this.required = required;
  }

  public Boolean getUnique()
  {
    return unique;
  }

  public void setUnique( Boolean unique )
  {
    this.unique = unique;
  }

  public String getDateStart()
  {
    return dateStart;
  }

  public void setDateStart( String dateStart )
  {
    this.dateStart = dateStart;
  }

  public String getDateEnd()
  {
    return dateEnd;
  }

  public void setDateEnd( String dateEnd )
  {
    this.dateEnd = dateEnd;
  }

  public Long getMin()
  {
    return min;
  }

  public void setMin( Long min )
  {
    this.min = min;
  }

  public Long getMax()
  {
    return max;
  }

  public void setMax( Long max )
  {
    this.max = max;
  }

  public int getListCount()
  {
    if ( this.list == null || this.list.isEmpty() )
    {
      return 0;
    }
    return this.list.size();
  }

  public CharacteristicsTypeValuePreviewBean getList( int index )
  {
    while ( index >= list.size() )
    {
      list.add( new CharacteristicsTypeValuePreviewBean() );
    }
    return (CharacteristicsTypeValuePreviewBean)list.get( index );
  }

  public ArrayList<CharacteristicsTypeValuePreviewBean> getCharacteristicsTypeListValues()
  {
    return list;
  }

  public void setList( CharacteristicsTypeValuePreviewBean listBean )
  {
    this.list.add( listBean );
  }

  public static class CharacteristicsTypeValuePreviewBean implements Serializable
  {
    private String id;
    private String name;

    public String getId()
    {
      return id;
    }

    public void setId( String id )
    {
      this.id = id;
    }

    public String getName()
    {
      return name;
    }

    public void setName( String name )
    {
      this.name = name;
    }

  }

}
