
package com.biw.hc.core.service.impl;

public enum HCHeaderEnum
{

  CLIENT_CODE( "clientcode" ), 
  CLIENT_ID( "clientid" ),
  USER_ID( "userid" ), 
  USER_NAME( "username" ),
  SIGNATURE( "signature" ), 
  CONTENT_TYPE( "content-type" ), 
  ACCEPT( "accept" ), 
  LOCALE( "locale" ), 
  PRODUCT( "product" ), 
  VERSION( "version" );

  private String header;

  HCHeaderEnum( String header )
  {
    this.header = header;
  }

  public String getHeader()
  {
    return header;
  }

}
