package pt.isec.ans.amov.ui.ViewModels

import android.net.Uri
import androidx.compose.runtime.MutableState

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.launch
import pt.isec.ans.amov.Utils.FireBase.AuthUtil
import pt.isec.ans.amov.Utils.FireBase.StorageUtil
import pt.isec.ans.amov.dataStructures.Attraction
import pt.isec.ans.amov.dataStructures.AttractionDetails
import pt.isec.ans.amov.dataStructures.Category
import pt.isec.ans.amov.dataStructures.CategoryDetails
import pt.isec.ans.amov.dataStructures.Location
import pt.isec.ans.amov.dataStructures.LocationDetails
import pt.isec.ans.amov.dataStructures.ReviewsDetails

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
    fun createUserWithEmail(email: String, password: String, name: String, photo: String) {
        if (email.isBlank() || password.isBlank())
            return

        viewModelScope.launch { AuthUtil.createUserWithEmail(email, password,name, photo){ e ->

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
    fun signOut() {
        AuthUtil.signOut()
        _error.value = null
        _user.value = null
    }

    fun getUserDetails(onResult: (String,String) -> Unit) {
        viewModelScope.launch {
            StorageUtil.getUserDetails { name, image ->
                onResult(name, image)
            }
        }
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
    //Categorias
    fun addCategory(name: String, desc : String, image: String) {
        viewModelScope.launch { StorageUtil.addCategory(name, desc, image){ e ->
            _error.value = e?.message
        }
        }
    }

    fun getNumCategoriesByUser(onResult: (Int) -> Unit) {
        viewModelScope.launch {
            StorageUtil.getNumCategoriesByUser { num ->
                onResult(num)
            }
        }
    }

    fun getNumLocationsByUser(onResult: (Int) -> Unit) {
        viewModelScope.launch {
            StorageUtil.getNumLocationsByUser { num ->
                onResult(num)
            }
        }
    }

    fun getNumAttractionsByUser(onResult: (Int) -> Unit) {
        viewModelScope.launch {
            StorageUtil.getNumAttractionsByUser { num ->
                onResult(num)
            }
        }
    }

    fun getAllCategoriesByUser(onResult: (List<CategoryDetails>, Throwable?) -> Unit) {
        viewModelScope.launch {
            StorageUtil.getAllDetailsFromCategoryByUser{ categoryDetailsList, error ->
                if (error == null) {
                    onResult(categoryDetailsList, null)
                } else {
                    onResult(emptyList(), error)
                }
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
    fun getCategories(name: String, onResult : (List<String>) -> Unit) {
        viewModelScope.launch {
            StorageUtil.getAllFromOneCategory(name) { desc ->
                onResult(desc)
            }
        }
    }
    fun updateCategories(categoryName: String,name: String, desc : String, image: String) {
        viewModelScope.launch { StorageUtil.updateCategory(categoryName,name, desc, image){ e ->
            _error.value = e?.message
        }
        }
    }

    fun deleteCategory(name: String) {
        viewModelScope.launch { StorageUtil.deleteCategory(name){ e ->
            _error.value = e?.message
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
    fun getLocationDetails(onResult: (Location) -> Unit, userGeo: GeoPoint, name: String) {
        viewModelScope.launch {
            StorageUtil.getLocationDetails(
                userGeo,
                name
            ) { locations ->
                if (locations != null) {
                    onResult(locations)
                }
            }
        }
    }
    fun getAttractionDetails(onResult: (Attraction) -> Unit, userGeo: GeoPoint, name: String) {
        viewModelScope.launch {
            StorageUtil.getAttractionDetails(
                userGeo = userGeo,
                name = name,
            ) { attraction ->
                if (attraction != null) {
                    onResult(attraction)
                }
            }
        }
    }

    fun getLocationDetails(userGeo: GeoPoint, locationGeo: GeoPoint, onResult: (Location) -> Unit) {
        viewModelScope.launch {
            StorageUtil.getLocationDetails(
                userGeo,
                locationGeo
            ) { locations ->
                if (locations != null) {
                    onResult(locations)
                }
            }
        }
    }

    fun getCategoryDetails(onResult: (Category) -> Unit, name: String, userGeo: GeoPoint) {
        viewModelScope.launch {
            StorageUtil.getCategoryDetails(
                name = name,
                userGeo = userGeo
            ) { category ->
                if (category != null) {
                    onResult(category)
                }
            }
        }
    }

    fun getCategoryDetails(onResult: (Category) -> Unit, name: String) {
        viewModelScope.launch {
            StorageUtil.getCategoryDetails(
                name = name,
            ) { category ->
                if (category != null) {
                    onResult(category)
                }
            }
        }
    }



    fun getAttractionDetails(attractionGeoPoint: GeoPoint, onResult: (Attraction) -> Unit) {
        viewModelScope.launch {
            StorageUtil.getAttractionDetails(
                attractionGeoPoint
            ) { attraction ->
                if (attraction != null) {
                    onResult(attraction)
                }
            }
        }
    }

    /*fun getAllAttractions(onResult: (List<String>) -> Unit) {
        viewModelScope.launch {
            StorageUtil.getAllAttractionsDocumentsNames { names ->
                onResult(names)
            }
        }
    }*/
    fun getLocations(name: String, onResult : (List<String>) -> Unit) {
        viewModelScope.launch {
            StorageUtil.getAllFromOneLocation(name) { desc ->
                onResult(desc)
            }
        }
    }
    fun updateLocations(locationName: String,country: String, region : String, desc: String, coordinates: GeoPoint, image : String) {
        viewModelScope.launch { StorageUtil.updateLocation(locationName,country,region,desc,coordinates,image){ e ->
            _error.value = e?.message
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

    fun getAllLocationsByUser(onResult: (List<LocationDetails>, Throwable?) -> Unit) {
        viewModelScope.launch {
            StorageUtil.getAllDetailsFromLocationByUser{ locationDetailsList, error ->
                if (error == null) {
                    onResult(locationDetailsList, null)
                } else {
                    onResult(emptyList(), error)
                }
            }
        }
    }

    fun deleteLocation(name: String) {
        viewModelScope.launch { StorageUtil.deleteLocation(name){ e ->
            _error.value = e?.message
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
    fun getAllAttractions(onResult: (List<String>) -> Unit) {
        viewModelScope.launch {
            StorageUtil.getAllAttractionsDocumentsNames { names ->
                onResult(names)
            }
        }
    }

    fun getAllAttractionsByUser(onResult: (List<AttractionDetails>, Throwable?) -> Unit) {
        viewModelScope.launch {
            StorageUtil.getAllDetailsFromAttractionByUser{ attractionDetailsList, error ->
                if (error == null) {
                    onResult(attractionDetailsList, null)
                } else {
                    onResult(emptyList(), error)
                }
            }
        }
    }

    fun switchAttractionToDelete(name: String, onResult: (Throwable?) -> Unit) {
        viewModelScope.launch {
            StorageUtil.switchToDeleteAttraction(name){ e ->
                _error.value = e?.message
                onResult(e)
            }
        }
    }

    fun getToDeleteAttractions(name: String, onResult : (Boolean) -> Unit) {
        viewModelScope.launch {
            StorageUtil.getToDeleteAttraction(name) { desc ->
                onResult(desc)
            }
        }
    }

    fun getAttractions(name: String, onResult : (List<String>) -> Unit) {
        viewModelScope.launch {
            StorageUtil.getAttractionDetails(name) { desc ->
                onResult(desc)
            }
        }
    }

    fun updateAttractions(attractionName: String,name: String, desc : String, coordinates: GeoPoint, category : String, Location : String, images : List<String>) {
        viewModelScope.launch { StorageUtil.updateAttraction(attractionName,name, desc, coordinates, category, Location, images){ e ->
            _error.value = e?.message
        }
        }
    }


    fun deleteAttraction(name: String) {
        viewModelScope.launch { StorageUtil.deleteAttraction(name){ e ->
            _error.value = e?.message
        }
        }
    }

    fun addReview(title: String, desc: String, image: String, attractionName: String, rating: Number) {
        viewModelScope.launch { StorageUtil.addReviews(title,desc,image,attractionName,rating){ e ->
            _error.value = e?.message
        }
        }
    }

    fun getAllReviewsByUser(onResult: (List<ReviewsDetails>, Throwable?) -> Unit) {
        viewModelScope.launch {
            StorageUtil.getAllDetailsFromReviewsByUser{ reviewsDetailsList, error ->
                if (error == null) {
                    onResult(reviewsDetailsList, null)
                } else {
                    onResult(emptyList(), error)
                }
            }
        }
    }

    fun deleteReview(reviewId: String) {
        viewModelScope.launch { StorageUtil.deleteReview(reviewId){ e ->
            _error.value = e?.message
        }
        }
    }

    fun getAllAttractionsCoordinates(onResult: (List<GeoPoint>) -> Unit) {
        viewModelScope.launch {
            StorageUtil.getAllAttractionsDocumentsCoordinates { coordinates ->
                onResult(coordinates)
            }
        }
    }
    fun getAttractionCategory(attraction: GeoPoint, onResult: (String?) -> Unit) {
        viewModelScope.launch {
            StorageUtil.getAttractionCategory(attraction) { category ->
                onResult(category)
            }
        }
    }

    fun addApprovedAttraction(attraction: String, onResult: (Throwable?) -> Unit) {
        viewModelScope.launch {
            StorageUtil.addApprovedAttractions(attraction) { e ->
                _error.value = e?.message
                onResult(e)
            }
        }
    }

    fun addApprovedToDeleteAttraction(attraction: String, onResult: (Throwable?) -> Unit) {
        viewModelScope.launch {
            StorageUtil.addApprovedToDeleteAttractions(attraction) { e ->
                _error.value = e?.message
                onResult(e)
            }
        }
    }

    fun getToDeleteBoolean(attraction: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            StorageUtil.getToDeleteBoolean(attraction) { to ->
                onResult(to)
            }
        }
    }

    fun addApprovedLocations(Location: String, onResult: (Throwable?) -> Unit) {
        viewModelScope.launch {
            StorageUtil.addApprovedLocations(Location) { e ->
                _error.value = e?.message
                onResult(e)
            }
        }
    }

    fun addApprovedCategories(Category: String, onResult: (Throwable?) -> Unit) {
        viewModelScope.launch {
            StorageUtil.addApprovedCategories(Category) { e ->
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