package com.example.movielopp.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.movielopp.R
import com.google.firebase.auth.FirebaseAuth
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

    private lateinit var loginSuccesfull: OnButtonLoginPressedListener
    private lateinit var registerListener: OnTextRegistredPressedListener
    private lateinit var auth:FirebaseAuth

    interface OnButtonLoginPressedListener {
        fun onLoginPressed()
    }

    interface OnTextRegistredPressedListener {
        fun onRegisteredPressed(username:String)

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

        val signInButton = activity?.findViewById<TextView>(R.id.signIn)
        auth = FirebaseAuth.getInstance()
        signInButton?.setOnClickListener {
            if (thereIsConnexion()) {
                loginUser()
            } else {
                Toast.makeText(activity?.applicationContext, getString(R.string.no_internet_conexion), Toast.LENGTH_SHORT).show()
            }
        }

        goToRegister.setOnClickListener {
            registerListener.onRegisteredPressed(email.text.toString())
        }

    }

    private fun thereIsConnexion():Boolean {
        val cm = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return cm!!.activeNetworkInfo != null
    }

    private fun loginUser() {
        val mail = email.text.toString()
        val passwd = password.text.toString()
        if (!mail.isEmpty() && !passwd.isEmpty()) {
            disableUIcomponents()
            FirebaseAuth.getInstance().signInWithEmailAndPassword(mail, passwd).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    enableUIcomponents()
                    loginSuccesfull.onLoginPressed()

                } else {
                    enableUIcomponents()
                    email.error = getString(R.string.wrong_mail_pwd)
                }
            }
        }else {
            enableUIcomponents()
            email.error = getString(R.string.mail__pwd_empty)
        }

    }

    private fun disableUIcomponents() {
        signIn.isEnabled = false
        goToRegister.isEnabled = false
        email.isEnabled = false
        password.isEnabled = false
    }

    private fun enableUIcomponents() {
        val signInButton = activity?.findViewById<TextView>(R.id.signIn)
        signInButton?.isEnabled = true
        val emailText = activity?.findViewById<TextView>(R.id.email)
        emailText?.isEnabled = true
        val passwordText = activity?.findViewById<TextView>(R.id.password)
        passwordText?.isEnabled = true
        val goToRegisterButton = activity?.findViewById<TextView>(R.id.goToRegister)
        goToRegisterButton?.isEnabled = true
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        loginSuccesfull = activity as OnButtonLoginPressedListener
        registerListener = activity as OnTextRegistredPressedListener
    }


}
