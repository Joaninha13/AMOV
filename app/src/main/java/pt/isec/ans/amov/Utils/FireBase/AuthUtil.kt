package pt.isec.ans.amov.Utils.FireBase

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AuthUtil {

    companion object {
        private val auth by lazy { Firebase.auth }

        val currentUser : FirebaseUser?
            get() = auth.currentUser

        fun createUserWithEmail(email: String, password: String,onResult : (Throwable?) -> Unit) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { result ->
                  onResult(result.exception)
                }
        }

        fun signInWithEmail(email: String, password: String, onResult : (Throwable?) -> Unit) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { result ->
                    onResult(result.exception)
                }
        }

        fun signOut() {
            if(currentUser != null)
                auth.signOut()
        }

    }

}