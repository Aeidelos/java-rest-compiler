package id.my.rizki.onlinecompiler.controller

import id.my.rizki.onlinecompiler.dto.CompilerOutput
import id.my.rizki.onlinecompiler.exception.FormatNotSupportedException
import id.my.rizki.onlinecompiler.exception.MainClassNotFoundException
import id.my.rizki.onlinecompiler.service.CompilerService
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileInputStream

@Rule
var rule = MockitoJUnit.rule()

class CompilerControllerTest {

    @InjectMocks
    var compilerService: CompilerService = Mockito.mock(CompilerService::class.java)

    @Mock
    lateinit var compilerController: CompilerController

    lateinit var files: MultipartFile
    var file = File("code/test/code.java")

    lateinit var files2: MultipartFile
    var file2 = File("code/test/code.docx")

    lateinit var files3: MultipartFile
    var file3 = File("code/test/CodeWithoutMainClass.java")


    @Before
    fun setUp() {
        var inputStream = FileInputStream(file)
        files = MockMultipartFile("code.java","code.java","application/java", inputStream)

        var inputStream2= FileInputStream(file2)
        files2 = MockMultipartFile("code.docx", "code.docx","application/java",inputStream2)

        var inputStream3 = FileInputStream(file3)
        files3 = MockMultipartFile("CodeWithoutMainClass.java","CodeWithoutMainClass.java","application/java", inputStream3)


        compilerController = CompilerController(compilerService)
    }


    @Test
    fun unitCompile() {
        Mockito.`when`(compilerService.doCompile(files)).thenReturn(CompilerOutput("Hello",""));
        assertEquals(CompilerOutput("Hello",""),compilerController.compile(files));
        Mockito.verify(compilerService).doCompile(files)
        Mockito.verifyNoMoreInteractions(compilerService)

    }

    @Test(expected = FormatNotSupportedException::class)
    fun unitCompile_expectedThrowFileFormatException() {
        assertEquals(compilerController.compile(files2), throw FormatNotSupportedException())
        Mockito.verify(compilerService).doCompile(files2)
        Mockito.verifyNoMoreInteractions(compilerService)
    }


}

class CompilerControllerIntegrationTest{

    lateinit var compilerControllerInt:CompilerController

    lateinit var files: MultipartFile
    var file = File("code/test/code.java")

    lateinit var files2: MultipartFile
    var file2 = File("code/test/code.docx")

    lateinit var files3: MultipartFile
    var file3 = File("code/test/CodeWithoutMainClass.java")


    @Before
    fun setUp() {
        var inputStream = FileInputStream(file)
        files = MockMultipartFile("code.java","code.java","application/java", inputStream)

        var inputStream2= FileInputStream(file2)
        files2 = MockMultipartFile("code.docx", "code.docx","application/java",inputStream2)

        var inputStream3 = FileInputStream(file3)
        files3 = MockMultipartFile("CodeWithoutMainClass.java","CodeWithoutMainClass.java","application/java", inputStream3)

        compilerControllerInt = CompilerController(CompilerService())
    }


    @Test(expected = MainClassNotFoundException::class)
    fun integration_compile_expectedMainClassNotFoundException() {
        assertEquals(compilerControllerInt.compile(files3), throw MainClassNotFoundException())
    }

    @Test(expected = FormatNotSupportedException::class)
    fun integration_compile_expectedThrowFileFormatException() {
        val actualOutput = compilerControllerInt.compile(files2)
        assertEquals( actualOutput, throw FormatNotSupportedException())
    }

    @Test
    fun integration_compile() {
        val expectedOutput = CompilerOutput("java", "")
        val actualOutput = compilerControllerInt.compile(files)
        assertEquals(expectedOutput, actualOutput)
    }
}