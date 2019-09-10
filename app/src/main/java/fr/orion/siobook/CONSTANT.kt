package fr.orion.siobook

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage

const val USER ="user"
const val IDUSER ="idU"
const val EMAILU ="emailU"
const val IMGURLU ="imgUrlU"
const val FNAMEU ="fNameU"
const val NAMEU ="nameU"
var IMGBITMAP =BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.men1)

const val PROJECT ="project"
const val IDP ="idP"
const val NAMEP ="nameP"
const val MILLISP ="millisP"
const val RATIOP ="ratioP"
const val STATEP ="state"
const val MEMBERP ="memberP"

const val USERINPROJECT ="UserInProject"
const val IDUIP ="idUIP"
const val IDU ="idU"
const val IDPROJECT ="idProject"
const val ROLEUIP ="roleUIP"
const val STATEUIP ="stateUIP"
const val LISTBOOLTASKUIP ="listbooltaskUIP"
const val LISTINTTASKUIP ="listInttaskUIP"
const val LISTTASKUIP ="listtaskUIP"
const val RATIOUIP ="ratioUIP"

const val MESSAGE ="message"
const val IDPM ="idPM"
const val IDM ="idM"
const val MILLISM ="millisM"
const val URLIMGUIP ="urlimgUIP"
const val CONTENTM ="contentM"

const val SMS ="SMS"
const val MMS ="MMS"


var IMGFULLSCREEN =BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.men1)
var IDIMGFULLSCREEN =""


var CUSER =Cuser()

var CPROJECT =Cproject()
var CIDPROJECT =""

var CUSERINPROJECT =CUserInProject()
var CIMGUIP =BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.men1)
var CIDUIP =""
var MYIDUIP =""

var LISTALLIMG: ArrayList<ArrayList<out Any>> = arrayListOf()

var LISTUIP: ArrayList<CUserInProject> =arrayListOf()
var LISTIDUIP: ArrayList<String> =arrayListOf()
//var LISTPROJECTH: ArrayList<Cproject> = arrayListOf()

var LISTLLM: ArrayList<LinearLayout> =arrayListOf()

var LISTFLDELETE: ArrayList<FrameLayout> =arrayListOf()
var LISTIMGINMSG: ArrayList<Bitmap> = arrayListOf()

val AUTH: FirebaseAuth = FirebaseAuth.getInstance()
@SuppressLint("StaticFieldLeak")
val DB = FirebaseFirestore.getInstance()
val DESC =Query.Direction.DESCENDING
val ASC =Query.Direction.ASCENDING
val STORAGE =FirebaseStorage.getInstance()

const val FLAG = "///////////////////////"

val LETTER: ArrayList<Char> = arrayListOf('a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z')
val MAJLETTER: ArrayList<Char> = arrayListOf('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z')
val NUMBER: ArrayList<Char> = arrayListOf('0','1','2','3','4','5','6','7','8','9')


interface OnItemClickListener {
    fun onItemClicked(position: Int, view: View)
}