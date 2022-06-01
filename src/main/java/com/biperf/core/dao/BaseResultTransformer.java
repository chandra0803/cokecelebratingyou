
package com.biperf.core.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.transform.ResultTransformer;

@SuppressWarnings( "serial" )
public abstract class BaseResultTransformer implements ResultTransformer
{
  @Override
  public abstract Object transformTuple( Object[] tuple, String[] aliases );

  @SuppressWarnings( "rawtypes" )
  @Override
  public List transformList( List collection )
  {
    return collection;
  }

  protected BigDecimal extractBigDecimal( Object value )
  {
    return (BigDecimal)value;
  }

  protected Long extractLong( Object value )
  {
    return value == null ? null : ( (BigDecimal)value ).longValue();
  }

  protected int extractInt( Object value )
  {
    return value == null ? 0 : ( (BigDecimal)value ).intValue();
  }

  protected int extractIntFromObject( Object value )
  {
    return value == null ? 0 : ( (Integer)value ).intValue();
  }

  protected String extractString( Object value )
  {
    return (String)value;
  }

  protected double extractDouble( Object value )
  {
    return value == null ? 0 : ( (BigDecimal)value ).doubleValue();
  }

  protected Date extractDate( Object value )
  {
    return (Date)value;
  }

  protected Boolean extractBoolean( Object value )
  {
    return value == null ? false : ( (BigDecimal)value ).intValue() == 1;
  }

}
