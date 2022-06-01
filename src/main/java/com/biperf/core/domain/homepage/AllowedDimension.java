
package com.biperf.core.domain.homepage;

import java.io.Serializable;

public class AllowedDimension implements Comparable<AllowedDimension>, Serializable
{
  private static final String DIMENSION_DELIMITER = "X";

  private Integer width;
  private Integer height;

  /**
   * Create an AllowedDimension from an available size string.  String should be in format: "[width]X[height]"
   *
   * @param sizeString
   */
  public AllowedDimension( String sizeString )
  {
    super();

    try
    {
      String[] dimensionArray = sizeString.split( DIMENSION_DELIMITER );
      this.width = Integer.parseInt( dimensionArray[0] );
      this.height = Integer.parseInt( dimensionArray[1] );
    }
    catch( NumberFormatException e )
    {
      this.width = 0;
      this.height = 0;
    }
  }

  @Override
  public String toString()
  {
    return "{w:" + width + ",h:" + height + "}";
  }

  public Integer getWidth()
  {
    return width;
  }

  public void setWidth( Integer width )
  {
    this.width = width;
  }

  public Integer getHeight()
  {
    return height;
  }

  public void setHeight( Integer height )
  {
    this.height = height;
  }

  @Override
  public int compareTo( AllowedDimension other )
  {
    int comparison = this.width.compareTo( other.getWidth() );
    if ( comparison == 0 )
    {
      comparison = this.height.compareTo( other.getHeight() );
    }
    return comparison;
  }

}
