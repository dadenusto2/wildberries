package com.example.week25

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

// Класс для вызова AlertDialog через DialogFragment со всеми этапами ЖЦ
class DialogFragment : DialogFragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i(this.toString(), "OnAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(this.toString(), "OnCreate")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            Log.i(this.toString(), "OnCreateDialog")
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Alert Dialog")
                .setMessage("AlertDialog через DialogFragment")
                .setPositiveButton("Закрыть положительный") {
                        dialog, id ->  dialog.cancel()
                }
                .setNegativeButton("Закрыть отрицательный") {
                        dialog, id ->  dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(this.toString(), "OnCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        Log.i(this.toString(), "OnStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i(this.toString(), "OnResume")
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        Log.i(this.toString(), "OnCancel")
    }

    override fun onPause() {
        super.onPause()
        Log.i(this.toString(), "OnPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i(this.toString(), "OnStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(this.toString(), "OnDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(this.toString(), "OnDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.i(this.toString(), "OnDetach")
    }
}