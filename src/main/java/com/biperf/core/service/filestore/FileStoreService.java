
package com.biperf.core.service.filestore;

import java.util.List;

import com.biperf.core.domain.enums.FileStoreType;
import com.biperf.core.domain.filestore.FileStore;
import com.biperf.core.service.SAO;

public interface FileStoreService extends SAO
{
  static final String BEAN_NAME = "fileStoreService";

  public FileStore save( Long userId, FileStore fileStore );

  public FileStore get( Long fileStoreId );

  public List<FileStore> getFileStoresForUser( Long userId );

  public String generateInternalFileName( Long userId, FileStoreType type );

  public boolean isLargeAudienceReportGenerationEnabled();
}
