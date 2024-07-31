package com.rentreadychecklist.pdf

import com.itextpdf.kernel.events.Event
import com.itextpdf.kernel.events.IEventHandler
import com.itextpdf.kernel.events.PdfDocumentEvent
import com.itextpdf.kernel.pdf.PdfName
import com.itextpdf.kernel.pdf.PdfNumber


/**
 * This class is to show PDF in portrait mode
 */

class PageRotationEventHandler : IEventHandler {
    private val portrait = PdfNumber(0)

    private var rotation: PdfNumber = portrait

    override fun handleEvent(event: Event) {
        val docEvent = event as PdfDocumentEvent
        docEvent.page.put(PdfName.Rotate, rotation)
    }
}