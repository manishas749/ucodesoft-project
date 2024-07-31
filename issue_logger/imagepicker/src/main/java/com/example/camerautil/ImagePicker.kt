package com.example.camerautil

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.annotation.IntRange
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.view.PreviewView
import androidx.fragment.app.Fragment
import com.example.camerautil.ImagePickerActivity.Companion.cropBitmap
import com.example.camerautil.camera.CameraProvider
import com.example.camerautil.compression.CompressionProvider.Companion.compressBitmap
import com.example.camerautil.compression.CompressionProvider.Companion.resizeImage
import com.example.camerautil.file.FileProvider
import com.example.camerautil.gallery.GalleryProvider
import com.example.camerautil.launcher.CameraLauncher
import com.example.camerautil.launcher.GalleryLauncher
import com.example.camerautil.listener.ChooserClickListener
import com.example.camerautil.provider.ImageProvider
import com.example.camerautil.provider.RequestCodeProvider
import com.example.camerautil.util.BitmapUtil.Companion.getBitmapFromUri
import com.example.camerautil.util.ChooserDialog
import java.io.File

/***
 *
 * ImagePicker
 *
 ***/

class ImagePicker {

    companion object {
        var isCropEnabled = false
        var isCompEnabled = false
        var isReSizeEnabled = false

        const val DEFAULT_COMPRESSION_QUALITY = 0
        var compressQuality: Int? = null

        fun with(activity: AppCompatActivity): Builder = Builder(activity = activity)

        fun with(fragment: Fragment): Builder = Builder(fragment = fragment)
    }

