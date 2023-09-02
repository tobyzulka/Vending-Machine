package vending.machine;

import java.util.List;

public interface VendingMachine {
    public long selectItemAndGetPrice(Item item); 
    public void insertBill(Bill bill); 
    public List<Bill> refund(); 
    public Bucket<Item, List<Bill>> collectItemAndChange(); 
    public void reset();
}