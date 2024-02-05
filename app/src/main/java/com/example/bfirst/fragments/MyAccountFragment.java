package com.example.bfirst.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bfirst.database.DBQueries;
import com.example.bfirst.activities.MyAddressesActivity;
import com.example.bfirst.models.MyOrderItemModel;
import com.example.bfirst.R;
import com.example.bfirst.activities.Register;
import com.example.bfirst.activities.UpdateUserInfoActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccountFragment extends Fragment {

    public MyAccountFragment() {
        // Required empty public constructor
    }
    public static final int MANAGE_ADDRESS = 1;
    private Button viewAllBtn;
    private CircleImageView profile_image,currentOrderImage;
    private TextView username,user_email,currentOrderStatus;
    private LinearLayout layout_container,recent_order_container;
    private Dialog loadingDialogue;
    private ImageView orderIndicator,packedIndicator,shippedIndicator,deliveredIndicator;
    private ProgressBar O_P_progress,P_S_progress,S_D_progress;
    private TextView your_recent_order_title;
    private TextView addressName,address,pinCode;
    private Button signOutBtn;
    private FloatingActionButton settingsBtn;







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  =  inflater.inflate(R.layout.fragment_my_account, container, false);
        ///////////////////////////////////loading dialogue/////////////////////////////////////////////

        loadingDialogue = new Dialog(getContext());
        loadingDialogue.setContentView(R.layout.loadingprogressdialogue);
        loadingDialogue.setCancelable(true);
        loadingDialogue.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialogue.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialogue.show();

/////////////////////////////////////loading dialogue/////////////////////////////////////////////

        viewAllBtn = view.findViewById(R.id.view_all_address_button);

        profile_image = view.findViewById(R.id.profile_image);
        currentOrderImage = view.findViewById(R.id.current__order_image);
        username = view.findViewById(R.id.username);
        user_email = view.findViewById(R.id.user_e_mail);
        layout_container = view.findViewById(R.id.layout_container);
        currentOrderStatus = view.findViewById(R.id.tv_current_order_status);


//        progress
        orderIndicator = view.findViewById(R.id.ordered_indicator);
        packedIndicator = view.findViewById(R.id.packed_indicator);
        shippedIndicator= view.findViewById(R.id.shipped_indicator);
        deliveredIndicator = view.findViewById(R.id.delivered_indicator);

        O_P_progress = view.findViewById(R.id.order_packed_progress);
        P_S_progress = view.findViewById(R.id.packed_shipped_progress);
        S_D_progress = view.findViewById(R.id.shipped_delivered_progress);

        recent_order_container = view.findViewById(R.id.recent_order_container);
        your_recent_order_title=view.findViewById(R.id.your_recent_order_title);

        addressName=view.findViewById(R.id.address_full_name);
        address=view.findViewById(R.id.address);
        pinCode=view.findViewById(R.id.address_pincode);

        signOutBtn=view.findViewById(R.id.signout_button);

        settingsBtn=view.findViewById(R.id.settings_button);




        layout_container.getChildAt(1).setVisibility(View.GONE);

        loadingDialogue.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                for (MyOrderItemModel myOrderItemModel : DBQueries.myOrderItemModelList) {
                    if (!myOrderItemModel.isCancellationRequested()) {

                        if (! myOrderItemModel.getOrderStatus().equals("Delivered") && ! myOrderItemModel.getOrderStatus().equals("Cancelled")) {
                            layout_container.getChildAt(1).setVisibility(View.VISIBLE);

                            Glide.with(getContext()).load(myOrderItemModel.getProductImage()).apply(new RequestOptions().placeholder(R.drawable.unload)).into(currentOrderImage);
                            currentOrderStatus.setText(myOrderItemModel.getOrderStatus());


                            switch (myOrderItemModel.getOrderStatus()) {


                                case "Ordered":
                                    orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));




                                    break;

                                case "Packed":
                                    orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));

                                    packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));

//
                                    O_P_progress.setProgress(100);



                                    break;

                                case "Shipped":
                                    orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));

                                    packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));

                                    shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));

