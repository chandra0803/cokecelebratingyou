
package com.biperf.core.indexing;

import com.biperf.core.service.SAO;

public interface BIElasticSearchAdminService extends SAO
{
  public static final String BEAN_NAME = "biElasticSearchIndexerService";

  public boolean createIndex() throws Exception;

  public boolean deleteIndex() throws Exception;

  public boolean indexExists() throws Exception;

  public boolean close() throws Exception;

  public boolean open() throws Exception;

  public void resetClientFactory();

  public void sendClientResetMessage();

  public EsIndexStatus getIndexStatus();
}
