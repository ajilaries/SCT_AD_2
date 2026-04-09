import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.material3.Button
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val taskList: MutableList<Task>,
    private val onEdit: (Int) -> Unit,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskText: TextView = view.findViewById(R.id.taskText)

        private fun findViewById(taskText: Any): TextView {}

        val editBtn: Button = view.findViewById(R.id.editBtn)
        val deleteBtn: Button = view.findViewById(R.id.deleteBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH & Any {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = taskList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.taskText.text = taskList[position].title

        holder.editBtn.setOnClickListener { onEdit(position) }
        holder.deleteBtn.setOnClickListener { onDelete(position) }
    }
}