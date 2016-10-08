package pk.nz.pinoyklasiks.activities.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pk.nz.pinoyklasiks.R;

/**
 * The fragment shows the main categories
 * of APP and other information
 * @Author Mikhail PASTUSHKOV
 * @Author Melchor RELATADO
*/

public class CategoriesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }
}
