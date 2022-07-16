package org.appcenter.inudorm.util

import android.os.Bundle
import android.util.Log

fun printBundle(bundle:Bundle) {
    val bundleKeySet = bundle.keySet()
    Log.d("BundlePrint", "${bundleKeySet.size} items")
    for (key in bundleKeySet) {
        Log.d("BundlePrint", "$key : ${bundle[key]}")
    }
}