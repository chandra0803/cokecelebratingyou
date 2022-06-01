
package com.biperf.core.domain.enums;

import java.sql.Types;
import java.util.Properties;

import org.hibernate.type.EnumType;

@SuppressWarnings( "serial" )
public class HibernateVarCharEnum extends EnumType
{
  public void setParameterValues( Properties parameters )
  {
    parameters.setProperty( TYPE, "" + Types.VARCHAR );
    super.setParameterValues( parameters );
  }
}
