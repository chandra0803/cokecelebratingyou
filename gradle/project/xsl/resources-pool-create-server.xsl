<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">

<xsl:output method="xml" indent="yes" />

<xsl:param name="projectName" />
<xsl:param name="driver" />
<xsl:param name="url" />
<xsl:param name="username" />
<xsl:param name="password" />
<xsl:param name="dialect" />
    
  <!-- copy everything from original file -->
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>
  
  <xsl:template match="Server/GlobalNamingResources">
    <xsl:copy>
    
      <!-- keep existing resources -->
      <xsl:apply-templates/>
      
      <!-- writes the value to file for testing
     <xsl:value-of select="$projectName"/> -->
     
      <!-- add new resource -->
   	  <Resource name="jdbc/{$projectName}"
          url="{$url}"
	      password="{$password}" 
          username="{$username}"
          auth="Container" 
          closeMethod="close"
          driverClassName="{$driver}"
          initialSize="1"
          maxIdle="15" 
          maxTotal="15" 
          maxWaitMillis="10000"
          minEvictableIdleTimeMillis="30000"
          minIdle="1"
          numTestsPerEvictionRun="10"
          timeBetweenEvictionRunsMillis="30000"
          type="javax.sql.DataSource">
      </Resource>

    </xsl:copy>

  </xsl:template>
  
</xsl:stylesheet> 