package com.app.collt.domain

data class DemoDataModel(
    var id: String = "",
    var input: DataModel? = null,
    var list: ArrayList<DummyModel> = arrayListOf()
)

data class DataModel(
    var id: String = "",
    var type: String = ""
)

data class DummyModel(
    var id: String = "",
    var input: DataModel? = null,
    var question: String = ""
)
