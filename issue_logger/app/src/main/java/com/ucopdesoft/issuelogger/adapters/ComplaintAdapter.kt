package com.ucopdesoft.issuelogger.adapters

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ucopdesoft.issuelogger.R
import com.ucopdesoft.issuelogger.databinding.ComplaintsItemLayoutBinding
import com.ucopdesoft.issuelogger.listeners.OnComplaintClickListener
import com.ucopdesoft.issuelogger.models.Complaint
import com.ucopdesoft.issuelogger.models.User
import com.ucopdesoft.issuelogger.utils.ComplaintStatus
import com.ucopdesoft.issuelogger.utils.Constants.Companion.USERNAME
import com.ucopdesoft.issuelogger.utils.Tables
import java.text.SimpleDateFormat
import java.util.Locale


class ComplaintAdapter(
    private val context: Context,
    private val UId: String,
    private val complaintClickListener: OnComplaintClickListener
) : RecyclerView.Adapter<ComplaintAdapter.ViewHolder>() {

    private val list = ArrayList<Complaint>()
    private val listOfUser = ArrayList<Pair<String, User>>()
    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

    class DiffUtilCallback(
        private val oldList: List<Complaint>,
        private val newList: List<Complaint>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem.javaClass == newItem.javaClass
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    class UserDiffUtilCallback(
        private val oldList: List<Pair<String, User>>, private val newList: List<Pair<String, User>>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem.javaClass == newItem.javaClass
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    inner class ViewHolder( val binding: ComplaintsItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(complaint: Complaint) {

            binding.apply {
                val user = listOfUser.find {
                    it.first == complaint.userId
                }
                complaintUserName.text =
                    if (user?.second?.userName != null && !user.second.userName.equals("null")) user.second.userName
                    else complaint.userId
                USERNAME = complaintUserName.text.toString()
                if (complaint.complaintDescription!="")
                {
                    complaintDescription.visibility = View.VISIBLE
                complaintDescription.text = complaint.complaintDescription
                }
                ComplaintDate.text = if (complaint.date != 0L) {
                    ComplaintDate.visibility = View.VISIBLE
                    SimpleDateFormat(
                        "MMMM d, yyyy K:mm aa",
                        Locale.getDefault()
                    ).format(complaint.date)
                } else {
                    ComplaintDate.visibility = View.GONE
                    ""
                }

                val color = when (complaint.status) {
                    ComplaintStatus.ACTIVE.status -> {
                        Color.parseColor("#6750A4")
                    }

                    ComplaintStatus.REJECTED.status -> {
                        Color.parseColor("#FF0000")
                    }

                    ComplaintStatus.RESOLVED.status -> {
                        Color.parseColor("#0B9C1A")
                    }

                    else -> {
                        Color.BLACK
                    }
                }
                complaintStatus.setTextColor(color)
                progressIndicator.setColorFilter(color)
                complaintStatus.text = complaint.status


//
//                userNameTv.text =
//                    if (user?.second?.userName != null && !user.second.userName.equals("null")) user.second.userName
//                    else complaint.userId
//
//                complaintIdTv.text = Html.fromHtml(
//                    context.resources.getString(R.string.cid) + "<br><b>CId:</b> #" + complaint.complaintId.subSequence(
//                        0,
//                        17
//                    ), 0
//                )
//
//                if (complaint.complaintTitle != context.resources.getString(R.string.nullstr) && complaint.complaintTitle.isNotEmpty()) {
//                    complaintTitleTv.text = complaint.complaintTitle
//                    complaintTitleTv.visibility = View.VISIBLE
//                } else {
//                    complaintTitleTv.visibility = View.GONE
//                }
//
//                if (complaint.date != 0L) {
//                    datetv.text = SimpleDateFormat(
//                        "d MMMM, yyyy K:mm aa",
//                        Locale.getDefault()
//                    ).format(complaint.date)
//                    datetv.visibility = View.VISIBLE
//                } else {
//                    datetv.visibility = View.GONE
//                }
//
//                if (complaint.complaintDescription != context.resources.getString(R.string.nullstr) && complaint.complaintDescription.isNotEmpty()) {
//                    complaintDescriptionTv.text = complaint.complaintDescription
//                    complaintDescriptionTv.visibility = View.VISIBLE
//                } else {
//                    complaintDescriptionTv.visibility = View.GONE
//                }
//
//
//
//                commentsTv.setOnClickListener {
//                    complaintClickListener.onClick(complaint)
//                }
//
                val complaintClickListener1 = complaintClickListener
                root.setOnClickListener {
                    val downVoteCount = binding.downVoteCount.text.toString()
                    Log.d("down",downVoteCount)
                    val upvoteCount = binding.upvoteCount.text.toString()
                    complaintClickListener1.onClick(complaint, upvoteCount,downVoteCount)
                }


                upvoteButton.setOnClickListener {
                    if (upvoteButton.isChecked) {
                        downVoteButton.isChecked = false

                        updateValue(
                            complaint.complaintId,
                            Tables.LIKES.tableName,
                            Tables.DISLIKES.tableName
                        )
                    } else {
                        firebaseDatabase.reference.child(Tables.LIKES.tableName)
                            .child(list[adapterPosition].complaintId).child(UId).removeValue()

                    }
                }


                downVoteButton.setOnClickListener {
                    if (downVoteButton.isChecked) {
                        upvoteButton.isChecked = false
                       // binding.downvote.setTextColor(context.resources.getColor(R.color.downvote_red))
                       // binding.downVoteCount.setTextColor(context.resources.getColor(R.color.downvote_red))

                        updateValue(
                            complaint.complaintId,
                            Tables.DISLIKES.tableName,
                            Tables.LIKES.tableName
                        )

                    } else {
                        firebaseDatabase.reference.child(Tables.DISLIKES.tableName)
                            .child(list[adapterPosition].complaintId).child(UId).removeValue()
                      //  binding.downvote.setTextColor(context.resources.getColor(R.color.downvote))
                       // binding.downVoteCount.setTextColor(context.resources.getColor(R.color.downvote))
                    }
                }
            }
            fetchLikeDisLikeValueFromFirebaseDb(complaint)
            fetchImagesFromFirebaseDb(complaint.complaintId)

        }

        //           fetchImagesFromFirebaseDb(complaint.complaintId)


            private fun fetchImagesFromFirebaseDb(complaintId: String) {
            firebaseDatabase.reference.child(Tables.IMAGES.tableName).child(complaintId).get()
                .addOnCompleteListener { images ->
                    if (images.isSuccessful) {
                        val imageList = ArrayList<String>()

                        for (image in images.result.children) {
                            imageList.add(image.value.toString())
                        }

                        if (imageList.isNotEmpty()) {
                            Glide.with(context).load(imageList[0])
                                .into(binding.complaintImg)
                        }
                    }


                }
        }

        private fun updateValue(
            complaintId: String, trueTableName: String, falseTableName: String
        ) {

            firebaseDatabase.reference.child(trueTableName).child(complaintId).child(UId)
                .setValue(true)

            firebaseDatabase.reference.child(falseTableName).child(complaintId).child(UId)
                .removeValue()

        }

        private fun fetchLikeDisLikeValueFromFirebaseDb(complaint: Complaint) {
            firebaseDatabase.reference.child(Tables.LIKES.tableName).child(complaint.complaintId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        val likeList = arrayListOf<String>()

                        for (item in snapshot.children) {
                            likeList.add(item.key.toString())
                        }
                        binding.upvoteButton.isChecked = likeList.contains(UId)
                        binding.upvoteCount.text = likeList.size.toString()
                        if (likeList.isNotEmpty())
                        {
                            binding.upvote.setTextColor(context.resources.getColor(R.color.upvote))
                            binding.upvoteCount.setTextColor(context.resources.getColor(R.color.upvote))

                        }
                        else
                        {
                            binding.upvote.setTextColor(context.resources.getColor(R.color.downvote))
                            binding.upvoteCount.setTextColor(context.resources.getColor(R.color.downvote))

                        }



//                        binding.upvote.apply {
//                            isChecked = likeList.contains(UId)
//                            setTextColor(
//                                context.resources.getColor(
//                                    if (likeList.contains(UId) && likeList.size > 0) {
//                                        R.color.app_default
//                                    } else {
//                                        R.color.gray
//                                    }, null
//                                )
//                            )
//                            text = " ".plus(likeList.size)
//                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })

            firebaseDatabase.reference.child(Tables.DISLIKES.tableName)
                .child(complaint.complaintId).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        val disLikeList = arrayListOf<String>()

                        for (item in snapshot.children) {
                            disLikeList.add(item.key.toString())
                        }

                        binding.downVoteButton.isChecked = disLikeList.contains(UId)
                        binding.downVoteCount.text = disLikeList.size.toString()

                        if (disLikeList.isNotEmpty())
                        {
                            binding.downvote.setTextColor(context.resources.getColor(R.color.downvote_red))
                            binding.downVoteCount.setTextColor(context.resources.getColor(R.color.downvote_red))

                        }
                        else
                        {
                            binding.downvote.setTextColor(context.resources.getColor(R.color.downvote))
                            binding.downVoteCount.setTextColor(context.resources.getColor(R.color.downvote))

                        }

                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        }

    }


    fun setData(
        newComplaints: List<Complaint>,
        pairs: List<Pair<String, User>>
    ) {
        val diffCallback = DiffUtilCallback(list, newComplaints)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        val userDiffCallback = UserDiffUtilCallback(listOfUser, pairs)
        val userDiffResult = DiffUtil.calculateDiff(userDiffCallback)
        list.clear()
        list.addAll(newComplaints)
        listOfUser.clear()
        listOfUser.addAll(pairs)
        diffResult.dispatchUpdatesTo(this)
        userDiffResult.dispatchUpdatesTo(this)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ComplaintsItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }
}