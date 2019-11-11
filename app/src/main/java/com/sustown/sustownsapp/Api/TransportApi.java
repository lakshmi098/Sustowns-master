package com.sustown.sustownsapp.Api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sustown.sustownsapp.Models.TransportOrdersModelObj;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface TransportApi {

    @GET(DZ_URL.TRANSPORT_REGISTRATION)
    Call<JsonElement> transportVendorRegister(@Query("username") String username, @Query("phone") String phone, @Query("email") String email, @Query("role") String role,
                                              @Query("fullname") String fullname, @Query("businessname") String businessname, @Query("town") String town, @Query("password") String password, @Query("country") String country,
                                              @Query("state") String state, @Query("city") String city, @Query("locarea") String locarea);

    @GET(DZ_URL.GET_TRANS_SERVICES)
    Call<JsonElement> getTransportServices(@Query("userid") String userid);

    @GET(DZ_URL.ADD_TRANSPORT)
    Call<JsonElement> addTransportService(@Query("invoice_id") String invoice_id, @Query("order_id") String order_id, @Query("pickupdatefrom") String pickupdatefrom, @Query("pickupdateto") String pickupdateto);

    @GET(DZ_URL.GET_KMS_BASED_PINCODES)
    Call<JsonElement> getKmsBasePincodes(@Query("pickup") String pickup, @Query("drop") String drop, @Query("kilometerprice") String kilometerprice);

    /*(Old Transport received orders)
    @GET
    Call<JsonObject> getTransportReceivedOrders(@Url String url);
*/
    // transport received orders latest one
    @POST(DZ_URL.GET_TRANSPORT_RECEIVED_ORDERS)
    Call<JsonObject> getTransportReceivedOrders(@Body TransportOrdersModelObj transportOrdersModel1);

    @GET
    Call<JsonObject> getBuyerTransportDetails(@Url String url);

    @GET(DZ_URL.GET_TRANSPORT_TYPE)
    Call<JsonElement> getTransportType();

    @GET(DZ_URL.GET_VEHICLE_TYPE)
    Call<JsonElement> getVehicleType(@Query(("trans")) String trans);

    @GET(DZ_URL.GET_TRANSPORT_BOOK_SERVICE)
    Call<JsonElement> getTransportBookService(@Query(("invoice_id")) String invoice_id, @Query(("order_id")) String order_id, @Query(("uid")) String uid, @Query(("trans_userid")) String trans_userid, @Query(("service_id")) String service_id,
                                              @Query(("charge_perkm")) String charge_perkm, @Query(("mincharge_km")) String mincharge_km, @Query(("totalpricetransport")) String totalpricetransport);


    @GET(DZ_URL.CANCEL_BOOKING)
    Call<JsonElement> cancelBooking(@Query(("buyer_uid")) String buyer_uid, @Query("orderRandomId") String orderRandomId, @Query("serviceId") String serviceId);

    @GET(DZ_URL.ADD_SERVICE)
    Call<JsonElement> addService(@Query("service") String service, @Query("prd_userid") String prd_userid, @Query("trans") String trans, @Query("vehicle") String vehicle,
                                 @Query("load") String load, @Query("taxfield") String taxfield, @Query("radi_perkm") String radi_perkm, @Query("discountfild") String discountfild,
                                 @Part MultipartBody.Part product_img, @Query("action") String action, @Query("egg") String egg, @Query("prd_bussid") String prd_bussid,
                                 @Query("service_pincode") String service_pincode, @Query("legal_address") String legal_address, @Query("cname") String cname,
                                 @Query("cunit") String cunit, @Query("ccost") String ccost, @Query("crmincharge") String crmincharge, @Query("trans_ser_latitude") String trans_ser_latitude, @Query("trans_ser_langitude") String trans_ser_langitude,
                                 @Query("sourcelocation") String sourcelocation, @Query("trans_ser_org_latitude") String trans_ser_org_latitude, @Query("trans_ser_org_langitude") String trans_ser_org_langitude,
                                 @Query("trans_ser_des_langitude") String trans_ser_des_langitude, @Query("point_destin_latitude") String point_destin_latitude, @Query("deslocation") String deslocation, @Query("cmincharge") String cmincharge, @Query("pincode") String pincode,
                                 @Query("pin") String pin);

    @GET(DZ_URL.ADD_NEW_ADDRESS)
    Call<JsonElement> addNewAddress(@Query("userid") String userid, @Query("displayname") String displayname, @Query("companyname") String companyname, @Query("fname") String fname,
                                    @Query("lname") String lname, @Query("email") String email, @Query("address1") String address1,
                                    @Query("address2") String address2, @Query("zipcode") String zipcode, @Query("country") String country, @Query("state") String state,
                                    @Query("city") String city, @Query("phone") String phone, @Query("fax") String fax,
                                    @Query("drop_latitude") String drop_latitude, @Query("drop_logitude") String drop_logitude);

    @GET(DZ_URL.EXISTING_ADDRESS)
    Call<JsonElement> getExistingAddress(@Query("userid") String userid);

    @GET(DZ_URL.SENT_ADDRESS)
    Call<JsonElement> sentAddress(@Query("userid") String userid, @Query("pr_userid") String pr_userid, @Query("pr_id") String pr_id,
                                  @Query("pr_zipcode") String pr_zipcode, @Query("pr_latitude") String pr_latitude, @Query("pr_longitude") String pr_longitude,
                                  @Query("addres_id") String addres_id);

    @GET(DZ_URL.GET_CATEGORIES_LIST)
    Call<JsonElement> getCategoriesList();

}
