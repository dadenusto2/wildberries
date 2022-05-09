package com.example.week25

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

// Второй фрагмент, вызаваемый из первого и вызывающий Alert Dialog
// со всеми этапами ЖЦ
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SecondFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onAttach(context: Context) {
        Log.i(this.toString(), "onAttach")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        Log.i(this.toString(), "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(this.toString(), "onCreateView")
        val view = inflater.inflate(R.layout.fragment_second, container, false)

        // нажатие на кнопку для вызова Alert Dialog
        val btn = view.findViewById<Button>(R.id.button)
        btn.setOnClickListener {
            val myDialogFragment = DialogFragment()
            val manager: FragmentManager = parentFragmentManager
            val transaction: FragmentTransaction = manager.beginTransaction()
            myDialogFragment.show(transaction, "dialog") }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(this.toString(), "onViewCreated")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.i(this.toString(), "onViewStateRestored")
    }

    override fun onStart() {
        super.onStart()
        Log.i(this.toString(), "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i(this.toString(), "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i(this.toString(), "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i(this.toString(), "onStop")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(this.toString(), "onSaveInstanceState")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(this.toString(), "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(this.toString(), "onDestroy")
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SecondFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SecondFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}