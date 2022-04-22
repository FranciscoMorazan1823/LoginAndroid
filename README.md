# Práctica 1 - RecyclerView 

## Introducción 

En cualquier desarrollo es normal encontrar la necesidad de mostrar una forma sencilla para el usuario de navegar, filtrar, buscar, administrar una colección de datos. Algunos de los problemas principales al implementar esta tarea es optimizar los tiempos de carga y la cantidad de memoria utilizada a medida la cantidad de datos se vuelve grande.

En este laboratorio comprenderás y utilizarás un ViewGroup diseñado par este propósito.

## RecyclerView

RecyclerView es una librería que forma parte del Android Jetpack y además el nombre de la clase que se utiliza en código, cuando la palabra RecyclerView este escrita cómo código se esta haciendo referencia a la clase `RecyclerView`. Este permite mostrar con facilidad un conjunto de datos encargándose de todo el trabajo que implica optimizar los recursos.

RecyclerView se basa en reciclar los `View` que utiliza para mostrar cada elemento en pantalla. Cuando `RecyclerView` es notificado de la cantidad de datos que debe mostrar, crea la cantidad mínimas de `View` para los elementos que se pueden mostrar en pantalla a la vez, cuando el usuario se desplaza sobre el conjunto de datos, los elementos que salen de la vista del usuario no se destruyen sino que se reutilizan para mostrar los elementos siguientes o anteriores, dependiendo de las acciones del usuario. Esto mejora en gran medida el rendimiento y la capacidad de respuesta de la aplicación reduciendo el consumo de energía y memoria, criterios de desempeño de alta importancia en el desarrollo móvil. 

## Clases clave

Para poder utilizar `RecyclerView` es necesario comprender las clases que trabajan juntas para mostrar un lista dinámica.

