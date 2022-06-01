<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">

<xsl:output method="xml" indent="yes" />

<xsl:param name="projectName" />
<xsl:param name="upperProjectName" />

  <!-- copy everything from original file -->
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>
  
  <!-- add resource -->
  <xsl:template match="Context">
    <xsl:copy>
  
      <!-- keep existing resources -->
      <xsl:apply-templates/>
	  
      <Resource auth="Container" 
      factory="org.apache.activemq.jndi.JNDIReferenceFactory" 
      name="jms/{$projectName}/topic/EHCacheReplicationTopic" 
      physicalName="USERS.TOPIC.{$upperProjectName}.TOPIC.EHCACHEREPLICATION" 
      type="org.apache.activemq.command.ActiveMQTopic"/>
	  
      <Resource auth="Container" 
      factory="org.apache.activemq.jndi.JNDIReferenceFactory" 
      name="jms/{$projectName}/queue/EHCacheLoaderQueue" 
      physicalName="USERS.QUEUE.{$upperProjectName}.QUEUE.EHCACHELOADER" 
      type="org.apache.activemq.command.ActiveMQQueue"/>
	
      <ResourceLink type="javax.sql.DataSource" name="jdbc/{$projectName}" global="jdbc/{$projectName}"/>
      
    </xsl:copy>
  </xsl:template> 
</xsl:stylesheet> 