package com.ucopdesoft.issuelogger.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ucopdesoft.issuelogger.databinding.ComplaintCommentLayoutBinding
import com.ucopdesoft.issuelogger.models.Comment
import com.ucopdesoft.issuelogger.utils.Constants.Companion.usersList
import java.text.SimpleDateFormat
import java.util.Locale

class ComplaintCommentsAdapter(private val list: List<Comment>, private val userId: String) :
    RecyclerView.Adapter<ComplaintCommentsAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ComplaintCommentLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment) {

            if (comment.userId == userId) {
                bindUserData(comment)
            } else {
                bindOtherUserData(comment)
            }

        }

        private fun bindUserData(comment: Comment) {
            binding.apply {

                otherUserCommentLay.visibility = View.GONE
                userCommentLay.visibility = View.VISIBLE

                userIdTv2.text = usersList.find { it.first == comment.userId }!!.second.userName

                commentTv2.text = comment.comment

                dateTv2.text = SimpleDateFormat(
                    "d MMMM, yyyy K:mm aa", Locale.getDefault()
                ).format(comment.date)
            }
        }

        private fun bindOtherUserData(comment: Comment) {
            binding.apply {

                userCommentLay.visibility = View.GONE
                otherUserCommentLay.visibility = View.VISIBLE

                userIdTv1.text = usersList.find { it.first == comment.userId }!!.second.userName

                commentTv1.text = comment.comment

                dateTv1.text = SimpleDateFormat(
                    "d MMMM, yyyy K:mm aa", Locale.getDefault()
                ).format(comment.date)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ComplaintCommentLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }
}