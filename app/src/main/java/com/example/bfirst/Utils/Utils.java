package com.example.bfirst.Utils;

import android.app.Dialog;
import android.view.ViewGroup;

import com.example.bfirst.R;

public class Utils {
    // Order Status
    public static String ORDERED = "Ordered";
    public static String PACKED = "Packed";
    public static String SHIPPED = "Shipped";
    public static String DELIVERED = "Delivered";
    public static String CANCELLED = "Cancelled";

    public static final String CATEGORIES = "CATEGORIES"; // collection
    public static final String TOP_DEALS = "TOP_DEALS"; // collection
    public static final String VIEW_TYPE = "view_type";  // document field
    public static final String CANCELLEDORDERS = "CANCELLED_ORDERS"; // collection
    public static final String ORDERS = "ORDERS"; // collection
    public static final String PRODUCTS = "PRODUCTS"; // collection
    public static final String USERS = "USERS"; // collection

    public static final String INDEX = "index"; // document field
    public static final String TIME = "time"; // document field

    public static final String USER_DATA = "USER_DATA"; // collection
    public static final String USER_ORDERS = "USER_ORDERS";
    public static final String USER_REWARDS = "USER_REWARDS";
    public static final String MY_ADDRESSES = "MY_ADDRESSES"; // document
    public static final String MY_CART = "MY_CART"; // document
    public static final String MY_NOTIFICATIONS = "MY_NOTIFICATIONS"; // document
    public static final String MY_RATINGS = "MY_RATINGS";// document
    public static final String MY_WISHLIST = "MY_WISHLIST";// document

    public static final String ORDERITEMS = "OrderItems"; // collection
    public static final String ORDER_ID_SMALL = "order_id"; // document field


    public static final String ADDRESS = "Address"; // document field
    public static final String CANCELLATION_REQUESTED = "Cancellation requested"; // document field
    public static final String CANCELLATION_DATE = "Cancelled date"; // document field
    public static final String COUPON_ID = "Coupon Id"; // document field
    public static final String CUTTED_PRICE_SPACE_SEPRATED = "Cutted Price"; // document field
    public static final String DELIVERY_PRICE = "Delivery price"; // document field
    public static final String DISCOUNTED_PRICE = "Discounted Price"; // document field
    public static final String FULLNAME = "Fullname"; // document field
    public static final String ORDER_ID_CAPITAL = "ORDER ID"; // document field
    public static final String ORDERED_DATE = "Ordered date"; // document field`
    public static final String ORDERED_STATUS = "Ordered status"; // document field
    public static final String PACKED_DATE = "Packed date"; // document field
    public static final String PAYMENT_METHOD = "Payment Method"; // document field
    public static final String PRODUCT_ID = "Product Id"; // document field
    public static final String PRODUCT_IMAGE = "Product Image"; // document field
    public static final String PRODUCT_PRICE_SPACE_SEPRATED = "Product Price"; // document field
    public static final String PRODUCT_QUANTIT = "Product Quantity"; // document field
    public static final String PRODUCT_TITLE_ = "Product Title"; // document field
    public static final String SHIPPED_DATE = "Shipped date"; // document field
    public static final String USER_ID = "User Id"; // document field
    public static final String DELIVERED_DATE = "delivered date"; // document field
    public static final String FREE_COUPONS_SPACE_SEPRATED = "free coupons"; // document field
    public static final String PINCODE = "pincode"; // document field


    public static final String LAST_SEEN = "Last seen"; // document field

    public static final String ALREADY_USED = "already_used"; // document field
    public static final String BODY = "body"; // document field
    public static final String LOWER_LIMIT = "lower_limit"; // document field
    public static final String PERCENTAGE = "percentage"; // document field
    public static final String TYPE = "type"; // document field
    public static final String UPPER_LIMIT = "upper_limit"; // document field
    public static final String VALIDITY = "validity"; // document field

    public static final String DISCOUNT = "Discount"; // document value
    public static final String FLAT_RS_OFF = "Flat Rs. * OFF"; // document value
    public static final String AMOUNT = "amount"; // document field

    // Notifications
    public static final String LIST_SIZE = "list_size"; // document field
    public static final String BODY_ = "Body_"; // document field
    public static final String IMAGE_ = "Image_"; // document field
    public static final String READED_ = "Readed_"; // document field

    // Addresses
    public static final String ALTERNATE_MOBILE_NO_ = "alternate_mobile_no_1"; // document field
    public static final String CITY_ = "city_"; // document field
    public static final String FLAT_NO_ = "flat_no_"; // document field
    public static final String LANDMARK_ = "landmark_"; // document field
    public static final String LOCALITY_ = "locality_"; // document field
    public static final String MOBILE_NO_ = "mobile_no_"; // document field
    public static final String NAME_ = "name_"; // document field
    public static final String PIN_CODE_ = "pin_code_"; // document field
    public static final String SELECTED_ = "selected_"; // document field
    public static final String STATE_ = "state_"; // document field

    // WishList
    public static final String PRODUCT_ID_ = "product_ID_"; // document field

    // Product Data
    public static final String QUANTITY = "QUANTITY"; // collection

    public static final String _STAR = "_star"; // document field
    public static final String COD = "COD"; // document field
    public static final String ADDED_ON = "added_on"; // document field
    public static final String AVERAGE_RATING = "average_rating"; // document field
    public static final String CUTTED_PRICE = "cutted_price"; // document field
    public static final String FREE_COUPON_BODY = "free_coupon_body"; // document field
    public static final String FREE_COUPON_TITLE = "free_coupon_title"; // document field
    public static final String FREE_COUPONS = "free_coupons"; // document field
    public static final String MAX_QUANTITY = "max_quantity"; // document field
    public static final String NO_OF_PRODUCTS_IMAGES = "no_of_products_images"; // document field
    public static final String OFFERS_APPLIED = "offers_applied"; // document field
    public static final String PRODUCT_DESCRIPTION = "product_description"; // document field
    public static final String PRODUCT_IMAGE_ = "product_image_"; // document field
    public static final String PRODUCT_OTHER_DETAILS = "product_other_details"; // document field
    public static final String PRODUCT_PRICE = "product_price"; // document field
    public static final String PRODUCT_TITLE = "product_title"; // document field
    public static final String SPEC_TITLE_ = "spec_title_"; // document field
    public static final String _FIELD_ = "_field_"; // document field
    public static final String _NAME = "_name"; // document field
    public static final String _VALUE = "_value"; // document field
    public static final String _TOTAL_FIELDS = "_total_fields"; // document field
    public static final String STOCK_QUANTITY = "stock_quantity"; // document field
    public static final String TAGS = "tags"; // document field
    public static final String TOTAL_RATINGS = "total_ratings"; // document field
    public static final String TOTAL_SPEC_TITLES = "total_spec_titles"; // document field
    public static final String USE_TAB_LAYOUT = "use_tab_layout"; // document field

    // paytm
    public static final String MERCHANT_ID = "RmLOny18647316489258";
    public static final String CLIENT_ID = "c87be96618e247b592af0e436b63f2bb";

    // Privacy policy
    public static final String PRIVACY_POLICY = "https://pages.flycricket.io/b-first/privacy.html";
    // terms and conditions
    public static final String TERMS_AND_CONDITIONS = "https://pages.flycricket.io/b-first/terms.html";

}
