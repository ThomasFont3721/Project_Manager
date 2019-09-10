package fr.orion.siobook

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import androidx.constraintlayout.widget.ConstraintLayout
import android.view.LayoutInflater
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_img_in_msg.view.*
import kotlinx.android.synthetic.main.list_message.view.*
import kotlinx.android.synthetic.main.list_project.view.*
import kotlinx.android.synthetic.main.list_task_uip.view.*
import kotlinx.android.synthetic.main.list_user.view.*
import kotlinx.android.synthetic.main.list_user_in_project.view.*
import java.util.*
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources



class MyListAdapter(private val context: AppCompatActivity, listID: ArrayList<String>, private val listproject: ArrayList<Cproject>, private val index: Int): ArrayAdapter<String>(context, R.layout.list_project, listID) {

    @SuppressLint("ViewHolder", "InflateParams", "SetTextI18n")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.list_project, null, true)

        rowView._name_project.text = listproject[position].nameP
        rowView._progressbar_state_project.progress =listproject[position].ratioP
        rowView._progressbar_state_project.progressDrawable.setColorFilter(
                when(listproject[position].state){
            1-> ContextCompat.getColor(context, R.color.colorGoodProject)
            2->ContextCompat.getColor(context, R.color.colorMidlleProject)
            3->ContextCompat.getColor(context, R.color.colorBadProject)
            4->ContextCompat.getColor(context, R.color.colorEndProject)
            else -> ContextCompat.getColor(context, R.color.colorErrorProject)
        }, PorterDuff.Mode.SRC_IN)

        LOG("listproject[position}/listproject.size -----> $position/${listproject.size}")


        if (index == 0){
            val progressAnimator =ObjectAnimator.ofInt(rowView._progressbar_state_project, "progress", 0, listproject[position].ratioP)
            progressAnimator.duration = 1000
            progressAnimator.interpolator = AccelerateDecelerateInterpolator()
            progressAnimator.start()
        }
        else { rowView._progressbar_state_project.progress = listproject[position].ratioP}

        rowView._state_project.text = "${listproject[position].ratioP}%"

        /*if (LISTPROJECTH.size > 0 &&  LISTPROJECTH.size == listproject.size && LISTPROJECTH[position].idP == listproject[position].idP ){
            val progressAnimator =ObjectAnimator.ofInt(rowView._progressbar_state_project, "progress", LISTPROJECTH[position].ratioP, listproject[position].ratioP)
            progressAnimator.duration = 1000
            progressAnimator.interpolator = AccelerateDecelerateInterpolator()
            progressAnimator.start()
            LOG("${LISTPROJECTH[position].ratioP} -> ${listproject[position].ratioP}")
            LISTPROJECTH[position] =listproject[position]
        }
        else {
            val progressAnimator =ObjectAnimator.ofInt(rowView._progressbar_state_project, "progress", 0, listproject[position].ratioP)
            progressAnimator.duration = 1000
            progressAnimator.interpolator = AccelerateDecelerateInterpolator()
            progressAnimator.start()
            Log.i(FLAG, "0 -> ${listproject[position].ratioP}")
            if (LISTPROJECTH.size < listproject.size && !LISTPROJECTH.contains(listproject[position])){
                LOG(" ip add --> ${listproject[position].idP} at position $position")
                LISTPROJECTH.add(position, listproject[position])
            }
        }
        var i=0
        while (i < listproject.size){
            if (listproject.size == LISTPROJECTH.size){
                LOG("${LISTPROJECTH[position].idP} ---> ${listproject[position].idP}")
            }
            i++
        }*/

        return rowView
    }
}
class MyAdapterNameList(private val context: AppCompatActivity, email: ArrayList<String>, private val user: ArrayList<Cuser>): ArrayAdapter<String>(context, R.layout.list_user, email) {

    @SuppressLint("ViewHolder", "InflateParams", "SetTextI18n")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.list_user, null, true)

        rowView._name_user.text = "${user[position].nameU} ${user[position].fNameU}"
        rowView._email_user.text = user[position].emailU
        return rowView
    }
}
class MyAdapterRoleList(private val context: AppCompatActivity, private val name: ArrayList<String>): ArrayAdapter<String>(context, R.layout.list_user, name) {

