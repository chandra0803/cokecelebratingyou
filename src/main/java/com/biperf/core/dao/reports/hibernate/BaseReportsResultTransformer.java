
package com.biperf.core.dao.reports.hibernate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.transform.ResultTransformer;

import com.biperf.core.utils.NumberFormatUtil;

@SuppressWarnings( "serial" )
public abstract class BaseReportsResultTransformer implements ResultTransformer
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
    return NumberFormatUtil.nullCheckBigDecimal( (BigDecimal)value ).longValue();
  }

  protected int extractInt( Object value )
  {
    return NumberFormatUtil.nullCheckBigDecimal( (BigDecimal)value ).intValue();
  }

  protected String extractString( Object value )
  {
    return (String)value;
  }

  protected double extractDouble( Object value )
  {
    return NumberFormatUtil.nullCheckBigDecimal( (BigDecimal)value ).doubleValue();
  }

  protected Date extractDate( Object value )
  {
    return (Date)value;
  }

  protected Boolean extractBoolean( Object value )
  {
    return ( (BigDecimal)value ).intValue() == 1;

  }

}