    class Builder(
        private var activity: AppCompatActivity? = null, fragment: Fragment? = null
    ) {

        private var imageProvider = ImageProvider.BOTH      //default image provider

        private var intent = Intent()       //empty intent

        private val cameraProvider = CameraProvider()       //CameraProvider class object

        private val fileProvider = FileProvider()       //FileProvider class object

        private val galleryProvider = GalleryProvider()     //GalleryProvider class object

        private var cameraLauncher: CameraLauncher      //CameraLauncher class object

        private var galleryLauncher: GalleryLauncher       //GalleryLauncher class object

        private var chooserDialog: ChooserDialog

        init {
            cameraLauncher = if (activity != null) {
                CameraLauncher(activity = activity)
            } else if (fragment != null) {
                CameraLauncher(fragment = fragment)
            } else {
                throw NullPointerException()
            }
            galleryLauncher = if (activity != null) {
                GalleryLauncher(activity = activity)
            } else if (fragment != null) {
                GalleryLauncher(fragment = fragment)
            } else {
                throw NullPointerException()
            }

            activity = fragment?.requireActivity() as AppCompatActivity
            chooserDialog = ChooserDialog(
                activity!!, cameraProvider, galleryProvider, cameraLauncher, galleryLauncher
            )
        }

        fun build(): ImagePicker = ImagePicker()        // return object of ImagePicker class

        fun provider(imageProvider: ImageProvider = ImageProvider.BOTH) =
            apply {       //set image provider according to user
                this.imageProvider = imageProvider
            }

        fun crop(enable: Boolean) = apply {       // for enabling Crop feature for default launchers
            isCropEnabled = enable
        }

        fun crop(bitmap: Bitmap?) {     // for cropping given bitmap
            cropBitmap = bitmap

            val intent = Intent(activity, ImagePickerActivity::class.java)
            intent.putExtra("imageBitmap", "imageBitmap")
            activity!!.startActivity(intent)
        }

        fun crop(imageUri: String?) {        //for cropping image from Uri
            val intent = Intent(activity, ImagePickerActivity::class.java)
            intent.putExtra("imageUri", imageUri)
            activity!!.startActivity(intent)
        }

        fun getBitmap(): Bitmap? = cropBitmap

        fun compress(enable: Boolean) = apply {       //enable image compression
            isCompEnabled = enable
        }

        fun compress(           //enable image compression upto the given quality
            enable: Boolean, @IntRange(
                from = 0, to = 100
            ) quality: Int = DEFAULT_COMPRESSION_QUALITY
        ) = apply {
            isCompEnabled = enable
            compressQuality = quality
        }

        fun compress(           //compress given bitmap upto the given quality and return bitmap
            bitmap: Bitmap?, @IntRange(from = 0, to = 100) quality: Int
        ): Bitmap? {
            return compressBitmap(bitmap, quality)
        }

        fun compress(       //compress bitmap from given Uri upto the given quality and return bitmap
            imageUri: Uri?, @IntRange(from = 0, to = 100) quality: Int
        ): Bitmap? {
            val bitmap = getBitmapFromUri(activity!!, imageUri)
            return compressBitmap(bitmap, quality)
        }

        fun reSize(enable: Boolean) = apply {
            isReSizeEnabled = enable
        }

        fun reSize(bitmap: Bitmap?): Bitmap? {      //resize the bitmap
            return resizeImage(bitmap!!)
        }

        fun reSize(imageUri: Uri?): Bitmap? {       //resize the bitmap from Uri
            val bitmap = getBitmapFromUri(activity!!, imageUri)
            return reSize(bitmap)
        }

        fun cameraOnly() = apply {     //set default image provider to CAMERA
            this.imageProvider = ImageProvider.CAMERA
            this.intent = getCameraIntent()
        }

        fun galleryOnly() = apply {        //set default image provider to GALLERY
            this.imageProvider = ImageProvider.GALLERY
            this.intent = getGalleryIntent()
        }

        /****************************************************************************************
         *                                                                                      *
         * Start method                                                                         *
         *  * where when                                                                        *
         *     * image provider is BOTH then it will show Chooser to the user                   *
         *        * if user provider custom layout then users layout will be shown              *
         *        * else default library dialog layout will be shown                            *
         *     * image provider is GALLERY then it will set gallery intent to intent            *
         *       then, call listener onClick method                                             *
         *     * image provider is CAMERA then it will set camera intent to intent              *
         *       then, call listener onClick method                                             *
         *                                                                                      *
         ****************************************************************************************/

        fun start(
            listener: ChooserClickListener,
            customLayout: Int = R.layout.camera_gallery_chooser_dialog
        ) = apply {       //start function for chooser
            Log.d("SHOW_DIALOG", "start: out $imageProvider")
            when (imageProvider) {
                ImageProvider.BOTH -> {
                    Log.d("SHOW_DIALOG", "start: t")
                    showChooserDialog(
                        listener, customLayout
                    )     //show chooser from where to get image
                }
                ImageProvider.GALLERY -> {
                    intent = getGalleryIntent()     //set gallery intent to intent
                    listener.onClick(RequestCodeProvider.GALLERY_REQ_CODE, intent)
                }
                else -> {
                    intent = getCameraIntent()      //set camera intent to intent
                    listener.onClick(RequestCodeProvider.CAMERA_REQ_CODE, intent)
                }
            }
        }

        /****************************************************************************************
         *                                                                                      *
         * start method                                                                         *
         * * where when                                                                         *
         *     * image provider is BOTH then it will show default Chooser to the user           *
         *        * if user click camera then the default camera launcher will launch           *
         *        * else if user click gallery then the default gallery launcher will launch    *
         *        * else if user click cancel it will dismiss the chooser                       *
         *     * image provider is GALLERY then it will launch gallery launcher                 *
         *     * image provider is CAMERA then it will launch camera launcher                   *
         *                                                                                      *
         ****************************************************************************************/

        fun start() = apply {
            when (imageProvider) {
                ImageProvider.BOTH -> {
                    showChooserDialog()     //show chooser from where to get image
                }
                ImageProvider.GALLERY -> {
                    intent = getGalleryIntent()     //set gallery intent to intent
                    galleryLauncher.galleryLauncher.launch(intent)      //launch default camera launcher with camera intent
                }
                else -> {
                    intent = getCameraIntent()      //set camera intent to intent
                    cameraLauncher.cameraLauncher.launch(intent)        //launch default gallery launcher with gallery intent
                }
            }
        }

        //get image as Bitmap from default camera launcher
        fun getCameraLauncherBitmap(): Bitmap? = cameraLauncher.getBitmap()

        //get image as URI from default camera launcher
        fun getCameraLauncherUri(): Uri? = cameraLauncher.getUri()

        //get image as URI from default gallery launcher
        fun getGalleryLauncherUri(): Uri? = galleryLauncher.getUri()

        //get images as arraylist of URIs from default gallery launcher
        fun getGalleryLauncherUris(): ArrayList<Uri?> = galleryLauncher.getUris()

        //get image as Bitmap from default gallery launcher
        fun getGalleryLauncherBitmap(): Bitmap? = galleryLauncher.getBitmap()

        //get images as arraylist of Bitmap from default gallery launcher
        fun getGalleryLauncherBitmaps(): ArrayList<Bitmap?> = galleryLauncher.getBitmaps()

        fun getIntent(): Intent = this.intent       //return intent to launch

        fun createIntent(onResult: (RequestCodeProvider?, Intent) -> Unit) = apply {
            if (imageProvider == ImageProvider.BOTH) {
                chooserDialog.chooserDialogWithListener(activity!!, object : ChooserClickListener {
                    override fun onClick(reqCode: RequestCodeProvider, intent: Intent) {
                        onResult(reqCode, intent)
                    }

                    override fun customDialogLayout(dialog: Dialog) {}
                })
            } else {
                val reqCode = when (imageProvider) {
                    ImageProvider.GALLERY -> {
                        RequestCodeProvider.GALLERY_REQ_CODE
                    }
                    ImageProvider.CAMERA -> {
                        RequestCodeProvider.CAMERA_REQ_CODE
                    }
                    else -> {
                        null
                    }
                }
                onResult(reqCode, getIntent())
            }
        }

        /****************************************************************************/
        /**                                                                        **/
        /**       start camera with activity, camera PreviewView, cameraFlip       **/
        /**    where if cameraFlip is true back camera is open else front camera   **/
        /**                                                                        **/
        /****************************************************************************/
        fun startCamera(cameraPreview: PreviewView, cameraFlip: Boolean) =
            cameraProvider.startCamera(activity!!, cameraPreview, cameraFlip)

        /********************************************/
        /**                                        **/
        /**   take photo from camera PreviewView   **/
        /** and save it to the specified directory **/
        /**                                        **/
        /********************************************/
        fun takePhoto(
            fileProvider: FileProvider, outputDir: File, FILE_NAME_FORMAT: String
        ) = cameraProvider.takePhoto(
            activity!!, fileProvider, outputDir, FILE_NAME_FORMAT
        )

        //get empty directory where you want to store images, etc
        fun getEmptyDirectory(path: String) = fileProvider.getDir(path)

        //get all images from specific file/directory
        fun getImageFromDirectory(file: File) = fileProvider.getImagesFromFile(file)

        //get all images from gallery
        fun getGalleryImages(): ArrayList<String> = fileProvider.fetchImagesFromGallery(activity!!)

        //return camera intent
        private fun getCameraIntent(): Intent = cameraProvider.cameraIntent(activity!!)

        //get gallery intent
        private fun getGalleryIntent(): Intent = getMultipleImageGalleryIntent()

        //get single image gallery intent
        private fun getSingleImageGalleryIntent(): Intent =
            galleryProvider.singleImageGalleryIntent()

        //get multiple selection of image from gallery intent
        private fun getMultipleImageGalleryIntent(): Intent =
            galleryProvider.multipleImageGalleryIntent()

        //chooser dialog with listener and custom layout
        private fun showChooserDialog(listener: ChooserClickListener, customLayout: Int) {
            chooserDialog.chooserDialogWithListener(activity!!, listener, customLayout)
        }

        //default chooser dialog
        private fun showChooserDialog() {
            chooserDialog.chooserDialog(activity!!, false)
        }
    }
}