package fr.orion.siobook

class Cuser(
        var idU: String ="",
        var emailU: String ="",
        var nameU: String ="",
        var fNameU: String ="",
        var imgUrlU: String =""
){
    fun affiche(): String{
        return "$FNAMEU $fNameU\n" +
                "$NAMEU $nameU\n" +
                "$IDUSER $idU\n" +
                "$IMGURLU $imgUrlU\n" +
                "$EMAILU $emailU\n"
    }
}