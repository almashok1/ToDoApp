package kz.adamant.todoapp.fragments.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kz.adamant.todoapp.data.models.ToDoData
import kz.adamant.todoapp.databinding.RowLayoutBinding


class ListAdapter : RecyclerView.Adapter<ListAdapter.ToDoViewHolder>() {
    private var dataList = emptyList<ToDoData>()

    class ToDoViewHolder(private val binding: RowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(toDoData: ToDoData) {
            binding.toDoData = toDoData
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ToDoViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowLayoutBinding.inflate(layoutInflater, parent, false)
                return ToDoViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        return ToDoViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(toDoDataList: List<ToDoData>) {
        val toDoDiffUtil = ToDoDiffUtil(dataList, toDoDataList)
        val toDoDiffResult = DiffUtil.calculateDiff(toDoDiffUtil)
        this.dataList = toDoDataList
        toDoDiffResult.dispatchUpdatesTo(this@ListAdapter)
    }

    fun getDataList() = this.dataList
}