package com.example.bfirst.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bfirst.database.DBQueries;
import com.example.bfirst.R;
import com.example.bfirst.adapters.CartAdapter;
import com.example.bfirst.models.CartItemModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DeliveryActivity extends AppCompatActivity {

    private RecyclerView deliveryRecyclerview;
    private Button changeOrAddNewAddressButton;
    public static final int SELECT_ADDRESS = 0;
    private TextView totalAmount;
    private TextView fullName;
    private String name, mobileNo;
    private TextView fullAddress;
    private TextView pinCode;
    private Button continueBtn;
    public static Dialog loadingDialogue;
    private Dialog paymentMethodDialogue;
    private TextView codTitle;
    private View divider;
    private ImageButton paytm, cod;
    private ConstraintLayout order_confirmation_layout;
    private ImageButton continue_shopping_Btn;
    private TextView orderId;
    private boolean successResponse = false;
    public static boolean fromCart;

    public static List<CartItemModel> cartItemModelList;
    private static final String TAG = "DeliveryActivity";

    private String order_id;
    public static boolean codOrderConfirmed = false;

    private FirebaseFirestore firebaseFirestore;
    public static boolean getQuantityIDs = true;
    public static CartAdapter cartAdapter;

    private String paymentMethod = "PAYTM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delivery");
        deliveryRecyclerview = findViewById(R.id.delivery_recyclerview);
        changeOrAddNewAddressButton = findViewById(R.id.change_or_add_address_btn);
        totalAmount = findViewById(R.id.total_cart_amount);

        fullName = findViewById(R.id.full_name);
        fullAddress = findViewById(R.id.address);
        pinCode = findViewById(R.id.address_pincode);
        continueBtn = findViewById(R.id.cart_continue_button);


        order_confirmation_layout = findViewById(R.id.order_confirmation_layout);
        order_confirmation_layout.setVisibility(View.GONE);
        continue_shopping_Btn = findViewById(R.id.continue_shopping_Btn);
        orderId = findViewById(R.id.order_id);


        ///////////////////////////////////loading dialogue/////////////////////////////////////////////

        loadingDialogue = new Dialog(DeliveryActivity.this);
        loadingDialogue.setContentView(R.layout.loadingprogressdialogue);
        loadingDialogue.setCancelable(true);
        loadingDialogue.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialogue.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


/////////////////////////////////////loading dialogue/////////////////////////////////////////////


        ///////////////////////////////////paymentMethod dialogue/////////////////////////////////////////////

        paymentMethodDialogue = new Dialog(DeliveryActivity.this);
        paymentMethodDialogue.setContentView(R.layout.payment_method);
        paymentMethodDialogue.setCancelable(true);
        paymentMethodDialogue.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        paymentMethodDialogue.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paytm = paymentMethodDialogue.findViewById(R.id.paytm);
        cod = paymentMethodDialogue.findViewById(R.id.cod_button);
        divider = paymentMethodDialogue.findViewById(R.id.payment_divider);
        codTitle = paymentMethodDialogue.findViewById(R.id.cod_btn_title);
