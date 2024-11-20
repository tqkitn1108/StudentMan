package vn.edu.hust.studentman

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(val students: MutableList<StudentModel>,
                     val onStudentRemoved: (StudentModel, Int) -> Unit): RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
  class StudentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val textStudentName: TextView = itemView.findViewById(R.id.text_student_name)
    val textStudentId: TextView = itemView.findViewById(R.id.text_student_id)
    val imageEdit: ImageView = itemView.findViewById(R.id.image_edit)
    val imageRemove: ImageView = itemView.findViewById(R.id.image_remove)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
    val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_student_item,
       parent, false)
    return StudentViewHolder(itemView)
  }

  override fun getItemCount(): Int = students.size

  override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
    val student = students[position]

    holder.textStudentName.text = student.studentName
    holder.textStudentId.text = student.studentId

    holder.imageEdit.setOnClickListener {
      showEditDialog(holder.itemView.context, position)
    }

    holder.imageRemove.setOnClickListener {
      showDeleteConfirmationDialog(holder.itemView.context, position)
    }
  }

  private fun showEditDialog(context: android.content.Context, position: Int) {
    val student = students[position]
    val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_student, null)
    val editName = dialogView.findViewById<EditText>(R.id.edit_student_name)
    val editId = dialogView.findViewById<EditText>(R.id.edit_student_id)

    editName.setText(student.studentName)
    editId.setText(student.studentId)

    AlertDialog.Builder(context)
      .setView(dialogView)
      .setTitle("Edit Student")
      .setPositiveButton("Save") { _, _ ->
        students[position] = StudentModel(editName.text.toString(), editId.text.toString())
        notifyItemChanged(position)
      }
      .setNegativeButton("Cancel", null)
      .show()
  }

  private fun showDeleteConfirmationDialog(context: android.content.Context, position: Int) {
    AlertDialog.Builder(context)
      .setTitle("Delete Student")
      .setMessage("Are you sure you want to delete this student?")
      .setPositiveButton("Delete") { _, _ ->
        val removedStudent = students.removeAt(position)
        notifyItemRemoved(position)
        onStudentRemoved(removedStudent, position)
      }
      .setNegativeButton("Cancel", null)
      .show()
  }
}