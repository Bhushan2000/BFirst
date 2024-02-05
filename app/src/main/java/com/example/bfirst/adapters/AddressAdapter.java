package com.example.bfirst.adapters;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bfirst.models.AddressModel;
import com.example.bfirst.database.DBQueries;
import com.example.bfirst.R;
import com.example.bfirst.activities.AddAddressActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.bfirst.activities.DeliveryActivity.SELECT_ADDRESS;
import static com.example.bfirst.fragments.MyAccountFragment.MANAGE_ADDRESS;
import static com.example.bfirst.activities.MyAddressesActivity.refreshItems;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    private List<AddressModel> addressModelList;
    private int MODE;
    private int preSelectedPosition;
    private boolean refresh = false;


    public AddressAdapter(List<AddressModel> addressModelList, int MODE ) {
        this.addressModelList = addressModelList;
        this.MODE = MODE;
        preSelectedPosition = DBQueries.selectedAddress;



    }

    @NonNull
    @Override
    public AddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item_layout, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.ViewHolder holder, int position) {
        String city = addressModelList.get(position).getCity();
        String locality = addressModelList.get(position).getLocality();
        String flatNo = addressModelList.get(position).getFlatNo();

        String pinCode = addressModelList.get(position).getPinCode();
        String landmark = addressModelList.get(position).getLandmark();
        String name = addressModelList.get(position).getName();
        String mobileNo = addressModelList.get(position).getPhoneNo();
        String alternatePhoneNo = addressModelList.get(position).getAlternatePhoneNo();
        String state = addressModelList.get(position).getState();

        boolean selected = addressModelList.get(position).isSelected();


        holder.setData(name, city, pinCode, selected, position, mobileNo, alternatePhoneNo, flatNo, locality, state, landmark);


    }

    @Override
    public int getItemCount() {
        return addressModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView fullName;
        private TextView address;
        private TextView pinCode;
        private ImageView icon;
        private LinearLayout option_container;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            pinCode = itemView.findViewById(R.id.pincode);
            icon = itemView.findViewById(R.id.icon_view);
            option_container = itemView.findViewById(R.id.option_container);


        }


        private void setData(String username, String city, String userPinCode, boolean selected, final int position, String mobileNo, String alternateMobileNo, String flatNo, String locality, String state, String landmark) {

            if (alternateMobileNo.equals("")) {

                fullName.setText(username + " - " + mobileNo);
            } else {
                fullName.setText(username + " - " + mobileNo + " or " + alternateMobileNo);

            }


            if (landmark.equals("")) {
                address.setText(flatNo + " " + locality + " " + " " + city + " " + state);

            } else {

                address.setText(flatNo + " " + locality + " " + landmark + " " + city + " " + state);
            }

            pinCode.setText(userPinCode);

            if (MODE == SELECT_ADDRESS) {
                option_container.setVisibility(View.GONE);


                icon.setImageResource(R.drawable.ic_check_black_24dp);


                if (selected) {

                    icon.setVisibility(View.VISIBLE);
                    preSelectedPosition = position;
                } else {

                    icon.setVisibility(View.GONE);
                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (preSelectedPosition != position) {

                            addressModelList.get(position).setSelected(true);
                            addressModelList.get(preSelectedPosition).setSelected(false);
                            refreshItems(preSelectedPosition, position);
                            preSelectedPosition = position;
                            DBQueries.selectedAddress = position;
                        }

                    }
                });

            } else if (MODE == MANAGE_ADDRESS) {


                option_container.setVisibility(View.GONE);

                ///// edit address
                option_container.getChildAt(0).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent addAddressIntent = new Intent(itemView.getContext(), AddAddressActivity.class);
                        addAddressIntent.putExtra("INTENT", "update_address");
                        addAddressIntent.putExtra("index", position);

                        itemView.getContext().startActivity(addAddressIntent);
                        refresh = false;

                    }
                });


                ///// remove address

                option_container.getChildAt(1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Map<String, Object> address = new HashMap<>();
                        int x = 0;
                        int selected = -1;

                        for (int i = 0; i < addressModelList.size(); i++) {
                            if (i != position) {
                                x++;
                                address.put("city_" + x, addressModelList.get(i).getCity());
                                address.put("locality_" + x, addressModelList.get(i).getLocality());
                                address.put("flat_no_" + x, addressModelList.get(i).getFlatNo());
                                address.put("pin_code_" + x, addressModelList.get(i).getPinCode());
                                address.put("landmark_" + x, addressModelList.get(i).getLandmark());
                                address.put("name_" + x, addressModelList.get(i).getName());
                                address.put("mobile_no_" + x, addressModelList.get(i).getPhoneNo());
                                address.put("alternate_mobile_no_" + x, addressModelList.get(i).getAlternatePhoneNo());
                                address.put("state_" + x, addressModelList.get(i).getState());


                                if (addressModelList.get(position).isSelected()) {
                                    if (position - 1 >= 0) {
                                        if (x == position) {

                                            address.put("selected_" + x, true);
                                            selected = x;

                                        }else{
                                            address.put("selected_" + x, addressModelList.get(i).isSelected());

                                        }
                                    } else {
                                        if (x == 1) {
                                            address.put("selected_" + x, true);
                                            selected = x;

                                        }else{
                                            address.put("selected_" + x, addressModelList.get(i).isSelected());

                                        }
                                    }
                                } else {
                                    address.put("selected_" + x, addressModelList.get(i).isSelected());

                                    if (addressModelList.get(i).isSelected()){
                                        selected = x;

                                    }
                                }


                            }

                        }

                        address.put("list_size", x);
                        final int finalSelected = selected;

                        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_ADDRESSES")
                                .set(address).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    DBQueries.addressModelList.remove(position);
                                    if (finalSelected != -1){

                                        DBQueries.selectedAddress = finalSelected - 1;
                                        DBQueries.addressModelList.get(finalSelected - 1).setSelected(true);
                                    }else if (DBQueries.addressModelList.size() == 0){
                                        DBQueries.selectedAddress = -1;

                                    }
                                    notifyDataSetChanged();

                                } else {
                                    Toast.makeText(itemView.getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                             }
                        });

                        refresh = false;

                    }
                });
                icon.setImageResource(R.drawable.ic_arrow_drop_down_circle_black_24dp);
                icon.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        option_container.setVisibility(View.VISIBLE);
                        if (refresh) {

                            refreshItems(preSelectedPosition, preSelectedPosition);
                        } else {
                            refresh = true;

                        }

                        preSelectedPosition = position;


                    }
                });
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refreshItems(preSelectedPosition, preSelectedPosition);


                        preSelectedPosition = -1;
                    }
                });


            }


        }


    }


}
