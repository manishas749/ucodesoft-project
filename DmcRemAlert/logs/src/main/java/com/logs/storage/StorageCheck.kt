package com.logs.storage

import android.os.Environment
import android.os.StatFs
import java.io.File

/**
 * This class used for check available size of internal memory
 */
class StorageCheck: Storage {

    /**
     * Get available size of internal memory.
     */
    override fun getInternalMemoryInfo(): Long {
        val path: File = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val availableBlocks = stat.availableBlocksLong
        val totalBlocks = stat.blockCountLong
        val availableSpace = formatSize(availableBlocks * blockSize)
        val totalSpace = formatSize(totalBlocks * blockSize)

        return formatSize(stat.availableBytes)
    }

    /**
     * Return format size of memory.
     */
    override fun formatSize(size: Long): Long {
        var totalSize = size

        totalSize /= 1048576

        return totalSize
    }
}