package com.example.week83

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

/**
 * Фрагмент "о приложении"
 */
class AboutFragment : Fragment() {
    lateinit var myFragmentView: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        myFragmentView = inflater.inflate(R.layout.fragment_about, container, false)

        return myFragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //ссылка на гитхаб
        val tvGithubLink = myFragmentView.findViewById(R.id.tv_github_link) as TextView
        tvGithubLink.movementMethod = LinkMovementMethod.getInstance()
    }
}