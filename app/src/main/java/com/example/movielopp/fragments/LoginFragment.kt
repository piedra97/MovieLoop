package com.example.movielopp.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.movielopp.model.User
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
    private var userToInsert:User? = null
    private var loginOk = false


    interface OnButtonLoginPressedListener {
        fun onLoginPressed()
    }

    interface OnTextRegistredPressedListener {
        fun onRegisteredPressed(username:String)

    }

    override fun onStart() {
        super.onStart()
       // userToInsert = arguments!!.getParcelable("user")
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

        auth = FirebaseAuth.getInstance()
        signIn.setOnClickListener {
            loginUser()
        }

        goToRegister.setOnClickListener {
            registerListener.onRegisteredPressed(email.text.toString())
        }

    }

    private fun loginUser() {
        val mail = email.text.toString()
        val passwd = password.text.toString()
        if (!mail.isEmpty() || !passwd.isEmpty()) {
            disableUIcomponents()
            FirebaseAuth.getInstance().signInWithEmailAndPassword(mail, passwd).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    enableUIcomponents()
                    loginSuccesfull.onLoginPressed()

                } else {
                    enableUIcomponents()
                    email.error = "Usuario o Contraseña incorrectos"
                }
            }
        }else {
            enableUIcomponents()
            email.error = "El mail o el Password no pueden estar vacíos"
        }

        /*val authenticationRepository = TokenAuthenticationRepository.instance
        var succes = true
        authenticationRepository.authenticate(RequestUser(userToInsert?.userName!!, userToInsert?.password!!, userToInsert?.requestToken!!), object : OnPutAuthenticationCallBack {
            override fun onSuccess(success: Boolean) {
                succes = success
                if (succes) {
                    print("User authenticated")
                }
            }

            override fun onError() {
                print("Not possible to validate the Token")
            }

        })*/

    }

    private fun disableUIcomponents() {
        signIn.isEnabled = false
        email.isEnabled = false
        password.isEnabled = false
    }

    private fun enableUIcomponents() {
        signIn.isEnabled = true
        email.isEnabled = true
        password.isEnabled = true
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        loginSuccesfull = activity as OnButtonLoginPressedListener
        registerListener = activity as OnTextRegistredPressedListener
    }

    companion object {

        fun newInstance(user: User): LoginFragment{
            val fragmentDetails = LoginFragment()
            val args = Bundle()

            //args.putParcelable("user", user)
            fragmentDetails.arguments = args

            return fragmentDetails
        }
    }


}