    @SuppressLint("ViewHolder", "InflateParams", "SetTextI18n")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.list_user, null, true)

        rowView._name_user.text = name[position]
        return rowView
    }
}
class MyListAdapterHome_Project(private val context: AppCompatActivity, listID: ArrayList<String>, private val listUIP: ArrayList<CUserInProject>, private val index: Int): ArrayAdapter<String>(context, R.layout.list_user_in_project, listID) {

    @SuppressLint("ViewHolder", "InflateParams", "SetTextI18n")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.list_user_in_project, null, true)


        rowView._name_user_in_project.text = "${listUIP[position].fnameUIP} ${listUIP[position].nameUIP}"
        rowView._role_user_in_project.text =listUIP[position].roleUIP
        rowView._ratio_user_in_project.text = "${listUIP[position].ratioUIP}%"
        rowView._progressbar_user_in_project.progressDrawable.setColorFilter(
                when(listUIP[position].stateUIP){
                    1->ContextCompat.getColor(context, R.color.colorGoodProject)
                    2->ContextCompat.getColor(context, R.color.colorMidlleProject)
                    3->ContextCompat.getColor(context, R.color.colorBadProject)
                    4->ContextCompat.getColor(context, R.color.colorPrimary)
                    else -> ContextCompat.getColor(context, R.color.colorErrorProject)
                }, PorterDuff.Mode.SRC_IN)

        if (index ==0){
            val progressAnimator = ObjectAnimator.ofInt(rowView._progressbar_user_in_project, "progress", 0, listUIP[position].ratioUIP)
            progressAnimator.duration = 1000
            progressAnimator.interpolator = AccelerateDecelerateInterpolator()
            progressAnimator.start()
        }
        else{
            rowView._progressbar_user_in_project.progress =listUIP[position].ratioUIP
        }
        return rowView
    }
}
@Suppress("IMPLICIT_CAST_TO_ANY")
class MyListAdapterMessage_Project(private val context: AppCompatActivity, listID: ArrayList<String>, private val listM: ArrayList<Cmessage>, private val lv: ListView): ArrayAdapter<String>(context, R.layout.list_message, listID) {

