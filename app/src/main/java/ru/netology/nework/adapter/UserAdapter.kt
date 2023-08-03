package ru.netology.nework.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import ru.netology.nework.R
import ru.netology.nework.databinding.CardUserBinding
import ru.netology.nework.databinding.FragmentUserBinding
import ru.netology.nework.dto.User
import ru.netology.nework.entity.UserEntity
import ru.netology.nework.ui.UserFragment
import ru.netology.nework.utils.loadCircleCrop

interface OnUserInteractionListener {
    fun openProfile(user: User)
}

class UserAdapter(private val onUserInteractionListener: OnUserInteractionListener)
    : RecyclerView.Adapter<UserAdapter.UserViewHolder>(){

    private var oldData = emptyList<UserEntity>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {

//       return UserViewHolder(
//           CardUserBinding.inflate(
//               LayoutInflater.from(parent.context),
//               parent,
//               false
//           )
//       )
//   }

    val binding = CardUserBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
    return UserViewHolder(binding, onUserInteractionListener)
}

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
 //       holder.binding.userName.text = oldData[position].name
        val item = oldData[position]
        holder.bind(item)

            }

    class UserViewHolder(
    private val binding: CardUserBinding,
    private val onUserInteractionListener: OnUserInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: UserEntity) {
        with(binding) {
            userName.text = user.name
            Glide.with(userAvatarCard)
                .load("${user.avatar}")
                .transform(CircleCrop())
                .placeholder(R.drawable.ic_default_user_profile_image)
                .into(userAvatarCard)

            userView.setOnClickListener {
                onUserInteractionListener.openProfile(user.toDto())
            }
        }
    }
}

//    class UserViewHolder(val binding: CardUserBinding) :
//        RecyclerView.ViewHolder(binding.root)




    override fun getItemCount(): Int {
        return oldData.size
    }

    fun setData(newData: List<UserEntity>){
        oldData = newData
        notifyDataSetChanged()
    }
}




//class UserAdapter(private val onUserInteractionListener: OnUserInteractionListener) :
//    ListAdapter<User, UserViewHolder>(UserDiffCallback()) {
//
//    private var oldData = emptyList<UserEntity>()
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
//        val binding = CardUserBinding.inflate(
//            LayoutInflater.from(parent.context), parent, false
//        )
//        return UserViewHolder(binding, onUserInteractionListener)
//    }
//
//    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
//        val item = getItem(position)
//        holder.bind(item)
//    }
//}
//
//class UserViewHolder(
//    private val binding: CardUserBinding,
//    private val onUserInteractionListener: OnUserInteractionListener,
//) : RecyclerView.ViewHolder(binding.root) {
//
//    fun bind(user: User) {
//        with(binding) {
//            userName.text = user.name
//            Glide.with(userAvatarCard)
//                .load("${user.avatar}")
//                .transform(CircleCrop())
//                .placeholder(R.drawable.ic_default_user_profile_image)
//                .into(userAvatarCard)
//
//            userView.setOnClickListener {
//                onUserInteractionListener.openProfile(user)
//            }
//        }
//    }
//}
//
//class UserDiffCallback : DiffUtil.ItemCallback<User>() {
//
//    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
//        return oldItem.id == newItem.id
//    }
//
//    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
//        return oldItem == newItem
//    }
//
//}