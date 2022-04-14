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
                    "Amoxicilina está indicado para el tratamiento de las siguientes infecciones en adultos y niños: Sinusitis bacteriana aguda, otitis media aguda, amigdalitis y faringitis estreptocócica aguda, exacerbación aguda de bronquitis crónica, neumonía adquirida en la comunidad, cistitis aguda, bacteriuria asintomática en el embarazo, pielonefritis aguda, fiebre tifoidea y paratifoidea, abscesos dentales con celulitis diseminada, infección protésica articular, erradicación de Helicobacter pylori, enfermedad de Lyme.",
                    6.80,
                    "https://www.farmaciaseconomicaselsalvador.com/PROD/SERV_ADMIN_FILES/Archivos/Imagenes/Articulos_MED/00041225_MED.jpg"),
                ProductEntity(0,
                    "Acetaminofen",
                    "Para el alivio seguro y rápido de dolores artríticos y reumáticos dolores musculares neuralgia bursitis.",
                    9.55,
                    "https://www.farmaciaseconomicaselsalvador.com/PROD/SERV_ADMIN_FILES/Archivos/Imagenes/Articulos_MED/00022040_MED.jpg"),
                ProductEntity(0,
                    "Dramamine",
                    "Prevención y tratamiento de los síntomas asociados al mareo por locomoción marítima, terrestre o aérea, tales como náuseas, vómitos y/o vértigos para adultos y niños a partir de 2 años.",
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
                    "Expectorante, antiséptico, antitusivo",
                    0.52,
                    "https://www.farmaciaseconomicaselsalvador.com/PROD/SERV_ADMIN_FILES/Archivos/Imagenes/Articulos_MED/00017329-(1)_MED.jpg"),
                ProductEntity(0,
                    "Vick Vaporub",
                    "Para resfriados de cabeza y pecho: Frótese al Vaporub sobre la garganta, pecho y espalda y cúbrase con paño tibio.",
                    1.26,
                    "https://www.farmaciaseconomicaselsalvador.com/PROD/SERV_ADMIN_FILES/Archivos/Imagenes/Articulos_MED/0030023_MED.jpg"),
                ProductEntity(0,
                    "Loratadina",
                    "Está indicado para el tratamiento coadyuvante de la combinación de loratadina con un agente corticosteroide sistémico para el alivio de los síntomas severos de la dermatitis atópica, angioedema, urticaria, rinitis alergia estacional y perenne, reacciones alérgicas alimentarias y medicamentosas, dermatitis por contacto alérgica y manifestaciones oculares de tipo alérgico, tales como conjuntivis alérgica.",
                    10.26,
                    "https://www.farmaciaseconomicaselsalvador.com/PROD/SERV_ADMIN_FILES/Archivos/Imagenes/Articulos_MED/00040334_MED.jpg"),
                ProductEntity(0,
                    "Pepto Bismol",
                    "Para alivio de las molestias estomacales más comunes del estómago: Indigestión, diarrea, malestar estomacal, acidez y náuseas",
                    1.14,
                    "https://www.farmaciaseconomicaselsalvador.com/PROD/SERV_ADMIN_FILES/Archivos/Imagenes/Articulos_MED/00009713_MED.jpg"),
            )
            products.forEach {
                db.getDatabaseDao().saveProduct(it)
            }
        }
    }
}