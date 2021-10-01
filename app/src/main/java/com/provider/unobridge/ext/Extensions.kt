package com.provider.unobridge.ext

import com.provider.unobridge.network.core.APIService

fun String.showImage():String{
   return "${APIService.BASE_URL}$this"
}