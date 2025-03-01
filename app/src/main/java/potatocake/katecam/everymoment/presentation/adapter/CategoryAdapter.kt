package potatocake.katecam.everymoment.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import potatocake.katecam.everymoment.R
import potatocake.katecam.everymoment.data.model.network.dto.vo.Category
import potatocake.katecam.everymoment.databinding.CategoryLayoutBinding

class CategoryAdapter(private val context: Context, categoryList: List<Category>?) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var categoryList: List<Category>
    private var selectedCategories = mutableSetOf<String>()

    init {
        if (categoryList.isNullOrEmpty())
            this.categoryList = listOf()
        else
            this.categoryList = categoryList
    }

    fun restoreSelected(categories: List<String>) {
        selectedCategories = categories.toMutableSet()
        notifyDataSetChanged()
    }

    fun resetSelected() {
        selectedCategories = mutableSetOf()
        notifyDataSetChanged()
    }

    fun getSelectedCategories(): List<String> {
        return selectedCategories.toList()
    }

    inner class ViewHolder(private val binding: CategoryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Category, isSelected: Boolean) {
            binding.category.text = context.getString(R.string.category_text, item.categoryName)

            if (isSelected) {
                binding.category.setBackgroundResource(R.drawable.category_background)
                binding.category.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.white
                    )
                )
            } else {
                binding.category.setBackgroundResource(R.drawable.category_gray_background)
                binding.category.setTextColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.search_gray
                    )
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CategoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categoryList[position]
        holder.bind(category, selectedCategories.contains(category.categoryName))

        holder.itemView.setOnClickListener {
            val categoryName = categoryList[holder.adapterPosition].categoryName
            if (selectedCategories.contains(categoryName)) {
                selectedCategories.remove(categoryName)
            } else {
                selectedCategories.add(categoryName)
            }
            notifyItemChanged(holder.adapterPosition)
        }
    }
}