    @SuppressLint("ViewHolder", "InflateParams", "SetTextI18n")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.list_message, null, true)

        val date = Calendar.getInstance()
        val message =listM[position]
        date.timeInMillis = message.millisM
        var mois =(date.get(Calendar.MONTH)+1).toString()
        var jour =date.get(Calendar.DAY_OF_MONTH).toString()
        var heure =date.get(Calendar.HOUR_OF_DAY).toString()
        var min =date.get(Calendar.MINUTE).toString()

        if (mois.length == 1){
            mois ="0$mois"
        }
        if (jour.length == 1){
            jour ="0$jour"
        }
        if (heure.length == 1){
            heure ="0$heure"
        }
        if (min.length == 1){
            min ="0$min"
        }

        rowView._hour_message.text ="$jour/$mois, $heure:$min"
        rowView._tv_name_uipSM.text =message.nameUIPSM

        when (message.typeMsg){
            SMS -> {
                rowView._content_message.text =message.contentM
                rowView._iv_img_mms.visibility =View.GONE
            }
            MMS -> {
                rowView._content_message.visibility =View.GONE
                val sizeW =800
                if (LISTALLIMG.size > 0) {
                    var bool = false
                    var find = 0
                    LISTALLIMG.forEachIndexed { i, it ->
                        if (it[0] == message.idM) {
                            bool = true
                            find = i
                        }
                    }
                    if (!bool) {
                        LOG("$position start img")
                        bool = true
                        val ONE_MEGABYTE: Long = 1024 * 1024
                        STORAGE.reference.child("img_msg/${message.idM}").getBytes(ONE_MEGABYTE)
                                .addOnSuccessListener {
                                    LISTALLIMG.add(arrayListOf(message.idM, BitmapFactory.decodeByteArray(it, 0, it.size)))

                                    rowView._iv_img_mms.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
                                    rowView._iv_img_mms.layoutParams.height =(sizeW*rowView._iv_img_mms.layoutParams.height/rowView._iv_img_mms.layoutParams.width)
                                    rowView._iv_img_mms.layoutParams.width =sizeW
                                    //lv.smoothScrollToPosition(listM.lastIndex)

                                    LOG("img ${message.idM} in message récuperer at position $position")
                                    LOG("$position end img")
                                }
                                .addOnFailureListener {
                                    LOG("img error in message ${message.idM}::: $it")
                                    bool = false
                                }
                    } else {
                        LOG("img in message : ${message.idM} find at position $position ")
                        rowView._iv_img_mms.setImageBitmap(LISTALLIMG[find][1] as Bitmap)
                        rowView._iv_img_mms.layoutParams.height =(sizeW*rowView._iv_img_mms.layoutParams.height/rowView._iv_img_mms.layoutParams.width)
                        rowView._iv_img_mms.layoutParams.width =sizeW
                    }
                }
                else {
                    LOG("$position start img in msg size list 0")

                    var recup =false
                    if (!recup){
                        recup =true
                        val ONE_MEGABYTE: Long = 1024 * 1024
                        STORAGE.reference.child("img_msg/${message.idM}").getBytes(ONE_MEGABYTE)
                                .addOnSuccessListener {

                                    LISTALLIMG.add(arrayListOf(message.idM,  BitmapFactory.decodeByteArray(it, 0, it.size)))

                                    rowView._iv_img_mms.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
                                    rowView._iv_img_mms.layoutParams.height =(sizeW*rowView._iv_img_mms.layoutParams.height/rowView._iv_img_mms.layoutParams.width)
                                    rowView._iv_img_mms.layoutParams.width =sizeW

                                    LOG("img ${message.idM} in message récuperer at position $position")
                                    LOG("$position end img")
                                }
                                .addOnFailureListener {
                                    recup =false
                                    LOG("img error in message ${message.idM}::: $it")
                                }
                    }
                }
            }
        }

        if (message.idUIPSM == MYIDUIP){
            rowView._content_message.setBackgroundResource(R.drawable.zz_background_user_me) //background bubble blue
            rowView._content_message.setTextColor(ContextCompat.getColor(context, R.color.colorMsgMe)) //change color

            rowView._ll_content_message.gravity =Gravity.END// mise a droite
            rowView._ll_hour_message.gravity =Gravity.END

            rowView._ll_name_sender.visibility =View.GONE
            rowView._cl_left.visibility =View.GONE
        }//mise a droite
        else {
            rowView._content_message.setBackgroundResource(R.drawable.zz_background_user_other)
            rowView._tv_lettre_left.text =message.nameUIPSM.toCharArray()[0].toString()
            try {
                if (LISTALLIMG.size > 0){
                    var bool =false
                    var i =0
                    var find =0
                    LISTALLIMG.forEach {
                        if (it[0] == message.idUIPSM){
                            bool =true
                            find =i
                        }
                        i++
                    }
                    if (!bool){
                        LOG("$position start img")
                        bool =true
                        val ONE_MEGABYTE: Long = 1024 * 1024
                        STORAGE.reference.child("img_user/${message.idUSM}").getBytes(ONE_MEGABYTE)
                                .addOnSuccessListener {
                                    rowView._iv_left.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
                                    rowView._tv_lettre_left.visibility =View.GONE

                                    LISTALLIMG.add(arrayListOf(message.idUIPSM, BitmapFactory.decodeByteArray(it, 0, it.size)))

                                    LOG("img ${message.idUSM} récuperer")
                                    LOG("$position end img")
                                }
                                .addOnFailureListener {
                                    rowView._tv_lettre_left.text =message.nameUIPSM.toCharArray()[0].toString()
                                    LOG("img error ::: $it")
                                    bool =false
                                }
                    }
                    else{
                        LOG("img uip : ${message.idUIPSM} find at position $position")
                        rowView._iv_left.setImageBitmap(LISTALLIMG[find][1] as Bitmap)
                        rowView._tv_lettre_left.visibility =View.GONE
                    }

                }
                else{
                    LOG("$position start img size list 0")

                    val ONE_MEGABYTE: Long = 1024 * 1024
                    STORAGE.reference.child("img_user/${message.idUSM}").getBytes(ONE_MEGABYTE)
                            .addOnSuccessListener {
                                rowView._iv_left.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
                                rowView._tv_lettre_left.visibility =View.GONE

                                LISTALLIMG.add(arrayListOf(message.idUIPSM, BitmapFactory.decodeByteArray(it, 0, it.size)))

                                LOG("img ${message.idUSM} récuperer")
                                LOG("$position end img")
                            }
                            .addOnFailureListener {
                                rowView._tv_lettre_left.text =message.nameUIPSM.toCharArray()[0].toString()
                                LOG("img error ::: $it")
                            }
                }


            }
            catch (e: Exception){
                LOG("error download img $position ::: $e")
            }
        }//chargement img uip

        return rowView
    }
}
class MyListAdapterImgInMsg(private val context: AppCompatActivity, listnothing: ArrayList<String>, private val listIMG: ArrayList<ByteArray>): ArrayAdapter<String>(context, R.layout.list_img_in_msg, listnothing) {

