package io.eganjs.extensions

fun String.toPascalCase(): String {
    data class Step(val currentChar: Char, val outputString: String)

    val initialStep = Step(' ', "")
    val (_, outputString) = this.fold(initialStep) { (previousChar, outputString), currentChar ->
        when {
            previousChar == '\'' && currentChar == 's' ->
                Step(currentChar, outputString + currentChar)

            !previousChar.isLetter() && currentChar.isLetter() ->
                Step(currentChar, outputString + currentChar.toUpperCase())

            currentChar.isLetter() ->
                Step(currentChar, outputString + currentChar)

            else ->
                Step(currentChar, outputString)
        }
    }
    return outputString
}
