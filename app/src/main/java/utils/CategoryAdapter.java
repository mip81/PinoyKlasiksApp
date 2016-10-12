package utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import pk.nz.pinoyklasiks.R;
import pk.nz.pinoyklasiks.beans.AbstractCategory;

/**
 * Adapter for categories
 * using the ArrayList of AbstractCategory
 * to fill the ListView
 *
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */

public class CategoryAdapter extends ArrayAdapter<AbstractCategory> {

    private InputStream is;                        // read assets with images for category

    // Constructor of customer adapter
    public CategoryAdapter(Context context, List<AbstractCategory> categories) {
        super(context, 0, categories);
    }

    /**
     * Bind views in layout to fields of AbstractCategory object
     * @param position in ArrayList<AbstractCategory>
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the AbstractCategory object for this position
        AbstractCategory abstractCategory = getItem(position);

        // Check if this view null inflate it create another one
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lv_categories, parent, false);
        }

        // Get the view to populate new data
        TextView tvCatName = (TextView)convertView.findViewById(R.id.tvCatName);
        TextView tvCatDesc = (TextView)convertView.findViewById(R.id.tvCatDesc);
        ImageView ivCatPic = (ImageView)convertView.findViewById(R.id.ivCatPic);

        // Populate our date into the layout views
            tvCatName.setText(abstractCategory.getCat_name());
            tvCatDesc.setText(abstractCategory.getDescription());

        try {
            // read the sream, convert it to Drawable and assign to ImageView
            is = getContext().getAssets().open(abstractCategory.getPic()+AppConst.PIC_EXT);
            Drawable d = Drawable.createFromStream(is, null);
            ivCatPic.setImageDrawable(d);

            if(AppConst.DEBUG) Log.d(AppConst.LOGD, " Push object into ListView :: "+abstractCategory);

        }catch (Exception e){
            // assign nopic to ImageView and Log exception
            ivCatPic.setImageResource(R.drawable.nopic);
            Log.e(AppConst.LOGE, "  IS in CategoryAdapter ::: "+e);

        }finally {
            if(is != null){
                try{
                    is.close();
                }catch (IOException e){
                    Log.e(AppConst.LOGE, "Can't close IS in CategoryAdapter ::: "+e);
                }
            }
        }

        return convertView;
    }
}
