package com.badve.ajinkya.heyweather.view;

/**
 * Created by Ajinkya on 31-05-2015.
 */
import java.util.HashMap;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/** Customizing AutoCompleteTextView to return Place Description
 * corresponding to the selected item
 * @see @Link http://wptrafficanalyzer.in/blog/android-autocompletetextview-with-google-places-autocomplete-api/
 *
 */
public class CustomAutoCompleteTextView extends AutoCompleteTextView {

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /** Returns the place description corresponding to the selected item */
    @Override
    protected CharSequence convertSelectionToString(Object selectedItem) {
        /** Each item in the autocompetetextview suggestion list is a hashmap object */
        HashMap<String, String> hm = (HashMap<String, String>) selectedItem;
        return hm.get("city_name");
        //return hm.get("description");
    }
}
