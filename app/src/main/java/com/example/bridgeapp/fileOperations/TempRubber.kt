package com.example.bridgeapp.fileOperations

import com.example.bridgeapp.structure.RubberObject
import java.io.*

object TempRubber {

    private const val saveFile = "rubber_save.data"

    lateinit var resumeClass: Class<*>
    lateinit var rubber: RubberObject

    fun loadRubber(file: File) {
        val input: ObjectInput
        try {
            val outFile = File(file,saveFile)
            input = ObjectInputStream(FileInputStream(outFile))
            resumeClass = input.readObject() as Class<*>
            rubber = input.readObject() as RubberObject
            input.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun saveRubber(file: File, savedRubber: RubberObject, activity: Class<*>) {
        val out: ObjectOutput
        try {
            val outFile = File(file,saveFile)
            out = ObjectOutputStream(FileOutputStream(outFile))
            out.writeObject(activity)
            out.writeObject(savedRubber)
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun saveExists(file: File): Boolean {
        return File(file,saveFile).exists()
    }

    fun deleteSave(file: File){
        File(file,saveFile).delete()
    }
}