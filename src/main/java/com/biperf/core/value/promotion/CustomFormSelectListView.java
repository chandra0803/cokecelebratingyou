
package com.biperf.core.value.promotion;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder( { "name", "id" } )
public class CustomFormSelectListView
{

  @JsonProperty( "name" )
  private String name;

  @JsonProperty( "id" )
  private String id;

  @JsonProperty( "countryCode" )
  private String countryCode;

  /**
   * 
   * @return
   *     The name
   */
  @JsonProperty( "name" )
  public String getName()
  {
    return name;
  }

  /**
   * 
   * @param name
   *     The name
   */
  @JsonProperty( "name" )
  public void setName( String name )
  {
    this.name = name;
  }

  /**
   * 
   * @return
   *     The id
   */
  @JsonProperty( "id" )
  public String getId()
  {
    return id;
  }

  /**
   * 
   * @param id
   *     The id
   */
  @JsonProperty( "id" )
  public void setId( String id )
  {
    this.id = id;
  }

  @Override
  public String toString()
  {
    return ToStringBuilder.reflectionToString( this );
  }

  @JsonProperty( "countryCode" )
  public String getCountryCode()
  {
    return countryCode;
  }

  @JsonProperty( "countryCode" )
  public void setCountryCode( String countryCode )
  {
    this.countryCode = countryCode;
  }

}
