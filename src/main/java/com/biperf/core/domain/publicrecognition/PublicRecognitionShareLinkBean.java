/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/publicrecognition/PublicRecognitionShareLinkBean.java,v $
 */

package com.biperf.core.domain.publicrecognition;

import java.io.Serializable;

/**
 *
 */
public class PublicRecognitionShareLinkBean implements Serializable
{

  private String nameId;
  private String url;
  private String name;

  public PublicRecognitionShareLinkBean()
  {
    super();
  }

  public PublicRecognitionShareLinkBean( String nameId, String name, String url )
  {
    super();
    this.nameId = nameId;
    this.url = url;
    this.name = name;

  }

  public String getNameId()
  {
    return nameId;
  }

  public void setNameId( String nameId )
  {
    this.nameId = nameId;
  }

  public String getUrl()
  {
    return url;
  }

  public void setUrl( String url )
  {
    this.url = url;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

}
