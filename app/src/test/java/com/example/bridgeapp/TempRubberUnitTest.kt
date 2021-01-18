package com.example.bridgeapp

import com.example.bridgeapp.activity.MainActivity
import com.example.bridgeapp.fileOperations.TempRubber
import com.example.bridgeapp.structure.RubberObject
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import java.io.File

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TempRubberUnitTest {
    @Before


    @Test
    fun saveExists_isCorrect() {
        val fileContains = File("C:\\Users\\Sebastian\\AndroidStudioProjects\\BridgeApp\\app\\testFiles\\contains")
        val fileEmpty = File("C:\\Users\\Sebastian\\AndroidStudioProjects\\BridgeApp\\app\\testFiles\\empty")
        assertEquals(true, TempRubber.saveExists(fileContains))
        assertEquals(false, TempRubber.saveExists(fileEmpty))
    }

    @Test
    fun addFile_isCorrect() {
        val fileSave = File("C:\\Users\\Sebastian\\AndroidStudioProjects\\BridgeApp\\app\\testFiles\\saveTest")

        assertEquals(false, TempRubber.saveExists(fileSave))
        TempRubber.saveRubber(fileSave, RubberObject(), MainActivity::class.java)
        assertEquals(true, TempRubber.saveExists(fileSave))
    }
}