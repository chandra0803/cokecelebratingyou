
package com.biperf.core.value.promotion;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder( { "id",
                      "sequenceNumber",
                      "label",
                      "copyValue",
                      "name",
                      "type",
                      "value",
                      "isRequired",
                      "maxDecimal",
                      "mask",
                      "fieldGroup",
                      "subType",
                      "selectList",
                      "linkName",
                      "linkUrl",
                      "trueLabel",
                      "falseLabel",
                      "copy" } )
public class CustomFormFieldView
{

  @JsonProperty( "id" )
  private Long id;

  @JsonProperty( "sequenceNumber" )
  private Integer sequenceNumber;

  @JsonProperty( "label" )
  private String label;

  @JsonProperty( "name" )
  private String name;

  @JsonProperty( "copyValue" )
  private String copyValue;

  @JsonProperty( "type" )
  private String type;

  @JsonProperty( "value" )
  private String value;

  @JsonProperty( "isRequired" )
  private Boolean isRequired;

  @JsonProperty( "maxDecimal" )
  private Long maxDecimal;

  @JsonProperty( "mask" )
  private Boolean mask;

  @JsonProperty( "fieldGroup" )
  private String fieldGroup;

  @JsonProperty( "subType" )
  private String subType;

  @JsonProperty( "selectList" )
  private List<CustomFormSelectListView> selectList;

  @JsonProperty( "linkName" )
  private String linkName;

  @JsonProperty( "linkUrl" )
  private String linkUrl;

  @JsonProperty( "trueLabel" )
  private String trueLabel;

  @JsonProperty( "falseLabel" )
  private String falseLabel;

  @JsonProperty( "copy" )
  private String copy;

  @JsonProperty( "size" )
  private Integer size;

  @JsonProperty( "format" )
  private String format;

  @JsonProperty( "customWhyCharCount" )
  private Integer customWhyCharCount;
  /**
   * 
   * @return
   *     The id
   */
  @JsonProperty( "id" )
  public Long getId()
  {
    return id;
  }

  /**
   * 
   * @param id
   *     The id
   */
  @JsonProperty( "id" )
  public void setId( Long id )
  {
    this.id = id;
  }

  /**
   * 
   * @return
   *     The sequenceNumber
   */
  @JsonProperty( "sequenceNumber" )
  public Integer getSequenceNumber()
  {
    return sequenceNumber;
  }

  /**
   * 
   * @param sequenceNumber
   *     The sequenceNumber
   */
  @JsonProperty( "sequenceNumber" )
  public void setSequenceNumber( Integer sequenceNumber )
  {
    this.sequenceNumber = sequenceNumber;
  }

  /**
   * 
   * @return
   *     The label
   */
  @JsonProperty( "label" )
  public String getLabel()
  {
    return label;
  }

  /**
   * 
   * @param label
   *     The label
   */
  @JsonProperty( "label" )
  public void setLabel( String label )
  {
    this.label = label;
  }

  /**
   * 
   * @return
   *     The copyValue
   */
  @JsonProperty( "copyValue" )
  public String getCopyValue()
  {
    return copyValue;
  }

  /**
   * 
   * @param copyValue
   *     The copyValue
   */
  @JsonProperty( "copyValue" )
  public void setCopyValue( String copyValue )
  {
    this.copyValue = copyValue;
  }

  /**
   * 
   * @return
   *     The name
   */
  @JsonProperty( "name" )
  public String getName()
  {
    return name;
  }

  /**
   * 
   * @param name
   *     The name
   */
  @JsonProperty( "name" )
  public void setName( String name )
  {
    this.name = name;
  }

  /**
   * 
   * @return
   *     The type
   */
  @JsonProperty( "type" )
  public String getType()
  {
    return type;
  }

  /**
   * 
   * @param type
   *     The type
   */
  @JsonProperty( "type" )
  public void setType( String type )
  {
    this.type = type;
  }

  /**
   * 
   * @return
   *     The isRequired
   */
  @JsonProperty( "isRequired" )
  public Boolean getIsRequired()
  {
    return isRequired;
  }

  /**
   * 
   * @param isRequired
   *     The isRequired
   */
  @JsonProperty( "isRequired" )
  public void setIsRequired( Boolean isRequired )
  {
    this.isRequired = isRequired;
  }

  /**
   * 
   * @return
   *     The maxDecimal
   */
  @JsonProperty( "maxDecimal" )
  public Long getMaxDecimal()
  {
    return maxDecimal;
  }

  /**
   * 
   * @param maxDecimal
   *     The maxDecimal
   */
  @JsonProperty( "maxDecimal" )
  public void setMaxDecimal( Long maxDecimal )
  {
    this.maxDecimal = maxDecimal;
  }

