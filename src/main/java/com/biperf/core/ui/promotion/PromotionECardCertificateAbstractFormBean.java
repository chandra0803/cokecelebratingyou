
package com.biperf.core.ui.promotion;

public class PromotionECardCertificateAbstractFormBean implements Comparable
{

  private Long id;
  private String imageName;
  private String previewImageName;
  private String createdBy;
  private long dateCreated;
  private Long version = null;
  private boolean selected;
  protected String name;

  public PromotionECardCertificateAbstractFormBean()
  {
    super();
  }

  public String getCreatedBy()
  {
    return createdBy;
  }

  public void setCreatedBy( String createdBy )
  {
    this.createdBy = createdBy;
  }

  public long getDateCreated()
  {
    return dateCreated;
  }

  public void setDateCreated( long dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  public String getImageName()
  {
    return imageName;
  }

  public void setImageName( String imageName )
  {
    this.imageName = imageName;
  }

  public boolean isSelected()
  {
    return selected;
  }

  public void setSelected( boolean selected )
  {
    this.selected = selected;
  }

  public Long getVersion()
  {
    return version;
  }

  public void setVersion( Long version )
  {
    this.version = version;
  }

  public String getPreviewImageName()
  {
    return previewImageName;
  }

  public void setPreviewImageName( String previewImageName )
  {
    this.previewImageName = previewImageName;
  }

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

  /**
   * Overridden from @see java.lang.Comparable#compareTo(java.lang.Object)
   * @param o
   * @return
   */
  public int compareTo( Object object )
  {
    if ( ! ( object instanceof PromotionECardCertificateAbstractFormBean ) )
    {
      throw new ClassCastException( "A PromotionECardCertificateAbstractFormBean was expected." );
    }
    PromotionECardCertificateAbstractFormBean promotionECardCertificateAbstractFormBean = (PromotionECardCertificateAbstractFormBean)object;
    return this.getName().compareTo( promotionECardCertificateAbstractFormBean.getName() );
  }

}
