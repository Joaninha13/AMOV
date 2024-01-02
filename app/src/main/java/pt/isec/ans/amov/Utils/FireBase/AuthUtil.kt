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

        fun createUserWithEmail(email: String, password: String, name: String, photo: String,onResult : (Throwable?) -> Unit) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { result ->

                    val db = Firebase.firestore
                    val User = hashMapOf(
                        "Name" to name,
                        "Photo" to photo,
                    )

                    if (AuthUtil.currentUser != null) {
                        db.collection("Users").document(currentUser!!.uid).set(User)
                            .addOnCompleteListener { results ->
                                onResult(result.exception)
                            }
                    }
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