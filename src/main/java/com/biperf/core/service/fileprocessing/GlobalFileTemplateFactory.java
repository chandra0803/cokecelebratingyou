
package com.biperf.core.service.fileprocessing;

import com.biperf.core.value.fileprocessing.FileInfo;

public interface GlobalFileTemplateFactory
{
  FileInfo getFileDetailInfo( String type );
}
