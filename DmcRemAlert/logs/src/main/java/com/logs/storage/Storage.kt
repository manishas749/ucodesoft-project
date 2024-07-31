package com.logs.storage

/**
 * This Storage interface used for get internal memory info and formatSize.
 */
interface Storage {

    /**
     * This function will return internal memory info.
     * @return long
     */
    fun getInternalMemoryInfo(): Long

    /**
     * This function will return format size of memory.
     * @param size size of memory in long.
     * @return long
     */
    fun formatSize(size: Long): Long
}