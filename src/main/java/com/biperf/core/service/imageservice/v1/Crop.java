/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.imageservice.v1;

/**
 * 
 * @author sivanand
 * @since Jan 31, 2019
 * @version 1.0
 */
public class Crop
{
  private int x;
  private int y;
  private int width;
  private int height;

  public int getX()
  {
    return x;
  }

  public void setX( int x )
  {
    this.x = x;
  }

  public int getY()
  {
    return y;
  }

  public void setY( int y )
  {
    this.y = y;
  }

  public int getWidth()
  {
    return width;
  }

  public void setWidth( int width )
  {
    this.width = width;
  }

  public int getHeight()
  {
    return height;
  }

  public void setHeight( int height )
  {
    this.height = height;
  }
}
