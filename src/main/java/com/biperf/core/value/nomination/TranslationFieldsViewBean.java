
package com.biperf.core.value.nomination;

public class TranslationFieldsViewBean
{
  private Long fieldId;
  private String name;
  private String text;

  public Long getFieldId()
  {
    return fieldId;
  }

  public String getName()
  {
    return name;
  }

  public String getText()
  {
    return text;
  }

  public void setFieldId( Long fieldId )
  {
    this.fieldId = fieldId;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public void setText( String text )
  {
    this.text = text;
  }
}
