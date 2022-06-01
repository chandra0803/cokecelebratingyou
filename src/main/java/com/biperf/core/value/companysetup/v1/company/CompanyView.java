
package com.biperf.core.value.companysetup.v1.company;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class CompanyView implements Serializable
{

  private static final long serialVersionUID = 1L;

  private String name;
  private String contactEmailAddress;
  private String id;

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getContactEmailAddress()
  {
    return contactEmailAddress;
  }

  public void setContactEmailAddress( String contactEmailAddress )
  {
    this.contactEmailAddress = contactEmailAddress;
  }

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  @Override
  public String toString()
  {
    return "CompanyView [name=" + name + ", contactEmailAddress=" + contactEmailAddress + ", id=" + id + "]";
  }

}
