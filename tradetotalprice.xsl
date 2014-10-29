<xsl:stylesheet
     xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
     xmlns:exslt="http://exslt.org/common"
     version="1.1">
	<xsl:output indent="yes"/>

   <xsl:template match="/">
      <xsl:variable name="alltrades">
			<alltrades>
		      <xsl:for-each select="Response/Record">
		         <xsl:variable name="rType" select="Type"/>
		         <xsl:if test="$rType='Trade'">
		             <xsl:copy-of select="Price"/>
		         </xsl:if>
		      </xsl:for-each>
			</alltrades>
      </xsl:variable>

      <totalpricetrades>
         <xsl:value-of select="sum(exslt:node-set($alltrades)/alltrades/*)"/>
      </totalpricetrades>

   </xsl:template>
</xsl:stylesheet>