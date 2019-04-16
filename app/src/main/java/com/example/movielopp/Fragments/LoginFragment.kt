package com.example.movielopp.Fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.movielopp.Model.User
import com.example.movielopp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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

            FirebaseAuth.getInstance().signInWithEmailAndPassword(mail, passwd).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val currentUser = FirebaseAuth.getInstance().currentUser
                    val db = FirebaseDatabase.getInstance()

                    val ref = db.getReference("/users/${currentUser?.uid}")

                    ref.addValueEventListener(object: ValueEventListener {
                        override fun onCancelled(databaseError: DatabaseError) {
                            print("The read failed: ${databaseError.code}")
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            val userToChange = snapshot.value as Map<Any, Any>
                            userToInsert = User(userToChange["uid"] as String, userToChange["userName"] as String, userToChange["password"] as String, userToChange["requestToken"] as String, userToChange["sessionID"] as String)



                            loginSuccesfull.onLoginPressed()
                        }

                    })



                } else {
                    email.error = "Usuario o Contraseña incorrectos"
                }
            }
        }else {
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
