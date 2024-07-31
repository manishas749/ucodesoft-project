package com.rentreadychecklist.pdf

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.core.graphics.scale
import com.itextpdf.io.font.FontConstants
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.events.PdfDocumentEvent
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.border.Border
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.UnitValue
import com.itextpdf.layout.renderer.DocumentRenderer
import com.rentreadychecklist.R
import com.rentreadychecklist.constants.Constants.Companion.pdfDownloadOrPrivate
import com.rentreadychecklist.db.AppData
import com.rentreadychecklist.model.bathroom.Bathrooms
import com.rentreadychecklist.model.bedrooms.Bedrooms
import com.rentreadychecklist.model.diningRoom.DiningRoom
import com.rentreadychecklist.model.frontdoors.FrontDoors
import com.rentreadychecklist.model.garage.Garage
import com.rentreadychecklist.model.greatroom.GreatRoom
import com.rentreadychecklist.model.imageupload.ImageUploadCommon
import com.rentreadychecklist.model.kitchen.Kitchen
import com.rentreadychecklist.model.laundry.LaundryRoom
import com.rentreadychecklist.model.livingRoom.LivingRoom
import com.rentreadychecklist.model.miscellaneous.Miscellaneous
import com.rentreadychecklist.model.outside.Outside
import com.rentreadychecklist.utils.ProgressBarDialogPdf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


/**
 * This class is used for generate PDF
 */
class PdfGenerator(applicationContext: Context) {

    var context = applicationContext
    private lateinit var headerHandler: Header
    var font = PdfFontFactory.createFont(FontConstants.HELVETICA)!!   //Font set for entire PDF
    private val tableHeader = arrayOf(
        context.resources.getString(R.string.ok),
        context.resources.getString(R.string.na),
        context.resources.getString(R.string.fix),
        context.resources.getString(R.string.time),
        context.resources.getString(R.string.productName),
        ""
    )
    private val columnWidth = floatArrayOf(20f, 20f, 20f, 50f, 60f, 310f)
    private lateinit var pageRotationEventHandler: PageRotationEventHandler


    //Generate PDF on click of download button

