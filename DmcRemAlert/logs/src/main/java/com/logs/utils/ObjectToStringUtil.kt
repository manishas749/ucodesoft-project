package com.logs.utils

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import java.lang.reflect.InvocationTargetException
import java.util.Arrays


/**
 * Utility for formatting object to string.
 */
class ObjectToStringUtil {

    /**
     * Bundle object to string, the string would be in the format of "Bundle[{...}]".
     * @param bundle
     * @return string
     */
    fun bundleToString(bundle: Bundle): String {
        if (bundle == null) {
            return "null"
        }

        val string: StringBuilder = StringBuilder(128)
        string.append("Bundle[{")
        bundleToBriefString(bundle, string)
        string.append("}]")

        return string.toString()

    }

    /**
     * Bundle object to string, the string would be in the format of "Bundle[{...}]" in brief.
     * @param bundle
     * @param b string builder
     */
    private fun bundleToBriefString(bundle: Bundle, b: StringBuilder) {
        var first = true

        for (key in bundle.keySet()) {
            if (!first) {
                b.append(", ")
            }
            b.append(key).append("=")

            if (bundle.getIntArray(key) is IntArray) {
                b.append(Arrays.toString(bundle.getIntArray(key)))
            } else if (bundle.getByteArray(key) is ByteArray) {
                b.append(Arrays.toString(bundle.getByteArray(key)))
            } else if (bundle.getBooleanArray(key) is BooleanArray) {
                b.append(Arrays.toString(bundle.getBooleanArray(key)))
            } else if (bundle.getShortArray(key) is ShortArray) {
                b.append(Arrays.toString(bundle.getShortArray(key)))
            } else if (bundle.getLongArray(key) is LongArray) {
                b.append(Arrays.toString(bundle.getLongArray(key)))
            } else if (bundle.getFloatArray(key) is FloatArray) {
                b.append(Arrays.toString(bundle.getFloatArray(key)))
            } else if (bundle.getDoubleArray(key) is DoubleArray) {
                b.append(Arrays.toString(bundle.getDoubleArray(key)))
            } else if (bundle.getStringArray(key) is Array<String>) {
                b.append(Arrays.toString(bundle.getStringArray(key)))
            } else if (bundle.getCharSequenceArray(key) is Array<CharSequence>) {
                b.append(Arrays.toString(bundle.getCharSequenceArray(key)))
            } else if (bundle.getParcelableArray(key) is Array<Parcelable>) {
                b.append(Arrays.toString(bundle.getParcelableArray(key)))
            } else if (bundle.getBundle(key) is Bundle) {
                b.append(ObjectToStringUtil().bundleToString(bundle.getBundle(key)!!))
            } else {
                //  b.append(value)
            }
            first = false
        }

        return
    }

    /**
     * Intent object to string, the string would be in the format of "Intent { ... }".
     * @param intent
     * @return string
     */
    fun intentToString(intent: Intent): String {
        if (intent == null) {
            return "null"
        }

        val stringBuilder = StringBuilder(128)
        stringBuilder.append("Intent { ")
        ObjectToStringUtil().intentToShortString(intent, stringBuilder)
        stringBuilder.append(" }")
        return stringBuilder.toString()

    }

    /**
     * Intent object to string, the string would be in the format of "Intent { ... }" in short.
     * @param intent
     * @param b string builder
     */
    private fun intentToShortString(intent: Intent, b: StringBuilder) {

        var first = true
        val mAction = intent.action
        if (mAction != null) {
            b.append("act=").append(mAction)
            first = false
        }

        val mCategories = intent.categories
        if (mCategories != null) {
            if (!first) {
                b.append(' ')
            }
            first = false
            b.append("cat=[")
            var firstCategory = true
            for (c in mCategories) {
                if (!firstCategory) {
                    b.append(',')
                }
                b.append(c)
                firstCategory = false
            }
            b.append("]")
        }

        val mData = intent.data
        if (mData != null) {
            if (!first) {
                b.append(' ')
            }
            first = false
            b.append("dat=")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                b.append(ObjectToStringUtil().uriToSafeString(mData))
            } else {
                val scheme = mData.scheme
                if (scheme != null) {
                    if (scheme.equals("tel", ignoreCase = true)) {
                        b.append("tel:xxx-xxx-xxxx")
                    } else if (scheme.equals("smsto", ignoreCase = true)) {
                        b.append("smsto:xxx-xxx-xxxx")
                    } else {
                        b.append(mData)
                    }
                } else {
                    b.append(mData)
                }
            }
        }


        val mType = intent.type
        if (mType != null) {
            if (!first) {
                b.append(' ')
            }
            first = false
            b.append("typ=").append(mType)
        }
        val mFlags = intent.flags
        if (mFlags != 0) {
            if (!first) {
                b.append(' ')
            }
            first = false
            b.append("flg=0x").append(Integer.toHexString(mFlags))
        }
        val mPackage = intent.getPackage()
        if (mPackage != null) {
            if (!first) {
                b.append(' ')
            }
            first = false
            b.append("pkg=").append(mPackage)
        }
        val mComponent = intent.component
        if (mComponent != null) {
            if (!first) {
                b.append(' ')
            }
            first = false
            b.append("cmp=").append(mComponent.flattenToShortString())
        }
        val mSourceBounds = intent.sourceBounds
        if (mSourceBounds != null) {
            if (!first) {
                b.append(' ')
            }
            first = false
            b.append("bnds=").append(mSourceBounds.toShortString())
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            val mClipData = intent.clipData
            if (mClipData != null) {
                if (!first) {
                    b.append(' ')
                }
                first = false
                b.append("(has clip)")
            }
        }
        val mExtras = intent.extras
        if (mExtras != null) {
            if (!first) {
                b.append(' ')
            }
            b.append("extras={")
            ObjectToStringUtil().bundleToBriefString(mExtras, b)
            b.append('}')
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            val mSelector = intent.selector
            if (mSelector != null) {
                b.append(" sel=")
                ObjectToStringUtil().intentToShortString(mSelector, b)
                b.append("}")
            }
        }

    }

    /**
     * Uri object to string, the string would be in the format of URI.
     * @param uri
     * @return string
     */
    private fun uriToSafeString(uri: Uri): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            try {
                val toSafeString = Uri::class.java.getDeclaredMethod("toSafeString")
                toSafeString.isAccessible = true
                return toSafeString.invoke(uri) as String
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
        return uri.toString()
    }


}