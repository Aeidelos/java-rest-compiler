package id.my.rizki.onlinecompiler.controller

import id.my.rizki.onlinecompiler.dto.RestErrorInfo
import id.my.rizki.onlinecompiler.exception.FormatNotSupportedException
import id.my.rizki.onlinecompiler.exception.MainClassNotFoundException
import org.apache.commons.io.FilenameUtils
import org.slf4j.LoggerFactory
import javax.servlet.http.HttpServletResponse
import org.springframework.web.context.request.WebRequest
import java.util.zip.DataFormatException
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
open class RequestHandler {

    companion object {
        var log = LoggerFactory.getLogger(this::class.java)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FormatNotSupportedException::class)
    @ResponseBody
    open fun <T> handleDataFormatException(ex: FormatNotSupportedException, request: WebRequest, response: HttpServletResponse) : RestErrorInfo<T> {
        log.info("Error logs : " + ex.message)
        return RestErrorInfo(code = HttpStatus.BAD_REQUEST.value(), status = HttpStatus.BAD_REQUEST.reasonPhrase,
                errors = ex.localizedMessage)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MainClassNotFoundException::class)
    @ResponseBody
    open fun <T> handleMainClassNotFoundException(ex: MainClassNotFoundException, request: WebRequest, response: HttpServletResponse) : RestErrorInfo<T> {
        log.info("Error logs : " + ex.message)
        return RestErrorInfo(code = HttpStatus.BAD_REQUEST.value(), status = HttpStatus.BAD_REQUEST.reasonPhrase,
                errors = ex.localizedMessage)
    }

    open fun checkFileFormatReceived(filename: String){
        val ext  = FilenameUtils.getExtension(filename)
        if (!ext.equals("java", true)) throw FormatNotSupportedException()
    }
}