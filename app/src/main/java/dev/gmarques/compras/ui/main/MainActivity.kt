package dev.gmarques.compras.ui.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseUser
import dev.gmarques.compras.App
import dev.gmarques.compras.R
import dev.gmarques.compras.data.firestore.Firestore
import dev.gmarques.compras.data.model.ShopList
import dev.gmarques.compras.data.repository.UserRepository
import dev.gmarques.compras.databinding.ActivityMainBinding
import dev.gmarques.compras.ui.Vibrator
import dev.gmarques.compras.ui.add_edit_shop_list.AddEditShopListActivity
import dev.gmarques.compras.ui.login.LoginActivity
import dev.gmarques.compras.ui.products.ProductsActivity
import dev.gmarques.compras.ui.profile.ProfileActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private val rvAdapter = ShopListAdapter(isDarkThemeEnabled(), ::rvItemClick)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        checkUserAuthenticated()
        initRecyclerView()
        initFabAddList()
        observeListsUpdates()

    }

    private fun isDarkThemeEnabled(): Boolean {
        val nightModeFlags =
            App.getContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }

    private fun checkUserAuthenticated() = lifecycleScope.launch {


        val user = UserRepository.getUser()

        if (user != null) {
            attUiWithUserData(user)
            setupDataBase()

        } else {
            startActivity(
                Intent(
                    applicationContext, LoginActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )

            this@MainActivity.finishAffinity()
        }
    }

    private suspend fun setupDataBase() = withContext(Dispatchers.IO) {

        Firestore.setupDatabase()
        viewModel.observeUpdates()
    }


    private fun attUiWithUserData(user: FirebaseUser) = binding.apply {
        binding.tvUserName.text = user.displayName

        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        tvGreetings.text = when (currentHour) {
            in 0..11 -> getString(R.string.Bom_dia)
            in 12..17 -> getString(R.string.Boa_tarde)
            else -> getString(R.string.Boa_noite)
        }

        user.photoUrl?.let { photoUrl ->
            Glide.with(root.context)
                .load(photoUrl)
                .circleCrop()
                .into(ivProfilePicture)
        } ?: run {
            // Esconde a imagem se não houver foto
            ivProfilePicture.visibility = View.GONE
            tvUserName.visibility = View.GONE
            tvGreetings.visibility = View.GONE
        }

        ivMenu.setOnClickListener {
            startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
        }
    }

    private fun initRecyclerView() {

        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = rvAdapter
    }

    private fun initFabAddList() = binding.fabAddList.setOnClickListener {

        startActivity(AddEditShopListActivity.newIntentAddShopList(this))
    }

    private fun observeListsUpdates() {

        viewModel.listsLiveData.observe(this@MainActivity) { newData ->
            if (!newData.isNullOrEmpty()) rvAdapter.submitList(newData)
        }
    }

    private fun rvItemClick(shopList: ShopList) {
        startActivityProducts(shopList, false)
    }

    private fun startActivityProducts(shopList: ShopList, suggestProducts: Boolean) {
        Vibrator.interaction()
        val intent = ProductsActivity.newIntent(this, shopList.id, suggestProducts)
        startActivity(intent)
    }
}



