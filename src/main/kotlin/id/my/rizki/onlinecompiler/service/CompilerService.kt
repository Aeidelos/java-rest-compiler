package id.my.rizki.onlinecompiler.service

import id.my.rizki.onlinecompiler.dto.CompilerOutput
import id.my.rizki.onlinecompiler.exception.MainClassNotFoundException
import id.my.rizki.onlinecompiler.utils.MD5
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedWriter
import java.io.File
import java.io.IOException
import java.io.OutputStreamWriter

@Service
class CompilerService {

    fun checkMainClassIsAvailable(mainClass: String?, file: File) {
        if (mainClass == "") {
            file.deleteRecursively()
            throw MainClassNotFoundException()
        }
    }

    fun processBuilderStart(builder: ProcessBuilder) : Process? {
        try {
            return builder.start()
        } catch (e: IOException) {
            println(e)
        }
        return null
    }

    fun doCompile(vararg codes: MultipartFile) : CompilerOutput {
        var location = MD5.generate(codes[0].originalFilename)
        val file = File("code/"+location)
        file.mkdir()
        var mainClass: String? = ""
        for(code in codes) {
            val compileData = File(file, code.originalFilename)
            FileUtils.writeByteArrayToFile(compileData, code.bytes)
            val result = File(compileData.path)
            val lines = FileUtils.readLines(result)
            val updatedLines = lines.filter {s -> !s.contains("package") }
            for (line in lines) if (line.contains("main") ) mainClass = code.originalFilename
            FileUtils.writeLines(result, updatedLines, false)
        }

        checkMainClassIsAvailable(mainClass, file)

        var result: String? = null
        var error_log: String? = null
        val classname = mainClass!!.replace(".java", "")
        val builder = ProcessBuilder("/bin/bash")

        var p: Process? = processBuilderStart(builder)

        val p_stdin = BufferedWriter(OutputStreamWriter(p!!.outputStream))
        try {
            p_stdin.write("cd $file")
            p_stdin.newLine()
            p_stdin.flush()
        } catch (e: IOException) {
            println(e)
        }
        try {
            p_stdin.write("javac *.java")
            p_stdin.newLine()
            p_stdin.flush()
        } catch (e: IOException) {
            println(e)
        }
        try {
            p_stdin.write("java $classname")
            p_stdin.newLine()
            p_stdin.flush()
        } catch (e: IOException) {
            println(e)
        }
        try {
            p_stdin.write("exit")
            p_stdin.newLine()
            p_stdin.flush()
        } catch (e: IOException) {
            println(e)
        }
        try {
            result = IOUtils.toString(p.inputStream, "UTF-8")
            error_log = IOUtils.toString(p.errorStream, "UTF-8")
        } catch (e: IOException) {
            e.printStackTrace()
        }
        file.deleteRecursively()
        return CompilerOutput(result!!, error_log!!)
    }

}