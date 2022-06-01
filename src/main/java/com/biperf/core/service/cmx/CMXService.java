
package com.biperf.core.service.cmx;

import java.util.List;
import java.util.Map;

import com.biperf.core.exception.DataException;

public interface CMXService
{
  public static final String BEAN_NAME = "cmxService";

  Map<String, String> getTranslation( String locale, List<String> keys ) throws DataException;
}
