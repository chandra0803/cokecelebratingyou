
package com.biperf.core.value.diycommunication;

import java.io.Serializable;

import com.biperf.core.value.FormattedValueBean;

public class ParticipantList implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String id;
  private String name;

  public ParticipantList( FormattedValueBean fmvBean )
  {
    this.setId( fmvBean.getId().toString() );
    this.setName( fmvBean.getFnameLname() );
  }

  public ParticipantList()
  {

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

}