//

                                    O_P_progress.setProgress(100);
                                    P_S_progress.setProgress(100);



                                    break;

                                case "Out for Delivered":
                                    orderIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));

                                    packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));

                                    shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));

                                    deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));


                                    O_P_progress.setProgress(100);
                                    P_S_progress.setProgress(100);
                                    S_D_progress.setProgress(100);
                                    break;


                            }
                            break;


                        }
                    }
                }



                int i = 0;
                for (MyOrderItemModel OrderItemModel : DBQueries.myOrderItemModelList) {
                    if ( i<4){

                        if (OrderItemModel.getOrderStatus().equals("Delivered")){
                            Glide.with(getContext()).load(OrderItemModel.getProductImage()).apply(new RequestOptions().placeholder(R.drawable.unload)).into((CircleImageView) recent_order_container.getChildAt(i));
                            i++;
                        }
                    }else{
                        break;
                    }


                }
                if (i==0){
                    your_recent_order_title.setText("No recent Orders");
                }
                if ( i<3){
                    for (int x = i; x < 4; x++){
                        recent_order_container.getChildAt(x).setVisibility(View.GONE);
                    }
                }

                loadingDialogue.show();
                loadingDialogue.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        loadingDialogue.setOnDismissListener(null);
                        if (DBQueries.addressModelList.size()==0){




                            addressName.setText("No Name Selected");
                            address.setText("No Address selected");
                            pinCode.setText("No pinCode Selected");


                        }else{



                            setAddress();
                        }

                    }
                });
                DBQueries.loadAddresses(getContext(),loadingDialogue,false);







            }
        });

        DBQueries.loadOrders(getContext(),null,loadingDialogue);

        viewAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myAddressIntent =  new Intent(getContext(), MyAddressesActivity.class);
                myAddressIntent.putExtra("MODE",MANAGE_ADDRESS);

                startActivity(myAddressIntent);
            }
        });

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                DBQueries.clearData();
                Intent registerIntent = new Intent(getContext(), Register.class);
                startActivity(registerIntent);
                getActivity().finish();

            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updateUserInfo = new Intent(getContext(), UpdateUserInfoActivity.class);
                updateUserInfo.putExtra("Name",username.getText());
                updateUserInfo.putExtra("Email",user_email.getText());
                updateUserInfo.putExtra("Photo",DBQueries.profile);
                startActivity(updateUserInfo);


            }
        });

        return view;


    }

    @Override
    public void onStart() {
        super.onStart();

        user_email.setText(DBQueries.email);
        username.setText(DBQueries.fullname);


        if ( ! DBQueries.profile.equals("")){


            Glide.with(getContext()).load(DBQueries.profile).apply(new RequestOptions().placeholder(R.drawable.ic_account_circle_black_24dp)).into(profile_image);
        }else{
            profile_image.setImageResource(R.drawable.ic_account_circle_black_24dp);
        }

        if (! loadingDialogue.isShowing()){
            if (DBQueries.addressModelList.size()==0){




                addressName.setText("No Name Selected");
                address.setText("No Address selected");
                pinCode.setText("No pinCode Selected");


            }else{



                setAddress();
            }
        }
    }

    private void setAddress() {
        String nameText,mobileNo;
        nameText = DBQueries.addressModelList.get(DBQueries.selectedAddress).getName();
        mobileNo = DBQueries.addressModelList.get(DBQueries.selectedAddress).getPhoneNo();
        if (DBQueries.addressModelList.get(DBQueries.selectedAddress).getAlternatePhoneNo().equals("")){

            addressName.setText(nameText + " - " + mobileNo);
        }else{
            addressName.setText(nameText + " - " + mobileNo +  " or " + DBQueries.addressModelList.get(DBQueries.selectedAddress).getAlternatePhoneNo());

        }

        String flatNo = DBQueries.addressModelList.get(DBQueries.selectedAddress).getFlatNo();
        String locality = DBQueries.addressModelList.get(DBQueries.selectedAddress).getLocality();
        String landmark = DBQueries.addressModelList.get(DBQueries.selectedAddress).getLandmark();
        String city = DBQueries.addressModelList.get(DBQueries.selectedAddress).getCity();
        String state = DBQueries.addressModelList.get(DBQueries.selectedAddress).getState();
        if (landmark.equals("")){
            address.setText(flatNo +" " +locality +" "  +" " + city +" " + state );

        }else{

            address.setText(flatNo +" " +locality +" " + landmark +" " + city +" " + state );
        }

        pinCode.setText(DBQueries.addressModelList.get(DBQueries.selectedAddress).getPinCode());
    }
}
