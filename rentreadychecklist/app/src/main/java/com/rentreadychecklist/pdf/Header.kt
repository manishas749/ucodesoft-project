package com.rentreadychecklist.pdf

import android.content.Context
import com.itextpdf.kernel.events.Event
import com.itextpdf.kernel.events.IEventHandler
import com.itextpdf.kernel.events.PdfDocumentEvent
import com.itextpdf.kernel.geom.Rectangle
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.canvas.PdfCanvas
import com.itextpdf.layout.Canvas
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Table


/**
 * This is for header to show on every page of PDF
 */
class Header(private var doc: Document, val context: Context, private val table: Table) :
    IEventHandler {

    override fun handleEvent(event: Event) {
        val docEvent = event as PdfDocumentEvent
        val pdf: PdfDocument = docEvent.document
        val page = docEvent.page
        val pdfCanvas = PdfCanvas(
            page.lastContentStream, page.resources, pdf
        )
        val rect = Rectangle(
            pdf.defaultPageSize.x + doc.leftMargin,
            pdf.defaultPageSize.top - doc.topMargin, 735f, 80f
        )
        Canvas(pdfCanvas, pdf, rect)
            .add(table)
        pdfCanvas.release()
    }
}




