<xsl:stylesheet
     xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
     version="1.1">
<xsl:output indent="yes"/>

   <xsl:template match="/">

   { allquotes : [
      <xsl:for-each select="/Record">
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
         <xsl:if test="$rType=Quote">
            { Response :
             	{
	             	RIC : <xsl:copy-of select="$rRIC" />,
	             	Date : <xsl:copy-of select="$rDate" />,
	             	Time : <xsl:copy-of select="$rTime" />,
	             	GMT : <xsl:copy-of select="$rGMT" />,
	             	Type : <xsl:copy-of select="$rType" />,
	             	Price : <xsl:copy-of select="$rPrice" />,
	             	Volume : <xsl:copy-of select="$rVolume" />,
	             	BidPrice : <xsl:copy-of select="$rBidPrice" />,
	             	BidSize : <xsl:copy-of select="$rBidSize" />,
	             	AskPrice : <xsl:copy-of select="$rAskPrice" />,
	             	AskSize : <xsl:copy-of select="$rAskSize" />
             	}
           	}
         </xsl:if>
      </xsl:for-each>
   ]}

   </xsl:template>
</xsl:stylesheet>
