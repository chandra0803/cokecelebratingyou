
package com.biperf.core.domain.enums;

public enum CustomFormElementAddress
{
  SEC_HEADING( 1, "it  has to come from ClaimFormStepElement getI18nLabel", "sect_head", false, "sect_head" ), COUNTRY( 2, "participant.address.COUNRTY_LABEL", "selection", true, "country" ), ADDR1(
      3, "participant.address.ADDR1_LABEL", "text", true, "address1" ), ADDR2( 4, "participant.address.ADDR2_LABEL", "text", false, "address2" ), ADDR3( 5, "participant.address.ADDR3_LABEL", "text",
          false, "address3" ), CITY( 6, "participant.address.CITY_LABEL", "text", true,
              "city" ), STATE( 7, "participant.address.STATE_LABEL", "selection", true, "state" ), POSTAL_CODE( 8, "participant.participant.POSTAL_CODE", "text", true, "postal" );

  private String cmAssetCode;
  private Integer index;
  private String type;
  private boolean required;
  private String subType;

  public static final String FIELD_GROUP = "address";
  public static final String HEADER = "header";

  private CustomFormElementAddress( Integer index, String cmAssetCode, String type, boolean required, String subType )
  {
    this.index = index;
    this.cmAssetCode = cmAssetCode;
    this.type = type;
    this.required = required;
    this.subType = subType;
  }

  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  public Integer getIndex()
  {
    return index;
  }

  public String getType()
  {
    return type;
  }

  public boolean isRequired()
  {
    return required;
  }

  public String getSubType()
  {
    return subType;
  }

  public boolean isSelectField()
  {
    return "selection".equalsIgnoreCase( getType() );
  }

  public boolean isCountryField()
  {
    return "country".equalsIgnoreCase( getSubType() );
  }

  public boolean isState()
  {
    return "state".equalsIgnoreCase( getSubType() );
  }

  public boolean isSecHead()
  {
    return "sect_head".equalsIgnoreCase( getType() );
  }

}
