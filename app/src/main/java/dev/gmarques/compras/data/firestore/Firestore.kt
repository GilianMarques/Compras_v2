package dev.gmarques.compras.data.firestore

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dev.gmarques.compras.BuildConfig
import dev.gmarques.compras.data.repository.UserRepository

class Firestore {

    // TODO: ajustar regras de acesso no console do firebase ate 10/01/25 ou o app nao vai mais conseguir acessar os dados
    companion object {

        private val userRootPath = if (BuildConfig.DEBUG) "debug_database" else UserRepository.getUser()!!.email!!

        private const val DATABASE = "Database"
        private const val SHOP_LISTS = "ShopLists"
        private const val PRODUCTS = "Products"
        private const val CATEGORIES = "Categories"
        private const val SUGGESTION_PRODUCT = "Suggestion_products"

        val shopListCollection by lazy {
            Firebase.firestore.collection(userRootPath).document(DATABASE).collection(SHOP_LISTS)
        }

        val categoryCollection by lazy {
            Firebase.firestore.collection(userRootPath).document(DATABASE).collection(CATEGORIES)
        }

        val productCollection by lazy {
            Firebase.firestore.collection(userRootPath).document(DATABASE).collection(PRODUCTS)
        }

        val suggestionProductCollection by lazy {
            Firebase.firestore.collection(userRootPath).document(DATABASE).collection(SUGGESTION_PRODUCT)
        }
    }
}