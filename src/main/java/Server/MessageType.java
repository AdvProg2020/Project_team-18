package Server;

public enum MessageType {
    DOES_USERNAME_EXIST,LOGIN,ERROR,SELLER_ADD_BALANCE,DOES_SELLER_HAVE_LOG,SELLER_SELL_HISTORY,REMOVE_PRODUCT_FROM_OFF,
    ADD_PRODUCT_TO_OFF,DOES_SELLER_HAVE_OFF,DOES_SELLER_HAVE_PRODUCT,EDIT_OFF,EDIT_PRODUCT,ADD_PRODUCT,REMOVE_PRODUCT_SELLER,ADD_BALANCE, GET_CART
    ,INCREASE_PRODUCT, DECREASE_PRODUCT,GET_CART_PRICE,ADD_RATE, GET_ALL_BUY_LOGS,DOES_CUSTOMER_HAVE_BUY_LOG,ADD_OFF,DOES_CUSTOMER_HAVE_DISCOUNT,
    UPDATE_DISCOUNT_USAGE, GET_DISCOUNT_PERCENTAGE, DOES_CUSTOMER_HAVE_MONEY,CHECK_DISCOUNT_VALIDITY,PERFORM_PAYMENT,
    CALCULATE_TOTAL_PRICE_WITH_DISCOUNT,CALCULATE_TOTAL_PRICE_WITHOUT_DISCOUNT,VIEW_ALL_PRODUCTS,PERFORM_FILTER,PERFORM_SORT,
    DISABLE_FILTER,DISABLE_SORT,DEFAULT_SORT,VIEW_ALL_CATEGORIES,ADD_COMMENT, GET_ALL_PRODUCTS, GET_ALL_PRODUCTS_IN_SALE,
    GET_PRODUCT_BY_ID,GET_SELL_LOG_BY_CODE,GET_BUY_LOG_BY_CODE,REGISTER,DOES_ANY_ADMIN_EXIST,EDIT_FIELD,GET_ALL_USERS,DELETE_USER,
    REMOVE_PRODUCT,ADD_CATEGORY,VIEW_ALL_DISCOUNT_CODES,VIEW_ALL_REQUESTS,VIEW_DISCOUNT_CODE,ADD_CUSTOMER_TO_DISCOUNT,
    REMOVE_CUSTOMER_FROM_DISCOUNT,EDIT_DISCOUNT_FIELD,CREATE_DISCOUNT_CODE,REMOVE_DISCOUNT_CODE,REMOVE_CATEGORY,ACCEPT_REQUEST,
    DECLINE_REQUEST,EDIT_CATEGORY_BY_NAME,EDIT_CATEGORY_BY_PROPERTIES,VIEW_CATEGORY,CHAT_MESSAGE,TERMINATE ,CREATE_BANK_ACCOUNT,
    VIEW_ALL_BUY_LOGS,SEND_PURCHASE,CHARGE_WALLET,ADD_AUCTION,VIEW_ALL_AUCTIONS,BIDDING,WITHDRAW_WALLET,PERFORM_PAYMENT_WiTH_BANK_ACCOUNT,
    GET_PAYED_FILE_PRODUCTS,GET_SOLD_FILE_PRODUCTS,LOGOUT,GET_SHOP_BALANCE,SET_WAGE,SET_MIN_BALANCE
}
