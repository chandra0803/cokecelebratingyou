
package com.biperf.core.value.promotion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.CollectionUtils;

import com.biperf.core.domain.enums.ClaimFormElementType;
import com.biperf.core.domain.enums.CustomFormElementAddress;
import com.biperf.core.utils.StringUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder( { "formId", "stepId", "fields" } )
public class CustomFormStepElementsView
{

  @JsonProperty( "formId" )
  private Long formId;

  @JsonProperty( "stepId" )
  private Long stepId;

  @JsonProperty( "customWhyId" )
  private Long customWhyId;

  @JsonProperty( "customWhyCharCount" )
  private Integer customWhyCharCount;

  @JsonProperty( "fields" )
  private List<CustomFormFieldView> fields;

  /**
   * 
   * @return
   *     The formId
   */
  @JsonProperty( "formId" )
  public Long getFormId()
  {
    return formId;
  }

  /**
   * 
   * @param formId
   *     The formId
   */
  @JsonProperty( "formId" )
  public void setFormId( Long formId )
  {
    this.formId = formId;
  }

  /**
   * 
   * @return
   *     The stepId
   */
  @JsonProperty( "stepId" )
  public Long getStepId()
  {
    return stepId;
  }

  /**
   * 
   * @param stepId
   *     The stepId
   */
  @JsonProperty( "stepId" )
  public void setStepId( Long stepId )
  {
    this.stepId = stepId;
  }

  @JsonProperty( "customWhyId" )
  public Long getCustomWhyId()
  {
    return customWhyId;
  }

  @JsonProperty( "customWhyId" )
  public void setCustomWhyId( Long customWhyId )
  {
    this.customWhyId = customWhyId;
  }

  @JsonProperty( "customWhyCharCount" )
  public Integer getCustomWhyCharCount()
  {
    return customWhyCharCount;
  }

  @JsonProperty( "customWhyCharCount" )
  public void setCustomWhyCharCount( Integer customWhyCharCount )
  {
    this.customWhyCharCount = customWhyCharCount;
  }

  /**
   * 
   * @return
   *     The fields
   */
  @SuppressWarnings( "unchecked" )
  @JsonProperty( "fields" )
  public List<CustomFormFieldView> getFields() // because of struts bean copy property
  {

    if ( fields == null )
    {
      Factory factory = new Factory()
      {
        public Object create()
        {
          return new CustomFormFieldView();
        }
      };
      fields = ListUtils.lazyList( new ArrayList<>(), factory );
    }
    return fields;
  }

  /**
   * 
   * @param fields
   *     The fields
   */
  @JsonProperty( "fields" )
  public void setFields( List<CustomFormFieldView> fields )
  {
    this.fields = fields;
  }

  @JsonIgnore
  @Override
  public String toString()
  {
    return ToStringBuilder.reflectionToString( this );
  }

  @JsonIgnore
  public Map<Long, List<CustomFormFieldView>> getAddressGroups()
  {

    Map<Long, List<CustomFormFieldView>> map = new HashMap<Long, List<CustomFormFieldView>>();

    for ( CustomFormFieldView field : fields )
    {

      if ( field.getFieldGroup() == null )
      {
        continue;
      }

      Long stepElementId = field.getId();
      List<CustomFormFieldView> fieldList = map.get( stepElementId );

      if ( CollectionUtils.isEmpty( fieldList ) )
      {
        List<CustomFormFieldView> l = new ArrayList<CustomFormFieldView>();
        l.add( field );
        map.put( stepElementId, l );

      }
      else
      {
        map.get( stepElementId ).add( field );
      }
    }
    return map;
  }

  @JsonIgnore
  public Map<Long, List<CustomFormFieldView>> getAddressGroupsWithOutSecHeading()
  {

    Map<Long, List<CustomFormFieldView>> addressGroups = this.getAddressGroups();

    Set<Entry<Long, List<CustomFormFieldView>>> entrySet = addressGroups.entrySet();

    List<CustomFormFieldView> withOutSecHeading;

    for ( Entry<Long, List<CustomFormFieldView>> entry : entrySet )
    {
      withOutSecHeading = new ArrayList<CustomFormFieldView>();
      List<CustomFormFieldView> fields = entry.getValue();

      for ( CustomFormFieldView customFormField : fields )
      {
        if ( !ClaimFormElementType.SECTION_HEADING.equalsIgnoreCase( customFormField.getType() ) )
        {
          withOutSecHeading.add( customFormField );
        }

      }

      addressGroups.get( entry.getKey() ).clear();
      addressGroups.get( entry.getKey() ).addAll( withOutSecHeading );

    }

    return addressGroups;
  }

  @JsonIgnore
  public Map<Long, String> getAddressGroupValue()
  {
    Map<Long, String> result = new HashMap<Long, String>();
    Map<Long, List<CustomFormFieldView>> addressGroups = this.getAddressGroups();
    Set<Entry<Long, List<CustomFormFieldView>>> entrySet = addressGroups.entrySet();

    for ( Entry<Long, List<CustomFormFieldView>> entry : entrySet )
    {
      List<CustomFormFieldView> fields = entry.getValue();
      StringBuilder stringBuilder = new StringBuilder();
      Long id = null;
      for ( CustomFormFieldView f : fields )
      {
        id = f.getId();
        stringBuilder.append( f.getValue() ).append( "|" );
      }

      if ( !StringUtil.isEmpty( stringBuilder.toString() ) )
      {
        String s = stringBuilder.toString();
        result.put( id, s.substring( 0, s.length() - 1 ) );
      }

    }

    return result;
  }

  @JsonIgnore
  public List<CustomFormFieldView> getNonGroupFields()
  {
    List<CustomFormFieldView> nonGroupFields = new ArrayList<CustomFormFieldView>();
    for ( CustomFormFieldView f : getFields() )
    {
      if ( !CustomFormElementAddress.FIELD_GROUP.equalsIgnoreCase( f.getFieldGroup() ) )
      {
        nonGroupFields.add( f );
      }
    }
    return nonGroupFields;
  }

  @JsonIgnore
  public Map<Long, CustomFormFieldView> getNonGroupFieldsByElementStepId()
  {

    Map<Long, CustomFormFieldView> map = new HashMap<Long, CustomFormFieldView>();

    List<CustomFormFieldView> nonGroupFields = this.getNonGroupFields();

    for ( CustomFormFieldView f : nonGroupFields )
    {
      map.put( f.getId(), f );
    }
    return map;
  }

  @JsonIgnore
  public Map<Long, String> getStepElementIdWithValue()
  {
    Map<Long, String> result = new HashMap<Long, String>();

    for ( CustomFormFieldView f : this.getNonGroupFields() )
    {
      result.put( f.getId(), f.getValue() );
    }
    result.putAll( this.getAddressGroupValue() );
    return result;

  }

}
