package com.logs.formatter.message.xml

import com.logs.Constants
import java.io.StringReader
import java.io.StringWriter
import java.lang.Exception
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

/**
 * Simply format the XML with a indent of XML_INDENT.
 */
class DefaultXmlFormatter: XmlFormatter {
    private val xmlIndent = 4

    /**
     * Format the XML with a indent of XML_INDENT.
     * @param data string
     * @return string
     */
    override fun format(data: String): String {
       var formattedString = Constants.EMPTY_STRING

        if (data.trim().isEmpty()){
            return formattedString
        }

        try {
            val xmlInput = StreamSource(StringReader(data))
            val xmlOutput = StreamResult(StringWriter())
            val transformer = TransformerFactory.newInstance().newTransformer()
            transformer.setOutputProperty(OutputKeys.INDENT,"yes")
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
            xmlIndent.toString())
            transformer.transform(xmlInput, xmlOutput)
            formattedString = xmlOutput.writer.toString().replaceFirst(">",">"+System.lineSeparator())

        }catch (e: Exception){
            e.printStackTrace()
            return data
        }

        return formattedString

    }
}