package fr.orion.siobook

class CUserInProject(
        var idUIP: String ="",
        var idU: String ="",
        var idProject: String ="",
        var roleUIP: String ="",
        var nameUIP: String ="",
        var fnameUIP: String ="",
        var emailUIP: String ="",
        var stateUIP: Int =1,
        var ratioUIP: Int =0,
        var listtaskUIP: ArrayList<String> =ArrayList(),
        var listbooltaskUIP: ArrayList<Boolean> =ArrayList(),
        var listInttaskUIP: ArrayList<Int> =ArrayList(),
        var imgUip: String =""
)