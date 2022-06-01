
package com.biperf.core.service;

/**
 * This class is will get translated to a Hibernate PropertyProjection class
 * and added the to Projection list.  There should be one of these classes for
 * each attribute the client wants populated in the returned bean
 * 
 */
public class ProjectionAttribute
{
  private String attributeName;
  private String attributeAlias;

  /*
   * In most cases, theses are the same
   */
  public ProjectionAttribute( String attributeName )
  {
    this.attributeName = attributeName;
    this.attributeAlias = attributeName;
  }

  public ProjectionAttribute( String attributeName, String attributeAlias )
  {
    this.attributeName = attributeName;
    this.attributeAlias = attributeAlias;
  }

  public String getAttributeName()
  {
    return attributeName;
  }

  public void setAttributeName( String attributeName )
  {
    this.attributeName = attributeName;
  }

  public String getAttributeAlias()
  {
    return attributeAlias;
  }

  public void setAttributeAlias( String attributeAlias )
  {
    this.attributeAlias = attributeAlias;
  }
}
