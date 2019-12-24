package com.sustown.sustownsapp.Api;

public interface DZ_URL {

    String BASE_URL = "https://www.sustowns.com/";
   // String BASE_URL = "https://www.dev.sustowns.com/";

    String LOGIN = "Sustownsservice/login/";
    String HOME_PRODUCTS = "Sustownsservice/home";
    String CART_COUNT = "Sustownsservice/cartcount/";
    String VENDOR_SIGNUP = "transportservices/registrationforall";
    String STORE_POULTRY = "Sustownsservice/store";
    String PRODUCT_DETAILS = "Sustownsservice/viewproduct";
    String NEWS = "Sustownsservice/news";
    String GET_CART_LIST = "Sustownsservice/viewcart";
    String REMOVE_CART = "Sustownsservice/remove_cartforsingle";
    String GET_SHIPPING_CHARGE = "Sustownsservice/get_kilometers_info/";
    String CLEAR_CART = "Sustownsservice/remove_cartfull";
    String ADD_PRODUCT = "Sustownsservice/addproduct";
    String GET_CATEGORIES = "Sustownsservice/category";
    String CAREER_LIST = "Sustownsservice/careerlist";
    String CONTACT_US = "Sustownsservice/homecontact";
    String VIDEOS = "Sustownsservice/videos";
    String MY_PRODUCTS = "Storemanagementser/myproducts";
    String STORE_EDIT_PRODUCT = "Storemanagementser/edittoproduct";
    String REMOVE_PRODUCT = "Storemanagementser/deleteproducts";
    String COPY_PRODUCT = "Storemanagementser/copytoproduct";
    String GET_BUSINESS_PROFILE = "Businessprofileservice/businessprofile";
    String EDIT_BUSINESS_PROFILE = "Businessprofileservice/businessprofilemod";
    String BUSINESS_CATEGORY = "Businessprofileservice/businesscategorymod";
    String BUSINESS_GALLERY = "Businessprofileservice/gallery";
    String BUSINESS_BADGE = "Businessprofileservice/businessbadge";
    String BUSINESS_CONTRACT_REVIEWS = "Businessprofileservice/ reviewbus";
    String BUSINESS_STORE_REVIEWS = "Businessprofileservice/ proreviewbus";
    String BIDCONTRACTS_OPEN = "Bidcontractservice/bidopen";
    String OPEN_QUOTE = "Bidcontractservice/bidopenquote";
    String BID_CONTRACTS_QUOTED = "Bidcontractservice/bidquoted";
    String QUOTE_MY_QUOTE = "Bidcontractservice/bidquotedquotedet";
    String STORE_MY_ORDER_SER = "Postcontractservice/myorderser";
    String STORE_RECEIVED_ORDERS = "Postcontractservice/mypurchases";
    String CONFIRM_ORDERS = "Postcontractservice/receivedconfirm";
    String CANCEL_ORDER = "Postcontractservice/receivedcancel";
    String STORE_ORDER_DETAILS = "Postcontractservice/orderdetailser";
    String MY_PRODUCT_CONTRACTS = "Postcontractservice/myproductcontract";
    String GET_COUNTRY = "Transportservices/countrylist";
    String GET_STATES = "Transportservices/subdivisionlist/?";
    String GET_CITIES = "Transportservices/cities/";
    String GET_CURRENCY = "Accountservice/currencyservice";
    String STORE_SENT_OFFERS = "Postcontractservice/madeofferapp";
    String STORE_RECEIVED_OFFERS = "Postcontractservice/makereceive";
    String ACCEPT_OFFER = "Postcontractservice/acceptmakeoffer";
    String SUBMIT_MAKE_OFFER = "Postcontractservice/addmackoffer";
    String DELETE_OFFER = "Postcontractservice/deletemakeoffer";
    String ADD_REVIEW = "sustownsservice/productreviewadd";
    String SEARCH_STORE = "Mainsearch/searchser";
    String PAY_BY_BANK_PLACEORDER = "Shopping/paybybankapp/";
    String VENDOR_PROFILE = "sustownsservice/service_view";
    String VENDOR_CATEGORY = "sustownsservice/service_viewcat";
    String VENDOR_RATING_REVIEW = "sustownsservice/service_viewreview";
    String VENDOR_OUR_PRODUCTS = "sustownsservice/service_viewproducts";
    String GET_SHIPPING_ADDRESS = "Checkoutservice/checkoutview";
    String GET_PAYMENT_ORDER = "shopping/get_order_info_ap";
    String SUBMIT_ADD_PAYMENT = "shopping/paybybank_ap";
    String SUBMIT_CONTRACT_REVIEW = "Businessprofileservice/reviewbus";
    String GET_MARKET_LIST = "market_prices/search_market_prices";
    String GET_BANK_DETAILS = "shopping/paybank_bankinfo_ap";
    String ADD_NEW_ADDRESS = "Sustownsservice/addnew_address";
    String EXISTING_ADDRESS = "Sustownsservice/existing_address";
    String SENT_ADDRESS = "Sustownsservice/sentexit_address";
    // contracts
    String BID_CONTRACTS_COMPLETE = "Bidcontractservice/bidcomplete";
    String BID_CONTRACTS_ADD_DOCUMENT = "Bidcontractservice/bidapprovequote";
    String BID_CONTRACTS_APPROVE = "Bidcontractservice/bidapprove";
    String BID_CONFIRM_APPROVE_CONTTRACT = "Bidcontractservice/approveconfirmcontract";
    String BID_CONTRACTS_COMPLETE_QUOTE_DETAILS = "Bidcontractservice/myquotecompletebid";