    fun pdfFileInDownload(
        list: List<AppData>,
        progressBarDialog: ProgressBarDialogPdf,
        title: String
    ) {
        pdfDownloadOrPrivate = context.resources.getString(R.string.download)
        val directory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!directory.exists()) {
            directory.mkdir()
        }
        pdfFile(title, directory, list, progressBarDialog)
    }

    //Generate PDF on the email page
    fun pdfFileInPrivate(list: List<AppData>, progressBarDialog: ProgressBarDialogPdf) {
        val folderName = context.resources.getString(R.string.rentReady)
        pdfDownloadOrPrivate = context.resources.getString(R.string.private_Folder)
        val directory: File = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val mediaDir = (context as Activity).externalMediaDirs.firstOrNull()

            if (mediaDir != null && mediaDir.exists()) {
                mediaDir
            } else {
                (context as Activity).filesDir
            }
        } else {
            File(Environment.getExternalStorageDirectory(), "/$folderName")
        }
        if (!directory.exists()) {
            directory.mkdir()
        }
        pdfFile(
            context.resources.getString(R.string.rentReadyPdf),
            directory,
            list,
            progressBarDialog
        )

    }


    private fun pdfFile(
        title: String, directory: File, list: List<AppData>,
        progressBarDialog: ProgressBarDialogPdf
    ) {
        val file: File?
        try {
            file = File(directory, title)
            if (!file.exists()) file.createNewFile()
            if (file.canWrite()) {
                pdfGenerator(list, progressBarDialog, file)
            } else {
                progressBarDialog.hideProgressBar()
                Toast.makeText(
                    context,
                    context.resources.getString(R.string.Already_file_exists_by_name),
                    Toast.LENGTH_SHORT
                ).show()
            }

        } catch (e: Exception) {
            progressBarDialog.hideProgressBar()
            Toast.makeText(
                context,
                context.resources.getString(R.string.Already_file_exists_by_name),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    //PDF generator code

    private fun pdfGenerator(
        list: List<AppData>,
        progressBarDialog: ProgressBarDialogPdf,
        file: File
    ) {

        MainScope().launch(Dispatchers.IO) {

            //PDF writer
            val pdfWriter = PdfWriter(FileOutputStream(file))
            pdfWriter.isFullCompression
            val pdfDoc = com.itextpdf.kernel.pdf.PdfDocument(pdfWriter)

            val event = PageXofY(pdfDoc)               //set Footer for PDF
            pageRotationEventHandler = PageRotationEventHandler()
            val doc = Document(pdfDoc, PageSize.A4)  // color set pending document
            doc.setMargins(100f, 20f, 90f, 36f)  //margin set for document

            val docRender = DocumentRenderer(doc, false)
            // image for home image
            val img = imageResize(R.drawable.rent_ready_image, 20, 17)


            //image for check image
            val checkImg = imageResize(R.drawable.check, 4, 5)

            //image for uncheck
            val unCheckImage = imageResize(R.drawable.unchecked1, 4, 5)

            // Set Header for PDF DOCUMENT
            try {
                headerHandler = Header(doc, context, addHeader(list, img))
                pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, event)
                pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, headerHandler)
                pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, pageRotationEventHandler)
            } catch (e: Exception) {
                e.printStackTrace()
            }


// TABLE ADD FOR PICTURES
            val takePicture = context.resources.getString(R.string.take_pictures)
            val pictures = titleAdd(takePicture)
            doc.add(pictures)
            try {
                docRender.flush()
                doc.add(addPicturesTables(checkImg, list, unCheckImage))

            } catch (e: Exception) {
                docRender.flush()
                doc.add(addPicturesTables(checkImg, list, unCheckImage))
            }


            // TABLE ADD FOR OUTSIDE
            val outside = context.resources.getString(R.string.pdfOutside)
            val outsideAdd = titleAdd(outside)
            doc.add(outsideAdd)
            try {
                docRender.flush()
                doc.add(addOutsideTable(list, checkImg, unCheckImage))

            } catch (e: Exception) {
                docRender.flush()
                doc.add(addOutsideTable(list, checkImg, unCheckImage))
            }

            // Table for Front Doors

            val frontDoors = context.resources.getString(R.string.pdfFrontDoors)
            val frontDoorsAdd = titleAdd(frontDoors)
            doc.add(frontDoorsAdd)
            try {
                docRender.flush()
                doc.add(addFrontTable(list, checkImg))

            } catch (e: Exception) {
                docRender.flush()
                doc.add(addFrontTable(list, checkImg))
            }

            //Table for Garage
            val garage = context.resources.getString(R.string.pdfGarage)
            val garageAdd = titleAdd(garage)
            doc.add(garageAdd)
            try {
                docRender.flush()
                doc.add(addGarageTable(list, checkImg,unCheckImage))

            } catch (e: Exception) {
                docRender.flush()
                doc.add(addGarageTable(list, checkImg,unCheckImage))

            }

            //Add Laundry Table
            val laundry = context.resources.getString(R.string.pdfLaundryRoom)
            val laundryRoom = titleAdd(laundry)
            doc.add(laundryRoom)
            try {
                docRender.flush()
                doc.add(laundryTableAdd(list, checkImg))

            } catch (e: Exception) {
                docRender.flush()
                doc.add(laundryTableAdd(list, checkImg))
            }

            //Add living Room Table
            val livingRoom = context.resources.getString(R.string.pdfLivingRoom)
            val livingRoomAdd = titleAdd(livingRoom)
            doc.add(livingRoomAdd)
            try {
                docRender.flush()
                doc.add(livingRoomAdd(list, checkImg, unCheckImage))

            } catch (e: Exception) {
                docRender.flush()
                doc.add(livingRoomAdd(list, checkImg, unCheckImage))

            }

            //Add great Room Table
            val greatRoom = context.resources.getString(R.string.pdfGreatRoom)
            val greatRoomAdd = titleAdd(greatRoom)
            doc.add(greatRoomAdd)
            try {
                docRender.flush()
                doc.add(greatRoomAdd(list, checkImg, unCheckImage))

            } catch (e: Exception) {
                docRender.flush()
                doc.add(greatRoomAdd(list, checkImg, unCheckImage))

            }

            //Add dining Room Table
            val diningRoom = context.resources.getString(R.string.pdfDiningRoom)
            val diningRoomAdd = titleAdd(diningRoom)
            diningRoomAdd.marginTop = 15f
            doc.add(diningRoomAdd)
            try {
                docRender.flush()
                doc.add(diningRoomAddTable(list, checkImg, unCheckImage))
            } catch (e: Exception) {
                docRender.flush()
                doc.add(diningRoomAddTable(list, checkImg, unCheckImage))

            }

            //Add kitchen Table
            val kitchen = context.resources.getString(R.string.pdfKitchen)
            val kitchenAdd = titleAdd(kitchen)
            doc.add(kitchenAdd)
            try {
                docRender.flush()
                doc.add(kitchenTableAdd(list, checkImg, unCheckImage))

            } catch (e: Exception) {
                docRender.flush()
                doc.add(kitchenTableAdd(list, checkImg, unCheckImage))

            }

            //Add Miscellaneous table

            val miscellaneous = context.resources.getString(R.string.pdfMiscellaneous)
            val miscellaneousAdd = titleAdd(miscellaneous)
            doc.add(miscellaneousAdd)

            try {
                docRender.flush()
                doc.add(miscellaneousTableAdd(list, checkImg, unCheckImage))

            } catch (e: Exception) {
                docRender.flush()
                doc.add(miscellaneousTableAdd(list, checkImg, unCheckImage))

            }

            // Add table for bedroom

            val bedroomTableList = bedroomTableAdd(list, checkImg, unCheckImage)

            for (i in bedroomTableList.indices) {
                val numberOfBedrooms = i + 1
                val bedroom = if (list[0].bedroom[i].bedroomName!="Bedroom"){
                    list[0].bedroom[i].bedroomName
                }else{
                    "Bedroom $numberOfBedrooms "
                }
                val bedroomAdd =
                    Paragraph(bedroom).setBold().setTextAlignment(TextAlignment.LEFT)
                        .setFontSize(15f)
                        .setBold().setFont(font)
                bedroomAdd.marginTop = 15f
                doc.add(bedroomAdd)
                try {
                    docRender.flush()
                    doc.add(bedroomTableList[i])
                } catch (e: Exception) {
                    docRender.flush()
                    doc.add(bedroomTableList[i])
                }

            }


            // Bathroom table
            val bathroomTableList = bathroomTableAdd(list, checkImg)
            for (i in bathroomTableList.indices) {
                val numberOfBathroom = i + 1
                val bathroom = if (list[0].bathroom[i].bathroomName!="Bathroom"){
                    list[0].bathroom[i].bathroomName
                }else{
                    "Bathroom $numberOfBathroom "
                }
                val bathroomAdd =
                    Paragraph(bathroom).setBold().setTextAlignment(TextAlignment.LEFT)
                        .setFontSize(15f)
                        .setBold().setFont(font)
                bathroomAdd.marginTop = 15f
                doc.add(bathroomAdd)
                try {
                    docRender.flush()
                    doc.add(bathroomTableList[i])
                } catch (e: Exception) {
                    docRender.flush()
                    doc.add(bathroomTableList[i])
                }


            }

            // Additional Notes
            val notes = context.resources.getString(R.string.pdfAdditionalNotes)
            val notesParagraph =
                Paragraph(notes).setBold().setFontSize(12f).setTextAlignment(TextAlignment.LEFT)
            val width = floatArrayOf(200f)
            val notesTable = Table(width)
            notesTable.marginTop = 30f
            notesTable.width = UnitValue.createPercentValue(100f)
            notesTable.height = 200f
            notesTable.setFixedLayout()
            val additionalNotes = list[0].homeScreen[0].additionalNotes
            val additionalNoteParagraph = Paragraph(additionalNotes)
            notesTable.addCell(Cell().add(notesParagraph).add(additionalNoteParagraph))
            doc.add(notesTable)


            //Images shown Below
            val image = context.resources.getString(R.string.pdfImages)
            val paragraph =
                Paragraph(image).setBold().setFont(font).setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20f).setUnderline()
            paragraph.marginTop = 50f
            doc.add(paragraph)


            // Image Add For Home Screen

            try {
                if (list[0].homeScreen[0].image.isNotEmpty()) {
                    val imageTitle = list[0].homeScreen[0].image[0].imageTitle
                    val homeScreenImageAdd =
                        Paragraph(imageTitle).setBold().setTextAlignment(TextAlignment.CENTER)
                            .setFontSize(15f)
                            .setBold().setFont(font)
                    homeScreenImageAdd.marginTop = 15f
                    doc.add(homeScreenImageAdd)
                    doc.add(imageHomeScreeAdd(list))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // NEW image outside
            try {
                val imageList = newOutsideImageList(list[0].outside)
                val fixImageTable = commonImagesTableAdd(imageList.first)
                val okImageTable = commonImagesTableAdd(imageList.second)
                if (fixImageTable.second) {
                    val outsideImages = context.resources.getString(R.string.fixOutsideImagesAre)
                    val picturesImages = titleImagesAdd(outsideImages)
                    docRender.flush()
                    doc.add(picturesImages)
                    doc.add(fixImageTable.first)
                }

                if (okImageTable.second) {
                    val outsideImages = context.resources.getString(R.string.okOutsideImagesAre)
                    val picturesImages = titleImagesAdd(outsideImages)
                    docRender.flush()
                    doc.add(picturesImages)
                    doc.add(okImageTable.first)
                }


            } catch (e: Exception) {
                e.printStackTrace()
            }


            //Image Add for front Doors
            try {
                val imageList = frontDoorImageList(list[0].frontDoors)
                val fixImageTable = commonImagesTableAdd(imageList.first)
                val okImageTable = commonImagesTableAdd(imageList.second)
                if (fixImageTable.second) {
                    val frontImages = context.resources.getString(R.string.fixFrontDoorImagesAre)
                    val picturesImages = titleImagesAdd(frontImages)
                    docRender.flush()
                    doc.add(picturesImages)
                    doc.add(fixImageTable.first)
                }

                if (okImageTable.second) {
                    val frontImages = context.resources.getString(R.string.okFrontDoorImagesAre)
                    val picturesImages = titleImagesAdd(frontImages)
                    docRender.flush()
                    doc.add(picturesImages)
                    doc.add(okImageTable.first)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }


            //image Add for Garage
            try {
                val imageList = garageImageList(list[0].garage)
                val fixImageTable = commonImagesTableAdd(imageList.first)
                val okImageTable = commonImagesTableAdd(imageList.second)
                if (fixImageTable.second) {
                    val garageImages = context.resources.getString(R.string.fixGarageImagesAre)
                    val picturesImages = titleImagesAdd(garageImages)
                    docRender.flush()
                    doc.add(picturesImages)
                    doc.add(fixImageTable.first)
                }

                if (okImageTable.second) {
                    val garageImages = context.resources.getString(R.string.okGarageImagesAre)
                    val picturesImages = titleImagesAdd(garageImages)
                    docRender.flush()
                    doc.add(picturesImages)
                    doc.add(okImageTable.first)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }


            //image Add for laundry room
            try {
                val imageList = laundryImageList(list[0].laundryRoom)
                val fixImageTable = commonImagesTableAdd(imageList.first)
                val okImageTable = commonImagesTableAdd(imageList.second)
                if (fixImageTable.second) {
                    val laundryImages = context.resources.getString(R.string.fixLaundryImagesAre)
                    val picturesImages = titleImagesAdd(laundryImages)
                    docRender.flush()
                    doc.add(picturesImages)
                    doc.add(fixImageTable.first)
                }

                if (okImageTable.second) {
                    val laundryImages = context.resources.getString(R.string.okLaundryImagesAre)
                    val picturesImages = titleImagesAdd(laundryImages)
                    docRender.flush()
                    doc.add(picturesImages)
                    doc.add(okImageTable.first)
                }


            } catch (e: Exception) {
                e.printStackTrace()
            }


            //image for living room
            try {
                val imageList = livingRoomImageList(list[0].livingRoom)
                val fixImageTable = commonImagesTableAdd(imageList.first)
                val okImageTable = commonImagesTableAdd(imageList.second)
                if (fixImageTable.second) {
                    val livingImages = context.resources.getString(R.string.fixLivingRoomImagesAre)
                    val picturesImages = titleImagesAdd(livingImages)
                    docRender.flush()
                    doc.add(picturesImages)
                    doc.add(fixImageTable.first)
                }

                if (okImageTable.second) {
                    val livingImages = context.resources.getString(R.string.okLivingRoomImagesAre)
                    val picturesImages = titleImagesAdd(livingImages)
                    docRender.flush()
                    doc.add(picturesImages)
                    doc.add(okImageTable.first)
                }
            } catch (e: Exception) {
                e.printStackTrace()

            }


            //image for great room
            try {
                val imageList = greatRoomImageList(list[0].greatRoom)
                val fixImageTable = commonImagesTableAdd(imageList.first)
                val okImageTable = commonImagesTableAdd(imageList.second)
                if (fixImageTable.second) {
                    val greatImages = context.resources.getString(R.string.fixGreatRoomImagesAre)
                    val picturesImages = titleImagesAdd(greatImages)
                    docRender.flush()
                    doc.add(picturesImages)
                    doc.add(fixImageTable.first)
                }

                if (okImageTable.second) {
                    val greatImages = context.resources.getString(R.string.okGreatImagesAre)
                    val picturesImages = titleImagesAdd(greatImages)
                    docRender.flush()
                    doc.add(picturesImages)
                    doc.add(okImageTable.first)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }


            //image for dining room
            try {
                val imageList = diningRoomImageList(list[0].diningRoom)
                val fixImageTable = commonImagesTableAdd(imageList.first)
                val okImageTable = commonImagesTableAdd(imageList.second)
                if (fixImageTable.second) {
                    val diningImages = context.resources.getString(R.string.fixDiningImagesAre)
                    val picturesImages = titleImagesAdd(diningImages)
                    docRender.flush()
                    doc.add(picturesImages)
                    doc.add(fixImageTable.first)
                }

                if (okImageTable.second) {
                    val diningImages = context.resources.getString(R.string.okDiningImagesAre)
                    val picturesImages = titleImagesAdd(diningImages)
                    docRender.flush()
                    doc.add(picturesImages)
                    doc.add(okImageTable.first)
                }


            } catch (e: Exception) {
                e.printStackTrace()

            }


            //image for kitchen
            try {
                val imageList = kitchenImageList(list[0].kitchen)
                val fixImageTable = commonImagesTableAdd(imageList.first)
                val okImageTable = commonImagesTableAdd(imageList.second)
                if (fixImageTable.second) {
                    val kitchenImages = context.resources.getString(R.string.fixKitchenImagesAre)
                    val picturesImages = titleImagesAdd(kitchenImages)
                    docRender.flush()
                    doc.add(picturesImages)
                    doc.add(fixImageTable.first)
                }

                if (okImageTable.second) {
                    val kitchenImages = context.resources.getString(R.string.okKitchenImagesAre)
                    val picturesImages = titleImagesAdd(kitchenImages)
                    docRender.flush()
                    doc.add(picturesImages)
                    doc.add(okImageTable.first)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }


            //image for miscellaneous
            try {
                val imageList = miscellaneousImageList(list[0].miscellaneous)
                val fixImageTable = commonImagesTableAdd(imageList.first)
                val okImageTable = commonImagesTableAdd(imageList.second)
                if (fixImageTable.second) {
                    val miscellaneousImages =
                        context.resources.getString(R.string.fixMiscellaneousImagesAre)
                    val picturesImages = titleImagesAdd(miscellaneousImages)
                    docRender.flush()
                    doc.add(picturesImages)
                    doc.add(fixImageTable.first)
                }
                if (okImageTable.second) {
                    val miscellaneousImages =
                        context.resources.getString(R.string.okMiscellaneousImagesAre)
                    val picturesImages = titleImagesAdd(miscellaneousImages)
                    docRender.flush()
                    doc.add(picturesImages)
                    doc.add(okImageTable.first)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            //bedroom images add new
            for (i in list[0].bedroom.indices) {
                try {
                    val imageList = bedroomImageList(list[0].bedroom[i].list)
                    val fixImageTable = commonImagesTableAdd(imageList.first)
                    val okImageTable = commonImagesTableAdd(imageList.second)
                    if (fixImageTable.second) {
                        val numberOfBedrooms = i + 1
                        val bedroom = "Fix Bedroom $numberOfBedrooms Images Are:  "
                        val bedroomAdd =
                            Paragraph(bedroom).setBold().setTextAlignment(TextAlignment.LEFT)
                                .setFontSize(15f)
                                .setBold().setFont(font)
                        bedroomAdd.marginTop = 15f
                        bedroomAdd.marginBottom = 20f
                        docRender.flush()
                        doc.add(bedroomAdd)
                        doc.add(fixImageTable.first)
                    }
                    if (okImageTable.second) {
                        val numberOfBedrooms = i + 1
                        val bedroom = "Ok Bedroom $numberOfBedrooms Images Are:  "
                        val bedroomAdd =
                            Paragraph(bedroom).setBold().setTextAlignment(TextAlignment.LEFT)
                                .setFontSize(15f)
                                .setBold().setFont(font)
                        bedroomAdd.marginTop = 15f
                        bedroomAdd.marginBottom = 20f
                        docRender.flush()
                        doc.add(bedroomAdd)
                        doc.add(okImageTable.first)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            //bathroom image add new
            for (i in list[0].bathroom.indices) {
                try {
                    val imageList = bathroomImageList(list[0].bathroom[i].list)
                    val fixImageTable = commonImagesTableAdd(imageList.first)
                    val okImageTable = commonImagesTableAdd(imageList.second)
                    if (fixImageTable.second) {
                        val numberOfBathrooms = i + 1
                        val bedroom = "Fix Bathroom $numberOfBathrooms Images Are:  "
                        val bedroomAdd =
                            Paragraph(bedroom).setBold().setTextAlignment(TextAlignment.LEFT)
                                .setFontSize(15f)
                                .setBold().setFont(font)
                        bedroomAdd.marginTop = 15f
                        bedroomAdd.marginBottom = 20f
                        docRender.flush()
                        doc.add(bedroomAdd)
                        doc.add(fixImageTable.first)
                    }
                    if (okImageTable.second) {
                        val numberOfBathrooms = i + 1
                        val bedroom = "Ok Bathroom $numberOfBathrooms Images Are:  "
                        val bedroomAdd =
                            Paragraph(bedroom).setBold().setTextAlignment(TextAlignment.LEFT)
                                .setFontSize(15f)
                                .setBold().setFont(font)
                        bedroomAdd.marginTop = 15f
                        bedroomAdd.marginBottom = 20f
                        docRender.flush()
                        doc.add(bedroomAdd)
                        doc.add(okImageTable.first)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            progressBarDialog.hideProgressBar()

            doc.close()

            withContext(Dispatchers.Main) {
                if (pdfDownloadOrPrivate == context.resources.getString(R.string.download)) {
                    Toast.makeText(
                        context,
                        context.resources.getString(R.string.pdfHasBeenDownloaded),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    // add header in document
    private fun addHeader(list: List<AppData>, image: Image): Table {
        val size = floatArrayOf(150f, 420f, 400f)
        val headerTable = Table(size)
        val rent = context.resources.getString(R.string.rentReadyCheckList)
        val paragraph1 =
            Paragraph(rent).setBold().setTextAlignment(TextAlignment.CENTER).setFontSize(20f)
                .setFont(font)
        val property = list[0].homeScreen[0].propertyAddress
        val paragraph2 =
            Paragraph("Property Address :  $property").setTextAlignment(TextAlignment.CENTER)
                .setFontSize(10f)
                .setFont(font)
        val date = list[0].homeScreen[0].date
        val time = list[0].homeScreen[0].time
        val columnWidth = floatArrayOf(150f)
        val dateTable = Table(columnWidth)
        dateTable.addCell(
            Cell().add("Date: $date \n Time: $time").setTextAlignment(TextAlignment.LEFT)
        )
        headerTable.addCell(
            Cell().add(Cell().add(dateTable).setBorder(Border.NO_BORDER))
                .setBorder(Border.NO_BORDER)
        ).setBorder(
            Border.NO_BORDER
        )
        val column = floatArrayOf(400f)
        val table = Table(column)
        table.addCell(Cell().add(paragraph1).setBorder(Border.NO_BORDER))
        table.addCell(
            Cell().add(paragraph2).setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.CENTER)
        ).setTextAlignment(TextAlignment.LEFT)
        headerTable.addCell(
            Cell().add(table).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER)
        )
        val columnWidthForImage = floatArrayOf(45f)
        val tableForImage = Table(columnWidthForImage)
        tableForImage.marginLeft = 20f
        tableForImage.addCell(
            Cell().add(image).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT)
        ).setTextAlignment(TextAlignment.CENTER).setBorder(
            Border.NO_BORDER
        )
        headerTable.addCell(
            Cell().add(tableForImage).setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT)
        ).setTextAlignment(TextAlignment.RIGHT)
            .setTextAlignment(
                TextAlignment.RIGHT
            ).setFont(font)
        return headerTable
    }

    // add fist address and  front  door lock table in document
    private fun addPicturesTables(image: Image, list: List<AppData>, unCheckImage: Image): Table {
        /*  Table for address   */
        val column = floatArrayOf(20f, 400f)
        val addressTable = Table(column)
        addressTable.marginTop = 2f
        addressTable.setBorder(Border.NO_BORDER)
        val addressCondition = list[0].homeScreen[0].addressCondition
        val frontDoorCondition = list[0].homeScreen[0].frontDoorCondition
        val lockCondition = list[0].homeScreen[0].lockSetCondition

        if (addressCondition) {
            addressTable.addCell(Cell().add(Cell().add(image)).setBorder(Border.NO_BORDER))
                .setTextAlignment(TextAlignment.CENTER)
        } else {
            addressTable.addCell(Cell().add(Cell().add(unCheckImage)).setBorder(Border.NO_BORDER))
                .setTextAlignment(TextAlignment.CENTER)
        }
        val addressColumn = context.resources.getString(R.string.address)
        val para1 = Paragraph(addressColumn).setTextAlignment(TextAlignment.LEFT).setFontSize(10f)
        addressTable.addCell(Cell().add(Cell().add(para1)).setBorder(Border.NO_BORDER))
            .setFont(font)


        if (frontDoorCondition) {
            addressTable.addCell(Cell().add(Cell().add(image)).setBorder(Border.NO_BORDER))
        } else {
            addressTable.addCell(Cell().add(Cell().add(unCheckImage)).setBorder(Border.NO_BORDER))

        }
        val front = context.resources.getString(R.string.frontDoorLockSetDeadboltHandleFullSet)
        val frontDoor = Paragraph(front).setTextAlignment(TextAlignment.LEFT).setFontSize(10f)
        addressTable.addCell(Cell().add(Cell().add(frontDoor)).setBorder(Border.NO_BORDER))
            .setFont(font)
        if (lockCondition) {
            addressTable.addCell(Cell().add(Cell().add(image)).setBorder(Border.NO_BORDER))

        } else {
            addressTable.addCell(Cell().add(Cell().add(unCheckImage)).setBorder(Border.NO_BORDER))
        }

        val lock = context.resources.getString(R.string.lockSetFromGarageToHouseDoor)
        val lockSet = Paragraph(lock).setTextAlignment(TextAlignment.LEFT).setFontSize(10f)

        addressTable.addCell(Cell().add(Cell().add(lockSet)).setBorder(Border.NO_BORDER))
            .setFont(font)
        return addressTable

    }

    // Add outside table in document
    private fun addOutsideTable(list: List<AppData>, image: Image, unCheckImage: Image): Table {
        val outsideTable = Table(columnWidth)
        outsideTable.width = UnitValue.createPercentValue(100f)
        outsideTable.setFixedLayout()
        outsideTable.marginTop = 4f
        for (element in tableHeader) {
            outsideTable.addCell(
                Cell().add(element).setBold().setFont(font).setFontSize(10f)
                    .setTextAlignment(TextAlignment.CENTER)
            )
        }

        for (i in 0 until list[0].outside.size) {
            if (list[0].outside[i].ok.ok == context.resources.getString(R.string.itemOK)) {
                outsideTable.addCell(
                    Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                )
            } else {
                outsideTable.addCell(Cell().add(list[0].outside[i].ok.ok))

            }
            if (list[0].outside[i].na == context.resources.getString(R.string.itemNA)) {
                outsideTable.addCell(
                    Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                )

            } else {
                outsideTable.addCell(Cell().add(list[0].outside[i].na))

            }
            if (list[0].outside[i].fix.fix == context.resources.getString(R.string.itemFIX)) {

                outsideTable.addCell(
                    Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                )

            } else {
                outsideTable.addCell(Cell().add(list[0].outside[i].fix.fix))

            }
            outsideTable.addCell(
                Cell().add(list[0].outside[i].fix.time).setFontSize(10f).setFont(font)
            )
            outsideTable.addCell(
                Cell().add(list[0].outside[i].fix.product).setFontSize(10f).setFont(font)
            )
            val notes = list[0].outside[i].fix.notes
            val itemName = list[0].outside[i].items.ItemName
            val itemNameParagraph = Paragraph(itemName).setBold()
            val itemCondition1 = list[0].outside[i].items.ItemCondition1
            val itemCondition2 = list[0].outside[i].items.ItemCondition2
            if (itemName == context.resources.getString(R.string.pdfSatelliteDishAndCables)) {
                val columnWidthCarpet = floatArrayOf(120f, 40f, 20f, 10f, 20f, 20f)
                val table = Table(columnWidthCarpet)
                table.setBorder(Border.NO_BORDER)
                table.addCell(
                    Cell().add(itemName).setBorder(Border.NO_BORDER).setFont(font)
                        .setFontSize(10f).setBold()
                )
                table.addCell(
                    Cell().add(context.resources.getString(R.string.story))
                        .setBorder(Border.NO_BORDER).setFont(font).setItalic()
                        .setFontSize(10f).setBold()
                )
                if (itemCondition1) {
                    table.addCell(
                        Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                    )
                } else {
                    table.addCell(
                        Cell().add(unCheckImage).setBorder(Border.NO_BORDER).setFontSize(10f)
                    )
                }
                table.addCell(
                    Cell().add(context.resources.getString(R.string.one))
                        .setBorder(Border.NO_BORDER).setFont(font)
                        .setFontSize(10f).setBold()
                )
                if (itemCondition2) {
                    table.addCell(
                        Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                    )
                } else {

                    table.addCell(
                        Cell().add(unCheckImage).setBorder(Border.NO_BORDER).setFontSize(10f)
                    )
                }
                table.addCell(
                    Cell().add(context.resources.getString(R.string.two)).setFont(font).setBorder(
                        Border.NO_BORDER
                    ).setFontSize(10f).setBold()
                )
                outsideTable.addCell(
                    Cell().add(table).add(Paragraph(notes).setFontSize(10f))
                )

            } else {
                outsideTable.addCell(
                    Cell().add(itemNameParagraph).add(notes).setFontSize(10f).setFont(font)
                )
            }

        }
        return outsideTable
    }


    // Add front table  in document
    private fun addFrontTable(list: List<AppData>, image: Image): Table {
        val frontDoorsTable = Table(columnWidth)
        frontDoorsTable.width = UnitValue.createPercentValue(100f)
        frontDoorsTable.setFixedLayout()
        frontDoorsTable.marginTop = 4f
        for (element in tableHeader) {
            frontDoorsTable.addCell(
                Cell().add(element).setBold().setFont(font).setFontSize(10f)
                    .setTextAlignment(TextAlignment.CENTER)
            )
        }

        for (i in 0 until list[0].frontDoors.size) {
            if (list[0].frontDoors[i].ok.ok == context.resources.getString(R.string.itemOK)) {
                frontDoorsTable.addCell(
                    Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                )
            } else {
                frontDoorsTable.addCell(Cell().add(list[0].frontDoors[i].ok.ok))

            }

            if (list[0].frontDoors[i].NA == context.resources.getString(R.string.itemNA)) {
                frontDoorsTable.addCell(
                    Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                )

            } else {
                frontDoorsTable.addCell(Cell().add(list[0].frontDoors[i].NA))

            }
            if (list[0].frontDoors[i].fix.fix == context.resources.getString(R.string.itemFIX)) {

                frontDoorsTable.addCell(
                    Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                )

            } else {
                frontDoorsTable.addCell(Cell().add(list[0].frontDoors[i].fix.fix))

            }
            frontDoorsTable.addCell(
                Cell().add(list[0].frontDoors[i].fix.time).setFontSize(10f).setFont(font)
            )
            frontDoorsTable.addCell(
                Cell().add(list[0].frontDoors[i].fix.product).setFontSize(10f).setFont(font)
            )
            val notes = list[0].frontDoors[i].fix.notes
            val itemName = list[0].frontDoors[i].items
            val itemNameParagraph = Paragraph(itemName).setBold()
            frontDoorsTable.addCell(
                Cell().add(itemNameParagraph).add(notes).setFontSize(10f).setFont(font)
            )


        }
        return frontDoorsTable
    }

    // Add garage table in document

    private fun addGarageTable(list: List<AppData>, image: Image,
                               unCheckImage: Image): Table {
        val garageTable = Table(columnWidth)
        garageTable.width = UnitValue.createPercentValue(100f)
        garageTable.setFixedLayout()

        garageTable.marginTop = 4f
        for (element in tableHeader) {
            garageTable.addCell(
                Cell().add(element).setBold().setFont(font).setFontSize(10f)
                    .setTextAlignment(TextAlignment.CENTER)
            )
        }

        for (i in 0 until list[0].garage.size) {
            if (list[0].garage[i].ok.ok == context.resources.getString(R.string.itemOK)) {
                garageTable.addCell(
                    Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                )
            } else {
                garageTable.addCell(Cell().add(list[0].garage[i].ok.ok))

            }

            if (list[0].garage[i].na == context.resources.getString(R.string.itemNA)) {
                garageTable.addCell(
                    Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                )

            } else {
                garageTable.addCell(Cell().add(list[0].garage[i].na))

            }
            if (list[0].garage[i].fix.fix == context.resources.getString(R.string.itemFIX)) {

                garageTable.addCell(
                    Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                )

            } else {
                garageTable.addCell(Cell().add(list[0].garage[i].fix.fix))

            }
            garageTable.addCell(
                Cell().add(list[0].garage[i].fix.time).setFontSize(10f).setFont(font)
            )
            garageTable.addCell(
                Cell().add(list[0].garage[i].fix.product).setFontSize(10f).setFont(font)
            )
            val notes = list[0].garage[i].fix.notes
            val itemName = list[0].garage[i].items.ItemName
            val itemCondition1 = list[0].garage[i].items.ItemCondition1
            val itemCondition2 = list[0].garage[i].items.ItemCondition2
            val itemNameParagraph = Paragraph(itemName).setBold()
            when(itemName) {
                context.resources.getString(R.string.itemElectrical),
                context.resources.getString(R.string.itemWallsBaseboardsCeiling) ,
                        context.resources.getString(R.string.itemWaterHeater)-> {
                    garageTable.addCell(
                        Cell().add(itemName).setFontSize(12f).setFont(font)
                            .setTextAlignment(TextAlignment.CENTER).setBold()
                    )

                }
                context.resources.getString(R.string.itemPaint) -> {

                    val columnWidthCarpet = floatArrayOf(50f, 25f, 50f, 25f, 70f)
                    val table = Table(columnWidthCarpet)
                    table.setBorder(Border.NO_BORDER)
                    table.addCell(
                        Cell().add(itemNameParagraph).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    if (itemCondition1) {
                        table.addCell(
                            Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    } else {
                        table.addCell(
                            Cell().add(unCheckImage).setBorder(Border.NO_BORDER)
                                .setFontSize(10f)
                        )
                    }

                    table.addCell(
                        Cell().add(context.resources.getString(R.string.touchUp))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setItalic()
                            .setFontSize(10f).setBold()
                    )
                    if (itemCondition2) {
                        table.addCell(
                            Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    } else {

                        table.addCell(
                            Cell().add(unCheckImage).setBorder(Border.NO_BORDER)
                                .setFontSize(10f)
                        )
                    }
                    table.addCell(
                        Cell().add(context.resources.getString(R.string.wholeWall))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setItalic().setFontSize(10f).setBold()
                    )
                    garageTable.addCell(
                        Cell().add(table).add(Paragraph(notes).setFontSize(10f))
                    )

                }
                else->
                {
                    garageTable.addCell(
                        Cell().add(itemNameParagraph).add(notes).setFontSize(10f).setFont(font)
                    )

                }
            }



        }
        return garageTable

    }

    // Add laundry table in document

    private fun laundryTableAdd(list: List<AppData>, image: Image): Table {
        val laundryTable = Table(columnWidth)
        laundryTable.width = UnitValue.createPercentValue(100f)
        laundryTable.setFixedLayout()
        laundryTable.marginTop = 4f
        for (element in tableHeader) {
            laundryTable.addCell(
                Cell().add(element).setBold().setFont(font).setFontSize(10f)
                    .setTextAlignment(TextAlignment.CENTER)
            )
        }

        for (i in 0 until list[0].laundryRoom.size) {
            if (list[0].laundryRoom[i].ok.ok == context.resources.getString(R.string.itemOK)) {
                laundryTable.addCell(
                    Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                )
            } else {
                laundryTable.addCell(Cell().add(list[0].laundryRoom[i].ok.ok))

            }

            if (list[0].laundryRoom[i].na == context.resources.getString(R.string.itemNA)) {
                laundryTable.addCell(
                    Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                )

            } else {
                laundryTable.addCell(Cell().add(list[0].laundryRoom[i].na))

            }
            if (list[0].laundryRoom[i].fix.fix == context.resources.getString(R.string.itemFIX)) {

                laundryTable.addCell(
                    Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                )

            } else {
                laundryTable.addCell(Cell().add(list[0].laundryRoom[i].fix.fix))

            }
            laundryTable.addCell(
                Cell().add(list[0].laundryRoom[i].fix.time).setFontSize(10f).setFont(font)
            )
            laundryTable.addCell(
                Cell().add(list[0].laundryRoom[i].fix.product).setFontSize(10f).setFont(font)
            )
            val notes = list[0].laundryRoom[i].fix.notes
            val itemName = list[0].laundryRoom[i].items
            val itemNameParagraph = Paragraph(itemName).setBold()
            laundryTable.addCell(
                Cell().add(itemNameParagraph).add(notes).setFontSize(10f).setFont(font)
            )
        }
        return laundryTable

    }

    // Add living room table in document
    private fun livingRoomAdd(list: List<AppData>, image: Image, unCheckImage: Image): Table {


        val livingRoomTable = Table(columnWidth)
        livingRoomTable.width = UnitValue.createPercentValue(100f)
        livingRoomTable.setFixedLayout()
        livingRoomTable.marginTop = 4f
        for (element in tableHeader) {
            livingRoomTable.addCell(
                Cell().add(element).setBold().setFont(font).setFontSize(10f)
                    .setTextAlignment(TextAlignment.CENTER)
            )
        }

        for (i in 0 until list[0].livingRoom.size) {
            if (list[0].livingRoom[i].ok.ok == context.resources.getString(R.string.itemOK)) {
                livingRoomTable.addCell(
                    Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                )
            } else {
                livingRoomTable.addCell(Cell().add(list[0].livingRoom[i].ok.ok))

            }

            if (list[0].livingRoom[i].na == context.resources.getString(R.string.itemNA)) {
                livingRoomTable.addCell(
                    Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                )

            } else {
                livingRoomTable.addCell(Cell().add(list[0].livingRoom[i].na))

            }
            if (list[0].livingRoom[i].fix.fix == context.resources.getString(R.string.itemFIX)) {

                livingRoomTable.addCell(
                    Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                )

            } else {
                livingRoomTable.addCell(Cell().add(list[0].livingRoom[i].fix.fix))

            }
            livingRoomTable.addCell(
                Cell().add(list[0].livingRoom[i].fix.time).setFontSize(10f).setFont(font)
            )
            livingRoomTable.addCell(
                Cell().add(list[0].livingRoom[i].fix.product).setFontSize(10f).setFont(font)
            )
            val notes = list[0].livingRoom[i].fix.notes
            val itemName = list[0].livingRoom[i].items.ItemName
            val itemNameParagraph = Paragraph(itemName).setBold()

            val itemCondition1 = list[0].livingRoom[i].items.ItemCondition1
            val itemCondition2 = list[0].livingRoom[i].items.ItemCondition2
            val itemNotes1 = list[0].livingRoom[i].items.ItemNotes1
            val itemNotes2 = list[0].livingRoom[i].items.ItemNotes2
            val itemNotes3 = list[0].livingRoom[i].items.ItemNotes3
            val notesBlind = list[0].livingRoom[i].items.itemNotes4


            when (itemName) {
                context.resources.getString(R.string.itemCarpetAndFlooring),
                context.resources.getString(R.string.itemWallsBaseboardsCeiling),
                context.resources.getString(R.string.itemWindows),
                context.resources.getString(R.string.itemDoors),
                context.resources.getString(R.string.itemElectrical),
                context.resources.getString(R.string.itemClosets),
                context.resources.getString(R.string.itemFireplace) -> {
                    livingRoomTable.addCell(
                        Cell().add(itemName).setFontSize(12f).setFont(font)
                            .setTextAlignment(TextAlignment.CENTER).setBold()
                    )
                }
                context.resources.getString(R.string.itemCarpet) -> {
                    val columnWidthCarpet = floatArrayOf(50f, 25f, 50f, 25f, 55f, 55f)
                    val table = Table(columnWidthCarpet)
                    table.setBorder(Border.NO_BORDER)
                    table.addCell(
                        Cell().add(itemNameParagraph).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    if (itemCondition1) {
                        table.addCell(
                            Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    } else {
                        table.addCell(
                            Cell().add(unCheckImage).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    }

                    table.addCell(
                        Cell().add(context.resources.getString(R.string.clean))
                            .setBorder(Border.NO_BORDER).setFont(font).setItalic()
                            .setFontSize(10f).setBold()
                    )
                    if (itemCondition2) {
                        table.addCell(
                            Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    } else {

                        table.addCell(
                            Cell().add(unCheckImage).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    }
                    table.addCell(
                        Cell().add(context.resources.getString(R.string.replace))
                            .setBorder(Border.NO_BORDER).setFont(font).setItalic()
                            .setFontSize(10f).setBold()
                    )
                    table.addCell(
                        Cell().add("If Replace,Size $itemNotes1").setFont(font).setBorder(
                            Border.NO_BORDER
                        ).setFontSize(10f).setBold()
                    )
                    livingRoomTable.addCell(
                        Cell().add(table).add(Paragraph(notes).setFontSize(10f))
                    )

                }
                context.resources.getString(R.string.itemPaint) -> {
                    val columnWidthCarpet = floatArrayOf(50f, 25f, 50f, 25f, 70f)
                    val table = Table(columnWidthCarpet)
                    table.setBorder(Border.NO_BORDER)
                    table.addCell(
                        Cell().add(itemNameParagraph).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    if (itemCondition1) {
                        table.addCell(
                            Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    } else {
                        table.addCell(
                            Cell().add(unCheckImage).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    }

                    table.addCell(
                        Cell().add(context.resources.getString(R.string.touchUp))
                            .setBorder(Border.NO_BORDER).setFont(font).setItalic()
                            .setFontSize(10f).setBold()
                    )
                    if (itemCondition2) {
                        table.addCell(
                            Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    } else {

                        table.addCell(
                            Cell().add(unCheckImage).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    }
                    table.addCell(
                        Cell().add(context.resources.getString(R.string.wholeWall))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setItalic().setFontSize(10f).setBold()
                    )
                    livingRoomTable.addCell(
                        Cell().add(table).add(Paragraph(notes).setFontSize(10f))
                    )

                }
                context.resources.getString(R.string.itemBlinds) -> {
                    val columnWidthCarpet =
                        floatArrayOf(50f)
                    val table = Table(columnWidthCarpet)
                    table.setBorder(Border.NO_BORDER)
                    table.addCell(
                        Cell().add(itemNameParagraph).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )

                    val width = floatArrayOf(50f, 80f)
                    val table1 = Table(width)
                    table1.addCell(
                        Cell().add(context.resources.getString(R.string.pdfDimensions))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f).setBold()
                    )
                    table1.addCell(
                        Cell().add(notesBlind).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    table1.addCell(
                        Cell().add(context.resources.getString(R.string.pdfColor))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f).setBold()
                    )
                    table1.addCell(
                        Cell().add(itemNotes1).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    table1.addCell(
                        Cell().add(context.resources.getString(R.string.pdfStyle))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f).setBold()
                    )
                    table1.addCell(
                        Cell().add(itemNotes2).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    table1.addCell(
                        Cell().add(context.resources.getString(R.string.pdfWand))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f).setBold()
                    )
                    table1.addCell(
                        Cell().add(itemNotes3).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    livingRoomTable.addCell(
                        Cell().add(table).add(table1)
                            .add(Paragraph(notes).setFontSize(10f))
                    )
                }
                context.resources.getString(R.string.itemEvidenceOfWaterDamage) -> {
                    val columnWidthCarpet = floatArrayOf(170f, 25f, 50f, 25f, 50f)
                    val table = Table(columnWidthCarpet)
                    table.setBorder(Border.NO_BORDER)
                    table.addCell(
                        Cell().add(itemNameParagraph).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    if (itemCondition1) {
                        table.addCell(
                            Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    } else {
                        table.addCell(
                            Cell().add(unCheckImage).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    }

                    table.addCell(
                        Cell().add(context.resources.getString(R.string.wall))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f).setBold()
                    )
                    if (itemCondition2) {
                        table.addCell(
                            Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    } else {

                        table.addCell(
                            Cell().add(unCheckImage).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    }
                    table.addCell(
                        Cell().add(context.resources.getString(R.string.ceiling))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f).setBold()
                    )
                    livingRoomTable.addCell(
                        Cell().add(table).add(Paragraph(notes).setFontSize(10f))
                    )

                }
                else -> {
                    livingRoomTable.addCell(
                        Cell().add(itemNameParagraph).add(notes).setFontSize(10f).setFont(font)
                    )
                }
            }
        }

        return livingRoomTable
    }

    // Add great room table in document
    private fun greatRoomAdd(list: List<AppData>, image: Image, unCheckImage: Image): Table {
        val greatRoomTable = Table(columnWidth)
        greatRoomTable.width = UnitValue.createPercentValue(100f)
        greatRoomTable.setFixedLayout()
        greatRoomTable.marginTop = 4f
        for (element in tableHeader) {
            greatRoomTable.addCell(
                Cell().add(element).setBold().setFont(font).setFontSize(10f)
                    .setTextAlignment(TextAlignment.CENTER)
            )
        }

        for (i in 0 until list[0].greatRoom.size) {
            if (list[0].greatRoom[i].ok.ok == context.resources.getString(R.string.itemOK)) {
                greatRoomTable.addCell(
                    Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                )
            } else {
                greatRoomTable.addCell(Cell().add(list[0].greatRoom[i].ok.ok))

            }

            if (list[0].greatRoom[i].na == context.resources.getString(R.string.itemNA)) {
                greatRoomTable.addCell(
                    Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                )

            } else {
                greatRoomTable.addCell(Cell().add(list[0].greatRoom[i].na))

            }
            if (list[0].greatRoom[i].fix.fix == context.resources.getString(R.string.itemFIX)) {

                greatRoomTable.addCell(
                    Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                )

            } else {
                greatRoomTable.addCell(Cell().add(list[0].greatRoom[i].fix.fix))

            }
            greatRoomTable.addCell(
                Cell().add(list[0].greatRoom[i].fix.time).setFontSize(10f).setFont(font)
            )
            greatRoomTable.addCell(
                Cell().add(list[0].greatRoom[i].fix.product).setFontSize(10f).setFont(font)
            )
            val notes = list[0].greatRoom[i].fix.notes
            val itemName = list[0].greatRoom[i].items.ItemName
            val itemNameParagraph = Paragraph(itemName).setBold()

            val itemCondition1 = list[0].greatRoom[i].items.ItemCondition1
            val itemCondition2 = list[0].greatRoom[i].items.ItemCondition2
            val itemNotes1 = list[0].greatRoom[i].items.ItemNotes1
            val itemNotes2 = list[0].greatRoom[i].items.ItemNotes2
            val itemNotes3 = list[0].greatRoom[i].items.ItemNotes3
            when (itemName) {
                context.resources.getString(R.string.itemCarpetAndFlooring),
                context.resources.getString(R.string.itemWallsBaseboardsCeiling),
                context.resources.getString(R.string.itemWindowsExteriorDoors),
                context.resources.getString(R.string.itemDoors),
                context.resources.getString(R.string.itemElectrical),
                context.resources.getString(R.string.itemClosets), context.resources.getString(R.string.itemFireplace) -> {
                    greatRoomTable.addCell(
                        Cell().add(itemNameParagraph).setFontSize(12f).setFont(font)
                            .setTextAlignment(TextAlignment.CENTER).setBold()
                    )

                }
                context.resources.getString(R.string.itemCarpet) -> {
                    val columnWidthCarpet = floatArrayOf(50f, 25f, 50f, 25f, 55f, 55f)
                    val table = Table(columnWidthCarpet)
                    table.setBorder(Border.NO_BORDER)
                    table.addCell(
                        Cell().add(itemNameParagraph).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    if (itemCondition1) {
                        table.addCell(
                            Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    } else {
                        table.addCell(
                            Cell().add(unCheckImage).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    }

                    table.addCell(
                        Cell().add(context.resources.getString(R.string.clean))
                            .setBorder(Border.NO_BORDER).setFont(font).setItalic()
                            .setFontSize(10f).setBold()
                    )
                    if (itemCondition2) {
                        table.addCell(
                            Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    } else {

                        table.addCell(
                            Cell().add(unCheckImage).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    }
                    table.addCell(
                        Cell().add(context.resources.getString(R.string.replace))
                            .setBorder(Border.NO_BORDER).setFont(font).setItalic()
                            .setFontSize(10f).setBold()
                    )
                    table.addCell(
                        Cell().add("If Replace,Size $itemNotes1").setFont(font).setBorder(
                            Border.NO_BORDER
                        ).setFontSize(10f).setBold()
                    )
                    greatRoomTable.addCell(
                        Cell().add(table).add(Paragraph(notes).setFontSize(10f))
                    )

                }
                context.resources.getString(R.string.itemPaint) -> {

                    val columnWidthCarpet = floatArrayOf(50f, 25f, 50f, 25f, 70f)
                    val table = Table(columnWidthCarpet)
                    table.setBorder(Border.NO_BORDER)
                    table.addCell(
                        Cell().add(itemNameParagraph).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    if (itemCondition1) {
                        table.addCell(
                            Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    } else {
                        table.addCell(
                            Cell().add(unCheckImage).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    }

                    table.addCell(
                        Cell().add(context.resources.getString(R.string.touchUp))
                            .setBorder(Border.NO_BORDER).setFont(font).setItalic()
                            .setFontSize(10f).setBold()
                    )
                    if (itemCondition2) {
                        table.addCell(
                            Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    } else {

                        table.addCell(
                            Cell().add(unCheckImage).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    }
                    table.addCell(
                        Cell().add(context.resources.getString(R.string.wholeWall))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setItalic().setFontSize(10f).setBold()
                    )
                    greatRoomTable.addCell(
                        Cell().add(table).add(Paragraph(notes).setFontSize(10f))
                    )

                }
                context.resources.getString(R.string.itemBlinds) -> {
                    val columnWidthCarpet =
                        floatArrayOf(50f)
                    val table = Table(columnWidthCarpet)
                    table.setBorder(Border.NO_BORDER)
                    table.addCell(
                        Cell().add(itemNameParagraph).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    val width = floatArrayOf(50f, 80f)
                    val table1 = Table(width)
                    table1.addCell(
                        Cell().add(context.resources.getString(R.string.pdfDimensions))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f).setBold()
                    )
                    table1.addCell(
                        Cell().add(list[0].greatRoom[i].items.itemNotes4)
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    table1.addCell(
                        Cell().add(context.resources.getString(R.string.pdfColor))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f).setBold()
                    )
                    table1.addCell(
                        Cell().add(itemNotes1).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    table1.addCell(
                        Cell().add(context.resources.getString(R.string.pdfStyle))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f).setBold()
                    )
                    table1.addCell(
                        Cell().add(itemNotes2).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    table1.addCell(
                        Cell().add(context.resources.getString(R.string.pdfWand))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f).setBold()
                    )
                    table1.addCell(
                        Cell().add(itemNotes3).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    greatRoomTable.addCell(
                        Cell().add(table).add(table1)
                            .add(Paragraph(notes).setFontSize(10f))
                    )
                }
                context.resources.getString(R.string.itemEvidenceOfWaterDamage) -> {
                    val columnWidthCarpet = floatArrayOf(170f, 25f, 50f, 25f, 50f)
                    val table = Table(columnWidthCarpet)
                    table.setBorder(Border.NO_BORDER)
                    table.addCell(
                        Cell().add(itemNameParagraph).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    if (itemCondition1) {
                        table.addCell(
                            Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    } else {
                        table.addCell(
                            Cell().add(unCheckImage).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    }

                    table.addCell(
                        Cell().add(context.resources.getString(R.string.wall))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f).setBold()
                    )
                    if (itemCondition2) {
                        table.addCell(
                            Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    } else {

                        table.addCell(
                            Cell().add(unCheckImage).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    }
                    table.addCell(
                        Cell().add(context.resources.getString(R.string.ceiling))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f).setBold()
                    )
                    greatRoomTable.addCell(
                        Cell().add(table).add(Paragraph(notes).setFontSize(10f))
                    )

                }
                else -> {
                    greatRoomTable.addCell(
                        Cell().add(itemNameParagraph).add(notes).setFontSize(10f).setFont(font)
                    )
                }
            }
        }
        return greatRoomTable

    }

    // Add dining room table in document
    private fun diningRoomAddTable(list: List<AppData>, image: Image, unCheckImage: Image): Table {

        val diningRoomTable = Table(columnWidth)
        diningRoomTable.width = UnitValue.createPercentValue(100f)
        diningRoomTable.setFixedLayout()
        diningRoomTable.marginTop = 4f
        for (element in tableHeader) {
            diningRoomTable.addCell(
                Cell().add(element).setBold().setFont(font).setFontSize(10f)
                    .setTextAlignment(TextAlignment.CENTER)
            )
        }

        for (i in 0 until list[0].diningRoom.size) {
            if (list[0].diningRoom[i].ok.ok == context.resources.getString(R.string.itemOK)) {
                diningRoomTable.addCell(
                    Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                )
            } else {
                diningRoomTable.addCell(Cell().add(list[0].diningRoom[i].ok.ok))

            }

            if (list[0].diningRoom[i].na == context.resources.getString(R.string.itemNA)) {
                diningRoomTable.addCell(
                    Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                )

            } else {
                diningRoomTable.addCell(Cell().add(list[0].diningRoom[i].na))

            }
            if (list[0].diningRoom[i].fix.fix == context.resources.getString(R.string.itemFIX)) {

                diningRoomTable.addCell(
                    Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                )

            } else {
                diningRoomTable.addCell(Cell().add(list[0].diningRoom[i].fix.fix))

            }
            diningRoomTable.addCell(
                Cell().add(list[0].diningRoom[i].fix.time).setFontSize(10f).setFont(font)
            )
            diningRoomTable.addCell(
                Cell().add(list[0].diningRoom[i].fix.product).setFontSize(10f).setFont(font)
            )
            val notes = list[0].diningRoom[i].fix.notes
            val itemName = list[0].diningRoom[i].items.ItemName
            val itemNameParagraph = Paragraph(itemName).setBold()
            val itemCondition1 = list[0].diningRoom[i].items.ItemCondition1
            val itemCondition2 = list[0].diningRoom[i].items.ItemCondition2
            val itemNotes1 = list[0].diningRoom[i].items.ItemNotes1
            val itemNotes2 = list[0].diningRoom[i].items.ItemNotes2
            val itemNotes3 = list[0].diningRoom[i].items.ItemNotes3

            when (itemName) {
                context.resources.getString(R.string.itemCarpetAndFlooring),
                context.resources.getString(R.string.itemWallsBaseboardsCeiling),
                context.resources.getString(R.string.itemWindowsExteriorDoors),
                context.resources.getString(R.string.itemDoors),
                context.resources.getString(R.string.itemElectrical),
                context.resources.getString(R.string.itemClosets), context.resources.getString(R.string.itemFireplace) -> {
                    diningRoomTable.addCell(
                        Cell().add(itemNameParagraph).setFontSize(12f).setFont(font)
                            .setTextAlignment(TextAlignment.CENTER).setBold()
                    )

                }
                context.resources.getString(R.string.itemCarpet) -> {
                    val columnWidthCarpet = floatArrayOf(50f, 25f, 50f, 25f, 55f, 55f)
                    val table = Table(columnWidthCarpet)
                    table.setBorder(Border.NO_BORDER)
                    table.addCell(
                        Cell().add(itemNameParagraph).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    if (itemCondition1) {
                        table.addCell(
                            Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    } else {
                        table.addCell(
                            Cell().add(unCheckImage).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    }

                    table.addCell(
                        Cell().add(context.resources.getString(R.string.clean))
                            .setBorder(Border.NO_BORDER).setFont(font).setItalic()
                            .setFontSize(10f).setBold()
                    )
                    if (itemCondition2) {
                        table.addCell(
                            Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    } else {

                        table.addCell(
                            Cell().add(unCheckImage).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    }
                    table.addCell(
                        Cell().add(context.resources.getString(R.string.replace))
                            .setBorder(Border.NO_BORDER).setFont(font).setItalic()
                            .setFontSize(10f).setBold()
                    )
                    table.addCell(
                        Cell().add("If Replace,Size $itemNotes1").setFont(font).setBorder(
                            Border.NO_BORDER
                        ).setFontSize(10f).setBold()
                    )
                    diningRoomTable.addCell(
                        Cell().add(table).add(Paragraph(notes).setFontSize(10f))
                    )

                }
                context.resources.getString(R.string.itemPaint) -> {

                    val columnWidthCarpet = floatArrayOf(50f, 25f, 50f, 25f, 70f)
                    val table = Table(columnWidthCarpet)
                    table.setBorder(Border.NO_BORDER)
                    table.addCell(
                        Cell().add(itemNameParagraph).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    if (itemCondition1) {
                        table.addCell(
                            Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    } else {
                        table.addCell(
                            Cell().add(unCheckImage).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    }

                    table.addCell(
                        Cell().add(context.resources.getString(R.string.touchUp))
                            .setBorder(Border.NO_BORDER).setFont(font).setItalic()
                            .setFontSize(10f).setBold()
                    )
                    if (itemCondition2) {
                        table.addCell(
                            Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    } else {

                        table.addCell(
                            Cell().add(unCheckImage).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    }
                    table.addCell(
                        Cell().add(context.resources.getString(R.string.wholeWall))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setItalic().setFontSize(10f).setBold()
                    )
                    diningRoomTable.addCell(
                        Cell().add(table).add(Paragraph(notes).setFontSize(10f))
                    )

                }
                context.resources.getString(R.string.itemBlinds) -> {
                    val columnWidthCarpet =
                        floatArrayOf(50f)
                    val table = Table(columnWidthCarpet)
                    table.setBorder(Border.NO_BORDER)
                    table.addCell(
                        Cell().add(itemNameParagraph).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    val width = floatArrayOf(50f, 80f)
                    val table1 = Table(width)
                    table1.addCell(
                        Cell().add(context.resources.getString(R.string.pdfDimensions))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f).setBold()
                    )
                    table1.addCell(
                        Cell().add(list[0].diningRoom[i].items.itemNotes4)
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    table1.addCell(
                        Cell().add(context.resources.getString(R.string.pdfColor))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f).setBold()
                    )
                    table1.addCell(
                        Cell().add(itemNotes1).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    table1.addCell(
                        Cell().add(context.resources.getString(R.string.pdfStyle))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f).setBold()
                    )
                    table1.addCell(
                        Cell().add(itemNotes2).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    table1.addCell(
                        Cell().add(context.resources.getString(R.string.pdfWand))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f).setBold()
                    )
                    table1.addCell(
                        Cell().add(itemNotes3).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    diningRoomTable.addCell(
                        Cell().add(table).add(table1)
                            .add(Paragraph(notes).setFontSize(10f))
                    )
                }
                context.resources.getString(R.string.itemEvidenceOfWaterDamage) -> {
                    val columnWidthCarpet = floatArrayOf(170f, 25f, 50f, 25f, 50f)
                    val table = Table(columnWidthCarpet)
                    table.setBorder(Border.NO_BORDER)
                    table.addCell(
                        Cell().add(itemNameParagraph).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    if (itemCondition1) {
                        table.addCell(
                            Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    } else {
                        table.addCell(
                            Cell().add(unCheckImage).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    }

                    table.addCell(
                        Cell().add(context.resources.getString(R.string.wall))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f).setBold()
                    )
                    if (itemCondition2) {
                        table.addCell(
                            Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    } else {

                        table.addCell(
                            Cell().add(unCheckImage).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    }
                    table.addCell(
                        Cell().add(context.resources.getString(R.string.ceiling))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f).setBold()
                    )
                    diningRoomTable.addCell(
                        Cell().add(table).add(Paragraph(notes).setFontSize(10f))
                    )

                }
                else -> {
                    diningRoomTable.addCell(
                        Cell().add(itemNameParagraph).add(notes).setFontSize(10f).setFont(font)
                    )
                }
            }
        }
        return diningRoomTable


    }

    // Add kitchen table in document

    private fun kitchenTableAdd(list: List<AppData>, image: Image, unCheckImage: Image): Table {
        val kitchenTable = Table(columnWidth)
        kitchenTable.width = UnitValue.createPercentValue(100f)
        kitchenTable.setFixedLayout()
        kitchenTable.marginTop = 4f
        for (element in tableHeader) {
            kitchenTable.addCell(
                Cell().add(element).setBold().setFont(font).setFontSize(10f)
                    .setTextAlignment(TextAlignment.CENTER)
            )
        }

        for (i in 0 until list[0].kitchen.size) {
            if (list[0].kitchen[i].ok.ok == context.resources.getString(R.string.itemOK)) {
                kitchenTable.addCell(
                    Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                )
            } else {
                kitchenTable.addCell(Cell().add(list[0].kitchen[i].ok.ok))

            }

            if (list[0].kitchen[i].na == context.resources.getString(R.string.itemNA)) {
                kitchenTable.addCell(
                    Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                )

            } else {
                kitchenTable.addCell(Cell().add(list[0].kitchen[i].na))

            }
            if (list[0].kitchen[i].fix.fix == context.resources.getString(R.string.itemFIX)) {

                kitchenTable.addCell(
                    Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                )

            } else {
                kitchenTable.addCell(Cell().add(list[0].kitchen[i].fix.fix))

            }
            kitchenTable.addCell(
                Cell().add(list[0].kitchen[i].fix.time).setFontSize(10f).setFont(font)
            )
            kitchenTable.addCell(
                Cell().add(list[0].kitchen[i].fix.product).setFontSize(10f).setFont(font)
            )
            val notes = list[0].kitchen[i].fix.notes
            val itemName = list[0].kitchen[i].items.ItemName
            val itemNameParagraph = Paragraph(itemName).setBold()

            val itemCondition1 = list[0].kitchen[i].items.ItemCondition1
            val itemCondition2 = list[0].kitchen[i].items.ItemCondition2
            val itemNotes1 = list[0].kitchen[i].items.ItemNotes1
            val itemNotes2 = list[0].kitchen[i].items.ItemNotes2
            val itemNotes3 = list[0].kitchen[i].items.ItemNotes3
            val notesBlind = list[0].kitchen[i].items.itemNotes4

            when (itemName) {
                context.resources.getString(R.string.itemElectrical),
                context.resources.getString(R.string.itemSink),
                context.resources.getString(R.string.itemWindows),
                context.resources.getString(R.string.pdfAppliances),
                context.resources.getString(R.string.itemPantry) -> {
                    kitchenTable.addCell(
                        Cell().add(itemName).setFontSize(12f).setFont(font)
                            .setTextAlignment(TextAlignment.CENTER).setBold()
                    )

                }
                context.resources.getString(R.string.itemBlinds) -> {
                    val columnWidthCarpet =
                        floatArrayOf(50f)
                    val table = Table(columnWidthCarpet)
                    table.setBorder(Border.NO_BORDER)
                    table.addCell(
                        Cell().add(itemNameParagraph).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )

                    val width = floatArrayOf(50f, 80f)
                    val table1 = Table(width)
                    table1.addCell(
                        Cell().add(context.resources.getString(R.string.pdfDimensions))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f).setBold()
                    )
                    table1.addCell(
                        Cell().add(notesBlind).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    table1.addCell(
                        Cell().add(context.resources.getString(R.string.pdfColor))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f).setBold()
                    )
                    table1.addCell(
                        Cell().add(itemNotes1).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    table1.addCell(
                        Cell().add(context.resources.getString(R.string.pdfStyle))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f).setBold()
                    )
                    table1.addCell(
                        Cell().add(itemNotes2).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    table1.addCell(
                        Cell().add(context.resources.getString(R.string.pdfWand))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f).setBold()
                    )
                    table1.addCell(
                        Cell().add(itemNotes3).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    kitchenTable.addCell(
                        Cell().add(table).add(table1)
                            .add(Paragraph(notes).setFontSize(10f))
                    )
                }
                context.resources.getString(R.string.itemRefrigerator) -> {
                    val columnWidthCarpet = floatArrayOf(150f, 20f, 70f, 20f, 70f)
                    val table = Table(columnWidthCarpet)
                    table.setBorder(Border.NO_BORDER)
                    table.addCell(
                        Cell().add(itemNameParagraph).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    if (itemCondition1) {
                        table.addCell(
                            Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    } else {
                        table.addCell(
                            Cell().add(unCheckImage).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    }

                    table.addCell(
                        Cell().add(context.resources.getString(R.string.drawers))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f).setBold()
                    )
                    if (itemCondition2) {
                        table.addCell(
                            Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    } else {

                        table.addCell(
                            Cell().add(unCheckImage).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    }
                    table.addCell(
                        Cell().add(context.resources.getString(R.string.shelves))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f).setBold()
                    )
                    kitchenTable.addCell(
                        Cell().add(table).add(Paragraph(notes).setFontSize(10f))
                    )
                }
                context.resources.getString(R.string.itemDishwasher) -> {
                    val columnWidthCarpet = floatArrayOf(100f, 20f, 70f)
                    val table = Table(columnWidthCarpet)
                    table.setBorder(Border.NO_BORDER)
                    table.addCell(
                        Cell().add(itemNameParagraph).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    if (itemCondition1) {
                        table.addCell(
                            Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    } else {
                        table.addCell(
                            Cell().add(unCheckImage).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    }

                    table.addCell(
                        Cell().add(context.resources.getString(R.string.insideRacks))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f).setBold()
                    )
                    kitchenTable.addCell(
                        Cell().add(table).add(Paragraph(notes).setFontSize(10f))
                    )

                }
                else -> {
                    kitchenTable.addCell(
                        Cell().add(itemNameParagraph).add(notes).setFontSize(10f).setFont(font)
                    )
                }
            }
        }
        return kitchenTable

    }

    // Add miscellaneous  table in document
    private fun miscellaneousTableAdd(
        list: List<AppData>,
        image: Image,
        unCheckImage: Image
    ): Table {
        val miscellaneousTable = Table(columnWidth)
        miscellaneousTable.width = UnitValue.createPercentValue(100f)
        miscellaneousTable.setFixedLayout()
        miscellaneousTable.marginTop = 4f
        for (element in tableHeader) {
            miscellaneousTable.addCell(
                Cell().add(element).setBold().setFont(font).setFontSize(10f)
                    .setTextAlignment(TextAlignment.CENTER)
            )
        }

        for (i in 0 until list[0].miscellaneous.size) {
            if (list[0].miscellaneous[i].ok.ok == context.resources.getString(R.string.itemOK)) {
                miscellaneousTable.addCell(
                    Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                )
            } else {
                miscellaneousTable.addCell(Cell().add(list[0].miscellaneous[i].ok.ok))

            }

            if (list[0].miscellaneous[i].na == context.resources.getString(R.string.itemNA)) {
                miscellaneousTable.addCell(
                    Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                )

            } else {
                miscellaneousTable.addCell(Cell().add(list[0].miscellaneous[i].na))


            }
            if (list[0].miscellaneous[i].fix.fix == context.resources.getString(R.string.itemFIX)) {

                miscellaneousTable.addCell(
                    Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                )

            } else {
                miscellaneousTable.addCell(Cell().add(list[0].miscellaneous[i].fix.fix))

            }
            miscellaneousTable.addCell(
                Cell().add(list[0].miscellaneous[i].fix.time).setFontSize(10f).setFont(font)
            )
            miscellaneousTable.addCell(
                Cell().add(list[0].miscellaneous[i].fix.product).setFontSize(10f).setFont(font)
            )
            val notes = list[0].miscellaneous[i].fix.notes
            val itemName = list[0].miscellaneous[i].items.ItemName
            val itemNameParagraph = Paragraph(itemName).setBold()

            val itemCondition1 = list[0].miscellaneous[i].items.ItemCondition1
            val itemCondition2 = list[0].miscellaneous[i].items.ItemCondition2


            when (itemName) {
                context.resources.getString(R.string.itemCleaning),
                context.resources.getString(R.string.itemSmokeDetectors),
                context.resources.getString(R.string.itemStairsHallway),
                context.resources.getString(R.string.itemHAVC) -> {
                    miscellaneousTable.addCell(
                        Cell().add(itemName).setFontSize(12f).setFont(font)
                            .setTextAlignment(TextAlignment.CENTER).setBold()
                    )

                }
                context.resources.getString(R.string.itemACFiltersCoverCapsScrews),
                context.resources.getString(R.string.itemFoamAroundCompressor) -> {
                    val columnWidthCarpet = floatArrayOf(180f, 20f, 70f, 20f, 70f)
                    val table = Table(columnWidthCarpet)
                    table.setBorder(Border.NO_BORDER)
                    table.addCell(
                        Cell().add(itemNameParagraph).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    if (itemCondition1) {
                        table.addCell(
                            Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    } else {
                        table.addCell(
                            Cell().add(unCheckImage).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    }

                    table.addCell(
                        Cell().add(context.resources.getString(R.string.repair))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f).setItalic().setBold()
                    )
                    if (itemCondition2) {
                        table.addCell(
                            Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    } else {

                        table.addCell(
                            Cell().add(unCheckImage).setBorder(Border.NO_BORDER).setFontSize(10f)
                        )
                    }
                    table.addCell(
                        Cell().add(context.resources.getString(R.string.replace))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f).setItalic().setBold()
                    )
                    miscellaneousTable.addCell(
                        Cell().add(table).add(Paragraph(notes).setFontSize(10f))
                    )
                }
                context.resources.getString(R.string.itemBlinds) -> {

                    val columnWidthCarpet = floatArrayOf(50f, 80f)
                    val table = Table(columnWidthCarpet)
                    table.setBorder(Border.NO_BORDER)
                    table.addCell(
                        Cell().add(itemNameParagraph).setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )
                    table.addCell(
                        Cell().add("").setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )

                    table.addCell(
                        Cell().add(context.resources.getString(R.string.pdfDimensions))
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f).setBold()
                    )
                    table.addCell(
                        Cell().add(list[0].miscellaneous[i].items.itemNotes4)
                            .setBorder(Border.NO_BORDER).setFont(font)
                            .setFontSize(10f)
                    )

                    miscellaneousTable.addCell(
                        Cell().add(table).add(Paragraph(notes).setFontSize(10f))
                    )

                }
                else -> {
                    miscellaneousTable.addCell(
                        Cell().add(itemNameParagraph).add(notes).setFontSize(10f).setFont(font)
                    )
                }
            }
        }
        return miscellaneousTable

    }

    // Add bedroom table in document
    private fun bedroomTableAdd(
        list: List<AppData>,
        image: Image,
        unCheckImage: Image
    ): List<Table> {
        val tableList = mutableListOf<Table>()

        for (bedroom in 0 until list[0].bedroom.size) {
            val bedroomTable = Table(columnWidth)
            bedroomTable.width = UnitValue.createPercentValue(100f)
            bedroomTable.setFixedLayout()
            bedroomTable.marginTop = 4f
            for (element in tableHeader) {
                bedroomTable.addCell(
                    Cell().add(element).setBold().setFont(font).setFontSize(10f)
                        .setTextAlignment(TextAlignment.CENTER)
                )
            }
            for (i in 0 until list[0].bedroom[bedroom].list.size) {
                if (list[0].bedroom[bedroom].list[i].ok.ok == context.resources.getString(R.string.itemOK)) {

                    bedroomTable.addCell(
                        Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                    )
                } else {
                    bedroomTable.addCell(Cell().add(list[0].bedroom[bedroom].list[i].ok.ok))

                }

                if (list[0].bedroom[bedroom].list[i].na == context.resources.getString(R.string.itemNA)) {
                    bedroomTable.addCell(
                        Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                    )

                } else {
                    bedroomTable.addCell(Cell().add(list[0].bedroom[bedroom].list[i].na))

                }
                if (list[0].bedroom[bedroom].list[i].fix.fix == context.resources.getString(R.string.itemFIX)) {

                    bedroomTable.addCell(
                        Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                    )

                } else {
                    bedroomTable.addCell(Cell().add(list[0].bedroom[bedroom].list[i].fix.fix))

                }
                bedroomTable.addCell(
                    Cell().add(list[0].bedroom[bedroom].list[i].fix.time).setFontSize(10f)
                        .setFont(font)
                )
                bedroomTable.addCell(
                    Cell().add(list[0].bedroom[bedroom].list[i].fix.product).setFontSize(10f)
                        .setFont(font)
                )
                val notes = list[0].bedroom[bedroom].list[i].fix.notes
                val itemName = list[0].bedroom[bedroom].list[i].items.ItemName
                val itemNameParagraph = Paragraph(itemName).setBold()


                val itemCondition1 = list[0].bedroom[bedroom].list[i].items.ItemCondition1
                val itemCondition2 = list[0].bedroom[bedroom].list[i].items.ItemCondition2
                val itemNotes1 = list[0].bedroom[bedroom].list[i].items.ItemNotes1
                val itemNotes2 = list[0].bedroom[bedroom].list[i].items.ItemNotes2
                val itemNotes3 = list[0].bedroom[bedroom].list[i].items.ItemNotes3
                val itemNotes4 = list[0].bedroom[bedroom].list[i].items.itemNotes4
                when (itemName) {
                    context.resources.getString(R.string.itemCarpetAndFlooring),
                    context.resources.getString(R.string.itemWallsBaseboardsCeiling),
                    context.resources.getString(R.string.itemWindowsSlidingGlassDoors),
                    context.resources.getString(R.string.itemDoors),
                    context.resources.getString(R.string.itemElectrical),
                    context.resources.getString(R.string.itemClosets) -> {
                        bedroomTable.addCell(
                            Cell().add(itemName).setFontSize(12f).setFont(font)
                                .setTextAlignment(TextAlignment.CENTER).setBold()
                        )

                    }
                    context.resources.getString(R.string.itemCarpet) -> {
                        val columnWidthCarpet = floatArrayOf(50f, 25f, 50f, 25f, 55f, 55f)
                        val table = Table(columnWidthCarpet)
                        table.setBorder(Border.NO_BORDER)
                        table.addCell(
                            Cell().add(itemNameParagraph).setBorder(Border.NO_BORDER).setFont(font)
                                .setFontSize(10f)
                        )
                        if (itemCondition1) {
                            table.addCell(
                                Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                            )
                        } else {
                            table.addCell(
                                Cell().add(unCheckImage).setBorder(Border.NO_BORDER)
                                    .setFontSize(10f)
                            )
                        }

                        table.addCell(
                            Cell().add(context.resources.getString(R.string.clean))
                                .setBorder(Border.NO_BORDER).setFont(font)
                                .setItalic()
                                .setFontSize(10f).setBold()
                        )
                        if (itemCondition2) {
                            table.addCell(
                                Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                            )
                        } else {

                            table.addCell(
                                Cell().add(unCheckImage).setBorder(Border.NO_BORDER)
                                    .setFontSize(10f)
                            )
                        }
                        table.addCell(
                            Cell().add(context.resources.getString(R.string.replace))
                                .setBorder(Border.NO_BORDER).setFont(font)
                                .setItalic()
                                .setFontSize(10f).setBold()
                        )
                        table.addCell(
                            Cell().add("If Replace,Size $itemNotes1").setFont(font).setBorder(
                                Border.NO_BORDER
                            ).setFontSize(10f).setBold()
                        )
                        bedroomTable.addCell(
                            Cell().add(table).add(Paragraph(notes).setFontSize(10f))
                        )

                    }
                    context.resources.getString(R.string.itemPaint) -> {

                        val columnWidthCarpet = floatArrayOf(50f, 25f, 50f, 25f, 70f)
                        val table = Table(columnWidthCarpet)
                        table.setBorder(Border.NO_BORDER)
                        table.addCell(
                            Cell().add(itemNameParagraph).setBorder(Border.NO_BORDER).setFont(font)
                                .setFontSize(10f)
                        )
                        if (itemCondition1) {
                            table.addCell(
                                Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                            )
                        } else {
                            table.addCell(
                                Cell().add(unCheckImage).setBorder(Border.NO_BORDER)
                                    .setFontSize(10f)
                            )
                        }

                        table.addCell(
                            Cell().add(context.resources.getString(R.string.touchUp))
                                .setBorder(Border.NO_BORDER).setFont(font)
                                .setItalic()
                                .setFontSize(10f).setBold()
                        )
                        if (itemCondition2) {
                            table.addCell(
                                Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                            )
                        } else {

                            table.addCell(
                                Cell().add(unCheckImage).setBorder(Border.NO_BORDER)
                                    .setFontSize(10f)
                            )
                        }
                        table.addCell(
                            Cell().add(context.resources.getString(R.string.wholeWall))
                                .setBorder(Border.NO_BORDER).setFont(font)
                                .setItalic().setFontSize(10f).setBold()
                        )
                        bedroomTable.addCell(
                            Cell().add(table).add(Paragraph(notes).setFontSize(10f))
                        )

                    }
                    context.resources.getString(R.string.itemBlinds) -> {
                        val columnWidthCarpet =
                            floatArrayOf(50f)
                        val table = Table(columnWidthCarpet)
                        table.setBorder(Border.NO_BORDER)
                        table.addCell(
                            Cell().add(itemNameParagraph).setBorder(Border.NO_BORDER).setFont(font)
                                .setFontSize(10f)
                        )
                        val width = floatArrayOf(50f, 80f)
                        val table1 = Table(width)
                        table1.addCell(
                            Cell().add(context.resources.getString(R.string.pdfDimensions))
                                .setBorder(Border.NO_BORDER).setFont(font)
                                .setFontSize(10f).setBold()
                        )
                        table1.addCell(
                            Cell().add(itemNotes4).setBorder(Border.NO_BORDER).setFont(font)
                                .setFontSize(10f)
                        )
                        table1.addCell(
                            Cell().add(context.resources.getString(R.string.pdfColor))
                                .setBorder(Border.NO_BORDER).setFont(font)
                                .setFontSize(10f).setBold()
                        )
                        table1.addCell(
                            Cell().add(itemNotes1).setBorder(Border.NO_BORDER).setFont(font)
                                .setFontSize(10f)
                        )
                        table1.addCell(
                            Cell().add(context.resources.getString(R.string.pdfStyle))
                                .setBorder(Border.NO_BORDER).setFont(font)
                                .setFontSize(10f).setBold()
                        )
                        table1.addCell(
                            Cell().add(itemNotes2).setBorder(Border.NO_BORDER).setFont(font)
                                .setFontSize(10f)
                        )
                        table1.addCell(
                            Cell().add(context.resources.getString(R.string.pdfWand))
                                .setBorder(Border.NO_BORDER).setFont(font)
                                .setFontSize(10f).setBold()
                        )
                        table1.addCell(
                            Cell().add(itemNotes3).setBorder(Border.NO_BORDER).setFont(font)
                                .setFontSize(10f)
                        )
                        bedroomTable.addCell(
                            Cell().add(table).add(table1)
                                .add(Paragraph(notes).setFontSize(10f))
                        )
                    }
                    context.resources.getString(R.string.itemEvidenceOfWaterDamage) -> {
                        val columnWidthCarpet = floatArrayOf(170f, 25f, 50f, 25f, 50f)
                        val table = Table(columnWidthCarpet)
                        table.setBorder(Border.NO_BORDER)
                        table.addCell(
                            Cell().add(itemNameParagraph).setBorder(Border.NO_BORDER).setFont(font)
                                .setFontSize(10f)
                        )
                        if (itemCondition1) {
                            table.addCell(
                                Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                            )
                        } else {
                            table.addCell(
                                Cell().add(unCheckImage).setBorder(Border.NO_BORDER)
                                    .setFontSize(10f)
                            )
                        }

                        table.addCell(
                            Cell().add(context.resources.getString(R.string.wall))
                                .setBorder(Border.NO_BORDER).setFont(font)
                                .setFontSize(10f).setBold()
                        )
                        if (itemCondition2) {
                            table.addCell(
                                Cell().add(image).setBorder(Border.NO_BORDER).setFontSize(10f)
                            )
                        } else {

                            table.addCell(
                                Cell().add(unCheckImage).setBorder(Border.NO_BORDER)
                                    .setFontSize(10f)
                            )
                        }
                        table.addCell(
                            Cell().add(context.resources.getString(R.string.ceiling))
                                .setBorder(Border.NO_BORDER).setFont(font)
                                .setFontSize(10f).setBold()
                        )
                        bedroomTable.addCell(
                            Cell().add(table).add(Paragraph(notes).setFontSize(10f))
                        )

                    }
                    else -> {
                        bedroomTable.addCell(
                            Cell().add(itemNameParagraph).add(notes).setFontSize(10f).setFont(font)
                        )
                    }
                }
            }
            tableList.add(bedroomTable)
        }

        return tableList
    }

    // Add bathroom table in document
    private fun bathroomTableAdd(
        list: List<AppData>,
        image: Image
    ): List<Table> {
        val tableList = mutableListOf<Table>()

        for (bathroom in 0 until list[0].bathroom.size) {
            val bathroomTable = Table(columnWidth)
            bathroomTable.width = UnitValue.createPercentValue(100f)
            bathroomTable.setFixedLayout()
            bathroomTable.marginTop = 4f
            for (element in tableHeader) {
                bathroomTable.addCell(
                    Cell().add(element).setBold().setFont(font).setFontSize(10f)
                        .setTextAlignment(TextAlignment.CENTER)
                )
            }


            for (i in 0 until list[0].bathroom[bathroom].list.size) {

                if (list[0].bathroom[bathroom].list[i].ok.ok == context.resources.getString(R.string.itemOK)) {
                    bathroomTable.addCell(
                        Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                    )
                } else {
                    bathroomTable.addCell(Cell().add(list[0].bathroom[bathroom].list[i].ok.ok))

                }

                if (list[0].bathroom[bathroom].list[i].na == context.resources.getString(R.string.itemNA)) {
                    bathroomTable.addCell(
                        Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                    )

                } else {
                    bathroomTable.addCell(Cell().add(list[0].bathroom[bathroom].list[i].na))

                }
                if (list[0].bathroom[bathroom].list[i].fix.fix == context.resources.getString(R.string.itemFIX)) {

                    bathroomTable.addCell(
                        Cell().add(Cell().add(image)).setTextAlignment(TextAlignment.CENTER)
                    )

                } else {
                    bathroomTable.addCell(Cell().add(list[0].bathroom[bathroom].list[i].fix.fix))

                }
                bathroomTable.addCell(
                    Cell().add(list[0].bathroom[bathroom].list[i].fix.time).setFontSize(10f)
                        .setFont(font)
                )
                bathroomTable.addCell(
                    Cell().add(list[0].bathroom[bathroom].list[i].fix.product).setFontSize(10f)
                        .setFont(font)
                )
                val notes = list[0].bathroom[bathroom].list[i].fix.notes
                val itemName = list[0].bathroom[bathroom].list[i].items.ItemName
                val itemNameParagraph = Paragraph(itemName).setBold()


                if (itemName == context.resources.getString(R.string.itemShowerTub) || itemName == context.resources.getString(
                        R.string.itemSink
                    ) ||
                    itemName == context.resources.getString(R.string.itemToilet) ||
                    itemName == context.resources.getString(R.string.itemDoors)
                ) {
                    bathroomTable.addCell(
                        Cell().add(itemName).setFontSize(12f).setFont(font)
                            .setTextAlignment(TextAlignment.CENTER).setBold()
                    )

                } else {

                    bathroomTable.addCell(
                        Cell().add(itemNameParagraph).add(notes).setFontSize(10f).setFont(font)
                    )
                }

            }
            tableList.add(bathroomTable)
        }

        return tableList

    }

    // Add images for home screen table in document

    private fun imageHomeScreeAdd(list: List<AppData>): Table {

        val columnWidth = floatArrayOf(350f, 350f)
        val imageHomeScreenTable = Table(columnWidth)
        imageHomeScreenTable.marginTop = 25f
        imageHomeScreenTable.marginRight = 35f
        val imageList = arrayListOf<String>()

        for (i in 0 until list[0].homeScreen[0].image.size) {
            val image = list[0].homeScreen[0].image[i].imagePath
            imageList.add(image)
            val homeImage =
                BitmapFactory.decodeFile(image)
            if (homeImage != null) {
               // homeImage.scale(1, 1)
               // val getBit = resizeBitmap(homeImage, 90, 70)
                val stream = ByteArrayOutputStream()
                homeImage.compress(Bitmap.CompressFormat.JPEG, 25, stream)
                val byteArray: ByteArray = stream.toByteArray()
                val imgData = ImageDataFactory.create(byteArray)
                val img = Image(imgData)
                img.setHeight(200f)
                img.setWidth(250f)
                val columnWidthImage = floatArrayOf(350f)
                val tableImage = Table(columnWidthImage)
                tableImage.marginTop = 2f
                tableImage.marginBottom = 2f
                tableImage.marginLeft = 2f
                tableImage.marginRight = 2f
                tableImage.addCell(
                    Cell().add(img).setBorder(Border.NO_BORDER)
                        .setTextAlignment(TextAlignment.CENTER)
                )
                imageHomeScreenTable.addCell(
                    Cell().add(tableImage).setTextAlignment(TextAlignment.CENTER)
                )
            }

        }
        if (imageList.size % 2 == 1) {
            imageHomeScreenTable.addCell(
                Cell().add("").setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER)
            )
                .setBorder(Border.NO_BORDER)
        }
        return imageHomeScreenTable
    }

    private fun commonImagesTableAdd(list: List<ImageUploadCommon>): Pair<Table, Boolean> {
        val columnWidth = floatArrayOf(350f, 350f)
        val imageTable = Table(columnWidth)
        imageTable.marginTop = 25f
        imageTable.marginRight = 35f

        for (i in list.indices) {
            val imageTitleBold = Paragraph(list[i].imageTitle).setBold()
            val tableImage = commonImageTable(list[i].imagePath, imageTitleBold)
            imageTable.addCell(
                Cell().add(tableImage).setTextAlignment(TextAlignment.CENTER)
            )
        }

        if (list.size % 2 == 1) {
            imageTable.addCell(
                Cell().add("").setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER)
            )
                .setBorder(Border.NO_BORDER)
        }

        return Pair(imageTable, list.isNotEmpty())
    }

    // Add images for outside table in document

    private fun newOutsideImageList(list: List<Outside>): Pair<List<ImageUploadCommon>, List<ImageUploadCommon>> {
        val fixImageList = arrayListOf<ImageUploadCommon>()
        val okImageList = arrayListOf<ImageUploadCommon>()

        for (i in list.indices) {
            for (j in list[i].fix.image.indices) {
                fixImageList.add(list[i].fix.image[j])
            }
            for (j in list[i].ok.image.indices) {
                okImageList.add(list[i].ok.image[j])
            }
        }

        return Pair(fixImageList, okImageList)
    }

    // Add images for frontDoor table in document
    private fun frontDoorImageList(list: List<FrontDoors>): Pair<List<ImageUploadCommon>, List<ImageUploadCommon>> {
        val fixImageList = arrayListOf<ImageUploadCommon>()
        val okImageList = arrayListOf<ImageUploadCommon>()

        for (i in list.indices) {
            for (j in list[i].fix.image.indices) {
                fixImageList.add(list[i].fix.image[j])
            }
            for (j in list[i].ok.image.indices) {
                okImageList.add(list[i].ok.image[j])
            }
        }

        return Pair(fixImageList, okImageList)
    }

    // Add images for garage table in document
    private fun garageImageList(list: List<Garage>): Pair<List<ImageUploadCommon>, List<ImageUploadCommon>> {
        val fixImageList = arrayListOf<ImageUploadCommon>()
        val okImageList = arrayListOf<ImageUploadCommon>()

        for (i in list.indices) {
            for (j in list[i].fix.image.indices) {
                fixImageList.add(list[i].fix.image[j])
            }
            for (j in list[i].ok.image.indices) {
                okImageList.add(list[i].ok.image[j])
            }
        }

        return Pair(fixImageList, okImageList)
    }

    // Add images for laundry table in document
    private fun laundryImageList(list: List<LaundryRoom>): Pair<List<ImageUploadCommon>, List<ImageUploadCommon>> {
        val fixImageList = arrayListOf<ImageUploadCommon>()
        val okImageList = arrayListOf<ImageUploadCommon>()

        for (i in list.indices) {
            for (j in list[i].fix.image.indices) {
                fixImageList.add(list[i].fix.image[j])
            }
            for (j in list[i].ok.image.indices) {
                okImageList.add(list[i].ok.image[j])
            }
        }

        return Pair(fixImageList, okImageList)
    }

    // Add images for livingRoom table in document
    private fun livingRoomImageList(list: List<LivingRoom>): Pair<List<ImageUploadCommon>, List<ImageUploadCommon>> {
        val fixImageList = arrayListOf<ImageUploadCommon>()
        val okImageList = arrayListOf<ImageUploadCommon>()

        for (i in list.indices) {
            for (j in list[i].fix.image.indices) {
                fixImageList.add(list[i].fix.image[j])
            }
            for (j in list[i].ok.image.indices) {
                okImageList.add(list[i].ok.image[j])
            }
        }

        return Pair(fixImageList, okImageList)
    }


    // Add images for greatRoom table in document

    private fun greatRoomImageList(list: List<GreatRoom>): Pair<List<ImageUploadCommon>, List<ImageUploadCommon>> {
        val fixImageList = arrayListOf<ImageUploadCommon>()
        val okImageList = arrayListOf<ImageUploadCommon>()

        for (i in list.indices) {
            for (j in list[i].fix.image.indices) {
                fixImageList.add(list[i].fix.image[j])
            }
            for (j in list[i].ok.image.indices) {
                okImageList.add(list[i].ok.image[j])
            }
        }

        return Pair(fixImageList, okImageList)
    }

    // Add images for Dining table in document
    private fun diningRoomImageList(list: List<DiningRoom>): Pair<List<ImageUploadCommon>, List<ImageUploadCommon>> {
        val fixImageList = arrayListOf<ImageUploadCommon>()
        val okImageList = arrayListOf<ImageUploadCommon>()

        for (i in list.indices) {
            for (j in list[i].fix.image.indices) {
                fixImageList.add(list[i].fix.image[j])
            }
            for (j in list[i].ok.image.indices) {
                okImageList.add(list[i].ok.image[j])
            }
        }

        return Pair(fixImageList, okImageList)
    }

    // Add images for Kitchen table in document
    private fun kitchenImageList(list: List<Kitchen>): Pair<List<ImageUploadCommon>, List<ImageUploadCommon>> {
        val fixImageList = arrayListOf<ImageUploadCommon>()
        val okImageList = arrayListOf<ImageUploadCommon>()

        for (i in list.indices) {
            for (j in list[i].fix.image.indices) {
                fixImageList.add(list[i].fix.image[j])
            }
            for (j in list[i].ok.image.indices) {
                okImageList.add(list[i].ok.image[j])
            }
        }

        return Pair(fixImageList, okImageList)
    }

    // Add images for Miscellaneous table in document
    private fun miscellaneousImageList(list: List<Miscellaneous>): Pair<List<ImageUploadCommon>, List<ImageUploadCommon>> {
        val fixImageList = arrayListOf<ImageUploadCommon>()
        val okImageList = arrayListOf<ImageUploadCommon>()

        for (i in list.indices) {
            for (j in list[i].fix.image.indices) {
                fixImageList.add(list[i].fix.image[j])
            }
            for (j in list[i].ok.image.indices) {
                okImageList.add(list[i].ok.image[j])
            }
        }

        return Pair(fixImageList, okImageList)
    }

    // Add images for bedroom table in document
    private fun bedroomImageList(list: List<Bedrooms>): Pair<List<ImageUploadCommon>, List<ImageUploadCommon>> {
        val fixImageList = arrayListOf<ImageUploadCommon>()
        val okImageList = arrayListOf<ImageUploadCommon>()

        for (i in list.indices) {
            for (j in list[i].fix.image.indices) {
                fixImageList.add(list[i].fix.image[j])
            }
            for (j in list[i].ok.image.indices) {
                okImageList.add(list[i].ok.image[j])
            }
        }

        return Pair(fixImageList, okImageList)
    }

    // Add images for bathroom table in document
    private fun bathroomImageList(list: List<Bathrooms>): Pair<List<ImageUploadCommon>, List<ImageUploadCommon>> {
        val fixImageList = arrayListOf<ImageUploadCommon>()
        val okImageList = arrayListOf<ImageUploadCommon>()

        for (i in list.indices) {
            for (j in list[i].fix.image.indices) {
                fixImageList.add(list[i].fix.image[j])
            }
            for (j in list[i].ok.image.indices) {
                okImageList.add(list[i].ok.image[j])
            }
        }

        return Pair(fixImageList, okImageList)
    }

    private fun commonImageTable(image: String, imageTitle: Paragraph): Table {
        val homeImage =
            BitmapFactory.decodeFile(image)

        val columnWidthImage = floatArrayOf(350f)
        val tableImage = Table(columnWidthImage)
        tableImage.marginTop = 2f
        tableImage.marginBottom = 2f
        tableImage.marginLeft = 2f
        tableImage.marginRight = 2f
        tableImage.isKeepTogether = true

        if (homeImage != null) {
            val bb = Bitmap.createBitmap(homeImage)
            val stream = ByteArrayOutputStream()
            bb.compress(Bitmap.CompressFormat.JPEG, 25, stream)
            val byteArray: ByteArray = stream.toByteArray()
            val imgData = ImageDataFactory.create(byteArray)
            val img = Image(imgData)
            img.setHeight(200f)
            img.setWidth(250f)

            tableImage.addCell(
                Cell().add(imageTitle).add(img).setBorder(Border.NO_BORDER)
                    .setTextAlignment(TextAlignment.CENTER)
            )
            return tableImage
        }
        return tableImage
    }

    private fun resizeBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        return Bitmap.createScaledBitmap(
            bitmap,
            convertDptoPixel(width),
            convertDptoPixel(height),
            false
        )
    }

    private fun convertDptoPixel(dp: Int): Int {
        return (dp * 2.75).toInt()
    }

    private fun imageResize(image: Int, width: Int, height: Int): Image {
        val homeImage =
            BitmapFactory.decodeResource(context.resources, image)
        homeImage.scale(1, 1)
        val getBit = resizeBitmap(homeImage, width, height)
        val stream = ByteArrayOutputStream()
        getBit.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray: ByteArray = stream.toByteArray()
        val imgData = ImageDataFactory.create(byteArray)
        return Image(imgData)
    }


    private fun titleAdd(title: String): Paragraph {
        return Paragraph(title).setBold().setTextAlignment(TextAlignment.LEFT)
            .setFontSize(15f)
            .setUnderline().setFont(font)
    }


    private fun titleImagesAdd(title: String): Paragraph {
        val picturesImages =
            Paragraph(title).setBold().setTextAlignment(TextAlignment.LEFT)
                .setFontSize(15f).setFont(font)
        picturesImages.marginTop = 5f
        return picturesImages
    }


}

