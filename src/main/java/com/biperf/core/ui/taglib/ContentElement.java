
package com.biperf.core.ui.taglib;

import java.util.HashSet;
import java.util.Set;

public class ContentElement
{
  private static final String TOMCAT_JSP_NAMING_CONVENTION = "org.apache.jsp.";// org.apache.jsp.layouts.g5BaseLayout_jsp
                                                                               // <-- Template

  private Long assetId;
  private Long contentKeyId;
  private String key;
  private String value;
  private Set<Object> pages = new HashSet<Object>();
  private String urlPrefix;

  public String getUrl()
  {
    return urlPrefix + "/assetDetail.do?assetId=" + assetId;
  }

  public Long getContentKeyId()
  {
    return contentKeyId;
  }

  public void setContentKeyId( Long contentKeyId )
  {
    this.contentKeyId = contentKeyId;
  }

  public String getKey()
  {
    return key;
  }

  public void setKey( String key )
  {
    this.key = key;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue( String value )
  {
    this.value = value;
  }

  public Set<Object> getPages()
  {
    return pages;
  }

  public void setPages( Set<Object> pages )
  {
    this.pages = pages;
  }

  public String getUrlPrefix()
  {
    return urlPrefix;
  }

  public void setUrlPrefix( String urlPrefix )
  {
    this.urlPrefix = urlPrefix;
  }

  public void addPage( Object page )
  {
    String pageValue = page.toString();
    if ( pageValue.startsWith( TOMCAT_JSP_NAMING_CONVENTION ) )
    {
      pageValue = "/" + pageValue.substring( TOMCAT_JSP_NAMING_CONVENTION.length(), pageValue.indexOf( "@" ) ).replace( ".", "/" ).replace( "_jsp", ".jsp" );
    }
    this.pages.add( pageValue );
  }

  public Long getAssetId()
  {
    return assetId;
  }

  public void setAssetId( Long assetId )
  {
    this.assetId = assetId;
  }

  @Override
  public String toString()
  {
    int limit = 40;
    int end = value.length() < limit ? value.length() : limit;
    String continued = value.length() > 20 ? "..." : "";
    return "ContentElement [contentKeyId=" + contentKeyId + ", key=" + key + ", value=" + value.substring( 0, end ) + continued + ", pages=" + pages + "]";
  }
}