    String CONTRACTS_PURCHASES = "Postcontractservice/mycontractpurchases";
    String CONTRACTS_ORDERS = "Postcontractservice/mycontractorders";
    String CONTRACT_ORDER_INVOICE = "Postcontractservice/mycontractjobinvoice";
  //  String ADD_PROD_CONTRACT_REQUEST = "Postcontractservice/addproductrequest";
    String ADD_PROD_CONTRACT_REQUEST ="Postcontractservice/addproductrequest/";
    String EDIT_POULTRY_PRODUCT_CONTRACT = "Postcontractservice/poultry_edit_contract";
    String APPROVE_QUOTE_RECEIVED_CONTRACT = "Postcontractservice/quoteapprovestat";
    String GET_RECEIVED_CONTRACTS = "Postcontractservice/Quoteapp";
    String CONFIRM_QUOTE_RECEIVED_CONTRACT = "Postcontractservice/completequotestatus";
    String MAKE_PAYMENT_BANK_APPROVE = "Postcontractservice/quoteapprove/";
    String TRANSPORT_REGISTRATION = "transportservices/addtransportvendor";
    String GET_TRANS_SERVICES = "transportservices/myservices";
    String ADD_TRANSPORT = "Transportservices/get_transport_req";
    String ADD_TRANSPORT_PROD_DETAILS = "Freight/productdetails_app/";
    String GET_CONTRACT_TRANSPORT_DETAILS = "Postcontractservice/get_contractproduct";
    String GET_CONTRACT_REQUEST_SERVICES = "Postcontractservice/get_transport_req";
    String GET_TRANSPORT_TYPE = "Transportservices/transtypelist/";
    String GET_VEHICLE_TYPE = "Transportservices/vehicletypelist/";
    String POST_TRANSPORT_VENDOR_CONFIRM = "freight/transportVendorConfirmOrder";
    String POST_TRANSPORT_CONTRACT_VENDOR_CONFIRM = "Postcontractservice/transportVendorConfirmOrder";
  //  String GET_TRANSPORT_DETAILS_LIST = "freight/transportBuyerTransportList/";
    String TRANSPORT_REQUEST_QUOTE = "Transportservices/add_transdetails_req";
  //  String CANCEL_BOOKING = "freight/transportcancel_byvendor";
    String ADD_SERVICE = "transportservices/addservice";
    String GET_CART_LIST_SERVER = "Sustownsservice/viewcart/?";
    String GET_VENDOR_SERVICES_ADD_PRODUCT = "Sustownsservice/getvendor_ser";

    String GET_PAYU_HASH = "checkoutservice/checkoutser/?";

    // CUSTOMIZATION
    String GET_CUSTOMIZATION_LIST = "customizationsserv/myservices";
    // Live
    String GET_FILTER_CONTINENTS = "Sustownsservice/getcontinents";
    String GET_FILTER_COUNTRIES = "Sustownsservice/getcountries";
    String GET_FILTER_CITIES = "Sustownsservice/getstates";
    String GET_FILTER_CATEGORIES = "Sustownsservice/getproductcategories";
    String GET_FILTER_PRODUCTLIST = "Sustownsservice/productslist";
    String ADD_TO_CART = "Sustownsservice/AddtoCart";
    String GET_CATEGORIES_LIST = "Sustownsservice/get_categeory";
    String GET_TRANSPORT_RECEIVED_ORDERS = "Sustownsservice/get_received_orders";
    String GET_TRANSPORT_ORDERDETAILS_LIST = "Transportservices/productdetails/";
    String GET_KMS_BASED_PINCODES = "Transportservices/sendquote/";
    String GET_TRANSPORT_CONTRACT_RECEIVEDORDERS = "Postcontractservice/get_received_orders";
    String GET_TRANSPORT_Contract_ORDERDETAILS_LIST = "Postcontractservice/productdetails/";
    String GET_CONTRACT_KMS_BASED_PINCODES = "Postcontractservice/sendquote/";
    String GET_CONTRACT_LOGISTICS_ORDERS_LIST = "Postcontractservice/contracttransportorders/";

    //Move To Live API'S

    // 1. add_transdetails_req IN Add Transport

}

