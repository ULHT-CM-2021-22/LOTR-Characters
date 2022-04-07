package pt.ulusofona.cm.lotrcharacters.model

abstract class LOTR {
    abstract fun getCharacters(onFinished: (List<LOTRCharacter>) -> Unit)
}