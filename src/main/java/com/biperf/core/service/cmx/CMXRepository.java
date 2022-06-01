
package com.biperf.core.service.cmx;

import java.util.Map;

import com.biperf.core.exception.DataException;

public interface CMXRepository
{
  <T> Map<String, Map<String, String>> getTranslation( T cmxTranslateRequest ) throws DataException;
}
