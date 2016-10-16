package pk.nz.pinoyklasiks.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class represent sub order
 *
 * @author Mikhail PASTUSHKOV
 * @author Melchor RELATADO
 */

public class SubOrder implements Serializable {

    private int order_id;

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
                    totalPrice += p.getProduct_price() * mapProducts.get(p);
                }
        return totalPrice;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }
}
