package com.example.movielopp.Fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.movielopp.MainActivity
import com.example.movielopp.R
import kotlinx.android.synthetic.main.fragment_login.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class LoginFragment : Fragment() {

    private lateinit var loginRegister: OnButtonLoginPressedListener
    private lateinit var registerListener: OnTextRegistredPressedListener


    interface OnButtonLoginPressedListener {
        fun onLoginPressed()
    }

    interface OnTextRegistredPressedListener {
        fun onRegisteredPressed(username:String)

    }

    override fun onResume() {
        super.onResume()

    }

    override fun onStop() {
        super.onStop()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn1.setOnClickListener {
            if (true) {
                loginRegister.onLoginPressed()
            } else {
                username.error = "Wrong Email or Password"
            }
        }

        goToRegister.setOnClickListener {
            registerListener.onRegisteredPressed(username.text.toString())
        }

    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        //loginRegister = activity as OnButtonLoginPressedListener
        registerListener = activity as OnTextRegistredPressedListener
    }


}
