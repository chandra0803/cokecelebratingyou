
package com.biperf.core.value.ssi;

/**
 * 
 * SSIContestDocumentView.
 * 
 * @author kandhi
 * @since Nov 10, 2014
 * @version 1.0
 */
public class SSIContestDocumentValueBean
{
  private String language;
  private String id;
  private String name;
  private String url;
  private String originalFilename;

  public SSIContestDocumentValueBean()
  {
    super();
  }

  public SSIContestDocumentValueBean( String language, String id, String name, String url, String originalFilename )
  {
    super();
    this.language = language;
    this.id = id;
    this.name = name;
    this.url = url;
    this.originalFilename = originalFilename;
  }

  public String getLanguage()
  {
    return language;
  }

  public void setLanguage( String language )
  {
    this.language = language;
  }

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

  public String getUrl()
  {
    return url;
  }

  public void setUrl( String url )
  {
    this.url = url;
  }

  public String getOriginalFilename()
  {
    return originalFilename;
  }

  public void setOriginalFilename( String originalFilename )
  {
    this.originalFilename = originalFilename;
  }

}
