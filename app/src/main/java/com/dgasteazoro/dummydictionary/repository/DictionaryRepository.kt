package com.dgasteazoro.dummydictionary.repository

import androidx.lifecycle.MutableLiveData
import com.dgasteazoro.dummydictionary.model.Word

class DictionaryRepository {

    private var _words = listOf(
        Word("arquivolta", "f. Arq. Moldura que decora la cara exterior de un arco."),
        Word(
            "pucelano, na",
            "adj. coloq. Natural de Valladolid, ciudad de España. U. t. c. s."
        ),
        Word(
            "alotropía",
            "f. Quím. Propiedad de algunos elementos químicos, debido a la cual pueden " +
                    "presentarse con estructuras moleculares distintas, como el oxígeno, que existe como " +
                    "oxígeno divalente y como ozono; o con características físicas diversas, como el carbono, que " +
                    "puede aparecer en forma de grafito o de diamante."
        ),
        Word("golpe", "m. Acción de dar con violencia un cuerpo contra otro"),
        Word(
            "concluir", "tr. Acabar o finalizar algo. Concluí mi exposición. U. t. c. " +
                    "intr. Medidas para concluir CON el exceso de velocidad en carretera."
        ),
        Word(
            "desbragar", " tr. And. Cavar alrededor de la cepa una pileta de unos" +
                    " 20 cm de profundidad, para quitar las raíces superficiales y recoger los " +
                    "brotes para injertos."
        ),
        Word("esforzar", "tr. Dar o comunicar fuerza o vigor."),
        Word(
            "convencionista",
            " m. y f. Guat., Nic. y Ven. Persona que participa en una convención (‖ reunión)."
        )
    ).toMutableList()

    val words: MutableLiveData<List<Word>> = MutableLiveData(_words)

    fun addWord(word: Word) {
        _words.add(word)
        words.value = _words
    }
}