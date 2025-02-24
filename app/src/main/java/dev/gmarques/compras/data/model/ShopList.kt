package dev.gmarques.compras.data.model

import android.content.Context
import dev.gmarques.compras.R.string.A_cor_nao_pode_ficar_vazia
import dev.gmarques.compras.R.string.O_nome_deve_ter_no_m_nimo_x_caracteres
import dev.gmarques.compras.R.string.O_nome_deve_ter_no_m_ximo_x_caracteres
import dev.gmarques.compras.R.string.O_nome_nao_pode_ficar_vazio
import dev.gmarques.compras.domain.utils.ExtFun.Companion.removeSpaces
import java.io.Serializable
import java.util.Locale
import java.util.UUID


data class ShopList(
    val name: String,
    val color: Int,
    val id: String = getNewId(),
    val creationDate: Long = System.currentTimeMillis(),
) : Serializable {

    companion object {
        private fun getNewId(): String {
            return UUID.randomUUID().toString()
        }
    }

    /**
     * Garante que nenhuma lista entre no banco de dados sem atender às regras de negócio
     */
    fun selfValidate(context: Context): ShopList {

        val resultValidateName = Validator.validateName(name, context)
        if (resultValidateName.isFailure) throw Exception("Nome da lista é invalido: '${name}' -> ${resultValidateName.exceptionOrNull()!!.message}'")
        return this
    }

    @Suppress("unused") // necessario pra uso com firebase
    constructor() : this("not_initialized", 0)

    constructor(id: String) : this("", 0, id)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ShopList

        if (name != other.name) return false
        if (color != other.color) return false
        if (id != other.id) return false
        if (creationDate != other.creationDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + color
        result = 31 * result + id.hashCode()
        result = 31 * result + creationDate.hashCode()
        return result
    }

    override fun toString(): String {
        return "ShopList(name='$name', color=$color, id='$id', creationDate=$creationDate)"
    }

    class Validator {
        companion object {

            private const val MAX_CHARS = 30
            private const val MIN_CHARS = 3
            private const val EMPTY_COLOR = -1
            private const val COLOR_LENGTH_THRESHOLD = 2

            fun validateName(input: String, context: Context): Result<String> {
                return when {
                    input.isEmpty() -> Result.failure(
                        Exception(
                            context.getString(O_nome_nao_pode_ficar_vazio)
                        )
                    )

                    input.length < MIN_CHARS -> Result.failure(
                        Exception(
                            String.format(context.getString(O_nome_deve_ter_no_m_nimo_x_caracteres), MIN_CHARS)
                        )
                    )

                    input.length > MAX_CHARS -> Result.failure(
                        Exception(
                            String.format(context.getString(O_nome_deve_ter_no_m_ximo_x_caracteres), MAX_CHARS)
                        )
                    )

                    else -> {
                        Result.success(input.removeSpaces().replaceFirstChar { it.titlecase(Locale.getDefault()) })
                    }
                }
            }


            fun validateColor(input: Int, context: Context): Result<Int> {
                return when {
                    input == EMPTY_COLOR || input.toString().length <= COLOR_LENGTH_THRESHOLD -> Result.failure(
                        Exception(
                            context.getString(A_cor_nao_pode_ficar_vazia)
                        )
                    )

                    else -> {
                        Result.success(input)
                    }
                }
            }
        }
    }

}

