package id.my.rizki.onlinecompiler.dto

class RestErrorInfo<T> (
        var code: Int = 200,
        var status: String = "OK",
        var data: T? = null,
        var errors: String? = null) {
}