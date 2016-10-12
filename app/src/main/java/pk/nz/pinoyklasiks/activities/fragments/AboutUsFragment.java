package pk.nz.pinoyklasiks.activities.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pk.nz.pinoyklasiks.R;

/**
 * Created 10/9/16.
 *
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */

public class AboutUsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about_us, null);
    }
}
