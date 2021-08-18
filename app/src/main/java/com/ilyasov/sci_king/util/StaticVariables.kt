package com.ilyasov.sci_king.util

import androidx.lifecycle.MutableLiveData
import com.downloader.Progress

object StaticVariables {
    var downloadCount = MutableLiveData(0)
    var downloadingItems = MutableLiveData<MutableMap<String, Progress>>()
}