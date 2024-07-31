package com.rentreadychecklist.pdf

import com.itextpdf.kernel.events.Event
import com.itextpdf.kernel.events.IEventHandler
import com.itextpdf.kernel.events.PdfDocumentEvent
import com.itextpdf.kernel.geom.Rectangle
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.canvas.PdfCanvas
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject
import com.itextpdf.layout.Canvas
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.property.TextAlignment

/**
 * This class is used for set common text and page number in bottom for every page.
 */
class PageXofY(pdf: PdfDocument?) :
    IEventHandler {
    private var placeholder: PdfFormXObject
    var side = 20f
    private var x = 400f
    private var y = 25f
    private var space = 4.5f
    private var descent = 3f

    //This is to show footer on every page of PDF
    override fun handleEvent(event: Event) {
        val docEvent = event as PdfDocumentEvent
        val pdf = docEvent.document
        val page = docEvent.page
        val pageNumber = pdf.getPageNumber(page)
        val pageSize = page.pageSize
        val pdfCanvas = PdfCanvas(
            page.lastContentStream, page.resources, pdf
        )
        val canvas = Canvas(pdfCanvas, pdf, pageSize)
        val text =
            "Turn off all Appliances, Air Condition, Lighting, and \n Lock all Doors and Windows before leaving the home."
        val paragraph =
            Paragraph(text).setTextAlignment(TextAlignment.CENTER).setBold().setFontSize(10f)
                .setItalic()
        val p = Paragraph()
            .add("Page ").add(pageNumber.toString()).setTextAlignment(TextAlignment.RIGHT)
        canvas.showTextAligned(paragraph, 300f, 20f, TextAlignment.CENTER)
        canvas.showTextAligned(p, 550f, 5f, TextAlignment.RIGHT)
        pdfCanvas.addXObject(placeholder, x + space, y - descent)
        pdfCanvas.release()
    }

    init {
        placeholder = PdfFormXObject(Rectangle(0f, 0f, side, side))
    }
}

