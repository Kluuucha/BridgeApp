package com.example.bridgeapp.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.bridgeapp.R

class EditNamesDialog : AppCompatDialogFragment() {
    private var listener: EditNamesDialogListener? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        val builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_edit_names, null)
        val players: Array<EditText> = arrayOf(
                view.findViewById(R.id.editNorth),
                view.findViewById(R.id.editEast),
                view.findViewById(R.id.editSouth),
                view.findViewById(R.id.editWest))
        builder.setView(view)
                .setTitle("Edit names")
                .setMessage("Enter the names for each player and click [SAVE]")
                .setNegativeButton("Cancel") { _: DialogInterface?, _: Int -> }
                .setPositiveButton("Save") { _: DialogInterface?, _: Int ->
                    val names = arrayOf(
                            players[0].text.toString(),
                            players[1].text.toString(),
                            players[2].text.toString(),
                            players[3].text.toString())
                    listener!!.applyNames(names)
                }
        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = try {
            context as EditNamesDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + "must implement EditNamesDialogListener")
        }
    }

    interface EditNamesDialogListener {
        fun applyNames(names: Array<String>?)
    }
}