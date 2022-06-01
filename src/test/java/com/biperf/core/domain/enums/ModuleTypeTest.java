
package com.biperf.core.domain.enums;

import org.junit.Assert;

import com.biperf.core.utils.ModuleType;

public class ModuleTypeTest extends BaseEnumTest
{

  public void testGetAllModuleType()
  {

    int totalLength = ModuleType.values().length;

    Assert.assertTrue( totalLength == ModuleType.getAllModuleType().size() );

  }

}
