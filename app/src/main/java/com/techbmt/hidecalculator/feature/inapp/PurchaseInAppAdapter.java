package com.techbmt.hidecalculator.feature.inapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.ProductDetails;
import com.techbmt.hidecalculator.R;

import java.util.ArrayList;
import java.util.List;

public class PurchaseInAppAdapter extends RecyclerView.Adapter<PurchaseInAppAdapter.ViewHolder> {
    private OnClickListener onClickListener;
    private List<ProductDetails> productDetailsList = new ArrayList<>();
    private Context context;

    public void setData(Context context, List<ProductDetails> productDetailsList) {
        this.productDetailsList = productDetailsList;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.item_data_inapp, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.display(productDetailsList.get(position));
    }

    @Override
    public int getItemCount() {
        return productDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvSubName;
        private ConstraintLayout item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubName = itemView.findViewById(R.id.tvSubName);
            item = itemView.findViewById(R.id.item);
        }

        public void display(ProductDetails productDetails) {
            if (productDetails == null) return;
            String price = productDetails.getOneTimePurchaseOfferDetails().getFormattedPrice();
            tvSubName.setText(setTitleValue(productDetails.getProductId(), price));
            item.setOnClickListener(view -> onClickListener.onClickItem(productDetails));
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClickItem(ProductDetails item);
    }

    private String setTitleValue(String productId, String price) {
        switch (productId) {
            case Constants.KEY_HIDDEN_CALCULATOR_1:
                return String.format(context.getResources().getString(R.string.message_purchase_one), price + "/5 coin");
            case Constants.KEY_HIDDEN_CALCULATOR_2:
                return String.format(context.getResources().getString(R.string.message_purchase_one), price + "/10 coin");
            case Constants.KEY_HIDDEN_CALCULATOR_3:
                return String.format(context.getResources().getString(R.string.message_purchase_one), price + "/20 coin");
            case Constants.KEY_HIDDEN_CALCULATOR_4:
                return String.format(context.getResources().getString(R.string.message_purchase_one), price + "/50 coin");
            case Constants.KEY_HIDDEN_CALCULATOR_5:
                return String.format(context.getResources().getString(R.string.message_purchase_one), price + "/100 coin");
            case Constants.KEY_HIDDEN_CALCULATOR_6:
                return String.format(context.getResources().getString(R.string.message_purchase_one), price + "/150 coin");
            case Constants.KEY_HIDDEN_CALCULATOR_7:
                return String.format(context.getResources().getString(R.string.message_purchase_one), price + "/200 coin");
            case Constants.KEY_HIDDEN_CALCULATOR_8:
                return String.format(context.getResources().getString(R.string.message_purchase_one), price + "/300 coin");
            default:
                return String.format(context.getResources().getString(R.string.message_purchase_one), "/0 coin");
        }
    }
}
