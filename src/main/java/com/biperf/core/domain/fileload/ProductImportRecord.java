/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.fileload;

/*
 * ProductImportRecord <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>jenniget</td> <td>Jan
 * 31, 2006</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class ProductImportRecord extends ImportRecord
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------
  private Long productId;
  private String productName;
  private String productDescription;
  private String skuCode;
  private Long categoryId;
  private String categoryName;
  private String categoryDescription;
  private Long subCategoryId;
  private String subCategoryName;
  private String subCategoryDescription;
  private Long characteristicId1;
  private Long characteristicId2;
  private Long characteristicId3;
  private Long characteristicId4;
  private Long characteristicId5;
  private String characteristicName1;
  private String characteristicName2;
  private String characteristicName3;
  private String characteristicName4;
  private String characteristicName5;

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public Long getProductId()
  {
    return productId;
  }

  public void setProductId( Long productId )
  {
    this.productId = productId;
  }

  public String getCategoryDescription()
  {
    return categoryDescription;
  }

  public void setCategoryDescription( String categoryDescription )
  {
    this.categoryDescription = categoryDescription;
  }

  public Long getCategoryId()
  {
    return categoryId;
  }

  public void setCategoryId( Long categoryId )
  {
    this.categoryId = categoryId;
  }

  public String getCategoryName()
  {
    return categoryName;
  }

  public void setCategoryName( String categoryName )
  {
    this.categoryName = categoryName;
  }

  public String getProductDescription()
  {
    return productDescription;
  }

  public void setProductDescription( String productDescription )
  {
    this.productDescription = productDescription;
  }

  public String getProductName()
  {
    return productName;
  }

  public void setProductName( String productName )
  {
    this.productName = productName;
  }

  public String getSkuCode()
  {
    return skuCode;
  }

  public void setSkuCode( String skuCode )
  {
    this.skuCode = skuCode;
  }

  public String getSubCategoryDescription()
  {
    return subCategoryDescription;
  }

  public void setSubCategoryDescription( String subCategoryDescription )
  {
    this.subCategoryDescription = subCategoryDescription;
  }

  public Long getSubCategoryId()
  {
    return subCategoryId;
  }

  public void setSubCategoryId( Long subCategoryId )
  {
    this.subCategoryId = subCategoryId;
  }

  public String getSubCategoryName()
  {
    return subCategoryName;
  }

  public void setSubCategoryName( String subCategoryName )
  {
    this.subCategoryName = subCategoryName;
  }

  // ---------------------------------------------------------------------------
  // Equals, Hashcode, and To String Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns true if the given object and this object are equal; returns false otherwise.
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object the object to compare to this object.
   * @return true if the given object and this object are equal; false otherwise.
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof ProductImportRecord ) )
    {
      return false;
    }

    ProductImportRecord productImportRecord = (ProductImportRecord)object;

    if ( this.getId() != null && this.getId().equals( productImportRecord.getId() ) )
    {
      return true;
    }
    return false;
  }

  /**
   * Returns the hashcode for this object.
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return the hashcode for this object.
   */
  public int hashCode()
  {
    return this.getId() != null ? this.getId().hashCode() : 0;
  }

  /**
   * Returns a string representation of this object.
   * 
   * @see java.lang.Object#toString()
   * @return a string representation of this object.
   */

  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "ProductImportRecord [" );
    buf.append( "{id=" ).append( super.getId() ).append( "}, " );
    buf.append( "]" );
    return buf.toString();
  }

  public Long getCharacteristicId1()
  {
    return characteristicId1;
  }

  public void setCharacteristicId1( Long characteristicId1 )
  {
    this.characteristicId1 = characteristicId1;
  }

  public Long getCharacteristicId2()
  {
    return characteristicId2;
  }

  public void setCharacteristicId2( Long characteristicId2 )
  {
    this.characteristicId2 = characteristicId2;
  }

  public Long getCharacteristicId3()
  {
    return characteristicId3;
  }

  public void setCharacteristicId3( Long characteristicId3 )
  {
    this.characteristicId3 = characteristicId3;
  }

  public Long getCharacteristicId4()
  {
    return characteristicId4;
  }

  public void setCharacteristicId4( Long characteristicId4 )
  {
    this.characteristicId4 = characteristicId4;
  }

  public Long getCharacteristicId5()
  {
    return characteristicId5;
  }

  public void setCharacteristicId5( Long characteristicId5 )
  {
    this.characteristicId5 = characteristicId5;
  }

  public String getCharacteristicName1()
  {
    return characteristicName1;
  }

  public void setCharacteristicName1( String characteristicName1 )
  {
    this.characteristicName1 = characteristicName1;
  }

  public String getCharacteristicName2()
  {
    return characteristicName2;
  }

  public void setCharacteristicName2( String characteristicName2 )
  {
    this.characteristicName2 = characteristicName2;
  }

  public String getCharacteristicName3()
  {
    return characteristicName3;
  }

  public void setCharacteristicName3( String characteristicName3 )
  {
    this.characteristicName3 = characteristicName3;
  }

  public String getCharacteristicName4()
  {
    return characteristicName4;
  }

  public void setCharacteristicName4( String characteristicName4 )
  {
    this.characteristicName4 = characteristicName4;
  }

  public String getCharacteristicName5()
  {
    return characteristicName5;
  }

  public void setCharacteristicName5( String characteristicName5 )
  {
    this.characteristicName5 = characteristicName5;
  }

}
