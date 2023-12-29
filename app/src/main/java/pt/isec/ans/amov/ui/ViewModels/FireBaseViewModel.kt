package pt.isec.ans.amov.ui.ViewModels

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.launch
import pt.isec.ans.amov.Utils.FireBase.AuthUtil
import pt.isec.ans.amov.Utils.FireBase.StorageUtil

data class  User (val name: String, val email: String, val picture: String?)

fun FirebaseUser.toUser() : User{
    val username = this.displayName ?: ""
    val strEmail = this.email ?: ""
    val photoUrl = this.photoUrl?.toString()

    return User(username, strEmail, photoUrl)
}

class FireBaseViewModel : ViewModel() {

    private val _error = mutableStateOf<String?>(null)
    val error : MutableState<String?>
        get() = _error

    private val _user = mutableStateOf(AuthUtil.currentUser?.toUser())

    val user : MutableState<User?>
        get() = _user

    //Auth
    fun createUserWithEmail(email: String, password: String) {
        if (email.isBlank() || password.isBlank())
            return

        viewModelScope.launch { AuthUtil.createUserWithEmail(email, password){ e ->
            if (e == null)
                _user.value = AuthUtil.currentUser?.toUser()

            _error.value = e?.message
        } }
    }

    fun signInWithEmail(email: String, password: String){
        if (email.isBlank() || password.isBlank())
            return

        viewModelScope.launch { AuthUtil.signInWithEmail(email, password){ e ->
            if (e == null)
                _user.value = AuthUtil.currentUser?.toUser()

            _error.value = e?.message
        } }
    }

    //Images
    fun uploadImage(imageUri: Uri, onSuccess: (String) -> Unit) {
        viewModelScope.launch { StorageUtil.uploadImageToFirebaseStorage(imageUri, onSuccess) }
    }

    fun uploadImages(imageUri: List<Uri>, onSuccess: (List<String>) -> Unit) {
        viewModelScope.launch { StorageUtil.uploadImagesToFirebaseStorage(imageUri){list ->
            onSuccess(list)
        } }
    }
    fun signOut() {
        AuthUtil.signOut()
        _error.value = null
        _user.value = null
    }

    //Categorias
    fun addCategory(name: String, desc : String, image: String) {
        viewModelScope.launch { StorageUtil.addCategory(name, desc, image){ e ->
            _error.value = e?.message
        }
        }
    }
    fun getAllCategories(onResult: (List<String>) -> Unit) {
        viewModelScope.launch {
            StorageUtil.getAllCategorysDocumentsNames { names ->
                onResult(names)
            }
        }
    }

    //Locations
    fun addLocation(country: String, region : String, desc: String, coordinates: GeoPoint, image : String) {
        viewModelScope.launch { StorageUtil.addLocation(country,region,desc,coordinates,image){ e ->
            _error.value = e?.message
        }
        }
    }
    fun getAllLocations(onResult: (List<String>) -> Unit) {
        viewModelScope.launch {
            StorageUtil.getAllLocationsDocumentsNames { names ->
                onResult(names)
            }
        }
    }

    fun getAllLocationsCoordinates(onResult: (List<GeoPoint>) -> Unit) {
        viewModelScope.launch {
            StorageUtil.getAllLocationsDocumentsCoordinates { coordinates ->
                onResult(coordinates)
            }
        }
    }

    //Attractions
    fun addAtraction(name: String, desc : String, coordinates: GeoPoint, category : String, Location : String, images : List<String>, onResult : (Throwable?) -> Unit) {
        viewModelScope.launch { StorageUtil.addAttraction(name, desc, coordinates, category, Location, images){ e ->
            _error.value = e?.message
            onResult(e)
        }
        }
    }

    //Users
    fun addUser(name: String, image: String) {
        viewModelScope.launch { StorageUtil.addUser(name, image){ e ->
            _error.value = e?.message
        }
        }
    }

    /*fun updateDataUserInFirestore() {
        viewModelScope.launch { StorageUtil.updateDataInFirestoreTrans{ e ->
            _error.value = e?.message
        } }
    }

    fun startObserver() {
       viewModelScope.launch { StorageUtil.startObserver() }
    }*/

}