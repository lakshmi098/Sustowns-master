package com.sustown.sustownsapp.Models;

public class TransportDetailsModel {
    String id;
    String user_id;
    String order_id;
    String product_order_id;
    String product_id;
    String quantity;
    String pr_price;
    String driver_name;
    String driver_number;
    String vehicle_number;
    String order_ranid;
    String pr_title;
    String total_price;
    String seller_name;
    String seller_number;
    String seller_address;
    String seller_zipcode;
    String display_name;
    String pay_phone;
    String pay_address1;
    String pay_zipcode;
    String pr_weight;
    String pr_packtype;
    String invoice_number;
    String order_date;

    public TransportDetailsModel(String id, String user_id, String order_id, String product_order_id, String product_id, String quantity, String pr_price, String driver_name, String driver_number, String vehicle_number, String order_ranid, String pr_title, String total_price, String order_date,
                                 String seller_name, String seller_number, String seller_address, String seller_zipcode, String display_name, String pay_phone, String pay_address1, String pay_zipcode, String pr_weight, String pr_packtype, String invoice_number) {
        this.id = id;
        this.user_id = user_id;
        this.order_id = order_id;
        this.product_order_id = product_order_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.pr_price = pr_price;
        this.driver_name = driver_name;
        this.driver_number = driver_number;
        this.vehicle_number = vehicle_number;
        this.order_ranid = order_ranid;
        this.pr_title = pr_title;
        this.total_price = total_price;
        this.seller_name = seller_name;
        this.seller_number = seller_number;
        this.seller_address = seller_address;
        this.seller_zipcode = seller_zipcode;
        this.display_name = display_name;
        this.pay_phone = pay_phone;
        this.pay_address1 = pay_address1;
        this.pay_zipcode = pay_zipcode;
        this.pr_weight = pr_weight;
        this.pr_packtype = pr_packtype;
        this.invoice_number = invoice_number;
        this.order_date = order_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getProduct_order_id() {
        return product_order_id;
    }

    public void setProduct_order_id(String product_order_id) {
        this.product_order_id = product_order_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPr_price() {
        return pr_price;
    }

    public void setPr_price(String pr_price) {
        this.pr_price = pr_price;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getDriver_number() {
        return driver_number;
    }

    public void setDriver_number(String driver_number) {
        this.driver_number = driver_number;
    }

    public String getVehicle_number() {
        return vehicle_number;
    }

    public void setVehicle_number(String vehicle_number) {
        this.vehicle_number = vehicle_number;
    }

    public String getOrder_ranid() {
        return order_ranid;
    }

    public void setOrder_ranid(String order_ranid) {
        this.order_ranid = order_ranid;
    }

    public String getPr_title() {
        return pr_title;
    }

    public void setPr_title(String pr_title) {
        this.pr_title = pr_title;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }


    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getSeller_number() {
        return seller_number;
    }

    public void setSeller_number(String seller_number) {
        this.seller_number = seller_number;
    }

    public String getSeller_address() {
        return seller_address;
    }

    public void setSeller_address(String seller_address) {
        this.seller_address = seller_address;
    }

    public String getSeller_zipcode() {
        return seller_zipcode;
    }

    public void setSeller_zipcode(String seller_zipcode) {
        this.seller_zipcode = seller_zipcode;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getPay_phone() {
        return pay_phone;
    }

    public void setPay_phone(String pay_phone) {
        this.pay_phone = pay_phone;
    }

    public String getPay_address1() {
        return pay_address1;
    }

    public void setPay_address1(String pay_address1) {
        this.pay_address1 = pay_address1;
    }

    public String getPay_zipcode() {
        return pay_zipcode;
    }

    public void setPay_zipcode(String pay_zipcode) {
        this.pay_zipcode = pay_zipcode;
    }

    public String getPr_weight() {
        return pr_weight;
    }

    public void setPr_weight(String pr_weight) {
        this.pr_weight = pr_weight;
    }

    public String getPr_packtype() {
        return pr_packtype;
    }

    public void setPr_packtype(String pr_packtype) {
        this.pr_packtype = pr_packtype;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

}
