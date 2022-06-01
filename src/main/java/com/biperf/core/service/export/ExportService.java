
package com.biperf.core.service.export;

import com.biperf.core.service.SAO;

public interface ExportService extends SAO
{
  public static String BEAN_NAME = "exportService";

  public void export( ExportData exportData );
}
