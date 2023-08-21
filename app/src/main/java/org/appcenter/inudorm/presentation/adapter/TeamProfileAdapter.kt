package org.appcenter.inudorm.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.appcenter.inudorm.R
import org.appcenter.inudorm.databinding.ItemTeamProfileBinding
import org.appcenter.inudorm.model.TeamProfile


class TeamProfileAdapter(
    private var _dataSet: ArrayList<TeamProfile>,
) :
    RecyclerView.Adapter<TeamProfileAdapter.TeamProfileViewHolder>() {


    var dataSet: ArrayList<TeamProfile>
        get() = _dataSet
        set(value) {
            _dataSet = value
        }

    inner class TeamProfileViewHolder(
        var viewBinding: ItemTeamProfileBinding,

        ) : RecyclerView.ViewHolder(viewBinding.root) {}

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TeamProfileViewHolder {

        val binding = DataBindingUtil.inflate<ItemTeamProfileBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.item_team_profile,
            viewGroup,
            false
        )

        return TeamProfileViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: TeamProfileViewHolder, position: Int) {
        viewHolder.viewBinding.teamProfile = _dataSet[position]
        _dataSet[position].let {
            if (it.hasInvitedToSchedule != null)
                viewHolder.viewBinding.radioButton2.visibility = View.VISIBLE
        }
    }

    override fun getItemCount() = dataSet.size

}