/////////////////////////////////////paymentMethod dialogue/////////////////////////////////////////////
        firebaseFirestore = FirebaseFirestore.getInstance();
        getQuantityIDs = true;
        order_id = UUID.randomUUID().toString().substring(0, 28);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        deliveryRecyclerview.setLayoutManager(layoutManager);


        cartAdapter = new CartAdapter(cartItemModelList, totalAmount, false);
        deliveryRecyclerview.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

        changeOrAddNewAddressButton.setVisibility(View.VISIBLE);
        changeOrAddNewAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQuantityIDs = false;
                Intent myAddressIntent = new Intent(DeliveryActivity.this, MyAddressesActivity.class);
                myAddressIntent.putExtra("MODE", SELECT_ADDRESS);

                startActivity(myAddressIntent);
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean allProductsAvailable = true;

                for (CartItemModel cartItemModel : cartItemModelList) {
                    if (cartItemModel.isQtyError()) {
                        allProductsAvailable = false;
                        break;
                    }
                    if (cartItemModel.getType() == cartItemModel.CART_ITEM) {


                        // enable payment COD from this condition


                        if (!cartItemModel.isCOD()) {
                            cod.setEnabled(false);
                            cod.setAlpha(0.5f);
                            divider.setAlpha(0.5f);
                            codTitle.setVisibility(View.GONE);
                            break;
                        } else {
                            cod.setEnabled(true);
                            cod.setAlpha(1f);
                            divider.setAlpha(1f);
                            cod.setVisibility(View.VISIBLE);

                        }
                    }
                }
                if (allProductsAvailable) {
                    paymentMethodDialogue.show();
                }


            }
        });


        ////////////////////////// C O D////////////////////////////
        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethod = "COD";
                placeOrderDetails();

            }
        });

        ////////////////////////// C O D////////////////////////////


        //////////////////////////////////////////////////payTm coding///////////////////////////////////////////////

        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethod = "PAYTM";
                placeOrderDetails();

            }

        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        ////////////////////////////// Accessing Quantity/////////////////////

        if (getQuantityIDs) {

            loadingDialogue.show();

            for (int x = 0; x < cartItemModelList.size() - 1; x++) {

                for (int y = 0; y < cartItemModelList.get(x).getProductQuantity(); y++) {
                    final String quantityDocumentName = UUID.randomUUID().toString().substring(0, 20);
                    Map<String, Object> timeStamp = new HashMap<>();
                    timeStamp.put("time", FieldValue.serverTimestamp());
                    final int finalX = x;
                    final int finalY = y;

                    firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(x).getProductID()).collection("QUANTITY").document(quantityDocumentName).set(timeStamp)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {


                                    if (task.isSuccessful()) {

                                        cartItemModelList.get(finalX).getQtyIDs().add(quantityDocumentName);
                                        if (finalY + 1 == cartItemModelList.get(finalX).getProductQuantity()) {


                                            firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(finalX).getProductID()).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).limit(cartItemModelList.get(finalX).getStockQuantity())
                                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {

                                                        ///////// SERVER QUANTITY LIST ///////////////////////
                                                        List<String> serverQuantity = new ArrayList<>();

                                                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                            serverQuantity.add(queryDocumentSnapshot.getId());

                                                        }

                                                        long availableQty = 0;

                                                        boolean noLongerAvailable = true;
                                                        for (String qtyId : cartItemModelList.get(finalX).getQtyIDs()) {

                                                            cartItemModelList.get(finalX).setQtyError(false);

                                                            if (!serverQuantity.contains(qtyId)) {


                                                                if (noLongerAvailable) {
                                                                    cartItemModelList.get(finalX).setInStock(false);
                                                                } else {
                                                                    cartItemModelList.get(finalX).setQtyError(true);
                                                                    cartItemModelList.get(finalX).setMaxQuantity(availableQty);
                                                                    Toast.makeText(DeliveryActivity.this, "All products may not be available at required quantity", Toast.LENGTH_SHORT).show();

                                                                }


                                                            } else {
                                                                availableQty++;
                                                                noLongerAvailable = false;
                                                            }


                                                        }
                                                        cartAdapter.notifyDataSetChanged();

                                                    } else {

                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();


                                                    }
                                                    loadingDialogue.dismiss();
                                                }
                                            });


                                        }


                                    } else {
                                        loadingDialogue.dismiss();
                                        String error = task.getException().getMessage();
                                        Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();


                                    }


                                }
                            });

                }


            }
        } else {
            getQuantityIDs = true;

        }
        ////////////////////////////// Accessing Quantity/////////////////////


        name = DBQueries.addressModelList.get(DBQueries.selectedAddress).getName();
        mobileNo = DBQueries.addressModelList.get(DBQueries.selectedAddress).getPhoneNo();
        if (DBQueries.addressModelList.get(DBQueries.selectedAddress).getAlternatePhoneNo().equals("")){

            fullName.setText(name + " - " + mobileNo);
        }else{
            fullName.setText(name + " - " + mobileNo +  " or " + DBQueries.addressModelList.get(DBQueries.selectedAddress).getAlternatePhoneNo());

        }

        String flatNo = DBQueries.addressModelList.get(DBQueries.selectedAddress).getFlatNo();
        String locality = DBQueries.addressModelList.get(DBQueries.selectedAddress).getLocality();
        String landmark = DBQueries.addressModelList.get(DBQueries.selectedAddress).getLandmark();
        String city = DBQueries.addressModelList.get(DBQueries.selectedAddress).getCity();
        String state = DBQueries.addressModelList.get(DBQueries.selectedAddress).getState();
        if (landmark.equals("")){
            fullAddress.setText(flatNo +" " +locality +" "  +" " + city +" " + state );

        }else{

            fullAddress.setText(flatNo +" " +locality +" " + landmark +" " + city +" " + state );
        }

        pinCode.setText(DBQueries.addressModelList.get(DBQueries.selectedAddress).getPinCode());

        if (codOrderConfirmed) {
            showConfirmationLayout();
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        loadingDialogue.dismiss();
        if (getQuantityIDs) {

            for (int x = 0; x < cartItemModelList.size() - 1; x++) {

                if (!successResponse) {

                    for (final String qtyID : cartItemModelList.get(x).getQtyIDs()) {
                        final int finalX = x;
                        firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(x).getProductID()).collection("QUANTITY").document(qtyID).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if (qtyID.equals(cartItemModelList.get(finalX).getQtyIDs().get(cartItemModelList.get(finalX).getQtyIDs().size() - 1))) {
                                            cartItemModelList.get(finalX).getQtyIDs().clear();

                                        }
                                    }
                                });

                    }
                } else {
                    cartItemModelList.get(x).getQtyIDs().clear();
                }

            }
        }
    }

    @Override
    public void onBackPressed() {
        if (successResponse) {
            finish();
            return;
        }
        super.onBackPressed();

    }

    private void showConfirmationLayout() {
        successResponse = true;
        codOrderConfirmed = false;
        getQuantityIDs = false;

        for (int x = 0; x < cartItemModelList.size() - 1; x++) {

            for (String qtyID : cartItemModelList.get(x).getQtyIDs()) {
                firebaseFirestore.collection("PRODUCTS").document(cartItemModelList.get(x).getProductID()).collection("QUANTITY").document(qtyID).update("user_ID", FirebaseAuth.getInstance().getUid());

            }


        }


        if (MainActivity.mainActivity != null) {

            MainActivity.mainActivity.finish();
            MainActivity.mainActivity = null;
            MainActivity.showCart = false;
        } else {
            MainActivity.resetMainActivity = true;
        }

        if (ProductsDetailsActivity.productDetailActivity != null) {
            ProductsDetailsActivity.productDetailActivity.finish();
            ProductsDetailsActivity.productDetailActivity = null;
        }


        //////////// send confirmation  sms/////////////////////////////////////

        String SMS_API = "https://www.fast2sms.com/dev/bulk";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SMS_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ////////nothing

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ////////nothing

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("authorization", "cS47t6Ld9oERzKuWeQM8blsrNInUjHTOhiPfG015gqp3mAxCwVKYRMkuGEiScaoZCxQbXVWsqfLJl7dw");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> body = new HashMap<>();
                body.put("sender_id", "FSTSMS");
                body.put("language", "english");
                body.put("route", "qt");
                body.put("numbers", mobileNo);
                body.put("message", "27763");
                body.put("variables", "{#FF#}");
                body.put("variables_values", order_id);

                return body;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 5000, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        RequestQueue requestQueue = Volley.newRequestQueue(DeliveryActivity.this);
        requestQueue.add(stringRequest);


        //////////// send confirmation  sms/////////////////////////////////////


        if (fromCart) {
            loadingDialogue.show();

            Map<String, Object> updateCartList = new HashMap<>();
            long cartListSize = 0;
            final List<Integer> indexList = new ArrayList<>();
            for (int x = 0; x < DBQueries.cartList.size(); x++) {

                if (!cartItemModelList.get(x).isInStock()) {
                    updateCartList.put("product_ID_" + cartListSize, cartItemModelList.get(x).getProductID());
                    cartListSize++;
                } else {
                    indexList.add(x);
                }

            }
            updateCartList.put("list_size", cartListSize);

            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART")
                    .set(updateCartList).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        for (int x = 0; x < indexList.size(); x++) {
                            DBQueries.cartList.remove(indexList.get(x).intValue());
                            DBQueries.cartItemModelList.remove(indexList.get(x).intValue());
                            DBQueries.cartItemModelList.remove(DBQueries.cartItemModelList.size() - 1);

                        }


                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                    loadingDialogue.dismiss();
                }
            });
        }


        changeOrAddNewAddressButton.setEnabled(false);
        continueBtn.setEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        orderId.setText("Order ID " + order_id);
        order_confirmation_layout.setVisibility(View.VISIBLE);
        continue_shopping_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void placeOrderDetails() {
        String userId = FirebaseAuth.getInstance().getUid();


        loadingDialogue.show();
        for (CartItemModel cartItemModel : cartItemModelList) {
            if (cartItemModel.getType() == cartItemModel.CART_ITEM) {


                final Map<String, Object> orderDetails = new HashMap<>();

                orderDetails.put("ORDER ID", order_id);

                orderDetails.put("Product Image", cartItemModel.getProductImage());
                orderDetails.put("Product Title", cartItemModel.getProductTitle());

                orderDetails.put("Product Id", cartItemModel.getProductID());
                orderDetails.put("User Id", userId);
                orderDetails.put("Product Quantity", cartItemModel.getProductQuantity());

                if (cartItemModel.getCuttingPrice() != null) {

                    orderDetails.put("Cutted Price", cartItemModel.getCuttingPrice());
                } else {
                    orderDetails.put("Cutted Price", "");

                }

                orderDetails.put("Product Price", cartItemModel.getProductPrice());
                if (cartItemModel.getSelectedCouponId() != null) {

                    orderDetails.put("Coupon Id", cartItemModel.getSelectedCouponId());
                } else {
                    orderDetails.put("Coupon Id", "");

                }

                if (cartItemModel.getDiscountedPrice() != null) {

                    orderDetails.put("Discounted price", cartItemModel.getDiscountedPrice());
                } else {
                    orderDetails.put("Discounted Price", "");

                }

                orderDetails.put("Ordered date", FieldValue.serverTimestamp());
                orderDetails.put("Packed date", FieldValue.serverTimestamp());
                orderDetails.put("Shipped date", FieldValue.serverTimestamp());
                orderDetails.put("delivered date", FieldValue.serverTimestamp());
                orderDetails.put("Cancelled date", FieldValue.serverTimestamp());
                orderDetails.put("Ordered status", "Ordered");


                orderDetails.put("Payment Method", paymentMethod);
                orderDetails.put("Address", fullAddress.getText());
                orderDetails.put("Fullname", fullName.getText());
                orderDetails.put("pincode", pinCode.getText());

                orderDetails.put("free coupons", cartItemModel.getFreeCoupons());
                orderDetails.put("Delivery price",  cartItemModelList.get(cartItemModelList.size() - 1).getDeliveryPrice());
                orderDetails.put("Cancellation requested", false);


                firebaseFirestore.collection("ORDERS").document(order_id).collection("OrderItems").document(cartItemModel.getProductID())
                        .set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {


                            Toast.makeText(DeliveryActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });


            } else {
                Map<String, Object> orderDetails = new HashMap<>();

                orderDetails.put("Total Items", cartItemModel.getTotalItems());
                orderDetails.put("Total Items Price", cartItemModel.getTotalItemPrice());
                orderDetails.put("Delivery Price", cartItemModel.getDeliveryPrice());
                orderDetails.put("Total Amount", cartItemModel.getTotalAmount());
                orderDetails.put("Saved Amount", cartItemModel.getSavedAmount());

                orderDetails.put("Payment Status", "Not paid");
                orderDetails.put("Order Status", "Cancelled");


                firebaseFirestore.collection("ORDERS").document(order_id)
                        .set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {


                            if (paymentMethod.equals("PAYTM")) {

                                // paytm code errors

                                paytm();
//


                                android.util.Log.d(TAG, "onComplete: sucess paytm");

                            } else {

                                cod();
                                android.util.Log.d(TAG, "onComplete: success cod");

                            }

                        } else {
                            Toast.makeText(DeliveryActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        }
        loadingDialogue.dismiss();
    }







    // Paytm Error






    private void paytm() {

        getQuantityIDs = false;
        paymentMethodDialogue.dismiss();
        loadingDialogue.show();

        if (ContextCompat.checkSelfPermission(DeliveryActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DeliveryActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }


        final String M_id = "RmLOny18647316489258";
        final String customer_id = FirebaseAuth.getInstance().getUid();
        String url = "https://b-first.000webhostapp.com/paytm/generateChecksum.php";
        final String callBack_url = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";

        RequestQueue requestQueue = Volley.newRequestQueue(DeliveryActivity.this);


        // Paytm SDK deprecated try to update it

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("CHECKSUMHASH")) {
                        String CHECKSUMHASH = jsonObject.getString("CHECKSUMHASH");

                        PaytmPGService paytmPGService = PaytmPGService.getStagingService("");


                        HashMap<String, String> paramMap = new HashMap<String, String>();
                        //these are mandatory parameters
                        paramMap.put("MID", M_id); //MID provided by paytm
                        paramMap.put("ORDER_ID", order_id);
                        paramMap.put("CUST_ID", customer_id);
                        paramMap.put("CHANNEL_ID", "WAP");
                        paramMap.put("TXN_AMOUNT", totalAmount.getText().toString().substring(3, totalAmount.getText().length() - 2));
                        paramMap.put("WEBSITE", "WEBSTAGING");
                        paramMap.put("CALLBACK_URL", callBack_url);
                        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                        paramMap.put("CHECKSUMHASH", CHECKSUMHASH);

                        PaytmOrder order = new PaytmOrder(paramMap);


                        paytmPGService.initialize(order, null);
                        paytmPGService.startPaymentTransaction(DeliveryActivity.this, true, true, new PaytmPaymentTransactionCallback() {
                            @Override
                            public void onTransactionResponse(Bundle inResponse) {
//                                        Toast.makeText(getApplicationContext(), "Payment Transaction Response"+inResponse, Toast.LENGTH_SHORT).show();

                                if (inResponse.getString("STATUS").equals("TXN_SUCCESS")) {

                                    Map<String, Object> updateStatus = new HashMap<>();
                                    updateStatus.put("Payment Status", "Paid");
                                    updateStatus.put("Order Status", "Ordered");
                                    firebaseFirestore.collection("ORDERS").document(order_id).update(updateStatus)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        Map<String, Object> userOrder = new HashMap<>();
                                                        userOrder.put("order_id", order_id);
                                                        userOrder.put("time",FieldValue.serverTimestamp());


                                                        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_ORDERS").document(order_id).set(userOrder)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            showConfirmationLayout();
                                                                        } else {
                                                                            Toast.makeText(DeliveryActivity.this, "Failed to update user order List", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });


                                                    } else {
                                                        Toast.makeText(DeliveryActivity.this, "ORDERED CANCELLED", Toast.LENGTH_LONG).show();

                                                    }
                                                }
                                            });

                                }

                            }

                            @Override
                            public void networkNotAvailable() {
                                Toast.makeText(getApplicationContext(), "Network Connection Error: Check Your Internet Connectivity", Toast.LENGTH_SHORT).show();


                            }

                            @Override
                            public void clientAuthenticationFailed(String inErrorMessage) {

                                Toast.makeText(getApplicationContext(), "Authentication Failed: Server Error" + inErrorMessage, Toast.LENGTH_SHORT).show();


                            }

                            @Override
                            public void someUIErrorOccurred(String inErrorMessage) {
                                Toast.makeText(getApplicationContext(), "UI Error" + inErrorMessage, Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                                Toast.makeText(getApplicationContext(), "Unable to load Web page" + inErrorMessage, Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onBackPressedCancelTransaction() {
                                Toast.makeText(getApplicationContext(), "Transaction Canceled", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                                Toast.makeText(getApplicationContext(), "Transaction Canceled" + inResponse.toString(), Toast.LENGTH_SHORT).show();

                            }
                        });


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loadingDialogue.dismiss();
                Toast.makeText(DeliveryActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                HashMap<String, String> paramMap = new HashMap<String, String>();
                //these are mandatory parameters
                paramMap.put("MID", M_id); //MID provided by paytm
                paramMap.put("ORDER_ID", order_id);
                paramMap.put("CUST_ID", customer_id);
                paramMap.put("CHANNEL_ID", "WAP");
                paramMap.put("TXN_AMOUNT", totalAmount.getText().toString().substring(3, totalAmount.getText().length() - 2));
                paramMap.put("WEBSITE", "WEBSTAGING");
                paramMap.put("CALLBACK_URL", callBack_url);
                paramMap.put("INDUSTRY_TYPE_ID", "Retail");
                Log.e("checksum ", "param " + paramMap.toString());

                return paramMap;


            }

        };

        requestQueue.add(stringRequest);

    }

    private void cod() {
        getQuantityIDs = false;
        paymentMethodDialogue.dismiss();

        Intent otpIntent = new Intent(DeliveryActivity.this, OTPverificationActivity.class);
        otpIntent.putExtra("mobileNo", mobileNo.substring(0, 10));
        otpIntent.putExtra("OrderID", order_id);

        startActivity(otpIntent);
    }


}
