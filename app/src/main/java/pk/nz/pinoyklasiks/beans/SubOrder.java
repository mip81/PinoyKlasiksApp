package pk.nz.pinoyklasiks.beans;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class represent sub order
 *
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */

public class SubOrder implements Serializable {

    private int orderId;

    // Product and quantity
    private Map<AbstractProduct, Integer> mapProducts = new LinkedHashMap<>();
    // Total price
    private double totalPrice;

    public Map<AbstractProduct, Integer> getMapProducts() {
        return mapProducts;
    }

    public void setMapProducts(Map<AbstractProduct, Integer> mapProducts) {
        this.mapProducts = mapProducts;
    }

    //count the total price
    public double getTotalPrice() {
        totalPrice = 0;

                for(AbstractProduct p : mapProducts.keySet()){
                    totalPrice += p.getProductPrice() * mapProducts.get(p);
                }
        return totalPrice;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int order_id) {
        this.orderId = order_id;
    }
}
