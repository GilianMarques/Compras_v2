package dev.gmarques.compras.ui.products

import android.animation.ValueAnimator
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.VectorDrawable
import android.os.Bundle
import android.text.Spanned
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.AnticipateInterpolator
import android.widget.LinearLayout
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.google.android.material.chip.Chip
import dev.gmarques.compras.R
import dev.gmarques.compras.data.model.Category
import dev.gmarques.compras.data.model.Product
import dev.gmarques.compras.data.model.ShopList
import dev.gmarques.compras.databinding.ActivityProductsBinding
import dev.gmarques.compras.domain.utils.ExtFun.Companion.currencyToDouble
import dev.gmarques.compras.domain.utils.ExtFun.Companion.dp
import dev.gmarques.compras.domain.utils.ExtFun.Companion.formatHtml
import dev.gmarques.compras.domain.utils.ExtFun.Companion.hideKeyboard
import dev.gmarques.compras.domain.utils.ExtFun.Companion.observeOnce
import dev.gmarques.compras.domain.utils.ExtFun.Companion.toCurrency
import dev.gmarques.compras.ui.Vibrator
import dev.gmarques.compras.ui.add_edit_product.AddEditProductActivity
import dev.gmarques.compras.ui.categories.CategoriesActivity
import dev.gmarques.compras.ui.main.BsdAddOrEditShopList
import dev.gmarques.compras.ui.suggest_products.SuggestProductsActivity
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductsActivity : AppCompatActivity(), ProductAdapter.Callback {

    private lateinit var viewModel: ProductsActivityViewModel
    private lateinit var binding: ActivityProductsBinding
    private lateinit var rvAdapter: ProductAdapter
    private var fabHidden: Boolean = false


    companion object {
        private const val LIST_ID = "list_id"
        private const val SUGGEST_PRODUCTS = "suggest_products"

        fun newIntent(context: Context, list: String, suggestProducts: Boolean = false): Intent {
            return Intent(context, ProductsActivity::class.java).apply {
                putExtra(LIST_ID, list)
                putExtra(SUGGEST_PRODUCTS, suggestProducts)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val shopListId = intent.getStringExtra(LIST_ID)!!
        val suggestProducts = intent.getBooleanExtra(SUGGEST_PRODUCTS, false)

        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[ProductsActivityViewModel::class.java]
        viewModel.init(shopListId)

        initToolbar()
        initRecyclerView()
        initSearch()
        initFabAddProduct()
        observeProductsUpdates()
        observePrices()
        observeShopList()
        setupOnBackPressed()
        observeCategories()
        if (suggestProducts) startActivitySuggestProduct()


    }

    private fun observeCategories() {
        viewModel.listCategoriesLD.observe(this) { categories ->
            categories?.let { updateCategoriesOnUi(categories.toList()) }
        }
    }

    private fun updateCategoriesOnUi(categories: List<Category>) = binding.apply {
        cgCategories.removeAllViews()
        Log.d("USUK", "ProductsActivity.".plus("updateCategoriesOnUi() "))
        val layoutParams =
            LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply { marginStart = 4.dp(); marginEnd = 4.dp() }

        lifecycleScope.launch {
            categories.forEachIndexed { index, category ->
                val chip = Chip(this@ProductsActivity)
                chip.layoutParams = layoutParams
                cgCategories.postDelayed({ cgCategories.addView(chip) }, (100 * index).toLong())

                chip.text = category.name
                chip.chipIcon = AppCompatResources.getDrawable(this@ProductsActivity, R.drawable.vec_category)
                (chip.chipIcon?.mutate() as? VectorDrawable)?.setTint(category.color)
                chip.isCheckable = true  // Torna o Chip selecionável
                chip.isCheckedIconVisible = true  // Exibe o ícone de seleção quando o Chip é selecionado
                if (viewModel.filterCategory == category) chip.isChecked = true
                chip.setOnClickListener {
                    Vibrator.interaction()
                    viewModel.filterByCategory(category)
                    hscCategories.smoothScrollTo(chip.x.toInt() - chip.width, 0)
                }
            }
        }
    }

    private fun observePrices() = viewModel.pricesLD.observe(this) {
        binding.apply {


            ValueAnimator.ofFloat(tvPriceList.text.toString().currencyToDouble().toFloat(), it.first.toFloat()).apply {
                interpolator = AnticipateInterpolator()
                duration = 500
                addUpdateListener {
                    lifecycleScope.launch {
                        withContext(Main) {
                            tvPriceList.text = it.animatedValue.toString().toDouble().toCurrency()
                        }
                    }
                }
            }.start()


            ValueAnimator.ofFloat(tvPriceCart.text.toString().currencyToDouble().toFloat(), it.second.toFloat()).apply {
                interpolator = AnticipateInterpolator()
                duration = 500
                addUpdateListener {
                    lifecycleScope.launch {
                        withContext(Main) {
                            tvPriceCart.text = it.animatedValue.toString().toDouble().toCurrency()
                        }
                    }
                }
            }.start()

        }
    }

    private fun observeShopList() = viewModel.shopListLD.observe(this) {
        binding.toolbar.tvActivityTitle.text = it?.name
    }

    private fun initSearch() {

        binding.edtSearch.doOnTextChanged { text, _, _, _ ->

            val term = text.toString()
            viewModel.searchProduct(term)
            binding.ivClearSearch.visibility = if (term.isEmpty()) GONE else VISIBLE
        }

        binding.ivClearSearch.setOnClickListener {
            binding.edtSearch.setText("")
            binding.edtSearch.hideKeyboard()
        }

    }

    private fun setupOnBackPressed() {
        onBackPressedDispatcher.addCallback {
            if (binding.edtSearch.text.isNullOrEmpty()) {
                finish()
            } else {
                binding.edtSearch.setText("")
                binding.edtSearch.hideKeyboard()
            }
        }
    }

    private fun initToolbar() {
        binding.toolbar.ivGoBack.setOnClickListener { this.onBackPressedDispatcher.onBackPressed() }

        binding.toolbar.ivMenu.setOnClickListener {
            showMenuDialog()
        }

    }

    private fun observeProductsUpdates() {
        viewModel.productsLD.observe(this) { newData ->
            rvAdapter.submitList(newData)

            if (binding.edtSearch.text.toString().isNotEmpty()) {
                when (newData.isEmpty()) {
                    true -> Vibrator.error()
                    false -> Vibrator.success()
                }
            }
        }
    }

    private fun initRecyclerView() {
        rvAdapter = ProductAdapter(this@ProductsActivity)

        val dragDropHelper = ProductDragDropHelperCallback(rvAdapter)
        val touchHelper = ItemTouchHelper(dragDropHelper)
        rvAdapter.attachItemTouchHelper(touchHelper)

        touchHelper.attachToRecyclerView(binding.rvProducts)

        binding.rvProducts.layoutManager = LinearLayoutManager(this)
        binding.rvProducts.adapter = rvAdapter
    }

    private fun initFabAddProduct() = binding.apply {

        fabAddProduct.setOnClickListener {
            startActivityAddProduct()
        }
        rvProducts.addOnScrollListener(object : OnScrollListener() {


            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {  // Rolando para cima - Esconde o FAB
                    if (fabHidden) return

                    fabAddProduct.animate().translationY(fabAddProduct.height.toFloat() * 2).alpha(0f).setStartDelay(100)
                        .setDuration(200L).start()
                    fabHidden = true

                } else if (dy < 0) { // Rolando para baixo - Mostra o FAB
                    if (!fabHidden) return

                    fabAddProduct.animate().translationY(0f).alpha(1f).setStartDelay(100).setDuration(200L).start()
                    fabHidden = false
                }
            }
        })
    }

    private fun startActivityAddProduct() {
        viewModel.shopListLD.observeOnce(this@ProductsActivity) { shopList ->

            Vibrator.interaction()
            val intent = AddEditProductActivity.newIntentAddProduct(this@ProductsActivity, shopList!!.id)
            startActivity(intent)
        }
    }

    private fun startActivitySuggestProduct() {
        Vibrator.interaction()
        val intent = SuggestProductsActivity.newIntent(this@ProductsActivity, viewModel.shopListLD.value!!.id)
        startActivity(intent)

    }

    private fun startActivityEditProduct(product: Product) {
        viewModel.shopListLD.observeOnce(this@ProductsActivity) { shopList ->

            Vibrator.interaction()
            val intent = AddEditProductActivity.newIntentEditProduct(this@ProductsActivity, shopList!!.id, product.id)
            startActivity(intent)
        }
    }

    private fun showMenuDialog() {
        viewModel.shopListLD.observeOnce(this) {
            Vibrator.interaction()

            BsdShopListMenu.Builder(this, it!!)
                .setRenameListener { renameList -> showRenameDialog(renameList) }
                .setSortListener { showSortProductsDialog() }
                .setSuggestionListener { startActivitySuggestProduct() }
                .setRemoveListener { removeList -> confirmRemove(removeList) }
                .setManageCategoriesListener { startCategoriesActivity() }
                .build().show()
        }
    }

    private fun startCategoriesActivity() {

        Vibrator.interaction()
        val intent = CategoriesActivity.newIntent(this@ProductsActivity)
        startActivity(intent)
    }

    private fun showSortProductsDialog() {
        BsdSortProducts(this) {
            viewModel.loadSortPreferences()
            //força os dados a serem recarregados, filtrados e ordenados
            viewModel.searchProduct("a")
            viewModel.searchProduct("")
        }.show()
    }

    private fun showRenameDialog(renameList: ShopList) {
        BsdAddOrEditShopList(this, renameList).setOnConfirmListener { shopList ->
            viewModel.addOrUpdateShopList(shopList)
        }.show()
    }

    private fun confirmRemove(shopList: ShopList) {
        val msg: Spanned = String.format(getString(R.string.Deseja_mesmo_remover_x), shopList.name).formatHtml()

        val dialogBuilder = AlertDialog.Builder(this).setTitle(getString(R.string.Por_favor_confirme)).setMessage(msg)
            .setPositiveButton(getString(R.string.Remover)) { dialog, _ ->
                lifecycleScope.launch {
                    viewModel.removeShopList(shopList)
                    dialog.dismiss()
                    finish()
                }
            }.setNegativeButton(getString(R.string.Cancelar)) { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun confirmRemove(product: Product) {
        val msg: Spanned = String.format(getString(R.string.Deseja_mesmo_remover_x), product.name).formatHtml()

        val dialogBuilder = AlertDialog.Builder(this).setTitle(getString(R.string.Por_favor_confirme)).setMessage(msg)
            .setPositiveButton(getString(R.string.Remover)) { dialog, _ ->
                viewModel.removeProduct(product)
                dialog.dismiss()
            }.setNegativeButton(getString(R.string.Cancelar)) { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    override fun rvProductsOnDragAndDrop(toPosition: Int, product: Product) {
       viewModel.updateProductPosition(product,toPosition)
    }

    override fun rvProductsOnEditItemClick(product: Product) {

        BsdEditProductPriceOrQuantity
            .Builder()
            .setActivity(this@ProductsActivity)
            .setProduct(product)
            .setEditListener {
                startActivityEditProduct(it)

            }
            .setRemoveListener {
                confirmRemove(it)

            }
            .setConfirmListener {
                viewModel.updateProductAsIs(it)
            }
            .build().show()
    }

    override fun rvProductsOnBoughtItemClick(product: Product, isBought: Boolean) {
        viewModel.updateProductBoughtState(product, isBought)
    }
}