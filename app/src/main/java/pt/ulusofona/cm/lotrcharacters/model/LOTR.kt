package pt.ulusofona.cm.lotrcharacters.model

abstract class LOTR {
    abstract fun getCharacters(onFinished: (Result<List<LOTRCharacter>>) -> Unit)
}