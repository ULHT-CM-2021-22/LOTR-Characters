package pt.ulusofona.cm.lotrcharacters.model

/**
 * This must be a pure class, without dependencies of json, parcelable, etc..
 */
data class LOTRCharacter(
    val id: String,
    val birth: String,
    val death: String,
    val gender: String,
    val name: String
)
