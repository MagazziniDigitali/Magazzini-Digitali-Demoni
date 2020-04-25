<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:marc="http://www.loc.gov/MARC21/slim"
	xmlns:md="http://www.depositolegale.it/MD"
	xmlns:oai_dc="http://www.openarchives.org/OAI/2.0/oai_dc/"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns="http://www.depositolegale.it/MD" xmlns:premis="info:lc/xmlns/premis-v2"
	xmlns:METS="http://www.loc.gov/METS/" xmlns:PREMIS="info:lc/xmlns/premis-v2"
	xmlns:xlin="http://www.w3.org/1999/xlink"
	xsi:schemaLocation="http://www.depositolegale.it/MD http://www.bncf.firenze.sbn.it/SchemaXML/MagazziniDigitali/MD.xsd"
	exclude-result-prefixes="md">
	<!-- 
	<xsl:import href="http://www.loc.gov/standards/marcxml/xslt/MARC21slimUtils.xsl"/>
	<xsl:include href="http://www.loc.gov/marcxml/xslt/MARC21slimUtils.xsl"/>
	-->
	<xsl:output method="xml" indent="yes"/>
	<!--
	Fixed 530 Removed type="original" from dc:relation 2010-11-19 tmee
	Fixed 500 fields. 2006-12-11 ntra
	Added ISBN and deleted attributes 6/04 jer
	-->
	<xsl:template match="/">
		<xsl:if test="METS:mets">
			<xsl:for-each select="METS:mets">
				<md>
					<xsl:for-each select="METS:dmdSec">
						<xsl:for-each select="METS:mdWrap">
							<xsl:for-each select="METS:xmlData">
								<xsl:for-each select="node()">
									<xsl:if test="name(.)='record'">
										<bib>
											<xsl:for-each select="node()">
												<xsl:if test="name(.)='metadata'">
													<xsl:apply-templates select="oai_dc:dc"/>
												</xsl:if>
											</xsl:for-each>
										</bib>
									</xsl:if>
								</xsl:for-each>
								<xsl:for-each select="marc:record">
									<xsl:apply-templates select="."/>
								</xsl:for-each>
								<xsl:for-each select="oai_dc:record">
									<xsl:apply-templates select="."/>
								</xsl:for-each>
							</xsl:for-each>
						</xsl:for-each>
					</xsl:for-each>
					<xsl:for-each select="METS:amdSec">
						<xsl:for-each select="METS:digiprovMD">
							<xsl:for-each select="METS:mdWrap[@MDTYPE='PREMIS']">
								<xsl:for-each select="METS:xmlData">
									<xsl:for-each select="PREMIS:premis">
										<xsl:for-each select="PREMIS:agent">
											<xsl:apply-templates select="."/>
										</xsl:for-each>
									</xsl:for-each>
								</xsl:for-each>
							</xsl:for-each>
						</xsl:for-each>
					</xsl:for-each>
					<xsl:for-each select="METS:fileSec">
						<xsl:for-each select="METS:fileGrp">
							<xsl:for-each select="METS:file">
								<xsl:variable name="date" select="METS:FLocat/@xlin:href"/>
								<xsl:if test="starts-with($date,'http://') or starts-with($date,'https://')">
									<url><xsl:value-of select="$date"/></url>
								</xsl:if>
							</xsl:for-each>
						</xsl:for-each>
					</xsl:for-each>
				</md>
			</xsl:for-each>
		</xsl:if>
	</xsl:template>

	<xsl:template match="oai_dc:dc//*" >
		<xsl:value-of select="concat('&lt;',name(),'&gt;')" disable-output-escaping="yes"/>
		<xsl:apply-templates/>
		<xsl:value-of select="concat('&lt;/',name(),'&gt;')" disable-output-escaping="yes"/>
	</xsl:template>
	<xsl:template match="PREMIS:agent">
		<premis:agent>
			<xsl:for-each select="PREMIS:agentIdentifier">
				<premis:agentIdentifier>
					<xsl:for-each select="PREMIS:agentIdentifierType">
						<premis:agentIdentifierType>
							<xsl:value-of select="."/>
						</premis:agentIdentifierType>
					</xsl:for-each>
					<xsl:for-each select="PREMIS:agentIdentifierValue">
						<premis:agentIdentifierValue>
							<xsl:value-of select="."/>
						</premis:agentIdentifierValue>
					</xsl:for-each>
				</premis:agentIdentifier>
			</xsl:for-each>
			<xsl:for-each select="PREMIS:agentType">
				<premis:agentType>
					<xsl:value-of select="."/>
				</premis:agentType>
			</xsl:for-each>
		</premis:agent>
	</xsl:template>
	<xsl:template match="marc:record">
		<bib>

			<xsl:variable name="leader" select="marc:leader"/>
			<xsl:variable name="leader6" select="substring($leader,7,1)"/>
			<xsl:variable name="leader7" select="substring($leader,8,1)"/>
			<xsl:variable name="controlField008" select="marc:controlfield[@tag=008]"/>
			<xsl:attribute name="level">
				<xsl:value-of select="$leader7"/>
			</xsl:attribute>

			<!-- Identifier -->
			<xsl:for-each select="marc:controlfield[@tag=001]">
				<dc:identifier>
					<xsl:value-of select="."/>
				</dc:identifier>
			</xsl:for-each>
			<xsl:for-each select="marc:datafield[@tag=856]">
				<dc:identifier>
					<xsl:value-of select="marc:subfield[@code='u']"/>
				</dc:identifier>
			</xsl:for-each>
			<xsl:for-each select="marc:datafield[@tag=020]">
				<dc:identifier>
					<xsl:text>URN:ISBN:</xsl:text>
					<xsl:value-of select="marc:subfield[@code='a']"/>
				</dc:identifier>
			</xsl:for-each>

			<!-- Title -->
			<xsl:for-each select="marc:datafield[@tag=245]">
				<dc:title>
					<xsl:call-template name="subfieldSelect">
						<xsl:with-param name="codes">abfghk</xsl:with-param>
					</xsl:call-template>
				</dc:title>
			</xsl:for-each>

			<!-- Creator -->
			<xsl:for-each
				select="marc:datafield[@tag=100]|marc:datafield[@tag=110]|marc:datafield[@tag=111]|marc:datafield[@tag=700]|marc:datafield[@tag=710]|marc:datafield[@tag=711]|marc:datafield[@tag=720]">
				<dc:creator>
					<xsl:value-of select="."/>
				</dc:creator>
			</xsl:for-each>

			<!-- Publisher -->
			<xsl:for-each select="marc:datafield[@tag=260]">
				<dc:publisher>
					<xsl:call-template name="subfieldSelect">
						<xsl:with-param name="codes">ab</xsl:with-param>
					</xsl:call-template>
				</dc:publisher>
			</xsl:for-each>

			<!-- Subject -->
			<xsl:for-each select="marc:datafield[@tag=600]">
				<dc:subject>
					<xsl:call-template name="subfieldSelect">
						<xsl:with-param name="codes">abcdq</xsl:with-param>
					</xsl:call-template>
				</dc:subject>
			</xsl:for-each>
			<xsl:for-each select="marc:datafield[@tag=610]">
				<dc:subject>
					<xsl:call-template name="subfieldSelect">
						<xsl:with-param name="codes">abcdq</xsl:with-param>
					</xsl:call-template>
				</dc:subject>
			</xsl:for-each>
			<xsl:for-each select="marc:datafield[@tag=611]">
				<dc:subject>
					<xsl:call-template name="subfieldSelect">
						<xsl:with-param name="codes">abcdq</xsl:with-param>
					</xsl:call-template>
				</dc:subject>
			</xsl:for-each>
			<xsl:for-each select="marc:datafield[@tag=630]">
				<dc:subject>
					<xsl:call-template name="subfieldSelect">
						<xsl:with-param name="codes">abcdq</xsl:with-param>
					</xsl:call-template>
				</dc:subject>
			</xsl:for-each>
			<xsl:for-each select="marc:datafield[@tag=650]">
				<dc:subject>
					<xsl:call-template name="subfieldSelect">
						<xsl:with-param name="codes">abcdq</xsl:with-param>
					</xsl:call-template>
				</dc:subject>
			</xsl:for-each>
			<xsl:for-each select="marc:datafield[@tag=653]">
				<dc:subject>
					<xsl:call-template name="subfieldSelect">
						<xsl:with-param name="codes">abcdq</xsl:with-param>
					</xsl:call-template>
				</dc:subject>
			</xsl:for-each>

			<!-- Description -->
			<xsl:for-each select="marc:datafield[@tag=520]">
				<dc:description>
					<xsl:value-of select="marc:subfield[@code='a']"/>
				</dc:description>
			</xsl:for-each>
			<xsl:for-each select="marc:datafield[@tag=521]">
				<dc:description>
					<xsl:value-of select="marc:subfield[@code='a']"/>
				</dc:description>
			</xsl:for-each>
			<xsl:for-each
				select="marc:datafield[500&lt;= @tag and @tag&lt;= 599 ][not(@tag=506 or @tag=530 or @tag=540 or @tag=546)]">
				<dc:description>
					<xsl:value-of select="marc:subfield[@code='a']"/>
				</dc:description>
			</xsl:for-each>

			<!-- Contributor -->
			<xsl:for-each
				select="marc:datafield[700&lt;= @tag and @tag&lt;= 799 ][not(@tag=700 or @tag=710 or @tag=711 or @tag=720)]">
				<dc:contributor>
					<xsl:value-of select="."/>
				</dc:contributor>
			</xsl:for-each>

			<!-- Date -->
			<xsl:for-each select="marc:datafield[@tag=260]/marc:subfield[@code='c']">
				<dc:date>
					<xsl:value-of select="."/>
				</dc:date>
			</xsl:for-each>

			<!-- Type -->
			<dc:type>
				<xsl:if test="$leader7='c'">
					<!--Remove attribute 6/04 jer-->
					<!--<xsl:attribute name="collection">yes</xsl:attribute>-->
					<xsl:text>collection</xsl:text>
				</xsl:if>
				<xsl:if test="$leader6='d' or $leader6='f' or $leader6='p' or $leader6='t'">
					<!--Remove attribute 6/04 jer-->
					<!--<xsl:attribute name="manuscript">yes</xsl:attribute>-->
					<xsl:text>manuscript</xsl:text>
				</xsl:if>
				<xsl:choose>
					<xsl:when test="$leader6='a' or $leader6='t'">text</xsl:when>
					<xsl:when test="$leader6='e' or $leader6='f'">cartographic</xsl:when>
					<xsl:when test="$leader6='c' or $leader6='d'">notated music</xsl:when>
					<xsl:when test="$leader6='i' or $leader6='j'">sound recording</xsl:when>
					<xsl:when test="$leader6='k'">still image</xsl:when>
					<xsl:when test="$leader6='g'">moving image</xsl:when>
					<xsl:when test="$leader6='r'">three dimensional object</xsl:when>
					<xsl:when test="$leader6='m'">software, multimedia</xsl:when>
					<xsl:when test="$leader6='p'">mixed material</xsl:when>
				</xsl:choose>
			</dc:type>
			<xsl:for-each select="marc:datafield[@tag=655]">
				<dc:type>
					<xsl:value-of select="."/>
				</dc:type>
			</xsl:for-each>

			<!-- Format -->
			<xsl:for-each select="marc:datafield[@tag=856]/marc:subfield[@code='q']">
				<dc:format>
					<xsl:value-of select="."/>
				</dc:format>
			</xsl:for-each>

			<!-- Source -->
			<xsl:for-each select="marc:datafield[@tag=856]/marc:subfield[@code='v']">
				<dc:source>
					<xsl:value-of select="."/>
				</dc:source>
			</xsl:for-each>

			<!-- Language -->
			<dc:language>
				<xsl:value-of select="substring($controlField008,36,3)"/>
			</dc:language>

			<!-- Relation -->
			<xsl:for-each select="marc:datafield[@tag=530]">
				<dc:relation>
					<xsl:call-template name="subfieldSelect">
						<xsl:with-param name="codes">abcdu</xsl:with-param>
					</xsl:call-template>
				</dc:relation>
			</xsl:for-each>
			<xsl:for-each
				select="marc:datafield[@tag=760]|marc:datafield[@tag=762]|marc:datafield[@tag=765]|marc:datafield[@tag=767]|marc:datafield[@tag=770]|marc:datafield[@tag=772]|marc:datafield[@tag=773]|marc:datafield[@tag=774]|marc:datafield[@tag=775]|marc:datafield[@tag=776]|marc:datafield[@tag=777]|marc:datafield[@tag=780]|marc:datafield[@tag=785]|marc:datafield[@tag=786]|marc:datafield[@tag=787]">
				<dc:relation>
					<xsl:call-template name="subfieldSelect">
						<xsl:with-param name="codes">ot</xsl:with-param>
					</xsl:call-template>
				</dc:relation>
			</xsl:for-each>

			<!-- Coverage -->
			<xsl:for-each select="marc:datafield[@tag=752]">
				<dc:coverage>
					<xsl:call-template name="subfieldSelect">
						<xsl:with-param name="codes">abcd</xsl:with-param>
					</xsl:call-template>
				</dc:coverage>
			</xsl:for-each>

			<!-- Rights -->
			<xsl:for-each select="marc:datafield[@tag=506]">
				<dc:rights>
					<xsl:value-of select="marc:subfield[@code='a']"/>
				</dc:rights>
			</xsl:for-each>
			<xsl:for-each select="marc:datafield[@tag=540]">
				<dc:rights>
					<xsl:value-of select="marc:subfield[@code='a']"/>
				</dc:rights>
			</xsl:for-each>

			<!-- Holdings -->
			<xsl:for-each select="marc:datafield[@tag=955]">
				<holdings>
					<xsl:for-each select="marc:subfield[@code='b']">
						<inventory_number>
							<xsl:value-of select="."/>
						</inventory_number>
					</xsl:for-each>
					<xsl:for-each select="marc:subfield[@code='v']">
						<precis_inv>
							<xsl:value-of select="."/>
						</precis_inv>
					</xsl:for-each>
					<xsl:for-each select="marc:subfield[@code='3']">
						<shelfmark>
							<xsl:value-of select="."/>
						</shelfmark>
					</xsl:for-each>
				</holdings>
			</xsl:for-each>





			<!--</oai_dc:dc>-->
		</bib>
	</xsl:template>
	
	<xsl:template name="subfieldSelect">
		<xsl:param name="codes">abcdefghijklmnopqrstuvwxyz</xsl:param>
		<xsl:param name="delimeter">
			<xsl:text></xsl:text>
		</xsl:param>
		<xsl:variable name="str">
			<xsl:for-each select="marc:subfield">
				<xsl:if test="contains($codes, @code)">
					<xsl:value-of select="text()"/>
					<xsl:value-of select="$delimeter"/>
				</xsl:if>
			</xsl:for-each>
		</xsl:variable>
		<xsl:value-of select="substring($str,1,string-length($str)-string-length($delimeter))"/>
	</xsl:template>
</xsl:stylesheet>
