package net.agusharyanto.objectcollector;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by agus on 10/15/15.
 */
public class HotelArrayAdapter extends ArrayAdapter<Hotel> {
    Context context;

    public HotelArrayAdapter(Context context, int resourceId,
                             List<Hotel> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageViewFoto;
        TextView txtName;
        TextView txtAddress;
        TextView txtTotalRoom;
     //   TextView txtNIM2;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Hotel rowItem = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_hotel, null);
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.textViewRowName);
            holder.txtAddress = (TextView) convertView.findViewById(R.id.textViewRowAddress);
            holder.txtTotalRoom = (TextView) convertView.findViewById(R.id.textViewRowTotalRoom);
     //       holder.txtNIM2 = (TextView) convertView.findViewById(R.id.textViewNIm2);
            holder.imageViewFoto = (ImageView) convertView.findViewById(R.id.imageViewRowHotel);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        holder.txtName.setText(rowItem.getName());
        holder.txtAddress.setText(rowItem.getAddress());
        holder.txtTotalRoom.setText(rowItem.getTotal_room());
        Picasso.with(context).load(AppConfig.URL_SERVER+"upload/thumb/"+rowItem.getFoto()).into(holder.imageViewFoto);
        if (position%2==0) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.White));
        }else{
            convertView.setBackgroundColor(context.getResources().getColor(R.color.LemonChiffon));

        }
        //holder.imageView.setImageResource(rowItem.getImageId());
        return convertView;
    }

}
