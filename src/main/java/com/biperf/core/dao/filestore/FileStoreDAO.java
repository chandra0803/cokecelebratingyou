
package com.biperf.core.dao.filestore;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.filestore.FileStore;

public interface FileStoreDAO extends DAO
{
  static final String BEAN_NAME = "fileStoreDAO";

  public FileStore save( FileStore fileStore );

  public FileStore get( Long fileStorageId );

  public List<FileStore> findFileStoresForUser( Long userId );
}
