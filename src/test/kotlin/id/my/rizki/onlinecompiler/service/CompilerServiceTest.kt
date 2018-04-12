package id.my.rizki.onlinecompiler.service

import com.sun.org.apache.xpath.internal.operations.Mult
import id.my.rizki.onlinecompiler.dto.CompilerOutput
import id.my.rizki.onlinecompiler.exception.MainClassNotFoundException
import junit.framework.Assert.assertEquals
import org.apache.commons.io.IOUtils
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Rule
import org.mockito.InjectMocks
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileInputStream

@Rule var rule: MockitoRule = MockitoJUnit.rule()

class CompilerServiceTest(
) {

    lateinit var compileService: CompilerService

    lateinit var files: MultipartFile
    var file = File("code/test/code.java")

    lateinit var files2: MultipartFile
    var file2 = File("code/test/CodeWithoutMainClass.java")

    lateinit var files3: MultipartFile
    var file3 = File("code/test/CodeWithoutPackage.java")

    lateinit var files4: MultipartFile
    var file4 = File("code/test/User.java")

    lateinit var files5: MultipartFile
    var file5 = File("code/test/UserMain.java")

    @Before
    fun setUp() {
        var inputStream = FileInputStream(file)
        files = MockMultipartFile("code.java","code.java","application/java",
                inputStream)

        var inputStream2 = FileInputStream(file2)
        files2 = MockMultipartFile("CodeWithoutMainClass.java","CodeWithoutMainClass.java","application/java"
                , inputStream2)

        var inputStream3 = FileInputStream(file3)
        files3 = MockMultipartFile("CodeWithoutPackage.java","CodeWithoutPackage.java","application/java",
                inputStream3)

        var inputStream4 = FileInputStream(file4)
        files3 = MockMultipartFile("User.java","User.java","application/java",
                inputStream4)

        var inputStream5 = FileInputStream(file5)
        files3 = MockMultipartFile("UserMain.java","UserMain.java","application/java",
                inputStream4)
        compileService = CompilerService()
    }

    @Test
    fun unitCompile() {
        var expectedOutput = CompilerOutput("java","")
        var actualOutput:CompilerOutput = compileService.doCompile(files)
        assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun unitCompile_withoutPackage() {
        var expectedOutput = CompilerOutput("java","")
        var actualOutput:CompilerOutput = compileService.doCompile(files3)
        assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun multipleFileCompile() {
        var actualOutput:CompilerOutput = compileService.doCompile(files4, files5)
        assertEquals(actualOutput, Mockito.any(CompilerOutput::class.java))
    }

    @Test(expected=MainClassNotFoundException::class)
    fun unitCompile_expectedMainClassNotFoundException() {
        compileService.doCompile(files2)
    }

}