1. `RecyclerView` es el `ViewGroup` que contiene las vistas de cada elemento de la lista, es como cualquier otro ViewGroup solamente que sus hijos se agregan de forma dinámica. En el XML siempre se utilizara como una etiqueta que cierra sobre si misma.
2. `RecyclerView.ViewHolder` es un contenedor de vistas que se utiliza para enlazar o relacionar el diseño de un elemento visual con sus datos. Recuerda que se crea pocos diseños y estos se reciclan, esta clase permite mantener la instancia de los elementos de diseño y modificar su contenido de forma sencilla según el dato a mostrar.
3. Para vincular la fuente de datos se utiliza un adaptador, la función de este es responder a las solicitudes del RecyclerView para poder vincular los datos con la vistas. Puedes pensar en esta clase como el adaptador de carga de tu teléfono (el cubito),que te permite obtener energía desde el toma corriente de tu casa, para llevársela a tu teléfono, es decir, **adapta** la fuente de datos (toma corriente) a quien quien requiere los datos (teléfono); Todo esto sin necesidad de modificar el funcionamiento de la fuente de datos ni del destino, puedes leer más sobre el patron adaptador [aquí](https://refactoring.guru/es/design-patterns/adapter) 
4. El **administrador de diseño** organiza los elementos individuales de la lista. Estos los proporciona la clase `LayoutManager` o pueden ser definidos de forma personalizada.


## En Marcha

En esta guiá aprenderás a utilizar RecyclerView, en el ejercicio que desarrollaras se mantendrá la fuente de datos lo más simple posible para poder comprender este ejercicio debe entender los siguientes conceptos:

- Fragmentos
- ViewModel
- DataBinding
- Navigation

En esta aplicación mostraras una lista de palabras

<Inserte Vista Prevua aqui>

## Configuración del proyecto

1. Crear un proyecto con el nombre DummyDictionary
2. El gradle de proyecto tendrá el siguiente código

```
buildscript {
    ext {
        activity_version = "1.4.0"
        appcompat_version = "1.4.1"
        fragment_version = "1.4.1"
        nav_version = "2.4.2"
    }

    repositories {
        google()
    }
    dependencies {
        classpath "androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version"
    }
}


plugins {
    id 'com.android.application' version '7.1.1' apply false
    id 'com.android.library' version '7.1.1' apply false
    id 'org.jetbrains.kotlin.android' version '1.6.10' apply false
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
```
3. Agregar los siguientes elementos al Gradel del modulo
   
```
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'androidx.navigation.safeargs.kotlin'
}
```

```
buildFeatures {
  dataBinding true
}

```
```

dependencies {

    implementation 'androidx.core:core-ktx:1.7.0'
    implementation "androidx.appcompat:appcompat:$appcompat_version"
    implementation 'com.google.android.material:material:1.5.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.3'

    implementation "androidx.activity:activity-ktx:$activity_version"
    implementation "androidx.fragment:fragment-ktx:$fragment_version"

    implementation "androidx.navigation:navigation-fragment-ktx:$nav_version"
    implementation "androidx.navigation:navigation-ui-ktx:$nav_version"

    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.3'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'
    debugImplementation "androidx.fragment:fragment-testing:$fragment_version"
}
```

## Model

En el patron MVVM la parte del modelo se define como clases de tipo POJO o data class en las cuales definimos la estructura de nuestros datos, utilizando entidades. Por eso crearemos un paquete llamado `model` y dentro crearemos la clase `Word`

```
data class Word(val word: String, val definition: String)
```
## Repository

Al MVVM le podemos agregar un capa más llamada Repository que nos permite tener un fuente de datos confiable, ya que este se encargara de obtener los datos del lugar correcto, ya sea desde la base de datos, archivos o Internet, sin exponer esos detalles al ViewModel, que se encargara de administrar los datos de la vista.

Para este ejemplo también sera el lugar donde definiremos las lista de palabras y su forma de manipularla, luego en otros laboratorios agregaremos una base de datos en lugar de datos estáticos.

Crea un paquete llamado `repository` y dentro la clase `DictionaryRepository` con el siguiente código:

```
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

    val words: MutableLiveData<List<Word>>
        get() = MutableLiveData(_words)

    fun addWord(word: Word) {
        _words.add(word)
        words.value = _words
    }
}
```

Lo que hace este código es exponer en modo lectura nuestra lista de palabras y agrega un método que nos permite agregar palabras al diccionario, en este misma clase se debe agregar la forma de actualizar o eliminar una palabra en nuestra fuente de datos, para nuestro caso la lista `_words`.

### Actividad

Agregue la opción de eliminar una palabra y la opción de modificar una definición

## ViewModel

Definiremos el modelo de datos que se mostrara en pantalla, este es realmente sencillo, solo mostraremos una lista de palabras, pero debemos escribirlo de tal manera que este preparado para la inyección de dependencia, para que el viewModel reciba en momento de la creación el repositorio de donde obtendrá los datos de la siguiente manera

```
class WordViewModel(private val repository: DictionaryRepository): ViewModel() {
    val words = repository.words
}
```

Por como funciona ViewModel, para crear un instancia de la clase anterior, debemos definir una Factory, las fabricas de ViewModel deben implementar utilizando la interfaz `ViewModelProvider.Factory` que nos obliga a sobre escribir el método `create` este sera el encargado de crear la instancia del ViewModel y pasar la referencia de un objeto de tipo `DictionaryRepository`

```
class WordViewModelFactory(private val repository: DictionaryRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordViewModel::class.java)) {
            return WordViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
```
Como podrás notar el código valida se el modelo de clase solicitado, es de tipo WordViewModel y si es así crea un instancia y la transforma en formato T, de lo contrario lanzara una excepción. Es necesario notar que esta Factory debe usarse para todo los ViewModels que dependan de DictionaryRepository. Si quires saber más sobre el patron de fabrica puedes consultar [aquí](https://refactoring.guru/es/design-patterns/factory-method) 

## Fragmento

Ya que tenemos listo la fuente de datos y el ViewModel, procederemos a crear un Fragmento llamado `WordListFragment` y su archivo XML con el nombre `fragment_word_list`.

En `fragment_word_list` utilizaremos el ViewGroup `RecyclerView` donde se ubicaran cada palabra del diccionario, además activaremos databinding para futuras modificaciones

```
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:android="http://schemas.android.com/apk/res/android">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <androidx.recyclerview.widget.RecyclerView
            android:id="@+id/word_list_recycler_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            tools:listitem="@layout/item_word" />

    </androidx.constraintlayout.widget.ConstraintLayout>
</layout>
```
 Como podras notar el ViewGroup se define con `androidx.recyclerview.widget.RecyclerView` y uno de sus atributos es `app:layoutManager` que nos permite definir como los elementos de la lista se organizaran, el valor actual declara que se utilizara el `LineraLayoutManager` que organiza nuestro elementos como un `LinearLayout`. Es importante reconocer que el layout manager se puede definir desde XML o desde código.

 En `WordListFragment` inflaremos la vista utilizando DataBinding y ademas se debe crear la instancia de un `WordViewModelFactory` para luego utilizarlo para obtener de forma adecuada la instancia de `WordViewModel`

```
 class WordListFragment : Fragment() {
    private val viewModelFactory by lazy {
        val repository = DictionaryRepository()
        WordViewModelFactory(repository)
    }
    private val viewModel: WordViewModel by viewModels {
        viewModelFactory
    }
    private lateinit var binding: FragmentWordListBinding
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_word_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
```

La delegación `by lazy` nos permite crear una incialización diferida, justo en el momento que se necesite. En cuanto a la inicialización del ViewModel pasaremos en el callback de `by viewModels` la fabrica que es capaz de construir nuestro ViewModel.

En el método `onCreteView` simplemente inflamos la interfaz utilizando DataBinding.

# Item UI

Si bien ya configuramos donde se mostrara los lista de palabras aun no hemos implementado la forma de conectar los datos con la vista, para lograrlo es necesario definir el XML que representara a cada elemento y un adaptador para conectar el RecyclerView y los datos.

Iniciaremos con el mas sencillo y es definir el estilo de los elemento de la lista en el archivo `item_word.xml` 

```z
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>

        <variable
            name="word"
            type="com.naldana.dummydictionary.model.Word" />
    </data>

    <androidx.cardview.widget.CardView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:cardUseCompatPadding="true">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:padding="@dimen/default_gap">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="@{word.word}"
                android:textAppearance="@style/TextAppearance.MaterialComponents.Headline6"
                tools:text="Palabra" />

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="@dimen/small_gap"
                android:layout_marginTop="@dimen/small_gap"
                android:text="@{word.definition}"
                android:textAppearance="@style/TextAppearance.MaterialComponents.Body2"
                tools:text="Definición de la palabra" />
        </LinearLayout>
    </androidx.cardview.widget.CardView>
</layout>
```

Como podrás notar se utilizara databading para enlazar los elementos visuales con su datos, par reducir un poco de código en el adaptador.

# Adapter

Un Adaptador para recycler view debe heredar la clase abstracta `RecyclerView.Adapter` que nos obliga a definir tres métodos importantes en ciclo de reciclaje de cada elemento visual y también solicita el contenedor de vistas o ViewHolder.

Para definir adecuadamente cada parte de estos crear una clase con el nombre de `WordAdapter` que herede de la clase `RecyclerView.Adapter`, esta te solicitara el tipo de dato que define al `ViewHolder` a utilizar. Que lo definiremos como una clase interna del Adaptador con el nombre de WordViewHolder

```
class WordAdapter : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {
  
    inner class WordViewHolder() : RecyclerView.ViewHolder() {

    }
    
}
```

Notaras dos errores, lo solucionaremos en seguida, el primero es que al heredar de `RecyclerView.ViewHolder` solicita el View que sera reciclado, como utilizaremos **DataBinding** en el constructor de WordAdapter recibiremos un objeto de tipo `ItemWordBinding` que ha sido generado por la librería a partir de `item_word.xml`. De este sacaremos la View Solicitada por `RecyclerView.ViewHolder` 


```
inner class WordViewHolder(private val binding: ItemWordBinding) : RecyclerView.ViewHolder(binding.root) {
    
}
```

Ahora solo hace falta agregar un método que nos permita enlazar esta vista con un dato, para eso definiremos el método bind en `WordViewHolder` para que se pueda actualizar el contenido según lo requiera `RecyclerView`

```
inner class WordViewHolder(private val binding: ItemWordBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(word: Word) {
        binding.word = word
        binding.executePendingBindings()
    }
}

```
con `binding.word = word` pasamos la variable hasta nuestro xml y con `binding.executePendingBindings()` ejecutamos la actualización el valor de todos los elementos que funcionan con DataBinding.

Aun nos queda un error que solucionar, ya que es necesario sobre escribir tres métodos importantes en el adaptador que son

```
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
      TODO("Not yet implemented")
  }

  override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
      TODO("Not yet implemented")
  }

  override fun getItemCount(): Int {
      TODO("Not yet implemented")
  }
```

`RecyclerView` ejecutara el método `onCreateViewHolder` cuando considere necesario crear un contenedor de vistas según la características de la pantalla o la cantidad de datos.

`RecyclerView` ejecutara el método `onBindViewHolder` cuando necesite enlazar un dato de la lista a un ViewHolder, proporcionando el ViewHolder y la posicion del dato en la lista

`RecyclerView` ejecutara el método `getItemCount` para obtener la cantidad de elementos en la lista, para saber cuantos ViewHolder crear.

Para implementar estos métodos primero tendremos que añadir el atributo `words` que contendrá la lista de palabras y un método `setData` para poder cambiar el conjunto de datos, cuando sea necesario, y notificar al `RecyclerView` que los datos cambiaron.

```
private var words: List<Word>? = null

fun setData(data: List<Word>) {
    words = data
    notifyDataSetChanged()
}
```

Con esto podremos implementar el método `getItemCount` para que retorne 0 cuando no hay lista o el tamaño de la lista

```
override fun getItemCount() = words?.size ?: 0
```

Lo siguiente a implementar es como se debe crear un ViewHolder en el método `onCreateViewHolder` Creando una instancia en inflando la vista utilizando databinding, en este caso a diferencia de este procedimiento en el fragment necesitamos obtener el inflater con `LayoutInflater.from(parent.context)` 

```
override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    WordViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_word,
            parent,
            false
        )
    )
```

Finalmente necesitamos `onBindViewHolder` la forma de enlazar los datos con el ViewHolder que el `RecyclerView` quiera usar con un elemento de la lista

```
override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
    words?.let {  
        holder.bind(it[position])
    }
}
```
# Todo junto

Ya que tenemos programado el adaptador y en el ViewModel la forma de obtener los datos, en el fragmento configuraremos como se deben unir en el método `onViewCreated` para necesitamos la referencia al RecyclerView que ubicamos en el fragmento, que lo obtendremos desde la variable `binding` luego le configuraremos el adaptador que debe utilizar y el layoutManager, solamente para que sirva de ejemplo de como se debe hacer desde código, ya que estaba configurado desde el XML.

```
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val wordListRecyclerView = binding.wordListRecyclerView
    val wordAdapter = WordAdapter()
    wordListRecyclerView.apply {
        adapter = wordAdapter
    }
}
```

Además necesitamos suscribirnos al LiveData que nos notificara de todos los cambios en la lista de palabras para eso escribiremos el siguiente código

```
viewModel.words.observe(viewLifecycleOwner) { data ->
    wordAdapter.setData(data)
}
```

Lo que estamos diciendo, mientras que el estemos en el ciclo de vida visible del fragmento seremos notificados de cualquier cambio en la lista, y con esa lista le diremos al adapter que actualize los datos en el `RecycleView`

# Navegación

Si bien ya esta todo configurado para que el fragmento funcione, no hace falta configurar la navegación de la aplicación, para eso necesitamos configurar un archivo de navigation llamado `nav_graph.xml` en el cual hay que añadir el fragmento `WordListFragment` como destino principal. El código de este archivo queda así

```
<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/nav_graph"
    app:startDestination="@id/wordListFragment">
    <fragment
        android:id="@+id/wordListFragment"
        android:name="com.naldana.dummydictionary.ui.word.WordListFragment"
        tools:layout="@layout/fragment_word_list"
        android:label="WordListFragment" />
</navigation>
```

y el contenido de `activity_main` sera el siguiente

```
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <androidx.fragment.app.FragmentContainerView
        android:id="@+id/nav_host_fragment"
        android:name="androidx.navigation.fragment.NavHostFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:defaultNavHost="true"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:navGraph="@navigation/nav_graph" />


</androidx.constraintlayout.widget.Constraint
```

Con esto al ejecutar la aplicación veremos en pantalla la palabra y su definición# practica-de-laboratorio-1-dgasteazoro
