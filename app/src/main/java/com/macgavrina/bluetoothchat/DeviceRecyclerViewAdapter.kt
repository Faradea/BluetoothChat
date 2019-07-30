package com.macgavrina.bluetoothchat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.macgavrina.bluetoothchat.R
import com.macgavrina.bluetoothchat.model.MyBluetoothDevice
import kotlinx.android.synthetic.main.device_list_item_card.view.*

public class DeviceRecyclerViewAdapter(inputOnClickListener: OnDeviceInListClickListener) : RecyclerView.Adapter<DeviceRecyclerViewAdapter.ViewHolder>() {

    private var mItems: List<MyBluetoothDevice>? = null
    private val mOnClickListener: OnDeviceInListClickListener = inputOnClickListener

    init {

    }

    fun setBluetoothDevices(expenses: List<MyBluetoothDevice>) {
        this.mItems = expenses
        notifyDataSetChanged()
    }

    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var name: TextView = view.device_list_item_name
        var mac: TextView = view.device_list_item_mac
        var isPaired: TextView = view.device_list_item_ispaired
        var layout: ConstraintLayout = view.device_list_item_layout

        private var mItem: MyBluetoothDevice? = null

        init {
        }

        fun setItem(item: MyBluetoothDevice) {
            mItem = item
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): DeviceRecyclerViewAdapter.ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        // create a new view
        val view = layoutInflater.inflate(R.layout.device_list_item_card, parent, false)
        // set the view's size, margins, paddings and layout parameters

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        val item = mItems?.get(position) ?: return

        holder.name.text = item.name
        holder.mac.text = item.mac

        if (item.isPaired) {
            holder.isPaired.text = "isPaired"
        } else {
            holder.isPaired.text = ""
        }

        holder.setItem(mItems?.get(position)!!)

        holder.layout.setOnClickListener {
            if (item == null) return@setOnClickListener
            mOnClickListener.onDeviceClick(item)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        if (mItems != null) {
            return mItems!!.size
        }
        return -1
    }

    interface OnDeviceInListClickListener {
        fun onDeviceClick(device: MyBluetoothDevice)
    }
}


