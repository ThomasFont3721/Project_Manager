package fr.orion.siobook

import com.google.firebase.Timestamp

class CprojectEnd (
        var nameP: String ="",
        var startDateP: Timestamp = Timestamp.now(),
        var endDateP: Timestamp = Timestamp.now(),
        var ratioP: Int =0,
        var state: Int =1,
        var memberP: ArrayList<String> =ArrayList(),
        var idP: String =""
)