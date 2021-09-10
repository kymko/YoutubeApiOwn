package com.example.youtubeapi.core.network

class Resource<T>(val status: Status, val data: T?, val message: String?, val code: Int?) {

    companion object {
        fun <T> success(data: T?, code: Int): Resource<T> {
            return Resource(Status.SUCCESS, data, null, code)
        }

        fun <T> error(msg: String?, data: T?, code: Int): Resource<T> {
            return Resource(Status.ERROR, data, msg, code)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data,null,null)
        }
    }
}