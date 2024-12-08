package database;

public class Ad {

    private Integer id;
    private String name;
    private String subject;
    private Integer price;
    private String location;
    private String email;
    private String phone;
    private String arrival;
    private String date;
    private float rating;
    private Integer count;
    private float sum;

    public Ad(String subject, Integer id, String location, String phone, String arrival, Integer price, String date){
        this.subject = subject;
        this.id = id;
        this.location = location;
        this.phone = phone;
        this.arrival = arrival;
        this.price = price;
        this.date = date;
    }

    public Ad(int id, String name, String subject, Integer price, String location, String email, String phone, String arrival, String date) {
        this.id = id;
        this.name = name;
        this.subject = subject;
        this.price = price;
        this.location = location;
        this.email = email;
        this.phone = phone;
        this.arrival = arrival;
        this.date = date;
    }

    public Ad(int id, float rating, String name, String subject, Integer price, String location, String email, String phone, String arrival, String date) {
        this.id = id;
        this.rating = rating;
        this.name = name;
        this.subject = subject;
        this.price = price;
        this.location = location;
        this.email = email;
        this.phone = phone;
        this.arrival = arrival;
        this.date = date;
    }

    public Ad(int id, float rating, String name, String subject, Integer price, String location, String email, String phone, String arrival, String date, Integer count) {
        this.id = id;
        this.rating = rating;
        this.name = name;
        this.subject = subject;
        this.price = price;
        this.location = location;
        this.email = email;
        this.phone = phone;
        this.arrival = arrival;
        this.date = date;
        this.count = count;
        this.sum = rating;
    }

    public Integer getId() { return id; }

    public String getName() {
        return name;
    }

    public String getSubject() {
        return subject;
    }

    public Integer getPrice() {
        return price;
    }

    public String getLocation() {
        return location;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone(){
        return phone;
    }

    public CharSequence getArrival() {
        return arrival;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getRating() { return rating; }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }
}
