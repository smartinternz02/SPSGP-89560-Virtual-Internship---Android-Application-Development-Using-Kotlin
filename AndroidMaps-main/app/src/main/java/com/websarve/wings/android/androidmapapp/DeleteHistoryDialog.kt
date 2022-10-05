package com.websarve.wings.android.androidmapapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.DialogFragment

/**
 * DeleteHistoryDialog.
 *
 *
 *
 */
class DeleteHistoryDialog : DialogFragment() {

    /**
     * onCreateDialog
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.i("DeleteHistoryDialog onCreateDialog", "start!!")
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.dialog_delete_title)
        builder.setMessage(R.string.dialog_delete_msg)
        builder.setPositiveButton(R.string.dialog_btn_erase, DialogButtonClickListener())
        builder.setNegativeButton(R.string.dialog_btn_cancel, DialogButtonClickListener())
        val dialog = builder.create()
        Log.i("DeleteHistoryDialog onCreateDialog", "end!!")
        return dialog
    }

    /**
     * DialogButtonClickListener
     */
    private inner class DialogButtonClickListener : DialogInterface.OnClickListener{
        /**
         * onClick
         */
        override fun onClick(dialog: DialogInterface, which: Int){
            Log.i("DeleteHistoryDialog onClick", "start!!")

            var msg = ""
            when(which){
                DialogInterface.BUTTON_POSITIVE ->{
                    Log.i("DeleteHistoryDialog onClick", "selected BUTTON_POSITIVE")
                    msg = getString(R.string.dialog_msg_erase)

                    // mainActivity  clearMap
                }
                DialogInterface.BUTTON_NEGATIVE ->{
                    Log.i("DeleteHistoryDialog onClick", "selected BUTTON_NEGATIVE")
                    msg = getString(R.string.dialog_msg_cancel)
                }
            }
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
            Log.i("DeleteHistoryDialog onClick", "end!!")
        }
    }
}
