
package com.biperf.core.ui.diyquiz;

/**
 * 
 * DIYQuizMaterialFileView.
 * 
 * @author kandhi
 * @since Jul 18, 2013
 * @version 1.0
 */
public class DIYQuizMaterialFileView
{
  private Long id;
  private String name;
  private String url;
  private String originalFilename;

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
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
