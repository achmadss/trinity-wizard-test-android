package com.achmadss.data.util

import android.content.res.AssetManager

fun AssetManager.readAssetsFile(fileName: String) =
    open(fileName).bufferedReader().use { it.readText() }