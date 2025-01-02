package dev.gmarques.compras.data.model

import dev.gmarques.compras.App
import dev.gmarques.compras.R.string.Insira_um_preco_valido
import dev.gmarques.compras.R.string.Insira_uma_quantidade_valida
import dev.gmarques.compras.R.string.O_nome_deve_ter_no_m_nimo_x_caracteres
import dev.gmarques.compras.R.string.O_nome_deve_ter_no_m_ximo_x_caracteres
import dev.gmarques.compras.R.string.O_nome_nao_pode_ficar_vazio
import dev.gmarques.compras.R.string.Os_detalhes_devem_ter_ate_x_caracteres
import dev.gmarques.compras.utils.ExtFun.Companion.removeSpaces
import java.util.Locale
import java.util.UUID

data class Product(
    val shopListId: String,
    val categoryId: String,
    val name: String,
    val position: Int = -1,
    val price: Double,
    val quantity: Int,
    val info: String,
    val hasBeenBought: Boolean = false, // isBought estava dando problema com o firebase
    val id: String = getNewId(),
    val creationDate: Long = System.currentTimeMillis(),
) {

    @Suppress("unused")  // necessario pra uso com firebase
    constructor() : this("", "", "not_initialized", -1, 0.0, 0, "not_initialized", false)


    /**
     * Garante que nenhum produto entre no banco de dados sem atender às regras de negócio
     */
    fun selfValidate(): Product {

        val resultValidateName = Validator.validateName(name)
        if (resultValidateName.isFailure) throw Exception("Nome do produto é invalido: '${name}' -> ${resultValidateName.exceptionOrNull()!!.message}'")

        val resultValidatePrice = Validator.validatePrice(price)
        if (resultValidatePrice.isFailure) throw Exception("Preço do produto é invalido '${price}' -> ${resultValidatePrice.exceptionOrNull()!!.message}'")

        val resultValidateQuantity = Validator.validateQuantity(quantity)
        if (resultValidateQuantity.isFailure) throw Exception("Quantidade do produto é invalida '${quantity}' -> ${resultValidateQuantity.exceptionOrNull()!!.message}'")

        val resultValidateInfo = Validator.validateInfo(info)
        if (resultValidateInfo.isFailure) throw Exception("Info do produto é invalida '${info}' -> ${resultValidateInfo.exceptionOrNull()!!.message}'")

        val resultValidateShopListId = Validator.validateShopListId(shopListId)
        if (resultValidateShopListId.isFailure) throw Exception("Id da lista associada ao produto é invalida '${shopListId}' -> ${resultValidateShopListId.exceptionOrNull()!!.message}'")

        val resultValidateCategoryId = Validator.validateCategoryId(categoryId)
        if (resultValidateCategoryId.isFailure) throw Exception("Id da categoria associada ao produto é invalida '${categoryId}' -> ${resultValidateCategoryId.exceptionOrNull()!!.message}'")


        return this
    }

    fun withNewId(): Product = this.copy(id = getNewId())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        if (shopListId != other.shopListId) return false
        if (categoryId != other.categoryId) return false
        if (name != other.name) return false
        if (position != other.position) return false
        if (price != other.price) return false
        if (quantity != other.quantity) return false
        if (info != other.info) return false
        if (hasBeenBought != other.hasBeenBought) return false
        if (id != other.id) return false
        if (creationDate != other.creationDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = shopListId.hashCode()
        result = 31 * result + categoryId.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + position
        result = 31 * result + price.hashCode()
        result = 31 * result + quantity
        result = 31 * result + info.hashCode()
        result = 31 * result + hasBeenBought.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + creationDate.hashCode()
        return result
    }

    override fun toString(): String {
        return "Product(shopListId='$shopListId', categoryId='$categoryId', name='$name', position=$position, price=$price, quantity=$quantity, info='$info', hasBeenBought=$hasBeenBought, id='$id', creationDate=$creationDate)"
    }

    companion object {
        private fun getNewId(): String {
            return UUID.randomUUID().toString()
        }
    }

    class Validator {
        companion object {

            const val MIN_QUANTITY = 1
            const val MAX_QUANTITY = 99
            private const val MAX_CHARS_INFO = 50
            private const val MAX_CHARS_NAME = 30
            private const val MIN_CHARS_NAME = 3
            private const val MAX_PRICE = 9_999.99
            private const val MIN_PRICE = 0.1

            fun validateName(input: String): Result<String> {
                return when {
                    input.isEmpty() -> Result.failure(
                        Exception(
                            App.getContext().getString(O_nome_nao_pode_ficar_vazio)
                        )
                    )

                    input.length < MIN_CHARS_NAME -> Result.failure(
                        Exception(
                            String.format(App.getContext().getString(O_nome_deve_ter_no_m_nimo_x_caracteres), MIN_CHARS_NAME)
                        )
                    )

                    input.length > MAX_CHARS_NAME -> Result.failure(
                        Exception(
                            String.format(App.getContext().getString(O_nome_deve_ter_no_m_ximo_x_caracteres), MAX_CHARS_NAME)
                        )
                    )

                    else -> {
                        Result.success(input.removeSpaces().replaceFirstChar { it.titlecase(Locale.getDefault()) })
                    }
                }
            }

            fun validateInfo(input: String): Result<String> {

                return when {

                    input.length > MAX_CHARS_INFO -> Result.failure(
                        Exception(
                            String.format(App.getContext().getString(Os_detalhes_devem_ter_ate_x_caracteres), MAX_CHARS_INFO)
                        )
                    )

                    else -> {
                        Result.success(input.removeSpaces().replaceFirstChar { it.titlecase(Locale.getDefault()) })
                    }
                }

            }

            fun validatePrice(input: Double): Result<Double> {

                return when {

                    input !in MIN_PRICE..MAX_PRICE -> Result.failure(
                        Exception(
                            (App.getContext().getString(Insira_um_preco_valido))
                        )
                    )

                    else -> Result.success(input)
                }

            }

            fun validateQuantity(input: Int): Result<Int> {

                return when {

                    input !in MIN_QUANTITY..MAX_QUANTITY -> Result.failure(
                        Exception(
                            (App.getContext().getString(Insira_uma_quantidade_valida))
                        )
                    )

                    else -> Result.success(input)
                }

            }

            fun validateShopListId(shopListId: String): Result<String> {
                return when {
                    shopListId.isEmpty() -> Result.failure(Exception("O id da lista não pode ser vazio"))
                    shopListId.length < 3 -> Result.failure(Exception("O id da lista parece ser inválido"))
                    else -> Result.success(shopListId)
                }
            }

            fun validateCategoryId(categoryId: String): Result<String> {
                return when {
                    categoryId.isEmpty() -> Result.failure(Exception("O id da categoria não pode ser vazio"))
                    categoryId.length < 3 -> Result.failure(Exception("O id da categoria parece ser inválido"))
                    else -> Result.success(categoryId)
                }
            }

        }
    }

}