    @SuppressLint("ViewHolder", "InflateParams", "SetTextI18n")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.list_img_in_msg, null, true)

        rowView._iv_msg_img_send.setImageBitmap(BitmapFactory.decodeByteArray(listIMG[position], 0, listIMG[position].size))
        rowView._iv_msg_img_send.layoutParams.width =400
        rowView._iv_msg_img_send.layoutParams.height =400

        return rowView
    }
}
class MyListAdapterTaskUIP(private val context: AppCompatActivity,private val listtask: ArrayList<String>,private val listbool: ArrayList<Boolean>,private val listint: ArrayList<Int>): ArrayAdapter<String>(context, R.layout.list_task_uip, listtask) {

    @SuppressLint("ViewHolder", "InflateParams", "SetTextI18n")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.list_task_uip, null, true)

        if (listbool[position]){
            rowView._iv_check.setImageDrawable(context.getDrawable(R.drawable.ic_check_white_24dp))
        }
        rowView._tv_text_check.text =listtask[position]

        val unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.zz_background_task_list_user)
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
        DrawableCompat.setTint(wrappedDrawable, when(listint[position]){
            1-> ContextCompat.getColor(context, R.color.colorGoodProject)
            2->ContextCompat.getColor(context, R.color.colorMidlleProject)
            3->ContextCompat.getColor(context, R.color.colorBadProject)
            4->ContextCompat.getColor(context, R.color.colorEndProject)
            else -> ContextCompat.getColor(context, R.color.colorErrorProject)
        })


        return rowView
    }
}

class MyAdapterMessage_Project(private val context: Context, private val listM: ArrayList<Cmessage>, private val rv: RecyclerView) : RecyclerView.Adapter<MyAdapterMessage_Project.MyViewHolder>() {

