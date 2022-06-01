
package com.biperf.core.ui.roster.enums;

import java.util.Arrays;

public enum GenericTypeEnum
{

  Type_HOME( "home", "hom" ), Type_HOM( "hom", "home" ), TYPE_BUSINESS( "business", "bus" ), TYPE_BUS( "bus", "business" ), TYPE_PERSONAL( "personal", "oth" ), TYPE_RECOVERY( "recovery",
      "rec" ), TYPE_REC( "rec", "recovery" ), TYPE_MOBILE( "mobile", "mob" ), TYPE_MOB( "mob", "mobile" ), TYPE_SMS( "sms", "sms" ), TYPE_OTH( "oth", "other" ), TYPE_OTHER( "other",
          "oth" ), TYPE_SHP( "shp", "shipping" ), TYPE_SHOP( "shipping", "shp" ), TYPE_PAX( "pax", "pax" ), TYPE_FIXED( "fixed",
              "pax" ), TYPE_CRITERIA( "criteria", "criteria" ), TYPE_DYNAMIC( "dynamic", "criteria" ), TYPE_FAX( "fax", "fax" ), TYPE_MALE( "male", "m" ), TYPE_FEMALE( "female", "f" );

  private final String code;
  private final String value;

  private GenericTypeEnum( String code, String value )
  {
    this.code = code;
    this.value = value;
  }

  public static GenericTypeEnum findTypeByCode( String code )
  {
    return Arrays.stream( values() ).filter( status -> status.getCode().equalsIgnoreCase( code ) ).findFirst().orElse( null );
  }

  public String getCode()
  {
    return code;
  }

  public String getValue()
  {
    return value;
  }
}
