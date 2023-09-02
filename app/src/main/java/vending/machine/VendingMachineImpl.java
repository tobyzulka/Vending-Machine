package vending.machine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VendingMachineImpl implements VendingMachine {
    private Inventory<Bill> cashInventory = new Inventory<Bill>();
    private Inventory<Item> itemInventory = new Inventory<Item>();  
    private long totalSales;
    private Item currentItem;
    private long currentBalance; 
   
    public VendingMachineImpl() {
        initialize();
    }
   
    private void initialize() {      
        for(Bill b : Bill.values()) {
            cashInventory.put(b, 5);
        }
       
        for(Item i : Item.values()) {
            itemInventory.put(i, 5);
        }
       
    }
   
   @Override
    public long selectItemAndGetPrice(Item item) {
        if(itemInventory.hasItem(item)) {
            currentItem = item;
            return currentItem.getPrice();
        }
        throw new SoldOutException("Habis, Silakan beli item lain");
    }

    @Override
    public void insertBill(Bill bill) {
        currentBalance = currentBalance + bill.getDenomination();
        cashInventory.add(bill);
    }

    @Override
    public Bucket<Item, List<Bill>> collectItemAndChange() {
        Item item = collectItem();
        totalSales = totalSales + currentItem.getPrice();
       
        List<Bill> change = collectChange();
       
        return new Bucket<Item, List<Bill>>(item, change);
    }
       
    private Item collectItem() throws NotSufficientChangeException, NotFullPaidException {
        if(isFullPaid()) {
            if(hasSufficientChange()) {
                itemInventory.deduct(currentItem);
                return currentItem;
            }           
            throw new NotSufficientChangeException("Perubahan Persediaan tidak mencukupi");
           
        }
        long remainingBalance = currentItem.getPrice() - currentBalance;
        throw new NotFullPaidException("Harga belum dibayar penuh, tersisa : ", remainingBalance);
    }
   
    private List<Bill> collectChange() {
        long changeAmount = currentBalance - currentItem.getPrice();
        List<Bill> change = getChange(changeAmount);
        updateCashInventory(change);
        currentBalance = 0;
        currentItem = null;
        return change;
    }
   
    @Override
    public List<Bill> refund() {
        List<Bill> refund = getChange(currentBalance);
        updateCashInventory(refund);
        currentBalance = 0;
        currentItem = null;
        return refund;
    }
   
   
    private boolean isFullPaid() {
        if(currentBalance >= currentItem.getPrice()) {
            return true;
        }
        return false;
    }

      
    private List<Bill> getChange(long amount) throws NotSufficientChangeException {
        List<Bill> changes = Collections.emptyList();
       
        if(amount > 0) {
            changes = new ArrayList<Bill>();
            long balance = amount;
            while(balance > 0) {
                if(balance >= Bill.NOCENG.getDenomination() 
                            && cashInventory.hasItem(Bill.NOCENG)) {
                    changes.add(Bill.NOCENG);
                    balance = balance - Bill.NOCENG.getDenomination();
                    continue;
                   
                } else if(balance >= Bill.GOCENG.getDenomination() 
                                 && cashInventory.hasItem(Bill.GOCENG)) {
                    changes.add(Bill.GOCENG);
                    balance = balance - Bill.GOCENG.getDenomination();
                    continue;
                   
                } else if(balance >= Bill.CEBAN.getDenomination() 
                                 && cashInventory.hasItem(Bill.CEBAN)) {
                    changes.add(Bill.CEBAN);
                    balance = balance - Bill.CEBAN.getDenomination();
                    continue;
                   
                } else if(balance >= Bill.NOBAN.getDenomination() 
                                 && cashInventory.hasItem(Bill.NOBAN)) {
                    changes.add(Bill.NOBAN);
                    balance = balance - Bill.NOBAN.getDenomination();
                    continue;
                   
                } else if(balance >= Bill.GOBAN.getDenomination() 
                                 && cashInventory.hasItem(Bill.GOBAN)) {
                    changes.add(Bill.GOBAN);
                    balance = balance - Bill.GOBAN.getDenomination();
                    continue;
                   
                } else {
                    throw new NotSufficientChangeException("Perubahan Tidak Memadai, Silakan coba produk lain");
                }
            }
        }
       
        return changes;
    }
   
    @Override
    public void reset() {
        cashInventory.clear();
        itemInventory.clear();
        totalSales = 0;
        currentItem = null;
        currentBalance = 0;
    } 
       
    public void printStats() {
        System.out.println("Total Penjualan : " + totalSales);
        System.out.println("Inventaris barang saat ini : " + itemInventory);
        System.out.println("Inventaris Kas Saat Ini : " + cashInventory);
    }   
   
  
    private boolean hasSufficientChange() {
        return hasSufficientChangeForAmount(currentBalance - currentItem.getPrice());
    }
   
    private boolean hasSufficientChangeForAmount(long amount) {
        boolean hasChange = true;
        try{
            getChange(amount);
        } catch(NotSufficientChangeException e) {
            return hasChange = false;
        }
       
        return hasChange;
    }

    private void updateCashInventory(List change) {
        for(Bill b : change){
            cashInventory.deduct(b);
        }
    }
   
    public long getTotalSales() {
        return totalSales;
    }
}