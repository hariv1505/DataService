<xsl:stylesheet
     xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
     version="1.1">
<xsl:output indent="yes"/>

   <xsl:template match="/">

   <allquotes>
      <xsl:for-each select="/Record">
         <xsl:variable name="rType" select="Type"/>
         <xsl:if test="$rType=Quote">
             <xsl:copy-of select="*"/>
         </xsl:if>
      </xsl:for-each>
   </allquotes>

   </xsl:template>
</xsl:stylesheet>
