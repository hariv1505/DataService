<xsl:stylesheet
     xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
     version="1.1">
<xsl:output indent="yes"/>

   <xsl:template match="/">
      <xsl:variable name="join">
         <join>
            <xsl:for-each select="/Record">
               <xsl:variable name="rType" select="Type"/>
               <xsl:if test="$rType=Trade">
                   <xsl:copy-of select="*"/>
               </xsl:if>
            </xsl:for-each>
         </join>
      </xsl:variable>

      <totalpricetrades>
         <xsl:value-of
             select="sum($join//Price)"/>
      </totalpricetrades>

   </xsl:template>
</xsl:stylesheet>
