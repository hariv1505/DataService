<xsl:stylesheet
     xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
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
      	<!-- <xsl:for-each select="$join">
      		
      	</xsl:for-each> -->
      	<!-- <xsl:copy-of select="$join" />  -->
      	<!-- <xsl:for-each select="$alltrades/alltrades/Price">
      		<xsl:value-of select="*" />
      	</xsl:for-each>-->
         <xsl:value-of select="sum($alltrades//price)"/>
      </totalpricetrades>

   </xsl:template>
</xsl:stylesheet>
