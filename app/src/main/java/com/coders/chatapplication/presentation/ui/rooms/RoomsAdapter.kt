package com.coders.chatapplication.presentation.ui.rooms

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.coders.chatapplication.R
import com.coders.chatapplication.domain.model.RoomModel
import com.coders.chatapplication.presentation.commons.AsyncDiffUtil
import com.coders.chatapplication.presentation.ui.views.AvatarImageView

class RoomsAdapter(
	private val onItemClicked: (RoomModel) -> Unit,
	private val thisUserId: Long
) : RecyclerView.Adapter<RoomsAdapter.ViewHolder>() {

	private val diffUtil = AsyncDiffUtil(this, object : DiffUtil.ItemCallback<RoomModel>() {
		override fun areItemsTheSame(oldItem: RoomModel, newItem: RoomModel): Boolean {
			return oldItem.id == newItem.id
		}

		override fun areContentsTheSame(oldItem: RoomModel, newItem: RoomModel): Boolean {
			return oldItem == newItem
		}
	})

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(
			LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false)
		)
	}

	override fun getItemCount(): Int = diffUtil.current().size

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bindView(diffUtil.current()[position])
	}

	fun update(rooms: List<RoomModel>) {
		diffUtil.update(rooms.toMutableList())
	}

	inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

		private val avatar = itemView.findViewById<AvatarImageView>(R.id.avatar)
		private val name = itemView.findViewById<TextView>(R.id.name)
		private val lastMessage = itemView.findViewById<TextView>(R.id.last_message)

		@SuppressLint("SetTextI18n")
		fun bindView(roomModel: RoomModel) {
			val otherUser =
				roomModel.users?.find { it.id != thisUserId } ?: throw Exception("WTF happened")
			avatar.setText("${otherUser.firstName?.get(0)}", ((otherUser.id ?: 0) % 255).toInt())
			name.text = "${otherUser.firstName} ${otherUser.lastName}"
			lastMessage.text = "${if (roomModel.lastMessage?.sender == thisUserId) {
				"You:"
			} else ""}${roomModel.lastMessage?.message ?: ""}"
			itemView.setOnClickListener {
				onItemClicked(roomModel)
			}
		}
	}
}