package com.aradevs.storagemanager

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.aradevs.storagemanager.dao.DatabaseDao
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(
    entities = [ProductEntity::class, CartEntity::class, PurchasesEntity::class],
    version = 2
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDatabaseDao(): DatabaseDao

    @DelicateCoroutinesApi
    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val lock = Any()
        private const val DATABASE_NAME = "app_db"

        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: getDatabase(context).also {
                instance = it
                GlobalScope.launch {
                    seedProducts(context)
                }
            }
        }

        @Synchronized
        fun getDatabase(context: Context): AppDatabase = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration().addCallback(object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Thread {
                    invoke(context)
                }.start()
            }

            override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                super.onDestructiveMigration(db)
                Thread {
                    invoke(context)
                }.start()
            }
        }).build()

        private suspend fun seedProducts(context: Context) {
            val db = invoke(context)
            val products = listOf(
                ProductEntity(0,
                    "Amoxicilina",
                    "Amoxicilina est?? indicado para el tratamiento de las siguientes infecciones en adultos y ni??os: Sinusitis bacteriana aguda, otitis media aguda, amigdalitis y faringitis estreptoc??cica aguda, exacerbaci??n aguda de bronquitis cr??nica, neumon??a adquirida en la comunidad, cistitis aguda, bacteriuria asintom??tica en el embarazo, pielonefritis aguda, fiebre tifoidea y paratifoidea, abscesos dentales con celulitis diseminada, infecci??n prot??sica articular, erradicaci??n de Helicobacter pylori, enfermedad de Lyme.",
                    6.80,
                    "https://www.farmaciaseconomicaselsalvador.com/PROD/SERV_ADMIN_FILES/Archivos/Imagenes/Articulos_MED/00041225_MED.jpg"),
                ProductEntity(0,
                    "Acetaminofen",
                    "Para el alivio seguro y r??pido de dolores artr??ticos y reum??ticos dolores musculares neuralgia bursitis.",
                    9.55,
                    "https://www.farmaciaseconomicaselsalvador.com/PROD/SERV_ADMIN_FILES/Archivos/Imagenes/Articulos_MED/00022040_MED.jpg"),
                ProductEntity(0,
                    "Dramamine",
                    "Prevenci??n y tratamiento de los s??ntomas asociados al mareo por locomoci??n mar??tima, terrestre o a??rea, tales como n??useas, v??mitos y/o v??rtigos para adultos y ni??os a partir de 2 a??os.",
                    1.72,
                    "https://www.farmaciaseconomicaselsalvador.com/PROD/SERV_ADMIN_FILES/Archivos/Imagenes/Articulos_MED/0110006_MED.jpg"),
                ProductEntity(0,
                    "Alcohol 90",
                    "Alcohol etilico en galon",
                    14.25,
                    "https://www.farmaciaseconomicaselsalvador.com/PROD/SERV_ADMIN_FILES/Archivos/Imagenes/Articulos_MED/00036627_MED.jpg"),
                ProductEntity(0,
                    "Alcohol Gel",
                    "Desinfectante de manos en gel.",
                    10.99,
                    "https://www.farmaciaseconomicaselsalvador.com/PROD/SERV_ADMIN_FILES/Archivos/Imagenes/Articulos_MED/00035699--5061_MED.png"),
                ProductEntity(0,
                    "Zorritone",
                    "Alivia la garganta irritada asociada a tos, resfriados y gripe.",
                    0.55,
                    "https://www.farmaciaseconomicaselsalvador.com/PROD/SERV_ADMIN_FILES/Archivos/Imagenes/Articulos_MED/00043786--5092_MED.png"),
                ProductEntity(0,
                    "Gargantinas",
                    "Expectorante, antis??ptico, antitusivo",
                    0.52,
                    "https://www.farmaciaseconomicaselsalvador.com/PROD/SERV_ADMIN_FILES/Archivos/Imagenes/Articulos_MED/00017329-(1)_MED.jpg"),
                ProductEntity(0,
                    "Vick Vaporub",
                    "Para resfriados de cabeza y pecho: Fr??tese al Vaporub sobre la garganta, pecho y espalda y c??brase con pa??o tibio.",
                    1.26,
                    "https://www.farmaciaseconomicaselsalvador.com/PROD/SERV_ADMIN_FILES/Archivos/Imagenes/Articulos_MED/0030023_MED.jpg"),
                ProductEntity(0,
                    "Loratadina",
                    "Est?? indicado para el tratamiento coadyuvante de la combinaci??n de loratadina con un agente corticosteroide sist??mico para el alivio de los s??ntomas severos de la dermatitis at??pica, angioedema, urticaria, rinitis alergia estacional y perenne, reacciones al??rgicas alimentarias y medicamentosas, dermatitis por contacto al??rgica y manifestaciones oculares de tipo al??rgico, tales como conjuntivis al??rgica.",
                    10.26,
                    "https://www.farmaciaseconomicaselsalvador.com/PROD/SERV_ADMIN_FILES/Archivos/Imagenes/Articulos_MED/00040334_MED.jpg"),
                ProductEntity(0,
                    "Pepto Bismol",
                    "Para alivio de las molestias estomacales m??s comunes del est??mago: Indigesti??n, diarrea, malestar estomacal, acidez y n??useas",
                    1.14,
                    "https://www.farmaciaseconomicaselsalvador.com/PROD/SERV_ADMIN_FILES/Archivos/Imagenes/Articulos_MED/00009713_MED.jpg"),
            )
            products.forEach {
                db.getDatabaseDao().saveProduct(it)
            }
        }
    }
}