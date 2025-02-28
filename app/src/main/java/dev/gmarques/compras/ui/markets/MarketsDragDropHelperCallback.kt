package dev.gmarques.compras.ui.markets

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class MarketDragDropHelperCallback(private val adapter: MarketAdapter) : ItemTouchHelper.Callback() {
    override fun isLongPressDragEnabled(): Boolean = false

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return makeMovementFlags(dragFlags, 0)
    }


    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {

        adapter.moveMarket(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    /**
     * Quando o usuario soltar a view que ta arrastando, rodo um for no range de views que sofreu alteraçao de posiçao pra
     * atualizar elas no DB
     */
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {

        adapter.updateDraggedMarkets()

        super.clearView(recyclerView, viewHolder)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Não implementado porque swipe não está sendo usado
    }
}
