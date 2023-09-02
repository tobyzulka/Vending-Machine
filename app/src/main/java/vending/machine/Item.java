package vending.machine;

public enum Item {
    BISKUIT("biskuit", 6000), CHIPS("Chips", 8000), OREO("Oreo", 10000), 
    TANGO("Tango", 12000), COKELAT("Cokelat", 15000);
   
    private String name;
    private int price;
   
    private Item(String name, int price){
        this.name = name;
        this.price = price;
    }
   
    public String getName(){
        return name;
    }
   
    public long getPrice(){
        return price;
    }
}
