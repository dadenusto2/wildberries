package com.example.week82.fragments

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.week82.R

class AboutFragment : Fragment() {
    lateinit var myFragmentView: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for myView.applicationContext fragment
        myFragmentView = inflater.inflate(R.layout.fragment_about, container, false)

        return myFragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val t2 = myFragmentView.findViewById(R.id.tv_github_link) as TextView
        t2.movementMethod = LinkMovementMethod.getInstance()
    }
}