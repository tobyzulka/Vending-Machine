package vending.machine;

public enum Bill {
    NOCENG(2000), GOCENG(5000), CEBAN(10000), 
    NOBAN(20000), GOBAN(50000); 

    private int denomination; 

    private Bill(int denomination) { 
        this.denomination = denomination; 
    } 

    public int getDenomination() { 
        return denomination; 
    }

}