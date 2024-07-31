package com.example.camerautil

import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import com.example.camerautil.ImagePicker.Companion.compressQuality
import com.example.camerautil.ImagePicker.Companion.isCompEnabled
import com.example.camerautil.ImagePicker.Companion.isCropEnabled
import com.example.camerautil.ImagePicker.Companion.isReSizeEnabled
import com.example.camerautil.crop.CropProvider
import com.example.camerautil.databinding.ActivityImagePickerBinding
import com.example.camerautil.launcher.CameraLauncher.Companion.camBitmap
import com.example.camerautil.launcher.GalleryLauncher.Companion.galBitmap
import com.example.camerautil.launcher.GalleryLauncher.Companion.galImageUri
import com.example.camerautil.provider.ImageProvider
import com.example.camerautil.util.BitmapUtil.Companion.createFlippedBitmap
import com.example.camerautil.util.BitmapUtil.Companion.getBitmapFromUri
import com.example.camerautil.util.BitmapUtil.Companion.rotateBitmap
import com.example.camerautil.util.Helper.Companion.applyCompressOrResize
import kotlin.math.min


class ImagePickerActivity : AppCompatActivity(), MenuProvider {

    private lateinit var binding: ActivityImagePickerBinding
    private lateinit var cropIv: CropProvider.CropImageView

    private var mScaleWidth: Int = 250
    private var mScaleHeight: Int = 1000
    private var imageProvider: String? = null
    private var originalBitmap: Bitmap? = null
    private var tempBitmap: Bitmap? = null
    private var bitmapToCrop: Bitmap? = null
    private var lWidth: Int = 0
    private var lHeight: Int = 0
    private var flag: Boolean = false

    companion object {
        private const val PADDING_AROUND = 300
        private const val ADDITIONAL_AROUND = 300

        var cropBitmap: Bitmap? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagePickerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.inflateMenu(R.menu.crop_activity_actionbar_menu)
        binding.toolbar.setNavigationIcon(R.drawable.round_arrow_back_24)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.show()

        val menuHost: MenuHost = this
        menuHost.addMenuProvider(this, this, Lifecycle.State.CREATED)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        flag = false

    }

    override fun onResume() {
        super.onResume()
        cropIv = findViewById(R.id.cropImageView)

        imageProvider = intent.extras?.getString("imageProvider")
        val imageBitmap = intent.extras?.getString("imageBitmap")
        val imageUri = intent.extras?.getString("imageUri")

        if (imageBitmap != "null" && imageBitmap != null) {
            bitmapToCrop = cropBitmap
        } else if (imageUri != "null" && imageUri != null) {
            bitmapToCrop = getBitmapFromUri(this, imageUri.toUri())
        }

        originalBitmap = if (galImageUri != null && imageProvider == ImageProvider.GALLERY.name) {
            getBitmapFromUri(this, galImageUri)
        } else if (camBitmap != null && imageProvider == ImageProvider.CAMERA.name) {
            camBitmap
        } else {
            bitmapToCrop
        }

        binding.cropViewBg.viewTreeObserver.addOnGlobalLayoutListener {

            if (flag) {
                lWidth = binding.cropViewBg.width - PADDING_AROUND - ADDITIONAL_AROUND
                lHeight = binding.cropViewBg.height - PADDING_AROUND - ADDITIONAL_AROUND
            } else {
                lWidth = binding.cropViewBg.width - PADDING_AROUND
                lHeight = binding.cropViewBg.height - PADDING_AROUND
            }

            tempBitmap = originalBitmap

            if (tempBitmap != null) {

                if (tempBitmap!!.width < lWidth && tempBitmap!!.height < lHeight) {
                    mScaleWidth = tempBitmap!!.width
                    mScaleHeight = tempBitmap!!.height
                } else if (tempBitmap!!.width > lWidth || tempBitmap!!.height > lHeight) {
                    tempBitmap = getImage(lWidth, lHeight, tempBitmap!!)
                    mScaleWidth = tempBitmap!!.width
                    mScaleHeight = tempBitmap!!.height
                } else {
                    doneAllTask(tempBitmap)
                    finish()
                }

                cropIv.visibility = View.VISIBLE
                cropIv.setImageBitmap(
                    Bitmap.createScaledBitmap(
                        tempBitmap!!, mScaleWidth, mScaleHeight, false
                    )
                )
            } else {
                Toast.makeText(this, "No item to show", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun getImage(lWidth: Int, lHeight: Int, bitmap: Bitmap): Bitmap? {
        val scale: Float = min(lHeight.toFloat() / bitmap.width, lWidth.toFloat() / bitmap.height)

        val matrix = Matrix()
        matrix.postScale(scale, scale)

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    override fun onDestroy() {
        super.onDestroy()
        isCropEnabled = false
        isCompEnabled = false
        isReSizeEnabled = false
        compressQuality = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.crop_activity_actionbar_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.rotateLeft -> {
                originalBitmap = rotateBitmap(originalBitmap!!, 0)
                cropIv.visibility = View.GONE
                cropIv.setImageBitmap(originalBitmap)
                flag = !flag
                true
            }
            R.id.rotateRight -> {
                originalBitmap = rotateBitmap(originalBitmap!!, 1)
                cropIv.visibility = View.GONE
                cropIv.setImageBitmap(originalBitmap)
                flag = !flag
                true
            }
            R.id.flipX -> {
                originalBitmap = createFlippedBitmap(originalBitmap!!, xFlip = true, yFlip = false)
                cropIv.visibility = View.GONE
                cropIv.setImageBitmap(originalBitmap)
                true
            }
            R.id.flipY -> {
                originalBitmap = createFlippedBitmap(originalBitmap!!, xFlip = false, yFlip = true)
                cropIv.visibility = View.GONE
                cropIv.setImageBitmap(originalBitmap)
                true
            }
            R.id.done -> {
                try {

                    doneAllTask(cropIv.croppedImage)

                } catch (e: Exception) {
                    e.stackTrace
                }

                finish()
                true
            }
            else -> false
        }
    }

    private fun doneAllTask(croppedImage: Bitmap?) {
        var cropBm: Bitmap? = croppedImage

        cropBm = applyCompressOrResize(cropBm)

        when (imageProvider) {
            ImageProvider.GALLERY.name -> {
                galBitmap = cropBm
            }
            ImageProvider.CAMERA.name -> {
                camBitmap = cropBm
            }
            else -> {
                cropBitmap = cropBm
            }
        }
    }
}