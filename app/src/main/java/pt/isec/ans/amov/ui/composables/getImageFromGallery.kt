package pt.isec.ans.amov.ui.composables

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import pt.isec.ans.compose.Utils.Utils

//no botao para escolher a imagem da galeria
//onClick = { pickImageLauncher.launch(PickVisualMediaRequest()) }

//depois tentar por isto a dar

@Composable
fun SelectGalleryImages() {

    val pickImageLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    // Aqui você tem a URI da imagem selecionada
                    // Agora você pode fazer o upload para o Firestore ou atualizar o estado conforme necessário
                }
            }
        }
}