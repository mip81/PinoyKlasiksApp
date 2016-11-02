package pk.nz.pinoyklasiks.activities.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pk.nz.pinoyklasiks.R;

/**<pre>
 * Title       : CategoriesFragment class
 * Purpose     : Have the ListView of categories
 *               used in the Main Activity
 *
 * * Date        : 24.09.2016
 * Input       : none
 * Proccessing : none
 *
 * Output      : fragment
 *
 * </pre>
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */
public class CategoriesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }
}
