<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">

<xsl:output method="xml" indent="yes" />

  <xsl:param name="projectName" />
  <xsl:param name="upperProjectName" />

  <!-- copy everything from original file -->
  <xsl:template match="@*|node()">
    <!-- ... except do not copy the resource we want to delete -->
    <xsl:if test="not(@physicalName=concat('USERS.TOPIC.',$upperProjectName,'.TOPIC.EHCACHEREPLICATION'))
     and not(@physicalName=concat('USERS.QUEUE.',$upperProjectName,'.QUEUE.EHCACHELOADER'))
     and not(@name=concat('jdbc/',$projectName))">
      <xsl:copy>
        <xsl:apply-templates select="@*|node()"/>
      </xsl:copy>
     </xsl:if>
  </xsl:template>
  
</xsl:stylesheet> 