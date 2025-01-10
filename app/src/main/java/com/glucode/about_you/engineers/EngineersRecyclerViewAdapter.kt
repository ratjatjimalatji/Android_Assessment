import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.glucode.about_you.databinding.ItemEngineerBinding
import com.glucode.about_you.databinding.ItemEngineerDetailsBinding
import com.glucode.about_you.engineers.models.Engineer

class EngineersRecyclerViewAdapter(
    private var engineers: List<Engineer>,
    private val onClick: (Engineer) -> Unit
) : RecyclerView.Adapter<EngineersRecyclerViewAdapter.EngineerViewHolder>() {
    override fun getItemCount() = engineers.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EngineerViewHolder {
        return EngineerViewHolder(
            ItemEngineerDetailsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: EngineerViewHolder, position: Int) {
        holder.bind(engineers[position], onClick)
    }

    inner class EngineerViewHolder(private val binding: ItemEngineerDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(engineer: Engineer, onClick: (Engineer) -> Unit) {
            binding.name.text = engineer.name
            binding.role.text = engineer.role
            binding.txtYearsWorked.text = engineer.quickStats.years.toString()
            binding.txtCoffeeCount.text = engineer.quickStats.coffees.toString()
            binding.txtBugCount.text =    engineer.quickStats.bugs.toString()

            // Add a click listener for the engineer item
            binding.root.setOnClickListener { onClick(engineer) }
        }
    }
    fun updateData(newEngineers: List<Engineer>) {
        engineers = newEngineers
        notifyDataSetChanged() // Refresh the RecyclerView
    }
}