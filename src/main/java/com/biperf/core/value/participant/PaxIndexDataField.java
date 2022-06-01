
package com.biperf.core.value.participant;

/**
 * field  mapping of pax index data
 * @see PaxIndexData.java
 */
public enum PaxIndexDataField
{
  FIRST_NAME( "firstNameDisplay" ), LAST_NAME( "lastNameDisplay" ), POSITION_TYPE_CODE( "positionTypeCode" ), DEPARTMENT_TYPE_CODE( "departmentTypeCode" ), NODE_ID( "nodeId" ), COUNTRY_ID(
      "countryId" ), AUDIENCE_IDS( "audienceIds" );

  private String fieldName;

  private PaxIndexDataField( String filed )
  {
    this.fieldName = filed;
  }

  public String getFieldName()
  {
    return fieldName;
  }
}
