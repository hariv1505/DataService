<xsl:stylesheet
     xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
     version="1.1">
<xsl:output indent="yes"/>

   <xsl:template match="/">

   { alltrades : [
      <xsl:for-each select="Response/Record">
      	 <xsl:variable name="rRIC" select="RIC"/>
      	 <xsl:variable name="rDate" select="Date"/>
      	 <xsl:variable name="rTime" select="Time"/>
         <xsl:variable name="rGMT" select="GMT"/>
         <xsl:variable name="rType" select="Type"/>
         <xsl:variable name="rPrice" select="Price"/>
         <xsl:variable name="rVolume" select="Volume"/>
         <xsl:variable name="rBidPrice" select="BidPrice"/>
         <xsl:variable name="rBidSize" select="BidSize"/>
         <xsl:variable name="rAskPrice" select="AskPrice"/>
         <xsl:variable name="rAskSize" select="AskSize"/>
         <xsl:if test="$rType='Trade'">
            { Response :
             	{
	             	RIC : <xsl:value-of select="$rRIC" />,
	             	Date : <xsl:value-of select="$rDate" />,
	             	Time : <xsl:value-of select="$rTime" />,
	             	GMT : <xsl:value-of select="$rGMT" />,
	             	Type : <xsl:value-of select="$rType" />,
	             	Price : <xsl:value-of select="$rPrice" />,
	             	Volume : <xsl:value-of select="$rVolume" />,
	             	BidPrice : <xsl:value-of select="$rBidPrice" />,
	             	BidSize : <xsl:value-of select="$rBidSize" />,
	             	AskPrice : <xsl:value-of select="$rAskPrice" />,
	             	AskSize : <xsl:value-of select="$rAskSize" />
             	}
           	}
         </xsl:if>
      </xsl:for-each>
   ] }

   </xsl:template>
</xsl:stylesheet>