    private var mOnItemClickListener: View.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_message, parent, false)

        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        val date = Calendar.getInstance()
        val message =listM[position]
        date.timeInMillis = message.millisM
        var mois =(date.get(Calendar.MONTH)+1).toString()
        var jour =date.get(Calendar.DAY_OF_MONTH).toString()
        var heure =date.get(Calendar.HOUR_OF_DAY).toString()
        var min =date.get(Calendar.MINUTE).toString()

        if (mois.length == 1){
            mois ="0$mois"
        }
        if (jour.length == 1){
            jour ="0$jour"
        }
        if (heure.length == 1){
            heure ="0$heure"
        }
        if (min.length == 1){
            min ="0$min"
        }

        holder.hour.text ="$jour/$mois, $heure:$min"
        holder.name.text =message.nameUIPSM

        when (message.typeMsg){
            SMS -> {
                LOG("SMS at position ::: $position")
                holder.content.text =message.contentM
                holder.ivMMS.visibility =View.GONE
            }
            MMS -> {
                LOG("MMS at position ::: $position")
                holder.content.visibility =View.GONE
                val sizeW =800
                if (LISTALLIMG.size > 0) {
                    var bool = false
                    var find = 0
                    LISTALLIMG.forEachIndexed { i, it ->
                        if (it[0] == message.idM) {
                            bool = true
                            find = i
                        }
                    }
                    if (!bool) {
                        LOG("$position start img")
                        bool = true
                        val ONE_MEGABYTE: Long = 1024 * 1024
                        STORAGE.reference.child("img_msg/${message.idM}").getBytes(ONE_MEGABYTE)
                                .addOnSuccessListener {
                                    LISTALLIMG.add(arrayListOf(message.idM, BitmapFactory.decodeByteArray(it, 0, it.size)))

                                    holder.ivMMS.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
                                    holder.ivMMS.layoutParams.height =(sizeW*holder.ivMMS.layoutParams.height/holder.ivMMS.layoutParams.width)
                                    holder.ivMMS.layoutParams.width =sizeW
                                    //rv.smoothScrollToPosition(listM.lastIndex)

                                    LOG("img ${message.idM} in message récuperer at position $position")
                                    LOG("$position end img")
                                }
                                .addOnFailureListener {
                                    LOG("img error in message ${message.idM}::: $it")
                                    bool = false
                                }
                    } else {
                        LOG("img in message : ${message.idM} find at position $position ")
                        holder.ivMMS.setImageBitmap(LISTALLIMG[find][1] as Bitmap)
                        holder.ivMMS.layoutParams.height =(sizeW*holder.ivMMS.layoutParams.height/holder.ivMMS.layoutParams.width)
                        holder.ivMMS.layoutParams.width =sizeW
                    }
                }
                else {
                    LOG("$position start img in msg size list 0")

                    var recup =false
                    if (!recup){
                        recup =true
                        val ONE_MEGABYTE: Long = 1024 * 1024
                        STORAGE.reference.child("img_msg/${message.idM}").getBytes(ONE_MEGABYTE)
                                .addOnSuccessListener {

                                    LISTALLIMG.add(arrayListOf(message.idM,  BitmapFactory.decodeByteArray(it, 0, it.size)))

                                    holder.ivMMS.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
                                    holder.ivMMS.layoutParams.height =(sizeW*holder.ivMMS.layoutParams.height/holder.ivMMS.layoutParams.width)
                                    holder.ivMMS.layoutParams.width =sizeW

                                    LOG("img ${message.idM} in message récuperer at position $position")
                                    LOG("$position end img")
                                }
                                .addOnFailureListener {
                                    recup =false
                                    LOG("img error in message ${message.idM}::: $it")
                                }
                    }
                }
            }
        }

        if (message.idUIPSM == MYIDUIP){
            holder.content.setBackgroundResource(R.drawable.zz_background_user_me) //background bubble blue
            holder.content.setTextColor(ContextCompat.getColor(context, R.color.colorMsgMe)) //change color

            holder.ll_content.gravity =Gravity.END// mise a droite
            holder.ll_hour.gravity =Gravity.END

            holder.ll_name.visibility =View.GONE
            holder.cl_left.visibility =View.GONE
        }//mise a droite
        else {
            holder.content.setBackgroundResource(R.drawable.zz_background_user_other)
            holder.letter.text =message.nameUIPSM.toCharArray()[0].toString()
            try {
                if (LISTALLIMG.size > 0){
                    var bool =false
                    var i =0
                    var find =0
                    LISTALLIMG.forEach {
                        if (it[0] == message.idUIPSM){
                            bool =true
                            find =i
                        }
                        i++
                    }
                    if (!bool){
                        LOG("$position start img")
                        bool =true
                        val ONE_MEGABYTE: Long = 1024 * 1024
                        STORAGE.reference.child("img_user/${message.idUSM}").getBytes(ONE_MEGABYTE)
                                .addOnSuccessListener {
                                    holder.ivUIP.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
                                    holder.letter.visibility =View.GONE

                                    LISTALLIMG.add(arrayListOf(message.idUIPSM, BitmapFactory.decodeByteArray(it, 0, it.size)))

                                    LOG("img ${message.idUSM} récuperer")
                                    LOG("$position end img")
                                }
                                .addOnFailureListener {
                                    holder.letter.text =message.nameUIPSM.toCharArray()[0].toString()
                                    LOG("img error ::: $it")
                                    bool =false
                                }
                    }
                    else{
                        LOG("img uip : ${message.idUIPSM} find at position $position")
                        holder.ivUIP.setImageBitmap(LISTALLIMG[find][1] as Bitmap)
                        holder.letter.visibility =View.GONE
                    }

                }
                else{
                    LOG("$position start img size list 0")

                    val ONE_MEGABYTE: Long = 1024 * 1024
                    STORAGE.reference.child("img_user/${message.idUSM}").getBytes(ONE_MEGABYTE)
                            .addOnSuccessListener {
                                holder.ivUIP.setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
                                holder.letter.visibility =View.GONE

                                LISTALLIMG.add(arrayListOf(message.idUIPSM, BitmapFactory.decodeByteArray(it, 0, it.size)))

                                LOG("img ${message.idUSM} récuperer")
                                LOG("$position end img")
                            }
                            .addOnFailureListener {
                                holder.letter.text =message.nameUIPSM.toCharArray()[0].toString()
                                LOG("img error ::: $it")
                            }
                }


            }
            catch (e: Exception){
                LOG("error download img $position ::: $e")
            }
        }//chargement img uip
    }

    override fun getItemCount(): Int {
        return listM.size
    }

    //TODO: Step 2 of 4: Assign itemClickListener to your local View.OnClickListener variable
    fun setOnItemClickListener(itemClickListener: View.OnClickListener) {
        mOnItemClickListener = itemClickListener
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hour: TextView =itemView._hour_message
        val name: TextView =itemView._tv_name_uipSM
        val content: TextView =itemView._content_message
        val letter: TextView =itemView._tv_lettre_left
        val ivMMS: ImageView =itemView._iv_img_mms
        val ivUIP: ImageView =itemView._iv_left
        val ll_content: LinearLayout =itemView._ll_content_message
        val ll_hour: LinearLayout =itemView._ll_hour_message
        val ll_name: LinearLayout =itemView._ll_name_sender
        val cl_left: ConstraintLayout =itemView._cl_left
        init {
            //TODO: Step 3 of 4: setTag() as current view holder along with
            // setOnClickListener() as your local View.OnClickListener variable.
            // You can set the same mOnItemClickListener on multiple views if required
            // and later differentiate those clicks using view's id.
            itemView.tag = this
            itemView.setOnClickListener(mOnItemClickListener)
        }
    }
}
class MyAdapterImgInMsg(private val listIMG: List<ByteArray>) : RecyclerView.Adapter<MyAdapterImgInMsg.MyViewHolder>() {

    private var mOnItemClickListener: View.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_img_in_msg, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.img.setImageBitmap(BitmapFactory.decodeByteArray(listIMG[position], 0, listIMG[position].size))
    }

    override fun getItemCount(): Int {
        return listIMG.size
    }

    //TODO: Step 2 of 4: Assign itemClickListener to your local View.OnClickListener variable
    fun setOnItemClickListener(itemClickListener: View.OnClickListener) {
        mOnItemClickListener = itemClickListener
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img = itemView._iv_msg_img_send
        init {
            //TODO: Step 3 of 4: setTag() as current view holder along with
            // setOnClickListener() as your local View.OnClickListener variable.
            // You can set the same mOnItemClickListener on multiple views if required
            // and later differentiate those clicks using view's id.
            itemView.tag = this
            itemView.setOnClickListener(mOnItemClickListener)
        }
    }
}