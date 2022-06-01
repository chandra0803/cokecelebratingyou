
package com.biperf.core.value.nomination;

public class NominationDetailInfoFieldsViewBean
{
  private Long sequenceNumber;
  private String label;
  private String value;
  private String type;
  private Long fieldId;

  public Long getSequenceNumber()
  {
    return sequenceNumber;
  }

  public void setSequenceNumber( Long sequenceNumber )
  {
    this.sequenceNumber = sequenceNumber;
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel( String label )
  {
    this.label = label;
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

  public Long getFieldId()
  {
    return fieldId;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public void setFieldId( Long fieldId )
  {
    this.fieldId = fieldId;
  }

}
