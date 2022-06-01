
package com.biperf.core.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.ColumnMapRowMapper;

public class HashMapColumnRowMapper extends ColumnMapRowMapper
{
  @Override
  protected Map<String, Object> createColumnMap( int columnCount )
  {
    return new HashMap<>( columnCount );
  }

  @Override
  protected String getColumnKey( String columnName )
  {
    return columnName.toLowerCase();
  }
}
