package ie.setu.domain

import jdk.incubator.vector.VectorOperators.Binary

data class Image(
    var id: Int,
    var title: String,
    var description:String,
    var image_file_path: String,
    var userId: Int
)
