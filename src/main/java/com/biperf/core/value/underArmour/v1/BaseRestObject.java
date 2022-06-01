
package com.biperf.core.value.underArmour.v1;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public abstract class BaseRestObject
{

  @Override
  public String toString()
  {
    return ReflectionToStringBuilder.toString( this );
  }
}
