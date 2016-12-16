package io.sodaoud.heretest.app.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.sodaoud.heretest.app.R;
import io.sodaoud.heretest.app.model.PlaceResult;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by sofiane on 12/13/16.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

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

    PlaceResult[] places;
    private final PublishSubject<PlaceResult> onClickSubject = PublishSubject.create();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PlaceResult place = places[position];
        holder.title.setText(place.getTitle());
        holder.desc.setText(place.getVicinity());
        holder.itemView.setOnClickListener(v -> onClickSubject.onNext(place));
    }

    public Observable<PlaceResult> getPositionClicks(){
        return onClickSubject.asObservable();
    }

    @Override
    public int getItemCount() {
        return places != null ? places.length : 0;
    }

    public void setPlaces(PlaceResult[] places) {
        this.places = places;
        notifyDataSetChanged();
    }
}