  /**
   * 
   * @return
   *     The mask
   */
  @JsonProperty( "mask" )
  public Boolean getMask()
  {
    return mask;
  }

  /**
   * 
   * @param mask
   *     The mask
   */
  @JsonProperty( "mask" )
  public void setMask( Boolean mask )
  {
    this.mask = mask;
  }

  /**
   * 
   * @return
   *     The fieldGroup
   */
  @JsonProperty( "fieldGroup" )
  public String getFieldGroup()
  {
    return fieldGroup;
  }

  /**
   * 
   * @param fieldGroup
   *     The fieldGroup
   */
  @JsonProperty( "fieldGroup" )
  public void setFieldGroup( String fieldGroup )
  {
    this.fieldGroup = fieldGroup;
  }

  /**
   * 
   * @return
   *     The subType
   */
  @JsonProperty( "subType" )
  public String getSubType()
  {
    return subType;
  }

  /**
   * 
   * @param subType
   *     The subType
   */
  @JsonProperty( "subType" )
  public void setSubType( String subType )
  {
    this.subType = subType;
  }

  /**
   * 
   * @return
   *     The selectList
   */
  @SuppressWarnings( "unchecked" )
  @JsonProperty( "selectList" )
  public List<CustomFormSelectListView> getSelectList()
  {

    if ( selectList == null )
    {
      Factory factory = new Factory()
      {
        public Object create()
        {
          return new CustomFormSelectListView();
        }
      };
      selectList = ListUtils.lazyList( new ArrayList<>(), factory );
    }
    return selectList;

  }

  /**
   * 
   * @param selectList
   *     The selectList
   */
  @JsonProperty( "selectList" )
  public void setSelectList( List<CustomFormSelectListView> selectList )
  {
    this.selectList = selectList;
  }

  /**
   * 
   * @return
   *     The linkName
   */
  @JsonProperty( "linkName" )
  public String getLinkName()
  {
    return linkName;
  }

  /**
   * 
   * @param linkName
   *     The linkName
   */
  @JsonProperty( "linkName" )
  public void setLinkName( String linkName )
  {
    this.linkName = linkName;
  }

  /**
   * 
   * @return
   *     The linkUrl
   */
  @JsonProperty( "linkUrl" )
  public String getLinkUrl()
  {
    return linkUrl;
  }

  /**
   * 
   * @param linkUrl
   *     The linkUrl
   */
  @JsonProperty( "linkUrl" )
  public void setLinkUrl( String linkUrl )
  {
    this.linkUrl = linkUrl;
  }

  /**
   * 
   * @return
   *     The trueLabel
   */
  @JsonProperty( "trueLabel" )
  public String getTrueLabel()
  {
    return trueLabel;
  }

  /**
   * 
   * @param trueLabel
   *     The trueLabel
   */
  @JsonProperty( "trueLabel" )
  public void setTrueLabel( String trueLabel )
  {
    this.trueLabel = trueLabel;
  }

  /**
   * 
   * @return
   *     The falseLabel
   */
  @JsonProperty( "falseLabel" )
  public String getFalseLabel()
  {
    return falseLabel;
  }

  /**
   * 
   * @param falseLabel
   *     The falseLabel
   */
  @JsonProperty( "falseLabel" )
  public void setFalseLabel( String falseLabel )
  {
    this.falseLabel = falseLabel;
  }

  /**
   * 
   * @return
   *     The copy
   */
  @JsonProperty( "copy" )
  public String getCopy()
  {
    return copy;
  }

  /**
   * 
   * @param copy
   *     The copy
   */
  @JsonProperty( "copy" )
  public void setCopy( String copy )
  {
    this.copy = copy;
  }

  @Override
  public String toString()
  {
    return ToStringBuilder.reflectionToString( this );
  }

  @JsonProperty( "value" )
  public String getValue()
  {
    return value;
  }

  @JsonProperty( "value" )
  public void setValue( String value )
  {
    this.value = value;
  }

  @JsonProperty( "size" )
  public Integer getSize()
  {
    return size;
  }

  @JsonProperty( "size" )
  public void setSize( Integer size )
  {
    this.size = size;
  }

  @JsonProperty( "format" )
  public String getFormat()
  {
    return format;
  }

  @JsonProperty( "format" )
  public void setFormat( String format )
  {
    this.format = format;
  }
  
  public Integer getCustomWhyCharCount()
  {
    return customWhyCharCount;
  }

  public void setCustomWhyCharCount( Integer customWhyCharCount )
  {
    this.customWhyCharCount = customWhyCharCount;
  }

}
