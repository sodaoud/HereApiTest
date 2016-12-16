package io.sodaoud.heretest.app.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.sodaoud.heretest.app.R;
import io.sodaoud.heretest.app.model.Route;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by sofiane on 12/13/16.
 */

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.desc)
        TextView desc;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    Route[] routes;
    private final PublishSubject<Route> onClickSubject = PublishSubject.create();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Route route = routes[position];
        holder.title.setText(route.getText());
        holder.itemView.setOnClickListener(v -> onClickSubject.onNext(route));
    }

    public Observable<Route> getPositionClicks() {
        return onClickSubject.asObservable();
    }

    @Override
    public int getItemCount() {
        return routes != null ? routes.length : 0;
    }

    public void setRoutes(Route[] routes) {
        this.routes = routes;
        notifyDataSetChanged();
    }
}
