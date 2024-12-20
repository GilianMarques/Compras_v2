package dev.gmarques.compras.ui.main

import android.app.Activity
import com.google.android.material.bottomsheet.BottomSheetDialog
import dev.gmarques.compras.data.data.model.ShopList
import dev.gmarques.compras.databinding.BsdShoplistMenuDialogBinding

class BsdShopListMenu(
    targetActivity: Activity,
    private val shopList: ShopList,
    private var renameListener: ((ShopList) -> Unit),
    private var removeListener: ((ShopList) -> Unit),
) {

    private var binding = BsdShoplistMenuDialogBinding.inflate(targetActivity.layoutInflater)
    private val dialog: BottomSheetDialog = BottomSheetDialog(targetActivity)


    init {
        dialog.setContentView(binding.root)

        binding.apply {

            fabRemove.setOnClickListener {
                removeListener(shopList)
                dialog.dismiss()
            }

            fabRename.setOnClickListener {
                renameListener(shopList)
                dialog.dismiss()
            }

            tvTitle.text = shopList.name
        }

    }


    fun show() {

        dialog.show()

        // Acesse o comportamento do BottomSheet e defina o estado para expandido
        // val behavior = BottomSheetBehavior.from(binding.root.parent as View)
        // behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }


}
