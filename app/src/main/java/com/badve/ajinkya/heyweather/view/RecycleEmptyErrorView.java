package com.badve.ajinkya.heyweather.view;

/**
 * Created by Ajinkya on 30-05-2015.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
/**
 * Custom recycleView to the set empty view and Error view
 * @author Ajinkya
 * {@link //https://gist.github.com/henrytao-me/2f7f113fb5f2a59987e7}
 */
public class RecycleEmptyErrorView extends RecyclerView {

    private View mEmptyView;

    private View mErrorView;

    private boolean isError;

    private int mVisibility;

    final private AdapterDataObserver mObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            updateEmptyView();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            updateEmptyView();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            updateEmptyView();
        }
    };

    public RecycleEmptyErrorView(Context context) {
        super(context);
        mVisibility = getVisibility();
    }

    /**
     *
     * @param context
     * @param attrs
     */
    public RecycleEmptyErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mVisibility = getVisibility();
    }

    /**
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public RecycleEmptyErrorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mVisibility = getVisibility();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setAdapter(Adapter adapter) {
        Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(mObserver);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(mObserver);
        }
        updateEmptyView();
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        mVisibility = visibility;
        updateErrorView();
        updateEmptyView();
    }

    /**
     * Update the emptyView
     */
    private void updateEmptyView() {
        if (mEmptyView != null && getAdapter() != null) {
            boolean isShowEmptyView = getAdapter().getItemCount() == 0;
            mEmptyView.setVisibility(isShowEmptyView && !shouldShowErrorView() && mVisibility == VISIBLE ? VISIBLE : GONE);
            super.setVisibility(!isShowEmptyView && !shouldShowErrorView() && mVisibility == VISIBLE ? VISIBLE : GONE);
        }
    }

    /**
     * update the error view
     */
    private void updateErrorView() {
        if (mErrorView != null) {
            mErrorView.setVisibility(shouldShowErrorView() && mVisibility == VISIBLE ? VISIBLE : GONE);
        }
    }

    private boolean shouldShowErrorView() {
        if (mErrorView != null && isError) {
            return true;
        }
        return false;
    }

    /**
     * set the emptyView
     * @param emptyView
     */
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
        updateEmptyView();
    }

    /**
     * set the error view
     * @param errorView
     */
    public void setErrorView(View errorView) {
        mErrorView = errorView;
        updateErrorView();
        updateEmptyView();
    }

    /**
     * Shows the error view
     */
    public void showErrorView() {
        isError = true;
        updateErrorView();
        updateEmptyView();
    }

    /**
     * Hide the error view
     */
    public void hideErrorView() {
        isError = false;
        updateErrorView();
        updateEmptyView();
    }

}
