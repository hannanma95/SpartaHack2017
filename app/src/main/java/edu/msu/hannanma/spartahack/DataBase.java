package edu.msu.hannanma.spartahack;

/**
 * Created by Matt on 1/21/2017.
 */


public class DataBase {
        public String Name_;
        public int Frequency_;
        public float Price_;

    public DataBase()
    {
    }
    public DataBase(String Name,int Frequency,float Price)
    {
        this.Name_=Name;
        this.Frequency_=Frequency;
        this.Price_=Price;
    }
    public void setName(String Name) {
        this.Name_ =Name;
    }
    public void setFrequency(int Frequency) {
        this.Frequency_= Frequency;
    }

    public void setPrice(float Price) {
        this.Price_ = Price;
    }

    public String getName_(){
        return Name_;
    }

    public int  getFrequency_(){
        return Frequency_;
    }

    public float getPrice_(){
        return Price_;
    }


}

