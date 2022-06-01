
package com.biperf.core.strategy.fileload.impl;

import javax.sql.DataSource;

public abstract class BaseFileloadStrategy
{
  private DataSource dataSource;

  protected DataSource getDataSource()
  {
    return dataSource;
  }

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }
}
