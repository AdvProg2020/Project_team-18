package Server;

public enum MessageType {
    DOES_USERNAME_EXIST,LOGIN,ERROR,SELLER_ADD_BALANCE,DOES_SELLER_HAVE_LOG,SELLER_SELL_HISTORY,REMOVE_PRODUCT_FROM_OFF,
    ADD_PRODUCT_TO_OFF,DOES_SELLER_HAVE_OFF,DOES_SELLER_HAVE_PRODUCT,EDIT_OFF,EDIT_PRODUCT,ADD_PRODUCT,REMOVE_PRODUCT_SELLER,ADD_BALANCE, GET_CART
    ,INCREASE_PRODUCT, DECREASE_PRODUCT,GET_CART_PRICE,ADD_RATE, GET_ALL_BUY_LOGS,DOES_CUSTOMER_HAVE_BUY_LOG,ADD_OFF,DOES_CUSTOMER_HAVE_DISCOUNT,
    UPDATE_DISCOUNT_USAGE, GET_DISCOUNT_PERCENTAGE, DOES_CUSTOMER_HAVE_MONEY,CHECK_DISCOUNT_VALIDITY,PERFORM_PAYMENT,
    CALCULATE_TOTAL_PRICE_WITH_DISCOUNT,CALCULATE_TOTAL_PRICE_WITHOUT_DISCOUNT,VIEW_ALL_PRODUCTS,PERFORM_FILTER,PERFORM_SORT,
    DISABLE_FILTER,DISABLE_SORT,DEFAULT_SORT,VIEW_ALL_CATEGORIES,ADD_COMMENT, GET_ALL_PRODUCTS, GET_ALL_PRODUCTS_IN_SALE,
    GET_PRODUCT_BY_ID
}
