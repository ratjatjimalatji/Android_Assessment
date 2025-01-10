import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.glucode.about_you.R
import com.glucode.about_you.databinding.ItemEngineerDetailsBinding
import com.glucode.about_you.engineers.models.Engineer
import com.glucode.about_you.utils.LocalStorageManager

class EngineersRecyclerViewAdapter(
    private var engineers: List<Engineer>,
    private val onClick: (Engineer) -> Unit
) : RecyclerView.Adapter<EngineersRecyclerViewAdapter.EngineerViewHolder>() {
    private lateinit var storageManager: LocalStorageManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EngineerViewHolder {
        storageManager = LocalStorageManager(parent.context)
        return EngineerViewHolder(
            ItemEngineerDetailsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount()= engineers.count()

    override fun onBindViewHolder(holder: EngineerViewHolder, position: Int) {
        holder.bind(engineers[position], onClick, storageManager)
    }

    inner class EngineerViewHolder(private val binding: ItemEngineerDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(engineer: Engineer, onClick: (Engineer) -> Unit, storageManager: LocalStorageManager) {
            binding.apply {
                name.text = engineer.name
                role.text = engineer.role
                txtYearsWorked.text = engineer.quickStats.years.toString()
                txtCoffeeCount.text = engineer.quickStats.coffees.toString()
                txtBugCount.text = engineer.quickStats.bugs.toString()

                // Load saved profile picture if available
                val savedProfilePic = storageManager.loadProfilePicture(engineer.name)
                if (savedProfilePic != null) {
                    profileImage.setImageBitmap(savedProfilePic)
                    profileImage.imageTintList = null
                } else {
                    // Load default image
                    val resourceId = root.context.resources.getIdentifier(
                        engineer.defaultImageName,
                        "drawable",
                        root.context.packageName
                    )
                    if (resourceId != 0) {
                        profileImage.setImageResource(resourceId)
                    } else {
                        profileImage.setImageResource(R.drawable.ic_person)
                    }
                }

                root.setOnClickListener { onClick(engineer) }
            }
        }
    }

    fun updateData(newEngineers: List<Engineer>) {
        engineers = newEngineers
        notifyDataSetChanged()
    }
}
